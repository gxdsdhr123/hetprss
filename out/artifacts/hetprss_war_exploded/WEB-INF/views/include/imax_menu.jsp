<%@page import="com.neusoft.framework.common.config.Global"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
	String menuAction = request.getServletPath();
	String imaxPath = Global.getConfig("imaxPath");
	if(menuAction.startsWith(imaxPath)){
		menuAction = menuAction.replaceFirst(imaxPath, "");
	    request.setAttribute("menuAction", menuAction);
	}
%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
#menu {
	margin: 0px;
	position: absolute;
	z-index: 999;
	position: absolute;
	background: rgba(255, 255, 255, 0.1);
	width: 100%;
	padding-top:5px;
	height:50px;
}

.layui-tab-title {
	border-bottom: 0px;
}

.layui-tab-title li {
	color: #fff;
	font-family: Microsoft YaHei;
	font-size: 18px;
	padding: 0 10px;
}

.layui-tab-title li:hover {
	background: rgba(0, 0, 0, 0.5);
}

.layui-tab-brief>.layui-tab-title .layui-this {
	color: #fff;
}

#kpiExplain {
	position: absolute;
	right: 15px;
	top: 10px;
	/*color:#63D79C;*/
	color: #01AAED;
	font-size: 100%;
	cursor: pointer;
}

#explainTable {
	margin: 20px;
}

#kpiNameTD {
	padding: 2px 5px;
}
</style>
<script type="text/javascript">
	layui.use([ "element","layer" ], function() {
		var element = layui.element;
		//监听导航点击
		$("#menu ul li").click(function(){
			var action = $(this).attr("action");
			if(action){
				document.location.href = ctxI +"/"+action;
			} else {
				layer.msg("该功能正在建设中敬请期待...");
			}
		});
		/* $("#kpiExplain").click(function(){
			$.ajax({
				type : "post",
				url : ctx+"/kpiExplain",
				async : true,
				data:{
					windowId:$("#menu ul li[class=layui-this]").attr("id")
				},
				dataType : "json",
				success : function(explains) {
					var table = "<table id='explainTable'>";
					if(explains&&explains.length>0){
						for(var i=0;i<explains.length;i++){
							if(explains[i]){
								table+="<tr>";
								var kpiName="";
								if(explains[i].KPINAME){
									kpiName=explains[i].KPINAME+"：";
								}
								table+="<td id='kpiNameTD' nowrap='nowrap' valign='top'>"+kpiName+"</td>";
								table+="<td>"+explains[i].KPIDESC+"</td>";
								table+="</tr>";
							}
						}
					}
					table+="</table>";
					var windIndex = layer.open({
					  title:false,
					  //offset:["50px","67%"],
					  offset:"b",
					  shadeClose:true,
					  closeBtn:false,
					  scrollbar :false,
					  type: 1,
					  anim:2,
					  //area:["32.5%","70%"],
					  area:["100%",""],
					  content: table
					});
					layer.style(windIndex, {
						  //background:"#5FB878",
						  background:"rgba(33,44,57,0.8)",
						  color:"#fff",
						  fontSize:"110%"
					});  
				}
			});
		}); */
	});
</script>
</head>
<body>
	<div id="menu" class="layui-tab layui-tab-brief">
		<ul class="layui-tab-title site-demo-title" lay-filter="menuChange">
			<li action="index"><img src="${path}/static/images/logo3.png" height="35"></img></li>
			<li action="index" class="${(menuAction eq '' or menuAction eq '/index')?'layui-this':''}">首页</li>
<%-- 			<li action="run1" class="${menuAction eq '/run1'?'layui-this':''}">航班正常性分析</li> --%>
			<li action="run2" class="${menuAction eq '/run2'?'layui-this':''}">航班运行情况</li>
			<li action="resource1" class="${menuAction eq '/resource1'?'layui-this':''}">人员保障情况</li>
<%-- 			<li action="resource2" class="${menuAction eq '/resource2'?'layui-this':''}">设备资源情况</li> --%>
			<li action="illegal" class="${menuAction eq '/illegal'?'layui-this':''}">部门违规情况</li>
			<li action="monitor" class="${menuAction eq '/monitor'?'layui-this':''}">航班保障标准</li>
		</ul>
	</div>
</body>
</html>