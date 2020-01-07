<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="sgy" uri="/WEB-INF/sgy"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src="${app}/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${app}/js/txtserach/jquery.textSearch-1.0.js"></script>
<style>
	.searchCss{
		background-color: yellow;
	}
	.fontCss {
		font-family: "微软雅黑","Tahoma","Arial","宋体";
		font-size: 13px;
	}
</style>
<div style="text-align: left;">
	<font style="text-align: left;">
		<img src="${app}/images/unchecked.png" 
		   name="role_chaeckedAll" onclick="chaeckedAll('role_chaecked',this);" checkeds="0" style="vertical-align: middle;"
		   onmouseover="onmouseoverImg(this);" onmouseout="onmouseoutImg(this);" />&nbsp;<strong style="color:red;font-weight: bold;">全选</strong> 
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		关键字搜索：<input type="text" name="searchTxt" id="searchTxt"  onkeyup="searchTxt();" />
	</font>
	<font style="text-align: center;margin-left: 10%;">
		<sgy:button cssClass="ovalbutton" onclick="saveModuleRole(this)">保 存</sgy:button>
	</font>
</div>
<br/>
<div style="overflow-y: auto;height: 90%;" id="roleDiv">
	<table  cellspacing="1" style="width:100%;" class="fontCss" cellpadding="0" border="0" >
	<input type="hidden" name="role_ids" id="role_ids" value="${roleStrs}" >
	<%-- 定义一个全局变量i=0 --%>
	<c:set var="i" value="0"/>	
	
	<c:forEach var="group" items="${roleGroupList}" >
		<tr><td colspan="3" style="color: blue;font-weight: bold;">${group.GROUP_NAME}&nbsp;<img src="${app}/images/unchecked.png" 
		   name="role_group_chaeckedAll" onclick="chaeckedGroupAll('group_${group.GROUP_ID }',this);" checkeds="0" style="vertical-align: middle;"
		   onmouseover="onmouseoverImg(this);" onmouseout="onmouseoutImg(this);" /></td></tr>
		<c:set var="i" value="0"/>	
		<c:forEach var="roles" items="${roleList}"  varStatus="vs" >
			<c:if test="${roles.GROUP_ID eq group.GROUP_ID }">
				<%--隔3个数据给加一个tr--%>
				<c:if test="${i % 4 == 0}"><tr></c:if>
					<td height="20" style="width: 35%;">
						<img src="${app}/images/unchecked.png" roleid="${roles.ROLE_ID }" rolename="${roles.ROLE_NAME }"
						   name="role_chaecked" group="group_${roles.GROUP_ID }" onclick="changeCheckedImg(this);" checkeds="0" style="vertical-align: middle;" 
						   onmouseover="onmouseoverImg(this);" onmouseout="onmouseoutImg(this);"  />&nbsp;&nbsp;${roles.ROLE_NAME}
					</td>
				<%--  i++  当等于3的时候,返回上面重新加载--%>
		      		<c:set var="i" value="${i + 1}" />
		      		<%--当三个的一行的时候加一个换行符号--%>
				<c:if test="${i % 4 == 3 || vs.last }"></tr><c:set var="i" value="0"/></c:if>
			</c:if>
		</c:forEach>  
		<tr><td colspan="3" style="border-bottom: 0px dashed #D3D3D3;">&nbsp;</td></tr>
	</c:forEach>
	</table>
</div>

<script language="javascript">
//匹配已经选择的角色，并勾选上
$('img[name=role_chaecked]').each(function(i){
	var val = $(this).attr("roleid")
	var roleidStr= '${roleStrs}';
	var roleids=roleidStr.split(",");
	for(var i=0;i<roleids.length;i++){
		if(val==roleids[i]){
			$(this).attr("src","${app}/images/checked.png"); 
			$(this).attr("checkeds","1"); 
		}
	}
});
</script>

