package com.sgy.taglib.ui;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.sgy.util.common.StringHelper;

public class SelectTag extends BaseUITag {
	private static final long serialVersionUID = 1601244286947908298L;

	private JspWriter out;

	public static long random_id = new java.util.Random(System.currentTimeMillis()).nextInt(10000);
	
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
			if(this.isDistinct())
				obj_list = StringHelper.distinctList(obj_list, optionValue, optionOrder);
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
			if("true".equals(readOnly) && id == null) {
				this.setId("slt" + random_id ++);
			}
			out.print("<select ");
			printTagAttribute();
			if("true".equals(readOnly)) {
				out.println(" style=\"background-color:#cccccc\"");
			}
			out.println(">");
			if (headLabel != null) {
				headValue = (headValue == null ? "" : headValue);
				out.print("<option value='" + headValue + "'>");
				out.println(headLabel + "</option>");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printSelectEnd() {
		try {
			out.print("</select>");
			if("true".equals(readOnly)) {
				out.println("<script language=\"javascript\" type=\"text/javascript\">");
				out.println("var tar_"+id+" = document.getElementById('"+id+"');");
				out.println("tar_"+id+".onfocus = function(){var index"+id+" = tar_"+id+".selectedIndex; tar_"+id+".onchange = function() { tar_"+id+".selectedIndex = index"+id+"; alert('不可更改！');}}");
				out.println("</script>");
			}
			this.setId(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printOption(String optionName, String optionValue) {
		try {
			out.print("<option value='" + optionValue + "' ");
			if (value != null && isSelected(optionValue)) {
				out.print("selected=\"selected\"");
			}
			out.println(">" + optionName + "</option>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    
    /** 
     * @description 判断是否需要默认选中
     * @param value
     * @return
     */ 
    public boolean isSelected(String value)
    {
        if(this.getValue() == null)
        {
            return false;
        }
        for(String sValue : this.getValue().split(delims))
        {
            if(value.equals(sValue))
            {
                return true;
            }
        }
        return false;
    }

	@Override
	public void printTagAttribute() {
		super.printTagAttribute();
		try {
			if (multiple != null)
				out.print("multiple=\"" + multiple + "\" ");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Object list;

	private String scope;

	private String optionLabel;

	private String optionValue;
	
	private String optionOrder;

	private String headLabel;

	private String headValue;

	private String multiple;
	
	private String value;
	
	private String readOnly;
	
	private boolean distinct;

    private String delims = ",";

	public String getDelims()
    {
        return delims;
    }

    public void setDelims(String delims)
    {
        this.delims = delims;
    }

    public String getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}

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

	public String getMultiple() {
		return multiple;
	}

	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}

	public boolean isDistinct() {
		return distinct;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public String getOptionOrder() {
		return optionOrder;
	}

	public void setOptionOrder(String optionOrder) {
		this.optionOrder = optionOrder;
	}
}