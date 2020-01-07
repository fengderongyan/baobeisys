if(typeof sgy == 'undefined') {
	var sgy = new Object();
}

//IOS使JS和客户端交互使用
function execute(url){
	try{
		var iframe = document.createElement("IFRAME");
		iframe.setAttribute("src", url);
		document.documentElement.appendChild(iframe);
		iframe.parentNode.removeChild(iframe);
		iframe = null;
	} catch(e){
   		
   	}
}

var xxx='';

sgy.phone = {
	clientType: /android/i.test(navigator.userAgent) ? 'ANDROID' : (/ipad|iphone|mac/i.test(navigator.userAgent) ? 'IOS' : 'PC'),
	showAlert: function (msg) {//alert消息框
		if(this.clientType == 'ANDROID'){
			window.client.showAlert(msg);
		} else if(this.clientType == 'IOS'){
			var url="hgponios::showAlert::"+msg; 
			execute(url);
		} else {
			
		}
	},
	showProgress: function (msg) {//显示加载等待框
		if(this.clientType == 'ANDROID'){
			window.client.showProgress(msg);
		} else if(this.clientType == 'IOS'){
			var url="sysios::showProgress::"+msg; 
			execute(url);
		} else {
			
		}
	},
	closeProgress: function () {//关闭加载等待框
		if(this.clientType == 'ANDROID'){
			window.client.closeProgress();
		} else if(this.clientType == 'IOS'){
			var url="sysios::closeProgress"; 
			execute(url);
		} else {
			
		}
	},
	showShortMessage: function (msg) {//显示短消息
		if(this.clientType == 'ANDROID'){
			window.client.showShortMessage(msg);
		} else if(this.clientType == 'IOS'){
			var url="sysios::showShortMessage::"+msg; 
			execute(url);
		} else {
			
		}
	},
	showLongMessage: function (msg) {//显示短消息(指定时间长度)
		if(this.clientType == 'ANDROID'){
			window.client.showLongMessage(msg);
		} else if(this.clientType == 'IOS'){
			var url="sysios::showMessage::"+msg; 
			execute(url);
		} else {
			
		}
	},
	openWebKit: function (url, title, titleFlag, screenType, param, hasRightBtn, rightIcon, rightJs, hasRightBtn1, rightIcon1, rightJs1) {//打开webkit
		if(this.clientType == 'ANDROID'){
			window.client.openWebKit(url, title, titleFlag, screenType, param, hasRightBtn, rightIcon, rightJs, hasRightBtn1, rightIcon1, rightJs1);
		} else if(this.clientType == 'IOS'){
			var url="sysios::openWebKit::"+url+"::"+title+"::"+titleFlag+"::"+screenType+"::"+param+"::"+hasRightBtn+"::"+rightIcon+"::"+rightJs+"::"+hasRightBtn1+"::"+rightIcon1+"::"+rightJs1 
			execute(url);
		} else {
			
		}
	},
	closeWebView: function () {//关闭webkit
		if(this.clientType == 'ANDROID'){
			window.client.closeWebView();
		} else if(this.clientType == 'IOS'){
			var url="sysios::closeWebView"; 
			execute(url)
		} else {
			
		}
	},
	closeWebViewRef: function (className) {//关闭webkit并刷新父类
		if(this.clientType == 'ANDROID'){
			window.client.closeWebViewRef(className);
		} else if(this.clientType == 'IOS'){
			var url="sysios::closeWebViewRef::"+className; 
			execute(url)
		} else {
			
		}
	},
	closeCallBack: function (jsStr) {//关闭webkit并执行父页面的js方法
		if(this.clientType == 'ANDROID'){
			window.client.closeCallBack(jsStr);
		} else if(this.clientType == 'IOS'){
			var url="sysios::closeCallBack::"+jsStr; 
			execute(url);
		} else {
			
		}
	},
	getLastLocation: function () {//获取地址经纬度json字符串
		if(this.clientType == 'ANDROID'){
			return window.client.getLastLocation();
		} else if(this.clientType == 'IOS'){
			xxx=''
			var url="sysios::getLastLocation"; 
			execute(url)
			return xxx;
		} else {
			
		}
	},
	getLocation: function (jsStr) {//获取地址经纬度json字符串
		if(this.clientType == 'ANDROID'){
			window.client.getLocation(jsStr);
		} else if(this.clientType == 'IOS'){
			var url="sysios::getLocation::"+jsStr; 
			execute(url)
		} else {
			
		}
	},
	getSysInfo: function (key) {//获取系统信息json字符串
		if(this.clientType == 'ANDROID'){
			return window.client.getSysInfo(key);
		} else if(this.clientType == 'IOS'){
			xxx=''
			var url="sysios::getSysInfo::"+key; 
			execute(url)
			return xxx;
		} else {
			
		}
	},
	getUserInfo: function (key) {//获取用户信息json字符串
		if(this.clientType == 'ANDROID'){
			return window.client.getUserInfo(key);
		} else if(this.clientType == 'IOS'){
			xxx='';
			var url="sysios::getUserInfo::"+key; 
		    execute(url)
		    return xxx;
		} else {
			
		}
	},
	showSelectDlg: function(data, textId, valueId, textKey, valueKey, hasSearch){//显示下拉框
		if(this.clientType == 'ANDROID'){
			window.client.showSelectDlg(data, textId, valueId, textKey, valueKey, hasSearch);
		} else if(this.clientType == 'IOS'){
			var url="sysios::showSelectDlg::"+data.replace(/\'/gi,'\"')+"::"+textId+"::"+valueId+"::"+textKey+"::"+valueKey+"::"+hasSearch; 
		    execute(url)
		} else {
			
		}
	},
	showDateDlg: function(type, domId, defaultValue){//显示日期框
		if(this.clientType == 'ANDROID'){
			window.client.showDateDlg(type, domId, defaultValue);
		} else if(this.clientType == 'IOS'){
			var url="sysios::showDateDlg::"+type+"::"+domId+"::"+defaultValue; 
		    execute(url)
		} else {
			
		}
	},
	openCamera: function(fileNameShowDom, filePathDom, fileIdDom, fileNameDom,jsScript){//打开照相机拍照
		if(this.clientType == 'ANDROID'){
			window.client.openCamera(fileNameShowDom, filePathDom, fileIdDom, fileNameDom,jsScript);
		} else if(this.clientType == 'IOS'){
			var url="sysios::openCamera::"+fileNameShowDom+"::"+filePathDom+"::"+fileIdDom+"::"+fileNameDom+"::"+jsScript; 
		    execute(url)
		} else {
			
		}
	},
	openFileSelect: function(fileType, fileNameShowDom, filePathDom, fileIdDom, fileNameDom,jsScript){//打开照相机拍照
		if(this.clientType == 'ANDROID'){
			window.client.openFileSelect(fileType, fileNameShowDom, filePathDom, fileIdDom, fileNameDom,jsScript);
		} else if(this.clientType == 'IOS'){
			var url="sysios::openFileSelect::"+fileNameShowDom+"::"+filePathDom+"::"+fileIdDom+"::"+fileNameDom+"::"+fileType+"::"+jsScript; 
		    execute(url)
		} else {
			
		}
	},
    submitFileForm: function(formDomId, filePaths, fileIds, fileSavePath,moduleName,tableName, linkBsusinessId){//提交拍照文件表单
		if(this.clientType == 'ANDROID'){
			window.client.submitFormWithMultiFiles(formDomId, filePaths, fileIds, fileSavePath,moduleName,tableName, linkBsusinessId);
		} else if(this.clientType == 'IOS'){
			var url="sysios::submitFormWithMultiFiles::"+formDomId+"::"+filePaths+"::"+fileIds+"::"+fileSavePath+"::"+moduleName+"::"+tableName+"::"+linkBsusinessId; 
		    execute(url)
		} else {
			
		}
	},
	previewServerPhoto: function(photoPath){//服务器图片预览
		if(this.clientType == 'ANDROID'){
			window.client.previewServerPhoto(photoPath);
		} else if(this.clientType == 'IOS'){
			var url="sysios::previewServerPhoto::"+photoPath; 
		    execute(url)
		} else {
			
		}
	},
	previewClientPhoto: function(photoPath){//本地图片预览
		if(this.clientType == 'ANDROID'){
			window.client.previewClientPhoto(photoPath);
		} else if(this.clientType == 'IOS'){
			var url="sysios::previewClientPhoto::"+photoPath; 
		    execute(url)
		} else {
			
		}
	},
	initCookie: function(){//COOKIE设置
		if(this.clientType == 'ANDROID'){
		
		} else if(this.clientType == 'IOS'){
		    document.cookie ='httpServerPort='+sgy.phone.getSysInfo('httpServerPort')+';path=/'
			document.cookie ='client_type=IOS;path=/';
			document.cookie ='SYSBASE='+sgy.phone.getSysInfo('SYSBASE')+';path=/';
			document.cookie ='mobileModel='+sgy.phone.getSysInfo('mobileModel')+';path=/';
			document.cookie ='versionName='+sgy.phone.getSysInfo('versionName')+';path=/';
			document.cookie ='versionIoS='+sgy.phone.getSysInfo('versionIoS')+';path=/';
			document.cookie ='ORG_ID='+sgy.phone.getSysInfo('ORG_ID')+';path=/';
			document.cookie ='POST_ID='+sgy.phone.getSysInfo('POST_ID')+';path=/'
			document.cookie ='aaa=wwe;path=/'
		} else {
			
		}
	},
	photoFromAlbum: function(fileNameShowDom, filePathDom, fileIdDom, fileNameDom, mediaType){//选择相册图片
		if(this.clientType == 'ANDROID'){
			window.client.photoFromAlbum(fileNameShowDom, filePathDom, fileIdDom, fileNameDom ,mediaType);
		} else if(this.clientType == 'IOS'){
			var url="sysios::photoFromAlbum::"+fileNameShowDom+"::"+filePathDom+"::"+fileIdDom+"::"+fileNameDom+"::"+mediaType; 
		    execute(url)
		} else {
			
		}
	},
	showAlertSelDialog: function(data){//弹出对话框，里面有几项供点击选择
		if(this.clientType == 'ANDROID'){
			window.client.showAlertSelDialog(data);
		} else if(this.clientType == 'IOS'){
			var url="sysios::showAlertSelDialog::"+data; 
		    execute(url)
		} else {
			
		}
	},
	openVideo: function(url){//播放视频文件，包括远程地址和本地SD卡视频
		if(this.clientType == 'ANDROID'){
			window.client.openVideo(url);
		} else if(this.clientType == 'IOS'){
		    var url="sysios::openVideoFile::"+url; 
		    execute(url)
		} else {
			
		}
	},
	putSettingValue:function(key,value){//数据库存放全局参数
	    if(this.clientType == 'ANDROID'){
			window.client.putSettingValue(key,value);
		} else if(this.clientType == 'IOS'){
		    var url="sysios::putSettingValue::"+key+"::"+value;
		    execute(url);
		} else {
			
		}
	},
	getSettingValue:function(key,defaultValue){//数据库获取全局参数
	    if(this.clientType == 'ANDROID'){
			return window.client.getSettingValue(key,defaultValue);
		} else if(this.clientType == 'IOS'){
		    xxx='';
			var url="sysios::getSettingValue::"+key+"::"+defaultValue; 
		    execute(url)
		    return xxx;
		} else {
			return "";
		}
	},
	call:function(msisdn){//拨打电话
	    if(this.clientType == 'ANDROID'){
			window.client.call(msisdn);
		} else if(this.clientType == 'IOS'){
		    var url="sysios::call::"+msisdn;
		    execute(url);
		} else {
			
		}
	},
	sms:function(msisdn,msg){//发送短信
	    if(this.clientType == 'ANDROID'){
			window.client.sms(msisdn,msg);
		} else if(this.clientType == 'IOS'){
		    var url="sysios::sms::"+msisdn+"::"+msg;
		    execute(url);
		} else {
			
		}
	},
	showMakers:function(citylon,citylat,json,nav_flag){//地图打点
	    if(this.clientType == 'ANDROID'){
			window.client.showMakers(citylon,citylat,json,nav_flag);
		} else if(this.clientType == 'IOS'){
		    var url="sysios::showMakers::"+citylon+"::"+citylat+"::"+json+"::"+nav_flag;
		    execute(url);
		} else {
			
		}
	},
	mapNavigation:function(startlon,startlat,endlon,endlat){//地图导航 开始经度，开始纬度 为空默认为当前人经纬度
	    if(this.clientType == 'ANDROID'){
			window.client.mapNavigation(startlon,startlat,endlon,endlat);
		} else if(this.clientType == 'IOS'){
		    var url="sysios::mapNavigation::"+startlon+"::"+startlat+"::"+endlon+"::"+endlat;
		    execute(url);
		} else {
			
		}
	},
	linePatrolWorkMap:function(line_id,cfg_id,patrol_weekday,patrol_order,line_color,patrol_line_type){
		if(this.clientType == 'ANDROID'){
			window.client.linePatrolWorkMap(line_id,cfg_id,patrol_weekday,patrol_order,line_color,patrol_line_type);
		} else if(this.clientType == 'IOS'){
		
		} else {
			
		}
	},//基础设施显示面
	showArea:function showArea(id,eqp_id){
		if(this.clientType == 'ANDROID'){
			window.client.showArea(id,eqp_id);
		} else if(this.clientType == 'IOS'){
			
		} else {
			
		}
	},//注册广播
	registerBroadcast:function registerBroadcast(action,functionName){
		if(this.clientType == 'ANDROID'){
			window.client.registerBroadcast(action,functionName);
		} else if(this.clientType == 'IOS'){
			
		} else {
			
		}
	},//发送广播
	sendBroadcast:function sendBroadcast(action,value){
		if(this.clientType == 'ANDROID'){
			window.client.sendBroadcast(action,value);
		} else if(this.clientType == 'IOS'){
			
		} else {
			
		}
	},//删除广播
	unRegisterBroadcast:function unRegisterBroadcast(action,value){
		if(this.clientType == 'ANDROID'){
			window.client.sendBroadcast(action,value);
		} else if(this.clientType == 'IOS'){
			
		} else {
			
		}
	},openSettingPage: function () {
		if(this.clientType == 'ANDROID'){
			window.client.openSettingPage();
		} else if(this.clientType == 'IOS'){
		} else {
			
		}
	},
	scanLihtBox:function(inputId, callBackJs){
		if(this.clientType == 'ANDROID'){
			window.client.scanLihtBox(inputId, callBackJs);
		} 
	}
}

var serverPath = sgy.phone.getSysInfo('serverPath');

function showSchDlg(){
	$('#searchDlg').show();
	$('#ibg').show();
}
function hideSchDlg(){
	$('#searchDlg').hide();
	$('#ibg').hide();
}

function noBarsOnTouchScreen(arg){
	var elem, tx, ty;

	if('ontouchstart' in document.documentElement ) {
		if (elem = document.getElementById(arg)) {
			elem.style.overflow = 'hidden';
			elem.ontouchstart = ts;
			elem.ontouchmove = tm;
		}
	}

	function ts(e) {
		var tch;
		
		if(e.touches.length == 1){
			e.stopPropagation();
			tch = e.touches[0];
			tx = tch.pageX;
			ty = tch.pageY;
		}
	}

function getPhotoUrl(photoPath,photoName){
    return photoPath+"/photo/"+photoName
}
	function tm(e) {
		var tch;
		
		if(e.touches.length == 1){
			e.preventDefault();
			e.stopPropagation();
			tch = e.touches[ 0 ];
			this.scrollTop +=  ty - tch.pageY;
			ty = tch.pageY;
		}
	}
}

function setSelectValue(selectId, selectValue, optionValue, optionLabel){
	var jsonData = eval($('#'+selectId+'_search').attr('data'));
	for(var j=0;j<jsonData.length;j++){
		if(selectValue == jsonData[j][optionValue]){
			$('#'+selectId+'_search').html(jsonData[j][optionLabel]);
			$('#'+selectId).val(jsonData[j][optionValue]);
			break;
		}
	}
}

function replaceNull(oldValue){
	if(oldValue == 'null' || oldValue == null){
		return '';
	}
	return oldValue;
}

$(function(){
	sgy.phone.initCookie();
   	$('.label-title').each(function(){
   		$(this).click(function(){
   		    var httpserverport = sgy.phone.getSysInfo('httpServerPort');
   			if($(this).attr('showFlag') == '1' || $(this).attr('showFlag') == undefined){
   				$(this).next().fadeOut('normal');
   				$(this).attr('showFlag', '0');
   				if($(this).find('img')){
   					$(this).find('img').attr('src', 'http://127.0.0.1:'+httpserverport+'/client/res/images/close.png');
   				}
   			} else {
   				$(this).next().fadeIn('normal');
   				$(this).attr('showFlag', '1');
   				if($(this).find('img')){
   					$(this).find('img').attr('src', 'http://127.0.0.1:'+httpserverport+'/client/res/images/open.png');
   				}
   			}
   		});
	});
	
	$('select').each(function(){
		var selectId = $(this).attr('id');//下拉框的ID值
		var selectName = $(this).attr('name');//下拉框的NAME值
		var selectSearchId = selectId + "_search";//下拉外面的SPAN值ID
		var selectSearchName = selectName + "_search";//下拉外面的SPAN值NAME
		var funChange = $(this).attr('onchange');//onchange方法
		var style = $(this).attr('style');
		var searchFlag = $(this).attr('searchFlag');
		if(searchFlag == 'undefined' || searchFlag == '' || searchFlag == null){
        	searchFlag = 'false';
        }
		
       	var value = $(this).val();//默认选中的值
       	var i = 0;
       	var jsonStr = '';
       	var optionLabel = $(this).attr('optionLabel');
        var optionValue = $(this).attr('optionValue');
        if(optionLabel == 'undefined' || optionLabel == '' || optionLabel == null){
        	optionLabel = 'TEXT';
        }
        if(optionValue == 'undefined' || optionValue == '' || optionValue == null){
        	optionValue = 'VALUE';
        }
		$(this).find('option').each(function(){
			if(i==0){
				jsonStr+="{'"+optionLabel+"':'"+$(this).html()+"','"+optionValue+"':'"+$(this).val()+"'}";
			}else{
				jsonStr+=",{'"+optionLabel+"':'"+$(this).html()+"','"+optionValue+"':'"+$(this).val()+"'}";
			}
			i ++;
		});
		jsonStr = "["+jsonStr+"]";
		$(this).prop('outerHTML', '<span id="'+selectSearchId+'" data="'+jsonStr+'" style="'+style+'" class="client-select" name="'
				+selectSearchName+'">请选择...</span><input type="hidden" name="'+selectName+'" id="'+selectId+'" onchange="'+funChange+'" >');
				
		var jsonData = eval(jsonStr);
		for(var j=0;j<jsonData.length;j++){
			if(value == jsonData[j][optionValue]){
				$('#'+selectSearchId).html(jsonData[j][optionLabel]);
				$('#'+selectId).val(jsonData[j][optionValue]);
				break;
			}
		}
		
		$('#'+selectSearchId).click(function(){
			if(eval($(this).attr('data')).length > 0){
				sgy.phone.showSelectDlg($(this).attr('data'), selectSearchId, selectId, optionLabel, optionValue, searchFlag);
			}
		});
		
	});
})


//自定义单选
function signSel(selVal, src, id){
 	$('#'+id).val(selVal);
 	$(src).parent().parent().find('dd').each(function(){
 		$(this).removeClass('iselected').addClass('iselect');
 	});
 	
 	$(src).parent().removeClass('iselect').addClass('iselected');
}

/** 
 * 截取字符串长度 两个字符为1个汉字
 * @param str：需要截取的字符串 
 * @param len: 需要截取的长度 
 */
function subStrMethod(str,len){
	var str_length = 0;
    var str_len = 0;
    var str_total_len = 0;
    str_cut = new String();
    try {
    	str_len = str.length;
    } catch(e){};
    
    for (var i = 0; i < str_len; i++) {
        a = str.charAt(i);
        str_total_len++;
        if (escape(a).length > 4) {
            //中文字符的长度经编码之后大于4  
            str_total_len++;
        }
    }
    for (var i = 0; i < str_len; i++) {
        a = str.charAt(i);
        str_length++;
        if (escape(a).length > 4) {
            //中文字符的长度经编码之后大于4  
            str_length++;
        }
        str_cut = str_cut.concat(a);
        if (str_length >= len) {
        	if(str_total_len != len){
        		str_cut = str_cut.concat("...");
        	}
            return str_cut;
        }
    }
    //如果给定字符串小于指定长度，则返回源字符串；  
    if (str_length < len) {
        return str;
    }
}

/**
*从手机的底部弹出文本输入框
*id   绑定事件的ID
*placeholder 文本提示
*control 保存后，文本内容要要存放的容器ID
*callBackFun 回调函数名
*/
function toBottonControl(id,placeholder,control_id,callBackFun){
	var textContent = $('#'+control_id).val();
	var htmlStr ='<div id="botton_control" class="botton-div">'+
				'	<h3 class="botton-div-title">'+
				'	  	<table style="width:100%;border: 0px;" >'+
				'	  	<tr><td class="close_botton_div">取消</td><td class="save_botton_div">保存</td></tr>'+
				'	  	</table>'+
				'	</h3>'+
				'	<div class="botton-div-sns">'+
				'  		<textarea placeholder="'+placeholder+'"  maxlength="150" id="_init_content" name="_init_content" '+
				'			style="border: 0px;width: 98%;height: 100px;background-color: #E8E8E8;" >'+textContent+'</textarea>'+
				'	</div>'+
				'</div>';
	
	$('body').append(htmlStr);
	
	$(".botton-div").addClass("botton-active");	
	if($(".bottonbg").length>0){
		$(".bottonbg").addClass("bottonbg-active");
	}else{
		$("body").append('<div class="bottonbg"></div>');
		$(".bottonbg").addClass("bottonbg-active");
	}
	
	//点击保存后，关闭层，并且插入到文本框和容器中
	$(".save_botton_div").click(function(){
		var _init_content = $('#_init_content').val();
		$('#'+id).html(subStrMethod(_init_content,10));
		$('#'+control_id).val(_init_content);
		$(".botton-div").removeClass("botton-active");	
		setTimeout(function(){
			$(".bottonbg-active").removeClass("bottonbg-active");	
			$(".bottonbg").remove();	
			$('#botton_control').remove();
		},300);
		
		if(callBackFun!=''){
			eval(callBackFun);
		}
		
		
	})
	
	//点击背景层或者点击“取消”，关闭层(.bottonbg-active点击背景层先去掉，用的时候再加)
	$(".close_botton_div").click(function(){
		$(".botton-div").removeClass("botton-active");	
		setTimeout(function(){
			$(".bottonbg-active").removeClass("bottonbg-active");	
			$(".bottonbg").remove();	
			$('#botton_control').remove();
		},300);
	})
}
