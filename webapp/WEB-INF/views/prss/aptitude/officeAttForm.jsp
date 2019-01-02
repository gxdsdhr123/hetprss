<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>部门属性设置</title>
<script type="text/javascript">
	$(function() {
		$("body").css("minHeight", "0px");
		layui.use("form");
	})
	function sub(){
		var jw = $("#jw").prop("checked");
		var jx = $("#jx").prop("checked");
		var hs = $("#hs").prop("checked");
		var json = {
				officeId : $("#officeId").val(),
				jwLimit : jw?1:0,
				jxLimit : jx?1:0,
				hsLimit : hs?1:0
			}
		return json;
	}
</script>
</head>
<body>
	<form class="layui-form" action="">
		<input type="hidden" name="officeId" id="officeId"
			value="${officeLimConf.officeId}">
		<div style="text-align:center;vertical-align:middle;line-height:80px;">
			<input type="checkbox" id="jw" name="jw" title="机位" <c:if test="${officeLimConf.jwLimit == 1}">checked</c:if>>
			<input type="checkbox" id="jx" name="jx" title="机型" <c:if test="${officeLimConf.jxLimit == 1}">checked</c:if>>
			<input type="checkbox" id="hs" name="hs" title="航空公司" <c:if test="${officeLimConf.hsLimit == 1}">checked</c:if>>
		</div>
	</form>
</body>
</html>