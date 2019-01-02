<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>离港信息录入</title>
<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/departPsgInfoForm.js"></script>
</head>
<body>
	<shiro:hasPermission name="fd:editPassengerT">
			<input type="hidden" value="1" id="editPassengerT" >
	</shiro:hasPermission>
    <form id="tableForm"  class="layui-form" action="${ctx}/departPsgInfo/save" method="post">
	<table class="layui-table">
	<tr>
	<td colspan="6">过站信息</td>
	</tr>
		<tr>
			<td width=16.5%>过站成人数</td>
			<td width=16.5%>
				<input type="text" name="transitGpxnP" id="transitGpxnP" value="${entity.transitGpxnP}" class="layui-input" 
					<c:if test="${isOverStation=='false'}">readonly="readonly"</c:if> lay-verify="numberByCus" />
			</td>
			<td width=16.5%>过站儿童数</td>
			<td width=16.5%>
				<input type="text" name="transitGpxnC" id="transitGpxnC" value="${entity.transitGpxnC}" class="layui-input"
					<c:if test="${isOverStation=='false'}">readonly="readonly"</c:if> lay-verify="numberByCus">
			</td>
			<td width=16.5%>过站婴儿数</td>
			<td width=16.5%>
				<input type="text" name="transitGpxnI" id="transitGpxnI" value="${entity.transitGpxnI}" class="layui-input"
					<c:if test="${isOverStation=='false'}">readonly="readonly"</c:if> lay-verify="numberByCus">
			</td>
		</tr>
		<tr>
			<td>过站货物重量(kg)</td>
			<td>
				<input type="text" name="transitHwWeight" id="transitHwWeight" value="${entity.transitHwWeight}" class="layui-input"
						<c:if test="${isOverStation=='false'}">readonly="readonly"</c:if> lay-verify="numberByCus">
			</td>
			<td>过站邮件重量(kg)</td>
			<td>
				<input type="text" name="transitMailWeight" id="transitMailWeight" value="${entity.transitMailWeight}" class="layui-input"
						<c:if test="${isOverStation=='false'}">readonly="readonly"</c:if> lay-verify="numberByCus" >
			</td>
			<td>过站行李重量(kg)</td>
			<td>
				<input type="text" name="transitBagnWeight" id="transitBagnWeight" value="${entity.transitBagnWeight}" class="layui-input"
						<c:if test="${isOverStation=='false'}">readonly="readonly"</c:if> lay-verify="numberByCus">
			</td>
		</tr>
	</table>
         <c:forEach items="${result}" var="item">
           <c:set var="crs" value="leg${item.routeIndex}GpxnP"></c:set>
           <c:set var="ets" value="leg${item.routeIndex}GpxnC"></c:set>
           <c:set var="yes" value="leg${item.routeIndex}GpxnI"></c:set>
           <c:set var="hw" value="leg${item.routeIndex}HwWeight"></c:set>
           <c:set var="yj" value="leg${item.routeIndex}MailWeight"></c:set>
           <c:set var="xl" value="leg${item.routeIndex}BagnWeight"></c:set>
           <input type="hidden" id="leg${item.routeIndex}AdPort" name="leg${item.routeIndex}AdPort" value="${item.routeEnName}">
           <table class="layui-table">
           <tr >
           <td colspan="6" >
         		  第${item.routeIndex}航段:${item.routeCnName}
           </td>
           </tr>
		<tr>
			<td width=16.5%>成人数</td>
			<td width=16.5%>
				<input type="text" name="leg${item.routeIndex}GpxnP" id="leg${item.routeIndex}GpxnP" value="${entity[crs]}" class="layui-input" lay-verify="numberByCus">
			</td>
			<td width=16.5%>儿童数</td>
			<td width=16.5%>
				<input type="text" name="leg${item.routeIndex}GpxnC" id="leg${item.routeIndex}GpxnC" value="${entity[ets]}" class="layui-input" lay-verify="numberByCus">
			</td>
			<td width=16.5%>婴儿数</td>
			<td width=16.5%>
				<input type="text" name="leg${item.routeIndex}GpxnI" id="leg${item.routeIndex}GpxnI" value="${entity[yes]}" class="layui-input" lay-verify="numberByCus">
			</td>
		</tr>
		<tr>
			<td>货物重量(kg)</td>
			<td>
				<input type="text" name="leg${item.routeIndex}HwWeight" id="leg${item.routeIndex}HwWeight" value="${entity[hw]}" class="layui-input" lay-verify="numberByCus">
			</td>
			<td>邮件重量(kg)</td>
			<td>
				<input type="text" name="leg${item.routeIndex}MailWeight" id="leg${item.routeIndex}MailWeight" value="${entity[yj]}" class="layui-input" lay-verify="numberByCus">
			</td>
			<td>行李重量(kg)</td>
			<td>
				<input type="text" name="leg${item.routeIndex}BagnWeight" id="leg${item.routeIndex}BagnWeight" value="${entity[xl]}" class="layui-input" lay-verify="numberByCus">
			</td>
		</tr>
	</table>
         </c:forEach>
         <div id="hiddenDiv">
		<table class="layui-table">
		<tr>
			<td width=16.5%>创建人</td>
			<td width=16.5%>
					<input type="text"  value="${entity.creatorName}" class="layui-input" readonly="readonly"/>
			</td>
			<td width=16.5%>创建时间</td>
			<td width=16.5%>
				<input  type="text"  value="${entity.createTm}" class="layui-input" readonly="readonly"/>
			</td>
			<td width=16.5%>修改人</td>
			<td width=16.5%>
					<input type="text"  value="${entity.updatorName}" class="layui-input" readonly="readonly"/>
			</td>
		</tr>
		</table>
		</div>
		<input type="hidden" name="type" id="type" value="${type}" class="layui-input">
		<input type="hidden" name="id" id="id" value="${entity.id}" class="layui-input">
		<input type="hidden" name="inFltid" id="inFltid" value="${inFltId}" class="layui-input">
		<input type="hidden" name="outFltid" id="outFltid" value="${outFltId}" class="layui-input">
		<button id="submitBtn" class="layui-btn" lay-submit lay-filter="save" style="display: none"></button>
	</form>
</body>
</html>