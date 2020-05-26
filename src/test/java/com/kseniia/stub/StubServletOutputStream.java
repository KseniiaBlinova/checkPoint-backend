package com.kseniia.stub;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;

public class StubServletOutputStream extends ServletOutputStream {

    public ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    public void write(int i) {
        outputStream.write(i);
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {

    }
}
