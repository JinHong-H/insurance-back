package cn.wghtstudio.insurance.controller.entity;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateInsuranceRequestBody {
    @Data
    public static class IdCardRequestBody {
        @NotNull
        private Integer id;
        private String name;
        private String number;
        private String address;
    }

    @Data
    public static class BusinessLicenseRequestBody {
        @NotNull
        private Integer id;
        private String name;
        private String number;
        private String address;
    }

    @Data
    public static class DrivingLicenseRequestBody {
        @NotNull
        private Integer id;
        private String plate;
        private String engine;
        private String frame;
        private String type;
    }

    @Data
    public static class CertificateRequestBody {
        @NotNull
        private Integer id;
        private String type;
        private String engine;
        private String frame;
    }

    @NotEmpty
    private String startTime;

    @NotNull
    private Integer paymentId;

    @NotNull
    private int carTypeId;

    private IdCardRequestBody idCard;

    private BusinessLicenseRequestBody businessLicense;

    private DrivingLicenseRequestBody drivingLicense;

    private CertificateRequestBody certificate;

    private List<Integer> otherFileId;
}

