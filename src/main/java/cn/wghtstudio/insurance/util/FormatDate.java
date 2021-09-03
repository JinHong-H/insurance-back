package cn.wghtstudio.insurance.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatDate {
    public static String getFormatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }
}
