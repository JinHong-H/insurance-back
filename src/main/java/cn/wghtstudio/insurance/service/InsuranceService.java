package cn.wghtstudio.insurance.service;

import cn.wghtstudio.insurance.dao.entity.User;
import cn.wghtstudio.insurance.service.entity.GetInsuranceListResponseBody;

import java.util.Map;

public interface InsuranceService {
    GetInsuranceListResponseBody getAllList(User user, Map<String, Object> params);
}
