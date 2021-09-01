package cn.wghtstudio.insurance.util.pdf;

public class SealFont {

    public SealFont() {
    }

    /**
     * 字体内容
     */
    private String fontText;
    /**
     * 是否加粗
     */
    private Boolean isBold = true;
    /**
     * 字形名，默认为宋体
     */
    private String fontFamily = "宋体";
    /**
     * 字体大小
     */
    private Integer fontSize;
    /**
     * 字距
     */
    private Double fontSpace;
    /**
     * 边距（环边距或上边距）
     */
    private Integer marginSize;

    public void setFontSpace(Double fontSpace) {
        this.fontSpace = fontSpace;
    }

    public void setMarginSize(Integer marginSize) {
        this.marginSize = marginSize;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public SealFont setFontText(String fontText) {
        this.fontText = fontText;
        return this;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public void setBold(Boolean bold) {
        isBold = bold;
    }

    public String getFontText() {
        return fontText;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public Double getFontSpace() {
        return fontSpace;
    }

    public Integer getMarginSize() {
        return marginSize;
    }

    public Boolean isBold() {
        return isBold;
    }
}
