package cn.wghtstudio.insurance.util.ocr;

import cn.wghtstudio.insurance.exception.OCRException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class OcrInfoGetter {
    private static final OkHttpClient client = new OkHttpClient();

    private static HttpUrl getUrl(String base, String token) {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(base)).
                newBuilder();
        return urlBuilder.addQueryParameter("access_token", token).build();
    }

    private static RequestBody getRequestBody(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();

        for (String key : params.keySet()) {
            builder.add(key, params.get(key));
        }

        return builder.build();
    }

    private static String execRequest(HttpUrl url, RequestBody requestBody) throws IOException {
        final Request request = new Request.Builder().
                url(url).
                header("Content-Type", "application/x-www-form-urlencoded").
                post(requestBody).
                build();
        final Call call = client.newCall(request);
        final Response response = call.execute();

        return Objects.requireNonNull(response.body()).string();
    }

    public static IdCardResponse idCard(String imgUrl, String token) throws IOException, OCRException {
        // 请求url
        final HttpUrl url = getUrl("https://aip.baidubce.com/rest/2.0/ocr/v1/idcard", token);
        RequestBody requestBody = getRequestBody(Map.of("url", imgUrl, "id_card_side", "front"));

        String response = execRequest(url, requestBody);
        ObjectMapper objectMapper = new ObjectMapper();
        IdCardResponse res = objectMapper.readValue(response, IdCardResponse.class);
        if (res.getErrorCode() != null) {
            throw new OCRException();
        }

        return res;
    }

    public static BusinessResponse businessLicense(String imgUrl, String token) throws IOException, OCRException {
        // 请求url
        final HttpUrl url = getUrl("https://aip.baidubce.com/rest/2.0/ocr/v1/business_license", token);
        RequestBody requestBody = getRequestBody(Map.of("url", imgUrl));

        String response = execRequest(url, requestBody);
        ObjectMapper objectMapper = new ObjectMapper();
        BusinessResponse res = objectMapper.readValue(response, BusinessResponse.class);
        if (res.getErrorCode() != null) {
            throw new OCRException();
        }

        return res;
    }

    public static DrivingLicenseResponse vehicleLicense(String imgUrl, String token) throws IOException, OCRException {
        // 请求url
        final HttpUrl url = getUrl("https://aip.baidubce.com/rest/2.0/ocr/v1/vehicle_license", token);
        RequestBody requestBody = getRequestBody(Map.of("url", imgUrl));

        String response = execRequest(url, requestBody);
        ObjectMapper objectMapper = new ObjectMapper();
        DrivingLicenseResponse res = objectMapper.readValue(response, DrivingLicenseResponse.class);
        if (res.getErrorCode() != null) {
            throw new OCRException();
        }

        return res;
    }

    public static CertificateResponse vehicleCertificate(String imgUrl, String token) throws IOException, OCRException {
        // 请求url
        final HttpUrl url = getUrl("https://aip.baidubce.com/rest/2.0/ocr/v1/vehicle_certificate", token);
        RequestBody requestBody = getRequestBody(Map.of("url", imgUrl));

        String response = execRequest(url, requestBody);
        ObjectMapper objectMapper = new ObjectMapper();
        CertificateResponse res = objectMapper.readValue(response, CertificateResponse.class);
        if (res.getErrorCode() != null) {
            throw new OCRException();
        }

        return res;
    }

    public static InsurancePolicyResponse vehicleInsurance(String pdfFile, String token) throws IOException, OCRException {
        final HttpUrl url = getUrl("https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic", token);
        RequestBody requestBody = getRequestBody(Map.of("pdf_file", pdfFile));

        String response = execRequest(url, requestBody);
        ObjectMapper objectMapper = new ObjectMapper();
        InsurancePolicyResponse res = objectMapper.readValue(response, InsurancePolicyResponse.class);
        if (res.getErrorCode() != null) {
            throw new OCRException();
        }

        return res;
    }
}
