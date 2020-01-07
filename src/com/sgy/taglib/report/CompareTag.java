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

public class CompareTag extends BodyTagSupport {
	
	private static final long serialVersionUID = -8107335987481249704L;
	private static final String styleType = "color: red;font-weight: bolder;font-style: italic;";
	
	public int doStartTag() throws JspException {
		try {
			printFormatValue();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
	
	public String getValueString(String Obj){
		if(Obj == null){
			Obj = "";
		}
		return Obj.trim();
	}
	
	public void printFormatValue() throws IOException{
		JspWriter out = pageContext.getOut();
		value = this.getValueString(value);
		minValue = this.getValueString(minValue);
		maxValue = this.getValueString(maxValue);
		style = this.getValueString(style);
		
		if(value.equals("")) {
			return;
		}
		
		if(minValue.equals("") && maxValue.equals("")) {
			return;
		}
		
		try {
			if(style.equals("")){
				style = styleType;
			}
			
			if(!minValue.equals("") && !maxValue.equals("")){
				if(Double.parseDouble(value) >= Double.parseDouble(minValue) && 
				   Double.parseDouble(value) <= Double.parseDouble(maxValue)){
					out.print(style);
				}
			}
			else if(!minValue.equals("") && maxValue.equals("")){
				if(Double.parseDouble(value) >= Double.parseDouble(minValue)){
					out.print(style);
				}
			}
			else if(minValue.equals("") && !maxValue.equals("")){
				if(Double.parseDouble(value) <= Double.parseDouble(maxValue)){
					out.print(style);
				}
			}
			else{
				out.print("");
			}
			
		} catch (Exception e) {
			out.print("");
		}
	}

	public static void main(String[] args) throws ParseException {
		System.out.println(Double.parseDouble("12.345".toString()));
	}
	
	private String value;
	
	private String minValue;
	
	private String maxValue;
	
	private String style;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
}