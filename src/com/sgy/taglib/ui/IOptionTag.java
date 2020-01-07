package com.sgy.taglib.ui;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

public class IOptionTag extends BodyTagSupport {

	private static final long serialVersionUID = -8107335987481249704L;

	private String selectedValue;
	private String parentId;
	private String onClick;
	
	private JspWriter out;
	
	public int doStartTag() throws JspException {
		out = pageContext.getOut();
		Tag tag = findAncestorWithClass(this, ISelectTag.class);
		if(tag == null) {
			return SKIP_BODY;
		}
		ISelectTag parent = (ISelectTag) tag;
		selectedValue = parent.getValue();
		parentId = parent.getId();
		onClick = parent.getOnclick();
		printOption();
		return EVAL_BODY_INCLUDE;
	}

	public void printOption() {
		try {
			out.print("<dd ");
			if (parentId != null)
				out.print("id=\"dd_" + parentId + "\" ");
			if (!"false".equals(selected)) {
				if ((selectedValue != null && value != null && value.equals(selectedValue))
					|| "true".equals(selected) || "selected".equals(selected)) {
					out.print("class=\"iselected\" ");
				} else {
					out.print("class=\"iselect\" ");
				}
			}
			
			if (null == onClick || onClick.equals("")) {
				out.print("><a hidefocus=\"true\" onclick=\"signSel('"+value+"', this, '"+parentId+"');return false;\">");
			} else {
				out.print("><a hidefocus=\"true\" onclick=\"signSel('"+value+"', this, '"+parentId+"');"+onClick+";return false;\">");
			}
				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			out.println("</a></dd>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doEndTag();
	}

	@Override
	public int doAfterBody() throws JspException {
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