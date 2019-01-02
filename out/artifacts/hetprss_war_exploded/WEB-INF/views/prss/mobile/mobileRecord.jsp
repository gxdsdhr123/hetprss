<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
		<title>手持设备管理记录</title>
		<meta name="decorator" content="default" />
		<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
		<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
		<script type="text/javascript" src="${ctxStatic}/prss/mobile/mobileRecord.js"></script>  
		<link rel="stylesheet" href="${ctxStatic}/prss/mobile/css/mobileRecord.css" />
		<script>
			var PATH = '${ctx}';
		</script>
	</head>
	<body>
		<div id="topDiv">
			<span class="proSpan">部门：</span>
			<select id="buMen"></select>
			<span class="proSpan">状态：</span>
			<select id="zhuangTai">
				<option value=""></option>
				<option value="1">借出</option>
				<option value="2">归还</option>
				<option value="3">交接</option>
			</select>
			<button id="searchBut" type="button" class="btn btn-link">查询</button>
			<button id="searchLogBut" type="button" class="btn btn-link">查看记录</button>
			<button id="printBut" type="button" class="btn btn-link">打印</button>
			<form id="printForm" style="display:none" action="${ctx}/mobileRecord/print" method="post">
				<textarea id="buMenDisplay" name="buMenDisplay"></textarea>
				<textarea id="zhuangTaiDisplay" name="zhuangTaiDisplay"></textarea>
			</form>
		</div>
		<table id="baseTable" class="layui-table">
			
		</table>
		<div id="hiddenDiv" style="display:none;height:450px;position:relative">
			<div id="hiddenTopDiv">
				<span>设备编号：</span>
				<span id="pdaId" style="display:none;"></span>
				<span id="pdaNo"></span>
				<input id="param" class="layui-input"/>
				<button id="paramBut" type="button" class="btn btn-link">查询</button>
			</div>
			<hr id="line">
			<div id="hiddenBottomDiv" style="height:400px;position:relative">
			</div>
		</div>
	</body>
</html>