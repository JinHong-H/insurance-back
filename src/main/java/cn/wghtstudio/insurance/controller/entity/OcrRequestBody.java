package cn.wghtstudio.insurance.controller.entity;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class OcrRequestBody {
    @NotEmpty
    private String imgUrl;
}
