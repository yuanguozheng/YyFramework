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
import com.coderyuan.yyframework.models.RequestParamModel;
import com.coderyuan.yyframework.models.ServiceInfoModel;
import com.coderyuan.yyframework.models.ServletHttpModel;
import com.coderyuan.yyframework.settings.Constants;
import com.coderyuan.yyframework.utils.JsonUtil;

/**
 * DispatcherServlet
 *
 * @author yuanguozheng
 */
public class DispatcherServlet extends HttpServlet {

    private static Map<String, ApiClassModel> sClassRouteMap;

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
        ServiceInfoModel serviceInfo = parsePath(rawPath);
        if (serviceInfo == null) {
            JsonUtil.writeJson(res, ApiResultManager.getErrorResult(ErrorTypes.NOT_FOUND));
            return;
        }
        serviceInfo.setServlet(new ServletHttpModel(req, res));
        ApiClassModel classInstance = createClassInstance(serviceInfo);
        if (classInstance == null) {
            return;
        }
        invokeMethod(serviceInfo, classInstance);
    }

    private ApiClassModel createClassInstance(ServiceInfoModel serviceInfo) {
        HttpServletResponse res = serviceInfo.getServlet().getResponse();
        ApiClassModel clsModel = sClassRouteMap.get(serviceInfo.getClassPath());
        if (clsModel == null) {
            JsonUtil.writeJson(res, ApiResultManager.getErrorResult(ErrorTypes.NOT_FOUND));
            return null;
        }
        if (clsModel.getRequestMethod() != RequestMethod.MethodEnum.ALL) {
            if (!serviceInfo.isMethodAvailable(clsModel.getRequestMethod())) {
                JsonUtil.writeJson(res, ApiResultManager.getErrorResult(ErrorTypes.METHOD_NOT_ALLOW));
                return null;
            }
        }
        return clsModel;
    }

    private void invokeMethod(ServiceInfoModel serviceInfo, ApiClassModel clsModel) {
        HttpServletRequest req = serviceInfo.getServlet().getRequest();
        HttpServletResponse res = serviceInfo.getServlet().getResponse();
        Class<?> cls = clsModel.getApiClass();
        Object classInstance;
        try {
            classInstance = cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            JsonUtil.writeJson(res, ApiResultManager.getErrorResult(ErrorTypes.NOT_FOUND));
            return;
        }
        ApiMethodModel methodModel = clsModel.getMethods().get(serviceInfo.getMethodPath());
        if (methodModel == null) {
            JsonUtil.writeJson(res, ApiResultManager.getErrorResult(ErrorTypes.NOT_FOUND));
            return;
        }
        if (methodModel.getRequestMethod() != RequestMethod.MethodEnum.ALL) {
            if (!req.getMethod().toUpperCase().equals(methodModel.getRequestMethod().toString())) {
                JsonUtil.writeJson(res, ApiResultManager.getErrorResult(ErrorTypes.METHOD_NOT_ALLOW));
                return;
            }
        }
        RequestParamModel reqParams = new RequestParamModel(serviceInfo.getServlet());
        reqParams.setStringParams(req.getParameterMap());
        try {
            methodModel.getMethod().invoke(classInstance, reqParams);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private ServiceInfoModel parsePath(String rawPath) {
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
        return new ServiceInfoModel(classUri, methodUri);
    }
}
