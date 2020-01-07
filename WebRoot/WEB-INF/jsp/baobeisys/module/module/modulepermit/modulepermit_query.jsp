<%@ page contentType="text/html; charset=UTF-8" %>

<html>
	<head>
		<title>前台模块权限管理</title>
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
		    <div data-options="region:'west',split:false" style="width:260px;height: 550px;" >
		    	<ul id="moduleTree" class="ztree"></ul>
		    </div>
		    <!-- 中间部分 -->
		    <div id="div_role" data-options="region:'center',fit:false" style="padding:5px;overflow: hidden;">
		    </div>  
		</div>
		<br/>
		<input type="hidden" name="module_id" id="module_id" value="">
	</body>
	<script language="javascript">
	//点击模块树 角色
	function editModulePermit(module_id) {
		$('#module_id').val(module_id);
		var url = '${app}/permit/showReplaceRole.do?module_id='+module_id;
		var res = new MyJqueryAjax(url,null).request();
		$('#div_role').html(res);
	}
	
	/**保存模块角色**/
	function saveModuleRole(src) {
		//防止重复提交
	 	disable(src);
		var roleIdStrs = $('#role_ids').val();//角色ID
		
        if(''==roleIdStrs){
        	if(!confirm('未选中任何角色，确认提交吗？')){
        		enable(src);
        		return false;
        	}
        }
        
        var module_id = $('#module_id').val();//菜单编号
		var url = '${app}/permit/saveModuleRole.do';
		var params="roleIdStrs="+roleIdStrs+"&module_id="+module_id;
		var res = new MyJqueryAjax(url,params).request();
		if(res==1) {
			alert('操作成功！');
			enable(src);
	  	} else {
			alert('操作失败！');
			enable(src);
	  	}
	}
	
	//搜索功能
	function searchTxt(){
    	var content = $('#roleDiv').html();
		var searchTxt = $('#searchTxt').val().trim();
		  if((/[\u4e00-\u9fa5]+/).test(searchTxt)||searchTxt==''){     
		     $('#roleDiv').textSearch(searchTxt,{markColor: "blue",markClass: "searchCss" });      
		  }
    }
    
	//角色全选或者全不选
	function chaeckedAll(tag,src){
		var checkeds=$(src).attr("checkeds"); 
		var roleId=$(src).attr("roleid"); 
		if(checkeds=='0'){
			$(src).attr("src","${app}/images/checked.png"); 
			$(src).attr("checkeds","1"); 
			$('img[name='+tag+']').each(function(i){
				$(this).attr("src","${app}/images/checked.png"); 
				$(this).attr("checkeds","1"); 
			});
		}else if (checkeds=='1'){
			$(src).attr("src","${app}/images/unchecked.png"); 
			$(src).attr("checkeds","0"); 
			$('img[name='+tag+']').each(function(i){
				$(this).attr("src","${app}/images/unchecked.png"); 
				$(this).attr("checkeds","0"); 
			});
		}
		insertRoleValue();
	}
	
	//角色全选或者全不选
	function chaeckedRole(tag,src){
		var checkeds=$(src).attr("checkeds"); 
		if(checkeds=='0'){
			$(src).attr("src","${app}/images/checked.png"); 
			$(src).attr("checkeds","1"); 
			$('img[name='+tag+']').each(function(i){
				$(this).attr("src","${app}/images/checked.png"); 
				$(this).attr("checkeds","1"); 
			});
			
		}else if (checkeds=='1'){
			$(src).attr("src","${app}/images/unchecked.png"); 
			$(src).attr("checkeds","0"); 
			$('img[name='+tag+']').each(function(i){
				$(this).attr("src","${app}/images/unchecked.png"); 
				$(this).attr("checkeds","0"); 
			});
			
			$('img[roleid='+roleId+']').each(function(i){
				$(this).attr("src","${app}/images/unchecked.png"); 
				$(this).attr("checkeds","0"); 
			});
		}
		insertRoleValue();
	}
	
	//按角色组全选或者全不选
	function chaeckedGroupAll(tag,src){
		var checkeds=$(src).attr("checkeds"); 
		if(checkeds=='0'){
			$(src).attr("src","${app}/images/checked.png"); 
			$(src).attr("checkeds","1"); 
			$('img[group='+tag+']').each(function(i){
				var roleId=$(this).attr("roleid"); 
				$('img[roleid='+roleId+']').each(function(i){
					$(this).attr("src","${app}/images/checked.png"); 
					$(this).attr("checkeds","1"); 
				});
			});
		}else if (checkeds=='1'){
			$(src).attr("src","${app}/images/unchecked.png"); 
			$(src).attr("checkeds","0"); 
			$('img[group='+tag+']').each(function(i){
				var roleId=$(this).attr("roleid"); 
				$('img[roleid='+roleId+']').each(function(i){
					$(this).attr("src","${app}/images/unchecked.png"); 
					$(this).attr("checkeds","0"); 
				});
			});
		}
		insertRoleValue();
	}
	
	//点击角色图片发生的事件，用来切换图片和判断是否选择
	function changeCheckedImg (src){
	  	var checkeds=$(src).attr("checkeds"); 
	  	var roleId=$(src).attr("roleid"); 
		if(checkeds=='0'){
			$(src).attr("src","${app}/images/checked.png"); 
			$(src).attr("checkeds","1"); 
			$('img[roleid='+roleId+']').each(function(i){
				$(this).attr("src","${app}/images/checked.png"); 
				$(this).attr("checkeds","1"); 
			});
		}else if (checkeds=='1'){
			$(src).attr("src","${app}/images/unchecked.png"); 
			$(src).attr("checkeds","0"); 
			
			$('img[roleid='+roleId+']').each(function(i){
				$(this).attr("src","${app}/images/unchecked.png"); 
				$(this).attr("checkeds","0"); 
			});
		}
		insertRoleValue();
	}
	
	/***当鼠标移动到复选框图标上*/
	function onmouseoverImg(src){
		var checkeds=$(src).attr("checkeds"); 
		if(checkeds=='0'){
			$(src).attr("src","${app}/images/overunchecked.png"); 
		}else if (checkeds=='1'){
			$(src).attr("src","${app}/images/outunchecked.png"); 
		}
	
	}
	/***当鼠标离开复选框图标上*/
	function onmouseoutImg(src){
		var checkeds=$(src).attr("checkeds"); 
		if(checkeds=='0'){
			$(src).attr("src","${app}/images/unchecked.png"); 
		}else if (checkeds=='1'){
			$(src).attr("src","${app}/images/checked.png"); 
		}
	}
	
	/**把选择的值赋到隐藏域中**/
	function insertRoleValue(){
		/****************获取角色******************/
		var roleids='';//所选择的角色
		//循环获取角色图片属性checkeds（1 已选择  1 已经选择） ;roleid(角色编号);rolename(角色名称);
		$("img[name='role_chaecked']").each(function(i){
			var checkeds =$(this).attr("checkeds");
			if(checkeds=='1') {
				roleids+=$(this).attr("roleid")+",";
			}
		});
		//把最后一个逗号截取掉
		roleids = roleids.substr(0,roleids.lastIndexOf(','));
		$('#role_ids').val(roleids);
	}
	
	</script>
	
	<!-- ZTree 异步请求加载树 -->
	<script type="text/javascript">
		var setting = {
			async: {
				enable: false,
				url: getUrl
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
			editModulePermit(treeNode.id);
		}
		
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
			var zTree = $.fn.zTree.getZTreeObj("moduleTree");
			zTree.updateNode(treeNode);
		}
		function onAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
			var zTree = $.fn.zTree.getZTreeObj("moduleTree");
			alert("异步获取数据出现异常。");
			treeNode.icon = "";
			zTree.updateNode(treeNode);
		}
		
		var zTreeMenuTree;
		$(document).ready(function(){
			//初始化组织树
			var url1 = '${app}/permit/loadModulePermit.do';
			new MyJqueryAjax(url1, null, showTree).request();
		});
		
		function showTree(req){
			var orgTreeJson = eval(req);
			if(orgTreeJson.length==0){
				alert('暂无数据！');
				window.close();
				return false;
			}
			zTreeMenuTree = $.fn.zTree.init($("#moduleTree"), setting, orgTreeJson);
			zTreeMenuTree.expandNode(zTreeMenuTree.getNodeByParam("id", "0_M"));
		}
	</script>
</html>