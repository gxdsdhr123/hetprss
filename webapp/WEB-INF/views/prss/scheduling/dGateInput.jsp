<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>混合航班国内到达口录入</title>
<script type="text/javascript">
	$(function() {
		$("body").css("minHeight", "0px");
		layui.use("form");
		$("#dGate").off("mousedown").on("contextmenu", function (e) {
	        e.preventDefault();
	        return false;
	    });
	})
	function sub(){
		var dGate = $("#dGate").val();
		var json = {
				fltId : $("#fltId").val(),
				dGate : dGate
			}
		return json;
	}
	
</script>
</head>
<body>
	<form class="layui-form" action="">
		<input type="hidden" name="fltId" id="fltId" value="${fltId }">
		<div class="layui-form-item">
			<label class="layui-form-label">国内登机口</label>
			<div class="layui-inline">
				<input type="text" id="dGate" name="dGate" class="layui-input" value="${dGate }" onkeyup="value=value.replace(/[^\w\.\/$]/ig,'')" onafterpaste="value=value.replace(/[^\w\.\/$]/ig,'')" maxlength="4">
			</div>
		</div>
	</form>
</body>
</html>