<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/includeBase.jsp" %>
<%@ include file="/inc/includeEasyUi.jsp"%>
<body>
	<sgy:csrfToken />
		<div id="tabsModuleRole" class="easyui-tabs" data-options="fit:true,border:false">
			<div id="easytab1" title="基本信息">
				<iframe id="baseInfo" frameborder="0" height="99.5%" width="100%" scrolling="no"
					src=""></iframe>
			</div>
			<!-- <div id="easytab2" title="角色权限" data-options="closable:false" style="overflow: auto;">
				<iframe id="roleqx" frameborder="0" height="99.5%" width="100%" scrolling="no"
					src=""></iframe>
			</div> -->
		</div>
		<input type="hidden" id="tabsId" name="tabsId" />

</body>
<script type="text/javascript" defer="defer">
	
	$('#tabsModuleRole').tabs({
			onSelect:function(title){
				var tab = $('#tabsModuleRole').tabs('getSelected');
				var tbid = tab.attr('id');
				if('easytab1'==tbid){
					if($('#tabsId').val() != 'easytab1'){
						$('#baseInfo').attr('src', '${app}/sys/rolecfg/edit.do?method='+'${method}'+'&role_id='+'${role_id}');
						$('#tabsId').val('easytab1');
					}
				}/* else if('easytab2'==tbid){
					if($('#tabsId').val() != 'easytab2'){
						$('#roleqx').attr('src', '${app}/adminpermit/roleQuery.do?role_id='+'${role_id}');
						$('#tabsId').val('easytab2');
					}
				} */
			}
		});
</script>
</html>
