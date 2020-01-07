<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<%@ include file="/inc/includeBase.jsp"%>
	</head>
	<body>
		<display:table name="userList" requestURI="${app}/yjcfg/userList.do?bb_type=${bb_type}"
			class="list" id="row" cellspacing="0" style="width:100%"
			cellpadding="0" pagesize="${param.pageSize}" export="false" sort="external"  partialList="false" size="size">
			<display:column style="width:50px;" title="<input name='totalCheck' id='totalCheck' type='checkbox' 
				onclick='selectAll();'>全选" media="html">
				<input type="checkbox" id="operator_id" name="operator_id" value="${row.operator_id}" />
			</display:column>
			<display:column title="序号" style="width:60px;" media="html">
				<c:out value="${row_rowNum + beginIndex}" />
			</display:column>
			<display:column title="归属组织"  property="org_name"/>
			<display:column title="工号"  style="width:100px;"  property="operator_id" />
			<display:column title="姓名" property="name" />
			<display:column title="角色" property="roleNames" />
			<display:column title="审核人" property="deal_oper_name"/>
			<display:column title="操作" media="html" style="width:100px;">
				<sgy:button cssClass="smallBtn_gray" id="doYjCfg"
					onclick="doYjCfg2('${row.operator_id }');return false;">审核配置</sgy:button>&nbsp;
			</display:column>
		</display:table>
		<center>
			<input type="hidden" id="doyjcfg_btn" onclick="doYjCfg();return false;"/>
		</center>
	</body>
	<script type="text/javascript">
		
		$(document).ready(function(){
			//启用frame查询按钮
			window.parent.enable($('#schbtn', parent.document));  
		});
		var bb_type= '${bb_type}';
		//点击全选时调用此函数
		function selectAll(){
	    	var ids = document.getElementsByName('operator_id');
	    	var totalCheck = $('#totalCheck').prop('checked');//jq1.6以上版本
	    	if(ids.length == 0){
	    		return;
	    	}
	    	for (var i = 0; i < ids.length; i++){
	    		ids[i].checked = totalCheck;
	    	}
	    }
		
		function doYjCfg(){
			var check_array = document.getElementsByName("operator_id");
		 	var operatorIdStr = "";
		 	var i, j = 0,k = 0 ;
			for (i = 0;i< check_array.length;i++){
				if (check_array[i].checked){
				  	j = 1;
				  	operatorIdStr += "," + check_array[i].value;
				} 
			}
			if(j == 0){
			 	alert("请选择需配置的人员！");
			 	window.parent.enable($('#doYjCfg', parent.document));
			 	return ;
			}
			operatorIdStr = operatorIdStr.substring(1);
			window.parent.enable($('#doYjCfg', parent.document));
			MyWindow.OpenCenterWindowScroll('${app}/yjcfg/yjcfgMain.do?operatorIdStr=' + operatorIdStr + '&bb_type=' + bb_type,'yjcfgMain',800,1200);
		}
		function doYjCfg2(operator_id){
			MyWindow.OpenCenterWindowScroll('${app}/yjcfg/yjcfgMain.do?operatorIdStr=' + operator_id + '&bb_type=' + bb_type,'yjcfgMain',800,1200);
		}
	</script>
</html>