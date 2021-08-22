package cn.wghtstudio.insurance.service;

import cn.wghtstudio.insurance.service.entity.BusinessLicenseResponseBody;
import cn.wghtstudio.insurance.service.entity.CertificateResponseBody;
import cn.wghtstudio.insurance.service.entity.DrivingLicenseResponseBody;
import cn.wghtstudio.insurance.service.entity.IdCardResponseBody;

import java.io.IOException;

public interface OcrInfoService {
    IdCardResponseBody idCardInfoService(String url) throws IOException;

    BusinessLicenseResponseBody businessInfoService(String url) throws IOException;

    DrivingLicenseResponseBody drivingInfoService(String url) throws IOException;

    CertificateResponseBody certificate(String url) throws IOException;
}
