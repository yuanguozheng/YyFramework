/**
 * Copyright (C) 2015 Coderyuan.com. All Rights Reserved.
 *
 * JustLib
 *
 * HashUtil.java created on 2015年6月22日
 *
 * @author yuanguozheng
 * @since 2015年6月22日
 * @version v1.0.0
 */
package com.coderyuan.yyframework.hash;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.coderyuan.yyframework.utils.Base64;

/**
 * SHA-1及MD5工具类
 */
public class HashUtil {

    public static String getSHA(String password) {
        MessageDigest mdSha1 = null;
        try {
            mdSha1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        try {
            mdSha1.update(password.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] data = mdSha1.digest();
        String SHAHash = null;
        try {
            SHAHash = convertToHex(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SHAHash;
    }

    public static String getMD5(String password) {
        StringBuffer MD5Hash = new StringBuffer();
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("JsMD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                MD5Hash.append(h);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return MD5Hash.toString();
    }

    private static String convertToHex(byte[] data) throws java.io.IOException {
        StringBuffer sb = new StringBuffer();
        String hex = null;
        hex = Base64.encodeToString(data, 0, data.length, 0);
        sb.append(hex);
        return sb.toString();
    }
}
