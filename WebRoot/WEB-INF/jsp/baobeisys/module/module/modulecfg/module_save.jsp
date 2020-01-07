<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${result==-1}">
	<script>
		alert("该节点下尚有子节点，无法删除！");
		history.back();
	</script>
</c:if>
<c:if test="${result==0}">
	<script>
		alert("操作不成功,请重试！");
		history.back();
	</script>
</c:if>
<c:if test="${result==1}">	
	<script>
		alert("操作成功！");
		window.opener.location.reload();
		window.close();
	</script>
</c:if>
