var setting = {
	data : {
		simpleData : {
			enable : true,
			idKey : "id",
			pIdKey : "pId",
			rootPId : '-1'
		}
	},
	view : {
		showLine : true
	},
	callback : {
		onClick : function(event, treeId, treeNode) {
			
		}
	}
};

$(document).ready(function() {
	$("#ztree").height($(window).height() - 100);
	initSchemaTree();
});
var treeScroll;
function initSchemaTree(){
	$.getJSON(ctx+"/grid/metaData/schemas", function(data) {
		var treeObj = $.fn.zTree.init($("#ztree"), setting, data).expandAll(true);
		if (treeScroll) {
			treeScroll.update();
		} else {
			treeScroll = new PerfectScrollbar('#ztree');
		}
		if(data&&data.length>0){
			var treeObj = $.fn.zTree.getZTreeObj("ztree");
			var childrenNodes = treeObj.getNodes()[0].children;
			if(childrenNodes&&childrenNodes.length>0){
				treeObj.selectNode(childrenNodes[0]);
				initTableGrid(childrenNodes[0].id);
			}
		}
	});
}

function initTableGrid(schemaId) {
	$("#dataGrid").bootstrapTable({
		toolbar : "#toolbar",
		url : ctx + "/grid/metaData/tables",
		method : "get",
		queryParams : {
			schemaId : schemaId
		},
		height:$(window).height() - 90,
		undefinedText : "",
		columns : [ {
			field : 'order',
			title : '序号',
			formatter : function(value, row, index) {
				return index + 1;
			}
		},{
			field : 'id',
			title : '表ID'
		},{
			field : "name",
			title : "表名称"
		}, {
			field : "alias",
			title : "表别名"
		}, {
			field : "cnname",
			title : "表名称（中文）"
		}, {
			field : "",
			title : "最近操作人"
		}, {
			field : "",
			title : "最近操作时间"
		}, {
			field : "remark",
			title : "备注"
		}, {
			field : "",
			title : "操作"
		} ]
	});
}

function queryTable(){
	
}