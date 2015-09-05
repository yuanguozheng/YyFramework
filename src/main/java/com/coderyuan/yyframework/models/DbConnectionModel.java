/**
 * Copyright (c) 2015 coderyuan.com. All Rights Reserved.
 * <p>
 * CoderyuanApiLib
 * <p>
 * DbConnectionModel.java created on 上午11:58
 *
 * @author yuanguozheng
 * @version 1.0.0
 * @since 15/8/6
 */
package com.coderyuan.yyframework.models;

/**
 * DbConnectionModel
 *
 * @author yuanguozheng
 */
public class DbConnectionModel {

    private String mDriver;
    private String mUrl;
    private String mUserName;
    private String mPassword;

    public String getDriver() {
        return mDriver;
    }

    public void setDriver(String driver) {
        mDriver = driver;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }
}
