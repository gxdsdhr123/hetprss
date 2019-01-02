<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>补配规则分配</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript">
	var airplaneTypeZ = [];//可拖拽机型
	if ('${info.JX_LIMIT1}' != '') {
		var jx1 = '${info.JX_LIMIT1}';
		airplaneTypeZ = jx1.split(",");
	}
	var airplaneTypeT = [];//可推机型
	if ('${info.JX_LIMIT2}' != '') {
		var jx2 = '${info.JX_LIMIT2}';
		airplaneTypeT = jx2.split(",");
	}

	var airplaneZ = [];//可拖拽航空公司限制
	if ('${info.HS_LIMIT1}' != '') {
		var hs1 = '${info.HS_LIMIT1}';
		airplaneZ = hs1.split(",");
	}
	var airplaneT = [];//可推出航空公司限制
	if ('${info.HS_LIMIT2}' != '') {
		var hs2 = '${info.HS_LIMIT2}';
		airplaneT = hs2.split(",");
	}

	var seatZ = [];//可拖拽机位限制
	if ('${info.JW_LIMIT1}' != '') {
		var jw1 = '${info.JW_LIMIT1}';
		seatZ = jw1.split(",");
	}
	var seatT = [];//可推出机位限制
	if ('${info.JW_LIMIT2}' != '') {
		var jw2 = '${info.JW_LIMIT2}';
		seatT = jw2.split(",");
	}
	var typeId = '${info.TYPE_ID}';
	var reviceModel = '${info.MODEL}';
	var reviceNo = '${info.DEVICE_ID}';
</script>
</head>
<body>
	<div>
		<form class="layui-form" action="" id="createForm">
			<input id="id" name="id" type="hidden" value="${info.ID}" />
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">牵引车类型</label>
					<div class="layui-input-inline">
						<select class="form-control select2" lay-filter="tracotrtype"
							name="tracotrtype" id="tracotrtype">
							<option value="all">请选择</option>
							<c:forEach items="${dimData}" var="var">
								<option value="${var.CODE}"
									${info.TYPE_ID==var.CODE?"selected":"" }>${var.NAME}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">设备型号</label>
					<div class="layui-input-inline">
						<select class="form-control select2" lay-filter="unitmodel"
							name="unitmodel" id="unitmodel">
							<option value="all">请选择</option>
						</select>
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">设备编号</label>
					<div class="layui-input-inline">
						<select class="form-control select2" lay-filter="unitnumber"
							name="unitnumber" id="unitnumber">
							<option value="all">请选择</option>
						</select>
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">车牌号</label>
					<div class="layui-input-inline">
						<input id="carNumber" name="carNumber" htmlEscape="false" required
							placeholder="车牌号" class="layui-input" type="text"
							style="width: 180px;" value="${setUpVO.demand}" />
					</div>
				</div>
			</div>
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">描述</label>
					<div class="layui-input-inline">
						<input id="desc" name="desc" htmlEscape="false" required
							placeholder="描述" class="layui-input" type="text"
							style="width: 400px;" value="${info.NOTE}" />
					</div>
				</div>
			</div>
		</form>
		<div class="rows">
			<div class="col-md-6">
				<div class="layui-inline">
					<label class="layui-form-label" style="">可拖拽机型</label>
					<div class="layui-input-inline">
						<select class="form-control select2 airplaneTypeZ"
							data-type="airplaneTypeZ" name="airplaneTypeZ" id="airplaneTypeZ"
							multiple="multiple">
							<c:forEach items="${atcactypeList}" var="actype">
								<option value="${actype.CODE}">${actype.DESCRIPTION}</option>
							</c:forEach>
						</select>
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="layui-inline">
					<label class="layui-form-label">航空公司限制</label>
					<div class="layui-input-inline">
						<select class="form-control select2 airplaneZ"
							data-type="airplaneZ" name="airplaneZ" id="airplaneZ"
							multiple="multiple">
							<c:forEach items="${airlineList}" var="var">
								<option value="${var.CODE}">${var.DESCRIPTION}</option>
							</c:forEach>
						</select>
					</div>
				</div>
			</div>
		</div>
		<div class="rows">
			<div class="col-md-12">
				<div class="layui-inline">
					<label class="layui-form-label">机位限制</label>
					<div class="layui-input-inline">
						<select class="form-control select2 seatZ" data-type="seatZ"
							name="seatZ" id="seatZ" multiple="multiple">
							<c:forEach items="${apronTypeList}" var="var">
								<option value="${var.CODE}">${var.DESCRIPTION}</option>
							</c:forEach>
						</select>
					</div>
				</div>
			</div>
		</div>
		<div class="rows">
			<div class="col-md-6">
				<div class="layui-inline">
					<label class="layui-form-label">可推出机型</label>
					<div class="layui-input-inline">
						<select class="form-control select2 airplaneType"
							data-type="airplaneType" name="airplaneTypeT" id="airplaneTypeT"
							multiple="multiple">
							<c:forEach items="${atcactypeList}" var="actype">
								<option value="${actype.CODE}">${actype.DESCRIPTION}</option>
							</c:forEach>
						</select>
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="layui-inline">
					<label class="layui-form-label">航空公司限制</label>
					<div class="layui-input-inline">
						<select class="form-control select2 airplaneT"
							data-type="airplaneT" name="airplaneT" id="airplaneT"
							multiple="multiple">
							<c:forEach items="${airlineList}" var="var">
								<option value="${var.CODE}">${var.DESCRIPTION}</option>
							</c:forEach>
						</select>
					</div>
				</div>
			</div>
		</div>
		<div class="rows">
			<div class="col-md-12">
				<div class="layui-inline">
					<label class="layui-form-label">机位限制</label>
					<div class="layui-input-inline">
						<select class="form-control select2 seatT" data-type="seatT"
							name="seatT" id="seatT" multiple="multiple">
							<c:forEach items="${apronTypeList}" var="var">
								<option value="${var.CODE}">${var.DESCRIPTION}</option>
							</c:forEach>
						</select>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript"
		src="${ctxStatic}/prss/produce/tractorSafeguardSet.js"></script>
	<script type="text/javascript"
		src="${ctxStatic}/prss/produce/jquery.form2json.js"></script>
</body>
</html>