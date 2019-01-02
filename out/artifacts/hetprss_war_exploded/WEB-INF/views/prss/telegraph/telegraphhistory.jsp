<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>报文历史消息</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css"
	rel="stylesheet" />
<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
	<div class="row" style="padding-top: 10px;">
		<input type="hidden" name="isHis" value="${isHis }" id="isHis"/>
		<input type="hidden" name="fltId" value="${fltId }" id="fltId"/>
		<c:if test="${fltId eq null or fltId eq '' }">
			<div class="col-md-12">
				<div class="layui-inline">
					<label class="layui-form-label">航班日期</label>
					<div class="layui-input-inline">
						<input type='text' maxlength="20" name='mflightdate'
							placeholder='请选择日期' class='form-control'
							onclick="WdatePicker({dateFmt:'yyyyMMdd'});" />
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">航班号</label>
					<div class="layui-input-inline">
						<input class="form-control" type="text" name="flightnumber" placeholder="航班号">
					</div>
				</div>
	
				<div class="layui-inline">
					<label class="layui-form-label">${isHis==2?'接收时间':'发送时间' } 从</label>
					<div class="layui-input-inline">
						<input class="form-control" id="beginTime" readonly="readonly"
							value="${auto.SEND_TIME }" name="beginTime" placeholder='开始时间'
							onclick="WdatePicker({startDate:'%yyyy-%MM-%dd 00:00',dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\'endTime\')}'})"
							type="text">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">到</label>
					<div class="layui-input-inline">
						<input class="form-control" id="endTime" name="endTime"
							value="${auto.STOP_TIME }" placeholder='结束时间'
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\'beginTime\')}'})"
							type="text">
					</div>
				</div>
			</div>
		</c:if>
		<div class="col-md-12" >
			<div class="layui-inline">
				<label class="layui-form-label">标识</label> <select
					name="isfavoriter" id="isfavoriter" class="form-control"
					style="width: 170px;">
					<option value="">全部</option>
					<option value="1">标记</option>
					<option value="0">未归档</option>
				</select>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">状态</label> <select
					name="status" id="status" class="form-control"
					style="width: 170px;">
					<option value="">全部</option>
					<option value="1">已读</option>
					<option value="0">未读</option>
				</select>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">报文类型</label>
				<div class="layui-input-inline">
					<select class="form-control select2 telegraphType"
						data-type="telegraphType" name="telegraphType" id="telegraphType"
						multiple="multiple">
						<c:forEach items="${telegraphTypeList}" var="var">
							<option value="${var.CODE}">${var.DESCRIPTION}</option>
						</c:forEach>
					</select>
				</div>
			</div>
		</div>
		<div class="col-md-12">
			<c:if test="${isHis==1 or isHis ==4 }">
				<div class="layui-inline">
					<label class="layui-form-label">已收/已发</label> 
					<div class="layui-input-inline">
						<select
							name="sendorrecieve" id="sendorrecieve" style="width: 170px;" class="form-control" >
							<option value="">全部</option>
							<option value="1">已收</option>
							<option value="2">已发</option>
						</select>
					</div>
				</div>
			</c:if>
			<c:if test="${fltId eq null or fltId eq '' }">
				<div class="layui-inline">
					<label class="layui-form-label">航空公司分组</label>
					<div class="layui-input-inline">
						<select name="unioncode" id="unioncode" class="form-control" 
							style="width: 170px;">
							<option value="">全部</option>
							<option value="1">东航</option>
							<option value="2">南航</option>
							<option value="3">海航</option>
							<option value="4">BGS外航</option>
						</select>
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">航空公司</label>
					<div class="layui-input-inline">
						<select class="form-control select2 airplane"
							data-type="airplane" name="airplane" id="airplane"
							multiple="multiple">
							<c:forEach items="${airlineList}" var="var">
								<option value="${var.CODE}">${var.DESCRIPTION}</option>
							</c:forEach>
						</select>
					</div>
				</div>
			</c:if>
		</div>
		<div class="col-md-12">
			<div class="bs-bars pull-right">
				<div id="toolbar">
					<button type="button" class="btn btn-link  search">查询</button>
					<c:if test="${isHis ==2 }">
						<button type="button" class="btn btn-link  send">发送</button>
					</c:if>
					<button type="button" class="btn btn-link  print">打印</button>
					<button type="button" class="btn btn-link  export">导出</button>
				</div>
			</div>
		</div>
		<table id="baseTable"
			class="table table-striped table-bordered table-hover"></table>
	</div>
	<div id="detail" style="display: none;">
		<div class="row">
			<div class="col-md-12 col-xs-12 col-sm-12">
				<div class="box-body">
					<div class="layui-inline">
						<label class="layui-form-label">航班日期</label>
						<div class="layui-input-inline">
							<input type='text' name='fd' class="layui-input" />
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">报文类型</label>
						<div class="layui-input-inline">
							<input class="layui-input" type="text" name="tt">
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">航班号</label>
						<div class="layui-input-inline">
							<input class="layui-input" type="text" name="fn">
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">报文时间</label>
						<div class="layui-input-inline">
							<input class="layui-input" type="text" name="st">
						</div>
					</div>
				</div>
				<div class="box-body">
					<div class="layui-inline">
						<label class="layui-form-label">接收时间</label>
						<div class="layui-input-inline">
							<input class="layui-input" type="text" name="at">
						</div>
					</div>
					
				</div>
				<div class="box-body">
					<div class="layui-inline">
						<label class="layui-form-label">报文原文</label>
						<div class="layui-input-inline">
							<textarea rows="" cols="159" class="layui-textarea" id="mtext" style="height: 300px;"></textarea>
						</div>
					</div>

				</div>
			</div>
		</div>
	</div>
	<form id="exportForm" action="${ctx}/telegraph/history/export" method="post" style="display: none">
			<input type="hidden" name="id" />
			<input type="hidden" name="isHis" value="${isHis }"/>
	</form>
	<script type="text/javascript"
		src="${ctxStatic}/prss/telegraph/telegraphhistory.js"></script>
</body>
</html>