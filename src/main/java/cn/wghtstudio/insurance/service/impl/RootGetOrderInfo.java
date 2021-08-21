package cn.wghtstudio.insurance.service.impl;


import cn.wghtstudio.insurance.dao.entity.Order;
import cn.wghtstudio.insurance.dao.entity.User;
import cn.wghtstudio.insurance.dao.repository.OrderRepository;

import java.util.List;
import java.util.Map;

public class RootGetOrderInfo implements GetOrderInfo {
    OrderRepository orderRepository;

    public RootGetOrderInfo(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getALLOrderList(User user, Map<String, Object> params) {
        return orderRepository.getOrderByUser(params);
    }

    @Override
    public Integer getALLOrderListCount(User user, Map<String, Object> params) {
        return orderRepository.getOrderCount(params);
    }
}
