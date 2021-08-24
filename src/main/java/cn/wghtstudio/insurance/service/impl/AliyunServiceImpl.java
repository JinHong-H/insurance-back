package cn.wghtstudio.insurance.service.impl;

import cn.wghtstudio.insurance.config.AliyunConfig;
import cn.wghtstudio.insurance.service.AliyunService;
import cn.wghtstudio.insurance.service.entity.STSTokenResponseToken;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AliyunServiceImpl implements AliyunService {
    @Resource
    private AliyunConfig aliyunConfig;

    @Override
    public STSTokenResponseToken getSTSToken() throws ClientException {
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
        final AssumeRoleResponse.Credentials credentials = response.getCredentials();

        return STSTokenResponseToken.builder().
                accessKey(credentials.getAccessKeyId()).
                accessKeySecret(credentials.getAccessKeySecret()).
                securityToken(credentials.getSecurityToken()).
                build();
    }
}
