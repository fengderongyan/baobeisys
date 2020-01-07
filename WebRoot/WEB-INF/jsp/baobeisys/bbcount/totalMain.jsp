<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/includeBase.jsp" %>
<%@ include file="/inc/includeEasyUi.jsp"%>
<html>
	<head>
		<title>报备汇总</title>
	</head>
	<body>
		<sgy:csrfToken />
		<div id="tabsModuleRole" class="easyui-tabs" data-options="fit:true,border:false">
			<div id="easytab1" title="饮酒报备汇总">
				<iframe id="yjTotal" frameborder="0" height="99.5%" width="100%" scrolling="no"
					src=""></iframe>
			</div>
			<div id="easytab2" title="请假报备汇总">
				<iframe id="qjTotal" frameborder="0" height="99.5%" width="100%" scrolling="no"
					src=""></iframe>
			</div>
			<div id="easytab3" title="用车报备汇总">
				<iframe id="ycTotal" frameborder="0" height="99.5%" width="100%" scrolling="no"
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
						$('#yjTotal').attr('src', '${app}/bbcount/yjTotalFrame.do');
						$('#tabsId').val('easytab1');
					}
				}else if('easytab2'==tbid){
					if($('#tabsId').val() != 'easytab2'){
						$('#qjTotal').attr('src', '${app}/bbcount/qjTotalFrame.do');
						$('#tabsId').val('easytab2');
					}
				}else if('easytab3'==tbid){
					if($('#tabsId').val() != 'easytab3'){
						$('#ycTotal').attr('src', '${app}/bbcount/ycTotalFrame.do');
						$('#tabsId').val('easytab3');
					}
				}
			}
		});
	</script>
</html>

