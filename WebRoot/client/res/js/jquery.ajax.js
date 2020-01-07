//AJAX获取数据-同步
MyJqueryAjax = function (v_url, data, func, dataType) {
	this.url = v_url;
	this.data = data;
	this.func = func;
	this.request = function () {
		var v_response;
		$.ajax({async:false, url:v_url, data: data, cache:false, type:"GET", dataType:dataType == null ? "text" : dataType, error:function (XMLHttpRequest, textStatus, errorThrown) {
			alert("请求数据异常!");
		}, success:(func !== null && func != undefined) ? func : function (req) {
			v_response = req;
		}});
		return v_response;
	};
};

//AJAX获取数据-异步
MyJqueryAjaxAsync = function (v_url, data, func, dataType) {
	this.url = v_url;
	this.data = data;
	this.func = func;
	this.request = function () {
		var v_response;
		$.ajax({async:true, url:v_url, data: data, cache:false, type:"GET", dataType:dataType == null ? "text" : dataType, error:function (XMLHttpRequest, textStatus, errorThrown) {
			alert("请求数据异常");
		}, success:(func !== null && func != undefined) ? func : function (req) {
			v_response = req;
		}});
		return v_response;
	};
};