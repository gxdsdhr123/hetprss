<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>客舱清洁收费标准</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript">      
    var arrList = new Array(); //将已经选择的做成数组   在修改的时候回显
    arrList = "${data.TODB_ACTYPE_CODE}".replace('[','').replace(']','').split(',');
    var actArr=${actType};//根据航空公司获取可选的机型列表
</script>  
<body>
	<div id="container" style="padding-top:30px">
		<div>
			<form   id="kcqjForm" >
			<div class="layui-form-item">
				<div class="layui-inline" >
					<label class="layui-form-label">航空公司</label>
						<div class="layui-inline" style="width:300px">
						<select id="alnCode" name="alnCode" class="mutilselect2 form-control" >
					         <option value="">请选择</option>
					         <c:forEach items="${airlines}" var="item">
					          <option <c:if test="${data.ALN_3CODE==item.id}">selected</c:if> value="${item.id}" >${item.text}</option>
					         </c:forEach>
						 </select>
						</div>
				</div>
			</div>
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">机型</label>
					<div class="layui-inline" style="width:300px">
						<select id="actType" name="actType" class="mutilselect2 form-control" multiple="multiple">

<%--  							<c:forEach items="${actType}" var="item"> --%>
<%-- 					          <option  value="${item.id}" >${item.text}</option> --%>
<%-- 					        </c:forEach> --%>
						 </select>
					</div>
				</div>
			</div>
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">航前费用</label>
					<div class="layui-input-inline" style="width: 300px;">
						<input id="hqCharges" name="hqCharges" class="layui-input" type="text" value="${data.HQ_CHARGES }" >
					</div>
				</div>
			</div>
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">航后费用</label>
					<div class="layui-input-inline" style="width: 300px;">
						<input id="hhCharges" name="hhCharges" class="layui-input" type="text" value="${data.HH_CHARGES }" >
					</div>
				</div>
			</div>
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">过站费用</label>
					<div class="layui-input-inline" style="width: 300px;">
						<input id="gzCharges" name="gzCharges" class="layui-input" type="text" value="${data.GZ_CHARGES }" >
					</div>
				</div>
			</div>
			<input id="id" name="id" class="layui-input" type="hidden" value="${data.ID }" >
			<input id="type" name="type" class="layui-input" type="hidden" value="${type }" >
		</form>
	</div>
		<div style="clear:both;"></div>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/statisticalanalysis/kcqjFeeScaleForm.js"></script>
</body>
</html>