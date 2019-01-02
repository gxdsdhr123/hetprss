<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>表达式设置</title>
	<meta name="decorator" content="default" />
	<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
	<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
    <link href="${ctxStatic}/prss/arrange/css/empPlanAdd.css" rel="stylesheet" />
    <link href="${ctxStatic}/jquery/plugins/jquery-ui/jquery-ui.min.css" rel="stylesheet" />
</head>
<body>
    <div class="rows">
        <div class="col-md-12">
        <div class="container" id="unfixedDiv">
        <form id="filterForm" class="layui-form" onkeydown="if(event.keyCode==13){return false;}">
            <div id="leftDiv4" >
                <div class="layui-form-item">
                    <label class="layui-form-label" style="width:100px;">可选表达式</label>
                    <div class="layui-input-inline">
                        <input class="layui-input" autocomplete="off" type="text" id="unfixedSearch" placeholder="输入关键字回车搜索" style="width:180px;display: inline-block;">
                    </div>
                </div>
                <ul class="list-group sortable unfixedUL" id="allUnfixedUL">
	                <c:forEach items="${selectableList}" var="item">
	                    <li class='list-group-item hr' data-code='${item.id };${item.title }'>${item.title }</li>
	                </c:forEach>
                </ul>
            </div>
            <div id="middleDiv">
                <button id="pushright3" type="button" class="btn btn-default fa fa-angle-double-right"></button>
                <button id="pushleft3" type="button" class="btn btn-default fa fa-angle-double-left"></button>
            </div>
            <div id="rightDiv4">
                <div class="layui-form-item">
                    <label class="layui-form-label">已选表达式</label>
                </div>
                <ul class="list-group sortable unfixedUL chooseUnfixedFiled" id="selUnfixedUL">
                    <c:forEach items="${selectedList}" var="item">
                        <li class='list-group-item hr ui-draggable ui-draggable-handle ui-sortable-handle' data-code='${item.id };${item.title }'>${item.title }</li>
                    </c:forEach>
                </ul>
            </div>
        </form>
    </div>
        </div>
    </div>
	<script type="text/javascript" src="${ctxStatic}/prss/stand/gateParam.js"></script>
	<script type="text/javascript" src="${ctxStatic}/prss/rule/jquery.form2json.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery/plugins/jquery-ui/jquery-ui.min.js"></script>
</body>
</html>