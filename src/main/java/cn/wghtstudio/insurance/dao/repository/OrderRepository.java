package cn.wghtstudio.insurance.dao.repository;

import cn.wghtstudio.insurance.dao.entity.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderRepository {
    List<Order> getOrderByUser(Integer userId);

    int getOrderCount(Integer userId);
}
