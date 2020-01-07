<%@ page contentType="text/html;charset=utf-8"%>
<%@ include file="/inc/includeBase.jsp"%>
<html>
	<head>
		<title>上传</title>
	<style>
		
		table.form_table tr.pageheader td {
			BACKGROUND-IMAGE: url(../../images/img_table_bg.png);
			text-valign: middle;
			text-align: center;
			FONT-WEIGHT: bold;
			FONT-SIZE: 15px;
			width: 100%;
			height: 26px;
			border-collapse: collapse;
			border: solid 1pt #D6E4F1;
			border-bottom: solid 0px;
			color: #004177;
		}
	</style>
	</head>
	<body >
		<form name="form1"
			action="${app}/sys/usercfg/impFile.do" method="post" ENCTYPE="multipart/form-data">
			<input type="hidden" id="operator_id" name="operator_id" value="${operatorId }" />
			<table width="100%" class="form_table">
				<tr class="pageheader">
					<td  align="center" colspan="2" >
						签名信息上传
					</td>
				</tr>
				<tr>
					<td style="text-align: left; width: 50px;"  class="outDetail">
						&nbsp;&nbsp;签名上传:
					</td>
					<td class="outDetail2">
						<input type="file" name="file" class="up_file" 
							style="width: 50%;height: 25px;" id="importFile" />
						&nbsp;&nbsp;&nbsp;
						<sgy:button cssClass="ovalbutton" onclick="imp();return false;">上 传</sgy:button>
					</td>
				</tr>
			</table>
		</form>
	</body>
	<script type="text/javascript">
		
		function imp(){
		    var file=$('#importFile').val();
		    if(file==""){
		    	alert('请选择需要上传的签名图片！');
		    	return false;
		    }
		    var str = file.substring(file.lastIndexOf(".")+1,file.length).toLowerCase();
	    	if(!(str=='jpg'||str=='jpeg'||str=='bmp'||str=='png')) {
				alert("只能上传格式为jpg、jpeg、bmp、png的图片！");
				form1.cl_type.focus();
				return false;
			}
			var operatorId = $('#operator_id').val();
			form1.action="${app}/sys/usercfg/impFile.do?operator_id="+operatorId;
			form1.submit();
	    }      
	</script>
</html>