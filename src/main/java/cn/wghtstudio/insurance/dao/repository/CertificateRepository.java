package cn.wghtstudio.insurance.dao.repository;

import cn.wghtstudio.insurance.dao.entity.Certificate;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CertificateRepository {
    void createCertificate(Certificate certificate);

    void updateCertificate(Certificate certificate);
}
