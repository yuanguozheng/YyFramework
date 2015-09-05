package com.coderyuan.yyframework.http;

import java.util.List;
import java.util.Map;

/**
 * @author yuanguozheng
 *
 */
public class HttpResponseModel {

    private String mData;

    private Map<String, List<String>> mHeaders;

    public void setData(String data) {
        mData = data;
    }

    public String getData() {
        return mData;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        mHeaders = headers;
    }

    public Map<String, List<String>> getHeaders() {
        return mHeaders;
    }
}
