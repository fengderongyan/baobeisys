<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<%@ include file="/inc/includeBase.jsp"%>
	</head>
	<body>
		<display:table name="roleList" requestURI="${app}/sys/rolecfg/roleList.do"
			class="list" id="row" cellspacing="0" style="width:100%"
			cellpadding="0" pagesize="${param.pageSize}" export="false" sort="external"  partialList="true" size="size">
			<display:column title="序号" style="width:60px;" media="html">
				<c:out value="${row_rowNum + beginIndex}" />
			</display:column>
			
			<display:column title="角色名称" property="role_name" />
			<display:column title="状态" property="status_name" />
			<display:column title="角色排序" property="role_order"  />
			<display:column title="操作人" property="record_name"  />
			<display:column title="操作时间" property="record_date"  />
			<display:column title="操作" media="html" style="width:220px;">
					<sgy:button cssClass="smallBtn_gray"
						onclick="edit('${row.role_id}','show');return false;">详细</sgy:button>&nbsp;
					<sgy:button cssClass="smallBtn_gray"
						onclick="edit('${row.role_id}','edit');return false;">修改</sgy:button>&nbsp;
					<sgy:button cssClass="smallBtn_gray"
						onclick="del('${row.role_id}');return false;">删除</sgy:button>&nbsp;
			</display:column>
		</display:table>
	</body>
	<script type="text/javascript">
	
		$(document).ready(function(){
			//启用frame查询按钮
			window.parent.enable($('#schbtn', parent.document));  
		});
		
    	function edit(role_id, method) {	
      　　		MyWindow.OpenCenterWindowScroll('${app}/sys/rolecfg/edit.do?role_id='+role_id+'&method='+method,'editrole',608,800);
    	}
    
		function del(role_id){
			if(confirm("确定要删除该条信息吗?")){
				var url = '${app}/sys/rolecfg/deleteRole.do';
				var params="role_id="+role_id;
				var res = new MyJqueryAjax(url,params).request();
				if(res==1) {
					alert('数据操作成功！');
			  	} else {
					alert('数据操作失败！');
			  	}
			  	window.parent.$('#form1').submit();
		  	}
		}
	</script>
</html>