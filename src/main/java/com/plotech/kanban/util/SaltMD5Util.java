package com.plotech.kanban.util;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Random;

/**
 * SaltMD5工具类，用于生成和验证加盐的MD5密码。
 */
public class SaltMD5Util {

    /**
     * 生成普通的MD5密码。
     *
     * @param input 要加密的字符串
     * @return MD5加密后的字符串
     */
    public static String MD5(String input) {
        MessageDigest md5 = null;
        try {
            // 生成普通的MD5密码
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return "no such algorithm";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        char[] charArray = input.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     * 生成加盐的MD5密码。
     *
     * @param password 要加密的密码
     * @return 加盐的MD5密码
     */
    public static String generateSaltPassword(String password) {
        Random random = new Random();
        // 生成一个16位的随机数，也就是所谓的盐
        StringBuilder stringBuilder = new StringBuilder(16);
        stringBuilder.append(random.nextInt(99999999)).append(random.nextInt(99999999));
        int len = stringBuilder.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                stringBuilder.append("0");
            }
        }
        // 生成盐
        String salt = stringBuilder.toString();
        // 将盐加到明文中，并生成新的MD5码
        password = md5Hex(password + salt);
        // 将盐混到新生成的MD5码中，之所以这样做是为了后期更方便的校验明文和秘文，也可以不用这么做，不过要将盐单独存下来，不推荐这种方式
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            assert password != null;
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return new String(cs);
    }

    private static String getSalt(String md5) {
        char[] cs = new char[16];
        for (int i = 0; i < 48; i += 3) {
            cs[i / 3] = md5.charAt(i + 1);
        }
        return new String(cs);
    }

    /**
     * 获取加盐加密的MD5中的盐值
     * @param md5 加盐加密的MD5密码
     * @return 盐值
     */
    public static String extractSalt(String md5){
        return getSalt(md5);
    }

    /**
     * 验证加盐的MD5密码。
     *
     * @param password 要验证的密码
     * @param md5      加盐的MD5密码
     * @return 是否验证成功
     */
    public static boolean verifySaltPassword(String password, String md5) {
        // 先从MD5码中取出之前加的盐和加盐后生成的MD5码
        char[] cs = new char[32];
        for (int i = 0; i < 48; i += 3) {
            cs[i / 3 * 2] = md5.charAt(i);
            cs[i / 3 * 2 + 1] = md5.charAt(i + 2);
        }
        String salt = getSalt(md5);
        //比较二者是否相同
        return Objects.equals(md5Hex(password + salt), new String(cs));
    }

    /**
     * 生成MD5码。
     *
     * @param src 要加密的字符串
     * @return MD5加密后的字符串
     */
    private static String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            return new String(new Hex().encode(bs));
        } catch (Exception e) {
            return null;
        }
    }
}