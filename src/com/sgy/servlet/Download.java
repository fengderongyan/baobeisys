package com.sgy.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Download extends HttpServlet {
	/**
	 * Constructor of the object.
	 */
	public Download() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		DBHelper db = (DBHelper) new SpringHelper().getBean("dbHelper");
//		String id = request.getParameter("id");
//		String sql ="update t_file_info a set a.hits = a.hits+"+1+" where a.id = ? ";
//		db.update(sql, new Object[]{id});
//		String file = request.getParameter("file_name");
//		String file_path = request.getParameter("file_path");
//		System.out.println("file_path is:" + file_path);
//		System.out.println("file is:" + file);
//		file = URLDecoder.decode(file, "UTF-8");
//		file_path = URLDecoder.decode(file_path, "UTF-8");
//		String fileName = file;
//		file = request.getRealPath("/")+"upload\\" + File.separator + file;
//		System.out.println("file is:" + file);
//		fileName = java.net.URLEncoder.encode(fileName, "UTF-8").replace('+', ' ');
//		file_path = java.net.URLEncoder.encode(file_path, "UTF-8").replace('+', ' ');
//		response.reset();
//		response.setContentType("application/x-download");
//		response.setHeader("Content-Disposition", "attachment;filename=" + file_path);
//		BufferedOutputStream bos = null;
//		BufferedInputStream bis = null;
//		try {
//			bis = new BufferedInputStream(new java.io.FileInputStream(file));
//			bos = new BufferedOutputStream(response.getOutputStream());
//			byte[] buff = new byte[1024];
//			int bytesRead;
//			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
//				bos.write(buff, 0, bytesRead);
//			}
//		} catch (Exception ex) {
//		} finally {
//			try {
//				bis.close();
//				bos.close();
//			} catch (Exception e) {
//			}
//		}
//		response.flushBuffer();
//		bis = null;
//		bos = null;
		request.setCharacterEncoding("GBK");
		String file = request.getParameter("file_name");
		System.out.print("file is:" + file);
		String fileNames[] = file.split("/");
        String lastFileName = fileNames[fileNames.length - 1];
		file = URLDecoder.decode(file, "UTF-8");
		String fileName = file;
		String file_path = request.getParameter("file_path") == null ? "":request.getParameter("file_path");
		file = request.getRealPath("/") + File.separator + file_path + file;
		System.out.println("file is:" + file);
		fileName = java.net.URLEncoder.encode(fileName, "UTF-8").replace('+',
				' ');
		response.reset();
		response.setContentType("application/x-download");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ lastFileName);
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new java.io.FileInputStream(file));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[1024];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (Exception ex) {
		} finally {
			try {
				bis.close();
				bos.close();
			} catch (Exception e) {
			}
		}
		response.flushBuffer();
		bis = null;
		bos = null;
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