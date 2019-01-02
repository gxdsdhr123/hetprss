<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<title>${flag=='add'?"新增":"修改" }发送人范围</title>
</head>
<body>
	<form id="createFormSender" class="layui-form" lay-filter="filter">
	<input type="hidden" name="flagsr" id="flagsr" value="0"/>
		<div class="layui-form-item formDiv">
			<div class="layui-inline">
				<label class="layui-form-label">发送者类型</label>
				<div class="layui-input-inline">
					<select lay-filter="sFilter" name="mfromtype" id="mfromtype">
						<option value="0" ${ vo.mfromtype == 0?'selected':''}>个人</option>
						<option value="1" ${ vo.mfromtype == 1?'selected':''}>角色</option>
						<option value="2" ${ vo.mfromtype == 2?'selected':''}>部门</option>
						<option value="9" ${ vo.mfromtype == 9?'selected':''}>系统</option>
					</select>
				</div>

			</div>
			<div class="layui-inline">
				<label class="layui-form-label">发送者名称</label>
				<div class="layui-input-inline">
					<input type="hidden" name="mfromer" id="mfromer"
						value="${vo.mfromer }" /> <input id="mfromername"
						name="mfromername" class="layui-input" type="text"
						value="${vo.mfromername }" onclick="senderList()">
				</div>
			</div>

			<div class="layui-inline">
				<label class="layui-form-label">是否触发处理</label>
				<div class="layui-input-inline">
					<input id="ifprocfrom" name="ifprocfrom" type="checkbox"  lay-filter="procFilter"
						${vo.ifprocfrom==1?'checked':''}>
					<i class="layui-icon"  id="addProc" ${vo.ifprocfrom==1?'hidden':''}>&#xe654;</i>   
					<i class="layui-icon"  id="removeProc" <c:if test="${procList== null || fn:length(procList) <= 1}">style="display: none;" </c:if> >&#xe640;</i>   
				</div>
			</div>
			<c:if test="${flag=='upd' }">
				<c:forEach items="${procList }" var="item">
					<div class="layui-inline cloneProc1">
						<label class="layui-form-label">处理类</label>
						<div class="layui-input-inline ">
							<select name="proceclsfrom" class="layui-disabled" ${vo.ifprocfrom==1?'':'disabled'}>
								<option value="all">请选择</option>
								<c:forEach items="${listPS}" var="listPS" varStatus="status">
									<option value="${listPS.code}"
										<c:if  test="${listPS.code==item.proceclsfrom }"> selected="selected"</c:if>>${listPS.name }</option>
		
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="layui-inline cloneProc2">
						<label class="layui-form-label">处理参数</label>
						<div class="layui-input-inline">
							<input name="procdefparamfrom"
								class="layui-input" type="text" value="${item.procdefparamfrom }" ${vo.ifprocfrom==1?'':'disabled'}
								style="width: 190px">
						</div>
					</div>
				</c:forEach>
			</c:if>
			<c:if test="${flag != 'upd'}">
				<div class="layui-inline cloneProc1">
					<label class="layui-form-label">处理类</label>
					<div class="layui-input-inline ">
						<select name="proceclsfrom" class="layui-disabled" ${vo.ifprocfrom==1?'':'disabled'}>
							<option value="all">请选择</option>
							<c:forEach items="${listPS}" var="listPS" varStatus="status">
								<option value="${listPS.code}"
									<c:if  test="${listPS.code==vo.proceclsfrom }"> selected="selected"</c:if>>${listPS.name }</option>
	
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="layui-inline cloneProc2">
					<label class="layui-form-label">处理参数</label>
					<div class="layui-input-inline">
						<input name="procdefparamfrom"
							class="layui-input" type="text" value="${vo.procdefparamfrom }" ${vo.ifprocfrom==1?'':'disabled'}
							style="width: 190px">
					</div>
				</div>
			</c:if>
		</div>
	</form>	
 	<script type="text/javascript" src="${ctxStatic}/prss/message/senderMessage.js"></script> 
	<script type="text/javascript" src="${ctxStatic}/prss/message/ChkUtil.js"></script>
</body>
</html>