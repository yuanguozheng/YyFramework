/*
 * Copyright (c) 2015 coderyuan.com. All rights reserved.
 */
package com.coderyuan.yyframework.models;

import java.lang.reflect.Method;

import com.coderyuan.yyframework.annotations.RequestMethod;

/**
 * ApiInfo
 *
 * @author yuanguozheng
 */
public class ApiInfo {

    private Class<?> mApiClass;

    private RequestMethod.MethodEnum mRequestMethod;

    private Method mMethod;

    private boolean mIsFileRequest = false;

    private boolean mEnableJsonp = false;

    public ApiInfo(ApiClassModel cls, ApiMethodModel mtd) {
        mApiClass = cls.getApiClass();
        mRequestMethod = cls.getRequestMethod() == RequestMethod.MethodEnum.ALL ?
                mtd.getRequestMethod() :
                cls.getRequestMethod();
        mMethod = mtd.getMethod();
        mIsFileRequest = mtd.isFileRequest();
        mEnableJsonp = mtd.isEnableJsonp();
    }

    public Class<?> getApiClass() {
        return mApiClass;
    }

    public void setApiClass(Class<?> apiClass) {
        mApiClass = apiClass;
    }

    public RequestMethod.MethodEnum getRequestMethod() {
        return mRequestMethod;
    }

    public void setRequestMethod(RequestMethod.MethodEnum requestMethod) {
        mRequestMethod = requestMethod;
    }

    public Method getMethod() {
        return mMethod;
    }

    public void setMethod(Method method) {
        mMethod = method;
    }

    public boolean isFileRequest() {
        return mIsFileRequest;
    }

    public void setIsFileRequest(boolean isFileRequest) {
        mIsFileRequest = isFileRequest;
    }

    public boolean isEnableJsonp() {
        return mEnableJsonp;
    }

    public void setEnableJsonp(boolean enableJsonp) {
        mEnableJsonp = enableJsonp;
    }
}
