<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<title>更改机位</title>
<style type="text/css">
body {
	height: 100% !important;
	min-height: unset !important;
	padding-top: 20px;
}
.stand {
	display: inline-block;
	width: 42px;
	height: 25px;
	border: 1px solid #555;
	text-align: center;
	margin: 0px -3px;
	cursor: pointer;
}
</style>
</head>
<body>
	<form class="form-horizontal">
      <div class="form-group" style="margin-right: 20px;">
        <label class="col-xs-3 control-label" style="text-align: right;">原机位</label>
        <div class="col-xs-3">
          <input id="oriStand" type="text" class="form-control" readonly="readonly"></input>
        </div>
        <label class="col-xs-3 control-label" style="text-align: right;">变更后机位</label>
        <div class="col-xs-3">
          <input id="nowStand" type="text" class="form-control" readonly="readonly"></input>
        </div>
      </div>
      <div id="stands" style="padding:0px 50px">
      	<c:forEach items="${standList}" var="stand">
      		<span class='stand' onclick='giveValue(this)'>${stand}</span>
      	</c:forEach>
      	<span class='stand' onclick='giveValue(this)'>清空</span>
      </div>
    </form>
    <script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/changeStand.js"></script>
</body>
</html>