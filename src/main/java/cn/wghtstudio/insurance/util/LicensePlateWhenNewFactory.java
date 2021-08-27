package cn.wghtstudio.insurance.util;

public class LicensePlateWhenNewFactory {
    public static String getLicensePlateWhenNew(String engine) {
        final int engineLength = engine.length();
        int startPos = engineLength - 6;
        if (startPos < 0) {
            startPos = 0;
        }

        return "新车-" + engine.substring(startPos);
    }
}
