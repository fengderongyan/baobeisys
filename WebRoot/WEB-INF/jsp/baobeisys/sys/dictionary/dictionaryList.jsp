<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<%@ include file="/inc/includeBase.jsp"%>
	</head>
	<body>
		<display:table name="list" requestURI="${app}/sys/dictionary/dictionaryList.do"
			class="list" id="row" cellspacing="0" style="width:100%"
			cellpadding="0" pagesize="${param.pageSize}" export="false" sort="external"  partialList="true" size="size">
			<display:column title="序号" style="width:32px;" media="html">
				<c:out value="${row_rowNum + beginIndex}" />
			</display:column>
			<display:column title="字典类型编码" property="DATA_TYPE_CODE" />
			<display:column title="字典类型名称" property="DATA_TYPE_NAME" />
			<display:column title="字典信息编码" property="DD_ITEM_CODE"  />
			<display:column title="字典信息名称" property="DD_ITEM_NAME" />
			
			<display:column title="操作" media="html" style="width:150px;">
				<sgy:button cssClass="smallBtn_gray"
					onclick="editInfo('${row.id}','edit');return false;">修改</sgy:button>&nbsp;
				<sgy:button cssClass="smallBtn_gray"
					onclick="deleteInfo('${row.id}');return false;">删除</sgy:button>&nbsp;
			</display:column>
		</display:table>
	</body>
	<script type="text/javascript">
		$(document).ready(function(){
			//启用frame查询按钮
			window.parent.enable($('#schbtn', parent.document));  
		});
		
    	function editInfo(id, method) {	
      　　		MyWindow.OpenCenterWindowScroll('${app}/sys/dictionary/dictionaryEdit.do?method='+method+'&id='+id,'dictCfgEdit',400,700);
    	}
    
	    function deleteInfo(id){
		 	if(!confirm('确定要删除该字典信息？')) {
				return false;
			}
			var url = '${app}/sys/dictionary/deleteDictionaryCfg.do';
			var params="id="+id;
			var res = new MyJqueryAjax(url,params).request();
			if(res==1) {
				alert('数据操作成功！');
		  	} else {
				alert('数据操作失败！');
		  	}
		  	window.parent.$('#form1').submit();
		}
	</script>
</html>