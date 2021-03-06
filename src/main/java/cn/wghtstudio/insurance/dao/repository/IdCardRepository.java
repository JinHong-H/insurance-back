package cn.wghtstudio.insurance.dao.repository;

import cn.wghtstudio.insurance.dao.entity.IdCard;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IdCardRepository {
    void createIdCard(IdCard idCard);

    void updateIdCard(IdCard idCard);

    void deleteIdCard(int id);
}
