<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/inc/includeBase.jsp" %>
	<%@ include file="/inc/includeEasyUi.jsp" %>
	<link rel="stylesheet" href="${app}/css/ztree/zTreeStyle/zTreeStyle.css" type="text/css" />
	<script type="text/javascript" src="${app}/js/ztree/jquery.ztree.core-3.5.js"></script>
	<script type="text/javascript" src="${app}/js/ztree/jquery.ztree.excheck-3.5.js"></script>
	<link rel="stylesheet" type="text/css" href="${app}/js/jquery/jquery.ui/css/jquery.ui.all.css"/>
	<script type="text/javascript" src="${app}/js/jquery/jquery.ui/jquery.ui.core.js"></script>
	<script type="text/javascript" src="${app}/js/jquery/jquery.ui/jquery.ui.widget.js"></script>
	<script type="text/javascript" src="${app}/js/jquery/jquery.ui/jquery.ui.button.js"></script>
	<script type="text/javascript" src="${app}/js/jquery/jquery.ui/jquery.ui.position.js"></script>
	<script type="text/javascript" src="${app}/js/jquery/jquery.ui/jquery.ui.menu.js"></script>
	<script type="text/javascript" src="${app}/js/jquery/jquery.ui/jquery.ui.autocomplete.js"></script>
	<script type="text/javascript" src="${app}/js/jquery/jquery.ui/jquery.ui.tooltip.js"></script>
	<script type="text/javascript" src="${app}/js/jquery/jquery.ui/jquery.combobox.js"></script>
	<c:choose>
		<c:when test="${param.method=='edit'}">
			<c:set var="title" value="修改角色信息"></c:set>
		</c:when>
		<c:when test="${param.method=='create'}">
			<c:set var="title" value="新建角色"></c:set>
		</c:when>
		<c:when test="${param.method=='show'}">
			<c:set var="title" value="角色详细信息"></c:set>
		</c:when>
	</c:choose>
	<title>${title}</title>
	<style type="">
	.hintInfo{padding:8px 35px 8px 10px;text-shadow:none;-webkit-box-shadow:0 2px 4px rgba(0,0,0,0.2);-moz-box-shadow:0 2px 4px rgba(0,0,0,0.2);box-shadow:0 2px 4px rgba(0,0,0,0.2);background-color:#f9edbe;border:1px solid #f0c36d;-webkit-border-radius:2px;-moz-border-radius:2px;border-radius:2px;color:#333}
	
	
	</style>
</head>
<body>
	<form id="form1" name="form1" action="${app}/sys/rolecfg/saveOrUpdateRole.do" method="post">
		<div id="queryPanel" class="queryPanel">
				<div id="queryPanel_title" class="queryPanel_title">
					<div class="queryPanel_title_collapse"></div>
					&nbsp;角色基本信息
				</div>
				<div id="queryPanel_content" class="queryPanel_content"
					style="position: relative;">
					<input type="hidden" id="roleid" name="roleid" value="${roleInfo.role_id}" />
					<input type="hidden" id="method" name="method" value="${param.method}" />
					<table class="detail_table" cellspacing="0" cellpadding="0" width="100%">
						<tr>
							<td class="outDetail"  >
								角色名称：
								
							</td>
							
							<td class="outDetail2"  >
								<input type="text" id="role_name" name="role_name" value="${roleInfo.role_name }"/>
							</td>
						</tr>
						<tr>
							<td class="outDetail"  >
								角色排序：
							</td>
							<td class="outDetail2"  >
								<input type="text" id="role_order" name="role_order"  value="${roleInfo.role_order }"
									required="true" dataType="Number" msg="角色排序必须是数字"/>
							</td>
						</tr>
						<tr>
							<td class="outDetail"  >
								状态：
							</td>
							<td class="outDetail2"  >
								<sgy:select id="status" name="status" value="${roleInfo.status}">
									<sgy:option value="1">有效</sgy:option>
									<sgy:option value="0">无效</sgy:option>
								</sgy:select>
							</td>
							
						</tr>
					</table>
				</div>
			</div>
			<br />
			<p align="center">
				<c:if test="${param.method!='show'}">
					<sgy:button cssClass="ovalbutton" onclick="sav(this);return false;">保 存</sgy:button>
				</c:if>&nbsp;
				<sgy:button cssClass="ovalbutton" onclick="window.close();">关 闭</sgy:button>
			</p>
	</form>
</body>
<script type="text/javascript" defer="defer">

	function sav(src) {
		disable(src);
		if(!Validator.Validate('form1')) {
			enable(src);
			return false;
		}
		$('#form1').submit();
	}
</script>
</html>
