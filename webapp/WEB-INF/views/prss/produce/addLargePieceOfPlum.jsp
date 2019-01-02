<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
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
	<form id="largePieceOfPlum" class="layui-form" action="" method="post">
		<input type="hidden" name="fltid" id="FLTID" class="content-c" value="${FLTID }"/>
		<!-- 机型 -->
		<input type="hidden" name="actType" id="ACTTYPECODE" value="${ACTTYPECODE }"/>
		<input type="hidden" name="actstandCode" value="${ACTSTANDCODE }"/>
		<input type="hidden" name="operator" value="${OPERATOR }"/>
		<input type="hidden" id="paramModify" value="${paramModify }"/>
		<input type="hidden" id="id" value="${ID }" name="id"/>
		
		<div  style=" margin-left: 5%; position: relative; width: 95%;">
			<div class="layui-form-item" style="display: flex;">
				<label class="layui-form-label"><font color="red">*</font>日期：</label>
				<div class="layui-input-inline" style="margin-right: 5%; width: 15%;align-items: center;">
					<input id="flightDate" class="layui-input " name="flightDate" readonly
					onfocus="WdatePicker({onpicking: changeSelectTime(), onpicked: changeSelectTime(), dateFmt:'yyyyMMdd',readOnly:true})" placeholder="请输入航班日期"
					value="${FLIGHTDATE }" />
				</div>
				<label class="layui-form-label"><font color="red">*</font>出港航班号：</label>
				<div class="layui-input-inline" style="margin-right: 3%; width: 15%;align-items: center;">
					<input id="flightNumber" class="layui-input " value="${FLIGHTNUMBER }" name="flightNumber" maxlength="10"  placeholder="请输入出港航班号"/>
				</div>
				<label class="layui-form-label">机号：</label>
				<div class="layui-input-inline" style="width: 15%;align-items: center;">
					<input id="AIRCRAFTNUMBER" class="layui-input content-c " value="${AIRCRAFTNUMBER }" name="aircraftNumber" maxlength="10"  readonly/>
				</div>
			</div>
			<div class="layui-form-item" style="display: flex;">
				<label class="layui-form-label">机位：</label>
				<div class="layui-input-inline" style="margin-right: 5%; width: 15%;align-items: center;">
					<input id="ACTSTANDCODE" class="layui-input content-c " value="${ACTSTANDCODE }" name="" maxlength="10" value="" readonly/>
				</div>
				<label class="layui-form-label">进港预落：</label>
				<div class="layui-input-inline" style="margin-right: 3%; width: 15%;align-items: center;">
					<input id="ETA" class="layui-input content-c " value="${ETA }" name="eta" maxlength="10" value="" readonly/>
				</div>
				<label class="layui-form-label">出港预落：</label>
				<div class="layui-input-inline" style="width: 15%;align-items: center;">
					<input id="ETD" class="layui-input content-c " value="${ETD }" name="etd" maxlength="10" value="" readonly/>
				</div>
			</div>
		</div>
		<div style=" margin-left: 5%;padding-left:15px" id="toolbar">
			<button id="addRow" type="button" class="btn btn-link">增加一行</button>
			<button id="delRow" type="button" class="btn btn-link">删除一行</button>
		</div>
		
		
		<!-- 表格区域 -->		
		<div  style="margin-left: 5%; position: relative;">
			<table class="layui-table" >
				<tr>
					<td style="width: 8%;">序号</td>
					<td>行李号</td>
					<td>目的站</td>
				</tr>
				<tbody id="tbody">
					<c:forEach items="${goodsList }" var="goods" varStatus="g">
						<tr class="cloneHtml">
							<td>
								<input class="layui-input" value="${g.count}" name="seq" maxlength="10"  readonly/>
							</td>
							<td>
								<input class="layui-input content-c bagNumber" value="${goods.BAGNUMBER }" name="bagNumber"  maxlength="50" placeholder="请输入行李号"/>
							</td>
							<td>
								<input class="layui-input content-c dest" value="${goods.DEST }" name="dest" maxlength="50" placeholder="请输入目的站"/>
							</td>
						</tr>
					</c:forEach>
					<c:if test="${goodsList == null }">
						<tr class="cloneHtml" >
							<td>
								<input class="layui-input" value="1" name="seq" maxlength="10"  readonly/>
							</td>
							<td>
								<input class="layui-input content-c bagNumber" value="${BAGNUMBER }" name="bagNumber"  maxlength="50" placeholder="请输入行李号"/>
							</td>
							<td>
								<input class="layui-input content-c dest" value="${DEST }" name="dest" maxlength="50" placeholder="请输入目的站"/>
							</td>
						</tr>
					</c:if>
				</tbody>	
				<tr>
					<td colspan="2">
						<span><font color="red">*</font>操作人</span>
					</td>
					<td colspan="2">
						<input type='hidden' name="charlineId" id="charlineId" value="${OPERATOR}" />
						<input type='text' name="operatorName" id="operatorName" value="${OPERATOR_NAME}"
								class='form-control'  readonly="readonly" />
<%-- 						<input type="hidden" name="operatorName" value="${OPERATORNAME }"/> --%>
<!-- 						<select name="" id="charlineId" class="select2 form-control" readonly> -->
<!-- 							<option value=""></option> -->
<%-- 							<c:forEach items="${charlieList}" var="office"> --%>
<%-- 								<option <c:if test="${OPERATOR==office.ID}">selected</c:if> value="${office.ID}">${office.NAME}</option> --%>
<%-- 							</c:forEach> --%>
<!-- 						</select> -->
					</td>
				</tr>
			</table>
		</div>
		<c:if test="${paramModify != null}">		
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">签字人</label>
					<div id="SIGNATORYdiv" class="layui-input-inline">
						<img src="${ctx }/largePieceOfPlum/luggage/outputPicture?id=${SIGNATORY }" height=45 width=80/>
					</div>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">留存图片</label>
					<div id="SIGNIMGdiv" >
						<input id="photoCode" style="display: none" type="text" value="${PHOTO_CODE }"  ></input>
					</div>
			</div>
		</c:if>
		
		
		<div style="display: none;">	
			<input type="reset" id="reset" />
		</div>
	</form>
</div>
<script>
	new PerfectScrollbar('#container');
</script>
		
<script type="text/javascript" src="${ctxStatic}/prss/produce/addLargePieceOfPlum.js"></script>
</body>
</html>