<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>补配规则分配</title>
	<meta name="decorator" content="default" />
	<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
	<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
	<link href="${ctxStatic}/prss/rule/css/taskAllotRuleSet.css" rel="stylesheet" />
	<script type="text/javascript">
		var defaultAtcactype=[];//注册号
		if('${defaultAtcactype}'!=''){
			<c:forEach items="${defaultAtcactype}" var="atcacType">
				defaultAtcactype.push("${atcacType}");
			</c:forEach>
		}
		
		var defaultFlightNumber=[];//航班号
		if('${defaultFlightNumber}'!=''){
			<c:forEach items="${defaultFlightNumber}" var="item">
				defaultFlightNumber.push("${item}");
			</c:forEach>
		}
	</script>
</head>
<body>
	<form class="layui-form" action="" id="createForm">
		<input id="id" name="id" type="hidden" value="${ruleInfo.id}" />
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">规则名称<font class="required">*</font></label>
				<div class="layui-input-inline">
					<input id="ruleName" name="ruleName" htmlEscape="false" required  
						placeholder="规则名称" class="layui-input" type="text"
						value="${ruleInfo.name}" />
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">描述</label>
				<div class="layui-input-inline">
					<input id="ruleDesc" name="ruleDesc" htmlEscape="false"
						placeholder="规则描述" class="layui-input" type="text"
						value="${ruleInfo.description}" />
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">是否生效</label>
				<div class="layui-input-inline">
					<select id="ruleIfValid" name="ruleIfValid">
						<option value="1" <c:if test="${ruleInfo.ifValid == 1}">selected</c:if>>是</option>
						<option value="0" <c:if test="${ruleInfo.ifValid == 0}">selected</c:if>>否</option>
					</select>
				</div>
			</div>
		</div>
		
		<div class="layui-form-item">
			
			<div class="layui-inline">
				<label class="layui-form-label">进出港<font class="required">*</font></label>
				<div class="layui-input-block">
					<c:forEach items="${inOutPortList}" var="inOutPort">
						<input type="checkbox" name="ruleExtInOutPort"  value="${inOutPort.code}" 
							${ruleExtend.inOutPort.indexOf(inOutPort.code)>-1||(ruleExtend.inOutPort==null&&inOutPort.code=='A0') ? 'checked' : ''} title="${inOutPort.desc}" />
					</c:forEach>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label" >补给要求<font class="required">*</font></label>
				<div class="layui-input-inline">
					<input id="demand" name="demand" htmlEscape="false" required  
						placeholder="补给要求" class="layui-input" type="text"
						value="${setUpVO.demand}" />
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label" >子分公司</label>
				<div class="layui-input-inline">
					<select name="branch" class="form-control branch">
						<option value='!'>请选择</option>
						<option value="北分" ${setUpVO.branch =='北分'?'selected':''}>北分</option>
<!-- 						<option value="贵州">贵分</option> -->
				</select>
				</div>
			</div>
		</div>
		
	</form>

	<div class="rows">
		<div class="col-md-12">
			<div class="layui-inline">
				<label class="layui-form-label" style="margin-left: 15px;">航班号</label>
				<div class="layui-input-inline">
					<input name="flightNumber" placeholder="请选择航班号" class="layui-input" type="text" readonly="readonly"
					onclick="openCheck('flightNumber')" style="width: 420px;">
				</div>
			</div>
		</div>
	</div>

	<div class="rows">
		<div class="col-md-12">
			<div class="layui-inline">
				<label class="layui-form-label" style="margin-left: 15px;">注册号</label>
				<div class="layui-input-inline">
					<input name="aircraft" placeholder="请选择注册号" class="layui-input" type="text" readonly="readonly"
					onclick="openCheck('aircraft')" style="width: 420px;">
				</div>
			</div>
		</div>
	</div>

	<div class="rows">
		<div class="col-md-12">
			<div class="layui-form-item  layui-form-text">
				<div class="layui-inline">
					<label class="layui-form-label"  style="margin-left:15px">扩展条件</label>
					<div class="layui-input-inline" style="width: 600px;">
						<input id="expression" name="expression" type="hidden"
							value="${ruleInfo.drlStr}"> 
						<input id="drools" name="drools" type="hidden" value='${fns:escapeHtml(ruleInfo.ruleExt)}' /> 
						<input id="colids" name="colids" type="hidden" value='${ruleInfo.colids}' />
						<textarea id="condition" name="condition" placeholder="请输入内容"
							disabled="disabled" class="layui-textarea" rows="2">${fns:escapeHtml(ruleInfo.text)}</textarea>
					</div>
					<div class="layui-inline">
						<a id="guide" href="javascript:void(0)" class="an-h">【配置扩展条件】</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<c:if test="${type =='update'}">
		<div class="rows">
			<div class="col-md-6">
				<div class="layui-inline">
					<label class="layui-form-label" style="margin-left:15px">创建人</label>
					<div class="layui-input-inline">
						<input id="creater" name="creater" htmlEscape="false" readonly="readonly"
							placeholder="创建人" class="layui-input" type="text"
							value="${ruleInfo.createUser}" />
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="layui-inline">
					<label class="layui-form-label">创建时间</label>
					<div class="layui-input-inline">
						<input id="createTime" name="createTime" htmlEscape="false"
							placeholder="创建时间" class="layui-input" type="text" readonly="readonly"
							value="${ruleInfo.createTime}" />
					</div>
				</div>
			</div>
		</div>

	</c:if>
	<script type="text/javascript" src="${ctxStatic}/prss/rule/setup/setUpRuleSet.js"></script>
	<script type="text/javascript" src="${ctxStatic}/prss/rule/jquery.form2json.js"></script>
</body>
</html>