<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>外航数据录入</title>
<meta name="decorator" content="default"/>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/fltDataInput.js"></script>
</head>
<body>
	<form id="inputForm" action="${ctx}/flightDynamic/saveFltData" class="layui-form" method="post">
		<input type="hidden" name="fltid" id="fltid" value="${data.fltid}">
		<input type="hidden" name="ioType" id="ioType" value="${ioType}">
		<c:choose>
			<%-- 进港 --%>
			<c:when test="${ioType eq 'I'}">
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">报文ETA：</label>
						<div class="layui-input-inline" style="width:102px">
							<input name="etaDate" id="etaDate" style="width:100%" value="${not empty data.etaDate?data.etaDate:data.currDate}" onclick="WdatePicker({isShowClear:false})" class="layui-input" type="text" readonly="readonly">
						</div>
						<div class="layui-input-inline" style="margin-left:-2px;">
							<input name="eta" id="eta" style="width:90px" value="${data.eta}" class="layui-input" type="text" placeholder="格式HHMI" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="4">
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">入位时间：</label>
						<div class="layui-input-inline" style="width:102px">
							<input name="standDate" id="standDate" value="${not empty data.standDate?data.standDate:data.currDate}" style="width:100%" onclick="WdatePicker({isShowClear:false})" class="layui-input" type="text" readonly="readonly">
						</div>
						<div class="layui-input-inline" style="margin-left:-2px;">
							<input name="standTm" id="standTm" style="width:90px" value="${data.standTm}" class="layui-input" type="text" placeholder="格式HHMI" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="4">
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">机号：</label>
						<div class="layui-input-inline">
							<input name="aircraftNumber" id="aircraftNumber" value="${data.aircraftNumber}" class="layui-input" type="text" onkeyup="this.value=this.value.toUpperCase()">
						</div>
						<div id="aircValidFlag" class="layui-form-mid layui-word-aux"></div>
					</div>
				</div>
			</c:when>
			<%-- 出港 --%>
			<c:otherwise>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">客舱门关闭：</label>
						<div class="layui-input-inline" style="width:102px">
							<input name="htchCloDate" id="htchCloDate" value="${not empty data.htchCloDate?data.htchCloDate:data.currDate}" style="width:100%" onclick="WdatePicker({isShowClear:false})" class="layui-input" type="text" readonly="readonly">
						</div>
						<div class="layui-input-inline" style="margin-left:-2px;">
							<input name="htchCloTm" id="htchCloTm" style="width:90px" value="${data.htchCloTm}" class="layui-input" type="text" placeholder="格式HHMI" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="4">
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">离位时间：</label>
						<div class="layui-input-inline" style="width:102px">
							<input name="relsStandDate" id="relsStandDate" value="${not empty data.relsStandDate?data.relsStandDate:data.currDate}" style="width:100%" onclick="WdatePicker({isShowClear:false})" class="layui-input" type="text" readonly="readonly">
						</div>
						<div class="layui-input-inline" style="margin-left:-2px;">
							<input name="relsStandTm" id="relsStandTm" style="width:90px" value="${data.relsStandTm}" class="layui-input" type="text" placeholder="格式HHMI" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="4">
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">机号：</label>
						<div class="layui-input-inline">
							<input name="aircraftNumber" id="aircraftNumber" value="${data.aircraftNumber}" class="layui-input" type="text" onkeyup="this.value=this.value.toUpperCase()">
						</div>
						<div id="aircValidFlag" class="layui-form-mid layui-word-aux"></div>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
	</form>
</body>
</html>