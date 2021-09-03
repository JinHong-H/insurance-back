package cn.wghtstudio.insurance.util.pdf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class SealCircle {
    // 线宽度
    private final Integer lineSize;

    // 半径
    private final Integer width;

    // 半径
    private final Integer height;
}
