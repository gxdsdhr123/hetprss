<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
		<title>航班监护记录编辑页面</title>
		<meta name="decorator" content="default" />
		<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
		<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
		<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript" src="${ctxStatic}/prss/produce/fltGuardianshipForm.js"></script>
		<script type="text/javascript">
			var operateType = "${operateType}";
		</script>
	</head>
	<body>
		<div id="container">
			<div id="baseTables">
				<form id="tableForm" class="layui-form" action="${ctx}/produce/fltGuardianship/save" method="post">
					<!-- 新增属性值 -->
					<input type="hidden" id="id" name="id" value="${modifyData.id}" style="background-color: black">
					<input type="hidden" id="fltId" name="fltId" value="${modifyData.fltId}" style="background-color: black">
					<input type="hidden" id="aln2code" name="aln2code" value="${modifyData.aln2code}" style="background-color: black">
					<input type="hidden" id="acttypeCode" name="acttypeCode" value="${modifyData.acttypeCode}" style="background-color: black">
					<input type="hidden" id="aln3code" name="aln3code" value="${modifyData.aln3code}" style="background-color: black">
					<input type="hidden" id="inOutFlagReal" name="inOutFlag" value="${modifyData.inOutFlag}" style="background-color: black">
					<input type="hidden" id="operatorName" name="operatorName" value="${modifyData.operatorName}" style="background-color: black">
					<input type="hidden" id="operator" name="operator" value="${modifyData.operator}" style="background-color: black">
					<input type="hidden" id="aircraftNumberReal" name="aircraftNumber" value="${modifyData.aircraftNumber}" style="background-color: black">
					<input type="hidden" id="actstandCodeReal" name="actstandCode" value="${modifyData.actstandCode}" style="background-color: black">
					
					<table id="baseTable" class="layui-table" style="width:590px;text-align:center;margin:10px auto;">
						<thead>
							<tr>
								<th colspan="4">航班监护记录表</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>日期</td>
								<td>
									<input type='text' maxlength="20" name='flightDate' id="flightDate" placeholder='请选择日期' class='form-control'
									onclick="WdatePicker({dateFmt:'yyyyMMdd'});" value="${modifyData.flightDate}"/>
								</td>
								<td>航班号</td>
								<td>
									<input type='text' name="flightNumber" id="flightNumber" class='form-control' value="${modifyData.flightNumber}"/>
								</td>
							</tr>
							<tr>
								<td>航班类型</td>
								<td>
									<select lay-filter="flightType" id="inOutFlag" class="form-control select2">
										<option value="" disabled selected="selected">请选择</option>
										<option <c:if test="${fn:substring(modifyData.inOutFlag,0,1)=='A'}">selected</c:if> value="A">进港航班</option>
										<option <c:if test="${fn:substring(modifyData.inOutFlag,0,1)=='D'}">selected</c:if> value="D">出港航班</option>
									</select>
								</td>
								<td>监护员</td>
								<td>
									<input type='text' class='form-control' value="${modifyData.operatorName}" disabled="disabled"/>
									<%-- <select lay-filter="operator" name="operator" id="operator" class="form-control select2" lay-search="">
										<option value="" disabled selected="selected">请选择</option>
										<c:forEach items="${sysUserSource}" var="item">
											<option <c:if test="${modifyData.operator==item.id}">selected</c:if> value="${item.id}" >${item.text}</option>
										</c:forEach>
									</select> --%>
								</td>
							</tr>
							<tr>
								<td>飞机号</td>
								<td><input type='text' id="aircraftNumber" class='form-control' value="${modifyData.aircraftNumber}"/></td>
								<td>机位</td>
								<td><input type='text' id="actstandCode" class='form-control' value="${modifyData.actstandCode}"/></td>
							</tr>
							<tr>
								<td>地面服务部</td>
								<td><input type='text' name="dmfwNum" id="dmfwNum" class='form-control' value="${modifyData.dmfwNum}"/></td>
								<td>机务</td>
								<td><input type='text' name="jwNum" id="jwNum" class='form-control' value="${modifyData.jwNum}"/></td>
							</tr>
							<tr>
								<td>清洁</td>
								<td><input type='text' name="qjNum" id="qjNum" class='form-control' value="${modifyData.qjNum}"/></td>
								<td>航务保障部</td>
								<td><input type='text' name="hwbzNum" id="hwbzNum" class='form-control' value="${modifyData.hwbzNum}"/></td>
							</tr>
							<tr>
								<td>货运</td>
								<td><input type='text' name="hyNum" id="hyNum" class='form-control' value="${modifyData.hyNum}"/></td>
								<td>机组</td>
								<td><input type='text' name="jzNum" id="jzNum" class='form-control' value="${modifyData.jzNum}"/></td>
							</tr>
							<tr>
								<td>其他部门</td>
								<td><input type='text' name="otherNum" id="otherNum" class='form-control' value="${modifyData.otherNum}"/></td>
								<td></td><td></td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>
		</div>
	</body>
</html>