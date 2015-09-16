/*
 * Copyright (c) 2015 coderyuan.com. All rights reserved.
 */
package com.coderyuan.yyframework.utils;

import java.io.File;

import javax.servlet.ServletContext;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.io.FileCleaningTracker;

import com.coderyuan.yyframework.models.ServiceInfoModel;

/**
 * ServletUtil
 *
 * @author yuanguozheng
 */
public class ServletUtil {

    public static ServiceInfoModel parsePath(String rawPath) {
        StringBuilder reqUri = new StringBuilder(rawPath);
        int lastLineIndex = reqUri.lastIndexOf("/");
        String methodUri = null;
        String classUri = null;
        if (lastLineIndex == -1 || (lastLineIndex == 0 && reqUri.length() == 1)) {
            return null;
        }
        while (lastLineIndex == reqUri.length() - 1) {
            reqUri.delete(lastLineIndex, reqUri.length());
            lastLineIndex = reqUri.lastIndexOf("/");
        }
        if (lastLineIndex == 0) {
            methodUri = null;
            classUri = reqUri.toString();
        } else if (lastLineIndex != -1 && lastLineIndex != reqUri.length() - 1) {
            methodUri = reqUri.substring(lastLineIndex);
            reqUri.delete(lastLineIndex, reqUri.length());
            classUri = reqUri.toString();
        }
        return new ServiceInfoModel(classUri, methodUri);
    }

    public static DiskFileItemFactory newDiskFileItemFactory(ServletContext context, File repository) {
        FileCleaningTracker fileCleaningTracker = FileCleanerCleanup.getFileCleaningTracker(context);
        DiskFileItemFactory factory =
                new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository);
        factory.setFileCleaningTracker(fileCleaningTracker);
        return factory;
    }
}
