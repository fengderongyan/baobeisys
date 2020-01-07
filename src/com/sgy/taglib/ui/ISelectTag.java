package com.sgy.taglib.ui;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class ISelectTag extends BaseUITag {
	private static final long serialVersionUID = 1601244286947908298L;

	private JspWriter out;

	public int doStartTag() throws JspException {
		out = pageContext.getOut();
		if (list == null) {
			this.printSelectStart();
			return EVAL_BODY_INCLUDE;
		}
		Object objList = getCollection();
		if (objList == null) {
			this.printSelectStart();
		} else if (objList instanceof List) {
			List obj_list = (List) objList;
			Map map = null;
			this.printSelectStart();
			for (int i = 0; i < obj_list.size(); i++) {
				map = (Map) obj_list.get(i);
				String label = optionLabel == null ? "" : String.valueOf(map
						.get(optionLabel)) ;
				String value = optionValue == null ? "" : String.valueOf(map
						.get(optionValue));
				this.printOption(label, value);
			}
		} else if (objList instanceof Map) {
			this.printSelectStart();
			Map objMap = (Map) objList;
			Iterator iter = objMap.entrySet().iterator();
			while (iter.hasNext()) {
				Entry entry = (Entry) iter.next();
				String value =  String.valueOf(entry.getKey());
				String label =  String.valueOf(entry.getValue());
				this.printOption(label, value);
			}
		}
		return SKIP_BODY;
	}

	@Override
	public int doEndTag() throws JspException {
		this.printSelectEnd();
		return super.doEndTag();
	}
	
	public Object getCollection() {
		if(!(list instanceof String)) {
			return list;
		}
		String list = (String) this.list;
		if (scope == null) {
			return pageContext.findAttribute(list);
		} else if ("page".equals(scope)) {
			return pageContext.getAttribute(list);
		} else if ("request".equals(scope)) {
			return pageContext.getRequest().getAttribute(list);
		} else if ("session".equals(scope)) {
			return pageContext.getSession().getAttribute(list);
		} else if ("application".equals(scope)) {
			return pageContext.getServletContext().getAttribute(list);
		} else {
			return null;
		}
	}

	public void printSelectStart() {
		try {
			if (headLabel != null) {
				out.print("<dd ");
				if (id != null)
					out.print("id=\"dd_" + id + "\" ");
				if (null == value || value.equals("")){
					out.print("class=\"iselected\" ");
				} else {
					out.print("class=\"iselect\" ");
				}
					
				if (null == onclick || onclick.equals("")) {
					out.print("><a hidefocus=\"true\" onclick=\"signSel('"+headValue+"', this, '"+id+"');return false;\">");
				} else {
					out.print("><a hidefocus=\"true\" onclick=\"signSel('"+headValue+"', this, '"+id+"');"+onclick+";return false;\">");
				}
				out.println(headLabel + "</a></dd>");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printSelectEnd() {
		try {
			out.print("<input type=\"hidden\" ");
			if (id != null)
				out.print("id=\"" + id + "\" ");
			if (name != null)
				out.print("name=\"" + name + "\" ");
			if (null != value && !value.equals("")){
				out.print("value=\""+value+"\" ");
			}
			out.print("/>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printOption(String optionName, String optionValue) {
		try {
			out.print("<dd ");
			if (id != null)
				out.print("id=\"dd_" + id + "\" ");
			if (value != null && optionValue.equals(value)) {
				out.print("class=\"iselected\" ");
			} else {
				out.print("class=\"iselect\" ");
			}
			
			if (null == onclick || onclick.equals("")) {
				out.print("><a hidefocus=\"true\" onclick=\"signSel('"+optionValue+"', this, '"+id+"');return false;\">");
			} else {
				out.print("><a hidefocus=\"true\" onclick=\"signSel('"+optionValue+"', this, '"+id+"');"+onclick+";return false;\">");
			}
				
			out.println(optionName + "</a></dd>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Object list;

	private String scope;

	private String optionLabel;

	private String optionValue;
	
	private String headLabel;

	private String headValue;

	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getHeadLabel() {
		return headLabel;
	}

	public Object getList() {
		return list;
	}

	public String getScope() {
		return scope;
	}

	public void setList(Object list) {
		this.list = list;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getOptionLabel() {
		return optionLabel;
	}

	public String getOptionValue() {
		return optionValue;
	}

	public void setOptionLabel(String optionLabel) {
		this.optionLabel = optionLabel;
	}

	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}
	
	public String getHeadValue() {
		return headValue;
	}

	public void setHeadLabel(String headLabel) {
		this.headLabel = headLabel;
	}

	public void setHeadValue(String headValue) {
		this.headValue = headValue;
	}
}