<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/inc/includeBase.jsp" %>
<c:if test="${result == 0}">
	<script>
		alert("操作不成功,请重试！");
		history.back();
	</script>
</c:if>
<c:if test="${result == 1}">
	<script>
		if('${method}'=='add'){
			alert('操作成功！请分配给相应的功能模块！');
			window.close();
			var goUrl = "${app}/platform/functionmodule/frame.do";
			window.opener.parent.parent.addTab('功能模块管理', '功能模块管理', goUrl, true, 'tabIcon');
		}else{
			alert('操作成功！');
			try {
				window.opener.parent.location.reload();
			} catch(e){}
			window.close();
		} 
	</script>
</c:if>