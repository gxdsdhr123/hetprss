<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>新增单据</title>
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/prss/produce/newAlnbill.js"></script>
</head>
<body>
	<form id="billForm" action="">
		<input type="hidden" name="TYPE_CODE" id="TYPE_CODE" value="${billInfo.TYPE_CODE}">
		<input type="hidden" name="ID" id="ID" value="${billInfo.ID}">
		<input type="hidden" name="DATA_TABLE" id="DATA_TABLE" value="${billInfo.DATA_TABLE}">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">STATION</label>
				<div class="layui-input-inline">
					<input type="text" autocomplete="off" class="layui-input" value="北京" readonly="readonly">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">NO.</label>
				<div class="layui-input-inline">
					<input type="text" autocomplete="off" class="layui-input" name="ID" id="ID"
						value="${alnBillId}">
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">FLT.NO.</label>
				<div class="layui-input-inline">
					<input type="text" autocomplete="off" class="layui-input" name="flightNumber"
						id="flightNumber" value="${billInfo.flightNumber}">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">A/C TYPE</label>
				<div class="layui-input-inline">
					<input type="text" autocomplete="off" class="layui-input" name="actType" id="actType"
						value="${billInfo.actType}">
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">A/C REGN</label>
				<div class="layui-input-inline">
					<input type="text" autocomplete="off" class="layui-input" name="aircraftNumber"
						id="aircraftNumber" value="${billInfo.aircraftNumber}">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">DATE</label>
				<div class="layui-input-inline">
					<input type="text" autocomplete="off" class="layui-input" name="flightDate" id="flightDate"
						onClick="WdatePicker({dateFmt:'yyyyMMdd'})" value="${billInfo.flightDate}" readonly="readonly">
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">PARKING BAY</label>
				<div class="layui-input-inline">
					<input type="text" autocomplete="off" class="layui-input" name="actstandCode" id="actstandCode"
						value="${billInfo.actstandCode}">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">航班性质</label>
				<div class="layui-input-inline">
					<select name="fltAttrCode" lay-filter="aihao" style="height:32px;">
						<option value=""></option>
						<option value="D" <c:if test="${billInfo.fltAttrCode == 'D' }">selected</c:if>>国内</option>
						<option value="I" <c:if test="${billInfo.fltAttrCode == 'I' }">selected</c:if>>国际</option>
						<option value="M" <c:if test="${billInfo.fltAttrCode == 'M' }">selected</c:if>>混装</option>
					</select>
				</div>
			</div>
		</div>
		<table class="layui-table">
			<tbody name="tbody">
				<tr>
					<td align="center" colspan="4">
						<div class="checkbox">
							<label>
								<input type="checkbox" name="arrival" value="${billInfo.arrival}">
								ARRIVAL
							</label>
						</div>
					</td>
					<td align="center" colspan="4">
						<div class="checkbox">
							<label>
								<input type="checkbox" name="transit" value="${billInfo.transit}">
								TRANSIT
							</label>
						</div>
					</td>
					<td align="center" colspan="4">
						<div class="checkbox">
							<label>
								<input type="checkbox" name="nightstop" value="${billInfo.nightstop}">
								NIGHTSTOP
							</label>
						</div>
					</td>
				</tr>
				<tr>
					<td align="center" colspan="4">
						<div class="checkbox">
							<label>
								<input type="checkbox" name="departure" value="${billInfo.departure}">
								DEPARTURE
							</label>
						</div>
					</td>
					<td align="center" colspan="4">
						<div class="checkbox">
							<label>
								<input type="checkbox" name="returntoramp" value="${billInfo.returntoramp}">
								RETURN TO RAMP
							</label>
						</div>
					</td>
					<td align="center" colspan="4">
						<div class="checkbox">
							<label>
								<input type="checkbox" name="others" value="${billInfo.others}">
								OTHERS
							</label>
						</div>
					</td>
				</tr>
				<tr>
					<td align="center" colspan="4">
						TOWING
					</td>
					<td align="center" colspan="4">
						<label class="layui-form-label">FROM</label>
						<div class="layui-input-inline">
							<input type="text" autocomplete="off" class="layui-input" name="towingFrom" id="towingFrom" value="${billInfo.towingFrom}">
						</div>
					</td>
					<td align="center" colspan="4">
						<label class="layui-form-label">TO</label>
						<div class="layui-input-inline">
							<input type="text" autocomplete="off" class="layui-input" name="towingTo" id="towingTo" value="${billInfo.towingTo}">
						</div>
					</td>
				</tr>
				<tr>
					<td align="center" colspan="3">
						<label class="layui-form-label">STA</label>
						<div class="layui-input-inline">
							<input type="text" autocomplete="off" class="layui-input" name="sta" id="sta" 
								value="${billInfo.sta}">
						</div>
					</td>
					<td colspan="3">
						<label class="layui-form-label">STD</label>
						<div class="layui-input-inline">
							<input type="text" autocomplete="off" class="layui-input" name="std" id="std" 
								value="${billInfo.std}">
						</div>
					</td>
					<td colspan="3">
						<label class="layui-form-label">ATA</label>
						<div class="layui-input-inline">
							<input type="text" autocomplete="off" class="layui-input" name="ata" id="ata" 
								value="${billInfo.ata}">
						</div>
					</td>
					<td colspan="3">
						<label class="layui-form-label">ATD</label>
						<div class="layui-input-inline">
							<input type="text" autocomplete="off" class="layui-input" name="atd" id="atd" 
								value="${billInfo.atd}">
						</div>
					</td>
				</tr>
				<tr class="text-center">
					<td colspan="2">ENGINE NUMBER</td>
					<td colspan="2">1</td>
					<td colspan="2">2</td>
					<td colspan="2">3</td>
					<td colspan="2">4</td>
					<td colspan="2">APU</td>
				</tr>
				<tr class="text-center">
					<td colspan="2">OIL UPLIFT(QTS/PINTS)</td>
					<td colspan="2">
						<input type="text" autocomplete="off" class="layui-input" name="oilUpliftOne"
							value="${billInfo.oilUpliftOne}">
					</td>
					<td colspan="2">
						<input type="text" autocomplete="off" class="layui-input" name="oilUpliftTwo"
							value="${billInfo.oilUpliftTwo}">
					</td>
					<td colspan="2">
						<input type="text" autocomplete="off" class="layui-input" name="oilUpliftThree"
							value="${billInfo.oilUpliftThree}">
					</td>
					<td colspan="2">
						<input type="text" autocomplete="off" class="layui-input" name="oilUpliftFour"
							value="${billInfo.oilUpliftFour}">
					</td>
					<td colspan="2">
						<input type="text" autocomplete="off" class="layui-input" name="oilUpliftApu"
							value="${billInfo.oilUpliftApu}">
					</td>
				</tr>
				<tr class="text-center">
					<td colspan="2">C S D OIL UPLIFT</td>
					<td colspan="2">
						<input type="text" autocomplete="off" class="layui-input" name="csdoilUpliftOne"
							value="${billInfo.csdoilUpliftOne}">
					</td>
					<td colspan="2">
						<input type="text" autocomplete="off" class="layui-input" name="csdoilUpliftTwo"
							value="${billInfo.csdoilUpliftTwo}">
					</td>
					<td colspan="2">
						<input type="text" autocomplete="off" class="layui-input" name="csdoilUpliftThree"
							value="${billInfo.csdoilUpliftThree}">
					</td>
					<td colspan="2">
						<input type="text" autocomplete="off" class="layui-input" name="csdoilUpliftFour"
							value="${billInfo.csdoilUpliftFour}">
					</td>
					<td colspan="2">
						<input type="text" autocomplete="off" class="layui-input" name="csdoilUpliftApu"
							value="${billInfo.csdoilUpliftApu}">
					</td>
				</tr>
				<tr class="text-center">
					<td colspan="2">HYDRAULIC OIL UPLIFT</td>
					<td colspan="2">
						<input type="text" autocomplete="off" class="layui-input" name="hydraulicoilUpliftOne"
							value="${billInfo.hydraulicoilUpliftOne}">
					</td>
					<td colspan="2">
						<input type="text" autocomplete="off" class="layui-input" name="hydraulicoilUpliftTwo"
							value="${billInfo.hydraulicoilUpliftTwo}">
					</td>
					<td colspan="2">
						<input type="text" autocomplete="off" class="layui-input" name="hydraulicoilUpliftThree"
							value="${billInfo.hydraulicoilUpliftThree}">
					</td>
					<td colspan="2">
						<input type="text" autocomplete="off" class="layui-input" name="hydraulicoilUpliftFour"
							value="${billInfo.hydraulicoilUpliftFour}">
					</td>
					<td colspan="2">
						<input type="text" autocomplete="off" class="layui-input" name="hydraulicoilUpliftApu"
							value="${billInfo.hydraulicoilUpliftApu}">
					</td>
				</tr>
				<tr class="text-center">
					<td colspan="4">
						ENGINE OIL TYPE&nbsp;&nbsp;
						<div class="layui-input-inline">
							<input type="text" autocomplete="off" class="layui-input" name=""
								value="">
						</div>
					</td>
					<td colspan="4">
						STOCK
						<select name="engineoilStock" lay-filter="aihao" value="${billInfo.engineoilStock}" style="height:32px;">
							<option value=""></option>
							<option value="OPERATOR"<c:if test="${billInfo.engineoilStock == 'OPERATOR' }">selected</c:if>>OPERATOR</option>
							<option value="BGS"<c:if test="${billInfo.engineoilStock == 'BGS' }">selected</c:if>>BGS</option>
						</select>
					</td>
					<td colspan="4">
						QTY
						<div class="layui-input-inline">
							<input type="text" autocomplete="off" class="layui-input" name="engineoilQty"
								value="${billInfo.engineoilQty}">
						</div>
					</td>
				</tr>
				<tr class="text-center">
					<td colspan="4">
						HYD TYPE&nbsp;&nbsp;
						<div class="layui-input-inline">
							<input type="text" autocomplete="off" class="layui-input" name=""
								value="">
						</div>
					</td>
					<td colspan="4">
						STOCK
						<select name="hydStock" lay-filter="" style="height:32px;">
							<option value=""></option>
							<option value="OPERATOR"<c:if test="${billInfo.hydStock == 'OPERATOR' }">selected</c:if>>OPERATOR</option>
							<option value="BGS"<c:if test="${billInfo.hydStock == 'BGS' }">selected</c:if>>BGS</option>
						</select>
					</td>
					<td colspan="4">
						QTY
						<div class="layui-input-inline">
							<input type="text" autocomplete="off" class="layui-input" name="hydQty"
								value="${billInfo.hydQty}">
						</div>
					</td>
				</tr>
				<tr class="text-center">
					<td colspan="4">
						APU OIL TYPE&nbsp;&nbsp;
						<div class="layui-input-inline">
							<input type="text" autocomplete="off" class="layui-input" name=""
								value="">
						</div>
					</td>
					<td colspan="4">
						STOCK
						<select name="apuoilStock" lay-filter="" style="height:32px;">
							<option value=""></option>
							<option value="OPERATOR"<c:if test="${billInfo.apuoilStock == 'OPERATOR' }">selected</c:if>>OPERATOR</option>
							<option value="BGS"<c:if test="${billInfo.apuoilStock == 'BGS' }">selected</c:if>>BGS</option>
						</select>
					</td>
					<td colspan="4">
						QTY
						<div class="layui-input-inline">
							<input type="text" autocomplete="off" class="layui-input" name="apuoilQty"
								value="${billInfo.apuoilQty}">
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="6" align="center">GROUND EQUIPMENT</td>
					<td colspan="6" align="center">HOUR/SERVICE</td>
				</tr>
				<tr>
					<td colspan="6" align="center">AIRCRAFT PUSH-OUT</td>
					<td colspan="6" align="center">
						<input type="text" autocomplete="off" class="layui-input" name="aircraftPushout"
							value="${billInfo.aircraftPushout}">
					</td>
				</tr>
				<tr>
					<td colspan="6" align="center">AIRCRAFT TOWING</td>
					<td colspan="6" align="center">
						<input type="text" autocomplete="off" class="layui-input" name="aircraftTowing"
							value="${billInfo.aircraftTowing}">
					</td>
				</tr>
				<tr>
					<td colspan="6" align="center">WATER SERVICING</td>
					<td colspan="6" align="center">
						<input type="text" autocomplete="off" class="layui-input" name="waterServicing"
							value="${billInfo.waterServicing}">
					</td>
				</tr>
				<tr>
					<td colspan="6" align="center">TOILET SERVICING</td>
					<td colspan="6" align="center">
						<input type="text" autocomplete="off" class="layui-input" name="toiletServicing"
							value="${billInfo.toiletServicing}">
					</td>
				</tr>
				<tr>
					<td colspan="6" align="center">GROUND POWER UNIT</td>
					<td colspan="6" align="center">
						<input type="text" autocomplete="off" class="layui-input" name="groundPowerUnit"
							value="${billInfo.groundPowerUnit}">
					</td>
				</tr>
				<tr>
					<td colspan="6" align="center">AIR CONDITIONG UNIT</td>
					<td colspan="6" align="center">
						<input type="text" autocomplete="off" class="layui-input" name="airConditiongUnit"
							value="${billInfo.airConditiongUnit}">
					</td>
				</tr>
				<tr>
					<td colspan="6" align="center">GAS TURBINE STARTER UNIT</td>
					<td colspan="6" align="center">
						<input type="text" autocomplete="off" class="layui-input" name="gasTurbineStarterUnit"
							value="${billInfo.gasTurbineStarterUnit}">
					</td>
				</tr>
				<tr>
					<td colspan="6" align="center">OXYGEN CHARGING</td>
					<td colspan="6" align="center">
						<input type="text" autocomplete="off" class="layui-input" name="oxygenCharging"
							value="${billInfo.oxygenCharging}">
					</td>
				</tr>
				<tr>
					<td colspan="6" align="center">NITROGEN CHARGING</td>
					<td colspan="6" align="center">
						<input type="text" autocomplete="off" class="layui-input" name="nitrogenCharging"
							value="${billInfo.nitrogenCharging}">
					</td>
				</tr>
				<tr>
					<td colspan="6" align="center">MAINTENANCE STEPS</td>
					<td colspan="6" align="center">
						<input type="text" autocomplete="off" class="layui-input" name="maintenanceSteps"
							value="${billInfo.maintenanceSteps}">
					</td>
				</tr>
				<tr>
					<td colspan="6" align="center">MAINT.PLATFORM</td>
					<td colspan="6" align="center">
						<input type="text" autocomplete="off" class="layui-input" name="maintPlatform"
							value="${billInfo.maintPlatform}">
					</td>
				</tr>
				<tr>
					<td colspan="6" align="center">WHEEL JACKS</td>
					<td colspan="6" align="center">
						<input type="text" autocomplete="off" class="layui-input" name="wheelJacks"
							value="${billInfo.wheelJacks}">
					</td>
				</tr>
				<tr>
					<td colspan="6" align="center">EQUIPMENT TOW TUG</td>
					<td colspan="6" align="center">
						<input type="text" autocomplete="off" class="layui-input" name="equipmentTowTug"
							value="${billInfo.equipmentTowTug}">
					</td>
				</tr>
				<tr>
					<td colspan="6" rowspan="2" align="center">RECTIFICATION/ADDITIONAL SERVICE</td>
					<td colspan="6" align="center">MAN HOURS</td>
				</tr>
				<tr>
					<td colspan="3" align="center">ENGR</td>
					<td colspan="3" align="center">MECH</td>
				</tr>
			</tbody>
		</table>
		<table id="addtr" style="display: none;">
			<tr onclick="clickRow(this)">
				<td colspan="6" align="center">
					<input type="text" autocomplete="off" class="layui-input" name="RECTIFICATION">
				</td>
				<td colspan="3" align="center">
					<input type="text" autocomplete="off" class="layui-input" name="ENGR">
				</td>
				<td colspan="3" align="center">
					<input type="text" autocomplete="off" class="layui-input" name="MECH">
				</td>
			</tr>
		</table>
		<div class="text-center" hidden="">
			<a class="btn btn-social-icon btn-bitbucket" onclick="addtr()">
				<i class="fa fa-plus"></i>
			</a>
			<a class="btn btn-social-icon btn-bitbucket" onclick="minustr()">
				<i class="fa fa-minus"></i>
			</a>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">操作人</label>
				<div class="layui-input-inline">
                    <select class="form-control select2 operator" data-type="operator"
                        name="operator" id="operator" >
                        <c:forEach items="${operatorsList}" var="item">
                            <option value="${item.CODE}" ${ item.CODE==billInfo.operator?'selected':''}>${item.DESCRIPTION}</option>
                        </c:forEach>
                    </select>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">创建时间</label>
				<div class="layui-input-inline">
					<label class="layui-form-label">${billInfo.createDate}</label>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">航空公司代办</label>
				<div id="SIGNATORYdiv" class="layui-input-inline">
				    <input type="text" autocomplete="off" class="layui-input"
                            name="sign" id="sign" value="${billInfo.sign }">
				    <c:if test="${flag=='edit' and  billInfo.signatory !=null and billInfo.signatory !=''}">
					   <input type="hidden" name="SIGNATORY" id="SIGNATORY" value="${billInfo.signatory}">
					   <img src="${ctx}/produce/bill/outputPicture?id=${billInfo.signatory}" height=45 width=80 />
					</c:if>
				</div>
			</div>
		</div>
	</form>
</body>
</html>