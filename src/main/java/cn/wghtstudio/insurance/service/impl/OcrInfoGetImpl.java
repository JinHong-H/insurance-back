package cn.wghtstudio.insurance.service.impl;

import cn.wghtstudio.insurance.exception.JsonParseErrorException;
import cn.wghtstudio.insurance.exception.OcrTokenGetErrorException;
import cn.wghtstudio.insurance.service.OcrInfoService;
import cn.wghtstudio.insurance.service.entity.*;
import cn.wghtstudio.insurance.util.OcrInfoGet;
import cn.wghtstudio.insurance.util.OcrTokenGet;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class OcrInfoGetImpl implements OcrInfoService {
    @Resource
    private OcrInfoGet ocrInfoGet;

    @Resource
    private OcrTokenGet ocrTokenGet;

    @Override
    public InsuranceDocumentResponseBody GetInsuranceDocumentService(String imgUrl) throws OcrTokenGetErrorException, JsonParseErrorException {
        String token = ocrTokenGet.getAuth();
        if (token == null || token.length() == 0) {
            throw new OcrTokenGetErrorException();
        }
        try {
            String result = ocrInfoGet.insuranceDocuments(imgUrl, token);
            if (result == null) {
                return null;
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(result, InsuranceDocumentResponseBody.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new JsonParseErrorException();
        }
    }

    @Override
    public IdCardResponseBody GetIdCardInfoService(String imgUrl) throws OcrTokenGetErrorException, JsonParseErrorException {
        String token = ocrTokenGet.getAuth();
        if (token == null || token.length() == 0) {
            throw new OcrTokenGetErrorException();
        }
        try {
            String result = ocrInfoGet.idcard(imgUrl, token);
            if (result == null) {
                return null;
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(result, IdCardResponseBody.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new JsonParseErrorException();
        }
    }

    @Override
    public BussyLicenseResponseBdoy GetBussyLicenseInfoService(String imgUrl) throws OcrTokenGetErrorException, JsonParseErrorException {
        String token = ocrTokenGet.getAuth();
        if (token == null || token.length() == 0) {
            throw new OcrTokenGetErrorException();
        }
        try {
            String result = ocrInfoGet.businessLicense(imgUrl, token);
            if (result == null) {
                return null;
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(result, BussyLicenseResponseBdoy.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new JsonParseErrorException();
        }
    }

    public DriveLicenseResponseBody GetDriveLicenseInfoService(String imgUrl) throws OcrTokenGetErrorException, JsonParseErrorException {
        String token = ocrTokenGet.getAuth();
        if (token == null || token.length() == 0) {
            throw new OcrTokenGetErrorException();
        }
        try {
            String result = ocrInfoGet.vehicleLicense(imgUrl, token);
            if (result == null) {
                return null;
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(result, DriveLicenseResponseBody.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new JsonParseErrorException();
        }
    }

    public CertificateResponseBody GetCertificateInfoService(String imgUrl) throws OcrTokenGetErrorException, JsonParseErrorException {
        String token = ocrTokenGet.getAuth();
        if (token == null || token.length() == 0) {
            throw new OcrTokenGetErrorException();
        }
        try {
            String result = ocrInfoGet.vehicleCertificate(imgUrl, token);
            if (result == null) {
                return null;
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(result, CertificateResponseBody.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new JsonParseErrorException();
        }
    }

}
