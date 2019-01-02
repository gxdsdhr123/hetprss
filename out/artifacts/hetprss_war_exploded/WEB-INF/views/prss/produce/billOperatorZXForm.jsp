<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>装卸工操作记录单</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
	<div id="container">
		<div id="baseTables">
			<form id="tableForm"  class="layui-form" action="${ctx}/produce/billOperatorZX/save"
				method="post">
				<table id="baseTable" border="0" style="width: 900px; text-align: left; margin: 20px auto;">
					<tbody>
						<tr>
							<td style="width: 210px">进港航班号：${result.info.IN_FLIGHT_NUMBER}</td>
							<td style="width: 210px">出港航班号：${result.info.OUT_FLIGHT_NUMBER}</td>
							<td style="width: 210px">机号：${result.info.AIRCRAFT_NUMBER}</td>
							<td style="width: 210px">机型：${result.info.ACTTYPE_CODE}</td>
							<td style="width: 210px">机位：${result.info.ACTSTAND_CODE}</td>
						</tr>
						<tr>
							<td style="width: 210px">进港预落：${result.info.ETA}</td>
							<td style="width: 210px">出港预起：${result.info.ETD}</td>
							<td colspan="3">航线：${result.info.ROUTE}</td>
						</tr>

						<tr>		
							
						</tr>
					</tbody>
				</table>
				<hr>
				<table id="dataTable"></table>
				<button id="submitBtn" class="layui-btn" lay-submit lay-filter="save" style="display: none"></button>
				<div style="display: none;" >
				<input type='text' name="fltid" id="fltid" value="${result.FLTID}"/>
				<input type='hidden' name="detail" id="detail" value='${result.detail}'/>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript"
		src="${ctxStatic}/prss/produce/billOperatorZXForm.js"></script>
</body>
</html>