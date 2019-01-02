<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp" %>
<link href="${ctxStatic}/prss/common/css/param.css" rel="stylesheet" />

<title>${flag=='add'?"新增":"修改" }消息订阅</title>
</head>

<body>
	<form id="createForm" class="layui-form" action="${ctx}/telegraph/auto/save">
		<input type="hidden" name="flagsr" id="flagsr" value="0"/>
		<input type="hidden" name="flag" id="flag" value="${flag }"/>
		<input type="hidden" name="id" id="id" value="${vo.ID }"/>
		<input type="hidden" name="ruleId" id="ruleId" value="${vo.RULE_ID }"/>
		<input type="hidden" id="schema" name="schema" value="99"/>
		<div class="layui-form-item  layui-form-text ">
			<div class="layui-inline">
				<label class="layui-form-label">触发类型</label>
				<div class="layui-input-inline" class="form-control select2 event">
					<select name="event" id="event">
						<option value="">请选择</option>
						<c:forEach items="${eventList}" var="item">
							<option value="${item.EVENT_ID }" ${vo.POCE_TYPE==item.EVENT_ID?'selected':'' }>${item.EVENT_NAME }</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">航空公司</label>
				<div class="layui-input-inline">
					<select name="airline" id="airline" class="form-control" data-type="airline" lay-verify="" lay-search>
						<option value="">请选择</option>
						<c:forEach items="${airlineList}" var="item">
							<option value="${item.AIRLINE_CODE }" ${vo.ALN_2CODE==item.AIRLINE_CODE?'selected':'' }>${item.DESCRIPTION_CN }</option>
						</c:forEach>
					</select>
				</div>
			</div>
		</div>

		<div class="layui-form-item  layui-form-text">
			<div class="layui-inline">
				<label class="layui-form-label">触发条件</label>
				<div class="layui-input-inline" style="width: 600px;">
					<input id="old_colids" type="hidden" value="${vo.COLIDS}">
					<input id="old_expression" type="hidden" value="${fn:escapeXml(vo.DRL_STR) }" > 
					<input id="old_drools" type="hidden" value="${fn:escapeXml(vo.RULE) }">
					<input id="old_condition" type="hidden" value="${vo.TEXT}">
					
					<input id="expression" name="expression" type="hidden" value="${fn:escapeXml(vo.DRL_STR) }">
					<input id="drools" name="drools" type="hidden" value="${fn:escapeXml(vo.RULE) }">
					<input id="colids" name="colids" type="hidden" value="${vo.COLIDS}">
					<textarea id="condition" name="condition" placeholder="请输入内容" disabled="disabled"
						class="layui-textarea" rows="2">${vo.TEXT}</textarea>
				</div>
				<div class="layui-inline">
					<a id="guide" href="javascript:void(0)" class="an-h">【向导】</a>
				</div>
			</div>
		</div>
		<div class="layui-form-item  layui-form-text">
			<div class="layui-inline">
				<label class="layui-form-label">模板名称</label>
				<div class="layui-input-inline">
					<input type="hidden" id="mtemplid" name="mtemplid"
						value="${vo.TG_TEMPL_ID}" /> <input id="mtemplname"
						name="mtemplname" class="layui-input" type="text" contenteditable="false"  
						value="${vo.TG_NAME }" onclick="showTempl()">
				</div>
			</div>
		</div>
		<div class="layui-form-item  layui-form-text">
			<div class="layui-inline">
				<label class="layui-form-label">报文正文</label>
				<div class="layui-input-inline" style="width: 600px;">
					<input type="hidden" id="old_varcols" value="${vo.VARCOLS}"/>
					<input type="hidden" id="old_mtext" value="${vo.TG_TEXT }"/>
				
					<input id="varcols" name="varcols" type="hidden"
						value="${vo.VARCOLS}">
					<div contenteditable="true" id="mtext" class="layui-textarea" style="overflow-wrap: break-word;">&nbsp;${vo.TG_TEXT }</div>
				</div>
				<div class="layui-inline">
					<a id="paramBtn" href="javascript:void(0)" onclick="paramList()"
						class="an-h">【参数】</a>
				</div>
			</div>
		</div>

		<c:if test="${flag=='upd'}">
			<div class="layui-form-item  layui-form-text">
				<div class="layui-inline">
					<label class="layui-form-label">创建人</label>
					<div class="layui-input-inline">
						 <input id="username" name="username"
							class="layui-input" type="text" value="${vo.NAME }"
							disabled="disabled">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">创建时间</label>
					<div class="layui-input-inline">
						<input id="crtime" name="crtime" class="layui-input" type="text"
							value="${vo.CREATETIME }" disabled="disabled">
					</div>
				</div>
			</div>
		</c:if>
		
		<div class="layui-form-item  layui-form-text">
			<div class="layui-inline">
				<label class="layui-form-label">发送方式</label>
				<div class="layui-input-inline">
					<input type="radio" name="sendType" value="0" title="自动发送" 
					${vo.TG_SEND_TYPE != '1'?'checked':'' }/>
					<input type="radio" name="sendType" value="1" title="弹出提醒手动发送" 
					${vo.TG_SEND_TYPE == '1'?'checked':'' }/>
				</div>
			</div>
		</div>
	</form>	
	
	<script type="text/javascript" src="${ctxStatic}/prss/telegraph/autoinfo.js"></script>
	<script type="text/javascript" src="${ctxStatic}/prss/common/params.js"></script>
	<script type="text/javascript" src="${ctxStatic}/prss/message/ChkUtil.js"></script>
</body>
</html>