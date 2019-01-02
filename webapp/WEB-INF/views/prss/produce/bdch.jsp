<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>摆渡车单据</title>
<script type="text/javascript"
	src="${ctxStatic}/prss/produce/billForm.js"></script>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
	<form id="billForm" class="layui-form" action="">
		<input type="hidden" name="TYPE_CODE" id="TYPE_CODE" value="${billInfo.TYPE_CODE }">
		<input type="hidden" name="ID" id="ID" value="${billInfo.ID }">
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
					<label class="layui-form-label">${billInfo.ACT_TYPE }</label>
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
      				<td align="center">数量</td>
    			</tr> 
 				<tr>
 					<td align="center"><input type="text" autocomplete="off" class="layui-input"
						name="ITEM_NAME" id="ITEM_NAME" value="${billInfo.ITEM_NAME }"></td>
 					<td align="center"><input type="text" autocomplete="off" class="layui-input"
						name="NUM" id="NUM" value="${billInfo.NUM }"></td>
 				</tr>
 				<tr>
 					<td align="center">备注</td>
 					<td><input type="text" autocomplete="off" class="layui-input"
						name="REMARK" id="REMARK" value="${billInfo.REMARK }"></td>
 				</tr>
 			</tbody>
 		</table>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">操作人</label>
				<div class="layui-input-inline">
					<label class="layui-form-label">${billInfo.OPERATOR }</label>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">创建时间</label>
				<div class="layui-input-inline">
					<label class="layui-form-label">${billInfo.CREATE_DATE }</label>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">航空公司代办</label>
				<div id="SIGNATORYdiv" class="layui-input-inline">
					<input type="hidden" name="SIGNATORY" id="SIGNATORY" value="${billInfo.SIGNATORY }">
					<img src="${ctx }/produce/bill/outputPicture?id=${billInfo.SIGNATORY }" height=45 width=80/>
				</div>
			</div>
		</div>
	</form>
</body>
</html>