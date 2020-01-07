package com.sgy.util.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPListParseEngine;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.sgy.util.common.DateHelper;

/** 
 * @description FTP服务器文件上传下载工具类
 * @date 2013-09-16 
 */  
public class FtpUtil
{
    public final Logger logger = Logger.getLogger(this.getClass());
    private FTPClient ftpClient;
    
    /**
     * @description connectServer 连接ftp服务器
     * @param serverIp FTP服务器的IP地址
     * @param user 登录FTP服务器的用户名
     * @param password 登录FTP服务器的用户名的口令
     * @param path 访问FTP服务器的文件路径
     * @return 
     * @throws java.io.IOException
     */
    public boolean connectServer(String serverIp, String user, String password, 
            String path) throws IOException
    {
        return this.connectServer(serverIp, 0, user, password, path, "GBK");
    }

    /** 
     * @description 连接FTP服务器
     * @param serverIp FTP服务器的IP地址
     * @param intPort FTP服务器端口号
     * @param user 登录FTP服务器的用户名
     * @param password 登录FTP服务器的用户名的口令
     * @param path 访问FTP服务器的文件路径
     * @throws IOException
     */ 
    public boolean connectServer(String serverIp, int intPort, String user, String password, 
            String path) throws IOException
    {
        return this.connectServer(serverIp, intPort, user, password, path, "GBK");
    }
    
    /** 
     * @description 连接FTP服务器
     * @param serverIp FTP服务器的IP地址
     * @param intPort FTP服务器端口号
     * @param user 登录FTP服务器的用户名
     * @param password 登录FTP服务器的用户名的口令
     * @param path 访问FTP服务器的文件路径
     * @param encoding 字符集编码
     * @throws IOException
     */ 
    public boolean connectServer(String serverIp, int intPort, String user, String password, 
            String path, String encoding) throws IOException
    {
        boolean isLogin = false;
        ftpClient = new FTPClient();
        this.ftpClient.setControlEncoding(encoding);    //设置字符集
        this.ftpClient.setDataTimeout(30 * 1000);
        this.ftpClient.setBufferSize(1024 * 2);
        
        try
        {
            if(intPort > 0) // ftp端口号
            {
                this.ftpClient.connect(serverIp, intPort);
            }
            else    //使用默认的端口号
            {
                this.ftpClient.connect(serverIp);
            }
            isLogin = this.ftpClient.login(user, password);
            int reply = this.ftpClient.getReplyCode();  // FTP服务器连接回答
            if(!FTPReply.isPositiveCompletion(reply))   // 判断请求连接的返回码是否为 230
            {
                this.ftpClient.disconnect();
                logger.error("登录FTP服务失败！");
                return isLogin;
            }
            if(isLogin)
            {
                logger.info("恭喜" + user + "成功登陆FTP服务器！");
                this.ftpClient.enterLocalPassiveMode(); //被动连接模式（一定要配置）
                //设置二进制传输
            	this.ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            	this.ftpClient.setFileTransferMode(FTPClient.STREAM_TRANSFER_MODE);
                if(path.length() != 0)  // path是ftp服务下主目录的子目录
                {
                    ftpClient.changeWorkingDirectory(path);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("登录FTP服务失败！" + e.getMessage());
        }
        return isLogin;
    }
    
    /**
     * @description 上传Ftp文件
     * @param localFileName 本地文件名称
     * @param String newFileName 上传到FTP服务器上的新文件名
     * @param remotePath 上传服务器路径 - 应该以“/”结束
     * @return boolean
     */
    public boolean uploadFile(String localFileName, String newFileName, String remotePath)
    {
        java.io.File localFile = new java.io.File(localFileName);
        return this.uploadFile(localFile, newFileName, remotePath);
    }
    
    /**
     * @description 上传Ftp文件
     * @param localFile 本地文件
     * @param String newFileName 上传到FTP服务器上的新文件名
     * @param remotePath 上传服务器路径 - 应该以“/”结束
     * @return boolean
     */
    public boolean uploadFile(File localFile, String newFileName, String remotePath)
    {
        BufferedInputStream inStream = null;
        String localFileName = localFile.getName();
        boolean uploadResult = false;
        try
        {
            inStream = new BufferedInputStream(new FileInputStream(localFile));
            if("".equals(newFileName))  //新增的文件名称没有传，默认与上传的本地文件名相同
            {
                newFileName = localFileName;
            }
            //logger.info(localFileName + "文件开始上传...");
            uploadResult = this.uploadFile(inStream, newFileName, remotePath);
            if(uploadResult)
            {
                //logger.info(localFileName + "文件上传成功！！！");
                return uploadResult;
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            logger.error(localFileName + "文件未找到...");
        }
        return uploadResult;
    }
    
    /**
     * @description 上传Ftp文件
     * @param newFileName 上传到FTP服务器上的新文件名
     * @param inStream 本地上传文件的输入流
     * @param remotePath 上传服务器路径 - 应该以“/”结束
     * @return boolean
     */
    public boolean uploadFile(InputStream inStream, String newFileName, String remotePath)
    {
        boolean uploadResult = false;
        try
        {
            if(!"".equals(remotePath))  //判断是否需要指定上传文件的路径
            {
                this.ftpClient.changeWorkingDirectory(remotePath);  //改变工作路径
            }
            uploadResult = this.ftpClient.storeFile(newFileName, inStream);  //上传文件
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(inStream != null)
            {
                try
                {
                    inStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return uploadResult;
    }
    
    /**
     * @description 下载文件
     * @param remoteFileName 待下载文件名称
     * @param remoteDownLoadPath remoteFileName 所在的路径
     * @param localDires 下载到当地那个路径下
     */
    public boolean downloadFile(String remoteFileName, String newFileName)
    {
        return this.downloadFile(remoteFileName, "", newFileName, "");
    }
    
    /**
     * @description 下载文件
     * @param remoteFileName 待下载文件名称
     * @param remoteDownLoadPath remoteFileName 所在的路径
     * @param localDires 下载到当地那个路径下
     */
    public boolean downloadFile(String remoteFileName, String remoteDownLoadPath, String localDir)
    {
        return this.downloadFile(remoteFileName, remoteDownLoadPath, "", localDir);
    }
    
    /**
     * @description 下载文件
     * @param remoteFileName 待下载的FTP服务中的文件名称
     * @param remoteDownLoadPath remoteFileName 所在的路径
     * @param newFileName 下载到本地的新文件的名称
     * @param localDir 下载到当地那个路径下
     */
    public boolean downloadFile(String remoteFileName, String remoteDownLoadPath, String newFileName, String localDir)
    {
        BufferedOutputStream outStream = null;
        boolean downloadResult = false;
        if("".equals(newFileName))  //判断下载文件是否重新起名字，若没有默认为ftp下载的文件名相同
        {
            newFileName = remoteFileName;
        }
        String newFilePath = "";
        if(!"".equals(localDir))    //下载的文件保存的本地路径
        {
            newFilePath = localDir + newFileName;    //本地新文件的路径和文件名称
        }
        else
        {
            newFilePath = newFileName;  //此文件名已经包含了具体本地路径
        }
        // 判断存放本地文件目录是否存在，不存在时新增文件夹
        File file = new File(newFilePath.substring(0, newFilePath.lastIndexOf("/")));
        if(!file.exists())
        {
            file.mkdirs();
        }
        try
        {
            if(!"".equals(remoteDownLoadPath))  //判断ftp服务器路径是否调整
            {
                this.ftpClient.changeWorkingDirectory(remoteDownLoadPath);
            }
            outStream = new BufferedOutputStream(new FileOutputStream(newFilePath));
            //logger.info(remoteFileName + "开始下载...");
            downloadResult = this.ftpClient.retrieveFile(remoteFileName, outStream);
            if(downloadResult)
            {
                //logger.info(remoteFileName + "成功已下载到" + newFilePath);
                return downloadResult;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error(remoteFileName + "下载失败！！！");
        }
        finally
        {
            if(outStream != null)
            {
                try
                {
                    outStream.flush();
                    outStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        if(downloadResult == false)
        {
            logger.error(remoteFileName + "下载失败！！！");
        }
        return downloadResult;
    }
    
    /**
     * @description 下载多个文件
     * @author 戴晓飞 2013-09-17
     * @param list 待下载的文件list集合
     * @param remoteDownLoadPath remoteFileName 所在的路径
     * @param localDir 下载到当地那个路径下
     */
    public void downloadFiles(List<String> list, String remoteDownLoadPath, String localDir)
    {
        if(list != null && list.size() > 0)
        {
            for(String fileName : list)
            {
                downloadFile(fileName, remoteDownLoadPath, "", localDir);
            }
        }
    }
    
    /**
     * @description 上传文件夹
     * @param localDirectory 当地文件夹
     * @param remoteDirectoryPath Ftp 服务器路径 以目录"/"结束
     */
    public boolean uploadDirectory(String localDirectory, String remoteDirectoryPath)
    {
        File src = new File(localDirectory);
        try
        {
            remoteDirectoryPath = remoteDirectoryPath + src.getName() + "/";
            this.ftpClient.makeDirectory(remoteDirectoryPath);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            logger.info(remoteDirectoryPath + "目录创建失败！");
        }
        File[] allFile = src.listFiles();
        
        // 循环遍历本件夹中的文件，单个文件上传
        for(int currentFile = 0; currentFile < allFile.length; currentFile++)
        {
            if(!allFile[currentFile].isDirectory()) //判断本地文件是否是文件夹
            {
                String srcName = allFile[currentFile].getPath().toString();
                uploadFile(new File(srcName), "", remoteDirectoryPath); //上传文件
            }
        }
        for(int currentFile = 0; currentFile < allFile.length; currentFile++)
        {
            if(allFile[currentFile].isDirectory())
            {
                // 递归调用，本件夹中包含的文件夹
                uploadDirectory(allFile[currentFile].getPath().toString(), remoteDirectoryPath);
            }
        }
        return true;
    }

    /**
     * @description 下载文件夹
     * @param localDirectoryPath本地地址
     * @param remoteDirectory 远程文件夹
     */
    public boolean downLoadDirectory(String localDirectoryPath, String remoteDirectory)
    {
        try
        {
            String fileName = new File(remoteDirectory).getName();
            localDirectoryPath = localDirectoryPath + fileName + "/";
            new File(localDirectoryPath).mkdirs();
            FTPFile[] allFile = this.ftpClient.listFiles(remoteDirectory);
            
            // 遍历FTP服务器上的文件，单个下载到本地
            for(int currentFile = 0; currentFile < allFile.length; currentFile++)
            {
                if(!allFile[currentFile].isDirectory()) //判断是否为文件夹
                {
                    downloadFile(allFile[currentFile].getName(), remoteDirectory, localDirectoryPath);
                }
            }
            for(int currentFile = 0; currentFile < allFile.length; currentFile++)
            {
                if(allFile[currentFile].isDirectory())  //判断是否为文件夹
                {
                    String remoteDirectoryPath = remoteDirectory + "/" + allFile[currentFile].getName();
                    downLoadDirectory(localDirectoryPath, remoteDirectoryPath); //递归调用
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            logger.error("下载文件夹失败！");
            return false;
        }
        return true;
    }
    
    /** 
     * @description 取得FTP服务器中某个目录下的所有文件列表的信息
     * @param remotePath FTP服务器的路径
     * @return List 包含文件的名称fileName 和 文件创建时间 createTime String类型的
     */ 
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getFileInfoList(String remotePath)
    {
        List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
        try
        {
            FTPListParseEngine engine = this.ftpClient.initiateListParsing(remotePath);
            FTPFile[] files = engine.getFiles();
            Map<String, Object> map = null;
            for(int i=0; i<files.length; i++)
            {
                if(files[i].isFile())   //判断是否为文件
                {
                    map = new HashMap();
                    map.put("fileName", files[i].getName());    //文件名称
                    map.put("createTime", DateHelper.getDateString(files[i].getTimestamp().getTime(), "yyyyMMddHHmmss")); //文件创建时间
                    fileList.add(map);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return fileList;
    }
    
    /** 
     * @description 取得FTP服务器某个目录下的符合条件的所有文件名
     * @author 戴晓飞 2013-09-17
     * @param remotePath FTP服务器的路径
     * @param fileNamePattern 用于匹配文件名的正则表达式
     * @return List<String> 包含文件的名称fileName
     */ 
    public List<String> getFileList(String remotePath, String fileNamePattern)
    {
        List<String> nameList = new ArrayList<String>();
        try
        {
            FTPFile[] files = ftpClient.listFiles(remotePath, new MyFtpFileFilter(fileNamePattern));
            for(int i=0; i<files.length; i++)
            {
            	nameList.add(files[i].getName());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return nameList;
    }
    
    /**
     * @description closeServer 断开与ftp服务器的链接
     */
    public void closeServer()
    {
        try
        {
            if(this.ftpClient != null)
            {
                this.ftpClient.disconnect();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    /** 
     * @description 解压GZ类型的压缩文件
     * @param localDir
     * @param fileName
     * @param toFileName
     */ 
    public void uncompressionGzFile(String localDir, String fileName, String toFileName)
    {
        try
        {
            // 本地的待解压的文件
            String gzFileName = localDir + fileName;
            GZIPInputStream gzi = new GZIPInputStream(new FileInputStream(gzFileName));
            if("".equals(toFileName))
            {
                toFileName = fileName.substring(0, fileName.lastIndexOf("."));
            }
            // 解压后数据文件名称
            toFileName = localDir + toFileName;
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(toFileName));
            int b;
            do
            {
                b = gzi.read();
                if(b == -1)
                    break;
                bos.write(b);
            }
            while(true);
            gzi.close();
            bos.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 在FTP服务器端生成XML文件
     * @author 戴晓飞 2013-09-17
     * @throws java.lang.Exception
     * @param document Document对象
     */
    public void upload(Document document) throws Exception
    {
        OutputStream os = null;
        try
        {
            String xmlFileName = "log" + DateHelper.getToday("yyyyMMddhhmmss") + ".xml";
            // 开始把Document映射到文件
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transFormer = transFactory.newTransformer();
            // 设置输出结果
            DOMSource domSource = new DOMSource(document);
            // 设置输出流
            os = ftpClient.storeFileStream("log/" + xmlFileName);
            StreamResult xmlResult = new StreamResult(os);
            // 输出xml文件
            transFormer.transform(domSource, xmlResult);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(os != null)
            {
                os.close();
            }
        }
    }
    
    public static void main(String[] args) throws IOException
    {
        FtpUtil ftp = new FtpUtil();
        
        try
        {
            ftp.connectServer("192.168.1.100", 21, "Destiny", "890116", "/test");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            ftp.closeServer();
        }
    }

    public FTPClient getFtpClient()
    {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient)
    {
        this.ftpClient = ftpClient;
    }
    
    /**
     * FTP文件过滤
     * @author 戴晓飞 2013-09-17
     */
    public class MyFtpFileFilter implements FTPFileFilter
    {
        private String fileNamePattern;
        
        public MyFtpFileFilter(String fileNamePattern)
        {
            this.fileNamePattern = fileNamePattern;
        }
        
        public boolean accept(FTPFile ftpFile)
        {
            boolean result = false;
            if(ftpFile.isFile())
            {
                String fileName = ftpFile.getName().toUpperCase();
                Pattern p = Pattern.compile(fileNamePattern.toUpperCase());
                Matcher m = p.matcher(fileName);
                result = m.find();
            }
            return result;
        }
        
        public String getFileNamePattern()
        {
            return fileNamePattern;
        }

        public void setFileNamePattern(String fileNamePattern)
        {
            this.fileNamePattern = fileNamePattern;
        }
    }
}
