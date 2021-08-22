package cn.wghtstudio.insurance.dao.repository;

import cn.wghtstudio.insurance.dao.entity.IdCard;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IdCardRepository {
    Integer createIdCard(IdCard idCard);
}
