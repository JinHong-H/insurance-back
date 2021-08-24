package cn.wghtstudio.insurance.util.ocr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Info {
    private String words;
    private Location location;
}
