<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<title>手持机设备管理表单</title>
<script type="text/javascript">
	layui.use([ "layer", "form", "element" ], function() {
		var layer = layui.layer;
	});
	
	$(document).ready(function() {
		$("body").height(500);
		$("body").css("position","relative");
		new PerfectScrollbar(document.body);
	});
	
	function saveVersion() {
		var officeId = $("#officeId").val();
		var pdaNo = $("#pdaNo").val();
		var imei = $("#imei").val();
		var status = $("#status").val();
		
		if(!officeId||!$.trim(officeId)){
			layer.msg("请输入部门",{icon:7});
			return false;
		}
		if(!pdaNo||!$.trim(pdaNo)){
			layer.msg("请输入设备编号",{icon:7});
			return false;
		}
		if(!imei||!$.trim(imei)){
			layer.msg("请输入IMEI",{icon:7});
			return false;
		}
		if(!status||!$.trim(status)){
			layer.msg("请输入设备状态",{icon:7});
			return false;
		}
		var loading = layer.load(2, {
			shade : [ 0.1, '#000' ]
		});
		$.ajax({
			type : 'post',
			async : false,
			url : ctx + "/mobile/mobileManage/savePDA",
			data : {
				'id' : $("#id").val(),
				'officeId' : officeId,
				'pdaNo' : pdaNo,
				'imei' : imei,
				'status' : status
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
	<form id="pdaForm" class="layui-form" action="">
		<input type="hidden" name="id" id="id" value="${pdaJson.id}">
		<div class="layui-form-item">
			<label class="layui-form-label">部门</label>
			<div class="layui-input-inline">
					<sys:treeselect id="office"
						name="office.id" value="${pdaJson.office}"
						labelName="office.name" labelValue="${pdaJson.officeName}"
						title="部门" url="/sys/office/treeData?type=2"
						cssClass="form-control" allowClear="true"
						notAllowSelectParent="true" />
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">设备编号</label>
			<div class="layui-input-inline">
				<input type="text" autocomplete="off" class="layui-input" name="pdaNo" id="pdaNo" value="${pdaJson.pdaNo }" >
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">IMEI</label>
			<div class="layui-input-inline">
				<input type="text" autocomplete="off" class="layui-input" name="imei" id="imei" value="${pdaJson.imei }" >
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">设备状态</label>
			<div class="layui-input-inline">
				<select id="status" name="status" lay-verify="">
  					<option value="">请选择</option>
						<c:forEach items="${status}" var="item">
							<option value="${item.value}" <c:if test="${item.value == pdaJson.status }">selected</c:if>>${item.text}</option>
						</c:forEach>
				</select>
			</div>
		</div>
	</form>
</body>
</html>