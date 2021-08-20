package cn.wghtstudio.insurance.dao.repository;

import cn.wghtstudio.insurance.dao.entity.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderRepository {
    List<Order> getOrderByUser(Map<String, Object> params);

    int getOrderCount(Map<String, Object> params);
}
