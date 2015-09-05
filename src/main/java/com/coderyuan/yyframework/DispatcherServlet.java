/**
 * Copyright (c) 2015 coderyuan.com. All Rights Reserved.
 *
 * YyFramework
 *
 * DispatcherServlet.java created on 下午4:50
 *
 * @author yuanguozheng
 * @version 1.0.0
 * @since 15/8/31
 */
package com.coderyuan.yyframework;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coderyuan.yyframework.annotations.RequestMethod;
import com.coderyuan.yyframework.api.ApiResultManager;
import com.coderyuan.yyframework.classscanner.ClassScanner;
import com.coderyuan.yyframework.db.DbUtils;
import com.coderyuan.yyframework.models.ApiClassModel;
import com.coderyuan.yyframework.models.ApiMethodModel;
import com.coderyuan.yyframework.utils.JsonUtil;

/**
 * DispatcherServlet
 *
 * @author yuanguozheng
 */
public class DispatcherServlet extends HttpServlet {

    /**
     * Database InitParams Constants
     */
    private static final String DB_DRIVER = "db-driver";
    private static final String DB_URL = "db-url";
    private static final String DB_USERNAME = "db-username";
    private static final String DB_PASSWORD = "db-password";

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

    private static Map<String, ApiClassModel> sClassRouteMap;

    private Class<?> mApiClass = null;
    private Object mApiNewInstance = null;
    private Method mOperationMethod = null;

    private boolean mAllowGet = true;
    private String mRestParam = null;
    private Map<String, String[]> mParams;

    private HttpServletRequest mRequest;
    private HttpServletResponse mResponse;

    @Override
    public void init() throws ServletException {
        initDataBase();
        scanApiClass();
    }

    private void scanApiClass() {
        String basePackage = getServletConfig().getInitParameter("base-package");
        ClassScanner scanner = new ClassScanner();
        scanner.doScan(basePackage, true);
        sClassRouteMap = scanner.getClasses();
    }

    private void initDataBase() {
        String dbDriver = getServletContext().getInitParameter(DB_DRIVER);
        String dbUrl = getServletContext().getInitParameter(DB_URL);
        String dbUserName = getServletContext().getInitParameter(DB_USERNAME);
        String dbPassword = getServletContext().getInitParameter(DB_PASSWORD);
        DbUtils.init(dbDriver, dbUrl, dbUserName, dbPassword);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        StringBuilder reqUri = new StringBuilder(req.getRequestURI().replace(req.getServletPath(), ""));
        int lastLineIndex = reqUri.lastIndexOf("/");
        String methodUri = null;
        String classUri = null;
        if (lastLineIndex == -1 || (lastLineIndex == 0 && reqUri.length() == 1)) {
            JsonUtil.writeJson(res, ApiResultManager.getErrorResult(ApiResultManager.ErrorTypes.NOT_FOUND));
            return;
        }
        while (lastLineIndex == reqUri.length() - 1) {
            reqUri.delete(lastLineIndex, reqUri.length());
            lastLineIndex = reqUri.lastIndexOf("/");
        }
        if (lastLineIndex == 0) {
            methodUri = null;
            classUri = reqUri.toString();
        } else if (lastLineIndex != -1 && lastLineIndex != reqUri.length() - 1) {
            methodUri = reqUri.substring(lastLineIndex);
            reqUri.delete(lastLineIndex, reqUri.length());
            classUri = reqUri.toString();
        }
        try {
            ApiClassModel clsModel = sClassRouteMap.get(classUri);
            if (clsModel == null) {
                JsonUtil.writeJson(res, ApiResultManager.getErrorResult(ApiResultManager.ErrorTypes.NOT_FOUND));
                return;
            }
            if (clsModel.getRequestMethod() != RequestMethod.MethodEnum.ALL) {
                if (!req.getMethod().toUpperCase().equals(clsModel.getRequestMethod().toString())) {
                    JsonUtil.writeJson(res,
                            ApiResultManager.getErrorResult(ApiResultManager.ErrorTypes.METHOD_NOT_ALLOW));
                    return;
                }
            }
            Class<?> cls = clsModel.getApiClass();
            Object instance = cls.newInstance();
            ApiMethodModel methodModel = clsModel.getMethods().get(methodUri);
            if (methodModel == null) {
                JsonUtil.writeJson(res, ApiResultManager.getErrorResult(ApiResultManager.ErrorTypes.NOT_FOUND));
                return;
            }
            if (methodModel.getRequestMethod() != RequestMethod.MethodEnum.ALL) {
                if (!req.getMethod().toUpperCase().equals(methodModel.getRequestMethod().toString())) {
                    JsonUtil.writeJson(res,
                            ApiResultManager.getErrorResult(ApiResultManager.ErrorTypes.METHOD_NOT_ALLOW));
                    return;
                }
            }
            methodModel.getMethod().invoke(instance);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
