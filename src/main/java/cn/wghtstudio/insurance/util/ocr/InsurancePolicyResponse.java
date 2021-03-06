package cn.wghtstudio.insurance.util.ocr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InsurancePolicyResponse {
    @JsonProperty("log_id")
    private String logId;
    @JsonProperty("wordsResult_num")
    private int wordsResultNum;
    @JsonProperty("words_result")
    private List<WordsResult> wordsResult;
    @JsonProperty("error_code")
    private Integer errorCode;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WordsResult {
        private String words;
    }
}
