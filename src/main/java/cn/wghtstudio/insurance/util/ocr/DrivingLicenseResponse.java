package cn.wghtstudio.insurance.util.ocr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrivingLicenseResponse {
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WordsResult {
        @JsonProperty("车辆识别代号")
        private Info carVerify;
        @JsonProperty("住址")
        private Info address;
        @JsonProperty("发证日期")
        private Info gotTime;
        @JsonProperty("发证单位")
        private Info gotCompany;
        @JsonProperty("品牌型号")
        private Info brand;
        @JsonProperty("车辆类型")
        private Info model;
        @JsonProperty("所有人")
        private Info owner;
        @JsonProperty("使用性质")
        private Info quality;
        @JsonProperty("发动机号码")
        private Info engine;
        @JsonProperty("号牌号码")
        private Info plate;
        @JsonProperty("注册日期")
        private Info registration;
    }

    @JsonProperty("words_result")
    private WordsResult wordsResult;
    @JsonProperty("log_id")
    private String logId;
    @JsonProperty("words_result_num")
    private int wordsResultNum;
    @JsonProperty("error_code")
    private Integer errorCode;
}
