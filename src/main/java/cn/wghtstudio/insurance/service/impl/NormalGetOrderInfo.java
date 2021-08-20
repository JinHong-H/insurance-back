package cn.wghtstudio.insurance.service.impl;

import cn.wghtstudio.insurance.dao.entity.Order;
import cn.wghtstudio.insurance.dao.entity.User;
import cn.wghtstudio.insurance.dao.repository.OrderRepository;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class NormalGetOrderInfo implements GetOrderInfo {
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
    public Integer getALLOrderListCount(User user, Map<String, Object> params) {
        params.put("userId", user.getId());
        System.out.println(params);
        return orderRepository.getOrderCount(params);
    }
}
