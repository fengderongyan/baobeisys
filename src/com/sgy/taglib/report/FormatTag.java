package com.sgy.taglib.report;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.sgy.util.common.StringHelper;

public class FormatTag extends BodyTagSupport {

	private static final long serialVersionUID = -8107335987481249704L;
	private static StringHelper str = new StringHelper();
	private static NumberFormat numberFormat0 = NumberFormat.getNumberInstance();
	private static NumberFormat numberFormat2 = NumberFormat.getNumberInstance();
	private static NumberFormat numberFormat4 = NumberFormat.getNumberInstance();
	private static NumberFormat percentFormat = NumberFormat.getPercentInstance();
	private static DateFormat dateFormatD = new SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat dateFormatT = new SimpleDateFormat("HH:mm:dd");
	private static DateFormat dateFormatDT = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
	private static DecimalFormat df2 = new DecimalFormat("####.00");
	private static DecimalFormat df4 = new DecimalFormat("####.0000");
	
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
		if(value == null) {
			return;
		}
		if(type == null) {
			out.print(value);
		}
		try {
			if(type.equals("点2")) {
				out.print(numberFormat2.format(value));
			} else if (type.equals("点2(无逗号)")) {
				out.print(df2.format(value));
			} else if (type.equals("点4")) {
				out.print(numberFormat4.format(value));
			} else if (type.equals("点4(无逗号)")) {
				out.print(df4.format(value));
			} else if (type.equals("比例")) {
				out.print(numberFormat2.format(value) + "%");
			} else if (type.equals("比例(系统转换)")) {
				out.print(percentFormat.format(value));
			} else if (type.equals("万元")) {
				out.print(numberFormat2.format(Float.parseFloat(value.toString())/10000));
			} else if (type.equals("年月日")) {
				if (value instanceof Date || value instanceof Calendar) {
				} else {
					value = dateFormatD.parse(value.toString());
				}
				out.print(String.format("%1$tY-%1$tm-%1$td", value));
			} else if (type.equals("时分秒")) {
				if (value instanceof Date || value instanceof Calendar) {
				} else {
					value = dateFormatT.parse(value.toString());
				}
				out.print(String.format("%1$tH:%1$tM:%1$tS", value));
			} else if (type.equals("年月日时分秒")) {
				if (value instanceof Date || value instanceof Calendar) {
				} else {
					value = dateFormatDT.parse(value.toString());
				}
				out.print(String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", value));
			} else if (type.equals("地区")) {//地区转换
				String dq = value.toString().trim();
				if(dq.equals("TOTA")){
					out.print("全区");
				} else if(dq.equals("WUXI")){
					out.print("无锡");
				} else if(dq.equals("JIYI")){
					out.print("江阴");
				} else if(dq.equals("YIXI")){
					out.print("宜兴");
				} else {
					out.print(dq);
				}
			} else if (type.equals("中文")){
				out.print(value.toString().replaceAll("<br/>", "\n"));
			} else {//没有配置字段类型
				out.print(value);
			}
		} catch (Exception e) {
			out.print(value);
		}
	}

	public static void main(String[] args) throws ParseException {
		System.out.println(String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", dateFormatD.parse("2009-01-02")));
	}
	
	private String type;

	private Object value;

	public String getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}