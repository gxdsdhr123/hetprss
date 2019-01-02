var msg_1 = "请选择消息类型";
	var msg_2 = "请选择消息模板";
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
				 $("#typeId").val(treeNode.id);				
				 refreshTable();
			}
		}
	};

	layui.use('layer');
	function refreshTree() {
		$.getJSON(ctx +"/message/type/getMessageType", function(data) {
			$.fn.zTree.init($("#ztree"), setting, data).expandAll(true);
		});
	}
	$(function(){
		refreshTree();
		$('#addType').click(function() {
			layui.use('layer',function(){
				var layer = layui.layer;
				var createIframe = layer.open({
					  type: 2, 
					  title:'新增类型',
					  maxmin:false,
					  shadeClose: true,
					  area: ["480px","400px"],
					  content: ctx + "/message/type/info?flag=add",
					  btn:["保存","重置","返回"],
					  yes:function(index, layero){
						  $(layero).find("iframe")[0].contentWindow.save();
					  },
					  btn2:function(index, layero){
						  $(layero).find("iframe")[0].contentWindow.document.getElementById("createForm").reset();
						  return false;
					  }
					});
// 				layer.full(createIframe);
			});
		});
		$('#updType').click(function() {
			var treeObj = $.fn.zTree.getZTreeObj("ztree");
			var nodes = treeObj.getSelectedNodes();
			if (nodes.length<=0) {
				layer.msg(msg_1,{icon:0,time: 1000});
			} else {
				var id = nodes[0].id;
				if( id == ""){
					layer.msg(msg_1,{icon:0,time: 1000});
				} else {
					layui.use('layer',function(){
						var layer = layui.layer;
						var createIframe = layer.open({
							  type: 2, 
							  title:'修改类型',
							  maxmin:false,
							  shadeClose: true,
							  area: ["500px","400px"],
							  content: ctx + "/message/type/info?flag=upd&id="+id,
							  btn:["保存","重置","返回"],
							  yes:function(index, layero){
								  $(layero).find("iframe")[0].contentWindow.save();
							  },
							  btn2:function(index, layero){
								  $(layero).find("iframe")[0].contentWindow.document.getElementById("createForm").reset();
								  return false;
							  }
							});
					});
				}
			}
		});
		$('#delType').click(function() {
			var treeObj = $.fn.zTree.getZTreeObj("ztree");
			var nodes = treeObj.getSelectedNodes();
			if (nodes.length<=0) {
				layer.msg(msg_1,{icon:0,time: 1000});
			} else {
				var id = nodes[0].id;
				if( id == ""){
					layer.msg(msg_1,{icon:0,time: 1000});
				} else {
					var name = nodes[0].name;
					layer.confirm('是否删除指令类型：' + name, {
					  btn: ['确定','取消'] //按钮
					}, function(){
						$.ajax({
							type:'post',
							data:{
								id : id
							},
							url:ctx+"/message/type/delete",
							dataType:"text",
							success:function(result){
								layer.msg(result, {icon: 1,time: 1000});
								if(result=='操作成功')
									refresh();
							},
							error:function(msg){
								layer.msg('操作失败', {icon: 2,time: 1000});
							}
						});
					});
				}
			}
		});
		
		$('#addTempl').click(function() {
			var treeObj = $.fn.zTree.getZTreeObj("ztree");
			var nodes = treeObj.getSelectedNodes();
			if (nodes.length<=0) {
				layer.msg(msg_1,{icon:0,time: 1000});
			} else {
				var id = nodes[0].id;
				if( id == ""){
					layer.msg(msg_1,{icon:0,time: 1000});
				} else {
					
					layui.use('layer',function(){
						var layer = layui.layer;
						var createIframe = layer.open({
							  type: 2, 
							  title:'新增模板',
							  maxmin:false,
							  shadeClose: true,
							  area: ["800px","500px"],
							  content: ctx + "/message/templ/info?flag=add&mtype="+id,
							  btn:["保存","重置","返回"],
							  yes:function(index, layero){
								  $(layero).find("iframe")[0].contentWindow.save();
							  },
							  btn2:function(index, layero){
								  $(layero).find("iframe")[0].contentWindow.document.getElementById("createForm").reset();
								  var old_varcols = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_varcols").value;
								  var old_mtext = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_mtext").value;
								  $(layero).find("iframe")[0].contentWindow.document.getElementById("varcols").value = old_varcols;
								  $($(layero).find("iframe")[0].contentWindow.document.getElementById("mtext")).html(old_mtext);							  
								//是否回复							  
								  var ifreply = $($(layero).find("iframe")[0].contentWindow.document.getElementsByName("ifreply")).prop("checked");
								  var user_defreply = $(layero).find("iframe")[0].contentWindow.document.getElementsByClassName("user_defreply");
								  if(ifreply==true){
									  $(user_defreply).removeAttr("hidden");
									}else if(ifreply==false){
									  $(user_defreply).attr("hidden","hidden");
									}
								//语音内容
								  var ifsound = $($(layero).find("iframe")[0].contentWindow.document.getElementsByName("ifsound")).prop("checked");
								  var user_ifsound = $(layero).find("iframe")[0].contentWindow.document.getElementsByClassName("user_ifsound");
								  if(ifsound==true){
									  $(user_ifsound).removeAttr("hidden");
									}else if(ifsound==false){
									  $(user_ifsound).attr("hidden","hidden");
									}
								  var refBtn = layer.getChildFrame("#refreshDetailTable",index);
								  refBtn.click();
								  $(layero).find("iframe")[0].contentWindow.removeall();
								  return false;
								  
							  }
						});
						layer.full(createIframe);
					});
				}
			}
		});
		$('#delTempl').click(function() {
			
				if($("#mTable").bootstrapTable("getSelections").length==0){
					layer.msg('请选择要删除的消息', {	icon:0});
				}else{
						var ids = $.map($('#mTable').bootstrapTable('getSelections'), function (row) {
			                return row.ID;
			            });
						if( ids == ""){
							layer.msg(msg_2,{icon:0,time: 1000});
						} else {
							layer.confirm('是否删除指令模板?', {
							  btn: ['确定','取消'] //按钮
							}, function(){
								deleteList(ids);
							});
						}
				}
		});
		
			var layer;
			var clickRowId = "";

			layui.use(['form','layer'],function(){
				layer = layui.layer;
			});
			jQuery.fn.bootstrapTable.columnDefaults.sortable = true;
			jQuery.fn.bootstrapTable.columnDefaults.align = "center";
			jQuery.fn.bootstrapTable.columnDefaults.editable = {toggle:'dblclick'};
			//向导内下拉表
			var tableOptions = {
				url: ctx+"/message/templ/list", //请求后台的URL（*）
				    method: "get",					  				 //请求方式（*）
				    dataType: "json",
					striped: true,									 //是否显示行间隔色
				    cache: true,
				    undefinedText:'',
				    checkboxHeader:false,
				    toolbar:$("#tool-box"),
				    search :true,
				    pagination: true,
				    sidePagination: 'server',
				    pageNumber: 1,
				    pageSize: 10,
				    sortName:'MTIME',
				    sortOrder:'desc',
				    pageList: [10, 15, 20],
				    queryParamsType:'',
				    queryParams: function (params) {
				        return {
				    		typeId :$("#typeId").val(),
				        	pageNumber: params.pageNumber,    
			                pageSize: params.pageSize,
			                sortName:params.sortName,
			                sortOrder:params.sortOrder,
			                q:params.searchText
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
						{field: "MTTYPECN", title: "指令类别",editable:false},
						{field: "TEMPNAME", title: "模板名称",editable:false,align:"center"},
						{field: "MTITLE", title: "模板标题",editable:false,align:"center"},
						{field: "MTEXT", title: "模板正文",editable:false,
							formatter:function(value,row,index){
								return row.MTEXT ;
							}
						},
						{field: "IFFLIGHT", title: "航班",editable:false,
							formatter:function(value,row,index){
								return row.IFFLIGHT==1?"是":"否";
							}	
						
						},
						{field: "ISSYS", title: "系统",editable:false,
							formatter:function(value,row,index){
								return row.ISSYS==1?"是":"否";
							}	
						},
						{field: "MFTYPE", title: "消息源",editable:false,
							formatter:function(value,row,index){
								return row.MFTYPE==0?"源指令":"转发指令";
							}	
						},
						{field: "MTIME", title: "创建时间",editable:false},
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

			tableOptions.height = $("#mTables").height();
			$("#mTable").bootstrapTable(tableOptions);
	});
	
	function modefy(id){
		iframe = layer.open({
			type: 2, 
			  title:false,
			  closeBtn:false,
			  area:[$("body").width()-200+"px",$("body").height()-200+"px"],
			  content:  ctx+"/message/templ/info?flag=upd&id="+id,
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
					//是否回复							  
				  var ifreply = $($(layero).find("iframe")[0].contentWindow.document.getElementsByName("ifreply")).prop("checked");
				  var user_defreply = $(layero).find("iframe")[0].contentWindow.document.getElementsByClassName("user_defreply");
				  if(ifreply==true){
					  $(user_defreply).removeAttr("hidden");
					}else if(ifreply==false){
					  $(user_defreply).attr("hidden","hidden");
					}
				//语音内容
				  var ifsound = $($(layero).find("iframe")[0].contentWindow.document.getElementsByName("ifsound")).prop("checked");
				  var user_ifsound = $(layero).find("iframe")[0].contentWindow.document.getElementsByClassName("user_ifsound");
				  if(ifsound==true){
					  $(user_ifsound).removeAttr("hidden");
					}else if(ifsound==false){
					  $(user_ifsound).attr("hidden","hidden");
					}
				  var refBtn = layer.getChildFrame("#refreshDetailTable",index);
				  refBtn.click();
				  return false;
			  }
			});
		layer.full(iframe);
	}
	function deleteList(ids){
		$.ajax({
			type:'post',
			data:{
				id : ids.join(",")
			},
			url:ctx+"/message/templ/delete",
			dataType:"text",
			success:function(result){
				layer.msg(result, {icon: 1,time: 1000});
				$('#mTable').bootstrapTable('refresh');
			},
			error:function(msg){
				layer.msg('操作失败', {icon: 2,time: 1000});
			}
		});
	}
	function refresh(){
		location.href = ctx+"/message/type/list";
	}
	function refreshTable() {
		$('#mTable').bootstrapTable('refresh');
	}