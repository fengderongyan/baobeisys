<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/includeBase.jsp" %>
<%@ include file="/inc/includeEasyUi.jsp"%>
<html>
	<head>
		<title>权限管理</title>
	</head>
	<body>
		<sgy:csrfToken />
		<div id="tabsModuleRole" class="easyui-tabs" data-options="fit:true,border:false">
			<c:forEach items="${yjbbLevList }" var="yjbbLevMap" >
				<div id="easytab_${yjbbLevMap.dd_item_code}" title="${yjbbLevMap.dd_item_name }" data-options="closable:false" style="overflow: auto;">
					<iframe id="easytab_${yjbbLevMap.dd_item_code}_iframe" frameborder="0" height="99.5%" width="100%" scrolling="no"
						src="${app}/yjcfg/yjcfg.do?deal_lev=${yjbbLevMap.dd_item_code}"></iframe>
				</div>
			</c:forEach>
		</div>
	</body>
	<script type="text/javascript">
		var operatorIdStr = '${param.operatorIdStr}';
		var bb_type = '${param.bb_type}';
		 $('#tabsModuleRole').tabs({
			onSelect:function(title){
				var tab = $('#tabsModuleRole').tabs('getSelected');
				var tbid = tab.attr('id');
				var deal_lev =tbid.substring(tbid.indexOf('_') + 1);
				$('#' + tbid + '_iframe').attr('src', '${app}/yjcfg/yjcfg.do?deal_lev=' + deal_lev + '&operatorIdStr=' + operatorIdStr + '&bb_type=' + bb_type);
			}
		}); 
	</script>
</html>

