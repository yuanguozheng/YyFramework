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

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;

import com.coderyuan.yyframework.utils.TimeUtils;

/**
 * RequestParamModel
 *
 * @author 国正
 */
public class RequestParamModel {

    private ServletHttpModel mServlet;

    private Map<String, String> mStringParams;

    private Map<String, FileItem> mFiles;

    public RequestParamModel(ServletHttpModel servlet) {
        mServlet = servlet;
        mStringParams = new HashMap<>();
        mFiles = new HashMap<>();
    }

    public Map<String, String> getStringParams() {
        return mStringParams;
    }

    public void setStringParams(Map<String, String> stringParams) {
        mStringParams = stringParams;
    }

    public ServletHttpModel getServlet() {
        return mServlet;
    }

    public void setServlet(ServletHttpModel servlet) {
        mServlet = servlet;
    }

    public Object getParamsModel(Class<?> modelClass) {
        try {
            Object instance = modelClass.newInstance();
            for (String pName : mStringParams.keySet()) {
                try {
                    Field field = modelClass.getDeclaredField(pName);
                    field.setAccessible(true);
                    Object desValue = convertType(field.getType(), mStringParams.get(pName));
                    field.set(instance, desValue);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
            for (String fpName : mFiles.keySet()) {
                try {
                    Field field = modelClass.getDeclaredField(fpName);
                    field.setAccessible(true);
                    Object desValue = (FileItem) mFiles.get(fpName);
                    field.set(instance, desValue);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
            return instance;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object convertType(Type desType, String rawValue) {
        Object obj;
        try {
            if (desType == String.class) {
                return rawValue;
            } else if (desType == Integer.TYPE) {
                obj = Integer.parseInt(rawValue);
            } else if (desType == Double.TYPE) {
                obj = Double.parseDouble(rawValue);
            } else if (desType == Float.TYPE) {
                obj = Float.parseFloat(rawValue);
            } else if (desType == Long.TYPE) {
                obj = Long.parseLong(rawValue);
            } else if (desType == Date.class) {
                obj = TimeUtils.parseDate(rawValue);
            } else if (desType == Short.class) {
                obj = Short.parseShort(rawValue);
            } else if (desType == Byte.class) {
                obj = Byte.parseByte(rawValue);
            } else if (desType == Boolean.class) {
                obj = Boolean.parseBoolean(rawValue);
            } else {
                obj = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            obj = null;
        }
        return obj;
    }

    public Map<String, FileItem> getFiles() {
        return mFiles;
    }

    public void setFiles(Map<String, FileItem> files) {
        mFiles = files;
    }
}
