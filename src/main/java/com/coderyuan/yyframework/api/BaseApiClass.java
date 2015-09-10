/**
 * Copyright (C) 2015 coderyuan.com. All Rights Reserved.
 * <p>
 * YyFramework
 * <p>
 * BaseApiClass.java created on 2015年6月24日
 *
 * @author yuanguozheng
 * @version v1.0.0
 * @since 2015年6月24日
 */
package com.coderyuan.yyframework.api;

import javax.servlet.http.HttpSession;

import com.coderyuan.yyframework.models.ResultModel;

/**
 * API基类
 *
 * @author yuanguozheng
 */
public abstract class BaseApiClass {

    protected boolean mAllowGet = true;
    protected BaseWebApiServlet mBase;

    public BaseApiClass(BaseWebApiServlet base) {
        mBase = base;
    }

    public BaseApiClass(BaseWebApiServlet base, boolean allowGet) {
        mBase = base;
        mAllowGet = allowGet;
    }

    public abstract ResultModel handleRequest();

    public String getParam(String name) {
        return mBase.getParam(name);
    }

    protected HttpSession getSession() {
        return mBase.getSession();
    }
}
