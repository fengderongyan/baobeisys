<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>模块管理</title>
		<%@ include file="/inc/includeBase.jsp" %>
		<link rel="stylesheet" href="${app}/css/ztree/zTreeStyle/zTreeStyle.css" type="text/css" />
		<script type="text/javascript" src="${app}/js/ztree/jquery.ztree.core-3.5.js"></script>
		<script type="text/javascript" src="${app}/js/ztree/jquery.ztree.excheck-3.5.js"></script>
	</head>
	<body class="sgy" scroll="auto">
		<form id="zTreeForm">
			<sgy:csrfToken />
			<div id="title" style='position: absolute; width: 100%; z-index: 20; left: 0px; top: 10px; overflow: hidden;'>
				<div style="text-align: center">
					<sgy:button cssClass="ovalbutton" onclick="add();return false;">新 增</sgy:button>
				</div>
				<div style="text-align: left; overflow-y: auto; overflow-x: auto; padding-left: 10px; padding-top: 0px; height: 550px;">
					<ul id="moduleTree" class="ztree"><img style="vertical-align: middle;" src="${app}/images/wait.gif" />&nbsp;正在加载菜单，请稍候......</ul>
				</div>
			</div>
		</form>
	</body>
	<script language="javascript">
		//初始化组织树
		$(document).ready(function(){
			setTimeout(loadOrgTree, 50);
		});
		
		//加载组织树
		function loadOrgTree(){
		
			var url = '${app}/adminmodule/loadModuleList.do'; 
			new MyJqueryAjax(url, null, showOrgTree).request();
		} 
		
		
		function add() {
			MyWindow.OpenNoCsrfWindowScroll('${app}/adminmodule/editModule.do?action=add', 'addAdminModule', 650, 850);
		}
		
		/**********ZTree 异步请求加载树**********/
		var setting = {
			async: {
				enable: false
			},
			
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				beforeClick: beforeClick,
				beforeExpand: beforeExpand,
				onAsyncSuccess: onAsyncSuccess,
				onAsyncError: onAsyncError
			}
		};
		
		function beforeClick(treeId, treeNode) {
			if(treeNode.id=='0'){
				return false;
			}
			MyWindow.OpenNoCsrfWindowScroll('${app}/adminmodule/editModule.do?action=update&module_id='+treeNode.id, 'editAdminModule', 600, 850);
		}
		
		function beforeExpand(treeId, treeNode) {
			if (treeNode.page == 0) treeNode.page = 1;
			return !treeNode.isAjaxing;
		}
		
		function onAsyncSuccess(event, treeId, treeNode, msg) {
			var zTree = $.fn.zTree.getZTreeObj("moduleTree");
			zTree.updateNode(treeNode);
		}
		
		function onAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
			var zTree = $.fn.zTree.getZTreeObj("moduleTree");
			alert("异步获取数据出现异常。");
			zTree.updateNode(treeNode);
		}
		
		
		function showOrgTree(req){
			var orgTreeJson = eval(req);
			if(orgTreeJson.length==0){
				return false;
			}
			zTreeOrgTree = $.fn.zTree.init($("#moduleTree"), setting, orgTreeJson);
			expandNodeLevel(0);
		}
		
		//根据层级展开树节点
		function expandNodeLevel(nodeLevel){
			var nodes = zTreeOrgTree.getNodes();
			for(var i=0;i<nodes.length;i++){
				if(nodes[i].level<=nodeLevel){
					zTreeOrgTree.expandNode(zTreeOrgTree.getNodeByParam("id", nodes[i].id));
				}
				
				var cNodes = zTreeOrgTree.transformToArray(nodes[i])
				for(var j=0;j<cNodes.length;j++){
					if(cNodes[j].level<=nodeLevel && nodes[i].level<cNodes[j].level){
						zTreeOrgTree.expandNode(zTreeOrgTree.getNodeByParam("id", cNodes[j].id));
					}
				}
			}
		}
	</script>
</html>