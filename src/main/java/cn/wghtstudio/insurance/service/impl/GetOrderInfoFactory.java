package cn.wghtstudio.insurance.service.impl;

import cn.wghtstudio.insurance.dao.repository.OrderRepository;
import cn.wghtstudio.insurance.util.JudgeUserUtil;

public class GetOrderInfoFactory {
    public static GetOrderInfo getOrderInfo(int auth, OrderRepository orderRepository) {
        if (JudgeUserUtil.isNormal(auth)) {
            return new NormalGetOrderInfo(orderRepository);
        }

        return null;
    }
}
