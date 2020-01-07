package com.sgy.taglib.ui;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class RadioListTag extends BaseUITag {

	private static final long serialVersionUID = -6376090820548698889L;

	private int count = 0;

	private int index = 0;

	private int cols = 10000;

	private String[] values;

	private JspWriter out;

	private void init() {
		count = 0;
		index = 0;
		cols = 10000;
	}

	public int doStartTag() throws JspException {
		init();
		out = pageContext.getOut();
		Object objList = getCollection();
		if (objList == null) {
			return SKIP_BODY;
		}
		try {
			cols = Integer.parseInt(columns);
		} catch (Exception e) {
			cols = 10000;
		}
		if (value != null) {
			if (value.getClass().isArray()) {
				values = (String[]) value;
			} else if (delims != null) {
				values = String.valueOf(value).split(delims);
			} else {
				values = new String[] { String.valueOf(value) };
			}
		} else {
			values = new String[0];
		}
		if (objList instanceof List) {
			List list = (List) objList;
			this.printListTable(list);
		}
		if (objList instanceof Map) {
			Map objMap = (Map) objList;
			this.printMapTable(objMap);
		}
		return SKIP_BODY;
	}

	/**
	 * 得到list属性指定的Attribute(可以为map或者list),不指定scope的话则全作用域遍历
	 */
	public Object getCollection() {
		if (!(list instanceof String)) {
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

	/**
	 * map类型的输出表格
	 * 
	 * @param objMap
	 */
	public void printMapTable(Map objMap) {
		count = objMap.size();
		this.printTableStart();
		Iterator iter = objMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();
			String value = (String) entry.getKey();
			String label = (String) entry.getValue();
			this.printTrStart();
			this.printradio(label, value);
			this.printTrEnd();
		}
		this.printTableEnd();
	}

	/**
	 * list类型的输出表格
	 * 
	 * @param list
	 */
	public void printListTable(List list) {
		count = list.size();
		Map map = null;
		this.printTableStart();
		for (int i = 0; i < list.size(); i++) {
			map = (Map) list.get(i);
			String label = radioLabel == null ? "" : String.valueOf((map
					.get(radioLabel)));
			String value = radioValue == null ? "" : String.valueOf((map
					.get(radioValue)));
			this.printTrStart();
			this.printradio(label, value);
			this.printTrEnd();
		}
		this.printTableEnd();
	}

	public void printTableStart() {
		try {
			out.print("<table style=\"border:0;\" width=\"100%\" ");
			if (tableClass != null) {
				out.print("class=\"" + tableClass + "\" ");
			}
			out.println(">");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printTableEnd() {
		try {
			out.println("</table>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printTrStart() {
		try {
			if (index % cols == 0) {
				out.println("<tr style=\"border:0\">");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printTrEnd() {
		try {
			if (index == count || index % cols == 0) {
				out.println("</tr>");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printradio(String radioName, String radioValue) {
		try {
			out.print("<td valign=\"top\" width='" + 100.0 / cols
					+ "%' style=\"border:0;text-align:left\">");
			out.print("<input type=\"radio\" value='" + radioValue + "' ");
			printradioTagAttribute(radioValue);
			out.print(">" + radioName + "</option>");
			out.println("</td>");
			index++;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printradioTagAttribute(String radioValue) {
		printTagAttribute();
		try {
			for (int i = 0; i < values.length; i++) {
				if (values[i].equals(radioValue)) {
					out.print("checked=\"checked\" ");
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String delims;

	private Object list;

	private Object value;

	private String scope;

	private String radioLabel;

	private String radioValue;

	private String tableClass;

	private String columns;

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

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getDelims() {
		return delims;
	}

	public String getRadioLabel() {
		return radioLabel;
	}

	public String getRadioValue() {
		return radioValue;
	}

	public void setRadioLabel(String radioLabel) {
		this.radioLabel = radioLabel;
	}

	public void setRadioValue(String radioValue) {
		this.radioValue = radioValue;
	}

	public void setDelims(String delims) {
		this.delims = delims;
	}

	public String getTableClass() {
		return tableClass;
	}

	public void setTableClass(String tableClass) {
		this.tableClass = tableClass;
	}

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}
}