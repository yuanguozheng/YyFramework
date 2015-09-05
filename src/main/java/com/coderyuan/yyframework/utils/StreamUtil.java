/**
 * Copyright (C) 2015 coderyuan.com. All Rights Reserved.
 *
 * Created by: yuanguozheng
 * Created: 2015年4月21日 下午13:52:10
 */
package com.coderyuan.yyframework.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;

import org.apache.commons.lang3.StringUtils;

/**
 * 流工具类
 */
public class StreamUtil {

    private static final int EOF = -1;

    private static final int BUFFER_SIZE = 1024;

    public interface ProgressListenrer {

        public void progress(long current, long total);

    }

    /**
     * Copy the content of the input stream into the output stream, using a temporary byte array buffer whose size is
     * defined by {@link #IO_BUFFER_SIZE}.
     * 
     * @param in The input stream to copy from.
     * @param out The output stream to copy to.
     * @throws java.io.IOException If any error occurs during the copy.
     */
    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = in.read(buffer)) != EOF) {
            out.write(buffer, 0, read);
        }
    }

    public static void copyStream(InputStream in, File outFile) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = FileUtils.openNewFileOutput(outFile);
            copyStream(in, fos);
        } finally {
            closeQuietly(fos);
        }
    }

    public static void copyStream(InputStream in, File outFile, long total, ProgressListenrer l) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = FileUtils.openNewFileOutput(outFile);
            copyStream(in, fos, total, l);
        } finally {
            closeQuietly(fos);
        }
    }

    public static long copyStream(InputStream in, OutputStream out, long total, ProgressListenrer l) throws IOException {
        int read = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        long current = 0;
        while ((read = in.read(buffer)) != EOF) {
            out.write(buffer, 0, read);
            current += read;
            if (l != null) {
                l.progress(current, total);
            }
        }
        return current;
    }

    public static String loadContent(InputStream stream) throws IOException {
        return loadContent(stream, null);
    }

    /**
     * Convert an {@link InputStream} to String.
     * 
     * @param stream the stream that contains data.
     * @param charsetName the encoding of the data.
     * @return the result string.
     * @throws IOException an I/O error occurred.
     */
    public static String loadContent(InputStream stream, String charsetName) throws IOException {
        if (null == stream) {
            throw new IllegalArgumentException("stream may not be null.");
        }
        if (StringUtils.isEmpty(charsetName)) {
            charsetName = System.getProperty("file.encoding", "utf-8");
        }
        final InputStreamReader reader = new InputStreamReader(stream, charsetName);
        final StringWriter writer = new StringWriter();
        final char[] buffer = new char[4 * BUFFER_SIZE];
        int len = reader.read(buffer);
        while (len > 0) {
            writer.write(buffer, 0, len);
            len = reader.read(buffer);
        }
        return writer.toString();
    }

    /**
     * 关闭对象，如：文件流
     * 
     * @param closeable，可关闭对象
     * @return 是否正常关闭
     */
    public static boolean closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}
