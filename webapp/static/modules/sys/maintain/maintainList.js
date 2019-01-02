var layer;
layui.use(["layer","form","laypage"],function(){	
	layer = layui.layer;
	var laypage = layui.laypage;
	var form = layui.form;
	laypage.render({
		elem: 'pagination',
		count:$("#totalRows").val(),
		pages :$("#pages").val(),
		curr:$("#pageNum").val(),
		limit:$("#pageCount").val(),
		groups : 5,
		skip : false,
		jump:function(obj, first){
			if(!first){
				 var pageNo = obj.curr;
				 $("#pageCount").val(obj.limit);
				 doQuery(pageNo);
			}
		},
		layout:['prev', 'page', 'next','count','limit']
		});
	var divH=$(window).height()-80;
	$('#tableDiv').css("height",divH);
	new PerfectScrollbar('#tableDiv');
});
$(document).ready(function() {
	//改变每页显示行数
	$("#pageSizeSelect").on("change",function(){
		var chagePageSize=$(this).val();
		$("#pageCount").val(chagePageSize);
		$("#pageNum").val("1");
		doQuery("1");
	});
	
	//全选
	$("#checkAll").click(function(){
		if($(this).is(':checked')){
			$(this).attr("title","取消全选")
			$(".delcheckbox").prop("checked",true);
		}else{
			$(this).attr("title","全选")
			$(".delcheckbox").prop("checked",false);
		}
	})
})
//查询
function doQuery(pageNo){
	$("#pageNum").val(pageNo);
	$("#searchForm").submit();
	return false;
}
//删除一条
function doOneDelete(param) {
	var tableId = $("#tabId").val();
	var url = ctx + "/sys/maintain/delete?" + param;
	var res = "1";
	$.ajax({
		url : url,
		type : "post",
		dataType : "html",
		data : {
			tabId : tableId
		},
		success : function(msg) {
			res = msg;
		}
	});
	return res;
}
//批量删除
function doMultiDelete() {
	if($(".delcheckbox:checked").length==0){
		layer.msg("请选择要删除的记录！", {
			icon : 7,
			time : 1000
		});
		return;
	}
	var succ = 0, error = 0;
	layer.confirm('确定要删除所选择的记录吗?', {
		shade : [ 0.3, '#eeeeee' ]
	}, function(index) {
		$(".delcheckbox:checked").each(function(k, v) {
			var param = $(v).val();
			var res = doOneDelete(param);
			if (res == "1") {
				succ++;
			} else {
				error++;
			}
		})
		layer.close(index);
		var msg = "删除成功" + succ + "条，失败" + error + "条。"
		showMsg(msg, function() {
			doQuery(1);
		})
	});
}
//删除触发函数
function doDelete(param) {
	layer.confirm('确认删除吗?', {
		shade : [ 0.3, '#eeeeee' ]
	}, function(index) {
		var res = doOneDelete(param);
		if (res == "1") {
			showMsg("删除成功!", function() {
				doQuery(1);
			})
		} else {
			showMsg("删除失败!", function() {
			})
		}
		layer.close(index);
	});
}
//显示消息
function showMsg(msg, func) {
	layer.msg(msg, {
		icon : 7,
		time : 600
	}, function() {
		func();
	});
}
//新增
var addWin;
function doAdd(){
	var tableId = $("#tabId").val();	
	addWin = layer.open({
		closeBtn : false,
		type : 2,
		title : "新增",
		area : [ '60%', '90%' ],
		btn : [ "重置", "保存", "关闭" ],
		btn1 : function(index, layero) {
			var addForm = layer.getChildFrame('#addForm', index);
			addForm[0].reset();
		},
		btn2 : function(index, layero) {
			var addForm = layer.getChildFrame("#addForm", index);
			var iframeWin = window[layero.find('iframe')[0]['name']];
			iframeWin.doSubmit();
			return false;
		},
		content : ctx + '/sys/maintain/addInit?tabId='+tableId
	});
	//layer.full(addWin);
}
//修改
var editWin;
function doModify(param,readonly){
	var tableId = $("#tabId").val();	
	if(readonly){
		editWin = layer.open({
			closeBtn : false,
			type : 2,
			title : "查看",
			area : [ '60%', '90%' ],
			btn : ["关闭"],
			content : ctx + '/sys/maintain/modifyInit?readonly=1&tabId='+tableId+"&"+param
		});
	}else{
		editWin = layer.open({
			closeBtn : false,
			type : 2,
			title : "修改",
			area : [ '60%', '90%' ],
			btn : [ "保存", "关闭" ],
			btn1 : function(index, layero) {
				var addForm = layer.getChildFrame("#modifyForm", index);
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.doSubmit();
//				doQuery($("#pageNum").val());
				return false;
			},
			content : ctx + '/sys/maintain/modifyInit?tabId='+tableId+"&"+param
		});
	}
	//layer.full(editWin);
}
