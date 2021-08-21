package cn.wghtstudio.insurance.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtilTests {
    @AllArgsConstructor
    @Getter
    @Setter
    static class TempData {
        @ExcelColumn(value = "姓名", column = 1)
        private String name;

        @ExcelColumn(value = "年龄", column = 2)
        private int age;
    }

    @Test
    void exportFile() throws IOException {
        List<TempData> tempDataList = new ArrayList<>() {
            {
                add(new TempData("test1", 5));
                add(new TempData("test2", 10));
            }
        };

        Workbook wb = ExcelUtil.export(tempDataList, TempData.class);
        assertThat("Workbook should not be null", wb, notNullValue());

        File file = File.createTempFile("export", ".xlsx", null);
        file.deleteOnExit();
        if (file.exists()) {
            boolean delRes = file.delete();
            assertThat("Can't delete file", delRes, equalTo(true));
        }

        try {
            wb.write(new FileOutputStream(file));
        } catch (Exception e) {
            assertThat("Can't write file", true);
            e.printStackTrace();
        }
    }
}
