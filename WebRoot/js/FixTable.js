//固定table中的行与列
//使用事例：FixTable.init('row',2,3).start(); 固定表头与前2列
//FixTable.init('row').start();||FixTable.init('row',1,1).start();只固定表头
//FixTable.init('row',3,2).start();只固定table的前三列，
//start函数中有2个参数，第一个传入滚动条横向滚动事件，第二个参数传入滚动条竖向滚动事件
//FixTable.init('row',2,3).start();后面函数完整使用事例：
//FixTable.init('row',2,3).start({vertical_fun:function(obj){alert(obj.type);}},//横向滚动函数 obj为当前FixTable对象
//								  crosswise_fun:function(obj){},//竖向滚动函数 obj为当前FixTable对象
//								  chick_tr_fun:function(obj){},//点击tr事件 obj为当前FixTable对象
//								  mouseover_tr_fun:function(obj){},//移进tr事件 obj为当前FixTable对象
//								  mouseout_tr_fun:function(obj){});//移出tr事件 obj为当前FixTable对象
FixTable={
	type:1,//1 单独固定表头，2单独固定列 3 固定列与表头 当type in(2,3)时rows对于的是固定的列数
	tableId:null,
	rows:1,//只对type为2，3的时候有用需要固定的列数
	top:null,//table的距离顶端的高
	left:null,//table距离左边的距离
	height:null,//table的总高度
	width:null,//tabel的总宽度
	table:null,//table的实例值
	bodyWidth:0,//固定列中的总宽度
	height_head:0,//表头的高,
	table_head:null,//当前固定表头的对象
	table_body:null,//当前固定列的对象
	table_common:null,//当前列与表头固定公共部分对象
	truth_cross:true,
	truth_verti:true,
	vertical_fun:function(){},//横向滚动函数
	crosswise_fun:function(){},//竖向滚动函数
	chick_tr_fun:function(){},//点击tr事件
	mouseover_tr_fun:function(){},//移进tr事件
	mouseout_tr_fun:function(){},//移出tr事件
	//初始化参数
	init:function(tableId_,rows_,type_){
		this.type = type_;
		this.tableId = tableId_;
		this.rows = rows_;
		this.table = $('#'+this.tableId);
		this.top = this.table.offset().top;
		this.left = this.table.offset().left;
		this.height = this.table.height();
		this.height_head = this.table.find('thead').height();
		return this;
	},
	test:function(params){
		params.test();
	},
	//分割参数
	divisionParams:function(params){
		try{
			this.vertical_fun = typeof(params.vertical_fun)=='function'?params.vertical_fun:this.vertical_fun;
		}catch(e){}
		try{
			this.crosswise_fun = typeof(params.crosswise_fun)=='function'?params.crosswise_fun:this.crosswise_fun;
		}catch(e){}
		try{
			this.chick_tr_fun = typeof(params.chick_tr_fun)=='function'?params.chick_tr_fun:this.chick_tr_fun;
		}catch(e){}
		try{
			this.mouseover_tr_fun = typeof(params.mouseover_tr_fun)=='function'?params.mouseover_tr_fun:this.mouseover_tr_fun;
		}catch(e){}
		try{
			this.mouseout_tr_fun = typeof(params.mouseout_tr_fun)=='function'?params.mouseout_tr_fun:this.mouseout_tr_fun;
		}catch(e){}
	},
	//开始固定
	start:function(params){
		this.divisionParams(params);
		var obj = this;//当前对象
		if(this.type == 1||this.type == null){//单独固定表头
			this.fixHead(function(){
							obj.verticalFun(obj);
							obj.vertical_fun(obj);
						},function(){
							obj.crosswiseFun(obj);
							obj.crosswise_fun(obj);
						});
		}else if(this.type==2){//单独固定列
			if(this.rows!=null && this.rows > 0){
				this.fixRow(function(){
							obj.verticalFun(obj);
							obj.vertical_fun(obj);
						},function(){
							obj.crosswiseFun(obj);
							obj.vertical_fun(obj);
						});
			}
		}else if(this.type==3){//列与表头同时固定
			//先固定表头
			this.fixHead(function(){
							obj.verticalFun(obj);
							obj.vertical_fun(obj);
						},function(){
							obj.crosswiseFun(obj);
							obj.crosswise_fun(obj);
						});
			if(this.rows!=null && this.rows > 0){
				//在固定列
				this.fixRow();
				//最后固定相同部分
				this.fixCommon();
			}
		}
		//最后添加事件
		this.changeColor();
	},
	//改变table颜色进入事件
	changeColor:function(){
		var obj = this;
		this.table.find('tbody').find('tr').each(function(i,n){
			$(n).mousemove(function(){
				var current_color = $(this).css('background-color');
				$(this).addClass("over");
				var tr = $(obj.table_body).find('tbody').find('tr');
				$(tr[i]).addClass('over');
				//添加用户事件
				obj.mouseover_tr_fun(obj,i,n);
			}).mouseout(function(){
				var current_color = $(this).css('background-color');
				$(this).removeClass("over");
				var tr = $(obj.table_body).find('tbody').find('tr');
				$(tr[i]).removeClass('over');
				//添加用户事件
				obj.mouseout_tr_fun(obj,i,n);
			}).click(function(){
				//添加用户事件
				obj.chick_tr_fun(obj,i,n);
			});
		});
	},
	//单独固定表头 vertical_fun横向滚动函数 crosswise_fun竖向滚动函数
	fixHead:function(vertical_fun,crosswise_fun){
		//取出thead的高
		var thead_height = this.table.find('thead').height();
		var thead_width = this.table.find('thead').width();
		var table = this.table.clone();
		//添加固定的表头
		var tableHead = document.createElement('div');
		$(tableHead).css({'border-bottom':'1px solid #E1E5E8','border-right':'1px solid #E1E5E8',
						 'z-index':'99',
						 'position':'absolute',
						 'left':this.left+'px',
						 'top':this.top+'px',
						 'height':thead_height+'px',
						 'width':thead_width+'px',
						 'overflow':'hidden'});
		$(table).width($(this.table).width());				 
		$(tableHead).html(table);
		$('body').append(tableHead);
		this.table_head = tableHead;
		this.scroll(vertical_fun,crosswise_fun);
	},
	//单独固定列
	fixRow:function(vertical_fun,crosswise_fun){
		for(var i=0;i<this.rows;i++){
			this.bodyWidth += document.getElementById(this.tableId).rows[0].cells[i].offsetWidth;
		}
		var table = this.table.clone();
		var body_div = document.createElement('div');
		$(body_div).css({'border-right':'1px solid #E1E5E8',
						 'z-index':100,
						 'position':'absolute',
						 'left':this.left+'px',
						 'top':this.top+'px',
						 'height':this.height+'px',
						 'width':this.bodyWidth+'px',
						 'overflow':'hidden'});
		$(table).width($(this.table).width());					 
		$(body_div).html(table);
		$('body').append(body_div);
		this.table_body = body_div;
		this.scroll(vertical_fun,crosswise_fun);
	},
	//列于行都固定时的公共部分
	fixCommon:function(){
		var table = this.table.clone();
		var com_div = document.createElement('div');
		$(com_div).css({'border-bottom':'1px solid #E1E5E8','border-right':'1px solid #E1E5E8',
						 'z-index':'101',
						 'position':'fixed',
						 'left':this.left+'px',
						 'top':this.top+'px',
						 'height':this.height_head+'px',
						 'width':this.bodyWidth+'px',
						 'overflow':'hidden'});
		$(table).width($(this.table).width());	
		$(com_div).html(table);
		$('body').append(com_div);
		
	},
	//滚动事件
	scroll:function(vertical_fun,crosswise_fun){
		var leftScroll = document.documentElement.scrollLeft;
		var topScroll = document.documentElement.scrollTop;
		$(window).scroll(function(){
			//判断滚动的滚动条是不是横向
			if(document.documentElement.scrollLeft != leftScroll){
				if(typeof(vertical_fun)=='function'){
					vertical_fun();
				}
				leftScroll = document.documentElement.scrollLeft;
			}
			//判断滚动的滚动条是不是竖向
			if(document.documentElement.scrollTop != topScroll){
				if(typeof(crosswise_fun)=='function'){
					crosswise_fun();
				}
				topScroll = document.documentElement.scrollTop;
			}
		});
	},
	//默认的横向滚动事件
	verticalFun:function(obj){
		try{
			if(!obj.truth_cross){
				$(obj.table_head).css('top',document.documentElement.scrollTop);
				$(obj.table_head).css('left',obj.left);
				$(obj.table_head).css('position','absolute');
				obj.truth_cross = true;
			}
		}catch(e){}
		try{
			if(obj.truth_verti){//重新还原原始高度
				$(obj.table_body).css('left',obj.left);
				$(obj.table_body).css('position','fixed');
				$(obj.table_body).css('top',-document.documentElement.scrollTop);
				obj.truth_verti = false;
			}
		}catch(e){}
	},
	//默认的竖向滚动事件
	crosswiseFun:function(obj){
		try{
			if(obj.truth_cross){//重新还原原始高度
				$(obj.table_head).css('left',-document.documentElement.scrollLeft);
				$(obj.table_head).css('top',obj.top);
				$(obj.table_head).css('position','fixed');
				obj.truth_cross = false;
			}
		}catch(e){}
		try{
			if(!obj.truth_verti){
				$(obj.table_body).css('left',document.documentElement.scrollLeft);
				$(obj.table_body).css('top',obj.top);
				$(obj.table_body).css('position','absolute');
				obj.truth_verti = true;
			}
		}catch(e){}
	}
};