package cn.wghtstudio.insurance.service.impl;

import cn.wghtstudio.insurance.dao.entity.*;
import cn.wghtstudio.insurance.dao.repository.OrderRepository;
import cn.wghtstudio.insurance.service.entity.GetInsuranceListItem;
import cn.wghtstudio.insurance.util.FormatDate;
import cn.wghtstudio.insurance.util.LicensePlateWhenNewFactory;
import cn.wghtstudio.insurance.util.excel.ExcelUtil;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CompanyGetOrderInfo extends GetOrderInfo {
    OrderRepository orderRepository;

    public CompanyGetOrderInfo(OrderRepository orderRepository) {
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
        List<BaseExportColumnItem> exportColumnItems = new ArrayList<>();
        orderList.forEach((item) -> {
            BaseExportColumnItem.BaseExportColumnItemBuilder builder = BaseExportColumnItem.builder();
            builder.number("PICC-CP-" + String.format("%06d", item.getId())).
                    startTime(FormatDate.getFormatDate(item.getStartTime())).
                    carType(item.getCarType().getName()).
                    payType(item.getPayment().getName());

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

        Workbook wb = ExcelUtil.export(exportColumnItems, BaseExportColumnItem.class);

        writeToResponse(response, wb);
    }
}
