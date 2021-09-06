package cn.wghtstudio.insurance.service.impl;

import cn.wghtstudio.insurance.controller.entity.CreateInsuranceRequestBody;
import cn.wghtstudio.insurance.dao.entity.*;
import cn.wghtstudio.insurance.dao.repository.*;
import cn.wghtstudio.insurance.exception.RecordNotFoundException;
import cn.wghtstudio.insurance.service.InsuranceService;
import cn.wghtstudio.insurance.service.entity.*;
import cn.wghtstudio.insurance.util.FormatDate;
import cn.wghtstudio.insurance.util.LicensePlateWhenNewFactory;
import cn.wghtstudio.insurance.util.excel.ExcelColumn;
import cn.wghtstudio.insurance.util.excel.ExcelUtil;
import lombok.Builder;
import lombok.Data;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
class ExportColumnItem {
    @ExcelColumn(value = "序号", column = 1)
    private Integer number;

    @ExcelColumn(value = "车牌号", column = 2)
    private String licensePlate;

    @ExcelColumn(value = "车架号", column = 3)
    private String frame;

    @ExcelColumn(value = "发动机号", column = 4)
    private String engine;

    @ExcelColumn(value = "车主", column = 5)
    private String owner;

    @ExcelColumn(value = "起保时间", column = 6)
    private String startTime;

    @ExcelColumn(value = "地址", column = 7)
    private String address;

    @ExcelColumn(value = "保单号", column = 8)
    private String policy;
}

@Component
public class InsuranceServiceImpl implements InsuranceService {
    @Resource
    OrderRepository orderRepository;

    @Resource
    IdCardRepository idCardRepository;

    @Resource
    BusinessLicenseRepository businessLicenseRepository;

    @Resource
    DrivingLicenseRepository drivingLicenseRepository;

    @Resource
    CertificateRepository certificateRepository;

    @Resource
    PolicyRepository policyRepository;

    @Resource
    OverInsurancePolicyRepository overInsurancePolicyRepository;

    @Resource
    OverInsurancePolicyPicRepository overInsurancePolicyPicRepository;

    @Resource
    OtherFileRepository otherFileRepository;

    @Override
    public GetInsuranceListResponseBody getAllList(User user, Map<String, Object> params) {
        GetInsuranceListResponseBody.GetInsuranceListResponseBodyBuilder builder = GetInsuranceListResponseBody.builder();
        builder.pageSize((Integer) params.get("limit"));
        builder.current((Integer) params.get("current"));

        // 得到对应用户的实体
        GetOrderInfo getOrderInfo = GetOrderInfoFactory.getOrderInfo(user.getRole().getValue(), orderRepository);

        // 得到对应的订单数目
        Integer count = getOrderInfo.getALLOrderListCount(user, params);
        if (count != null) {
            builder.total(count);
        }

        // 处理订单符合返回数据
        List<GetInsuranceListItem> getInsuranceListItems = getOrderInfo.processListItem(user, params);
        builder.items(getInsuranceListItems);

        return builder.build();
    }

    @Override
    public GetOrderDetailResponseBody getOrderDetail(User user, Map<String, Object> params) throws RecordNotFoundException {
        // 得到对应用户的实体
        GetOrderInfo getOrderInfo = GetOrderInfoFactory.getOrderInfo(user.getRole().getValue(), orderRepository);
        // 得到订单列表
        Order order = getOrderInfo.getOrderDetail(user, params);
        if (order == null) {
            throw new RecordNotFoundException();
        }

        GetOrderDetailResponseBody.GetOrderDetailResponseBodyBuilder builder = GetOrderDetailResponseBody.builder();
        builder.id(order.getId()).
                payType(order.getPayment().getName()).
                carType(order.getCarType().getName()).
                startTime(FormatDate.getFormatDate(order.getStartTime()));

        if (order.getIdCard() != null) {
            final IdCard idCard = order.getIdCard();
            builder.owner(idCard.getName());
            builder.idCard(GetOrderDetailResponseBody.IdCard.builder().
                    url(idCard.getUrl()).
                    number(idCard.getNumber()).
                    address(idCard.getAddress()).
                    build());
        } else if (order.getBusinessLicense() != null) {
            final BusinessLicense businessLicense = order.getBusinessLicense();
            builder.owner(businessLicense.getName());
            builder.business(GetOrderDetailResponseBody.Business.builder().
                    url(businessLicense.getUrl()).
                    number(businessLicense.getNumber()).
                    address(businessLicense.getAddress()).
                    build());
        }

        if (order.getDrivingLicense() != null) {
            final DrivingLicense drivingLicense = order.getDrivingLicense();
            builder.licensePlate(drivingLicense.getPlateNumber());
            builder.driving(GetOrderDetailResponseBody.Driving.builder().
                    url(drivingLicense.getUrl()).
                    frame(drivingLicense.getFrame()).
                    engine(drivingLicense.getEngine()).
                    type(drivingLicense.getType()).
                    build());
        } else if (order.getCertificate() != null) {
            final Certificate certificate = order.getCertificate();
            builder.licensePlate(LicensePlateWhenNewFactory.getLicensePlateWhenNew(certificate.getEngine()));
            builder.certificate(GetOrderDetailResponseBody.Certificate.builder().
                    url(certificate.getUrl()).
                    frame(certificate.getFrame()).
                    engine(certificate.getEngine()).
                    build());
        }

        if (order.getPolicy() != null) {
            final Policy policy = order.getPolicy();
            builder.policy(GetOrderDetailResponseBody.Policy.builder().
                    url(policy.getUrl()).
                    name(policy.getName()).
                    number(policy.getNumber()).
                    build());
        }

        return builder.build();
    }

    @Override
    public GetPolicyResponseBody getPolicyList(Map<String, Object> params) {
        GetPolicyResponseBody.GetPolicyResponseBodyBuilder builder = GetPolicyResponseBody.builder();
        builder.pageSize((Integer) params.get("limit"));
        builder.current((Integer) params.get("current"));

        // 得到保单总数
        Integer count = policyRepository.getPolicyCount();
        if (count != null) {
            builder.total(count);
        }

        // 得到保单列表
        List<Policy> res = policyRepository.getPolicyList(params);

        // 处理保单符合返回数据
        List<GetPolicyListItem> getPolicyListItems = res.stream().map((item) -> {
            GetPolicyListItem.GetPolicyListItemBuilder itemBuilder = GetPolicyListItem.builder().
                    id(item.getId()).
                    name(item.getName()).
                    url(item.getUrl()).
                    number(item.getNumber()).
                    processType(item.getProcessType());
            return itemBuilder.build();
        }).collect(Collectors.toList());
        builder.items(getPolicyListItems);

        return builder.build();
    }

    @Override
    @Transactional
    public void createNewOrder(User user, CreateInsuranceRequestBody req) throws ParseException {
        // 创建 order
        Order.OrderBuilder builder = Order.builder();
        Order order = builder.
                createBy(user.getId()).
                startTime(new SimpleDateFormat("yyyy-MM-dd").parse(req.getStartTime())).
                paymentId(req.getPaymentId()).
                carTypeId(req.getCarTypeId()).
                build();
        orderRepository.createOrder(order);

        int orderId = order.getId();

        // 开始更新证明材料手动修正字段与外键关联
        if (req.getIdCard() != null) {
            CreateInsuranceRequestBody.IdCardRequestBody body = req.getIdCard();
            IdCard idCard = IdCard.builder().
                    id(body.getId()).
                    name(body.getName()).
                    address(body.getAddress()).
                    number(body.getNumber()).
                    orderId(orderId).
                    build();
            idCardRepository.updateIdCard(idCard);
        } else if (req.getBusinessLicense() != null) {
            CreateInsuranceRequestBody.BusinessLicenseRequestBody body = req.getBusinessLicense();
            BusinessLicense businessLicense = BusinessLicense.builder().
                    id(body.getId()).
                    name(body.getName()).
                    address(body.getAddress()).
                    number(body.getNumber()).
                    orderId(orderId).
                    build();
            businessLicenseRepository.updateBusinessLicense(businessLicense);
        }

        if (req.getDrivingLicense() != null) {
            CreateInsuranceRequestBody.DrivingLicenseRequestBody body = req.getDrivingLicense();
            DrivingLicense drivingLicense = DrivingLicense.builder().
                    id(body.getId()).
                    plateNumber(body.getPlate()).
                    engine(body.getEngine()).
                    frame(body.getFrame()).
                    type(body.getType()).
                    orderId(orderId).
                    build();
            drivingLicenseRepository.updateDrivingLicense(drivingLicense);
        } else if (req.getCertificate() != null) {
            CreateInsuranceRequestBody.CertificateRequestBody body = req.getCertificate();
            Certificate certificate = Certificate.builder().
                    id(body.getId()).
                    engine(body.getEngine()).
                    frame(body.getFrame()).
                    orderId(orderId).
                    build();
            certificateRepository.updateCertificate(certificate);
        }

        if (req.getOtherFileId() != null) {
            otherFileRepository.updateOtherFiles(Map.of("orderId", orderId, "otherIds", req.getOtherFileId()));
        }
    }

    @Override
    @Transactional
    public void deleteOrder(User user, Map<String, Object> params) {
        // 得到对应用户的实体
        GetOrderInfo getOrderInfo = GetOrderInfoFactory.getOrderInfo(user.getRole().getValue(), orderRepository);
        Order order = getOrderInfo.getOrderDetail(user, params);

        System.out.println(order);

        // 依次删除文件
        if (order.getIdCard() != null) {
            idCardRepository.deleteIdCard(order.getIdCard().getId());
        }

        if (order.getBusinessLicense() != null) {
            businessLicenseRepository.deleteBusinessLicense(order.getBusinessLicense().getId());
        }

        if (order.getDrivingLicense() != null) {
            System.out.println("delete");
            drivingLicenseRepository.deleteDrivingLicense(order.getDrivingLicense().getId());
        }

        if (order.getCertificate() != null) {
            certificateRepository.deleteCertificate(order.getCertificate().getId());
        }

        if (order.getPolicy() != null) {
            policyRepository.deletePolicy(order.getPolicy().getId());
        }

        if (order.getOverInsurancePolicy() != null) {
            overInsurancePolicyRepository.deleteOverInsurancePolicy(order.getOverInsurancePolicy().getId());
        }

        if (order.getOverInsurancePolicyPic() != null) {
            overInsurancePolicyPicRepository.deleteOverInsurancePolicyPic(order.getOverInsurancePolicyPic().getId());
        }

        orderRepository.deleteOrderById(order.getId());
    }

    @Override
    public void exportExcel(HttpServletResponse response, User user, Map<String, Object> params) throws IOException {
        // 得到对应用户的实体
        GetOrderInfo getOrderInfo = GetOrderInfoFactory.getOrderInfo(user.getRole().getValue(), orderRepository);

        // 得到订单列表
        List<Order> orderList = getOrderInfo.getALLOrderList(user, params);

        // 处理订单符合返回数据
        List<ExportColumnItem> exportColumnItems = new ArrayList<>();
        orderList.forEach((item) -> {
            ExportColumnItem.ExportColumnItemBuilder builder = ExportColumnItem.builder();
            builder.number(exportColumnItems.size() + 1).
                    startTime(FormatDate.getFormatDate(item.getStartTime()));

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

        Workbook wb = ExcelUtil.export(exportColumnItems, ExportColumnItem.class);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-disposition", "attachment;filename=export.xlsx");
        response.flushBuffer();

        OutputStream outputStream = response.getOutputStream();
        wb.write(outputStream);
        wb.close();
        outputStream.flush();
        outputStream.close();
    }
}
