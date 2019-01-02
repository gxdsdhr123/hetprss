<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="编号"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="输入框名称"%>
<%@ attribute name="value" type="java.lang.String" required="true" description="输入框值"%>
<i id="${id}Icon" class="fa fa-${not empty value?value:' hide'}"></i>&nbsp;<label id="${id}IconLabel">${not empty value?value:'无'}</label>&nbsp;
<input id="${id}" name="${name}" type="hidden" value="${value}"/><a id="${id}Button" href="javascript:" class="btn">选择</a>&nbsp;&nbsp;
<script type="text/javascript">
layui.use(["layer"],function(){
	var layer = layui.layer;
	$("#${id}Button").click(function(){
		layer.open({
			title:"选择${title}",
			type : 2,
			area : [ "1000px", "450px" ],
			btn : ["确定","清除","关闭"],
			shadeClose : false, // 开启遮罩关闭
			content:"${ctx}/tag/iconselect?value="+$("#${id}").val(),
			yes:function(index,layero){
				var icon = layer.getChildFrame('#icon', index).val();
				$("#${id}Icon").attr("class", "fa fa-"+icon);
                $("#${id}IconLabel").text(icon);
                $("#${id}").val(icon);
				layer.close(index);
			},
			btn2:function(index,layero){
				$("#${id}Icon").attr("class", "icon- hide");
                $("#${id}IconLabel").text("无");
                $("#${id}").val("");
			}
		});
	});
});
</script>
