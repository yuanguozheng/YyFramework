/**
 * Copyright (c) 2015 coderyuan.com. All Rights Reserved.
 *
 * YyFramework
 *
 * ApiMethodModel.java created on 17:24
 *
 * @author 国正
 * @version 1.0.0
 * @since 2015/9/5 0005
 */
package com.coderyuan.yyframework.models;

import java.lang.reflect.Method;

import com.coderyuan.yyframework.annotations.RequestMethod;

/**
 * ApiMethodModel
 *
 * @author 国正
 */
public class ApiMethodModel {

    private Method mMethod;

    private RequestMethod.MethodEnum mRequestMethod;

    private boolean mIsFileRequest = false;

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
}
