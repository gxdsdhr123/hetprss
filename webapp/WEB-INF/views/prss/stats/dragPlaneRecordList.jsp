<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
		<title>拖飞机记录</title>
		<meta name="decorator" content="default" />
		<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
		<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
		<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
		<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript" src="${ctxStatic}/prss/stats/dragPlaneRecordList.js"></script>
		<style>
        .layui-input {
            display: block;
            width: 100%;
            padding-left: 10px;
        }
    </style>
		
	</head>
	<body>
		<div id="container">
				<div class="layui-form-item" id="toolbar">
					<div style="width: 100%">
						<span>航班号</span>
						<div style="width: 100px" class="layui-inline">
							<input style="width: 100px" type='text' name='fltNo' id="fltNo" class='layui-input' onfocus="setNullValue(this)"/>
						</div>
						<span>机号</span>
						<div style="width: 100px" class="layui-inline">
							<input style="width: 100px" type='text' name='aircraftNumber' id="aircraftNumber" class='layui-input' onfocus="setNullValue(this)"/>
						</div>
						<span>创建日期</span>
						<div style="width: 100px" class="layui-inline">
							<input style="width: 100px" type='text' name='startDate' id="startDate"
								placeholder='请选择日期' class='layui-input'
								onclick="WdatePicker({dateFmt:'yyyyMMdd'});" />
						</div>
						<span>操作人</span>
						<div style="width: 100px" class="layui-inline">
							<input style="width: 100px" type='text' name='userName' id="userName" class='layui-input' onfocus="setNullValue(this)"/>
						</div>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<div class="layui-btn-group">
							<button id="search" class="layui-btn  layui-btn-primary" type="button">
							 	查询
							</button>
							<button id="down" class="layui-btn  layui-btn-primary" type="button">
								打印
							</button>
						</div>
					</div>
				</div>
			<div id="baseTables">
				<table id="baseTable"></table>
			</div>
		</div>
		<!-- 导出excel -->
		<form id="exportForm" method="post"></form>
	</body>
</html>