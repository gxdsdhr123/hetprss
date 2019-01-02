<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>条件分配规则设置</title>
	<meta name="decorator" content="default" />
	<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
	<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
    <link href="${ctxStatic}/prss/stand/css/conditinRule.css" rel="stylesheet" />
    <link href="${ctxStatic}/jquery/plugins/jquery-ui/jquery-ui.min.css" rel="stylesheet" />
    <link href="${ctxStatic}/prss/rule/css/guide.css" rel="stylesheet" />
    <style type="text/css">
    .layui-btn + .layui-btn {
        margin-left: 0px;
    }
    </style>
</head>
<body>
	<form class="layui-form" action="" id="createForm">
		<input id="id" name="id" type="hidden" value="${ruleInfo.ID}" />
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">规则名称<font class="required">*</font></label>
				<div class="layui-input-inline">
					<input id="ruleName" name="ruleName" htmlEscape="false" required  
						placeholder="规则名称" class="layui-input" type="text"
						value="${ruleInfo.RULE_NAME}" />
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">分配规则类型</label>
				<div class="layui-input-inline">
					<select id="ruleType" name="ruleType">
						<option value="0" <c:if test="${ruleInfo.TYPE == 0}">selected</c:if>>允许</option>
						<option value="1" <c:if test="${ruleInfo.TYPE == 1}">selected</c:if>>禁止</option>
					</select>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">是否生效</label>
				<div class="layui-input-inline">
					<select id="ruleIfValid" name="ruleIfValid">
						<option value="0" <c:if test="${ruleInfo.IF_VALID == 0}">selected</c:if>>是</option>
						<option value="1" <c:if test="${ruleInfo.IF_VALID == 1}">selected</c:if>>否</option>
					</select>
				</div>
			</div>
            <div class="layui-inline">
                <label class="layui-form-label">优先级</label>
                <div class="layui-input-inline">
					<select id="priLevel" name="priLevel">
	                    <c:forTokens items="9,8,7,6,5,4,3,2,1" delims="," var="index">
	                        <option value="${ index}" <c:if test="${ruleInfo.PRI_LVL == index}">selected</c:if>>${index }</option>
	                    </c:forTokens>
                    </select>
                </div>
            </div>
		</div>
		
		<div class="layui-form-item">
			<div class="layui-inline" style="width: 100%">
				<label class="layui-form-label">规则</label>
				<div class="layui-input-inline" style="width: 90%">
					<div id="mtext" class="layui-textarea"
						style="overflow-wrap: break-word;">${ruleInfo.condition }</div>
				</div>
			</div>
		</div>
        
        <div class="layui-form-item">
            <div class="layui-inline" style="width: 100%">
                <label class="layui-form-label">表达式</label>
                <div class="layui-input-inline" style="width: 90%" id="params">
                    <c:forEach items="${paramList }" var="item" step="1"
                        varStatus="status">
                        <input type="button" value="${item.COL_CNNAME }"
                            class="layui-btn layui-btn-small layui-btn-normal" style="width: 120px;"
                            data-en="${item.COL_NAME }" data-id="${item.ID }"
                            onclick="setValue(this)"></input>
                    </c:forEach>
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-inline" style="width: 100%">
                <label class="layui-form-label"></label>
                <input type="button" style="width: 120px;"
                        class="layui-btn layui-btn-small layui-btn-normal" value="编辑表达式"
                        onclick="addParam()" />
			</div>
        </div>
        
        <div class="layui-form-item">
			<div class="layui-inline" style="width: 100%">
				<label class="layui-form-label">逻辑规则</label>
				<div class="layui-input-inline" style="width: 70%">
					<input type="button"
						class="layui-btn layui-btn-small layui-btn-normal" value="+"
						onclick="getValue(this,this.value,'add','opt')" /> <input
						type="button" class="layui-btn layui-btn-small layui-btn-normal"
						value="-" onclick="getValue(this,this.value,'sub','opt')" /> <input
						type="button" class="layui-btn layui-btn-small layui-btn-normal"
						value="*" onclick="getValue(this,this.value,'mul','opt')" /> <input
						type="button" class="layui-btn layui-btn-small layui-btn-normal"
						value="/" onclick="getValue(this,this.value,'div','opt')" /> <input
						type="button" class="layui-btn layui-btn-small layui-btn-normal"
						value="等于" onclick="getValue(this,'==','eq','opt')" /> <input
						type="button" class="layui-btn layui-btn-small layui-btn-normal"
						value="不等于" onclick="getValue(this,'!=','uneq','opt')" /> <input
						type="button" class="layui-btn layui-btn-small layui-btn-normal"
						value="大于" onclick="getValue(this,'>','gt','opt')" /> <input
						type="button" class="layui-btn layui-btn-small layui-btn-normal"
						value="大于等于" onclick="getValue(this,'>=','gte','opt')" /> <input
						type="button" class="layui-btn layui-btn-small layui-btn-normal"
						value="小于" onclick="getValue(this,'&lt;','lt','opt')" /> <input
						type="button" class="layui-btn layui-btn-small layui-btn-normal"
						value="小于等于" onclick="getValue(this,'&lt;=','lte','opt')" /> <input
						type="button" class="layui-btn layui-btn-small layui-btn-normal"
						value="匹配" onclick="getValue(this,'match','match','opt')" /> <input
						type="button" class="layui-btn layui-btn-small layui-btn-normal"
						value="包含" onclick="getValue(this,'contain','contain','opt')" /> <input
						type="button" class="layui-btn layui-btn-small layui-btn-normal"
						value="前匹配" onclick="getValue(this,'lmatch','lmatch','opt')" /> <input
						type="button" class="layui-btn layui-btn-small layui-btn-normal"
						value="后匹配" onclick="getValue(this,'rmatch','rmatch','opt')" />
				</div>
			</div>
        </div>
        <div class="layui-form-item">
            <div class="layui-inline" style="width: 100%">
                <label class="layui-form-label">逻辑链接</label>
                <div class="layui-input-inline" style="width: 400px;">
					<input type="button"
						class="layui-btn layui-btn-small layui-btn-normal" value="并且"
						onclick="getValue(this,'&&','and','join')" /> <input type="button"
						class="layui-btn layui-btn-small layui-btn-normal" value="或者"
						onclick="getValue(this,'||','or','join')" /> <input type="button"
						class="layui-btn layui-btn-small layui-btn-normal" value="左括号"
						onclick="getValue(this,'(','lb','join')" /> <input type="button"
						class="layui-btn layui-btn-small layui-btn-normal" value="右括号"
						onclick="getValue(this,')','rb','join')" />  <input type="button"
						class="layui-btn layui-btn-small layui-btn-normal" value="数字"
						onclick="inpuValue(this,'int','val')" /> <input type="button"
						class="layui-btn layui-btn-small layui-btn-normal" value="字符"
						onclick="inpuValue(this,'string','val')" /> 
				</div>
				<div class="layui-input-inline">
				    <input name="input_val" id="input_val" class="layui-input" type="text">
                </div>
            </div>
		</div>
    </form>
	<div class="row">
		<div class="col-md-1"></div>
		<div class="col-md-7">
			<div id="leftDiv4">
				<div class="layui-form-item">
					<label class="layui-form-label" style="width: 100px;">可选机位</label>
					<div class="layui-input-inline">
						<input class="layui-input" autocomplete="off" type="text"
							id="unfixedSearch" placeholder="输入关键字回车搜索"
							style="width: 180px; display: inline-block;">
					</div>
				</div>
				<ul class="list-group sortable unfixedUL" id="allUnfixedUL">
					<c:forEach items="${actstandList }" var="actstand">
						<li class='list-group-item hr' data-code='${actstand.id }'>${actstand.id }</li>
					</c:forEach>
				</ul>
			</div>
			<div id="middleDiv">
				<button id="pushright3" type="button"
					class="btn btn-default fa fa-angle-double-right"></button>
				<button id="pushleft3" type="button"
					class="btn btn-default fa fa-angle-double-left"></button>
			</div>
			<div id="rightDiv4">
				<div class="layui-form-item">
					<label class="layui-form-label">机位分配顺序</label>
				</div>
				<ul class="list-group sortable unfixedUL chooseUnfixedFiled"
					id="selUnfixedUL">
					<c:forEach items="${fixedList }" var="actstand">
						<li
							class='list-group-item hr ui-draggable ui-draggable-handle ui-sortable-handle'
							data-code='${actstand }'>${actstand }</li>
					</c:forEach>
				</ul>
			</div>
		</div>
	</div>
	<div id="leftOrRight" hidden="">
         <ul class="pop-ul">
            <li data-value="left" class="layui-nav-item">所选表达式前</li>
            <li data-value="right" class="layui-nav-item">所选表达式后</li>
         </ul>
	</div>
	
	<script type="text/javascript" src="${ctxStatic}/prss/stand/conditionRuleSet.js"></script>
	<script type="text/javascript" src="${ctxStatic}/prss/rule/jquery.form2json.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery/plugins/jquery-ui/jquery-ui.min.js"></script>
</body>
</html>