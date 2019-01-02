<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
		<title>清仓操作组勤务报告新增页面</title>
		<meta name="decorator" content="default" />
		<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
		<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
		<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript">
			var PATH = '${ctx}';
		</script>
	</head>
	<body>
		<div id="container">
			<div id="baseTables">
				<form id="tableForm"  action="${ctx}/produce/qcczAssignReport/doSave" method="post">
					<table id="baseTable" class="layui-table" style="width:900px;text-align:center;margin:10px auto;">
						<thead>
							<tr>
								<th colspan=6>机务部清舱操作组勤务报告</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td style="width:150px"><span style="color:red;">*</span>航班号</td>
								<td style="width:150px">
									<input type='text' name="ID" id="ID" class='form-control' value="${result.ID}" style="display:none;"/>
									<input type='text' name="FLTID" id="FLTID" class='form-control' value="${result.FLTID}" style="display:none;"/>
									<input type='text' name="JOB_TYPE" id="JOB_TYPE" class='form-control' value="${result.JOB_TYPE}" style="display:none;"/>
									<input type='text' name="flightNumber" id="flightNumber" class='form-control' value="${result.FLIGHT_NUMBER}"/>
								</td>
								<td style="width:150px"><span style="color:red;">*</span>航班日期</td>
								<td style="width:150px">
									<input type='text' maxlength="20" name='flightDate' id="flightDate"
									placeholder='请选择日期' class='form-control'
									onclick="WdatePicker({dateFmt:'yyyyMMdd'});" value="${result.FLIGHT_DATE}"/>
								</td>
								<td style="width:150px">机位</td>
								<td style="width:150px">
									<input type='text' name="ACTSTAND_CODE" id="ACTSTAND_CODE" class='form-control' disabled="disabled" value="${result.ACTSTAND_CODE}"/>
								</td>
							</tr>
							<tr>
								<td>机号</td>
								<td><input type='text' name="AIRCRAFT_NUMBER" id="AIRCRAFT_NUMBER" class='form-control' value="${result.AIRCRAFT_NUMBER}"/></td>
								<td>机型</td>
								<td><input type='text' name="ACTTYPE_CODE" id="ACTTYPE_CODE" class='form-control' value="${result.ACTTYPE_CODE}"/></td>
								<td>航班类型</td>
								<td><input type='text' name="PROPERTY_CODE" id="PROPERTY_CODE" class='form-control' disabled="disabled" value="${result.PROPERTY_CODE}"/></td>
							</tr>
							<tr>
								<td>实际到港时间</td>
								<td><input type='text' name="ATA" id="ATA" class='form-control' disabled="disabled" value="${result.ATA}"/></td>
								<td>实际离岗时间</td>
								<td><input type='text' name="ATD" id="ATD" class='form-control' disabled="disabled" value="${result.ATD}"/></td>
								<td><span style="color:red;">*</span>任务类型</td>
								<td>
									<select name="TYPENAME" id="TYPENAME" class="select2 form-control">
										<option value=""></option>
										<c:if test="${result.JOB_TYPE != null}">
											<option value="${result.JOB_TYPE}" selected>${result.TYPENAME}</option>
										</c:if>
									</select>
								</td>
							</tr>
							<tr>
								<td>接收任务时间</td>
								<td colspan=2>
									<input type='text' name="ACT_ARRANGE_TM" id="ACT_ARRANGE_TM" class='form-control' disabled="disabled" value="${result.ACT_ARRANGE_TM}"/>
								</td>
								<td>到位时间</td>
								<td colspan=2>
									<input type='text' name="DW_FT" id="DW_FT" class='form-control' disabled="disabled" value="${result.DW_FT}"/>
								</td>
							</tr>
							<tr>
								<td>完成清舱时间</td>
								<td colspan=2>
									<input type='text' name="WC_FT" id="WC_FT" class='form-control' disabled="disabled" value="${result.WC_FT}"/>
								</td>
								<td>开始清舱时间</td>
								<td colspan=2>
									<input type='text' name="KSCZ_FT" id="KSCZ_FT" class='form-control' disabled="disabled" value="${result.KSCZ_FT}"/>
								</td>
							</tr>
							<tr>
								<td>卫生间清洁人员</td>
								<td colspan=2>
									<select name="wsjqj" id="wsjqj" class="select2 form-control" multiple="multiple">
										<option value=""></option>
										<c:forEach items="${peopleList}" var="people">
											<option value="${people.ID}">${people.NAME}</option>
										</c:forEach>
									</select>
								</td>
								<td>服务台/驾驶舱清洁人员</td>
								<td colspan=2>
									<select name="fwt" id="fwt" class="select2 form-control" multiple="multiple">
										<option value=""></option>
										<c:forEach items="${peopleList}" var="people">
											<option value="${people.ID}">${people.NAME}</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td>地毯清洁人员</td>
								<td colspan=2>
									<select name="dtqj" id="dtqj" class="select2 form-control" multiple="multiple">
										<option value=""></option>
										<c:forEach items="${peopleList}" var="people">
											<option value="${people.ID}">${people.NAME}</option>
										</c:forEach>
									</select>
								</td>
								<td>重级清洁人员</td>
								<td colspan=2>
									<select name="zjcz" id="zjcz" class="select2 form-control" multiple="multiple">
										<option value=""></option>
										<c:forEach items="${peopleList}" var="people">
											<option value="${people.ID}">${people.NAME}</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td>客舱清洁人员</td>
								<td colspan=5>
									<select name="kcqj" id="kcqj" class="select2 form-control" multiple="multiple">
										<option value=""></option>
										<c:forEach items="${peopleList}" var="people">
											<option value="${people.ID}">${people.NAME}</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td>备注</td>
								<td colspan=5>
									<input type='text' name="REMARK" id="REMARK" class='form-control' value="${result.REMARK}"/>
								</td>
							</tr>
							<tr>
								<td colspan=3>清舱副领班</td>
								<td colspan=3>乘务长签字</td>
							</tr>
							<tr>
								<td colspan=3>
									<select name="lingban" id="lingban" class="select2 form-control">
										<option value=""></option>
										<c:forEach items="${peopleList}" var="people">
											<option value="${people.ID}" <c:if test="${result.OPERATOR == people.ID}" >selected</c:if>>${people.NAME}</option>
										</c:forEach>
									</select>
								</td>
								<td colspan=3>
									<img src="${ctx}/flightDynamic/readExpAtta?fileId=${result.SIGNATORY}" style="width:100%;height:200px;"/>
								</td>
							</tr>
						</tbody>
					</table>
					<input type='text' name="selectPeople1" id="selectPeople1" value="${selectPeople1}" style="display:none;"/>
					<input type='text' name="selectPeople2" id="selectPeople2" value="${selectPeople2}" style="display:none;"/>
					<input type='text' name="selectPeople3" id="selectPeople3" value="${selectPeople3}" style="display:none;"/>
					<input type='text' name="selectPeople4" id="selectPeople4" value="${selectPeople4}" style="display:none;"/>
					<input type='text' name="selectPeople5" id="selectPeople5" value="${selectPeople5}" style="display:none;"/>
					<input type='text' name="operator" id="operator" value="${result.OPERATOR}" style="display:none;"/>
				</form>
			</div>
		</div>
	<script type="text/javascript" src="${ctxStatic}/prss/produce/qcczAssignReportInfo.js"></script>
	</body>
</html>