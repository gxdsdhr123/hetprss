<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<%-- <%@include file="/WEB-INF/views/include/treeview.jsp"%> --%>
<%@include file="/WEB-INF/views/include/head.jsp"%>
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<script src="${ctxStatic}/prss/message/message_main.js" type="text/javascript" ></script>
<script src="${ctxStatic}/prss/message/talk.js" type="text/javascript" ></script>
<link href="${ctxStatic}/prss/message/css/message_main.css" type="text/css" rel="stylesheet"/>

<title>消息接收发送</title>
<script type="text/javascript">
</script>
<style type="text/css">

</style>
</head>

<body>
	<span id="connstate" style="font-weight:bold;color:red"></span>
  	<div id="msgs"></div>
	<input type="hidden" value="${fns:getConfig('cp.server')}" id="server">
	<input type="hidden" value="${fns:getConfig('cp.appId')}" id="appkey"/>
	<input type="hidden" value="P_${fns:getUser().id}" id="uid">
	<input type="hidden" id="defaultSubs" value="${defaultSubs }">
	<div class="row" style="margin: 0px;" id="main">
		<div class="col-md-8 col-xs-8 col-sm-8 height "
			style="padding: 0px;display: none;" id ="templ-right">
			<div class="box box-widget height">
				<div class="layui-tab  layui-tab-card" style="height: 70%; margin: 0px;" lay-filter="demo" >
					<i class="glyphicon glyphicon-transfer" onclick="show(4)" id="show"
					style="cursor: pointer;z-index: 99;font-size:18px;color: white;padding: 5px;float: right;margin: 5px 10px 5px 5px;"></i>
					<ul class="layui-tab-title" style="margin: 0px;">
						<li lay-id="EVENT" class="layui-this" onclick="show(1,'EVENT');">待办事件</li>
						<li lay-id="SUBS" onclick="show(1,'SUBS');">待办订阅</li>
						<li lay-id="WARN" onclick="show(1,'WARN');">待办预警</li>
						<li lay-id="FEEDBACK" onclick="show(1);">待反馈</li>
						<li lay-id="11" onclick="show(1);">消息模板</li>
					</ul>
					<div class="layui-tab-content">
						<div class="layui-tab-item EVENT_list layui-show" style="height: 100%;overflow-y:auto">
						</div>
						<div class="layui-tab-item SUBS_list " style="height: 100%;overflow-y:auto">
						</div>
						<div class="layui-tab-item WARN_list " style="height: 100%;overflow-y:auto">
						</div>
						<div class="layui-tab-item sendmessage" style="height: 100%;overflow-y:auto">
						</div>
						<div class="layui-tab-item " style="padding: 5px;height: 100% !important;">
							<div  class="query_mess_div">
								查询模板： <input type="text"  class="query_mess"/>
							</div>
							<div class="templateList">
							</div>
						</div>
					</div>
				</div>
				
				<div class="layui-tab  layui-tab-card" style="height: 30%; margin: 0px;" lay-filter="demo" >
					<div class="layui-tab-content">
							<form id="fileList" action="${ctx}/message/common/doSend" method="post" enctype="multipart/form-data">
								<input type="hidden" value="${flag }" id="flag" name="flag"/>
								<input type="hidden" value="${fltid }" id="fltid" name="fltid"/>
								<input type="hidden" value="${oth_fltid }" id="oth_fltid" name="oth_fltid"/>
								<input type="hidden" value="${fiotype }" id="fiotype" name="fiotype"/>
								<input type="hidden" value="${flightDate }" id="flightDate" name="flightDate"/>
								<input type="hidden" value="${flightNumber }" id="flightNumber" name="flightNumber" />
								<input type="hidden" value="${tid }" name="tid" id="tid"/>
								<input type="hidden" name="toListStr" id="toListStr"/>
								<input type="hidden" name="transsubId" id="transsubId"/>
								<input type="hidden" name="oldmid" id="oldmid"/>
							<textarea rows="3" cols="23" class="mtext" id="mtext" name="mtext"></textarea> 
							<div style="text-align: right;width: 96%"> 需要回复<input	type="checkbox" id="ifreply" name="ifreply"/>
								<button type="button" class="layui-btn layui-btn-small layui-btn-normal messagefile">上传附件</button>
								<button type="button" class="layui-btn layui-btn-small layui-btn-normal send" style="margin: 0px;">发送消息</button>
							</div>
								<div id="messFile" style="display: none;">
									<input type="hidden" name="filenum" id="filenum"/>
									<input id="fileInput" name="file" style="display:none" onchange="fileOnChange()" type="file" multiple="multiple">
										<table class="layui-table">
									      <thead>
									        <tr>
									          <th>文件名</th>
									          <th>大小</th>
									          <th>操作</th>
									        </tr>
									      </thead>
									      <tbody id="fileTable"></tbody>
									    </table>
								    <input id="delFile" name="delFile" style="display:none"/>
							    </div>
							</form>
					</div>
				</div>
			</div>
		</div>
		
		<div class="col-md-4 col-xs-4 col-sm-4 height " 
			style="padding: 0px;" id="to-left">
			<div class="box box-widget height" id="messRecipient">
				<div class="box-header height-top" style="height: 42px;">
						<b>接收人角色</b>
						<i class="glyphicon glyphicon-transfer" onclick="show(3)" style="cursor: pointer;float: right;"></i>
				</div>
				<div class="box-body height-90 padding" id="template_to">
				</div>
			</div>
		</div>
	</div>
	
	<div id="messLoadFile" style="display: none;">
		<table class="layui-table">
			<thead>
				<tr>
					<th>文件名</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody id="fileLoadTable"></tbody>
		</table>
	</div>
</body>
</html>
