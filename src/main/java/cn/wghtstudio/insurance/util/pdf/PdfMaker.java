package cn.wghtstudio.insurance.util.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PdfMaker {
    // dpi 越大转换后越清晰，相对转换速度越慢
    private static final Integer DPI = 100;

    // 转换后的图片类型
    private static final String IMG_TYPE = "png";

    // pdf 表单中填充的数据
    private final Map<String, String> dataMap;

    // pdf 中要追加的印章图片
    Map<String, byte[]> imgMap;

    private final ByteArrayOutputStream bos;

    private final BaseFont font;

    private final PdfStamper stamper;

    private AcroFields form;

    /**
     * 初始化表单
     * 设置表单格式 设置 form 字段
     */
    private void initForm() {
        stamper.setFormFlattening(true);
        form = stamper.getAcroFields();

        for (String key : form.getFields().keySet()) {
            form.setFieldProperty(key, "textfont", font, null);
            form.setFieldProperty(key, "textsize", 9, null);
        }
    }

    /**
     * 填充 pdf 中的表单
     *
     * @throws DocumentException itext 内置异常
     * @throws IOException       io 异常
     */
    private void fillForm() throws DocumentException, IOException {
        for (String key : dataMap.keySet()) {
            String value = dataMap.get(key);
            form.setField(key, value);
        }
    }

    /**
     * 添加印章图片
     *
     * @throws DocumentException itext 内置异常
     * @throws IOException       io 异常
     */
    private void fillImg() throws DocumentException, IOException {
        for (String key : imgMap.keySet()) {
            byte[] value = imgMap.get(key);
            int pageNo = form.getFieldPositions(key).get(0).page;
            Rectangle signRect = form.getFieldPositions(key).get(0).position;
            float x = signRect.getLeft();
            float y = signRect.getBottom();
            Image image = Image.getInstance(value);

            //获取图片页面
            PdfContentByte under = stamper.getOverContent(pageNo);

            //图片大小自适应
            image.scaleToFit(signRect.getWidth(), signRect.getHeight());

            //添加图片
            image.setAbsolutePosition(x, y);
            under.addImage(image);
        }
    }

    public PdfMaker(Map<String, String> dataMap, Map<String, byte[]> imgMap) throws IOException, DocumentException {
        this.dataMap = dataMap;
        this.imgMap = imgMap;

        this.bos = new ByteArrayOutputStream();

        // 读取 pdf 模板
        URL pdfUrl = Thread.currentThread().getContextClassLoader().getResource("PdfTemplate/TemplateWord.pdf");
        URL fontUrl = Thread.currentThread().getContextClassLoader().getResource("PdfTemplate/simsun.ttc");
        if (pdfUrl == null || fontUrl == null) {
            throw new RuntimeException();
        }
        PdfReader reader = new PdfReader(pdfUrl.getPath());
        this.font = BaseFont.createFont(fontUrl.getPath() + ",1",
                BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        this.stamper = new PdfStamper(reader, bos);

        initForm();
    }

    public byte[] generate(boolean isOfficial) throws DocumentException, IOException {
        fillForm();
        if (isOfficial) {
            fillImg();
        }
        // 如果为 false, 生成的 PDF 文件可以编辑; 如果为 true, 生成的 PDF 文件不可以编辑
        stamper.setFormFlattening(true);
        stamper.close();

        return bos.toByteArray();
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
