/**
 * Copyright (c) 2015 coderyuan.com. All Rights Reserved.
 *
 * YyFramework
 *
 * RequestParamModel.java created on 1:11
 *
 * @author 国正
 * @version 1.0.0
 * @since 2015/9/8 0008
 */
package com.coderyuan.yyframework.models;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coderyuan.yyframework.annotations.RequestMethod;

/**
 * RequestParamModel
 *
 * @author 国正
 */
public class RequestParamModel<T> {

    private ServletHttpModel mServlet;

    private T mParams;

    public T getParams() {
        return mParams;
    }

    public void setParams(T params) {
        mParams = params;
    }

    public ServletHttpModel getServlet() {
        return mServlet;
    }

    public void setServlet(ServletHttpModel servlet) {
        mServlet = servlet;
    }
}
