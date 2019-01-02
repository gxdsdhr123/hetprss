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

var zTreeObj;
$(document).ready(function() {
	$(".sortable").css("position","relative");
	$(".sortable").each(function(){
		new PerfectScrollbar(this);
	});
	$("body").css("minHeight", "0px");
	$(".content").css("minHeight", "0px");
	$(".content").css("padding", "0px");
	layui.use("form");
	if(typeof(sTree)=="undefined"){
		sTree = {};
	}
	zTreeObj = $.fn.zTree.init($("#aztree"), setting, treeData);
	zTreeObj = $.fn.zTree.init($("#sztree"), setting, sTree);
	// 选择
	$("#pushright").click(function() {
		moveTree("aztree", "sztree");
	});
	// 取消选择
	$("#pushleft").click(function() {
		moveTree("sztree", "aztree");
	});
})
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
/*function moveTree(fromTree, toTree){
	// 获取左边树选中的节点
	var treeLeftObj = $.fn.zTree.getZTreeObj(fromTree);
	var nodes = treeLeftObj.getCheckedNodes();
	// 执行转移操作
	var treeRightObj = $.fn.zTree.getZTreeObj(toTree);
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
			if (nodes[i].isLastNode) {
				treeLeftObj.removeNode(nodes[i].getParentNode());
			}
			treeLeftObj.removeNode(nodes[i]);
		}else{
			if (treeRightObj.getNodeByParam("id", nodes[i].id) == null) {
				treeRightObj.addNodes(null, nodes[i]);
			} else {
				treeRightObj.addNodes(treeRightObj.getNodeByParam("id", nodes[i].id), nodes[i].children);
			}
			treeLeftObj.removeNode(nodes[i]);
		}
	}
}*/

function prepareMembers() {
	var treeRightObj = $.fn.zTree.getZTreeObj("sztree");
	var nodes = treeRightObj.transformToArray(treeRightObj.getNodes());
	var selectMemberIdsp = "";
	for (var i = 0; i < nodes.length; i++) {
		if(nodes[i].pId != 0){
			selectMemberIdsp = selectMemberIdsp + nodes[i].id + ",";
		}
	}
	selectMemberIdsp = selectMemberIdsp.substring(0, selectMemberIdsp.length - 1);
	parent.$("#info").val(selectMemberIdsp);
}