package com.sgy.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;

import com.sgy.util.Constants;
import com.sgy.util.common.WeblogicHelper;
import com.sgy.util.net.IpHelper;

public class ServletContextListener implements javax.servlet.ServletContextListener {
    public Logger log = Logger.getLogger(this.getClass());

    public ServletContextListener() {
    }

    public void contextDestroyed(ServletContextEvent arg0) {
    }

    /**
     * @description 初始化一些服务器全局变量信息
     * @param arg0
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
        ServletContext servletContext = arg0.getServletContext();

        // 设置系统名称
        servletContext.setAttribute("app", servletContext.getContextPath());
        servletContext.setAttribute("server_mac", IpHelper.getAllMacInfo()); // 服务器MAC地址
        servletContext.setAttribute("server_port", WeblogicHelper.getWeblogicServerPort()); //服务器端口号
        servletContext.setAttribute("server_ip", IpHelper.getAllIpInfo()); // 服务器IP地址
        Constants.init(servletContext.getContextPath());
    }
}
