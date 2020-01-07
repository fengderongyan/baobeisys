package com.sgy.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * 防止xss攻击
 * 替换请求参数中的script关键字符
 * @author chang
 * @createDate 2014-11-14
 */
public class XSSFilter implements Filter {

	protected Logger logger = Logger.getLogger(getClass());

	public void setFilterConfig(FilterConfig config) {
		this.config = config;
	}

	public FilterConfig getFilterConfig() {
		return config;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		XSSHttpServletRequestWrapper xssRequest = new XSSHttpServletRequestWrapper((HttpServletRequest) request);
		
		chain.doFilter(xssRequest, response);
	}

	public void destroy() {
		config = null;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		config = filterConfig;
	}

	public FilterConfig config;

}
