/*
 */ 
package com.sgy.util.common;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

/** 
 * @description 获取Tomcat应用服务器中的一些配置信息
 * @date 2013-09-06 
 */
public class TomcatHelper
{
    private static String TOMCAT_ADDR = System.getProperty("catalina.home")+"/conf/server.xml"; //tomcat的server.xml配置文件路径
    private static String TOMCAT_XPATH = "/Server/Service[@name='Catalina']/Connector[@protocol='HTTP/1.1']/";  //配置文件中xpath路径
    
    /** 
     * @description 获取Tomcat服务器的端口号
     * @return
     */ 
    public static String getTomcatServerPort()
    {
        return getTomcatPortFromConfigXml(new File(TOMCAT_ADDR), TOMCAT_XPATH + "@port[1]");
    }
    
    /** 
     * @description 获取Tomcat服务器中配置的IP地址
     * @return
     */ 
    public static String getTomcatServerIp()
    {
        return getTomcatPortFromConfigXml(new File(TOMCAT_ADDR), TOMCAT_XPATH + "@address[1]");
    }
    
    /** 
     * @description 获取Tomcat配置文件中的参数
     * @param serverXml
     * @return
     */ 
    public static String getTomcatPortFromConfigXml(File serverXml, String xPathExpression)
    {
        try
        {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true); // never forget this!
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document doc = builder.parse(serverXml);
            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            XPathExpression expr = xpath.compile(xPathExpression);
            String result = (String) expr.evaluate(doc, XPathConstants.STRING);
            return result;
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
