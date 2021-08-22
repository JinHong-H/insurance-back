package cn.wghtstudio.insurance.util.ocr;

import cn.wghtstudio.insurance.config.OcrConfig;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Objects;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class OcrTokenResponse {
    @JsonProperty(value = "access_token")
    private String accessToken;
}

@Component
public class GetOcrToken {
    @Resource
    private OcrConfig ocrConfig;

    private final OkHttpClient client = new OkHttpClient();

    /**
     * 获取API访问token
     * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
     *
     * @return assess_token 示例：
     * "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
     */
    public String getAuthToken() throws IOException {
        // 官网获取的 API Key 更新为你注册的
        String clientId = ocrConfig.getApiKey();

        // 官网获取的 Secret Key 更新为你注册的
        String clientSecret = ocrConfig.getSecretKey();

        // 获取token地址
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse("https://aip.baidubce.com/oauth/2.0/token")).
                newBuilder();
        HttpUrl url = urlBuilder.addQueryParameter("grant_type", "client_credentials").
                addQueryParameter("client_id", clientId).
                addQueryParameter("client_secret", clientSecret).
                build();

        final Request request = new Request.Builder().url(url).build();
        final Call call = client.newCall(request);

        final Response response = call.execute();
        ObjectMapper objectMapper = new ObjectMapper();
        OcrTokenResponse tokenResponse = objectMapper.readValue(Objects.requireNonNull(response.body()).string(), OcrTokenResponse.class);

        return tokenResponse.getAccessToken();
    }
}
