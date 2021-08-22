package cn.wghtstudio.insurance.util.ocr;

import cn.wghtstudio.insurance.util.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Component
public class OcrInfoGetter {
    private final OkHttpClient client = new OkHttpClient();

    private HttpUrl getUrl(String base, String token) {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(base)).
                newBuilder();
        return urlBuilder.addQueryParameter("access_token", token).build();
    }

    private RequestBody getRequestBody(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();

        for (String key : params.keySet()) {
            builder.add(key, params.get(key));
        }

        return builder.build();
    }

    private String execRequest(HttpUrl url, RequestBody requestBody) throws IOException {
        final Request request = new Request.Builder().
                url(url).
                header("Content-Type", "application/x-www-form-urlencoded").
                post(requestBody).
                build();
        final Call call = client.newCall(request);
        final Response response = call.execute();

        return Objects.requireNonNull(response.body()).string();
    }

    public IdCardResponse idCard(String imgUrl, String token) throws IOException {
        // 请求url
        final HttpUrl url = getUrl("https://aip.baidubce.com/rest/2.0/ocr/v1/idcard", token);
        RequestBody requestBody = getRequestBody(Map.of("url", imgUrl, "id_card_side", "front"));

        String response = execRequest(url, requestBody);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response, IdCardResponse.class);
    }

    public BusinessResponse businessLicense(String imgUrl, String token) throws IOException {
        // 请求url
        final HttpUrl url = getUrl("https://aip.baidubce.com/rest/2.0/ocr/v1/business_license", token);
        RequestBody requestBody = getRequestBody(Map.of("url", imgUrl));

        String response = execRequest(url, requestBody);
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(response);
        return objectMapper.readValue(response, BusinessResponse.class);
    }

    public String vehicleLicense(String imgUrl, String token) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/vehicle_license";
        try {
            String param = "url=" + imgUrl;
            return HttpUtil.post(url, token, param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String vehicleCertificate(String imgUrl, String token) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/vehicle_certificate";
        try {
            String param = "url=" + imgUrl;
            return HttpUtil.post(url, token, param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
