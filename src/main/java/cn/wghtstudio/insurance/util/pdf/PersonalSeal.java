package cn.wghtstudio.insurance.util.pdf;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PersonalSeal implements Seal {
    // 默认从10x10的位置开始画，防止左上部分画布装不下
    private final static int INIT_BEGIN = 10;

    private final int imageSize;

    private final int lineSize;

    private final SealFont font;

    private final String addString;

    // 画布
    private BufferedImage image;

    // 画笔
    private Graphics2D graphics2D;

    private void scaleAndDrawRect() {
        // 拉伸
        BufferedImage bi = new BufferedImage(imageSize, imageSize, image.getType());
        Graphics2D ng2d = bi.createGraphics();
        ng2d.setPaint(Color.RED);
        ng2d.drawImage(image, 0, 0, imageSize, imageSize, null);

        // 画正方形
        ng2d.setStroke(new BasicStroke(lineSize));
        ng2d.drawRect(0, 0, imageSize, imageSize);
        ng2d.dispose();

        image = bi;
        graphics2D = image.createGraphics();
    }

    /**
     * 画三字
     *
     * @param fixH      修复膏
     * @param fixW      修复宽
     * @param isWithYin 是否含有“印”
     */
    private void drawThreeFont(int fixH, int fixW, boolean isWithYin) {
        fixH -= 9;
        int marginW = fixW + lineSize;
        //设置字体
        Font f = new Font(font.getFontFamily(), Font.BOLD, font.getFontSize());
        graphics2D.setFont(f);
        FontRenderContext context = graphics2D.getFontRenderContext();
        Rectangle2D rectangle = f.getStringBounds(font.getFontText().substring(0, 1), context);
        float marginH = (float) (Math.abs(rectangle.getCenterY()) * 2 + marginW) + fixH;
        int oldW = marginW;

        if (isWithYin) {
            graphics2D.drawString(font.getFontText().substring(2, 3), marginW, marginH);
            marginW += rectangle.getCenterX() * 2 + (font.getFontSpace() == null ? INIT_BEGIN : font.getFontSpace());
        } else {
            marginW += rectangle.getCenterX() * 2 + (font.getFontSpace() == null ? INIT_BEGIN : font.getFontSpace());
            graphics2D.drawString(font.getFontText().substring(0, 1), marginW, marginH);
        }

        //拉伸
        scaleAndDrawRect();

        graphics2D.setPaint(Color.RED);
        graphics2D.setFont(f);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isWithYin) {
            graphics2D.drawString(font.getFontText().substring(0, 1), marginW, marginH += fixH);
            rectangle = f.getStringBounds(font.getFontText(), context);
            marginH += Math.abs(rectangle.getHeight());
            graphics2D.drawString(font.getFontText().substring(1), marginW, marginH);
        } else {
            graphics2D.drawString(font.getFontText().substring(1, 2), oldW, marginH += fixH);
            rectangle = f.getStringBounds(font.getFontText(), context);
            marginH += Math.abs(rectangle.getHeight());
            graphics2D.drawString(font.getFontText().substring(2, 3), oldW, marginH);
        }
    }

    /**
     * 画四字
     *
     * @param fixH 修复膏
     * @param fixW 修复宽
     */
    private void drawFourFont(int fixH, int fixW) {
        int marginW = fixW + lineSize;

        scaleAndDrawRect();

        graphics2D.setPaint(Color.RED);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontRenderContext context = graphics2D.getFontRenderContext();

        Font f = new Font(font.getFontFamily(), Font.BOLD, font.getFontSize());
        graphics2D.setFont(f);
        Rectangle2D rectangle = f.getStringBounds(font.getFontText().substring(0, 1), context);
        float marginH = (float) (Math.abs(rectangle.getCenterY()) * 2 + marginW) + fixH;

        graphics2D.drawString(font.getFontText().substring(2, 3), marginW, marginH);
        int oldW = marginW;
        marginW +=
                Math.abs(rectangle.getCenterX()) * 2 + (font.getFontSpace() == null ? INIT_BEGIN : font.getFontSpace());

        graphics2D.drawString(font.getFontText().substring(0, 1), marginW, marginH);
        marginH += Math.abs(rectangle.getHeight());

        graphics2D.drawString(font.getFontText().substring(3, 4), oldW, marginH);

        graphics2D.drawString(font.getFontText().substring(1, 2), marginW, marginH);
    }

    /**
     * 生成私人印章图片
     */
    private void buildPersonSeal() {
        if (font == null || font.getFontText().length() < 2 || font.getFontText().length() > 4) {
            throw new RuntimeException("FontText.length illegal!");
        }

        int fixH = 18;
        int fixW = 2;

        //2.1设置画笔颜色
        graphics2D.setPaint(Color.RED);

        //2.2抗锯齿设置
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //3.写签名
        int marginW = fixW + lineSize;
        float marginH;
        FontRenderContext context = graphics2D.getFontRenderContext();
        Rectangle2D rectangle;
        Font f;

        if (font.getFontText().length() == 2) {
            if (addString != null && addString.trim().length() > 0) {
                font.setFontText(font.getFontText() + addString);
                drawThreeFont(fixH, fixW, true);
            } else {
                f = new Font(font.getFontFamily(), Font.BOLD, font.getFontSize());
                graphics2D.setFont(f);
                rectangle = f.getStringBounds(font.getFontText().substring(0, 1), context);
                marginH = (float) (Math.abs(rectangle.getCenterY()) * 2 + marginW) + fixH - 4;
                graphics2D.drawString(font.getFontText().substring(0, 1), marginW, marginH);
                marginW += Math.abs(rectangle.getCenterX()) * 2 + (font.getFontSpace() == null ?
                        INIT_BEGIN :
                        font.getFontSpace());
                graphics2D.drawString(font.getFontText().substring(1), marginW, marginH);

                scaleAndDrawRect();
            }
            return;
        }

        if (font.getFontText().length() == 3) {
            if (addString != null && addString.trim().length() > 0) {
                font.setFontText(font.getFontText() + addString);
                drawFourFont(fixH, fixW);
            } else {
                font.setFontText(font.getFontText());
                drawThreeFont(fixH, fixW, false);
            }
            return;
        }

        drawFourFont(fixH, fixW);
    }

    public PersonalSeal(int imageSize, int lineSize, SealFont font, String addString) {
        this.imageSize = imageSize;
        this.lineSize = lineSize;
        this.font = font;
        this.addString = addString;

        image = new BufferedImage(imageSize, imageSize / 2, BufferedImage.TYPE_4BYTE_ABGR);
        graphics2D = image.createGraphics();
    }

    @Override
    public byte[] draw() throws IOException {
        buildPersonSeal();

        try (ByteArrayOutputStream outStream = new ByteArrayOutputStream()) {
            // bufferedImage转为byte数组
            ImageIO.write(image, "png", outStream);
            return outStream.toByteArray();
        }
    }
}
