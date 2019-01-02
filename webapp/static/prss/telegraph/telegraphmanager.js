var msg_1 = "请选择报文类型";
var msg_2 = "请选择一条报文";
var setting = {
	data : {
		simpleData : {
			enable : true,
			idKey : "id",
			pIdKey : "pId",
			rootPId : '0'
		}
	},
	view:{
		showLine:false
	},
	callback : {
		onClick : function(event, treeId, treeNode) {
			var id = treeNode.id == '0' ? '' : treeNode.id;
	        $("#typeId").val(id);				
			$('#baseTable').bootstrapTable('refresh');
		}
	}
};
layui.use('layer');
$(function(){
	var layer;
	var clickRowId = "";
	refreshTree();
	layui.use(['form','layer'],function(){
		layer = layui.layer;
	});
	jQuery.fn.bootstrapTable.columnDefaults.sortable = true;
	jQuery.fn.bootstrapTable.columnDefaults.align = "left";
	jQuery.fn.bootstrapTable.columnDefaults.editable = {toggle:'dblclick'};
	var tableOptions = {
	url: ctx+"/telegraph/templ/getTelegraphListList", //请求后台的URL（*）
	    method: "get",					  				 //请求方式（*）
	    dataType: "json",
		striped: true,									 //是否显示行间隔色
	    cache: true,
	    undefinedText:'',
	    checkboxHeader:false,
	    toolbar:$("#tool-box"),
	    search:false,
	    pagination: true,
	    sidePagination: 'server',
	    pageNumber: 1,
	    pageSize: 10,
	    sortName:'CREATETIME',
	    sortOrder:'desc',
	    pageList: [10, 15, 20],
	    queryParamsType:'',
	    queryParams: function (params) {
	        return {
	    		typeId : $("#typeId").val(),
	        	pageNumber: params.pageNumber,    
                pageSize: params.pageSize,
                sortName:params.sortName,
                sortOrder:params.sortOrder
	        }
	    },
	    columns: [
			{field: "seq",title:"序号",sortable:false,editable:false,
				formatter:function(value,row,index){
					return index+1;
				}
			},
			{field: "checkbox",checkbox:true,sortable:false,editable:false},
			{field: "ID", title: "ID",editable:false,visible:false},
			{field: "TG_NAME", title: "报文名称",editable:false},
			{field: "TGTYPENAME", title: "报文类型",editable:false,align:"center"},
			{field: "TG_TEXT", title: "报文正文",editable:false,
				formatter:function(value,row,index){
					return row.TG_TEXT ;
				}
			},
			{field: "TG_ADDRESS", title: "发送地址",editable:false},
			{field: "mtext", title: "SITA地址",editable:false,visible:false},
			{field: "totypename", title: "邮箱地址",editable:false,visible:false},
			{field: "USERNAME", title: "创建人",editable:false},
			{field: "CREATETIME", title: "创建时间",editable:false},
			{field: "mj", title: "操作",editable:false,
				formatter:function(value,row,index){
					return "<i class='fa fa-edit' onclick='modefy("+row.ID+")'></i>";
			}}
	    ],
	    onDblClickRow:onDblClickRow
	};
	function onDblClickRow(row,tr,field){
		var id=row.ID;
		modefy(id);
	}

	tableOptions.height = $("#baseTables").height();
	$("#baseTable").bootstrapTable(tableOptions);
	
	
	
	//删除
	$('#remove').click(function() {
		if($("#baseTable").bootstrapTable("getSelections").length==0){
			layer.msg('请选择要删除的消息', {
				icon : 2
			});
		}else{
			layer.confirm("是否删除选中的消息？",{offset:'100px'},function(index){
				var ids = $.map($('#baseTable').bootstrapTable('getSelections'), function (row) {
	                return row.ID;
	            });
				deleteList(ids);
				layer.close(index);
			});
		}
	});

	//新增
	$('#add').click(function() {
		var treeObj = $.fn.zTree.getZTreeObj("ztree");
		var nodes = treeObj.getSelectedNodes();
		if (nodes.length<=0) {
			layer.alert(msg_1,{icon:0,time: 1000});
		} else {
			var id = nodes[0].id;
			if( id == ""){
				layer.alert(msg_1,{icon:0,time: 1000});
			} else {
				iframe = layer.open({
					  type: 2, 
					  title:false,
					  closeBtn:false,
					  area:[$("body").width()+"px",$("body").height()-200+"px"],
					  content: ctx+"/telegraph/templ/info?flag=add&typeId=" + id,
					  btn:["保存","重置","返回"],
					  yes:function(index, layero){
						  var result = $(layero).find("iframe")[0].contentWindow.save();
						  if(result){
							  layer.closeAll('iframe');
							  refresh();
						  }
					  },
					  btn2:function(index, layero){
						  layer.getChildFrame("#createForm",index)[0].reset();

						  var old_varcols = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_varcols").value;
						  var old_mtext = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_mtext").value;
						  $(layero).find("iframe")[0].contentWindow.document.getElementById("varcols").value = old_varcols;
						  $($(layero).find("iframe")[0].contentWindow.document.getElementById("mtext")).html(old_mtext);
						  $(layero).find("iframe")[0].contentWindow.resetAll();
						  
						  var refBtn = layer.getChildFrame("#refreshDetailTable",index);
						  refBtn.click();
						  return false;
					  }
					});
					layer.full(iframe);
			}
		}
	});
})
function modefy(id){
	iframe = layer.open({
		type: 2, 
		  title:false,
		  closeBtn:false,
		  area:[$("body").width()-200+"px",$("body").height()-200+"px"],
		  content:  ctx+"/telegraph/templ/info?id="+id,
		  btn:["保存","重置","返回"],
		  yes:function(index, layero){
			  var result = $(layero).find("iframe")[0].contentWindow.save();
			  if(result){
				  layer.closeAll('iframe');
				  refresh();
			  }
		  },
		  btn2:function(index, layero){
			  layer.getChildFrame("#createForm",index)[0].reset();

			  var old_varcols = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_varcols").value;
			  var old_mtext = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_mtext").value;
			  $(layero).find("iframe")[0].contentWindow.document.getElementById("varcols").value = old_varcols;
			  $($(layero).find("iframe")[0].contentWindow.document.getElementById("mtext")).html(old_mtext);
			  $(layero).find("iframe")[0].contentWindow.resetAll();
			  
			  var refBtn = layer.getChildFrame("#refreshDetailTable",index);
			  refBtn.click();
			  return false;
		  }
		});
	layer.full(iframe);
}

function refresh(){
	var options = $('#baseTable').bootstrapTable('refresh', {
		query : {
			typeId : $("#typeId").val()
		}
	});
}
function deleteList(ids){
	$.ajax({
		type:'post',
		url:ctx+"/telegraph/templ/delete",
		async:true,
		data:{
			'ids':ids
		},
		dataType:'text',
		success:function(msg){
			layer.close();
			 $('#baseTable').bootstrapTable('refresh');
		}
	});
}



function refreshTree() {
	$.getJSON(ctx +"/telegraph/templ/getTree", function(data) {			
		$.fn.zTree.init($("#ztree"), setting, data).expandAll(true);
	}).error(function(){
	});
}

