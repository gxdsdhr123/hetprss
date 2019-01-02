<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
		<title>电子围栏配置信息新增、编辑页</title>
		<meta name="decorator" content="default" />
		<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
		<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
		<script type="text/javascript" src="${ctxStatic}/prss/rule/gisConfigForm.js"></script>
		<script type="text/javascript">
			var operateType = "${operateType}";
			var areaCodeSource = ${areaCodeSource};
		</script>
	</head>
	<body>
		<div id="baseTables">
			<form id="tableForm" action="${ctx}/rule/gisConfig/save" method="post">
				<input type="hidden" id="id" name="id" value="${modifyData.id}" style="background-color: black">
				<input type="hidden" id="areaCodesVal" name="areaCodesVal" value="${modifyData.areaCode}" style="background-color: black">
				<input type="hidden" id="operateType" name="operateType" value="${operateType}" style="background-color: black">
				
				<table id="baseTable" class="layui-table" style="width:590px;text-align:center;margin:10px auto;">
					<tr>
						<td>保障类型</td>
						<td>
							<select id="reskind" name="reskind" class="form-control select2" onchange="getRestype()">
								<option value="">请选择保障类型</option>
								<c:forEach items="${reskindSource}" var="item">
									<option <c:if test="${modifyData.jobKind==item.id}">selected</c:if> value="${item.id}" >${item.text}</option>
								</c:forEach>
							</select>
						</td>
						<td>作业类型</td>
						<td>
								<select id="restype" name="restype" class="form-control select2" onchange="getProcess()">
								<option value="">请选择作业类型</option>
								<c:forEach items="${restypeSource}" var="item">
									<option <c:if test="${modifyData.jobType==item.id}">selected</c:if> value="${item.id}" >${item.text}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td>流程名称</td>
						<td>
							<select id="processId" name="processId" class="form-control select2" onchange="getNode()">
								<option value="">请选择流程</option>
								<c:forEach items="${processSource}" var="item">
									<option <c:if test="${modifyData.procId==item.id}">selected</c:if> value="${item.id}" >${item.text}</option>
								</c:forEach>
							</select>
						</td>
						<td>节点名称</td>
						<td>
							<select id="nodeId" name="nodeId" class="form-control select2">
								<option value="">请选择节点</option>
								<c:forEach items="${nodeSource}" var="item">
									<option <c:if test="${modifyData.nodeId==item.id}">selected</c:if> value="${item.id}" >${item.text}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td>任务类型</td>
						<td>
							<select id="targetType" name="targetType" class="form-control select2" onchange="getTargetArea()">
								<option value="">请选择任务类型</option>
								<option <c:if test="${modifyData.targetType==0}">selected</c:if> value="0">机位</option>
								<option <c:if test="${modifyData.targetType==1}">selected</c:if> value="1">登机口</option>
								<option <c:if test="${modifyData.targetType==2}">selected</c:if> value="2">到达口</option>
							</select>
						</td>
						<td>任务位置</td>
						<td>
							<select id="targetArea" name="targetArea" class="form-control select2">
								<option value="">请选择任务位置</option>
								<c:forEach items="${targetAreaSource}" var="item">
									<option <c:if test="${modifyData.targetArea==item.id}">selected</c:if> value="${item.id}" >${item.text}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td>电子围栏区域</td>
						<td colspan="3">
							<textarea id="areaName" style="background-color: black;width:400px;height: 140px;" readonly="readonly" onclick="openCheck()">${modifyData.areaName}</textarea>
						</td>
					</tr>
					<tr>
						<td>延迟时间</td>
						<td>
							<div style="float:left;">
								<input type='text' style="float:left;width:75%" id="delaySecond" name="delaySecond" class='form-control' value="${modifyData.delaySecond}"/>
								<span>秒</span>
							</div>
						</td>
						<td>是否生效</td>
						<td>
							<select id="inUse" name="inUse" style="width: 100px;height: 30px">
								<option <c:if test="${modifyData.inUse==1}">selected</c:if> value="1">生效</option>
								<option <c:if test="${modifyData.inUse==0}">selected</c:if> value="0">无效</option>
							</select>
						</td>
					</tr>	
				</table>
			</form>
		</div>
	</body>
</html>