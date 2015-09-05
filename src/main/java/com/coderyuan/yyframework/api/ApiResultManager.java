/**
 * Copyright (C) 2015 coderyuan.com. All Rights Reserved.
 * <p>
 * CoderyuanApiLib
 * <p>
 * ApiResultManager.java created on 2015年6月17日
 *
 * @author yuanguozheng
 * @version v1.0.0
 * @since 2015年6月17日
 */
package com.coderyuan.yyframework.api;

import java.util.Objects;

import com.coderyuan.yyframework.models.ResultModel;

public class ApiResultManager {

    public enum ErrorTypes {
        NOT_FOUND, SERVER_ERROR, PARAM_ERROR, DB_ERROR, METHOD_NOT_ALLOW, USER_NOT_EXISTED, USER_EXISTED,
        USER_NOT_AVAILABLE, NOT_LOGIN, VERIFIED_FAILED, ITEM_ADDED, UNKNOWN_ERROR
    }

    private static final int ERROR_STATE = -1;
    private static final int SUCCESS_STATE = 0;

    public static ResultModel getResult(int status, Object data, String error) {
        ResultModel<Object> result = new ResultModel<>();
        result.setStatus(status);
        result.setData(data);
        result.setError(error);
        return result;
    }

    public static ResultModel getErrorResult(String error) {
        return getResult(ERROR_STATE, null, error);
    }

    public static ResultModel getErrorResult(ErrorTypes error) {
        return getErrorResult(error.toString());
    }

    public static ResultModel getSuccessResult(Object data) {
        return getResult(SUCCESS_STATE, data, null);
    }

    public static ResultModel getDefaultSuccessResult() {
        return getSuccessResult(null);
    }

    public static ResultModel getRawResult(String result) {
        ResultModel<String> raw = new ResultModel<>();
        raw.setRawOutput(true);
        raw.setData(result);
        return raw;
    }
}
