
MyJqueryAjax = function (v_url, data, func, dataType) {
	this.url = v_url;
	this.data = data;
	this.func = func;
	this.request = function () {
		var v_response;
		$.ajax({async:false, url:v_url, data: data, cache:false, type:"POST", dataType:dataType == null ? "text" : dataType, error:function (XMLHttpRequest, textStatus, errorThrown) {
			alert("MyJqueryAjax Request Error!");
		}, success:(func !== null && func != undefined) ? func : function (req) {
			v_response = req;
		}});
		return v_response;
	};
};

MyJqueryAjax2 = function (v_url, data, func, dataType,errorFunc) {
	this.url = v_url;
	this.data = data;
	this.func = func;
	this.request = function () {
		var v_response;
		$.ajax({async:true, url:v_url, data: data, cache:false, type:"POST", dataType:dataType == null ? "text" : dataType, 
		error: (errorFunc !== null && errorFunc != undefined) ? errorFunc: function (req) {
			alert("调用失败!");
		}, success:(func !== null && func != undefined) ? func: function (req) {
			v_response = req;
		}});
		
	};
};

MyAjax = function (url, params, func, asyn) {
	this.url = url;
	this.params = params;
	this.func = func;
	this.responseText = "";
	this.request = function () {
		new Ajax.Request(url, {method:"post", parameters:params, onSuccess:(func != null && func != undefined) ? func : function (req) {
			this.responseText = req.responseText;
		}, onFailure:function () {
			alert("MyAjax Request Error!");
		}, asynchronous:asyn || false});
		return this.responseText;
	};
};

