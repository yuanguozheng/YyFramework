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
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

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
import com.coderyuan.yyframework.models.ErrorTypes;
import com.coderyuan.yyframework.settings.Constants;
import com.coderyuan.yyframework.utils.JsonUtil;

/**
 * DispatcherServlet
 *
 * @author yuanguozheng
 */
public class DispatcherServlet extends HttpServlet {

    private static Map<String, ApiClassModel> sClassRouteMap;

    private Map<String, String[]> mParams;

    @Override
    public void init() throws ServletException {
        initDataBase();
        scanApiClass();
    }

    private void scanApiClass() {
        String basePackage = getServletConfig().getInitParameter(Constants.BASE_PACKAGE);
        ClassScanner scanner = new ClassScanner();
        scanner.doScan(basePackage, true);
        sClassRouteMap = scanner.getClasses();
    }

    private void initDataBase() {
        String dbDriver = getServletContext().getInitParameter(Constants.DB_DRIVER);
        String dbUrl = getServletContext().getInitParameter(Constants.DB_URL);
        String dbUserName = getServletContext().getInitParameter(Constants.DB_USERNAME);
        String dbPassword = getServletContext().getInitParameter(Constants.DB_PASSWORD);
        DbUtils.init(dbDriver, dbUrl, dbUserName, dbPassword);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding(Constants.CHARSET);
        String rawPath = req.getRequestURI().replace(req.getServletPath(), "");
        ServiceInfo serviceInfo = parsePath(rawPath);
        if (serviceInfo == null) {
            JsonUtil.writeJson(res, ApiResultManager.getErrorResult(ErrorTypes.NOT_FOUND));
            return;
        }
        createClassInstance(serviceInfo);
    }

    private Object createClassInstance(HttpServletRequest req, HttpServletResponse res, ServiceInfo serviceInfo) {
        try {
            ApiClassModel clsModel = sClassRouteMap.get(serviceInfo.getClassPath());
            if (clsModel == null) {
                JsonUtil.writeJson(res, ApiResultManager.getErrorResult(ErrorTypes.NOT_FOUND));
                return null;
            }
            if (clsModel.getRequestMethod() != RequestMethod.MethodEnum.ALL) {
                if (!req.getMethod().toUpperCase().equals(clsModel.getRequestMethod().toString())) {
                    mJsonUtil.writeJson(ApiResultManager.getErrorResult(ErrorTypes.METHOD_NOT_ALLOW));
                    return null;
                }
            }
            Class<?> cls = clsModel.getApiClass();
            return cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            mJsonUtil.writeJson(ApiResultManager.getErrorResult(ErrorTypes.SERVER_ERROR));
            return null;
        }
    }

    private void invokeMethod(HttpServletRequest req, ServiceInfo serviceInfo,
                              ApiClassModel clsModel, Object instance)
            throws IOException, IllegalAccessException, InvocationTargetException {
        ApiMethodModel methodModel = clsModel.getMethods().get(serviceInfo.getMethodPath());
        if (methodModel == null) {
            mJsonUtil.writeJson(ApiResultManager.getErrorResult(ErrorTypes.NOT_FOUND));
            return;
        }
        if (methodModel.getRequestMethod() != RequestMethod.MethodEnum.ALL) {
            if (!req.getMethod().toUpperCase().equals(methodModel.getRequestMethod().toString())) {
                mJsonUtil.writeJson(ApiResultManager.getErrorResult(ErrorTypes.METHOD_NOT_ALLOW));
                return;
            }
        }
        methodModel.getMethod().invoke(instance);
    }

    private ServiceInfo parsePath(String rawPath) {
        StringBuilder reqUri = new StringBuilder(rawPath);
        int lastLineIndex = reqUri.lastIndexOf("/");
        String methodUri = null;
        String classUri = null;
        if (lastLineIndex == -1 || (lastLineIndex == 0 && reqUri.length() == 1)) {
            return null;
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
        return new ServiceInfo(classUri, methodUri);
    }

    static class ServiceInfo {

        private String mRequestMethod;

        private HttpServletResponse mResponse;

        private String mClassPath;

        private String mMethodPath;

        public ServiceInfo(String classPath, String methodPath) {
            mClassPath = classPath;
            mMethodPath = methodPath;
        }

        public String getClassPath() {
            return mClassPath;
        }

        public void setClassPath(String classPath) {
            mClassPath = classPath;
        }

        public String getMethodPath() {
            return mMethodPath;
        }

        public void setMethodPath(String methodPath) {
            mMethodPath = methodPath;
        }

        public String getRequestMethod() {
            return mRequestMethod;
        }

        public void setRequestMethod(String requestMethod) {
            mRequestMethod = requestMethod.toUpperCase();
        }
    }
}
