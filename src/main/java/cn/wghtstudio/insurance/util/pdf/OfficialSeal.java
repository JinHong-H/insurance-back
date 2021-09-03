package cn.wghtstudio.insurance.util.pdf;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class OfficialSeal implements Seal {
    // 默认从 10x10 的位置开始画，防止左上部分画布装不下
    private final int INIT_BEGIN = 10;

    // 配置
    private final SealConfiguration config;

    // 画布
    private final BufferedImage image;

    // 画笔
    private final Graphics2D graphics2D;

    private void initGraphics() {
        // 抗锯齿设置
        // 其中文本不抗锯齿, 其他图形抗锯齿
        RenderingHints hints = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF
        );
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHints(hints);

        // 设置画笔为透明画笔
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 0));

        // 填充背景为透明
        graphics2D.fillRect(0, 0, config.getImageSize(), config.getImageSize());

        // 设置画笔为不透明画笔
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1));

        // 设置画笔颜色
        graphics2D.setPaint(config.getBackgroundColor());
    }

    /**
     * 画圆
     *
     * @param circle 圆配置对象
     */
    private void drawCircle(SealCircle circle, int x, int y) {
        if (circle == null) {
            return;
        }

        // 圆线条粗细默认是圆直径的 1/35
        int lineSize = circle.getLineSize() == null ? circle.getHeight() * 2 / (35) : circle.getLineSize();

        // 画圆
        graphics2D.setStroke(new BasicStroke(lineSize));
        graphics2D.drawOval(x, y, circle.getWidth() * 2, circle.getHeight() * 2);
    }

    /**
     * 绘制圆弧形文字
     *
     * @param circleRadius 弧形半径
     * @param font         字体对象
     * @param isTop        是否字体在上部，否则在下部
     */
    private void drawArcFont4Circle(int circleRadius, SealFont font, boolean isTop) {
        if (font == null) {
            return;
        }

        //1.字体长度
        int fontTextLen = font.getFontText().length();

        //2.字体大小，默认根据字体长度动态设定 TODO
        int fontSize = font.getFontSize() == null ? (55 - fontTextLen * 2) : font.getFontSize();

        //3.字体样式
        int fontStyle = font.getIsBold() ? Font.BOLD : Font.PLAIN;

        //4.构造字体
        Font f = new Font(font.getFontFamily(), fontStyle, fontSize);

        FontRenderContext context = graphics2D.getFontRenderContext();
        Rectangle2D rectangle = f.getStringBounds(font.getFontText(), context);

        //5.文字之间间距，默认动态调整
        double fontSpace;
        if (font.getFontSpace() != null) {
            fontSpace = font.getFontSpace();
        } else {
            if (fontTextLen == 1) {
                fontSpace = 0;
            } else {
                fontSpace = rectangle.getWidth() / (fontTextLen - 1) * 0.9;
            }
        }

        //6.距离外圈距离
        int marginSize = font.getMarginSize() == null ? INIT_BEGIN : font.getMarginSize();

        //7.写字
        double newRadius = circleRadius + rectangle.getY() - marginSize;
        double radianPerInterval = 2 * Math.asin(fontSpace / (2 * newRadius));

        double fix = 0.04;
        if (isTop) {
            fix = 0.18;
        }
        double firstAngle;
        if (!isTop) {
            if (fontTextLen % 2 == 1) {
                firstAngle = Math.PI + Math.PI / 2 - (fontTextLen - 1) * radianPerInterval / 2.0 - fix;
            } else {
                firstAngle = Math.PI + Math.PI / 2 - ((fontTextLen / 2.0 - 0.5) * radianPerInterval) - fix;
            }
        } else {
            if (fontTextLen % 2 == 1) {
                firstAngle = (fontTextLen - 1) * radianPerInterval / 2.0 + Math.PI / 2 + fix;
            } else {
                firstAngle = (fontTextLen / 2.0 - 0.5) * radianPerInterval + Math.PI / 2 + fix;
            }
        }

        for (int i = 0; i < fontTextLen; i++) {
            double theta;
            double thetaX;
            double thetaY;

            if (!isTop) {
                theta = firstAngle + i * radianPerInterval;
            } else {
                theta = firstAngle - i * radianPerInterval;
            }
            thetaX = newRadius * Math.sin(Math.PI / 2 - theta);
            thetaY = newRadius * Math.cos(theta - Math.PI / 2);

            AffineTransform transform;
            if (!isTop) {
                transform = AffineTransform.getRotateInstance(Math.PI + Math.PI / 2 - theta);
            } else {
                transform = AffineTransform.getRotateInstance(Math.PI / 2 - theta + Math.toRadians(8));
                transform.scale(0.8, 1.5);
            }

            Font f2 = f.deriveFont(transform);
            graphics2D.setFont(f2);
            graphics2D.drawString(font.getFontText().substring(i, i + 1), (float) (circleRadius + thetaX + INIT_BEGIN),
                    (float) (circleRadius - thetaY + INIT_BEGIN));
        }
    }

    /**
     * 绘制椭圆弧形文字
     *
     * @param circle 外围圆
     * @param font   字体对象
     * @param isTop  是否字体在上部，否则在下部
     */
    private void drawArcFont4Oval(SealCircle circle, SealFont font, boolean isTop) {
        if (font == null) {
            return;
        }
        float radiusX = circle.getWidth();
        float radiusY = circle.getHeight();
        float radiusWidth = radiusX + circle.getLineSize();
        float radiusHeight = radiusY + circle.getLineSize();

        //1.字体长度
        int fontTextLen = font.getFontText().length();

        //2.字体大小，默认根据字体长度动态设定
        int fontSize = font.getFontSize() == null ? 25 + (10 - fontTextLen) / 2 : font.getFontSize();

        //3.字体样式
        int fontStyle = font.getIsBold() ? Font.BOLD : Font.PLAIN;

        //4.构造字体
        Font f = new Font(font.getFontFamily(), fontStyle, fontSize);

        //5.总的角跨度
        float totalArcAng = (float) (font.getFontSpace() * fontTextLen);

        //6.从边线向中心的移动因子
        float minRat = 0.90f;

        double startAngle = isTop ? -90f - totalArcAng / 2f : 90f - totalArcAng / 2f;
        double step = 0.5;
        int alCount = (int) Math.ceil(totalArcAng / step) + 1;
        double[] angleArr = new double[alCount];
        double[] arcLenArr = new double[alCount];
        int num = 0;
        double accArcLen = 0.0;
        angleArr[num] = startAngle;
        arcLenArr[num] = accArcLen;
        num++;
        double angR = startAngle * Math.PI / 180.0;
        double lastX = radiusX * Math.cos(angR) + radiusWidth;
        double lastY = radiusY * Math.sin(angR) + radiusHeight;
        for (double i = startAngle + step; num < alCount; i += step) {
            angR = i * Math.PI / 180.0;
            double x = radiusX * Math.cos(angR) + radiusWidth, y = radiusY * Math.sin(angR) + radiusHeight;
            accArcLen += Math.sqrt((lastX - x) * (lastX - x) + (lastY - y) * (lastY - y));
            angleArr[num] = i;
            arcLenArr[num] = accArcLen;
            lastX = x;
            lastY = y;
            num++;
        }
        double arcPer = accArcLen / fontTextLen;
        for (int i = 0; i < fontTextLen; i++) {
            double arcL = i * arcPer + arcPer / 2.0;
            double ang = 0.0;
            for (int p = 0; p < arcLenArr.length - 1; p++) {
                if (arcLenArr[p] <= arcL && arcL <= arcLenArr[p + 1]) {
                    ang = (arcL >= ((arcLenArr[p] + arcLenArr[p + 1]) / 2.0)) ? angleArr[p + 1] : angleArr[p];
                    break;
                }
            }
            angR = (ang * Math.PI / 180f);
            float x = radiusX * (float) Math.cos(angR) + radiusWidth;
            float y = radiusY * (float) Math.sin(angR) + radiusHeight;
            double qxang = Math.atan2(radiusY * Math.cos(angR), -radiusX * Math.sin(angR));
            double fxang = qxang + Math.PI / 2.0;

            int subIndex = isTop ? i : fontTextLen - 1 - i;
            String c = font.getFontText().substring(subIndex, subIndex + 1);

            //获取文字高宽
            FontMetrics fm = new JLabel().getFontMetrics(f);
            int w = fm.stringWidth(c), h = fm.getHeight();

            if (isTop) {
                x += h * minRat * (float) Math.cos(fxang);
                y += h * minRat * (float) Math.sin(fxang);
                x += -w / 2f * (float) Math.cos(qxang);
                y += -w / 2f * (float) Math.sin(qxang);
            } else {
                x += (h * minRat) * (float) Math.cos(fxang);
                y += (h * minRat) * (float) Math.sin(fxang);
                x += w / 2f * (float) Math.cos(qxang);
                y += w / 2f * (float) Math.sin(qxang);
            }

            // 旋转
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.scale(0.8, 1);
            if (isTop)
                affineTransform.rotate(Math.toRadians((fxang * 180.0 / Math.PI - 90)), 0, 0);
            else
                affineTransform.rotate(Math.toRadians((fxang * 180.0 / Math.PI + 180 - 90)), 0, 0);
            Font f2 = f.deriveFont(affineTransform);
            graphics2D.setFont(f2);
            graphics2D.drawString(c, (int) x + INIT_BEGIN, (int) y + INIT_BEGIN);
        }
    }

    /**
     * 画文字
     *
     * @param circleWidth  边线圆宽度
     * @param circleHeight 边线圆高度
     * @param font         字体对象
     */
    private void drawFont(int circleWidth, int circleHeight, SealFont font) {
        if (font == null) {
            return;
        }

        //1.字体长度
        int fontTextLen = font.getFontText().length();

        //2.字体大小，默认根据字体长度动态设定
        int fontSize = font.getFontSize() == null ? (55 - fontTextLen * 2) : font.getFontSize();

        //3.字体样式
        int fontStyle = font.getIsBold() ? Font.BOLD : Font.PLAIN;

        //4.构造字体
        Font f = new Font(font.getFontFamily(), fontStyle, fontSize);
        graphics2D.setFont(f);

        FontRenderContext context = graphics2D.getFontRenderContext();
        String[] fontTexts = font.getFontText().split("\n");
        if (fontTexts.length > 1) {
            int y = 0;
            for (String fontText : fontTexts) {
                y += Math.abs(f.getStringBounds(fontText, context).getHeight());
            }
            //5.设置上边距
            float marginSize = INIT_BEGIN + (float) (circleHeight / 2 - y / 2);
            for (String fontText : fontTexts) {
                Rectangle2D rectangle2D = f.getStringBounds(fontText, context);
                graphics2D.drawString(fontText, (float) (circleWidth / 2 - rectangle2D.getCenterX() + 1), marginSize);
                marginSize += Math.abs(rectangle2D.getHeight());
            }
        } else {
            Rectangle2D rectangle2D = f.getStringBounds(font.getFontText(), context);
            //5.设置上边距，默认在中心
            float marginSize = font.getMarginSize() == null ?
                    (float) (circleHeight / 2 - rectangle2D.getCenterY()) :
                    (float) (circleHeight / 2 - rectangle2D.getCenterY()) + (float) font.getMarginSize();
            graphics2D.drawString(font.getFontText(), (float) (circleWidth / 2 - rectangle2D.getCenterX() + 1), marginSize);
        }
    }

    /**
     * 生成印章图片
     */
    private void buildSeal() {
        // 画边线圆
        if (config.getBorderCircle() != null) {
            drawCircle(config.getBorderCircle(), INIT_BEGIN, INIT_BEGIN);
        } else {
            throw new RuntimeException("BorderCircle can not null！");
        }

        int borderCircleWidth = config.getBorderCircle().getWidth();
        int borderCircleHeight = config.getBorderCircle().getHeight();

        // 画内边线圆
        if (config.getBorderInnerCircle() != null) {
            int x = INIT_BEGIN + borderCircleWidth - config.getBorderInnerCircle().getWidth();
            int y = INIT_BEGIN + borderCircleHeight - config.getBorderInnerCircle().getHeight();
            drawCircle(config.getBorderInnerCircle(), x, y);
        }

        // 画内环线圆
        if (config.getInnerCircle() != null) {
            int x = INIT_BEGIN + borderCircleWidth - config.getInnerCircle().getWidth();
            int y = INIT_BEGIN + borderCircleHeight - config.getInnerCircle().getHeight();
            drawCircle(config.getInnerCircle(), x, y);
        }

        // 画弧形主文字
        if (borderCircleHeight != borderCircleWidth) {
            drawArcFont4Oval(config.getBorderCircle(), config.getMainFont(), true);
        } else {
            drawArcFont4Circle(borderCircleHeight, config.getMainFont(), true);
        }

        // 画弧形副文字
        if (borderCircleHeight != borderCircleWidth) {
            drawArcFont4Oval(config.getBorderCircle(), config.getViceFont(), false);
        } else {
            drawArcFont4Circle(borderCircleHeight, config.getViceFont(), false);
        }

        // 画中心字
        drawFont((borderCircleWidth + INIT_BEGIN) * 2, (borderCircleHeight + INIT_BEGIN) * 2,
                config.getCenterFont());

        // 画抬头文字
        drawFont((borderCircleWidth + INIT_BEGIN) * 2, (borderCircleHeight + INIT_BEGIN) * 2, config.getTitleFont());

        graphics2D.dispose();
    }

    public OfficialSeal(SealConfiguration config) {
        this.config = config;

        // 初始化画布
        image = new BufferedImage(config.getImageSize(), config.getImageSize(), BufferedImage.TYPE_4BYTE_ABGR);

        // 初始化画笔
        graphics2D = image.createGraphics();

        // 初始化参数
        initGraphics();
    }

    @Override
    public byte[] draw() throws IOException {
        buildSeal();

        try (ByteArrayOutputStream outStream = new ByteArrayOutputStream()) {
            // bufferedImage转为byte数组
            ImageIO.write(image, "png", outStream);
            return outStream.toByteArray();
        }
    }
}
