<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>手持机版本管理表单</title>
<script type="text/javascript">
	layui.use([ "layer", "form", "element" ], function() {
		var layer = layui.layer;
	});

	function saveVersion() {
		var updateversion = $("#updateversion").val();
		var updateurl = $("#updateurl").val();
		var serverurl = $("#serverurl").val();
		var pushurl = $("#pushurl").val();
		
		var regu = /^[1-9][0-9]*\.[0-9]+$/;
		var re = new RegExp(regu);
		if (!re.test(updateversion)) {
			layer.msg("版本号输入格式错误",{icon:7});
			return false;
		}
		if(!updateurl||!$.trim(updateurl)){
			layer.msg("请输入安装包下载路径",{icon:7});
			return false;
		}
		if(!serverurl||!$.trim(serverurl)){
			layer.msg("请输入登录服务地址",{icon:7});
			return false;
		}
		if(!pushurl||!$.trim(pushurl)){
			layer.msg("请输入推送服务地址",{icon:7});
			return false;
		}
		var json = {};
		for(var i=0;i<$("#vsnForm input").length;i++){
			var o = $("#vsnForm input").eq(i);
			json[o.attr('name')] = o.val() || '';
		}
		var loading = layer.load(2, {
			shade : [ 0.1, '#000' ]
		});
		$.ajax({
			type : 'post',
			async : false,
			url : ctx + "/mobile/mobileVersion/saveVersion",
			data : {
				'versionDate' :JSON.stringify(json)
			},
			error : function() {
				layer.close(loading);
				layer.msg('保存失败！', {
					icon : 2
				});
			},
			success : function(data) {
				layer.close(loading);
				if (data == "success") {
					layer.msg('保存成功！', {
						icon : 1,
						time : 600
					},function(){
						parent.saveSuccess();
					});
				} else {
					layer.msg('保存失败！', {
						icon : 2,
						time : 600
					});
				}
			}
		}); 
	}
</script>
</head>
<body>
	<form id="vsnForm" class="layui-form" action="">
		<input type="hidden" name="id" id="id" value="${vsnJson.id}">
		<div class="layui-form-item">
			<label class="layui-form-label">版本</label>
			<div class="layui-input-inline">
				<input type="text" autocomplete="off" class="layui-input" name="updateversion" id="updateversion" value="${vsnJson.updateversion }" >
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">描述</label>
			<div class="layui-input-block">
				<input type="text" autocomplete="off" class="layui-input" name="updatedesc" id="updatedesc" value="${vsnJson.updatedesc }" >
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">安装包下载路径</label>
			<div class="layui-input-block">
				<input type="text" autocomplete="off" class="layui-input" name="updateurl" id="updateurl" value="${vsnJson.updateurl }" >
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">登录服务地址</label>
			<div class="layui-input-block">
				<input type="text" autocomplete="off" class="layui-input" name="serverurl" id="serverurl" value="${vsnJson.serverurl }" >
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">推送服务地址</label>
			<div class="layui-input-block">
				<input type="text" autocomplete="off" class="layui-input" name="pushurl" id="pushurl" value="${vsnJson.pushurl }" >
			</div>
		</div>
	</form>
</body>
</html>