package cn.wghtstudio.insurance.dao.entity;

import lombok.Data;

import java.util.List;

@Data
public class Role {
    private int id;
    private String name;
    private int value;
    private List<Route> routeList;
}
