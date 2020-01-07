/*
 */
package com.sgy.util.net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;


/**
 * @description HttpURLConnection + XML 接口请求方式
 * @date 2013-08-15
 */
public class HttpGbkURLConnHelper
{
    public static final Logger log = Logger.getLogger(HttpGbkURLConnHelper.class);
    private static int CONNECTTIMEOUT = 10000; // 连接主机的超时时间（单位：毫秒）
    private static int READTIMEOUT = 10000; // 从主机读取数据的超时时间（单位：毫秒）

    /**
     * @description 读取HTTP请求的内容信息
     * @param request
     * @return
     * @throws IOException
     */
    public static String readRequestInfo(HttpServletRequest request)
    {
        StringBuffer sb = new StringBuffer();
        try
        {
            InputStream in = request.getInputStream();
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(in, "utf-8"));//gbk
            String line = "";
            while((line = bufferReader.readLine()) != null)
            {
                sb.append(line);
            }
            bufferReader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * @description 调用Http请求
     * @return
     * @throws IOException
     */
    public static String execute(String serverUrl, String message) throws IOException
    {
        return execute(serverUrl, message, CONNECTTIMEOUT, READTIMEOUT);
    }
    
    /**
     * @description 调用Http请求
     * @author 戴晓飞 2014-09-05
     * @return
     * @throws IOException
     */
    public static String execute(String serverUrl, String message, int connectTimeout, 
    		int readTimeout) throws IOException
    {
    	
    	java.net.URL connURL = new java.net.URL(serverUrl);
        HttpURLConnection httpCon = (HttpURLConnection) connURL.openConnection();

        // 设置http请求的头部
        httpCon.setUseCaches(false);// Post 请求不能使用缓存
        httpCon.setDoOutput(true); // http正文内，因此需要设为true, 默认情况下是false;
        httpCon.setDoInput(true); // 设置是否从httpUrlConnection读入，默认情况下是true;

        // 设定传送的内容类型是可序列化的java对象
        httpCon.setRequestProperty("Content-type", "text/xml; charset=UTF-8");//GBK
        httpCon.setRequestMethod("POST"); // 设定请求的方法为"POST"，默认是GET
        httpCon.setConnectTimeout(connectTimeout); // 连接主机的超时时间（单位：毫秒）
        httpCon.setReadTimeout(readTimeout); // 从主机读取数据的超时时间（单位：毫秒）

        // 写入http请求的正文
        DataOutputStream dataOutputStream = new DataOutputStream(httpCon.getOutputStream());
        dataOutputStream.write(message.getBytes("UTF-8"));//GBK
        dataOutputStream.flush();
        dataOutputStream.close();
        int responseCode = httpCon.getResponseCode();
        if(responseCode == HttpURLConnection.HTTP_OK) // 返回码正确
        {
            // 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。
            BufferedReader bufReader = new BufferedReader(
                    new InputStreamReader(httpCon.getInputStream(), "UTF-8"));//GBK
            StringBuffer sb = new StringBuffer();
            String line = "";
            while((line = bufReader.readLine()) != null)
            {
                sb.append(line);
            }
            bufReader.close();
            return sb.toString();
        }
        else    // 返回码错误，例如：404
        {
            log.debug("调用Http请求有异常！返回码为：" + responseCode);
            return "";
        }
    }

    public static int getCONNECTTIMEOUT()
    {
        return CONNECTTIMEOUT;
    }

    public static void setCONNECTTIMEOUT(int connecttimeout)
    {
        CONNECTTIMEOUT = connecttimeout;
    }

    public static int getREADTIMEOUT()
    {
        return READTIMEOUT;
    }

    public static void setREADTIMEOUT(int readtimeout)
    {
        READTIMEOUT = readtimeout;
    }
}