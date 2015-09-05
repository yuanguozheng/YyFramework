/**
 * Copyright (C) 2015 coderyuan.com. All Rights Reserved.
 * <p>
 * CoderyuanApiLib
 * <p>
 * FileUtils.java created on 2015年6月22日
 *
 * @author yuanguozheng
 * @version v1.0.0
 * @since 2015年6月22日
 */
package com.coderyuan.yyframework.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

import eu.medsea.mimeutil.MimeUtil;

/**
 * @author yuanguozheng
 */
public class FileUtils {

    /**
     * 遍历文件夹中文件
     *
     * @param filepath 文件路径
     *
     * @return 返回file［］ 数组
     */
    public static File[] getFileList(String filepath) {
        File d = null;
        File list[] = null;
        /** 建立当前目录中文件的File对象 **/
        try {
            d = new File(filepath);
            if (d.exists()) {
                list = d.listFiles();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /** 取得代表目录中所有文件的File对象数组 **/
        return list;
    }

    /**
     * 读取文本文件内容
     *
     * @return 返回文本文件的内容
     */
    public static String readTxt(String file) {
        InputStreamReader read = null;
        try {
            read = new InputStreamReader(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert read != null;
        BufferedReader bufferedReader = new BufferedReader(read);
        StringBuilder lineTxt = new StringBuilder();
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lineTxt.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineTxt.toString();
    }

    /**
     * 新建目录
     *
     * @param folderPath 目录
     *
     * @return 返回目录创建后的路径
     */
    public static String createFolder(String folderPath) {
        String txt = folderPath;
        try {
            java.io.File myFilePath = new java.io.File(txt);
            txt = folderPath;
            if (!myFilePath.exists()) {
                myFilePath.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return txt;
    }

    /**
     * 多级目录创建
     *
     * @param folderPath 准备要在本级目录下创建新目录的目录路径例如 c:myf
     * @param paths      无限级目录参数，各级目录以单数线区分 例如 a|b|c
     *
     * @return 返回创建文件后的路径
     */
    public static String createFolders(String folderPath, String paths) {
        String txts = folderPath;
        try {
            String txt;
            txts = folderPath;
            StringTokenizer st = new StringTokenizer(paths, "|");
            while (st.hasMoreTokens()) {
                txt = st.nextToken().trim();
                if (txts.lastIndexOf("/") != -1) {
                    txts = createFolder(txts + txt);
                } else {
                    txts = createFolder(txts + txt + "/");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return txts;
    }

    /**
     * 新建文件
     *
     * @param filePathAndName 文本文件完整绝对路径及文件名
     * @param fileContent     文本文件内容
     *
     * @return
     */
    public static void createFile(String filePathAndName, String fileContent) {
        try {
            String filePath = filePathAndName;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            if (!myFilePath.exists()) {
                myFilePath.createNewFile();
            }
            FileWriter resultFile = new FileWriter(myFilePath);
            PrintWriter myFile = new PrintWriter(resultFile);
            String strContent = fileContent;
            myFile.println(strContent);
            myFile.close();
            resultFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 有编码方式的文件创建
     *
     * @param filePathAndName 文本文件完整绝对路径及文件名
     * @param fileContent     文本文件内容
     * @param encoding        编码方式 例如 GBK 或者 UTF-8
     *
     * @return
     */
    public static void createFile(String filePathAndName, String fileContent, String encoding) {
        try {
            String filePath = filePathAndName;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            if (!myFilePath.exists()) {
                myFilePath.createNewFile();
            }
            PrintWriter myFile = new PrintWriter(myFilePath, encoding);
            String strContent = fileContent;
            myFile.println(strContent);
            myFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     *
     * @param filePathAndName 文本文件完整绝对路径及文件名
     *
     * @return Boolean 成功删除返回true遭遇异常返回false
     */
    public static boolean delFile(String filePathAndName) {
        boolean bea = false;
        try {
            String filePath = filePathAndName;
            File myDelFile = new File(filePath);
            if (myDelFile.exists()) {
                myDelFile.delete();
                bea = true;
            } else {
                bea = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bea;
    }

    /**
     * 删除文件
     *
     * @param folderPath 文件夹完整绝对路径
     *
     * @return
     */
    public static void delFolder(String folderPath) {
        try {
            /** 删除完里面所有内容 **/
            delAllFile(folderPath);
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            /** 删除空文件夹 **/
            myFilePath.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除指定文件夹下所有文件
     *
     * @param path 文件夹完整绝对路径
     *
     * @return
     */
    public static boolean delAllFile(String path) {
        boolean bea = false;
        File file = new File(path);
        if (!file.exists()) {
            return bea;
        }
        if (!file.isDirectory()) {
            return bea;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                /** 先删除文件夹里面的文件 **/
                delAllFile(path + "/" + tempList[i]);
                /** 再删除空文件 **/
                delFolder(path + "/" + tempList[i]);
                bea = true;
            }
        }
        return bea;
    }

    /**
     * 复制单个文件
     *
     * @param oldPathFile 准备复制的文件源
     * @param newPathFile 拷贝到新绝对路径带文件名
     *
     * @return
     */
    public static void copyFile(String oldPathFile, String newPathFile) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPathFile);
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(oldPathFile);
                FileOutputStream fs = new FileOutputStream(newPathFile);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    fs.write(buffer, 0, byteread);
                }
                System.out.println(bytesum);
                fs.close();
                inStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制整个文件夹的内容
     *
     * @param oldPath 准备拷贝的目录
     * @param newPath 指定绝对路径的新目录
     *
     * @return
     */
    public static void copyFolder(String oldPath, String newPath) {
        try {
            /** 如果文件夹不存在 则建立新文件 **/
            new File(newPath).mkdirs();
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }
                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                /** 如果是子文件 **/
                if (temp.isDirectory()) {
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 移动文件
     *
     * @param oldPath
     * @param newPath
     *
     * @return
     */
    public static void moveFile(String oldPath, String newPath) {
        copyFile(oldPath, newPath);
        delFile(oldPath);
    }

    /**
     * 移动目录
     *
     * @param oldPath
     * @param newPath
     *
     * @return
     */
    public static void moveFolder(String oldPath, String newPath) {
        copyFolder(oldPath, newPath);
        delFolder(oldPath);
    }

    /**
     * 建立一个可以追加的BufferedReader
     *
     * @param fileDir
     * @param fileName
     *
     * @return
     */
    public static BufferedWriter getWriter(String fileDir, String fileName) {
        try {
            File f1 = new File(fileDir);
            if (!f1.exists()) {
                f1.mkdirs();
            }
            f1 = new File(fileDir, fileName);
            if (!f1.exists()) {
                f1.createNewFile();
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(f1.getPath(), true));
            return bw;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * 得到一个BufferedReader
     *
     * @param fileDir
     * @param fileName
     * @param encoding
     *
     * @return
     */
    public static BufferedReader getReader(String fileDir, String fileName, String encoding) {
        try {
            File file = new File(fileDir, fileName);
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
            BufferedReader br = new BufferedReader(read);
            return br;

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获得去除扩展名后的文件名
     *
     * @param fileName，原始文件名
     *
     * @return 无扩展名的文件名，无文件名返回null
     */
    public static String getFileNameWithoutExtension(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return null;
        }
        String name = fileName;
        int index = fileName.lastIndexOf('.');
        if (index != -1) {
            name = fileName.substring(0, index);
        }
        return name;
    }

    /**
     * 获得扩展名
     *
     * @param 文件名
     *
     * @return 扩展名（不含.），无扩展名返回null
     */
    public static String getExt(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        return index == -1 ? null : fileName.substring(index + 1);
    }

    /**
     * 用文件名获得MIME
     *
     * @param 文件名
     *
     * @return MIME，无文件名返回null
     */
    public static String getMimeByName(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
        Collection<?> mimeTypes = MimeUtil.getMimeTypes(name);
        return mimeTypes.toString();
    }

    public static FileOutputStream openNewFileOutput(File file) throws IOException {
        delFile(file.getAbsolutePath());
        ensureParent(file);
        file.createNewFile();
        return new FileOutputStream(file);
    }

    /**
     * 保证文件父目录存在
     *
     * @param file，文件
     */
    public static boolean ensureParent(final File file) {
        if (null != file) {
            final File parentFile = file.getParentFile();
            if ((null != parentFile) && !parentFile.exists()) {
                parentFile.mkdirs();
            }
            return true;
        } else {
            return false;
        }
    }
}
