<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<title>要客导入</title>
<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/fdVipImport.js"></script>
</head>
<body>
	<div id="toolbar">
		<button id="importBtn" type="button" class="btn btn-link">导入</button>
		<button id="delBtn" type="button" class="btn btn-link">删除</button>
		<button id="emptiedBtn" type="button" class="btn btn-link">清空</button>
		<button id="hisBtn" type="button" class="btn btn-link">历史</button>
	</div>
	<table id="fdVipImportGrid"></table>
	<form id="fileList" style="display: none;" action="${ctx}/flightDynamic/readVipExcel" method="post" enctype="multipart/form-data">
		<div class="layui-form-item">
			<div class="layui-inline">
    			<label class="layui-form-label">要客计划:</label>
    			<div class="layui-input-inline">
      				<input id="fileInput" name="file" type="file">
    			</div>
  			</div>
  		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
    			<label class="layui-form-label">备注：</label>
    			<div class="layui-input-inline">
      				请导入小于 5MB 的 .xls 文件。
    			</div>
  			</div>
  		</div>
	</form>
</body>
</html>