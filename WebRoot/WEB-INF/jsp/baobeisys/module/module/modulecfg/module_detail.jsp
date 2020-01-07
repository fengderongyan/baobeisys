<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>前台模块权限配置</title>
		<%@ include file="/inc/includeBase.jsp" %>
		<%@ include file="/inc/includeEasyUi.jsp" %>
		<style type="text/css">
			#tblItem th, #tblItem td {
				border: 1px solid #8DB2E3;
				font-size: 12px;
			}
			.add {
				background: url("${app}/images/add.png") -5px -12px;
				width:30px;
				height:19px;
				cursor:pointer;
			}
		</style>
	</head>
	<body class="sgy">
		<form id="form1" name="form1" method="post" action="${app}/module/saveOrUpdate.do">
		<sgy:csrfToken />
		<div id="queryPanel" class="queryPanel">
			<div id="queryPanel_title" class="queryPanel_title">
				<div class="queryPanel_title_collapse"></div>&nbsp;模块权限配置
			</div>
		    <div id="queryPanel_content" class="queryPanel_content">
		    	<div style="width: 100%;height: 530px;margin:0 auto;text-align: center;" >
					<div class="easyui-tabs" data-options="fit:true,border:true" id="module_tabs">
						<div title="菜单基本信息配置" style="padding:5px;overflow-y:auto;">
							<table class="form_table" id="td1" cellspacing="0" cellpadding="0" style="width:100%">
								<tr>
									<td class="outDetail" width="15%">
										<font color="red">*</font>&nbsp;模块名称：
									</td>
									<td class="outDetail2" width="35%">
										<input type="text" name="module_name" id="module_name" style="width: 90%;" maxlength="50"
											value="${map.module_name }" require="true" dataType="Require" msg="请输入模块名称" />
									</td>
									<td class="outDetail" width="15%">
										<font color="red">*</font>&nbsp;上级目录：
									</td>
									<td class="outDetail2" width="35%">
										<input type="text" name="superior_name" id="superior_name" value="${map.superior_name}"
											readonly="readonly" style="width: 80%;" require="true" dataType="Require" msg="请选择上级目录" />
										<img src="${app}/images/search.gif" alt="选择上级目录" style="cursor: pointer;width: 22px;height: 18;margin-left: 0px;margin-top: 3px; auto; vertical-align: middle;" onclick="selSuperModule();" />
										<input type="hidden" name="module_level" id="module_level" value="${map.module_level }" />
										<input type="hidden" name="superior_id" id="superior_id" value="${map.superior_id }" />
									</td>
								</tr>
								<tr  >
									<td class="outDetail">
										模块URL：
									</td>
									<td class="outDetail2" colspan="3">
										<input type="text" id="module_url" name="module_url" style="width: 92%;" 
											value="${map.module_url }" />
									</td>
								</tr>
								<tr>
									<td width="15%" class="outDetail">
										<font color="red">*</font>&nbsp;模块代码：
									</td>
									<td class="outDetail2" >
										<input type="hidden" value="sys" name="module_code_mid" id="module_code_mid"  >
										<input type="hidden" name="prefix_code" id="prefix_code" style="width: 30%;" maxlength="20"
											value="${map.prefixCode}" readonly="readonly"  />
										<label id="module_prefix_code">${map.prefixCode}</label>.${empty suffix_code?map.suffixCode:suffix_code}
										<input type="hidden" value="${empty suffix_code?map.suffixCode:suffix_code}" name="suffix_code" id="suffix_code">
										<input type="hidden" value="" name="module_code" id="module_code"  >	
										
									</td>
									<td width="15%" class="outDetail">
										模块顺序：
									</td>
									<td class="outDetail2">
										<input type="text" name="module_order" style="width: 80%;" value="${map.module_order}" 
											require="false" dataType="Number" msg="模块顺序只能输入数字" />
									</td>
								</tr>
								<tr>
									<td width="15%" class="outDetail">
										<font color="red">*</font>&nbsp;菜单类型：
									</td>
									<td class="outDetail2" colspan="3">
										<sgy:select name="module_os" id="module_os" value="${map.module_os}"
											 onchange="changeModuleOs()" require="true" dataType="Require" msg="请选择菜单类型！" >
											<sgy:option value="">请选择...</sgy:option>
											<sgy:option value="1">PC菜单</sgy:option>
											<sgy:option value="2">客户端菜单</sgy:option>
										</sgy:select>
									</td>
								</tr>
							</table>
							<br />
						
						<div title="模块权限配置" style="padding:5px;overflow-y:auto;">
							<table id="tblItem" cellspacing="0" class="sgy"
								style="width: 100%; border-collapse: collapse; border: 1px solid #8DB2E3;">
								<tr style="background-color: #E8EFF6; height: 24px;">
									<th style="width: 35px;">序号</th>
									<th style="width: 80px;">URL类型</th>
									<th style="width: 200px;">权限名称</th>
									<th style="width: 100px;">权限编码</th>
									<th>权限URL</th>
									<th style="width: 80px;">操作级别</th>
									<th style="width: 40px;">
										<div title="增加一行" class="add" id="add"></div>
									</th>
								</tr>
							</table>
						</div>
						
					</div>
				</div>
			</div>
		</div>
		<p align="center">
			<br/>
			<input type="hidden" id="method" name="method" value="${param.action}"/>
			<input type="hidden" id="module_id" name="module_id" value="${empty suffix_code?param.module_id:suffix_code}"/>
			<input type="hidden" id="module_folder_flag" name="module_folder_flag" value="${map.module_folder_flag}"/>
			<sgy:button cssClass="ovalbutton" onclick="sav(this);return false;">保 存</sgy:button>
			<input type="hidden" name="hidPermitId" id="hidPermitId" value="" />
			<c:if test="${param.action == 'update'}">
				&nbsp;&nbsp;
				<sgy:button id="btn_open" cssClass="ovalbutton" onclick="del();return false;">删 除</sgy:button>
			</c:if>
			&nbsp;&nbsp;
			<sgy:button cssClass="ovalbutton" onclick="window.close();return false;">关 闭</sgy:button>
		</p>
		<br/>
		</form>
	</body>
<script language="javascript">

	//选择上级目录节点
	function selSuperModule(){
		var module_id = $('#superior_id').val();
		
		var url = "${app}/module/getSuperiorTree.do?module_id="+module_id;
		var strs = MyWindow.OpenModalDialog(url,"","dialogWidth:320px;dialogHeight:420px;edge:Raised;center:Yes;help:No;resizable:no;status:No;dialogHide:no;scroll:no;");
		if(strs == 'undefined' || strs ==null || strs == ''){
			return false;
		}
		
		var arr = strs.split("^");
		var sel_id = arr[0];
		var sel_name = arr[1];
		var sel_level = arr[2];
		$('#superior_id').val(sel_id);
 		$('#superior_name').val(sel_name);
		$('#module_level').val(sel_level);
		
		if(sel_id!='0'){
			$('#module_prefix_code').css('display','');
			var url = '${app}/module/getModuleDefaultCodePrefix.do';
			var params = 'module_id='+sel_id;
			var prefixCode = new MyJqueryAjax(url, params).request();
			if(sel_id==1213){
				prefixCode='sys.1213';
			}
			$('#prefix_code').val(prefixCode);//隐藏值
			$('#module_prefix_code').html(prefixCode);//显示前缀
			setLblShow('lbl_prefix',$('#prefix_code').val(),'1');
			setLblShow('lbl_middle',$('#suffix_code').val(),'1');
			setLblShow('lbl_suffix','','0');
			
			setPermitCode('permit_code_prefix',$('#prefix_code').val(),'1');
			setPermitCode('permit_code_middle',$('#suffix_code').val(),'1');
			setPermitCode('permit_code_suffix','','0');
		}else{
			$('#module_prefix_code').html('sys');
		}
		
		//MyWindow.OpenCenterWindowScroll('${app}/module/getSuperiorTree.do?superior_id='+module_id,'selOrg',420, 320);
	}
	
	//控制全选或者全不选
	function operAll(tag,src){
		$('input[name='+tag+']').each(function(i){
				$(this).attr('checked',src.checked);
		});
	}
	function setLblShow(element,code,flag){
		var lblcode = document.getElementsByTagName('label');
		$('label[id='+element+']').each(function(i){
			if(flag==1){
				code = code;
			}else{
				code = (i+1);
			}
			$(this).html(code);
			
		});
	}
	
	setLblShow('lbl_prefix',$('#prefix_code').val(),'1');
	setLblShow('lbl_middle',$('#suffix_code').val(),'1');
	setLblShow('lbl_suffix','1','0');
	
	setPermitCode('permit_code_prefix',$('#prefix_code').val(),'1');
	setPermitCode('permit_code_middle',$('#suffix_code').val(),'1');
	setPermitCode('permit_code_suffix','','0');
	
	function setPermitCode(element,code,flag){
		var permit_codes = document.getElementsByName(element);
		for(var i=0; i<permit_codes.length; i++){
			if(flag==1){
				code = code;
			}else{
				code = (i+1);
			}
			document.getElementsByName(element)[i].value=code;
		}
	}
	
	function setPermitCode3(code){
		var permit_code_suffix = document.getElementsByName('permit_code_suffix');
		for(var i=0; i<permit_code_suffix.length; i++){
			document.getElementsByName('permit_code_suffix')[i].value=(i+1);
		}
	}
	
	//删除菜单
	function del() {
		if(confirm('确定删除？')) {
			var module_id = '${param.module_id}';
			var params = 'action=delete&module_id='+module_id;
			var url = '${app}/module/deleteModule.do';
			var ajaxRes = new MyJqueryAjax(url, params).request();
			if(ajaxRes == 0) {
				alert('操作失败，请重试！');
				return false;
			}else if(ajaxRes == -2){
				alert('存在有效子目录，不能删除！');
				return false;
			}else if(ajaxRes == 1){
				alert('操作成功！');
			}
			window.close();
			window.opener.document.location.reload();
		}
	}
	
	//保存菜单
	function sav(src) {
		disable(src);
		if(!Validator.Validate('form1')){
			enable(src);
			return false;
		}
		
		var isCodeExist = false;
		var url = '${app}/module/checkModuleCodeExists.do';
		var module_id = '${param.module_id}';
		var module_code = $('#prefix_code').val()+'.'+$('#suffix_code').val();
		var superior_id = $('#superior_id').val();
		var module_code_mid = $('#module_code_mid').val()+'.'+$('#suffix_code').val();
		
		if(superior_id!='0'){
			$('#module_code').val(module_code);
		}else{
			if(module_code_mid==''){
				enable(src);
				alert('请填写模块代码！');
				$('#module_code_mid').focus();
				return;
			}
			$('#module_code').val(module_code_mid);
		}
		
		
		
		var params = 'module_code=' + module_code+'&module_id='+module_id;
		var ajaxRes = new MyJqueryAjax(url, params).request();
		if(ajaxRes != 0) {
			isCodeExist=true;
		}
		if(isCodeExist==true) {
			alert('模块编码已存在！');
			enable(src);
			$('#module_code').focus();
			return false; 
		}
		
		var action = '${param.action}';
		if(action=='update'){
			var module_id = $('#module_id').val();
			var superior_id  = $('#superior_id').val();
			if(superior_id==''){
				alert('请重新选择上级目录！');
				enable(src);
				$('#superior_id').focus();
				return false;
			}else if(superior_id==module_id){
				alert('不能把当前目录作为自己的上级目录，请重新选择上级目录！');
				enable(src);
				$('#superior_id').focus();
				return false;
			}
			var module_folder_flag = $('#module_folder_flag').val();
			if($('#module_url').val()!='' && module_folder_flag=='1'){
				var isSubExist = false;
				var url = '${app}/module/checkModuleSubExists.do';
				var module_id = '${param.module_id}';
				var params = 'module_id='+module_id;
				var ajaxRes = new MyJqueryAjax(url, params).request();
				if(ajaxRes != 0) {
					isSubExist=true;
				}
				if(isSubExist==true) {
					alert('该模块已经存在子目录，不能更改成具体模块！');
					enable(src);
					$('#module_url').focus();
					return false; 
				}
			}
		}
		$('#form1').submit();
	}
	
	$(document).ready(function(){
		// 动态增加行
		$("#add").click(function(){
			var rowNum = $("tr[id='tr_id']").length + 1 ;  
			$("#tr_id").clone(true).insertAfter($("tr[id='tr_id']:last"));
			$("tr[id='tr_id']:last").find("input[id='permit_name']").val(""); // permit_name
			$("tr[id='tr_id']:last").find("input[id='permit_id']").val(""); // permit_id
			$("tr[id='tr_id']:last").find("input[id='permit_code_prefix']").val(""); // permit_code_prefix
			$("tr[id='tr_id']:last").find("input[id='permit_code_middle']").val(""); // permit_code_middle
			$("tr[id='tr_id']:last").find("input[id='permit_code_suffix']").val(""); // permit_code_suffix
			$("tr[id='tr_id']:last").find("input[id='permit_url']").val(""); // permit_url
			$("tr[id='tr_id']:last").find("select[id='operate_level']").val("0"); // operate_level
			$("td[id='xh_td']:last").text(rowNum);
			setPermitCode('permit_code_prefix',$('#prefix_code').val(),'1');
			setPermitCode('permit_code_middle',$('#suffix_code').val(),'1');
			setPermitCode('permit_code_suffix','1','0');
			
			setLblShow('lbl_prefix',$('#prefix_code').val(),'1');
			setLblShow('lbl_middle',$('#suffix_code').val(),'1');
			setLblShow('lbl_suffix','1','0');
		});
		
	});
	
	//删除行
	function deleteRow(permit_id,id, e,src) {
		try {
			var event = event ? event: e;
			var Elm = event.srcElement ? event.srcElement : e.target;
			while(Elm && Elm.tagName != "TR") {
				Elm = Elm.parentElement;
			}
			if(Elm.parentElement.rows.length <= 1) {
				alert("已是最后一条记录，不能删除！");
				return;
			}
			
			delRow(id, e);
			rebuildRowNum(id);
			setPermitCode('permit_code_suffix','1','0');
			setLblShow('lbl_suffix','1','0');
			
			if(permit_id!=''){//把删除的权限编号记录下来，传到后台删除
				var hidPermitId = $('#hidPermitId').val();
				if(hidPermitId!=''){
					hidPermitId = hidPermitId+','+permit_id;
				}else{
					hidPermitId = permit_id;
				}
				$('#hidPermitId').val(hidPermitId);
			}
		} catch(e) {
			alert("Err 5001:\r\n" + e);
		}
	}
	
	//展示选择图标
	function showModuleIcon(){
		var module_icon = $("#module_icon").val();
		//模式对话框
		var url = '${app}/module/showModuleIconList.do?module_icon=' + module_icon;
		var res = MyWindow.OpenModalDialog(url,"","dialogWidth:500px;dialogHeight:400px;edge:Raised;center:Yes;help:No;resizable:no;status:No;dialogHide:no;scroll:no;");
		if(res == 'undefined' || res == null || res == '')
		{
			return false;
		}
		$("#module_icon").val(res);
	}
	
	$(document).ready(function(){
		// 动态增加行
		$("#addParam").click(function(){
			var rowNum = $("tr[id='tr_param_id']").length + 1 ;  
			$("#tr_param_id").clone(true).insertAfter($("tr[id='tr_param_id']:last"));
			$("tr[id='tr_param_id']:last").find("input[id='param_name']").val(""); // param_name
			$("tr[id='tr_param_id']:last").find("input[id='param_id']").val(""); // param_id
			$("tr[id='tr_param_id']:last").find("input[id='param_value']").val(""); // param_value
			$("td[id='xh_param_td']:last").text(rowNum);
		});
		
	});
	
	//删除一行
	function deleteParamRow(id, e,src) {
		try {
			var event = event ? event: e;
			var Elm = event.srcElement ? event.srcElement : e.target;
			while(Elm && Elm.tagName != "TR") {
				Elm = Elm.parentElement;
			}
			if(Elm.parentElement.rows.length <= 1) {
				alert("已是最后一条记录，不能删除！");
				return;
			}
			
			delRow(id, e);
			rebuildRowNum(id);
			
		} catch(e) {
			alert("Err 5001:\r\n" + e);
		}
	}
	
	//改变模块类型，改变页面
	
	function changeModuleOs(){
		var module_os = $('#module_os').val();//模块类型：1:PC菜单   2:客户端菜单
		if(module_os=='1'){
			$('#pc_tbody').css('display','');
			$('#client_tbody').css('display','none');
			$('#client_param_tbody').css('display','none');
			var ctab = $('#module_tabs').tabs('getTab', '模块权限配置').panel('options').tab;
			ctab.show();//显示卡片
		}else if(module_os=='2'){
			$('#pc_tbody').css('display','none');
			$('#client_tbody').css('display','');
			$('#client_param_tbody').css('display','');
			var ctab = $('#module_tabs').tabs('getTab', '模块权限配置').panel('options').tab;
            ctab.hide();//隐藏卡片
            
		}
	}
	
	$(document).ready(function(){
			changeModuleOs();
			changeHasEjcd();
	});
	
	function changeHasEjcd(){
		if($('#has_ejkhdcd').val()=='1'){
			$('#has_ejkhdcd_td').attr('colspan','1');
			$('#open_ejcd_type1').css('display','');
			$('#open_ejcd_type2').css('display','');
		} else {
			$('#has_ejkhdcd_td').attr('colspan','3');
			$('#open_ejcd_type1').css('display','none');
			$('#open_ejcd_type2').css('display','none');
		}
	}
	
</script>


</html>