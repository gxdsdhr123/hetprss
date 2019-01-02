<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>历史消息</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css"
	rel="stylesheet" />
<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
	<input id="num" name="num" type="hidden" value="${num}" />
	<input id="aid" name="aid" type="hidden" value="${aid}" />
	<input id="fltid" name="fltid" type="hidden" value="${fltid}" />
	<div class="row">
		<div class="col-md-12 col-xs-12 col-sm-12">
			<div class="box-body">
				<div class="layui-inline">
					<label class="layui-form-label">航班日期</label>
					<div class="layui-input-inline">
						<div class="input-group">
						<c:if test="${num==2 }">
							<input type='text' maxlength="20" name='mflightdate'
								placeholder='请选择日期' class='form-control'
								onclick="WdatePicker({dateFmt:'yyyyMMdd'});" />						
						</c:if>
						<c:if test="${num!=2 }">
							<input type='text' maxlength="20" name='mflightdate'
								placeholder='请选择日期' class='form-control'
								onclick="WdatePicker({dateFmt:'yyyyMMdd',minDate:'%y-%M-{%d-1} {%H-48}:%m:%s',maxDate:'%y-%M-%d %H:%m:%s'});" />
						</c:if>
						</div>
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">航班号</label>
					<div class="layui-input-inline">
						<input class="layui-input" type="text" name="flightnumber">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">消息标题</label>
					<div class="layui-input-inline">
						<input class="layui-input" type="text" name="mtitle">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">消息内容</label>
					<div class="layui-input-inline">
						<input class="layui-input" type="text" name="mtext">
					</div>
				</div>
				<div class="bs-bars pull-right">
					<div id="toolbar">
						<button type="button" class="btn btn-link  search">查询</button>
						<c:if test="${num==2}">
							<button id="print" type="button" class="btn btn-link">打印</button>
						</c:if>
					</div>
				</div>
				<table id="baseTable"
					class="table table-striped table-bordered table-hover"></table>
			</div>
		</div>
	</div>
	<form id="printForm" action="${ctx}/message/history/print"
		method="post" style="display: none">
		<textarea id="printTitle" name="title"></textarea>
		<textarea id="printmflightdate" name="mflightdate"></textarea>
		<textarea id="printflightnumber" name="flightnumber"></textarea>
		<textarea id="printmtitle" name="mtitle"></textarea>
		<textarea id="printmtext" name="mtext"></textarea>
		<textarea id="printnum" name="printnum"></textarea>
	</form>

	<div id="fileList" style="display: none">

		<input id="fileInput" name="files" style="display: none"
			onchange="fileOnChange()" type="file" multiple="multiple" />
		<table class="layui-table" style="margin: 0px;">
			<thead>
				<tr>
					<th>文件名</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody id="fileTable"></tbody>
		</table>
		<input id="downloadFile" name="downloadFile" style="display: none" />
	</div>
	<script type="text/javascript"
		src="${ctxStatic}/prss/message/historyMessage.js"></script>
</body>
</html>