<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>参数配置</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<style type="text/css">
	.layui-layer-page .layui-layer-content{
		overflow: hidden !important;
	}
	.table th, .table td {  
		text-align: center;  
		vertical-align: middle!important;  
	} 
		
</style>
<body>
	<div id="container" style="padding-top:30px">
		<div id="tcCoefficientArea" >
<%-- 		action="${ctx}/tc/statistics/saveTcCoefficient" method="post" --%>
			<form  class="layui-form" id="coefficientForm" >
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">摆渡车</label>
					<div class="layui-input-inline" style="width: 300px;">
						<input id="bdc" name="bdc" class="layui-input" type="text" value="${tcCoefficient.bdc }" lay-verify="numberByCus">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">客梯车</label>
					<div class="layui-input-inline" style="width: 300px;">
						<input id="ktc" name="ktc" class="layui-input" type="text" value="${tcCoefficient.ktc }" lay-verify="numberByCus">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">牵引车</label>
					<div class="layui-input-inline" style="width: 300px;">
						<input id="qyc" name="qyc" class="layui-input" type="text" value="${tcCoefficient.qyc }" lay-verify="numberByCus">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">清水车</label>
					<div class="layui-input-inline" style="width: 300px;">
						<input id="qsc" name="qsc" class="layui-input" type="text" value="${tcCoefficient.qsc }" lay-verify="numberByCus">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">污水车</label>
					<div class="layui-input-inline" style="width: 300px;">
						<input id="wsc" name="wsc" class="layui-input" type="text" value="${tcCoefficient.wsc }" lay-verify="numberByCus">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">平台车</label>
					<div class="layui-input-inline" style="width: 300px;">
						<input id="ptc"  name="ptc" class="layui-input" type="text" value="${tcCoefficient.ptc }" lay-verify="numberByCus">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">残登车</label>
					<div class="layui-input-inline" style="width: 300px;">
						<input id="cdc"  name="cdc" class="layui-input" type="text" value="${tcCoefficient.cdc }"  lay-verify="numberByCus">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">空调车</label>
					<div class="layui-input-inline" style="width: 300px;">
						<input id="kotc" name="kotc" class="layui-input" type="text" value="${tcCoefficient.kotc }" lay-verify="numberByCus">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">气源车</label>
					<div class="layui-input-inline" style="width: 300px;">
						<input id="qiyc" name="qiyc" class="layui-input" type="text" value="${tcCoefficient.qiyc }" lay-verify="numberByCus">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">电源车</label>
					<div class="layui-input-inline" style="width: 300px;">
						<input id="dyc" name="dyc" class="layui-input" type="text" value="${tcCoefficient.dyc }" lay-verify="numberByCus">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">除冰车</label>
					<div class="layui-input-inline" style="width: 300px;">
						<input id="cbc" name="cbc" class="layui-input" type="text" value="${tcCoefficient.cbc }" lay-verify="numberByCus">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">传动带</label>
					<div class="layui-input-inline" style="width: 300px;">
						<input id="csd" name="csd" class="layui-input" type="text" value="${tcCoefficient.csd }" lay-verify="numberByCus">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">垃圾车</label>
					<div class="layui-input-inline" style="width: 300px;">
						<input id="ljc" name="ljc" class="layui-input" type="text" value="${tcCoefficient.ljc }" lay-verify="numberByCus">
					</div>
				</div>
				<button id="submitBtn" class="layui-btn" lay-submit lay-filter="save" style="display: none"></button>
			</div>
		</form>
	</div>
		<div style="clear:both;"></div>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/statisticalanalysis/tcCoefficientForm.js"></script>
</body>
</html>