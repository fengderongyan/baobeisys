package com.sgy.util.db;

import org.logicalcobwebs.proxool.ProxoolDataSource;

import com.sgy.util.common.DESUtil;

/**
 * @description 解析加密的数据源配置信息
 * @date 2013-07-30
 */
public class DataSource extends ProxoolDataSource
{
    /**
     * @description 重置数据库链接信息为明文
     * @param String password
     */
    public void setPassword(String password)
    {
        String passWord = this.DesDecode(password);
        super.setPassword(passWord);
        String url = this.resetUrl(super.getDriverUrl(), super.getPassword());
        super.setDriverUrl(url);
    }

    /**
     * @description 替换url的密码为明文
     * @param String url
     * @param String password
     * @return String
     */
    public String resetUrl(String url, String password)
    {
        int begin = url.indexOf('/');
        int end = url.indexOf('@');
        return url.substring(0, begin + 1) + password + url.substring(end);
    }
    
    /**
     * @description 根据数据库配置文件密码密文解析得到明文
     * @param String password
     * @return String
     */
    public String DesDecode(String password)
    {
        DESUtil des = new DESUtil("sgy");
        return des.decryptStr(password);
    }
}