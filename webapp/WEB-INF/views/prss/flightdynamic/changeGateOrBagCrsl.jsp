<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title></title>
<script type="text/javascript"
	src="${ctxStatic}/prss/flightdynamic/changeGateOrBagCrsl.js"></script>
	<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
	<form id="form" action="" class="layui-form">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">旧值</label>
				<div class="layui-input-inline" >
					<input type="text" id="oldValue" name="oldValue" class="layui-input" 
					value="${oldValue}" readonly="readonly">
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">新值</label>
				<div class="layui-input-inline" >
					<select id="newValue" name="newValue" >
				         <option value="">请选择</option>
				         <c:forEach items="${selectData}" var="item">
				          <option value="${item.id}" >${item.text}</option>
				         </c:forEach>
			 		</select>
				</div>
			</div>
		</div>
		
		<input type="hidden" class="layui-input" name="fltid" id="fltid" value="${fltid}" >
		<input type="hidden" class="layui-input" name="type" id="type" value="${type}" >
	</form>
</body>
</html>