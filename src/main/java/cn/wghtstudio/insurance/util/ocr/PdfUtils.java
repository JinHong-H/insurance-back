package cn.wghtstudio.insurance.util.ocr;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PdfUtils {
    /**
     * dpi越大转换后越清晰，相对转换速度越慢
     */
    private static final Integer DPI = 100;

    /**
     * 转换后的图片类型
     */
    private static final String IMG_TYPE = "png";

    public static byte[] pdfMadeFromTemplate(Map<String, String> dataMap, Map<String, byte[]> imgMap) {
        PdfReader reader;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            BaseFont bf = BaseFont.createFont(Paths.get("src", "main", "resources", "PdfTemplate", "simsun.ttc,1").normalize().toAbsolutePath().toString(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            reader = new PdfReader(Paths.get("src", "main", "resources", "PdfTemplate", "TemplateWord.pdf").normalize().toAbsolutePath().toString());// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            stamper.setFormFlattening(true);
            AcroFields form = stamper.getAcroFields();
            for (String key : form.getFields().keySet()) {
                form.setFieldProperty(key, "textfont", bf, null);
                form.setFieldProperty(key, "textsize", 9, null);
            }
            for (String key : dataMap.keySet()) {
                String value = dataMap.get(key);
                form.setField(key, value);
            }
            for (String key : imgMap.keySet()) {
                byte[] value = imgMap.get(key);
                int pageNo = form.getFieldPositions(key).get(0).page;
                Rectangle signRect = form.getFieldPositions(key).get(0).position;
                float x = signRect.getLeft();
                float y = signRect.getBottom();
                //根据路径读取图片
                Image image = Image.getInstance(value);
                //获取图片页面
                PdfContentByte under = stamper.getOverContent(pageNo);
                //图片大小自适应
                image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                //添加图片
                image.setAbsolutePosition(x, y);
                under.addImage(image);
            }
            stamper.setFormFlattening(true);// 如果为false，生成的PDF文件可以编辑，如果为true，生成的PDF文件不可以编辑
            stamper.close();
            return bos.toByteArray();
        } catch (IOException | DocumentException e) {
            return null;
        }
    }

    /**
     * PDF转图片
     *
     * @param fileContent PDF文件的二进制流
     * @return 图片文件的二进制流
     */
    public static List<byte[]> pdfToImage(byte[] fileContent) throws IOException {
        List<byte[]> result = new ArrayList<>();
        try (PDDocument document = PDDocument.load(fileContent)) {
            PDFRenderer renderer = new PDFRenderer(document);
            for (int i = 0; i < document.getNumberOfPages(); ++i) {
                // TODO:字体缺失警告(也可能不是这个原因)，目前不影响生成
                BufferedImage bufferedImage = renderer.renderImageWithDPI(i, DPI);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, IMG_TYPE, out);
                result.add(out.toByteArray());
            }
        }
        return result;
    }
}
