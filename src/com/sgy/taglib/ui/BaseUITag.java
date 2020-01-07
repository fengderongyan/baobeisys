package com.sgy.taglib.ui;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class BaseUITag extends BodyTagSupport {

	private static final long serialVersionUID = 1108785571659883354L;

	public void printTagAttribute() {
		JspWriter out = pageContext.getOut();
		try {
			if (dataType != null)
				out.print("dataType=\"" + dataType + "\" ");
			if (msg != null)
				out.print("msg=\"" + msg + "\" ");
			if (require != null)
				out.print("require=\"" + require + "\" ");
			if (required != null)
				out.print("required=\"" + required + "\" ");
			if (validType != null)
				out.print("validType=\"" + validType + "\" ");
			if (missingMessage != null)
				out.print("missingMessage=\"" + missingMessage + "\" ");
			if (invalidMessage != null)
				out.print("invalidMessage=\"" + invalidMessage + "\" ");
			if (dataOptions != null)
				out.print("data-options=\"" + dataOptions + "\" ");
			if (cssClass != null)
				out.print("class=\"" + cssClass + "\" ");
			if (style != null)
				out.print("style=\"" + style + "\" ");
			if (size != null)
				out.print("size=\"" + size + "\" ");
			if (dir != null)
				out.print("dir=\"" + dir + "\" ");
			if ("true".equals(disabled))
				out.print("disabled=\"" + disabled + "\" ");
			if ("true".equals(readonly))
				out.print("readonly=\"" + readonly + "\" ");
			if (id != null)
				out.print("id=\"" + id + "\" ");
			if (name != null)
				out.print("name=\"" + name + "\" ");
			if (tabindex != null)
				out.print("tabindex=\"" + tabindex + "\" ");
			if (fieldValue != null)
				out.print("value=\"" + fieldValue + "\" ");
			if (title != null)
				out.print("title=\"" + title + "\" ");
			if (align != null)
				out.print("align=\"" + align + "\" ");
			if (alt != null)
				out.print("alt=\"" + alt + "\" ");
			if (lang != null)
				out.print("lang=\"" + lang + "\" ");
			if (src != null)
				out.print("src=\"" + src + "\" ");
			if (maxlength != null)
				out.print("maxlength=\"" + maxlength + "\" ");
			if (onclick != null)
				out.print("onclick=\"" + onclick + "\" ");
			if (ondblclick != null)
				out.print("ondblclick=\"" + ondblclick + "\" ");
			if (onmousedown != null)
				out.print("onmousedown=\"" + onmousedown + "\" ");
			if (onmouseup != null)
				out.print("onmouseup=\"" + onmouseup + "\" ");
			if (onmouseover != null)
				out.print("onmouseover=\"" + onmouseover + "\" ");
			if (onmousemove != null)
				out.print("onmousemove=\"" + onmousemove + "\" ");
			if (onmouseout != null)
				out.print("onmouseout=\"" + onmouseout + "\" ");
			if (onfocus != null)
				out.print("onfocus=\"" + onfocus + "\" ");
			if (onblur != null)
				out.print("onblur=\"" + onblur + "\" ");
			if (onkeypress != null)
				out.print("onkeypress=\"" + onkeypress + "\" ");
			if (onkeydown != null)
				out.print("onkeydown=\"" + onkeydown + "\" ");
			if (onkeyup != null)
				out.print("onkeyup=\"" + onkeyup + "\" ");
			if (onselect != null)
				out.print("onselect=\"" + onselect + "\" ");
			if (onchange != null)
				out.print("onchange=\"" + onchange + "\" ");
			if (accept != null)
				out.print("accept=\"" + accept + "\" ");
			if (accesskey != null)
				out.print("accesskey=\"" + accesskey + "\" ");
			if (otherAttr != null)
				out.print(otherAttr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected String dataType;
	protected String msg;
	protected String require;
	protected String cssClass;
	protected String style;
	protected String size;
	protected String dir;
	protected String disabled;
	protected String readonly;
	protected String id;
	protected String name;
	protected String tabindex;
	protected String fieldValue;
	protected String title;
	protected String align;
	protected String alt;
	protected String lang;
	protected String src;
	protected String maxlength;
	protected String required;
	protected String validType;
	protected String missingMessage;
	protected String invalidMessage;
	protected String dataOptions;

	// HTML scripting events attributes
	protected String onclick;
	protected String ondblclick;
	protected String onmousedown;
	protected String onmouseup;
	protected String onmouseover;
	protected String onmousemove;
	protected String onmouseout;
	protected String onfocus;
	protected String onblur;
	protected String onkeypress;
	protected String onkeydown;
	protected String onkeyup;
	protected String onselect;
	protected String onchange;

	// common html attributes
	protected String accept;
	protected String accesskey;
	
	protected String otherAttr;

	public String getOtherAttr() {
		return otherAttr;
	}
	public void setOtherAttr(String otherAttr) {
		this.otherAttr = otherAttr;
	}
	public String getAccept() {
		return accept;
	}
	public String getAccesskey() {
		return accesskey;
	}
	public String getAlign() {
		return align;
	}
	public String getAlt() {
		return alt;
	}
	public String getCssClass() {
		return cssClass;
	}
	public String getDataType() {
		return dataType;
	}
	public String getDir() {
		return dir;
	}
	public String getDisabled() {
		return disabled;
	}
	public String getId() {
		return id;
	}
	public String getLang() {
		return lang;
	}
	public String getMaxlength() {
		return maxlength;
	}
	public String getMsg() {
		return msg;
	}
	public String getName() {
		return name;
	}
	public String getOnblur() {
		return onblur;
	}
	public String getOnchange() {
		return onchange;
	}
	public String getOnclick() {
		return onclick;
	}
	public String getOndblclick() {
		return ondblclick;
	}
	public String getOnfocus() {
		return onfocus;
	}
	public String getOnkeydown() {
		return onkeydown;
	}
	public String getOnkeypress() {
		return onkeypress;
	}
	public String getOnkeyup() {
		return onkeyup;
	}
	public String getOnmousedown() {
		return onmousedown;
	}
	public String getOnmousemove() {
		return onmousemove;
	}
	public String getOnmouseout() {
		return onmouseout;
	}
	public String getOnmouseover() {
		return onmouseover;
	}
	public String getOnmouseup() {
		return onmouseup;
	}
	public String getOnselect() {
		return onselect;
	}
	public String getReadonly() {
		return readonly;
	}
	public String getRequire() {
		return require;
	}
	public String getSize() {
		return size;
	}
	public String getSrc() {
		return src;
	}
	public String getStyle() {
		return style;
	}
	public String getTabindex() {
		return tabindex;
	}
	public String getTitle() {
		return title;
	}
	public void setAccept(String accept) {
		this.accept = accept;
	}
	public void setAccesskey(String accesskey) {
		this.accesskey = accesskey;
	}
	public void setAlign(String align) {
		this.align = align;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public void setMaxlength(String maxlength) {
		this.maxlength = maxlength;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setOnblur(String onblur) {
		this.onblur = onblur;
	}
	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}
	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}
	public void setOndblclick(String ondblclick) {
		this.ondblclick = ondblclick;
	}
	public void setOnfocus(String onfocus) {
		this.onfocus = onfocus;
	}
	public void setOnkeydown(String onkeydown) {
		this.onkeydown = onkeydown;
	}
	public void setOnkeypress(String onkeypress) {
		this.onkeypress = onkeypress;
	}
	public void setOnkeyup(String onkeyup) {
		this.onkeyup = onkeyup;
	}
	public void setOnmousedown(String onmousedown) {
		this.onmousedown = onmousedown;
	}
	public void setOnmousemove(String onmousemove) {
		this.onmousemove = onmousemove;
	}
	public void setOnmouseout(String onmouseout) {
		this.onmouseout = onmouseout;
	}
	public void setOnmouseover(String onmouseover) {
		this.onmouseover = onmouseover;
	}
	public void setOnmouseup(String onmouseup) {
		this.onmouseup = onmouseup;
	}
	public void setOnselect(String onselect) {
		this.onselect = onselect;
	}
	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}
	public void setRequire(String require) {
		this.require = require;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public void setTabindex(String tabindex) {
		this.tabindex = tabindex;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
	public String getRequired() {
		return required;
	}
	public void setRequired(String required) {
		this.required = required;
	}
	public String getValidType() {
		return validType;
	}
	public void setValidType(String validType) {
		this.validType = validType;
	}
	public String getMissingMessage() {
		return missingMessage;
	}
	public void setMissingMessage(String missingMessage) {
		this.missingMessage = missingMessage;
	}
	public String getInvalidMessage() {
		return invalidMessage;
	}
	public void setInvalidMessage(String invalidMessage) {
		this.invalidMessage = invalidMessage;
	}
	public String getDataOptions() {
		return dataOptions;
	}
	public void setDataOptions(String dataOptions) {
		this.dataOptions = dataOptions;
	}
	
}