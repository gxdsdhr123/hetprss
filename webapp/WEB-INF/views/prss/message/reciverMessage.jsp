<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp" %>
<title>接收人范围</title>
</head>
<body>
	<form id="createFormReciver" class="layui-form" lay-filter="filter">
	<input type="hidden" name="flagsr" id="flagsr" value="1"/>
<%-- 		<input type="hidden" name="flag" id="flag" value="${flag }"/> --%>
<%-- 		<input type="hidden" name="id" id="id" value="${id }"/> --%>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">接收者类型</label>
				<div class="layui-input-inline">
					<select lay-filter="rFilter" name="mtotype" id="mtotype">
						<option value="0" ${ vo.mtotype == 0?'selected':''}>个人</option>
						<option value="1" ${ vo.mtotype == 1?'selected':''}>角色</option>
						<option value="2" ${ vo.mtotype == 2?'selected':''}>部门</option>
						<option value="8" ${ vo.mtotype == 8?'selected':''}>作业人</option>


					</select>
				</div>
			</div>

			<div class="layui-inline texter" >
				<label class="layui-form-label" id="receiverName">${vo.mtotype==8?'保障作业类型':'接收者名称'}</label>
				<div class="layui-input-inline">
					<input type="hidden" name="mtoer" id="mtoer" value="${vo.mtoer }" />
					<input id="mtoername" name="mtoername" class="layui-input "
						type="text" value="${vo.mtoername }" onclick="senderList()">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">手机短信</label>
				<div class="layui-input-inline">
					<input name="ifsms" type="checkbox" id="ifsms"
						${vo.ifsms==1?'checked':''} >
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">是否触发处理</label>
				<div class="layui-input-inline">
					<input name="ifprocto" type="checkbox" id="ifprocto"  lay-filter="procFilter"
						${vo.ifprocto==1?'checked':''}>
				</div>
			</div>

			<div class="layui-inline">
				<label class="layui-form-label">处理类</label>
				<div class="layui-input-inline">
					<select name="proceclsto" id="proceclsto"  class="layui-disabled" ${vo.ifprocto==1?'':'disabled'}>
						<option value="all">请选择</option>
						<c:forEach items="${listPR}" var="listPR" varStatus="status">
							<option value="${listPR.code}"
								<c:if  test="${listPR.code==vo.proceclsto }"> selected="selected"</c:if>>${listPR.name }</option>

						</c:forEach>
					</select>
				</div>
			</div>

			<div class="layui-inline">
				<label class="layui-form-label">处理参数</label>
				<div class="layui-input-inline">
					<input id="procdefparamto" name="procdefparamto" ${vo.ifprocto==1?'':'disabled'}
						class="layui-input" type="text" value="${vo.procdefparamto }">
				</div>
			</div>

			<div class="layui-inline">
				<label class="layui-form-label">转发</label>
				<div class="layui-input-inline">
					<input name="iftrans" type="checkbox" id="iftrans" lay-filter="transFilter" 
						${vo.iftrans==1?'checked':''}>
				</div>
			</div>
		
		  <div class="layui-inline">
				<label class="layui-form-label">模板</label>
				<div class="layui-input-inline">
				 <input type="hidden" name="transtemplid" id="transtemplid" value="${vo.transtemplid }"/>
				<input id="transtemplname" name="transtemplname" class="layui-input" type="text" ${vo.iftrans==1?'':'disabled'}
				value="${vo.transtemplname }" onclick="mMessage()" style="width: 190px">
				</div>
		    </div>
	
		</div>
		<div class="layui-form-item  layui-form-text">
			<div class="layui-inline">
				<label class="layui-form-label">接收条件</label>
				<div class="layui-input-inline" style="width: 400px;">
					<input id="old_colids" type="hidden" value="${vo.colids}">
					<input id="old_drlStr" type="hidden" value="${vo.drlStr }" > 
					<input id="old_drools" type="hidden" value="${vo.drools }">
					<input id="old_condition" type="hidden" value="${vo.condition}">
					
					
					<input id="colids" name="colids" type="hidden" value="${vo.colids}">
					<input id="drlStr" name="drlStr" type="hidden" value="${vo.drlStr }" value="${vo.drlStr }"> 
					<input id="drools" name="drools" type="hidden" value="${vo.drools }">
					
					<textarea id="condition" name="condition" placeholder="请使用向导配置条件"
						disabled="disabled" class="layui-textarea" rows="2">${vo.condition}</textarea>
				</div>
				<div class="layui-inline">
					<a id="guide" href="javascript:void(0)" class="an-h"
						onclick="guide()">【向导】</a>
				</div>
			</div>
		</div>
	</form>
	<script type="text/javascript" src="${ctxStatic}/prss/message/ChkUtil.js"></script>
	<script type="text/javascript" src="${ctxStatic}/prss/common/params.js"></script>
	<script type="text/javascript" src="${ctxStatic}/prss/message/reciverMessage.js"></script> 
</body>
</html>