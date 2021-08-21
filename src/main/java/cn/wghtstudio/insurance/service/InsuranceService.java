package cn.wghtstudio.insurance.service;

import cn.wghtstudio.insurance.dao.entity.User;
import cn.wghtstudio.insurance.service.entity.GetInsuranceListResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public interface InsuranceService {
    GetInsuranceListResponseBody getAllList(User user, Map<String, Object> params);

    void exportExcel(HttpServletResponse response, User user, Map<String, Object> params) throws IOException;
}
