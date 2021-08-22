package cn.wghtstudio.insurance.service;

import cn.wghtstudio.insurance.exception.GetOcrTokenErrorException;
import cn.wghtstudio.insurance.service.entity.IdCardResponseBody;

import java.io.IOException;

public interface OcrInfoService {
    IdCardResponseBody idCardInfoService(String url) throws GetOcrTokenErrorException, IOException;
}
