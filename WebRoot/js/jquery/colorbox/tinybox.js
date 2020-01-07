TINY={
	box:{
		v:{opacity:70,close:1,animate:1,fixed:1,mask:1,maskid:'',boxid:'',topsplit:2,url:0,post:0,height:0,width:0,html:0,iframe:0,imgId:'',zoomHeight:0,zoomWidht:0,move:true,zoom:true},
		tinner:null,
		tcontent:null,
		tmask:null,
		tclose:null,//关闭按钮
		img_width:0,//图片的宽
		img_height:0,//图片的高
		zoom_img_width:0,//缩放后图片的宽
		zoom_img_height:0,//缩放后图片的高
		clentHeight:0,
		clientwidth:0,
		bl:0,//缩放比例
		//显示图片信息
		show:function(o){
			var obj = this;
			for(s in o){
				this.v[s]=o[s];
			}
			
			$('.tinner').remove();
			$('.tcontent').remove();
			$('.tmask').remove();
			$('.tclose').remove();
			
			this.clentHeight = this.height();
			this.clientwidth = this.width();
			
			this.tinner=document.createElement('div'); 
			this.tinner.className='tinner';
			this.tcontent=document.createElement('div'); 
			this.tcontent.className='tcontent';
			this.tmask=document.createElement('div'); 
			this.tmask.className='tmask';
			this.tclose=document.createElement('div'); 
			this.tclose.className='tclose'; 
			//加入到页面
			this.tmask.appendChild(this.tcontent);
			this.tmask.appendChild(this.tclose);
			//加入到div
			document.body.appendChild(this.tmask);
			//加入图片
			this.tcontent.innerHTML = this.v.html;
			
			//添加关闭事件
			$(this.tclose).click(function(){
				$(obj.tmask).fadeOut(400,function(){
					$(obj.tmask).remove();
				});
			});
			$(this.tmask).css('cursor','move');
			var img = new Image();
			//出现加载动画
			document.body.appendChild(this.tinner);
			$(this.tinner).css({'top':(this.clentHeight-$(this.tinner).height())/2,'left':(this.clientwidth-$(this.tinner).width())/2});
			img.onload=function(){
				obj.img_width = img.width;
				obj.img_height = img.height;
				
				$(obj.tinner).css('background-image','');
				
				$(obj.tinner).css({'width':$(obj.tinner).width(),
								   'height':$(obj.tinner).width()*obj.bl});
				obj.resize(obj.tinner,obj);
				var task = window.setInterval(function(){
					var value = 25;
					$(obj.tinner).height(value*obj.bl+$(obj.tinner).height());
					$(obj.tinner).width(value+$(obj.tinner).height());
					obj.resize(obj.tinner,obj);
					if($(obj.tinner).height() >= obj.zoom_img_height){
						window.clearInterval(task);
						$(obj.tinner).remove();
						$(obj.tmask).show();
					}
				},10);
				//获取缩放后图片大小
				obj.getSize(obj);
				$(obj.tcontent).find('#'+obj.v.imgId).css({'height':obj.zoom_img_height,'width':obj.zoom_img_width});
				$(obj.tcontent).css({'height':obj.zoom_img_height,'width':obj.zoom_img_width});
				$(obj.tmask).css({'height':obj.zoom_img_height,
								  'width':obj.zoom_img_width});
				
				obj.resize(obj.tmask,obj);
				obj.moveImg(obj);
			};
			img.onerror=function(){
				alert('图片加载失败,请确认是否存在图片！');
				$(obj.tmask).remove();
				$(obj.tinner).remove();
			};
			img.src = $('#'+this.v.imgId).attr('src');
		},
		//获取缩放后图片大小
		getSize:function(obj){
			var height = obj.img_height;
			var width = obj.img_width;
			var div_height = obj.v.zoomHeight;
			var div_width = obj.v.zoomWidth;
			obj.bl = parseFloat(height/width);
			obj.checkSize(obj,height,width);
		},
		//递归获取图片的size
		checkSize:function(obj,height,width){
			if(height > obj.v.zoomHeight){
				height = obj.v.zoomHeight;
				width = parseInt(height/obj.bl);
			}else if(width > obj.v.zoomWidth){
				width = obj.v.zoomWidth;
				height = parseInt(width*obj.bl);
			}
			if(height <= obj.v.zoomHeight && width <= obj.v.zoomWidth){
				obj.zoom_img_width = width;
				obj.zoom_img_height = height;
			}else{
				obj.checkSize(obj,height,width);
			}
		},
		
		//移动图片
		moveImg:function(obj){
			var img = $(obj.tcontent).find('#'+obj.v.imgId);
			var down_x = 0;
			var down_y = 0;
			var top = 0;
			var left = 0;
			var x=0;
			var y=0;
			var truth = false;
			var over_truth = false;
			var y_h = obj.zoom_img_height;
			var y_w = obj.zoom_img_width;
			var py_top = 0;
			var py_left = 0;
			$(obj.tmask).mousedown(function(e){
				truth = true;
				down_x = e.clientX;
				down_y = e.clientY;
				top = $(obj.tmask).offset().top;
				left = $(obj.tmask).offset().left;
				x = down_x-left;
				y = down_y-top;
				$(obj.tmask).css({'position':'absolute','left':left,'top':top});
				return false;
			}).mouseup(function(e){
				truth = false;
				$(obj.tmask).css({'position':''});
				
				var height = obj.height();
				var width = obj.width();
				
				var img_top = $(this).offset().top;
				var img_left = $(this).offset().left;
				
				var img_height = img.height();
				var img_width = img.width();
				
				py_top = height/2-(img_top+(img_height/2));
				py_left = width/2-(img_left+(img_width)/2);
				
				return false;
			}).mouseover(function(){
				if(obj.v.zoom){
					top = $(this).offset().top;
					left = $(this).offset().left;
					over_truth = true;
				}
			}).mouseout(function(){
				over_truth = false;
			});
			$(document).mousemove(function(e){
				if(truth){
					down_x = e.clientX;
					down_y = e.clientY;
					$(obj.tmask).css({'left':down_x-x,'top':down_y-y});
				}
				return false;
			});
			//添加缩放
			var count = 10;
			obj.tmask.onmousewheel =function(e){
				if(over_truth){
					if (event.wheelDelta >= 120){
						count += 25;
					}else if (event.wheelDelta <= -120){
						if(count + y_w>100){
							count -= 25;
						}
					}
					img.width(count + y_w);
					img.height(count*obj.bl + y_h);
					$(obj.tcontent).height(count*obj.bl + y_h );
					$(obj.tcontent).width(count + y_w);
					$(obj.tmask).height(count*obj.bl + y_h );
					$(obj.tmask).width(count + y_w);
					obj.reAbsoluteSize(count + y_w ,count*obj.bl + y_h,py_top,py_left,obj);
				}
			}
		},
		reAbsoluteSize:function(w,h,py_top,py_left,obj){
			var height = this.height();
			var width = this.width();
			$(obj.tmask).css({'top':(height/2)-py_top-(h/2),'left':(width/2)-py_left-(w/2)});
		},
		//重新定位
		resize:function(mb_obj,obj){
			$(mb_obj).css({'top':(obj.clentHeight-$(mb_obj).height())/2,
							      'left':(obj.clientwidth-$(mb_obj).width())/2});
		},
		top:function(){return document.documentElement.scrollTop||document.body.scrollTop},
		width:function(){return self.innerWidth||document.documentElement.clientWidth||document.body.clientWidth},
		height:function(){return self.innerHeight||document.documentElement.clientHeight||document.body.clientHeight}
	}
};