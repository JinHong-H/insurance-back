package cn.wghtstudio.insurance.service.impl;

import cn.wghtstudio.insurance.dao.entity.User;
import cn.wghtstudio.insurance.dao.repository.OrderRepository;
import cn.wghtstudio.insurance.exception.AuthNotMatchException;
import cn.wghtstudio.insurance.service.InsuranceService;
import cn.wghtstudio.insurance.service.entity.GetInsuranceListResponseBody;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class InsuranceServiceImpl implements InsuranceService {
    @Resource
    OrderRepository orderRepository;

    @Override
    public GetInsuranceListResponseBody getAllList(User user) throws AuthNotMatchException {
        GetInsuranceListResponseBody.GetInsuranceListResponseBodyBuilder builder = GetInsuranceListResponseBody.builder();

        // 得到对应用户的实体
        GetOrderInfo getOrderInfo = GetOrderInfoFactory.getOrderInfo(user.getRole().getValue(), orderRepository);
        if (getOrderInfo == null) {
            throw new AuthNotMatchException();
        }

        // 得到对应的订单数目
        Integer count = getOrderInfo.getALLOrderListCount(user, null, null);
        if (count != null) {
            builder.total(count);
        }

        return builder.build();
    }
}
