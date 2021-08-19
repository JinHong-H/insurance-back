package cn.wghtstudio.insurance.service.impl;

import cn.wghtstudio.insurance.dao.entity.Order;
import cn.wghtstudio.insurance.dao.entity.User;
import cn.wghtstudio.insurance.dao.repository.OrderRepository;
import cn.wghtstudio.insurance.exception.AuthNotMatchException;
import cn.wghtstudio.insurance.service.InsuranceService;
import cn.wghtstudio.insurance.service.entity.GetInsuranceListItem;
import cn.wghtstudio.insurance.service.entity.GetInsuranceListResponseBody;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InsuranceServiceImpl implements InsuranceService {
    @Resource
    OrderRepository orderRepository;

    @Override
    public GetInsuranceListResponseBody getAllList(User user, Map<String, Object> params) {
        GetInsuranceListResponseBody.GetInsuranceListResponseBodyBuilder builder = GetInsuranceListResponseBody.builder();
        builder.pageSize((Integer) params.get("limit"));
        builder.offset((Integer) params.get("offset"));

        // 得到对应用户的实体
        GetOrderInfo getOrderInfo = GetOrderInfoFactory.getOrderInfo(user.getRole().getValue(), orderRepository);

        // 得到对应的订单数目
        Integer count = getOrderInfo.getALLOrderListCount(user, null);
        if (count != null) {
            builder.total(count);
        }

        // 得到订单列表
        List<Order> orderList = getOrderInfo.getALLOrderList(user, params);

        // 处理订单符合返回数据
        List<GetInsuranceListItem> getInsuranceListItems = orderList.stream().map((item) -> {
            GetInsuranceListItem.GetInsuranceListItemBuilder itemBuilder = GetInsuranceListItem.builder();
            itemBuilder.id(item.getId());
            itemBuilder.payType(item.getPayment().getName());
            itemBuilder.carType(item.getCarType().getName());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            itemBuilder.startTime(simpleDateFormat.format(item.getStartTime()));

            if (item.getIdCard() != null) {
                itemBuilder.owner(item.getIdCard().getName());
            }
            if (item.getBusinessLicense() != null) {
                itemBuilder.owner(item.getBusinessLicense().getName());
            }
            if (item.getDrivingLicense() != null) {
                itemBuilder.licensePlate(item.getDrivingLicense().getPlateNumber());
            }
            if (item.getCertificate() != null) {
                itemBuilder.licensePlate("新车");
            }

            return itemBuilder.build();
        }).collect(Collectors.toList());
        builder.items(getInsuranceListItems);

        return builder.build();
    }
}
