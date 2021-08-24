package cn.wghtstudio.insurance.service;

import cn.wghtstudio.insurance.exception.OCRException;
import cn.wghtstudio.insurance.service.entity.BusinessLicenseResponseBody;
import cn.wghtstudio.insurance.service.entity.CertificateResponseBody;
import cn.wghtstudio.insurance.service.entity.DrivingLicenseResponseBody;
import cn.wghtstudio.insurance.service.entity.IdCardResponseBody;

import java.io.IOException;

public interface OcrInfoService {
    IdCardResponseBody idCardInfoService(String url) throws IOException, OCRException;

    BusinessLicenseResponseBody businessInfoService(String url) throws IOException, OCRException;

    DrivingLicenseResponseBody drivingInfoService(String url) throws IOException, OCRException;

    CertificateResponseBody certificate(String url) throws IOException, OCRException;
}
