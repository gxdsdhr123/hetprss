<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>旅客登机人数交接单</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
	<div id="container">
		<div id="baseTables">
			<form id="tableForm"  class="layui-form" action="${ctx}/produce/passengerNum/save"
				method="post">
				<table id="baseTable" class="layui-table"
					style="width: 900px; text-align: left; margin: 10px auto;">
					<thead>
						<tr>
							<th colspan=8>旅客登机人数核对单</th>
						</tr>
					</thead>
					<tbody>

						<tr>
							<td>航班日期</td>
							<td><input type='text' maxlength="20" name='flightDate'
									   id="flightDate" placeholder='请选择日期' class='form-control'
									   onclick="WdatePicker({dateFmt:'yyyyMMdd'});"
									   value="${result.FLIGHT_DATE}" onblur="getFltInfo()" lay-verify="required"/></td>
							<td>航班号</td>
							<td>
								<input type='text' name="flightNumber"
									   id="flightNumber" class='form-control'
									   value="${result.FLIGHT_NUMBER}" onblur="getFltInfo()" lay-verify="required"/></td>
							<td>机位/桥位</td>
							<td><input type='text' name="actstandCode"
									   id="actstandCode" class='form-control'
									   value="${result.ACTSTAND_CODE}" readonly="readonly" /></td>
							<td>机号</td>
							<td><input type='text' name="aircraftNumber"
									   id="aircraftNumber" class='form-control'
									   value="${result.AIRCRAFT_NUMBER}" readonly="readonly" /></td>
						</tr>
						<tr>
							<td>航程</td>
							<td><input type='text' name="route"
									   id="route" class='form-control'
									   value="${result.ROUTE}" readonly="readonly" /></td>
							<td>延误时间</td>
							<td><input type='text' name="delayTM"
									   id="delayTM" class='form-control'
									   value="${result.DELAY_TM}" readonly="readonly" /></td>
							<td>STD</td>
							<td><input type='text' name="std"
									   id="std" class='form-control'
									   value="${result.STD}" readonly="readonly" /></td>
							<td>ETD</td>
							<td><input type='text' name="etd"
									   id="etd" class='form-control'
									   value="${result.ETD}" readonly="readonly" /></td>
						</tr>
						<tr>
							<td>登机口变更</td>
							<td><input type='text' name="gate"
									   id="gate" class='form-control'
									   value="${result.GATE}" readonly="readonly" /></td>
							<td>通知上客时间</td>
							<td><input type='text' name="skzlTM"
									   id="skzlTM" class='form-control'
									   value="${result.SKZL_TM}" readonly="readonly" /></td>
							<td>总人数</td>
							<td><input type='text' name="allPSGNum"
									   id="allPSGNum" class='form-control'
									   value="${result.ALL_PSG_NUM}" readonly="readonly" /></td>
							<td>过站人数</td>
							<td><input type='text' name="gzPSGNum"
									   id="gzPSGNum" class='form-control'
									   value="${result.GZ_PSG_NUM}" readonly="readonly" /></td>
						</tr>
					</tbody>
				</table>
				<table id="baseTable2" class="layui-table"
						   style="width: 900px; text-align: center; margin: 10px auto;">
				<tr>
					<td>加机组人数</td>
					<td>特殊旅客</td>
					<td>贵宾人数</td>
					<td>贵宾序号</td>
				</tr>
				<tr>
					<td><input type='text' name="jjzNum"
							   id="jjzNum" class='form-control'
							   value="${result.JJZ_NUM}"/></td>
					<td><input type='text' name="tsPSGNum"
							   id="tsPSGNum" class='form-control'
							   value="${result.TS_PSG_NUM}" /></td>
					<td><input type='text' name="gbPSGNum"
							   id="gbPSGNum" class='form-control'
							   value="${result.GB_PSG_NUM}" /></td>
					<td><input type='text' name="gbxh"
							   id="gbxh" class='form-control'
							   value="${result.GBXH}" /></td>
				</tr>
				<tr>
					<td>补行李件数</td>
					<td>旅客序号</td>
					<td>过站终止行程</td>
					<td>二次登机交接</td>
				</tr>
				<tr>
					<td><input type='text' name="bxljs"
							   id="bxljs" class='form-control'
							   value="${result.BXLJS}" /></td>
					<td><input type='text' name="psgxh"
							   id="psgxh" class='form-control'
							   value="${result.PSGXH}" /></td>
					<td><input type='text' name="gzzzxc"
							   id="gzzzxc" class='form-control'
							   value="${result.GZZZXC}" /></td>
					<td><input type='text' name="ecdjjj"
							   id="ecdjjj" class='form-control'
							   value="${result.ECDJJJ}" /></td>
				</tr>
				<tr>
					<td>补牌序号</td>
					<td>手机值机序号</td>
					<td>丢牌序号</td>
					<td>减客信息</td>
				</tr>
				<tr>
					<td><input type='text' name="bpxh"
							   id="bpxh" class='form-control'
							   value="${result.BPXH}" /></td>
					<td><input type='text' name="sjzjxh"
							   id="sjzjxh" class='form-control'
							   value="${result.SJZJXH}" /></td>
					<td><input type='text' name="dpxh"
							   id="dpxh" class='form-control'
							   value="${result.DPXH}" /></td>
					<td><input type='text' name="jkxx"
							   id="jkxx" class='form-control'
							   value="${result.JKXX}" /></td>
				</tr>
			</table>
			<table id="baseTable3" class="layui-table"
				   style="width: 900px; text-align: center; margin: 10px auto; margin-top: 0px;">
				<thead>
				<tr>
					<th colspan=5>交接物品（明细）</th>
				</tr>
				</thead>
				<tbody>
				<tr>
					<td colspan=2>描述</td>
					<td>视频</td>
					<td>图片</td>
					<td>语音</td>
				</tr>
				<tr>
					<td colspan=2>描述</td>
					<td>
						<c:choose>
							<c:when test="${empty result.VIDEO}">
								<button type="button" style="height:auto;line-height:1.5" class="btn btn-md btn-link" disabled="disabled">下载</button>
							</c:when>
							<c:otherwise>
								<button type="button" style="height:auto;line-height:1.5" value="${result.VIDEO},视频" class="btn btn-md btn-link" onclick="downFile(this)">下载</button>
							</c:otherwise>
						</c:choose>
					</td>
					<td>
						<c:choose>
							<c:when test="${empty result.PICTURE}">
								<button type="button" style="height:auto;line-height:1.5" class="btn btn-md btn-link" disabled="disabled">下载</button>
							</c:when>
							<c:otherwise>
								<button type="button" style="height:auto;line-height:1.5" value="${result.PICTURE},图片" class="btn btn-md btn-link" onclick="downFile(this)">下载</button>
							</c:otherwise>
						</c:choose>
					</td>
					<td>
						<c:choose>
							<c:when test="${empty result.SOUND}">
								<button type="button" style="height:auto;line-height:1.5" class="btn btn-md btn-link" disabled="disabled">下载</button>
							</c:when>
							<c:otherwise>
								<button type="button" style="height:auto;line-height:1.5" value="${result.SOUND},语音" class="btn btn-md btn-link" onclick="downFile(this)">下载</button>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td>作业人</td>
					<td><input type='text' name="operatorName"
							   id="operatorName" class='form-control'
							   value="${result.OPERATOR_NAME}" readonly="readonly" /></td>
					<td>贵宾签字</td>
					<td  colspan=2>
						<c:choose>
							<c:when test="${empty result.GB_SIGN}">
							<input type='text' name="gbSign"
								   id="gbSign" class='form-control'
								   value="${result.GB_SIGN}" readonly="readonly" /></td>
							</c:when>
							<c:otherwise>
								<img style="width:300;height:30px;" src="data:image/png;base64,${result.GB_SIGN}" />
							</c:otherwise>
						</c:choose>

					</td>
				</tr>
				<tr>
					<td>站坪司机签字</td>
					<td>
						<c:choose>
						<c:when test="${empty result.TC_SIGN}">
						<input type='text' name="tcSign"
							   id="tcSign" class='form-control'
							   value="${result.TC_SIGN}" readonly="readonly" /></td>
						</c:when>
						<c:otherwise>
							<img style="width:300;height:30px;" src="data:image/png;base64,${result.TC_SIGN}" />
						</c:otherwise>
						</c:choose>
					</td>
					<td rowspan=2>机组</td>
					<td  colspan=2 rowspan=2>
						<c:choose>
						<c:when test="${empty result.JZ_SIGN}">
						<input type='text' name="jzSign"
							   id="jzSign" class='form-control'
							   value="${result.JZ_SIGN}" readonly="readonly" /></td>
						</c:when>
						<c:otherwise>
							<img style="width:300;height:30px;" src="data:image/png;base64,${result.JZ_SIGN}" />
						</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td>交接人签字</td>
					<td>
						<c:choose>
						<c:when test="${empty result.JJ_SIGN}">
						<input type='text' name="jjSign"
							   id="jjSign" class='form-control'
							   value="${result.JJ_SIGN}" readonly="readonly" /></td>
						</c:when>
						<c:otherwise>
							<img style="width:300;height:30px;" src="data:image/png;base64,${result.JJ_SIGN}" />
						</c:otherwise>
						</c:choose>
					</td>
				</tr>
				</tbody>
			</table>
				<button id="submitBtn" class="layui-btn" lay-submit lay-filter="save" style="display: none"></button>
				<div style="display: none;" >
				<input type='text' name="id" id="id"  value="${result.ID}" />
				<input type='text' name="type" id="type"  value="${type}" />
				<input type='text' name="fltid" id="fltid" value="${result.FLTID}"/>
				<input type='text' name="aln3code" id="aln3code" value="${result.ALN_3CODE}"/>
				<input type='text' name="inOutFlag" id="inOutFlagReal" value="${result.IN_OUT_FLAG}"/> 
				<input type='text' name="operator" id="operator"
								<c:if test="${type=='add'}">value="${userid}"</c:if>
								<c:if test="${type=='edit'}">value="${result.OPERATOR}"</c:if>
								class='form-control'  readonly="readonly" />
				</div>
			</form>
			<!-- 下载文件表单 -->
			<form id="downFileForm" action="${ctx}/produce/passengerNum/downloadAttachment" style="display: none" method="post">
				<input type="hidden" id="downFileInfo" name="downFileInfo">
			</form>
		</div>
	</div>
	<script type="text/javascript"
		src="${ctxStatic}/prss/produce/billPassengerNumForm.js"></script>
</body>
</html>