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
			.mark_c {
				position: absolute;
				width: 100%;
				height: 100%;
				background: rgba(0, 0, 0, 0.01);
				z-index: 9;
			}
			.margin-r {
			    margin-right: 6%;
			}
			.layui-table {
			    width: 91%;
			}
			.td-w {
				width: 13%;
			}
			.layui-table td { 
			    text-align: center;
			}
			.td-27 {
				width: 27%;
			}
			.td-45 {
				width: 45%;
			}
		</style>
	</head>
	<body>
		<div class="mark_c" style="display: block;">loading...</div>
		<form id="abnormalForm" class="layui-form" action="" method="post">
			<div style="position: relative; width: 95%;">
				<div class="layui-form-item" style="display: flex;">
					<label class="layui-form-label">开始时间：</label>
					<div class="layui-input-inline" style="align-items: center;">
						<input type="text" id="feedBackDate" readonly name="startTime" placeholder="开始时间" class="layui-input" onfocus="WdatePicker({dateFmt:'yyyyMMdd',readOnly:true})"/>
					</div>
					<label class="layui-form-label">结束时间：</label>
					<div class="layui-input-inline" style="align-items: center;">
						<input type="text" id="feedBackDate" readonly name="endTime" placeholder="结束时间" class="layui-input" onfocus="WdatePicker({dateFmt:'yyyyMMdd',readOnly:true})"/>
					</div>
				</div>
				<div class="layui-form-item" style="display: flex;">
					<label class="layui-form-label">涉及岗位：</label>
					<div class="layui-input-inline" style="align-items: center;">
						<select name="sendDept" id="sendDept"  class="select2 form-control departType" data-type="departType">
							<option></option>
							<c:forEach items="${sendDeptList}" var="office">
								<option value="${office.ID}">${office.NAME}</option>
							</c:forEach>
						</select>
					</div>
					<label class="layui-form-label">反馈情况：</label>
					<div class="layui-input-inline" style="align-items: center;">
						<select name="feebBack" id="sendDept1" style="display: inherit" class="select2 form-control" data-type="departType">					
							<option value="">请选择反馈情况</option>
							<option value="dept-0">未调查</option>
							<option value="cdm-1">已完成</option>
							<option value="cdm-0">调查中</option>
							<option value="dept-1">已反馈</option>
						</select>
					</div>
				</div>
				<div class="layui-form-item" style="display: flex;">
					<label class="layui-form-label">航空公司：</label>
					<div class="layui-input-inline" style="align-items: center;">
						<select name="airFlightCompany" id="airlineCompany"  class="select2 form-control departType" data-type="departType" multiple="multiple">
							<option value=""></option>
							<c:forEach items="${airFlightList}" var="office">
								<option value="${office.AIRLINE_CODE}">${office.AIRLINE_CODE}</option>
							</c:forEach>
						</select>
					</div>
		
					<label class="layui-form-label">航空分组：</label>
					<div class="layui-input-inline" style="align-items: center;">
						<select name="airGroup" id="airGroup"  class="select2 form-control departType" data-type="departType" multiple="multiple">
							<option value=""></option>
							<c:forEach items="${airFlightInfoList}" var="office">
								<option value="${office.ID}">${office.NAME}</option>
							</c:forEach>
						</select>
					</div>
				</div>
			</div>
		</form>
		<script type="text/javascript" src="${ctxStatic}/prss/abnormalFlightManagement/seachAbnormalFlight.js"></script>
	</body>
</html>