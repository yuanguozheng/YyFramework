/**
 * Copyright (C) 2015 coderyuan.com. All Rights Reserved.
 * <p>
 * CoderyuanApiLib
 * <p>
 * JsonUtil.java created on 2015年6月17日
 *
 * @author yuanguozheng
 * @version v1.0.0
 * @since 2015年6月17日
 */
package com.coderyuan.yyframework.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.coderyuan.yyframework.api.ApiResultManager;
import com.coderyuan.yyframework.models.ResultModel;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {

    static Gson sGson = new GsonBuilder().setExclusionStrategies(new JsonKit("mRawOutput", "mJsonpMode"))
            .serializeNulls().create();

    public static void writeJson(HttpServletResponse res, ResultModel obj) throws IOException {
        res.setContentType("application/json;\tcharset=utf-8");
        res.setCharacterEncoding("utf-8");
        PrintWriter writer = res.getWriter();
        if (obj == null) {
            obj = ApiResultManager.getErrorResult(ApiResultManager.ErrorTypes.UNKNOWN_ERROR);
        }
        if (obj.getRawOutput()) {
            writer.write(obj.getData().toString());
        } else {
            writer.write(toJson(obj));
        }
    }

    public static String toJson(Object obj) {
        try {
            return sGson.toJson(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"status\":false,\"msg\":\"JSON_ERROR\"}";
        }
    }

    public static void writeJson(HttpServletResponse res, ResultModel obj, String callback) throws IOException {
        res.setContentType("application/json;\tcharset=utf-8");
        res.setCharacterEncoding("utf-8");
        PrintWriter writer = res.getWriter();
        if (obj == null) {
            obj = ApiResultManager.getErrorResult(ApiResultManager.ErrorTypes.UNKNOWN_ERROR);
        }
        if (obj.getRawOutput()) {
            writer.write(String.format("%s(%s)", callback, obj.getData().toString()));
        } else {
            writer.write(String.format("%s(%s)", callback, toJson(obj)));
        }
    }

    static class JsonKit implements ExclusionStrategy {

        static String[] keys;

        public JsonKit(String... keys) {
            JsonKit.keys = keys;
        }

        @Override
        public boolean shouldSkipClass(Class<?> arg0) {
            return false;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes arg0) {
            for (String key : keys) {
                if (key.equals(arg0.getName())) {
                    return true;
                }
            }
            return false;
        }
    }
}
