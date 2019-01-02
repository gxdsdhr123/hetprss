$(function(){
	var layer;
	layui.use(['form','layer'],function(){
		layer = layui.layer;
	});
	
	//bootstrap-Table全局样式
	jQuery.fn.bootstrapTable.columnDefaults.sortable = true;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	
	//页面表格初始化
	showTable();
	
	//初始化班组下拉框
	$("#banZu").select2({
		language: "zh-CN",
	    placeholder: "请选择组员",
	    width:'100%',
	    tags: true,
	    allowClear: true,
	    ajax: {
	        url: PATH +"/qyCarEmployeeWork/searchBanZu",
	        dataType: 'json',
	        delay: 250,
	        data: function (params) {
	        	return {
	    	        name: encodeURI(params.term),  //参数
	    	    };
	        },
	        processResults: function (data, params) {
	            var array = new Array();
	            if (data) {
	                for (var i = 0; i < data.length; i++) {
	                    array.push({ id: data[i].GROUP_NAME, text: data[i].GROUP_NAME });
	                }
	            }
	            var result = new Object();
	            result.results = array;
	            return result;
	        },
	        cache: true
	    },
	    escapeMarkup: function (markup) { return markup; }, 
	    minimumInputLength: 0
	});
	//初始化组员下拉框
	$("#zuYuan").select2({
		language: "zh-CN",
	    placeholder: "请选择组员",
	    width:'100%',
	    tags: true,
	    allowClear: true,
	    ajax: {
	        url: PATH +"/qyCarEmployeeWork/searchZuYuan",
	        dataType: 'json',
	        delay: 250,
	        data: function (params) {
	            return getParams(params);
	        },
	        processResults: function (data, params) {
	            var array = new Array();
	            if (data) {
	                for (var i = 0; i < data.length; i++) {
	                    array.push({ id: data[i].NAME, text: data[i].NAME });
	                }
	            }
	            var result = new Object();
	            result.results = array;
	            return result;
	        },
	        cache: true
	    },
	    escapeMarkup: function (markup) { return markup; }, 
	    minimumInputLength: 0
	});
	//筛选按钮
	$("#searchBut").on('click',function(){
		layer.open({	
			type : 1,
			title : "筛选",
			//area : 'auto',
			area : [ "600px", "400px" ],
			maxmin : false,
			shadeClose : true,
			scrollbar: false, 
			content : $("#container"),
			btn : [ "确定", "取消" ],
			yes: function(index, layero){
				showTable();
            	layer.close(index);
			},
			btn2: function(index, layero){
			}
		})
	})
	
	//打印Excel
	$("#printBut").on('click',function(){
		var beginTime = $("#beginTime").val();
		var endTime = $("#endTime").val();
		var banZu = $("#banZu").val();
		var zuYuan = $("#zuYuan").val();
		$("#beginTimeDisplay").text(beginTime);
		$("#endTimeDisplay").text(endTime);
		$("#banZuDisplay").text(banZu);
		$("#zuYuanDisplay").text(zuYuan);
		$("#printForm").submit();
	});
	
});
//表格数据查询
function showTable(){
	var tableOptionsJolin = {
		url: PATH + "/qyCarEmployeeWork/showTable",
	    method: "post",					  				 //请求方式（*）
	    dataType: "json",
		striped: true,									 //是否显示行间隔色
	    cache: true,
	    undefinedText:'',
	    checkboxHeader:false,
	    search :false,
	    pagination: true,
	    sidePagination: 'server',
	    contentType: "application/x-www-form-urlencoded",
	    pageNumber: 1,
	    pageSize: 10,
	    pageList: [10, 20, 50],
	    queryParamsType:'',
	    queryParams: function (params) {	//参数
	        return {
	    		banZu :encodeURI($("#banZu").val()),
	    		zuYuan :encodeURI($("#zuYuan").val()),
	    		beginTime :$("#beginTime").val(),
	    		endTime :$("#endTime").val(),
	        	pageNumber: params.pageNumber,    
                pageSize: params.pageSize,
	        }
	    },
	    columns:[
			{field: "SEQ",title:"序号",sortable:false,editable:false,
				formatter:function(value,row,index){
					return index+1;
				}
			},
			{field: "TEAM", title: "班组",editable:false},
			{field: "NAME", title: "姓名",editable:false},
			{field: "TUI_NUM", title: "推飞机架次",editable:false},
			{field: "TUO_NUM", title: "拖飞机架次",editable:false},
			{field: "I_NUM", title: "国际总架次",editable:false},
			{field: "D_NUM", title: "国内总架次",editable:false},
			{field: "T3I_NUM", title: "T3国际架次",editable:false},
			{field: "TOTAL", title: "总计",editable:false},
           ]
//			    onDblClickRow:onDblClickRow
			    
		};
		$("#baseTable").bootstrapTable('destroy').bootstrapTable(tableOptionsJolin);	
}
function getParams(params){
	var banZu = $("#banZu").val();
	if(banZu == null){
		layer.msg("请先选择班组！" , {icon : 7, time : 1000});
	}else{
		return {
	        name: encodeURI(params.term),  //参数
	        banZu:encodeURI(banZu)
	    };
	}
}