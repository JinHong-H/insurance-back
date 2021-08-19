package cn.wghtstudio.insurance.service;

import cn.wghtstudio.insurance.dao.entity.User;
import cn.wghtstudio.insurance.service.entity.GetInsuranceListResponseBody;

public interface InsuranceService {
    GetInsuranceListResponseBody getAllList(User user);
}
