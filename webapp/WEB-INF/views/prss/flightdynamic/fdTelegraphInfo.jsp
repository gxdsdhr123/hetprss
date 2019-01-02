<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>补配规则分配</title>
	<meta name="decorator" content="default" />
	<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
	<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
</head>
<body>
	<form class="layui-form" action="" id="createForm">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">航班日期</label>
				<div class="layui-input-inline">
					<input id="ruleName" htmlEscape="false" readonly="readonly"  
						class="layui-input" type="text"
						value="${info.FLIGHT_DATE}" />
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">报文类型</label>
				<div class="layui-input-inline">
					<input id="ruleDesc" htmlEscape="false" readonly="readonly"  
						class="layui-input" type="text"
						value="${info.TEL_TYPE}" />
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">航班号</label>
				<div class="layui-input-inline">
                    <input id="ruleDesc" htmlEscape="false" readonly="readonly"  
                        class="layui-input" type="text"
                        value="${info.FLIGHT_NUMBER}" />
				</div>
			</div>
		</div>
        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">报文时间</label>
                <div class="layui-input-inline">
                    <input id="ruleName" htmlEscape="false" readonly="readonly"  
                        class="layui-input" type="text"
                        value="${info.DTTM}" />
                </div>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label">接受时间</label>
                <div class="layui-input-inline">
                    <input id="ruleDesc" htmlEscape="false" readonly="readonly"  
                        class="layui-input" type="text"
                        value="${info.ACCEPT_TIME}" />
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-inline" style="width: 100%;">
                <label class="layui-form-label">报文原文</label>
                <div class="layui-input-inline" style="width: 70%;">
                     <textarea rows="" cols="" style="height: 300px;" class="layui-textarea" readonly="readonly">${info.DATA}</textarea>
                </div>
            </div>
        </div>
		
	</form>
</body>
</html>