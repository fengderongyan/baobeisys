MyJqueryAjax = function (v_url, data, func, dataType) {
	
	this.url = v_url;
	this.data = data;
	this.func = func;
	this.request = function () {
		var v_response;
		$.ajax({
			async:false, //同步请求
			url:v_url, //请求地址
			//contentType:"application/x-www-form-urlencoded;charset=UTF-8",
			data: data, //参数
			cache:false, //设置为 false 将不会从浏览器缓存中加载请求信息
			type:"POST", 
			dataType:dataType == null ? "text" : dataType, 
			error:function (XMLHttpRequest, textStatus, errorThrown) {
				alert("后台服务请求失败！");
			}, 
			success:(func !== null && func != undefined) ? func : function (req) {
				v_response = req;
			}
		});
		return v_response;
	};
};

MyJqueryAjax2 = function (v_url, data, func, dataType,errorFunc) {
	if(v_url.indexOf('?') == -1){
		v_url = v_url + '?csrfToken='+$('#csrfToken').val();
	} else {
		v_url = v_url + '&csrfToken='+$('#csrfToken').val();
	}
	this.url = v_url;
	this.data = data;
	this.func = func;
	this.request = function () {
		var v_response;
		$.ajax({async:true, url:v_url, data: data, cache:false, type:"POST", dataType:dataType == null ? "text" : dataType, 
		error: (errorFunc !== null && errorFunc != undefined) ? errorFunc: function (req) {
			alert("后台服务请求失败！");
		}, success:(func !== null && func != undefined) ? func: function (req) {
			v_response = req;
		}});
		
	};
};


