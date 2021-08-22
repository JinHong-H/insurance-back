package cn.wghtstudio.insurance.util.ocr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdCardResponse {
    @Data
    public static class Info {
        private Location location;
        private String words;
    }

    @Data
    public static class WordsResult {
        @JsonProperty("住址")
        private Info address;
        @JsonProperty("公民身份号码")
        private Info number;
        @JsonProperty("出生")
        private Info born;
        @JsonProperty("姓名")
        private Info name;
        @JsonProperty("性别")
        private Info sex;
        @JsonProperty("民族")
        private Info nationality;
    }

    @JsonProperty("log_id")
    private String logId;
    private int direction;
    @JsonProperty("image_status")
    private String imageStatus;
    private String photo;
    @JsonProperty("photo_location")
    private Location photoLocation;
    @JsonProperty("words_result")
    private WordsResult wordsResult;
    @JsonProperty("words_result_num")
    private int wordsResultNum;
}
