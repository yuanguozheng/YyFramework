/**
 * Copyright (C) 2015 coderyuan.com. All Rights Reserved.
 *
 * CoderyuanApiLib
 *
 * Requester.java created on 2015年7月9日
 *
 * @author yuanguozheng
 * @since 2015年7月9日
 * @version v1.0.0
 */
package com.coderyuan.yyframework.http;

import java.util.HashMap;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.coderyuan.yyframework.http.HttpUtil.Method;

/**
 * @author yuanguozheng
 * 
 */
public class Requester {

    private static HttpUtil sGetUtil;
    private static HttpUtil sPostUtil;

    public static String doGet(String uri) {
        if (sGetUtil == null) {
            sGetUtil = new HttpUtil(Method.GET);
        }
        sGetUtil.setUrl(uri);
        HttpResponseModel response = sGetUtil.request();
        if (!StringUtils.isBlank(response.getData())) {
            return response.getData();
        }
        return null;
    }

    public static String doPost(String uri, HashMap<String, String> params) {
        if (sPostUtil == null) {
            sPostUtil = new HttpUtil(Method.POST);
        }
        sPostUtil.clear();
        sPostUtil.setUrl(uri);
        sPostUtil.setRequestStringParams(params);
        HttpResponseModel response = sPostUtil.request();
        if (!StringUtils.isBlank(response.getData())) {
            return response.getData();
        }
        return null;
    }

    public static String doPost(String uri, TreeMap<String, String> params) {
        if (sPostUtil == null) {
            sPostUtil = new HttpUtil(Method.POST);
        }
        sPostUtil.clear();
        sPostUtil.setUrl(uri);
        for (String key : params.keySet()) {
            sPostUtil.addParam(key, params.get(key));
        }
        HttpResponseModel response = sPostUtil.request();
        if (!StringUtils.isBlank(response.getData())) {
            return response.getData();
        }
        return null;
    }
}
