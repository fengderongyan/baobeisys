<%@ page contentType="text/html; charset=gbk"%>
<%@ include file="/inc/includeBase.jsp"%>
<html>
    <body>
  		<display:table name="tcbgList" requestURI="${app}/todo/allTcList.do" class="list"
			id="row" cellspacing="0" cellpadding="0" pagesize="15" export="true" 
			style="width: 100%" >
		<display:column title="���">
			<c:out value="${row_rowNum}" />
		</display:column>
		<display:column title="��������" property="da_id" />
		<display:column title="�������" property="bg_id"/>
		<display:column title="��λ����" property="dw_name" />
		<display:column title="����״̬" style="width:60px;" media="html">
			<c:choose>
				<c:when test="${row.lc_id == 1 }">���ύ</c:when>
				<c:when test="${row.lc_id == 2 }">��У��</c:when>
				<c:when test="${row.lc_id == 3 }">��ǩ��</c:when>
				<c:when test="${row.lc_id == 4 }">����ӡ</c:when>
				<c:when test="${row.lc_id == 5 }">�Ѵ�ӡ</c:when>
				<c:when test="${row.lc_id == 6 }"><font color="red">���ύ</font></c:when>
				<c:when test="${row.lc_id == 7 }"><font color="red">��У��</font></c:when>
			</c:choose>
		</display:column>
    	</display:table>
    </body>
</html>
