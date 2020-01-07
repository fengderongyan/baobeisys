package com.sgy.util.common;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class StringHelper {
	public String ArrayToString(String[] strArray, String splitFlag) {
		String tmpString = "";
		if (strArray.length == 0) {
			tmpString = "";
		} else {
			for (int i = 0; i < strArray.length; i++) {
				tmpString = tmpString + strArray[i];
				if (i < strArray.length - 1) {
					tmpString = tmpString + splitFlag;
				}
			}
		}
		tmpString = tmpString.trim();
		return tmpString;
	}

	public String ArrayToString2(String[] strArray, String splitFlag) {
		String tmpString = "";
		if (strArray.length == 0) {
			tmpString = "''";
		} else {
			for (int i = 0; i < strArray.length; i++) {
				tmpString = tmpString + "'" + strArray[i] + "'";
				if (i < strArray.length - 1) {
					tmpString = tmpString + splitFlag;
				}
			}
		}
		tmpString = tmpString.trim();
		return tmpString;
	}

	public static String getFileExt(String fileName) {
		String value = new String();
		int start = 0;
		int end = 0;
		if (fileName == null)
			return null;
		start = fileName.lastIndexOf(46) + 1;
		end = fileName.length();
		value = fileName.substring(start, end);
		if (fileName.lastIndexOf(46) > 0)
			return value;
		else
			return "";
	}

	public boolean isNumeric(String strData, boolean dotFlag) {
		if (strData == null) {
			return false;
		}
		char[] numbers = strData.toCharArray();
		for (int i = 0; i < numbers.length; i++) {
			if (dotFlag) {
				if (!Character.isDigit(numbers[i]))
					return false;
			} else {
				if (!Character.isDigit(numbers[i]) && numbers[i] != '.')
					return false;
			}
		}
		if (strData.lastIndexOf(46) != strData.indexOf(46))
			return false;
		return true;
	}

	public static String changeChinese(String chnString) {
		String strChinese = null;
		byte[] temp;
		if (chnString == null || chnString == "") {
			return new String("");
		}
		try {
			temp = chnString.getBytes("ISO-8859-1");
			strChinese = new String(temp);
		} catch (java.io.UnsupportedEncodingException e) {
			System.out.println(e);
		}
		return strChinese;
	}
	public static String changeSybase(String chnString) {
		String strChinese = null;
		byte[] temp;
		if (chnString == null || chnString == "") {
			return new String("");
		}
		try {
			temp = chnString.getBytes("gb2312");
			strChinese = new String(temp);
		} catch (java.io.UnsupportedEncodingException e) {
			System.out.println(e);
		}
		return strChinese;
	}
	public static String Replace(String source, String oldString,
			String newString) {
		StringBuffer output = new StringBuffer();

		int lengthOfSource = source.length(); // 源锟街凤拷锟斤拷
		int lengthOfOld = oldString.length(); // 锟斤拷锟芥换锟街凤拷锟斤拷

		int posStart = 0; // 锟斤拷始锟斤拷锟斤拷位锟斤拷
		int pos; // 锟斤拷锟斤拷锟斤拷锟街凤拷锟轿伙拷锟?

		while ((pos = source.indexOf(oldString, posStart)) >= 0) {
			output.append(source.substring(posStart, pos));

			output.append(newString);
			posStart = pos + lengthOfOld;
		}
		if (posStart < lengthOfSource) {
			output.append(source.substring(posStart));
		}
		return output.toString();
	}

	/**
	 * 返回字符串sourceString下标nLength左侧字符串
	 * 如果字符串长度和传入下标相等 返回原字符串
	 * @param sourceString 传入字符串
	 * @param nLength 下标
	 * @return
	 */
	public static String Left(String sourceString, int nLength) {
		if (sourceString == null ||  "".equals(sourceString)
				|| sourceString.length() <= nLength) {
			return sourceString;
		}
		return sourceString.substring(0, nLength);
	}
	
	public static String lpad(String str, int length, char c) {
		while (str.length() < length) {  
			str = c + str;  
        }  
        return str;  
	}
	
	public static String rpad(String str, int length, char c) {
		while (str.length() < length) {  
			str = str + c;
        }  
        return str;  
	}

	public static String Reverse(String strReverse) {
		if (strReverse == null) {
			return strReverse;
		} else {
			StringBuffer tmpString = new StringBuffer(strReverse);

			tmpString = tmpString.reverse();

			return tmpString.toString();
		}
	}
	/**
	 * 返回字符串sourceString下标nLength右侧字符串
	 * 如果字符串长度和传入下标相等 返回原字符串
	 * @param sourceString 传入字符串
	 * @param nLength 下标
	 * @return
	 */
	public static String Right(String sourceString, int nLength) {
		if (sourceString == null || "".equals(sourceString)
				|| sourceString.length() <= nLength) {
			return sourceString;
		}
		return sourceString.substring(nLength+1,
				sourceString.length());
	}

	public static String Mid(String sourceString, int nStart, int nLength) {
		try {
			if (sourceString == null || "".equals(sourceString)) {
				return sourceString;
			}
			int Length = sourceString.length();
			if (nStart > Length || nStart < 0) {
				return null;
			}
			if ((nStart + nLength) > Length)
				return sourceString.substring(nStart, Length);
			return sourceString.substring(nStart, nStart + nLength);
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public static String toSql(String str) {
		String sql = new String(str);
		return Replace(sql, "'", "''");
	}

	public static String toHtmlInput(String str) {
		if (str == null)
			return null;

		String html = new String(str);

		html = Replace(html, "&", "&amp;");
		html = Replace(html, "<", "&lt;");
		html = Replace(html, ">", "&gt;");

		return html;
	}

	public static String toHtml(String str) {
		if (str == null) {
			return null;
		}

		String html = new String(str);

		html = toHtmlInput(html);
		html = Replace(html, "\r\n", "\n");
		html = Replace(html, "\n", "<br>");
		html = Replace(html, "\t", "    ");
		html = Replace(html, " ", " &nbsp;");

		return html;
	}

	public static String notEmpty(Object value) {
		if (value == null) {
			value = "";
		}
		return String.valueOf(value);
	}

	public static String get(Map map, String keyName) {
		return notEmpty(map.get(keyName));
	}
	
	public static double getDouble(Map map, String keyName) {
		String str= notEmpty(map.get(keyName));
		if(str.equals(""))
			str="0";
		return Double.parseDouble(str);
	}
	
	public static float getFloat(Map map, String keyName) {
		String str= notEmpty(map.get(keyName));
		if(str.equals(""))
			str="0";
		return Float.parseFloat(str);
	}
	public static int getInt(Map map, String keyName) {
		String str= notEmpty(map.get(keyName));
		if(str.equals(""))
			str="0";
		return Integer.parseInt(str);
	}
	
	/**
	 * 获取map相应的键值，如果键值为空则返回错误信息
	 * @param map
	 * @param keyName
	 * @param resultMap
	 * @return
	 */
	public static String getKeyValue(Map map, String keyName, Map resultMap) {
		String keyValue = notEmpty(map.get(keyName));
		if (keyValue.equals("")) {
			resultMap.put("errMsg", keyName + "键值丢失！");
			resultMap.put("result", 0);
		}
		return keyValue;
	}

	/**
	 * 替换参数字符串
	 * @param sql
	 * @param params
	 */
	public static String getSql(String sql, Object[] params) {
		int i = 0;
		StringTokenizer st = new StringTokenizer(sql, "?", true);
		StringBuffer bf= new StringBuffer("");
		while (st.hasMoreTokens()) {
			String temp = st.nextToken();
			if(temp.equals("?")) {
				bf.append("'"+String.valueOf(params[i])+"'");
				i++;
			} else {
				bf.append(temp);
			}
		}
		
		return bf.toString();
	}
	
	/**
	 * 获取不定参数的sql语句
	 * @param sql
	 * @param list
	 * @return
	 */
	public static String getSql(String sql, List<String> list) {
		return getSql(sql, list.toArray());
	}

	public static String toUtf8String(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {
					System.out.println(ex);
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}

	public static String ListToString(List list, String key, String split) {
		String str = "";
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			str += get(map, key) + split;
		}
		if (!"".equals(str))
			str = str.substring(0, str.lastIndexOf(split));
		return str;

	}
	
	public static String listToStringWithDistinct(List list, String key, String split) {
		String str = "";
		List<String> lst = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			String value = get(map, key);
			if(!lst.contains(value) && !value.equals("")) {
				lst.add(value);
				str += value + split;
			}
		}
		if (!"".equals(str))
			str = str.substring(0, str.lastIndexOf(split));
		else
			str = "''";
		return str;
	}
	
	public static String listToStringWithDistinct2(List list, String key, String split) {
		String str = "";
		List<String> lst = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			String value = get(map, key);
			if(!lst.contains(value) && !value.equals("")) {
				lst.add(value);
				str += value + split;
			}
		}
		if (!"".equals(str))
			str = str.substring(0, str.lastIndexOf(split));
		else
			str = "";
		return str;
	}
	
	public static List distinctList(List list, final String key, final String orderKey) {
		List<Map> lst = (List)ObjectHelper.byteClone(list);
		if(orderKey != null)
			Collections.sort(lst, new Comparator<Map>() {
				public int compare(Map m1, Map m2) {
					if(m1.get(orderKey) instanceof Number) {
						BigDecimal n1 = (BigDecimal)m1.get(orderKey);
						BigDecimal n2 = (BigDecimal)m1.get(orderKey);
						return n1.compareTo(n2);
					} else {
						String s1 = get(m1,orderKey);
						String s2 = get(m2,orderKey);
						return s1.compareTo(s2);
					}
				}
			});
		String temp = null;
		for(Iterator<Map> iter = lst.iterator();iter.hasNext();) {
			Map mapCur = iter.next();
			String curValue = get(mapCur,key);
			if(curValue.equals(temp))
				iter.remove();
			else
				temp = curValue;
		}
		return lst;
	}

	public static String toUTF8(String str) {
		String s = str;
		try {
			StringBuffer result = new StringBuffer();
			for (int i = 0; i < s.length(); i++)
				if (s.charAt(i) > '\177') {
					result.append("&#x");
					String hex = Integer.toHexString(s.charAt(i));
					StringBuffer hex4 = new StringBuffer(hex);
					hex4.reverse();
					int len = 4 - hex4.length();
					for (int j = 0; j < len; j++)
						hex4.append('0');

					for (int j = 0; j < 4; j++)
						result.append(hex4.charAt(3 - j));

					result.append(';');
				} else {
					result.append(s.charAt(i));
				}

			return result.toString();
		} catch (Exception e) {
			return s;
		}
	}

	public static String encoderByMd5(String str)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		byte[] unencodedPassword = str.getBytes();
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			return str;
		}
		md.reset();
		md.update(unencodedPassword);
		byte[] encodedPassword = md.digest();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < encodedPassword.length; i++) {
			if ((encodedPassword[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
		}
		return buf.toString();
	}
	public static String convertFileSize(long filesize) {
		String strUnit = "B";
		String strAfterComma = "";
		int intDivisor = 1;
		if (filesize >= 1024 * 1024) {
			strUnit = "MB";
			intDivisor = 1024 * 1024;
		} else if (filesize >= 1024) {
			strUnit = "KB";
			intDivisor = 1024;
		} if (intDivisor == 1)
			return filesize + strUnit;

		strAfterComma = "" + 100 * (filesize % intDivisor) / intDivisor;
		if (strAfterComma == "")
			strAfterComma = ".0";
		return filesize / intDivisor + "." + strAfterComma + strUnit;
	}
	
	public static int toInteger(String s){
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			return 0;
		}
	}
	public String[] Split(String strSplit, String splitFlag) {
		if (strSplit == null || splitFlag == null) {
			String[] tmpSplit = new String[1];
			tmpSplit[0] = strSplit;
			return tmpSplit;
		}

		java.util.StringTokenizer st = new java.util.StringTokenizer(strSplit,
				splitFlag);

		int size = st.countTokens();
		String[] tmpSplit = new String[size];

		for (int i = 0; i < size; i++) {
			tmpSplit[i] = st.nextToken();
		}
		return tmpSplit;
	}
	
	/**
	 * md5加密
	 * @param source
	 * @return
	 */
	public static String md5(String source) {  

		StringBuffer sb = new StringBuffer(32);  

		try {  
			MessageDigest md = MessageDigest.getInstance("MD5");  
			byte[] array = md.digest(source.getBytes("UTF-8"));  

			for (int i = 0; i < array.length; i++) {  
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));  
			}  
		} catch (Exception e) {  
			return "";  
		}  
		return sb.toString();  
	}  
	
	/**
	 * md5加密
	 * @param source
	 * @return
	 */
	public static String md5(byte[] source) {  

		StringBuffer sb = new StringBuffer(32);  

		try {  
			MessageDigest md = MessageDigest.getInstance("MD5");  
			byte[] array = md.digest(source);  

			for (int i = 0; i < array.length; i++) {  
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));  
			}  
		} catch (Exception e) {  
			return "";  
		}  
		return sb.toString();  
	}  
	
    /**
     * 把目标字符串用特定的字符补全到特定位数
     * @param str 目标字符串
     * @param str_length 结果字符串长度
     * @param flag 标志:left, 左边追加；right, 右边追加；
     * @param ch 追加的字符
     * @return
     */
    public static String addExtraCharForStr(String str, int length, String flag, char ch)
    {
        StringBuffer sb = new StringBuffer();
        int strLen = str.length();
        if(strLen < length)
        {
            while(strLen < length)
            {
            	if("right".equals(flag))
            	{
            		sb.append(str).append(ch);
            	}
            	else
            	{
            		sb.append(ch).append(str);
            	}
                str = sb.toString();
                strLen = str.length();
            }
        }
        return str;
    }
    
    /**
     * 将类似123,456修改为'123','456'的形式
     * @param str 源字符
     * @param split 分隔符
     * @return
     */
    public static String rebuildStr(String str, String split){
    	String[] array = str.split(split);
    	String newStrng = "";
    	for (int i = 0; i < array.length; i++) {
    		newStrng += ",'"+array[i]+"'";
		}
    	if (!newStrng.equals("")) {
    		newStrng = newStrng.substring(1);
		}
    	return newStrng;
    }
    
    /**
     * 断目标字符串是否包含特定字符串
     * @param container
     * @param regx
     * @return
     */
    public static boolean isContains(String container, String regx[]) {
        boolean result = false;
        for (int i = 0; i < regx.length; i++) {
            if (container.indexOf(regx[i]) != -1) {
                return true;
            }
        }
        return result;
    }
    
    public static void main(String[] args) {
		String sql = "select * from t_xxxxx a where a.xxx=? and a.dddd=?";
		StringHelper sh = new StringHelper();
		List<String> p = new ArrayList<String>();
		p.add("?ddddd");
		p.add("$xxxxxx");
		sh.getSql(sql, p);
		System.out.println(sh.md5("12345678"));
	}
    
    /**
	 * 图形报表用
	 * 
	 */
	public static String ListToString(List list, String key, String split,
			String str2) {
		String str = "";
		if ("1".equals(str2)) {
			for (int i = 0; i < list.size(); i++) {
				Map map = (Map) list.get(i);
				str += "'" + get(map, key) + "'" + split;
			}
			if (!"".equals(str))
				str = str.substring(0, str.lastIndexOf(split));
		} else {
			System.out.println("用错方法了~~~~");
		}
		return str;

	}
}