<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<title>${flag=='add'?"新增":"修改" }发送地址范围</title>
</head>
<body>
	<form id="createFormSender" class="layui-form" lay-filter="filter">
		<input type="hidden" name="flagsr" id="flagsr" value="0"/>
		<input type="hidden" name="ids" id="ids" value="${ids }"/>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">类型</label>
				<div class="layui-input-inline">
					<c:if test="${tableName == 'sendList' }">
						<select lay-filter="sFilter" name="sitaType" id="sitaType" >
							<option value="1" ${ sitaType == 1?'selected':''}>SITA</option>
							<option value="2" ${ sitaType == 2?'selected':''}>邮件</option>
						</select>
					</c:if>
					<c:if test="${tableName != 'sendList' }">
						<select name="sitaType" id="sitaType" ${ tableName == 'sendList'?'':'disabled'}>
							<option value="1" ${ senderSitaType == 1?'selected':''}>SITA</option>
							<option value="2" ${ senderSitaType == 2?'selected':''}>邮件</option>
						</select>
					</c:if>
				</div>
			</div>

			<div class="layui-inline">
				<label class="layui-form-label">航空公司</label>
				<div class="layui-input-inline">
					<input type="hidden" name="tg_site_id" id="tg_site_id"
						value="${tg_site_id }" /> <input id="tg_site_name"
						name="tg_site_name" class="layui-input" type="text"
						value="${tg_site_name }" onclick="senderList()" contenteditable="false">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">地址</label>
				<div class="layui-input-inline">
					<input id="tg_address" disabled="disabled"
						name="tg_address" class="layui-input" type="text"
						value="${tg_address }">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">是否触发处理</label>
				<div class="layui-input-inline">
					 <input name="ifprocfrom" type="checkbox" id="ifprocfrom" ${ifprocfrom==1?'checked':''} 
					   lay-filter="procFilter">
				</div>
			</div>

			<div class="layui-inline">
				<label class="layui-form-label">触发类</label>
				<div class="layui-input-inline">
				 	<select name="proceclsfrom" id="proceclsfrom" lay-filter="proceclsfrom" 
				 		class="layui-disabled"  ${vo.ifprocfrom==1?'':'disabled'}>
				  		<option value="all" >请选择</option>
                      	<c:forEach items="${listPS}" var="listPS">
				  			<option value="${listPS.code }" ${proceclsfrom==listPS.code?'selected':''}>${listPS.name }</option>
					  	</c:forEach>
					</select>
				</div>
		    </div>
			<div class="layui-inline">
				<label class="layui-form-label">触发参数</label>
				<div class="layui-input-inline">
					<input id="procdefparamfrom" name="procdefparamfrom" ${vo.ifprocfrom==1?'':'disabled'}
						class="layui-input" type="text" value="${procdefparamfrom }"
						style="width: 190px">
				</div>
			</div>
		</div>
	</form>	
 	<script type="text/javascript" src="${ctxStatic}/prss/telegraph/sendersite.js"></script> 
	<script type="text/javascript" src="${ctxStatic}/prss/message/ChkUtil.js"></script>
</body>
</html>