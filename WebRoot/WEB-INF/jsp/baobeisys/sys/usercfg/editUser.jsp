<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/inc/includeBase.jsp" %>
	<%@ include file="/inc/includeEasyUi.jsp" %>
	<link rel="stylesheet" href="${app}/css/ztree/zTreeStyle/zTreeStyle.css" type="text/css">
	<script type="text/javascript" src="${app}/js/ztree/jquery.ztree.core-3.5.js"></script>
	<script type="text/javascript" src="${app}/js/ztree/jquery.ztree.excheck-3.5.js"></script>
	<c:choose>
		<c:when test="${param.method=='edit'}">
			<c:set var="title" value="修改用户信息"></c:set>
		</c:when>
		<c:when test="${param.method=='create'}">
			<c:set var="title" value="新建用户"></c:set>
		</c:when>
		<c:when test="${param.method=='show'}">
			<c:set var="title" value="用户详细信息"></c:set>
		</c:when>
	</c:choose>
	<title>${title}</title>
	<style type="">
	.hintInfo{padding:8px 35px 8px 10px;text-shadow:none;-webkit-box-shadow:0 2px 4px rgba(0,0,0,0.2);-moz-box-shadow:0 2px 4px rgba(0,0,0,0.2);box-shadow:0 2px 4px rgba(0,0,0,0.2);background-color:#f9edbe;border:1px solid #f0c36d;-webkit-border-radius:2px;-moz-border-radius:2px;border-radius:2px;color:#333}
	
	</style>
</head>
<body>
	<form id="form2" name="form2" action="${app}/sys/usercfg/saveOrUpdateUser.do" method="post"  >
		<sgy:csrfToken />
		<div id="queryPanel" class="queryPanel">
			<div id="queryPanel_title" class="queryPanel_title">
				<div class="queryPanel_title_collapse"></div>&nbsp;${title}
			</div>
		    <div id="queryPanel_content" class="queryPanel_content">
		    	<input type="hidden" name="method" value="${param.method}" />
		    	<input type="hidden" id="org_level" name="org_level"  value="${userInfo.org_level}" />
				<table class="detail_table" cellspacing="0" cellpadding="0" style="width: 100%;">
					<tr height="20">
						<td class="outDetail">
							<font color="red">*</font>&nbsp;工号：
						</td>
						<td class="outDetail2">
							<c:choose>
								<c:when test="${param.method=='create'}">
									<input type="text" id="operator_id" name="operator_id" value="${userInfo.operator_id}" 
										style="width: 90%;" required="true" dataType="Require" msg="工号不能为空" />
								</c:when>
								<c:otherwise>
									<input type="hidden" id="operator_id" name="operator_id" value="${userInfo.operator_id}" 
										style="width: 90%;" required="true" dataType="Require" msg="工号不能为空" />
									${userInfo.operator_id}
								</c:otherwise>
							</c:choose>
							<input type="hidden" id="old_operator_id" name="old_operator_id" value="${userInfo.operator_id}" />	
						</td>
						<td  class="outDetail">
							<font color="red">*</font>&nbsp;姓名：
						</td>
						<td class="outDetail2">
							<input type="text" id="name" name="name" value="${userInfo.name}" 
								style="width: 90%;" required="true" dataType="Require" msg="姓名不能为空" />
						</td>
					</tr>
					<tr >
						<td class="outDetail">
							<font color="red">*</font>&nbsp;手机：
						</td>
						<td class="outDetail2">
							<input type="text" id="mobile" name="mobile" maxlength="11" style="width: 90%;"
								value="${userInfo.mobile}" required="true" dataType="Number" msg="请填写数字号码!" />
						</td>
						<td  class="outDetail">
							邮箱：
						</td>
						<td   class="outDetail2">
							<input type="text" id="email" name="email" value="${userInfo.EMAIL}" style="width: 90%;" />
						</td>
					</tr>
					
					
					<!-- <tr height="20">
						<td class="outDetail">
							排序：
						</td>
						<td class="outDetail2">
							<input type="text" id="user_order" name="user_order" value="${userInfo.user_order}" 
								style="width: 90%;" require="false" dataType="Number" msg="排序必须为数字" />
						</td>
						<td class="outDetail" >
						</td>
						<td class="outDetail2">
						</td>
					</tr>
				--></table>
		    </div>
		</div>
		<br />
		<div style="width: 98%;height: 300px;margin:0 auto;text-align: center;">
			<div class="easyui-tabs" data-options="fit:true,border:true">
				<div title="用户组织" style="padding:5px;overflow-y:auto;overflow-x: hidden;"> 
					<input type="hidden" id="org_id" name="org_id" value="${userInfo.org_id}"  required="true" dataType="Require" msg="请选择组织！" />
					<ul id="orgztree" class="ztree"><img id="loadImg" style="vertical-align: middle;" src="${app}/images/wait.gif" />&nbsp;正在加载组织，请稍候......</ul>
				</div>
				<div title="用户角色" style="padding:5px;overflow-y:auto;overflow-x: hidden;"> 
					<sgy:checkboxlist name="role_id" id="role_id" list="rolesList"
						checkboxLabel="ROLE_NAME" checkboxValue="ROLE_ID"
						value="${userInfo.role_ids}" delims="," columns="1" onclick="checkRole(this);"
						required="true" dataType="Group" msg="至少为用户分配一种角色" />
				</div>
			</div>
		</div>
		<br />
		<p align="center">
			<c:if test="${param.method!='show'}">
				<sgy:button cssClass="ovalbutton" id="saveBtn" onclick="sav(this);return false;">保 存</sgy:button>
			</c:if>&nbsp;
			<sgy:button cssClass="ovalbutton" onclick="window.close();">关 闭</sgy:button>
		</p>
		<br />
		<br />
	</form>
	<div class="hintInfo"><strong>温馨提示：</strong>
		<div>a)新增用户初始密码为：1-6。</div>
	</div>
</body>
<script type="text/javascript" defer="defer">
	//验证方法
	function errorCheck() { 
		var operator_id = $('#operator_id').val().trim();
		if(operator_id.length<3){
			alert('工号不能小于三位！');
			return;
		};
		
		if(!Validator.Validate('form2')){
			return false;
		}
		
		//判断工号是否存在
		var isOperIdExist=false;
		var url1 = '${app}/sys/usercfg/checkUserOperId.do';
		var params1 = 'operator_id='+$('#operator_id').val()+'&method='+'${param.method}';
		var ajaxRes1 = new MyJqueryAjax(url1, params1).request();
		if(ajaxRes1 != 0) {
			isMobileExist=true; 
		}
		if(isMobileExist==true) {
			alert('该工号已存在！');
			$('#operator_id').focus();
			return false; 
		}
		
		//判断手机号码是否存在
		var isMobileExist=false;
		var url2 = '${app}/sys/usercfg/checkUserMsisdn.do';
		var params2 = 'operator_id='+$('#old_operator_id').val()+'&mobile='+$('#mobile').val() + '&method='+'${param.method}';
		var ajaxRes2 = new MyJqueryAjax(url2, params2).request();
		if(ajaxRes2 != 0) {
			isMobileExist=true; 
		}
		if(isMobileExist==true) {
			alert('该手机号码已存在！');
			$('#mobile').focus();
			return false; 
		}
		
		return true;
	}
	
	//保存方法
	function sav(src) {
		//防止重复提交
		disable(document.getElementById("saveBtn"));
		if(!errorCheck()) {
			enable(document.getElementById("saveBtn"));
			return false;
		}
		form2.submit();
	}
	
	/******* ZTree 请求加载树 *****/
	var org_id = '${param.org_id}';
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
			onAsyncError: onAsyncError
		}
	};
	
	//获取地址
	function getUrl(treeId, treeNode) {
		var param = 'org_id='+ treeNode.id+'&csrfToken='+$('#csrfToken').val();
		var url = '${app}' + '/sys/usercfg/loadOrgListTree.do?' + param;
		return url;
	}
	
	//获取成功组织树
	function onAsyncSuccess(event, treeId, treeNode, msg) {
		var zTree = $.fn.zTree.getZTreeObj("orgztree");
		zTree.updateNode(treeNode);
	}
	
	//获取失败组织树
	function onAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
		var zTree = $.fn.zTree.getZTreeObj("orgztree");
		alert("异步获取组织数据出现异常。");
		treeNode.icon = "";
		zTree.updateNode(treeNode);
	}
	
	//加载组织树
	function loadOrgTree(){
		var param = '?org_id='+ $('#org_id').val();
		var url1 = '${app}/sys/usercfg/loadOrgListTree.do' + param;
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

	//页面事件
	document.onkeydown=function(event){
		var e = event || window.event || arguments.callee.caller.arguments[0];
		//禁用回车事件
		if(e && e.keyCode==13){
			return false;
		}
	}; 
	
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

		$('#org_id').val(treeNode.id);
	}
	
	//点击通过名称模糊搜索
	var count=0;
	var lastName;
	$("#search").click(function(){
		var userName = $('#searchName').val();
		if(userName!=''){
			if(count==0){
			lastName=$('#searchName').val();
			}
			if(lastName!=$('#searchName').val()){
				count=0;
				lastName=$('#searchName').val();
			}
			var treeObj = $.fn.zTree.getZTreeObj("orgztree");
			//通过名称模糊搜索
			var nodes =   treeObj.transformToArray(treeObj.getNodesByParamFuzzy("name", lastName, null));
			for(i = count; i < nodes.length; i++) {
				//if(nodes[i].orgType=='user'){//只查找人员
					count++;
					if(count>=nodes.length){
					count=0;
					}
					if(nodes[i].name.indexOf(lastName)!=-1){
					treeObj.selectNode(nodes[i]);
					//实现自动打开
					//treeObj.expandNode(nodes[i], false,false , false);
					return;
					}
				//}
			}
		}
	});
	
	$('#searchName').keydown(function(e){
		if(e.keyCode==13){
		  if(count==0){
			lastName=$('#searchName').val();
			}
			if(lastName!=$('#searchName').val()){
				count=0;
				lastName=$('#searchName').val();
			}
			var treeObj = $.fn.zTree.getZTreeObj("orgztree");
			//通过名称模糊搜索
			var nodes =   treeObj.transformToArray(treeObj.getNodesByParamFuzzy("name", lastName, null));
			for(i = count; i < nodes.length; i++) {
				count++;
				if(count>=nodes.length){
				count=0;
				}
				if(nodes[i].name.indexOf(lastName)!=-1){
					treeObj.selectNode(nodes[i]);
					//实现自动打开
					//treeObj.expandNode(nodes[i], false,false , false);
					$('#searchName').focus();
					return;
				}
			}
		}
	}); 
	
	$(document).ready(function(){
		var org_level = $('#org_level').val();
		var org_id = $('#org_id').val();
		setTimeout(loadOrgTree, 50);
		$('input[name = role_id]').each(function(i,n){
			if($(n).attr("checked") == 'checked'){
				checkRole(n);
			}
		});
	});
	
	
	//选择角色
	function checkRole(obj){
		if(obj.value == '1006'){
			if(obj.checked){
				$('input[name = role_id]').each(function(i,n){
					if($(n).val() != '1006'){ 
						$(n).attr('checked',false);
						$(n).attr('disabled',true);
						$('#td_org_label').show();
						$('#td_org_val').show();
					}
				});
				$('#cp_id').attr('require',true);
			}else{
				$('input[name = role_id]').each(function(i,n){
					$(n).attr('disabled',false);
					$('#td_org_label').hide();
					$('#td_org_val').hide();
					$('#cp_id').val('');
					$('#cp_name').val('');
					$('#cp_id').attr('require',false);
				});
			}
		}
	}
	
	//搜索企业
	function searchCp(){
		MyWindow.OpenCenterWindowScroll('${app}/sys/usercfg/cpFrame.do','searchCp',650,700);
	}
	
	//选择企业编号
	function checkCp(cp){
		$('#cp_id').val(cp.cp_id);
		$('#cp_name').val(cp.cp_name);
	}
</script>
</html>
