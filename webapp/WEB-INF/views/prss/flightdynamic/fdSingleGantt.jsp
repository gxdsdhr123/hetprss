<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>单航班甘特图</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<style type="text/css">
html,body{
	height:100% !important;
	overflow:hidden;
}
#info-box{
	color:#D3E2FF;
	background-color:#041332;
	padding: 5px;
	font-size: 12px;
}
#info-box .col-md-7 div{
	padding-right:0px;
}
#info-box > .row > .col-md-4{
	line-height: 40px;
	text-align: center;
}
.back{
	position: absolute;
	z-index: 1;
	width: 70%;
	height: 4px;
	left: 1%;
	background-color: #405886;
	top: 18px;
}
.flow{
	position: absolute;
	z-index: 2;
	width: 50%;
	height: 10px;
	background-color: #234084;
	border-radius: 10px;
	left: 0px;
	top: 15px;
	border: 1px solid #335791;
}
.rate{
	position: absolute;
	z-index: 3;
	top: 10px;
	left: 21%;
}
.plane{
	position: absolute;
	z-index: 3;
	left: 46%;
	top: 15px;
}
.video{
	position: absolute;
	top: 13px;
	z-index: 3;
	left: 80%;
	color: #D3E2FF;
}
</style>
<script type="text/javascript" src="${ctxStatic}/jquery/plugins/SJgantt/SJgantt-fdSingle.js"></script>
<body>
	<input id="inFltid" type="hidden" value="${inFltid}">
	<input id="outFltid" type="hidden" value="${outFltid}">
	<div id="info-box" class="container-full">
		<div class="row">
			<div class="col-md-4" style="width:20%">${depAirport}-${arrAirport}<c:if test='${status != null}'>（${status}）</c:if></div>
			<div class="col-md-7" style="width:60%">
				<div class="row">
					<div class="col-md-2">机型：${actType}</div>
					<div class="col-md-2">机位：${actStand}</div>
					<div class="col-md-2">进港航班：${inFltNumber}</div>
					<div class="col-md-2">计落：${sta}</div>
					<div class="col-md-2">预落：${eta}</div>
					<div class="col-md-2">实落：${ata}</div>
				</div>
				<div class="row">
					<div class="col-md-2">机号：${actNumber}</div>
					<div class="col-md-2">登机口：${gate}</div>
					<div class="col-md-2">出港航班：${outFltNumber}</div>
					<div class="col-md-2">计起：${std}</div>
					<div class="col-md-2">预起：${etd}</div>
					<div class="col-md-2">实起：${atd}</div>
				</div>
			</div>
			<div class="col-md-2" style="width:20%">
				<div class="back"></div>
				<div class="flow"></div>
				<span class="rate">80%</span>
				<canvas class="plane" height="10" width="30"></canvas>
				<a class="video fa fa-video-camera"></a>
			</div>
		</div>
	</div>
	<canvas id="SJgantt" class="SJgantt"></canvas>
	<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/fdSingleGantt.js"></script>
</body>
</html>