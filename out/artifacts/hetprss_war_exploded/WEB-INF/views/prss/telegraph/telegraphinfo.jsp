<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/prss/common/css/param.css" rel="stylesheet" />
<title>${flag=='add'?"新增":"修改" }模板</title>
</head>

<body>
	<form id="createForm" class="layui-form"
		action="${ctx}/message/type/save">
		<input type="hidden" name="flag" id="flag" value="${flag }" /> <input
			type="hidden" name="id" id="id" value="${id}" /> <input
			type="hidden" name="typeId" id="typeId" value="${vo.ALN_2CODE }" />
			<input type="hidden" id="schema" name="schema" value="99"/>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">报文名称</label>
				<div class="layui-input-inline">
					<input name="tg_name" class="layui-input" type="text"
						value="${vo.TG_NAME}">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">报文类型</label>
				<div class="layui-input-inline">
					<select name="tgType">
						<c:forEach items="${typeList}" var="var">
							<option value="${var.ID }"
								${var.ID==vo.TG_TYPE_ID?'selected':'' }>${var.TG_CODE }</option>
						</c:forEach>
					</select>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			
			<div class="layui-inline">
				<label class="layui-form-label">优先级</label>
				<div class="layui-input-inline">
					<select name="priority">
						<c:forEach items="${priorityList}" var="var">
							<option value="${var.ID }"
								${var.ID==vo.PRIORITY?'selected':'' }>${var.NAME }</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">进出港类型</label>
				<div class="layui-input-inline">
					<select name="fiotype" >
						<option value="A0" ${vo.FIO_TYPE=="A0"?'selected':'' }>进港</option>
						<option value="D0" ${vo.FIO_TYPE=="D0"?'selected':'' }>出港</option>
					</select>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">模板关联事件</label>
				<div class="layui-input-inline">
					<input id="eventid" name="eventid" type="hidden" value="${vo.EVENT_ID}">
					<input id="eventname" name="eventname" class="layui-input" type="text" value="${vo.EVENT_NAME}"
					 onclick="searchEvent()" contenteditable="false">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">解析使用</label>
				<div class="layui-input-inline">
					<input name="ifanalysis"  type="checkbox" ${vo.IFANALYSIS==1?'checked':''}>
				</div>
			</div>
		</div>
		<div class="layui-form-item  layui-form-text">
			<div class="layui-inline">
				<div class="layui-block" style="width: 600px;">
					<label class="layui-form-label">报文正文</label>
					<div class="layui-input-block" style="margin-left: 120px;">
						<input type="hidden" id="old_varcols" value="${vo.VARCOLS}"/>
						<input type="hidden" id="old_mtext" value="${vo.TG_TEXT }"/>
						
						<input id="varcols" name="varcols"  type="hidden" value="${vo.VARCOLS}">
						<div contenteditable="true" id="mtext"  class="layui-textarea" 
						style="width: 100%;overflow-wrap: break-word;">&nbsp;${vo.TG_TEXT  }</div>
					</div>
					</div>
				</div>
			<div class="layui-inline">
				<a id="varList" href="javascript:void(0)" onclick="paramList()" class="an-h">【参数】</a>   
			</div>
		</div>
		<c:if test="${flag != 'add'}">
			<div class="layui-form-item">
				<div class="layui-inline">
					<div class="layui-form-mid">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;创建人</div>
					<div class="layui-input-inline">
						<input name="createuser" class="layui-input" type="text"
							value="${vo.CREATEUSER }" disabled="disabled">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">创建时间</label>
					<div class="layui-input-inline">
						<input name="createtime" class="layui-input" type="text"
							value="${vo.CREATETIME }" disabled="disabled">
					</div>
				</div>
			</div>
		</c:if>
	</form>

	<ul id="myTab" class="nav nav-tabs">
		<li class="active"><a href="#home" data-toggle="tab">发送地址</a></li>
		<li><a href="#ios" data-toggle="tab">接收地址</a></li>
	</ul>
	<div id="myTabContent" class="tab-content">
		<div class="tab-pane fade in active" id="home">
		    <button id="addPage" type="button" class="btn btn-link">增加</button>
			<button id="updatePage" type="button" class="btn btn-link">修改</button>
			<button id="delSender" type="button" class="btn btn-link">删除</button>
			<div id="sendLists">
				<table id="sendList"></table>
		    </div>
		</div>
		<div class="tab-pane fade" id="ios">
			<button id="addPageOther" type="button" class="btn btn-link">增加</button>
			<button id="updatePageOther" type="button" class="btn btn-link">修改</button>
			<button id="delReveiver" type="button" class="btn btn-link">删除</button>
			<div id="receiveLists">
				<table id="receiveList"></table>
			</div>
		</div>
	</div>
	<script type="text/javascript"
		src="${ctxStatic}/prss/telegraph/telegraphinfo.js"></script>
	<script type="text/javascript" src="${ctxStatic}/prss/common/params.js"></script>
	<script type="text/javascript"
		src="${ctxStatic}/prss/message/ChkUtil.js"></script>
</body>
</html>