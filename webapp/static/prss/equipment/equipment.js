var clickedPid = "";
var setting = {
		edit: {
			enable: true,
			removeTitle:"删除",
			renameTitle:"修改",
			showRemoveBtn: setRemoveBtn,
			showRenameBtn: setRenameBtn
		},
		
		data : {
			simpleData : {
				enable : true,
				idKey : "id",
				pIdKey : "pId",
				rootPId : '0'
			}
		},
		//页面上显示的效果
		view:{
			addHoverDom: addHoverDom,
			removeHoverDom: removeHoverDom,
			showLine:false
		},
	
		callback : {
			beforeDrag: beforeDrag,
			beforeRemove: beforeRemove,
			beforeEditName:beforeEditName,
			onRename: onRename,
			onRemove: onRemove,
			onClick : function(event, treeId, treeNode) {
				 clickedPid = treeNode.pId;
				 $("#typeId").val(treeNode.id);
				 $("#typeName").val(treeNode.name);
				 refreshTable();
			}
		}
	};

layui.use('layer');
function refreshTree() {
	$.getJSON(ctx +"/manage/equipment/treeData", function(data) {
		$.fn.zTree.init($("#ztree"), setting, data).expandAll(true);
	});
}
$(function(){
	
	//var car;
	//var typeName;
	
	refreshTree();
	
	var layer;
	var clickRowId = "";
	layui.use(['form','layer'],function(){
		layer = layui.layer;
	});
//	jQuery.fn.bootstrapTable.columnDefaults.sortable = true;
//	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
//	jQuery.fn.bootstrapTable.columnDefaults.editable = {toggle:'dblclick'};
	//向导内下拉表
	var tableOptionsJolin = {
			url: ctx+"/manage/equipment/list",
		    method: "get",					  				 //请求方式（*）
		    dataType: "json",
			striped: true,									 //是否显示行间隔色
		    cache: true,
		    undefinedText:'',
		    checkboxHeader:false,
		    toolbar:$("#tool-box"),
		    search :false,
		    pagination: true,
		    sidePagination: 'server',
		    pageNumber: 1,
		    pageSize: 10,
		    sortName:'DEVICE_ID',
		    sortOrder:'DESC',
		    pageList: [10, 15, 20],
		    queryParamsType:'',
		    queryParams: function (params) {	//参数
		    	var typeId = $("#typeId").val();
		    	var deptId = "";
		    	if(clickedPid=="e2caa5db3b68"){
		    		deptId = typeId;
		    		typeId = "";
		    	}
		        return {
		        	deptId:deptId,
		    		typeId :typeId,
		        	pageNumber: params.pageNumber,    
	                pageSize: params.pageSize,
	                sortName:params.sortName,
	                sortOrder:params.sortOrder
		        }
		    },
		   /* onCheck : function onCheck(row, tr, field) {
				console.log("选中了当前数据");
				car = row.CAR_NUMBER;
				console.log(car);
			},*/
		    columns:[
					{field: "seq",title:"序号",sortable:false,editable:false,
						formatter:function(value,row,index){
							return index+1;
						}
					},
					{field: "checkbox",checkbox:true,sortable:false,editable:false},
					{field: "DEVICE_ID", title: "DEVICE_ID",editable:false,visible:false},
					{field: "TYPE_NAME", title: "设备类型",editable:false},
					{field: "DEVICE_NO", title: "设备编号",editable:false},
					{field: "DEVICE_MODEL", title: "设备型号",editable:false},
					{field: "CAR_NUMBER", title: "车牌号",editable:false},
					{field: "INNER_NUMBER", title: "自编号",editable:false},
					{field: "SEATING_NUM", title: "核载人数",formatter : function(value, row, index) {return value?value:'-';},editable:false},
					{field: "DEVICE_STATUSS", title: "设备状态",editable:false},
					{field: "REMARK", title: "备注",editable:false},
					{field: "NAME", title: "创建人",editable:false},
					{field: "CREATE_DATEI", title: "创建时间",editable:false},
		           ],
//		    onDblClickRow:onDblClickRow
		    
	};
//	function onDblClickRow(row,tr,field){
//		var id=field.DEVICE_ID;
//		return id;
//	}
	//筛选
	$("#batchAddBtn").click(function(){
		doBatchAdd();
	});
	
	//新增
	$("#batchAddBtnNew").click(function(){
		doBatchAddNew();
	});
	
	//修改
	$("#batchAddBtnRevamp").click(function(){
		if($("#mTable").bootstrapTable("getSelections").length==0){
			layer.msg('请选择设备列表', {	icon:0});
		}else{
			var ids = $.map($('#mTable').bootstrapTable('getSelections'), function (row) {
	            return row.DEVICE_ID;
	        });
			var arr = [];
			arr = ids;
			if(ids == ""){
				layer.msg(msg_2,{icon:0,time: 1000});
			}else if(arr.length!=1){
				layer.msg("每次只能选择一个修改", {icon: 1,time: 2000});
			}else if(arr.length==1){
//				layer.confirm('是否修改？',{
//					btn: ['确定','取消']
//				},function(){
//					doBatchAddRevamp(ids);
//				});
				doBatchAddRevamp(ids);
			}
		}
	});
	
	//删除
	$("#batchAddBtnDel").click(function(){
		if($("#mTable").bootstrapTable("getSelections").length==0){
			layer.msg('请选择设备列表', {	icon:0});
		}else{
			var ids = $.map($('#mTable').bootstrapTable('getSelections'), function (row) {
	            return row.DEVICE_ID;
	        });
			if(ids == ""){
				layer.msg(msg_2,{icon:0,time: 1000});
			}else{
				layer.confirm('是否删除?',{
					btn: ['确定','取消'],icon:3 //按钮
				},function(){
					doBatchAddDel(ids);
				});
			}
		}
	});
	
	//tableOptionsJolin.height = $("#mTables").height();//定义表格的高度
	$("#mTable").bootstrapTable(tableOptionsJolin);	
	
});

function refreshTable() {
	$('#mTable').bootstrapTable('refresh');
}
function beforeDrag(treeId, treeNodes){
	return false;
}
var newCount = 0;
function addHoverDom(treeId, treeNode) {
	var sObj = $("#" + treeNode.tId + "_span"); //获取节点信息  
	if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;  
	var addStr = 
		"<span class='button add' id='addBtn_" + treeNode.tId + "' title='新增' onfocus='this.blur();'></span>"; //定义添加按钮
	if(treeNode.name== '设备类型'){
		return false;
	}
	sObj.after(addStr); //加载添加按钮 
	var btn = $("#addBtn_"+treeNode.tId); 
	
	//绑定添定义添加操作 
	if (btn) btn.bind("click", function(){
		$("#gufanglong123").val("");

		$(".select2-selection__rendered").empty();
		$(".select2-selection__rendered").append($("<span class='select2-selection__placeholder'>请选择</span>"));
		$(".select2-selection__rendered").val("");
		
		layer.open({
			type : 1,
			title : "新增",
			//area : [ "800px", "300px" ],
			area : 'auto',
			maxmin : false,
			shadeClose : true,
			content : $("#filterDouJQK"),
			btn : [ "确定", "取消" ],
			yes :function(index){
				var typeName = $("#filterDouJQK .typeName").val();
				var reskind =  $("#filterDouJQK .reskind").val();
				if(typeName=="" || typeName==null){
					layer.msg("请输入设备类型", {icon:0,time: 2000});
					return false;
				}
				if(reskind=="" || reskind==null){
					layer.msg("请选择保障类型", {icon:0,time: 2000});
					return false;
				}
				$.ajax({
					url:ctx+"/manage/equipment/newNode",
					type : "POST",
					async : false,
					data :{
						//name:"new"+newCount,
						name:typeName,
						pid:treeNode.id,
						reskind:reskind
					},
					success:function(result){
						if(result==1){
							layer.close(index);
							window.location.reload();
							layer.msg("成功", {icon: 1,time: 1000});
							return false;
						}
//						if(result==1){
//							var zTree = $.fn.zTree.getZTreeObj("ztree");
//							zTree.addNodes(treeNode, {id:(100 + newCount), pId:treeNode.id, name:"new" + (newCount)});
//							window.location.reload();
//							return false;
//						}
					}
					
				});
			},
		
			btn2 : function(index){
				$(".select2-selection__rendered").empty();
				$(".select2-selection__rendered").append($("<span class='select2-selection__placeholder'>请选择</span>"));
				$("#filterDouJQK")[0].reset();
				layer.close(index);
			}
		
		});
		//newCount++;
//		$.ajax({
//			url:ctx+"/sys/Equipment/newNode",
//			type : "POST",
//			async : false,
//			data :{
//				//name:"new"+newCount,
//				name:"node",
//				pid:treeNode.id
//			},
//			success:function(result){
//				if(result==1){
//					window.location.reload();
//					return false;
//				}
////				if(result==1){
////					var zTree = $.fn.zTree.getZTreeObj("ztree");
////					zTree.addNodes(treeNode, {id:(100 + newCount), pId:treeNode.id, name:"new" + (newCount)});
////					window.location.reload();
////					return false;
////				}
//			}
//			
//		});
		return false;
	});
	
	
	
}
function removeHoverDom(treeId, treeNode){
	$("#addBtn_"+treeNode.tId).unbind().remove();
	
}


function beforeEditName(treeId, treeNode){
	var fluag = false;
	 layer.open({	
		type : 2,
		title : "修改",
		//area : 'auto',
		area : [ "500px", "350px" ],
		maxmin : false,
		shadeClose : true,
		content : ctx+"/manage/equipment/editName?id="+treeNode.id,
		btn : [ "确定", "取消" ],
		yes :function(index){
			if(typeof(typeNameI) == "undefined" || typeof(reskindI) == "undefined"){
				var names = nameI;
				if(names=="" || names==null){
					layer.msg("请输入部门名称", {icon:0,time: 2000});
					return false;
				}
				
				$.ajax({
					url:ctx+"/manage/equipment/renameDept",
					type : "POST",
					async : false,
					data :{
						id:treeNode.id,
						name:names,
						
					},
					success:function(result){
						if(result==1){
							layer.close(index);
							window.location.reload();
							layer.msg("成功", {icon: 1,time: 1000});
							return false;
						}
					}
				});
				
				return false;
			}
			
			var typeNames = typeNameI;
			var reskinds =  reskindI;
			if(typeNames=="" || typeNames==null){
				layer.msg("请输入设备类型", {icon:0,time: 2000});
				return false;
			}
			if(reskinds=="" || reskinds==null){
				layer.msg("请选择保障类型", {icon:0,time: 2000});
				return false;
			}
			$.ajax({
				url:ctx+"/manage/equipment/renameNode",
				type : "POST",
				async : false,
				data :{
					typeId:treeNode.id,
					typeName:typeNames,
					reskind:reskinds
					
				},
				success:function(result){
					if(result==1){
						layer.close(index);
						window.location.reload();
						layer.msg("成功", {icon: 1,time: 1000});
						return false;
						
					}
				}
				
			});
			
			fluag = true;
		},
		btn2 : function(index){
			layer.close(index);
		}
	});
	
	 return fluag;
	
}
function onRename(event, treeId, treeNode){
	/*$.ajax({
		url:ctx+"/sys/Equipment/renameNode",
		type : "POST",
		async : false,
		data :{
			typeId:treeNode.id,
			typeName:typeName
		},
		success:function(result){
			if(result==1){
				window.location.reload();
				return false;
				
			}
		}
		
	})*/
	
//	$.ajax({
//		url:ctx+"/manage/equipment/renameNode",
//		type : "POST",
//		async : false,
//		data :{
//			typeId:treeNode.id,
//			typeName:treeNode.name
//		},
//		success:function(result){
//			if(result==1){
//				window.location.reload();
//				return false;
//				
//			}
//		}
//		
//	})
};

function beforeRemove(treeId, treeNode){
	debugger
	var flag = false;
	
	layer.confirm('确认删除 '+ treeNode.name+'吗 ?',{btn: ['确定','取消'],icon:3},function(){
		
		flag = true;
		onRemove(treeId, treeNode);
		
	});
	
	return flag;
	
};
function onRemove(treeId, treeNode){
	$.ajax({
		url:ctx+"/manage/equipment/RemoveNode",
		type : "POST",
		async : false,
		data :{
			typeId:treeNode.id,
		},
		success:function(result){
			if(result!=0){
				layer.msg("成功", {icon: 1,time: 1000});
				window.location.reload();
			}else{
				layer.msg("请你重新操作", {icon: 1,time: 2000});
			}
		}
		
	})
}

function setRemoveBtn(treeId, treeNode){
	return treeNode.name!='设备类型' && treeNode.pId!='e2caa5db3b68';
}

function setRenameBtn(treeId, treeNode){
	return treeNode.name!='设备类型'&& treeNode.pId!='e2caa5db3b68';
}

function doBatchAdd(){
	layer.open({
		type : 1,
		title : "筛选",
		area : [ "800px", "300px" ],
		maxmin : false,
		shadeClose : true,
		content : $("#filterDou123"),
		btn : [ "确定", "取消" ],
		yes : function(index) {
			var deviceNo = $("#filterDou123 .deviceNo").val();	//设备编号 deviceNo
			var deviceModel = $("#filterDou123 .deviceModel").val();	//设备型号 deviceModel
			var carNumber = $("#filterDou123 .carNumber").val();	//车牌号 carNumber
			$.ajax({
				url: ctx+"/manage/equipment/list",
				type : 'post',
				async : true,
				data : {"deviceNo":deviceNo,"deviceModel":deviceModel,"carNumber":carNumber,"pageSize": 10,"pageNumber":1,"typeId":0,"sortName":"DEVICE_ID","sortOrder":"DESC"},
				success : function(result){
					$('#mTable').bootstrapTable('load', result);
					//$("#filterDou123")[0].reset();
					$(".select2-selection__rendered").empty();
					$(".select2-selection__rendered").append($("<span class='select2-selection__placeholder'>请选择</span>"));
				}
			});		
			layer.close(index);
		},
		btn2 : function(index){
			$(".select2-selection__rendered").empty();
			$(".select2-selection__rendered").append($("<span class='select2-selection__placeholder'>请选择</span>"));
			$("#filterDou123")[0].reset();
			//return false;
			layer.close(index);
		}
		
	});
}

var id = $("#typeId").val();
function doBatchAddNew(){
	$('#douxf').val('0');
	var name = $("#typeName").val();
	$("#filterDou456 .typeName").val(name);
	if(name=='0'){
		layer.msg("请选择设备类型", {icon:0,time: 2000});
		return false;
	}else if(clickedPid==""||clickedPid=="e2caa5db3b68"){
		layer.msg("请选择设备类型", {icon:0,time: 2000});
		return false;
	}

	layer.open({
		type : 1,
		title : "新增",
		area : [ "800px", "500px" ],
		maxmin : false,
		shadeClose : true,
		content : $("#filterDou456"),
		btn : [ "保存", "取消" ],
		yes : function(index) {
			
			var typeName = $("#filterDou456 .typeName").val();
			var deviceNo = $("#filterDou456 .deviceNo").val();
			var deviceModel = $("#filterDou456 .deviceModel").val();
			var carNumber =  $("#filterDou456 .carNumber").val();
			var deviceStatus = $("#filterDou456 .deviceStatus").val();
			var innerNumber = $("#filterDou456 .innerNumber").val();
			var seatingNum = $("#filterDou456 .seatingNum").val();
			var remark = $("#filterDou456 .remark").val();
			if(deviceNo=="" || deviceModel=="" || carNumber=="" || deviceStatus==""){
				layer.msg("请输入必填项", {icon:0,time: 2000});
				return false;
			}
			if(deviceNo==null || deviceModel==null || carNumber==null || deviceStatus==null){
				layer.msg("请输入必填项", {icon:0,time: 2000});
				return false;
			}
			var en =  /[\u4e00-\u9fa5]/;
			if(en.test(deviceNo)){
				layer.msg("设备编号不允许输入汉字", {icon:0,time: 2000});
				return false;
			}
			$.ajax({
				url: ctx+"/manage/equipment/create",
				type : 'post',
				async : true,
				data:{"typeName":typeName,"deviceNo":deviceNo,"deviceModel":deviceModel,"carNumber":carNumber,"deviceStatus":deviceStatus,"seatingNum":seatingNum,"remark":remark,"typeId":$("#typeId").val(),"innerNumber":innerNumber},
				success:function(result){
					if(result!=0){
						layer.msg("成功", {icon: 1,time: 2000});
						$.get(ctx+"/manage/equipment/list",{ "pageSize":"10", "pageNumber":"1","sortName":"DEVICE_ID","sortOrder":"DESC","typeId":result},function(result){
							$('#mTable').bootstrapTable('load', result);
							$("#filterDou456 .typeName").val("");
							$("#filterDou456 .deviceNo").val("");
							$("#filterDou456 .deviceModel").val("");
							$("#filterDou456 .carNumber").val("");
							$("#filterDou456 .remark").val("");
							$("#filterDou456")[0].reset();
							$(".select2-selection__rendered").empty();
							$(".select2-selection__rendered").append($("<span class='select2-selection__placeholder'>请选择</span>"));
							
							
							
						});
						
						
					}
				},
			});
			layer.close(index);
		},
		btn2 : function(index){
			$("#filterDou456")[0].reset();
			$(".select2-selection__rendered").empty();
			$(".select2-selection__rendered").append($("<span class='select2-selection__placeholder'>请选择</span>"));
//			return false;
			layer.close(index);
		}
	});
}

function doBatchAddRevamp(ids){
	layer.open({
		type : 2,
		title : "修改",
		area : [ "800px", "500px" ],
		maxmin : false,
		shadeClose : true,
		content : ctx+"/manage/equipment/revampVal?id="+ids,
		btn : [ "保存", "取消" ],
		yes : function(index){
			var typeName = typeNameI;//typeNameI
			var deviceNo = deviceNoI;
			var deviceModel = deviceModelI;
			var carNumber =  carNumberI;
			var deviceStatus = deviceStatusI;
			var innerNumber = innerNumberI;
			var seatingNum = seatingNumI;
			var remark = remarkI;
			if(deviceNo=="" || deviceModel=="" || carNumber=="" ||deviceStatus==""){
				layer.msg("请输入必填项", {icon:0,time: 2000});
				return false;
			}
			if(deviceNo==null || deviceModel==null || carNumber==null ||deviceStatus==null){
				layer.msg("请输入必填项", {icon:0,time: 2000});
				return false;
			}
			$.ajax({
				url: ctx+"/manage/equipment/revamp",
				type : 'post',
				async : true,
				data:{"id" : ids.join(","),"typeName":typeName,"deviceNo":deviceNo,"deviceModel":deviceModel,"carNumber":carNumber,"deviceStatus":deviceStatus,"seatingNum":seatingNum,"remark":remark,"innerNumber":innerNumber},
				success:function(result){
					if(result==1){
						layer.msg("成功", {icon: 1,time: 2000});
						$('#mTable').bootstrapTable('refresh');
					}
				}
			});
			layer.close(index);
		},
		btn2 : function(index){
			$('#mTable').bootstrapTable('refresh');
			//return false;
			layer.close(index);
		}
	});
}

function doBatchAddDel(ids){
			$.ajax({
				url: ctx+"/manage/equipment/DelRevamp",
				type:'post',
				data:{
					id : ids.join(",")
				},
				success:function(result){
					if(result==1){
						layer.msg("成功", {icon: 1,time: 1000});
						$('#mTable').bootstrapTable('refresh');
					}
				},
				error:function(msg){
					layer.msg('操作失败', {icon: 2,time: 1000});
				}
			});
		
}


//---------------------

$('#filterDou123 .carNumber').select2({  
	placeholder: "请选择",  
    width:"140%",
    language : "zh-CN",
	templateResult : formatRepo,
	templateSelection : formatRepoSelection,
	escapeMarkup : function(markup) {
		return markup;
	}
});

$('#filterDou123 .deviceModel').select2({  
	placeholder: "请选择",  
    width:"140%",
    language : "zh-CN",
	templateResult : formatRepo,
	templateSelection : formatRepoSelection,
	escapeMarkup : function(markup) {
		return markup;
	}
});

$('#filterDou123 .deviceNo').select2({  
	placeholder: "请选择",  
    width:"140%",
    language : "zh-CN",
	templateResult : formatRepo,
	templateSelection : formatRepoSelection,
	escapeMarkup : function(markup) {
		return markup;
	}
});

//增加节点
$('#filterDouJQK .reskind').select2({  
	placeholder: "请选择",  
    width:"100%",
    language : "zh-CN",
	templateResult : formatRepo,
	templateSelection : formatRepoSelection,
	escapeMarkup : function(markup) {
		return markup;
	}
});

//新增节点
$('#filterDou456 .deviceStatus').select2({  
	placeholder: "请选择",  
    width:"100%",
    language : "zh-CN",
	templateResult : formatRepo,
	templateSelection : formatRepoSelection,
	escapeMarkup : function(markup) {
		return markup;
	}
});
	

function formatRepo(repo) {
		return repo.text;
	}

function formatRepoSelection(repo) {
	return repo.text;
}

















