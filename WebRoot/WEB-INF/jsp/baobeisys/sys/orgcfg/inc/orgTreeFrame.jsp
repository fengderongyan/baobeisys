<%@ page contentType="text/html; charset=UTF-8"%>
<head>
	<title>选择组织机构</title>
	<%@ include file="/inc/includeBase.jsp" %>
	<%@ include file="/inc/includeEasyUi.jsp" %>
	<link rel="stylesheet" href="${app}/css/ztree/zTreeStyle/zTreeStyle.css" type="text/css" />
	<script type="text/javascript" src="${app}/js/ztree/jquery.ztree.core-3.5.js"></script>
	<script type="text/javascript" src="${app}/js/ztree/jquery.ztree.excheck-3.5.js"></script>
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="expires" content="0" />
	<base target="_self">
</head>
<body class="sgy" scroll="no">
	<form id="formSch" name="formSch" method="post">
		<div id="orgTabs" class="easyui-tabs" data-options="fit:true,border:true" style="height: 445px;width: 200px;">
			<div id="orgTree" title="组织树" style="padding:5px;">
				<ul id="orgztree" class="ztree"><img style="vertical-align: middle;" src="${app}/images/wait.gif" />&nbsp;正在加载组织，请稍候......</ul>
			</div>
		</div>
		<p align="center">	
			<br />
			<sgy:button cssClass="ovalbutton" onclick="isOK(this);return false;">确 定</sgy:button>
			&nbsp;&nbsp;
			<sgy:button cssClass="ovalbutton" onclick="window.close();">关 闭</sgy:button>
		</p>
	</form>
</body>
<script type="text/javascript" defer="defer">
	//确定
	function isOK(src) {
		var tab = $('#orgTabs').tabs('getSelected');
		var tbid = tab.attr('id');
		var strs = '';
		if('orgTree' == tbid){//当前tab为组织树
			var treeObj = $.fn.zTree.getZTreeObj("orgztree");
			var sNodes = treeObj.getCheckedNodes();
			var cnt = 0 ;
			if(sNodes.length !=0){
				var superior_name = '';
				var superior_id = '';
				var region_id = '';
				var superior_id = '';
				var orgLevel = '';
				for(var k=0; k<sNodes.length; k++){
					if(k != sNodes.length-1){
						superior_id += ''+sNodes[k].id+',';
						superior_name += ''+sNodes[k].name+',';
						region_id += ''+sNodes[k].countyId+',';
						orgLevel += ''+sNodes[k].orgLevel+',';
					}else{
						superior_id += ''+sNodes[k].id;
						superior_name += ''+sNodes[k].name;
						region_id += ''+sNodes[k].countyId;
						orgLevel += ''+sNodes[k].orgLevel;
					}
					cnt = cnt+1;
				}
				
				//组织层级加1
				orgLevel = parseInt(orgLevel)+parseInt(1);
				
				//获取组织编号
				var url = '${app}/sys/orgcfg/getOrgId.do';
				var params="superior_id="+superior_id;
				var org_id = new MyJqueryAjax(url,params).request();
				strs = superior_id+'^'+superior_name+'^'+region_id+'^'+org_id+'^'+orgLevel;
			}
			if(cnt>50){
				alert('选择组织机构不能超过50个，当前选择：'+cnt);
				strs = '';
				return false;
			}
		}
		
		if (window.opener != undefined) { //chrome内核 
			window.opener.returnValueChrome(strs); //关闭前调用父窗口方法赋值
		}
		else {//IE内核
			window.returnValue = strs;
		}
		window.close();
	}
	
	/**********ZTree 异步请求加载树**********/
	var chooseType = '1';//1：单选 其他：多选
	var countyId = '${param.countyId}';
	var orgIds = '${param.orgIds}';
	var openLevel =  '';//树首次打开的层级
	var rejectOrgIds =  '${param.rejectOrgIds}';//要剔除的组织编号
	
	//定义单选按钮组织树样式
	var setting1 = {
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
			onCheck: radioCheck,
			onAsyncSuccess: onAsyncSuccess,
			onAsyncError: onAsyncError,
           	onClick: zTreeOnClick
		}
	};
	
	// 节点被点击的事件回调函数（能点击节点选中）
    function zTreeOnClick(event, treeId, treeNode) {
       var treeObj = $.fn.zTree.getZTreeObj("orgztree");
       var node = treeObj.getNodeByTId(treeNode.tId);
       treeObj.checkNode(node, true, true);
    }
	
	//获取地址
	function getUrl(treeId, treeNode) {
		var param = 'org_id='+ treeNode.id+'&orgIds='+orgIds+'&rejectOrgIds='+rejectOrgIds;
		return "${app}/sys/orgcfg/loadOrgTree.do?" + param;
	}
	
	//成功获取组织
	function onAsyncSuccess(event, treeId, treeNode, msg) {
		var zTree = $.fn.zTree.getZTreeObj("orgztree");
		zTree.updateNode(treeNode);
	}
	
	//获取数据出现异常
	function onAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
		var zTree = $.fn.zTree.getZTreeObj("orgztree");
		alert("异步获取组织数据出现异常。");
		treeNode.icon = "";
		zTree.updateNode(treeNode);
	}
	
	//初始化组织树
	$(document).ready(function(){
		setTimeout(loadOrgTree, 50);
	});
	
	//easyUi组件渲染完成后调用
	$.parser.onComplete = function(){
		
	}
	
	//加载组织树
	function loadOrgTree(){
		var params = '?countyId='+countyId+'&orgIds='+orgIds+'&rejectOrgIds='+rejectOrgIds;
		var url1 = '${app}/sys/orgcfg/loadOrgTree.do'+params;
		new MyJqueryAjax(url1, null, showOrgTree).request();
	}
	
	//展示组织树
	function showOrgTree(req){
		var orgTreeJson = eval(req);
		if(orgTreeJson.length==0){
			alert('暂无符合条件的组织机构！');
			return false;
		}
		zTreeOrgTree = $.fn.zTree.init($("#orgztree"), setting1, orgTreeJson);
		if(expandNodeLevel!=''){
			expandNodeLevel(openLevel);
		}else{
			expandNodeLevel(0);
		}
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
	
	//单选点击事件
	function radioCheck(event, treeId, treeNode){
		var zTree = $.fn.zTree.getZTreeObj("orgztree");
		if(treeNode.checked === true){//如果是选中，将其他选中节点置不选中
			var node = zTree.getCheckedNodes(true);//获取所有选中的节点
			for(var i = 0; i < node.length; i++){
				if(node[i].id != treeNode.id){
					zTree.checkNode(node[i], false, false, false);
				}
			}
		}
	}
	
	//页面事件
	document.onkeydown=function(event){
		var e = event || window.event || arguments.callee.caller.arguments[0];
		//禁用回车事件
		if(e && e.keyCode==13){
			return false;
		}
	}; 
</script>