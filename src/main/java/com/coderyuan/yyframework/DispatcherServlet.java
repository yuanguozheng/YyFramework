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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;

import com.coderyuan.yyframework.annotations.RequestMethod;
import com.coderyuan.yyframework.api.ApiResultManager;
import com.coderyuan.yyframework.classscanner.ClassScanner;
import com.coderyuan.yyframework.db.DbUtils;
import com.coderyuan.yyframework.models.ApiInfo;
import com.coderyuan.yyframework.models.ErrorTypes;
import com.coderyuan.yyframework.models.RequestParamModel;
import com.coderyuan.yyframework.models.ResultModel;
import com.coderyuan.yyframework.models.ServiceInfoModel;
import com.coderyuan.yyframework.models.ServletHttpModel;
import com.coderyuan.yyframework.settings.Constants;
import com.coderyuan.yyframework.utils.ConsoleLogUtil;
import com.coderyuan.yyframework.utils.JsonUtil;
import com.coderyuan.yyframework.utils.ServletUtil;

/**
 * DispatcherServlet
 *
 * @author yuanguozheng
 */
public class DispatcherServlet extends HttpServlet {

    private static Map<String, ApiInfo> sClassRouteMap = new HashMap<String, ApiInfo>();
    private static long sMaxFileSize = Constants.DEFAULT_FILE_SIZE;
    private static DiskFileItemFactory sDiskFileItemFactory;

    @Override
    public void init() throws ServletException {
        initDebugMode();
        initDataBase();
        initFileSize();
        scanApiClass();
    }

    private void initDebugMode() {
        String isDebug = getServletConfig().getInitParameter(Constants.DEBUG);
        boolean debug;
        try {
            debug = Boolean.parseBoolean(isDebug);
            ConsoleLogUtil.setDebug(debug);
        } catch (Exception e) {
            ConsoleLogUtil.log("Release mode.");
        }
    }

    private void initFileSize() {
        String fileSizeStr = getServletConfig().getInitParameter(Constants.MAX_FILE_UPLOAD_SIZE);
        long size;
        try {
            size = Long.parseLong(fileSizeStr);
            sMaxFileSize = size;
        } catch (Exception e) {
            ConsoleLogUtil.log("Init max file upload size failed, use 1GB.");
        }
    }

    private void scanApiClass() {
        String basePackage = getServletConfig().getInitParameter(Constants.BASE_PACKAGE);
        if (StringUtils.isEmpty(basePackage)) {
            return;
        }
        ClassScanner scanner = new ClassScanner();
        scanner.doScan(basePackage, true);
        sClassRouteMap = scanner.getApis();
        ConsoleLogUtil.log(String.format("Found %d apis.", sClassRouteMap.size()));
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
        ConsoleLogUtil.log(req.getMethod() + " " + req.getRequestURL());
        if (sClassRouteMap.size() == 0) {
            ConsoleLogUtil.log("Not found any api class.");
            JsonUtil.writeJson(res, ApiResultManager.getErrorResult(ErrorTypes.NOT_FOUND));
            return;
        }
        if (sDiskFileItemFactory == null) {
            sDiskFileItemFactory = ServletUtil.newDiskFileItemFactory(getServletContext(),
                    new File(System.getProperty("java.io.tmpdir")));
        }
        req.setCharacterEncoding(Constants.CHARSET);
        String rawPath = req.getRequestURI().replace(req.getContextPath(), "").replace(req.getServletPath(), "");
        ServiceInfoModel serviceInfo = ServletUtil.parsePath(rawPath);
        if (serviceInfo == null) {
            JsonUtil.writeJson(res, ApiResultManager.getErrorResult(ErrorTypes.NOT_FOUND));
            return;
        }
        serviceInfo.setServlet(new ServletHttpModel(req, res));
        invokeApi(serviceInfo);
    }

    private void invokeApi(ServiceInfoModel serviceInfo) {
        HttpServletRequest req = serviceInfo.getServlet().getRequest();
        HttpServletResponse res = serviceInfo.getServlet().getResponse();
        String p = serviceInfo.getFullPath();
        if (StringUtils.isEmpty(p)) {
            JsonUtil.writeJson(res, ApiResultManager.getErrorResult(ErrorTypes.NOT_FOUND));
            return;
        }
        if (!sClassRouteMap.containsKey(p)) {
            JsonUtil.writeJson(res, ApiResultManager.getErrorResult(ErrorTypes.NOT_FOUND));
            return;
        }
        ApiInfo apiInfo = sClassRouteMap.get(p);
        RequestMethod.MethodEnum rm = apiInfo.getRequestMethod();
        if (rm != RequestMethod.MethodEnum.ALL) {
            if (!req.getMethod().toUpperCase().equals(rm.toString())) {
                JsonUtil.writeJson(res, ApiResultManager.getErrorResult(ErrorTypes.METHOD_NOT_ALLOW));
                return;
            }
        }
        doReflect(serviceInfo, apiInfo);
    }

    private void doReflect(ServiceInfoModel serviceInfo, ApiInfo apiInfo) {
        HttpServletRequest req = serviceInfo.getServlet().getRequest();
        HttpServletResponse res = serviceInfo.getServlet().getResponse();
        Class<?> apiClass = apiInfo.getApiClass();
        Object classInstance = null;
        try {
            classInstance = apiClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Method method = apiInfo.getMethod();
        RequestParamModel reqParams = new RequestParamModel(serviceInfo.getServlet());
        if (apiInfo.isFileRequest()) {
            initFileRequestParams(req, reqParams);
        }
        Map<String, String[]> normalParams = req.getParameterMap();
        for (String key : normalParams.keySet()) {
            reqParams.getStringParams().put(key, normalParams.get(key)[0]);
        }
        try {
            ResultModel result = null;
            if (method.getParameterTypes().length == 1) {
                if (method.getParameterTypes()[0] == RequestParamModel.class) {
                    result = (ResultModel) method.invoke(classInstance, reqParams);
                }
            } else if (method.getParameterTypes().length == 0) {
                result = (ResultModel) method.invoke(classInstance);
            }
            if (reqParams.getStringParams().containsKey("callback")) {
                JsonUtil.writeJson(res, result, reqParams.getStringParams().get("callback"));
            } else {
                JsonUtil.writeJson(res, result);
            }
            return;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        JsonUtil.writeJson(res, ApiResultManager.getErrorResult(ErrorTypes.SERVER_ERROR));
    }

    private void initFileRequestParams(HttpServletRequest req, RequestParamModel reqParams) {
        ServletFileUpload fileUpload = new ServletFileUpload();
        fileUpload.setFileItemFactory(sDiskFileItemFactory);
        fileUpload.setFileSizeMax(sMaxFileSize);
        boolean multipartContent = ServletFileUpload.isMultipartContent(req);
        if (!multipartContent) {
            return;
        }
        List<FileItem> items = null;
        try {
            items = fileUpload.parseRequest(req);
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        Iterator<FileItem> iter = null;
        if (items != null) {
            iter = items.iterator();
        }
        if (iter == null) {
            return;
        }
        while (iter.hasNext()) {
            FileItem item = iter.next();
            if (item.isFormField()) {
                reqParams.getStringParams().put(item.getFieldName(), item.getString());
            } else {
                reqParams.getFiles().put(item.getFieldName(), item);
            }
        }
    }

}
