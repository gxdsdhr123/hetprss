<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title></title>
<script type="text/javascript"
	src="${ctxStatic}/prss/scheduling/changeZXOutInfo.js"></script>
</head>
<body>
	<form id="form" action="" class="layui-form">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">出港行李实装件数</label>
				<div class="layui-input-inline" >
					<input type="text" id="outBaggageReal" name="outBaggageReal" class="layui-input" 
					value="${plusInfo.OUT_BAGGAGE_REAL}">
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">出港大件行李件数</label>
				<div class="layui-input-inline">
				<input type="text"  id="outLargeBaggage" name="outLargeBaggage" class="layui-input" 
					value="${plusInfo.OUT_LARGE_BAGGAGE}">
			</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">出港装卸备注</label>
				<div class="layui-input-block">
				<textarea  id="outZxRemark" name="outZxRemark"
					class="layui-textarea" >${plusInfo.OUT_ZX_REMARK}</textarea>
			</div>
			</div>
		</div>
		
		<input type="hidden" class="layui-input" name="fltid" id="fltid" value="${fltid}" >
		<input type="hidden" class="layui-input" name="outBaggageRealOld" id="outBaggageRealOld" value="${plusInfo.OUT_BAGGAGE_REAL}" >
		<input type="hidden" class="layui-input" name="outLargeBaggageOld" id="outLargeBaggageOld" value="${plusInfo.OUT_LARGE_BAGGAGE}" >
		<input type="hidden" class="layui-input" name="outZxRemarkOld" id="outZxRemarkOld" value="${plusInfo.OUT_ZX_REMARK}" >
	</form>
</body>
</html>