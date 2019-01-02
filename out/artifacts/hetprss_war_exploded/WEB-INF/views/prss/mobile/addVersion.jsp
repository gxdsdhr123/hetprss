<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>上传手持机新版本</title>
<script type="text/javascript" src="${ctxStatic}/prss/mobile/spark-md5.js"></script>
<script type="text/javascript">
	layui.use([ "layer", "form", "element" ], function() {
		var layer = layui.layer;
	});
	
	$(document).ready(function() {
		init();
	});
	
	function saveVersion() {
		var updateversion = $("#updateversion").val();
		var updateurl = $("#updateurl").val();
		var serverurl = $("#serverurl").val();
		var pushurl = $("#pushurl").val();
		var apkFile = document.getElementById("apkFile").files;
		if(apkFile[0]){
			if (".apk" != apkFile[0].name.substring(apkFile[0].name.length-4)) {
				console.log(apkFile[0].name);
				layer.msg("安装包格式错误",{icon:7});
				return false;
			}
		} else {
			layer.msg("请选择安装包",{icon:7});
			return false;
		}
		var form = new FormData($('#vsnForm')[0]);
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
		var loading = layer.load(2, {
			shade : [ 0.1, '#000' ]
		});
		$.ajax({
			type : 'post',
			async : false,
			url : ctx + "/mobile/mobileVersion/addVersion",
			data : form,
			processData : false,
			cache : false,
			contentType : false,
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
	
	function init() {
        document.getElementById('apkFile').addEventListener('change', function () {
            var blobSlice = File.prototype.slice || File.prototype.mozSlice || File.prototype.webkitSlice,
                file = this.files[0],
                chunkSize = 2097152,                             // Read in chunks of 2MB
                chunks = Math.ceil(file.size / chunkSize),
                currentChunk = 0,
                spark = new SparkMD5.ArrayBuffer(),
                fileReader = new FileReader();

            fileReader.onload = function (e) {
                spark.append(e.target.result);                   // Append array buffer
                currentChunk++;

                if (currentChunk < chunks) {
                    loadNext();
                } else {
                    $("#MD5").val(spark.end());
                }
            };

            fileReader.onerror = function () {
                console.warn('oops, something went wrong.');
            };

            function loadNext() {
                var start = currentChunk * chunkSize,
                    end = ((start + chunkSize) >= file.size) ? file.size : start + chunkSize;

                fileReader.readAsArrayBuffer(blobSlice.call(file, start, end));
            }

            loadNext();
        });
    }
</script>
</head>
<body>
	<input type="hidden" name="maxVsn" id="maxVsn" value="${maxVsn }">
	<form id="vsnForm" class="layui-form" action="" enctype="multipart/form-data">
		<input type="hidden" name="MD5" id="MD5" value="">
		<div class="layui-form-item">
			<label class="layui-form-label">版本</label>
			<div class="layui-input-inline">
				<input type="text" autocomplete="off" class="layui-input" name="updateversion" id="updateversion" value="" >
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">描述</label>
			<div class="layui-input-block">
				<input type="text" autocomplete="off" class="layui-input" name="updatedesc" id="updatedesc" value="" >
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">安装包下载路径</label>
			<div class="layui-input-block">
				<input type="text" autocomplete="off" class="layui-input" name="updateurl" id="updateurl" value="http://10.40.18.22:7012/api/" >
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">登录服务地址</label>
			<div class="layui-input-block">
				<input type="text" autocomplete="off" class="layui-input" name="serverurl" id="serverurl" value="http://10.40.18.22:7012/api/" >
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">推送服务地址</label>
			<div class="layui-input-block">
				<input type="text" autocomplete="off" class="layui-input" name="pushurl" id="pushurl" value="http://10.40.18.41:8083/CloudPushANC/" >
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">安装包文件</label>
			<div class="layui-input-block">
				<input type="file" name="apkFile" id="apkFile" />
			</div>
		</div>
	</form>
</body>
</html>