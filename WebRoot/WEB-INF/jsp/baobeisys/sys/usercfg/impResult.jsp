<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.sgy.util.Constants"%>
<style type="text/css">
</style>
<html>
	<head>
		<title></title>
	</head>
	<script>
		alert('${to_file_name}');
	    var loadImg=window.parent.document.getElementById("divWait");
	    loadImg.style.display = "none";
	    var btn = window.parent.document.getElementById("btnUpload");
	    btn.disabled=false;
	</script>
	<body style="background-color: #F4F4F5">
		<input type="hidden" name="method" id="method"
				value="${param.method}" />
		<form>
			<br />
			<table border="0" align="center" cellpadding="0" cellspacing="0"
				width="60%">
				<tr height="20px">
					<td colspan="2" class="outDetail">
						&nbsp;
						<center>
							<c:if test="${impResult==-1}">
								<strong style="font-size: 13; color: red;">导入出错！</strong>
							</c:if>
							<c:if test="${impResult==1}">
								<strong style="font-size: 13; color: red;">数据批量导入成功！</strong>
								<a
									href="<%=Constants.DOWNLOAD%>?file_name=WEB-INF/jsp/baobeisys/download/userinfo/${to_file_name}">
									<strong style="font-size: 13">数据校验结果文件</strong> 
								</a>
							</c:if>
						</center>
					</td>
				</tr>
			</table>
		</form>
	</body>

	<c:choose>
		<c:when test="${impResult==0}">
			<script>
	           alert('导入文件失败!');
	         </script>
		</c:when>
		<c:when test="${impResult==1}">
			<c:if test="${param.method == 'imp'}">
				<script>
		           alert('数据批量导入成功!');
		           window.parent.close();
		         </script>
		    </c:if>
		</c:when>
		<c:when test="${impResult == 2}">
			<script>
	           alert('后台数据校验失败!');
	         </script>
		</c:when>
		<c:when test="${impResult==-1}">
			<script>
	           alert('数据校验失败,请下载校验结果文件!');
	         </script>
		</c:when>
		<c:when test="${impResult==-3}">
			<script>
	           alert('后台数据校验失败!');
	         </script>
		</c:when>
		<c:when test="${impResult==-4}">
			<script>
	           alert('excel导入失败，请检查excel是否有宏及筛选等操作!');
	         </script>
		</c:when>
	</c:choose>
</html>
