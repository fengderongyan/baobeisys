<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<%@ include file="/inc/includeBase.jsp"%>
	</head>
	<body>
		<display:table name="userList" requestURI="${app}/sys/usercfg/userList.do"
			class="list" id="row" cellspacing="0" style="width:100%"
			cellpadding="0" pagesize="${param.pageSize}" export="false" sort="external"  partialList="false" size="size">
			<display:column title="序号" style="width:60px;" media="html">
				<c:out value="${row_rowNum + beginIndex}" />
			</display:column>
			<display:column title="归属组织"  property="org_name"/>
			<display:column title="工号"  style="width:100px;"  property="operator_id" />
			<display:column title="姓名" property="name" />
			<display:column title="角色" property="roleNames" />
			<display:column title="手机" property="mobile" style="width:100px;" />
			<display:column title="操作" media="html" style="width:220px;">
					<sgy:button cssClass="smallBtn_gray"
						onclick="optUser('${row.operator_id}', 3);return false;">密码复位</sgy:button>&nbsp;
					<sgy:button cssClass="smallBtn_gray"
						onclick="editUser('${row.operator_id}','show');return false;">详细</sgy:button>&nbsp;
					<sgy:button cssClass="smallBtn_gray"
						onclick="editUser('${row.operator_id}','edit');return false;">修改</sgy:button>&nbsp;
					<c:if test="${row.status == '1'}">
						<sgy:button cssClass="smallBtn_gray"
							onclick="optUser('${row.operator_id}','${row.STATUS}');return false;">删除</sgy:button>&nbsp;
					</c:if>
					<c:if test="${row.status != '1'}">
						<sgy:button cssClass="smallBtn_gray"
							onclick="optUser('${row.operator_id}','${row.STATUS}');return false;">恢复</sgy:button>&nbsp;
					</c:if>
			</display:column>
		</display:table>
	</body>
	<script type="text/javascript">
		
		
		$(document).ready(function(){
			//启用frame查询按钮
			window.parent.enable($('#schbtn', parent.document));  
		});
		
		function qmUpload(operatorId) {
			MyWindow.OpenCenterWindowScroll('${app}/sys/usercfg/qmInfoUpload.do?operator_id='+operatorId,'uploadQmInfo',250,650);
		}
		
    	function editUser(operator_id, method) {	
      　　		MyWindow.OpenCenterWindowScroll('${app}/sys/usercfg/editUser.do?operator_id='+operator_id+'&method='+method,'editUser',608,800);
    	}
    
		function optUser(operator_id, flag){
			if(flag==1) {
				if(!confirm('确定要删除吗？')) {
					return false;
				}
			}
			
			if(flag==0) {
				if(!confirm('确定要恢复吗？')) {
					return false;
				}
			}
			
			if(flag==3) {
			  	if(!confirm('确定要密码复位吗？（复位后的密码：工号后三位+手机号码后三位）')) {
					return false;
				}
			}
			
			var url = '${app}/sys/usercfg/deleteUser.do';
			var params="operator_id="+operator_id+"&flag="+flag;
			var res = new MyJqueryAjax(url,params).request();
			if(res==1) {
				alert('数据操作成功！');
		  	} else {
				alert('数据操作失败！');
		  	}
		  	window.parent.$('#form1').submit();
		}
		
		//下载文件 
	    function downloadFile(url,name) {
	    	window.location.href="<%=Constants.DOWNLOAD%>?file_name="+encodeURI(encodeURI(url))+
	    			"&filename="+encodeURI(encodeURI(name));
	    	<%--IE 和 360 可用，火狐不可用，原因是：火狐缓存优先机制 
	    		window.location.reload('<%=Constants.DOWNLOAD%>?file_name='+encodeURI(encodeURI(url))+
	    			'&filename='+encodeURI(encodeURI(name))); --%>
	    }
	</script>
</html>