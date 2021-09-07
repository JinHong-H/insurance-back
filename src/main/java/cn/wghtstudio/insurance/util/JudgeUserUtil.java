package cn.wghtstudio.insurance.util;

public class JudgeUserUtil {
    private static final int rootAuth = 1;
    private static final int adminAuth = 1 << 1;
    private static final int companyAuth = 1 << 2;
    private static final int normalAuth = 1 << 3;

    public static boolean isNormal(int auth) {
        return (JudgeUserUtil.normalAuth & auth) != 0;
    }

    public static boolean isAdmin(int auth) {
        return (JudgeUserUtil.adminAuth & auth) != 0;
    }
}
