<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>发送报文</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css"
	rel="stylesheet" />
<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
</script>

</head>
<body>
	<div class="content">
		<form id="createForm" class="form-horizontal"
			action="${ctx}/telegraph/auto/saveTime" enctype="multipart/form-data"
			method="post">
			<input type="hidden" id="msgtext" name="msgtext"> <input
				type="hidden" id="isHis" name="isHis" value="${isHis}">
				<input type="hidden" name="id" value="${id}" id="id"/>

			<div class="row">
				<div class="col-sm-12">
					<div class="form-group">
						<label for="inputEmail3" class="col-sm-1 control-label">航班号</label>
						<div class="col-sm-2">
							<input class="form-control" id="flightNumber" name="flightNumber" type="text"
								value="${info.FLIGHT_NUMBER}">
						</div>
						<label for="inputEmail3" class="col-sm-1 control-label">航班日期</label>
						<div class="col-sm-2">
							<input class="form-control" id="flightDate" name="flightDate" value="${info.FLIGHT_DATE }"
								readonly="readonly"
								onclick="WdatePicker({dateFmt:'yyyyMMdd'})" type="text">
						</div>
						<label for="inputEmail3" class="col-sm-1 control-label">进出港标识</label>
						<div class="col-sm-2">
							<select name="fiotype" id="fiotype" class="form-control">
								<option value="">全部</option>
								<option value="A" ${info.IOTYPE=='A'?'selected':'' }>进港</option>
								<option value="D" ${info.IOTYPE=='D'?'selected':'' }>出港</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label for="inputEmail3" class="col-sm-1 control-label">报文类型</label>
						<div class="col-sm-2">
							<select name="telegraphType" class="form-control"
								id="telegraphType">
								<option value="">请选择</option>
								<c:forEach items="${telegraphTypeList}" var="var">
									<option value="${var.TG_CODE }" ${info.TG_CODE==var.TG_CODE?'selected':'' }>${var.TG_CODE }</option>
								</c:forEach>
							</select>
						</div>
						<label class="col-sm-1 control-label">优先级</label>
						<div class="col-sm-2">
							<c:if test="${info ==null }">
								<select name="priority" class="form-control" id="priority">
									<option value="">请选择</option>
									<c:forEach items="${priorityList}" var="var">
										<option value="${var.ID }" ${var.NAME=='QU'?'selected':'' }>${var.NAME }</option>
									</c:forEach>
								</select>
							</c:if>
							<c:if test="${info !=null }">
								<select name="priority" class="form-control" id="priority">
									<option value="">请选择</option>
									<c:forEach items="${priorityList}" var="var">
										<option value="${var.ID }" ${var.NAME==info.PRIORITY?'selected':'' }>${var.NAME }</option>
									</c:forEach>
								</select>
							</c:if>
						</div>

						<label for="inputEmail3" class="col-sm-1 control-label">发报地址</label>
						<div class="col-sm-2">
							<input class="form-control" id="sendAddress" name="sendAddress"
								type="text" value="${sendAddress }" readonly="readonly">
						</div>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="col-sm-12">
					<div class="form-group">
						<label class="col-sm-1 control-label">报文正文</label>
						<div class="col-sm-9">
							<textarea rows="" cols="" class="layui-textarea" id="mtext" name="mtext" 
								style="height: 250px;">${info.TEXT }</textarea>
						</div>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="col-sm-12">
					<div class="form-group sita" >
						<label class="col-sm-1 control-label">收报地址
							</label>
						<div class="col-sm-8">
							<input class="form-control" name="sita" id="sita" type="text">
						</div>
						<div class="col-sm-2"  id="btn-group">
								<button type="button" class="layui-btn layui-btn-primary"
									onclick="senderList();">选择</button>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
	<script type="text/javascript"
		src="${ctxStatic}/prss/telegraph/sendTelegraph.js"></script>

</body>
</html>