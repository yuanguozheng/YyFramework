/**
 * Copyright (c) 2015 coderyuan.com. All Rights Reserved.
 *
 * YyFramework
 *
 * ServiceInfoModel.java created on 1:00
 *
 * @author 国正
 * @version 1.0.0
 * @since 2015/9/8 0008
 */
package com.coderyuan.yyframework.models;

import com.coderyuan.yyframework.annotations.RequestMethod;

/**
 * ServiceInfoModel
 *
 * @author 国正
 */
public class ServiceInfoModel {

    private ServletHttpModel mServlet;

    private String mClassPath;

    private String mMethodPath;

    public ServiceInfoModel(String classPath, String methodPath) {
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

    public boolean isMethodAvailable(RequestMethod.MethodEnum reqMethod) {
        return mServlet.getRequest().getMethod().toUpperCase().equals(reqMethod.toString());
    }

    public ServletHttpModel getServlet() {
        return mServlet;
    }

    public void setServlet(ServletHttpModel servlet) {
        mServlet = servlet;
    }

    public String getFullPath() {
        return String.format("%s%s", mClassPath, mMethodPath);
    }
}
