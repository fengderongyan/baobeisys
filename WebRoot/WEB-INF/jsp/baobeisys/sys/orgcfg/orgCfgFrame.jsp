<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>组织管理</title>
		<%@ include file="/inc/includeBase.jsp"%>
	</head>
	<body>
		<form id="form1" name="form1" method="post" action="${app}/sys/orgcfg/orgCfgList.do" target="listQuery">
			<div id="queryPanel" class="queryPanel">
				<div id="queryPanel_title" class="queryPanel_title">
					<div class="queryPanel_title_collapse"></div>
					&nbsp;查询条件
				</div>
				<div id="queryPanel_content" class="queryPanel_content"
					style="position: relative;">
					<table class="search" cellspacing="0" cellpadding="0">
						<tr>
							<td class="outDetail" style="width: 70px;">
								层级：
							</td>
							<td class="outDetail2" style="width: 100px;">
								<sgy:select list="orgLevList" name="org_lev" id="org_lev"
									headLabel="请选择..." headValue=""
									optionLabel="org_lev" optionValue="org_lev" />
							</td>
							<td class="outDetail" style="width: 70px;">
								组织编号：
							</td>
							<td class="outDetail2" style="width: 100px;">
								<input type="text" id="org_id" name="org_id" />
							</td>
							<td class="outDetail" style="width: 70px;">
								组织名称：
							</td>
							<td class="outDetail2" style="width: 100px;">
								<input type="text" id="org_name" name="org_name" />
							</td>
							
						</tr>
					</table>
					<div id="queryPanel_footer" class="queryPanel_footer"
						style="position: absolute; bottom: 1px; right: 10px;">
						<input type="hidden" id="pageSize" name="pageSize" value="" />
						<sgy:button id="schbtn" cssClass="ovalbutton" onclick="sch();return false;">查 询</sgy:button>
						&nbsp;&nbsp;&nbsp;&nbsp;
						
						<sgy:button id="schbtn" cssClass="ovalbutton" onclick="add();return false;">新 增</sgy:button>
					</div>
				</div>
			</div>
		</form>
		<div id="listPanel" class="queryPanel">
			<div id="queryPanel_title" class="queryPanel_title">
				<div class="queryPanel_title_collapse"></div>
				&nbsp;组织列表
			</div>
			<div id="queryPanel_content" class="queryPanel_content">
				<iframe id="listQuery" allowtransparency="true" name="listQuery"
					frameborder="0" width="100%" scrolling="auto"></iframe>
			</div>
		</div>
	</body>
	<script type="text/javascript">
	 	//先调用设置行数的函数，再调用默认查询函数
		setPageSize('listQuery');
		sch();
  		function sch()
  		{
  			disable(document.getElementById("schbtn"));
  			$('#listQuery').attr('src','${app}/inc/baseLoading.jsp');
			setTimeout('form1.submit();',500);
  		}
  	
		function add() {
			MyWindow.OpenCenterWindowScroll('${app}/sys/orgcfg/orgCfgEdit.do?method=create','orgCfgEdit',400,700);
		}
	</script>
</html>
