package com.sgy.util.spring;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.sgy.util.common.StringHelper;

public class RequestHelper {
	
	/**
	 * 获取分页条数，如果不传，默认16条
	 * @param request
	 * @param paramName
	 * @return
	 */
	public int getPageSize(HttpServletRequest request, String paramName){
		String obj = StringHelper.notEmpty(request.getParameter(paramName));
		if (obj.equals("")) {
			obj = "16";
		}
		return StringHelper.toInteger(obj);
	}

	public String getValue(HttpServletRequest request, String paramName) {
		String obj = request.getParameter(paramName);
		if (obj == null) {
			obj = "";
		}
		return obj.trim();
	}

	public String[] getValues(HttpServletRequest request, String paramName) {
		String[] value;
		value = request.getParameterValues(paramName);
		if (value == null)
			value = new String[] {};
		return value;
	}

	public String getValuesString(HttpServletRequest request, String paramName) {
		return getValuesString(request, paramName, ",");
	}

	public String getValuesString(HttpServletRequest request, String paramName,
			String delims) {
		String temp = "";
		String[] values = request.getParameterValues(paramName);
		if (values == null) {
			return "";
		} else {
			for (int i = 0; i < values.length; i++) {
				if (i == values.length - 1) {
					temp += values[i].trim();
				} else {
					temp += values[i].trim() + delims;
				}
			}
		}
		return temp;
	}
	
	public String getMpValue(MultipartHttpServletRequest multipartRequest, String paramName) {
		String obj = multipartRequest.getParameter(paramName);
		if (obj == null) {
			obj = "";
		}
		return obj.trim();
	}
	
	public String[] getMpValues(MultipartHttpServletRequest multipartRequest, String paramName) {
		String[] value;
		value = multipartRequest.getParameterValues(paramName);
		if (value == null)
			value = new String[] {};
		return value;
	}
	
	/**
	 * 获取enctype="multipart/form-data"类型表单中的表单内容
	 * @param request
	 * @param paramName
	 * @return
	 */
	public String getValueFile(HttpServletRequest request, String paramName){
		String returnValue = "";
		/** 创建磁盘文件对象 */  
        DiskFileItemFactory factory = new DiskFileItemFactory();  
        /** 创建文件获取对象 */  
        ServletFileUpload upload = new ServletFileUpload(factory);  
        /** 得到所有的文件 */  
        List<FileItem> items = null;  
        try {  
        	items = upload.parseRequest(request);
        } catch (FileUploadException e) {  
            e.printStackTrace();  
        }
        Iterator<FileItem> i = items.iterator();  
        while (i.hasNext()) {  
        	try {  
        		FileItem item = (FileItem) i.next();  
                // 检查当前项目是普通表单项目还是上传文件。  
                String fieldName = item.getFieldName();  
                if(item.isFormField()){ //如果是文件enctype="multipart/form-data"类型表单
                    if (fieldName.equals(paramName)) {  
                    	returnValue = item.getString();// 显示表单内容。  
                    } 
                }
        	} catch (Exception e) {  
                e.printStackTrace();  
            }  
        } 
        return returnValue;
	}
	
	/**
	 * 获取根目录的真实路径
	 * @param request
	 * @return
	 */
	public String getWebRootRealPath()
	{
		String rootPath = this.getClass().getResource("").getPath().replaceAll("%20", " ");;
		rootPath = rootPath.substring(0, rootPath.indexOf("WEB-INF")-1);
		
		return rootPath;
	}
	
	//
	public String getAjaxValue(HttpServletRequest request, String paramName) {
        String obj = request.getParameter(paramName);
        if (obj == null) {
            obj = "";
        }
        try {
            obj = java.net.URLDecoder.decode(obj, "UTF-8");
        } catch (Exception e) {
            obj = "";
        }
        return obj.trim();
    }
	
	public String[] getAjaxValues(HttpServletRequest request, String paramName) {
        String[] value, newValue;
        String tmp = "";
        value = request.getParameterValues(paramName);
        if (value == null)
            value = new String[] {};
        newValue = value;
        if (value.length > 0) {
            newValue = new String[value.length];

            try {
                for (int i = 0; i < value.length; i++) {
                    tmp = value[i];
                    tmp = java.net.URLDecoder.decode(tmp, "UTF-8");
                    newValue[i] = tmp;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        return newValue;
    }
}
