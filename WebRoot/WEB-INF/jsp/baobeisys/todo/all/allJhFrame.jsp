<%@ page contentType="text/html; charset=gbk"%>
<%@ include file="/inc/includeBase.jsp"%>
<html>
	<head>
		<title>�鿴��������</title>
	</head>
	<body scroll="no">
		<form name="form1" action="${app}/todo/allJhList.do" target="list">
			<table width=100% height="8%" border="0">
				<tr>
					<td align="center" style="font-size: 15px">
						��ȣ�
						<sgy:select list="yearsList" name="year" id="year"
							headLabel="��ѡ��..." headValue=""
							optionLabel="year" optionValue="year" />
						&nbsp;&nbsp;
						<sgy:button cssClass="ovalbutton" onclick="ssh();">�� ѯ</sgy:button>
					</td>
				</tr>
			</table>
		</form>
		<iframe id="list" name="list" frameborder="0" height="85%"
			width="100%" scrolling="no"></iframe>
		<script type="text/javascript">
		    function ssh(){
			    form1.submit();
		    }
			form1.submit();
		</script>
	</body>
</html>