<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>非例行服务</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
	<div id="container">
		<div id="baseTables">
			<form id="tableForm"  class="layui-form" action="${ctx}/produce/special/save"
				method="post">
				<table id="baseTable" class="layui-table"
					style="width: 900px; text-align: center; margin: 10px auto;">
					<thead>
						<tr>
							<th colspan=6>非例行服务收费单</th>
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
							<td>起飞时间</td>
							<td><input type='text' name="departTime" id="departTime"
								class='form-control' value="${result.DEPART_TM}"
								readonly="readonly" /></td>
							<td>降落时间</td>
							<td><input type='text' name="arrivalTime" id="arrivalTime"
								class='form-control' value="${result.ARRIVAL_TM}"
								readonly="readonly" /></td>
						</tr>
						<!-- 							4 -->
						<tr>
							<td>航站</td>
							<td><input type='text' name="station" id="station"
								class='form-control' value="${result.STATION}"
								readonly="readonly" /></td>
							<td>机号</td>
							<td><input type='text' name="aircraftNumber"
								id="aircraftNumber" class='form-control'
								value="${result.AIRCRAFT_NUMBER}" readonly="readonly" /></td>
						</tr>
						<!-- 							5 -->
						<tr>
							<td>航班性质</td>
<!-- 							<td><input type='text' name="propertyName" id="propertyName" -->
<%-- 								class='form-control' value="${result.PROPERTY_NAME}"  --%>
<!-- 								readonly="readonly" /></td> -->
							<td>
								<select id="propertyCode"  name='propertyCode' class="layui-select">
									<option <c:if test="${result.PROPERTY_CODE==''}">selected</c:if> value=""></option>
									<option <c:if test="${result.PROPERTY_CODE=='航前'}">selected</c:if> value="航前">航前</option>
									<option <c:if test="${result.PROPERTY_CODE=='航后'}">selected</c:if> value="航后">航后</option>
									<option <c:if test="${result.PROPERTY_CODE=='短停'}">selected</c:if> value="短停">短停</option>
							</select></td>
							<td>服务项目</td>
							<td>
								<input type='text' name="servicePos" id="servicePos"
								class='form-control' value="${result.SERVICE_POS}"/>
							</td>
						</tr>	
						<!-- 							6 -->
						<tr>
							<td>单价</td>
							<td><input type='text' name="unitPrice" id="unitPrice"
								class='form-control' value="${result.UNIT_PRICE}" onblur="calculatePrice()" lay-verify="price"/></td>
							<td>用量或工时</td>
							<td><input type='text' name="duration" id="duration"
								class='form-control' value="${result.DURATION}" onblur="calculatePrice()" lay-verify="numberByCus"/></td>
						</tr>
						<!-- 							7 -->
						<tr>						
							<td>收费总价</td>
							<td><input type='text' name="allPrice" id="allPrice"
								class='form-control' value="${result.ALL_PRICE}" readonly="readonly" /></td>

							<td>操作人</td>
							<td>
							<input type='text' name="operatorName" id="operatorName"
								<c:if test="${type=='add'}">value="${username}"</c:if>
								<c:if test="${type=='edit'}">value="${result.OPERATOR_NAME}"</c:if>
								class='form-control'  readonly="readonly" />
<!-- 								<select id="operator" name="operator" lay-search> -->
<!-- 									<option value="">如不填写，则默认为当前用户</option> -->
<%-- 									<c:forEach items="${userArr}" var="item"> --%>
<%-- 										<option <c:if test="${result.OPERATOR==item.ID}">selected</c:if> value="${item.ID}" >${item.TEXT}</option> --%>
<%-- 									</c:forEach> --%>
<!-- 								</select>	 -->
							</td>
						</tr>
						<!-- 							8 -->
						<tr>								
							<td>提交时间</td>
							<td><input type='text' name="updateTime" id="updateTime" placeholder='如不填写，则默认为系统时间'
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});"
								class='form-control' value="${result.UPDATE_TM}" /></td>

							<td>机组签字</td>
							<td>
								<c:if test="${type=='add' || result.SIGN=='' || result.SIGN==null}"><input type='text' name="signRemark" id="signRemark" class='form-control' value="${result.SIGN_REMARK}"/></c:if>
								<c:if test="${type=='edit' && result.SIGN!='' && result.SIGN!=null}"><img src="${ctx }/produce/special/outputPicture?id=${result.SIGN}" height=45 width=80/></c:if>
							</td>
						</tr>
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
				<input type='text' name="acttypeCode" id="acttypeCode" value="${result.ACTTYPE_CODE}"/> 
				<input type='text' name="aln2code" id="aln2code" value="${result.ALN_2CODE}"/> 
				<input type='text' name="aln3code" id="aln3code" value="${result.ALN_3CODE}"/> 
<%-- 				<input type='text' name="propertyCode" id="propertyCode" value="${result.PROPERTY_CODE}"/>  --%>
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
		src="${ctxStatic}/prss/produce/billSpecialForm.js"></script>
</body>
</html>