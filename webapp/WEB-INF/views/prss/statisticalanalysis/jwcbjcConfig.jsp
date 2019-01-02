<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>机务除冰架次配置</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" type="text/css">
<body>
	<div id="container" style="padding-top:30px">
		<form  id="listForm">
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">航空公司</label>
					<div class="layui-input-inline">
						<select id="ALN_2CODE" name="ALN_2CODE" lay-filter="ALN_2CODE">
							<option value="">请选择</option>
							<c:forEach items="${airLines}" var="item">
								<option value="${item.AIRLINE_CODE}">${item.AIRLINE_SHORTNAME}-${item.AIRLINE_CODE}</option>
							</c:forEach>
						</select>
					</div>
					<div class="layui-form-mid">除冰液价格</div>
					<div class="layui-input-inline" style="width: 100px;" >
						<input id="CBY_FEE" name="CBY_FEE" class="layui-input" type="text">
					</div>
					<div class="layui-form-mid">防冰液价格</div>
					<div class="layui-input-inline" style="width: 100px;" >
						<input id="FBY_FEE" name="FBY_FEE" class="layui-input" type="text">
					</div>
					
				</div>
				<div class="layui-inline  pull-right" id="toolbar">
					<button id="btnSubmit" class="layui-btn layui-btn-small layui-btn-primary add" type="button">
					 	新增
					</button>
				</div>
			</div>
		</form>
		

		<div id="baseTables" style="width:100%;">
			<table id="baseTableConfig"></table>
		</div>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/statisticalanalysis/jwcbjcConfig.js"></script>
</body>
</html>