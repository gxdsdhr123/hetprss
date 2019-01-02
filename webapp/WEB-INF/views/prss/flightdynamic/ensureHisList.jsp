<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/prss/flightdynamic/css/ensureList.css" 	rel="stylesheet" />
</head>
<body>
	<ul id="myTabs" class="nav nav-tabs" role="tablist">
      <li role="presentation" class="active"><a href="#fltsingle" id="fltsingle-tab" role="tab" data-toggle="tab" aria-controls="fltsingle" aria-expanded="true">单航班甘特图</a></li>
      <li role="presentation" class=""><a href="#fltmonitor" id="fltmonitor-tab" role="tab" data-toggle="tab" aria-controls="fltmonitor" aria-expanded="false">航班监控图</a></li>
    </ul>
    <div id="myTabContent" class="tab-content">
      <div role="tabpanel" class="tab-pane fade active in" id="fltsingle" aria-labelledby="fltsingle-tab">
        <iframe  src="${ctx}/flightDynamic/listSingleHisGantt?inFltid=${inFltid }&outFltid=${outFltid}" style="overflow:auto;" frameborder="no" width="100%" height="100%"></iframe>
      </div>
      <div role="tabpanel" class="tab-pane fade" id="fltmonitor" aria-labelledby="fltmonitor-tab">
      	<iframe  src="${ctx }/fltmonitor/fltmonitorList?inFltid=${inFltid }&outFltid=${outFltid}&hisFlag=his" style="overflow:auto;" frameborder="no" width="100%" height="100%"></iframe>
      </div>
     </div>
	<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/ensureList.js"></script>
</body>
</html>