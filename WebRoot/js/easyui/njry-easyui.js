/*定义验证规则组*/
Validator = {
	Phone:/^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/, 
	Msisdn:/^(147|150|151|152|158|159|182|183|187|188|198|13[4-9]{1})[0-9]{8}$/, 
	Mobile:/^(13|14|15|17|18|19)[0-9]{9}$/,
	Currency:/^\d+(\.\d+)?$/, 
	Number:/^\d+$/, Zip:/^[1-9]\d{5}$/, 
	QQ:/^[1-9]\d{4,8}$/, 
	Integer:/^[-\+]?\d+$/,
	Integer2:/^\d+$/,IP:/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/, 
	Double:/^[-\+]?\d+(\.\d+)?$/, 
	Double2:/^[+]?\d+(\.\d+)?$/, 
	English:/^[A-Za-z]+$/, 
	Chinese:/^[\u0391-\uFFE5]+$/, 
	Username:/^[a-z]\w{3,}$/i, 
	UnSafe:/^(([A-Z]*|[a-z]*|\d*|[-_\~!@#\$%\^&\*\.\(\)\[\]\{\}<>\?\\\/\'\"]*)|.{0,5})$|\s/
}

/*重写验证规则*/
$.extend($.fn.validatebox.defaults.rules, {   
    Require: {//easyui下拉框必选
        validator: function(value, param){
            return $('#' + param[1]).combobox('getValue') != '';   
        },   
        message: '{0}' 
    },
    Phone: {//电话号码
        validator: function(value, param){  
            return Validator.Phone.test(value);   
        },   
        message: '{0}' 
    },
    Mobile: {//电话号码
        validator: function(value, param){  
            return Validator.Mobile.test(value);   
        },   
        message: '{0}' 
    },
    Currency: {
        validator: function(value, param){  
            return Validator.Currency.test(value);   
        },   
        message: '{0}' 
    },
    Number: {//数字
        validator: function(value, param){  
            return Validator.Number.test(value);   
        },   
        message: '{0}' 
    },
    QQ: {
        validator: function(value, param){  
            return Validator.QQ.test(value);   
        },   
        message: '{0}' 
    },
    Msisdn: {//移动号码
        validator: function(value, param){  
            return Validator.Msisdn.test(value);   
        },   
        message: '{0}' 
    },
    Mobile: {//移动号码
        validator: function(value, param){  
            return Validator.Mobile.test(value);   
        },   
        message: '{0}' 
    },
    Integer: {
        validator: function(value, param){  
            return Validator.Integer.test(value);   
        },   
        message: '{0}' 
    },
    Integer2: {
        validator: function(value, param){  
            return Validator.Integer2.test(value);   
        },   
        message: '{0}' 
    },
    Double: {
        validator: function(value, param){  
            return Validator.Double.test(value);   
        },   
        message: '{0}' 
    },
    Double2: {
        validator: function(value, param){  
            return Validator.Double2.test(value);   
        },   
        message: '{0}' 
    },
    English: {
        validator: function(value, param){  
            return Validator.English.test(value);   
        },   
        message: '{0}' 
    },
    Chinese: {
        validator: function(value, param){  
            return Validator.Chinese.test(value);   
        },   
        message: '{0}' 
    },
    Username: {
        validator: function(value, param){  
            return Validator.Username.test(value);   
        },   
        message: '{0}' 
    },
    UnSafe: {
        validator: function(value, param){  
            return Validator.UnSafe.test(value);   
        },   
        message: '{0}' 
    },
    Group: {
        validator: function(value, param){
            return mustChecked(value, param[1]);
        },
        message: '{0}'
    } 
});

/* 判定一组是否有一个选中 */
var mustChecked = function (value, name) {
    var groups = document.getElementsByName(name);
	var hasChecked = 0;
	min = 1;
	max = groups.length;
	for (var i = groups.length - 1; i >= 0; i--) {
		if (groups[i].checked) {
			hasChecked++;
		}
	}
	return min <= hasChecked && hasChecked <= max;
};

/*重写 combobox的过滤规则*/
$.extend($.fn.combobox.defaults, {
	filter: function(q, row){
		var opts = $(this).combobox('options');
		return row[opts.textField].indexOf(q) >= 0;
	}
});


easyUI = {
	alert:function (msg, title) {
		$.messager.alert((title == null ? '系统提醒' : title), msg);
	}
}