<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<%-- <%@include file="/WEB-INF/views/include/head.jsp"%> --%>
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>

<style type="text/css">
html,body{
	width:100% !important;
	height:100% !important;
	overflow:auto;
}
.layui-form select {
    display: initial;
    height: 100%;
}
.selected {
    position: absolute;
    height: 176px;
    z-index: 9;
}
.select2-results__options {
	height: 105px;
}
.select2-container .select2-selection--single {
    height: 34px; 
}
 .select2-container--default.select2-container--disabled .select2-selection--single{ 
     background-color: #002F63; 
 } 
 
 .select2-container--default .select2-selection--multiple .select2-selection__choice { 
	color: #222222; 
 } 
 .select2-container--default .select2-selection--multiple { 
 	background-color: white; 
 	border: 1px solid #D2D2D2 ; 
 	border-radius: 4px; 
 	cursor: text; 
 } 
 #select2-departType-results { 
 	position: relative; 
 } 
 
.mark_c {
    position: absolute;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.01);
    z-index: 9;
}
#departType {
	position: relative;
	margin: 0px auto;
	padding: 0px;
	width: 100%;
	height: 100%;
	overflow-y: auto;
}
</style>

<body >
	<div class="mark_c" style="display: block;"></div>
	<input type="hidden" id="officeId" value="${officeId }" />
	<div class="layui-form" style="float: left; margin-left: 10%; position: fixed;">
		<div class="layui-form-item"> 
			<label class="layui-form-label"><font color="red">*</font>部门：</label>
			<div class="layui-input-inline">
				<select name="departType" id="departType" class="select2 form-control departType" data-type="departType" style="width: 140%; " onchange="changeSelect();" <c:if test="${flag == 1 }">disabled</c:if>>					
					<option value="">请选择部门</option> 
					<c:forEach items="${officeList}" var="office">
						<option value="${office.ID}">${office.NAME}</option> 
					</c:forEach>
				</select>
			</div>


		</div>
		<div class="layui-form-item"> 
			<label class="layui-form-label">排序：</label>
			<div class="layui-input-inline">
				<input type="text" id="seqNum" maxlength="5" style="width: 140%; border-radius: 4px;" class='form-control select2 airplaneType' value="${seqNum }" onKeyPress="return changeSeqNum(event);">
			</div>
		</div>
	</div>
<script type="text/javascript">
	$("#departType option[value='${officeId }']").attr("selected",true);

	function changeSeqNum(evt) {
	    //火狐使用evt.which获取键盘按键值，IE使用window.event.keyCode获取键盘按键值
	    var ev = evt.which?evt.which:window.event.keyCode;
	    if((ev>=48&&ev<=57) || ev==8){
	        return true;    
	    }else{
	       return false;
	    }  
	};
	$('#seqNum').blur(function() {
		var seqNum = $('#seqNum').val();
		parent.seqNum = seqNum;
	});
	function changeSelect() {		
		$('#officeName').val($('#departType').find("option:selected").text());
		$('#officeId').val($('#departType').find("option:selected").val());
		parent.officeId = $('#officeId').val();
	}
	$(function() {		
		$('#departType').select2({  
			placeholder: "请选择部门",  
	        width:"140%",
	        language : "zh-CN",
			templateResult : formatRepo,
			templateSelection : formatRepoSelection,
			escapeMarkup : function(markup) {
				return markup;
			}
		});
		
	});
		
	function formatRepo(repo) {
		return repo.text;
	}
	// select2显示选项处理
	function formatRepoSelection(repo) {
		return repo.text;
	}
	
	window.onload = function() {
		// 取消遮罩
		setTimeout(function() {
			$('div[class=mark_c]').attr({'style': 'display: none;'});
		}, 600);
		
	}
</script>
</body>