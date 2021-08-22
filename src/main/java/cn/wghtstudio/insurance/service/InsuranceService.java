package cn.wghtstudio.insurance.service;

import cn.wghtstudio.insurance.controller.entity.CreateInsuranceRequestBody;
import cn.wghtstudio.insurance.dao.entity.User;
import cn.wghtstudio.insurance.service.entity.GetInsuranceListResponseBody;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public interface InsuranceService {
    GetInsuranceListResponseBody getAllList(User user, Map<String, Object> params);

    @Transactional
    void createNewOrder(User user, CreateInsuranceRequestBody req);

    void exportExcel(HttpServletResponse response, User user, Map<String, Object> params) throws IOException;
}
