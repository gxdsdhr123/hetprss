<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>邮货交接单</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
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
</style>
<body>
	<div class="mark_c" style="display: block;"></div>
	<input type="hidden" value="${sign }" id="sign" />

	<div id="tool-box" style="padding-top: 5px;">

		<div style="float: left;padding-left:15px" id="toolbar">
			<button id="addBill" type="button" class="btn btn-link">增加单据</button>
			<button id="modifyBill" type="button" class="btn btn-link">修改单据</button>
			<button id="delBill" type="button" class="btn btn-link">删除单据</button>
		</div>
	</div>
	<div id="createDetailTableDiv" style="width:100%;position: relative;" onselectstart="return false" style="-moz-user-select:none;">
		<table id="createDetailTable" style="table-layout: fixed;"></table>
	</div>
	
		
<script type="text/javascript" src="${ctxStatic}/prss/handoverBill/handoverBill.js"></script>
</body>
</html>