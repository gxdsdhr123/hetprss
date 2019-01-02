var layer;// 初始化layer模块
var baseTable;// 基础表格
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})
	initSelect2("airline","请选择航空公司，支持多选");//航空公司
	initSelect2("airport","请选择机场，支持多选");//报文类型
	
	
	//查询功能
	$(".search").click(function() {
		if(searchOption()){
			var options = $('#baseTable').bootstrapTable('refresh', {
				query : {
					pageNumber: 1,    
	                pageSize: 10
					
				}
			});
		}else{
			layer.msg("ETD必须同时填写或同时为空",{icon: 2});
			
		}
		
	})
	
	jQuery.fn.bootstrapTable.columnDefaults.sortable = false;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";

	baseTable = $("#baseTable");
	var tableOptions = {
		url: ctx + "/flightdynamic/passengerMonitor/dataList", 
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
	    	var airport = $("select[name='airport']").val();
			if(airport!=null&&airport!="null"){
				airport = airport.join(",");
			}else{
				airport="";
			}
			var airline = $("select[name='airline']").val();
			if(airline!=null&&airline!="null"){
				airline = airline.join(",");
			}else{
				airline="";
			}
	        return {
	        	flightDate :encodeURIComponent($("#flightDate").val()),
	        	etd :encodeURIComponent($("#etdStart").val())+encodeURIComponent($("#etdEnd").val()),
	        	flightNumber :encodeURIComponent($("#flightNumber").val()),
	        	airline:encodeURIComponent(airline),
	        	airport:encodeURIComponent(airport),
	        	pageNumber: params.pageNumber,    
                pageSize: params.pageSize
	        }
	    },
	    columns: [
	    [
			{field: "FLIGHT_NUMBER", title: "航班号",colspan:1,rowspan: 2,editable:false},
			{field: "ETD", title: "预计起飞时间",colspan:1,rowspan: 2,editable:false,align:'left',halign:'center'},
			{field: "PAX_NUM", title: "总人数（柜台/自助/网上）",colspan:1,rowspan: 2,editable:false,align:'left',halign:'center'},
			{field: "PCK_NUM", title: "值机（成人/儿童/婴儿）",colspan:1,rowspan: 2,editable:false,align:'left',halign:'center'},
			{field: "PSC_NUM", title: "过检（成人/儿童/婴儿）",colspan:1,rowspan: 2,editable:false,align:'left',halign:'center'},
			{field: "PBD_NUM", title: "登机（成人/儿童/婴儿）",colspan:1,rowspan: 2,editable:false,align:'left',halign:'center'},
			{field: "GATE", title: "登机口",colspan:1,rowspan: 2,editable:false,align:'left',halign:'center'},
			{title: "登机开始时间",colspan:2,rowspan: 1,editable:false,align:'left',halign:'center'},
			{title: "登机结束时间",colspan:2,rowspan: 1,editable:false,align:'left',halign:'center'},
			{field: "HTCH_CLO_TM", title: "关舱门时间",colspan:1,rowspan: 2,editable:false,align:'left',halign:'center'},
			{field: "COBT", title: "计算撤轮挡时间COBT",colspan:1,rowspan: 2,editable:false,align:'left',halign:'center'},
			{field: "OFF_CHOCK_TM", title: "实际撤轮挡时间AOBT",colspan:1,rowspan: 2,editable:false,align:'left',halign:'center'}

	    ],[
			{field: "BRD_BTM", title: "实际",editable:false,align:'left',halign:'center'},
			{field: "BSUGST_CHECK_TM", title: "建议",editable:false,align:'left',halign:'center',
		        cellStyle:function(value,row,index){  
		            if (row.BRD_BTM-row.BSUGST_CHECK_TM>0){  
		                return {css:{"background-color":"red"}}  
		            }  else{
		            	return {}
		            }
		        }
			},			
			{field: "BRD_ETM", title: "实际",editable:false,align:'left',halign:'center'},
			{field: "ESUGST_CHECK_TM", title: "建议",editable:false,align:'left',halign:'center',
		        cellStyle:function(value,row,index){  
		            if (row.BRD_ETM-row.ESUGST_CHECK_TM>0){  
		                return {css:{"background-color":"red"}}  
		            } else{
		            	return {}
		            }
		        }
			}
	       ]
	    ]
	};
	tableOptions.height = $("#baseTables").height();// 表格适应页面高度
	baseTable.bootstrapTable(tableOptions);// 加载基本表格
});
function searchOption(){
	var etdStart=$("#etdStart").val();
	var etdEnd=$("#etdEnd").val();
	if((etdStart!=""&&etdEnd!="")||(etdStart==""&&etdEnd=="")){
		return true;
	}else{
		return false;
	}
}

function initSelect2(selectId,tip){
	$('#'+selectId).select2({  
        placeholder: tip,  
//        width:"400px",
        language : "zh-CN",
		templateResult : formatRepo,
		templateSelection : formatRepoSelection,
		escapeMarkup : function(markup) {
			return markup;
		}
    }); 
}

function formatRepo(repo) {
	return repo.text;
}
// select2显示选项处理
function formatRepoSelection(repo) {
	return repo.text;
}




