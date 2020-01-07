<%@page import="com.sgy.util.Constants"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net/el"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="sgy" uri="/WEB-INF/sgy"%>

<jsp:useBean id="str" class="com.sgy.util.common.StringHelper" scope="application"/>
<!-- <meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests"> -->
<script type="text/javascript" src="${app}/js/jquery/jquery-1.12.4.js"></script> 
<script type="text/javascript" src="${app}/js/jquery/jquery.ajax.js"></script>
<script type="text/javascript" src="${app}/js/common.js"></script>
<script type="text/javascript" src="${app}/js/sgyjs.js"></script>
<script type="text/javascript" src="${app}/js/My97DatePicker/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css" href="${app}/css/themes/reset.css" />
<link rel="stylesheet" type="text/css" href="${app}/css/themes/default/default.css" />
<link rel="stylesheet" type="text/css" href="${app}/css/themes/default/displaytag.css" />
<link rel="Shortcut Icon" type="image/x-icon" href="${app}/favicon.ico"/>
