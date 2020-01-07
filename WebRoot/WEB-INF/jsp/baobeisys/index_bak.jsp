<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<%@ include file="/inc/includeBase.jsp" %>
		<%@ include file="/inc/includeEasyUi.jsp"%>
		<link rel="stylesheet" type="text/css" href="${app}/css/index/tabs.css" />
		<link rel="stylesheet" type="text/css" href="${app}/css/index/index.css" />
		<link rel="stylesheet" href="${app}/css/ztree/zTreeStyle/zTreeStyle.css" type="text/css" />
		<script type="text/javascript" src="${app}/js/ztree/jquery.ztree.core-3.5.js"></script>
		<script type="text/javascript" src="${app}/js/ztree/jquery.ztree.excheck-3.5.js"></script>
		
		<title>后台管理</title>
		<style type="text/css">
			html, body {
					min-width: 1240px;
			}
			
			.homeIcon {
				background-repeat: no-repeat;
				background-image: url("${app}/images/home.gif");
			}
			
			.tabIcon {
				background-repeat: no-repeat;
				background-image: url("${app}/images/tab.png");
			}
		</style>
		<script type="text/javascript">
		function addTab(id, tabName, url, isClosed, iconCls){
			tabName = '<font color=black>'+tabName+'<font>';
			if(isClosed == null) isClosed=true;
     		if(iconCls == null) iconCls='';
     		var tab = $('#tabs').tabs('getTab', tabName);
     		if(tab==null||(tab.panel('options').id!=id)){//新增tab
    			$('#tabs').tabs('add',{ 
					id:id,
				    title:tabName,   
				    content:'<iframe id="frm_'+id+'" name="frm_'+id+'" src="'+url+'" width="100%" height="100%" frameborder="0" scrolling="auto"></iframe>',   
				    iconCls:iconCls,
				    closable:isClosed
				});
     		} else {//更新tab
     			$('#tabs').tabs('update',{   
				    tab: tab,
					options: {
						content:'<iframe id="frm_'+id+'" name="frm_'+id+'" src="'+url+'" width="100%" height="100%" frameborder="0" scrolling="auto"></iframe>'
					}
				}); 
				var tabIndex = $('#tabs').tabs('getTabIndex', tab);//获取tab的索引
				$('#tabs').tabs('select', tabIndex);//选中更新的tab
     		}
     		$('a[class=tabs-inner]').each(function(){
				$(this).removeAttr('href');
			});
			$('a[class=tabs-close]').each(function(){
				$(this).removeAttr('href');
			});
		}
		$(document).ready(function(){
			addTab('-1', '<font color=blue>首页</font>', '${app}/todo/frame.do', false, 'homeIcon');
		});
		
		//添加菜单点击日志
		function addNodeLog(module_id, module_name, op_type_id, op_type_name){
			var url = '${app}/commondata/addNodeLog.do?module_id='+encodeURI(module_id)+'&module_name='+encodeURI(module_name)
				+'&op_type_id='+op_type_id+'&op_type_name='+encodeURI(op_type_name);
			new MyJqueryAjax2(url).request();
		}
	</script>
	</head>
	<body scroll="auto">
		<div style="height: 100%; width: 100%; position: relative;">
			<div class="top">
				<div class="ttop">
					<div style="width:200px;position: absolute;right:0px;top:5px;">
						<table>
							<tr>
								<td style="width: 70px;color: white;font-weight: bold;height: 70px;cursor: pointer;" onclick="personSetting(this);">
									修改密码
								</td>
								<td style="width: 70px;color: white;font-weight: bold;height: 70px;cursor: pointer;" onclick="logOut();">
									退出
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
			<div class="main">
				<div id="cc" class="easyui-layout" style="width: 100%; height: 100%;border: 0px solid red;">
					<div data-options="region:'west',title:'&nbsp;&nbsp;系统菜单',split:false" style="width: 270px;border: 0px;padding: 5px; ">
						<ul id="moduleTree" class="ztree" style="bottom:34px;"><img style="vertical-align: middle;" src="${app}/images/wait.gif" />&nbsp;正在加载菜单，请稍候......</ul>
					</div>
					<div id="menu_1" data-options="region:'center'" style="background-color: #FFFFFF;border-top: 0px">
						<div id="tabs" class="easyui-tabs" data-options="fit:true,border:false">
						</div>
					</div>
				</div>
			</div>
			<div class="bottom">
				<p>
					操作员：${user.name}
				</p>
				<p style="padding-right: 20px;">
					登录时间：${loginDate}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					归属组织：${user.orgName }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					用户角色：${user.roleNames }
				</p>
			</div>
		</div>
		<div id="mm" class="easyui-menu" style="width:150px;">  
	        <div id="mm-tabcloseall">关闭全部</div>  
	        <div id="mm-tabcloseother">关闭其他</div>  
	        <div class="menu-sep"></div>  
	        <div id="mm-tabcloseright">关闭右侧标签</div>  
	        <div id="mm-tabcloseleft">关闭左侧标签</div>  
	    </div>  
	</body>
	<script type="text/javascript">
		//初始化组织树
		$(document).ready(function(){
			setTimeout(loadOrgTree, 50);
			//tab右键事件
			bindTabEvent();    
			bindTabMenuEvent();  
		});
	
		//弹出消息框
		function showMessager(){
			$.messager.show({
				title:' <font style="color: #08315A;">消息提醒</font>',
				width: 350,
				height: 230,
				msg:'<div id="messagerDiv" style="font-size:14px;font-weight:bold;"></div>',
				timeout:5000,//如果定义为 0，除非用户关闭，消息窗口将不会关闭。如果定义为非 0 值，消息窗口将在超时后自动关闭
				showType:'slide'//可用的值是： null、slide、fade、show。默认是 slide
			});
			
			url = '${app}/todo/ajaxLoadMessager.do';
			res = new MyJqueryAjax(url).request();
			$('#messagerDiv').html(res);
		}
		
		//获取试用购买
		function getTrialBuy(){
			addTab('-2', '试用购买', '${app}/company/trialbuy/trialBuyFrame.do', true, 'tabIcon');
		}
		
		//退出
		function logOut(){
			if(confirm('您确定要退出系统吗?')){
				var url = '${app}/commondata/recordLogout.do?';
  	  			var res = new MyJqueryAjax(url, null).request();
	  			window.location.href='${app}/login.jsp';
			}
		}
		
		//个人设置
		function personSetting(src){
			addTab('修改密码', '修改密码', '${app}/sys/password/changePassWordFrame.do', true, 'tabIcon');
		}
		
		//系统帮助中心
		function helpCenter(){
			
		}
		
		//打开菜单
		function openModule(module_id,module_name,module_url,moduleFlag,openFlag){
			var new_url = '${app}/'+module_url;
			if(moduleFlag=='2'&&module_url!=''){//如果是具体菜单，并且module_url不为空
				if(openFlag=='1'){//tab打开
					// 重组请求的URL
					window.addTab(module_id, module_name, new_url, true, 'tabIcon');
				}else if(openFlag=='2'){//本浏览器打开
					window.open().location = new_url;
				}else if(openFlag=='3'){//全屏打开
					if(window.screen){
						var height = window.screen.availHeight - 41;
						var width = window.screen.availWidth - 18;
					}
					
					window.open(new_url, module_name, 'height='+height+', width='+width+
					', top=0, left=0, toolbar=no, menubar=no, scrollbars=no, '+
					' resizable=no, location=no, status=no, directories=no');
				}
				// 记录菜单点击日志
				addNodeLog(module_id, module_name, 'menu', '点击菜单');
			}
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
			openModule(treeNode.id,treeNode.name,treeNode.moduleUrl,treeNode.moduleFlag,treeNode.openFlag);
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
		
		//easyUi组件渲染完成后调用
		$.parser.onComplete = function(){
			
		}
		
		//加载组织树
		function loadOrgTree(){	
			var url = '${app}/sys/moduletree/loadModuleList.do';
			new MyJqueryAjax(url, null, showOrgTree).request();
		}
		
		function showOrgTree(req){
			var orgTreeJson = eval(req);
			if(orgTreeJson.length==0){
				alert('暂无符合条件的菜单！');
				$('#moduleTree').html('');
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
		
		/********************tab右键事件**************************/
	    //绑定tab的双击事件、右键事件  
	    function bindTabEvent(){
	        $(".tabs-inner").on('dblclick',function(){  //.live
	            var subtitle = $(this).children("span").text();  
	            if($(this).next().is('.tabs-close')){  
	                $('#tabs').tabs('close',subtitle);  
	            }  
	        });  
	        $(".tabs-inner").on('contextmenu',function(e){  //.live
	            $('#mm').menu('show', {  
	                left: e.pageX,  
	                top: e.pageY  
	         });  
	         var subtitle =$(this).children("span").text();  
	         $('#mm').data("currtab",subtitle);  
	         return false;  
	        });  
	     }  
	     
	     //绑定tab右键菜单事件  
		function bindTabMenuEvent() {
		    //全部关闭  
		    $('#mm-tabcloseall').click(function() {  
		        $('.tabs-inner span').each(function(i, n) {  
		            if ($(this).parent().next().is('.tabs-close')) {  
		                var t = $(n).text();  
		                $('#tabs').tabs('close', t);  
		            }  
		        });  
		    });  
		    //关闭除当前之外的TAB  
		    $('#mm-tabcloseother').click(function() {  
		        var currtab_title = $('#mm').data("currtab");  
		        $('.tabs-inner span').each(function(i, n) {  
		            if ($(this).parent().next().is('.tabs-close')) {  
		                var t = $(n).text();  
		                if (t != currtab_title)  
		                    $('#tabs').tabs('close', t);  
		            }  
		        });  
		    });  
		    //关闭当前右侧的TAB  
		    $('#mm-tabcloseright').click(function() {  
		        var nextall = $('.tabs-selected').nextAll();  
		        if (nextall.length == 0) {  
		            alert('已经是最后一个了');  
		            return false;  
		        }  
		        nextall.each(function(i, n) {  
		            if ($('a.tabs-close', $(n)).length > 0) {  
		                var t = $('a:eq(0) span', $(n)).text();  
		                $('#tabs').tabs('close', t);  
		            }  
		        });  
		        return false;  
		    });  
		    //关闭当前左侧的TAB  
		    $('#mm-tabcloseleft').click(function() {  
		        var prevall = $('.tabs-selected').prevAll();  
		        if (prevall.length == 1) {  
		            alert('已经是第一个了');  
		            return false;  
		        }  
		        prevall.each(function(i, n) {  
		            if ($('a.tabs-close', $(n)).length > 0) {  
		                var t = $('a:eq(0) span', $(n)).text();  
		                $('#tabs').tabs('close', t);  
		            }  
		        });  
		        return false;  
		    });  
		} 
	</script>
</html>