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
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private PolicyRepository policyRepository;
    
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
    public CertificateResponseBody certificate(String url) throws IOException, OCRException {
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
    
    public static String getMatcher(String regex, String source) {
        String result = "";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }
    
    
    public InsurancepolicyResponseBody insurance(String url) throws IOException, OCRException {
        final String token = getOcrToken.getAuthToken();
        final InsurancepolicyResponse response = infoGetter.vehicleInsurance(url, token);
        final InsurancepolicyResponse.WordsResult wordsResult = response.getWordsResult();
        String number = null, plateNumber = null;
        String pattern1 = "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{1}(([A-HJ-Z]{1}[A-HJ-NP-Z0-9]{5})|([A-HJ-Z]{1}(([DF]{1}[A-HJ-NP-Z0-9]{1}[0-9]{4})|([0-9]{5}[DF]{1})))|([A-HJ-Z]{1}[A-D0-9]{1}[0-9]{3}警)))|([0-9]{6}使)|((([沪粤川云桂鄂陕蒙藏黑辽渝]{1}A)|鲁B|闽D|蒙E|蒙H)[0-9]{4}领)|(WJ[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼·•]{1}[0-9]{4}[TDSHBXJ0-9]{1})|([VKHBSLJNGCE]{1}[A-DJ-PR-TVY]{1}[0-9]{5})";
        String pattern2 = "[A-Z]{4}[0-9]{16}";
        for (String word : wordsResult.getWords()) {
            String tmpWord = getMatcher(pattern2, word);
            if (!Objects.equals(tmpWord, "")) {
                number = tmpWord;
            }
            tmpWord = getMatcher(pattern1, word);
            if (!Objects.equals(tmpWord, "")) {
                plateNumber = tmpWord;
            }
        }
        
        Policy policy = Policy.builder().
                url(url).
                number(plateNumber).
                build();
        
        policyRepository.createPolicy(policy);
        
        return InsurancepolicyResponseBody.builder().
                number(number).
                plateNumber(plateNumber).
                build();
    }
}
