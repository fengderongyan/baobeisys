package com.sgy.util.paginate;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.util.HtmlUtils;

import com.sgy.util.common.StringHelper;


public class TextPaginate{
	public final Logger log = Logger.getLogger(this.getClass());
	private int maxPages, maxLength;
	private int pageSize, pageIndex = 1;
	private String text;
	
	public TextPaginate(String text) {
		this(text, 300);
	}
	
	public TextPaginate(String text, int pageSize) {  
		this.text = HtmlUtils.htmlUnescape(text);
		this.maxLength = this.text.length();

		this.pageSize = pageSize;
		
		this.maxPages = this.maxLength / this.pageSize;
		if (this.maxLength % this.pageSize != 0)
			this.maxPages ++;
	}

	public String getPageText() {
		if(maxPages == 0)
			return text.replace("　", "<br/>");
		int startIndex = (pageIndex - 1) * pageSize;
		int endIndex = startIndex + pageSize;
		endIndex = (endIndex + 1) > maxLength ? (maxLength - 1) : endIndex; 
		
		return HtmlUtils.htmlEscape(text.substring(startIndex, endIndex)).replace("　", "<br/>").replaceAll("(<br/>)+", "<br/>");
	}
	
	public String getNavigation(HttpServletRequest request,String url) { 
		if(url.contains("jsessionid=")){
		}
		else if(url.contains(".jspa")){
			url = url.substring(0, url.indexOf(".jspa")+5)
			+ ";jsessionid=" + request.getSession().getId()
			+ url.substring(url.indexOf(".jspa")+5);
		} 
		else if(url.contains(".jsp")){
			url = url.substring(0, url.indexOf(".jsp")+4)
			+ ";jsessionid=" + request.getSession().getId()
			+ url.substring(url.indexOf(".jsp")+4);
		}
		StringBuffer sb = new StringBuffer();
		if(pageIndex > 1) {
			sb.append("<a href=\"" + url + "&amp;txtIndex=1\">首页</a> ");
			sb.append("<a href=\"" + url + "&amp;txtIndex=" + (pageIndex - 1) + "\">上一页</a> ");
		}
		if(maxPages>0){
			sb.append(pageIndex + "/" + maxPages);
		}
		if(pageIndex < maxPages) {
			sb.append("<a href=\"" + url + "&amp;txtIndex=" + (pageIndex + 1) + "\">下一页</a> ");
			sb.append("<a href=\"" + url + "&amp;txtIndex=" + maxPages + "\">末页</a>");
		}
		return StringHelper.toUTF8(sb.toString());
	}

	public void setPageIndex(String sPageIndex) {
		int pageIndex = 1;
		try {
			if(sPageIndex == null || sPageIndex.equals(""))
				pageIndex = 1;
			else
				pageIndex = Integer.parseInt(sPageIndex);
		} catch (Exception e) {}
		if(pageIndex < 1 || maxPages == 0)
			this.pageIndex = 1;
		else if(pageIndex > maxPages)
			this.pageIndex = maxPages;
		else
			this.pageIndex = pageIndex;
	}
	
	public void setPageIndex(int pageIndex) {
		if(pageIndex < 1)
			this.pageIndex = 1;
		else if(pageIndex > maxPages)
			this.pageIndex = maxPages;
		else
			this.pageIndex = pageIndex;
	}

	public int getCurFirstRow() {
		return (pageIndex - 1) * pageSize + 1;
	}
	
	public int getMaxPages() {
		return maxPages;
	}

	public int getMaxLengths() {
		return maxLength;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public String getText() {
		return text;
	}
 
}
