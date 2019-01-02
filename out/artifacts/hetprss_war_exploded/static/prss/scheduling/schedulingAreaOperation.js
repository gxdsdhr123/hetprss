var layer;
var atree;
var stree;
var areaType = 0;
var setting = {
	data : {
		simpleData : {
			enable : true,
			idKey : "id",
			pIdKey : "pId",
			rootPId : '0'
		}
	},
	edit : {
		enable : true,
		showRemoveBtn : false,
		showRenameBtn : false,
		drag : {
			isCopy : false,
			isMove : false
		}
	},
	view : {
		showLine : false,
		selectedMulti : false
	},
	check : {
		enable : true,
		chkboxType : {
			"Y" : "s",
			"N" : "ps"
		}
	}
};
$(function(){
	layui.use([ "layer","form", "element" ], function() {
		layer = layui.layer;
		var form = layui.form;
		form.on('radio(area)', function(data) {
			areaType = data.value;
			showArea(data.value);
		});
	});
	$("html,body").css("cssText","height:100% !important");
	$(".ztree").each(function(){
		scrollbar = new PerfectScrollbar(this);
	})
	areaType = $("#type").val();
	showArea(areaType);
})
function showArea(type){
	var func = "getAllJW";
	if(type == 0){
		$("#leftTitle label").text("全部机位");
		$("#rightTitle label").text("已选机位");
	}
	if(type == 1){
		$("#leftTitle label").text("全部机型");
		$("#rightTitle label").text("已选机型");
		func = "getAllJX";
	}
	$.ajax({
		type:'post',
		url:ctx+"/scheduling/gantt/"+func,
		data:{
			id:$("#id").val()
		},
		dataType:'json',
		success:function(res){
			$.fn.zTree.destroy();
			$(".ztree").empty();
			if(type == 0){
				atree = $.fn.zTree.init($("#aztree"), setting, res.atree);
				stree = $.fn.zTree.init($("#sztree"), setting, res.stree);
				// 选择
				$("#pushright").unbind().click(function() {
					moveTree("aztree", "sztree");
				});
				// 取消选择
				$("#pushleft").unbind().click(function() {
					moveTree("sztree", "aztree");
				});
			}
			if(type == 1){
				var search = $("<input class='search form-control' placeholder='请输入关键字' oninput='search(event)' type='text'>");
				var ul = $("<ul class='list-group' id='allul'></ul>");
				var sul = $("<ul class='list-group' id='selectul'></ul>");
				var length = res.atree.length;
				for(var i=0;i<length;i++){
					var li = $("<li class='list-group-item' id='"+res.atree[i].actype+"' data-code='"+res.atree[i].actype+"' onclick='liclick(this);' onmousedown='lidown(this);' onmousemove='limove(this);' onmouseup='liup(this);'>"+res.atree[i].actName+"</li>");
					ul.append(li);
				}
				$("#aztree").append(search).append(ul);
				for(var j=0;j<res.stree.length;j++){
					var li = $("<li class='list-group-item' id='"+res.stree[j].actype+"' data-code='"+res.stree[j].actype+"' onclick='liclick(this);' onmousedown='lidown(this);' onmousemove='limove(this);' onmouseup='liup(this);'>"+res.stree[j].actName+"</li>");
					sul.append(li);
				}
				$("#sztree").append(search.clone()).append(sul);
				// 选择
				$("#pushright").unbind().click(function() {
					var lis = $("#allul").find("li.bechoose");
					$("#selectul").append(lis.clone(true).removeClass("bechoose"));
					lis.remove();
				});
				// 取消选择
				$("#pushleft").unbind().click(function() {
					var lis = $("#selectul").find("li.bechoose");
					$("#allul").append(lis.clone(true).removeClass("bechoose"));
					lis.remove();
				});
			}
			scrollbar.update();
		}
	});
}
/**
 * wangtg
 * 使用zTree进行左右转移操作
 * param1:zTree的DOM容器的id
 * param2:zTree的DOM容器的id
 * 建议放入common文件统一调用
 */
function moveTree(fromTree, toTree){
	var treeLeftObj=null;
	var nodes=null;
	var treeRightObj=null;
	// 获取左边树选中的节点
	treeLeftObj = $.fn.zTree.getZTreeObj(fromTree);
	nodes = treeLeftObj.getCheckedNodes();		
	// 执行转移操作
	treeRightObj = $.fn.zTree.getZTreeObj(toTree);
	for (var i = 0; i < nodes.length; i++) {
		var parentId = nodes[i].pId;
		if(parentId != 0){
			
			if (treeRightObj.getNodeByParam("id", parentId) == null) {
				var parentNode = [ {
					id : parentId,
					name : nodes[i].getParentNode().name,
					pId : "0"
					} ];
				treeRightObj.addNodes(null, parentNode);
			}
			treeRightObj.addNodes(treeRightObj.getNodeByParam("id", parentId), nodes[i]);
			treeLeftObj.removeNode(nodes[i]);
			//如果子节点都转移过去，将空的父节点移除
			var currentNode=treeLeftObj.getNodeByParam("id",parentId)
			var leftOtherCld=currentNode.children;
			if($.isEmptyObject(leftOtherCld)){
				treeLeftObj.removeNode(currentNode);
			}
		}else{
			if (treeRightObj.getNodeByParam("id", nodes[i].id) == null) {
				//如果状态是全部勾选选 1：部分勾选 0：全不勾选
				if(nodes[i].check_Child_State==2){
					//直接将整个父节点转移
					treeRightObj.addNodes(null, nodes[i]);
					treeLeftObj.removeNode(nodes[i]);
					//因为数组还会继续遍历后面的子节点，所以要在数组中将属于这个父节点的子节点删掉
					nodes.splice(i,nodes[i].children.length+1);
				}else{
					var parentNode = [ {
						id : nodes[i].id,
						name : nodes[i].name,
						pId : "0"
						} ];
					treeRightObj.addNodes(null, parentNode);
				}
			} 
		}
	}
}
function search(e) {
	var value = e.target.value.trim();
	$(e.target).parent().parent().find(".list-group-item").each(function() {
		if ($(this).text().indexOf(value) == -1) {
			$(this).hide();
		} else {
			$(this).show();
		}
	})
	scrollbar.update();
}
function liclick(obj) {
	if (!$(obj).hasClass("bechoose")) {
		$(obj).addClass("bechoose");
	} else {
		$(obj).removeClass("bechoose");
	}
}

function lidown(obj) {
	holdDown = 1;
}
function liup(obj) {
	holdDown = 0;
}
function limove(obj) {
	if (holdDown == 1) {
		if (!$(obj).hasClass("bechoose")) {
			$(obj).addClass("bechoose");
		}
	}
}
function filter(node){
	return node.level == 1;
}
function save(id){
	if($("#name").val() == null || $("#name").val() == ""){
		layer.msg("请填写区域名称",{icon:7});
		return false;
	}
	var data = {
			name:$("#name").val(),
			type:areaType
	}
	if(id){
		data.id = id;
	}
	var nodeList = [];
	if(areaType == 0){
		var nodes = stree.getNodesByFilter(filter);
		if(nodes.length == 0){
			layer.msg("请选择区域",{icon:7});
			return false;
		}
		for(var i=0;i<nodes.length;i++){
			nodeList.push(nodes[i].name);
		}
	}
	if(areaType == 1){
		$("#sztree li").each(function(i,ele){
			nodeList.push($(ele).data("code"));
		})
	}
	data.data = nodeList;
	var loading = layer.load(2);
	$.ajax({
		type:'post',
		url:ctx+"/scheduling/gantt/saveResGanttArea",
		data:{
			data:JSON.stringify(data),
		},
		error:function(){
			layer.msg("请求发送失败",{icon:2,time:1000},function(){
				parent.closeLayer();
			});
			layer.close(loading);
		},
		success:function(msg){
			if(msg == "success"){
				layer.msg("保存成功！",{icon:1,time:1000},function(){
					parent.closeLayer();
				});
			}else{
				layer.msg("保存失败",{icon:2,time:1000});
				console.error(msg);
			}
		}
	});
}