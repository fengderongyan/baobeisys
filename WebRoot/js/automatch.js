//定义一个自动下拉对像(参数1：下拉实例名,目的DIV的ID;)
function jsAuto(instanceName,objID){
	this._msg = [];//存放的信息
	this._x = null;
	this._o = document.getElementById( objID );
	this._if = document.getElementById( 'iframe_'+objID );
	if (!this._o) return;
	this._f = null;
	this._i = instanceName;
	this._r = null;
	this._c = 0;
	this._s = false;
	this._v = null;
	this._length = 0;
	this._o.style.visibility = "hidden";//默认影藏
	this._o.style.position = "absolute";//采用绝对定位
    this._o.style.overflow = "auto";// 超出范围自动滚动
    this._o.style.height = "205";//默认高度为205像素
    this._if.style.visibility = "hidden";//默认影藏
	this._if.style.position = "absolute";//采用绝对定位
    this._if.style.overflow = "auto";// 超出范围自动滚动
    this._if.style.height = "205";//默认高度为205像素
	return this;
};

//当鼠标或键盘事件时的样式
jsAuto.prototype.directionKey=function() { with (this){
	var e = _e.keyCode ? _e.keyCode : _e.which;
	var l = _o.childNodes.length;
	(_c>l-1 || _c<0) ? _s=false : "";

	if( e==40  &&  _s )
	{
		_o.childNodes[_c].className="mouseout";
		(_c >= l-1) ? _c=0 : _c ++;
		_o.childNodes[_c].className="mouseover";
		_r.value=_o.childNodes[_c].innerHTML.replace("<STRONG>","").replace("</STRONG>","");
	}
	if( e==38  &&  _s )
	{
		_o.childNodes[_c].className="mouseout";
		_c--<=0 ? _c = _o.childNodes.length-1 : "";
		_o.childNodes[_c].className="mouseover";
		_r.value=_o.childNodes[_c].innerHTML.replace("<STRONG>","").replace("</STRONG>","");
	}
	if( e==13 )
	{
		if(_o.childNodes[_c]  &&  _o.style.visibility=="visible")
		{
			_r.value = _x[_c];
			_o.style.visibility = "hidden";
			_if.style.visibility = "hidden";
		}
	}
	if( !_s )
	{
		_c = 0;
		_o.childNodes[_c].className="mouseover";
		_s = true;
	}
}};


//鼠标经过事件
jsAuto.prototype.domouseover=function(obj) { with (this){
	_o.childNodes[_c].className = "mouseout";
	_c = 0;
	obj.tagName=="DIV" ? obj.className="mouseover" : obj.parentElement.className="mouseover";
}};

//鼠标移除事件
jsAuto.prototype.domouseout=function(obj){
	obj.tagName=="DIV" ? obj.className="mouseout" : obj.parentElement.className="mouseout";
};

//点击事件
jsAuto.prototype.doclick=function(msg) { with (this){
	if(_r){
		_r.value = msg;
		_o.style.visibility = "hidden";
		_if.style.visibility = "hidden";
	}
	else{
		alert("javascript autocomplete ERROR :\n\n can not get return object.");
		return;
	}
}};

//添加下拉信息
jsAuto.prototype.item=function(msg){
	if( msg.indexOf(",")>0 )
	{
		var arrMsg=msg.split(",");
		for(var i=0; i<arrMsg.length; i++)
		{
			arrMsg[i] ? this._msg.push(arrMsg[i]) : "";
		}
	}
	else
	{
		this._msg.push(msg);
	}
	this._msg.sort();
};

//在DIV下添加内容
jsAuto.prototype.append=function(msg) { with (this){
	_i ? "" : _i = eval(_i);
	_x.push(msg);
	var div = document.createElement("DIV");
	div.onmouseover = function(){_i.domouseover(this)};
	div.onmouseout = function(){_i.domouseout(this)};
	div.onclick = function(){_i.doclick(msg)};
	var re  = new RegExp("(" + _v + ")","i");
	div.style.lineHeight="140%";
	div.className = "mouseout";
	div.style.paddingLeft="5px";
	div.style.paddingTop="3px";
	if (_v) div.innerHTML =""+msg.replace(re , "<strong>$1</strong>");
	div.style.fontFamily = "verdana";
	_o.appendChild(div);
}};

//显示
jsAuto.prototype.display=function() { with(this){ 
	if(_f && _v!="")
	{
		//_o.style.left = $(_r).offset().left;
		_o.style.width = _r.offsetWidth;
		//_o.style.top = $(_r).offset().top +$(_r).height()+2;
		$(_o).css({position: "absolute",'top':$(_r).position().top-8,'left':$(_r).position().left-2,'z-index':0}); 
		_o.style.visibility = "visible";
		
		//_if.style.left = $(_r).offset().left;
		_if.style.width = _r.offsetWidth;
		//_if.style.top = $(_r).offset().top +$(_r).height()+2;
		$(_if).css({position: "absolute",'top':$(_r).position().top-8,'left':$(_r).position().left-2,'z-index':0}); 
		_if.style.visibility = "visible";
	}
	else
	{
		_o.style.visibility="hidden";
		_if.style.visibility="hidden";
	}
}};

jsAuto.prototype.handleEvent=function(fValue,fID,event,url) { with (this){
	  //一个字符时不查询
	  if(fValue.trim().length<=1){
	  	 _o.style.visibility="hidden";
		_if.style.visibility="hidden";
	 	 return false;
	  }
	  var re;
	  _e = event;
	  var e = _e.keyCode ? _e.keyCode : _e.which;
	  _r = document.getElementById(fID);
	  if((e!=38 && e!=40 && e!=13)){
            _x = [];
			_f = false;
			_v = fValue.trim();
			_i = eval(_i);
			re = new RegExp(""+ fValue.trim() + "", "i");	  	
	      	_o.innerHTML="";
	      	var params = 'value='+fValue.trim();;
   		    new MyJqueryAjax2(url,params,function(result){
	          _msg=result;
	          for(var i=0; i<_msg.length; i++){
		        if(re.test(_msg[i].VALUE)){
			       _i.append(_msg[i].VALUE);
			       _f = true;
		        }
	          }
	          _i ? _i.display() : alert("can not get instance");
   			
   		    },'json').request();
	   }
	      
	   if(_o.childNodes.length>0){
	   	   if((e==38 || e==40 || e==13)){
		  	   _i.directionKey();
		 	   var cnt=parseInt($(_o).height()/20);
			   if(_c>cnt){
			   	_o.scrollTop=(_c-cnt)*20;
			   }
		    }else{
				_c=0;
				_o.childNodes[_c].className = "mouseover";
				_s=true;
		    }
	    }
   }
};

window.onerror=new Function("return true;");