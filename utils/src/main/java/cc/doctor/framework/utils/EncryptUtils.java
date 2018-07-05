package cc.doctor.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by doctor on 2017/7/12.
 */
public class EncryptUtils {
    private static final Logger log = LoggerFactory.getLogger(EncryptUtils.class);

    private EncryptUtils() {
    }

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

    /**
     * 数据加密
     *
     * @param data 字符串数据
     * @param pk   公钥（base64编码）
     * @return 加密后的字节序
     */
    public static byte[] rsaEncrypt(String data, String pk) {
        try {
            PublicKey publicKey = getPublicKey(pk);
            Cipher cipher = Cipher.getInstance("RSA");//java默认"RSA"="RSA/ECB/PKCS1Padding"
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data.getBytes());
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 私钥解密
     *
     * @param content    待解密的内容
     * @param privateKey 私钥
     */
    public static byte[] rsaDecrypt(byte[] content, String privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));
            return cipher.doFinal(content);
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public static PublicKey getPublicKey(String publicKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    //将base64编码后的私钥字符串转成PrivateKey实例
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    //生成密钥对
    public static KeyPair genKeyPair(int keyLength) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keyLength);
        return keyPairGenerator.generateKeyPair();
    }
}
