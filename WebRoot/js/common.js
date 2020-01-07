var admin_app_root = "/baobeisys";
Validator = {mode:"", Require:/.+/, Email:/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/, Phone:/^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/, Msisdn:/^(147|150|151|152|157|158|159|182|183|184|187|188|198|13[4-9]{1})[0-9]{8}$/, Mobile:/^(13|14|15|17|18|19)[0-9]{9}$/, Url:/^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/, IdCard:"this.IsIdCard(value)", Currency:/^\d+(\.\d+)?$/, Number:/^\d+$/, Zip:/^[1-9]\d{5}$/, QQ:/^[1-9]\d{4,8}$/, Integer:/^[-\+]?\d+$/,
 Integer2:/^\d+$/,IP:/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/, Double:/^[-\+]?\d+(\.\d+)?$/, English:/^[A-Za-z]+$/, Chinese:/^[\u0391-\uFFE5]+$/, Username:/^[a-z]\w{3,}$/i, UnSafe:/^(([A-Z]*|[a-z]*|\d*|[-_\~!@#\$%\^&\*\.\(\)\[\]\{\}<>\?\\\/\'\"]*)|.{0,5})$|\s/,Longitude:/^1[1-2][0-9]\.\d{6,12}$/,Latitude:/^3[0-6]\.\d{6,12}$|2[8-9]\.\d{6,12}$/, File:/^.+(.txt|.csv|.dbf|.mht|.rar|.zip|.7z|.xls|.xlsx|.doc|.docx|.ppt|.pptx|.jpg|.jpeg|.gif|.png|.bmp|.pdf|.flv|.mkv|.rmvb|.3gp|.wmv|.mov|.avi|.mpg)$/i, IsSafe:function (str) {
	return !this.UnSafe.test(str);
}, SafeString:"this.IsSafe(value)", Filter:"this.DoFilter(value, getAttribute('accept'))", Limit:"this.limit(value.length,getAttribute('min'),  getAttribute('max'))", LimitB:"this.limit(this.LenB(value), getAttribute('min'), getAttribute('max'))", Date:"this.IsDate(value, getAttribute('min'), getAttribute('format'))", Repeat:"value == document.getElementsByName(getAttribute('to'))[0].value", Range:"getAttribute('min') <= (value|0) && (value|0) <= getAttribute('max')", Compare:"this.compare(value,getAttribute('operator'),getAttribute('to'))", Custom:"this.Exec(value, getAttribute('regexp'))", Group:"this.MustChecked(getAttribute('name'), getAttribute('min'), getAttribute('max'))", ErrorItem:[document.forms[0]], ErrorMessage:[], Validate:function (theForm, mode) {
	mode = mode || 1;
	this.mode = mode;
	try {
		theForm = document.getElementById(theForm);
	}
	catch (e) {
	}
	var obj = theForm || event.srcElement;
	var count = obj.elements == undefined ? obj.all.length : obj.elements.length;//modify chang
	this.ErrorMessage.length = 1;
	this.ErrorItem.length = 1;
	this.ErrorItem[0] = obj;
	for (var i = 0; i < count; i++) {
		var foundError = false;
		with ((obj.elements == undefined ? obj.all[i] : obj.elements[i])) {
			var _dataType = getAttribute("dataType");
			
			//alert(_dataType);
			if (typeof (_dataType) == "object" || typeof (this[_dataType]) == "undefined") {
				continue;
			}
			this.ClearState((obj.elements == undefined ? obj.all[i] : obj.elements[i]));
			if ((obj.elements == undefined ? obj.all[i] : obj.elements[i]).disabled) {
				continue;
			}
			if (getAttribute("require") == "false" && value == "") {
				continue;
			}
			switch (_dataType) {
			  case "IdCard":
			  case "Date":
			  case "Repeat":
			  case "Range":
			  case "Compare":
			  case "Custom":
			  case "Group":
			  case "Limit":
			  case "LimitB":
			  case "SafeString":
			  case "Filter":
				if (!eval(this[_dataType])) {
					this.AddError(i, getAttribute("msg"));
					if (mode == 1) {
						foundError = true;
					}
				}
				break;
			  default:
				if (!this[_dataType].test(value.trim())) {
					this.AddError(i, getAttribute("msg"));
					if (mode == 1) {
						foundError = true;
					}
				}
				break;
			}
		}
		if (foundError == true) {
			break;
		}
	}
	if (this.ErrorMessage.length > 1) {
		var errCount = this.ErrorItem.length;
		switch (mode) {
		  case 1:
			alert(this.ErrorMessage.join("\n"));
			try {
				this.ErrorItem[1].focus();
			}catch(e){};
			break;
		  case 2:
			for (var i = 1; i < errCount; i++) {
				try {
					var span = document.createElement("SPAN");
					span.id = "__ErrorMessagePanel";
					span.style.color = "red";
					span.style.fontSize = "12px";
					this.ErrorItem[i].parentNode.appendChild(span);
					span.innerHTML = this.ErrorMessage[i].replace(/\d+:/, "*");
				}
				catch (e) {
					alert(e.description);
				}
			}
			try {
				this.ErrorItem[1].focus();
			}catch(e){};
			break;
		  default:
			this.ErrorMessage = ["\u4ee5\u4e0b\u539f\u56e0\u5bfc\u81f4\u63d0\u4ea4\u5931\u8d25\uff1a\t\t\t\t"];
			alert(this.ErrorMessage.join("\n"));
			break;
		}
		return false;
	}
	return true;
}, limit:function (len, min, max) {
	min = min || 0;
	max = max || Number.MAX_VALUE;
	return min <= len && len <= max;
}, LenB:function (str) {
	return str.replace(/[^\x00-\xff]/g, "**").length;
}, ClearState:function (elem) {
	with (elem) {
		if (style.color == "red") {
			style.color = "";
		}
		var lastNode = parentNode.childNodes[parentNode.childNodes.length - 1];
		if (lastNode.id == "__ErrorMessagePanel") {
			parentNode.removeChild(lastNode);
		}
	}
}, AddError:function (index, str) {
	this.ErrorItem[this.ErrorItem.length] = (this.ErrorItem[0].elements == undefined ? this.ErrorItem[0].all[index] : this.ErrorItem[0].elements[index]);
	if (this.mode == 1) {
		this.ErrorMessage[this.ErrorMessage.length] = str;
	} else {
		this.ErrorMessage[this.ErrorMessage.length] = this.ErrorMessage.length + ":" + str;
	}
}, Exec:function (op, reg) {
	return new RegExp(reg, "g").test(op);
}, compare:function (op1, operator, op2) {
	switch (operator) {
	  case "NotEqual":
		return (op1 != op2);
	  case "GreaterThan":
		return (op1 > op2);
	  case "GreaterThanEqual":
		return (op1 >= op2);
	  case "LessThan":
		return (op1 < op2);
	  case "LessThanEqual":
		return (op1 <= op2);
	  default:
		return (op1 == op2);
	}
}, MustChecked:function (name, min, max) {
	var groups = document.getElementsByName(name);
	var hasChecked = 0;
	min = min || 1;
	max = max || groups.length;
	for (var i = groups.length - 1; i >= 0; i--) {
		if (groups[i].checked) {
			hasChecked++;
		}
	}
	return min <= hasChecked && hasChecked <= max;
}, DoFilter:function (input, filter) {
	return new RegExp("^.+.(?=EXT)(EXT)$".replace(/EXT/g, filter.split(/\s*,\s*/).join("|")), "gi").test(input);
}, IsIdCard:function (number) {
	var date, Ai;
	var verify = "10x98765432";
	var Wi = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
	var area = ["", "", "", "", "", "", "", "", "", "", "", "\u5317\u4eac", "\u5929\u6d25", "\u6cb3\u5317", "\u5c71\u897f", "\u5185\u8499\u53e4", "", "", "", "", "", "\u8fbd\u5b81", "\u5409\u6797", "\u9ed1\u9f99\u6c5f", "", "", "", "", "", "", "", "\u4e0a\u6d77", "\u6c5f\u82cf", "\u6d59\u6c5f", "\u5b89\u5fae", "\u798f\u5efa", "\u6c5f\u897f", "\u5c71\u4e1c", "", "", "", "\u6cb3\u5357", "\u6e56\u5317", "\u6e56\u5357", "\u5e7f\u4e1c", "\u5e7f\u897f", "\u6d77\u5357", "", "", "", "\u91cd\u5e86", "\u56db\u5ddd", "\u8d35\u5dde", "\u4e91\u5357", "\u897f\u85cf", "", "", "", "", "", "", "\u9655\u897f", "\u7518\u8083", "\u9752\u6d77", "\u5b81\u590f", "\u65b0\u7586", "", "", "", "", "", "\u53f0\u6e7e", "", "", "", "", "", "", "", "", "", "\u9999\u6e2f", "\u6fb3\u95e8", "", "", "", "", "", "", "", "", "\u56fd\u5916"];
	var re = number.match(/^(\d{2})\d{4}(((\d{2})(\d{2})(\d{2})(\d{3}))|((\d{4})(\d{2})(\d{2})(\d{3}[x\d])))$/i);
	if (re == null) {
		return false;
	}
	if (re[1] >= area.length || area[re[1]] == "") {
		return false;
	}
	if (re[2].length == 12) {
		Ai = number.substr(0, 17);
		date = [re[9], re[10], re[11]].join("-");
	} else {
		Ai = number.substr(0, 6) + "19" + number.substr(6);
		date = ["19" + re[4], re[5], re[6]].join("-");
	}
	if (!this.IsDate(date, "ymd")) {
		return false;
	}
	var sum = 0;
	for (var i = 0; i <= 16; i++) {
		sum += Ai.charAt(i) * Wi[i];
	}
	Ai += verify.charAt(sum % 11);
	return (number.length == 15 || number.length == 18 && number == Ai);
}, IsDate:function (op, formatString) {
	formatString = formatString || "ymd";
	var m, year, month, day;
	switch (formatString) {
	  case "yyyy-mm-dd":
		m = op.match(new RegExp("^((\\d{4}))([-])(\\d{2})([-])(\\d{2})$"));
		if (m == null) {
			return false;
		}
		day = m[6];
		month = m[4] * 1;
		year = (m[2].length == 4) ? m[2] : GetFullYear(parseInt(m[3], 10));
		break;
	  case "ymd":
		m = op.match(new RegExp("^((\\d{4})|(\\d{2}))([-./])(\\d{1,2})\\4(\\d{1,2})$"));
		if (m == null) {
			return false;
		}
		day = m[6];
		month = m[5] * 1;
		year = (m[2].length == 4) ? m[2] : GetFullYear(parseInt(m[3], 10));
		break;
	  case "dmy":
		m = op.match(new RegExp("^(\\d{1,2})([-./])(\\d{1,2})\\2((\\d{4})|(\\d{2}))$"));
		if (m == null) {
			return false;
		}
		day = m[1];
		month = m[3] * 1;
		year = (m[5].length == 4) ? m[5] : GetFullYear(parseInt(m[6], 10));
		break;
	  default:
		break;
	}
	if (!parseInt(month)) {
	
		return false;
	}
	month = month == 0 ? 12 : month;
	var date = new Date(year, month - 1, day);
	return (typeof (date) == "object" && year == date.getFullYear() && month == (date.getMonth() + 1) && day == date.getDate());
	function GetFullYear(y) {
		return ((y < 30 ? "20" : "19") + y) | 0;
	}
}, IsFile:function (value) {
	return this.File.test(value.toLowerCase());
}, isEmpty:function (value) {
	return this.Require.test(value);
}, isMsisdn:function (value) {
	return this.Msisdn.test(value.trim());
	},
	isMobile:function(value){
	return this.Mobile.test(value.trim());	
	},
   isInteger2:function (value) {
	return this.Integer2.test(value);
	},
	isIP:function (value) {
		if(this.IP.test(value))
		{
			if( RegExp.$1 <256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256)
			 return true;
		}
		return false;
	},
	isLongitude:function (value) {
		return this.Longitude.test(value);
	},
	isLatitude:function (value) {
		return this.Latitude.test(value);
	}
	
};

var has_showModalDialog = !!window.showModalDialog;//定义一个全局变量判定是否有原生showModalDialog方法
if(!has_showModalDialog &&!!(window.opener)){		
	window.onbeforeunload=function(){
		window.opener.hasOpenWindow = false;		//弹窗关闭时告诉opener：它子窗口已经关闭
	}
}
//定义window.showModalDialog如果它不存在
if(window.showModalDialog == undefined){
	window.showModalDialog = function(url,mixedVar,features){
		if(window.hasOpenWindow){
			alert("您已经打开了一个窗口！请先处理它");//避免多次点击会弹出多个窗口
			window.myNewWindow.focus();
		}
		window.hasOpenWindow = true;
		if(mixedVar) var mixedVar = mixedVar;
		//因window.showmodaldialog 与 window.open 参数不一样，所以封装的时候用正则去格式化一下参数
		if(features) var features = features.replace(/(dialog)|(px)/ig,"").replace(/;/g,',').replace(/\:/g,"=");
		//window.open("Sample.htm",null,"height=200,width=400,status=yes,toolbar=no,menubar=no");
		//window.showModalDialog("modal.htm",obj,"dialogWidth=200px;dialogHeight=100px"); 
		var left = (window.screen.width - parseInt(features.match(/width[\s]*=[\s]*([\d]+)/i)[1]))/2;
		var top = (window.screen.height - parseInt(features.match(/height[\s]*=[\s]*([\d]+)/i)[1]))/2;
		window.myNewWindow = window.open(url,"_blank",features);
	}
}

// 屏幕分辨率
var screenHeight = window.screen.height;

MyWindow = {OpenCenterWindow:function (url, name, height, width) {
	var str = " height=" + height + ",innerHeight=" + height;
	str += ",width=" + width + ",innerWidth=" + width;
	if (window.screen) {
		var ah = screen.availHeight - 30;
		var aw = screen.availWidth - 10;
		var xc = (aw - width) / 2;
		var yc = (ah - height) / 2;
		xc = xc >= 0 ? xc : 0 ;
		yc = yc >= 0 ? yc : 0 ;
		str += ",left=" + xc + ",screenX=" + xc;
		str += ",top=" + yc + ",screenY=" + yc;
	}
	
	//var root = url.substring(1).substring(0,url.substring(1).indexOf('/'));
	//var openWindowUrl = "/"+root+"/inc/openCenterWindow.jsp??_url_="+encodeURIComponent(url);
	if(url.indexOf('?') == -1){
		url = url + '?csrfToken='+$('#csrfToken').val();
	} else {
		url = url + '&csrfToken='+$('#csrfToken').val();
	}
	return window.open(url, name, str);
}, OpenCenterWindowScroll:function (url, name, height, width) {
	var str = " height=" + height + ",innerHeight=" + height;
	str += ",width=" + width + ",innerWidth=" + width;
	if (window.screen) {
		var ah = screen.availHeight - 30;
		var aw = screen.availWidth - 10;
		var xc = (aw - width) / 2;
		var yc = (ah - height) / 2;
		xc = xc >= 0 ? xc : 0 ;
		yc = yc >= 0 ? yc : 0 ;
		str += ",left=" + xc + ",screenX=" + xc;
		str += ",top=" + yc + ",screenY=" + yc;
		str += ",scrollbars=yes";
	}
	//var root = url.substring(1).substring(0,url.substring(1).indexOf('/'));
	//var openWindowUrl = "/"+root+"/inc/openCenterWindow.jsp?_url_="+encodeURIComponent(url);
	if(url.indexOf('?') == -1){
		url = url + '?csrfToken='+$('#csrfToken').val();
	} else {
		url = url + '&csrfToken='+$('#csrfToken').val();
	}
	return window.open(url, name, str);
}, OpenNoCsrfWindowScroll:function (url, name, height, width) {
	var str = " height=" + height + ",innerHeight=" + height;
	str += ",width=" + width + ",innerWidth=" + width;
	if (window.screen) {
		var ah = screen.availHeight - 30;
		var aw = screen.availWidth - 10;
		var xc = (aw - width) / 2;
		var yc = (ah - height) / 2;
		xc = xc >= 0 ? xc : 0 ;
		yc = yc >= 0 ? yc : 0 ;
		str += ",left=" + xc + ",screenX=" + xc;
		str += ",top=" + yc + ",screenY=" + yc;
		str += ",scrollbars=yes";
	}
	return window.open(url, name, str);
},OpenModalDialog:function(wintype, url, args, style){
	if(arguments.length==3)
	{
		style = arguments[2];
		args = arguments[1];
		url = arguments[0];
		wintype = "0";
	}

	var sUrl = url;
	var param = "?rnd="+Math.random();
	var idx = url.indexOf("?");
	if(idx >= 0)
	{
		sUrl = url.substring(0, idx);
		param = param + "&" + url.substring(idx+1); 
	}
	sUrl = sUrl + param;
	//var root = sUrl.substring(1).substring(0,sUrl.substring(1).indexOf('/'));
	//var openWindowUrl = "/"+root+"/inc/openCenterWindow.jsp?_url_="+encodeURIComponent(sUrl);
	if(url.indexOf('?') == -1){
		url = url + '?csrfToken='+$('#csrfToken').val();
	} else {
		url = url + '&csrfToken='+$('#csrfToken').val();
	}
	if("0"==wintype || "modal"==wintype)
	{
		return window.showModalDialog(url,window, style);
	}else if("1"==wintype || "modeless"==wintype)
	{
		return window.showModelessDialog(url, args, style);
	}else
	{
		return window.open(url, args, style);
	}
},OpenNoCsrfModalDialog:function(wintype, url, args, style){
	if(arguments.length==3)
	{
		style = arguments[2];
		args = arguments[1];
		url = arguments[0];
		wintype = "0";
	}

	var sUrl = url;
	var param = "?rnd="+Math.random();
	var idx = url.indexOf("?");
	if(idx >= 0)
	{
		sUrl = url.substring(0, idx);
		param = param + "&" + url.substring(idx+1); 
	}
	sUrl = sUrl + param;
	//var root = sUrl.substring(1).substring(0,sUrl.substring(1).indexOf('/'));
	//var openWindowUrl = "/"+root+"/inc/openCenterWindow.jsp?_url_="+encodeURIComponent(sUrl);
	if("0"==wintype || "modal"==wintype)
	{
		return window.showModalDialog(url,window, style);
	}else if("1"==wintype || "modeless"==wintype)
	{
		return window.showModelessDialog(url, args, style);
	}else
	{
		return window.open(url, args, style);
	}
}
};


String.prototype.endWith = function(oString){   
	var reg = new RegExp(oString+"$");   
	return reg.test(this);   
}
String.prototype.startWith = function(oString){   
	var reg = new RegExp("^"+oString);   
	return reg.test(this);   
}


String.prototype.trim = function(){return this.replace(/(^\s*)|(\s*$)/g,"");}

//页面禁止鼠标右键
document.oncontextmenu = function (){
	return true;
}

function getRow(src) {
	var row = src;
	while (row && row.tagName != "TR") {
		row = row.parentElement;
	}
	return row;
}


function getCbxCheckedValue(object_name)
{ 
	var cbx_values = '';
	$("input:checked[name='"+object_name+"']").each(function(i)
	{
		cbx_values += $(this).val()+',';
	});
	if(cbx_values != ''){
		cbx_values = cbx_values.substr(0,cbx_values.length-1);
	}
	return cbx_values;
}

function getCbxCheckedCount(object_name)
{
	return $("input:checked[name='"+object_name+"']").length;
}

/**为兼容火狐 需将event传入(火狐event只能在事件发生的现场使用)
 * 火狐不支持srcElement支持target
 **/
function delRow(tableId, e) {
	var tb = document.getElementById(tableId);
	var event = event ? event: e;
	var src = event.srcElement ? event.srcElement : e.target;
	var row = getRow(src); 
	tb.deleteRow(row.rowIndex);
}

function rebuildRowNum(tableId) {
	var tb = document.getElementById(tableId);
	var rows = tb.rows;
	var cell=rows[0].cells[0];
	var rowspan=cell.rowSpan;
	var rownum=1;
	for (i = rowspan; i < rows.length;  ) {
		cell = rows[i].cells[0];
		rowspan=cell.rowSpan;
		cell.innerText = rownum;
		i=rowspan+i;
		rownum=rownum+1;
	}
}

//设置查询页面的pageSize隐藏控件的值
function getPageSize(tableId, iframeId, rowHeight){
	//设置iframe的高度
	$('#'+iframeId).height($(document).height()-$('#'+tableId).height()-3);
	//如果不传行高，默认27
	rowHeight = (rowHeight == null) ? 27 : rowHeight;
	//设置pageSize值
	$('#pageSize').val(Math.round($('#'+iframeId).height()/rowHeight));
}

//取节点的距页面顶部和左部的距离
function absPos(node){
	var x=y=0;
	do{
		x+=node.offsetLeft;
		y+=node.offsetTop;
	}while(node=node.offsetParent);
	return {
		'x':x,
		'y':y
	};         
}

//设置查询页面的pageSize隐藏控件的值
function setPageSize(iframeId){
	//设置iframe的高度
	$('#'+iframeId).height($(document).height()-absPos($('#'+iframeId)[0]).y-5);
	$('#pageSize').val(Math.round($('#'+iframeId).height()/37));
}

//display表格行变色
function changeTrBgColor() {
	$(".list tr.even").mouseover(function(){
		$(this).addClass("over");}).mouseout(function(){
		$(this).removeClass("over");});
	$(".list tr.even").addClass("even");
	
	$(".list tr.odd").mouseover(function(){
		$(this).addClass("over");}).mouseout(function(){
		$(this).removeClass("over");});
	$(".list tr.odd").addClass("odd");
}

$(function(){
	//查询面板收缩展开
   	$('.queryPanel_title').each(function(){
   		$(this).click(function(){
   			if($(this).attr('showFlag') == '1' || $(this).attr('showFlag') == undefined){
   				$(this).next().fadeOut('fast');
   				$(this).attr('showFlag', '0');
   				if($(this).children().find('.queryPanel_title_collapse')){
   					$(this).children().removeClass('queryPanel_title_collapse').addClass('queryPanel_title_expand');
   				}
   			} else {
   				$(this).next().fadeIn('fast');
   				$(this).attr('showFlag', '1');
   				if($(this).children().find('.queryPanel_title_expand')){
   					$(this).children().removeClass('queryPanel_title_expand').addClass('queryPanel_title_collapse');
   				}
   			}
   		});
	});
	
	//display表格行变色
	$(".list tr.even").mouseover(function(){
		$(this).addClass("over");}).mouseout(function(){
		$(this).removeClass("over");});
	$(".list tr.even").addClass("even");
	
	$(".list tr.odd").mouseover(function(){
		$(this).addClass("over");}).mouseout(function(){
		$(this).removeClass("over");});
	$(".list tr.odd").addClass("odd");
	
});

//自定义单选
function signSel(selVal, src, id){
 	$('#'+id).val(selVal);
 	$(src).parent().parent().find('dd').each(function(){
 		$(this).removeClass('iselected').addClass('iselect');
 	});
 	
 	$(src).parent().removeClass('iselect').addClass('iselected');
}

// 禁用自定义按钮
function disable(src){
	var btn = $(src);
	btn.attr('func', btn.attr('onclick'));	// 记住原始绑定函数信息
	//btn.attr('onclick', 'return false;');	// 将原始绑定函数解除
	btn.attr('class', 'ovalBtn_disable');	// 改变按钮样式
	btn.get(0).onclick = null;
}

// 启用自定义按钮
function enable(src){
	var btn = $(src);
	btn.attr('class', 'ovalbutton');	// 恢复原来的样式
	var fun = "var oldFn = function(){"+btn.attr('func')+"}";	// 定义新的函数
	eval(fun);	// 执行函数
	btn.get(0).onclick = eval('oldFn');
}

/**
 *@description: 替换所有匹配项
 *@param: 
 *		str 要替换的字符串
 *		oldCode 要替换字符串中的字符
 *		newCode 替换后的字符
 *		g 执行全局匹配（查找所有匹配而非在找到第一个匹配后停止）。
 *		m 执行多行匹配。
 *		i 执行对大小写不敏感的匹配。
 */
function replaceAll(str,oldCode,newCode){
    return (str.replace(new RegExp(oldCode,"gm"),newCode));
}

//IE下console.log()报错处理，主要针对高德地图中测距问题
if(!window.console){
	window.console={}
	window.console.log=function(){return;}
}

//页面等待操作显示
Loading={
  loading_app:admin_app_root,
  title:'页面正在加载中，请稍候...',
  //title加载时显示的等待提示标题默认为：页面正在加载中，请稍候...
  //app当前项目的名称默认为：/main
  //opacityVal_层的透明度
  showLoad:function(title_,app_,fun,opacityVal_){
  	this.title = title_==null?this.title:title_;
  	this.app = app_==null?this.app:app_;
    var c_h = $(document).height()-4;
    var bodyw = $(document).width();
    var img_div_h = ($(window).height()==0 ? $(document).height() : $(window).height())/2;
    var img_div = 260;
    this.opacityVal = opacityVal_==null?0.3:opacityVal_/100;
    this.opacityVal2 = opacityVal_==null?30:opacityVal_;
    var html = '<div id="_add_loading_">'+
          '<div id="_loading_">'+
           '    <iframe style="top:0px;left:0px;width: 100%;height:'+c_h+'px;position: absolute;filter:alpha(opacity=50);-moz-opacity:0.5;opacity:0.5;" id="_iframe_load_"'+
            '     frameborder="0" scrolling="no"></iframe>'+
            ' <div id="_Outprogressbar_" style="position: fixed; width: 100%; height:'+c_h+'px;'+
             '    left: 0px; top: 0px; background-color: #D8D8D8; z-index: 5000;filter:alpha(opacity='+this.opacityVal2+');-moz-opacity:'+this.opacityVal2+';opacity:'+this.opacityVal2+';">'+
            ' </div>'+
            ' <div id="_img_" style="position: absolute;text-align: center;width:'+img_div+'px;left: '+(bodyw-img_div)/2+'px;top:'+img_div_h+'px;'+
            '    margin-bottom: 5px;z-index: 9999999999999">'+
            '   <div style="margin-bottom: 10px;margin-top: 10px;">'+
            '     <img id="_loadImg_" style="margin-bottom: 5px;" src="'+this.loading_app+'/images/loading/loading1.gif"></img><br />'+
          '       <span id="_title_" style="font-weight: bold;color: black;" >'+this.title+'</span>'+
          '     </div>'+
          '   </div>'+
          '</div>'+
         '</div>';
        window.setTimeout(function(){
  			$('body').append(html);
		    if(typeof(fun)=='function'){
		    	fun();
		    }
  		},20);
  		
    
  },
  //完成关闭加载框
  //img:1 默认加载成功的图片提示
  //img:0 默认加载失败的图片提示
  //img:null 无任何提示操作，加载完成后加载框直接去除
  //img:当不是1、0、null的时候可以是任何图片路径值
  //title:加载完成后的提示语句
  //fun:带入的函数操作 
  finish:function(img,title,fun){
  	if(img==null){
  		if(typeof(fun)=='function'){
  			fun();
  		}
  		$('#_add_loading_').remove();
  	}else if(img=='1'){//加载成功图片显示
  		img = this.app+'/images/success.png';
  		title = title==null?"":title;
  		$('#_title_').css('color','blue');
  		this.finish_com(img,title,fun);
  	}else if(img=='0'){//加载成功错误信息提示图片显示
  		img = this.app+'/images/warning.png';
  		title = title==null?"":title;
  		$('#_title_').css('color','red');
  		this.finish_com(img,title,fun);
  	}else{//其他图片自定义
  		title = title==null?"":title;
  		this.finish_com(img,title,fun);
  	}
  },
  finish_com:function(img,title,fun){
  	if(img!='2'){
  		$('#_loadImg_').attr('src',img);
  	} else {
  		$('#_loadImg_').remove();
  	}
  	title = title==null?"":title;
  	$('#_title_').text(title);
  	window.setTimeout(function(){
  		$('#_add_loading_').fadeOut(500,function(){
  			$(this).remove();
  			if(typeof(fun)=='function'){
		    	fun();
		    }
  		});
  	},500);
  }
};
//图片按比例缩放
zoomImg = {
	v:{zoomHeight:0,zoomWidth:0,imgId:''},
	height:0,//图片高度
	width:0,//图片宽度
	bl:0,//占比
	zoom:function(params){
		var obj = this;
		for(s in params){this.v[s]=params[s]}
		var img = $('#'+this.v.imgId);//图片对象
		var imge = new Image();//图片对象
		//加载成功
		imge.onload=function(){
			obj.height = imge.height;
			obj.width = imge.width;
			obj.bl = parseFloat(obj.height/obj.width);
			//递归重新定位获取图片的size
			obj.checkSize(obj);
			//重新赋值图片大小
			img.height(obj.height);
			img.width(obj.width);
			params.height=obj.height;
			params.width=obj.width;
		};
		//加载失败
		imge.onerror=function(){
			//alert('图片加载失败,请确认是否存在图片！');
			$(obj.tmask).remove();
			$(obj.tinner).remove();
		};
		imge.src = img.attr('src');
	},
	//递归获取图片的size
	checkSize:function(obj){
		if(obj.height > obj.v.zoomHeight){
			obj.height = obj.v.zoomHeight;
			obj.width = parseInt(obj.height/obj.bl);
		}else if(obj.width > obj.v.zoomWidth){
			obj.width = obj.v.zoomWidth;
			obj.height = parseInt(obj.width*obj.bl);
		}
		if(obj.height <= obj.v.zoomHeight && obj.width <= obj.v.zoomWidth){
			return false;
		}else{
			obj.checkSize(obj);
		}
	}
}

//锁定table中的列同时锁定table的表头
//TableID table的id
//FixColumnNumber 需要锁定多少列
function FixTable(TableID, FixColumnNumber) {
	var width = $(window).width();
	var height = $(window).height();
	if ($("#" + TableID + "_tableLayout").length != 0) {
	$("#" + TableID + "_tableLayout").before($("#" + TableID));
	$("#" + TableID + "_tableLayout").empty();
	}
	else {
	$("#" + TableID).after("<div id='" + TableID + "_tableLayout' style='overflow:hidden;height:" + height + "px; width:" + width + "px;'></div>");
	}
	$('<div id="' + TableID + '_tableFix"></div>'
	+ '<div id="' + TableID + '_tableHead"></div>'
	+ '<div id="' + TableID + '_tableColumn"></div>'
	+ '<div id="' + TableID + '_tableData"></div>').appendTo("#" + TableID + "_tableLayout");
	var oldtable = $("#" + TableID);
	var tableFixClone = oldtable.clone(true);
	tableFixClone.attr("id", TableID + "_tableFixClone");
	$("#" + TableID + "_tableFix").append(tableFixClone);
	var tableHeadClone = oldtable.clone(true);
	tableHeadClone.attr("id", TableID + "_tableHeadClone");
	$("#" + TableID + "_tableHead").append(tableHeadClone);
	var tableColumnClone = oldtable.clone(true);
	tableColumnClone.attr("id", TableID + "_tableColumnClone");
	$("#" + TableID + "_tableColumn").append(tableColumnClone);
	$("#" + TableID + "_tableData").append(oldtable);
	$("#" + TableID + "_tableLayout table").each(function () {
	$(this).css("margin", "0");
	});
	var HeadHeight = $("#" + TableID + "_tableHead thead").height();
	HeadHeight += 2;
	$("#" + TableID + "_tableHead").css("height", HeadHeight);
	$("#" + TableID + "_tableFix").css("height", HeadHeight);
	var ColumnsWidth = 0;
	var ColumnsNumber = 0;
	$("#" + TableID + "_tableColumn tr:last td:lt(" + FixColumnNumber + ")").each(function () {
	ColumnsWidth += $(this).outerWidth(true);
	ColumnsNumber++;
	});
	ColumnsWidth += 2;
	if ($.browser.msie) {
	switch ($.browser.version) {
	case "7.0":
	if (ColumnsNumber >= 3) ColumnsWidth--;
	break;
	case "8.0":
	if (ColumnsNumber >= 2) ColumnsWidth--;
		break;
	}
	}
	$("#" + TableID + "_tableColumn").css("width", ColumnsWidth);
	$("#" + TableID + "_tableFix").css("width", ColumnsWidth);
	$("#" + TableID + "_tableData").scroll(function () {
	$("#" + TableID + "_tableHead").scrollLeft($("#" + TableID + "_tableData").scrollLeft());
	$("#" + TableID + "_tableColumn").scrollTop($("#" + TableID + "_tableData").scrollTop());
	});
	$("#" + TableID + "_tableFix").css({ "overflow": "hidden", "position": "relative", "z-index": "50", "background-color": "Silver" });
	$("#" + TableID + "_tableHead").css({ "overflow": "hidden", "width": width - 17, "position": "relative", "z-index": "45", "background-color": "Silver" });
	$("#" + TableID + "_tableColumn").css({ "overflow": "hidden", "height": height - 17, "position": "relative", "z-index": "40", "background-color": "Silver" });
	$("#" + TableID + "_tableData").css({ "overflow": "auto", "width": width, "height": height, "position": "relative", "z-index": "35" });
	if ($("#" + TableID + "_tableHead").width() > $("#" + TableID + "_tableFix table").width()) {
	$("#" + TableID + "_tableHead").css("width", $("#" + TableID + "_tableFix table").width());
	$("#" + TableID + "_tableData").css("width", $("#" + TableID + "_tableFix table").width() + 17);
	
	}
	if ($("#" + TableID + "_tableColumn").height() > $("#" + TableID + "_tableColumn table").height()) {
	$("#" + TableID + "_tableColumn").css("height", $("#" + TableID + "_tableColumn table").height());
	$("#" + TableID + "_tableData").css("height", height);
	}
	$("#" + TableID + "_tableFix").offset($("#" + TableID + "_tableLayout").offset());
	$("#" + TableID + "_tableHead").offset($("#" + TableID + "_tableLayout").offset());
	$("#" + TableID + "_tableColumn").offset($("#" + TableID + "_tableLayout").offset());
	$("#" + TableID + "_tableData").offset($("#" + TableID + "_tableLayout").offset());
}

//display翻页加载进度条
function displayTagLoading(){
	Loading.showLoad('正在加载中,请稍候...','',function(){});	
}

//记录导出日志，以及导出按钮禁用，启用操作
exportBtn={

	/*
	*功能：记录自定义导出日志,并禁用按钮
	*exportBtnId:导出按钮的ID编号
	*app：当前项目的名称,如：${module_prefix.SXXD}
	*remark：备注，如：上行下达所有工单导出
	*module_id：菜单编号
	*request_url请求的URL地址
	*condition 查询条件 如果是表单的，则可以序列化：$("#form1").serialize()，如果没有查询条件可以为空
	*/
	customExportLog:function(exportBtnId,app,remark,module_id,request_url,condition){
		var intervalTimeVal;
		 	//禁用按钮
		disable(document.getElementById(exportBtnId));
		if(condition!=''){
			//由于‘&’符号为特殊符号在url中为分割符所以在传值的时候转换一下
			condition=condition.replace(/\&/g,",");
		}
		var v_url = app+"/commondata/recordInfoExportLog.do?export_type=2";
		/**if(v_url.indexOf('?') == -1){
			v_url = v_url + '?csrfToken='+$('#csrfToken').val();
		} else {
			v_url = v_url + '&csrfToken='+$('#csrfToken').val();
		}*/
		var data = "remark="+remark+"&module_id="+module_id+
				   "&request_url="+request_url+'&condition='+condition;
		var v_log_id;//日志编号
		$.ajax({
			async:false,
		    type: "POST",
		    url: v_url,
		    data: data,
		    success: function(result){
		    	v_log_id =  result;
		    	//先把之前的定时器清掉然后再定义
		    	clearTimeout(intervalTimeVal);
		    	//定义定时器，每隔一秒请求查看导出状态
		    	intervalTimeVal=setInterval(function(){
			  		var v_url2 = app+"/commondata/getExportLogStatus.do?log_id="+v_log_id;
					/*if(v_url2.indexOf('?') == -1){
						v_url2 = v_url2 + '?csrfToken='+$('#csrfToken').val();
					} else {
						v_url2 = v_url2 + '&csrfToken='+$('#csrfToken').val();
					}*/
					var data2 = "";
					$.ajax({
					    type: "POST",
					    url: v_url2,
					    data: data2, 
					    success: function(result){
					    	if(result>=1){
					    		//如果状态为1，则表示已导出，启用按钮，然后清掉定时器
					    		enable(document.getElementById(exportBtnId));
					    		clearTimeout(intervalTimeVal);
					    	}
					    }
				 	});
			  	},1000);
		    }
		});
		return v_log_id;
	},
	
	/*
	*功能：记录自定义导出日志
	*exportBtnId:导出按钮的ID编号
	*app：当前项目的名称,如：${module_prefix.SXXD}
	*remark：备注，如：上行下达所有工单导出
	*module_id：菜单编号
	*request_url请求的URL地址
	*condition 查询条件 如果是表单的，则可以序列化：$("#form1").serialize()，如果没有查询条件可以为空
	*/
	customExportLog2:function(exportBtnId,app,remark,module_id,request_url,condition){
		var intervalTimeVal;
		 	//禁用按钮
		//disable(document.getElementById(exportBtnId));
		if(condition!=''){
			//由于‘&’符号为特殊符号在url中为分割符所以在传值的时候转换一下
			condition=condition.replace(/\&/g,",");
		}
		var v_url = app+"/commondata/recordInfoExportLog.do?export_type=2";
		/**if(v_url.indexOf('?') == -1){
			v_url = v_url + '?csrfToken='+$('#csrfToken').val();
		} else {
			v_url = v_url + '&csrfToken='+$('#csrfToken').val();
		}*/
		var data = "remark="+remark+"&module_id="+module_id+
				   "&request_url="+request_url+'&condition='+condition;
		var v_log_id;//日志编号
		$.ajax({
			async:false,
		    type: "POST",
		    url: v_url,
		    data: data,
		    success: function(result){
		    	v_log_id =  result;
		    	//先把之前的定时器清掉然后再定义
		    	clearTimeout(intervalTimeVal);
		    	//定义定时器，每隔一秒请求查看导出状态
		    	intervalTimeVal=setInterval(function(){
			  		var v_url2 = app+"/commondata/getExportLogStatus.do?log_id="+v_log_id;
					/*if(v_url2.indexOf('?') == -1){
						v_url2 = v_url2 + '?csrfToken='+$('#csrfToken').val();
					} else {
						v_url2 = v_url2 + '&csrfToken='+$('#csrfToken').val();
					}*/
					var data2 = "";
					$.ajax({
					    type: "POST",
					    url: v_url2,
					    data: data2, 
					    success: function(result){
					    	if(result>=1){
					    		//如果状态为1，则表示已导出，启用按钮，然后清掉定时器
					    		enable(document.getElementById(exportBtnId));
					    		clearTimeout(intervalTimeVal);
					    	}
					    }
				 	});
			  	},1000);
		    }
		});
		return v_log_id;
	},
  
	/*
	*功能：记录display自带导出日志
	*app：当前项目的名称,如：${module_prefix.SXXD}
	*remark：备注，如：上行下达所有工单导出
	*module_id：菜单编号
	*/
	displayExportLog:function(app,remark,module_id,request_url,condition){
		$("span[class='export csv']").click(function(){
			var url_href = $("div[class='exportlinks']").find("a").attr('href');//获取display导出按钮的链接地址
			var arrParam=url_href.split("?d-16544-e=1");//根据关键字分隔链接地址
			var request_url = arrParam[0]; //请求的URL地址
			condition = arrParam[1]; //查询条件 如果是表单的，则可以序列化：$("#form1").serialize()，如果没有查询条件可以为空
			if(condition!=''){
				//由于‘&’符号为特殊符号在url中为分割符所以在传值的时候转换一下
				condition=condition.replace(/\&/g,",").substring(2);
			}
			
			var v_url = app+"/commondata/recordInfoExportLog.do?export_type=1";
			/*if(v_url.indexOf('?') == -1){
				v_url = v_url + '?csrfToken='+$('#csrfToken').val();
			} else {
				v_url = v_url + '&csrfToken='+$('#csrfToken').val();
			}*/
			var data = "remark="+remark+"&module_id="+module_id+
			       "&request_url="+request_url+'&condition='+condition;
			$.ajax({
			    type: "POST",
			    url: v_url,
			    data: data,
			    success: function(result){
			    }
			});
		});
	}
  
};

/**
 * 校验文件后缀名，不运行文件后缀名是以下几种
 * txt、csv、dbf、mht、rar、zip、7z、xls、xlsx、doc、docx、ppt、pptx、jpg、jpeg、gif、png、bmp、pdf、flv、mkv、rmvb、3gp、wmv、mov、avi、mpg
 **/
function chkFileSuffix(fileName){
	var fileReg = /^.+(.txt|.csv|.dbf|.mht|.rar|.zip|.7z|.xls|.xlsx|.doc|.docx|.ppt|.pptx|.jpg|.jpeg|.gif|.png|.bmp|.pdf|.flv|.mkv|.rmvb|.3gp|.wmv|.mov|.avi|.mpg)$/i;
	return fileReg.test(fileName);
}

// 导出table到excel
function exportHtmlToExcel(id, fileName, app){
	// 
	var expForm = $('#expForm');
	if(expForm.length == 0) {
		expForm = $('<form id="expForm" method="post"></form>');
		expForm.attr("action", app+"/commondata/exportHtmlToExcel.do?csrfToken="+$('#csrfToken').val());
		expForm.append('<input type="hidden" id="expFileName" name="fileName" />')
		expForm.append('<input type="hidden" id="expHtmlTable" name="htmlTable" />');
		expForm.appendTo("body");
	}
	var table = $("#" + id);
	var tfoot = table.find('tfoot');
	if(tfoot.length > 0) {
		table.find('tbody:last').after(tfoot);
	}
	var htmlTable = table.html().replace(/<\/*(a|img|input).*?>/gi, '');
	$('#expHtmlTable').val(htmlTable);
	$('#expFileName').val(fileName);
	expForm.submit();
}