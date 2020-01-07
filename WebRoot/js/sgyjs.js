/*****公共js工具类****/

var sgyjs = {
		isIE : function() {
			var userAgent = navigator.userAgent; // 取得浏览器的userAgent字符串
			var isIE = userAgent.indexOf("compatible") > -1
					&& userAgent.indexOf("MSIE") > -1; // 判断是否IE<11浏览器
			var isEdge = userAgent.indexOf("Edge") > -1 && !isIE; // 判断是否IE的Edge浏览器
			var isIE11 = userAgent.indexOf('Trident') > -1
					&& userAgent.indexOf("rv:11.0") > -1;
			if (isIE || isEdge || isIE11) {
				return true;
			} else {
				return false;// 不是ie浏览器
			}
		},
		/**
		 * 判断是否为空 @str 字符串
		 */
		isEmpty : function(v) {
			switch (typeof v) {
			case 'undefined':
				return true;
			case 'string':
				if (sgyjs.trim(v).length == 0)
					return true;
				break;
			case 'boolean':
				if (!v)
					return true;
				break;
			case 'number':
				if (0 === v)
					return true;
				break;
			case 'object':
				if (null === v)
					return true;
				if (undefined !== v.length && v.length == 0)
					return true;
				for ( var k in v) {
					return false;
				}
				return true;
				break;
			}
			return false;

		},
		/**
		 * 去掉空格字符串
		 */
		trim:function(str){
			return str.replace(/(^\s*)|(\s*$)/g, '');
		},
		
		doReturnAjax:function(url, params, dataType, type){
		    	var result = '';
		    	if(sgyjs.isEmpty(url)){
					alert("请求地址url不能为空");
					return false;
				}
		    	if(sgyjs.isEmpty(params)){
		    		params = {};
		    	}
		    	if(sgyjs.isEmpty(type)){
		    		type='post';
		    	}
		    	if(sgyjs.isEmpty(dataType)){
		    		dataType='json';
		    	}

		    	$.ajax({
						type :type,
						url : url,
						data : params,
						async:false,//这一步很重要
						timeout:30000,
						dataType :dataType,
						success : function(data) {
							result = data;
						},
						error : function(jqXHR, textStatus, errorThrown) {
							alert(网络连接超时);
						}
				});
		    	return result;
		    }
}