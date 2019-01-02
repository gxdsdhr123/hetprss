$(function(){
	var layer;
	var clickRowId = "";
	layui.use(['form','layer'],function(){
		layer = layui.layer;
	});
	
	jQuery.fn.bootstrapTable.columnDefaults.sortable = true;
	jQuery.fn.bootstrapTable.columnDefaults.align = "left";
	jQuery.fn.bootstrapTable.columnDefaults.editable = {toggle:'dblclick'};
	var tableOptions = {
	url: ctx+"/telegraph/auto/autoList", //请求后台的URL（*）
	    method: "get",					  				 //请求方式（*）
	    dataType: "json",
		striped: true,									 //是否显示行间隔色
	    cache: true,
	    undefinedText:'',
	    checkboxHeader:false,
//	    toolbar:$("#tool-box"),
	    sortName:"CREATETIME",
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
	        	tg_name : $("input[name='tg_name']").val(),
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
			{field: "ID", title: "ID",editable:false,visible:false},
			{field: "EVENT_NAME", title: "触发事件",editable:false},
			{field: "DESCRIPTION_CN", title: "航空公司",editable:false},
			{field: "TG_NAME", title: "模板名称",editable:false},
			{field: "FIO_TYPE", title: "进出港",editable:false},
			{field: "TG_TEXT", title: "内容",editable:false,
				formatter:function(value,row,index){
					return row.TG_TEXT ;
				}
			},
			{field: "NAME", title: "创建人",editable:false},
			{field: "CREATETIME", title: "创建时间",editable:false},
			{field: "SEND_TYPE_NAME", title: "发送方式",editable:false},
			{field: "mj", title: "操作",sortable:false,editable:false,
				formatter:function(value,row,index){
					return "<i class='fa fa-edit' onclick='modefy("+row.ID+")'></i>";
				}
			}
	    ],
	    onDblClickRow:onDblClickRow
	};
	function onDblClickRow(row,tr,field){
		var id=row.ID;
		modefy(id);
	}
	$("#baseTable").bootstrapTable(tableOptions);
	//新增
	$('#create').click(function() {
		iframe = layer.open({
			  type: 2, 
			  title:false,
			  closeBtn:false,
			  area:[$("body").width()-200+"px",$("body").height()-200+"px"],
			  content: ctx+"/telegraph/auto/info?flag=add",
			  btn:["保存","重置","返回"],
			 
			  yes:function(index, layero){
				 var result = $(layero).find("iframe")[0].contentWindow.save();
				 if(result){
					 layer.closeAll('iframe');
					 $('#baseTable').bootstrapTable('refresh');
				 }
			  },
			  btn2:function(index, layero){
				 
				  layer.getChildFrame("#createForm",index)[0].reset();

				  var old_colids = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_colids").value;
				  var old_expression = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_expression").value;
				  var old_drools = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_drools").value;
				  var old_condition = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_condition").value;
				  $(layero).find("iframe")[0].contentWindow.document.getElementById("colids").value = old_colids;
				  $(layero).find("iframe")[0].contentWindow.document.getElementById("expression").value = old_expression;
				  $(layero).find("iframe")[0].contentWindow.document.getElementById("drools").value = old_drools;
				  $($(layero).find("iframe")[0].contentWindow.document.getElementById("condition")).html(old_condition);

				  var old_varcols = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_varcols").value;
				  var old_mtext = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_mtext").value;
				  $(layero).find("iframe")[0].contentWindow.document.getElementById("varcols").value = old_varcols;
				  $($(layero).find("iframe")[0].contentWindow.document.getElementById("mtext")).html(old_mtext);
				  
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
	                return row.ID;
	            });
				deleteList(ids);
				layer.close(index);
			});
		}
	});
});	
function deleteSubscribe(ids,ruleids){
	layer.confirm("是否删除选中的消息？",{offset:'100px'},function(index){
		deleteList(ids,ruleids);
		layer.close(index);
	});
}

function modefy(id){
	iframe = layer.open({
		type: 2, 
		  title:false,
		  closeBtn:false,
		  area:[$("body").width()-200+"px",$("body").height()-200+"px"],
		  content:  ctx+"/telegraph/auto/info?id="+id+"&flag=upd",
		  btn:["保存","重置","返回"],
		  yes:function(index, layero){
			  var result = $(layero).find("iframe")[0].contentWindow.save();
			  if(result){
				  layer.closeAll('iframe');
				  $('#baseTable').bootstrapTable('refresh');
			  }
		  },
		  btn2:function(index, layero){
			
			  layer.getChildFrame("#createForm",index)[0].reset();

			  var old_colids = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_colids").value;
			  var old_expression = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_expression").value;
			  var old_drools = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_drools").value;
			  var old_condition = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_condition").value;
			  $(layero).find("iframe")[0].contentWindow.document.getElementById("colids").value = old_colids;
			  $(layero).find("iframe")[0].contentWindow.document.getElementById("expression").value = old_expression;
			  $(layero).find("iframe")[0].contentWindow.document.getElementById("drools").value = old_drools;
			  $($(layero).find("iframe")[0].contentWindow.document.getElementById("condition")).html(old_condition);

			  var old_varcols = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_varcols").value;
			  var old_mtext = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_mtext").value;
			  $(layero).find("iframe")[0].contentWindow.document.getElementById("varcols").value = old_varcols;
			  $($(layero).find("iframe")[0].contentWindow.document.getElementById("mtext")).html(old_mtext);
			  
			  var refBtn = layer.getChildFrame("#refreshDetailTable",index);
			  refBtn.click();
			  return false;
		  }
		});
	layer.full(iframe);
}

function deleteList(ids,ruleids){	
	$.ajax({
		type:'post',
		url:ctx+"/telegraph/auto/delete",
		async:true,
		data:{
			'ids':ids,
			'ruleids':ruleids
		},
		dataType:'text',
		
		success:function(msg){
			layer.close();
			 $('#baseTable').bootstrapTable('refresh');
		}
	});
}

function submitCheck() {
	var hbevent = $("select[name=hbevent]").val();
	var schtime = $("input[name=schtime]").val();
	var condition = $("textarea[name=condition]").text();
	var flightnumber = $("input[name=flightnumber]").val();
	var hbiotype = $("select[name=hbiotype]").val();
	var mtemplid = $("input[name=mtemplid]").val();
	var disable = $("input[name=disable]").val();
	var mtext = $("textarea[name=mtext]").text();
	var totype = $("#mfromtype").val();
	var torangenames = $("select[name=torangenames]").val();
	var ifsms = $("input[name=ifsms]").prop("checked")== true?"1":"0";
	
	var fu = new ChkUtil();
	var res = '';

	res = fu.checkFormInput(hbevent, true,'',50);
	if (res.length > 0) {
		var msg = '[触发类型]合法性检查错误,' + res;
		layer.msg(msg, {icon: 2,time: 1000});
		document.all("input[name=hbevent]").focus();
		return false;
	}
	res = fu.checkFormInput(schtime, true,'',50);
	if (res.length > 0) {
		var msg = '[发送时间]合法性检查错误,' + res;
		layer.msg(msg, {icon: 2,time: 1000});
		document.all("input[name=schtime]").focus();
		return false;
 	}
	res = fu.checkFormInput(condition, true,'',50);
	if (res.length > 0) {
		var msg = '[触发条件]合法性检查错误,' + res;
		layer.msg(msg, {icon: 2,time: 1000});
		document.all("textarea[name=condition]").focus();
		return false;
 	}		 
	res = fu.checkFormInput(flightnumber, false,'int',2);
	if (res.length > 0) {
		var msg = '[航班号]合法性检查错误,' + res;
		layer.msg(msg, {icon: 2,time: 1000});
		document.all("input[name=flightnumber]").focus();
		return false;
	}
				 
	res = fu.checkFormInput(hbiotype, false,'int',4);
	if (res.length > 0) {
		var msg = '[进出港]合法性检查错误,' + res;
		layer.msg(msg, {icon: 2,time: 1000});
		document.all("input[name=hbiotype]").focus();
		return false;
	}
	res = fu.checkFormInput(mtemplid, false,'int',4);
	if (res.length > 0) {
		var msg = '[模板名称]合法性检查错误,' + res;
		layer.msg(msg, {icon: 2,time: 1000});
		document.all("input[name=mtemplid]").focus();
		return false;
	}
	res = fu.checkFormInput(disable, false,'',50);
	if (res.length > 0) {
		var msg = '[启用状态]合法性检查错误,' + res;
		layer.msg(msg, {icon: 2,time: 1000});
		document.all("input[name=disable]").focus();
		return false;
	}
	res = fu.checkFormInput(mtext, false,'int',4);
	if (res.length > 0) {
		var msg = '[消息正文]合法性检查错误,' + res;
		layer.msg(msg, {icon: 2,time: 1000});
		document.all("input[name=mtext]").focus();
		return false;
	}
	res = fu.checkFormInput(totype, false,'',50);
	if (res.length > 0) {
		var msg = '[接收人类型]合法性检查错误,' + res;
		layer.msg(msg, {icon: 2,time: 1000});
		document.all("input[name=totype]").focus();
		return false;
	}
	res = fu.checkFormInput(torangenames, false,'int',2);
	if (res.length > 0) {
		var msg = '[接收人]合法性检查错误,' + res;
		layer.msg(msg, {icon: 2,time: 1000});
		document.all("input[name=torangenames]").focus();
		return false;
	}
	res = fu.checkFormInput(ifsms, false,'int',2);
	if (res.length > 0) {
		var msg = '[短信]合法性检查错误,' + res;
		layer.msg(msg, {icon: 2,time: 1000});
		document.all("input[name=ifsms]").focus();
		return false;
	}
	return true;
}


//向导赋值至消息正文
function getAllValue() {
	
	var val=$("#condition1").val();
	var a=$("#condition").val();
	var b=a+" ["+val+"] ";
	$("#condition").val(b);	 
}
//向导运算获取值方法
function getValue (val) {
	var a=$("#condition1").val();
	var b=a+" "+val;
	$("#condition1").val(b);	 
	}
//查询功能
$(".search").click(function() {
	var options = $('#baseTable').bootstrapTable('refresh', {
		query : {
	        disablename : $("select[name='disable']").val(),
			hbiotypename : $("input[name='hbiotypename']").val(),
			flightnumber : $("input[name='flightnumber']").val(),
			crtime : $("input[name='crtime']").val()
			
		}
	});
})
