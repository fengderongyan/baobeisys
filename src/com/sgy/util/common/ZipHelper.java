package com.sgy.util.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import nochump.util.FileUtils;
import nochump.util.extend.UnzipFromFlex;
import nochump.util.extend.ZipOutput2Flex;
import org.apache.log4j.Logger;

/**
 * 压缩、解压缩帮助类
 * @author ChangShuai
 * @date 2011-11-20
 * 需用到EncryptZip.jar
 */
public class ZipHelper {
	
	private static Logger logger = Logger.getLogger(ZipHelper.class);
	
	/**
	 * 生成加密压缩文件，加密压缩单个文件
	 * @param zipDir 需加密文件的路径(文件)
	 * @param EncryptZipFile 加密后的文件
	 * @param password 加密密码
	 */
    public static void encryptZipFile(String zipDir, String EncryptZipFile, 
    		String password) {
        
        File file = new File(zipDir);   
        byte[] zipByte = ZipOutput2Flex.getEncryptZipByte(file, password);
        FileUtils.writeByteFile(zipByte, new File(EncryptZipFile));
        
	}
    
	/**
	 * 生成加密压缩文件，加密压缩加密整个文件夹下所有文件
	 * @param zipDir 需加密文件的路径(文件夹)
	 * @param EncryptZipFile 加密后的文件
	 * @param password 加密密码
	 */
    public static void encryptZipFiles(String zipDir, String EncryptZipFile, 
    		String password) {
        
        File file = new File(zipDir);   
        byte[] zipByte = ZipOutput2Flex.getEncryptZipByte(file.listFiles(), password);
        FileUtils.writeByteFile(zipByte, new File(EncryptZipFile));
        
	}
    
    /**
	 * 解压压缩文件
	 * @param zipDir 解压文件存放的路径
	 * @param EncryptZipFile 加密的压缩文件
	 * @param password 解密密码
	 */
    public static void decryptZipFile(String EncryptZipFile, String zipDir,
    		String password) {
        
        File file = new File(EncryptZipFile);   
        byte[] zipByte = FileUtils.readFileByte(file);
        try {
            UnzipFromFlex.unzipFiles(zipByte,password, zipDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    /** 
     * 压缩文件 
     * 
     * @param zipFileName 保存的压缩包文件路径 
     * @param filePath 需要压缩的文件夹或者文件路径 
     * @param base 压缩的路径 
     * @throws Exception 
     */ 
    public static void zipFile(String zipFileName, String filePath, String base) 
    	throws Exception { 

    	File inputFile = new File(filePath);
    	ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName)); 

    	if (!inputFile.exists()) { 
    		throw new FileNotFoundException("在指定路径未找到需要压缩的文件！"); 
    	} 

    	zip(out, inputFile, base);// 递归压缩方法 
    	out.close(); 
    } 

    /** 
     * 递归压缩方法 
     * 
     * @param out 压缩包输出流 
     * @param f 需要压缩的文件 
     * @param base 压缩的路径 
     * @throws Exception 
     */ 
    private static void zip(ZipOutputStream out, File f, String base) 
    	throws Exception { 
    	if (f.isDirectory()) { // 如果是文件夹，则获取下面的所有文件 
    		File[] fl = f.listFiles(); 
    		base = base.length() == 0 ? "" : base + "/"; 
    		if (!"".equals(base)) {
    			out.putNextEntry(new ZipEntry(base)); 
    		}
    		for (int i = 0; i < fl.length; i++) { 
    			zip(out, fl[i], base + fl[i].getName()); 
    		} 
    	} else { // 如果是文件，则压缩 
    		out.putNextEntry(new ZipEntry(base)); // 生成下一个压缩节点 
    		FileInputStream in = new FileInputStream(f); // 读取文件内容 
    		int b; 
    		while ((b = in.read()) != -1) { 
    			out.write(b); // 写入到压缩包 
    		} 
    		in.close(); 
    	} 
    } 

    /**
     * 解压缩文件
     * @param zipFilePath 待解压文件
     * @param fileSavePath 解压缩存放路径
     * @throws Exception
     */
    public static void unZipFile(String zipFilePath, String fileSavePath) 
    	throws Exception { 

    	if (!fileSavePath.endsWith("\\")) { 
    		fileSavePath += "\\"; 
    	} 

    	File file = new File(zipFilePath); 
    	File savePath = new File(fileSavePath); 

    	// 验证待解压文件是否存在 
    	if (!file.exists()) { 
    		throw new FileNotFoundException("在指定路径未找到ZIP压缩文件！"); 
    	} 

    	// 创建解压缩目录 
    	if (!savePath.exists()) { 
    		savePath.mkdirs(); 
    	} 

    	ZipInputStream zis = new ZipInputStream(new FileInputStream(file)); 
    	FileOutputStream fos = null; 
    	ZipEntry entry = null; 
    	int b; 

    	while ((entry = zis.getNextEntry()) != null) { 
    		file = new File(fileSavePath + entry.getName()); 
    		if (entry.isDirectory()) { 
    			// 目录 
    			file.mkdirs(); 
    		} else { 
    			// 文件 
    			fos = new FileOutputStream(file); 
    			while ((b = zis.read()) != -1) { 
    				fos.write(b); 
    			} 
    			fos.close(); 
    		} 
    	} 

    	zis.close(); 
    } 
    
    public static String getRandomString(int length) {   
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";   
        Random random = new Random();   
        StringBuffer sb = new StringBuffer();   
        for (int i = 0; i < length; i++) {   
            int number = random.nextInt(base.length());   
            sb.append(base.charAt(number));   
        }   
        return sb.toString();   
    }  

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//encryptZipFiles("D:\\log","D:\\test.zip","test");
		
		//encryptZipFile("D:\\log\\csproject.log","D:\\test.zip","test");
		
		//decryptZipFile("D:\\test.zip","D:\\log","test");
		
		try { 
    		//zipFile("d:\\test.zip", "d:\\log", ""); 
    		//zipFile("d:\\test.zip", "d:\\log\\csproject.log", "csproject.log"); 
    		//unZipFile("d:\\test.zip", "d:\\home"); 
    	} catch (Exception e) { 
    		e.printStackTrace(); 
    	} 
	}

}
