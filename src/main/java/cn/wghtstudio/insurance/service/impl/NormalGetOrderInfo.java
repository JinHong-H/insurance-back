package cn.wghtstudio.insurance.service.impl;

import cn.wghtstudio.insurance.dao.entity.Order;
import cn.wghtstudio.insurance.dao.entity.User;
import cn.wghtstudio.insurance.dao.repository.OrderRepository;

import java.util.Date;
import java.util.List;

public class NormalGetOrderInfo implements GetOrderInfo {
    OrderRepository orderRepository;

    public NormalGetOrderInfo(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getALLOrderList(User user, Date startTime, Date endTime) {
        return orderRepository.getOrderByUser(user.getId());
    }

    @Override
    public Integer getALLOrderListCount(User user, Date startTime, Date endTime) {
        return orderRepository.getOrderCount(user.getId());
    }
}
