package cn.wghtstudio.insurance.util.pdf;

import java.io.IOException;

public interface Seal {
    byte[] draw() throws IOException;
}
