<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>装卸工操作记录单</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript"
	src="${ctxStatic}/prss/produce/billOperatorZXList.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
</head>
<body>

	<div id="toolbar">

        <div class="layui-input-inline">
            <label class="layui-form-label">数据日期</label>
        </div>
        <div class="layui-input-inline" style="width: 100px;">
            <input id="dateStart" name="dateStart" class="layui-input" type="text" value="${defaultStart}"
                   onfocus="WdatePicker({dateFmt:'yyyyMMdd'})">
        </div>

        <div class="layui-input-inline">
            <label class="layui-form-label">航班号</label>
        </div>
        <div class="layui-input-inline" style="width: 100px;">
            <input id="flightNumber" name="flightNumber" class="layui-input" type="text">
        </div>

        <div class="layui-input-inline">
            <label class="layui-form-label">监装员</label>
        </div>
        <div class="layui-input-inline" style="width: 100px;">
            <input id="operator" name="operator" class="layui-input" type="text">
        </div>

        <button id="btnSubmit" class="layui-btn layui-btn-small layui-btn-primary search" type="button">
            查询
        </button>

        <button id="btnDetail" class="layui-btn layui-btn-small layui-btn-primary detail" type="button">
            查看
        </button>
	</div>

	<table id="baseTable"></table>
</body>
</html>