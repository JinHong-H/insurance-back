package cn.wghtstudio.insurance.dao.repository;

import cn.wghtstudio.insurance.dao.entity.CarType;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CarTypeRepository {
    CarType getCarTypeById(int id);
}
