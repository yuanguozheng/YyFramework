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

    String JSON_ERROR = "{\"status\":-1,\"data\":null,\"err\":\"JSON_ERROR\"}";

    String BAD_ERROR = "{\"status\":-1,\"data\":null,\"err\":\"BAD_SERVER_ERROR\"}";

    /**
     * Database InitParams Constants
     */
    String DB_DRIVER = "db-driver";
    String DB_URL = "db-url";
    String DB_USERNAME = "db-username";
    String DB_PASSWORD = "db-password";

    String BASE_PACKAGE = "base-package";

    String DEBUG = "debug";

    String MAX_FILE_UPLOAD_SIZE = "max-file-size";

    long KB = 1024;
    long MB = KB * KB;
    long GB = MB * KB;
    long TB = GB * KB;

    long DEFAULT_FILE_SIZE = GB;

    String LOG_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
}
