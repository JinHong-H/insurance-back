package cn.wghtstudio.insurance.util.ocr;

public class SealCircle {

    public SealCircle(Integer lineSize, Integer width,Integer height) {
        this.lineSize = lineSize;
        this.width = width;
        this.height = height;
    }

    /**
     * 线宽度
     */
    private final Integer lineSize;
    /**
     * 半径
     */
    private final Integer width;
    /**
     * 半径
     */
    private final Integer height;

    public Integer getLineSize() {
        return lineSize;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWidth() {
        return width;
    }
}
