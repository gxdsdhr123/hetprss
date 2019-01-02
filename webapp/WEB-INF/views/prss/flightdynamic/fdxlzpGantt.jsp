<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>行李转盘（甘特图）</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<link href="${ctxStatic}/jquery/plugins/perfectScroll/perfect-scrollbar.min.css" rel="stylesheet" />
<link href="${ctxStatic}/prss/flightdynamic/css/flightDynList.css" rel="stylesheet" />
<style type="text/css">
#stopMenu{
position: absolute;
display:none;
}
#stop,#member{
background-color: rgba(2, 17, 50,0.9);
color: #CFCFCF;
}
#stop:hover,#member:hover{
background-color: rgba(05, 22, 59,0.9);
}
#stop:active,#member:active{
background-color: rgba(19, 40, 93,0.9);
color: #FFFFFF;
}
#loading {
	height: 65px;
	width: 300px;
	text-align: center;
	position: absolute;
	left: calc(50% - 150px);
	top: calc(50% - 32.5px);
	z-index: 999;
	background-color: #141B28;
	padding: 21px;
	border: 1px solid RGBA(95, 123, 180, 0.6);
	border-top-right-radius: 4px;
	border-bottom-left-radius: 4px;
	box-shadow: 0px 0px 4px -2px;
}
#loading::before{
	width: 30px;
	height: 30px;
	z-index: 1000;
	position: absolute;
	content: '';
	border-bottom: 4px solid #5F7BB4;
	display: block;
	border-left: 4px solid #5F7BB4;
	border-bottom-left-radius: 3px;
	bottom: 0px;
	left: 0px;
}
#loading::after{
	width: 30px;
	height: 30px;
	z-index: 1000;
	position: absolute;
	content: '';
	border-top: 4px solid #5F7BB4;
	display: block;
	border-right: 4px solid #5F7BB4;
	border-top-right-radius: 3px;
	top: 0px;
	right: 0px;
}
</style>
<script type="text/javascript" src="${ctxStatic}/jquery/plugins/SJgantt/SJgantt-xlzp.js"></script>
<body>
	<input id="loginName" type="hidden" value="${fns:getUser().loginName}">
	<div id="tool-box" style="padding:5px;">
		<div class="btn-group">
			<button id="refresh" type="button" class="btn btn-link">刷新</button>
			<button type="button" class="btn btn-link dropdown-toggle" style="margin-right: 0px;padding: 6px 0px;" 
				data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				<span class="caret"></span> <span class="sr-only">Toggle
					Dropdown</span>
			</button>
			<ul class="dropdown-menu">
				<li><a href="javascript:void(0)" onclick="autoReload(10)">10秒钟自动刷新</a></li>
				<li><a href="javascript:void(0)" onclick="autoReload(30)">30秒钟自动刷新</a></li>
				<li><a href="javascript:void(0)" onclick="autoReload(60)">1分钟自动刷新</a></li>
				<li><a href="javascript:void(0)" onclick="autoReload(180)">3分钟自动刷新</a></li>
				<li><a href="javascript:void(0)" onclick="autoReload(300)">5分钟自动刷新</a></li>
				<li role="separator" class="divider" style="background-color: #006DC0;"></li>
				<li><a id="refresh-c" href="javascript:void(0)">自定义</a></li>
			</ul>
		</div>
		<button id="graph" type="button" class="btn btn-link">动态列表</button>
		<button id="save" type="button" class="btn btn-link">保存行李转盘</button>
		<button id="cancel" type="button" class="btn btn-link">取消调整</button>
	</div>
	<canvas id="SJgantt" class="SJgantt"></canvas>
	<script>
		$(function(){
			$("body").css("overflow","hidden");
			$("#graph").click(function(){
				var form = $("<form id='graphForm' style='display:none' action='"+ctx + "/flightDynamic/list'></form>")
				$("body").append(form);
				$("#graphForm").submit();
			});
		})
	</script>
	<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/fdxlzpGantt.js"></script>
</body>
</html>