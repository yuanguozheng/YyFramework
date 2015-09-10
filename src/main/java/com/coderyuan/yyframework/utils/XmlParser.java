/**
 * Copyright (c) 2015 coderyuan.com. All Rights Reserved.
 * <p>
 * YyFramework
 * <p>
 * XmlParser.java created on 上午11:40
 *
 * @author yuanguozheng
 * @version 1.0.0
 * @since 15/8/6
 */
package com.coderyuan.yyframework.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * XmlParser
 *
 * @author yuanguozheng
 */
public class XmlParser {

    public static Document getDocument(String file) {
        String content = null;
        try {
            content = FileUtils.readTxt(file);
            return DocumentHelper.parseText(content);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getValue(Element e, String key) {
        return e.elementText(key);
    }
}
