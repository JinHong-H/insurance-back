package cn.wghtstudio.insurance.service;

import cn.wghtstudio.insurance.exception.JsonPraseErrorException;
import cn.wghtstudio.insurance.exception.OcrTokenGetErrorException;
import cn.wghtstudio.insurance.service.entity.*;
import jdk.security.jarsigner.JarSignerException;

public interface OcrInfoService {
	InsuranceDocumentResponseBody GetInsuranceDocumentService(String imgUrl) throws OcrTokenGetErrorException, JsonPraseErrorException;
	IdCardResponseBody GetIdCardInfoService(String imgUrl)throws OcrTokenGetErrorException,JsonPraseErrorException;
	BussyLicenseResponseBdoy GetBussyLicenseInfoService(String imgUrl) throws OcrTokenGetErrorException,JsonPraseErrorException;
	DriveLicenseResponseBody GetDriveLicenseInfoService(String imgUrl) throws OcrTokenGetErrorException,JsonPraseErrorException;
	CertificateResponseBody GetCertificateInfoService(String imgUrl) throws OcrTokenGetErrorException,JsonPraseErrorException;
	
}
