package cn.wghtstudio.insurance.service;

import cn.wghtstudio.insurance.exception.OCRException;
import cn.wghtstudio.insurance.service.entity.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface OcrInfoService {
    IdCardResponseBody idCardInfoService(String url) throws IOException, OCRException;

    BusinessLicenseResponseBody businessInfoService(String url) throws IOException, OCRException;

    DrivingLicenseResponseBody drivingInfoService(String url) throws IOException, OCRException;

    CertificateResponseBody certificateInfoService(String url) throws IOException, OCRException;

    OtherFileResponseBody otherFileService(String url);

    void policyRecordService(MultipartFile file) throws IOException, InterruptedException;

//    InsurancepolicyResponseBody insurance(String url) throws IOException, OCRException;
}
