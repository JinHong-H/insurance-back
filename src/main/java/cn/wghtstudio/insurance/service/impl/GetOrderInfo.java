package cn.wghtstudio.insurance.service.impl;

import cn.wghtstudio.insurance.dao.entity.Order;
import cn.wghtstudio.insurance.dao.entity.User;
import cn.wghtstudio.insurance.service.entity.GetInsuranceListItem;
import cn.wghtstudio.insurance.util.FormatDate;
import cn.wghtstudio.insurance.util.LicensePlateWhenNewFactory;
import cn.wghtstudio.insurance.util.excel.ExcelColumn;
import lombok.Builder;
import lombok.Data;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@Data
@Builder
class BaseExportColumnItem {
    @ExcelColumn(value = "序号", column = 1)
    private String number;

    @ExcelColumn(value = "车主", column = 2)
    private String owner;

    @ExcelColumn(value = "起保时间", column = 3)
    private String startTime;

    @ExcelColumn(value = "车辆类型", column = 4)
    private String carType;

    @ExcelColumn(value = "支付方式", column = 5)
    private String payType;

    @ExcelColumn(value = "车牌号", column = 6)
    private String licensePlate;

    @ExcelColumn(value = "车架号", column = 7)
    private String frame;

    @ExcelColumn(value = "发动机号", column = 8)
    private String engine;

    @ExcelColumn(value = "地址", column = 9)
    private String address;

    @ExcelColumn(value = "保单号", column = 10)
    private String policy;
}

public abstract class GetOrderInfo {
    static GetInsuranceListItem getGetInsuranceListItem(Order item, GetInsuranceListItem.GetInsuranceListItemBuilder itemBuilder) {
        itemBuilder.id(item.getId()).
                payType(item.getPayment().getName()).
                carType(item.getCarType().getName()).
                startTime(FormatDate.getFormatDate(item.getStartTime())).
                createAt(FormatDate.getFormatDateTime(item.getCreateAt()));

        if (item.getIdCard() != null) {
            itemBuilder.owner(item.getIdCard().getName());
        } else if (item.getBusinessLicense() != null) {
            itemBuilder.owner(item.getBusinessLicense().getName());
        }

        if (item.getDrivingLicense() != null) {
            itemBuilder.licensePlate(item.getDrivingLicense().getPlateNumber());
        } else if (item.getCertificate() != null) {
            itemBuilder.licensePlate(LicensePlateWhenNewFactory.getLicensePlateWhenNew(item.getCertificate().getEngine()));
        }

        if (item.getPolicy() != null) {
            itemBuilder.policy(item.getPolicy().getNumber());
        }

        if (item.getOverInsurancePolicy() != null) {
            itemBuilder.overPolicy(item.getOverInsurancePolicy().getUrl());
        }

        return itemBuilder.build();
    }

    static void writeToResponse(HttpServletResponse response, Workbook wb) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-disposition", "attachment;filename=export.xlsx");
        response.flushBuffer();

        OutputStream outputStream = response.getOutputStream();
        wb.write(outputStream);
        wb.close();
        outputStream.flush();
        outputStream.close();
    }

    public abstract List<Order> getALLOrderList(User user, Map<String, Object> params);

    public abstract List<GetInsuranceListItem> processListItem(User user, Map<String, Object> params);

    public abstract Order getOrderDetail(User user, Map<String, Object> params);

    public abstract Integer getALLOrderListCount(User user, Map<String, Object> params);

    public abstract void exportExcelItem(HttpServletResponse response, User user, Map<String, Object> params) throws IOException;
}
