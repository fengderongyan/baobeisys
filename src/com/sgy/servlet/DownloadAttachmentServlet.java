package com.sgy.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sgy.util.common.FileHelper;
import com.sgy.util.db.DBHelperSpring;
import com.sgy.util.spring.SpringHelper;

/**
 * 附件下载
 * @author 戴晓飞
 * @date 2013-08-21
 */
public class DownloadAttachmentServlet extends HttpServlet
{
    private static final long serialVersionUID = -4701306019602729335L;

    public DownloadAttachmentServlet()
    {
        super();
    }

    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String attachmentId = request.getParameter("attachmentId");
        DBHelperSpring db = (DBHelperSpring)SpringHelper.getBean("dbHelper");
        
        FileHelper fh = new FileHelper(db);
        try
        {
            int exc = fh.downloadAttachment(attachmentId, response);
            if(exc == -1)
            {
                response.setContentType("text/html;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().print("<script language='javascript'>alert('文件不存在！');history.back();</script>");
            }
        }
        catch (Exception e)
        {
            
        }
    }

    public void init() throws ServletException
    {
        
    }
    
    public void destroy()
    {
        super.destroy(); 
    }
}
