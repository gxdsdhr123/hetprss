<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<title>值机状态变更</title>
<style type="text/css">
body {
	height: 100% !important;
	min-height: unset !important;
	padding-top: 20px;
}
</style>
<script type="text/javascript">
	var data = ${data};
</script>
<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/changeCheckinStatus.js"></script>
</head>
<body>
	<input id="fltid" type="hidden" value="${fltid}">
	<input id="oldval" type="hidden" value="${oldval}">
	<form class="form-horizontal">
      <div class="form-group">
        <label class="col-xs-3 control-label" style="text-align: right;">值机状态</label>
        <div class="col-xs-8">
          <select id="counterStatus" class="form-control">
          	<option value=""></option>
          	<option value="1">值机开始</option>
          	<option value="2">催促值机</option>
          	<option value="3">值机结束</option>
          	<option value="4">取消</option>
          	<option value="5">延误</option>
          </select>
        </div>
      </div>
    </form>
</body>
</html>