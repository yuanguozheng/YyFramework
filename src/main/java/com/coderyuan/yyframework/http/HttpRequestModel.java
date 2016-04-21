/**
 * Copyright (c) 2015 coderyuan.com. All Rights Reserved.
 *
 * YyFramework
 *
 * HttpRequestModel.java created on 16:06
 *
 * @author yuanguozheng
 * @version 1.0.0
 * @since 2015/9/10 0010
 */
package com.coderyuan.yyframework.http;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * HttpRequestModel
 *
 * @author yuanguozheng
 */
public class HttpRequestModel {

    private HttpUtil.Method mMethod;

    private String mUrl;

    private HashMap<String, String> mStringParams;

    private ArrayList<HttpFileModel> mFiles;

    private HashMap<String, String> mHeaders;

    private boolean mNeedUrlEncoded = false;

    private String mProxyHost;

    private int mProxyPort;

    private boolean mFollowRedirect = false;

    private InputStream mCaStream;

    private String mTrustHostName;

    public HttpRequestModel() {
        mStringParams = new HashMap<String, String>();
        mFiles = new ArrayList<HttpFileModel>();
        mHeaders = new HashMap<String, String>();
    }

    public HttpUtil.Method getMethod() {
        return mMethod;
    }

    public void setMethod(HttpUtil.Method method) {
        mMethod = method;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public HashMap<String, String> getStringParams() {
        return mStringParams;
    }

    public void setStringParams(HashMap<String, String> stringParams) {
        mStringParams = stringParams;
    }

    public ArrayList<HttpFileModel> getFiles() {
        return mFiles;
    }

    public void setFiles(ArrayList<HttpFileModel> files) {
        mFiles = files;
    }

    public HashMap<String, String> getHeaders() {
        return mHeaders;
    }

    public void setHeaders(HashMap<String, String> headers) {
        mHeaders = headers;
    }

    public Boolean getNeedUrlEncoded() {
        return mNeedUrlEncoded;
    }

    public void setNeedUrlEncoded(Boolean needUrlEncoded) {
        mNeedUrlEncoded = needUrlEncoded;
    }

    public String getProxyHost() {
        return mProxyHost;
    }

    public void setProxyHost(String proxyHost) {
        mProxyHost = proxyHost;
    }

    public int getProxyPort() {
        return mProxyPort;
    }

    public void setProxyPort(int proxyPort) {
        mProxyPort = proxyPort;
    }

    public boolean isFollowRedirect() {
        return mFollowRedirect;
    }

    public void setFollowRedirect(boolean followRedirect) {
        mFollowRedirect = followRedirect;
    }

    public InputStream getCaStream() {
        return mCaStream;
    }

    public void setCaStream(InputStream caStream) {
        mCaStream = caStream;
    }

    public String getTrustHostName() {
        return mTrustHostName;
    }

    public void setTrustHostName(String trustHostName) {
        mTrustHostName = trustHostName;
    }
}
