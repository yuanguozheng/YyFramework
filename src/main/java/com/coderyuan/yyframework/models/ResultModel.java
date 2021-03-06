/**
 * Copyright (C) 2015 coderyuan.com. All Rights Reserved.
 * <p>
 * YyFramework
 * <p>
 * ResultModel.java created on 2015年6月17日
 *
 * @author yuanguozheng
 * @version v1.0.0
 * @since 2015年6月17日
 */
package com.coderyuan.yyframework.models;

import com.google.gson.annotations.SerializedName;

public class ResultModel {

    @SerializedName("status")
    private int mStatus = 0;

    @SerializedName("data")
    private Object mData;

    @SerializedName("err")
    private String mError = null;

    private boolean mRawOutput = false;

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public Object getData() {
        return mData;
    }

    public void setData(Object data) {
        mData = data;
    }

    public boolean getRawOutput() {
        return mRawOutput;
    }

    public void setRawOutput(boolean rawOutput) {
        mRawOutput = rawOutput;
    }

    public String getError() {
        return mError;
    }

    public void setError(String error) {
        mError = error;
    }
}
