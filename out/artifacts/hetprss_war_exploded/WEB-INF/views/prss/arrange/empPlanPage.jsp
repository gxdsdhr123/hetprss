<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/prss/arrange/css/empPlanList.css" rel="stylesheet" />
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<title>人员计划</title>
<script type="text/javascript">
var dateTp = "${selDate}";
var currentDate = "${currentDate}";
</script>
</head>
<body>
	<div id="toolbar" >
		<button id="addBtn" type="button" class="btn btn-link">增加</button>
		<button id="modifyBtn" type="button" class="btn btn-link">修改</button>
		<button id="deleteBtn" type="button" class="btn btn-link">删除</button>
		<button id="orderBtn" type="button" class="btn btn-link">工作排序</button>
		<button id="stopBtn" type="button" class="btn btn-link">停止工作</button>
		<button id="resumeBtn" type="button" class="btn btn-link">恢复工作</button>
	</div>
	<table id="dataGrid"></table>
	
	<div id="planRangeDate" style="display:none">
			<div class="layui-form-item">
				<label class="layui-form-label planRangeLable">开始时间</label>
				<div class="layui-input-inline planRangeDiv">
			    	<input class='layui-input rangeDate' type='text' placeholder='开始时间' 
			    		onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-{%d}',maxDate:'#F{$dp.$D(\'stopEnd\')}'})" id="stopStart">
			    </div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label planRangeLable">结束时间</label>
				<div class="layui-input-inline planRangeDiv">
			    	<input class='layui-input rangeDate' type='text' placeholder='结束时间' 
			    		onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\'stopStart\')}'})" id="stopEnd">
			    </div>
			</div>
			<div class="layui-form-item">
		    <label class="layui-form-label planRangeLable">停用原因</label>
		    <div class="layui-input-inline planRangeDiv">
		    	<select name="reason" id="reason">
		      		<option value="1">工作原因</option>
		      		<option value="2">非工作原因</option>
		      		<option value="3">其它</option>
	     	  </select>
	     	  </div>
			</div>
	</div>
	
	<script type="text/javascript" src="${ctxStatic}/prss/arrange/empPlanPage.js"></script>
</body>
</html>