package cn.wghtstudio.insurance.service;

import cn.wghtstudio.insurance.exception.OCRException;
import cn.wghtstudio.insurance.service.entity.*;

import java.io.IOException;

public interface OcrInfoService {
    IdCardResponseBody idCardInfoService(String url) throws IOException, OCRException;

    BusinessLicenseResponseBody businessInfoService(String url) throws IOException, OCRException;

    DrivingLicenseResponseBody drivingInfoService(String url) throws IOException, OCRException;

    CertificateResponseBody certificateInfoService(String url) throws IOException, OCRException;

    OtherFileResponseBody otherFileService(String url);

    InsurancepolicyResponseBody insurance(String url) throws IOException, OCRException;
}
