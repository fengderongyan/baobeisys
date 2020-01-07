package com.sgy.taglib.ui;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class CheckboxListTag extends BaseUITag {

	private static final long serialVersionUID = 1601244286947908298L;

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
				values = (String [])value;
			} else if (delims != null) {
				values = String.valueOf(value).split(delims);
			} else {
				values = new String[]{ String.valueOf(value) };
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
	
	/**
	 * map类型的输出表格
	 * @param objMap
	 */
	public void printMapTable(Map objMap) {
		count = objMap.size();
		this.printTableStart();
		Iterator iter = objMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();
			String value =  String.valueOf(entry.getKey());
			String label =  String.valueOf(entry.getValue());
			this.printTrStart();
			this.printCheckbox(label, value);
			this.printTrEnd();
		}
		this.printTableEnd();
	}
	
	/**
	 * list类型的输出表格
	 * @param list
	 */
	public void printListTable(List list) {
		count = list.size();
		Map map = null;
		this.printTableStart();
		for (int i = 0; i < list.size(); i++) {
			map = (Map) list.get(i);
			String label = checkboxLabel == null ? "" :  String.valueOf(map
					.get(checkboxLabel));
			String value = checkboxValue == null ? "" :  String.valueOf(map
					.get(checkboxValue));
			this.printTrStart();
			this.printCheckbox(label, value);
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
			if(index % cols == 0) {
				out.println("<tr style=\"border:0\">");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printTrEnd() {
		try {
			if(index == count || index % cols == 0) {
				out.println("</tr>");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printCheckbox(String checkboxName, String checkboxValue) {
		try {
			out.print("<td valign=\"top\" width='" + 100.0/(cols==10000?count:cols) + "%' style=\"border:0;text-align:left;font-size: 12px;line-height:25px;\">");
			out.print("<input type=\"checkbox\" value='" + checkboxValue+ "' ");
			printCheckboxTagAttribute(checkboxValue);
			out.print("><label>" + checkboxName + "</label></input>");
			out.println("</td>");
			index ++;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printCheckboxTagAttribute(String checkboxValue) {
		printTagAttribute();
		try {
			if (min != null)
				out.print("min=\"" + min + "\" ");
			if (max != null)
				out.print("max=\"" + max + "\" ");
			for(int i=0;i<values.length;i++) {
				if(values[i].equals(checkboxValue)) {
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

	private String checkboxLabel;

	private String checkboxValue;

	private String tableClass;

	private String columns;
	
	private String min;
	
	private String max;

	public String getMax() {
		return max;
	}

	public String getMin() {
		return min;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public void setMin(String min) {
		this.min = min;
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

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getCheckboxLabel() {
		return checkboxLabel;
	}

	public String getCheckboxValue() {
		return checkboxValue;
	}

	public String getDelims() {
		return delims;
	}

	public void setCheckboxLabel(String checkboxLabel) {
		this.checkboxLabel = checkboxLabel;
	}

	public void setCheckboxValue(String checkboxValue) {
		this.checkboxValue = checkboxValue;
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