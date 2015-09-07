/**
 * Copyright (c) 2015 coderyuan.com. All Rights Reserved.
 *
 * YyFramework
 *
 * ServletHttpModel.java created on 1:17
 *
 * @author 国正
 * @version 1.0.0
 * @since 2015/9/8 0008
 */
package com.coderyuan.yyframework.models;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ServletHttpModel
 *
 * @author 国正
 */
public class ServletHttpModel {

    private HttpServletRequest mRequest;

    private HttpServletResponse mResponse;

    public ServletHttpModel(HttpServletRequest req, HttpServletResponse res) {
        mRequest = req;
        mResponse = res;
    }

    public HttpServletRequest getRequest() {
        return mRequest;
    }

    public void setRequest(HttpServletRequest request) {
        mRequest = request;
    }

    public HttpServletResponse getResponse() {
        return mResponse;
    }

    public void setResponse(HttpServletResponse response) {
        mResponse = response;
    }
}
