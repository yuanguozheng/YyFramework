/**
 * Copyright (C) 2015 coderyuan.com. All Rights Reserved.
 * <p>
 * CoderyuanApiLib
 * <p>
 * ResultModel.java created on 2015年6月17日
 *
 * @author yuanguozheng
 * @version v1.0.0
 * @since 2015年6月17日
 */
package com.coderyuan.yyframework.models;

import com.google.gson.annotations.SerializedName;

public class ResultModel<T> {

    @SerializedName("status")
    private int mStatus = 0;

    @SerializedName("data")
    private T mData;

    @SerializedName("err")
    private String mError = null;

    private boolean mRawOutput = false;

    private boolean mJsonpMode = false;

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public T getData() {
        return mData;
    }

    public void setData(T data) {
        mData = data;
    }

    public boolean isRawOutput() {
        return mRawOutput;
    }

    public boolean getRawOutput() {
        return mRawOutput;
    }

    public void setRawOutput(boolean rawOutput) {
        mRawOutput = rawOutput;
    }

    public boolean isJsonpMode() {
        return mJsonpMode;
    }

    public void useJsonp() {
        mJsonpMode = true;
    }

    public String getError() {
        return mError;
    }

    public void setError(String error) {
        mError = error;
    }
}
