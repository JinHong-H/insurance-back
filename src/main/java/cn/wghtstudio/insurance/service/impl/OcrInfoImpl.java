package cn.wghtstudio.insurance.service.impl;

import cn.wghtstudio.insurance.dao.entity.BusinessLicense;
import cn.wghtstudio.insurance.dao.entity.IdCard;
import cn.wghtstudio.insurance.dao.repository.BusinessLicenseRepository;
import cn.wghtstudio.insurance.dao.repository.IdCardRepository;
import cn.wghtstudio.insurance.service.OcrInfoService;
import cn.wghtstudio.insurance.service.entity.BusinessLicenseResponseBody;
import cn.wghtstudio.insurance.service.entity.CertificateResponseBody;
import cn.wghtstudio.insurance.service.entity.DrivingLicenseResponseBody;
import cn.wghtstudio.insurance.service.entity.IdCardResponseBody;
import cn.wghtstudio.insurance.util.ocr.BusinessResponse;
import cn.wghtstudio.insurance.util.ocr.GetOcrToken;
import cn.wghtstudio.insurance.util.ocr.IdCardResponse;
import cn.wghtstudio.insurance.util.ocr.OcrInfoGetter;
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

    @Override
    public IdCardResponseBody idCardInfoService(String url) throws IOException {
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
    public BusinessLicenseResponseBody businessInfoService(String url) throws IOException {
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
    public DrivingLicenseResponseBody drivingInfoService(String url) throws IOException {
        return null;
    }

    @Override
    public CertificateResponseBody certificate(String url) throws IOException {
        return null;
    }
}
