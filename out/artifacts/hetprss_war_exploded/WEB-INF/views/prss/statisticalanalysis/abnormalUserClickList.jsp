<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
		<title>手持机异常/违规点击人员统计</title>
		<meta name="decorator" content="default" />
		<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
		<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
		<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
		<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript" src="${ctxStatic}/prss/statisticalanalysis/abnormalUserClickList.js"></script>
	</head>
	<body>
		<div id="container">
			<form class="layui-form">
				<div class="layui-form-item" id="toolbar">
					<div class="layui-inline" >
						<label class="layui-form-label">开始日期</label>
						<div style="width: 100px" class="layui-input-inline">
							<input style="width: 100px" type='text' maxlength="20" name='startDate' id="startDate"
								placeholder='请选择日期' class='form-control'
								onclick="WdatePicker({dateFmt:'yyyyMMdd',maxDate:'#F{$dp.$D(\'endDate\')}'});" />
						</div>
						<label style="width: 100px"class="layui-form-label">结束日期</label>
						<div style="width: 100px" class="layui-input-inline">
							<input type='text' maxlength="20" name='endDate' id="endDate"
								placeholder='请选择日期' class='form-control'
								onclick="WdatePicker({dateFmt:'yyyyMMdd',minDate:'#F{$dp.$D(\'startDate\')}'});" />
						</div>
						<label style="width: 100px"class="layui-form-label"></label>
						<div style="width: 150px" class="layui-input-inline">
							<select id="reskind" name="reskind" class="form-control select2">
								<option value="" >全部保障类型</option>
								<c:forEach var="item" items="${reskinds}">
									<option value="${item.value}">${item.text}</option>
								</c:forEach>
							</select>
						</div>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<span>姓名</span>
						<div style="width: 100px" class="layui-inline">
							<input style="width: 100px" type='text' name='searchValue' id="searchValue" class='layui-input' onfocus="setNullValue(this)"/>
						</div>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<div class="layui-btn-group">
							<button id="search" class="layui-btn  layui-btn-primary" type="button">
							 	确定
							</button>
							<button id="down" class="layui-btn  layui-btn-primary" type="button">
								打印
							</button>
						</div>
					</div>
				</div>
			</form>
			<div id="baseTables">
				<table id="baseTable"></table>
			</div>
			<div id="detail" class="detail" style="display:none;height:500px;overflow:hidden;position: relative;">
				<table id="detailTable">
				</table>
			</div>
		</div>
		<!-- 导出excel -->
		<form id="exportForm" method="post">
			<input type="hidden" id="param" name="param">
		</form>
	</body>
</html>