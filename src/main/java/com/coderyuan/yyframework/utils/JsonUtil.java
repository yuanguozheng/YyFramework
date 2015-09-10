/**
 * Copyright (C) 2015 coderyuan.com. All Rights Reserved.
 * <p>
 * YyFramework
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

import org.apache.commons.lang3.StringUtils;

import com.coderyuan.yyframework.api.ApiResultManager;
import com.coderyuan.yyframework.models.ErrorTypes;
import com.coderyuan.yyframework.models.ResultModel;
import com.coderyuan.yyframework.settings.Constants;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {

    private static Gson sSerialization = new GsonBuilder()
            .setExclusionStrategies(new JsonKit("mRawOutput"))
            .serializeNulls().create();

    private static Gson sDeserialization = new Gson();

    private HttpServletResponse mResponse;

    public JsonUtil(HttpServletResponse response) {
        mResponse = response;
    }

    public static Gson getGson() {
        return sDeserialization;
    }

    public static String toJson(Object obj) {
        try {
            return sSerialization.toJson(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return Constants.JSON_ERROR;
        }
    }

    public static void writeJson(HttpServletResponse res, ResultModel obj) {
        try {
            doWrite(res, obj, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeJson(HttpServletResponse res, ResultModel obj, String callback) {
        try {
            doWrite(res, obj, callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void doWrite(HttpServletResponse res, ResultModel obj, String callback) throws IOException {
        res.setContentType(Constants.RESPONSE_MIME);
        res.setCharacterEncoding(Constants.CHARSET);
        PrintWriter writer = res.getWriter();
        if (obj == null) {
            obj = ApiResultManager.getErrorResult(ErrorTypes.UNKNOWN_ERROR);
        }
        if (!StringUtils.isEmpty(callback)) {
            writer.write(callback + "(");
        }
        if (obj.getRawOutput()) {
            writer.write(obj.getData().toString());
        } else {
            writer.write(toJson(obj));
        }
        if (!StringUtils.isEmpty(callback)) {
            writer.write(")");
        }
    }

    public void writeJson(ResultModel obj) {
        writeJson(mResponse, obj);
    }

    public void writeJson(ResultModel obj, String callback) {
        writeJson(mResponse, obj, callback);
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
