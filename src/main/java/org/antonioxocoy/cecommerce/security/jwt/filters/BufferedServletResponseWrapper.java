package org.antonioxocoy.cecommerce.security.jwt.filters;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class BufferedServletResponseWrapper extends HttpServletResponseWrapper {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private PrintWriter writer = new PrintWriter(outputStream);

    public BufferedServletResponseWrapper(HttpServletResponse response)
            throws IOException {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        LOG.info("getOutputStream");

        return new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener listener) {

            }

            @Override
            public void write(int b) throws IOException {
                LOG.info("write int");

                outputStream.write(b);
            }

            @Override
            public void write(byte[] b) throws IOException {
                LOG.info("write byte[]");

                outputStream.write(b);
            }
        };
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        LOG.info("getWriter");
        return writer;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (writer != null) {
            writer.flush();
        } else if (outputStream != null) {
            outputStream.flush();
        }
    }

    public String getResponseData() {
        return outputStream.toString();
    }

}
