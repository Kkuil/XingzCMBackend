package top.kkuily.xingbackend.utils;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

/**
 * @author 小K
 * @description api签名工具类
 */
public class ApiSignAuthUtils {

    /**
     * @param message    String
     * @param privateKey String
     * @return String
     * @throws Exception
     * @description 生成签名
     */
    public static String generateSignature(String message, String privateKey) throws Exception {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKeyObj = keyFactory.generatePrivate(keySpec);

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKeyObj);
        signature.update(message.getBytes("UTF-8"));
        byte[] signedBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signedBytes);
    }

    /**
     * @param message   String
     * @param signature String
     * @param publicKey String
     * @return boolean
     * @throws Exception
     * @description 验证签名
     */
    public static boolean verifySignature(String message, String signature, String publicKey) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKeyObj = keyFactory.generatePublic(keySpec);

        Signature verifySignature = Signature.getInstance("SHA256withRSA");
        verifySignature.initVerify(publicKeyObj);
        verifySignature.update(message.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        return verifySignature.verify(signatureBytes);
    }
}
