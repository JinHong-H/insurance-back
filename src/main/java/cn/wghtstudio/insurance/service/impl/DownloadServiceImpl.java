package cn.wghtstudio.insurance.service.impl;

import cn.wghtstudio.insurance.dao.entity.Order;
import cn.wghtstudio.insurance.dao.entity.OtherFile;
import cn.wghtstudio.insurance.dao.repository.OrderRepository;
import cn.wghtstudio.insurance.dao.repository.OtherFileRepository;
import cn.wghtstudio.insurance.service.DownloadService;
import cn.wghtstudio.insurance.util.LicensePlateWhenNewFactory;
import lombok.Builder;
import lombok.Data;
import okhttp3.*;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class DownloadServiceImpl implements DownloadService {
    @Resource
    OrderRepository orderRepository;

    @Resource
    OtherFileRepository otherFileRepository;

    @Data
    @Builder
    static class CompressItem {
        private String name;
        private String url;
    }

    @Data
    static class OrderBaseParams {
        private String identify;
        private String plate;
    }

    private String getFix(String url) {
        final String[] names = url.split("\\.");
        if (names.length > 0) {
            return names[names.length - 1];
        } else {
            return "jpg";
        }
    }

    private OrderBaseParams getOrderBaseParams(Order item) {
        OrderBaseParams baseParams = new OrderBaseParams();

        if (item.getIdCard() != null) {
            baseParams.setIdentify(item.getIdCard().getName());
        } else if (item.getBusinessLicense() != null) {
            baseParams.setIdentify(item.getBusinessLicense().getName());
        }

        if (item.getDrivingLicense() != null) {
            baseParams.setPlate(item.getDrivingLicense().getPlateNumber());
        } else if (item.getCertificate() != null) {
            baseParams.setPlate(LicensePlateWhenNewFactory.getLicensePlateWhenNew(item.getCertificate().getEngine()));
        }

        return baseParams;
    }

    private Map<String, List<CompressItem>> getEvidenceFolder(List<Order> orders) {
        Map<String, List<CompressItem>> folder = new HashMap<>();

        orders.forEach((item) -> {
            OrderBaseParams baseParams = getOrderBaseParams(item);
            if (baseParams.getIdentify() == null || baseParams.getPlate() == null) {
                return;
            }


            folder.computeIfAbsent(baseParams.getIdentify(), k -> new ArrayList<>());

            if (item.getIdCard() != null) {
                List<CompressItem> compressItems = folder.get(baseParams.getIdentify());
                compressItems.add(CompressItem.builder().
                        name(baseParams.getPlate() + "-?????????." + getFix(item.getIdCard().getUrl())).
                        url(item.getIdCard().getUrl()).
                        build());
            } else if (item.getBusinessLicense() != null) {
                List<CompressItem> compressItems = folder.get(baseParams.getIdentify());
                compressItems.add(CompressItem.builder().
                        name(baseParams.getPlate() + "-????????????." + getFix(item.getBusinessLicense().getUrl())).
                        url(item.getBusinessLicense().getUrl()).
                        build());
            }

            if (item.getDrivingLicense() != null) {
                List<CompressItem> compressItems = folder.get(baseParams.getIdentify());
                compressItems.add(CompressItem.builder().
                        name(baseParams.getPlate() + "-?????????." + getFix(item.getDrivingLicense().getUrl())).
                        url(item.getDrivingLicense().getUrl()).
                        build());
            } else if (item.getCertificate() != null) {
                List<CompressItem> compressItems = folder.get(baseParams.getIdentify());
                compressItems.add(CompressItem.builder().
                        name(baseParams.getPlate() + "-?????????." + getFix(item.getCertificate().getUrl())).
                        url(item.getCertificate().getUrl()).
                        build());
            }

            List<OtherFile> otherFiles = otherFileRepository.getOtherFilesByOrderId(item.getId());
            for (int i = 0; i < otherFiles.size(); i++) {
                List<CompressItem> compressItems = folder.get(baseParams.getIdentify());
                compressItems.add(CompressItem.builder().
                        name(baseParams.getPlate() + "-????????????" + i + "." + getFix(otherFiles.get(i).getUrl())).
                        url(otherFiles.get(i).getUrl()).
                        build());
            }
        });

        return folder;
    }

    private Map<String, List<CompressItem>> getOverInsurancePolicyFolder(List<Order> orders, Integer downloadType) {
        Map<String, List<CompressItem>> folder = new HashMap<>();
        orders.forEach((item) -> {
            OrderBaseParams baseParams = getOrderBaseParams(item);
            if (baseParams.getIdentify() == null || baseParams.getPlate() == null) {
                return;
            }


            folder.computeIfAbsent(baseParams.getIdentify(), k -> new ArrayList<>());

            if (item.getPolicy() != null) {
                List<CompressItem> compressItems = folder.get(baseParams.getIdentify());
                if (downloadType != 1) {
                    compressItems.add(CompressItem.builder().
                            name(baseParams.getPlate() + "-?????????." + getFix(item.getOverInsurancePolicyPic().getName())).
                            url(item.getOverInsurancePolicyPic().getUrl()).
                            build());
                }
                if (downloadType != 0) {
                    compressItems.add(CompressItem.builder().
                            name(baseParams.getPlate() + "-?????????." + getFix(item.getOverInsurancePolicy().getName())).
                            url(item.getOverInsurancePolicy().getUrl()).
                            build());
                }
            }
        });
        return folder;
    }

    private Map<String, List<CompressItem>> getPolicyFolder(List<Order> orders) {
        Map<String, List<CompressItem>> folder = new HashMap<>();

        orders.forEach((item) -> {
            OrderBaseParams baseParams = getOrderBaseParams(item);
            if (baseParams.getIdentify() == null || baseParams.getPlate() == null) {
                return;
            }


            folder.computeIfAbsent(baseParams.getIdentify(), k -> new ArrayList<>());

            if (item.getPolicy() != null) {
                List<CompressItem> compressItems = folder.get(baseParams.getIdentify());
                compressItems.add(CompressItem.builder().
                        name(baseParams.getPlate() + "-?????????." + getFix(item.getPolicy().getUrl())).
                        url(item.getOverInsurancePolicy().getUrl()).
                        build());
            }
        });

        return folder;
    }

    private void writeFileToResponse(OutputStream outputStream, Map<String, List<CompressItem>> folder) throws InterruptedException, IOException {
        int count = 0;
        for (Map.Entry<String, List<CompressItem>> node : folder.entrySet()) {
            count += node.getValue().size();
        }

        final Lock lock = new ReentrantLock();
        final CountDownLatch countDownLatch = new CountDownLatch(count);
        final OkHttpClient client = new OkHttpClient();

        try (ZipArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(outputStream)) {
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
                                ZipArchiveEntry entry = new ZipArchiveEntry(node.getKey() + "/" + compressItem.getName());
                                zipOutputStream.putArchiveEntry(entry);
                                zipOutputStream.write(body);
                                zipOutputStream.closeArchiveEntry();
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

    @Override
    public void getEvidenceUrlService(HttpServletResponse response, List<Integer> ids) throws IOException, InterruptedException {
        List<Order> orders = orderRepository.getOrderByUser(Map.of("ids", ids));
        Map<String, List<CompressItem>> folder = getEvidenceFolder(orders);

        response.setContentType("application/zip");
        response.setHeader("Content-disposition", "attachment;filename=export.zip");
        response.flushBuffer();

        OutputStream outputStream = response.getOutputStream();
        writeFileToResponse(outputStream, folder);
    }

    @Override
    public void getPolicyService(HttpServletResponse response, List<Integer> ids) throws IOException, InterruptedException {
        List<Order> orders = orderRepository.getOrderByUser(Map.of("ids", ids));
        Map<String, List<CompressItem>> folder = getPolicyFolder(orders);

        response.setContentType("application/zip");
        response.setHeader("Content-disposition", "attachment;filename=export.zip");
        response.flushBuffer();

        OutputStream outputStream = response.getOutputStream();
        writeFileToResponse(outputStream, folder);
    }

    @Override
    public void getOverInsurancePolicyService(HttpServletResponse response, List<Integer> ids, Integer downloadType) throws IOException, InterruptedException {
        List<Order> orders = orderRepository.getOrderByUser(Map.of("ids", ids));
        Map<String, List<CompressItem>> folder = getOverInsurancePolicyFolder(orders, downloadType);

        response.setContentType("application/zip");
        response.setHeader("Content-disposition", "attachment;filename=export.zip");
        response.flushBuffer();

        OutputStream outputStream = response.getOutputStream();
        writeFileToResponse(outputStream, folder);
    }
}
