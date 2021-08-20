package cn.wghtstudio.insurance.service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

// 身份证
@Data
public class IdCardResponseBody {
    private String log_id;
    private int direction;
    private String image_status;
    private String photo;
    private Location photo_location;
    private Words_result words_result;
    private int words_result_num;

    public static class Words_result {
        @JsonProperty("住址")
        private Info address;
        @JsonProperty("公民身份号码")
        private Info idnumber;
        @JsonProperty("出生")
        private Info born;
        @JsonProperty("姓名")
        private Info name;
        @JsonProperty("性别")
        private Info sex;
        @JsonProperty("民族")
        private Info nationality;

        public static class Info {
            private Location location;
            private String words;
        }

    }
}
