/**
 * Copyright (c) 2015 coderyuan.com. All Rights Reserved.
 *
 * YyFramework
 *
 * ClassScanner.java created on 17:53
 *
 * @author 国正
 * @version 1.0.0
 * @since 2015/9/5 0005
 */
package com.coderyuan.yyframework.classscanner;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.StringUtils;

import com.coderyuan.yyframework.annotations.RequestMethod;
import com.coderyuan.yyframework.annotations.RequestPath;
import com.coderyuan.yyframework.annotations.RequestWithFile;
import com.coderyuan.yyframework.models.ApiClassModel;
import com.coderyuan.yyframework.models.ApiMethodModel;
import com.coderyuan.yyframework.models.ResultModel;

/**
 * ClassScanner
 *
 * @author 国正
 */
public class ClassScanner {

    private final String CLASS_FILE_SUFFIX = ".class"; // Java字节码文件后缀

    private Map<String, ApiClassModel> classes = new HashMap<>();
    private FilenameFilter javaClassFilter;  // 类文件过滤器,只扫描一级类
    private String packPrefix;  // 包路径根路劲

    public ClassScanner() {
        javaClassFilter = (dir, name) -> {
            // 排除内部类
            return !name.contains("$");
        };
    }

    public Integer doScan(String basePath, String packagePath) throws ClassNotFoundException {
        packPrefix = basePath;
        String packTmp = packagePath.replace('.', '/');
        File dir = new File(basePath, packTmp);
        // 不是文件夹
        if (dir.isDirectory()) {
            scanNormalPackage(dir);
        }
        return classes.size();
    }

    public Integer doScan(String packagePath, boolean recursive) {
        Enumeration<URL> dir;
        String filePackPath = packagePath.replace('.', '/');
        try {
            // 得到指定路径中所有的资源文件
            dir = Thread.currentThread().getContextClassLoader().getResources(filePackPath);
            packPrefix = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            if (System.getProperty("file.separator").equals("\\")) {
                packPrefix = packPrefix.substring(1);
            }
            // 遍历资源文件
            while (dir.hasMoreElements()) {
                URL url = dir.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    File file = new File(url.getPath().substring(1));
                    scanNormalPackage(file);
                }
                // 暂不需要扫描Jar包
                //  else if ("jar".equals(protocol)) {
                //  scanJar(url, recursive);
                //  }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return classes.size();
    }

    private void scanJar(URL url, boolean recursive) throws IOException, ClassNotFoundException {
        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
        JarFile jarFile = jarURLConnection.getJarFile();
        // 遍历Jar包
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) entries.nextElement();
            String fileName = jarEntry.getName();
            if (jarEntry.isDirectory()) {
                if (recursive) {
                }
                continue;
            }
            // .class
            if (fileName.endsWith(CLASS_FILE_SUFFIX)) {
                String className = fileName.substring(0, fileName.indexOf('.')).replace('/', '.');
                //classes.put(className, Class.forName(className));
            }
        }
    }

    private void scanNormalPackage(File dir) throws ClassNotFoundException {
        File[] fs = dir.listFiles(javaClassFilter);
        for (int i = 0; fs != null && i < fs.length; i++) {
            File f = fs[i];
            String path = f.getAbsolutePath();
            // 跳过其他文件
            if (path.endsWith(CLASS_FILE_SUFFIX)) {
                String className = StringUtil.getPackageByPath(f, packPrefix); // 获取包名
                Class<?> cls = Class.forName(className);
                RequestPath requestPath = cls.getAnnotation(RequestPath.class);
                if (requestPath == null) {
                    continue;
                }
                if (StringUtils.isEmpty(requestPath.value())) {
                    continue;
                }
                ApiClassModel classModel = new ApiClassModel();
                classModel.setApiClass(cls);
                RequestMethod requestMethod = cls.getAnnotation(RequestMethod.class);
                if (requestMethod == null) {
                    classModel.setRequestMethod(RequestMethod.MethodEnum.ALL);
                } else {
                    classModel.setRequestMethod(requestMethod.value());
                }
                Map<String, ApiMethodModel> methods = scanMethods(cls);
                classModel.setMethods(methods);
                classes.put(requestPath.value(), classModel);
            }
        }
    }

    private Map<String, ApiMethodModel> scanMethods(Class<?> cls) {
        HashMap<String, ApiMethodModel> methodsMap = new HashMap<>();
        Method[] methods = cls.getMethods();
        for (Method m : methods) {
            RequestPath requestPath = m.getAnnotation(RequestPath.class);
            if (requestPath == null) {
                continue;
            }
            if (StringUtils.isEmpty(requestPath.value())) {
                continue;
            }
            if (m.getModifiers() != Modifier.PUBLIC) {
                continue;
            }
            if (m.getReturnType() != ResultModel.class) {
                continue;
            }
            ApiMethodModel methodModel = new ApiMethodModel();
            methodModel.setMethod(m);
            RequestMethod requestMethod = m.getAnnotation(RequestMethod.class);
            RequestWithFile requestWithFile = m.getAnnotation(RequestWithFile.class);
            if (requestMethod == null) {
                methodModel.setRequestMethod(RequestMethod.MethodEnum.ALL);
            } else {
                methodModel.setRequestMethod(requestMethod.value());
            }
            if (requestWithFile != null) {
                methodModel.setIsFileRequest(true);
            }
            methodsMap.put(requestPath.value(), methodModel);
        }
        return methodsMap;
    }

    public Map<String, ApiClassModel> getClasses() {
        return classes;
    }
}
