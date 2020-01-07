<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sgy.util.Constants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>修改密码</title>
		<%@ include file="/inc/includeBase.jsp"%>
	</head>
	<body>
		<form id="form1" name="form1" action="" method="post">
			<sgy:csrfToken />
			<div id="queryPanel" class="queryPanel">
				<div id="queryPanel_title" class="queryPanel_title">
					<div class="queryPanel_title_collapse"></div>
					&nbsp;${qzxg_flag eq '1' ? '您的密码为初始密码，请修改密码' : '修改密码'}
				</div>
				<div id="queryPanel_content" class="queryPanel_content">
					<table align="center" class="form_table" id="td1" cellspacing="0"
						cellpadding="0" style="width: 100%;">
						<tr>
							<td align="center" class="outDetail" style="width: 35%">
								<font color="red">*</font>&nbsp;用户：
							</td>
							<td class="outDetail2" style="width: 65%">
								${operatorId}
								<input type="hidden" name="operator_id" id="operator_id" value="${operatorId}" />
								<input type="hidden" name="qzxg_flag" id="qzxg_flag" value="${qzxg_flag}" />	
							</td>
						</tr>
						<c:if test="${qzxg_flag ne '1'}">
							<tr>
								<td class="outDetail">
									<font color="red">*</font>&nbsp;原密码：
								</td>
								<td class="outDetail2">
									<input type="password" name="old_password" id="old_password"
										dataType="Require" msg="原密码不能为空！" size="30" />
								</td>
							</tr>
						</c:if>
						<tr>
							<td class="outDetail"　>
								<font color="red">*</font>&nbsp;新密码：
							</td>
							<td class="outDetail2">
								<input type="password" name="new_password1" id="new_password1"
									dataType="Require" msg="新密码不能为空！" size="30" />
							</td>
						</tr>
						<tr>
							<td class="outDetail"　>
								<font color="red">*</font>&nbsp;确认密码：
							</td>
							<td class="outDetail2">
								<input type="password" name="new_password2" id="new_password2"
									dataType="Require" msg="确认密码不能为空！" size="30" />
							</td>
						</tr>
						<tr>
							<td class="outDetail">
								<b style="color: red;">密码规则：</b>
							</td>
							<td class="outDetail2">
								<div style="color: red;">
									长度8~16位,包含小写字母、大写字母、数字、特殊符号中任意两种
								</div>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<br />
			<div style="text-align: center;">
				<a class="ovalbutton" onclick="sav(this);return false;"><span>保
						存</span> </a>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
		</form>
		<script type="text/javascript">
			// 保存密码修改
			function sav(src)
			{
				disable(src);
				if(!Validator.Validate("form1"))
				{
					enable(src);
					return false;
				}
				var operator_id = $("#operator_id").val();	// 人员工号
				var old_password = $("#old_password").val();	// 原密码
				var new_password1 = $("#new_password1").val();	// 新密码
		        var new_password2 = $("#new_password2").val();	// 确认密码
		        $("#password").val(new_password1);
		        if(old_password == new_password1)
		        {
		        	alert("新密码与原密码相同，请重新输入！");
		        	enable(src);
					return false;
		        }
				if(new_password1 != new_password2)
				{
					alert("新密码与确认密码不一致！");
					enable(src);
					return false;
				}
				if(validPassword(new_password1) < 3)
				{
					alert("密码格式不符合要求！");
					$('new_password1').focus();
					enable(src);
					return false;
				}
				
				var qzxg_flag = $("#qzxg_flag").val();//1  原始密码 要强制修改密码 
				
				if(qzxg_flag!='1'){
					// 校验原密码
					var url = '${app}/sys/password/checkPassWord.do';
					var params="operator_id="+operator_id+"&old_password="+old_password;
					var res = new MyJqueryAjax(url,params).request();
					if(res == 0){
						alert("原密码不正确，请重新输入！");
						enable(src);
						return false;
					}
				}
				
				// 修改密码 
				var url = '${app}/sys/password/savePassWord.do';
				params = "operator_id="+operator_id+"&new_password="+new_password1;
				res = new MyJqueryAjax(url, params).request();
				if(res == 1){
					alert("密码修改成功！");
					if(qzxg_flag=='1'){
						window.location = '${app}login/index.do';
					}else{
						window.location.reload();
					}
					
				}else {
					alert("密码修改失败，请重新操作！");
					enable(src);
					return false;
				}
			}
			
			// 校验密码格式
			function validPassword(passwork)
			{
				var res = 0;
				if(/(?=^.{8,16}$).*/.test(passwork))
				{
					res++;
				}
				else
				{
					return res;
				}
				if(/(?=(.*\d){1,}).*/.test(passwork))
				{
					res++;
				}
				if(/(?=(.*[a-z]){1,}).*/.test(passwork))
				{
					res++;
				}
				if(/(?=(.*[A-Z]){1,}).*/.test(passwork))
				{
					res++;
				}
				if(/(?=(.*[\W|_]){1,}).*/.test(passwork))
				{
					res++;
				}
				return res;
			}
			
			function jump() 
			{
				if(window.screen) 
				{
					var height = window.screen.availHeight;
					var width = window.screen.availWidth;
				}
				var url = '${app}/login/index.do?screenHeight='+window.screen.height+'&screenWidth='+window.screen.width;
				win = window.open(url, '后台管理' + Math.round(Math.random() * 1000), 'height='+height+', width='+width+', top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no, directories=no');
				window.opener = null;	//去掉关闭时候的提示窗口
      			window.open("", "_self");
      			window.close();
      			win.moveTo(0, 0);
　　     			win.resizeTo(window.screen.availWidth, window.screen.availHeight);
			}
		</script>
	</body>
</html>