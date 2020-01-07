<%@ page contentType="text/html; charset=gbk"%>
<%@ include file="/inc/includeBase.jsp"%>
<html>
    <body>
  		<display:table name="tcbgList" requestURI="${app}/todo/allTcList.do" class="list"
			id="row" cellspacing="0" cellpadding="0" pagesize="15" export="true" 
			style="width: 100%" >
		<display:column title="序号">
			<c:out value="${row_rowNum}" />
		</display:column>
		<display:column title="档案编码" property="da_id" />
		<display:column title="报告编码" property="bg_id"/>
		<display:column title="单位名称" property="dw_name" />
		<display:column title="报告状态" style="width:60px;" media="html">
			<c:choose>
				<c:when test="${row.lc_id == 1 }">待提交</c:when>
				<c:when test="${row.lc_id == 2 }">待校核</c:when>
				<c:when test="${row.lc_id == 3 }">待签发</c:when>
				<c:when test="${row.lc_id == 4 }">待打印</c:when>
				<c:when test="${row.lc_id == 5 }">已打印</c:when>
				<c:when test="${row.lc_id == 6 }"><font color="red">待提交</font></c:when>
				<c:when test="${row.lc_id == 7 }"><font color="red">待校核</font></c:when>
			</c:choose>
		</display:column>
    	</display:table>
    </body>
</html>
