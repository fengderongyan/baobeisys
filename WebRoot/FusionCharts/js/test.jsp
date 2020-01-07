<%@page language="java" contentType="text/html;charset=gbk"%>
<html>
<head>
	<script src="FusionCharts.js" type="text/javascript" ></script>
	<script src="sgy-fc-load.js" type="text/javascript" ></script>
</head>
<body style="text-align: center;">
	<div id="chart1" style="border:1px solid blue;height:450px;width:600px;">
	</div>
</body>
<script type="text/javascript">
	var object = {
		id:'chart1',
		width:600,
		height:450,
		dataType:'mixed',
		caption:'�����',
		subCaption:'�ӱ���',
		xAxisName:'X������',
		yAxisName:'Y������',
		PYAxisName:'Y��������',
		SYAxisName:'Y��������',
		objectData:[{key1:'201010',key2:'2000',key3:'3000'},{key1:'201011',key2:'2200',key3:'3200'}],
		seriesName:[{key:'key2', seriesName:'����', showType:'line'},{key:'key3', seriesName:'�㽶', showType:'area'}],
		categoryName:'key1', 
		render:"chart1", 
		chartType:"ScrollCombiDY2D"
	};
	sgy.FusionChartsUtil.makeChart(object);
	//alert(escapeXML("%dd%ee'ee'dd&ff&gg\"gg\"hh<hh<jj>jj>"));
	//var fChart = new FusionCharts('/ntreport/FusionCharts/swf/MSBar3D.swf', 'chart1', 400, 500, '0', '1');
	//fChart.setDataXML("<chart caption='�����' subCaption='�ӱ���' xAxisName='X������' yAxisName='Y������'><categories><category label='201010'/><category label='201011'/></categories><dataset seriesName='����' renderAs='line'><set value='2000' /><set value='2200' /></dataset><dataset seriesName='�㽶' renderAs='area'><set value='3000' /><set value='3200' /></dataset></chart>");
	//fChart.setDataURL("Data2.xml")
	//fChart.render('chart1');
	
</script>
</html>