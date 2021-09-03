package cn.wghtstudio.insurance.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface DownloadService {
    void getEvidenceUrlService(HttpServletResponse response, List<Integer> ids) throws IOException, InterruptedException;

    void getPolicyService(HttpServletResponse response, List<Integer> ids) throws IOException, InterruptedException;

    void getOverInsurancePolicyService(HttpServletResponse response, List<Integer> ids, Integer downloadType) throws IOException, InterruptedException;
}
