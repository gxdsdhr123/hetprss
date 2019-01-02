<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css"
	rel="stylesheet" />
<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<div id="filterDiv" style="display: none !important">
	<form id="filterForm" class="form-horizontal layui-form">
		<table class="layui-table"
			style="width: 490px; text-align: center; margin: 10px auto;">
			<tbody>
				<tr>
					<td nowrap="nowrap">开始日期</td>
					<td><input class="layui-input" type='text' name='startDate'
						id="startDate" placeholder='请选择航班日期'
						onclick="WdatePicker({dateFmt:'yyyyMMdd'});" /></td>
					<td nowrap="nowrap">结束日期</td>
					<td><input class="layui-input" type='text' name='endDate'
						id="endDate" placeholder='请选择航班日期'
						onclick="WdatePicker({dateFmt:'yyyyMMdd'});" /></td>
				</tr>
				<tr>
					<td nowrap="nowrap">航空公司</td>
					<td colspan="3"><input name="airline" placeholder="请选择"
						class="layui-input" type="text" readonly="readonly"
						onclick="openCheck('airline')"> <input name="airlinevalue"
						style="display: none;"></td>
				</tr>
				<tr>
					<td nowrap="nowrap">进/出港</td>
					<td colspan="2"><select name="flag" id="flag"
						class="form-control stayFlag">
							<option value="">请选择</option>
							<option value="A">进港</option>
							<option value="D">出港</option>
					</select></td>
					<td>-</td>
			</tbody>
		</table>
	</form>
</div>