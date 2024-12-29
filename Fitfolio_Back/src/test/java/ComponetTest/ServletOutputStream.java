package ComponetTest;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

class ServletOutputStreamWrapper extends ServletOutputStream {
    private final ByteArrayOutputStream outputStream;

    public ServletOutputStreamWrapper(ByteArrayOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
    }

    @Override
    public void write(int b) throws IOException {
        outputStream.write(b);
    }
}

