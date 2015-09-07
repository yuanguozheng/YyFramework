/**
 * Copyright (c) 2015 coderyuan.com. All Rights Reserved.
 *
 * YyFramework
 *
 * Constants.java created on 下午3:50
 *
 * @author yuanguozheng
 * @version 1.0.0
 * @since 15/9/7
 */
package com.coderyuan.yyframework.settings;

/**
 * Constants
 *
 * @author yuanguozheng
 */
public interface Constants {

    String RESPONSE_MIME = "application/json;charset=utf-8";

    String CHARSET = "UTF-8";

    String GET = "GET";

    String POST = "POST";

    String JSON_ERROR = "{\"status\":false,\"data\":null,\"err\":\"JSON_ERROR\"}";

    /**
     * Database InitParams Constants
     */
    String DB_DRIVER = "db-driver";
    String DB_URL = "db-url";
    String DB_USERNAME = "db-username";
    String DB_PASSWORD = "db-password";

    String BASE_PACKAGE = "base-package";
}
