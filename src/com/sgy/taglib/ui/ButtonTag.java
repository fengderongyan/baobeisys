package com.sgy.taglib.ui;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
 * 自定义按钮标签
 * @author chang
 * @createDate Aug 26, 2013
 * @description
 */
public class ButtonTag extends BaseUITag {
	private static final long serialVersionUID = 1601244286947908298L;

	private JspWriter out;

	public int doStartTag() throws JspException {
		out = pageContext.getOut();
		try {
			out.print("<a ");
			printTagAttribute();
			out.print("><span>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			out.print("</span></a>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doEndTag();
	}

}