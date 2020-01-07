<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${result==0}">
	<script>
		alert("操作不成功,请重试！");
		history.back();
	</script>
</c:if>
<c:if test="${result==2}">
	<script>
		alert("操作不成功,请上传2M以内的签名信息图片！");
		history.back();
	</script>
</c:if>
<c:if test="${result==1}">
	<script>
		alert("操作成功！");
		try {
			if(window.opener.parent.parent.parent != window.opener.parent.parent){
				try{
					if(window.opener.location.href.indexOf('d-16544-p') == -1) {
						window.opener.parent.document.forms[0].submit();
					} else {
						window.opener.location.reload();
					}
		    	} catch(e){
		    		window.opener.document.forms[0].submit();
		    	}
			} else {
				try {
					window.opener.document.forms[0].submit();
				} catch(e){
					window.opener.parent.document.forms[0].submit();
				}			
			}
		} catch(e){
			
		}
		window.close();
	</script>
</c:if>
