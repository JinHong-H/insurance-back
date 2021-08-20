package cn.wghtstudio.insurance.util;

import cn.wghtstudio.insurance.config.OcrConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class OcrTokenGet {
    @Resource
    private OcrConfig ocrConfig;


    /**
     * 获取API访问token
     * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
     *
     * @return assess_token 示例：
     * "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
     */
    public String getAuth() {
        // 官网获取的 API Key 更新为你注册的
        String clientId = ocrConfig.getApiKey();
        // 官网获取的 Secret Key 更新为你注册的
        String clientSecret = ocrConfig.getSecretKey();
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + clientId
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + clientSecret;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 遍历所有的响应头字段
//			for (String key : map.keySet()) {
//				System.err.println(key + "--->" + map.get(key));
//			}
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }

//            JsonMapper jsonObject = new JsonMapper(result.toString());
//            return jsonObject.getString("access_token");

            return "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
