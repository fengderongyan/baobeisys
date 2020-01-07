<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>后台管理</title>
		<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
		<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>
		<script type="text/javascript" src="${app}/js/jquery/jquery-1.12.4.js"></script>
		<script type="text/javascript" src="${app}/js/jquery/jquery.ajax.js"></script>
		<script type="text/javascript" src="${app}/js/common.js"></script>
		<link rel="stylesheet" type="text/css" href="${app}/css/themes/default/default.css" />
		<style type=text/css>
			table.mobile tr.pageheader1 td {
				text-valign: middle;
				text-align: center;
				FONT-WEIGHT: bold;
				FONT-SIZE: 13px;
				height: 26px;
				border-collapse: collapse;
				border: solid 1pt #D6E4F1;
				border-bottom: solid 0px;
				color: #004177;
				
			}
			table.list {
				border-collapse: collapse;
				background-color: white;
				font-size: 12px;
				width: 100%;
			}
			table.list th,table.list td {
				padding: 1px 2px 1px 2px !important;
				text-align: center;
				vertical-align: middle;
				align: center;
				font-weight: normal;
				border: 1px solid #B8C9D8;
			}
			table.list thead tr{
				color: #0054A6;
				font-size: 12px;
				height: 25px;
				font: 12px;
			}
			
			table.list thead tr th{
				font-weight: bold !important;
			}
			
			table.list tr.over {
				background-color: #fefae2;
			}
			
			table.list tr.even {
				background-color: #ECF7FF;
			}
			
			table.list tr.odd {
				background-color: #FFFFFF;
			}
			
			.todo_mid {
				width:100%;
				height:100%;
				background:url(${app}/css/images/welcome.png) center no-repeat;
				background-size:50% 50%;
			}
		</style>
	</head>
	<body scroll="no" style="width: 100%; height: 100vh">
		<sgy:csrfToken />
		<center style="height:100vh">
		<div class="todo_mid">
			
		</div>
		</center>
	</body>
	
	<script type="text/javascript">
		
	</script>
</html>