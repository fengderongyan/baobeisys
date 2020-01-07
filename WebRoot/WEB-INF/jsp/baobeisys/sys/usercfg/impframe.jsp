<%@ page contentType="text/html;charset=utf-8"%>
<%@ include file="/inc/includeBase.jsp"%>
<html>
	<head>
		<title>导入</title>
		
	<style>
		input {
			border: 1pt solid Gray;
			padding-top: 1pt;
			height: 14pt;
			font-size: 12px;
			CURSOR: hand;
		}
		
		table.mobile th,table.mobile tr,table.mobile td {
			padding: 0px 0px 0px 10px !important;
			border: 1px solid #83AADA;
			valign: middle;
			font-size: 12px;
			text-align: center;
			align: center;
			border-collapse: collapse;
		}
		
		table.mobile tr.pageheader td {
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
	<body topmargin="0" leftmargin="0" scroll="no" class="mobile">
		<form name="form1"
			action="${app}/jktsreport/jkts/impFile.do" method="post"
			target="upload" ENCTYPE="multipart/form-data">
			<input type="hidden" name="method" id="method"
				value="${param.method}" />
			<table width="100%" class="mobile">
				<tr class="pageheader">
					<td colspan="3" align="center">
						用户验证系统导入
					</td>
				</tr>
				<tr style="height: 26px;">
					<td width="150px;">
						上传文件:
					</td>
					<td style="text-align: left;">
						<input type="file" name="file" style="width: 50%; height: 25px;" id="importFile" />
						&nbsp;
						<input type="button" onclick="imp();" name="btnUpload"
							id="btnUpload"
							style="background-image: url('/sys/images/btn_bg.GIF'); width: 58px; height: 24px; text-align: center; vertical-align: middle; padding: 0px 0px 0px 0px;"
							value=" 导 入 " />
						
						<a href="#"
							onclick="downloadFile('WEB-INF/jsp/baobeisys/sys/usercfg/importTemplate.xls', 'importTemplate');">
							<font style="text-decoration: underline; color: red;">
								模板下载 </font> 
						</a>
					</td>
				</tr>
			</table>
			<div id="lblInfo"></div>
			<div id="divWait" style="display: none; text-align: center;">
				<label id="label1" style="color: green; font-weight: bolder; text-align: center;">
					正 在 处 理,请 稍 候 ...
				</label>
			</div> 
			<iframe id="upload" name="upload" frameborder="0" height="70%"
				    width="100%" scrolling="no"></iframe>
		</form>
	</body>
	<script type="text/javascript">
		
		function imp(){
		    var file=$('#importFile').val();
		    if(file==""){
			    alert('请选择需要导入的文件！');
			    return false;
		    }
		    var str= file.substring(file.lastIndexOf(".")+1,file.length).toLowerCase();
			if(str!="xls") {
				alert("只能上传后缀名xls的文件！");
				form1.importFile.focus();
				return false;
			}
			divWait.style.display="inline";
			lblInfo.innerHTML = "";
			form1.btnUpload.disabled = true;
			form1.action="${app}/sys/usercfg/impInfo.do";
			form1.submit();
	    }      
	    
	    //下载文件 
	    function downloadFile(url,name) {
	    	window.location.href='<%=Constants.DOWNLOAD%>?file_name='+encodeURI(encodeURI(url))+
	    			'&filename='+encodeURI(encodeURI(name));
	    	//火狐浏览器缓存优先机制，如下方法不可用；IE正常
	    	<%-- window.location.reload('<%=Constants.DOWNLOAD%>?file_name='+encodeURI(encodeURI(url))+
	    			'&filename='+encodeURI(encodeURI(name))); --%>
	    }
	    
	    
	    
	</script>
</html>