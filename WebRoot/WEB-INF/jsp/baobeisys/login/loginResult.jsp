<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/inc/includeBase.jsp" %>
<script>
  var result='${loginRes}';
  var checkRes = '${checkRes}';
  var operatorId = '${operatorId}';
  var pass = '${pwd}';
  if(result==0){
  	 alert('用户名不存在!');
  	 window.location.href="${app}/login.jsp";
  }else if(result==2){
  	 alert('用户名和密码不匹配！');
  	 window.location.href="${app}/login.jsp";
  }else if(result==-3){
  	 alert('无法连接数据库！');
  	 window.location.href="${app}/login.jsp";
  }
  if(checkRes == -4){
  	alert('密码为初始密码！');
	MyWindow.OpenCenterWindowScroll("${app}/login/editPwdFrame.do?flag="+checkRes+"&pass="+pass+"&operator_id="+operatorId, "editPwdSys", "260", "400");
  }
</script>