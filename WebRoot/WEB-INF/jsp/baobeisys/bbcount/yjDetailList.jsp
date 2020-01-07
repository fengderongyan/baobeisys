<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/inc/includeBase.jsp" %>
</head>
	<body>
		<sgy:csrfToken />
		<display:table name="list" requestURI="${app}/bbcount/yjDetailList.do"
			class="list" id="row" cellspacing="0" style="width:100%"
			cellpadding="0" pagesize="${param.pageSize}" export="true" 
			sort="external" partialList="true" size="size">
			<display:column title="序号" style="width: 50px;" media="html">
				<c:out value="${row_rowNum+beginIndex}" />
			</display:column>
			<display:column title="归属组织" property="org_name"/>
			<display:column title="姓名" property="name" />
			<display:column title="饮酒类别" property="yj_type"/>
			<display:column title="开始时间" property="start_date" />
		</display:table>
	</body>
	<script type="text/javascript">
		$(document).ready(function(){
			//启用frame查询按钮
			window.parent.enable($('#schbtn', parent.document));  
		});
	</script>
</html>