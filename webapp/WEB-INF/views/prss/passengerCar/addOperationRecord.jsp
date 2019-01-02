<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title></title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<style type="text/css">
.mark_c {
    position: absolute;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.01);
    z-index: 9;
}

html,body{
	width:100% !important;
	height:100% !important;
	overflow:auto;
}
.select2-results__options {
	height: 105px;
}
.select2-container .select2-selection--single {
    height: 34px; 
}
.select2-container--default.select2-container--disabled .select2-selection--single {
    background-color: initial;
}

.layui-form-select { 
 	display: none; 
} 
.editable-input .select2 {
    min-width: 150px!important;
}
#container {
	position: relative;
	margin: 0px auto;
	padding: 0px;
	width: 100%;
	height: 100%;
	overflow-y: auto;
}
.editableform .form-control {
    width: 150px;
}
.layui-table thead tr{
	background-color:#0066b6;
	pointer-events: none;
}
.layui-table thead tr td{
	border:none;
	text-align:center;
}
i{
	cursor:pointer;
}
</style>
<body>
	<div class="mark_c" style="display: block;">loading...</div>
	<div id="container" style="padding-top:10px;">
	<form id="passengerCar" class="" action="" method="post">
		<input type="hidden" name="fltid" id="FLTID" class="content-c" value="${FLTID}"/>
		<%-- <input type="hidden" id="operator" value="${OPERATOR }"/>
		<input type="hidden" id="operatorName" name="operatorName" value="${OPERATORNAME }"/>
		<input type="hidden" id="paramModify" value="${paramModify }"/>
		<input type="hidden" id="id" value="${ID }" name="id"/>
		<input type="hidden" id="data" value="" name="data"/> --%>
		
		<div  style="position: relative;">
			<div class="layui-form-item" style="display: flex;">
				<label class="layui-form-label"><font color="red">*</font>日期：</label>
				<div class="layui-input-inline" style="width: 15%;align-items: center;">
					<input id="flightDate" class="layui-input modify-display" name="flightDate" readonly style="cursor: pointer;"
					onfocus="WdatePicker({onpicking: changeSelectTime(), onpicked: changeSelectTime(), dateFmt:'yyyyMMdd',readOnly:true})" placeholder="请输入航班日期"
					value="${FLIGHTDATE}" disabled="disabled"/>
				</div>
				<label class="layui-form-label"><font color="red">*</font>岗位：</label>
				<div class="layui-input-inline" style="width: 15%;align-items: center;">
					<input id="post" class="layui-input content-c " value="${POST}" name="post" maxlength="50" placeholder="请输入岗位" disabled="disabled"/>
				</div>
				<%-- <label class="layui-form-label"><font color="red">*</font>操作员：</label>
				<div class="layui-input-inline" style="width: 15%;align-items: center;">
					<select name="operator" id="operList"  class="select2 form-control operList " data-type="operList" readonly>
						<option value=""></option>
						<c:forEach items="${operList}" var="office">
							<option value="${office.ID}">${office.NAME}</option>
						</c:forEach>
					</select>
				</div> --%>
				<label class="layui-form-label"><font color="red">*</font>当班总值：</label>
				<div class="layui-input-inline" style="width: 15%;align-items: center;">
					<input id="scheduler" class="layui-input content-c " value="${SCHEDULER }" name="scheduler" maxlength="50" placeholder="请输入当班调度" disabled="disabled"/>
				</div>
				<label class="layui-form-label"><font color="red">*</font>当班调度：</label>
				<div class="layui-input-inline" style="width: 15%;align-items: center;">
					<input id="scheduler" class="layui-input content-c " value="${SCHEDULER }" name="scheduler" maxlength="50" placeholder="请输入当班调度" disabled="disabled"/>
				</div>
				<label class="layui-form-label">注册号：</label>
				<div class="layui-input-inline" style="width: 15%;align-items: center;">
					<input id="AIRCRAFTNUMBER" class="layui-input content-c modify-display" value="${AIRCRAFTNUMBER }" name="aircraftNumber" disabled="disabled"/>
				</div>
			</div>
			<div class="layui-form-item" style="display: flex;">
				<label class="layui-form-label"><font color="red">*</font>航班号：</label>
				<div class="layui-input-inline" style="width: 15%;align-items: center;">
					<input id="flightNumber" class="layui-input modify-display" value="${FLIGHTNUMBER }" name="flightNumber" maxlength="10" disabled="disabled" placeholder="请输入出港航班号"/>
				</div>
				<label class="layui-form-label">ETA：</label>
				<div class="layui-input-inline" style="width: 15%;align-items: center;">
					<input id="ETA" class="layui-input content-c modify-display" value="${ETA }" name="eta" disabled="disabled"/>
				</div>
				<label class="layui-form-label">ETD：</label>
				<div class="layui-input-inline" style="width: 15%;align-items: center;">
					<input id="ETD" class="layui-input content-c modify-display" value="${ETD }" name="etd" disabled="disabled"/>
				</div>
				<label class="layui-form-label">机位：</label>
				<div class="layui-input-inline" style="width: 15%;align-items: center;">
					<input id="ACTSTANDCODE" class="layui-input content-c modify-display" value="${ACTSTANDCODE }" name="actstandCode" disabled="disabled"/>
				</div>
				<label class="layui-form-label">机型：</label>
				<div class="layui-input-inline" style="width: 15%;align-items: center;">
					<input id="ACTTYPECODE" class="layui-input content-c modify-display" value="${ACTTYPECODE }" name="actType" disabled="disabled"/>
				</div>
			</div>
		</div>
	
		<div style="padding-left:15px" id="toolbar">
			<button id="addRow" type="button" class="btn btn-link">增加一行</button>
			<button id="delRow" type="button" class="btn btn-link">删除一行</button>
		</div>
			
		<!-- 表格区域 -->		
		<%-- <div id="createDetailTableDiv" style="width:100%;position: relative;" onselectstart="return false" style="-moz-user-select:none;">
			<table id="createDetailTable" style="table-layout: fixed;"></table>
		</div>
		<c:if test="${paramModify == 'modify' }">
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">签字人</label>
					<div id="SIGNATORYdiv" class="layui-input-inline">
						<img src="${ctx }/passengerCar/operationRecord/outputPicture?id=${SIGNATORY }" height=45 width=80/>
					</div>
				</div>
			</div>
		</c:if> --%>
	
		<table id="baseTable" class="layui-table">
			<thead>
				<tr>
					<td rowspan=2>序号</td>
					<td rowspan=2>车号</td>
					<td colspan=3>对靠撤离时间</td>
					<td colspan=3 rowspan=2>检车情况</td>
					<td colspan=4>其他异常情况</td>
				</tr>
				<tr>
					<td>到位</td>
					<td>对靠</td>
					<td>撤离</td>
					<td>视频</td>
					<td>图片</td>
					<td>语音</td>
					<td>文字 </td>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="car" items="${carList}" varStatus="status">
					<tr class="firstTr">
						<td rowspan=3 width=7% >${status.index + 1}</td>
						<td rowspan=3 width=7%>
							<input type="text" class="deviceNo" name="deviceNo" value="${car.DEVICE_NO}"/>
							<input type="text" class="billId" name="billId" value="${car.BILLID}" style="display:none"/>
						</td>
						<td rowspan=3 width=7%>${car.TIME1}</td>
						<td rowspan=3 width=7%>${car.TIME2}</td>
						<td rowspan=3 width=7%>${car.TIME3}</td>
						<td width=7% height=40px><input type="checkbox" class="WG" <c:if test="${car.WG == 1}">checked</c:if>/>外观</td>
						<td width=7% height=40px><input type="checkbox" class="HB" <c:if test="${car.HB == 1}">checked</c:if>/>护板</td>
						<td width=8% height=40px><input type="checkbox" class="SJ" <c:if test="${car.SJ == 1}">checked</c:if>/>升降</td>
						<td rowspan=3 width=7%><i class="fa fa-youtube-play" onclick="doOpen(${car.REMARKID},'3')">&nbsp查看</i></td>
						<td rowspan=3 width=7%><i class="fa fa-photo" onclick="doOpen(${car.REMARKID},'1')">&nbsp查看</i></td>
						<td rowspan=3 width=7%><i class="fa fa-caret-square-o-right" onclick="doOpen(${car.REMARKID},'2')">&nbsp查看</i></td>
						<td rowspan=3 width=7%>
							<textarea class="remark" name="remark" style="width:90px;background-color:#002f63;height:120px;border:1px solid #006dc0">${car.REMARK}</textarea>
						</td>
					</tr>
					<tr>
						<td height=40px><input type="checkbox" class="DG" <c:if test="${car.DG == 1}">checked</c:if>/>灯光</td>
						<td height=40px><input type="checkbox" class="LT" <c:if test="${car.LT == 1}">checked</c:if>/>轮胎</td>
						<td height=40px><input type="checkbox" class="WS" <c:if test="${car.WS == 1}">checked</c:if>/>卫生</td>
					</tr>
					<tr>
						<td height=40px><input type="checkbox" class="ZD" <c:if test="${car.ZD == 1}">checked</c:if>/>制动</td>
						<td height=40px><input type="checkbox" class="ZJ" <c:if test="${car.ZJ == 1}">checked</c:if>/>支脚</td>
						<td height=40px><input type="checkbox" class="MHQ" <c:if test="${car.MHQ == 1}">checked</c:if>/>灭火器</td>
					</tr>
					<tr>
						<td colspan=4 height=40px></td>
						<td>操作员：</td>
						<td>
							<%-- <input type="text" class="operator" name="operator" value="${car.OPERATOR}" />
							<input type="text" class="operatorName" name="operatorName" value="${car.OPERATOR_NAME}" style="width:80px"/> --%>
							<select name="operator" class="select2 form-control operList " data-type="operList" >
								<option value=""></option>
								<c:forEach items="${operList}" var="office">
									<option value="${office.ID}" <c:if test="${office.ID == car.OPERATOR}">selected</c:if>>${office.NAME}</option>
								</c:forEach>
							</select>
						</td>
						<td>取车地：</td>
						<td><input type="text" class="QCD" name="QCD" value="${car.QCD}" style="width:80px"/></td>
						<td>送车地：</td>
						<td><input type="text" class="SCD" name="SCD" value="${car.SCD}" style="width:80px"/></td>
						<td>报送时间：</td>
						<td>${car.CREATE_DATE}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</form>
</div>
<script>
	new PerfectScrollbar('#container');
</script>
<script type="text/javascript" src="${ctxStatic}/prss/passengerCar/addOperationRecord.js"></script>
</body>
</html>