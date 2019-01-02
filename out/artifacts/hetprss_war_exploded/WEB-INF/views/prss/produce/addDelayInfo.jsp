<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>航延信息录入</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<link href="${ctxStatic}/prss/produce/css/addDelayInfo.css" rel="stylesheet" />
</head>
<body>
	<h1 class="bill-title">航延信息录入</h1>
	<table class="header_table">
		<tr>
			<td>航班日期：<span>${fltInfo.FLIGHT_DATE}</span></td>
			<td>航班号：<span>${fltInfo.FLIGHT_NUMBER}</span></td>
			<td>机号：<span>${fltInfo.AIRCRAFT_NUMBER}</span></td>
			<td>延误原因：<span>${fltInfo.DELAY_REASON}</span></td>
		</tr>
	</table>
	<form id="form1" name="form1">
		<input type="hidden" id="delayInfoId" name="delayInfoId" value="${delay.delayInfoId }"/>
		<input type="hidden" id="fltid" name="fltid" value="${fltid }"/>
		<input type="hidden" id="flightNumber" name="flightNumber" value="${fltInfo.FLIGHT_NUMBER}"/>
		<input type="hidden" id="flightDate" name="flightDate" value="${fltInfo.FLIGHT_DATE}"/>
		<input type="hidden" id="delayReason" name="delayReason" value="${fltInfo.DELAY_REASON}"/>
		
		<table id="inputTable">
			<colgroup>
				<col width="5%">
				<col width="5%">
				<col width="5%">
				<col width="5%">
				<col width="5%">
				<col width="5%">
				<col width="5%">
				<col width="5%">
				<col width="5%">
				<col width="5%">
				<col width="5%">
				<col width="5%">
				<col width="5%">
				<col width="5%">
				<col width="5%">
				<col width="5%">
				<col width="10%">
				<col width="6%">
			</colgroup>
			<tbody>
				<tr>
					<td colspan="2" rowspan="2">安排类型</td>
					<td colspan="7">预计服务项目</td>
					<td colspan="7">实际服务项目</td>
					<td rowspan="2">酒店</td>
					<td rowspan="2">制作人</td>
				</tr>
				<tr>
					<td>早餐</td>
					<td>午餐</td>
					<td>晚餐</td>
					<td>住宿</td>
					<td>交通</td>
					<td>饮料</td>
					<td>夜宵</td>
					<td>早餐</td>
					<td>午餐</td>
					<td>晚餐</td>
					<td>住宿</td>
					<td>交通</td>
					<td>饮料</td>
					<td>夜宵</td>
				</tr>
				<tr id="lk_row1">
					<input type="hidden" name="id" value="${delay.details[0].id }"/>
					<td rowspan="2">旅客</td>
					<td>当日</td>
					<td><input class="layui-input" name="eBreakfast" value="${delay.details[0].eBreakfast }"/></td>
					<td><input class="layui-input" name="eLunch" value="${delay.details[0].eLunch }"/></td>
					<td><input class="layui-input" name="eDinner" value="${delay.details[0].eDinner }"/></td>
					<td><input class="layui-input" name="eAccommodation" value="${delay.details[0].eAccommodation }"/></td>
					<td><input class="layui-input" name="eTraffic" value="${delay.details[0].eTraffic }"/></td>
					<td><input class="layui-input" name="eDrinks" value="${delay.details[0].eDrinks }"/></td>
					<td><input class="layui-input" name="eNightSnack" value="${delay.details[0].eNightSnack }"/></td>
					<td><input class="layui-input" name="aBreakfast" value="${delay.details[0].aBreakfast }"/></td>
					<td><input class="layui-input" name="aLunch" value="${delay.details[0].aLunch }"/></td>
					<td><input class="layui-input" name="aDinner" value="${delay.details[0].aDinner }"/></td>
					<td><input class="layui-input" name="aAccommodation" value="${delay.details[0].aAccommodation }"/></td>
					<td><input class="layui-input" name="aTraffic" value="${delay.details[0].aTraffic }"/></td>
					<td><input class="layui-input" name="aDrinks" value="${delay.details[0].aDrinks }"/></td>
					<td><input class="layui-input" name="aNightSnack" value="${delay.details[0].aNightSnack }"/></td>
					<td rowspan="2"><textarea class="layui-textarea" id="lkHotel" name="lkHotel" >${delay.lkHotel}</textarea></td>
					<td rowspan="5">${operatorName }</td>
				</tr>
				<tr id="lk_row2">
					<input type="hidden" name="id" value="${delay.details[1].id }"/>
					<td>次日</td>
					<td><input class="layui-input" name="eBreakfast" value="${delay.details[1].eBreakfast }"/></td>
					<td><input class="layui-input" name="eLunch" value="${delay.details[1].eLunch }"/></td>
					<td><input class="layui-input" name="eDinner" value="${delay.details[1].eDinner }"/></td>
					<td><input class="layui-input" name="eAccommodation" value="${delay.details[1].eAccommodation }"/></td>
					<td><input class="layui-input" name="eTraffic" value="${delay.details[1].eTraffic }"/></td>
					<td><input class="layui-input" name="eDrinks" value="${delay.details[1].eDrinks }"/></td>
					<td><input class="layui-input" name="eNightSnack" value="${delay.details[1].eNightSnack }"/></td>
					<td><input class="layui-input" name="aBreakfast" value="${delay.details[1].aBreakfast }"/></td>
					<td><input class="layui-input" name="aLunch" value="${delay.details[1].aLunch }"/></td>
					<td><input class="layui-input" name="aDinner" value="${delay.details[1].aDinner }"/></td>
					<td><input class="layui-input" name="aAccommodation" value="${delay.details[1].aAccommodation }"/></td>
					<td><input class="layui-input" name="aTraffic" value="${delay.details[1].aTraffic }"/></td>
					<td><input class="layui-input" name="aDrinks" value="${delay.details[1].aDrinks }"/></td>
					<td><input class="layui-input" name="aNightSnack" value="${delay.details[1].aNightSnack }"/></td>
				</tr>
				<tr id="jz_row1">
					<input type="hidden" name="id" value="${delay.details[2].id }"/>
					<td rowspan="2">机组</td>
					<td>当日</td>
					<td><input class="layui-input" name="eBreakfast" value="${delay.details[2].eBreakfast }"/></td>
					<td><input class="layui-input" name="eLunch" value="${delay.details[2].eLunch }"/></td>
					<td><input class="layui-input" name="eDinner" value="${delay.details[2].eDinner }"/></td>
					<td><input class="layui-input" name="eAccommodation" value="${delay.details[2].eAccommodation }"/></td>
					<td><input class="layui-input" name="eTraffic" value="${delay.details[2].eTraffic }"/></td>
					<td><input class="layui-input" name="eDrinks" value="${delay.details[2].eDrinks }"/></td>
					<td><input class="layui-input" name="eNightSnack" value="${delay.details[2].eNightSnack }"/></td>
					<td><input class="layui-input" name="aBreakfast" value="${delay.details[2].aBreakfast }"/></td>
					<td><input class="layui-input" name="aLunch" value="${delay.details[2].aLunch }"/></td>
					<td><input class="layui-input" name="aDinner" value="${delay.details[2].aDinner }"/></td>
					<td><input class="layui-input" name="aAccommodation" value="${delay.details[2].aAccommodation }"/></td>
					<td><input class="layui-input" name="aTraffic" value="${delay.details[2].aTraffic }"/></td>
					<td><input class="layui-input" name="aDrinks" value="${delay.details[2].aDrinks }"/></td>
					<td><input class="layui-input" name="aNightSnack" value="${delay.details[2].aNightSnack }"/></td>
					<td rowspan="2"><textarea class="layui-textarea" id="jzHotel" name="jzHotel" >${delay.jzHotel}</textarea></td>
				</tr>
				<tr id="jz_row2">
					<input type="hidden" name="id" value="${delay.details[3].id }"/>
					<td>次日</td>
					<td><input class="layui-input" name="eBreakfast" value="${delay.details[3].eBreakfast }"/></td>
					<td><input class="layui-input" name="eLunch" value="${delay.details[3].eLunch }"/></td>
					<td><input class="layui-input" name="eDinner" value="${delay.details[3].eDinner }"/></td>
					<td><input class="layui-input" name="eAccommodation" value="${delay.details[3].eAccommodation }"/></td>
					<td><input class="layui-input" name="eTraffic" value="${delay.details[3].eTraffic }"/></td>
					<td><input class="layui-input" name="eDrinks" value="${delay.details[3].eDrinks }"/></td>
					<td><input class="layui-input" name="eNightSnack" value="${delay.details[3].eNightSnack }"/></td>
					<td><input class="layui-input" name="aBreakfast" value="${delay.details[3].aBreakfast }"/></td>
					<td><input class="layui-input" name="aLunch" value="${delay.details[3].aLunch }"/></td>
					<td><input class="layui-input" name="aDinner" value="${delay.details[3].aDinner }"/></td>
					<td><input class="layui-input" name="aAccommodation" value="${delay.details[3].aAccommodation }"/></td>
					<td><input class="layui-input" name="aTraffic" value="${delay.details[3].aTraffic }"/></td>
					<td><input class="layui-input" name="aDrinks" value="${delay.details[3].aDrinks }"/></td>
					<td><input class="layui-input" name="aNightSnack" value="${delay.details[3].aNightSnack }"/></td>
				</tr>
				<tr>
					<td colspan="2">备注</td>
					<td colspan="15"><input id="notes" class="layui-input" name="notes" value="${delay.notes}"/></td>
				</tr>
			</tbody>
		</table>
	</form>
	<script type="text/javascript"
		src="${ctxStatic}/prss/produce/addDelayInfo.js"></script>
</body>
</html>