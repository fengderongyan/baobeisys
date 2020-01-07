package com.sgy.util.paginate;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.sgy.util.common.StringHelper;

public class Paginate {
	public final Logger log = Logger.getLogger(this.getClass());
	private int maxPages, maxRows;
	private int pageSize, pageIndex = 1;
	private List list;

	public Paginate(List list) {
		this(list, 10);
	}
	
	public Paginate(List list, int pageSize) {
		this.list = list;
		this.maxRows = list.size();
		this.pageSize = pageSize;
		
		this.maxPages = this.maxRows / this.pageSize;
		if (this.maxRows % this.pageSize != 0)
			this.maxPages ++;
	}

	/**
	 * 构造方法三：数据库分页
	 * @param size 数据总条数
	 * @param pageSize 每页数据条数
	 */
	public Paginate(int size, int pageSize){
		this.maxRows = size;
		this.pageSize = pageSize;
		this.maxPages = this.maxRows / this.pageSize;
		if (this.maxRows % this.pageSize != 0)
			this.maxPages ++;
	}
	
	public List getPageList() {
		List retlist = new ArrayList();
		if(maxRows == 0)
			return retlist;
		for (int i = (pageIndex - 1) * pageSize + 1; i < pageSize * pageIndex + 1; i++) {
			if (i > this.maxRows)
				break;
			retlist.add(list.get(i - 1));
		}
		return retlist;
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
			sb.append("<a href=\"#\" style=\"fontont-size: 18px;text-decoration: underline;\" onclick=\"setHref('"+url+"&amp;pageIndex=1');return false;\">首页</a> ");
			sb.append("<a href=\"#\" style=\"fontont-size: 18px;text-decoration: underline;\" onclick=\"setHref('"+url+"&amp;pageIndex=" + (pageIndex - 1) + "');return false;\">上页</a> ");
		}
		sb.append(pageIndex + "/" + maxPages);
		if(pageIndex < maxPages) {
			sb.append(" <a href=\"#\" style=\"fontont-size: 18px;text-decoration: underline;\" onclick=\"setHref('"+url+"&amp;pageIndex=" + (pageIndex + 1) + "');return false;\">下页</a> ");
			sb.append("<a href=\"#\" style=\"fontont-size: 18px;text-decoration: underline;\" onclick=\"setHref('"+url+"&amp;pageIndex=" + maxPages + "');return false;\">末页</a>");
		}
		return StringHelper.toUTF8(sb.toString());
	}

	public String getNavigationPage(HttpServletRequest request,String url) {
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
		sb.append("<span class=\"pagelinks\"><span class=\"left\">");
		//如果当前页大于第一页则“首页/上一页”有链接，否则没有链接
		if(pageIndex > 1) {
			sb.append("[<a href=\"" + url + "&amp;pageIndex=1\" style=\"color:black;\">首页</a>/");
			sb.append("<a href=\"" + url + "&amp;pageIndex=" + (pageIndex - 1) + "\" style=\"color:black;\">上一页</a>] ");
		} else {
			sb.append("[首页/上一页] ");
		}
		//循环显示页数
		if(maxPages <= 6){
			for (int i=1; i<=maxPages;i++) {
				//如果是当前页则页数加粗并且没有链接
				if(i == pageIndex){
					sb.append("<font style=\"font-weight: bold;\">"+i+"</font>");
				} else {
					sb.append("<a href=\"" + url + "&amp;pageIndex=" + i + "\" style=\"color:black;\">"+i+"</a>");
				}
				if(i != maxPages){
					sb.append(", ");
				}
			}
		} else {
			if(pageIndex <= 3){
				for (int i=1; i<=6;i++) {
					//如果是当前页则页数加粗并且没有链接
					if(i == pageIndex){
						sb.append("<font style=\"font-weight: bold;\">"+i+"</font>");
					} else {
						sb.append("<a href=\"" + url + "&amp;pageIndex=" + i + "\" style=\"color:black;\">"+i+"</a>");
					}
					if(i != 6){
						sb.append(", ");
					}
				}
			} else if(pageIndex<=maxPages-3){
				for (int i=pageIndex-3; i<=pageIndex+2;i++) {
					//如果是当前页则页数加粗并且没有链接
					if(i == pageIndex){
						sb.append("<font style=\"font-weight: bold;\">"+i+"</font>");
					} else {
						sb.append("<a href=\"" + url + "&amp;pageIndex=" + i + "\" style=\"color:black;\">"+i+"</a>");
					}
					if(i != pageIndex+2){
						sb.append(", ");
					}
				}
			}else{
				for (int i=maxPages-5; i<=maxPages;i++) {
					//如果是当前页则页数加粗并且没有链接
					if(i == pageIndex){
						sb.append("<font style=\"font-weight: bold;\">"+i+"</font>");
					} else {
						sb.append("<a href=\"" + url + "&amp;pageIndex=" + i + "\" style=\"color:black;\">"+i+"</a>");
					}
					if(i != maxPages){
						sb.append(", ");
					}
				}
			}
		}
		
		//如果当前页小于最大页则“下一页/末页”有链接，否则没有链接
		if(pageIndex < maxPages) {
			sb.append(" [<a href=\"" + url + "&amp;pageIndex=" + (pageIndex + 1) + "\" style=\"color:black;\">下一页</a>/");
			sb.append("<a href=\"" + url + "&amp;pageIndex=" + maxPages + "\" style=\"color:black;\">末页</a>]");
		} else {
			sb.append(" [下一页/末页]");
		}
		sb.append("</span><span class=\"right\"> 共"+maxPages+"页, 显示第"+pageIndex+"页</span></span>");
		return maxPages > 1 ? sb.toString() : "" ;
	}

	public void setPageIndex(String sPageIndex) {
		int pageIndex = 1;
		try {
			if(sPageIndex == null || sPageIndex.equals(""))
				pageIndex = 1;
			else
				pageIndex = Integer.parseInt(sPageIndex);
		} catch (Exception e) {}
		if(pageIndex > maxPages)
			this.pageIndex = maxPages;
		else if(pageIndex < 1)
			this.pageIndex = 1;
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

	public int getMaxRows() {
		return maxRows;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public List getList() {
		return list;
	}
}
