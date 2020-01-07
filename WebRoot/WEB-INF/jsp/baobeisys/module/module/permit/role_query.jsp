<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>前台角色权限管理</title>
		<%@ include file="/inc/includeBase.jsp" %>
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
		
		<style type="text/css">
		
		.custom-combobox-input {
			width:300px;
		}
		.ui-autocomplete {
			position: absolute;
			top: 0;
			left: 0;
			cursor: default;
			max-height: 300px;
			max-width: 300px;
			overflow-y: auto;
		}
		</style>
	</head>
	<body class="sgy" scroll="no">
		<form id="zTreeForm" name="dTreePostForm" method="post" action="${app}/permit/roleSave.do">
			<sgy:csrfToken />
		   	 <div style="overflow: hidden;">
			   	 <div align="center" style="margin-top: 5px;"> 
			    	&nbsp;&nbsp;角色视野：
						<sgy:select list="${roles}" id="role_id" name="role_id" onchange="cha(this.value);"
							headValue="" optionLabel="GROUP_ROLE_NAME" optionValue="ROLE_ID"  
							value="${param.role_id}" style="width:300px;" />
						&nbsp;&nbsp;
						<label id="dis_none">
						<sgy:button cssClass="ovalbutton" onclick="save(this);">保 存</sgy:button></label>
						<input type="hidden" name="moduleIds" id="moduleIds" value="">
					&nbsp;&nbsp;
			    </div>
			    <div style="overflow-y: auto;height: 500px;padding:5px;">
			    	<ul id="roleModuleTree" class="ztree"></ul>
			    </div>
		   	 </div>
		</from>
	</body>
	<script language="javascript">
	/**保存角色模块配置**/
	function save(src) {
		disable(src);
		var role_id = $('#role_id').val();
		if(''==role_id || null==role_id){
			alert("请选择角色视野！");
			enable(src);
			return ;
		}
		var treeObj = $.fn.zTree.getZTreeObj("roleModuleTree");
		var sNodes = treeObj.getCheckedNodes();
		var moduleIds = '';
		if(sNodes.length !=0){
			var moduleids = '';
			for(var k=0; k<sNodes.length; k++){
				moduleIds += ''+sNodes[k].id+',';
			}
			moduleIds = moduleIds.substr(0,moduleIds.length-1);
		}else{
			if(!confirm('未选着任何模块，确认提交吗？')){
				enable(src);
				return false;
			}
		}
		$('#moduleIds').val(moduleIds);
		
		var url = '${app}/permit/roleSave.do';
		var params="moduleIds="+moduleIds+"&role_id="+role_id;
		var res = new MyJqueryAjax(url,params).request();
		if(res==1) {
			alert('操作成功！');
	  	} else {
			alert('操作失败！');
	  	}
	  	enable(src);
	}
	//角色下拉列表框模糊查询
	$("#role_id" ).combobox();
	
	//下拉列表框onchange事件
	$('#role_id').combobox({
		select:function(){
			chanageRole();
		}
	});
	function chanageRole(){
		var role_id = $('#role_id').val();
		if(''==role_id || null==role_id){
			return ;
		}
		window.location.href="${app}/permit/roleQuery.do?role_id="+role_id+'&csrfToken='+$('#csrfToken').val();
	}
	</script>
	<!-- ZTree 异步请求加载树 -->
	<script type="text/javascript">
		var setting = {
			async: {
				enable: false,
				url: getUrl
			},
			check: {
				//chkboxType: { "Y" : "s", "N" : "ps" },
				chkboxType: { "Y" : "ps", "N" : "s" },
				enable: true
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				beforeExpand: beforeExpand,
				onAsyncSuccess: onAsyncSuccess,
				onAsyncError: onAsyncError
			}
		};
		
		function getUrl(treeId, treeNode) {
			var param = "module_id="+ treeNode.id, 
			aObj = $("#" + treeNode.tId + "_a");
			return "${app}/permit/loadModulePermit.do?" + param;
		}
		
		function beforeExpand(treeId, treeNode) {
			if (treeNode.page == 0) treeNode.page = 1;
			return !treeNode.isAjaxing;
		}
		function onAsyncSuccess(event, treeId, treeNode, msg) {
			var zTree = $.fn.zTree.getZTreeObj("roleModuleTree");
			zTree.updateNode(treeNode);
		}
		function onAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
			var zTree = $.fn.zTree.getZTreeObj("roleModuleTree");
			alert("异步获取数据出现异常。");
			treeNode.icon = "";
			zTree.updateNode(treeNode);
		}
		var zTreeMenuTree;
		$(document).ready(function(){
			var role_id = $('#role_id').val();
			//初始化组织树
			var url1 = '${app}/permit/loadModulePermit.do?role_id='+role_id;
			new MyJqueryAjax(url1, null, showTree).request();
		});
		
		function showTree(req){
			var orgTreeJson = eval(req);
			if(orgTreeJson.length==0){
				alert('暂无数据！');
				window.close();
				return false;
			}
			zTreeMenuTree = $.fn.zTree.init($("#roleModuleTree"), setting, orgTreeJson);
			zTreeMenuTree.expandNode(zTreeMenuTree.getNodeByParam("id", "0_M"));
		}
	</script>
</html>