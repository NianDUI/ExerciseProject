package top.niandui.common.uitls;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @Title: AESUtil.java
 * @description: AES加密
 * @time: 2020/6/22 16:55
 * @author: liyongda
 * @version: 1.0
 */
public class AESUtil {
    // 加密方式
    private static final String ENCODE_TYPE = "AES";
    // 算法
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    // 默认密匙
    private static final byte[] KEY = {11, -43, 58, 44, 32, -74, -29, 25, -34, -66, -85, 117, -121, -108, 46, -83};

    /**
     * 生成密匙
     *
     * @param length 密钥长度：128/192/256
     * @return 生成的密匙
     */
    public static byte[] generateKey(int length) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ENCODE_TYPE);
            keyGenerator.init(length);
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey.getEncoded();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("密匙生成失败");
        }
    }

    /**
     * AES加密，使用默认密匙
     *
     * @param src 原内容
     * @return 加密后内容
     */
    public static String encode(String src) {
        return encode(src, KEY);
    }

    /**
     * AES加密，使用指定密匙
     *
     * @param src 原内容
     * @param key 密匙
     * @return 加密后内容
     */
    public static String encode(String src, String key) {
        return encode(src, key.getBytes());
    }

    /**
     * AES加密，使用指定密匙
     *
     * @param src 原内容
     * @param key 密匙
     * @return 加密后内容
     */
    public static String encode(String src, byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, ENCODE_TYPE));
            return Base64.getEncoder().encodeToString(cipher.doFinal(src.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("加密");
        }
    }


    /**
     * AES解密，使用默认密匙
     *
     * @param src 原内容
     * @return 解密后内容
     */
    public static String decode(String src) {
        return decode(src, KEY);
    }


    /**
     * AES解密，使用指定密匙
     *
     * @param src 原内容
     * @param key 密匙
     * @return 解密后内容
     */
    public static String decode(String src, String key) {
        return decode(src, key.getBytes());
    }

    /**
     * AES解密，使用指定密匙
     *
     * @param src 原内容
     * @param key 密匙
     * @return 解密后内容
     */
    public static String decode(String src, byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, ENCODE_TYPE));
            return new String(cipher.doFinal(Base64.getDecoder().decode(src)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("解密失败");
        }
    }
}
