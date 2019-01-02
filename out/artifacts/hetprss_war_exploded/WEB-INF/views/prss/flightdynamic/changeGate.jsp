<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<title>登机状态变更</title>
<style type="text/css">
body {
	height: 100% !important;
	min-height: unset !important;
	padding-top: 20px;
}
</style>
</head>
<body>
	<form class="form-horizontal">
      <div class="form-group">
        <label class="col-xs-3 control-label" style="text-align: right;">登机口</label>
        <div class="col-xs-8">
          <select id="gates" class="form-control">
          	<c:forEach items="${gateList}" var="gate">
          		<option value="${gate}">${gate}</option>
          	</c:forEach>
          </select>
        </div>
      </div>
    </form>
    <script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/changeGate.js"></script>
</body>
</html>