<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.sgy.util.Constants"%>
<%@ include file="/inc/includeBase.jsp"%>
<script>
    var loadImg=window.parent.document.getElementById("divWait");
    loadImg.style.display="none";
    var btn = window.parent.document.getElementById("btnUpload");
    btn.disabled=false;
</script>

<style type="text/css">
	body.mobile {
		leftmargin: 0px;
		topmargin: 0px;
		color: #000000;
		padding: 0px;
		margin: 0px;
		width:100%;
		text-align:center;
		font-family: "Tahoma", "Arial", "宋体";
	}	
	
	body.mobile table.mobile {
		border: 1px solid #7FB4DE;
		margin: 0 0 0 0 !important;
		border-collapse: collapse;
		width:100%;
		font-size:12px;
		border-bottom: #ACE186 solid 2px;
	}
	
	table.mobile tr.pageheader td {
		BACKGROUND-IMAGE: url(../images/tablerow1.gif);
		text-valign: middle;
		text-align: left;
		padding: 4px 0px 0px 20px !important;
		FONT-WEIGHT: bold;
		FONT-SIZE: 15px;
		color: #0054A6;
		width: 100%;
		height: 33px;
		border: 0px;
	}
	
	table.mobile td.outDetail2 {
		background-color: white;
		padding: 0px 0px 0px 2px !important;
		text-align: center;
		FONT-SIZE: 14px;
		word-break: break-all;
		word-wrap:break-word;
	}
</style>
<html>
	<head>
		<title>错误信息</title>
	</head>
	<body class="mobile">
		<form>
			<br /><br />
			<center>
				<table class="mobile" border="0" align="center" cellpadding="0" cellspacing="0"
					style="width:420px">
					<tr height="20px" class="pageHeader">
						<td colspan="2" class="outDetail" style="text-align:left">
							&nbsp;
							<strong style="font-size:13">错误信息：</strong>
						</td>
					</tr>
					<tr style="border: 0">
						<td align="left" class="outDetail2" style="color: #ee2222;font-size:12;border: 0"
							 height="50">
							${error }
						</td>
					</tr>
				</table>
				<sgy:button cssClass="smallBtn_gray"
					onclick="window.location.href = 'about:blank'">返 回</sgy:button> 				
				&nbsp;&nbsp;&nbsp;&nbsp;
				<sgy:button cssClass="smallBtn_gray"
					onclick="window.location.href = 'about:blank'">关 闭</sgy:button>
			</center>
		</form>
	</body>
</html>

