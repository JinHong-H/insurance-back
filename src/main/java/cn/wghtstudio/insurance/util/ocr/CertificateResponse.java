package cn.wghtstudio.insurance.util.ocr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CertificateResponse {
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WordsResult {
        @JsonProperty("ManufactureDate")
        private String ManufactureDate;
        @JsonProperty("CarColor")
        private String CarColor;
        @JsonProperty("LimitPassenger")
        private String LimitPassenger;
        @JsonProperty("EngineType")
        private String EngineType;
        @JsonProperty("TotalWeight")
        private String TotalWeight;
        @JsonProperty("Power")
        private String Power;
        @JsonProperty("CertificationNo")
        private String CertificationNo;
        @JsonProperty("FuelType")
        private String FuelType;
        @JsonProperty("Manufacturer")
        private String Manufacturer;
        @JsonProperty("SteeringType")
        private String SteeringType;
        @JsonProperty("Wheelbase")
        private String Wheelbase;
        @JsonProperty("SpeedLimit")
        private String SpeedLimit;
        @JsonProperty("EngineNo")
        private String EngineNo;
        @JsonProperty("SaddleMass")
        private String SaddleMass;
        @JsonProperty("AxleNum")
        private String AxleNum;
        @JsonProperty("CarModel")
        private String CarModel;
        @JsonProperty("VinNo")
        private String VinNo;
        @JsonProperty("CarBrand")
        private String CarBrand;
        @JsonProperty("EmissionStandard")
        private String EmissionStandard;
        @JsonProperty("Displacement")
        private String Displacement;
        @JsonProperty("CertificateDate")
        private String CertificateDate;
        @JsonProperty("CarName")
        private String CarName;
        @JsonProperty("TyreNum")
        private String TyreNum;
        @JsonProperty("ChassisID")
        private String ChassisID;
        @JsonProperty("ChassisModel")
        private String ChassisModel;
        @JsonProperty("SeatingCapacity")
        private String SeatingCapacity;
        @JsonProperty("QualifySeal")
        private String QualifySeal;
        @JsonProperty("CGSSeal")
        private String CGSSeal;
    }

    @JsonProperty("log_id")
    private String logId;
    @JsonProperty("wordsResult_num")
    private int wordsResultNum;
    @JsonProperty("direction")
    private int direction;
    @JsonProperty("words_result")
    private WordsResult wordsResult;
    @JsonProperty("error_code")
    private Integer errorCode;
}
