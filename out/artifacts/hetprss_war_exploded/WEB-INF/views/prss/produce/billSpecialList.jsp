<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>非例行服务收费单</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript"
	src="${ctxStatic}/prss/produce/billSpecialList.js"></script>
	<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
</head>
<body>

	<div id="toolbar">
		<button id="newBtn" type="button" class="btn btn-link">新增单据</button>
		<button id="editBtn" type="button" class="btn btn-link">修改单据</button>
		<button id="delBtn" type="button" class="btn btn-link">删除单据</button>
		<button id="wordBtn" type="button" class="btn btn-link">下载单据</button>
		<button id="excelBtn" type="button" class="btn btn-link">导出列表</button>
		<div class="layui-inline">
			<label class="layui-form-label" style="padding: 10px 10px !important;margin-bottom: 0px;">数据日期</label>
			<div class="layui-input-inline">
				<input id="searchDate" name="searchDate" onClick="dateFilter();" class="layui-input" readonly="readonly"
					value="" type="text">
			</div>
		</div>
        <button id="btnSubmit" class="layui-btn layui-btn-small layui-btn-primary search" type="button">
            <i class="fa fa-search">&nbsp;</i>筛选
        </button>
<!-- 		<div class="layui-inline"> -->
<!-- 			<label class="layui-form-label">搜索</label> -->
<!-- 			<div class="layui-input-inline"> -->
<!-- 				<input class="layui-input" type="text" name="searchKeyDown" id="searchKeyDown" /> -->
<!-- 			</div> -->
<!-- 		</div> -->
	</div>
	
	<table id="baseTable"></table>
	<form id="printWord" action="${ctx}/produce/special/exportWord" method="post" style="display: none">
			<input type="hidden" id="id" name="id"/>
	</form>
	<form id="printExcel" class="layui-form" action="${ctx}/produce/special/exportExcel" method="post" style="display: none">
		<div class="layui-form-item" id="chooseDate">
		    <label class="layui-form-label">导出开始日期</label>
		    <div class="layui-input-inline">
		      <input type='text' name="startDate" id="startDate" placeholder='如不填写，则导出全部'
								onclick="WdatePicker({dateFmt:'yyyyMMdd'});"
								class='form-control'/>
		    </div>
  		</div>
  		<div class="layui-form-item" id="chooseDate">
		    <label class="layui-form-label">导出结束日期</label>
		    <div class="layui-input-inline">
		      <input type='text' name="endDate" id="endDate" placeholder='如不填写，则导出全部'
								onclick="WdatePicker({dateFmt:'yyyyMMdd'});"
								class='form-control'/>
		    </div>
  		</div>
	</form>
</body>
</html>