package cn.wghtstudio.insurance.util;

import cn.wghtstudio.insurance.config.AliyunConfig;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.InputStream;

@Component
public class AliyunUtil {
    private static AliyunConfig aliyunConfig;

    @Resource
    public void setAliyunConfig(AliyunConfig config) {
        aliyunConfig = config;
    }


    public static AssumeRoleResponse.Credentials getAliyunSTSToken() throws ClientException {
        String endpoint = "sts.cn-hangzhou.aliyuncs.com";
        String roleSessionName = "OSSPutObject";
        String policy = "{\n" +
                "    \"Version\": \"1\", \n" +
                "    \"Statement\": [\n" +
                "        {\n" +
                "            \"Action\": [\n" +
                "                \"oss:PutObject\"\n" +
                "            ], \n" +
                "            \"Resource\": [\n" +
                "                \"acs:oss:*:*:versicherung/*\" \n" +
                "            ], \n" +
                "            \"Effect\": \"Allow\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        String regionId = "";

        // 添加endpoint。
        DefaultProfile.addEndpoint(regionId, "Sts", endpoint);

        // 构造default profile。
        IClientProfile profile = DefaultProfile.getProfile(regionId, aliyunConfig.getAccessKeyID(), aliyunConfig.getAccessKeySecret());

        // 构造client。
        DefaultAcsClient client = new DefaultAcsClient(profile);
        final AssumeRoleRequest request = new AssumeRoleRequest();
        request.setSysMethod(MethodType.POST);
        request.setRoleArn(aliyunConfig.getArn());
        request.setRoleSessionName(roleSessionName);
        request.setPolicy(policy); // 如果policy为空，则用户将获得该角色下所有权限。
        request.setDurationSeconds(3600L); // 设置临时访问凭证的有效时间为3600秒。

        final AssumeRoleResponse response = client.getAcsResponse(request);
        return response.getCredentials();
    }

    public static void putObject(String path, InputStream source) {
        String endpoint = "https://oss-cn-beijing.aliyuncs.com";
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, aliyunConfig.getAccessKeyID(), aliyunConfig.getAccessKeySecret());

        ossClient.putObject("versicherung", path, source);

        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
