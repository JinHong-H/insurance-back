package cn.wghtstudio.insurance.service.impl;

import cn.wghtstudio.insurance.dao.entity.Order;
import cn.wghtstudio.insurance.dao.repository.OrderRepository;
import cn.wghtstudio.insurance.service.DownloadService;
import cn.wghtstudio.insurance.util.LicensePlateWhenNewFactory;
import lombok.Builder;
import lombok.Data;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class DownloadServiceImpl implements DownloadService {
    @Resource
    OrderRepository orderRepository;

    @Data
    @Builder
    static class CompressItem {
        private String name;
        private String url;
    }

    private String getFix(String url) {
        final String[] names = url.split("\\.");
        if (names.length > 0) {
            return names[names.length - 1];
        } else {
            return "jpg";
        }
    }

    private Map<String, List<CompressItem>> getFolder(List<Order> orders) {
        Map<String, List<CompressItem>> folder = new HashMap<>();

        orders.forEach((item) -> {
            String identify;
            String plate;

            if (item.getIdCard() != null) {
                identify = item.getIdCard().getName();
            } else if (item.getBusinessLicense() != null) {
                identify = item.getBusinessLicense().getName();
            } else {
                return;
            }

            if (item.getDrivingLicense() != null) {
                plate = item.getDrivingLicense().getPlateNumber();
            } else if (item.getCertificate() != null) {
                plate = LicensePlateWhenNewFactory.getLicensePlateWhenNew(item.getCertificate().getEngine());
            } else {
                return;
            }


            folder.computeIfAbsent(identify, k -> new ArrayList<>());

            if (item.getIdCard() != null) {
                List<CompressItem> compressItems = folder.get(identify);
                compressItems.add(CompressItem.builder().
                        name(plate + "-身份证." + getFix(item.getIdCard().getUrl())).
                        url(item.getIdCard().getUrl()).
                        build());
            } else if (item.getBusinessLicense() != null) {
                List<CompressItem> compressItems = folder.get(identify);
                compressItems.add(CompressItem.builder().
                        name(plate + "-印业执照." + getFix(item.getBusinessLicense().getUrl())).
                        url(item.getBusinessLicense().getUrl()).
                        build());
            }

            if (item.getDrivingLicense() != null) {
                List<CompressItem> compressItems = folder.get(identify);
                compressItems.add(CompressItem.builder().
                        name(plate + "-驾驶证." + getFix(item.getDrivingLicense().getUrl())).
                        url(item.getDrivingLicense().getUrl()).
                        build());
            } else if (item.getCertificate() != null) {
                List<CompressItem> compressItems = folder.get(identify);
                compressItems.add(CompressItem.builder().
                        name(plate + "-合格证." + getFix(item.getCertificate().getUrl())).
                        url(item.getCertificate().getUrl()).
                        build());
            }
        });

        return folder;
    }

    @Override
    public void getEvidenceUrlService(HttpServletResponse response, List<Integer> ids) throws IOException, InterruptedException {
        List<Order> orders = orderRepository.getOrderByUser(Map.of("ids", ids));
        Map<String, List<CompressItem>> folder = getFolder(orders);

        int count = 0;
        for (Map.Entry<String, List<CompressItem>> node : folder.entrySet()) {
            count += node.getValue().size();
        }

        final Lock lock = new ReentrantLock();
        final CountDownLatch countDownLatch = new CountDownLatch(count);
        final OkHttpClient client = new OkHttpClient();

        response.setContentType("application/zip");
        response.setHeader("Content-disposition", "attachment;filename=export.zip");
        response.flushBuffer();

        OutputStream outputStream = response.getOutputStream();

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
            for (Map.Entry<String, List<CompressItem>> node : folder.entrySet()) {
                for (CompressItem compressItem : node.getValue()) {
                    final Request request = new Request.Builder().url(compressItem.getUrl()).build();
                    final Call call = client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            lock.lock();
                            try {
                                final byte[] body = Objects.requireNonNull(response.body()).bytes();
                                zipOutputStream.putNextEntry(new ZipEntry(node.getKey() + "/" + compressItem.getName()));
                                zipOutputStream.write(body);
                                zipOutputStream.closeEntry();
                                countDownLatch.countDown();
                            } finally {
                                lock.unlock();
                            }
                        }
                    });
                }
            }
            countDownLatch.await();
        }
    }
}
