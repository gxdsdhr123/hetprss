<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/prss/rule/css/guide.css" rel="stylesheet" />
<title>编辑条件</title>
</head>
<body style="min-height: 10px">
	<input type="hidden" name="varId" id="varId" value="${varId }" />
	<table id="contentTable" >
		<tbody>
			<tr>
				<td style="width: 200px;">
				变量名称
					<div id="varLists" style="position: relative;"></div>
				</td>
				<td style="width: 30%;vertical-align:top">
					逻辑规则
					<div>
					<input type="button"
								class="layui-btn layui-btn-small layui-btn-normal" value="+"
								onclick="getValue(this.value,'add','opt')" style="font-size:16px"></input>
					<input type="button"
								class="layui-btn layui-btn-small layui-btn-normal" style="font-size:16px" value="-"
								onclick="getValue(this.value,'sub','opt')" ></input>
					</div>
				</td>
				<td style="width: 25%;vertical-align:top">
					<table>
						<tr>
							<td><input name="input_val" id="input_val" class="layui-input" type="number"></td>
						</tr>
						<tr>
							<td style="text-align:right">
								<input type="button"
									class="layui-btn layui-btn-small layui-btn-normal" value="数字"
									onclick="inpuValue('int','val')"></input> 
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td colspan='3'>
					<div id="mtext" class="layui-textarea"
						style="overflow: auto;" >${express }</div></td>
			</tr>
		</tbody>
	</table>
	<script type="text/javascript" src="${ctxStatic}/prss/rule/guide.js"></script>

</body>
</html>