package cn.wghtstudio.insurance.controller.entity;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CreateInsuranceRequestBody {
    @NotEmpty
    private String startTime;

    @NotEmpty
    private int paymentId;

    @NotEmpty
    private int carTypeId;

    private Integer idCardId;

    private Integer businessLicenseId;

    private Integer drivingLicenseId;

    private Integer certificateId;

    private Integer otherFileId;
}

