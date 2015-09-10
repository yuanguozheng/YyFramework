/**
 * Copyright (C) 2015 coderyuan.com. All Rights Reserved.
 * <p>
 * YyFramework
 * <p>
 * BaseWebApiServlet.java created on 2015年6月17日
 *
 * @author yuanguozheng
 * @version v1.0.0
 * @since 2015年6月17日
 */
package com.coderyuan.yyframework.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import com.coderyuan.yyframework.models.ErrorTypes;
import com.coderyuan.yyframework.models.ResultModel;
import com.coderyuan.yyframework.utils.JsonUtil;

/**
 * Api基类
 *
 * @author yuanguozheng
 */
@SuppressWarnings("serial")
@Deprecated
public class BaseWebApiServlet extends HttpServlet {

    /**
     * Charsets
     */
    private static final String DES_CHARSET = "utf-8";

    /**
     * Code Names
     */
    private static final String API_EXT = "Api";
    private static final String ENTER_METHOD_NAME = "handleRequest";
    private static final String GET_FLAG_NAME = "mAllowGet";

    /**
     * Formats
     */
    private static final String FULL_FORMAT = "%s.%s";
    private static final String CLASS_FORMAT = "%s%s";

    private Class<?> mApiClass = null;
    private Object mApiNewInstance = null;
    private Method mOperationMethod = null;

    private boolean mAllowGet = true;
    private String mRestParam = null;
    private Map<String, String[]> mParams;

    private HttpServletRequest mRequest;
    private HttpServletResponse mResponse;

    public BaseWebApiServlet() {
        super();
    }

    public BaseWebApiServlet(boolean isAllowGet) {
        super();
        setAllowGet(isAllowGet);
    }

    public void setAllowGet(boolean allowGet) {
        mAllowGet = allowGet;
    }

    public boolean isAllowGet() {
        return mAllowGet;
    }

    public ResultModel doOperation() {
        String packageName = this.getClass().getPackage().getName();
        String restParam = getRestParam();
        if (StringUtils.isEmpty(restParam)) {
            restParam = packageName;
        }
        String className = String.format(CLASS_FORMAT, StringUtils.capitalize(restParam), API_EXT);
        String apiClassName = String.format(FULL_FORMAT, packageName, className);
        try {
            mApiClass = Class.forName(apiClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return ApiResultManager.getErrorResult(ErrorTypes.NOT_FOUND);
        }
        try {
            Constructor<?> constructor = mApiClass.getConstructor(BaseWebApiServlet.class);
            mApiNewInstance = constructor.newInstance(this);
            Field allowGetField = mApiClass.getSuperclass().getDeclaredField(GET_FLAG_NAME);
            allowGetField.setAccessible(true);
            mAllowGet = (boolean) allowGetField.get(mApiNewInstance);
            if (!mAllowGet && mRequest.getMethod() == "GET") {
                return ApiResultManager.getErrorResult(ErrorTypes.METHOD_NOT_ALLOW);
            }
            mOperationMethod = mApiClass.getMethod(ENTER_METHOD_NAME);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResultManager.getErrorResult(ErrorTypes.NOT_FOUND);
        }
        try {
            return (ResultModel) mOperationMethod.invoke(mApiNewInstance);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResultManager.getErrorResult(ErrorTypes.SERVER_ERROR);
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (!mAllowGet) {
            JsonUtil.writeJson(res, ApiResultManager.getErrorResult(ErrorTypes.METHOD_NOT_ALLOW));
            return;
        }
        procRequest(req, res);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        procRequest(req, res);
    }

    public String getRequestBody() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(mRequest.getInputStream(), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuffer reString = new StringBuffer();
        String tmp = null;
        while (true) {
            try {
                tmp = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (tmp == null) {
                break;
            } else {
                reString.append(tmp);
            }
        }
        return reString.toString();
    }

    public Map<String, String[]> getAllParams() {
        return mParams == null ? null : mParams;
    }

    public String getParam(String key) {
        return mParams.containsKey(key) ? mParams.get(key)[0] : null;
    }

    public String getRestParam() {
        return StringUtils.isBlank(mRestParam) ? null : mRestParam;
    }

    public HttpSession getSession() {
        return getRequest() != null ? getRequest().getSession() : null;
    }

    public HttpServletRequest getRequest() {
        return mRequest;
    }

    public HttpServletResponse getResponse() {
        return mResponse;
    }

    private void initRestParam(HttpServletRequest req) {
        mRestParam = req.getPathInfo().replaceAll("/", "");
    }

    private void procRequest(HttpServletRequest req, HttpServletResponse res) throws UnsupportedEncodingException,
            IOException {
        mRequest = req;
        mResponse = res;
        req.setCharacterEncoding(DES_CHARSET);
        initParams(req);
        if (mParams.containsKey("callback")) {
            JsonUtil.writeJson(res, doOperation(), mParams.get("callback")[0]);
        } else {
            JsonUtil.writeJson(res, doOperation());
        }
    }

    private void initParams(HttpServletRequest req) throws UnsupportedEncodingException {
        mParams = req.getParameterMap();
        initRestParam(req);
    }
}