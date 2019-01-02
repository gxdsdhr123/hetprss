<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>自动分配开关</title>
<script type="text/javascript">
	$(function() {
		$("body").css("minHeight", "0px");
		layui.use("form");
	})
	function sub(){
		var bulid = $("#bulid").prop("checked");
		var assign = $("#assign").prop("checked");
		var json = {
				officeId : $("#officeId").val(),
				bulid : bulid?0:2,
				assign : assign?0:2
			}
		return json;
	}
</script>
</head>
<body>
	<form class="layui-form" action="">
		<input type="hidden" name="officeId" id="officeId" value="${result.OFFICE_ID }">
		<div class="layui-form-item">
			<div class="layui-inline">
				<input type="checkbox" id="bulid" name="bulid" title="自动生成任务"
				<c:if test="${result.TASK_FLAG == 0}">checked</c:if>>
				最后更新时间：${result.TASK_UPDATE_TIME }
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<input type="checkbox" id="assign" name="assign" title="自动分配任务"
				<c:if test="${result.WORKER_FLAG == 0}">checked</c:if>>
				最后更新时间：${result.WORKER_UPDATE_TIME }
			</div>
		</div>
	</form>
</body>
</html>