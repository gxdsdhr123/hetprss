<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>考斯特残升车单据</title>
	<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
	<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript"
	src="${ctxStatic}/prss/produce/billForm.js"></script>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
	<form id="billForm" class="" action="">
		<input type="hidden" name="TYPE_CODE" id="TYPE_CODE" value="${billInfo.TYPE_CODE }">
		<input type="hidden" name="ID" id="ID" value="${billInfo.ID }">
		<input type="hidden" name="DATA_TABLE" id="DATA_TABLE" value="${billInfo.DATA_TABLE}">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">序号</label>
				<div class="layui-input-inline">
					<label class="layui-form-label">${billInfo.ID }</label>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">航班号</label>
				<div class="layui-input-inline">
					<input type="text" autocomplete="off" class="layui-input"
						name="FLIGHT_NUMBER" id="FLIGHT_NUMBER" value="${billInfo.FLIGHT_NUMBER }">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">机型</label>
				<div class="layui-input-inline">
				
					<c:if test="${billInfo.TYPE_CODE!='jwqyc' }">
						<label class="layui-form-label">${billInfo.ACT_TYPE }</label>
					</c:if>
					<c:if test="${billInfo.TYPE_CODE=='jwqyc' }">
						<select class="form-control select2 ACT_TYPE" data-type="ACT_TYPE" style="width: 190px;"
							name="ACT_TYPE" id="ACT_TYPE" >
							<option value="">请选择机型</option>
							<c:forEach items="${atcactypeList}" var="actype">
								<option value="${actype.DESCRIPTION}" ${ actype.DESCRIPTION==billInfo.ACT_TYPE?'selected':''}>${actype.DESCRIPTION}</option>
							</c:forEach>
						</select>
					</c:if>
				</div>
			</div>
		</div>
	
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">机号</label>
				<div class="layui-input-inline">
					<input type="text" autocomplete="off" class="layui-input"
						name="AIRCRAFT_NUMBER" id="AIRCRAFT_NUMBER" value="${billInfo.AIRCRAFT_NUMBER }">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">日期</label>
				<div class="layui-input-inline">
					<input type="text" autocomplete="off" class="layui-input" name="ITEM_DATE" id="ITEM_DATE" 
						onClick="WdatePicker({dateFmt:'yyyyMMdd'})" value="${billInfo.ITEM_DATE }" readonly="readonly">
				</div>
			</div>
		</div>
		<table class="layui-table" >
  			<colgroup>
    			<col width="200">
    			<col>
  			</colgroup>
 			<tbody>
 				<tr>
      				<td align="center">项目</td>
      				<td align="center">开始时间</td>
      				<td align="center">结束时间</td>
      				<td align="center" colspan="3">数量</td>
    			</tr> 
 				<tr>
 					<td align="center"><input type="text" autocomplete="off" class="layui-input"
						name="ITEM_NAME" id="ITEM_NAME" value="${billInfo.ITEM_NAME }"></td>
 					<td align="center"><input type="text" autocomplete="off" class="layui-input" name="START_TM" id="START_TM" 
						onClick="WdatePicker({dateFmt:'HHmm'})" value="${billInfo.START_TM }">
 					<td align="center"><input type="text" autocomplete="off" class="layui-input" name="END_TM" id="END_TM" 
						onClick="WdatePicker({dateFmt:'HHmm'})" value="${billInfo.END_TM }">
 					<td align="center" colspan="3"><input type="text" autocomplete="off" class="layui-input"
						name="NUM" id="NUM" value="${billInfo.NUM }"></td>
 				</tr>
 				<tr>
 					<td align="center">备注</td>
 					<td colspan="3"><input type="text" autocomplete="off" class="layui-input"
						name="REMARK" id="REMARK" value="${billInfo.REMARK }"></td>
 				</tr>
 			</tbody>
 		</table>
		<div class="layui-inline">
			<label class="layui-form-label">操作人</label>
			<div class="layui-input-inline">
			<c:if test="${billInfo.TYPE_CODE!='jwqyc' }">
				<label class="layui-form-label">${billInfo.OPERATOR }</label>
			</c:if>
			<c:if test="${billInfo.TYPE_CODE=='jwqyc' }">
				<select class="form-control select2 OPERATOR" data-type="OPERATOR"
						name="OPERATOR" id="OPERATOR" >
						<c:forEach items="${operatorsList}" var="item">
							<option value="${item.CODE}" ${ item.DESCRIPTION==billInfo.OPERATOR?'selected':''}>${item.DESCRIPTION}</option>
						</c:forEach>
					</select>
			</c:if>
			</div>
		</div>
		<div class="layui-inline">
			<label class="layui-form-label">创建时间</label>
			<div class="layui-input-inline">
				<label class="layui-form-label">${billInfo.CREATE_DATE }</label>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">航空公司代办</label>
				<div id="SIGNATORYdiv" class="layui-input-inline">
					<c:if test="${billInfo.TYPE_CODE=='jwqyc' }">
						<input type="text" autocomplete="off" class="layui-input"
							name="SIGN" id="SIGN" value="${billInfo.SIGN }">
					</c:if>
					<c:if test="${flag=='edit' and  billInfo.SIGNATORY !=null and billInfo.SIGNATORY !=''}">
						<input type="hidden" name="SIGNATORY" id="SIGNATORY" value="${billInfo.SIGNATORY }">
						<img src="${ctx }/produce/bill/outputPicture?id=${billInfo.SIGNATORY }" height=45 width=80/>
					</c:if>
				</div>
			</div>
		</div>
	</form>
</body>
</html>