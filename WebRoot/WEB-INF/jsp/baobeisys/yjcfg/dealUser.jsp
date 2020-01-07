<%@page import="com.sgy.util.Constants" language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net/el"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="sgy" uri="/WEB-INF/sgy"%>
<center style="width:100%;" >
	<display:table name="userList" class="list" id="row" cellspacing="0" cellpadding="0" style="width:100%">
		<display:column title="添加审核人" media="html" style="width:40px!importent">
			<sgy:button cssClass="smallBtn_gray" onclick="addDealOper('${row.operator_id}','${row.name }');return false;" >添加</sgy:button>&nbsp;
		</display:column>
		<display:column title="序号">
			${row_rowNum }
		</display:column>
		<display:column title="工号"  property="operator_id" />
		<display:column title="姓名"  property="name"/>
		<display:column title="角色"  property="roleNames"/>
	</display:table>
</center>

