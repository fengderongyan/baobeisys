package com.sgy.taglib.wap;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.springframework.web.util.HtmlUtils;

public class ATag extends BodyTagSupport {

	private static final long serialVersionUID = -8107335987481249704L;
  
	private JspWriter out;
	
	public int doStartTag() throws JspException {
		out = pageContext.getOut();
		printA();
		return EVAL_BODY_BUFFERED;
	}

	public void printA() {
		try {
			if(href.contains(".jspa")){
				href = href.substring(0, href.indexOf(".jspa")+5)
				+ ";jsessionid="+pageContext.getSession().getId()
				+ href.substring(href.indexOf(".jspa")+5);
			} 
			else if(href.contains(".jsp")){ 
				href = href.substring(0, href.indexOf(".jsp")+4)
				+ ";jsessionid="+pageContext.getSession().getId()
				+ href.substring(href.indexOf(".jsp")+4);
			}
			out.print("<a href=\"" + href + "\" "); 
			if (title != null){
				out.print("title=\"" + title + "\" ");
			} 
			out.print(">");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			out.print("</a>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doEndTag();
	}

	@Override
	public int doAfterBody() throws JspException{
		try {
			out.print(HtmlUtils.htmlEscape(this.getBodyContent().getString()));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return SKIP_BODY;
	}
 
	private String href;

	private String title;

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}