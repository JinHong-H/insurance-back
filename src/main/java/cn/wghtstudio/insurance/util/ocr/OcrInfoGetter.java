package cn.wghtstudio.insurance.util.ocr;

import cn.wghtstudio.insurance.util.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class OcrInfoGetter {
    private final OkHttpClient client = new OkHttpClient();

    public IdCardResponse idCard(String imgUrl, String accessToken) throws IOException {
        // 请求url
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse("https://aip.baidubce.com/rest/2.0/ocr/v1/idcard")).
                newBuilder();
        HttpUrl url = urlBuilder.addQueryParameter("access_token", accessToken).build();

        RequestBody requestBody = new FormBody.Builder().
                add("url", imgUrl).
                add("id_card_side", "front").
                build();

        final Request request = new Request.Builder().
                url(url).
                header("Content-Type", "application/x-www-form-urlencoded").
                post(requestBody).
                build();
        final Call call = client.newCall(request);

        final Response response = call.execute();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(Objects.requireNonNull(response.body()).string(), IdCardResponse.class);
    }

    public String businessLicense(String imgUrl, String token) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/business_license";
        try {
            String param = "url=" + imgUrl;

            return HttpUtil.post(url, token, param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
