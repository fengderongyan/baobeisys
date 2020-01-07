<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/includeBase.jsp" %>
<html>
<head>
<style type="text/css">
body {
	leftmargin: 0px;
	topmargin: 0px;
	color: #000000;
	font-size: 12px;
	padding: 0px;
	margin: 0px;
	font-family: "Tahoma", "Arial", "宋体";
	text-align: center;
	border-collapse: collapse;
}

table.mobile {
	border: 1px solid #B0BEC7;
	margin: 0 0 0 0 !important;
	border-collapse: collapse;
	width: 100%;
}

th, tr, td {
	padding: 0px 0px 0px 10px !important;
	border-collapse: collapse;
	border: 1px solid #666; 
	valign: middle;
	font-size: 12px;
	text-align: center;
	align: center;
}

tr.pageheader td {
	background-image: url("images/header.jpg");
	border-collapse: collapse;
	text-valign: middle;
	text-align: center;
	font-weight: bold;
	font-size: 15px;
	color: #083681;
	width: 100%;
	height: 28px;
}


td.outDetail {
	background-color: "#EEF3F6";
	padding: 0px 0px 0px 0px !important;
	border-collapse: collapse;
	text-align: center;
	height: 25px
}

td.outDetail2 {
	background-color: white;
	padding: 0px 0px 0px 2px !important;
	border-collapse: collapse;
	text-align: left;
	word-break: break-all;
	word-wrap: break-word;
	height: 25px;
}

input, select {
	font-family: "Tahoma", "Arial", "宋体";
	font-size: 12px;
	color: #000000;
}

input {
	border: expression(( this . type == "text" || this
		. type == "file") ? "1px solid #B0BEC7" : "" );
	padding: 2px 0px 0px 2px;
	color: #000000;
}

td.outDetail2 input {
	size: 18;
}

table.report th {
	background-color: #EEF2F3;
}

.btn{
	padding: 1px 2px 1px 2px !important;
	BORDER: 1px solid #7FB4DE;
	background-color: "#FFFFFF";
	FONT-SIZE: 12px;
	color:#0054A6 !important;
	filter:progid:DXImageTransform.Microsoft.Gradient(enabled='true',startColorStr='#FFFFFF', endColorStr='#C9E9F9');
	CURSOR: hand;
}
</style>
	</head>
	<body  style="background: #F3F2FA;" leftmargin="20%" topmargin="35%">
		<form id="form1" name="form1" action="">
			<br/>
			<TABLE style="width: 90%;border: 2px;" class="mobile">
            <tr class="pageHeader">
                <td colspan="2">
                    修 改 密 码
                </td>
            </tr>
             <tr height="28">
                 <td width="35%" class="outDetail">原密码：</td>
                 <td width="65%" class="outDetail2"><input type="password" name="oldpwd" dataType="Require" msg="原密码不能为空" style="width: 99%"></td>
             </tr>
             <tr height="28" >
                 <td width="35%" class="outdetail" >新密码：</td>
                 <td width="65%" class="outdetail2"><input type="password" name="newpwd" dataType="Require" msg="新密码不能为空" style="width: 99%"></td>
             </tr>
              <tr height="28" >
              	<td width="35%" class="outdetail" >确认新密码：</td>
                 <td width="65%" class="outdetail2"><input type="password" name="checkpwd" dataType="Require" msg="确认新密码不能为空" style="width: 99%"></td>
              </tr>
			</TABLE>
			<p align="center">
				<br />
				<input type="button" class="btn" onclick="sav();"
					name="btnRePswd" value="提 交">
				&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" class="btn" onclick="window.close();"
					value="关 闭">
			
			</p>
			<c:if test="${param.flag eq '-4'}">
			<center><br><font color="red">密码为原始密码请修改密码<br>(新修改的密码至少包含字母和数字，长度不得低于8位) </font></center>
			</c:if>
		</form>
	</body>

<script type="text/javascript">
	form1.oldpwd.focus();
	function sav(){
		if(!Validator.Validate("form1"))
		{
			return false;
		}
		var	oldpwd1 = '${param.pass}';
		var oldpwd2=form1.oldpwd.value;
		var newpwd=form1.newpwd.value;
		var checkpwd=form1.checkpwd.value;
		if(oldpwd1 != oldpwd2){
			alert('原密码输入有误！');
			form1.oldpwd.focus();
			return false;
		}
		if(newpwd.length<8){
			alert("新密码至少为8位!");
			return false;
		}
		//var exp  =  /^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*$/i ;
		var exp1 = /^[0-9]*$/i;//纯数字验证
		var exp2 = /^[a-zA-z]*$/i;//纯字母验证
		
		if(exp1.test(newpwd))
		{
			alert('密码必需包含字母和数字的组合');
			return ;
		}
		if(exp2.test(newpwd))
		{
			alert('密码必需包含字母和数字的组合');
			return ;
		} 
		
		
		if(newpwd!=checkpwd){
			alert('两次输入的新密码不一致！');
			return false;
		}if(oldpwd1==newpwd){
			alert('原密码与新密码不能相同！');
			form1.newpwd.focus();
			return false;
		}
		var params="flag=${param.flag}&operator_id=${param.operator_id}&newpwd="+encodeURI(encodeURI(newpwd));
		var url="${app}/login/editPwd.do?"+params;
		var result=new MyJqueryAjax(url).request();
		if(result==1){
			alert('修改成功，请重新登陆系统');
			
			//top.location.reload();
			window.opener.location.href="${app}/login.jsp";
			window.close();
			return;
		}else if(result==0){
			alert('修改失败！');
			return ;
		}else{
			alert('操作失败！');
			return false;
		}
		
	}
</script>
</html>