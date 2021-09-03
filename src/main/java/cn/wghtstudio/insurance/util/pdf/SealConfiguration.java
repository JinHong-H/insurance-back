package cn.wghtstudio.insurance.util.pdf;

import lombok.Builder;
import lombok.Data;

import java.awt.*;

@Data
@Builder
public class SealConfiguration {
    // 主文字
    private SealFont mainFont;

    //副文字
    private SealFont viceFont;

    // 抬头文字
    private SealFont titleFont;

    // 中心文字
    private SealFont centerFont;

    // 边线圆
    private SealCircle borderCircle;

    // 内边线圆
    private SealCircle borderInnerCircle;

    // 内线圆
    private SealCircle innerCircle;

    // 背景色，默认红色
    private Color backgroundColor = Color.RED;

    // 图片输出尺寸，默认300
    private Integer imageSize = 300;
}
