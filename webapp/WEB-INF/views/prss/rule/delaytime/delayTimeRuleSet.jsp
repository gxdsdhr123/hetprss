<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>延误时长</title>
	<meta name="decorator" content="default" />
	<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
	<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
	<link href="${ctxStatic}/prss/rule/css/taskAllotRuleSet.css" rel="stylesheet" />
	<script type="text/javascript">
	</script>
</head>
<body>
	<form class="layui-form" action="" id="createForm" lay-filter="filter">
		<input id="id" name="id" type="hidden" value="${delayTimeVO.ID}" />
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">英文名称<font class="required">*</font></label>
				<div class="layui-input-inline">
					<input id="enName" name="enName" ${type=='update'?'readonly':'' } htmlEscape="false" required 
						placeholder="英文名称" class="layui-input" type="text"
						value="${delayTimeVO.EN_NAME}" />
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">中文时长名称<font class="required">*</font></label>
				<div class="layui-input-inline">
					<input id="cnName" name="cnName" htmlEscape="false"
						placeholder="中文时长名称" class="layui-input" type="text"
						value="${delayTimeVO.CN_NAME}" />
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">是否生效</label>
				<div class="layui-input-inline">
					<select id="ruleIfValid" name="ruleIfValid">
						<option value="1" <c:if test="${delayTimeVO.IF_VALID == 1}">selected</c:if>>是</option>
						<option value="0" <c:if test="${delayTimeVO.IF_VALID == 0}">selected</c:if>>否</option>
					</select>
				</div>
			</div>
		</div>
        <hr></hr>	
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">进出港<font class="required">*</font></label>
				<div class="layui-input-block" lay-filter="divFilter">
					<c:forEach items="${inOutPortList}" var="inOutPort">
						<input type="checkbox" name="ruleExtInOutPort"  value="${inOutPort.code}" 
							${inOutPort.code=='A0' ? 'checked' : ''} title="${inOutPort.desc}" />
					</c:forEach>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label" >优先级<font class="required">*</font></label>
				<div class="layui-input-inline">
					<select name="priority" class="form-control priority">
                        <c:forEach begin="1" end="9" step="1" varStatus="status">
                            <option value="${10-status.index}" >${10-status.index}</option>
                        </c:forEach>
                    </select>
				</div>
			</div>
		</div>
		
	</form>

	<div class="rows">
		<div class="col-md-6">
			<div class="layui-inline">
				<label class="layui-form-label" style="margin-left: 15px;">机型</label>
				<div class="layui-input-inline">
					<input name="acttypeCode" placeholder="请选择机型" class="layui-input" type="text" readonly="readonly"
					onclick="openCheck('acttypeCode')" style="width: 420px;">
                    <input name="acttypeCodeValue" type="hidden">
				</div>
			</div>
		</div>
		<div class="col-md-6">
			<div class="layui-inline">
				<label class="layui-form-label" style="margin-left: 15px;">航空公司</label>
				<div class="layui-input-inline">
					<input name="alnCode" placeholder="请选择航空公司" class="layui-input" type="text" readonly="readonly"
					onclick="openCheck('alnCode')" style="width: 420px;">
					<input name="alnCodeValue" type="hidden">
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
						<input id="extend_expression" name="extend_expression" type="hidden" value=""> 
						<input id="extend_drools" name="extend_drools" type="hidden" value='' /> 
						<input id="extend_colids" name="extend_colids" type="hidden" value='' />
						<textarea id="extend_condition" name="extend_condition" placeholder="请输入内容"
							disabled="disabled" class="layui-textarea" rows="2"></textarea>
					</div>
					<div class="layui-inline">
						<a id="extendGuide" href="javascript:void(0)" class="an-h" onclick="openGuide('extend')">【配置扩展条件】</a>
					</div>
				</div>
			</div>
		</div>
	</div>
    <hr></hr>   
    <div class="rows">
        <div class="col-md-9">
            <div class="layui-form-item  layui-form-text">
                <div class="layui-inline">
                    <label class="layui-form-label"  style="margin-left:15px">时长=</label>
                    <div class="layui-input-inline" style="width: 600px;">
                        <input id="time_expression" name="expression" type="hidden" value=""> 
                        <input id="time_drools" name="time_drools" type="hidden" value='' /> 
                        <input id="time_colids" name="time_colids" type="hidden" value='' />
                        <textarea id="time_condition" name="time_condition" placeholder="请输入内容"
                            disabled="disabled" class="layui-textarea" rows="2"></textarea>
                    </div>
                    <div class="layui-inline">
                        <a id="timeGuide" href="javascript:void(0)" class="an-h" onclick="showGuide('time')">【配置扩展条件】</a>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="layui-layer-btn layui-layer-btn-">
                <a class="layui-layer-btn0" id="add">添加</a>
            </div>
        </div>
    </div>
	
	<div id="baseTables">
        <table id="baseTable"></table>
    </div>
    
    
	<script type="text/javascript" src="${ctxStatic}/prss/rule/delaytime/delayTimeRuleSet.js"></script>
	<script type="text/javascript" src="${ctxStatic}/prss/rule/jquery.form2json.js"></script>
</body>
</html>