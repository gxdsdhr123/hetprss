<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
		<title>航班动态预排甘特图页面--拖飞机页面弹窗</title>
		<meta name="decorator" content="default" />
		<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
		<%@include file="/WEB-INF/views/include/edittable.jsp"%>
		<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
		<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" type="text/css">
		<link rel="stylesheet"  href="${ctxStatic}/prss/flightdynamic/css/fdWalkthroughGanttDragFlight.css"/>
	</head>
	<body>
		<input id="mainId" type="text" value="${id}" style="display:none;"/>
		<input id="fltId" type="text" value="${fltId}" style="display:none;"/>
		<input id="fltNo" type="text" value="${fltNo}" style="display:none;"/>
		<input id="start" type="text" value="${start}" style="display:none;"/>
		<input id="end" type="text" value="${end}" style="display:none;"/>
		<div id="container">
			<div id="top" style="padding-top:10px;">
				<div class="layui-form-item" style="display: flex;">
					<label class="layui-form-label">航班号：</label>
					<div class="layui-input-inline">
						<input id="fltNum" class="layui-input content-c " value="${fltNum}" name="fltNum" maxlength="50" disabled="disabled" />
					</div>
					<label class="layui-form-label">机号：</label>
					<div class="layui-input-inline">
						<input id="airCode" class="layui-input content-c " value="${airCode}" name="airCode" maxlength="50" disabled="disabled" />
					</div>
				</div>
				<div class="layui-form-item" style="display: flex;">
					<label class="layui-form-label">原机位：</label>
					<div class="layui-input-inline">
						<input id="saveActstand" class="layui-input content-c " value="${saveActstand}" name="saveActstand" maxlength="50" disabled="disabled"/>
					</div>
					<label class="layui-form-label">拖至机位：</label>
					<div class="layui-input-inline">
						<input id="tempActstand" class="layui-input content-c " value="${TEMPACTSTAND}" name="tempActstand" maxlength="50"/>
					</div>
				</div>
				<div class="layui-form-item" style="display: flex;">
					<label class="layui-form-label">预计开始时间：</label>
					<div class="layui-input-inline">
						<input type='text' maxlength="20" name='sBeginTime' id="sBeginTime"
							placeholder='请选择日期' class='form-control'
							onclick="WdatePicker({dateFmt:'yyyyMMdd HH:mm',minDate:'${start}',maxDate:'#F{$dp.$D(\'sEndTime\')||\'${end}\'}'});" />
					</div>
					<label class="layui-form-label">预计结束时间：</label>
					<div class="layui-input-inline">
						<input type='text' maxlength="20" name='sEndTime' id="sEndTime"
							placeholder='请选择日期' class='form-control'
							onclick="WdatePicker({dateFmt:'yyyyMMdd HH:mm',maxDate:'${end}',minDate:'#F{$dp.$D(\'sBeginTime\')||\'${start}\'}'});" />
					</div>
				</div>
				<button id="saveBut" class="layui-btn layui-btn-small pull-right" type="button" style="width:100px;margin-right:70px;">
				 	保&nbsp;&nbsp;&nbsp;&nbsp;存
				</button>
			</div>
			<div style="clear:both;"></div>
			<div class="title">
				数据采集
			</div>
			<div class="layui-form-item" style="display: flex;">
				<label class="layui-form-label">实际开始时间：</label>
				<div class="layui-input-inline">
					<input id="aBeginTime" class="layui-input content-c " value="${aBeginTime}" name="aBeginTime" maxlength="50"/>
				</div>
				<label class="layui-form-label">实际结束时间：</label>
				<div class="layui-input-inline">
					<input id="aEndTime" class="layui-input content-c " value="${aEndTime}" name="aEndTime" maxlength="50"/>
				</div>
			</div>
			<div class="title" style="margin-bottom:0px;">
				人工确认
			</div>
			<div class="taskContainer">
				
			</div>
		</div>
		<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/fdWalkthroughGanttDragFlight.js"></script>
	</body>
</html>