<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ include file="/inc/includeBase.jsp" %>
<c:set var="iconPath" value="images/menu_img"/>
	<head>
		<title>选择图标</title>
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="expires" content="0" />
	</head>
	<body>
		<sgy:csrfToken />
		<div style="height: 330px;overflow: auto;">
			<table align="center" width="90%" style="margin-top: 20px;">
				<c:forEach items="${moduleIconList}" var="iconName" varStatus="s">
					<c:if test="${(s.count - 1) % 5 == 0}"><tr></c:if>
						<td align="center">
							<img src='${app}/${iconPath}/${iconName}' class="modIcon"
								width='30' height='30' onclick="chooseIcon(this)" style="border: 2px solid white;"/>
								<br/>
							<input type="radio" name="moduleIcon" value="${iconName}" 
								class="iconRadio" onclick="chooseRadio(this);"/><br/><br/>
						</td>
				</c:forEach>
			</table>
		</div>
		<br/>
		<div align="center">
			<sgy:button cssClass="ovalbutton" onclick="getIcon();return false;">保 存</sgy:button>&nbsp;&nbsp;
			<sgy:button cssClass="ovalbutton" onclick="window.close();return false;">关 闭</sgy:button>
		</div>
	</body>
	<script type="text/javascript">
	$(document).ready(function(){
		var chooseIcon = "${param.module_icon}";
		if(chooseIcon != "")
		{
			$('input:radio[name="moduleIcon"]').each(function(i){
				if($(this).val() == chooseIcon)
				{
					$(this).prev().prev().attr("style", "border: 2px solid red");
					$(this).attr("checked", true);
				}
			});
		}
	});
	
	// 选中单选按钮
	function chooseRadio(src)
	{
		$(src).next().next().attr("checked", true);
		
		$("img.modIcon").each(function(i){
			//$(this).attr("style", "border: 2px solid white");
		});
		//$(src).prev().prev().attr("style", "border: 2px solid red");
	}
	// 选中图标
	function chooseIcon(src)
	{
		$("img.modIcon").each(function(i){
		//	$(this).attr("style", "border: 2px solid white");
		});
		//$(src).attr("style", "border: 2px solid red");
		$(src).next().next().attr("checked", true);
	}
	// 确定选择
	function getIcon()
	{
		var module_icon = $('input:radio[name="moduleIcon"]:checked').val();
		if(module_icon == null)
		{
			alert("请选择模块图标！");
			return false;
		}
		window.returnValue = module_icon;
		window.close();
	}
	</script>
</html>