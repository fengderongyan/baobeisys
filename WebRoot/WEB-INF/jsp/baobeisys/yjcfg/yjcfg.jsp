<%@ page contentType="text/html; charset=UTF-8" %>

<html>
	<head>
		<title>饮酒审核配置</title>
		<%@ include file="/inc/includeBase.jsp" %>
		<%@ include file="/inc/includeEasyUi.jsp" %>
		<link rel="stylesheet" href="${app}/css/ztree/zTreeStyle/zTreeStyle.css" type="text/css" />
		<script type="text/javascript" src="${app}/js/ztree/jquery.ztree.core-3.5.js"></script>
		<script type="text/javascript" src="${app}/js/ztree/jquery.ztree.excheck-3.5.js"></script>
		<link rel="stylesheet" type="text/css" href="${app}/css/themes/default/default.css" />
	</head>
	<body class="sgy" scroll="auto">
		<sgy:csrfToken />
		<div id="cc" class="easyui-layout" style="width:100%;height:97%;">  
			<div id="div_role" data-options="region:'north',fit:false" style="padding:5px;">
			
					<div style="font-size: 15px;font-weight: bold;padding:3px;line-height:30px" >
						审核人：
						<span id="dealSpan">
						<c:if test="${dealUsers != null }">
							<c:forEach items="${dealUsers }" var="dealUserMap">
								<span style="border: 1px solid #E1E5E8;padding:2px">
									${dealUserMap.deal_oper_name }&nbsp;
									<a style="font-size: 12px;color: red;" onclick="deleteDealUser('${dealUserMap.deal_oper_id }')">删除</a>
								</span>
							</c:forEach>
						</c:if>
						</span>
					</div>
				
		    </div>
		    <div data-options="region:'west',split:false" style="width:260px;height: 550px;" >
		    	<ul id="moduleTree" class="ztree"><img style="vertical-align: middle;" src="${app}/images/wait.gif" />&nbsp;正在加载菜单，请稍候......</ul>
		    </div>
		    <!-- 中间部分 -->
		    <div id="div_dealuser" data-options="region:'center',fit:false" style="padding:5px;overflow: scroll;">
		    </div>  
		</div>
		<br/>
		<input type="hidden" name="module_id" id="module_id" value="">
	</body>
	<script language="javascript">
	var bb_type = '${param.bb_type}';
	//初始化组织树
	$(document).ready(function(){
		setTimeout(loadOrgTree, 50);
	});
	
	//加载组织树
	function loadOrgTree(){
	
		var url = '${app}/yjcfg/loadOrgList.do'; 
		new MyJqueryAjax(url, null, showOrgTree).request();
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
		if(treeNode.id == '10000'){
			return false;
		}
		var res = new MyJqueryAjax('${app}/yjcfg/getUserListByOrgId.do?org_id='+treeNode.id,null).request();
		//sgyjs.doReturnAjax('${app}/yjcfg/getUserListByOrgId.do', {org_id:treeNode.id}, 'text', 'post');
		$('#div_dealuser').html(res);
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
	//添加审核人
	function addDealOper(deal_oper_id, deal_oper_name){
		var dealSpanHtml = $('#dealSpan').html();
		var flag_reset = 0;
		if($("#dealSpan>span").length == 0){//无子元素span
			flag_reset = 1;
		}
		var deal_lev = '${param.deal_lev}';
		var operatorIdStr = '${param.operatorIdStr}';
		var url = '${app}/yjcfg/addDealOper.do';
		var param = {deal_oper_id:deal_oper_id,deal_oper_name:deal_oper_name,deal_lev:deal_lev,operatorIdStr:operatorIdStr,flag_reset:flag_reset,bb_type:bb_type};
		var dealUserList = sgyjs.doReturnAjax(url, param, 'json', 'post');
		$('#dealSpan').html('');
		for(var i=0;i<dealUserList.length;i++){
			$('#dealSpan').append(
				'<span style="border: 1px solid #E1E5E8;padding:2px">' +
					 dealUserList[i].deal_oper_name + '&nbsp;' +
					'<a style="font-size: 12px;color: red;" onclick="deleteDealUser(&quot;' + dealUserList[i].deal_oper_id + '&quot;)">删除</a>' +
				'</span>'		
			)
		}
		
	}
	
	//删除审核人
	function deleteDealUser(deal_oper_id){
		var deal_lev = '${param.deal_lev}';
		var operatorIdStr = '${param.operatorIdStr}';
		var url = '${app}/yjcfg/deleteDealUser.do';
		var param = {deal_oper_id:deal_oper_id,deal_lev:deal_lev,operatorIdStr:operatorIdStr,bb_type:bb_type};
		var dealUserList = sgyjs.doReturnAjax(url, param, 'json', 'post');
		$('#dealSpan').html('');
		for(var i=0;i<dealUserList.length;i++){
			$('#dealSpan').append(
				'<span style="border: 1px solid #E1E5E8;padding:2px">' +
					 dealUserList[i].deal_oper_name + '&nbsp;' +
					'<a style="font-size: 12px;color: red;" onclick="deleteDealUser(&quot;' + dealUserList[i].deal_oper_id + '&quot;)">删除</a>' +
				'</span>'		
			)
		}
	}
	</script>
</html>