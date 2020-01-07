package com.sgy.util.common;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;

/** 
 * @description 获取Weblogic应用服务器中的一些配置信息
 * @date 2013-12-06 
 */  
public class WeblogicHelper
{
    /**
     * @description 获取Weblogic服务器的IP地址
     * @author 杨飞 2013-12-05
     * @return
     */
    public static String getWeblogicServerIp()
    {
        try
        {
            Context context = new InitialContext();
            MBeanServer mbeanServer = (MBeanServer) context.lookup("java:comp/env/jmx/runtime");
            String RUNTIME_SERVICE_MBEAN = "com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean";
            ObjectName rs = new ObjectName(RUNTIME_SERVICE_MBEAN);
            ObjectName serverrt = (ObjectName) mbeanServer.getAttribute(rs, "ServerRuntime");
            String listenAddr = (String) mbeanServer.getAttribute(serverrt, "ListenAddress");
            String[] tempAddr = listenAddr.split("/");
            if(tempAddr.length == 1)
            {
                listenAddr = tempAddr[0];
            }
            else if(tempAddr[tempAddr.length - 1].trim().length() != 0)
            {
                listenAddr = tempAddr[tempAddr.length - 1];
            }
            else if(tempAddr.length > 2)
            {
                listenAddr = tempAddr[tempAddr.length - 2];
            }
            return listenAddr;
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    /**
     * @description 获取Weblogic服务器的端口号
     * @author 杨飞 2013-12-05
     * @return
     */
    public static String getWeblogicServerPort()
    {
        try
        {
            Context context = new InitialContext();
            MBeanServer mbeanServer = (MBeanServer) context.lookup("java:comp/env/jmx/runtime");
            String RUNTIME_SERVICE_MBEAN = "com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean";
            ObjectName rs = new ObjectName(RUNTIME_SERVICE_MBEAN);
            ObjectName serverrt = (ObjectName) mbeanServer.getAttribute(rs, "ServerRuntime");
            return mbeanServer.getAttribute(serverrt, "ListenPort").toString();
        }
        catch (Exception ex)
        {
            return null;
        }
    }
}
