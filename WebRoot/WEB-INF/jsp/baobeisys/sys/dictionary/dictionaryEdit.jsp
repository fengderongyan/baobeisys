<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/inc/includeBase.jsp" %>
	<c:choose>
		<c:when test="${param.method=='edit'}">
			<c:set var="title" value="修改字典信息"></c:set>
		</c:when>
		<c:when test="${param.method=='create'}">
			<c:set var="title" value="新增字典信息"></c:set>
		</c:when>
		<c:when test="${param.method=='show'}">
			<c:set var="title" value="字典详细信息"></c:set>
		</c:when>
	</c:choose>
	<title>${title}</title>
</head>
<body>
	<form id="form1" name="form1" action="${app}/sys/dictionary/saveOrUpdate.do" method="post">
		<div id="queryPanel" class="queryPanel">
			<div id="queryPanel_title" class="queryPanel_title">
				<div class="queryPanel_title_collapse"></div>&nbsp;${title}
			</div>
		    <div id="queryPanel_content" class="queryPanel_content">
				<table class="detail_table" cellspacing="0" cellpadding="0" style="width: 100%;">
					<span>
					<tr id="addInfo">
						<td  class="outDetail">
							字典类型：
						</td>
						<td class="outDetail2" colspan="3">
							<input type="text" id="data_type_code1" name="data_type_code1" value="" 
								msg="请填写字典类型！"/>
						</td>
					</tr>
					<tr id="addTypeInfo">
						<td class="outDetail">
							字典类型：
						</td>
						
						<td class="outDetail2" colspan="3">
							<sgy:select list="typeList" name="data_type_code2" id="data_type_code2" onchange="changType();"
								headLabel="请选择..." headValue="" dataType="Require" msg="请选择字典类型！" value="${dictionaryInfo.data_type_code }"
								optionLabel="data_type_name" optionValue="data_type_code" />
							<input type="hidden" id="dicId" name="dicId" value="${dictionaryInfo.id }"  />
							<input type="hidden" id="method" name="method" value="${method }"  />
							<input type="hidden" id="data_type_code" name="data_type_code" value=""  />
								&nbsp;&nbsp;&nbsp;
								<span id="rep_span" name="rep_span"></span>
						</td>
					</tr>
					<tr height="20">
						<td class="outDetail">
							<font color="red">*</font>&nbsp;字典信息编码：
						</td>
						<td class="outDetail2" colspan="3">
							<input type="text" id="dd_item_code" name="dd_item_code" value="${dictionaryInfo.dd_item_code }" 
								${param.method == 'edit' ? 'readonly' : '' }
								dataType="Number" msg="请填写数字类型的字典信息编码！" />
						</td>
					</tr>
					<tr height="20">
						<td class="outDetail">
							<font color="red">*</font>&nbsp;排序：
						</td>
						<td class="outDetail2" colspan="3">
							<input type="text" id="order_id" name="order_id" value="${dictionaryInfo.order_id }" 
								dataType="Number" msg="请填写数字类型的字典信息编码！" />
						</td>
					</tr>
					<tr height="20">
						<td class="outDetail">
							<font color="red">*</font>&nbsp;字典信息名称：
						</td>
						<td class="outDetail2" colspan="3">
							<input type="text" id="dd_item_name" name="dd_item_name" value="${dictionaryInfo.dd_item_name }" size="68"
								dataType="Require" msg="请填写字典信息名称！" />
						</td>
					</tr>
				</table>
		    </div>
		</div>
		<br />
		<p align="center">
			<c:if test="${param.method!='show'}">
				<sgy:button cssClass="ovalbutton" onclick="sav(this);return false;">保 存</sgy:button>
			</c:if>&nbsp;
			<sgy:button cssClass="ovalbutton" onclick="window.close();">关 闭</sgy:button>
		</p>
	</form>
</body>
<script type="text/javascript" defer="defer">
	$(document).ready(function(){
		//window.document.getElementById('addTypeInfo').style.display="none";
		$('#addInfo').hide();
	});
	
	function changeRadio(){
		var radValue = $('input:radio[name="addType"]:checked').val();
		if(radValue == 1){
			$('#addInfo').hide();
			$('#addTypeInfo').show();
			$('#data_type_code1').val('');
			$('#data_type_code1').removeAttr("dataType");
			$('#addTypeInfo2').attr('dataType', 'Require');
			//window.document.getElementById('addTypeInfo').style.display="none";
			//window.document.getElementById('addInfo').style.display="inline";
		}
		if(radValue == 2){
			$('#addTypeInfo').hide();
			$('#addInfo').show();
			$('#data_type_code2').val('');
			$('#data_type_code2').removeAttr("dataType");
			$('#addTypeInfo1').attr('dataType', 'Require');
			//window.document.getElementById('addInfo').style.display="none";
			//window.document.getElementById('addTypeInfo').style.display="inline";
		}
		
	}
	
	function changType(){
		var type = $('#data_type_code2').val();
		var url = '${app}/sys/dictionary/ajaxMaxInfoId.do';
	    var params = 'data_type_code=' + type;
 		var result = new MyJqueryAjax(url, params).request().trim();
  		$('#rep_span').html(result);
	}
	
	//保存
	function sav(src) {
		disable(src);
		if(!Validator.Validate('form1')) {
			enable(src);
			return false;
		}
		var v1 = $('#data_type_code1').val();
		var v2 = $('#data_type_code2').val();
		var v = '';
		if(v1 == ''){
			$('#data_type_code').val(v2);
			v = v2;
		}
		if(v2 == ''){
			$('#data_type_code').val(v1);
			v = v1;
		}
		var infoId = $('#dd_item_code').val();

		//验证信息编码不能重复
		var id = $('#dicId').val();
		var url = '${app}/sys/dictionary/checkInfoId.do?dd_item_code='+infoId+'&data_type_code='+v+'&id='+id+'&method='+'${method}';
		var result = new MyJqueryAjax(url).request();
		if(result>0){
			alert('同类型下信息编码已存在，请重新确认！');
			enable(src);
			return;
		}
		
		$('#form1').submit();
	}
	
</script>
</html>
