<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
		<title>清洁服务详情页面</title>
		<meta name="decorator" content="default" />
		<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
		<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
		<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
		<style type="text/css">
			td{
				text-align:center;
			}
			.textTr td input{
				width:100px;
			}
		</style>
	</head>
	<body>
		<div id="container">
			<div style="text-align:center;font-size: 230%;margin:20px 0px;"><span>客舱清洁服务单</span></div>
			<div id="baseTables">
				<form id="tableForm"  action="${ctx}/produce/cleanServerBill/doSave" method="post">
					<input type="hidden" id="hidId" name="hidId" value="${detailMap.ID}">
					<input type="hidden" id="hidClean" value="${detailMap.CLEANERS}">
					<div class="layui-form-item">
						<div class="layui-inline pull-left">
							<label class="layui-form-label">航空公司</label>
							<div class="layui-input-inline">
								<select name="airline" id="airline" class="select2 form-control">
									<option value=""></option>
									<c:forEach items="${airlineList}" var="airline">
										<option value="${airline.ID}" <c:if test="${detailMap.ALN_3CODE == airline.ID}" >selected</c:if>>${airline.NAME}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="layui-inline  pull-right">
							<label class="layui-form-label">航班性质</label>
							<div class="layui-input-inline">
								<select name="fltStatus" id="fltStatus" class="select2 form-control">
									<option value=""></option>
									<option value="1" <c:if test="${detailMap.FLT_TYPE == '1'}" >selected</c:if>>航前</option>
									<option value="2" <c:if test="${detailMap.FLT_TYPE == '2'}" >selected</c:if>>过站</option>
									<option value="3" <c:if test="${detailMap.FLT_TYPE == '3'}" >selected</c:if>>驻场</option>
								</select>
							</div>
						</div>
					</div>
					<table class="layui-table" style="table-layout:fixed">
						<tbody>
							<tr>
								<td width=40>日期</td>
								<td width=40>航班号</td>
								<td width=40>飞机号</td>
								<td width=60>机型</td>
								<td width=60>保障时间</td>
								<td width=80>备注</td>
							</tr>
							<tr class="textTr">
								<td>
									<input type='text' maxlength="20" name='flightDate' id="flightDate"
										placeholder='请选择日期' class='form-control' value="${detailMap.FLIGHT_DATE}"
										onclick="WdatePicker({dateFmt:'yyyyMMdd'});" />
								</td>
								<td>
									<input type="text" id="flightNum" name="flightNum" value="${detailMap.FLIGHT_NUMBER}">
								</td>
								<td>
									<input type="text" id="AirNum" name="AirNum" value="${detailMap.AIRCRAFT_NUMBER}">
								</td>
								<td>
									<input type="text" id="atcType" name="atcType" value="${detailMap.ACTTYPE_CODE}" style="width:130px" >
								</td>
								<td>
									<input type='hidden' value="${detailMap.JOB_BEGIN_B}"  name='bTime' id="bTime">
									<input type='hidden' value="${detailMap.JOB_END_E}"  name='eTime' id="eTime">
									<input type='text' maxlength="20"
										placeholder='请选择' class='' style="width:60px" value="${detailMap.JOB_BEGIN}" 
										onclick="WdatePicker({onpicked:function(dp){bTime.value=dp.cal.getNewDateStr()},dateFmt:'yyyy-MM-dd HH:mm:ss'});" />
									--
									<input type='text' maxlength="20"
										placeholder='请选择' class='' style="width:60px" value="${detailMap.JOB_END}" 
										onclick="WdatePicker({onpicked:function(dp){eTime.value=dp.cal.getNewDateStr()},dateFmt:'yyyy-MM-dd HH:mm:ss'});" />
								</td>
								<td rowspan=2>
									<textarea style="width: 230px;height: 75px;background: #002f63;border: 1px solid #006dc0;"
									 id="remark" name="remark">${detailMap.REMARK}
									</textarea>
								</td>
							</tr>
							<tr>
								<td rowspan=2>携带工具</td>
								<td colspan=4>
									<div>
										<div style="margin:0px 15px;display:inline;">
											<input type="checkbox" name="scj" <c:if test="${detailMap.scj == '1'}" >checked</c:if>>手持机
										</div>
										<div style="margin:0px 15px;display:inline;">
											<input type="checkbox" name="ljd" <c:if test="${detailMap.ljd == '2'}" >checked</c:if>>垃圾袋
										</div>
										<div style="margin:0px 15px;display:inline;">
											<input type="checkbox" name="bj" <c:if test="${detailMap.bj == '3'}" >checked</c:if>>簸箕
										</div>
										<div style="margin:0px 15px;display:inline;">
											<input type="checkbox" name="sb" <c:if test="${detailMap.sb == '4'}" >checked</c:if>>扫把
										</div>
										<div style="margin:0px 15px;display:inline;">
											<input type="checkbox" name="xcq" <c:if test="${detailMap.xcq == '5'}" >checked</c:if>>吸尘器
										</div>
										<div style="margin:0px 15px;display:inline;">
											<input type="checkbox" name="mj" <c:if test="${detailMap.mj == '6'}" >checked</c:if>>毛巾
										</div>
									</div>
								</td>
							</tr>
							<tr>
								<td>其它工具</td>
								<td colspan=3>
									<input name="other" type="text" style="width:420px" value="${detailMap.OTHER_TOOLS}">
								</td>
								<td>乘务签字及评语</td>
							</tr>
							<tr>
								<td colspan=5 width=220>清洁员</td>
								<td rowspan=2>
									<img src="${ctx}/flightDynamic/readExpAtta?fileId=${detailMap.SIGN}" style="max-width:220px;width:220px;height:80px;"/>
								</td>
							</tr>
							<tr>
								<td colspan=5>
									<select name="cleaners" id="cleaners" class="select2 form-control" multiple="multiple">
										<option value=""></option>
										<c:forEach items="${peopleList}" var="people">
											<option value="${people.ID}">${people.NAME}</option>
										</c:forEach>
									</select>
								</td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>
		</div>
	<script type="text/javascript" src="${ctxStatic}/prss/produce/cleanServerForm.js"></script>
	</body>
</html>