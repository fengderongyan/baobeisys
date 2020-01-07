package com.sgy.taglib.ui;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class CheckboxTag extends BaseUITag {

	private static final long serialVersionUID = 515299547208112376L;

	public int doStartTag() throws JspException {
		printCheckbox();
		return SKIP_BODY;
	}

	public void printCheckbox() {
		JspWriter out = pageContext.getOut();
		try {
			out.print("<input type=\"checkbox\" ");
			printTagAttribute();
			if ((fieldValue != null && value != null && fieldValue.equals(value))
					|| "true".equals(checked) || "checked".equals(checked)) {
				out.print("checked =\"checked\" ");
			}
			out.println(">");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String checked;
	
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}
}