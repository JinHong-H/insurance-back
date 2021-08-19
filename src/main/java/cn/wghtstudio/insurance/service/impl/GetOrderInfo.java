package cn.wghtstudio.insurance.service.impl;

import cn.wghtstudio.insurance.dao.entity.Order;
import cn.wghtstudio.insurance.dao.entity.User;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface GetOrderInfo {
    List<Order> getALLOrderList(User user, Map<String, Object> params);

    Integer getALLOrderListCount(User user, Map<String, Object> params);
}
