<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>请假报备明细</title>
		<%@ include file="/inc/includeBase.jsp"%>
		<script type="text/javascript" src="${app}/js/My97DatePicker/WdatePicker.js"></script>
	</head>
	<body>
		<form id="form1" name="form1" method="post" action="${app}/bbcount/qjDetailList.do" target="listQuery">
			<sgy:csrfToken />
			<div id="queryPanel" class="queryPanel">
				<div id="queryPanel_title" class="queryPanel_title">
					<div class="queryPanel_title_collapse"></div>
					&nbsp;查询条件
				</div>
				<div id="queryPanel_content" class="queryPanel_content" style="position: relative;">
					<table class="search" cellspacing="0" cellpadding="0">
						<tr class="pagesearch">
							<td class="outDetail"  style="width: 80px;">
								归属组织：
							</td>
							<td class="outDetail2"">
								<sgy:select list="${orgList}" id="org_id" name="org_id" 
									headValue="" headLabel="请选择..." optionLabel="org_name" optionValue="org_id"  style="width:300px;" />
							</td>
							<td class="outDetail">
								&nbsp;&nbsp;报备时间：
							</td>
							<td class="outDetail2" colspan="3">
								<input type="text" name="begin_date" id="begin_date" class="Wdate"
									value="${begin_date}" size="15" onfocus="WdatePicker();" />
								至&nbsp;
								<input type="text" name="end_date" id="end_date" class="Wdate"
									value="${end_date}" size="15" onfocus="WdatePicker();" />
							</td>
							<td class="outDetail">
								&nbsp;&nbsp;报备人：
							</td>
							<td class="outDetail2" colspan="3">
								<input type="text" id="bbr" name="bbr" />
							</td>
						</tr>
					</table>
					<div id="queryPanel_footer" class="queryPanel_footer"
						style="position: absolute; bottom: 1px; right: 10px;">
						<input type="hidden" id="pageSize" name="pageSize" value="" />
						<sgy:button cssClass="ovalbutton"  id="schbtn" onclick="sch();return false;">查 询</sgy:button>
					</div>
				</div>
			</div>
			<div id="listPanel" class="queryPanel">
				<div id="queryPanel_title" class="queryPanel_title">
					<div class="queryPanel_title_collapse"></div>
					&nbsp;请假报备明细列表
				</div>
				<div id="queryPanel_content" class="queryPanel_content">
					<iframe id="listQuery" allowtransparency="true" name="listQuery"
						frameborder="0" width="100%" scrolling="auto"></iframe>
				</div>
			</div>
		</form>
	</body>
	<script type="text/javascript">
		//先调用设置行数的函数，再调用默认查询函数
		setPageSize('listQuery');
		sch();
		function sch() {
			disable(document.getElementById("schbtn"));
			$('#listQuery').attr('src','${app}/inc/baseLoading.jsp');
	  		setTimeout('form1.submit();',500);
		}
	</script>
</html>