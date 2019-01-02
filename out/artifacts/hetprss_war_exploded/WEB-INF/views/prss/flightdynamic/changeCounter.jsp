<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<title>值机柜台变更</title>
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
<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/changeCounter.js"></script>
</head>
<body>
	<input id="fltid" type="hidden" value="${fltid}">
	<input id="oldval" type="hidden" value="${oldval}">
	<form class="form-horizontal">
      <div class="form-group">
        <label class="col-xs-3 control-label" style="text-align: right;">值机柜台</label>
        <div class="col-xs-8">
          <select id="counters" class="form-control"></select>
        </div>
      </div>
    </form>
</body>
</html>