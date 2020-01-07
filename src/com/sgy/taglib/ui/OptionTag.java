package com.sgy.taglib.ui;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

public class OptionTag extends BodyTagSupport {

	private static final long serialVersionUID = -8107335987481249704L;

	private String selectedValue;
    
    SelectTag parent;
	
	private JspWriter out;
	
	public int doStartTag() throws JspException {
		out = pageContext.getOut();
		Tag tag = findAncestorWithClass(this, SelectTag.class);
		if(tag == null) {
			return SKIP_BODY;
		}
		parent = (SelectTag) tag;
		selectedValue = parent.getValue();
		printOption();
		return EVAL_BODY_INCLUDE;
	}

	public void printOption() {
		try {
			out.print("<option value=\"" + value + "\" ");
			if (!"false".equals(selected)) {
				if ((selectedValue != null && value != null && parent.isSelected(value))
					|| "true".equals(selected) || "selected".equals(selected)) {
					out.print("selected=\"selected\" ");
				}
			}
			out.println(">");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			out.println("</option>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doEndTag();
	}

	@Override
	public int doAfterBody() throws JspException {
		// TODO Auto-generated method stub
		return super.doAfterBody();
	}

	private String value;

	private String selected;

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}