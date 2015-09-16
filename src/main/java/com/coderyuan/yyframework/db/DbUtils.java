/**
 * Copyright (c) 2015 coderyuan.com. All Rights Reserved.
 * <p>
 * WlanApp
 * <p>
 * DbUtils.java created on 下午6:41
 *
 * @author yuanguozheng
 * @version 1.0.0
 * @since 15/7/28
 */
package com.coderyuan.yyframework.db;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import com.coderyuan.yyframework.models.DbConnectionModel;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

/**
 * DbUtils
 *
 * @author yuanguozheng
 */
public class DbUtils {

    private static DbUtils sDb;
    private ComboPooledDataSource mDatasource;

    private static String sDriver;
    private static String sUrl;
    private static String sUserName;
    private static String sPassword;

    private DbUtils() {
        try {
            mDatasource = new ComboPooledDataSource();
            mDatasource.setDriverClass(sDriver);
            mDatasource.setJdbcUrl(sUrl);
            mDatasource.setUser(sUserName);
            mDatasource.setPassword(sPassword);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    public static DbUtils getInstance() {
        return sDb;
    }

    public static void init(String driver, String url, String username, String password) {
        sDriver = driver;
        sUrl = url;
        sUserName = username;
        sPassword = password;
        sDb = new DbUtils();
    }

    public static void init(DbConnectionModel dbInfo) {
        init(dbInfo.getDriver(), dbInfo.getUrl(), dbInfo.getUserName(), dbInfo.getPassword());
    }

    public Connection getConn() {
        try {
            return mDatasource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void finalize() throws Throwable {
        DataSources.destroy(mDatasource);
        super.finalize();
    }
}
