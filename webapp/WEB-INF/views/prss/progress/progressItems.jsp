<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<c:forEach items="${taskList}" var="taskItem">
	<div class="task-item" onselectstart="return false" data-infltid="${taskItem.infltid}" data-outfltid="${taskItem.outfltid}" data-taskstate="${taskState}" style="background:#092955">
		<table class="taskGrid">
			<tr>
				<td rowspan="2" style="font-size:140%;width:30px;">
					<c:choose>
						<c:when test="${taskItem.alarm eq 3}">
							<i class="fa fa-exclamation-circle text-red">&nbsp;</i>
						</c:when>
						<c:when test="${taskItem.alarm eq 2}">
							<i class="fa fa-exclamation-circle text-orange">&nbsp;</i>
						</c:when>
						<c:when test="${taskItem.alarm eq 1}">
							<i class="fa fa-exclamation-circle" style="color:#e6f312">&nbsp;</i>
						</c:when>
						<c:otherwise>
							<i class="fa fa-circle" style="color:#2C5961">&nbsp;</i>
						</c:otherwise>
					</c:choose>
				</td>
				<%-- 航班号 --%>
				<td>
					<span style="color:#FFC000">${taskItem.inVipFlag}</span>
					<c:choose>
						<c:when test="${not empty taskItem.infltid}">
							<span data-fltid="${taskItem.infltid}" onclick="openFltRemark(this)">${taskItem.infltNo}</span>
						</c:when>
						<c:otherwise>
							${taskItem.infltNo}
						</c:otherwise>
					</c:choose>
					/
					<span style="color:#FFC000">${taskItem.outVipFlag}</span>
					<c:choose>
						<c:when test="${not empty taskItem.outfltid}">
							<span data-fltid="${taskItem.outfltid}" onclick="openFltRemark(this)">${taskItem.outfltNo}</span>
						</c:when>
						<c:otherwise>
							${taskItem.outfltNo}
						</c:otherwise>
					</c:choose>
				</td>
				<%-- 机型 --%>
				<td style="color:#999;width:10%">${taskItem.actType}</td>
				<%-- 机号 --%>
				<td style="color:#999;width:10%">${taskItem.actNo}</td>
				<%-- 进港：机位、出港：登机口 --%>
				<td style="color:#999;width:10%">${taskItem.actStand}</td>
				<%-- 性质：D、国内 I、国际  M、混合 --%>
				<td style="width:40px">${taskItem.attr}</td>
				<%-- 任务数 --%>
				<td rowspan="2" style="width:40px">
					<label class="pull-right" style="font-size:150%">${taskItem.taskNum}</label>
				</td>
			</tr>
			<tr>
				<td colspan="4" style="color:#999">
					<label>STA：${taskItem.sta}</label>
					<label>ETA：${taskItem.eta}</label>
					<label>ATA：${taskItem.ata}</label>
					<br>
					<label>STD：${taskItem.std}</label>
					<label>ETD：${taskItem.etd}</label>
					<label>ATD：${taskItem.atd}</label>
				</td>
				<td>
					<%-- 航班状态 --%>
					<c:choose>
						<%-- 正常 --%>
						<c:when test="${taskItem.fltStatusCode eq 0}">
							<label class="text-green">${taskItem.fltStatus}</label>
						</c:when>
						<%-- 取消 --%>
						<c:when test="${taskItem.fltStatusCode eq 1}">
							<label style="color:#D99694">${taskItem.fltStatus}</label>
						</c:when>
						<%-- 删除 --%>
						<c:when test="${taskItem.fltStatusCode eq 2}">
							<label>${taskItem.fltStatus}</label>
						</c:when>
						<%-- 延误 --%>
						<c:when test="${taskItem.fltStatusCode eq 3}">
							<label class="text-yellow">${taskItem.fltStatus}</label>
						</c:when>
						<%-- 起飞 --%>
						<c:when test="${taskItem.fltStatusCode eq 4}">
							<label class="text-red">${taskItem.fltStatus}</label>
						</c:when>
						<%-- 降落 --%>
						<c:when test="${taskItem.fltStatusCode eq 5}">
							<label class="text-light-blue">${taskItem.fltStatus}</label>
						</c:when>
						<%-- 空反 --%>
						<c:when test="${taskItem.fltStatusCode eq 6}">
							<label class="text-purple">${taskItem.fltStatus}</label>
						</c:when>
						<%-- 地反 --%>
						<c:when test="${taskItem.fltStatusCode eq 7}">
							<label class="text-purple">${taskItem.fltStatus}</label>
						</c:when>
						<c:otherwise>
							<label>${taskItem.fltStatus}</label>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</table>
	</div>
</c:forEach>
