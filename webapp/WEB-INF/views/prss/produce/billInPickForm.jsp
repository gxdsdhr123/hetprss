<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>进港航班接机核对表</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
	<div id="container">
		<div id="baseTables">
			<form id="tableForm" class="layui-form"
				action="${ctx}/produce/inPick/save" method="post">
				<table id="baseTable" class="layui-table"
					style="width: 900px; text-align: center; margin: 10px auto;">
					<thead>
						<tr>
							<th colspan=4>进港航班接机核对表</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td style="width: 150px"><span style="color: red;">*</span>日期</td>
							<td><input type='text' maxlength="20" name='flightDate'
								id="flightDate" placeholder='请选择日期' class='form-control'
								onclick="WdatePicker({dateFmt:'yyyyMMdd'});"
								value="${result.FLIGHT_DATE}" onblur="getFltInfo()"
								lay-verify="required" /></td>

							<td style="width: 150px"><span style="color: red;">*</span>航班号</td>
							<td><input type='text' name="flightNumber" id="flightNumber"
								class='form-control' value="${result.FLIGHT_NUMBER}"
								onblur="getFltInfo()" lay-verify="required" /></td>
						</tr>
						<tr>
							<td>机号</td>
							<td><input type='text' name="aircraftNumber"
								id="aircraftNumber" class='form-control'
								value="${result.AIRCRAFT_NUMBER}" readonly="readonly" /></td>
							<td>机位</td>
							<td><input type='text' name="actstandCode" id="actstandCode"
								class='form-control' value="${result.ACTSTAND_CODE}"
								readonly="readonly" /></td>
						</tr>
						<tr>
							<td>机型</td>
							<td><input type='text' name="acttypeCode" id="acttypeCode"
								class='form-control' value="${result.ACTTYPE_CODE}"
								readonly="readonly" /></td>
							<td><span style="color: red;">*</span>航班属性</td>
							<td><select id="inOutFlag" name="inOutFlag"
								class='form-control' lay-filter="valueChange">
									<option
										<c:if test="${fn:substring(result.IN_OUT_FLAG,0,1)==''}">selected</c:if>
										value=""></option>
									<option
										<c:if test="${fn:substring(result.IN_OUT_FLAG,0,1)=='A'}">selected</c:if>
										value="A">进港</option>
									<option
										<c:if test="${fn:substring(result.IN_OUT_FLAG,0,1)=='D'}">selected</c:if>
										value="D">出港</option>
							</select></td>
						</tr>
						<tr>
							<td>STA</td>
							<td><input type='text' name="sta" id="sta"
								class='form-control' value="${result.STA}" readonly="readonly" /></td>
							<td>ETA</td>
							<td><input type='text' name="eta" id="eta"
								class='form-control' value="${result.ETA}" readonly="readonly" /></td>
						</tr>
						<tr>
							<td>航线类型</td>
							<td><select id="fltAttr" name="fltAttr" class='form-control'>
									<option value=" " ${result.FLT_ATTR==' '?'selected':'' }></option>
									<option value="国内" ${result.FLT_ATTR=='国内'?'selected':'' }>国内</option>
									<option value="国际" ${result.FLT_ATTR=='国际'?'selected':'' }>国际</option>
									<option value="混合" ${result.FLT_ATTR=='混合'?'selected':'' }>混合</option>
							</select></td>
							<td>开舱时间</td>
							<td><input type='text' name="kcTm"
								placeholder="例：8点10分应填写0810" id="kcTm" class='form-control'
								lay-verify="fltTime" value="${result.KC_TM}" /></td>
						</tr>
						<tr>
							<td>航程</td>
							<td><input type='text' name="route" id="route"
								class='form-control' value="${result.ROUTE}" readonly="readonly" /></td>
							<td>航班类型</td>
							<td><select id="fltType" name="fltType" class='form-control'>
									<option value="   " ${result.FLT_TYPE=='  '?'selected':'' }></option>
									<option value="航前" ${result.FLT_TYPE=='航前'?'selected':'' }>航前</option>
									<option value="航后" ${result.FLT_TYPE=='航后'?'selected':'' }>航后</option>
									<option value="过站" ${result.FLT_TYPE=='过站'?'selected':'' }>过站</option>
							</select></td>
						</tr>
						<tr>
							<td colspan=4>交接物品</td>
						</tr>
						<tr>
							<td>描述</td>
							<td>视频</td>
							<td>图片</td>
							<td>语音</td>
						</tr>
						<tr>
							<td><input type='text' name="jjwp" id="jjwp"
								class='form-control' value="${result.JJWP}" /></td>
							<td><c:choose>
									<c:when test="${empty result.VIDEO}">
										<button type="button" style="height: auto; line-height: 1.5"
											class="btn btn-md btn-link" disabled="disabled">下载</button>
									</c:when>
									<c:otherwise>
										<button type="button" style="height: auto; line-height: 1.5"
											value="${result.VIDEO},视频" class="btn btn-md btn-link"
											onclick="downFile(this)">下载</button>
									</c:otherwise>
								</c:choose></td>
							<td><c:choose>
									<c:when test="${empty result.PICTURE}">
										<button type="button" style="height: auto; line-height: 1.5"
											class="btn btn-md btn-link" disabled="disabled">下载</button>
									</c:when>
									<c:otherwise>
										<button type="button" style="height: auto; line-height: 1.5"
											value="${result.PICTURE},图片" class="btn btn-md btn-link"
											onclick="downFile(this)">下载</button>
									</c:otherwise>
								</c:choose></td>
							<td><c:choose>
									<c:when test="${empty result.SOUND}">
										<button type="button" style="height: auto; line-height: 1.5"
											class="btn btn-md btn-link" disabled="disabled">下载</button>
									</c:when>
									<c:otherwise>
										<button type="button" style="height: auto; line-height: 1.5"
											value="${result.SOUND},语音" class="btn btn-md btn-link"
											onclick="downFile(this)">下载</button>
									</c:otherwise>
								</c:choose></td>
						<tr>
							<td colspan=4>备注</td>
						</tr>
						<tr>
							<td>描述</td>
							<td>视频</td>
							<td>图片</td>
							<td>语音</td>
						</tr>
						<tr>
							<td><input type='text' name="remark" id="remark"
								class='form-control' value="${result.REMARK}" /></td>
							<td><c:choose>
									<c:when test="${empty result.VIDEO}">
										<button type="button" style="height: auto; line-height: 1.5"
											class="btn btn-md btn-link" disabled="disabled">下载</button>
									</c:when>
									<c:otherwise>
										<button type="button" style="height: auto; line-height: 1.5"
											value="${result.VIDEO},视频" class="btn btn-md btn-link"
											onclick="downFile(this)">下载</button>
									</c:otherwise>
								</c:choose></td>
							<td><c:choose>
									<c:when test="${empty result.PICTURE}">
										<button type="button" style="height: auto; line-height: 1.5"
											class="btn btn-md btn-link" disabled="disabled">下载</button>
									</c:when>
									<c:otherwise>
										<button type="button" style="height: auto; line-height: 1.5"
											value="${result.PICTURE},图片" class="btn btn-md btn-link"
											onclick="downFile(this)">下载</button>
									</c:otherwise>
								</c:choose></td>
							<td><c:choose>
									<c:when test="${empty result.SOUND}">
										<button type="button" style="height: auto; line-height: 1.5"
											class="btn btn-md btn-link" disabled="disabled">下载</button>
									</c:when>
									<c:otherwise>
										<button type="button" style="height: auto; line-height: 1.5"
											value="${result.SOUND},语音" class="btn btn-md btn-link"
											onclick="downFile(this)">下载</button>
									</c:otherwise>
								</c:choose></td>
						</tr>
						<tr>
							<td colspan=1>服务员签字</td>
							<td colspan=3><c:if
									test="${type=='add' || result.FWY_SIGN=='' || result.FWY_SIGN==null}">
									<input type='text' name="fwySign" id="fwySign"
										class='form-control' value="${result.FWY_SIGN}" />
								</c:if> <c:if
									test="${type=='edit' && result.FWY_SIGN!='' && result.FWY_SIGN!=null}">
									<img
										src="${ctx }/produce/inPick/outputPicture?id=${result.FWY_SIGN}"
										height=45 width=80 />
								</c:if></td>
						</tr>
						<tr>
							<td colspan=1>站坪司机签字</td>
							<td colspan=3><c:if
									test="${type=='add' || result.ZPSJ_SIGN=='' || result.ZPSJ_SIGN==null}">
									<input type='text' name="zpsjSign" id="zpsjSign"
										class='form-control' value="${result.ZPSJ_SIGN}" />
								</c:if> <c:if
									test="${type=='edit' && result.ZPSJ_SIGN!='' && result.ZPSJ_SIGN!=null}">
									<img
										src="${ctx }/produce/inPick/outputPicture?id=${result.ZPSJ_SIGN}"
										height=45 width=80 />
								</c:if></td>
						</tr>
						<tr>
							<td colspan=1>交接人签字</td>
							<td colspan=3><c:if
									test="${type=='add' || result.JJR_SIGN=='' || result.JJR_SIGN==null}">
									<input type='text' name="jjrSign" id="jjrSign"
										class='form-control' value="${result.JJR_SIGN}" />
								</c:if> <c:if
									test="${type=='edit' && result.JJR_SIGN!='' && result.JJR_SIGN!=null}">
									<img
										src="${ctx }/produce/inPick/outputPicture?id=${result.JJR_SIGN}"
										height=45 width=80 />
								</c:if></td>
						</tr>
					</tbody>
				</table>
				<button id="submitBtn" class="layui-btn" lay-submit
					lay-filter="save" style="display: none"></button>
				<div style="display: none;">
					<input type='text' name="id" id="id" value="${result.ID}" /> <input
						type='text' name="type" id="type" value="${type}" /> <input
						type='text' name="fltid" id="fltid" value="${result.FLTID}" /> <input
						type='text' name="aln3code" id="aln3code"
						value="${result.ALN_3CODE}" /> <input type='text'
						name="inOutFlag" id="inOutFlagReal" value="${result.IN_OUT_FLAG}" />
					<input type='text' name="operator" id="operator"
						<c:if test="${type=='add'}">value="${userid}"</c:if>
						<c:if test="${type=='edit'}">value="${result.OPERATOR}"</c:if>
						class='form-control' readonly="readonly" />
				</div>
			</form>
			<!-- 下载文件表单 -->
			<form id="downFileForm"
				action="${ctx}/produce/inPick/downloadAttachment"
				style="display: none" method="post">
				<input type="hidden" id="downFileInfo" name="downFileInfo">
			</form>
		</div>
	</div>
	<script type="text/javascript"
		src="${ctxStatic}/prss/produce/billInPickForm.js"></script>
</body>
</html>