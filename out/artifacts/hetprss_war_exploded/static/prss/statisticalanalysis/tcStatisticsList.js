var layer;// 初始化layer模块
var baseTable;// 基础表格
var iframe;
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})
	
	
	//查询功能
	$(".search").click(function() {
		if(searchOption()){
			var a = $('#baseTable').bootstrapTable('refresh', {
				query : {
					pageNumber: 1,    
	                pageSize: 10
					
				}
			});
			
			
		}else{
			layer.msg("日期不能为空",{icon: 2});
			
		}
		
	});
	//打印功能
	$(".print").click(function() {
		$("#listForm").submit();
	})
	
	$("#btnCoefficient").click(function(){
		iframe = layer.open({
			type : 2,
			title : '参数配置',
//			area:[$("body").width()*0.5+"px",$("body").height()*0.65+"px"],
			area:['500px','500px'],
			btn: ['确定','取消'],
			closeBtn : false,
			content : ctx + "/tc/statistics/getTcCoefficient",
			btn1 : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.save();
				return false;
			}
		});
		
	})
	
	jQuery.fn.bootstrapTable.columnDefaults.sortable = false;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";

	baseTable = $("#baseTable");
	var tableOptions = {
		url: ctx + "/tc/statistics/dataList", 
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
	    pageList: [10,15,20,50,100],
	    queryParamsType:'',
	    queryParams: function (params) {
	    	return getParams(params);
	    },
	    columns: [
	        {field: "NUM", title: "序号",align:'left',halign:'center',editable:false},
			{field: "NAME", title: "姓名",align:'left',halign:'center',editable:false},
			{field: "HE_JI", title: "合计",align:'left',halign:'center',editable:false},
			{field: "BD_NUM", title: "摆渡",align:'left',halign:'center',editable:false},
			{field: "BD_COUNT", title: "小计",align:'left',halign:'center',editable:false},
			{field: "QS_NUM", title: "清水",align:'left',halign:'center',editable:false},
			{field: "QS_COUNT", title: "小计",align:'left',halign:'center',editable:false},
			{field: "DY_NUM", title: "电源",align:'left',halign:'center',editable:false},
			{field: "DY_COUNT", title: "小计",align:'left',halign:'center',editable:false},
			{field: "PT_NUM", title: "平台",align:'left',halign:'center',editable:false},
			{field: "PT_COUNT", title: "小计",align:'left',halign:'center',editable:false},
			{field: "KET_NUM", title: "客梯",align:'left',halign:'center',editable:false},
			{field: "KET_COUNT", title: "小计",align:'left',halign:'center',editable:false},
			{field: "QYI_NUM", title: "牵引",align:'left',halign:'center',editable:false},
			{field: "QYI_COUNT", title: "小计",align:'left',halign:'center',editable:false},
			{field: "WS_NUM", title: "污水",align:'left',halign:'center',editable:false},
			{field: "WS_COUNT", title: "小计",align:'left',halign:'center',editable:false},
			{field: "CHS_NUM", title: "传送",align:'left',halign:'center',editable:false},
			{field: "CHS_COUNT", title: "小计",align:'left',halign:'center',editable:false},
			{field: "CAS_NUM", title: "残登",align:'left',halign:'center',editable:false},
			{field: "CAS_COUNT", title: "小计",align:'left',halign:'center',editable:false},
			{field: "KOT_NUM", title: "空调",align:'left',halign:'center',editable:false},
			{field: "KOT_COUNT", title: "小计",align:'left',halign:'center',editable:false},
			{field: "QYU_NUM", title: "气源",align:'left',halign:'center',editable:false},
			{field: "QYU_COUNT", title: "小计",align:'left',halign:'center',editable:false},
			{field: "CB_NUM", title: "除冰",align:'left',halign:'center',editable:false},
			{field: "CB_COUNT", title: "小计",align:'left',halign:'center',editable:false},
			{field: "LJ_NUM", title: "垃圾车",align:'left',halign:'center',editable:false},
			{field: "LJ_COUNT", title: "小计",align:'left',halign:'center',editable:false},
			{field: "CHNJC_NUM", title: "场外机组车",align:'left',halign:'center',editable:false},
			{field: "CHNJC_COUNT", title: "小计",align:'left',halign:'center',editable:false},
			{field: "CHWJC_NUM", title: "场内机组车",align:'left',halign:'center',editable:false},
			{field: "CHWJC_COUNT", title: "小计",align:'left',halign:'center',editable:false}
	    ]
	};

	tableOptions.height = $("#baseTables").height();// 表格适应页面高度
	baseTable.bootstrapTable(tableOptions);// 加载基本表格
});
function searchOption(){
	var dateStart=$("#dateStart").val();
	var dateEnd=$("#dateEnd").val();
	if(dateStart==""||dateEnd==""){
		return false;
	}else{
		return true;
	}
}

//获取搜索数据
function getParams(params){
    return {
    	dateStart :encodeURIComponent($("#dateStart").val()),
    	dateEnd: encodeURIComponent($("#dateEnd").val()),
    	pageNumber: params.pageNumber,
    	pageSize: params.pageSize
    	
    }
}
function saveSuccess(){
	layer.close(iframe);
	$("#baseTable").bootstrapTable('refresh');
}






