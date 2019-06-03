package com.dlb;

import org.apache.hadoop.hive.ql.exec.UDF;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * dlb 专用
 *
 * @author <a href="mailto:huoguo@2dfire.com">火锅</a>
 * @time 2019/5/29
 */
public class TokenChange extends UDF {


    public static void main(String[] args) {
        System.out.println(new TokenChange().parseToken(""));
    }

    public String evaluate(String str) {
        try {
            if(str != null){
                str = str.trim();
            }
            return parseToken(str);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(str);
        }
        return "error:0";
    }

    /**
     * The Constant DES.
     */
    private final static String DES = "DES";

    /**
     * 分隔符
     */
    private final static String SEPARATOR = "|";

    /**
     * //ResourceBundle.getBundle("key").getString("encryptKey");
     * 加密key
     */
    private final static String encryptKey = "mallcaiMALLCAI";

    public String parseToken(String deveiceToken) {
        deveiceToken = deveiceToken.replace(" ", "+");
        return parse(deveiceToken);
    }


    static public String parse(String userDeveiceKey) {
        try {
            String tempKey = decrypt(userDeveiceKey, encryptKey);
            String[] keys = tempKey.split("[" + SEPARATOR + "]");
            return keys[0];
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String data, String key) throws IOException,
            Exception {
        if (data == null)
            return null;
        byte[] buf = Base64.getDecoder().decode(data);
        byte[] bt = decrypt(buf, key.getBytes());
        return new String(bt);
    }

    /**
     * 根据键值进行解密
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        Cipher cipher = cipherInit(data, key, Cipher.DECRYPT_MODE);
        return cipher.doFinal(data);
    }

    private static Cipher cipherInit(byte[] data, byte[] key, int cipherValue)
            throws Exception {
        /** 生成一个可信任的随机数源 **/
        SecureRandom sr = new SecureRandom();
        /** 从原始密钥数据创建DESKeySpec对象 **/
        DESKeySpec dks = new DESKeySpec(key);
        /** 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象 **/
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        /** Cipher对象实际完成加密或解密操作 **/
        Cipher cipher = Cipher.getInstance(DES);
        /** 用密钥初始化Cipher对象 **/
        cipher.init(cipherValue, securekey, sr);
        return cipher;
    }


}

