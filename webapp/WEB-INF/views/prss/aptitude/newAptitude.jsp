<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>区域设置</title>
<script type="text/javascript"
	src="${ctxStatic}/prss/aptitude/newAptitude.js"></script>
</head>
<body>
	<form id="aptitudeForm" action="" class="layui-form">
		<input type="hidden" name="id" id="id" value="${office.ID }">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">部门：</label>
				<div class="layui-input-inline" style="padding: 9px 0px">${office.NAME }</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">区域类型：</label>
				<div class="layui-input-inline">
					<select name="type" id="type" lay-verify="required" lay-filter="type">
						<option value="">请选择区域类型</option>
						<option value="0">资质</option>
						<option value="1">分工</option>
					</select>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">区域名称：</label>
				<div class="layui-input-inline">
					<input type="text" autocomplete="off" class="layui-input"
						name="name" id="name" value="">
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">区域属性：</label>
				<div class="layui-input-inline" style="width: 300px;">
					<input type="radio" name="att" value="0" title="机位"
						lay-filter="jw"
						<c:if test="${officeLimConf.jwLimit == 0}">
					disabled
					</c:if>>
					<input type="radio" name="att" value="1" title="机型"
						lay-filter="jx"
						<c:if test="${officeLimConf.jxLimit == 0}">
					disabled
					</c:if>>
					<input type="radio" name="att" value="2" title="航空公司"
						lay-filter="hs"
						<c:if test="${officeLimConf.hsLimit == 0}">
					disabled
					</c:if>>
				</div>
			</div>
		</div>
		<div id="areTypeDiv" class="layui-form-item" style="display:none">
			<div class="layui-inline">
				<label class="layui-form-label">所属资质区域：</label>
				<div class="layui-input-inline">
				<select name="areType" id="areType" lay-verify="required" lay-filter="areType">
				<option value=''>请选择一个资质区域</option>
					</select>
				</div>
			</div>
		</div>
		<input type="hidden" name="info" id="info" value="">
	</form>
	<iframe id="frame" name="frame" width="100%" height="200px"
		frameborder="no"> </iframe>
</body>
</html>