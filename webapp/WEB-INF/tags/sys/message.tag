<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="content" type="java.lang.String" required="true" description="消息内容"%>
<%@ attribute name="type" type="java.lang.String" description="消息类型：info、success、warning、error、loading"%>
<c:if test="${not empty content}">
	<c:if test="${not empty type}">
		<c:set var="ctype" value="${type}" />
	</c:if>
	<c:if test="${empty type}">
		<c:set var="ctype" value="${fn:indexOf(content,'失败') eq -1?'success':'error'}" />
	</c:if>
	<script type="text/javascript">
	layui.use(["layer"],function(){
		var layer = layui.layer;
		if("${ctype}"=="loading"){
			
		} else {
			var icon = -1;
			if("${ctype}"=="success"){
				icon = 1;
			} else if("${ctype}"=="error"){
				icon = 2;
			}	else if("${ctype}"=="warning"){
				icon = 7;
			}
			layer.msg("${content}", {
				icon:icon
			});
		}
	});
	</script>
</c:if>
