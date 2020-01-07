<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/inc/includeBase.jsp" %>
</head>
	<body>
		<sgy:csrfToken />
		<display:table name="list" requestURI="${app}/sys/log/infoDownloadHisList.do"
			class="list" id="row" cellspacing="0" style="width:100%" partialList="true" size="size"
			cellpadding="0" pagesize="${param.pageSize}" export="false" sort="external" >
			<display:column title="序号" media="html" style="width: 50px;">
				<c:out value="${row_rowNum+beginIndex}" />
			</display:column>
			<display:column title="归属组织" property="ORG_NAME"/>
			<display:column title="人员工号" property="OPERATOR_ID" style="width: 120px;"/>
			<display:column title="姓名" property="NAME" style="width: 120px;"/>
			<display:column title="手机号码" property="MOBILE" style="width: 120px;"/>
			<display:column title="下载时间" property="begin_date" style="width: 200px;"/>
			<display:column title="下载IP" property="ip_address" style="width: 200px;"/>
			<display:column title="下载信息" property="remark" />
		</display:table>
	</body>
	<script type="text/javascript">
		$(document).ready(function(){
			//启用frame查询按钮
			window.parent.enable($('#schbtn', parent.document));  
		});
	</script>
</html>