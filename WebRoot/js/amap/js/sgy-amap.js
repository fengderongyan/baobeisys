/**
 * 初始化地图信息
 * mapId:地图容器标识
 * centerLng:地图中心点经度
 * centerLat:地图中心点纬度
 * mapZoom:地图初始化缩放级别
 * extendAttr:地图属性重新定义对象
 **/
function Amap(mapId, centerLng, centerLat, mapZoom, app, extendAttr){
	this.map = new AMap.Map(mapId, $.extend({
			rotateEnable:false,//地图是否可旋转
			dragEnable:true,//地图是否可通过鼠标拖拽平移，默认为true
			zoomEnable:true,//地图是否可缩放，默认值为true。
			zooms:[3,18],//地图显示的缩放级别范围
			doubleClickZoom:true,//地图是否可通过双击鼠标放大地图，默认为true
			jogEnable:true,//地图是否使用缓动效果，默认值为true
			scrollWheel:true,//地图是否可通过鼠标滚轮缩放浏览，默认为true
			//二维地图显示视口
	        view: new AMap.View2D({
	            center:this.getLnglat(centerLng, centerLat),//地图中心点
	            zoom:mapZoom //地图显示的缩放级别
	        })
		},
		extendAttr ? extendAttr : {}
	));
	this.app = app;
	this.initEventType();//初始化各类覆盖物的事件类型
	this.initOverlayArrObj();//初始化地图数组对象
	this.initLayers();//地图图层对象
	this.initOverlayContainer();//初始化地图容器对象
}

/**
 * 添加工具条
 */
Amap.prototype.addToolBar = function(){
	var obj = this;
	//添加地图工具条插件
	this.map.plugin(["AMap.ToolBar"],function(){
		obj.toolBar = new AMap.ToolBar();
		obj.map.addControl(obj.toolBar);
	});
}

/**
 * 添加鹰眼
 */
Amap.prototype.addOverView = function(){
	var obj = this;
	this.map.plugin(["AMap.OverView"],function(){
		obj.overView = new AMap.OverView({  
        	visible:true //初始化显示鹰眼  
		});
		obj.map.addControl(obj.overView);  
	    obj.overView.open(); //展开鹰眼
	});
}

/**
 * 添加比例尺
 */
Amap.prototype.addScale = function(){
	var obj = this;
	//添加地图比例尺插件
	this.map.plugin(["AMap.Scale"],function(){
		obj.scale = new AMap.Scale();
		obj.map.addControl(obj.scale);
	});
}

/**
 * 添加鼠标工具
 */
Amap.prototype.addMouseTool = function(){
	var obj = this;
	//添加鼠标工具插件
	this.map.plugin(["AMap.MouseTool"],function(){
		obj.mouseTool = new AMap.MouseTool(obj.map);
	});
}

/**
 * 关闭鼠标工具
 * isRemove 是否删除由鼠标工具产生的覆盖物
 */
Amap.prototype.closeMouseTool = function(isRemove){
	isRemove = isRemove ? isRemove : true;
	this.mouseTool.close(isRemove);
}

/**
 * 鼠标工具绘制多边形
 */
Amap.prototype.mouseDrawPolygon = function(callFunc){
	var polygonOption = {
	    strokeColor:"#FF33FF",
	    strokeOpacity:1,
	    strokeWeight:2
	};
	this.mouseTool.polygon(polygonOption);
	
	AMap.event.addListener(this.mouseTool, "draw", callFunc?callFunc:function(e){});
}

/**
 * 添加多边形、线编辑工具
 */
Amap.prototype.addPolyEditor = function(overlay){
	this.removeControl(this.polyEditor);
	var obj = this;
	//添加鼠标工具插件
	this.map.plugin(["AMap.PolyEditor"],function(){
		obj.polyEditor = new AMap.PolyEditor(obj.map, overlay);
	});
}

Amap.prototype.openPolyEditor = function(){
	this.polyEditor.open();
}

Amap.prototype.closePolyEditor = function(){
	this.polyEditor.close();
}

Amap.prototype.removeControl = function(controlObj){
	if(controlObj){
		this.removeControl(controlObj);
	}
}

/**
 * 地图类型切换：
 */
Amap.prototype.addMapType = function(extendAttr){
	var obj = this;
	this.map.plugin(["AMap.MapType"], function() {
		var type = new AMap.MapType($.extend(
			{
				defaultType:0,//初始状态使用2D地图，1为卫星地图
				showRoad:false,//叠加实时交通图层
				showRoad:false//叠加路网图层
			},
			extendAttr ? extendAttr : {}
		));
		obj.map.addControl(type);
	});
}

/**
 * 实例化信息窗口
 * extendAttr:拓展属性
 **/
Amap.prototype.initInfoWindow = function(extendAttr){
	//实例化信息窗体
	this.infoWindow = new AMap.InfoWindow($.extend(
		{
			isCustom:true,  //使用自定义窗体
			offset:new AMap.Pixel(16, -30)//-113, -140
		},
		extendAttr ? extendAttr : {}
	));
}

/**
 * 实例化各类覆盖物的事件类型
 */
Amap.prototype.initEventType = function(){
	this.eventType = {
		marker:["click","dblclick","rightclick","mousemove","mouseover","mouseout","dragstart","dragging","dragend"],
		polyline:["click","dblclick","rightclick","hide","mouseover","mouseout","zoomchange"],
		polygon:["click","dblclick","rightclick","hide","mouseover","mouseout","zoomchange"],
		layer:["complete"],
		map:["zoomchange"]
	}
}

/**
 * 初始化地图覆盖物数组对象
 **/
Amap.prototype.initOverlayArrObj = function(){
	//地图覆盖物数组对象
	this.overlayArrObj = {};// {id1:{state:'0~2',arr:[]},id2:{state:'0~2',arr:[]},...} state=0表示一次性数组，state=1表示通过人为删除，state=2表示程序保留数组
	this.CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".split("");
}

//初始化地图图层对象
Amap.prototype.initLayers = function(){
	this.layers = {};
	if(this.map.getLayers().length > 0){
		this.layers["baseLayer"] = this.map.getLayers()[0];
	}
}

//初始化地图覆盖物容器
Amap.prototype.initOverlayContainer = function(){
	this.container = {
		marker:{},
		polyline:{},
		polygon:{}
	};
}

/**
 * 生成唯一索引编号，例如数组对象编号
 * len：索引长度
 * radix：基数
 **/
Amap.prototype.getUUID = function(len, radix){
	var chars = this.CHARS;
	var uuid = new Array();
	var i;
	radix = radix || chars.length;
	
	if(len){
		// Compact form 
		for (i = 0; i < len; i++) uuid.push(chars[0 | Math.random()*radix]); 
	}else{
		// rfc4122, version 4 form 
		var r;

		// rfc4122 requires these characters 
		uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-'; 
		uuid[14] = '4'; 

		// Fill in random data.  At i==19 set the high bits of clock sequence as 
		// per rfc4122, sec. 4.1.5 
		for (i = 0; i < 36; i++) {
			if (!uuid[i]) {
			r = 0 | Math.random()*16;
			uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
			}
		}
    }
	
	return uuid.join('');
}

/**
 * 地图像素
 * 
 **/
Amap.prototype.getPixel = function(x, y){
	return new AMap.Pixel(x, y);
}

/**
 * lng:经度
 * lat:纬度
 **/
Amap.prototype.getLnglat = function(lng, lat){
	return new AMap.LngLat(lng, lat);
}

/**
 * 获取经纬度数组
 * lnglats:经纬度集合
 * separator:分隔符
 **/
Amap.prototype.getLnglatArr = function(lnglats, separator){
	var lnglatArr,arr,i,lnglat;
	
	if(!separator) separator=";";//默认分隔符为分号
	if(lnglats=="" || lnglats==null) return null;
	if(lnglats.lastIndexOf(separator) == lnglats.length-1){
		lnglats = lnglats.substring(0, lnglats.lastIndexOf(separator));
	}
	
	lnglatArr = [];
	arr = lnglats.split(separator);
	for(i=0;i<arr.length;i++){
		lnglat = arr[i].split(",");
		lnglatArr.push(this.getLnglat(lnglat[0], lnglat[1]));
	}
	return lnglatArr;
}

/**
 * 计算地图距离对应的容器像素
 **/
Amap.prototype.distanceToPix = function(distance){
	var mapResolution,pix;
	mapResolution = this.map.getResolution();// 米/像素
   	pix = distance/mapResolution;
   	return pix;
}

/**
 * 地图根据覆盖物数组自适应视野范围（包括层级及中心点）
 * arrayId：地图数组对象编号
 **/
Amap.prototype.setFitView = function(overlayArr){
	try{
		if(overlayArr instanceof Array){
			this.map.setFitView(overlayArr);
		}else{
			var arr = [];
			arr.push(overlayArr);
			this.map.setFitView(arr);
		}
	}catch(et){}
}

//设置地图显示的中心点
Amap.prototype.setCenter = function(lng, lat){
	this.map.setCenter(this.getLnglat(lng, lat));
}

//设置地图显示的缩放级别
Amap.prototype.setZoom = function(zoomLevel){
	this.map.setZoom(zoomLevel);
}

/**
 * 地图缩放至指定级别并以指定点为地图显示中心点
 * zoom: 地图显示级别
 * clng：地图中心点经度 
 * clat: 地图中心点纬度
 **/
Amap.prototype.setZoomAndCenter = function(zoomLevel, lng, lat){
	this.map.setZoomAndCenter(zoomLevel, this.getLnglat(lng, lat));
}

/**
 * 获取地图数组对象编号
 * arrayState：数组保存状态（0表示一次性数组，1表示通过人为删除，2表示程序保留数组）
 **/
Amap.prototype.getAmapArrayId = function(arrayState){
	var amapArrayId = this.getUUID(8, 32);
	this.overlayArrObj[amapArrayId] = {state:(arrayState?arrayState:0),arr:[]};
	return amapArrayId;
}

/**
 * 获取地图数组对象编号
 * arrayState：数组保存状态（0表示一次性数组，1表示通过人为删除，2表示程序保留数组）
 **/
Amap.prototype.addAmapArrayValue = function(amapArrayId, value){
	this.getAmapArray(amapArrayId).push(value);
}

Amap.prototype.addAmapArray = function(amapArrayId, arrayState, arr){
	this.overlayArrObj[amapArrayId] = {state:(arrayState?arrayState:0), arr:(arr?arr:[])};
	return this.overlayArrObj[amapArrayId].arr;
}

/**
 * 获取地图数组对象
 * arrayId：地图数组对象编号
 **/
Amap.prototype.getAmapArray = function(arrayId){
	var arrObj = this.overlayArrObj[arrayId];
	if(arrObj){
		return (arrObj.arr ? arrObj.arr:[]);
	}else{
		return this.addAmapArray(arrayId);
	}
}

/**
 * 通过地图数组编号删除数组
 * arrayState：数组保存状态（0表示一次性数组，1表示通过人为删除，2表示程序保留数组）
 **/
Amap.prototype.clearAmapArray = function(arrayId){
	var arrObj = this.getAmapArray[arrayId];
	if(arrObj){
		if(arrObj.arr){
			this.clearOverlay(arrObj.arr);
			arrObj.arr.length = 0;
		}
	};
	delete arrObj;
	delete this.overlayArrObj[arrayId];
}

//通过图层编号获取图层
Amap.prototype.getLayer = function(layerId){
	if(this.layers[layerId]){
		return this.layers[layerId];
	}else{
		return null;
	}
}

/**
 * 添加图层
 * layerId:图层编号
 * url:取图地址
 * layerClass:图层类
 * callTileUrl：图层取图回调函数
 **/
Amap.prototype.addLayer = function(layerId, url, callTileUrl){
	var tileLayerOptions = {
		map:this.map,
		tileUrl:url,
		getTileUrl:callTileUrl ? callTileUrl:function(){},
		zIndex:2
	}
	
	var layer = {};
	layer = new AMap.TileLayer(tileLayerOptions);
	
	//定义图层拓展属性
	layer.extData = {
		events:{
			complete:{
				listener:null,
				handler:null,
				instance:null
			}
		},
		data:null,
		overlayType:"layer"
	};
	layer.getExtData = function(){
		return this.extData;
	}
	
	this.layers[layerId] = layer;
}

//删除图层
Amap.prototype.removeLayer = function(layerId){
	var layer = this.layers[layerId];
	if(layer){
		this.removeOverlay(layer);
	}
	delete this.layers[layerId];
}

//添加卫星图层
Amap.prototype.addSatellite = function(){
	var layerId = "satellite";
	if(!this.layers[layerId]){
		var layer = new AMap.TileLayer.Satellite();
		layer.setMap(this.map);
		this.layers[layerId] = layer;
	}
}

//删除卫星图层
Amap.prototype.removeSatellite = function(){
	var layerId = "satellite";
	if(this.layers[layerId]){
		this.layers[layerId].setMap(null);
		delete this.layers[layerId];
	}
}

/**
 * 判断是否存在覆盖物
 * overlayType：覆盖类型
 * overlayId：编号
 **/
Amap.prototype.existOverlay = function(overlayType, overlayId){
	if(this.getOverlayById(overlayType, overlayId)){
		return true;
	}else{
		return false;
	}
}

//是否存在点
Amap.prototype.existMarker = function(markerId){
	return this.existOverlay("marker", markerId);
}

//是否存在线
Amap.prototype.existPolyline = function(polylineId){
	return this.existOverlay("polyline", polylineId);
}

//是否存在多边形
Amap.prototype.existPolygon = function(polygonId){
	return this.existOverlay("polygon", polygonId);
}

/**
 * 通过覆盖类型、编号获取对象
 * overlayType：覆盖类型
 * overlayId：编号
 **/
Amap.prototype.getOverlayById = function(overlayType, overlayId){
	if(this.container[overlayType]){
		var overlay = this.container[overlayType][overlayId];
		if(overlay){
			return overlay;
		}else{
			return null;
		}
	}else{
		return null;
	}
}

/**
 * 通过标注物编号获取对象
 * markerId：标注物编号
 **/
Amap.prototype.getMarkerById = function(markerId){
	return this.getOverlayById("marker", markerId);
}

/**
 * 通过线编号获取对象
 * polylineId：线编号
 **/
Amap.prototype.getPolylineById = function(polylineId){
	return this.getOverlayById("polyline", polygonId);
}

/**
 * 通过多边形编号获取对象
 * polygonId：多边形编号
 **/
Amap.prototype.getPolygonById = function(polygonId){
	return this.getOverlayById("polygon", polygonId);
}

/**
 * 通过覆盖类型、编号添加覆盖物对象
 * overlayType：覆盖类型
 * overlayId：编号
 * overlay:覆盖物对象
 **/
Amap.prototype.addOverlay = function(overlayType, overlayId, overlay){
	if(this.getOverlayById(overlayType, overlayId)){
		return true;
	}else{
		this.container[overlayType][overlayId] = overlay;
	}
}

/**
 * title:点标记提示信息
 * lng:标记经度
 * lat:标记纬度
 * extendAttr:
 * extData:
 **/
Amap.prototype.addMarker = function(id, title, lng, lat, extendAttr, extData){
	if(this.existMarker(id)){
		this.removeMarkerById(id);
	}
	
	var lnglat,markerOption,marker,extObj;
	lnglat = this.getLnglat(lng, lat);
	markerOption = $.extend(
		{
			map:this.map,
			title:title,//点标注的文字提示
			position:lnglat,//位置
			offset:new AMap.Pixel(-10,-34),
			icon:this.app+'/js/amap/images/l_blue.png', //复杂图标
			content:'',//
			draggable:false,//设置点标记是否可拖拽移动，默认为false
			topWhenMouseOver:true//鼠标移进时marker是否置顶，默认置顶
		},
		extendAttr ? extendAttr : {}
	);
	marker = new AMap.Marker(markerOption);
	extObj = $.extend(
		{
			events:{
				click:{
					listener:null,
					handler:null,
					instance:null
				}
			},
			data:null,
			overlayType:"marker"
		},
		extData ? extData : {}
	);
	
	marker.setExtData(extObj);//用户自定义拓展属性
	//添加事件
	this.addListeners(marker, marker);
	
	//触发事件 
	marker.trigger = function(eventName){
		AMap.event.trigger(this, eventName);
	}
	//添加点到地图容器
	this.addOverlay("marker", id, marker);
	return marker;
}

/**
 * lnglats:经纬度
 * extendAttr:
 * extData:
 **/
Amap.prototype.addPolyline = function(id, lnglats, extendAttr, extData){
	if(this.existPolyline(id)){
		this.removePolylineById(id);
	}
	
	var obj = this;
	var polylineOptions,polyline,extObj;
	polylineOptions = $.extend(
		{
			map:this.map,
			path:lnglats,
			strokeColor:"#FF0000", //线颜色
			strokeOpacity:1, //线透明度 
			strokeWeight:1, //线宽
			strokeStyle:"dashed", //线样式
			strokeDasharray:[10,5] //补充线样式
		},
		extendAttr ? extendAttr : {}
	);
	
	polyline = new AMap.Polyline(polylineOptions);
	
	extObj = $.extend(
		{
			events:{
				click:{
					listener:null,
					handler:null,
					instance:null
				}
			},
			data:null,
			overlayType:"polyline",
			markers:[]
		},
		extData ? extData : {}
	);
	
	polyline.setExtData(extObj);//用户自定义拓展属性
	//添加事件
	this.addListeners(polyline, polyline);
	//添加线到地图容器
	this.addOverlay("polyline", id, polyline);
	return polyline;
}

/**
 * lnglats:经纬度
 * extendAttr:
 * extData:
 **/
Amap.prototype.addPolygon = function(id, lnglats, extendAttr, extData){
	if(this.existPolygon(id)){
		this.removePolygonById(id);
	}
	
	var polygonOptions,polygon,extObj;
	polygonOptions = $.extend(
		{
			map:this.map,
			path:lnglats,
			strokeColor:"#1791fc",//线颜色
		    strokeOpacity:0.1,//线透明度
		    strokeWeight:1,//线宽
		    fillColor: "#1791fc", //填充色
			fillOpacity: 0.9//填充透明度
		},
		extendAttr ? extendAttr : {}
	);
	
	polygon = new AMap.Polygon(polygonOptions);
	extObj = $.extend(
		{
			events:{
				click:{
					listener:null,
					handler:null,
					instance:null
				}
			},
			data:null,
			overlayType:"polygon"
		},
		extData ? extData : {}
	);
	
	//触发事件 
	polygon.trigger = function(eventName){
		AMap.event.trigger(this, eventName);
	}
	
	polygon.setExtData(extObj);//用户自定义拓展属性
	//添加事件
	this.addListeners(polygon, polygon);
	//添加线到地图容器
	this.addOverlay("polygon", id, polygon);
	return polygon;
}

/**
 * 添加轨迹线（附加点）
 * lnglats:经纬度
 * extendAttr:
 * extData:
 **/
Amap.prototype.addTrackline = function(id, lnglats, range, extendAttr){
	if(this.existPolyline(id)){
		this.removePolylineById(id);
	}
	
	var obj = this;
	var polylineOptions,polyline,extObj;
	polylineOptions = $.extend(
		{
			map:this.map,
			path:lnglats,
			strokeColor:"#FFFFFF", //线颜色
			strokeOpacity:0.4, //线透明度 
			strokeWeight:this.distanceToPix(range*2), //线宽
			strokeStyle:"solid", //线样式
			isOutline:true,//线条是否带描边
			outlineColor:"#E6D5FB"//线条描边颜色
		},
		extendAttr ? extendAttr : {}
	);
	
	polyline = new AMap.Polyline(polylineOptions);
	
	extObj = {
		events:{
			click:{
				listener:null,
				handler:null,
				instance:null
			}
		},
		data:null,
		overlayType:"polyline",
		markers:[]
	};
	//添加拐点
	for(var i=0;i<lnglats.length;i++){
		var lnglat = lnglats[i];
		try{
			var marker = new AMap.Marker({
				map:this.map,
				position:lnglat,//位置
				offset:new AMap.Pixel(-6,-7),
				icon:this.app+'/js/amap/images/circle.png' //复杂图标
			});
			extObj.markers.push(marker);
		}catch(e){
			//alert(e);
		};
	}
	//地图缩放事件
	var eventListener = AMap.event.addListener(this.map, "zoomchange", function(e){
		polyline.setOptions({strokeWeight:obj.distanceToPix(range*2)});
		var markers = extObj.markers;
		if(obj.map.getZoom() >= 15){
			for(var i=0;i<markers.length;i++){
				markers[i].show();
			}
		}else{
			for(var i=0;i<markers.length;i++){
				markers[i].hide();
			}
		}
	});
	extObj.events["zoomchange"] = {listener:eventListener, instance:this.map};
	AMap.event.trigger(this.map, "zoomchange");
	
	polyline.setExtData(extObj);//用户自定义拓展属性
	//添加事件
	this.addListeners(polyline, polyline);
	//添加线到地图容器
	this.addOverlay("polyline", id, polyline);
	return polyline;
}

/**
 * 设置覆盖物的内容显示
 * overlay：覆盖物
 * content：文本内容
 * imageUrl：图片地址 
 **/
Amap.prototype.setContent = function(overlay, content, imageUrl){
	if(overlay){
		//自定义点标记内容 
		var markerContent = document.createElement("div");
		markerContent.className = "markerContentStyle";
		
		//点标记中的图标
		var markerImg= document.createElement("img");
	    markerImg.className="markerlnglat";
	    if(imageUrl){
	    	markerImg.src=imageUrl;
	    }else{
	    	markerImg.src=this.app+"/js/amap/images/l_blue.png";
	    }
		markerContent.appendChild(markerImg);
		
		//点标记中的文本
		var markerSpan = document.createElement("span");
		markerSpan.innerHTML = content;
		markerContent.appendChild(markerSpan);
		
		overlay.setContent(markerContent);
	}
}

/**
 * 添加轨迹动画线
 * 
 * lng：经度
 * lat：纬度
 * lnglats：线的经纬度集合
 * isAttachSequenceMarker：附加编号点 true添加 false不添加
 **/
Amap.prototype.addTrackAnimation = function(lng, lat, lnglats, isAttachSequenceMarker){
	var obj = this;
	var lnglat = this.getLnglat(lng, lat);
	var lnglatArr = this.getLnglatArr(lnglats, ";");
	var extObj = {
		events:{
			click:{
				listener:null,
				handler:null,
				instance:null
			}
		},
		data:null,
		overlayType:"marker",
		markers:[]
	};
	//添加点
	var addMarkers = function(){
		//不添加附加点
		if(!isAttachSequenceMarker){
			return false;
		}
		for(var i=0;i<lnglatArr.length;i++){
			var lnglat = lnglatArr[i];
			try{
				var marker = new AMap.Marker({
					map:obj.map,
					position:lnglat,//位置
					zIndex:10,
					offset:new AMap.Pixel(-10,-30),
					icon:obj.app+'/js/amap/images/number/lan_'+(i+1)+'.png' //复杂图标
				});
				extObj.markers.push(marker);
			}catch(e){
				//alert(e);
			};
		}
	}
	//删除点
	var removeMarkers = function(){
		//没有附加点
		if(!isAttachSequenceMarker){
			return false;
		}
		var markers = obj.trackMarker.getExtData().markers;
		if(markers){
			for(var i=0;i<markers.length;i++){
				obj.removeOverlay(markers[i]);
			}
		}
	}
	if(this.trackMarker){
		removeMarkers();
		this.trackMarker.setPosition(lnglat);
		this.trackPolyline.setPath(lnglatArr);
		this.linePath=lnglatArr;
		this.animationBeginPosition = lnglat;//轨迹开始点
		this.moveMarker=this.trackMarker;
		this.stopAnimation();//停止上次的轨迹播放
	}else{
		//添加运动的点
		this.trackMarker = new AMap.Marker({
			map:this.map,
			position:lnglat,//基点位置
			icon:this.app+"/js/amap/images/walkman.png", //marker图标，直接传递地址url
			offset:new AMap.Pixel(-15,-48), //相对于基点的位置
			autoRotation:true,
			zIndex:800
		});
		
		//绘制轨迹
		this.trackPolyline = new AMap.Polyline({
			map:this.map,
			path:lnglatArr,
			strokeColor:"#00A",//线颜色
			strokeOpacity:1,//线透明度
			strokeWeight:3,//线宽
			strokeStyle:"solid"//线样式
		});
		
		this.linePath=lnglatArr;
		this.animationBeginPosition = lnglat;//轨迹开始点
		this.moveMarker = this.trackMarker;
		AMap.event.addListener(this.trackMarker,"moving",function(){
			obj.moveMarker = this;
		});
		AMap.event.addListener(this.trackMarker,"movealong",function(){
			obj.linePath = obj.trackPolyline.getPath();
			obj.moveMarker = obj.trackMarker;
		});
	}
	
	addMarkers();
	this.trackMarker.setExtData(extObj);
	
	var overlayArr = [];
	overlayArr.push(this.trackMarker);
	overlayArr.push(this.trackPolyline);
	this.map.setFitView(overlayArr);
}

/**
 * 移除轨迹播放
 **/
Amap.prototype.removeTrackAnimation = function(){
	this.removeOverlay(this.trackMarker);
	this.removeOverlay(this.trackPolyline);
	this.trackMarker = null;
	this.trackPolyline = null;
}

/**
 * 开始轨迹播放
 * speed：播放速度 单位：千米/小时
 * loop:是否循环执行动画
 **/
Amap.prototype.startAnimation = function(speed, loop){
	if(this.trackMarker){
		if(!speed) speed=3000;
		if(loop == null || loop == undefined) loop=false;
		this.trackMarker.moveAlong(this.linePath, speed, null, loop);
	}
}

/**
 * 停止轨迹播放（重新回到轨迹播放开始点）
 **/
Amap.prototype.stopAnimation = function(){
	if(this.trackMarker){
		this.trackMarker.stopMove();
		this.linePath = this.trackPolyline.getPath();
		this.moveMarker.setPosition(this.animationBeginPosition);//重新设置起始点
	}
}

/**
 * 暂停轨迹播放
 **/
Amap.prototype.pauseAnimation = function(){
	if(this.trackMarker){
		var point0 = this.moveMarker.getPosition();//当前轨迹点的位置
		var linePath1 = this.linePath;//上次轨迹路径
		var linePath2 = [];//最新轨迹
		for(var i=0;i<linePath1.length-1;i++){
			var point1 = linePath1[i];
			var point2 = linePath1[i+1];
			var total = point1.distance(point2);
			var line1 = point0.distance(point1);
			var line2 = point0.distance(point2);
			if(parseInt(total) == parseInt((line1+line2))){//判断三点一线
				linePath2.push(point0);
				while(i++<linePath1.length){
					if(linePath1[i]){
						linePath2.push(linePath1[i]);
					}
				}
				this.linePath = linePath2;
				break;
			}
		}
		this.trackMarker.stopMove();
	}
}

/**
 * 通过覆盖类型、编号删除对象
 * overlayType：覆盖类型
 * overlayId：编号
 **/
Amap.prototype.removeOverlayById = function(overlayType, overlayId){
	var overlay = this.getOverlayById(overlayType, overlayId);
	if(overlay){
		if(overlayType == "polyline"){
			var markers = overlay.getExtData().markers;
			if(markers){
				for(var i=0;i<markers.length;i++){
					this.removeOverlay(markers[i]);
				}
				overlay.getExtData().markers.length = 0;
			}
		}
		
		this.removeOverlay(overlay);
		delete this.container[overlayType][overlayId];
	}
}

/**
 * 通过覆盖类型、编号数组删除对象
 * overlayType：覆盖类型
 * overlayId：编号
 **/
Amap.prototype.removeOverlayByIds = function(overlayType, overlayIds){
	for(var i=0;i<overlayIds.length;i++){
		this.removeOverlayById(overlayType, overlayIds[i]);
	}
}

//删除标注物对象
Amap.prototype.removeMarkerById = function(markerId){
	if(markerId instanceof Array){
		this.removeOverlayByIds("marker", markerId);
	}else{
		this.removeOverlayById("marker", markerId);
	}
}

//删除线对象
Amap.prototype.removePolylineById = function(polylineId){
	if(polylineId instanceof Array){
		this.removeOverlayByIds("polyline", polylineId);
	}else{
		this.removeOverlayById("polyline", polylineId);
	}
}

//删除多边形对象
Amap.prototype.removePolygonById = function(polygonId){
	if(polygonId instanceof Array){
		this.removeOverlayByIds("polygon", polygonId);
	}else{
		this.removeOverlayById("polygon", polygonId);
	}
}

/**
 * 设置信息窗口内容
 **/
Amap.prototype.createInfoWindow = function(title, content){
	$("div[class='info']").remove();//删除指定元素
	
	var infoWindow = this.infoWindow;
	var info = document.createElement("div");
	info.className = "info";
	
	//可以通过下面的方式修改自定义窗体的宽高
	//info.style.width = "400px";

	// 定义顶部标题
	var top = document.createElement("div");
	top.className = "info-top";
	var titleD = document.createElement("div");
	titleD.innerHTML = title;
	var closeX = document.createElement("img");
	closeX.src = this.app+"/js/amap/images/close2.gif";
	closeX.onclick = function(){
		infoWindow.close();
	};
	
	top.appendChild(titleD);
	top.appendChild(closeX);
	info.appendChild(top);
	
	// 定义中部内容
	var middle = document.createElement("div");
	middle.className = "info-middle";
	middle.style.backgroundColor='white';
	middle.innerHTML = content;
	info.appendChild(middle);
	
	// 定义底部内容
	var bottom = document.createElement("div");
	bottom.className = "info-bottom";
	bottom.style.position = 'relative';
	bottom.style.top = '0px';
	bottom.style.margin = '0 auto';
	var sharp = document.createElement("img");
	sharp.src = this.app + "/js/amap/images/sharp.png";
	bottom.appendChild(sharp);	
	info.appendChild(bottom);
	
	return info;
}

/**
 * 打开地图信息窗口
 * lnglat：打开地方
 * title:信息窗口标题
 * content:信息窗口内容
 **/
Amap.prototype.openInfoWindow = function(lnglat, title, content){
	this.infoWindow.setContent(this.createInfoWindow(title, content));
	this.infoWindow.open(this.map, lnglat);
}

/**
 * 通过地图对象关闭信息窗口
 */
Amap.prototype.closeInfoWindow = function(){
	this.map.clearInfoWindow();
}

/**
 * 设置地图信息窗口内容
 * 
 * data:数据源
 * labels:需要列出的数据标签 结构形式如[{lname:'姓名',lvalue:'name'},{lname:'地址',lvalue:'address'}]
 * data_type:数据对象类型（JSON、JQUERY、TEXT）
 **/
Amap.prototype.setInfoWindowContent = function(data, labels, data_type){
	var htmlContent = "<table class='detail_table' style='width: 99%;font-size: 12px;' align='center'>";
	for(var i=0;i<labels.length;i++){
		var label = labels[i];
		htmlContent += "<tr><td width='35%' class='outDetail' align='right'>" + label.lname + "：</td>"
					+ "<td width='65%' class='outDetail2'>" + this.getDataValue(data, label.lvalue, data_type) + "</td></tr>";
	}
	htmlContent += "</table>";
	
	return htmlContent;
}

Amap.prototype.addMapListener = function(overlay, eventName, handler, context){
	this.addOverlayListener(this.map, "map", eventName, handler, context, overlay);
}

/**
 * 给点覆盖物添加事件
 * markerOverlay
 * eventName
 * handler
 * context
 **/
Amap.prototype.addMarkerListener = function(markerOverlay, eventName, handler, context){
	this.addOverlayListener(markerOverlay, "marker", eventName, handler, context, markerOverlay);
}

/**
 * 给多边形添加事件
 * polygonOverlay
 * eventName
 * handler
 * context
 **/
Amap.prototype.addPolygonListener = function(polygonOverlay, eventName, handler, context){
	this.addOverlayListener(polygonOverlay, "polygon", eventName, handler, context, polygonOverlay);
}

/**
 * 给点覆盖物添加事件
 * markerOverlay
 * overlayType
 * eventName
 * handler
 * context
 **/
Amap.prototype.addOverlayListener = function(instance, overlayType, eventName, handler, context, overlay){
	var eventArr = this.eventType[overlayType];
	var existEventName = false;//是否存在事件名称
	for(var i=0;i<eventArr.length;i++){
		if(eventArr[i] == eventName){
			existEventName = true;
		}
		if(existEventName){
			break;
		}
	}
	if(!existEventName){
		alert("该对象不存在"+eventName+"事件！");
		return false;
	}
	var extData = overlay.getExtData();
	var events = extData.events;
	if(events[eventName]){
		this.removeListener(events[eventName].listener);//删除之前的事件，重新添加
	}else{
		overlay.getExtData().events[eventName] = {listener:null,handler:null,instance:null};
	}
	this.addListener(instance, eventName, handler, context, overlay);
}

/**
 * instance:注册事件的对象
 * context:事件上下文（可选，缺省时，handler中this指向参数instance引用的对象，否则this指向context引用的对象）
 * overlay:添加事件的对象
 */
Amap.prototype.addListeners = function(context, overlay){
	var extData = overlay.getExtData();
	var events = extData.events;
	var overlayType = extData.overlayType;
	var eventArr = this.eventType[overlayType];
	//为指定覆盖物添加事件
	for(var i=0;i<eventArr.length;i++){
		var eventName = eventArr[i];
		if(events[eventName]){//事件存在
			if(events[eventName].handler){//handler 未定义或者为空
				var instance = events[eventName].instance ? events[eventName].instance : overlay;
				this.addListener(instance, eventName, events[eventName].handler, context, overlay);
			}
		}
	}
}

/**
 * 注册地图事件对象
 * instance:注册事件的对象
 * eventName:事件名称
 * handler:事件功能函数
 * overlay:主动注册事件的覆盖物
 * context:事件上下文（可选，缺省时，handler中this指向参数instance引用的对象，否则this指向context引用的对象）
 **/
Amap.prototype.addListener = function(instance, eventName, handler, context, overlay){
	var eventListener = AMap.event.addListener(instance, eventName, handler, context);
	overlay.getExtData().events[eventName].listener = eventListener;
	overlay.getExtData().events[eventName].instance = instance;
}

Amap.prototype.trigger = function(instance, eventName){
	AMap.event.trigger(instance, eventName);
}

Amap.prototype.removeOverlay = function(overlay){
	if(!overlay){
		return ;
	}
	//清除覆盖物对象上的所有监听事件
	this.removeOverlayListeners(overlay);
	//清除覆盖物
	try{
		overlay.setMap(null);
	}catch(et){};
}

/**
 * 清除指定覆盖物数组
 * overlayArr：地图覆盖物数组
 **/
Amap.prototype.removeOverlays = function(overlayArr){
	for(var i=0;i<overlayArr.length;i++){
		var obj = overlayArr[i];
		if(obj == undefined){
			continue;
		}
		removeOverlay(obj);
	}
	overlayArr.length = 0;
}

/**
 * 清除覆盖物对象上的所有监听事件
 * overlay : 覆盖物
 **/
Amap.prototype.removeOverlayListeners = function(overlay){
	if(overlay.getExtData()){
		if(overlay.getExtData().events){
			var events = overlay.getExtData().events;
			for(var eventName in events){
				this.removeListener(events[eventName].listener);
			}
		}
	}
}

Amap.prototype.removeListener = function(listener){
	if(listener){
		AMap.event.removeListener(listener);
	}
}

//清除地图上所有的覆盖物（不包含覆盖物上的监听事件）
Amap.prototype.clearMap = function(){
	this.map.clearMap();
}

/**
 * 获取数据值
 * 
 * data:数据
 * key:数据属性
 * data_type:数据对象类型（JSON JQUERY）
 **/
Amap.prototype.getDataValue = function(data, key, data_type){
	if(data_type == "JSON"){
		return data[key] != null ? data[key] : "";
	}else if(data_type == "JQUERY"){
		return data.attr(key);
	}else if(data_type == "TEXT"){
		return key;
	}else{
		return data;
	}
}

Amap.prototype.printObj = function(obj){
	for(var key in obj){
		alert(key + " = " + obj[key]);
	}
}