package com.sgy.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 处理请求中的参数值，替换script关键字
 * @author chang
 * @createDate 2014-11-19
 */
public class XSSHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XSSHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getHeader(String name) {
        return super.getHeader(name);
    }

    @Override
    public String getQueryString() {
        return super.getQueryString();
    }

    @Override
    public String getParameter(String name) {
    	String value = super.getParameter(name);
        if (null == value) {
        	return null;
        }
        return cleanXSS(value);
    }

    @Override
    public String[] getParameterValues(String name) {
    	String[] value = super.getParameterValues(name);
        if (null == value) {
        	return null;
        }
        for (int i = 0; i < value.length; i++) {
			value[i] = cleanXSS(value[i]);
		}
        return value;
    }
    
    private String cleanXSS(String value) {
    	value = value.replaceAll("(?i)<\\s*script.*<\\s*/\\s*script\\s*>", "");
		return value;
	}
    
}