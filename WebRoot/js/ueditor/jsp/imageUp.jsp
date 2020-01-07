<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.sgy.util.common.UeditorHelper"%>
<%@ page import="com.sgy.util.Constants"%>

<%
    request.setCharacterEncoding("utf-8");
	response.setCharacterEncoding("utf-8");
	UeditorHelper up = new UeditorHelper(request);
    //up.getUrl();
    String fileDir =  request.getParameter("file_dir");
    fileDir = (fileDir == null) ? "" : "/"+fileDir;
    up.setSavePath(Constants.SYSTEM_ATTACHMENT_PATH+"ueditor_image" + fileDir);
    String[] fileType = {".gif" , ".png" , ".jpg" , ".jpeg" , ".bmp"};
    up.setAllowFiles(fileType);
    up.setMaxSize(10000); //单位KB
    up.upload();
    //System.out.println("{'original':'"+up.getOriginalName()+"','url':'"+up.getUrl()+"','title':'"+up.getTitle()+"','state':'"+up.getState()+"'}");
    response.getWriter().print("{'original':'"+up.getOriginalName()+"','url':'"+up.getUrl()+"','title':'"+up.getTitle()+"','state':'"+up.getState()+"','fileId':'"+up.getFileId()+"'}");
%>
