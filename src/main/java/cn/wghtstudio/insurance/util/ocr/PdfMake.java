package cn.wghtstudio.insurance.util.ocr;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

public class PdfMake {
    public static byte[] pdfMadeFromTemplate(Map<String, String> o) {
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
            for (String key : o.keySet()) {
                String value = o.get(key);
                form.setField(key, value);
            }
            stamper.setFormFlattening(true);// 如果为false，生成的PDF文件可以编辑，如果为true，生成的PDF文件不可以编辑
            stamper.close();
            return bos.toByteArray();
        } catch (IOException | DocumentException e) {
            return null;
        }
    }
}
