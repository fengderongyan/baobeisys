package com.sgy.taglib.largeURL;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class LargeURLTag extends BodyTagSupport {
	
	private String action;
	private JspWriter out;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int doStartTag() throws JspException {
		out = pageContext.getOut();
		try {
			out.print("<iframe id=\"postData_iframe\" src=\"\" frameborder=\"0\" width=\"0px\" height=\"0px\"></iframe>");
			String bodyString = "<body><form id=\"postData_form\" method=\"post\" action=\""+this.action+"\" target=\"_parent\">";
			ServletRequest request = pageContext.getRequest();
			
			Enumeration<String> eParameters = request.getParameterNames();
	        String parameterName, parameterValue;
	        while(eParameters.hasMoreElements()) {
	            parameterName = eParameters.nextElement();
	            parameterValue = request.getParameter(parameterName);
	            bodyString +="<input type=\"hidden\" id=\""+parameterName+"\" name=\""+parameterName+"\" value=\""+parameterValue+"\" />";
	        }
	        bodyString += "</body></form>";
	        
	        out.print("<script type=\"text/javascript\">var iframe = document.getElementById(\"postData_iframe\");" +
	        		"iframe.contentWindow.document.write('"+bodyString+"');</script>");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		return super.doEndTag();
	}

}