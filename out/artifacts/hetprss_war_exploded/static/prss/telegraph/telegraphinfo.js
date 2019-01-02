var senderSitaType ='';
$(function(){
	var layer;
	var clickRowId = "";
	layui.use(['form','layer'],function(){
		layer = layui.layer;
	});
//	jQuery.fn.bootstrapTable.columnDefaults.sortable = true;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	jQuery.fn.bootstrapTable.columnDefaults.editable = {toggle:'dblclick'};
	var id = $("input[name=id]").val();
	
	//发送地址表格初始化
	initList("sendLists","sendList",ctx+"/telegraph/templ/getSenderList?id="+id);
	//接收地址表格	
	initList("receiveLists","receiveList",ctx+"/telegraph/templ/getReceiverList?id="+id);
	
	//新增发送人范围页面
	$('#addPage').click(function() {
		addPage('sendList');
	});
	
	//修改发送地址范围
	$('#updatePage').click(function() {
		updatePage('sendList');
	});
	
	//新增接收人范围
	$('#addPageOther').click(function() {
		addPage('receiveList');
	});
	
	//修改接收地址范围
	$('#updatePageOther').click(function() {
		updatePage('receiveList');
	});

	//删除发送人范围
	$('#delSender').click(function() {
		if($("#sendList").bootstrapTable("getSelections").length==0){
			layer.msg('请选择要删除的数据', {
				icon : 2
			});
		}else{
			layer.confirm("是否删除选中的数据？",{offset:'100px'},function(index){
				var checkboxs = $.map($('#sendList').bootstrapTable('getSelections'), function (row) {
	                return row.checkbox;
	            });
				
				$('#sendList').bootstrapTable('remove', {
			        field: 'checkbox',
			        values: checkboxs
			    });
				layer.close(index);
			});
		}
	});

	//删除接收人范围
	$('#delReveiver').click(function() {
		if($("#receiveList").bootstrapTable("getSelections").length==0){
			layer.msg('请选择要删除的数据', {
				icon : 2
			});
		}else{
			layer.confirm("是否删除选中的数据？",{offset:'100px'},function(index){
				var checkboxs = $.map($('#receiveList').bootstrapTable('getSelections'), function (row) {
	                return row.checkbox;
	            });
				
				$('#receiveList').bootstrapTable('remove', {
			        field: 'checkbox',
			        values: checkboxs
			    });
				layer.close(index);
			});
		}
		
		
	});
	
	//设置滚动条--begin
	var mtext = $("#mtext");
	mtext.css("position", "relative");
	new PerfectScrollbar(mtext[0]);
	//设置滚动条--end
})

function initList(div,table,url){
	var tableOptions = {
			url: url, //请求后台的URL（*）
		    method: "get",					  				 //请求方式（*）
		    dataType: "json",
			striped: true,									 //是否显示行间隔色
		    pagination: false,				   				 //是否显示分页（*）
		    cache: true,
		    undefinedText:'',
		    checkboxHeader:false,
		    toolbar:$("#tool-box"),
		    search:false,
		    queryParams:function(){
		    	var temp = {
		    			ifall:'no',
		    	    	num:'100'
		    	}
		    	return temp;
		    },
		    columns: [

				{field: "id",title:"序号",editable:false,
					formatter:function(value,row,index){
						return index+1;
					}
				},
				{field: "ind",visible:false},
				{field: "checkbox",checkbox:true,sortable:false,editable:false},
				{field: "sitaType",visible:false},
				{field: "sitaTypeName",visible:false},
				{field: "tg_site_id",visible:false},
				{field: "tg_site_name", title: "航空公司",editable:false},
				{field: "tg_address", title: "地址",editable:false},
				{field: "ifprocfrom", title: "发送触发处理",visible:false},
				{field: "ifprocfromname", title: "是否触发",editable:false},
				{field: "proceclsfrom", title: "触发类",visible:false},
				{field: "proceclsfromname", title: "触发类",editable:false},
				{field: "procdefparamfrom", title: "触发参数",editable:false},
				{field: "tid",visible:false}
		    ]

		};
		tableOptions.height = $("#" +div).height();
		$("#" + table).bootstrapTable(tableOptions);
		//隐藏列
		$('#' + table).bootstrapTable('hideColumn', '');
}


function addPage(tableName){
	if(tableName == 'sendList'){
		var data = $('#' + tableName).bootstrapTable('getData');
		if(data.length==1){
			layer.msg('发送地址只能为一条', {icon: 2,time: 1000});
			return false;
		}
	} else {
		var data = $('#sendList').bootstrapTable('getData');
		if(data.length==0){
			layer.msg('请先添加发送地址', {icon: 0,time: 1000});
			return false;
		}
	}
	var ids = $.map($('#' + tableName).bootstrapTable('getData'), function (row) {
        return row.tg_site_id;
    });
	
	parent.layer.open({
		type: 2, 
		title:'新增地址范围',
		maxmin:false,
//		shadeClose: true,
		area: ["550px","400px"],
		content: ctx + "/telegraph/templ/senderSite?ids=" + ids + '&senderSitaType=' + senderSitaType + '&tableName=' + tableName,
		btn:["保存","重置","返回"],
		yes:function(index, layero){
			var sitaType = $(layero).find("iframe")[0].contentWindow.document.getElementById('sitaType').value;
//			var sitaTypeName = $(layero).find("iframe")[0].contentWindow.document.getElementById('sitaType').options[sitaType].text;
			var sitaTypeName ='';
			if(sitaType==1){
				sitaTypeName = '邮件';
			} else {
				sitaTypeName = 'SITA';
			}
			if(tableName == 'sendList'){
				senderSitaType = sitaType;
			}
			var tg_site_id = $(layero).find("iframe")[0].contentWindow.document.getElementById("tg_site_id").value;
			var tg_site_name = $(layero).find("iframe")[0].contentWindow.document.getElementById("tg_site_name").value;
			var tg_address = $(layero).find("iframe")[0].contentWindow.document.getElementById("tg_address").value;
			var index=$(layero).find("iframe")[0].contentWindow.document.getElementById("proceclsfrom").selectedIndex ;
			var proceclsfrom=$(layero).find("iframe")[0].contentWindow.document.getElementById("proceclsfrom").value ;
			var proceclsfromname =$(layero).find("iframe")[0].contentWindow.document.getElementById('proceclsfrom').options[index].text;
			var procdefparamfrom = $(layero).find("iframe")[0].contentWindow.document.getElementById("procdefparamfrom").value;
			var ifprocfrom = $($(layero).find("iframe")[0].contentWindow.document.getElementById("ifprocfrom")).next().hasClass("layui-form-checked")== true?"1":"0";
			var ind = $('#' + tableName).bootstrapTable("getData").length;
			//发送地址范围校验
			if(tg_site_id==""){
				parent.layer.msg("名称不能为空");
				return;
			}
			if(tg_site_name==""){
				parent.layer.msg("地址不能为空");
				return;
			}

			if(ifprocfrom=="1" && proceclsfrom=="all"){
				parent.layer.msg("触发处理类不能为空");
				return;
			} else if(ifprocfrom =='0') {
				proceclsfrom = '';
				procdefparamfrom = '';
			}

			if(proceclsfromname=='请选择') proceclsfromname = '';
			$('#' + tableName).bootstrapTable('append',[{
				"ind":ind,
				"sitaType": sitaType,
				"sitaTypeName": sitaTypeName,
				"tg_site_id": tg_site_id,
				"tg_site_name": tg_site_name,
				"tg_address": tg_address,
				"proceclsfrom": proceclsfrom,
				"proceclsfromname": proceclsfromname,
				"procdefparamfrom": procdefparamfrom,
				"ifprocfrom": ifprocfrom,
				"ifprocfromname": ifprocfrom=="1"?'是':'否'
			}]);
			layero.find(".layui-layer-close").click();
		},
		btn2:function(index, layero){
			$(layero).find("iframe")[0].contentWindow.document.getElementById("createFormSender").reset();
			return false;
		}
	});
}

function updatePage(tableName){
	if($('#' + tableName).bootstrapTable("getSelections").length<1){
		layer.msg('请选择一条修改信息', {
			icon : 2
		});
		return false;
	}
	var ids = $.map($('#' + tableName).bootstrapTable('getData'), function (row) {
        return row.tg_site_id;
    });
	
	var currentRow = $('#' + tableName).find("input[type=checkbox]:checked").parents("tr").data("index");
	var sitaType = $('#' + tableName).bootstrapTable('getSelections')[0].sitaType;
	var sitaTypeName = $('#' + tableName).bootstrapTable('getSelections')[0].sitaTypeName;
	sitaTypeName = encodeURIComponent(encodeURIComponent(sitaTypeName));
	var tg_site_id = $('#' + tableName).bootstrapTable('getSelections')[0].tg_site_id;
	var tg_site_name = $('#' + tableName).bootstrapTable('getSelections')[0].tg_site_name;
	tg_site_name = encodeURIComponent(encodeURIComponent(tg_site_name));
	var tg_address = $('#' + tableName).bootstrapTable('getSelections')[0].tg_address;
	tg_address = encodeURIComponent(encodeURIComponent(tg_address));
	var proceclsfrom = $('#' + tableName).bootstrapTable('getSelections')[0].proceclsfrom;
	var proceclsfromname = $('#' + tableName).bootstrapTable('getSelections')[0].proceclsfromname;
	proceclsfromname = encodeURIComponent(encodeURIComponent(proceclsfromname));
	var procdefparamfrom = $('#' + tableName).bootstrapTable('getSelections')[0].procdefparamfrom;
	var ifprocfrom = $('#' + tableName).bootstrapTable('getSelections')[0].ifprocfrom;
	var ifprocfromname = $('#' + tableName).bootstrapTable('getSelections')[0].ifprocfromname;
	ifprocfromname = encodeURIComponent(encodeURIComponent(ifprocfromname));
	  parent.layer.open({
		  type: 2, 
		  title:'修改地址范围',
		  maxmin:false,
		  shadeClose: true,
		  area: ["550px","400px"],
		  content: ctx + "/telegraph/templ/senderSite?sitaType="+sitaType+"&sitaTypeName="+sitaTypeName+"&tg_site_id="+"tg_site_id"
		  +"&tg_site_name="+tg_site_name+"&tg_address="+tg_address+"&proceclsfrom="+proceclsfrom+"&proceclsfromname="+proceclsfromname
		  +"&procdefparamfrom="+procdefparamfrom+"&ifprocfrom="+ifprocfrom+"&ifprocfromname="+ifprocfromname +"&ids=" + ids,
		  btn:["保存","重置","返回"],
		  yes:function(index, layero){
			  var sitaType = $(layero).find("iframe")[0].contentWindow.document.getElementById('sitaType').value;
			  var sitaTypeName ='';
			  if(sitaType==1){
				  sitaTypeName = '邮件';
			  } else {
				  sitaTypeName = 'SITA';
			  }
			  var tg_site_id = $(layero).find("iframe")[0].contentWindow.document.getElementById("tg_site_id").value;
			  var tg_site_name = $(layero).find("iframe")[0].contentWindow.document.getElementById("tg_site_name").value;
			  var tg_address = $(layero).find("iframe")[0].contentWindow.document.getElementById("tg_address").value;
			  var index=$(layero).find("iframe")[0].contentWindow.document.getElementById("proceclsfrom").selectedIndex ;
			  var proceclsfrom=$(layero).find("iframe")[0].contentWindow.document.getElementById("proceclsfrom").value ;
			  var proceclsfromname =$(layero).find("iframe")[0].contentWindow.document.getElementById('proceclsfrom').options[index].text;
			  var procdefparamfrom = $(layero).find("iframe")[0].contentWindow.document.getElementById("procdefparamfrom").value;
			  var ifprocfrom = $($(layero).find("iframe")[0].contentWindow.document.getElementById("ifprocfrom")).next().hasClass("layui-form-checked")== true?"1":"0";
			  var ind = $('#' + tableName).bootstrapTable("getData").length;
			  //发送地址范围校验
			  if(tg_site_id==""){
					parent.layer.msg("名称不能为空");
					return;
			  }
			  if(tg_site_name==""){
				parent.layer.msg("地址不能为空");
				return;
			  }
			
			  if(ifprocfrom=="1"&&proceclsfrom=="all"){
					parent.layer.msg("触发处理类不能为空");
					return;
			  } else if(ifprocfrom =='0')  {
					proceclsfrom = '';
					procdefparamfrom = '';
			  }
			  if(proceclsfromname=='请选择') proceclsfromname = '';
			  $('#' + tableName).bootstrapTable('updateRow',{index: currentRow, row:{
		      	  "ind":ind,
		    	  "sitaType": sitaType,
		    	  "sitaTypeName": sitaTypeName,
		    	  "tg_site_id": tg_site_id,
		    	  "tg_site_name": tg_site_name,
		    	  "tg_address": tg_address,
		    	  "proceclsfrom": proceclsfrom,
		    	  "proceclsfromname": proceclsfromname,
		    	  "procdefparamfrom": procdefparamfrom,
		    	  "ifprocfrom": ifprocfrom,
		    	  "ifprocfromname": ifprocfrom=="1"?'是':'否'
			    }});
			  layero.find(".layui-layer-close").click();
		  },
		  btn2:function(index, layero){
			  $(layero).find("iframe")[0].contentWindow.document.getElementById("createFormSender").reset();
			  return false;
		  }
		});
}

var index = parent.layer.getFrameIndex(window.name);
//校验
function submitCheck() {

	var tg_name = $("input[name=tg_name]").val();
	var tgType = $("select[name=tgType]").val();
	var mtext = $("#mtext").text();
	mtext = mtext == "请输入内容"?"":mtext;
	var eventid = $("input[name=eventid]").val();
	var fu = new ChkUtil();
	var res = '';

	res = fu.checkFormInput(tg_name, true,'',50);
	if (res.length > 0) {
		var msg = '[报文名称]合法性检查错误,' + res;
		layer.msg(msg, {icon: 2,time: 1000});
		$("input[name=tg_name]").focus();
		return false;
	}
	res = fu.checkFormInput(tgType, true,'',10);
	if (res.length > 0) {
		var msg = '[报文类型]合法性检查错误,' + res;
		layer.msg(msg, {icon: 2,time: 1000});
		$("select[name=tgType]").focus();
		return false;
 	}
//	res = fu.checkFormInput(eventid, true,'',20);
//	if (res.length > 0) {
//		var msg = '[模板关联事件]合法性检查错误,' + res;
//		layer.msg(msg, {icon: 2,time: 1000});
//		$("input[name=eventid]").focus();
//		return false;
//	}
	res = fu.checkFormInput(mtext, true,'',500);
	if (res.length > 0) {
		var msg = '[报文正文]合法性检查错误,' + res;
		layer.msg(msg, {icon: 2,time: 1000});
		$("#mtext").focus();
		return false;
 	}		 

	var a = $('#sendList').bootstrapTable("getData").length;
	var b = $('#receiveList').bootstrapTable("getData").length;
	if (a == 0) {
		//var msg = '请填写发送地址范围信息' ;
		//layer.msg(msg, {icon: 2,time: 1000});
		//return false;
	}
	if (b == 0) {
		//var msg = '请填写接收地址范围信息' ;
		//layer.msg(msg, {icon: 2,time: 1000});
		//return false; 
	}
	return true;
}
//页面保存
var save = function (){
	var id = $("input[name=id]").val();
	var flag = $("input[name=flag]").val();
	var typeId = $("input[name=typeId]").val();
	var tg_name = $("input[name=tg_name]").val();
	var tgType = $("select[name=tgType]").val();
	var priority = $("select[name=priority]").val();
	var mtext = $("#mtext").text();
	var old = $("#mtext").html();
	old = old.replace(/\<br\>/ig, '\n');
	var oldobj = $("<div>"+old+"</div>");
	mtext = oldobj.text();
//	var varcols=$("#varcols").val();
	var varcols = getvarcols();
	var check = submitCheck();
	var sendList=JSON.stringify($('#sendList').bootstrapTable('getData'));
	var receiveList=JSON.stringify($('#receiveList').bootstrapTable('getData'));
	var eventid = $("input[name=eventid]").val();
	var fiotype = $("select[name=fiotype]").val();
	var ifanalysis = $("input[name=ifanalysis]").prop("checked");
	if(check){
	  	$.ajax({
			type:'post',
			data:{
				'id': id, 
				'eventid': eventid,
				'flag': flag,
				'fiotype': fiotype,
				'typeId' : typeId,
				'tg_name' : tg_name,
				'tgType' : tgType,
				'priority' : priority,
				'mtext' : mtext,
				'varcols' :varcols,
				'sendList' : sendList,
				'ifanalysis' : ifanalysis==true?1:0,
				'receiveList' : receiveList	
			},
			async:true,
			url:ctx+"/telegraph/templ/save",
			dataType:"text",
			success:function(result){
				if(result == "操作成功"){
					parent.layer.close(index);
					parent.layer.msg(result, {icon: 1,time: 1000});
					parent.refresh();
				} else {
					layer.msg(result, {icon: 1,time: 1000});
					return false;
				}
			},
			error:function(msg){
				var result = "操作失败";
				layer.msg(result, {icon: 1,time: 1000});
				return false;
			}
		});
	}
	return check;
}


//参数获取值页面
function paramList(){		
	layui.use(['form','element']);
	var createIframe = layer.open({
	  type: 2, 
	  title:'选择参数变量',
	  maxmin:false,
	  shadeClose: true,
	  area: ["480px","400px"],
	  content: ctx + "/telegraph/templ/getVariable"
	});
}
function getmtext(val){
	$("#mtext").val(val);
}

function cc(val,val1,ischg,valselect){
	var tc = document.getElementById("mtext");
	tc.focus(); 
	if(typeof document.selection != "undefined"){ 
        var range = document.selection.createRange();   
        range.pasteHTML("<div class='display:inline;color:red;' contentEditable='false' id="+val1+">["+val+"]</div>");
    } else if(window.getSelection()!=undefined){
    	var range = window.getSelection().getRangeAt(0);
    	range.collapse(false);
    	var node = range.createContextualFragment("<div style='display:inline;color:red;' contentEditable='false' id="+val1+">["+val+"]</div>");
    	var c = node.lastChild;
        range.insertNode(node);
        if(c){
    	   range.setEndAfter(c);
    	   range.setStartAfter(c);
		}
		var j = window.getSelection();
		console.log(j);
		j.removeAllRanges();
		j.addRange(range);
    }
	
//	var a=$("#mtext").val();
//	var b=a+" ["+val+"] ";
//	$("#mtext").val(b);
	var a=$("#varcols").val();
	
	if (a == "") {
		if (ischg == 1) {
			var c = val1 + "_1_" + valselect;
		} else {
			var c = val1 + "_0_0"
		}
	} else {
		if (ischg == 1) {
			var c = "," + val1 + "_1_" + valselect;
		} else {
			var c = "," + val1 + "_0_0"
		}
	}
	
	var a=$("#varcols").val();
	var b=a+c;

	$("#varcols").val(b);
	
	

}

function removeall(){
	$('#sendList').bootstrapTable('removeAll');
	$('#receiveList').bootstrapTable('removeAll');
}
function resetAll(){
	$('#sendList').bootstrapTable('refresh');
	$('#receiveList').bootstrapTable('refresh');
}

//模板关联事件页面
function searchEvent(){	
layui.use(['form','element']);
 layer.open({
		  type: 2, 
		  title:'关联事件',
		  maxmin:false,
		  shadeClose: true,
		  area: ["420px","300px"],
		  content: ctx + "/message/templ/searchEventListPage?type=TM"
		  })

	}
//关联事件信息赋值
function getEventValue(name,no){
	$('#eventname').val(name);
	$('#eventid').val(no);
}
