package cn.wghtstudio.insurance.service;

import cn.wghtstudio.insurance.service.entity.STSTokenResponseToken;
import com.aliyuncs.exceptions.ClientException;

public interface AliyunService {
    STSTokenResponseToken getSTSToken() throws ClientException;
}
