package cn.wghtstudio.insurance.service.impl;


import cn.wghtstudio.insurance.dao.entity.*;
import cn.wghtstudio.insurance.dao.repository.OrderRepository;
import cn.wghtstudio.insurance.service.entity.GetInsuranceListItem;
import cn.wghtstudio.insurance.util.FormatDate;
import cn.wghtstudio.insurance.util.LicensePlateWhenNewFactory;
import cn.wghtstudio.insurance.util.excel.ExcelColumn;
import cn.wghtstudio.insurance.util.excel.ExcelUtil;
import lombok.Builder;
import lombok.Data;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
class RootExportColumnItem {
    @ExcelColumn(value = "序号", column = 1)
    private String number;

    @ExcelColumn(value = "车主", column = 2)
    private String owner;

    @ExcelColumn(value = "起保时间", column = 3)
    private String startTime;

    @ExcelColumn(value = "业务员", column = 4)
    private String nickname;

    @ExcelColumn(value = "车辆类型", column = 5)
    private String carType;

    @ExcelColumn(value = "支付方式", column = 6)
    private String payType;

    @ExcelColumn(value = "车牌号", column = 7)
    private String licensePlate;

    @ExcelColumn(value = "车架号", column = 8)
    private String frame;

    @ExcelColumn(value = "发动机号", column = 9)
    private String engine;

    @ExcelColumn(value = "地址", column = 10)
    private String address;

    @ExcelColumn(value = "保单号", column = 11)
    private String policy;
}

public class RootGetOrderInfo extends GetOrderInfo {
    OrderRepository orderRepository;

    public RootGetOrderInfo(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getALLOrderList(User user, Map<String, Object> params) {
        return orderRepository.getOrderByUser(params);
    }

    @Override
    public List<GetInsuranceListItem> processListItem(User user, Map<String, Object> params) {
        List<Order> orders = getALLOrderList(user, params);

        return orders.stream().map(item -> {
            GetInsuranceListItem.GetInsuranceListItemBuilder itemBuilder = GetInsuranceListItem.builder();
            itemBuilder.username(item.getUser().getNickname());

            return getGetInsuranceListItem(item, itemBuilder);
        }).collect(Collectors.toList());
    }

    @Override
    public Order getOrderDetail(User user, Map<String, Object> params) {
        List<Order> orders = orderRepository.getOrderByUser(params);
        return orders.get(0);
    }

    @Override
    public Integer getALLOrderListCount(User user, Map<String, Object> params) {
        return orderRepository.getOrderCount(params);
    }

    @Override
    public void exportExcelItem(HttpServletResponse response, User user, Map<String, Object> params) throws IOException {
        // 得到订单列表
        List<Order> orderList = getALLOrderList(user, params);

        // 处理订单符合返回数据
        List<RootExportColumnItem> exportColumnItems = new ArrayList<>();
        orderList.forEach((item) -> {
            RootExportColumnItem.RootExportColumnItemBuilder builder = RootExportColumnItem.builder();
            builder.number("PICC-CP-" + String.format("%06d", item.getId())).
                    startTime(FormatDate.getFormatDate(item.getStartTime())).
                    carType(item.getCarType().getName()).
                    payType(item.getPayment().getName()).
                    nickname(item.getUser().getNickname());

            if (item.getIdCard() != null) {
                IdCard idCard = item.getIdCard();
                builder.owner(idCard.getName()).
                        address(idCard.getAddress());
            } else if (item.getBusinessLicense() != null) {
                BusinessLicense businessLicense = item.getBusinessLicense();
                builder.owner(businessLicense.getName()).
                        address(businessLicense.getAddress());
            }

            if (item.getDrivingLicense() != null) {
                DrivingLicense drivingLicense = item.getDrivingLicense();
                builder.licensePlate(drivingLicense.getPlateNumber()).
                        frame(drivingLicense.getFrame()).
                        engine(drivingLicense.getEngine());
            } else if (item.getCertificate() != null) {
                Certificate certificate = item.getCertificate();
                builder.licensePlate(LicensePlateWhenNewFactory.getLicensePlateWhenNew(item.getCertificate().getEngine())).
                        frame(certificate.getFrame()).
                        engine(certificate.getEngine());
            }

            if (item.getPolicy() != null) {
                builder.policy(item.getPolicy().getNumber());
            }

            exportColumnItems.add(builder.build());
        });

        Workbook wb = ExcelUtil.export(exportColumnItems, RootExportColumnItem.class);
        writeToResponse(response, wb);
    }
}
