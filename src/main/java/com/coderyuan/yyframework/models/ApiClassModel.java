/**
 * Copyright (c) 2015 coderyuan.com. All Rights Reserved.
 *
 * YyFramework
 *
 * ApiClassModel.java created on 16:44
 *
 * @author 国正
 * @version 1.0.0
 * @since 2015/9/5 0005
 */
package com.coderyuan.yyframework.models;

import java.util.Map;

import com.coderyuan.yyframework.annotations.RequestMethod;

/**
 * ApiClassModel
 *
 * @author 国正
 */
public class ApiClassModel {

    private Class<?> mApiClass;
    private RequestMethod.MethodEnum mRequestMethod;
    private Map<String, ApiMethodModel> mMethods;

    public Map<String, ApiMethodModel> getMethods() {
        return mMethods;
    }

    public void setMethods(Map<String, ApiMethodModel> methods) {
        mMethods = methods;
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
}
