function escapeXML(str){
	if(!isNaN(str))
		return str;
	return str.replace(/%/g,"%25").replace(/'/g,"%26apos;").replace(/&/g,"%26")
		.replace(/"/g,"%22").replace(/</g,"&lt;").replace(/>/g,"&gt;");
}
if(typeof sgy == "undefined") var sgy = new Object();
if(typeof sgy.FusionChartsUtil == "undefined") sgy.FusionChartsUtil = new Object();
sgy.FusionCharts = function () {
	this.id = "";
	this.fChart = null;
	
	this.width = 640;
	this.height = 480;
	this.dataType = 'single';	// 数据格式:single mixed 
	this.objectData = [];
	this.seriesName = {};			// [{key:"c1", seriesName:"梨子", showType:"line"},{key:"c2", value:"香蕉"}];
	this.categoryName = '';			// "key1";
	this.showValues = 0;			// 是否显示图表节点上的文字  默认为1显示
	this.render = "";
	this.chartType = "";			// "Pie3D.swf"
	this.animation = 1;				// 图形显示时是否有动画的形式，默认是1，也就是有动画效果。 1/0
	this.caption = "";				// 图形显示的标题
	this.xAxisName = "";			// 横坐标文字描述
	this.yAxisName = "";			// 纵坐标文字描述
	this.PYAxisName = "";			// 主纵坐标文字描述
	this.SYAxisName = "";			// 次纵坐标文字描述
	
	this.baseFontSize = '12';		// 字体大小 默认为12px
	this.baseFont = '黑体';			// 字体 默认为宋体
	this.baseFontColor = 'black';	// 字体颜色默认为  黑色
	this.labelDisplay="Rotate"; 	// 横坐标是否旋转
	this.useRoundEdges= 1;			// 为2D图的时候  样式的变化  1/0
	this.numberSuffix= ''; 			// 数据前缀单位    如果要设置为 % 应设置为 %25
	this.yAxisMaxValue= '';   		// y轴最大值
	/*
	当chrtType为AngularGauge.swf
	有另外两个属性= showTickMarks是否显示数值， cylFillColor  背景色
	cloums=手动设置x轴的文字显示，如=["一月";"二月"]设置此属性的时候，categoryName属性不必设置。
	
	alarmValue= 设置告警线的值是多少
	alarmName=告警线的名称
	alarmColor =告警线的颜色
	*/
	this.chartAtributes = '';
	this.setAtributes = '';
	this.datasetAtributes = '';
	this.categoriesAtributes = '';
	this.categoryAtributes = '';
	this.xml = "";
}

if(typeof sgy.FusionChartsUtil.charts == "undefined") sgy.FusionChartsUtil.charts = new Object();
sgy.FusionChartsUtil.addChart = function(id, value) {
	this.charts[id] = value;
};
sgy.FusionChartsUtil.getChart = function(id){
	return this.charts[id];
};
sgy.FusionChartsUtil.makeChart = function (param) {
	var chart = this.getChart(param.id);
	if(chart == null) {
		chart = new sgy.FusionCharts();
		this.initChart(chart, param);
		this.makeXML(chart);
		var fChart = new FusionCharts('/baobeisys/FusionCharts/swf/' + chart.chartType + '.swf', chart.id, 
			chart.width, chart.height, '0', '1');
		chart.fChart = fChart;
		fChart.setDataXML(chart.xml);
		fChart.render(chart.render);
		this.addChart(param.id, chart);
	} else {
		this.initChart(chart, param);
		this.makeXML(chart);
		fChart.setDataXML(chart.xml);
		fChart.render(chart.render);
	}
}
/**
 * 初始化chart配置
 */
sgy.FusionChartsUtil.initChart = function (chart, param) {
	chart.id = param.id;
	chart.width = param.width || chart.width;
	chart.height = param.height || chart.height;
	chart.dataType = param.dataType || chart.dataType;
	chart.objectData = param.objectData || chart.dataType;
	chart.seriesName = param.seriesName || chart.seriesName;
	chart.categoryName = param.categoryName || chart.categoryName;
	chart.showValues = param.showValues || chart.showValues;
	chart.render = param.render || chart.render;
	chart.chartType = param.chartType || chart.chartType;
	chart.caption = param.caption || chart.caption;
	chart.subCaption = param.subCaption || chart.subCaption;
	chart.xAxisName = param.xAxisName || chart.xAxisName;
	chart.yAxisName = param.yAxisName || chart.yAxisName;
	chart.PYAxisName = param.PYAxisName || chart.PYAxisName;
	chart.SYAxisName = param.SYAxisName || chart.SYAxisName;
	chart.chartAtributes = param.chartAtributes || chart.chartAtributes;
	chart.setAtributes = param.setAtributes || chart.setAtributes;
	chart.datasetAtributes = param.datasetAtributes || chart.datasetAtributes;
	chart.categoriesAtributes = param.categoriesAtributes || chart.categoriesAtributes;
	chart.categoryAtributes = param.categoryAtributes || chart.categoryAtributes;
}
/**
 * 根据配置将json数据转化成xml字符串
 */
sgy.FusionChartsUtil.makeXML = function (chart) {
	var xml = chart.xml;
	var data = chart.objectData;
	xml = "<chart";
	
	xml += this.makeXMLAttr(chart, "caption");
	xml += this.makeXMLAttr(chart, "subCaption");
	xml += this.makeXMLAttr(chart, "xAxisName");
	if(/DY/.test(chart.chartType)) {
		xml += this.makeXMLAttr(chart, "PYAxisName");
		xml += this.makeXMLAttr(chart, "SYAxisName");
	} else {
		xml += this.makeXMLAttr(chart, "yAxisName");
	}
	xml += this.makeXMLAttr(chart, "baseFontSize");
	xml += this.makeXMLAttr(chart, "baseFont");
	xml += this.makeXMLAttr(chart, "baseFontColor");
	xml += this.makeXMLAttr(chart, "showValues");
	//xml += this.makeXMLAttr(chart, "labelDisplay");
	xml += this.makeXMLAttr(chart, "useRoundEdges");
	
	if(chart.chartAtributes) xml += " " + escapeXML(chart.chartAtributes) + " ";
	if(chart.setAtributes) xml += " " + escapeXML(chart.setAtributes) + " ";
	if(chart.datasetAtributes) xml += " " + escapeXML(chart.datasetAtributes) + " ";
	if(chart.categoriesAtributes) xml += " " + escapeXML(chart.categoriesAtributes) + " ";
	if(chart.categoryAtributes) xml += " " + escapeXML(chart.categoryAtributes) + " ";
	
	xml += ">";
	if(chart.dataType == 'single'){
		for(var i = 0; i < data.length; i++) {
			xml += "<set";
			xml += this.makeXMLAttr(data[i], "label", chart.categoryName);
			xml += this.makeXMLAttr(data[i], "value", chart.seriesName[0].key);
			xml += " />";
		}
	} else {
		var xmlds = "";
		var xmlCategorys = "<categories>";
		for(var i = 0; i < data.length; i++) {
			xmlCategorys += "<category"
			xmlCategorys += this.makeXMLAttr(data[i], "label", chart.categoryName);
			xmlCategorys += "/>"
		}
		xmlCategorys += "</categories>";

		for(var i = 0; i < chart.seriesName.length; i++) {
			xmlds += "<dataset";
			xmlds += " seriesName='" + escapeXML(chart.seriesName[i].seriesName) + "'";
			if(chart.seriesName[i].showType) xmlds += this.makeXMLAttr(chart.seriesName[i], "renderAs", "showType");
			if(/DY/.test(chart.chartType)) {
				if(chart.seriesName[i].parentYAxis) xmlds += this.makeXMLAttr(chart.seriesName[i], "parentYAxis");
			}
			xmlds += ">";
			for(var j = 0; j < data.length; j++) {
				xmlds += "<set";
				xmlds += this.makeXMLAttr(data[j], "value", chart.seriesName[i].key);
				xmlds += " />";
			}
			xmlds += "</dataset>";
		}
		xml += xmlCategorys + xmlds;
	}
	xml += "</chart>";
	chart.xml = xml;
	//alert(chart.xml);
}
/**
 * 添加xml属性 组装成obj="prop"的形式
 */
sgy.FusionChartsUtil.makeXMLAttr = function (obj, attr, prop) {
	if(prop == 'undefined' || prop ==null ){
		prop = attr;
	}
	if(obj[prop]==null){
		return "";
	}
	return " " + attr + "='" + escapeXML(obj[prop]) + "' ";
}
