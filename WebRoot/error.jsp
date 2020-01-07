<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>提示信息</title>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<style type=text/css>
		body {
			font-size: 12px;
		}
		
		a {
			color: #1b72af;
			text-decoration: none
		}
		
		a:hover {
			text-decoration: underline
		}
		
		ol {
			margin-left: 0px;
			color: #888;
			list-style-type: decimal
		}
		
		li {
			line-height: 22px;
			color: red;
		}
		
		.mt10 {
			margin-top: 20px
		}
		
		.tip-page {
			margin: 100px auto 25px;
			width: 600px
		}
		
		.tip-table {
			background: #ffffff;
			margin: 0px 1px;
			WIDTH: 598px
		}
		
		.tip-top {
			background: #1665C1;
			height: 50px
		}
		
		.tip-bgB {
			background: #1b72af;
			padding-top: 1px
		}
		
		.tip-bgC {
			background: #1b72af;
			padding-top: 1px
		}
		
		.tip-content {
			padding-left: 30px;
		}
		
		.error_mid {
			clear:both;
			float:left;
			width:100%;
			position:absolute;
			height:104%;
			_top:80px;
			padding-top:20px;
			border-top:1px solid #e2e2e2;
			border-bottom:1px solid #e2e2e2;
			background:url(css/images/bg6.jpg) top no-repeat;
		}
		</style>
	</head>
	<body>
	<div class="error_mid">
		<div class="tip-page">
			<div class=tip-bgC>
				<div class=tip-top></div>
				<table class="tip-table" cellspacing="0" cellpadding="0">
					<tr>
						<td height="130" align="center" width="100">
							<div>
								<img src="${app}/images/warning.png" width="70"/>
							</div>
						</td>
						<td height="130">
							<div class="tip-content">
								对不起！您没有登录或者没有权限访问此页面，可能有如下几个原因：
								<ol>
									<li>
										您要访问的链接可能已失效，或数据已被删除！
									</li>
									<li>
										您尚未登录或会话超时，请您重新登录！
									</li>
								</ol>
							</div>
						</td>
					</tr>
					<tr>
						<td align="center" colspan="2">
							<div style="margin-bottom: 20px;">
								<a tabIndex="20" href="#" onclick="window.close();">关 闭</a>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<a id="showindex" tabIndex="20" href="#" onclick="login();" target="_self">登 录</a>
							</div>
						</td>
					</tr>
				</table>
			</div>
			<div class=tip-bgB></div>
		</div>
		</div>
		<script type="text/javascript">
		function login() {
			window.top.location.href = "${app}/login.jsp";
		}
		</script>
	</body>
</html>