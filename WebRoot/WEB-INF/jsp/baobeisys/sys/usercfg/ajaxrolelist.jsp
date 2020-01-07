<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="sgy" uri="/WEB-INF/sgy"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<sgy:checkboxlist name="role_id" id="role_id" list="rolesList"
	checkboxLabel="ROLE_NAME" checkboxValue="ROLE_ID"
	value="${userInfo.role_ids}" delims="," columns="1" onclick="checkRole(this)"
	required="true" dataType="Group" msg="至少为用户分配一种角色" />
