/**
 * Copyright (C) 2015 coderyuan.com. All Rights Reserved.
 *
 * YyFramework
 *
 * Requester.java created on 2015年7月9日
 *
 * @author yuanguozheng
 * @version v1.0.0
 * @since 2015年7月9日
 */
package com.coderyuan.yyframework.http;

import org.apache.commons.lang3.StringUtils;

import com.coderyuan.yyframework.http.HttpUtil.Method;

/**
 * 快速HTTP请求工具
 *
 * @author yuanguozheng
 */
public class Requester {

    private static HttpUtil sGetUtil;
    private static HttpUtil sPostUtil;

    public static HttpResponseModel requestWithModel(HttpRequestModel reqModel) {
        if (sGetUtil == null) {
            sGetUtil = new HttpUtil(Method.GET);
        }
        if (sPostUtil == null) {
            sPostUtil = new HttpUtil(Method.POST);
        }
        HttpUtil util = reqModel.getMethod() == Method.GET ? sGetUtil : sPostUtil;
        util.clear();
        if (StringUtils.isNotEmpty(reqModel.getProxyHost()) && reqModel.getProxyPort() != 0) {
            util.setProxy(reqModel.getProxyHost(), reqModel.getProxyPort());
        }
        util.setFollowRedirect(reqModel.isFollowRedirect());
        util.setUrl(reqModel.getUrl());
        util.setHeaders(reqModel.getHeaders());
        util.setRequestStringParams(reqModel.getStringParams());
        util.setNeedUrlencoded(reqModel.getNeedUrlEncoded());
        return util.request();
    }

    public static HttpResponseModel postWithRawUrlParams(String url, String params) {
        if (sPostUtil == null) {
            sPostUtil = new HttpUtil(Method.POST);
        }
        sPostUtil.clear();
        sPostUtil.setUrl(url);
        sPostUtil.setDirectWriteParams(params);
        return sPostUtil.request();
    }
}
