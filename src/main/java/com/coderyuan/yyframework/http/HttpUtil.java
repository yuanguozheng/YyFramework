/**
 * Copyright (C) 2015 coderyuan.com. All Rights Reserved.
 *
 * YyFramework
 *
 * HttpUtil.java created on 2015年9月10日
 *
 * @author yuanguozheng
 * @version v1.0.0
 * @since 2015年9月10日
 */
package com.coderyuan.yyframework.http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.coderyuan.yyframework.utils.StreamUtil;

/**
 * HttpUtl
 *
 * @author yuanguozheng
 */
public class HttpUtil {

    public boolean isFollowRedirect() {
        return mFollowRedirect;
    }

    public void setFollowRedirect(boolean followRedirect) {
        mFollowRedirect = followRedirect;
    }

    public enum Method {
        GET, POST, POST_FILE
    }

    private static final String METHOD_GET = "GET";

    private static final String METHOD_POST = "POST";

    private static final String BOUNDARY = "----abcdefg";

    private static final String POST_CONTENT_TYPE = "application/x-www-form-urlencoded";

    private static final String FILE_CONTENT_TYPE = "multipart/form-data";

    private static final String CHARSET = "utf-8";

    private static final String RN = "\r\n";

    private static final String HEADER_CONTENT_TYPE = "Content-Type";

    private static final String HEADER_CONNECTION = "Connection";

    private static final String STRING_PARAM_FORMAT = "Content-Disposition: form-data; name=\"%s\"";

    private static final String FILE_PARAM_FORMAT = "Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"";

    private static final int TIME_OUT = 30 * 1000;

    private static final boolean CACHES = false;

    private static final String MIME_FORMAT = "%s;boundary=%s";

    private static final String KEEP_ALIVE = "Keep-Alive";

    private URL mUrl;
    private HttpURLConnection mConnection;
    private OutputStream mOutputStream;
    private HashMap<String, String> mStringParamsMap;
    private ArrayList<HttpFileModel> mFiles;
    private Method mMethod;
    private String mServerUrl;
    private Map<String, String> mRequestHeaders;
    private boolean mDirectWriteParams = false;
    private String mDirectParams;
    private boolean mNeedUrlencoded = false;
    private Proxy mProxy = null;
    private boolean mFollowRedirect = false;

    /**
     * 构造函数，初始化Http工具类
     *
     * @param {@link Method}，请求方法，可选值：GET、POST、POST_FILE
     * @param {@link OnCompleted}，用于回传返回数据
     */
    public HttpUtil(Method method) {
        if (method == null) {
            return;
        }
        mMethod = method;
        mStringParamsMap = new HashMap<String, String>();
        mRequestHeaders = new HashMap<String, String>();
        if (mMethod == Method.POST_FILE) {
            mFiles = new ArrayList<HttpFileModel>();
        }
    }

    public HttpResponseModel request() {
        try {
            buildRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doRequest();
    }

    public void addUrlEncodedParam(String key, String value) {
        try {
            addParam(key, URLEncoder.encode(value, CHARSET));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加字符串参数
     */
    public void addParam(String key, String value) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        if (mStringParamsMap == null) {
            mStringParamsMap = new HashMap<String, String>();
        }
        mStringParamsMap.put(key, value);
    }

    /**
     * 添加单个文件参数
     */
    public void addFile(HttpFileModel fileModel) {
        if (mMethod != Method.POST_FILE) {
            return;
        }
        if (mFiles == null) {
            mFiles = new ArrayList<HttpFileModel>();
        }
        mFiles.add(fileModel);
    }

    /**
     * 设置服务器Url
     *
     * @param url
     */
    public void setUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return;
        }
        mServerUrl = url;
    }

    /**
     * 直接写入URL参数
     */
    public void setDirectWriteParams(String params) {
        if (!StringUtils.isEmpty(params)) {
            mDirectWriteParams = true;
            mDirectParams = params;
        }
    }

    /**
     * 添加文件列表
     */
    public void setFiles(ArrayList<HttpFileModel> files) {
        if (mMethod == Method.POST_FILE) {
            mFiles = files;
        }
    }

    public void setHeaderValue(String key, String value) {
        if (mRequestHeaders != null) {
            mRequestHeaders.put(key, value);
        }
    }

    public void setHeaders(Map<String, String> headers) {
        mRequestHeaders = headers;
    }

    public void setRequestStringParams(HashMap<String, String> map) {
        mStringParamsMap = map;
    }

    public boolean isNeedUrlencoded() {
        return mNeedUrlencoded;
    }

    public void setNeedUrlencoded(boolean needUrlencoded) {
        mNeedUrlencoded = needUrlencoded;
    }

    public void setProxy(String host, int port) {
        mProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
    }

    public void clear() {
        if (mFiles != null) {
            mFiles.clear();
        }
        if (mRequestHeaders != null) {
            mRequestHeaders.clear();
        }
        if (mStringParamsMap != null) {
            mStringParamsMap.clear();
        }
        mProxy = null;
    }

    /**
     * 开始请求
     *
     * @return 服务器返回字符串，出错返回null
     */
    private HttpResponseModel doRequest() {
        HttpResponseModel responseModel = new HttpResponseModel();
        responseModel.setHttpCode(-1);
        try {
            responseModel.setHttpCode(mConnection.getResponseCode());
            responseModel.setErrorMsg(mConnection.getResponseMessage());
            Map<String, List<String>> responseHeaders = mConnection.getHeaderFields();
            InputStream is = mConnection.getInputStream();
            responseModel.setHeaders(responseHeaders);
            responseModel.setInputStream(is);
            return responseModel;
        } catch (IOException e) {
            e.printStackTrace();
            if (StringUtils.isEmpty(responseModel.getErrorMsg())) {
                responseModel.setErrorMsg(e.getCause().toString());
            }
        }
        return responseModel;
    }

    /**
     * 构建HTTP请求
     *
     * @throws IOException，写入流失败时抛出异常
     */
    private void buildRequest() throws IOException {
        if (StringUtils.isEmpty(mServerUrl)) {
            return;
        }
        if (mMethod == Method.GET) {
            buildGetRequest();
        } else {
            mUrl = new URL(mServerUrl);
        }
        if (mUrl == null) {
            return;
        }
        setHeaders();
        if (mConnection == null) {
            return;
        }
        if (mMethod != Method.GET) {
            buildPostRequest();
        }
    }

    private void buildPostRequest() {
        try {
            mOutputStream = new DataOutputStream(mConnection.getOutputStream());
            boolean hasFileParam = buildFileBody();
            buildStringParam();
            if (mMethod == Method.POST_FILE || hasFileParam) {
                writeEndBoundary();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                mOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                StreamUtil.closeQuietly(mOutputStream);
            }
        }
    }

    private void buildGetRequest() {
        try {
            StringBuilder getParamBuilder = new StringBuilder(mServerUrl);
            buildStringParam();
            if (!StringUtils.isEmpty(mDirectParams)) {
                if (getParamBuilder.indexOf("?") == -1) {
                    getParamBuilder.append("?");
                }
                getParamBuilder.append(mDirectParams);
            }
            mUrl = new URL(getParamBuilder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置请求头
     */
    private void setHeaders() {
        try {
            URLConnection conn = mProxy == null ? mUrl.openConnection() : mUrl.openConnection(mProxy);
            mConnection = (HttpURLConnection) conn;
            mConnection.setInstanceFollowRedirects(mFollowRedirect);
            mConnection.setUseCaches(CACHES);
            mConnection.setConnectTimeout(TIME_OUT);
            if (mMethod == Method.GET) {
                mConnection.setRequestMethod(METHOD_GET);
            } else {
                mConnection.setDoOutput(true);
                mConnection.setDoInput(true);
                mConnection.setRequestMethod(METHOD_POST);
                if (mMethod == Method.POST_FILE) {
                    String mime = String.format(MIME_FORMAT, FILE_CONTENT_TYPE, BOUNDARY);
                    mConnection.setRequestProperty(HEADER_CONTENT_TYPE, mime);
                } else {
                    mConnection.setRequestProperty(HEADER_CONTENT_TYPE, POST_CONTENT_TYPE);
                }
            }
            mConnection.setRequestProperty(HEADER_CONNECTION, KEEP_ALIVE);
            if (mRequestHeaders != null) {
                for (String key : mRequestHeaders.keySet()) {
                    mConnection.setRequestProperty(key, mRequestHeaders.get(key));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建字符串参数请求体
     *
     * @return 是否含有字符串参数
     *
     * @throws IOException，写入流失败时抛出异常
     */
    private boolean buildStringParam() throws IOException {
        if ((mStringParamsMap == null) || (mStringParamsMap.size() == 0)) {
            return false;
        }
        StringBuilder builder = new StringBuilder();
        if (mMethod == Method.POST && mDirectWriteParams) {
            if (StringUtils.isEmpty(mDirectParams)) {
                builder.append(mDirectParams);
            }
        } else {
            for (String key : mStringParamsMap.keySet()) {
                if (mMethod == Method.POST_FILE) {
                    buildMultipartString(builder, key);
                } else {
                    String value = mStringParamsMap.get(key);
                    String kvString;
                    if (mNeedUrlencoded) {
                        kvString = String.format("%s=%s",
                                URLEncoder.encode(key, CHARSET),
                                URLEncoder.encode(value, CHARSET));
                    } else {
                        kvString = String.format("%s=%s", key, value);
                    }
                    builder.append(kvString);
                    builder.append("&");
                }
            }
        }
        if (builder.length() == 0) {
            return false;
        }
        if (mMethod != Method.POST_FILE) {
            builder.deleteCharAt(builder.length() - 1);
        }
        if (mMethod == Method.GET) {
            mDirectParams = builder.toString();
            return true;
        }
        writeStringBytes(builder.toString());
        return true;
    }

    /**
     * 构建multipart格式的字符串参数
     *
     * @param builder
     * @param key
     */
    private void buildMultipartString(StringBuilder builder, String key) {
        String value = mStringParamsMap.get(key);
        builder.append("--");
        builder.append(BOUNDARY);
        builder.append(RN);
        builder.append(String.format(STRING_PARAM_FORMAT, key));
        builder.append(RN);
        builder.append(RN);
        builder.append(value);
        builder.append(RN);
    }

    /**
     * 写入字符串流
     */
    private void writeStringBytes(String string) throws IOException {
        byte[] stringBytes = string.getBytes(CHARSET);
        mOutputStream.write(stringBytes);
    }

    /**
     * 构建文件请求体
     *
     * @return 是否含有文件
     *
     * @throws IOException，写入流失败时抛出异常
     */
    private boolean buildFileBody() throws IOException {
        if ((mFiles == null) || (mFiles.size() == 0)) {
            return false;
        }
        for (HttpFileModel fileModel : mFiles) {
            writeStringBytes("--");
            writeStringBytes(BOUNDARY);
            writeStringBytes(RN);
            writeStringBytes(String.format(FILE_PARAM_FORMAT, fileModel.getKey(), fileModel.getFileName()));
            writeStringBytes(RN);
            writeStringBytes("Content-Type: ");
            writeStringBytes(fileModel.getMime());
            writeStringBytes(RN);
            writeStringBytes(RN);
            writeFileStream(fileModel.getFile());
            writeStringBytes(RN);
        }
        return true;
    }

    /**
     * 写入文件流
     */
    private void writeFileStream(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            while (fis.read(buffer) != -1) {
                mOutputStream.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            StreamUtil.closeQuietly(fis);
        }
    }

    /**
     * 写入请求结束符号
     *
     * @throws IOException，写入流失败时抛出异常
     */
    private void writeEndBoundary() throws IOException {
        writeStringBytes("--");
        writeStringBytes(BOUNDARY);
        writeStringBytes("--");
    }
}
