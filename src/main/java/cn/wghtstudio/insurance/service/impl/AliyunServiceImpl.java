package cn.wghtstudio.insurance.service.impl;

import cn.wghtstudio.insurance.service.AliyunService;
import cn.wghtstudio.insurance.service.entity.STSTokenResponseToken;
import cn.wghtstudio.insurance.util.AliyunUtil;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.stereotype.Component;

@Component
public class AliyunServiceImpl implements AliyunService {
    @Override
    public STSTokenResponseToken getSTSToken() throws ClientException {
        final AssumeRoleResponse.Credentials credentials = AliyunUtil.getAliyunSTSToken();

        return STSTokenResponseToken.builder().
                accessKey(credentials.getAccessKeyId()).
                accessKeySecret(credentials.getAccessKeySecret()).
                securityToken(credentials.getSecurityToken()).
                build();
    }
}
