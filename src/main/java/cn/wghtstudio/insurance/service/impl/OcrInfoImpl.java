package cn.wghtstudio.insurance.service.impl;

import cn.wghtstudio.insurance.dao.entity.*;
import cn.wghtstudio.insurance.dao.repository.*;
import cn.wghtstudio.insurance.exception.OCRException;
import cn.wghtstudio.insurance.service.OcrInfoService;
import cn.wghtstudio.insurance.service.entity.*;
import cn.wghtstudio.insurance.util.ocr.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

@Component
public class OcrInfoImpl implements OcrInfoService {
    @Resource
    private OcrInfoGetter infoGetter;

    @Resource
    private GetOcrToken getOcrToken;

    @Resource
    private IdCardRepository idCardRepository;

    @Resource
    private BusinessLicenseRepository businessLicenseRepository;

    @Resource
    private DrivingLicenseRepository drivingLicenseRepository;

    @Resource
    private CertificateRepository certificateRepository;

    @Resource
    private OtherFileRepository otherFileRepository;

    @Override
    public IdCardResponseBody idCardInfoService(String url) throws IOException, OCRException {
        final String token = getOcrToken.getAuthToken();
        final IdCardResponse response = infoGetter.idCard(url, token);

        final IdCardResponse.WordsResult wordsResult = response.getWordsResult();
        IdCard idCard = IdCard.builder().
                url(url).
                name(wordsResult.getName().getWords()).
                address(wordsResult.getAddress().getWords()).
                number(wordsResult.getNumber().getWords()).
                build();

        idCardRepository.createIdCard(idCard);

        return IdCardResponseBody.builder().
                id(idCard.getId()).
                name(idCard.getName()).
                address(idCard.getAddress()).
                number(idCard.getNumber()).
                build();
    }

    @Override
    public BusinessLicenseResponseBody businessInfoService(String url) throws IOException, OCRException {
        final String token = getOcrToken.getAuthToken();
        final BusinessResponse response = infoGetter.businessLicense(url, token);

        final BusinessResponse.WordsResult wordsResult = response.getWordsResult();
        BusinessLicense businessLicense = BusinessLicense.builder().
                url(url).
                name(wordsResult.getCompanyName().getWords()).
                address(wordsResult.getAddress().getWords()).
                number(wordsResult.getCreditCode().getWords()).
                build();

        businessLicenseRepository.createBusinessLicense(businessLicense);

        return BusinessLicenseResponseBody.builder().
                id(businessLicense.getId()).
                name(businessLicense.getName()).
                address(businessLicense.getAddress()).
                number(businessLicense.getNumber()).
                build();
    }

    @Override
    public DrivingLicenseResponseBody drivingInfoService(String url) throws IOException, OCRException {
        final String token = getOcrToken.getAuthToken();
        final DrivingLicenseResponse response = infoGetter.vehicleLicense(url, token);

        final DrivingLicenseResponse.WordsResult wordsResult = response.getWordsResult();
        DrivingLicense drivingLicense = DrivingLicense.builder().
                url(url).
                owner(wordsResult.getOwner().getWords()).
                plateNumber(wordsResult.getPlate().getWords()).
                engine(wordsResult.getEngine().getWords()).
                frame(wordsResult.getCarVerify().getWords()).
                type(wordsResult.getModel().getWords()).
                build();

        drivingLicenseRepository.createDrivingLicense(drivingLicense);

        return DrivingLicenseResponseBody.builder().
                id(drivingLicense.getId()).
                plate(drivingLicense.getPlateNumber()).
                engine(drivingLicense.getEngine()).
                frame(drivingLicense.getFrame()).
                type(drivingLicense.getType()).
                build();
    }

    @Override
    public CertificateResponseBody certificateInfoService(String url) throws IOException, OCRException {
        final String token = getOcrToken.getAuthToken();
        final CertificateResponse response = infoGetter.vehicleCertificate(url, token);

        final CertificateResponse.WordsResult wordsResult = response.getWordsResult();
        Certificate certificate = Certificate.builder().
                url(url).
                engine(wordsResult.getEngineNo()).
                frame(wordsResult.getVinNo()).
                build();

        certificateRepository.createCertificate(certificate);

        return CertificateResponseBody.builder().
                id(certificate.getId()).
                engine(certificate.getEngine()).
                frame(certificate.getFrame()).
                build();
    }

    @Override
    public OtherFileResponseBody otherFileService(String url) {
        OtherFile otherFile = OtherFile.builder().
                url(url).
                build();

        otherFileRepository.createOtherFile(otherFile);

        return OtherFileResponseBody.builder().
                id(otherFile.getId()).
                build();
    }
}
