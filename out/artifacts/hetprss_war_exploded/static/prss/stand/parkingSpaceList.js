var layer;// 初始化layer模块
var baseTable;// 基础表格
var holdDown = 0;
var scrollbar;
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	});
	$(".sortable").css("position","relative");
	$(".sortable").each(function(){
		scrollbar = new PerfectScrollbar(this);
	});
	$("body").css("minHeight", "0px");
	$(".content").css("minHeight", "0px");
	$(".content").css("padding", "0px");
	// 选择
	$("#pushright").click(function() {
		var lis = $("#allul").find("li.bechoose");
		$("#selectul").append(lis.clone(true).removeClass("bechoose"));
		lis.remove();
	});
	// 取消选择
	$("#pushleft").click(function() {
		var lis = $("#selectul").find("li.bechoose");
		$("#allul").append(lis.clone(true).removeClass("bechoose"));
		lis.remove();
	});
	//全部选择
	$("#pushrightAll").click(function() {
		var lis = $("#allul").find("li");
		lis.addClass("bechoose");
		$("#selectul").append(lis.clone(true).removeClass("bechoose"));
		lis.remove();
	});
	// 全部取消
	$("#pushleftAll").click(function() {
		var lis = $("#selectul").find("li");
		lis.addClass("bechoose");
		$("#allul").append(lis.clone(true).removeClass("bechoose"));
		lis.remove();
	});
	//保存方法
	$("#save").click(function() {
		var code=($("#actCode").val());//取出Code
		if(code!=null&& code!=""){
			var loading = layer.load(2,{shade:[0.1,'#000']});
			
			var selectData=getSelectData();
			$.ajax({
	    	    type: "POST",
	    	    url: ctx + '/stand/parkingSpace/saveEdit',
	    	    data: {
	    	    	"selectData":selectData,
	    	    	"code":code
	    	    },
	    	    dataType: "json",
	    	    success: function(result){
	    	    	layer.msg(result.msg,{icon:1});
	    	    	//重新加载表格
	    	    	$('#baseTable').bootstrapTable('refresh', {
	    	    		query :  function (params) {
	    					return {
	    			        	pageNumber: params.offset+1,    
	    			        	pageSize: 10
	    					}
	    					
	    				}
	    	    		
	    			});
	    	    	layer.close(loading);
    	        }
	    	});
		}else{
			layer.msg("未选择数据，无法保存",{icon : 2});
		}
		
		
	});
	
	jQuery.fn.bootstrapTable.columnDefaults.sortable = false;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";

	baseTable = $("#baseTable");
	var leftTableOptions = {
		url: ctx + "/stand/parkingSpace/dataList", 
	    method: "get",
	    dataType: "json",
		striped: true,
	    cache: true,
	    undefinedText:'',
	    checkboxHeader:false,
	    search:false,
	    pagination: true,
	    sidePagination: 'server',
	    pageNumber: 1,
	    pageSize: 10,
	    pageList: [10, 15, 20],
	    queryParamsType:'',
	    queryParams: function (params) {
	    	return {pageNumber: params.pageNumber,
	    		   pageSize: params.pageSize}
	    },
	    columns: [
	        {field: "BAY_CODE", title: "CODE",align:'left',halign:'center',editable:false,visible:false},
			{field: "DESCRIPTION_CN", title: "机位名称",align:'left',halign:'center',editable:false},
			{field: "AIRCRAFT_TYPE", title: "可停放机型",align:'left',halign:'center',editable:false},
			
	    ],
	    onClickRow: function (row,element) {  
	    	//加入点击行的样式，以便清晰的看到页面右侧select数据的所属行
	    	$(".clickRow").removeClass("clickRow");
	    	element.addClass("clickRow");
	    	
	    	$("#actCode").val(row.BAY_CODE);//将code放进隐藏行 以便更新数据
	    	var loading = layer.load(2,{shade : [0.2,'#000']});
	    	$.ajax({
	    	    type: "POST",
	    	    url: ctx + '/stand/parkingSpace/getTreeData',
	    	    data: {
	    	    	"code":row.BAY_CODE
	    	    },
	    	    dataType: "json",
	    	    success: function(dataMap){
	    	    	$("#allul").empty();//清空右侧select div
	    	    	$("#selectul").empty();
    	        	var leftdata = dataMap.leftData;
    	        	var rightdata=dataMap.rightData;
    	        	for(var d in leftdata){
    	        		$("#allul").append('<li class="list-group-item" id="'+leftdata[d].TODB_ACTYPE_CODE+
    	        				           '" onclick="liclick(this);" onmousedown="lidown(this);"'+
    	        				            'onmousemove="limove(this);" onmouseup="liup(this);">'+leftdata[d].ACTYPE_CODE+'</li>');
    	        	}
    	        	for(var d in rightdata){
	        		$("#selectul").append('<li class="list-group-item" id="'+rightdata[d].ACTYPE_CODE+
	        				           '" onclick="liclick(this);" onmousedown="lidown(this);"'+
	        				            'onmousemove="limove(this);" onmouseup="liup(this);">'+rightdata[d].ACTYPE_CODE+'</li>');
	        	    }
    	        	layer.close(loading);
	    	     }
	    	});
	      }
	};

	leftTableOptions.height = $("#baseTables").height();// 表格适应页面高度
	baseTable.bootstrapTable(leftTableOptions);// 加载基本表格
});

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
//function prepareMembers() {
//	var selectMemberIdsp = "";
//	var lis = $("#selectul").find("li");
//	for (var i = 0; i < lis.length; i++) {
//		var id = lis.eq(i).attr("id");
//		selectMemberIdsp = selectMemberIdsp + id + ",";
//	}
//	selectMemberIdsp = selectMemberIdsp.substring(0, selectMemberIdsp.length - 1);
//	parent.$("#info").val(selectMemberIdsp);
//}
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
//获取选择的数据，转换成字符串
function getSelectData(){
	var key=new Array();
	$("#selectul").find('li').each(function() {  
          key.push($(this).text());  
        });
	return key.join(",");
}









