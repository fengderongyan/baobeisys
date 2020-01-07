package com.sgy.util.common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/** 
 * @description DES加密解密算法工具类
 * @date 2013-10-10 
 */  
public class DESUtil
{
    Key key;

    public DESUtil()
    {}

    /**
     * @description 构造方法
     * @param str 密钥
     */
    public DESUtil(String str)
    {
        setKey(str); // 生成密匙
    }

    public Key getKey()
    {
        return key;
    }

    public void setKey(Key key)
    {
        this.key = key;
    }

    /**
     * 根据参数生成密钥 KEY
     */
    public void setKey(String strKey)
    {
        try
        {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");  
            secureRandom.setSeed(strKey.getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("DES");
            kg.init(secureRandom);
            this.key = kg.generateKey();
            kg = null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 加密 String 明文输入, String 密文输出
     */
    public String encryptStr(String strMing)
    {
        byte[] byteMi = null;
        byte[] byteMing = null;
        String strMi = "";
        BASE64Encoder base64en = new BASE64Encoder();
        try
        {
            byteMing = strMing.getBytes("UTF8");
            byteMi = this.encryptByte(byteMing);
            strMi = base64en.encode(byteMi);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            base64en = null;
            byteMing = null;
            byteMi = null;
        }
        return strMi;
    }

    /**
     * 解密 以 String 密文输入, String 明文输出
     * @param strMi
     * @return
     */
    public String decryptStr(String strMi)
    {
        BASE64Decoder base64De = new BASE64Decoder();
        byte[] byteMing = null;
        byte[] byteMi = null;
        String strMing = "";
        try
        {
            byteMi = base64De.decodeBuffer(strMi);
            byteMing = this.decryptByte(byteMi);
            strMing = new String(byteMing, "UTF8");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            base64De = null;
            byteMing = null;
            byteMi = null;
        }
        return strMing;
    }

    /**
     * 加密以 byte[] 明文输入, byte[] 密文输出
     * @param byteS
     * @return
     */
    private byte[] encryptByte(byte[] byteS)
    {
        byte[] byteFina = null;
        Cipher cipher;
        try
        {
            cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byteFina = cipher.doFinal(byteS);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            cipher = null;
        }
        return byteFina;
    }

    /**
     * 解密以 byte[] 密文输入 , 以 byte[] 明文输出
     * @param byteD
     * @return
     */
    private byte[] decryptByte(byte[] byteD)
    {
        Cipher cipher;
        byte[] byteFina = null;
        try
        {
            cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byteFina = cipher.doFinal(byteD);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            cipher = null;
        }
        return byteFina;
    }

    /**
     * 文件 file 进行加密并保存目标文件 destFile 中
     * @param file 要加密的文件 如 c:/test/srcFile.txt
     * @param destFile 加密后存放的文件名 如 c:/ 加密后文件 .txt
     */
    public void encryptFile(String file, String destFile) throws Exception
    {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, this.key);
        InputStream is = new FileInputStream(file);
        OutputStream out = new FileOutputStream(destFile);
        CipherInputStream cis = new CipherInputStream(is, cipher);
        byte[] buffer = new byte[1024];
        int r;
        while((r = cis.read(buffer)) > 0)
        {
            out.write(buffer, 0, r);
        }
        cis.close();
        is.close();
        out.close();
    }

    /**
     * 文件采用 DES 算法解密文件
     * @param file 已加密的文件 如 c:/ 加密后文件 .txt *
     * @param destFile 解密后存放的文件名 如 c:/ test/ 解密后文件 .txt
     */
    public void decryptFile(String file, String dest) throws Exception
    {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, this.key);
        InputStream is = new FileInputStream(file);
        OutputStream out = new FileOutputStream(dest);
        CipherOutputStream cos = new CipherOutputStream(out, cipher);
        byte[] buffer = new byte[1024];
        int r;
        while((r = is.read(buffer)) >= 0)
        {
            cos.write(buffer, 0, r);
        }
        cos.close();
        out.close();
        is.close();
    }

    public static void main(String[] args) throws Exception
    {
        DESUtil des = new DESUtil("sgy");
        // DES 加密文件
        // des.encryptFile("D:/test.docx", "D:/ 加密 test.docx");
        // DES 解密文件
        // des.decryptFile("D:/ 加密 test.docx", "D:/ 解密 test.docx");
        String str1 = "newpassword";
        // DES 加密字符串
        String str2 = des.encryptStr(str1);
        System.out.println(str2);
        // DES 解密字符串
        //String deStr = des.decryptStr(str1);
        //System.out.println(deStr);
    }
}
