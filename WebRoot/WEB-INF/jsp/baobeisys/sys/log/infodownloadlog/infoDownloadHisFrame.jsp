<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>系统登录流水</title>
		<%@ include file="/inc/includeBase.jsp"%>
		<script type="text/javascript" src="${app}/js/My97DatePicker/WdatePicker.js"></script>
	</head>
	<body>
		<form id="form1" name="form1" method="post" action="${app}/sys/log/infoDownloadHisList.do" target="listQuery">
			<sgy:csrfToken />
			<div id="queryPanel" class="queryPanel">
				<div id="queryPanel_title" class="queryPanel_title">
					<div class="queryPanel_title_collapse"></div>&nbsp;查询条件
				</div>
				<div id="queryPanel_content" class="queryPanel_content" style="position: relative;">
					<table class="search" cellspacing="0" cellpadding="0">
						<tr>
							<td class="outDetail"  style="width: 80px;">
								人员工号：
							</td>
							<td class="outDetail2">
								<input type="text" name="operator_id" />
							</td>
							<td class="outDetail"  style="width: 80px;">
								姓名：
							</td>
							<td class="outDetail2"">
								<input type="text" name="name" />
							</td>
							<td class="outDetail" style="width: 80px;">
								
							</td>
							<td class="outDetail2">
								
							</td>
						</tr>
						<tr class="pagesearch">
							<td class="outDetail"  style="width: 80px;">
								手机号码：
							</td>
							<td class="outDetail2"">
								<input type="text" name="mobile" />
							</td>
							<td class="outDetail"  style="width: 80px;">
								归属组织：
							</td>
							<td class="outDetail2"">
								<input type="text" name="org_name" />
							</td>
							<td class="outDetail">
								登录时间：
							</td>
							<td class="outDetail2" >
								<input type="text" name="begin_date" id="begin_date" class="Wdate"
									value="${begin_date}" size="15" onfocus="WdatePicker();" />
								至&nbsp;
								<input type="text" name="end_date" id="end_date" class="Wdate"
									value="${end_date}" size="15" onfocus="WdatePicker();" />
							</td>
						</tr>
					</table>
					<div id="queryPanel_footer" class="queryPanel_footer"
						style="position: absolute; bottom: 1px; right: 10px;">
						<input type="hidden" id="pageSize" name="pageSize" value="" />
						<sgy:button cssClass="ovalbutton" id="schbtn" onclick="sch();return false;">查 询</sgy:button>
					</div>
				</div>
			</div>
			<div id="listPanel" class="queryPanel">
				<div id="queryPanel_title" class="queryPanel_title">
					<div class="queryPanel_title_collapse"></div>
					&nbsp;信息下载流水列表
				</div>
				<div id="queryPanel_content" class="queryPanel_content">
					<iframe id="listQuery" allowtransparency="true" name="listQuery" frameborder="0" width="100%" scrolling="auto"></iframe>
				</div>
			</div>
		</form>
		
		<script type="text/javascript">
		 	//先调用设置行数的函数，再调用默认查询函数
		 	setPageSize('listQuery');
		 	sch();
		 	function sch() 
		 	{
		 		var begin_date = $('#begin_date').val();
		 		var end_date = $('#end_date').val();
				
				if(begin_date==''){
					alert("请选择登录时间！");
					$('#begin_date').focus();
					return;
				}
				
				if(end_date==''){
					alert("请选择登录时间！");
					$('#end_date').focus();
					return;
				}
		      	disable(document.getElementById("schbtn"));
				$('#listQuery').attr('src','${app}/inc/baseLoading.jsp');
		  		setTimeout('form1.submit();',500);
		 	}
			
			// 导出
		    function expt()
		    {
		 		var begin_date = $('#begin_date').val();
		 		var end_date = $('#end_date').val();
				if(begin_date==''){
					alert("请选择登录时间！");
					$('#begin_date').focus();
					return;
				}
				
				if(end_date==''){
					alert("请选择登录时间！");
					$('#end_date').focus();
					return;
				}
				
				//插入日志并禁用按钮
				var log_id = exportBtn.customExportLog('exptbtn','${app}','登录流水导出',
				'${param.module_id}','${app}/sys/log/exportLoginHis.do',
				$("#form1").serialize());
		    	var url = "${app}/sys/log/exportLoginHis.do?" + $("#form1").serialize()+'&log_id='+log_id;
		    	
		    	window.location.href = url;
		    }
		</script>
	</body>
</html>
