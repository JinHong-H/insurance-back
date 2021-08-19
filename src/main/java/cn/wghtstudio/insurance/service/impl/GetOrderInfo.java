package cn.wghtstudio.insurance.service.impl;

import cn.wghtstudio.insurance.dao.entity.Order;
import cn.wghtstudio.insurance.dao.entity.User;

import java.util.Date;
import java.util.List;

public interface GetOrderInfo {
    List<Order> getALLOrderList(User user, Date startTime, Date endTime);

    Integer getALLOrderListCount(User user, Date startTime, Date endTime);
}
