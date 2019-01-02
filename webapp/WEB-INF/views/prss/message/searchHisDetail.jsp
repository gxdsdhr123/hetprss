<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/edittable.jsp"%>

<link href="${ctxStatic}/prss/message/css/messageTypeInfo.css"
	rel="stylesheet" />
<style type="text/css">
.layui-table {
	margin: 0px;
}

#contentTable td:nth-child(odd) {
	padding: 7px 7px 7px 30px;
}

#contentTable td {
	border-right: 1px #006DC0 solid;
	margin-left: 10px;
}

#contentTable table {
	border-bottom: 1px #006DC0 solid;
}
</style>
<title>指令详情</title>
<script type="text/javascript">
	
</script>
</head>
<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<body>
	<table id="contentTable">
		<tr>
			<td width="296px;"><label>消息类型：</label>
				<div class="layui-input-inline">
					<input id="mtype" name="mtype" class="layui-input" type="text"
						value="${vo.mtype }" disabled="disabled">
				</div></td>
			<td rowspan="7"
				style="vertical-align: text-top; width: 250px; height: 300px; overflow: auto;">
				<table class="layui-table">
					<thead>
						<tr>
							<th>消息回复人</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${list}" var="vo">
							<tr>
								<td>${vo.message}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</td>
		</tr>
		<tr>
			<td><label>航&nbsp;&nbsp;班&nbsp;号：</label>
				<div class="layui-input-inline">
					<input id="flightnumber" name="flightnumber" class="layui-input"
						type="text" value="${vo.flightnumber }" disabled="disabled">
				</div></td>
		</tr>
		<tr>
			<td><label>定时发送：</label>
				<div class="layui-input-inline">

					<input id="ifautosend" name="ifautosend" class="layui-input"
						type="text" value="${vo.ifautosend }" disabled="disabled">
				</div></td>
		</tr>
		<tr>
			<td><label>消息标题：</label>
				<div class="layui-input-inline">
					<input id="mtitle" name="mtitle" class="layui-input" type="text"
						value="${vo.mtitle }" disabled="disabled">
				</div></td>
		</tr>
		<tr>
			<td><label>消息正文：</label>
				<div class="layui-input-inline">
					<div class="mtext" style="width: 120px; word-wrap: break-word; border: 1px #006DC0 solid; width: 153px; height: 75px; overflow: auto;"
						disabled="disabled">${vo.mtext }</div>
				</div></td>
		</tr>
		<tr>
			<td><label>发&nbsp;&nbsp;送&nbsp;人：</label>
				<div class="layui-input-inline">

					<input id="sendercn" name="sendercn" class="layui-input"
						type="text" value="${vo.sendercn }" disabled="disabled">
				</div></td>
		</tr>
		<tr>
			<td><label>发送时间：</label>
				<div class="layui-input-inline">

					<input id="sendtime" name="sendtime" class="layui-input"
						type="text" value="${vo.sendtime }" disabled="disabled">
				</div></td>
		</tr>
	</table>

	<script type="text/javascript">
		var tpl = $(".mtext");
		tpl.css("position", "relative");
		new PerfectScrollbar(tpl[0]);
	</script>

</body>
</html>