$(function(){
	var layer;
	var clickRowId = "";

	layui.use(['form','layer'],function(){
		layer = layui.layer;
	});
	
	jQuery.fn.bootstrapTable.columnDefaults.sortable = true;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	jQuery.fn.bootstrapTable.columnDefaults.editable = {toggle:'dblclick'};
	var tableOptions = {
	url: ctx+"/message/subscribe/subscribeList", //请求后台的URL（*）
	    method: "get",					  				 //请求方式（*）
	    dataType: "json",
		striped: true,									 //是否显示行间隔色
	    cache: true,
	    undefinedText:'',
	    checkboxHeader:false,
//	    toolbar:$("#tool-box"),
	    sortName:"crtime",
	    sortOrder:"desc",
	    search:false,
	    pagination: true,
	    sidePagination: 'server',
	    pageNumber: 1,
	    pageSize: 10,
	    pageList: [10, 15, 20],
	    queryParamsType:'',
	    queryParams: function (params) {
	        return {
		        disablename : $("select[name='disable']").val(),
				hbiotypename : $("input[name='hbiotypename']").val(),
				flightnumber : $("input[name='flightnumber']").val(),
				jobKind : $("#jobKind").val(),
				event : $("input[name='event']").val(),
				crtime : $("input[name='crtime']").val(),
	        	pageNumber: params.pageNumber,    
                pageSize: params.pageSize,
                sortName:params.sortName,
                sortOrder:params.sortOrder
	        }
	    },
	    columns: [
			{field: "id",title:"序号",sortable:false,editable:false,
				formatter:function(value,row,index){
					return index+1;
				}
			},
			{field: "checkbox",checkbox:true,sortable:false,editable:false},
			{field: "hbeventname", title: "触发事件",editable:false},
			{field: "kindname", title: "保障类型",editable:false},
			{field: "flightnumber", title: "航班号",editable:false},
			{field: "hbiotypename", title: "进出类型",editable:false},
			{field: "mtemplid", title: "模板名称",editable:false},
			{field: "mtext", title: "内容",editable:false},
			{field: "totypename", title: "接收类型",editable:false},
			{field: "crtime", title: "创建时间",editable:false},
			{field: "disablename", title: "状态",editable:false},
			{field: "mj", title: "操作",sortable:false,editable:false,
				formatter:function(value,row,index){
//					return "</i>&nbsp;&nbsp;&nbsp;<i class='fa fa-edit' onclick='modefy("+row.id+")'>"+
//					"</i>&nbsp;&nbsp;&nbsp;<i class='fa fa-eye' onclick='deleteSubscribe(\""+row.id+"\",\""+row.ruleid+"\")'></i>";
					

					return "<i class='fa fa-edit' onclick='modefy("+row.id+")'>"+
					"</i>";
			}}
	    ],
	    onDblClickRow:onDblClickRow
	};
	function onDblClickRow(row,tr,field){
		var id=row.id;
		modefy(id);
	}
	$("#baseTable").bootstrapTable(tableOptions);

	//新增
	$('#create').click(function() {
		iframe = layer.open({
			  type: 2, 
			  title:false,
			  resize : false,
			  closeBtn:false,
			  area:[$("body").width()-200+"px",$("body").height()-200+"px"],
			  content: ctx+"/message/subscribe/getdetailPage?flag=add",
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
				  var old_colids = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_colids").value;
				  var old_drlStr = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_drlStr").value;
				  var old_drools = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_drools").value;
				  var old_condition = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_condition").value;
				  $(layero).find("iframe")[0].contentWindow.document.getElementById("colids").value = old_colids;
				  $(layero).find("iframe")[0].contentWindow.document.getElementById("drlStr").value = old_drlStr;
				  $(layero).find("iframe")[0].contentWindow.document.getElementById("drools").value = old_drools;
				  $($(layero).find("iframe")[0].contentWindow.document.getElementById("condition")).html(old_condition);

				  var old_varcols = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_varcols").value;
				  var old_mtext = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_mtext").value;
				  $(layero).find("iframe")[0].contentWindow.document.getElementById("varcols").value = old_varcols;
				  $($(layero).find("iframe")[0].contentWindow.document.getElementById("mtext")).html(old_mtext);
				  
				  var mfromtype = $(layero).find("iframe")[0].contentWindow.document.getElementById("mfromtype").value;
				  var torangenames = $(layero).find("iframe")[0].contentWindow.document.getElementById("torangenames");
				  console.info(mfromtype);
				  if(mfromtype==-1){
					  $(torangenames).attr("disabled",true);   
			      }else{
			         $(torangenames).attr("disabled",false);
				  }
				  
				  var refBtn = layer.getChildFrame("#refreshDetailTable",index);
				  refBtn.click();
				  return false;
			  }
			});
		layer.full(iframe);
	});

	
	//删除
	$('#remove').click(function() {
		if($("#baseTable").bootstrapTable("getSelections").length==0){
			layer.msg('请选择要删除的消息', {
				icon : 2
			});
		}else{
			layer.confirm("是否删除选中的消息？",{offset:'100px'},function(index){
				var ids = $.map($('#baseTable').bootstrapTable('getSelections'), function (row) {
	                return row.id;
	            });
				var ruleids = $.map($('#baseTable').bootstrapTable('getSelections'), function (row) {
	                return row.ruleid;
	            });
				var jobids = $.map($('#baseTable').bootstrapTable('getSelections'), function (row) {
	                return row.jobId;
	            });
				deleteSubscribeList(ids,ruleids,jobids);
				layer.close(index);
			});
		}
	});

});
function deleteSubscribe(ids,ruleids,jobids){
	layer.confirm("是否删除选中的消息？",{offset:'100px'},function(index){
		deleteSubscribeList(ids,ruleids,jobids);
		layer.close(index);
	});
}

function modefy(id){
//	var id=$("#baseTable").bootstrapTable("getSelections")[0].id;
	iframe = layer.open({
		type: 2, 
		  title:false,
		  resize : false,
		  area:[$("body").width()-200+"px",$("body").height()-200+"px"],
		  closeBtn:false,
		  content:  ctx+"/message/subscribe/getdetailPage?id="+id+"&flag=upd",
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
			  
			  var old_colids = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_colids").value;
			  var old_drlStr = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_drlStr").value;
			  var old_drools = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_drools").value;
			  var old_condition = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_condition").value;
			  $(layero).find("iframe")[0].contentWindow.document.getElementById("colids").value = old_colids;
			  $(layero).find("iframe")[0].contentWindow.document.getElementById("drlStr").value = old_drlStr;
			  $(layero).find("iframe")[0].contentWindow.document.getElementById("drools").value = old_drools;
			  $($(layero).find("iframe")[0].contentWindow.document.getElementById("condition")).html(old_condition);
			  
			  var old_varcols = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_varcols").value;
			  var old_mtext = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_mtext").value;
			  $(layero).find("iframe")[0].contentWindow.document.getElementById("varcols").value = old_varcols;
			  $($(layero).find("iframe")[0].contentWindow.document.getElementById("mtext")).html(old_mtext);
			  
			  var mfromtype = $(layero).find("iframe")[0].contentWindow.document.getElementById("mfromtype").value;
			  var torangenames = $(layero).find("iframe")[0].contentWindow.document.getElementById("torangenames");
			  console.info(mfromtype);
			  if(mfromtype==-1){
				  $(torangenames).attr("disabled",true);   
		      }else{
		         $(torangenames).attr("disabled",false);
			  }
			  
			  var refBtn = layer.getChildFrame("#refreshDetailTable",index);
			  refBtn.click();
			  return false;
		  }
		});
	layer.full(iframe);
}

function deleteSubscribeList(ids,ruleids,jobids){	
	$.ajax({
		type:'post',
		url:ctx+"/message/subscribe/delete",
		async:true,
		data:{
			'ids':ids,
			'ruleids':ruleids,
			'jobids':jobids
		},
		dataType:'text',
		success:function(msg){
			layer.msg(msg, {icon : 1,time:1000});
			$('#baseTable').bootstrapTable('refresh');
		}
	});
}

//查询功能
$(".search").click(function() {
	refresh();
})
function refresh(){

	$('#baseTable').bootstrapTable('refresh', {
		query : {
	        disablename : $("select[name='disable']").val(),
			hbiotypename : $("input[name='hbiotypename']").val(),
			flightnumber : $("input[name='flightnumber']").val(),
			crtime : $("input[name='crtime']").val()
			
		}
	});
}