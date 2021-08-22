package cn.wghtstudio.insurance.controller.entity;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class CreateInsuranceRequestBody {
    @Data
    public static class IdCardRequestBody {
        @NotEmpty
        private Integer id;
        private String name;
        private String number;
        private String address;
    }

    @Data
    public static class BusinessLicenseRequestBody {
        @NotEmpty
        private Integer id;
        private String name;
        private String number;
        private String address;
    }

    @Data
    public static class DrivingLicenseRequestBody {
        @NotEmpty
        private int id;
        private String plate;
        private String engine;
        private String frame;
        private String type;
    }

    @Data
    public static class CertificateRequestBody {
        @NotEmpty
        private Integer id;
        private String engine;
        private String frame;
    }

    @NotEmpty
    private String startTime;

    @NotEmpty
    private int paymentId;

    @NotEmpty
    private int carTypeId;

    private IdCardRequestBody idCard;

    private BusinessLicenseRequestBody businessLicense;

    private DrivingLicenseRequestBody drivingLicense;

    private CertificateRequestBody certificate;

    private List<Integer> otherFileId;
}

