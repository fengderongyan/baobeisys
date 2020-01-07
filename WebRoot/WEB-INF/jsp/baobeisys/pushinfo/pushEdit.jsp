<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/inc/includeBase.jsp" %>
	<c:choose>
		<c:when test="${param.method=='show'}">
			<c:set var="title" value="查看推送信息详情"></c:set>
		</c:when>
		<c:when test="${param.method=='create'}">
			<c:set var="title" value="新增推送信息"></c:set>
		</c:when>
	</c:choose>
	<title>${title}</title>
	<script type="text/javascript" src="${app}/FCKeditor/fckeditor.js"></script> 
</head>
<body>
	<form id="form1" name="form1" action="${app}/pushinfo/pushInfo/saveAndPush.do" method="post">
		<div id="queryPanel" class="queryPanel">
			<div id="queryPanel_title" class="queryPanel_title">
				<div class="queryPanel_title_collapse"></div>&nbsp;${title}
			</div>
		    <div id="queryPanel_content" class="queryPanel_content">
				<table class="detail_table" cellspacing="0" cellpadding="0" style="width: 100%;">
					<c:if test="${method == 'create' }">
					<tr>
						<td width="20%" class="outDetail">
							标题：
						</td>
						<td class="outDetail2" colspan="3">
							<input type="text" id="title" name="title" dataType="Require" msg="请填写标题" />&nbsp;&nbsp;
						</td>
					</tr>
					<tr>
						<td width="20%" class="outDetail">
							推送内容：
						</td>
						<td class="outDetail2" colspan="3">
							<textarea rows="9" cols="60" id="content" name="content" dataType="Require" msg="请填写内容" style="border-color: #EAF7FA; bo"></textarea>
						</td>
					</tr>
					
					</c:if>
					<c:if test="${method == 'show' }">
					<tr>
						<td width="20%" class="outDetail">
							标题：
						</td>
						<td class="outDetail2" colspan="3">
							${pushInfo.title }
						</td>
					</tr>
					<tr>
						<td width="20%" class="outDetail">
							推送内容：
						</td>
						<td class="outDetail2" colspan="3">
							<textarea rows="9" cols="60" id="content" name="content" dataType="Require" msg="请填写内容" style="border-color: #EAF7FA; bo">${pushInfo.content }</textarea>
						</td>
					</tr>
					
					</c:if>
				 </table>
		    </div>
		</div>
		<br />
		<p align="center">
			<c:if test="${param.method!='show'}">
				<sgy:button cssClass="ovalbutton" onclick="sav(this);return false;">推送</sgy:button>
			</c:if>&nbsp;
			<sgy:button cssClass="ovalbutton" onclick="window.close();">关 闭</sgy:button>
		</p>
	</form>
</body>
<script type="text/javascript" defer="defer">
	$(document).ready(function(){
		
	});
	
	
	//保存
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
