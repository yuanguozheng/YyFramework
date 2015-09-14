/**
 * Copyright (c) 2015 coderyuan.com. All Rights Reserved.
 * <p>
 * WlanApp
 * <p>
 * JdbcUtil.java created on 上午10:58
 *
 * @author yuanguozheng
 * @version 1.0.0
 * @since 15/7/27
 */
package com.coderyuan.yyframework.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang3.StringUtils;

/**
 * JdbcUtil
 *
 * @author yuanguozheng
 */
public class JdbcUtil {

    private static QueryRunner sQueryRunner = new QueryRunner();

    public static boolean update(Connection con, String sql, Object... params) {
        if (sql == null || StringUtils.isEmpty(sql)) {
            return false;
        }
        int result = -1;
        try {
            result = sQueryRunner.update(con, sql, params);
            if (!con.getAutoCommit()) {
                con.commit();
            }
        } catch (SQLException e) {
            System.out.println("Execute Update/Insert Failed!");
            System.out.println("SQL: " + sql);
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(con);
        }
        return result > 0;
    }

    public static boolean update(Connection con, String sql, List<Object> params) {
        Object[] paramsArray = convertParams(params);
        return update(con, sql, paramsArray);
    }

    public static Map<String, Object> queryToMap(Connection con, String sql, Object... params) {
        if (sql == null || StringUtils.isEmpty(sql)) {
            return null;
        }
        Map<String, Object> map = null;
        try {
            map = sQueryRunner.query(con, sql, new MapHandler(), params);
        } catch (SQLException e) {
            System.out.println("Execute Query Failed!");
            System.out.println("SQL: " + sql);
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(con);
        }
        return map;
    }

    public static Map<String, Object> queryToMap(Connection con, String sql, List<Object> params) {
        Object[] paramsArray = convertParams(params);
        return queryToMap(con, sql, paramsArray);
    }

    public static List<Map<String, Object>> queryToList(Connection con, String sql, Object... params) {
        if (sql == null || StringUtils.isEmpty(sql)) {
            return null;
        }
        List<Map<String, Object>> list = null;
        try {
            list = sQueryRunner.query(con, sql, new MapListHandler(), params);
        } catch (SQLException e) {
            System.out.println("Execute Query Failed!");
            System.out.println("SQL: " + sql);
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(con);
        }
        return list;
    }

    public static List<Map<String, Object>> queryToList(Connection con, String sql, List<Object> params) {
        Object[] paramsArray = convertParams(params);
        return queryToList(con, sql, paramsArray);
    }

    public static <T> T queryToModel(Connection con, String sql, Class<T> T, Object... params) {
        if (sql == null || StringUtils.isEmpty(sql)) {
            return null;
        }
        T resultObject = null;
        try {
            resultObject = sQueryRunner.query(con, sql, new BeanHandler<T>(T), params);
        } catch (SQLException e) {
            System.out.println("Execute Query Failed!");
            System.out.println("SQL: " + sql);
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(con);
        }
        return resultObject;
    }

    public static <T> T queryToModel(Connection con, String sql, Class<T> T, List<Object> params) {
        Object[] paramsArray = convertParams(params);
        return queryToModel(con, sql, T, paramsArray);
    }

    public static <T> List<T> queryToModelList(Connection con, String sql, Class<T> T, Object... params) {
        if (sql == null || StringUtils.isEmpty(sql)) {
            return null;
        }
        List<T> list = null;
        try {
            list = sQueryRunner.query(con, sql, new BeanListHandler<T>(T), params);
        } catch (SQLException e) {
            System.out.println("Execute Query Failed!");
            System.out.println("SQL: " + sql);
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(con);
        }
        return list;
    }

    public static <T> List<T> queryToModelList(Connection con, String sql, Class<T> T, List<Object> params) {
        Object[] paramsArray = convertParams(params);
        return queryToModelList(con, sql, T, paramsArray);
    }

    public static Object queryToObject(Connection con, String sql, Object... params) {
        if (sql == null || StringUtils.isEmpty(sql)) {
            return null;
        }
        Map<String, Object> objectMap = queryToMap(con, sql, params);
        if (objectMap == null) {
            return null;
        }
        if (objectMap.keySet().size() != 0) {
            String key = objectMap.keySet().toArray()[0].toString();
            return objectMap.get(key);
        } else {
            return null;
        }
    }

    private static Object[] convertParams(List<Object> params) {
        Object[] paramsArray = null;
        if (params != null) {
            if (params.size() != 0) {
                paramsArray = params.toArray();
            }
        }
        return paramsArray;
    }
}
