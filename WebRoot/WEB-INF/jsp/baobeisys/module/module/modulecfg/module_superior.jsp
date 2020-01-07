<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>前台选择上级目录</title>
		<%@ include file="/inc/includeBase.jsp" %>
		<%@ include file="/inc/includeEasyUi.jsp" %>
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<link rel="stylesheet" href="${app}/css/ztree/zTreeStyle/zTreeStyle.css" type="text/css" />
		<script type="text/javascript" src="${app}/js/ztree/jquery.ztree.core-3.5.js"></script>
		<script type="text/javascript" src="${app}/js/ztree/jquery.ztree.excheck-3.5.js"></script>
	</head>
	<body class="sgy" scroll="no">
		<form id="form2" name="form2" action="" method="post">
			<sgy:csrfToken />
			<div style="height:350px; overflow: auto;">
				<div title="上级模块目录" style="padding: 15px;">
					<ul id="moduleTree" class="ztree"></ul>
				</div>
			</div>
			<br />
			<p align="center">
				<sgy:button cssClass="ovalbutton" onclick="isOK(this);return false;">确 定</sgy:button>
				&nbsp;&nbsp;
				<sgy:button cssClass="ovalbutton" onclick="window.close();">关 闭</sgy:button>
			</p>
			<br />
		</form>
	</body>
	<script type="text/javascript" defer="defer">
		//确定
		function isOK(src) {
			var treeObj = $.fn.zTree.getZTreeObj("moduleTree");
			var sNodes = treeObj.getCheckedNodes();
			var strs = '';
			if(sNodes.length !=0){
				var module_name = '';
				var module_id = '';
				var module_level = '';
				for(var k=0; k<sNodes.length; k++){
					if(k != sNodes.length-1){
						module_id += ''+sNodes[k].id+',';
						module_name += ''+sNodes[k].name+',';
						module_level += ''+sNodes[k].level+',';
					}else{
						module_id += ''+sNodes[k].id;
						module_name += ''+sNodes[k].name;
						module_level += ''+sNodes[k].level;
					}
				}
				if(module_id =='1213'){
					module_level = '0';
				}
				module_level = parseInt(module_level)+1;
				strs = module_id+'^'+module_name+'^'+module_level;
			}
			window.returnValue=strs;
			window.close();
		}
	</script>
	
	<!-- ZTree 异步请求加载树 -->
	<script type="text/javascript">
		var module_id = '${param.module_id}';
		var setting = {
			async: {
				enable: true,
				url: getUrl
			},
			check: {
				chkStyle: "radio",
				radioType: "all",
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
				onAsyncError: onAsyncError,
				onClick: zTreeOnClick
			}
		};
		
		// 节点被点击的事件回调函数（能点击节点选中）
	    function zTreeOnClick(event, treeId, treeNode) {
	       var treeObj = $.fn.zTree.getZTreeObj("moduleTree");
	       var node = treeObj.getNodeByTId(treeNode.tId);
	       treeObj.checkNode(node, true, true);
	    }
		
		function getUrl(treeId, treeNode) {
			var param = "module_id="+ treeNode.id+'&superior_id='+module_id, 
			aObj = $("#" + treeNode.tId + "_a");
			return "${app}/admin/module/loadSuperiorModule.do?" + param;
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
			treeNode.icon = "";
			zTree.updateNode(treeNode);
		}
		
		$(document).ready(function(){
			//初始化组织树
			var url1 = '${app}/module/loadSuperiorModule.do?superior_id='+module_id;
			new MyJqueryAjax(url1, null, showTree).request();
		});
		
		function showTree(req){
			var orgTreeJson = eval(req);
			if(orgTreeJson.length==0){
				alert('暂无数据！');
				window.close();
				return false;
			}
			zTreeOrgTree = $.fn.zTree.init($("#moduleTree"), setting, orgTreeJson);
		}
	</script>
</html>