package com.sgy.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.sgy.util.Constants;

public class MobileUpload extends HttpServlet {
	/**
	 * Constructor of the object.
	 */
	public MobileUpload() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			request.setCharacterEncoding("UTF-8");
			DiskFileItemFactory factory = new DiskFileItemFactory();
			String path = Constants.ATTACHMENT_ROOT;
			factory.setRepository(new File(path));//设置setSizeThreshold方法中提到的临时文件的存放目录
			factory.setSizeThreshold(1024);//用于设置是否使用临时文件保存解析出的数据的那个临界值
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> items = (List) upload.parseRequest(request);
			for (FileItem item : items) {
				if (item.isFormField()) {// 一般
					String name = item.getFieldName();
					String value = java.net.URLDecoder.decode(item.getString("UTF-8"), "UTF-8");
					if(name.equals("path")) {
						path = Constants.ATTACHMENT_ROOT+ value;
						//判断上传附件的目录是否存在，不存在则创建
						File directory = new File(path);
						if(!directory.exists() ){
						    directory.mkdirs();
						}
					}
				} else {// Field
				    String clientType=request.getParameter("clientType");
				    String fileSavePath=request.getParameter("fileSavePath");
				    String uploadAddress="";
				    if(clientType!=null&&clientType.equals("IOS")){
                        uploadAddress="/"+fileSavePath+"/";    //在Linux系统下的路径用“/”
                        File directory = new File(path+fileSavePath);
                        if(!directory.exists() ){
                            directory.mkdirs();
                        }
                    }
					String value = java.net.URLDecoder.decode(item.getName(), "UTF-8");
					int start = value.lastIndexOf("\\");
					String filename = value.substring(start + 1);
					item.write(new File(path+uploadAddress, filename));
				}
			}
			response.getWriter().println("successed");
		} catch (Exception ex) {
			ex.printStackTrace();
			response.getWriter().println(ex.getMessage());
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occure
	 */
	public void init() throws ServletException {
		// Put your code here
	}
}
