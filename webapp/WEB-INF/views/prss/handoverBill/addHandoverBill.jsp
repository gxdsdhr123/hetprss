<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title></title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
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
table tr td {
	text-align: center;
}
html,body{
	width:100% !important;
	height:100% !important;
	overflow:auto;
}
/* .layui-form select { */
/*     height: 100%; */
/* } */
/* .selected { */
/*     position: absolute; */
/*     height: 176px; */
/*     z-index: 9; */
/* } */
.select2-results__options {
	height: 105px;
}
.select2-container .select2-selection--single {
    height: 34px; 
}

 .select2-container--default .select2-selection--multiple .select2-selection__choice { 
	color: #222222; 
 } 
 .select2-container--default .select2-selection--multiple { 
 	background-color: #002F63!important;; 
 	border: 1px solid #D2D2D2 ; 
 	border-radius: 4px; 
 	cursor: text; 
 } 
.select2-container--disabled .select2-selection--multiple {
    background-color: none;
    cursor: default;
}
.layui-form-select { 
 	display: none; 
} 
.editableform .form-control {
    width: 100%;
}
.td-d {
	width: 13%;
}
.editable-input .select2 {
    min-width: 150px!important;
}
.select2-container--default.select2-container--disabled .select2-selection--single {
    background-color: #002F63;;
}
select.input-sm {
    line-height: inherit;
}
#container {
	position: relative;
	margin: 0px auto;
	padding: 0px;
	width: 100%;
	height: 100%;
	overflow-y: auto;
}
</style>
<body>
	<div class="mark_c" style="display: block;">loading...</div>
<div id="container">
	<form id="handoverBill" class="layui-form" action="" method="post">
		<input type="hidden" name="sign" id="sign" value="${sign }"/>
		
		<input type="hidden" name="fltid" id="FLTID" class="content-c" value="${FLTID }"/>
		
		<input type="hidden" name="operator" value="${OPERATOR }"/>
		<input type="hidden" name="operatorName" value="${OPERATORNAME }"/>
		<input type="hidden" id="paramModify" value="${paramModify }"/>
		<input type="hidden" id="id" value="${ID }" name="id"/>
		<input type="hidden" id="data" value="" name="data"/>
		
		<div  style="position: relative;">
			<table class="layui-table" >
				<tr>
					<td><font color="red">*</font>航班日期</td>
					<c:if test="${sign == \"in\" }">
						<td><font color="red">*</font>进港航班号</td>				
					</c:if>
					<c:if test="${sign == \"out\" }">
						<td><font color="red">*</font>航班号</td>								
					</c:if>
					<td >机号</td>
					<c:if test="${sign == \"in\" }">
						<td >机型</td>					
					</c:if>
					<c:if test="${sign == \"out\" }">
						<td >目的站</td>										
					</c:if>
					<td >进港预落时间</td>
					<c:if test="${sign == \"out\" }">
						<td >预起时间</td>					
					</c:if>
					<td >停机位</td>
					<td class="td-d"><font color="red">*</font>查理</td>
					<td style="width: 12%;" ><font color="red">*</font>操作时间</td>
				</tr>
				<tr>
					<td>
						<input id="flightDate" class="layui-input " name="flightDate" readonly
						onfocus="WdatePicker({onpicking: changeSelectTime(), onpicked: changeSelectTime(), dateFmt:'yyyyMMdd',readOnly:true})" placeholder="请输入航班日期"
						value="${FLIGHTDATE }" />
					</td>
					<td>
						<input id="flightNumber" class="layui-input " value="${FLIGHTNUMBER }" name="flightNumber" maxlength="10"  
						placeholder="请输入<c:if test='${sign == \"in\" }'>进</c:if><c:if test='${sign == \"out\" }'>出</c:if>港航班号" />
					</td>
					<td>
						<input id="AIRCRAFTNUMBER" class="layui-input content-c" value="${AIRCRAFTNUMBER }" name="aircraftNumber" maxlength="10"  readonly/>
					</td>
					<c:if test="${sign == \"in\" }">
						<td>
							<input id="ACTTYPECODE" class="layui-input content-c" value="${ACTTYPECODE }" name="actType" maxlength="10"  readonly/>
						</td>
					</c:if>
					<c:if test="${sign == \"out\" }">
						<td>
							<input id="" class="layui-input content-c" value="" name="" maxlength="10"  readonly/>
						</td>									
					</c:if>
					
					<td>
						<input id="ETA" class="layui-input content-c" value="${ETA }" name="eta" maxlength="10"  readonly/>
					</td>
					<c:if test="${sign == \"out\" }">
						<td>
							<input id="ETD" class="layui-input content-c" value="${ETD }" name="etd" maxlength="10"  readonly/>
						</td>
					</c:if>
					<td>
						<input id="ACTSTANDCODE" class="layui-input content-c" value="${ACTSTANDCODE }" name="actstandCode" maxlength="10"  readonly/>
					</td>
					<td>
						<select name="" id="operList"  class="select2 form-control operList " data-type="operList" >
							<option value=""></option>
							<c:forEach items="${operList}" var="office">
								<option value="${office.ID}">${office.NAME}</option>
							</c:forEach>
						</select>
					</td>
					<td>
						<input id="createDate" class="layui-input " name="createDate"  value="${CREATEDATE }" readonly
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" placeholder="请输入操作时间"
						 />
<%-- 						<input id="createDate" class="layui-input " value="${CREATEDATE }" name="createDate" maxlength="10"  /> --%>
					</td>
				</tr>
			</table>
		</div>
		
		<!-- 表格区域 -->		
		<div id="createDetailTableDiv" style="width:100%;position: relative;" onselectstart="return false" style="-moz-user-select:none;">
			<table id="createDetailTable" style="table-layout: fixed;"></table>
		</div>
		<div style="display: none;">	
			<input type="reset" id="reset" />
		</div>
	</form>
</div>
<script>
	new PerfectScrollbar('#container');
</script>
		
<script type="text/javascript" src="${ctxStatic}/prss/handoverBill/addHandoverBill.js"></script>
</body>
</html>