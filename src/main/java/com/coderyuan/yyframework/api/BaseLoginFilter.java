/**
 * Copyright (C) 2015 coderyuan.com. All Rights Reserved.
 *
 * CoderyuanApiLib
 *
 * BaseLoginFilter.java created on 2015年6月29日
 *
 * @author yuanguozheng
 * @version v1.0.0
 * @since 2015年6月29日
 */
package com.coderyuan.yyframework.api;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.coderyuan.yyframework.api.ApiResultManager.ErrorTypes;
import com.coderyuan.yyframework.utils.JsonUtil;

/**
 * Filter基类
 *
 * @author yuanguozheng
 */
public abstract class BaseLoginFilter implements Filter {

    private HttpServletRequest mRequest;
    private HttpServletResponse mResponse;
    private HttpSession mSession;
    private Map<String, String[]> mParams;

    public abstract boolean isLogin();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        mRequest = (HttpServletRequest) request;
        mResponse = (HttpServletResponse) response;
        mSession = mRequest.getSession();
        if (mSession == null) {
            JsonUtil.writeJson(mResponse, ApiResultManager.getErrorResult(ErrorTypes.NOT_LOGIN));
            return;
        }
        initParams();
        if (!isLogin()) {
            JsonUtil.writeJson(mResponse, ApiResultManager.getErrorResult(ErrorTypes.NOT_LOGIN));
            return;
        }
        chain.doFilter(mRequest, mResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    public HttpServletRequest getRequest() {
        return mRequest;
    }

    public HttpServletResponse getResponse() {
        return mResponse;
    }

    public String getParam(String key) {
        return mParams.containsKey(key) ? mParams.get(key)[0] : null;
    }

    public Object getSessionObject(String key) {
        return mSession.getAttribute(key);
    }

    public void setSessionObject(String key, Object value) {
        mSession.setAttribute(key, value);
    }

    private void initParams() {
        mParams = mRequest.getParameterMap();
    }
}
