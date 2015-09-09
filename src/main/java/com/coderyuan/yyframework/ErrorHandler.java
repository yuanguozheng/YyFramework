/**
 * Copyright (c) 2015 coderyuan.com. All Rights Reserved.
 *
 * YyFramework
 *
 * ErrorHandler.java created on 19:06
 *
 * @author yuanguozheng
 * @version 1.0.0
 * @since 2015/9/9 0009
 */
package com.coderyuan.yyframework;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coderyuan.yyframework.settings.Constants;

/**
 * ErrorHandler
 *
 * @author yuanguozheng
 */
public class ErrorHandler extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType(Constants.RESPONSE_MIME);
        res.setCharacterEncoding(Constants.CHARSET);
        PrintWriter writer = res.getWriter();
        writer.write(Constants.BAD_ERROR);
    }
}
