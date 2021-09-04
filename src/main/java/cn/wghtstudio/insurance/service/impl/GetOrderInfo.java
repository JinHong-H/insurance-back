package cn.wghtstudio.insurance.service.impl;

import cn.wghtstudio.insurance.dao.entity.Order;
import cn.wghtstudio.insurance.dao.entity.User;
import cn.wghtstudio.insurance.service.entity.GetInsuranceListItem;
import cn.wghtstudio.insurance.util.FormatDate;
import cn.wghtstudio.insurance.util.LicensePlateWhenNewFactory;
import org.apache.poi.ss.formula.eval.NotImplementedException;

import java.util.List;
import java.util.Map;

public abstract class GetOrderInfo {
    static GetInsuranceListItem getGetInsuranceListItem(Order item, GetInsuranceListItem.GetInsuranceListItemBuilder itemBuilder) {
        itemBuilder.startTime(FormatDate.getFormatDate(item.getStartTime()));

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

    List<Order> getALLOrderList(User user, Map<String, Object> params) {
        throw new NotImplementedException("getALLOrderList");
    }

    List<GetInsuranceListItem> processListItem(User user, Map<String, Object> params) {
        throw new NotImplementedException("processListItem");
    }

    Order getOrderDetail(User user, Map<String, Object> params) {
        throw new NotImplementedException("getOrderDetail");
    }

    Integer getALLOrderListCount(User user, Map<String, Object> params) {
        throw new NotImplementedException("getALLOrderListCount");
    }
}
