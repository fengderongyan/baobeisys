<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="com.sgy.util.Constants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<script type="text/javascript">
	//下载文件 
	downloadFile();
    function downloadFile() {
    	window.location.href="<%=Constants.DOWNLOAD%>?file_name="+'/WEB-INF/jsp/baobeisys/app-debug.apk'+
    			"&filename="+'app-debug.apk';
    }
</script>