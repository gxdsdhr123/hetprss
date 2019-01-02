<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>分配角色</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<%@include file="/WEB-INF/views/include/bootstrap.jsp" %>
	<script type="text/javascript">
		var layer;
		layui.use("layer",function(){
			layer = layui.layer;
		});
		var officeTree;
		var selectedTree;//zTree已选择对象
		
		// 初始化
		$(document).ready(function(){
			officeTree = $.fn.zTree.init($("#officeTree"), setting, officeNodes);
			selectedTree = $.fn.zTree.init($("#selectedTree"), setting, selectedNodes);
			// 滚动条
			new PerfectScrollbar($('body')[0]);
		});

		var setting = {view: {selectedMulti:false,nameIsHTML:true,showTitle:false,dblClickExpand:false},
				data: {simpleData: {enable: true}},
				callback: {onClick: treeOnClick}};
		
		var officeNodes=[
	            <c:forEach items="${officeList}" var="office">
	            {id:"${office.id}",
	             pId:"${not empty office.parent?office.parent.id:0}", 
	             name:"${office.name}"},
	            </c:forEach>];
	
		var pre_selectedNodes =[
   		        <c:forEach items="${userList}" var="user">
   		        {id:"${user.id}",
   		         pId:"0",
   		         name:"<font color='red' style='font-weight:bold;'>${user.name}</font>"},
   		        </c:forEach>];
		
		var selectedNodes =[
		        <c:forEach items="${userList}" var="user">
		        {id:"${user.id}",
		         pId:"0",
		         name:"<font color='red' style='font-weight:bold;'>${user.name}</font>"},
		        </c:forEach>];
		
		var pre_ids = "${selectIds}".split(",");
		var ids = "${selectIds}".split(",");
		
		//点击选择项回调
		function treeOnClick(event, treeId, treeNode, clickFlag){
			$.fn.zTree.getZTreeObj(treeId).expandNode(treeNode);
			if("officeTree"==treeId){
				$.get("${ctx}/sys/role/users?officeId=" + treeNode.id, function(userNodes){
					$.fn.zTree.init($("#userTree"), setting, userNodes);
				});
			}
			if("userTree"==treeId){
				//alert(treeNode.id + " | " + ids);
				//alert(typeof ids[0] + " | " +  typeof treeNode.id);
				if($.inArray(String(treeNode.id), ids)<0){
					selectedTree.addNodes(null, treeNode);
					ids.push(String(treeNode.id));
				}
			};
			if("selectedTree"==treeId){
				if($.inArray(String(treeNode.id), pre_ids)<0){
					selectedTree.removeNode(treeNode);
					ids.splice($.inArray(String(treeNode.id), ids), 1);
				}else{
					layer.msg("角色原有成员不能清除！");
				}
			}
		};
		function clearAssign(){
			layer.confirm('确定清除角色【${role.name}】下的已选人员？', {
			  btn: ['是','取消'], //按钮
			  icon:3
			}, function(){
				var tips="";
				if(pre_ids.sort().toString() == ids.sort().toString()){
					tips = "未给角色【${role.name}】分配新成员！";
				}else{
					tips = "已选人员清除成功！";
				}
				ids=pre_ids.slice(0);
				selectedNodes=pre_selectedNodes;
				$.fn.zTree.init($("#selectedTree"), setting, selectedNodes);
		    	layer.msg(tips);
			});
		};
	</script>
</head>
<body>
	<div class="row" style="margin:5px;">
		<div class="col-xs-4">
			<div class="box" style="height:280px;">
				<div class="box-header with-border">
					<h5 class="box-title" style="font-size:12px!important;font-weight: bold;">
						所在部门
					</h5>
				</div>
				<div class="box-body">
					<div id="officeTree" class="ztree"></div>
				</div>
			</div>
		</div>
		<div class="col-xs-4">
			<div class="box" style="height:280px;">
				<div class="box-header with-border">
					<h5 class="box-title" style="font-size:12px!important;font-weight: bold;">
						待选人员
					</h5>
				</div>
				<div class="box-body">
					<div id="userTree" class="ztree"></div>
				</div>
			</div>
		</div>
		<div class="col-xs-4">
			<div class="box" style="height:280px;">
				<div class="box-header with-border">
					<h5 class="box-title" style="font-size:12px!important;font-weight: bold;">
						已选人员
					</h5>
				</div>
				<div class="box-body">
					<div id="selectedTree" class="ztree"></div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
