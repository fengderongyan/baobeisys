package com.sgy.taglib.page;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * 分页标签处理类
 */
public class PagerTag extends TagSupport {
    private static final long serialVersionUID = 5729832874890369508L;
    private String url;         //请求URI
    private int pageSize = 10;  //每页要显示的记录数
    private int pageNo = 1;     //当前页号
    private int recordCount;    //总记录数

    @SuppressWarnings("unchecked")
    public int doStartTag() throws JspException {
        int pageCount = (recordCount + pageSize - 1) / pageSize;  //计算总页数
        
        //拼写要输出到页面的HTML文本
        StringBuilder sb = new StringBuilder();
        
        sb.append("<style type=\"text/css\">");
        sb.append(".pagination {padding: 5px;float:right;font-size:12px;font-family：'宋体','Tahoma','Arial'}");
        sb.append(".pagination a, .pagination a:link, .pagination a:visited {padding:2px 5px;margin:2px;border:0px solid #aaaadd;text-decoration:none;color:#006699;outline:none;}");
        sb.append(".pagination a:hover, .pagination a:active {border: 0px solid #ff0000;text-decoration: none;outline:none;}");
        sb.append(".pagination span.current {padding: 2px 5px;margin: 2px;border: 0px solid #ff0000;font-weight: bold;background-color: #FFFFFF;color: #000000;}");
        sb.append(".pagination span.disabled {padding: 2px 5px;margin: 2px;border: 0px solid #eee; color: #000000;}");
        sb.append("</style>\r\n");
        sb.append("<div class=\"pagination\">\r\n");
        
        
        //如果总条数小于每页要显示的记录数，则说明数据可以一页就可以全部展示出来，不加载以下代码
        if(recordCount<=pageSize){
            return SKIP_BODY;
        }else{
            //页号越界处理
            if(pageNo > pageCount){     pageNo = pageCount; }
            if(pageNo < 1){     pageNo = 1; }
            
            sb.append("<form method=\"post\" action=\"").append(this.url)
                .append("\" name=\"qPagerForm\" id=\"qPagerForm\">\r\n");
            
            //获取请求中的所有参数
            HttpServletRequest request = (HttpServletRequest) pageContext
                    .getRequest();
            Enumeration<String> enumeration = request.getParameterNames();
            String name = null;  //参数名
            String value = null; //参数值
            //把请求中的所有参数当作隐藏表单域
            while (enumeration.hasMoreElements()) {
                name =  enumeration.nextElement();
                value = request.getParameter(name);
                // 去除页号
                if (name.equals("pageNo")) {
                    if (null != value && !"".equals(value)) {
                        pageNo = Integer.parseInt(value);
                    }
                    continue;
                }
                
                // 去除每页显示多少条
                if (name.equals("pageSize")) {
                    if (null != value && !"".equals(value)) {
                        pageSize = Integer.parseInt(value);
                    }
                    continue;
                }
                sb.append("<input type=\"hidden\" name=\"")
                  .append(name)
                  .append("\" value=\"")
                  .append(value)
                  .append("\"/>\r\n");
            }
            
            int beginNum = (pageNo-1)*pageSize+1;
            int endNum = pageNo*pageSize;
            
            // 把当前页号设置成请求参数
            sb.append("<input type=\"hidden\" name=\"").append("pageNo")
                .append("\" value=\"").append(pageNo).append("\"/>\r\n");
            
            // 把每页显示多少条设置成请求参数
            sb.append("<input type=\"hidden\" name=\"").append("pageSize")
                .append("\" value=\"").append(pageSize).append("\"/>\r\n");
            
            // 输出统计数据
            sb.append("&nbsp;共有<strong> ").append(recordCount)
                .append(" </strong>条")
                .append("，<strong>")
                .append(pageCount)
                .append("</strong>页：&nbsp;\r\n");
            
            //上一页处理
            if (pageNo == 1) {
                sb.append("<span class=\"disabled\">&laquo;&nbsp;上一页")
                    .append("</span>\r\n");
            } else {
                sb.append("<a name=\"turnOverPageLabel\" href=\"javascript:turnOverPage(")
                  .append((pageNo - 1))
                  .append(")\">&laquo;&nbsp;上一页</a>\r\n");
            }
            
            //如果前面页数过多,显示"..."
            int start = 1; 
            if(this.pageNo > 4){
                start = this.pageNo - 1;
                sb.append("<a name=\"turnOverPageLabel\" href=\"javascript:turnOverPage(1)\">1</a>\r\n");
                sb.append("<a name=\"turnOverPageLabel\" href=\"javascript:turnOverPage(2)\">2</a>\r\n");
                sb.append("&hellip;\r\n");
            }
            //显示当前页附近的页
            int end = this.pageNo + 1;
            if(end > pageCount){
                end = pageCount;
            }
            for(int i = start; i <= end; i++){
                if(pageNo == i){   //当前页号不需要超链接
                    sb.append("<span class=\"current\">")
                        .append(i)
                        .append("</span>\r\n");
                }else{
                    sb.append("<a name=\"turnOverPageLabel\" href=\"javascript:turnOverPage(")
                        .append(i)
                        .append(")\">")
                        .append(i)
                        .append("</a>\r\n");
                }
            }
            //如果后面页数过多,显示"..."
            if(end < pageCount - 2){
                sb.append("&hellip;\r\n");
            }
            if(end < pageCount - 1){
                sb.append("<a name=\"turnOverPageLabel\" href=\"javascript:turnOverPage(")
                .append(pageCount - 1)
                .append(")\">")
                .append(pageCount - 1)
                .append("</a>\r\n");
            }
            if(end < pageCount){
                sb.append("<a name=\"turnOverPageLabel\" href=\"javascript:turnOverPage(")
                .append(pageCount)
                .append(")\">")
                .append(pageCount)
                .append("</a>\r\n"); 
            }
            
            //下一页处理
            if (pageNo == pageCount) {
                sb.append("<span class=\"disabled\">下一页&nbsp;&raquo;")
                    .append("</span>\r\n");
            } else {
                sb.append("<a name=\"turnOverPageLabel\" href=\"javascript:turnOverPage(")
                    .append((pageNo + 1))
                    .append(")\">下一页&nbsp;&raquo;</a>\r\n");
            }
            sb.append("</form>\r\n");
    
            // 生成提交表单的JS
            sb.append("<script language=\"javascript\">\r\n");
            sb.append("  function turnOverPage(no){\r\n");
            sb.append("    if(no>").append(pageCount).append("){");
            sb.append("      no=").append(pageCount).append(";}\r\n");
            sb.append("    if(no<1){no=1;}\r\n");
            sb.append("    document.qPagerForm.pageNo.value=no;\r\n");
            sb.append("    document.qPagerForm.submit();\r\n");
            sb.append("  }\r\n");
            sb.append("</script>\r\n");
        }
        sb.append("</div>\r\n");
        
        //把生成的HTML输出到响应中
        try {
            pageContext.getOut().println(sb.toString());
        } catch (IOException e) {
            throw new JspException(e);
        }
        return SKIP_BODY;  //本标签主体为空,所以直接跳过主体
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }
}