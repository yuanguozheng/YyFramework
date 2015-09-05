/**
 * Copyright (C) 2015 Coderyuan.com. All Rights Reserved.
 *
 * Created by: yuanguozheng
 * Created: 2015年4月20日 下午4:01:46
 */
package com.coderyuan.yyframework.models;

import java.io.File;

import com.coderyuan.yyframework.utils.FileUtils;

/**
 * 文件信息模型类
 * 
 * @author yuanguozheng
 * 
 */
public class FileModel {

    private String mKey;
    private File mFile;

    /**
     * 获得文件名
     * 
     * @return 文件名，无文件返回null
     */
    public String getFileName() {
        return mFile != null ? mFile.getName() : null;
    }

    /**
     * 获得MIME
     * 
     * @return MIME，无文件返回null
     */
    public String getMime() {
        return mFile != null ? FileUtils.getMimeByName(mFile.getName()) : null;
    }

    /**
     * 获得参数名
     * 
     * @return
     */
    public String getKey() {
        return mKey;
    }

    /**
     * 设置参数名
     * 
     * @param 参数名
     */
    public void setKey(String key) {
        mKey = key;
    }

    /**
     * 获得文件对象
     * 
     * @return 文件对象
     */
    public File getFile() {
        return mFile;
    }

    /**
     * 设置文件对象
     * 
     * @param 文件对象
     */
    public void setFile(File file) {
        mFile = file;
    }
}
