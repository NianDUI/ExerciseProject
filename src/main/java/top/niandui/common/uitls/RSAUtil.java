package top.niandui.common.uitls;

import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA基于因子分解的非对称称加密。支持公钥加密、私钥解密；支持私钥加密、公钥解密；
 * 参考文档：https://blog.csdn.net/leifchen90/article/details/84749579
 *
 * @author liyongda
 * @version 1.0
 * @date 2020/11/20 9:20
 */
public class RSAUtil {
    // 加密算法
    private static final String RSA = "RSA";
    // 经过java.util.Base64.getEncoder()处理后的公钥编码密钥
    private static final String RSA_PUBLIC_ENCODED_KEY_BASE64 = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJg2Zsf4SrrGAJMSrfsjNJqyk9KxXNqcZUGttaIp/rsBJYyXidXR9r5NbYZ5ahqmNjSlWWxYmMfkPpe5nUFQyxsCAwEAAQ==";
    // 经过java.util.Base64.getEncoder()处理后的私钥编码密钥
    private static final String RSA_PRIVATE_ENCODED_KEY_SPEC_BASE64 = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAmDZmx/hKusYAkxKt+yM0mrKT0rFc2pxlQa21oin+uwEljJeJ1dH2vk1thnlqGqY2NKVZbFiYx+Q+l7mdQVDLGwIDAQABAkBjrv0Q6SLHvvSRXSJj3mKNDXaR/WX6JYKKyOCzGx2gCdCbEm1H7E7rc9nsCf2MAinF70OtBYor8XksHQN3v16BAiEA8HJAEZ6nQo3CM9L6q0KAph3A3C0TuP1blGDvM0V2xEkCIQCiDwPFvbfniMFmROmGpWC+qQrkPez8IEJMzJWL4cgMQwIgeRN+8aBrZxcNCJ2fvidhssRevkAwo0FpISFisfWzpYkCIQCeHfY5TgUMI/NG3D/ZPqxo+jgFP6Jk0Pi/Q/E5XR65TwIhAMqqFLEVYi761Kj8Jz8Mfobl6l74B3YcDIHbxHtq65xj";

    private RSAUtil() {
    }

    /*************************公钥相关**************************/
    // 公钥加密，使用默认公钥编码密钥
    public static String publicKeyEncrypt(String source) {
        return publicKeyEncrypt(source, RSA_PUBLIC_ENCODED_KEY_BASE64);
    }

    // 公钥加密，使用指定公钥编码密钥
    public static String publicKeyEncrypt(String source, String publicEncodedKeyBase64) {
        if (!StringUtils.hasText(source)) {
            return source;
        }
        byte[] result = encryptDecrypt(Cipher.ENCRYPT_MODE, 1, publicEncodedKeyBase64, source.getBytes());
        return Base64.getEncoder().encodeToString(result);
    }

    // 公钥解密，使用默认公钥编码密钥
    public static String publicKeyDecrypt(String source) {
        return publicKeyDecrypt(source, RSA_PUBLIC_ENCODED_KEY_BASE64);
    }

    // 公钥解密，使用指定公钥编码密钥
    public static String publicKeyDecrypt(String source, String publicEncodedKeyBase64) {
        if (!StringUtils.hasText(source)) {
            return source;
        }
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] result = encryptDecrypt(Cipher.DECRYPT_MODE, 1, publicEncodedKeyBase64, decoder.decode(source));
        return new String(result);
    }

    /*************************私钥相关**************************/
    // 私钥加密，使用默认私钥编码密钥
    public static String privateKeyEncrypt(String source) {
        return privateKeyEncrypt(source, RSA_PRIVATE_ENCODED_KEY_SPEC_BASE64);
    }

    // 私钥加密，使用指定私钥编码密钥
    public static String privateKeyEncrypt(String source, String privateEncodedKeyBase64) {
        if (!StringUtils.hasText(source)) {
            return source;
        }
        byte[] result = encryptDecrypt(Cipher.ENCRYPT_MODE, 2, privateEncodedKeyBase64, source.getBytes());
        return Base64.getEncoder().encodeToString(result);
    }

    // 私钥解密，使用默认私钥编码密钥
    public static String privateKeyDecrypt(String source) {
        return privateKeyDecrypt(source, RSA_PRIVATE_ENCODED_KEY_SPEC_BASE64);
    }

    // 私钥解密，使用指定私钥编码密钥
    public static String privateKeyDecrypt(String source, String privateEncodedKeyBase64) {
        if (!StringUtils.hasText(source)) {
            return source;
        }
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] result = encryptDecrypt(Cipher.DECRYPT_MODE, 2, privateEncodedKeyBase64, decoder.decode(source));
        return new String(result);
    }

    /*************************RSA加密解密**************************/
    /**
     * RSA加密解密
     *
     * @param mode             操作模式：Cipher.ENCRYPT_MODE、Cipher.DECRYPT_MODE
     * @param keyType          需要生成密钥的类型：1 公钥、2 私钥
     * @param encodedKeyBase64 base64编码后的密钥：1 公钥、2 私钥
     * @param source           要加密或解密的原文
     * @return 返回加密或解密后的内容
     */
    public static byte[] encryptDecrypt(int mode, int keyType, String encodedKeyBase64, byte[] source) {
        if (mode != Cipher.ENCRYPT_MODE && mode != Cipher.DECRYPT_MODE) {
            throw new RuntimeException("错误的操作模式");
        }
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            // 密钥base64解密
            byte[] encodedKey = Base64.getDecoder().decode(encodedKeyBase64);
            Key key;
            if (keyType == 1) {
                // 1 公钥，密钥规范
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
                key = keyFactory.generatePublic(keySpec);
            } else {
                // 2 私钥，密钥规范
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
                key = keyFactory.generatePrivate(keySpec);
            }
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(mode, key);
            return cipher.doFinal(source);
        } catch (Exception e) {
            throw new RuntimeException(mode == Cipher.ENCRYPT_MODE ? "加密错误！" : "解密错误！", e);
        }
    }

    /**
     * 生成密匙对
     *
     * @return 生成的密钥对的
     */
    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
            keyPairGenerator.initialize(512);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            System.out.println("经过java.util.Base64.getEncoder()处理后的公钥编码密钥 = " + Base64.getEncoder().encodeToString(rsaPublicKey.getEncoded()));
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
            System.out.println("经过java.util.Base64.getEncoder()处理后的私钥编码密钥 = " + Base64.getEncoder().encodeToString(rsaPrivateKey.getEncoded()));
            return keyPair;
        } catch (Exception e) {
            throw new RuntimeException("密钥对生成错误！", e);
        }
    }

    /*************************main测试**************************/
    public static void main(String[] args) {
        // 生成公钥和私钥
//        generateKeyPair();

        String soure = "123456789abc";
        System.out.println("明文 = " + soure);
        System.out.println("-------------------");
        // 公钥加密
        String target = publicKeyEncrypt(soure);
        System.out.println("公钥加密 = " + target);
        // 私钥解密
        soure = privateKeyDecrypt(target);
        System.out.println("私钥解密 = " + soure);

        System.out.println("-------------------");
        // 私钥加密
        target = privateKeyEncrypt(soure);
        System.out.println("私钥加密 = " + target);
        // 公钥解密
        soure = publicKeyDecrypt(target);
        System.out.println("公钥解密 = " + soure);
    }

}
