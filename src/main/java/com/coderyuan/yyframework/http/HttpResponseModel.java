package com.coderyuan.yyframework.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author yuanguozheng
 */
public class HttpResponseModel {

    private static final int BUFFER_SIZE = 512;

    private int mHttpCode;

    private String mErrorMsg;

    private Map<String, List<String>> mHeaders;

    private byte[] mData;

    public String getStringData() {
        return new String(mData);
    }

    public byte[] getBytesData() {
        return mData;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        mHeaders = headers;
    }

    public Map<String, List<String>> getHeaders() {
        return mHeaders;
    }

    public String getHeaderItemString(String item) {
        List<String> strings = mHeaders.get(item);
        if (strings == null || strings.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (String i : strings) {
            builder.append(i);
        }
        return builder.toString();
    }

    public void setInputStream(InputStream inputStream) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int count;
        try {
            while ((count = inputStream.read(buffer, 0, BUFFER_SIZE)) > 0) {
                stream.write(buffer, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mData = stream.toByteArray();
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
