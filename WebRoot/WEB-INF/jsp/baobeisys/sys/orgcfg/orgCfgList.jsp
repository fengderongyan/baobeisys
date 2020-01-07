<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<%@ include file="/inc/includeBase.jsp"%>
	</head>
	<body>
		<display:table name="orgList" requestURI="${app}/sys/orgcfg/orgCfgList.do"
			class="list" id="row" cellspacing="0" style="width:100%"
			cellpadding="0" pagesize="${param.pageSize}" export="false" sort="external"  partialList="true" size="size">
			<display:column title="序号" style="width:32px;" media="html">
				<c:out value="${row_rowNum + beginIndex}" />
			</display:column>
			<display:column title="组织归属" property="county_name" />
			<display:column title="组织层级" property="orglev_name" />
			<display:column title="组织编号" property="org_id"  />
			<display:column title="组织名称" property="org_name" />
			
			<display:column title="操作" media="html" style="width:150px;">
				<sgy:button cssClass="smallBtn_gray"
					onclick="editOrg('${row.org_id}','${row.IS_BRANCH_ROOT}','show');return false;">详细</sgy:button>&nbsp;
				<sgy:button cssClass="smallBtn_gray"
					onclick="editOrg('${row.org_id}','${row.IS_BRANCH_ROOT}','edit');return false;">修改</sgy:button>&nbsp;
				<sgy:button cssClass="smallBtn_gray"
					onclick="deleteOrg('${row.org_id}');return false;">删除</sgy:button>&nbsp;
			</display:column>
		</display:table>
	</body>
	<script type="text/javascript">
		$(document).ready(function(){
			//启用frame查询按钮
			window.parent.enable($('#schbtn', parent.document));  
		});
		
    	function editOrg(org_id, isBranchRoot, method) {	
      　　		MyWindow.OpenCenterWindowScroll('${app}/sys/orgcfg/orgCfgEdit.do?org_id='+org_id
      				+'&is_branch_root='+isBranchRoot+'&method='+method,'orgCfgEdit',400,700);
    	}
    
	    function deleteOrg(org_id){
		 	if(!confirm('确定要删除该组织？')) {
				return false;
			}
			var url = '${app}/sys/orgcfg/deleteOrgCfg.do';
			var params="org_id="+org_id;
			var res = new MyJqueryAjax(url,params).request();
			if(res==1) {
				alert('数据操作成功！');
		  	} else {
				alert('数据操作失败！');
		  	}
		  	window.parent.$('#form1').submit();
		}
	</script>
</html>