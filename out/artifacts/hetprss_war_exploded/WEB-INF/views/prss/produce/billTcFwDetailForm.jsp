<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
		<title>特车计费详情，新增、修改页</title>
		<meta name="decorator" content="default" />
		<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
		<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
		<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript" src="${ctxStatic}/prss/produce/billTcFwDetailForm.js"></script>
		<script type="text/javascript">
			var operateType = "${operateType}";
		</script>
	</head>
	<body>
		<div id="container">
			<div id="baseTables">
				<form id="tableForm" action="${ctx}/produce/billTcFw/save" method="post">
					<!-- 保存的属性值 -->
					<div style="display: none">
						<input type="text" id="id" name="id" value="${modifyData.id}" style="background-color: black">
						<input type="text" id="inFltId" name="inFltId" value="${modifyData.inFltId}" style="background-color: black">
						<input type="text" id="outFltId" name="outFltId" value="${modifyData.outFltId}" style="background-color: black">
						<input type="text" id="inFlightDate" name="inFlightDate" value="${modifyData.inFlightDate}" style="background-color: black">
						<input type="text" id="outFlightDate" name="outFlightDate" value="${modifyData.outFlightDate}" style="background-color: black">
						<input type="text" id="operateType" name="operateType" value="${operateType}" style="background-color: black">
						<input type="text" id="serviceNumber" name="serviceNumber" value="${modifyData.serviceNumber}" style="background-color: black">
						<input type="text" id="serviceCode" name="serviceCode" value="HET-JL•DF-1055" style="background-color: black">
						
						<!-- 更新操作时不可编辑字段值 -->
						<input type="text" id="flightDateUnedit" name="flightDateUnedit" value="${modifyData.flightDate}" style="background-color: black">
						<input type="text" id="inFlightNumberUnedit" name="inFlightNumberUnedit" value="${modifyData.inFlightNumber}" style="background-color: black">
						<input type="text" id="outFlightNumberUnedit" name="outFlightNumberUnedit" value="${modifyData.outFlightNumber}" style="background-color: black">
					</div>
					
					<!-- 计费详情表格 -->
					<table id="baseTable" class="layui-table" style="width:950px;text-align:center;margin:20px auto;">
						<thead>
							<tr>
								<th colspan="7">特车计费详情单</th>
							</tr>
						</thead>
						<tbody>
							<!-- 航班信息 -->
							<tr>
								<td style="width: 130px">航空公司：<br><span id="airlineDescription">${modifyData.airlineDescription}</span></td>
								<td style="width: 100px">航班日期：<br>
									<input style="width: 100px" type='text' maxlength="20" name="flightDate" id="flightDate" value="${modifyData.flightDate}" 
									placeholder='请选择日期' class='form-control'
									onclick="WdatePicker({dateFmt:'yyyyMMdd'});" />
								</td>
								<td style="text-align:left;">服务时间：<br>
									<input style="width: 120px" type='text' maxlength="20" name="serviceStart" id="serviceStart" value="${modifyData.serviceStart}" 
									placeholder='服务开始时间' class='form-control'
									onclick="WdatePicker({dateFmt:'HH:mm'});" />
									<input style="width: 120px" type='text' maxlength="20" name="serviceEnd" id="serviceEnd" value="${modifyData.serviceEnd}" 
									placeholder='服务结束时间' class='form-control'
									onclick="WdatePicker({dateFmt:'HH:mm'});" /></td>
								<td colspan="2">服务单号：<span id="serviceNumberShow">${modifyData.serviceNumber}</span></td>
								<td colspan="2"><span id="serviceCodeShow">HET-JL•DF-1055</span></td>
							</tr>
							<tr>
								<td>进港航班号：<br><input type='text' id="inFlightNumber" name="inFlightNumber" class='form-control' value="${modifyData.inFlightNumber}"/></td>
								<td>出港航班号：<br><input type='text' id="outFlightNumber" name="outFlightNumber" class='form-control' value="${modifyData.outFlightNumber}"/></td>
								<td>飞机号：<span id="aircraftNumber">${modifyData.aircraftNumber}</span></td>
								<td colspan="2">机型：<span id="acttypeCode">${modifyData.acttypeCode}</span></td>
								<td colspan="2">站平值班人员：<span id="operatorName">${modifyData.operatorName}</span></td>
							</tr>
							<!-- 特车计费信息 -->
							<tr>
								<td colspan="2">旅客摆渡车</td>
								<td colspan="2">机组摆渡车</td>
								<td>牵引车</td>
								<td>残疾人登机车</td>
								<td>传送带</td>
							</tr>
							<tr>
								<td>进港</td>
								<td>出港</td>
								<td>进港</td>
								<td>出港</td>
								<td>
									<div style="float:left;">
										<input style="float:left;width:65%" type='text' id="qianyin" name="qianyin" class='form-control' value="${modifyData.qianyin}"/>
										<span>次</span>
									</div>
								</td>
								<td>
									<div style="float:left;">
										<input type='text' style="float:left;width:65%" id="cansheng" name="cansheng" class='form-control' value="${modifyData.cansheng}"/>
										<span>次</span>
									</div>
								</td>
								<td>
									<div style="float:left;">
										<input type='text' style="float:left;width:65%" id="chuansong" name="chuansong" class='form-control' value="${modifyData.chuansong}"/>
										<span>次</span>
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<div style="float:left;">
										<input type='text' style="float:left;width:65%" id="lkbdcIn" name="lkbdcIn" class='form-control' value="${modifyData.lkbdcIn}"/>
										<span>次</span>
									</div>
								</td>
								<td>
									<div style="float:left;">
										<input type='text' style="float:left;width:65%" id="lkbdcOut" name="lkbdcOut" class='form-control' value="${modifyData.lkbdcOut}"/>
										<span>次</span>
									</div>
								</td>
								<td>
									<div style="float:left;">
										<input type='text' style="float:left;width:65%" id="jzbdcIn" name="jzbdcIn" class='form-control' value="${modifyData.jzbdcIn}"/>
										<span>次</span>
									</div>
								</td>
								<td>
									<div style="float:left;">
										<input type='text' style="float:left;width:65%" id="jzbdcOut" name="jzbdcOut" class='form-control' value="${modifyData.jzbdcOut}"/>
										<span>次</span>
									</div>
								</td>
								<td>垃圾车</td>
								<td>清水车</td>
								<td>污水车</td>
							</tr>
							<tr>
								<td colspan="2">客梯车</td>
								<td colspan="2">升降平台车</td>
								<td>
									<div style="float:left;">
										<input type='text' style="float:left;width:65%" id="laji" name="laji" class='form-control' value="${modifyData.laji}"/>
										<span>次</span>
									</div>
								</td>
								<td>
									<div style="float:left;">
										<input type='text' style="float:left;width:65%" id="qingshui" name="qingshui" class='form-control' value="${modifyData.qingshui}"/>
										<span>次</span>
									</div>
								</td>
								<td>
									<div style="float:left;">
										<input type='text' style="float:left;width:65%" id="wushui" name="wushui" class='form-control' value="${modifyData.wushui}"/>
										<span>小时</span>
									</div>
								</td>
							</tr>
							<tr>
								<td>进港</td>
								<td>出港</td>
								<td>进港</td>
								<td>出港</td>
								<td>气源车</td>
								<td>电源车</td>
								<td>空调车</td>
							</tr>
							<tr>
								<td>
									<div style="float:left;">
										<input type='text' style="float:left;width:65%" id="ktcIn" name="ktcIn" class='form-control' value="${modifyData.ktcIn}"/>
										<span>小时</span>
									</div>
								</td>
								<td>
									<div style="float:left;">
										<input type='text' style="float:left;width:65%" id="ktcOut" name="ktcOut" class='form-control' value="${modifyData.ktcOut}"/>
										<span>小时</span>
									</div>
								</td>
								<td>
									<div style="float:left;">
										<input type='text' style="float:left;width:65%" id="ptcIn" name="ptcIn" class='form-control' value="${modifyData.ptcIn}"/>
										<span>小时</span>
									</div>
								</td>
								<td>
									<div style="float:left;">
										<input type='text' style="float:left;width:65%" id="ptcOut" name="ptcOut" class='form-control' value="${modifyData.ptcOut}"/>
										<span>小时</span>
									</div>
								</td>
								<td>
									<div style="float:left;">
										<input type='text' style="float:left;width:65%" id="qiyuan" name="qiyuan" class='form-control' value="${modifyData.qiyuan}"/>
										<span>小时</span>
									</div>
								</td>
								<td>
									<div style="float:left;">
										<input type='text' style="float:left;width:65%" id="dianyuan" name="dianyuan" class='form-control' value="${modifyData.dianyuan}"/>
										<span>小时</span>
									</div>
								</td>
								<td>
									<div style="float:left;">
										<input type='text' style="float:left;width:65%" id="kongtiao" name="kongtiao" class='form-control' value="${modifyData.kongtiao}"/>
										<span>小时</span>
									</div>
								</td>
							</tr>
							<tr>
								<td rowspan="2">机组签字</td>
								<td colspan="2" rowspan="2">
									<c:if test="${operateType=='add'}">-</c:if>
									<c:if test="${operateType=='modify'}"><img height="70px" width="100%!important;" src="data:image/png;base64,${modifyData.picture}" /></c:if>
								</td>
								<td rowspan="2">备注</td>
								<td colspan="2" rowspan="2">
									<textarea id="remark" name="remark" class='form-control'>${modifyData.remark}</textarea>
								</td>
								<td>除冰车</td>
							</tr>
							<tr>
								<td>
									<div style="float:left;">
										<input type='text' style="float:left;width:65%" id="chubing" name="chubing" class='form-control' value="${modifyData.chubing}"/>
										<span>小时</span>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>
			<!-- 导出excel -->
			<form id="exportForm" method="post"></form>
		</div>
	</body>
</html>