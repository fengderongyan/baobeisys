package com.sgy.util.net;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.*;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

/**
 * ＩＰ地址操作类
 */
public class IpHelper {
	/**
	 * 将IP地址转化成整数的方法如下： 1、通过String的indexOf方法找出IP字符串中的点"."的位置。
	 * 2、根据点的位置，使用String的substring方法把IP字符串分成4段。
	 * 3、使用Long的parseLong方法把子段转化成一个3位整数。 4、通过左移位操作（<<）给每一段的数字加权，第一段的权为2的24次方，第二段的权为2的16次方，第三段的权为2的8次方，最后一段的权为1
	 * 
	 * 将整数形式的IP地址转化成字符串的方法如下： 1、将整数值进行右移位操作（>>>），右移24位，右移时高位补0，得到的数字即为第一段IP。
	 * 2、通过与操作符（&）将整数值的高8位设为0，再右移16位，得到的数字即为第二段IP。
	 * 3、通过与操作符吧整数值的高16位设为0，再右移8位，得到的数字即为第三段IP。
	 * 4、通过与操作符吧整数值的高24位设为0，得到的数字即为第四段IP。
	 * 
	 * @param strIp
	 * @return
	 */
	public static long ipToLong(String strIp) {
		long[] ip = new long[4];
		// 先找到IP地址字符串中.的位置
		int position1 = strIp.indexOf(".");
		int position2 = strIp.indexOf(".", position1 + 1);
		int position3 = strIp.indexOf(".", position2 + 1);
		// 将每个.之间的字符串转换成整型
		ip[0] = Long.parseLong(strIp.substring(0, position1));
		ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2));
		ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3));
		ip[3] = Long.parseLong(strIp.substring(position3 + 1));
		return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];

	}

	// 将十进制整数形式转换成127.0.0.1形式的ip地址
	public static String longToIP(long longIp) {
		StringBuffer sb = new StringBuffer("");
		// 直接右移24位
		sb.append(String.valueOf((longIp >>> 24)));
		sb.append(".");
		// 将高8位置0，然后右移16位
		sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
		sb.append(".");
		// 将高16位置0，然后右移8位
		sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
		sb.append(".");
		// 将高24位置0
		sb.append(String.valueOf((longIp & 0x000000FF)));
		return sb.toString();
	}

	/**
	 * 根据ＩＰ地址和子网掩码得到网络号 ip & mask
	 * 
	 * @param ip
	 * @param mask
	 * @return
	 */
	public static String getNetId(String ip, String mask) {

		Long longIp = ipToLong(ip);
		Long longMask = ipToLong(mask);
		Long longNetId = longIp & longMask;
		return longToIP(longNetId);
	}

	/**
	 * 根据ＩＰ地址和子网掩码得到主机号 ip & ^mask
	 * 
	 * @param ip
	 * @param mask
	 * @return
	 */
	public static String getMachineId(String ip, String mask) {

		Long longIp = ipToLong(ip);
		Long longMask = ipToLong(mask);
		Long longNetId = longIp & (~longMask);
		return String.valueOf(ipToLong(longToIP(longNetId)));
	}

	/**
	 * 得到ip地址对应的二进制字符串
	 * 
	 * @param ip
	 * @return
	 */
	public static String ipToBinary(String ip) {
		long longIp = ipToLong(ip);
		return Long.toBinaryString(longIp);
	}

	/**
	 * 根据客户需要的ＩＰ地址个数，计算出每个ＩＰ地址组中的地址个数
	 * 
	 * @param ip_num
	 * @return
	 */
	public static int getNumOfIpGroup(int ip_num) {
		int num = 4;
		double a = 0;
		double b = 0;
		for (int i = 1; i < 1000; i++) {
			a = Math.pow(2, i) - 2;
			b = Math.pow(2, (i + 1)) - 3;
			if (a <= ip_num && b >= ip_num) {
				num = (int) Math.pow(2, (i + 1));
				break;
			}
		}
		return num;
	}

	/**
	 * 根据IP地址，主机数得到子网掩码
	 * 
	 * @param ip
	 * @param machine_num
	 * @return
	 */
	public static String getMask(int machine_num) {
		double a = 0;
		int zero_cnt = 0;
		for (int i = 0; i < 1000; i++) {
			a = Math.pow(2, i);
			if (a >= machine_num) {
				zero_cnt = i;
				break;
			}
		}
		// String mask=longToIP((int)ipToLong("1")<<i);
		// String bin_ip=ipToBinary(ip);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 32; i++)
			sb.append("1");
		for (int i = 1; i <= zero_cnt; i++) {
			sb.setCharAt(sb.length() - i, '0');
		}
		return binaryToIp(sb.toString());
	}

	public static String binaryToIp(String binary) {
		StringBuffer sb = new StringBuffer();
		sb.append(Integer.parseInt(binary.substring(0, 8), 2)).append(".")
				.append(Integer.parseInt(binary.substring(8, 16), 2)).append(
						".").append(
						Integer.parseInt(binary.substring(16, 24), 2)).append(
						".").append(
						Integer.parseInt(binary.substring(24, 32), 2));

		return sb.toString();
	}

	public static String getIpThree(String ip) {

		return ip.equals("") ? "" : ip.substring(0, ip.lastIndexOf("."));
	}

	public static int getIpFourInt(String ip) {
		int index = ip.lastIndexOf(".");
		String fourip = ip.substring(index + 1);
		return ip.equals("") ? 0 : Integer.valueOf(fourip);
	}

	public static void main(String[] args) {
		String ipStr = "255.255.255.192";
		long longIp = ipToLong(ipStr);
		// ip地址转化成二进制形式输出

//		System.out.println(IpHelper.getAllIpInfo());
//		System.out.println(IpHelper.getAllMacInfo());
	}

	/** 
	 * @description 获取本机所有的MAC地址
	 * @return
	 */ 
	public static String getAllMacInfo()
    {
        String macAddressStr = "";
        try
        {
            // 返回所有网络接口的一个枚举实例
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            StringBuffer sb = new StringBuffer();
            while(e.hasMoreElements())
            {
                NetworkInterface network = e.nextElement();// 获得当前网络接口
                if(network != null)
                {
                    if(network.getHardwareAddress() != null)
                    {
                        // 获得MAC地址
                        // 结果是一个byte数组，每项是一个byte，我们需要通过parseByte方法转换成常见的十六进制表示
                        byte[] macAddress = network.getHardwareAddress();
                        if(macAddress != null && macAddress.length > 1)
                        {
                            sb.append(parseByte(macAddress[0])).append(":")
                                    .append(parseByte(macAddress[1])).append(":").append(
                                            parseByte(macAddress[2])).append(":").append(
                                            parseByte(macAddress[3])).append(":");
                        }
                    }
                }
                else
                {
                    System.out.println("获取MAC地址发生异常！");
                }
            }
            macAddressStr = sb.toString();
            if(macAddressStr.indexOf("00:00:00:00:00:00,") > -1)    //去掉那些无效的地址
            {
                macAddressStr = macAddressStr.replaceAll("00:00:00:00:00:00,", "");
            }
            macAddressStr = macAddressStr.substring(0, macAddressStr.length() - 1);
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }
        return macAddressStr;
    }
	
	/** 
	 * @description 获取本机的所有IP地址
	 * @return
	 */ 
	public static String getAllIpInfo()
    {
	    String ipAddressStr = "";
        try
        {
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            StringBuffer sb = new StringBuffer();
            for(; e.hasMoreElements();)
            {
                NetworkInterface item = e.nextElement();
                for(InterfaceAddress address : item.getInterfaceAddresses())
                {
                    if(address.getAddress() instanceof Inet4Address)
                    {
                        Inet4Address inet4Address = (Inet4Address) address.getAddress();
                        sb.append(inet4Address.getHostAddress()).append(",");
                    }
                }
            }
            ipAddressStr = sb.toString();
            if(ipAddressStr.indexOf("127.0.0.1,") > -1) //去掉那些无效的地址
            {
                ipAddressStr = ipAddressStr.replaceAll("127.0.0.1,", "");
            }
            ipAddressStr = ipAddressStr.substring(0, ipAddressStr.length() - 1);
        }
        catch (IOException ex)
        {
            System.out.println("获取IP地址发生异常！");
        }
        return ipAddressStr;
    }
	
    /** 
     * @description 格式化二进制
     * @param b
     * @return
     */ 
    private static String parseByte(byte b) 
    {
        String s = "00" + Integer.toHexString(b);
        return s.substring(s.length() - 2);
    }
    
    /**
     * 获取请求的IP地址，如果有通过apache代理访问的使用本方法
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if(null == ip || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(null == ip || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(null == ip || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
