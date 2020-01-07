<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>后台管理</title>
		<meta http-equiv="Cache-Control" content="no-store"/>
		<meta http-equiv="Pragma" content="no-cache"/>
		<meta http-equiv="Expires" content="0"/>
		<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE" />
		
		<%@ include file="/inc/includeBase.jsp" %>
		<link rel="stylesheet" type="text/css" href="${app}/css/login/login.css" />
		<script type="text/javascript" src="${app}/js/jquery-1.12.4.js"></script>
		<script type="text/javascript" src="${app}/js/jquery.ajax.js"></script>
		<script type="text/javascript" src="${app}/js/jquery/wbox/wbox.js"></script> 
		<link rel="stylesheet" type="text/css" href="${app}/js/jquery/wbox/wbox/wbox.css" />
		<link rel="stylesheet" type="text/css" href="${app}/css/themes/default/default.css" />
		<link rel="stylesheet" type="text/css" href="${app}/css/themes/reset.css" />
	</head>
	<style type="text/css">
		input {
			height: 22px;
			padding: 2px 0px 0px 2px;
		}
		
		input:-webkit-autofill { 
			-webkit-box-shadow: 0 0 0px 1000px #FBFBFB inset; 
		} 
		
		#fade {
			display: none;
			background: #000; 
			position: absolute; 
			left: 0; top: 0; 
			width: 100%; 
			opacity: .30;
			z-index: 9999;
		}
		
		.popup_block{
			display: none;
			height: 130px;
			background: #fff;
			padding: 10px; 	
			border: 5px solid #ddd;
			float: left;
			font-size: 1.2em;
			position: absolute;
			top: 50%; left: 50%;
			z-index: 99999;
			-webkit-box-shadow: 0px 0px 20px #000;
			-moz-box-shadow: 0px 0px 20px #000;
			box-shadow: 0px 0px 20px #000;
			-webkit-border-radius: 10px;
			-moz-border-radius: 10px;
			border-radius: 10px;
		}
		
		a.close {
			position: absolute;
			top: -16px;
			right: -16px;
		}
	</style>

	<body style="background-color:white;">
		<!-- 顶层LOGO和菜单 -->
		<div class="login_top">
			
			<div class="login_menu">
				<a id="favorites_a"  onclick="addFavorite()" tabindex="1">收藏</a>
				<span class="login_line">|</span>
				<a tabindex="1" href="javascript:void(0);"
					onclick="this.style.behavior='url(#default#homepage)';this.setHomePage('http://10.33.194.22:8090/baobeisys/login.jsp');this.blur();">设为首页</a>
			</div>
		</div>

		<!-- 中部大图 -->
		<div class="login_mid">
			<!-- 登录窗口 -->
			<div class="login_tab_all">
				<div class="login_tab_top"></div>
				<div class="login_tab_mid">
					<form name="loginForm" id="loginForm" action="login/login.do" method="post" ><!-- login/login.do -->
						<div class="main_tab">
							<div class="main_tab_l">
								<div class="main_tab_l_tit">
									登录名
								</div>
								<input type="text" id="loginName" name="loginName" onkeypress="onKeyPress('loginName');"
									class="input01" value="请输入登录名或手机号" tabindex="101" />
								<input type="hidden" id="loginType" name="loginType" value="1" />	
							</div>
						</div>

						<div class="main_tab" id="pwdDiv">
							<div class="main_tab_l">
								<div class="main_tab_l_tit">
									密&nbsp;&nbsp;&nbsp;码
								</div>
								<input type="password" id="loginPassword" name="loginPassword" onkeypress="onKeyPress('loginPwd');"
									class="input01" value="" style="display: none" tabindex="103" />
								<input type="text" id="passwd_input_placeholder" class="input01"
									value="请输入密码" tabindex="102" />
							</div>
						</div>


						<!-- 登录按钮 -->
						<input type="button" id="login_btn" class="button01"
							onclick="login();" value="登 录" />
				</div>
				<!-- 找回密码 -->

				</div>
				</form>

			</div>
		</div>
		
		<div id="codeDiv" style="display:none;height: 280px;width: 600px;text-align: center;" class="demo" >
			<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
			<table class="sgy" id="td1" cellspacing="0" cellpadding="0" style="width: 100%;">
				<tr height="30" >
					<td class="outDetail" style="width: 20%;border-bottom:1px dashed #D3D3D3">
						手机号码：<font color="red">*</font>
					</td>
					<td style="text-align: left;border-bottom:1px dashed #D3D3D3">
						<input type="text" id="msisdn" name="msisdn" class="msisdn" />&nbsp;&nbsp;&nbsp;
						<label id="codeBtnLabel">
						
						</label>
					</td>
				</tr>
				<tr height="30" >
					<td class="outDetail" style="width: 20%;border: 1px solid #D3D3D3;">
						验证码：<font color="red">*</font>
					</td>
					<td style="text-align: left;border: 1px solid #D3D3D3;">
						<input type="text" id="verify_code" name="verify_code" />
					</td>
				</tr>
			</table>
			<br />
			<a class="ovalbutton"  id="nextBtn" name="nextBtn" onclick="checkCode(this)" style="text-decoration:none;">
				<span>下一步</span>
			</a>
		</div>
		<div id="PassWordDiv" style="display:none;height: 280px;width: 600px;text-align: center;" class="demo" >
			<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
			<div class="mainlist">
				<table class="sgy" id="td1" cellspacing="0" cellpadding="0" style="width: 100%;">
					<tr height="30">
						<td class="outDetail" style="width: 20%">
							修改的工号：
							<input type="hidden" name="operatorId" id="operatorId" />
						</td>
						<td class="outDetail2" style="text-align: left;border: 1px solid #D3D3D3;padding-left: 2px" id="operatorId_td">
						</td>
					</tr>
					<tr height="30">
						<td class="outDetail" style="width: 20%">
							用户姓名：
						</td>
						<td class="outDetail2" style="text-align: left;border: 1px solid #D3D3D3;padding-left: 2px" id="user_name_td">
							
						</td>
					</tr>
					<tr height="30">
						<td class="outDetail" style="width: 20%">
							<font color="red">*</font> &nbsp; 新密码：
						</td>
						<td class="outDetail2" style="text-align: left;border: 1px solid #D3D3D3;">
							<input type="password" id="new_password1" name="new_password1" class="new_password1" autocomplete="off" />
						</td>
					</tr>
					<tr height="30">
						<td class="outDetail" style="width: 20%">
							<font color="red">*</font> &nbsp;确认密码：
						</td>
						<td class="outDetail2" style="text-align: left;border: 1px solid #D3D3D3;">
							<input type="password" id="new_password2" name="new_password2" class="new_password2" autocomplete="off" />
						</td>
					</tr>
					<tr height="30">
						<td class="outDetail2" colspan="2" style="text-align: left;border: 1px solid #D3D3D3;">
						<font style="color: red;padding-left: 40px;font-weight: bold;">密码规则：</font>	长度8~16位,包含小写字母、大写字母、数字、特殊符号中任意两种
						</td>
					</tr>
				</table>
				<br />
				<a class="ovalbutton" id="savePassWord" name="savePassWord" onclick="savePassWord(this)" style="text-decoration:none;">
					<span>保 存</span>
				</a>
			</div>
		</div>
		<!-- 底部菜单 -->
		<div class="login_footer">
			<span style="display: block; height: 20px; line-height: 20px;">
				
			</span>
			<span style="height: 20px; line-height: 20px;"> 
				 江苏沙光鱼软件科技有限公司提供技术支持<!--<span class="login_line">|</span> 帮助中心<span class="login_line">|</span> 服务热线：152xxxxxxxx-->
			</span>

		</div>
		

	</body>
	<script type="text/javascript">
	
		/**
		* 文本输入框鼠标定位时消除提示语
		* @param inputId 输入框id
		* @param inputDefaultValue 输入框默认值
		* @author yuhao
		*/
		function removeTipsOnFocus(inputId,inputDefaultValue){
			$("#"+inputId).focus(function(){
				if($(this).val() == inputDefaultValue){
					$(this).val("");
				}
			});
			$("#"+inputId).blur(function(){
				if($(this).val() == ""){
					$(this).val(inputDefaultValue);
				}
			});
		}
			
		/**
		* 密码域输入框鼠标定位时消除提示语
		* @param passwdId 密码域id
		* @param passwdPlaceHolderId 替代符id
		* @author yuhao
		*/
		function switchPasswdAndTips(passwdId,passwdPlaceHolderId){
			// 密码输入框隐藏
			$("#"+passwdId).hide();
			// 密码输入框提示语显示
			$("#"+passwdPlaceHolderId).show();
			// 密码输入框鼠标定位时消除提示语
			$("#"+passwdPlaceHolderId).focus(function(){
				$(this).hide();
				$("#"+passwdId).show();
				$("#"+passwdId).focus();
			});
			$("#"+passwdId).blur(function(){
				if($(this).val() == ""){
					$("#"+passwdPlaceHolderId).show();
					$(this).hide();
				}
			});
		}
				
	
		$(window).bind("scroll resize",function(){
			$(".login_mid").css("height",document.body.clientHeight-150);
		});
		$(".login_mid").css("height",document.body.clientHeight-150);
		
		init();
		removeTipsOnFocus("loginName","请输入登录名或手机号");
		switchPasswdAndTips("loginPassword","passwd_input_placeholder");
		setTimeout('pwdFocus();',200);
		function init() {
			$('#loginName').val(getCookie("operator_id_sys") || "请输入登录名或手机号");
			
			var loginType = $('#loginType').val();//登陆类型  1 编号登陆  2手机号码登陆
			if(loginType=='2'){
				$('#forgetUsername_a').html('手机号登陆');
			}else if(loginType=='1'){
				$('#forgetUsername_a').html('工号登陆');
			}
		}
		function pwdFocus(){
			if($('#loginName').val() != "请输入登录名或手机号"){
				$('#passwd_input_placeholder').focus();
			}
		}
		
		function login(){
			if($('#loginName').val()=='请输入登录名或手机号' || $('#loginName').val()==''){
				alert('请输入登录名或手机号');
				return false;
			}
			if($('#loginPassword').val()=='请输入密码' || $('#loginPassword').val()==''){
				alert('请输入密码');
				return false;
			}
			$('#screenHeight').val(screenHeight);
			SetCookie ("operator_id_sys", $('#loginName').val());
			SetCookie ("loginType_sys", $('#loginType').val());
			SetCookie ("passwd_sys", $('#loginPassword').val());
			
			//$('#loginForm').action = "${app}mobile/loginmi/checkLogin.do";
			$('#loginForm').submit();
			//checkpwd();
		}
		
		//登录检查
		function checkpwd(){
			var loginName=$('#loginName').val();
			var loginPassword=$('#loginPassword').val();
			var params="loginPassword="+loginPassword+"&loginName="+loginName;
			var url="${app}/login/checkPwd.do?"+params;
			var result=new MyJqueryAjax(url).request();
			if(result==-4){//密码为初始密码
				editpwd(result);
			}else{
				$('#loginForm').submit();
			}

		}
		
		//强制跳转到修改密码页面
		function editpwd(result){
		
			var operatorId = $('#loginName').val();
			var pass = $('#loginPassword').val();
			MyWindow.OpenCenterWindowScroll("${app}/login/editPwdFrame.do?flag="+result+"&pass="+pass+"&operator_id="+operatorId,"editPwdSys", "260"," 400");
			
		}
	
		function resets() {
			$('#loginForm').reset();
		}
		
		function onKeyPress(flag) {
			if(window.event.keyCode == 13){
				if(flag == 'loginName'){
					$('#loginPassword').show();
					$('#passwd_input_placeholder').hide();
					$('#loginPassword').focus();
				}
				if(flag == 'loginPwd'){
					login();
				}
			}
		} 
		
		function SetCookie(name,value) {
		    var Days = 30; //此 cookie 将被保存 30 天
		    var exp  = new Date();    //new Date("December 31, 9998");
		    exp.setTime(exp.getTime() + Days*24*60*60*1000);
		    document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
		}
		
		function getCookie(name) {
			var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
			if(arr != null) return unescape(arr[2]); return null;
		}
		
		function delCookie(name) {
			var exp = new Date();
			exp.setTime(exp.getTime() - 1);
			var cval=getCookie(name);
			if(cval!=null) document.cookie= name + "="+cval+";expires="+exp.toGMTString();
		}
		
		$(document).ready(function(){
			// 默认隐藏忘记密码功能
			// $("#inside").hide();
			
			$('#inside').wBox({
				title:'<span style="font-size:13px;">找回密码</span>',
				target:"#codeDiv"
			});
			
			$("#inside").click(function(){ 
				var BtnHtml = '<a class="ovalbutton"  id="codeBtn" name="codeBtn" onclick="sendMsgCode()" '+
							  'style="text-decoration:none;vertical-align: middle;"><span id="codeBtnSpan">'+
							  '点击获取验证码</span></a>'
				$('#codeBtnLabel').html(BtnHtml)
			});
			
			// 关闭遮罩层
			$('a.close').live('click', function() {
			  	$('#fade, .popup_block').fadeOut(function() {
					$('#fade, a.close').remove();  
				});
				return false;
			});
		});
		
		var count = 31; //倒计时30秒后重新获取
		var intervalTimeVal; //定时器
		
		//改变获取验证码按钮样式
		function changeCodeBtnStyle(){
			count--;
			$('#codeBtnSpan').html(count +'秒后重新获取');
			if(count==0){
				$("#codeBtn").attr('class', 'ovalbutton');	// 恢复原来的样式
				var fun = "var oldFn = function(){sendMsgCode();}";	// 定义新的函数
				eval(fun);	// 执行函数
				$("#codeBtn").get(0).onclick = eval('oldFn');
				$('#codeBtnSpan').html('点击获取验证码');
				clearInterval(intervalTimeVal);
				count = 31;
			}
		}
		
        //发送短信验证码
	    function sendMsgCode(){
	        var msisdn = $("#msisdn").val();
	        if(msisdn == ''){
	        	alert('请输入手机号码！');
	        	$("#msisdn").focus();
	        	return;
	        }
	        var url = "${app}/sys/personalsetting/getMsgCode.do";
			var params = "msisdn="+msisdn;
			var result = new MyJqueryAjax(url, params, null, 'JSON').request();
	        if(result == 1){
	            alert('验证码发送成功！');
	            $("#codeBtn").attr('onclick', 'return false;');	// 将原始绑定函数解除
				$("#codeBtn").attr('class', 'ovalBtn_disable');	// 改变按钮样式
	            intervalTimeVal = setInterval(changeCodeBtnStyle,1000);
	            return;	
	        }
	        else if(result == 0){
	            alert('手机号码不存在！');
	            return;
	        }
	        else{
	            alert('发送验证码失败，请重新点击发送验证码！');
	            return;
	        }
	    }
	    
	    //验证短信验证码是否正确
		function checkCode(){
			var msisdn = $("#msisdn").val();
			var verify_code = $("#verify_code").val();
			if(msisdn==''){
	        	alert('请输入手机号码！');
	        	$("#msisdn").focus();
	        	return;
	        }
	        if(verify_code==''){
	        	alert('请输入验证码！');
	        	$("#verify_code").focus();
	        	return;
	        }
	        var url = "${app}/sys/personalsetting/checkMsgCode.do";
			var params = "verify_code="+verify_code+"&msisdn="+msisdn;
			var result = new MyJqueryAjax(url, params).request();
	        if(result == 1){
	        	var url = "${app}/sys/personalsetting/getUserInfo.do";
	        	var params = "msisdn="+msisdn;
				var userInfo = new MyJqueryAjax(url, params, null, 'JSON').request();
				userInfo = eval(userInfo);
				$("#operatorId").val(userInfo[0].OPERATOR_ID);	// 警员工号
	            $("#operatorId_td").html(userInfo[0].OPERATOR_ID);	// 警员工号
	            $("#user_name_td").html(userInfo[0].NAME);	// 用户姓名
				$('.wBox_close').click();
				var wBox = $("#aaa").wBox({
					title:'<span style="font-size:13px;">设置密码</span>',
					target:"#PassWordDiv"
				});
				wBox.showBox();
			}else{
	            alert('验证码错误！');
	            $("#verify_code").focus();
	            return;
	        }
		}
	    
	    //保存修改密码
	    function savePassWord (src){
	    	var operator_id = $("#operatorId").val();	// 警员工号
	    	var new_password1 = $("#new_password1").val();	// 新密码
	        var new_password2 = $("#new_password2").val();	// 确认密码
	        if(new_password1 == ''){
	        	alert('请输入新密码！');
	        	$("#new_password1").focus();
	        	return;
	        }
	        if(new_password2 == ''){
	        	alert('请输入确认密码！');
	        	$("#new_password2").focus();
	        	return;
	        }
			if(new_password1 != new_password2)
			{
				alert("新密码与密码确认不一致！");
				return false;
			}
			if(validPassword(new_password1) < 3)
			{
				alert("密码格式不符合要求！");
				$('new_password1').focus();
				return false;
			}
			
			// 修改密码
			var url = "${app}/sys/personalsetting/savePassWord.do";
			var params = "operator_id="+operator_id+"&new_password="+new_password1;
			res = new MyJqueryAjax(url, params).request();
			if(res == 1)
			{
				alert("密码修改成功！");
				$("#verify_code").val();
				$("#new_password1").val();
				$("#new_password2").val();
				$('.wBox_close').click();
			}
			else
			{
				alert("密码修改失败，请重新操作！");
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
			else{
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
		
		//添加收藏
		function addFavorite() {
		    var url = window.location;
		    var title = document.title;
		    var ua = navigator.userAgent.toLowerCase();
		    if (ua.indexOf("360se") > -1) {
		       window.external.addFavorite(url, title);
		    }
		    else if (ua.indexOf("msie 8") > -1) {
		        window.external.AddToFavoritesBar(url, title); //IE8
		    }
		    else if (document.all) {
				try{
					window.external.addFavorite(url, title);
				}catch(e){
					alert('您的浏览器不支持,请按 Ctrl+D 手动收藏!');
				}
		    }
		    else if (window.sidebar) {
		        window.sidebar.addPanel(title, url, "");
		    }
		    else {
				alert('您的浏览器不支持,请按 Ctrl+D 手动收藏!');
		    }
		}
		
		//改变登陆方式
		function changeLoginType(){
			var loginType = $('#loginType').val();//登陆类型  1 警员编号登陆  2手机号码登陆
			SetCookie ("loginType_sys", loginType);
			if(loginType=='1'){//如果是警员编号登陆 点击的时候改变为手机号码登陆
				$('#loginType').val('2');
				$('#forgetUsername_a').html('手机号登陆');
			}else if(loginType=='2'){//如果是手机号码登陆 点击的时候改变为警员编号登陆
				$('#loginType').val('1');
				$('#forgetUsername_a').html('工号登陆');
			}
		}
		
		//修改
	    function openBrowserSet(){	
	    	MyWindow.OpenCenterWindowScroll('${app}/login/browserSet.do', '', 760, 950);
	    }
	</script>
</html>