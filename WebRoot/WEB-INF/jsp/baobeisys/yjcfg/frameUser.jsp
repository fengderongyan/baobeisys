<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>用户管理</title>
		<%@ include file="/inc/includeBase.jsp"%>
	</head>
	<body>
		<form id="form1" name="form1" method="post" action="${app}/yjcfg/userList.do?bb_type=${param.bb_type}" target="listQuery">
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
								工号：
							</td>
							<td class="outDetail2" style="width: 100px;">
								<input type="text" id="operator_id" name="operator_id" />
							</td>
							<td class="outDetail" style="width: 70px;">
								姓名：
							</td>
							<td class="outDetail2" style="width: 100px;">
								<input type="text" id="name" name="name" />
				
							<td class="outDetail" style="width: 70px;">
								手机号码：
							</td>
							<td class="outDetail2" style="width: 100px;">
								<input type="text" id="mobile" name="mobile" />
							</td>
							
						</tr>
						<tr>
							<td class="outDetail">
								所属组织：
							</td>
							<td class="outDetail2">
								<input type="text" id="org_name" name="org_name" />
							</td>
							<td class="outDetail">
								角色：
							</td>
							<td class="outDetail2">
								<sgy:select list="rolesList" name="role_id" id="role_id"
									headLabel="请选择..." headValue=""
									optionLabel="ROLE_NAME" optionValue="role_id" />
							</td>
							<td class="outDetail">
								&nbsp;是否有效：
							</td>
							<td class="outDetail2">
								<sgy:select id="status" name="status">
									<sgy:option value="1">是</sgy:option>
									<sgy:option value="0">否</sgy:option>
								</sgy:select>
							</td>
						</tr>
					</table>
					<div id="queryPanel_footer" class="queryPanel_footer"
						style="position: absolute; bottom: 1px; right: 10px;">
						<input type="hidden" id="pageSize" name="pageSize" value="" />
						<sgy:button id="schbtn" cssClass="ovalbutton" onclick="sch();return false;">查 询</sgy:button>
						&nbsp;&nbsp;
						<sgy:button id="doYjCfg" cssClass="ovalbutton" onclick="doYjCfg();return false;">批量配置</sgy:button>
						<sgy:button id="schbtn" cssClass="ovalbutton" onclick="testjk();return false;">接口测试</sgy:button>
						<%--
 						<sgy:button cssClass="ovalbutton" onclick="addmore();return false;">多个新增</sgy:button>  
 						&nbsp;&nbsp;
 						<sgy:button cssClass="ovalbutton" onclick="imp();return false;">导入</sgy:button> --%>
					</div>
				</div>
			</div>
		</form>
		<div id="listPanel" class="queryPanel">
			<div id="queryPanel_title" class="queryPanel_title">
				<div class="queryPanel_title_collapse"></div>
				&nbsp;用户列表
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
  		
  		function doYjCfg(){
  			disable(document.getElementById("doYjCfg"));
  			$("#listQuery").contents().find("#doyjcfg_btn").click();
  		}
  		
  		function testjk(){
			var url = "http://localhost:8080/schoolsys/wechat/applet/wxgoodsinfo/insertOrderInfo";
			var jsonstr = [{goods_id:'7832c6ba378d4fbfb788e22858ad300a',goods_cnt:'1'},{goods_id:'adc023c1598746eaa2d1a41ffce6087d',goods_cnt:'2'}]
			var jsonstr2 = JSON.stringify(jsonstr);
			console.log(jsonstr2);
			$.ajax({
						type :'post',
						url : url,
						data : {goodsPayJson:jsonstr2, total_pay:'50', lxr_phone:'123', lxr_name: 'aa'},
						headers:{openId:'wssafqqqe1221212'},
						async:false,//这一步很重要
						timeout:30000,
						dataType :'json',
						success : function(data) {
							console.log(data);
						},
						error : function(jqXHR, textStatus, errorThrown) {
							alert("网络连接超时");
						}
				});
		}
  	
	</script>
</html>

