package com.coderyuan.yyframework.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.coderyuan.yyframework.models.FileModel;
import com.coderyuan.yyframework.utils.StreamUtil;

public class HttpUtil {

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

    private static final String STRINGPARAM_FORMAT = "Content-Disposition: form-data; name=\"%s\"";

    private static final String FILEPARMA_FORMAT = "Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"";

    private static final int TIME_OUT = 30 * 1000;

    private static final boolean CACHES = false;

    private static final String MIME_FORMAT = "%s;boundary=%s";

    private static final String KEEP_ALIVE = "Keep-Alive";

    private URL mUrl;
    private HttpURLConnection mConnection;
    private OutputStream mOutputStream;
    private HashMap<String, String> mStringParamsMap;
    private ArrayList<FileModel> mFiles;
    private Method mMethod;
    private String mServerUrl;
    private Map<String, List<String>> mResponseHeaders;
    private Map<String, String> mRequestHeaders;
    private boolean mDirectWriteParams;

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
        if (mMethod == Method.POST_FILE) {
            mFiles = new ArrayList<FileModel>();
        }
    }

    public HttpResponseModel request() {
        try {
            buildRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = doRequest();
        HttpResponseModel data = new HttpResponseModel();
        data.setData(result);
        data.setHeaders(mResponseHeaders);
        return data;
    }

    /**
     * 添加字符串参数
     *
     * @param 键
     * @param 值
     */
    public void addParam(String key, String value) {
        if (mStringParamsMap == null) {
            mStringParamsMap = new HashMap<String, String>();
        }
        mStringParamsMap.put(key, value);
    }

    /**
     * 添加单个文件参数
     *
     * @param 文件信息
     */
    public void addFile(FileModel fileModel) {
        if (mMethod != Method.POST_FILE) {
            return;
        }
        if (mFiles == null) {
            mFiles = new ArrayList<FileModel>();
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
     *
     * @param isDirect
     * @param params
     */
    public void setDirectWriteParams(String params) {
        mDirectWriteParams = true;
    }

    /**
     * 添加文件列表
     *
     * @param 文件列表
     */
    public void setFiles(ArrayList<FileModel> files) {
        if (mMethod == Method.POST_FILE) {
            mFiles = files;
        }
    }

    public void setHeaders(Map<String, String> headers) {
        mRequestHeaders = headers;
    }

    public void setRequestStringParams(HashMap<String, String> map) {
        mStringParamsMap = map;
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
    }

    /**
     * 开始请求
     *
     * @return 服务器返回字符串，出错返回null
     */
    private String doRequest() {
        try {
            mResponseHeaders = mConnection.getHeaderFields();
            BufferedReader reader = new BufferedReader(new InputStreamReader(mConnection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
        StringBuilder builder = new StringBuilder();
        try {
            if (mServerUrl.contains("?")) {
                buildStringParam(builder, true);
            }
            mUrl = new URL(mServerUrl + builder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (mUrl == null) {
            return;
        }
        setHeaders();
        if (mConnection == null) {
            return;
        }
        if (mMethod == Method.GET) {
            return;
        }
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

    /**
     * 设置请求头
     */
    private void setHeaders() {
        try {
            mConnection = (HttpURLConnection) mUrl.openConnection();
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
     * @throws IOException
     */
    private boolean buildStringParam() throws IOException {
        return buildStringParam(null, false);
    }

    /**
     * 构建字符串参数请求体（支持外传builder）
     *
     * @param outBuilder，外部builder
     *
     * @return 是否含有字符串参数
     *
     * @throws IOException，写入流失败时抛出异常
     */
    private boolean buildStringParam(StringBuilder outBuilder, boolean append) throws IOException {
        if ((mStringParamsMap == null) || (mStringParamsMap.size() == 0)) {
            return false;
        }
        StringBuilder builder = new StringBuilder();
        if ((mMethod == Method.GET) && !append) {
            builder.append("?");
        }
        for (String key : mStringParamsMap.keySet()) {
            if (mMethod == Method.POST_FILE) {
                buildMultipartString(builder, key);
            } else {
                buildUrlencodedString(builder, key);
                builder.append("&");
            }
        }
        if (mMethod != Method.POST_FILE) {
            builder.deleteCharAt(builder.length() - 1);
        }
        if (mMethod == Method.GET) {
            outBuilder = builder;
            return true;
        }
        if (builder.length() != 0) {
            writeStringBytes(builder.toString());
            return true;
        }
        return false;
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
        builder.append(String.format(STRINGPARAM_FORMAT, key));
        builder.append(RN);
        builder.append(RN);
        builder.append(value);
        builder.append(RN);
    }

    /**
     * 构建Url转码格式的字符串参数
     *
     * @param builder，字符串builder
     * @param key，键
     *
     * @throws UnsupportedEncodingException，URL转码失败时抛出异常
     */
    private void buildUrlencodedString(StringBuilder builder, String key) throws UnsupportedEncodingException {
        String value = mStringParamsMap.get(key);
        String kvString = String.format("%s=%s", key, value);
        builder.append(kvString);
    }

    /**
     * 写入字符串流
     *
     * @param 字符串
     *
     * @throws IOException，写入流失败时抛出异常
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
        for (FileModel fileModel : mFiles) {
            StringBuilder builder = new StringBuilder();
            builder.append("--");
            builder.append(BOUNDARY);
            builder.append(RN);
            builder.append(String.format(FILEPARMA_FORMAT, fileModel.getKey(), fileModel.getFileName()));
            builder.append(RN);
            builder.append("Content-Type: " + fileModel.getMime());
            builder.append(RN);
            builder.append(RN);
            writeStringBytes(builder.toString());
            writeFileStream(fileModel.getFile());
            writeStringBytes(RN);
        }
        return true;
    }

    /**
     * 写入文件流
     *
     * @param 文件
     */
    private void writeFileStream(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = fis.read(buffer)) != -1) {
                mOutputStream.write(buffer);
            }
            System.out.println("Flie Length: " + len);
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
