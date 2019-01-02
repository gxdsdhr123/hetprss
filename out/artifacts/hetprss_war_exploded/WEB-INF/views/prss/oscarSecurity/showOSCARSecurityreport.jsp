<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>OSCAR保障报告</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<style type="text/css">
html,body{
	width:100% !important;
	height:100% !important;
	overflow:auto;
}



.margin-r {
    margin-right: 6%;
}
.layui-table {
    width: 91%;
}
/* .td-w { */
/* 	width: 13%; */
/* } */
.td-weight {
	font-weight: bold;
}
.layui-table td { 
    text-align: center;
}
.td-27 {
	width: 27%;
}
.td-45 {
	width: 45%;
}
.td-h {
	height: 80px;
}
.td-h span {
	font-size: 2.5rem;
}
#container {
	position: relative;
	margin: 0px auto;
	padding: 0px;
	width: 100%;
	height: 100%;
	overflow-y: auto;
}
</style>
</head>
<body>
<div id="container">
	<form id="abnormalForm" class="layui-form" action="" method="post">
		<!-- 航班id -->
		<input type="hidden" name="fltid" value="${FLTID }" id="FLTID" />
		<input type="hidden" name="fltid" value="${SHOWID }" id="FLTID" />

	<!-- 表格区域 -->
	<div  style="margin-left: 5%; position: relative;">
		<table class="layui-table" >
			<tr>
				<td class="td-h" colspan="7"><span>OSCAR保障报告</span></td>
			</tr>
			<tr>
				<td class="td-w td-weight" colspan="2">日期</td>
				<td><span>${FLIGHTDATE }</span></td>
				<td class="td-w td-weight">进港航班号</td>
				<td><span>${INFLIGHTNUMBER }</span></td>
				<td class="td-w td-weight">出港航班号</td>
				<td><span>${OUTFLIGHTNUMBER }</span></td>
			</tr>
			<tr>
				<td class="td-w td-weight" colspan="2">机位</td>
					<td><span>${ACTSTANDCODE }</span></td>
				<td class="td-w td-weight">机号</td>
				<td><span>${AIRCRAFTNUMBER }</span></td>
				<td class="td-w td-weight">机型</td>
				<td><span>${ACTTYPECODE }</span></td>
			</tr>
			<tr>
				<td class="td-w td-weight" colspan="2">入位</td>
				<td><span>${STANDTM }</span></td>
				<td class="td-w td-weight">离位</td>
				<td><span>${RELSSTANDTM }</span></td>
				<td class="td-w td-weight">客舱关门</td>
				<td><span>${HTCHCLOTM }</span></td>
			</tr>
			<tr>
				<td class="td-w td-weight" colspan="2">机组允许登机</td>
				<td><span>${ZKTOSCARBZOUTARRIVE1FTM }</span></td>
				<td class="td-w td-weight">  进港OSCAR </td>
				<td><span>${INOSCARNAME }</span></td>
				<td class="td-w td-weight">出港OSCAR</td>
				<td><span>${OUTOSCARNAME }</span></td>
			</tr>
			<!-- 检查项表头 -->
			<tr>
				<td class="td-w td-weight" colspan="2">序号</td>
				<td class="td-w td-weight" colspan="3">检查项目</td>
				<td class="td-w td-weight">是/否符合</td>
				<td class="td-w td-weight">操作时间</td>
			</tr>
			<!-- 检查项 -->
			<c:if test="${itemList != null }">
				<c:forEach items="${itemList }" var="item" varStatus="status">
					<c:if test="${item.ITEMNAME != \"0\"}">
						<tr>
							<td colspan="2"><span>${status.count }</span></td>
							<td colspan="3"><span>${item.ITEMNAME }</span></td>
							<td><span>${item.ITEMVAL }</span></td>
							<td><span>${item.CREATEDATE }</span></td>
						</tr>
					</c:if>
					<c:if test="${item.ITEMNAME == \"0\"}">
						<tr>
							<td colspan="2"><span>1</span></td>
							<td colspan="3"><span></span></td>
							<td><span></span></td>
							<td><span></span></td>
						</tr>
					</c:if>
				</c:forEach>
			</c:if>
			
			
			<!-- 备注区域 -->
			<tr>
				<td class="td-w td-weight" colspan="7">备注</td>
			</tr>
			<c:if test="${remarkList != null }">			
				<c:forEach items="${remarkList }" var="remark" varStatus="status">
						<tr>
							<td><span>${status.count }</span></td>

							<td colspan="5">
								<textarea style="border: 0;overflow: auto; width:50%; height: 40px; background-color: #002F63;" disabled>${remark.REMARKTEXT }</textarea>
													
							<c:if test="${remark.FILETYPE == 1 }">
								<i class="fa fa-photo">&nbsp;<i/>
								<a href="javascript:void(0);" onclick="picOrAudio('${remark.REMARKID }', '${remark.FILETYPE }');">查看</a>
							</c:if>
							<c:if test="${remark.FILETYPE == 2 }">						
								<i class="fa fa-volume-up">&nbsp;<i/>
								<a href="javascript:void(0);" onclick="picOrAudio('${remark.REMARKID }', '${remark.FILETYPE }');">播放</a>
							</c:if>
							<c:if test="${remark.FILETYPE == 3 }">						
								<i class="fa fa-video-camera">&nbsp;<i/>
								<a href="javascript:void(0);" onclick="picOrAudio('${remark.REMARKID }', '${remark.FILETYPE }');">播放</a>
							</c:if>
							<c:if test="${remark.FILETYPE == 9 }">						
								<i class="fa fa-briefcase">&nbsp;<i/>
								<a href="javascript:void(0);" onclick="picOrAudio('${remark.REMARKID }', '9');">查看</a>
							</c:if>
							</td>
							<td ><span>${remark.CREATEDATE }</span></td>
						</tr>
				</c:forEach>
			</c:if>
			<c:if test="${empty remarkList }">		
				<tr>
					<td>1</td>
					<td colspan="5"></td>
					<td ></td>
				</tr>
			</c:if>
			
			
		</table>
	</div>
	
	</form>
</div>
<script>
	new PerfectScrollbar('#container');
</script>
	<script type="text/javascript" src="${ctxStatic}/prss/oscarSecurity/showOSCARSecurityreport.js"></script>

</body>
</html>