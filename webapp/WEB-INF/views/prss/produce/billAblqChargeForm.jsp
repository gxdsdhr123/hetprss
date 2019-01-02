<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>廊桥收费单</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
	<div id="container">
		<div id="baseTables">
			<form id="tableForm"  class="layui-form" action="${ctx}/produce/ablqCharge/save"
				method="post">
				<table id="baseTable" class="layui-table"
					style="width: 900px; text-align: center; margin: 10px auto;">
					<thead>
						<tr>
							<th colspan=6>廊桥收费单</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td style="width: 150px"><span style="color: red;">*</span>航班号</td>
							<td style="width: 300px">
								<input type='text' name="flightNumber"
								id="flightNumber" class='form-control'
								value="${result.FLIGHT_NUMBER}" onblur="getFltInfo()" lay-verify="required"/></td>
							<td style="width: 150px">航空公司</td>
							<td style="width: 300px"><input type='text' name="airlineShortName"
								id="airlineShortName" class='form-control' value="${result.AIRLINE_SHORTNAME}"
								readonly="readonly" /></td>
						</tr>
						<!-- 							2 -->
						<tr>
							<td><span style="color: red;">*</span>航班日期</td>
							<td><input type='text' maxlength="20" name='flightDate'
								id="flightDate" placeholder='请选择日期' class='form-control'
								onclick="WdatePicker({dateFmt:'yyyyMMdd'});"
								value="${result.FLIGHT_DATE}" onblur="getFltInfo()" lay-verify="required"/></td>
							<td>航班属性</td>
							<td>
								<select id="inOutFlag"  class='form-control'  lay-filter="valueChange">
									<option <c:if test="${fn:substring(result.IN_OUT_FLAG,0,1)==''}">selected</c:if> value=""></option>
									<option <c:if test="${fn:substring(result.IN_OUT_FLAG,0,1)=='A'}">selected</c:if> value="A">进港</option>
									<option <c:if test="${fn:substring(result.IN_OUT_FLAG,0,1)=='D'}">selected</c:if> value="D">出港</option>
								</select>
							</td>
						</tr>
						<!-- 							3 -->
						<tr>
							<td>机号</td>
							<td><input type='text' name="aircraftNumber"
								id="aircraftNumber" class='form-control'
								value="${result.AIRCRAFT_NUMBER}" readonly="readonly" /></td>
							<td>机型</td>
							<td><input type='text' name="acttypeCode" id="acttypeCode"
								class='form-control' value="${result.ACTTYPE_CODE}"
								readonly="readonly" /></td>
						</tr>
						<!-- 							4 -->
						<tr>
							<td>开始时间</td>
							<td><input type='text' name="startTime" placeholder="例：8点10分应填写0810"
								id="startTime" class='form-control'  lay-verify="fltTime"
								value="${result.START_TM}" /></td>
							<td>结束时间</td>
							<td><input type='text' name="endTime" placeholder="例：8点10分应填写0810"
								id="endTime" class='form-control'  lay-verify="fltTime"
								value="${result.END_TM}" /></td>
						</tr>
						
						<!-- 							6 -->
						<tr>
							<td>牵引车次数</td>
							<td><input type='text' name="nums"
								id="nums" class='form-control' lay-verify="numberByCus"
								value="${result.NUMS}" /></td>
								
							<td>操作人</td>
							<td>
								<input type='text' name="operatorName" id="operatorName"
								<c:if test="${type=='add'}">value="${username}"</c:if>
								<c:if test="${type=='edit'}">value="${result.OPERATOR_NAME}"</c:if>
								class='form-control'  readonly="readonly" />
								<%-- <select id="operator" name="operator" lay-search>
									<option value="">如不填写，则默认为当前用户</option>
									<c:forEach items="${userArr}" var="item">
										<option <c:if test="${result.OPERATOR==item.ID}">selected</c:if> value="${item.ID}" >${item.TEXT}</option>
									</c:forEach>
								</select> --%>	
							</td>
						</tr>
						<tr>
							<td>提交时间</td>
							<td><input type='text' name="updateTime" id="updateTime" placeholder='如不填写，则默认为系统时间'
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});"
								class='form-control' value="${result.UPDATE_TM}" /></td>
								
							<td>机组签字</td>
							<td>
								<img src="${ctx }/produce/ablqCharge/outputPicture?id=${result.SIGN}" height=45 width=80/>
<%-- 								<img style="width:300;height:40px;" src="data:image/png;base64,'${result.SIGN}'" /> --%>
							</td>
						</tr>
						<!-- 							6 -->
						<tr>
							<td>备注</td>
							<td colspan="3"><textarea  name="remark" id="remark"
								class='layui-textarea' >${result.REMARK}</textarea></td>
						</tr>
					</tbody>
				</table>
				<button id="submitBtn" class="layui-btn" lay-submit lay-filter="save" style="display: none"></button>
				<div style="display: none;" >
				<input type='text' name="id" id="id"  value="${result.ID}" />
				<input type='text' name="type" id="type"  value="${type}" />
				<input type='text' name="fltid" id="fltid" value="${result.FLTID}"/>
				<input type='text' name="aln2code" id="aln2code" value="${result.ALN_2CODE}"/> 
				<input type='text' name="aln3code" id="aln3code" value="${result.ALN_3CODE}"/> 
				<input type='text' name="inOutFlag" id="inOutFlagReal" value="${result.IN_OUT_FLAG}"/>
				<input type='text' name="operator" id="operator"
								<c:if test="${type=='add'}">value="${userid}"</c:if>
								<c:if test="${type=='edit'}">value="${result.OPERATOR}"</c:if>
								class='form-control'  readonly="readonly" />
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript"
		src="${ctxStatic}/prss/produce/billAblqChargeForm.js"></script>
</body>
</html>