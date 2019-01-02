var layer;// 初始化layer模块
var clickRowId = "";// 当前选中行，以便单选后操作,获取数据主键id
var win = null;
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	});
	
	//删除电子围栏配置信息
	$("#delete").click(function(){
		if(clickRowId==null || clickRowId==''){
			layer.msg('请选择要删除的数据', {
				icon : 2
			});
		}else{
			var loading = layer.load(2, {shade : [ 0.3, '#000' ]});
			$.ajax({
		        type: "POST",
		        url: ctx + '/rule/gisConfig/delete',
		        data: {
		        	"id":clickRowId
		        },
		        success: function(data){
		        	//将航班相关详细信息自动填入、选中
		        	if(data=="success"){
		        		layer.msg("删除成功！", {icon: 1,time:3000});
		        		$("#baseTable").bootstrapTable("refresh");
		        		clickRowId = null;
		        	}else{
		        		layer.alert("删除失败！", {icon: 2,time:3000});
		        	}
		        	layer.close(loading);
		        }
		    });
		}
	});

	//新增功能
	$("#add").click(function() {
		win = layer.open({
			type : 2,
			title : "新增",
			area:['610px','500px'],
			closeBtn : false,
			content : ctx + "/rule/gisConfig/toForm?operateType=add&id=",
			btn : [ "保存", "取消" ],
			yes : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.doSave();
				clickRowId = null;
				return false;
			}
		});
	});
	
	//一键生效
	$("#useAll").click(function() {
		setInUse("useAll");
	});
	
	//一键失效
	$("#notUseAll").click(function() {
		setInUse("notUseAll");
	});
	
	//初始化数据表格
	var tableOptions = getOptions();
	$("#baseTable").bootstrapTable(tableOptions);// 加载基本表格
});

function getOptions(){
	var tableOptions = {
		url: ctx + "/rule/gisConfig/getData", 
	    method: "get",
	    dataType: "json",
	    align:"center",
		striped: true,
	    cache: true,
	    sortable: false,
	    undefinedText:'',
	    checkboxHeader:false,
	    search:false,
	    pagination: true,
	    sidePagination: 'server',
	    pageNumber: 1,
	    pageSize: 20,
	    pageList: [20,50,100],
	    paginationPreText: "上一页",  
		paginationNextText: "下一页",
	    queryParams: function (params) {
	        return {
	        	reskind : $("#reskind").val(),
	        	restype : $("#restype").val(),
	        	pageNumber: params.offset+1,    
                pageSize: params.limit
	        }
	    },
	    onClickRow:function onClickRow(row,tr,field){
			clickRowId = row.id;
			$(".clickRow").removeClass("clickRow");
			$(tr).addClass("clickRow");
		},
		onDblClickCell : function onDblClickCell(field, value, row, td) {
			clickRowId = row.id;
			win = layer.open({
				type : 2,
				title : "修改",
				area:['610px','500px'],
				closeBtn : false,
				content : ctx + "/rule/gisConfig/toForm?operateType=modify&id="+clickRowId,
				btn : [ "保存", "取消" ],
				yes : function(index, layero) {
					var iframeWin = window[layero.find('iframe')[0]['name']];
					iframeWin.doSave();
					clickRowId = null;
					return false;
				}
			});
		},
	    columns: [
			{field: "SEQ",title:"序号",sortable:false,editable:false,
				formatter:function(value,row,index){
					return index+1;
				}
			},
			{field: "id",title:"id",sortable:false,editable:false,visible:false,
				formatter:function(value,row,index){
					return row.id;
				}
			},
			{field: "kindName", title: "保障类型",editable:false},
			{field: "typeName", title: "作业类型",editable:false},
			{field: "procName", title: "流程名称",editable:false},
			{field: "nodeName", title: "节点",editable:false},
			{field: "targetTypeName", title: "任务类型",editable:false},
			{field: "targetAreaName", title: "任务位置",editable:false},
			{field: "areaCodeName", title: "判定执行电子围栏区域",editable:false},
			{field: "delaySecond", title: "延迟时间",editable:false},
			{field: "operatorName", title: "操作者",editable:false},
			{field: "updateTm", title: "修改日期",editable:false}
	    ]
	};
	tableOptions.height = $("#baseTables").height();// 表格适应页面高度
	return tableOptions;
}

function refreshBaseTable(){
	layer.close(win);
	$("#baseTable").bootstrapTable("refresh");
}

function setInUse(type){
	var msg = "";
	var inUse = "";
	if(type=="useAll"){
		msg = "确定将所有配置设置为生效吗？";
		inUse = "1";
	}else{
		msg = "确定将所有配置设置为失效吗？";
		inUse = "0";
	}
	layer.confirm(msg, {btn: ['确定', '取消']}, function () {
		var loading = layer.load(2, {shade : [ 0.3, '#000' ]});
		$.ajax({
	        type: "POST",
	        url: ctx + '/rule/gisConfig/setInUse',
	        data: {
	        	"inUse":inUse
	        },
	        success: function(data){
	        	layer.close(loading);
	        	if(data=="success"){
	        		layer.msg("设置成功！", {icon: 1,time:3000});
	        		$("#baseTable").bootstrapTable("refresh");
	        	}else{
	        		layer.alert("设置出现问题，请联系管理员！", {icon: 2,time:3000});
	        	}
	        	clickRowId = null;
	        	clickRowInfo = null;
	        }
	    });
	});
}