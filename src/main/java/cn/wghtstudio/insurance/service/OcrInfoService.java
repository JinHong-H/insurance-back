package cn.wghtstudio.insurance.service;

import cn.wghtstudio.insurance.exception.JsonParseErrorException;
import cn.wghtstudio.insurance.exception.OcrTokenGetErrorException;
import cn.wghtstudio.insurance.service.entity.*;

public interface OcrInfoService {
    InsuranceDocumentResponseBody GetInsuranceDocumentService(String imgUrl) throws OcrTokenGetErrorException, JsonParseErrorException;

    IdCardResponseBody GetIdCardInfoService(String imgUrl) throws OcrTokenGetErrorException, JsonParseErrorException;

    BussyLicenseResponseBdoy GetBussyLicenseInfoService(String imgUrl) throws OcrTokenGetErrorException, JsonParseErrorException;

    DriveLicenseResponseBody GetDriveLicenseInfoService(String imgUrl) throws OcrTokenGetErrorException, JsonParseErrorException;

    CertificateResponseBody GetCertificateInfoService(String imgUrl) throws OcrTokenGetErrorException, JsonParseErrorException;
}
