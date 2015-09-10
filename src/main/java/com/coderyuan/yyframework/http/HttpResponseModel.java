package com.coderyuan.yyframework.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * @author yuanguozheng
 */
public class HttpResponseModel {

    private static final int BUFFER_SIZE = 512;

    private int mHttpCode;

    private String mErrorMsg;

    private InputStream mInputStream;

    private Map<String, List<String>> mHeaders;

    public String getStringData() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(mInputStream));
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public byte[] getBytesData() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int count;
        try {
            while ((count = mInputStream.read(buffer, 0, BUFFER_SIZE)) > 0) {
                stream.write(buffer, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream.toByteArray();
    }

    public void setHeaders(Map<String, List<String>> headers) {
        mHeaders = headers;
    }

    public Map<String, List<String>> getHeaders() {
        return mHeaders;
    }

    public InputStream getInputStream() {
        return mInputStream;
    }

    public void setInputStream(InputStream inputStream) {
        mInputStream = inputStream;
    }

    public int getHttpCode() {
        return mHttpCode;
    }

    public void setHttpCode(int httpCode) {
        mHttpCode = httpCode;
    }

    public String getErrorMsg() {
        return mErrorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        mErrorMsg = errorMsg;
    }
}
