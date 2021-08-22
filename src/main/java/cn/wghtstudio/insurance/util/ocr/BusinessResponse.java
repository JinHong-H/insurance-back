package cn.wghtstudio.insurance.util.ocr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessResponse {
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WordsResult {
        @JsonProperty("社会信用代码")
        private Info creditCode;
        @JsonProperty("组成形式")
        private Info formation;
        @JsonProperty("经营范围")
        private Info scope;
        @JsonProperty("成立日期")
        private Info establish;
        @JsonProperty("法人")
        private Info legalPerson;
        @JsonProperty("注册资本")
        private Info capital;
        @JsonProperty("证件编号")
        private Info number;
        @JsonProperty("地址")
        private Info address;
        @JsonProperty("单位名称")
        private Info companyName;
        @JsonProperty("有效期")
        private Info validPeriod;
        @JsonProperty("类型")
        private Info type;
        @JsonProperty("实收资本")
        private Info realCapital;
    }

    @JsonProperty("log_id")
    private String logId;
    @JsonProperty("words_result")
    private WordsResult wordsResult;
    @JsonProperty("words_result_num")
    private int wordsResultNum;
}
