/**
 * Copyright (C) 2015 coderyuan.com. All Rights Reserved.
 *
 * CoderyuanApiLib
 *
 * TokenUtil.java created on 2015年7月9日
 *
 * @author yuanguozheng
 * @since 2015年7月9日
 * @version v1.0.0
 */
package com.coderyuan.yyframework.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

import com.coderyuan.yyframework.hash.HashUtil;

/**
 * @author yuanguozheng
 * 
 */
public class TokenUtil {

    public static String getToken(String key, String[] params) {
        Arrays.sort(params);
        StringBuilder builder = new StringBuilder();
        for (String p : params) {
            builder.append(p);
            builder.append("&");
        }
        builder.deleteCharAt(builder.length() - 1);
        String base64String = null;
        try {
            base64String = Base64.encodeToString(builder.toString().getBytes("UTF-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String secret = null;
        try {
            secret = DesUtil.encode(key, base64String);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String encodeToken = null;
        try {
            encodeToken = URLEncoder.encode(HashUtil.getSHA(secret).trim(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeToken;
    }
}
