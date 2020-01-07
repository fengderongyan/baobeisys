<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/inc/includeBase.jsp" %>
	<%@ include file="/inc/includeEasyUi.jsp" %>
	<link rel="stylesheet" href="${app}/css/ztree/zTreeStyle/zTreeStyle.css" type="text/css">
	<script type="text/javascript" src="${app}/js/ztree/jquery.ztree.core-3.5.js"></script>
	<script type="text/javascript" src="${app}/js/ztree/jquery.ztree.excheck-3.5.js"></script>
	
	<title>多用户信息新增</title>
	<style type="">
	.hintInfo{padding:8px 35px 8px 10px;text-shadow:none;-webkit-box-shadow:0 2px 4px rgba(0,0,0,0.2);-moz-box-shadow:0 2px 4px rgba(0,0,0,0.2);box-shadow:0 2px 4px rgba(0,0,0,0.2);background-color:#f9edbe;border:1px solid #f0c36d;-webkit-border-radius:2px;-moz-border-radius:2px;border-radius:2px;color:#333}
	
	</style>
</head>
<body>
	<form id="form2" name="form2" action="${app}/sys/usercfg/saveMoreUser.do" method="post"  >
		<sgy:csrfToken />
		<div id="queryPanel" class="queryPanel">
			<div id="queryPanel_title" class="queryPanel_title">
				<div class="queryPanel_title_collapse"></div>&nbsp;${title}
			</div>
		    <div id="queryPanel_content" class="queryPanel_content">
		    	<center>
		    	<div>
					<input type="hidden" id="pageSize" name="pageSize" value="" />
					<sgy:button id="schbtn" cssClass="ovalbutton" onclick="addInfo();return false;">新增一条</sgy:button>
					<br/>&nbsp;
				</div>
				</center>
				<table class="form_table" style="width: 99%;"
					align="center" id="dcTabs" name="dcTabs" >
					<tr height="25">
						<td class="outDetail" >
							<font color="red">*</font>&nbsp;工号 
						</td>
						<td class="outDetail" >
							<font color="red">*</font>&nbsp;姓名 
						</td>
						<td class="outDetail" >
							<font color="red">*</font>&nbsp;手机 
						</td>
						<td class="outDetail" >
							<font color="red">*</font>&nbsp;用户组织  
						</td>
						<td class="outDetail" >
							<font color="red">*</font>&nbsp;用户角色  
						</td>
						<td class="outDetail" >
							操作
						</td>
					</tr>
					<tr id="copyTr" name="copyTr">
						<td class="outDetail2" >
							<input type="text" id="operator_id" name="operator_id" dataType="Require" msg="工号不能为空！" placeholder="请输入工号！"  />
						</td>
						<td class="outDetail" >
							<input type="text" id="name" name="name" size="10" dataType="Require" msg="姓名不能为空！" placeholder="请输入姓名！"  />
						</td>
						<td class="outDetail" >
							<input type="text" id="mobile" name="mobile" maxlength="11" size="14" dataType="Mobile" msg="请正确填写手机号码！" placeholder="请填写手机号码！" />
						</td>
						<td class="outDetail" >
							<input type="text" id="org_id" name="org_id" dataType="Require" msg="请填写用户组织编号！" placeholder="请填写用户组织编号！"
								  />
						</td>
						<td class="outDetail" >
							<input type="text" id="role_id" name="role_id" dataType="Require" msg="请填写用户角色名称！" placeholder="请填写角色名称！"
								   /> 
						</td>
						<td class="outDetail" >
							<a style="color: red; cursor: hand; text-decoration: none; text-decoration: underline;"
								onclick="delRow('copyTr');return false;"> 
								<b style="font-size: 12px; font-weight: bold;">删 除</b> 
							</a>
						</td>
					</tr>
						
						
				</table>	
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
	//保存方法
	function sav(src){
		src.disabled=true;
		if(!Validator.Validate("form2")){
			src.disabled=false;
			return false;
		}
		form2.submit();
	}
	
	//动态添加
	function addInfo(){
		$("tr[id='copyTr']:last").after($("tr[id='copyTr']:first").clone());
		$("tr[id='copyTr']:last").show();
		
		$("tr[id='copyTr']:last").find("#operator_id").attr("dataType","Require");
		$("tr[id='copyTr']:last").find("#operator_id").attr("msg","工号不能为空！");
		$("tr[id='copyTr']:last").find("#operator_id").val('');
		
		$("tr[id='copyTr']:last").find("#name").attr("dataType","Require");
		$("tr[id='copyTr']:last").find("#name").attr("msg","姓名不能为空！");
		$("tr[id='copyTr']:last").find("#name").val('');
		
		$("tr[id='copyTr']:last").find("#mobile").attr("dataType","Mobile");
		$("tr[id='copyTr']:last").find("#mobile").attr("msg","请正确填写手机号码！");
		$("tr[id='copyTr']:last").find("#mobile").val('');
		
		$("tr[id='copyTr']:last").find("#org_id").attr("dataType","Require");
		$("tr[id='copyTr']:last").find("#org_id").attr("msg","请选择用户组织！");
		$("tr[id='copyTr']:last").find("#org_id").val('');
		
		$("tr[id='copyTr']:last").find("#role_id").attr("dataType","Require");
		$("tr[id='copyTr']:last").find("#role_id").attr("msg","请选择用户角色！");
		$("tr[id='copyTr']:last").find("#role_id").val('');
	
	}
	
	//动态删除
	function delRow(trId){
		var tr = document.getElementsByName(trId);
		if(tr.length == 1){
			alert('最后一条不能删除！');
			return false;
		}
		//var tb = document.getElementById(trId);  //传入删除按钮所在tdID 
		//火狐不兼容 event.srcElementevent 处理，且FF不支持event直接使用
		var event = window.event || arguments.callee.caller.arguments[0];
		var src = event.srcElement || event.target;;
		var row = getRow(src);
		//var tab = $(row).parent().parent();
		$(row).remove();
	}
	
</script>
</html>
