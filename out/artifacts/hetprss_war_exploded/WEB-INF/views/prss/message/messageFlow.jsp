<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>历史消息</title>
<meta name="decorator" content="default" />
<script src="${ctxStatic }/echarts/echarts.min.js"></script>
<style type="text/css">
.div {
	position: relative;
	overflow: hidden;
	height: 420px;
	padding: 0px;
	margin: 10px;
	border-width: 0px;
	cursor: default;
}
</style>
</head>
<body style="text-align: center;" >
    <div id="main" class="div"></div>

<script type="text/javascript">
	var num = ${num};
	var height = num * 100;
	
	var body_height = $("body").innerHeight();
	if(body_height > height){
		$("#main").height(body_height);
	} else {
		$("#main").height(height);
	}
	var option = ${option};
    var myChart = echarts.init(document.getElementById('main'));
    myChart.setOption(option);
    </script>
</body>
</html>