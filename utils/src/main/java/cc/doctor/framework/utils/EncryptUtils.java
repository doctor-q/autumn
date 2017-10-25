package cc.doctor.framework.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by doctor on 2017/7/12.
 */
public class EncryptUtils {
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static String bytes2HexString(byte[] b) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret.append(hex);
        }
        return ret.toString();
    }

    //保持一直，自己做padding
    private static String padding(String text) {
        if (text.length() >= 16) {
            return text.substring(0, 16);
        }
        int miss = 16 - text.length() % 16;
        StringBuilder secretKeyBuilder = new StringBuilder(text);
        for (int i = 0; i < miss; i++) {
            secretKeyBuilder.append((char) 0);
        }
        return secretKeyBuilder.toString();
    }

    public static String encrypt(String content, String secretKey) throws Exception {
        secretKey = padding(secretKey);
        content = padding(content);
        SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        IvParameterSpec iv = new IvParameterSpec(secretKey.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(content.getBytes());
        return bytes2HexString(encrypted);
    }


    public static String decrypt(String cipherText, String secretKey) throws Exception {
        secretKey = padding(secretKey);
        SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        IvParameterSpec iv = new IvParameterSpec(secretKey.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] encrypted1 = hexStringToBytes(cipherText);
        byte[] original = cipher.doFinal(encrypted1);
        return new String(original).trim();
    }
}
