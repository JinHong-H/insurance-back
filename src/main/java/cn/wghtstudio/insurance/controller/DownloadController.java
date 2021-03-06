package cn.wghtstudio.insurance.controller;

import cn.wghtstudio.insurance.service.DownloadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/download")
public class DownloadController {
    private final Logger logger = LoggerFactory.getLogger(DownloadController.class);

    @Resource
    DownloadService downloadService;

    @GetMapping("/evidence")
    public void getEvidenceUrl(HttpServletResponse response, @RequestParam List<Integer> ids) {
        try {
            downloadService.getEvidenceUrlService(response, ids);
        } catch (Exception e) {
            logger.warn("Exception", e);
        }
    }

    @GetMapping("/policy")
    public void getPolicyUrl(HttpServletResponse response, @RequestParam List<Integer> ids) {
        try {
            downloadService.getPolicyService(response, ids);
        } catch (Exception e) {
            logger.warn("Exception", e);
        }
    }

    /**
     * 下载投保单
     *
     * @param downloadType 下载格式 默认 0--下载png格式  1--下载pdf格式  others--都下载
     */
    @GetMapping("/overInsurancePolicy")
    public void getOverInsurancePolicyUrl(HttpServletResponse response, @RequestParam List<Integer> ids, @RequestParam(defaultValue = "0", name = "downloadType") Integer downloadType) {
        try {
            downloadService.getOverInsurancePolicyService(response, ids, downloadType);
        } catch (Exception e) {
            logger.warn("Exception", e);
        }
    }
}
