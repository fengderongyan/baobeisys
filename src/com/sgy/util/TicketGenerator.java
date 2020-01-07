/*
 * @date 2013-08-15 18:16:08  
 */
package com.sgy.util;

import com.sgy.util.common.DateHelper;
import com.sgy.util.common.StringHelper;

/**
 * @description 单点登录token令牌生成器
 * @date 2013-08-15
 */
public class TicketGenerator
{
    private String current_date = DateHelper.getToday("yyyyMMddHHmmss"); // 当前系统时间
    private String ip_addr = ""; // 请求的IP地址
    private String operator_id = ""; // 人员工号
    
    /**
     * @description 构造方法
     * @param ip_addr
     * @param operator_id
     * @param system_id
     */
    public TicketGenerator(String ip_addr, String operator_id)
    {
        this.ip_addr = ip_addr;
        this.operator_id = operator_id;
    }

    /**
     * @description 获取指定位数的随机码
     * @param len
     * @return
     */
    public String getRandomStr(int len)
    {
        StringBuffer buf = new StringBuffer("a,b,c,d,e,f,g,h,i,g,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z");
        buf.append(",1,2,3,4,5,6,7,8,9,0");
        buf.append(",A,B,C,D,E,F,G,H,I,G,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z");
        String[] arr = buf.toString().split(",");
        StringBuffer b = new StringBuffer();
        java.util.Random r;
        int k;
        for(int i = 0; i < len; i++)
        {
            r = new java.util.Random();
            k = r.nextInt();
            b.append(String.valueOf(arr[Math.abs(k % 62)]));
        }
        return b.toString();
    }

    /**
     * @description 拼接相关字符串生成唯一的ticket(MD5加密)
     * @return
     */
    public String getTicket()
    {
        String ticketStr = this.current_date + getRandomStr(10) + this.ip_addr
                + this.operator_id ;
        return StringHelper.md5(ticketStr);
    }

    public String getIp_addr()
    {
        return ip_addr;
    }

    public void setIp_addr(String ip_addr)
    {
        this.ip_addr = ip_addr;
    }

    public String getOperator_id()
    {
        return operator_id;
    }

    public void setOperator_id(String operator_id)
    {
        this.operator_id = operator_id;
    }
}
