package cn.wghtstudio.insurance.service.impl;

import cn.wghtstudio.insurance.dao.entity.Order;
import cn.wghtstudio.insurance.dao.entity.User;
import cn.wghtstudio.insurance.dao.repository.OrderRepository;
import cn.wghtstudio.insurance.service.entity.GetInsuranceListItem;
import cn.wghtstudio.insurance.util.FormatDate;
import cn.wghtstudio.insurance.util.LicensePlateWhenNewFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NormalGetOrderInfo extends GetOrderInfo {
    OrderRepository orderRepository;

    public NormalGetOrderInfo(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getALLOrderList(User user, Map<String, Object> params) {
        params.put("userId", user.getId());
        return orderRepository.getOrderByUser(params);
    }

    @Override
    public List<GetInsuranceListItem> processListItem(User user, Map<String, Object> params) {
        List<Order> orders = getALLOrderList(user, params);

        return orders.stream().map(item -> {
            GetInsuranceListItem.GetInsuranceListItemBuilder itemBuilder = GetInsuranceListItem.builder();
            itemBuilder.id(item.getId()).
                    payType(item.getPayment().getName()).
                    carType(item.getCarType().getName());

            return getGetInsuranceListItem(item, itemBuilder);
        }).collect(Collectors.toList());
    }

    @Override
    public Order getOrderDetail(User user, Map<String, Object> params) {
        params.put("userId", user.getId());
        List<Order> orders = orderRepository.getOrderByUser(params);
        return orders.get(0);
    }

    @Override
    public Integer getALLOrderListCount(User user, Map<String, Object> params) {
        params.put("userId", user.getId());
        return orderRepository.getOrderCount(params);
    }
}
