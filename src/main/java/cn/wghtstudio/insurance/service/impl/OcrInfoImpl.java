package cn.wghtstudio.insurance.service.impl;

import cn.wghtstudio.insurance.dao.entity.IdCard;
import cn.wghtstudio.insurance.dao.repository.IdCardRepository;
import cn.wghtstudio.insurance.service.OcrInfoService;
import cn.wghtstudio.insurance.service.entity.IdCardResponseBody;
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
//    @Resource
//    private OcrInfoGet ocrInfoGet;
//
//    @Resource
//    private OcrTokenGet ocrTokenGet;
//
//    @Override
//    public InsuranceDocumentResponseBody GetInsuranceDocumentService(String imgUrl) throws OcrTokenGetErrorException, JsonParseErrorException {
//        String token = ocrTokenGet.getAuth();
//        if (token == null || token.length() == 0) {
//            throw new OcrTokenGetErrorException();
//        }
//        try {
//            String result = ocrInfoGet.insuranceDocuments(imgUrl, token);
//            if (result == null) {
//                return null;
//            }
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.readValue(result, InsuranceDocumentResponseBody.class);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            throw new JsonParseErrorException();
//        }
//    }
//
//    @Override
//    public IdCardResponseBody GetIdCardInfoService(String imgUrl) throws OcrTokenGetErrorException, JsonParseErrorException {
//        String token = ocrTokenGet.getAuth();
//        if (token == null || token.length() == 0) {
//            throw new OcrTokenGetErrorException();
//        }
//        try {
//            String result = ocrInfoGet.idcard(imgUrl, token);
//            if (result == null) {
//                return null;
//            }
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.readValue(result, IdCardResponseBody.class);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            throw new JsonParseErrorException();
//        }
//    }
//
//    @Override
//    public BussyLicenseResponseBdoy GetBussyLicenseInfoService(String imgUrl) throws OcrTokenGetErrorException, JsonParseErrorException {
//        String token = ocrTokenGet.getAuth();
//        if (token == null || token.length() == 0) {
//            throw new OcrTokenGetErrorException();
//        }
//        try {
//            String result = ocrInfoGet.businessLicense(imgUrl, token);
//            if (result == null) {
//                return null;
//            }
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.readValue(result, BussyLicenseResponseBdoy.class);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            throw new JsonParseErrorException();
//        }
//    }
//
//    public DriveLicenseResponseBody GetDriveLicenseInfoService(String imgUrl) throws OcrTokenGetErrorException, JsonParseErrorException {
//        String token = ocrTokenGet.getAuth();
//        if (token == null || token.length() == 0) {
//            throw new OcrTokenGetErrorException();
//        }
//        try {
//            String result = ocrInfoGet.vehicleLicense(imgUrl, token);
//            if (result == null) {
//                return null;
//            }
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.readValue(result, DriveLicenseResponseBody.class);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            throw new JsonParseErrorException();
//        }
//    }
//
//    public CertificateResponseBody GetCertificateInfoService(String imgUrl) throws OcrTokenGetErrorException, JsonParseErrorException {
//        String token = ocrTokenGet.getAuth();
//        if (token == null || token.length() == 0) {
//            throw new OcrTokenGetErrorException();
//        }
//        try {
//            String result = ocrInfoGet.vehicleCertificate(imgUrl, token);
//            if (result == null) {
//                return null;
//            }
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.readValue(result, CertificateResponseBody.class);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            throw new JsonParseErrorException();
//        }
//    }

}
