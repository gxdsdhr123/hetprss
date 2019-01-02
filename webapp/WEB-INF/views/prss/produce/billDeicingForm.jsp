<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>飞机除、防冰单据Form</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
	<div id="container">
		<div id="baseTables">
			<form id="tableForm"  class="layui-form" action="${ctx}/produce/deicing/save"
				method="post">
				<table id="baseTable" class="layui-table"
					style="width: 900px; text-align: center; margin: 10px auto;">
					<thead>
						<tr>
							<th colspan=6>除/防冰服务收费单</th>
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
						<tr>
							<td>机号</td>
							<td><input type='text' name="aircraftNumber"
								id="aircraftNumber" class='form-control'
								value="${result.AIRCRAFT_NUMBER}" readonly="readonly" /></td>
							<td>航站</td>
							<td><input type='text' name="station" id="station"
								class='form-control' value="${result.STATION}"
								readonly="readonly" /></td>
						</tr>
						<tr>
							<td>航段</td>
							<td><input type='text' name="routeName"
								id="routeName" class='form-control'
								value="${result.ROUTE_NAME}" readonly="readonly" /></td>
							<td>机型</td>
							<td><input type='text' name="acttypeCode" id="acttypeCode"
								class='form-control' value="${result.ACTTYPE_CODE}"
								readonly="readonly" /></td>
						</tr>
						<tr>
							<td>起飞时间</td>
							<td colspan="3"><input type='text' name="ata" id="ata"
								class='form-control' value="${result.ATA}"
								readonly="readonly" /></td>
						</tr>
						<tr>
							<td>除冰车次</td>
							<td><input type='text' name="defidCarnum"
								id="defidCarnum" class='form-control'
								value="${result.DEFID_CARNUM==null?1:result.DEFID_CARNUM}"/></td>
							<td>外界温度</td>
							<td><input type='text' name="temperature" id="temperature"
								class='form-control' value="${result.TEMPERATURE}"/></td>
						</tr>
						<tr>
							<td >除冰液型号</td>
							<td colspan="3">
								<input type="radio" class="form-control" value="1" name="defidModel" title="FCY-1A" ${result.DEFID_MODEL==1?"checked":""}/>
								<input type="radio" class="form-control" value="2" name="defidModel" title="FCY-IBO+" ${result.DEFID_MODEL==2?"checked":""}/>
								<input type="radio" class="form-control" value="3" name="defidModel" title="CLEAN WING-I" ${result.DEFID_MODEL==3?"checked":""}/>
							</td>
						</tr>
						<tr>
							<td >除冰液用量</td>
							<td colspan="3">
								<input type='text' name="defidDosage" id="defidDosage"
								class='form-control' value="${result.DEFID_DOSAGE}"/>
							</td>
						</tr>
						<tr>
							<td >防冰液型号</td>
							<td >
								<input type="radio" class="form-control" value="1" name="aifidModel" title="FCY-2" ${result.AIFID_MODEL==1?"checked":""}/>
							</td>
							<td >防冰液用量</td>
							<td >
								<input type='text' name="aifidDosage" id="aifidDosage"
								class='form-control' value="${result.AIFID_DOSAGE}"/>
							</td>
						</tr>
						<tr>
							<td>进行除防冰服务的主要原因</td>
							<td colspan="3"><textarea  name="reason" id="reason"
								class='layui-textarea' >${result.REASON}</textarea></td>
						</tr>
						<tr>
							<td>操作人</td>
							<td colspan="3">
								<input type='text' name="operatorName" id="operatorName"
								<c:if test="${type=='add'}">value="${username}"</c:if>
								<c:if test="${type=='edit'}">value="${result.OPERATOR_NAME}"</c:if>
								class='form-control'  readonly="readonly" />
							</td>
						</tr>
						<tr>
							<td>机组签字</td>
							<td colspan="3">
								<c:if test="${type=='add' || result.SIGN=='' || result.SIGN==null}"><input type='text' name="signRemark" id="signRemark" class='form-control' value="${result.SIGN_REMARK}"/></c:if>
								<c:if test="${type=='edit' && result.SIGN!='' && result.SIGN!=null}"><img src="${ctx }/produce/deicing/outputPicture?id=${result.SIGN}" height=45 width=80/></c:if>
							</td>
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
<%-- 				<input type='text' name="propertyCode" id="propertyCode" value="${result.PROPERTY_CODE}"/>  --%>
				<input type='text' name="inOutFlag" id="inOutFlagReal" value="${result.IN_OUT_FLAG}"/> 
				<input type='text' name="routeName" id="routeName" value="${result.ROUTE_NAME}"/> 
				<input type='text' name="operator" id="operator"
								<c:if test="${type=='add'}">value="${userid}"</c:if>
								<c:if test="${type=='edit'}">value="${result.OPERATOR}"</c:if>
								class='form-control'  readonly="readonly" />
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript"
		src="${ctxStatic}/prss/produce/billDeicingForm.js"></script>
</body>
</html>