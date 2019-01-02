$(function(){
	var layer;
	layui.use(['form','layer'],function(){
		layer = layui.layer;
	});
	new PerfectScrollbar($("#hiddenBottomDiv")[0]);
	//bootstrap-Table全局样式
	jQuery.fn.bootstrapTable.columnDefaults.sortable = true;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	
	$.ajax({
        type: "POST",
        url: PATH + '/mobileRecord/searchBuMen',
        dataType: "json",
        success: function(data){
        	var select = $("#buMen");
        	$(select).append("<option value=\"\"></option>")
        	for(var i=0;i<data.length;i++){
        		var option = "<option value=\""+data[i].ID+"\">"+data[i].NAME+"</option>";
        		$(select).append(option);
        	}
        }
	});
	
	$("#buMen").select2({
		language: "zh-CN",
	    placeholder: "请选择部门",
	    width:'10%'
	});
	
	$("#zhuangTai").select2({
		language: "zh-CN",
	    placeholder: "请选择状态",
	    width:'10%'
	});
	
	$("#searchBut").on('click',function(){
		showTable();
	})
	
	$("#paramBut").on('click',function(){
		selectMobileLog();
	})
	
	//打印Excel
	$("#printBut").on('click',function(){
		var buMen = $("#buMen").val();
		var zhuangTai = $("#zhuangTai").val();
		$("#buMenDisplay").text(buMen);
		$("#zhuangTaiDisplay").text(zhuangTai);
		$("#printForm").submit();
	});
	
	$("#searchLogBut").on('click',function(){
		var pdaId = $("#pdaId").text();
		if("" != pdaId){
			selectMobileLog();
			layer.open({	
				type : 1,
				title : "设备记录",
				//area : 'auto',
				area : [ "400px", "500px" ],
				maxmin : false,
				shadeClose : true,
				scrollbar: false, 
				content : $("#hiddenDiv")
			})
		}else{
			layer.msg("请先选择设备！" , {icon : 7, time : 1000});
		}
	})
	
	//初始化页面加载表格
	showTable();
	
});
//表格数据查询
function showTable(){
	var tableOptionsJolin = {
		url: PATH + "/mobileRecord/showTable",
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
	        	buMen :encodeURI($("#buMen").val()),
	        	zhuangTai :encodeURI($("#zhuangTai").val()),
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
			{field: "NAME", title: "部门",editable:false},
			{field: "PDA_ID", title: "设备id",visible:false},
			{field: "PDA_NO", title: "设备编号",editable:false},
			{field: "IMEI", title: "IMEI",editable:false},
			{field: "PDA_STATUS", 
				title: "状态",
				editable:false,
				formatter:function(value, row, index){
					var s = "";
					if(value == "1"){
						s = "借出";
					}else if (value == "2"){
						s = "在库";
					}else{
						s = "交接";
					}
					return s;
				}
			},
			{field: "USE_USER", title: "领用人",editable:false},
			{field: "USE_TIME", title: "领用时间",editable:false},
			{field: "BACK_USER", title: "归还人",editable:false},
			{field: "BACK_TIME", title: "归还时间",editable:false},
           ],
           onClickRow:function(row,tr){
        	   $(".clickRow").removeClass("clickRow");
       		   $(tr).addClass("clickRow");
       		   $("#pdaId").text(row.PDA_ID);
       		   $("#pdaNo").text(row.PDA_NO);
           }
		};
		$("#baseTable").bootstrapTable('destroy').bootstrapTable(tableOptionsJolin);	
}
//弹出窗设备记录查询
function selectMobileLog(){
	var param = $("#param").val();
	var pdaId = $("#pdaId").text();
	$.ajax({
		type: "POST",
        url: PATH + '/mobileRecord/selectMobileLog',
        data: {
        	"param":param,
        	"pdaId":pdaId
        },
        dataType: "json",
        success: function(data){
        	$("#hiddenBottomDiv").children().remove()
        	for(var i=0;i<data.length;i++){
        		$("#hiddenBottomDiv").append("<p><span class=\"lineSpan\">"+data[i].TIME+
        				"</span><span class=\"lineSpan\">"+data[i].NAME+
        				"</span><span class=\"lineSpan\">"+data[i].PDA_STATUS+"</span></p>")
        	}
        }
	})
}