<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/includeBase.jsp" %>
<%@ include file="/inc/includeEasyUi.jsp"%>
<html>
	<head>
		<title>系统日志</title>
	</head>
	<body>
		<sgy:csrfToken />
		<div id="tabsModuleRole" class="easyui-tabs" data-options="fit:true,border:false">
			<div id="easytab1" title="系统登录流水">
				<iframe id="queryModule" frameborder="0" height="99.5%" width="100%" scrolling="no"
					src=""></iframe>
			</div>
			<div id="easytab2" title="菜单访问流水">
				<iframe id="cdfwls" frameborder="0" height="99.5%" width="100%" scrolling="no"
					src=""></iframe>
			</div>
			<div id="easytab3" title="移动端登录流水">
				<iframe id="mobiLogin" frameborder="0" height="99.5%" width="100%" scrolling="no"
					src=""></iframe>
			</div>
		</div>
		<input type="hidden" id="tabsId" name="tabsId" />
	</body>
	<script type="text/javascript">
		$('#tabsModuleRole').tabs({
			onSelect:function(title){
				var tab = $('#tabsModuleRole').tabs('getSelected');
				var tbid = tab.attr('id');
				if('easytab1'==tbid){
					if($('#tabsId').val() != 'easytab1'){
						$('#queryModule').attr('src', '${app}/sys/log/loginHisFrame.do');
						$('#tabsId').val('easytab1');
					}
				}else if('easytab2'==tbid){
					if($('#tabsId').val() != 'easytab2'){
						$('#cdfwls').attr('src', '${app}/sys/log/nodeHisFrame.do');
						$('#tabsId').val('easytab2');
					}
				}else if('easytab3'==tbid){
					if($('#tabsId').val() != 'easytab3'){
						$('#mobiLogin').attr('src', '${app}/sys/log/mobileLoginHisFrame.do');
						$('#tabsId').val('easytab3');
					}
				}
			}
		});
	</script>
</html>

