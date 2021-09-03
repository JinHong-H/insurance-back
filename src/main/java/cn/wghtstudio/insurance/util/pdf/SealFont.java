package cn.wghtstudio.insurance.util.pdf;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SealFont {
    // 字体内容
    private String fontText;

    // 是否加粗
    private Boolean isBold = true;

    // 字形名，默认为宋体
    private String fontFamily = "宋体";

    // 字体大小
    private Integer fontSize;

    // 字距
    private Double fontSpace;

    // 边距（环边距或上边距）
    private Integer marginSize;
}
