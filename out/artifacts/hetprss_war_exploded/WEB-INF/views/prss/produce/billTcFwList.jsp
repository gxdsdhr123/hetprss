<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
		<title>特车计费数据列表</title>
		<meta name="decorator" content="default" />
		<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
		<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
		<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
		<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript" src="${ctxStatic}/prss/produce/billTcFwList.js"></script>
	</head>
	<body>
		<div id="container">
			<form class="layui-form">
				<div class="layui-form-item" id="toolbar">
					<div class="layui-inline" >
						<div class="layui-form-item">
							<button id="add" class="layui-btn  layui-btn-primary" type="button">
								新增
							</button>
							<button id="detail" class="layui-btn  layui-btn-primary" type="button">
								查看
							</button>
							<button id="delete" class="layui-btn  layui-btn-primary" type="button">
								删除
							</button>
							<button id="print" class="layui-btn  layui-btn-primary" type="button">
								打印
							</button>
							<button id="export" class="layui-btn  layui-btn-primary" type="button">
								导出
							</button>
							<button id="search" class="layui-btn  layui-btn-primary" type="button">
								查询
							</button>
							<div class="btn-group pull-right">
								<label class="layui-form-label">航班日期</label>
								<div style="width: 100px" class="layui-input-inline">
								<input style="width: 100px" type='text' maxlength="20" name="fltDate" id="fltDate"
									placeholder='请选择日期' class='form-control'
									onclick="WdatePicker({dateFmt:'yyyyMMdd'});" />
								</div>
								<label style="width: 100px"class="layui-form-label">航班号</label>
								<div style="width: 100px" class="layui-input-inline">
									<input type='text' class='form-control' name='fltNo' id="fltNo"/>
								</div>
							</div>
						</div>
					</div>
				</div>
			</form>
			<div id="baseTables">
				<table id="baseTable"></table>
			</div>
		</div>
		<!-- 导出excel -->
		<form id="exportForm" method="post"></form>
	</body>
</html>