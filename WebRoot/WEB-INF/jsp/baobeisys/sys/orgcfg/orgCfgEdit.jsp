<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/inc/includeBase.jsp" %>
	<c:choose>
		<c:when test="${param.method=='edit'}">
			<c:set var="title" value="修改组织信息"></c:set>
		</c:when>
		<c:when test="${param.method=='create'}">
			<c:set var="title" value="新增组织信息"></c:set>
		</c:when>
		<c:when test="${param.method=='show'}">
			<c:set var="title" value="组织详细信息"></c:set>
		</c:when>
	</c:choose>
	<title>${title}</title>
</head>
<body>
	<form id="form1" name="form1" action="${app}/sys/orgcfg/saveOrUpdateOrg.do" method="post">
		<div id="queryPanel" class="queryPanel">
			<div id="queryPanel_title" class="queryPanel_title">
				<div class="queryPanel_title_collapse"></div>&nbsp;${title}
			</div>
		    <div id="queryPanel_content" class="queryPanel_content">
				<table class="detail_table" cellspacing="0" cellpadding="0" style="width: 100%;">
					<tr height="20">
						<td class="outDetail">
							<font color="red">*</font>&nbsp;上级组织：
						</td>
						<td class="outDetail2" colspan="3">
							<c:if test="${param.method != 'show'}">
								<input type="text" name="superior_name" id="superior_name" style="width: 300px;cursor:pointer;" 
									required="true" dataType="Require" msg="请选择上级组织！"
									readonly="readonly" value="${map.superior_name}" onclick="openOrg();return false;"/>
								<img src="${app}/images/cleanup.png" alt="" style="cursor: pointer;margin-left: 0px;vertical-align: middle;" 
										 title="点击清空上级组织！" onclick="restOrg();" height="20"/>
								<input type="hidden" name="superior_id" id="superior_id" value="${map.superior_id }" />
							</c:if>
							<c:if test="${param.method == 'show'}">
								${map.superior_name}
							</c:if>
						</td>
					</tr>
					<tr>
						<td width="15%" class="outDetail">
							<font color="red">*</font>&nbsp;组织编号：
						</td>
						<td class="outDetail2" colspan="3">
							<c:if test="${param.method != 'show'}">
								<label id = "org_label">${map.org_id}</label>
								<input type="hidden" id="org_id" name="org_id" value="${map.org_id}" style="width: 160px;"
									required="true" dataType="Require" msg="组织编号不能为空！" />
								<input type="hidden" id="old_org_id" name="old_org_id" value="${map.org_id}"  />
							</c:if>
							<c:if test="${param.method == 'show'}">
								${map.org_id}
							</c:if>	
						</td>
					</tr>
					<tr height="20">
						<td class="outDetail">
							<font color="red">*</font>&nbsp;组织名称：
						</td>
						<td class="outDetail2" colspan="3">
							<c:if test="${param.method != 'show'}">
								<input type="text" id="org_name" name="org_name" value="${map.org_name}" style="width: 90%"
									required="true" dataType="Require" msg="组织名称不能为空！" />
							</c:if>
							<c:if test="${param.method == 'show'}">
								${map.org_name}
							</c:if>	
						</td>
					</tr>
				</table>
		    </div>
		</div>
		<br />
		<p align="center">
			<input type="hidden" id="org_level" name="org_level" value="${map.org_lev}"  />
			<input type="hidden" id="county_id" name="county_id" value="${map.county_id}"  />
			<input type="hidden" name="method" id="method" value="${param.method}" />
			<c:if test="${param.method!='show'}">
				<sgy:button cssClass="ovalbutton" onclick="sav(this);return false;">保 存</sgy:button>
			</c:if>&nbsp;
			<sgy:button cssClass="ovalbutton" onclick="window.close();">关 闭</sgy:button>
		</p>
	</form>
</body>
<script type="text/javascript" defer="defer">
	$(document).ready(function(){
		if(${param.method != 'create'}){
			if(${map.is_branch_root == 1}){
				$('#location').show();
			}else {
				$('#location').hide();
			}
		}
	});

	function showLocation(){
		var isBranchRoot = $('#is_branch_root').val();
		if(isBranchRoot == 1){
			$('#location').show();
		}else{
			$('#location').hide();
		}
	}
	
	
	//自定义showModalDialog 方法
	function returnValueChrome(option){//为打开的窗口定义方法，让打开的窗口关闭时通过window.opener赋值回来并执行
		
		var arr = option.split("^");//返回的指标字符串
 		var superior_id = arr[0];//上级组织编号
 		var superior_name = arr[1];//上级组织名称
 		var countyId = arr[2];//地市编号
 		var orgId = arr[3]; //组织编号
 		var orgLevel = arr[4]; //组织层级
 		//赋值
 		$('#superior_name').attr('title',superior_name);//改变title值
 		$('#superior_name').val(superior_name);
 		$('#superior_id').val(superior_id);
 		$('#county_id').val(countyId);
 		$('#org_id').val(orgId);
 		$('#org_label').html(orgId);
 		$('#org_level').val(orgLevel);
	}
	
	//保存
	function sav(src) {
		disable(src);
		var superior_id = $('#superior_id').val();
		if(superior_id==''){
			alert('请选择上级组织！');
			enable(src);
			return;
		}
		var method = $('#method').val();
		var old_org_id = $('#old_org_id').val();//以前的组织编号
		var org_id = $('#org_id').val();//最新的组织编号
		
		//验证组织编号不能重复
		var url = '${app}/sys/orgcfg/checkOrgId.do';
		var params = 'org_id='+org_id+'&old_org_id='+old_org_id;
		var result = new MyJqueryAjax(url, params).request();
		if(result>0){
			alert('组织编号已存在，请重新确认！');
			enable(src);
			return;
		}
		
		if(!Validator.Validate('form1')) {
			enable(src);
			return false;
		}
		var orgLev = $('').val();
		$('#form1').submit();
	}
	
	//加载组织选择器页面
 	function openOrg(){
 		var countyId = '';
 		var org_id = $('#org_id').val();
 		var params = 'countyId='+countyId+'&rejectOrgIds='+org_id+'&orgIds='+$('#superior_id').val();
 			
 		//加载组织选择器页面
 		var url = "${app}/sys/orgcfg/orgTreeFrame.do?"+params;
		var strs = MyWindow.OpenModalDialog(url,"","dialogWidth:500px;dialogHeight:570px;edge:Raised;center:Yes;help:No;resizable:no;status:No;dialogHide:no;scroll:yes;");
		if(strs == 'undefined' || strs == null || strs == ''){
 			return false;
 		}
 		
 		var arr = strs.split("^");//返回的指标字符串
 		var superior_id = arr[0];//上级组织编号
 		var superior_name = arr[1];//上级组织名称
 		var countyId = arr[2];//地市编号
 		var orgId = arr[3]; //组织编号
 		var orgLevel = arr[4]; //组织层级
 		
 		//赋值
 		$('#superior_name').attr('title',superior_name);//改变title值
 		$('#superior_name').val(superior_name);
 		$('#superior_id').val(superior_id);
 		$('#region_id').val(countyId);
 		$('#org_id').val(orgId);
 		$('#org_label').html(orgId);
 		$('#org_level').val(orgLevel);
 	}
 	
 	//清空组织
 	function restOrg(){
 		$('#superior_name').attr('title','');//改变title值
 		$('#superior_name').val('');
 		$('#superior_id').val('');
 		$('#region_id').val('');
 		$('#org_id').val('');
 	}
</script>
</html>
