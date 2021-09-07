package cn.wghtstudio.insurance.service.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetOrderDetailResponseBody {
    @Data
    @Builder
    public static class IdCard {
        private String url;
        private String address;
        private String number;
    }

    @Data
    @Builder
    public static class Business {
        private String url;
        private String address;
        private String number;
    }

    @Data
    @Builder
    public static class Driving {
        private String url;
        private String frame;
        private String engine;
        private String type;
    }

    @Data
    @Builder
    public static class Certificate {
        private String url;
        private String type;
        private String frame;
        private String engine;
    }

    @Data
    @Builder
    public static class Policy {
        private String url;
        private String name;
        private String number;
    }

    private int id;
    private String owner;
    private String licensePlate;
    private String startTime;
    private String carType;
    private String payType;

    private IdCard idCard;
    private Business business;
    private Driving driving;
    private Certificate certificate;
    private Policy policy;
}
