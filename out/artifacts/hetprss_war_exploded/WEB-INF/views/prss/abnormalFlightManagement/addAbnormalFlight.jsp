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
			#container {
				position: relative;
				margin: 0px auto;
				padding: 0px;
				width: 100%;
				height: 100%;
				overflow-y: auto;
			}
			.inDiv{
				display:inline-block;
			}
		</style>
	</head>
	<body>
		<div id="container">
			<form id="abnormalForm" class="layui-form" action="${ctx}/abnormal/abnormalFlightManagement/printword" method="post">
				<!-- 航班id -->
				<input type="hidden" name="fltid" value="" id="FLTID" value="${FLTID }"/>
				<input type="hidden" id="aircraftNumber" name="aircraftNumber" />
				<input type="hidden" id="sign" value="${sign }" />
				<input type="hidden" class="flightDate" value="${FLIGHT_DATE}" />
				<input type="hidden" id="officeids" value="${OFFICEIDCHECKED }" />
				<input type="hidden" id="infoSourceChecked" value="${INFO_SOURCE }" />
				<input type="hidden" id="feedBackId" value="${FEEDBACKID }" />
				<!-- 搜索区域 -->
				<div style="text-align:center;margin-top:10px; ">
					<div class="layui-form-item inDiv">
						<label class="layui-form-label"><font color="red">*</font>航班日期：</label>
						<div class="layui-input-inline">
							<input id="flightDate" class="layui-input" name="flightDate" readonly
								onfocus="WdatePicker({dateFmt:'yyyyMMdd',readOnly:true})" placeholder="请输入航班日期"
								value="${FLIGHT_DATE}" />
						</div>
					</div>
					<div class="layui-form-item inDiv">
						<label class="layui-form-label"><font color="red">*</font>航班号：</label>
						<div class="layui-input-inline">
							<input id="flightNumber" class="layui-input" value="${FLIGHT_NUMBER}" name="flightNumber" maxlength="10" value="" placeholder="请输入航班号"/>
						</div>
					</div>
					<div class="layui-form-item inDiv" id='sendDeptContent'>
						<label class="layui-form-label"><font color="red">*</font>发送部门：</label>
						<div class="layui-input-inline">
							<select name="officeId" id="sendDept"  class="select2 form-control departType" data-type="departType" multiple="multiple">
								<option value=""></option>
								<c:forEach items="${sendDeptList}" var="office">
									<option value="${office.ID}">${office.NAME}</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>
				<!-- radio区域 -->
				<div style="text-align:center;position: relative; width: 95%;padding-left:6%">
					<div class="layui-form-item" style="margin:0px auto;">
						<label class="layui-form-label"><font color="red">*</font>信息来源：</label>
						<div class="layui-input-inline" style="width: 87%; display: flex; align-items: center;">
							<input name="infoSource" title="航空公司" type="radio" value="航空公司"  class="">
							<input name="infoSource" title="机场" type="radio" value="机场"  class="">
							<input name="infoSource" title="现场" type="radio" value="现场"  class="">
							<input name="infoSource" title="其他" type="radio" id="infoSource" value=""  class="">
							<input type="text" id="infoSourceInfo"  style="width: 19%; border-radius: 4px;" placeholder="请输入其他消息来源"  class='form-control select2' value="" maxlength="20">
							<input id="searchBut" style="width:100px;margin-left:100px;padding-left:0px" type="button" class="layui-input" value="确定"/>
						</div>
					</div>
				</div>
				<!-- 表格区域 -->
				<div style="text-align:center; position: relative;">
					<table class="layui-table" style="margin:10px auto;">
						<tr>
							<td class="td-w">进港航班号</td>
							<td><span><input type="text" name="inFlightNumber" id="IN_FLIGHT_NUMBER"  value="${IN_FLIGHT_NUMBER}" disabled="disabled" class="layui-input content-c" /></span></td>
							<td class="td-w">机号</td>
							<td><span><input type="text" name="aircraftNumber" id="AIRCRAFT_NUMBER" value="${AIRCRAFT_NUMBER}" disabled="disabled" class="layui-input content-c" /></span></td>
							<td class="td-w">机型</td>
							<td><span><input type="text" name="acttypeCode" id="ACTTYPE_CODE" value="${ACTTYPE_CODE}" disabled="disabled" class="layui-input content-c" /></span></td>
						</tr>
						<tr>
							<td class="td-w">出港航班号</td>
							<td><span><input type="text" name="outFlightNumber" id="OUT_FLIGHT_NUMBER" value="${OUT_FLIGHT_NUMBER}" disabled="disabled" class="layui-input content-c" /></span></td>
							<td class="td-w">STA</td>
							<td><span><input type="text" name="sta" id="STA" value="${STA}" disabled="disabled" class="layui-input content-c" /></span></td>
							<td class="td-w">STD</td>
							<td><span><input type="text" name="std" id="STD" value="${STD}" disabled="disabled" class="layui-input content-c" /></span></td>
						</tr>
						<tr>
							<td class="td-w">机位</td>
							<td><span><input type="text" name="actstandCode" id="ACTSTAND_CODE" value="${ACTSTAND_CODE}" disabled="disabled" class="layui-input content-c" /></span></td>
							<td class="td-w">ATA</td>
							<td><span><input type="text" name="ata" id="ATA" value="${ATA}" disabled="disabled" class="layui-input content-c" /></span></td>
							<td class="td-w">ATD</td>
							<td><span><input type="text" name="atd" id="ATD" value="${ATD}" disabled="disabled" class="layui-input content-c" /></span></td>
						</tr>
						<tr>
							<td class="td-w">客关时间</td>
							<td><span><input type="text" name="htchCloTm" id="HTCH_CLO_TM" value="${CLO_TM}" disabled="disabled" class="layui-input content-c" /></span></td>
							<td class="td-w">货关时间</td>
							<td><span><input type="text" name="goodclose" id="GOODCLOSE" value="${GOODCLOSE}" disabled="disabled" class="layui-input content-c" /></span></td>
							<td class="td-w">首次TSAT</td>
							<td><span><input type="text" name="ftsatTm" id="FTSAT_TM" value="${FTSAT_TM}" disabled="disabled" class="layui-input content-c" /></span></td>
						</tr>
						<tr>
							<td class="td-w">起场</td>
							<td><span><input type="text" name="departApt4code" id="DEPART_APT4CODE" value="${DEPART_APT4CODE}" disabled="disabled" class="layui-input content-c" /></span></td>
							<td class="td-w">落场</td>
							<td><span><input type="text" name="arrivalApt4code" id="ARRIVAL_APT4CODE" value="${ARRIVAL_APT4CODE}" disabled="disabled" class="layui-input content-c" /></span></td>
							<td class="td-w">TSAT</td>
							<td><span><input type="text" name="tsat" id="TSAT" value="${TSAT}" disabled="disabled" class="layui-input content-c" /></span></td>
						</tr>
						<tr>
							<td class="td-w">延误原因/时间</td>
							<td colspan="5"><input type="text" name="dealyReason" id="DEALY_REASON" value="${DEALY_REASON}" disabled="disabled" class="layui-input content-c" /></td>
						</tr>
						<tr>
							<td class="td-w">备注</td>
							<td colspan="5">
							 <textarea name="remark" id="remark" style="border: 0;overflow: auto; width:100%; height: 100px; background-color: #002F63;" class="content-c">${REMARK }</textarea>
							</td>
						</tr>
						<tr>
							<td>运控值班员</td>
							<td colspan="2">
								<span>
									<input type="text" name="dutyName" value="${DUTYNAME }"  disabled="disabled" class="layui-input" />
								</span>
								<input type="hidden" name="operatror" value="${dutyId }" class="layui-input" />
							</td>
							<td>日期</td>
							<td colspan="2">
								<span>
									<input type="text" name="operDate" id="operDate" value='<fmt:formatDate value="${OPER_DATE }" pattern="yyyy-MM-dd HH:mm"/>' class="layui-input" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',readOnly:true})"/>
								</span>
							</td>
						</tr>
					</table>
				</div>
				<c:if test="${sign != 'add' }">
				<hr/>
				<!-- 查看下的部门反馈 -->
				<div style="text-align:center; position: relative;">
					<table class="layui-table" style="margin:0px auto;">
						<tr>
							<td colspan="6">保障部门反馈</td>
						</tr>
						<c:forEach items="${deptFeedBackInfo }" var="dept" varStatus="index">
							<input type="hidden" id="feedBackIdKey" value="${dept.ID }" />
							<tr>
								<td class="td-w" colspan="2"><input type="hidden" name="feedBackOfficeName" value="${dept.OFFICENAME}" />${dept.OFFICENAME}</td>
								<td colspan="4">
								<textarea id="feedBackContent" name="deptFeedBackContent" style="border: 0;overflow: auto; width:100%; height: 100px; background-color: #002F63;" class="content-c" >${dept.CONTENT }</textarea>
								</td>
							</tr>
							<tr>
								<td>值班员</td>
								<td colspan="2" class="td-27">
									<span>
										<input type="text" disabled name="feedBackOperName"  <c:if test="${dept.OPERATROR != null }">value='${dept.DEPTFEENNAME }'</c:if> <c:if test="${dept.OPERATROR == null }">value='${userName }'</c:if>  class="layui-input" />
									</span>
									<input type="hidden" id="feedBackOper" name="feedBackOper" <c:if test="${dept.OPERATROR != null }">value=${dept.OPERATROR }</c:if> <c:if test="${dept.OPERATROR == null }">value=${userId }</c:if> class="layui-input" />
								</td>
								<td>日期</td> 
								<td colspan="2" class="td-45">
									<span>
										<input type="text" id="feedBackDate" disabled name="operDate1" value='<fmt:formatDate value="${dept.OPER_DATE }" pattern="yyyy-MM-dd HH:mm"/>' class="layui-input" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',readOnly:true})"/>
									</span>
								</td>
							</tr>
						</c:forEach>
					</table>
				</div>
				</c:if>
				<c:if test="${sign != 'add' }">
				<hr/>
				<!-- CDM判责 -->
				<div style="text-align:center; position: relative;">
					<table class="layui-table" style="margin:0px auto;">
						<tr>
							<td colspan="6">CDM判责</td>
						</tr>
						<tr>
							<td class="td-w" colspan="6">
								<textarea id="cmdContent" name="cmdContent" style="border: 0;overflow: auto; width:100%; height: 100px; background-color: #002F63;" class="content-c" >${CDM_CONTENT }</textarea>
							</td>
						</tr>
						<tr>
							<td>值班经理</td>
							<td colspan="2" class="td-27">
								<span>
									<input type="text" name="cmdName" <c:if test="${CDMUSERID != null }">value='${CDMUSERNAME }'</c:if> <c:if test="${CDMUSERID == null }">value='${cmdUserName }'</c:if>  class="layui-input" />
								</span>
								<input type="hidden" name="operatror" id="cdmId" <c:if test="${CMDUSERID != null }">value=${CDMUSERID }</c:if> <c:if test="${dep.OPERATROR == null }">value=${cmdUserId }</c:if>  class="layui-input" />
							</td>
							<td>日期</td>
							<td colspan="2" class="td-45">
								<span>
									<input disabled type="text" name="operDate2" id="cdmDate" value='<fmt:formatDate value="${CDM_DATE }" pattern="yyyy-MM-dd HH:mm"/>' class="layui-input" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',readOnly:true})"/>
								</span>
							</td>
						</tr>
					</table>
				</div>
			</c:if>
			</form>
		</div>
		<script>
			new PerfectScrollbar('#container');
		</script>
		<script type="text/javascript" src="${ctxStatic}/prss/abnormalFlightManagement/addAbnormalFlight.js"></script>
	</body>
</html>