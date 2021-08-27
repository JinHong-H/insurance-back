package cn.wghtstudio.insurance.util.ocr;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class PdfMake {
    public static void pdfout(Map<String, String> o, String templatePath, String newPDFPath,String fontPath) {
        // 模板路径
        // String templatePath = ".\\TemplateWord.pdf";
        // 生成的新文件路径
        // String newPDFPath = ".\\TemplateWordsuccess.pdf";
        
        PdfReader reader;
        FileOutputStream out;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            out = new FileOutputStream(newPDFPath);// 输出流
            reader = new PdfReader(templatePath);// 读取pdf模板
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
            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();
            
        } catch (IOException | DocumentException e) {
            System.out.println(e);
        }
    }
}
