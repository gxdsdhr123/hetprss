<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
		<title>清洁服务单</title>
		<meta name="decorator" content="default" />
		<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
		<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
		<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript" src="${ctxStatic}/prss/produce/cleanServerList.js"></script>
		<style type="text/css">
			#container{
				padding-top:20px;
			}
			.select2-container .selection .select2-selection .select2-selection__rendered{
				width:140px;
				line-height:30px;
			}
			.select2-container .selection .select2-selection{
				height:36px;
			}
		</style>
	</head>
	<body>
		<div id="container">
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">航班日期</label>
					<div class="layui-input-inline">
						<input type='text' maxlength="20" name='fltDate' id="fltDate"
							placeholder='请选择日期' class='form-control' value="${fltDate}"
							onclick="WdatePicker({dateFmt:'yyyyMMdd'});" />
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">航班号</label>
					<div class="layui-input-inline">
						<input type='text' maxlength="20" name='fltNum' id="fltNum" class='form-control'/>
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">操作者</label>
					<div class="layui-input-inline">
						<select name="operator" id="operator" class="select2 form-control">
							<option></option>
							<c:forEach items="${peopleList}" var="people">
								<option value="${people.ID}">${people.NAME}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="layui-inline  pull-right" id="toolbar">
					<button id="btnSubmit" class="layui-btn layui-btn-small layui-btn-primary search" type="button">
					 	<i class="fa fa-search">&nbsp;</i>查询
					</button>
					<button id="add" class="layui-btn layui-btn-small layui-btn-primary" type="button">
						<i class="fa fa-pencil-square-o">&nbsp;</i>增加单据
					</button>
					<button id="modify" class="layui-btn layui-btn-small layui-btn-primary" type="button">
						<i class="fa fa-pencil-square-o">&nbsp;</i>修改单据
					</button>
					<button id="delete" class="layui-btn layui-btn-small layui-btn-primary" type="button">
						<i class="fa fa-pencil-square-o">&nbsp;</i>删除单据
					</button>
					<button id="down" class="layui-btn layui-btn-small layui-btn-primary" type="button">
						<i class="fa fa-download">&nbsp;</i>下载
					</button>
				</div>
			</div>
			<form id="printForm" action="${ctx}/produce/cleanServerBill/print" method="post" style="display: none">
				<input type="hidden" name="id" />
			</form>
			<div id="baseTables">
				<table id="baseTable"></table>
			</div>
		</div>
	</body>
</html>