<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>延误原因变更</title>
<script type="text/javascript"
	src="${ctxStatic}/prss/flightdynamic/changeDelayReason.js"></script>
</head>
<body>
	<form id="form" action="" class="layui-form">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">航班号</label>
				<div class="layui-input-inline" >
					<input type="text" id="fltNumber" name="fltNumber" class="layui-input" 
					value="${FLIGHT_NUMBER}" readonly="readonly">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">航班日期</label>
				<div class="layui-input-inline" >
					<input type="text" id="fltDate" name="fltDate" class="layui-input" 
					value="${FLIGHT_DATE}" readonly="readonly">
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">机号</label>
				<div class="layui-input-inline" >
					<input type="text" id="actNumber" name="actNumber" class="layui-input" 
					value="${AIRCRAFT_NUMBER}" readonly="readonly">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">机型</label>
				<div class="layui-input-inline" >
					<input type="text" id="actType" name="actType" class="layui-input" 
					value="${ACTTYPE_CODE}" readonly="readonly">
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">延误原因</label>
				<div class="layui-input-inline" >
				<select name="delayReason" id="delayReason" lay-search >
					<option value=""></option>
					<c:forEach items="${alnDelaySource}" var="reason">
						<option value="${reason.id}" <c:if test="${reason.id==DELAY_REASON}">selected</c:if>>${reason.text}</option>
					</c:forEach>
				</select>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">放行原因</label>
				<div class="layui-input-inline" >
				<select name="releaseReason" id="releaseReason" lay-search >
					<option value=""></option>
					<c:forEach items="${releaseDelaySource}" var="reason">
						<option value="${reason.id}" <c:if test="${reason.id==RELEASE_REASON}">selected</c:if>>${reason.text}</option>
					</c:forEach>
				</select>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">延误详情</label>
			<div class="layui-input-block">
				<textarea id="delayReasonDetail" name="delayReasonDetail"
					class="layui-textarea" >${DELAY_REASON_DETAIL}</textarea>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">放行延误详情</label>
			<div class="layui-input-block">
				<textarea id="releaseReasonDetail" name="releaseReasonDetail"
					class="layui-textarea">${RELEASE_REASON_DETAIL}</textarea>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">内部原因</label>
			<div class="layui-input-block">
				<textarea  id="delayReasonInner" name="delayReasonInner"
					class="layui-textarea" >${DELAY_REASON_INNER}</textarea>
			</div>
		</div>
		<input type="hidden" class="layui-input" name="fltid" id="fltid" value="${fltid}" >
		<input type="hidden" class="layui-input" name="delayReasonOld" id="delayReasonOld" value="${DELAY_REASON}" >
		<input type="hidden" class="layui-input" name="releaseReasonOld" id="releaseReasonOld" value="${RELEASE_REASON}" >
		<input type="hidden" class="layui-input" name="delayReasonDetailOld" id="delayReasonDetailOld" value="${DELAY_REASON_DETAIL}" >
		<input type="hidden" class="layui-input" name="releaseReasonDetailOld" id="releaseReasonDetailOld" value="${RELEASE_REASON_DETAIL}" >
	</form>
</body>
</html>