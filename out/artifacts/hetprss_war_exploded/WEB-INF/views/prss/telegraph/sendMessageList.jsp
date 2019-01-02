<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>发送报文</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/head.jsp"%>
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css"
	rel="stylesheet" />
<link href="${ctxStatic}/prss/common/css/param.css" rel="stylesheet" />
<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	var templId = '${auto.TG_TEMPL_ID}';
</script>

</head>
<body>
	<div class="content">
		<form id="createForm" class="form-horizontal"
			action="${ctx}/telegraph/auto/saveTime" enctype="multipart/form-data"
			method="post">
			<input type="hidden" id="msgtext" name="msgtext"> <input
				type="hidden" id="fltid" name="fltid" value="${fltInfo.FLTID}">
			<input type="hidden" name="manualId" value="${auto.ID }"/>
			<input type="hidden" name="tgSiteType" value="${auto.TG_SITE_TYPE }"/>

			<div class="row">
				<div class="col-sm-12">
					<div class="form-group">
						<label for="inputEmail3" class="col-sm-1 control-label">航班号</label>

						<div class="col-sm-2">
							<input class="form-control" id="fltno" name="fltno" type="text"
								value="${fltInfo.FLIGHT_NUMBER}" readonly="readonly">
						</div>
						<label for="inputEmail3" class="col-sm-1 control-label">航班日期</label>

						<div class="col-sm-2">
							<input class="form-control" id="fltdate" name="fltdate" value="${fltdate }"
								readonly="readonly"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" type="text">
						</div>

						<label for="inputEmail3" class="col-sm-1 control-label">报文模板</label>

						<div class="col-sm-2">
								<select path="mtemplid" id="mtemplid" name="mtemplid" lay-verify="required"
									lay-search="" class="form-control" lay-filter="templFilter">
									<option value="">请选择</option>
									<c:forEach items="${templateList }" var="item">
										<option value="${item.ID }" ${item.ID == auto.TG_TEMPL_ID?'selected':'' }>${item.TG_NAME }</option>
									</c:forEach>
								</select>
						</div>
					</div>
					<div class="form-group">
						<label for="inputEmail3" class="col-sm-1 control-label">报文类型</label>
						<div class="col-sm-2">
							<input class="form-control" id="tempType" type="text" value="${auto.TG_CODE }"
								readonly="readonly">
						</div>
						<label class="col-sm-1 control-label">优先级</label>
						<div class="col-sm-2">
							<select name="priority" class="form-control" id="priority">
								<option value="">请选择</option>
								<c:forEach items="${priorityList}" var="var">
									<c:if test="${vo.PRIORITY==null or vo.PRIORITY=='' }">
										<option value="${var.ID }"
											${var.NAME=='QD'?'selected':'' }>${var.NAME }</option>
									</c:if>
									<c:if test="${vo.PRIORITY!=null and vo.PRIORITY!='' }">
										<input type="text" value="${vo.PRIORITY==null or vo.PRIORITY=='' }" />
										<option value="${var.ID }"
											${var.ID==vo.PRIORITY?'selected':'' }>${var.NAME }</option>
									</c:if>
								</c:forEach>
							</select>
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
								style="height: 300px;">${auto.TG_TEXT}</textarea>
<!-- 							<div contenteditable="true" id="mtext" name="mtext"  -->
<%-- 								style="overflow: auto;height: 300px;" class="layui-textarea">${fn:escapeXml(auto.TG_TEXT) }</div> --%>
						</div>
						<a class="col-sm-1 control-label" onclick="openParmsForm();" href="javascript:void(0)" 
							style="cursor: pointer;display: none;">【参数】</a>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="col-sm-12">
					<div class="form-group sita">
						<label class="col-sm-1 control-label" id="addressName">${auto.TG_SITE_TYPE==2?'邮件地址':'SITA地址' }
							</label>
						<div class="col-sm-5">
							<input class="form-control" name="address" id="address" type="text" value="${auto.ADDRESS }">
						</div>
						<label class="col-sm-2 control-label" style="color: red;text-align: left;">
							多个地址请使用“;”分隔
						</label>
					</div>
				</div>
			</div>

			<div class="row">
				<h4 class="text-center">
					<strong>自动发送设置${(auto!=null)?(auto.SEND_STATUS==2?"【已停止】":"【发送】"):"" }</strong>
				</h4>
			</div>
			<div class="row">
				<div class="col-sm-12">
					<div class="form-group">
						<label for="inputEmail3" class="col-sm-1 control-label">发送时间</label>
						<div class="col-sm-2">
							<input class="form-control" id="beginTime" readonly="readonly" value="${auto.SEND_TIME }" name="beginTime"
								onclick="WdatePicker({startDate:'%yyyy-%MM-%dd 00:00:00',dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endTime\')}'})"
								type="text">
						</div>
						<label for="inputEmail3" class="col-sm-1 control-label">循环时间</label>

						<div class="col-sm-2">
<!-- 							<input class="form-control" id="recycleTime" type="text"> -->
							<select name="recycleTime" class="form-control" id="recycleTime">
								<option value="15" ${auto.WHILE_TIME==15?"selected":"" }>15分钟</option>
								<option value="30" ${auto.WHILE_TIME==30?"selected":"" }>30分钟</option>
							</select>
						</div>

						<label for="inputEmail3" class="col-sm-1 control-label">剩余时间</label>
						<div class="col-sm-2">
							<input class="form-control" id="leftTime" type="text"
								readonly="readonly">
						</div>
						<label for="inputEmail3" class="col-sm-1 control-label">停止时间</label>
						<div class="col-sm-2">
							<input class="form-control" id="endTime" name="endTime" value="${auto.STOP_TIME }"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'beginTime\')}'})"
								type="text">
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12">
					<div class="form-group">
						<label for="inputEmail3" class="col-sm-1 control-label">发送方式</label>
						<div class="col-sm-5">
							<div class="form-group">
								<div class="radio">
									<label> <input name="sendType" id="optionsRadios1" ${auto.TG_SEND_TYPE==0?"checked":"" }
										value="0"  type="radio"><strong>
											自动发送</strong>
									</label>
									<div class="col-sm-1"></div>
									<label> <input name="sendType" id="optionsRadios2" ${auto.TG_SEND_TYPE!=0?"checked":"" }
										value="1" type="radio"><strong> 弹出提醒人工发送</strong>
									</label>
								</div>
							</div>
						</div>
						<label for="inputEmail3" class="col-sm-1 control-label">停止条件</label>
						<div class="col-sm-5">
							<div class="checkbox">
								<label> <input type="checkbox" value="1" ${auto.STOP_CONDITION==1?"checked":"" }
									name="stopcondition" id="stopcondition"><strong>
										飞机起飞</strong>
								</label>
							</div>
						</div>
					</div>
				</div>
			</div>

			<input type="hidden" id="fltid" name=fltid> <input type="hidden"
				id="teleText" name=teleText> <input type="hidden" id="submitType"
				name="submitType"> <input type="hidden" id="flag" value="add"
				name="flag"> <input type="hidden" id="varcols" name="varcols"> <input
				type="hidden" id="airline_code" name="airline_code">


			<div class="row text-center" id="btn-group">
				<div class="col-sm-4"></div>
				<div class="col-sm-1">
					<button type="button" class="layui-btn layui-btn-primary"
						onclick="formSubmit();">保存</button>
				</div>
				<div class="col-sm-1">
					<button type="button" onclick="clear();"
						 class="layui-btn layui-btn-primary">重置</button>
				</div>
				<div class="col-sm-1">
					<button type="button"  class="layui-btn layui-btn-primary"
						onclick="terminal();">终止</button>
				</div>
				<div class="col-sm-4"></div>
			</div>

		</form>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/common/params.js"></script>
	<script type="text/javascript"
		src="${ctxStatic}/prss/telegraph/sendMessageList.js"></script>

</body>
</html>