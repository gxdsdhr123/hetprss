<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>机构管理</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/treetable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/common/mustache.min.js"></script>
<script type="text/javascript">

	// 父窗口iframe自适应
	var FHeight = parent.document.documentElement.clientHeight;
	$('#officeContent', parent.window.document).attr('height', Math.floor(FHeight*0.87));
	// 父窗口ztree自适应
	$('#ztree', parent.window.document).attr({'style': 'height: ' + Math.floor(FHeight*0.87) + 'px'});
	
	var layer;
	layui.use(['layer'],function(){
		layer = layui.layer;
	});
	$(document).ready(function() {
		var tpl = $("#treeTableTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data = ${fns:toJson(list)};
		var rootId = "${not empty office.id ? office.id : '0'}";
		addRow("#treeTableList", tpl, data, rootId, true);
		$("#treeTable").treeTable({expandLevel : 5});
	});
	function addRow(list, tpl, data, pid, root){
		for (var i=0; i<data.length; i++){
			var row = data[i];
			if ((${fns:jsGetVal('row.parentId')}) == pid){
				$(list).append(Mustache.render(tpl, {
					dict: {
						type: getDictLabel(${fns:toJson(fns:getDictList('sys_office_type'))}, row.type)
					}, pid: (root?0:pid), row: row
				}));
				addRow(list, tpl, data, row.id);
			}
		}
	}
	function deleteOffice(id){
		parent.layui.layer.confirm('要删除该机构及所有子机构项吗？', {
		  btn: ['是','取消'] //按钮
		}, function(){
			redirect(ctx+"/sys/office/delete?id="+id,"parent");
			parent.layui.layer.closeAll();
		});
	}
</script>
</head>
<body>
	<sys:message content="${message}" />
	<table id="treeTable" class="layui-table">
		<thead>
			<tr>
				<th>机构名称</th>
				<th>归属区域</th>
				<th>机构编码</th>
				<th>机构类型</th>
				<th>备注</th>
				<shiro:hasPermission name="sys:office:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody id="treeTableList"></tbody>
	</table>
	<script type="text/template" id="treeTableTpl">
		<tr id="{{row.id}}" pId="{{pid}}">
			<td><a href="javascript:void(0)" onclick="redirect('${ctx}/sys/office/form?id={{row.id}}','parent')">{{row.name}}</a></td>
			<td>{{row.area.name}}</td>
			<td>{{row.code}}</td>
			<td>{{dict.type}}</td>
			<td>{{row.remarks}}</td>
			<shiro:hasPermission name="sys:office:edit">
			<td nowrap class="btn-cell">
				<div class="layui-btn-group">
					<button type="button" class="layui-btn layui-btn-primary layui-btn-small" onclick="redirect('${ctx}/sys/office/form?id={{row.id}}','parent')">修改</button>
					<button type="button" class="layui-btn layui-btn-primary layui-btn-small" onclick="deleteOffice('{{row.id}}')">删除</button>
					<button type="button" class="layui-btn layui-btn-primary layui-btn-small" onclick="redirect('${ctx}/sys/office/form?parent.id={{row.id}}','parent')">添加下级机构</button>
				</div>
			</td>	
			</shiro:hasPermission>
		</tr>
	</script>
</body>
</html>