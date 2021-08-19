package cn.wghtstudio.insurance.dao.repository;

import cn.wghtstudio.insurance.dao.entity.Payment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentRepository {
    Payment getPaymentById(int id);
}
