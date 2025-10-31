package org.example.magicDomain.util;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
/**
 * @author 朱文权
 * 该类负责对数据库密码加密，采用md5加密算法
 * */
//加密算法
public class PasswordEncryptor {

    //md5加密算法
    private static final String MD5_ALGORITHM = "MD5";
    public static String encrypt(String data) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(MD5_ALGORITHM);

        byte[] digest = messageDigest.digest(data.getBytes());
        Formatter formatter = new Formatter();

        for (byte b : digest) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }
    //33f87cd64fb3977ef5cc85080e79ea20
    public static void main(String[] args) throws Exception {
        String password="asjdjasjdkakdsfeetrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr";
        System.out.println("加密前"+password+" 长度为："+password.length());
        System.out.println("加密后"+encrypt(password)+" 长度为："+encrypt(password).length());
    }
}
