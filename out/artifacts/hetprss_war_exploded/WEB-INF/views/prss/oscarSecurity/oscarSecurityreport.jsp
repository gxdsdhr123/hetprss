<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>OSCAR保障报告管理</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<style type="text/css">
#cellDetailInfo {
    width: 23%;
    position: absolute;
    z-index: 9;
    background: rgba(0, 0, 0, 0.35);
    margin-left: 77%;
}
.mark_c {
    position: absolute;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.01);
    z-index: 9;
}
#container {
	position: relative;
	margin: 0px auto;
	padding: 0px;
	width: 100%;
	height: 100%;
	overflow-y: auto;
}
</style>
<body>
	<div class="mark_c" style="display: block;"></div>
<div id="container">
	<div id="tool-box" style="padding-top: 5px;">
		<div style="float: left;padding-left:15px" >
			<label class="layui-form-label">开始日期：</label>
			<div class="layui-input-inline">
				<input id="startDate" class="layui-input" name="startDate" readonly
					onfocus="WdatePicker({dateFmt:'yyyyMMdd',readOnly:true})" placeholder="请输入开始日期" value="${startDate }" />			
			</div>
		</div>
		<div style="float: left;padding-left:15px" >
			<label class="layui-form-label">结束日期：</label>
			<div class="layui-input-inline">
				<input id="endDate" class="layui-input" name="endDate" readonly
					onfocus="WdatePicker({dateFmt:'yyyyMMdd',readOnly:true})" placeholder="请输入结束日期" value="${endDate }" />			
			 </div>
		</div>
		<div style="float: left;padding-left:15px" >
			<div class="layui-input-inline">
				<input id="searchData" class="layui-input" value="${searchData }" placeholder="模糊搜索" maxlength="20"/>
			</div>
		</div>

		<div style="float: left;padding-left:15px" id="">
			<button id="searchBtn" type="button" class="btn btn-link">查询</button>
		</div>
		<div style="float: left;padding-left:20px" id="">
			<button id="showDetail" type="button" class="btn btn-link">查看</button>
			<button id="printWorld" type="button" class="btn btn-link">打印</button>
			<button id="downloadAtta" type="button" class="btn btn-link">下载附件</button>
		</div>
	</div>
	
	<div id="createDetailTableDiv" style="width:100%;position: relative;" onselectstart="return false" style="-moz-user-select:none;">
		<table id="createDetailTable" style="table-layout: fixed;"></table>
	</div>
	
	<div style="display: none;">
		<form action="${ctx }/oscarrSecurity/report/print" method="post" id="print">
			<input type="hidden" name="searchId" id="searchId" value="" />
			<input type="hidden" name="showId" id="showId" value="" />
			<input type="hidden" name="param" id="param" value="" />
		</form>
	</div>
</div>
<script>
	new PerfectScrollbar('#container');
</script>
<script type="text/javascript" src="${ctxStatic}/prss/oscarSecurity/oscarSecurityreport.js"></script>
</body>
</html>