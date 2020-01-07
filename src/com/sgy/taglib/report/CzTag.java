package com.sgy.taglib.report;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.sgy.util.common.StringHelper;

public class CzTag extends BodyTagSupport {
	private static final StringHelper str = new StringHelper();
	private static NumberFormat numberFormat0 = NumberFormat.getNumberInstance();
	private static NumberFormat numberFormat2 = NumberFormat.getNumberInstance();
	private static NumberFormat numberFormat4 = NumberFormat.getNumberInstance();
	private static NumberFormat percentFormat = NumberFormat.getPercentInstance();
	
	static {
		numberFormat0.setMaximumFractionDigits(0);
		numberFormat0.setMinimumFractionDigits(0);
		numberFormat2.setMaximumFractionDigits(2);
		numberFormat2.setMinimumFractionDigits(2);
		numberFormat4.setMaximumFractionDigits(4);
		numberFormat4.setMinimumFractionDigits(4);
		percentFormat.setMaximumFractionDigits(2);
		percentFormat.setMinimumFractionDigits(2);
	}

	public int doStartTag() throws JspException {
		try {
			printFormatValue();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	public void printFormatValue() throws IOException{
		JspWriter out = pageContext.getOut();
		if(map2 != null && colcode != null) {
			value2 = map2.get(colcode);
			if(list1 != null && primarykey != null) {
				String[] pk = primarykey.toUpperCase().replaceAll(" ", "").split(",");
				if(Arrays.asList(pk).contains(colcode.toUpperCase())) {
					out.print(value2);
					return;
				}
				String tmp_day_time = "";
				List compareList = new ArrayList();
				Map map1 = null;
				// 从最后一条往上找，碰到日期改变就over
				for(int i = list1.size() - 1; i >= 0; i --) {
					map1 = (Map)list1.get(i);
					String day_time = str.get(map1, "day_time");
					if(!tmp_day_time.equals(day_time) && !tmp_day_time.equals(""))
						break;
					tmp_day_time = day_time;
					// 比较关键字段，一样的放入compareList
					boolean same = false;
					for(String key : pk) {
						if(key.equals("DAY_TIME"))
							continue;
						String v1 = str.get(map1, key);
						String v2 = str.get(map2, key);
						if(!v1.equals("") && !v2.equals("") && v1.equals(v2)) {
							same = true;
						} else {
							same = false;
							break;
						}
					}
					if(pk.length > 0 && same)
						compareList.add(map1);
				}
				if(compareList.size() == 1) {
					value1 = ((Map)compareList.get(0)).get(colcode);
				} else
					return; // 比对到多个的话就 pass
			}
		}
		if(value1 == null) {
			return;
		}
		if(value2 == null) {
			return;
		}
		try {
			float a = Float.parseFloat(value1.toString());
			float b = Float.parseFloat(value2.toString());
			if(type.equals("点2")) {
				out.print(numberFormat2.format(a-b));
			} else if (type.equals("点4")) {
				out.print(numberFormat4.format(a-b));
			} else if (type.equals("比例")) {
				out.print(numberFormat2.format(a-b) + "%");
			} else if (type.equals("比例(系统转换)")) {
				out.print(percentFormat.format(a-b));
			} else if (type.equals("万元")) {
				out.print(numberFormat2.format((a-b)/10000));
			} else if (type.equals("整数")) {
				out.print(numberFormat0.format(a-b));
			} else if (type.equals("")) {
				out.print(numberFormat0.format(a-b));
			} else {
				out.print(value1);
			}
		} catch (Exception e) {
			out.print("");
		}
	}

	public static void main(String[] args) throws ParseException {
		
	}
	
	private Object value1;
	private List list1;
	private String primarykey;

	private Object value2;
	private Map map2;
	private String colcode;
	
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getValue1() {
		return value1;
	}

	public void setValue1(Object value1) {
		this.value1 = value1;
	}

	public Object getValue2() {
		return value2;
	}

	public void setValue2(Object value2) {
		this.value2 = value2;
	}

	public List getList1() {
		return list1;
	}

	public String getPrimarykey() {
		return primarykey;
	}

	public Map getMap2() {
		return map2;
	}

	public String getColcode() {
		return colcode;
	}

	public void setList1(List list1) {
		this.list1 = list1;
	}

	public void setPrimarykey(String primarykey) {
		this.primarykey = primarykey;
	}

	public void setMap2(Map map2) {
		this.map2 = map2;
	}

	public void setColcode(String colcode) {
		this.colcode = colcode;
	}
}