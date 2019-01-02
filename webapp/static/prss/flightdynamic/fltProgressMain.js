var refresh_unit = 30 * 1000;// 刷新单位是分钟
var layer;
layui.use([ 'layer' ], function() {
	layer = layui.layer;
});
$(document).ready(function() {
	loadGrid();
	$(window).resize(function(){
		$(".fixed-table-container").height($(window).height()-50);
	});
	setInterval(function() {
		reloadGrid();
	}, refresh_unit);
});
/**
 * 加载表格
 */
function loadGrid(interval) {
	$("#dataGrid").bootstrapTable({
		url : ctx + "/fltProgress/gridData",
		method : "get",
		dataType:"json",
		height:$(window).height(),
		pagination : false,
		clickToSelect : false,
		undefinedText : "",
		/*search : true, // 是否开启搜索功能
		searchOnEnterKey : true,*/
		columns : getGridColumns(),
		responseHandler : function(data) {
			return formatData(data);
		},
		onLoadSuccess:function(){
			mergeCells();
		},
		onClickCell : function(field, value, row, $element) {
			if(field&&field.substring(0,1)=="C"){
				showDetail(field.substring(1),row.infltid?row.infltid:'',row.outfltid?row.outfltid:'');
			}
		}
	});

}

var tableDataTmp = [];
function formatData(data) {
	var gridData = [];
	tableDataTmp = data;
	if(data){
		for(var i=0;i<data.length;i++){
			if(data[i].fltData){
				gridData = $.merge(gridData,data[i].fltData);
			}
		}
	}
	return gridData;
}

function mergeCells(){
	var index = 0;
	for (var i = 0; i < tableDataTmp.length; i++) {
		var fltData = tableDataTmp[i].fltData;
		if (fltData && fltData.length > 0) {
			$("#dataGrid").bootstrapTable('mergeCells', {
				index : index,
				field : 'apronCode',
				rowspan : fltData.length
			});
			index += fltData.length;
		}
	}
}

function getGridColumns() {
	var columns = [{
		field : "apronCode",
		title : "机坪",
		width:"100px",
		align:"center"
	},{
		field : "actstandCode",
		title : "机位",
		width:"100px",
		align:"center"
	},{
		field : "infltid",
		title : "进港航班id",
		width:"100px",
		align:"center",
		visible:false
	},{
		field : "outfltid",
		title : "出港航班id",
		width:"100px",
		align:"center",
		visible:false
	},{
		field : "fltno",
		width:"100px",
		title : "航班号",
		formatter(value, row, index, field){
			var infltid = row.infltid;
			var outfltid = row.outfltid;
			var arr = value.split("/");
			var i = "<a id='inFltId' style='cursor:pointer;' onClick=openImage("+infltid+",'')>"+arr[0]+"</a>";
			var d = "<a id='outFltId' style='cursor:pointer;' onClick=openImage('',"+outfltid+")>"+arr[1]+"</a>";
			var result = i +"/"+ d;
			return result;
		}
	},{
		field : "eta",
		title : "进港预落",
		width:"100px",
		align:"center",
		formatter(value, row, index, field){
			var ata = row.ata;
			var eta = row.eta;
			if(eta&&eta.indexOf("(")<0){
				eta ="&emsp;&nbsp;&nbsp;"+eta;
			}
			if(ata){
				return "<span class='badge bg-red'>"+eta+"&nbsp;起飞</span>"
			} else if(eta){
				return "<span class='badge bg-green'>"+eta+"&nbsp;落地</span>"
			} else {
				return value;
			}
		}
	},{
		field : "etd",
		title : "出港预起",
		width:"100px",
		align:"center",
		formatter(value, row, index, field){
			var atd = row.atd;
			var etd = row.etd;
			if(etd&&etd.indexOf("(")<0){
				etd = "&emsp;&nbsp;&nbsp;"+etd;
			}
			if(atd){
				return "<span class='badge bg-red'>"+etd+"&nbsp;起飞</span>"
			} else if(etd){
				return "<span class='badge label-warning'>"+etd+"&nbsp;预出</span>"
			} else {
				return value;
			}
		}
	},{
		field : "C11",
		title : "航线保障",
		width:"100px",
		align:"center",
		cellStyle:function(value, row, index, field){
			return {classes: 'cellHover'};
		}
	},{
		field : "C1",
		title : "客梯车靠接",
		width:"100px",
		align:"center",
		cellStyle:function(value, row, index, field){
			return {classes: 'cellHover'};
		}
	},{
		field : "C15",
		title : "客梯车撤离",
		width:"100px",
		align:"center",
		cellStyle:function(value, row, index, field){
			return {classes: 'cellHover'};
		}
	},{
		field : "C2",
		title : "进港摆渡车",
		width:"100px",
		align:"center",
		cellStyle:function(value, row, index, field){
			return {classes: 'cellHover'};
		}
	},{
		field : "C3",
		title : "出港摆渡车",
		width:"100px",
		align:"center",
		cellStyle:function(value, row, index, field){
			return {classes: 'cellHover'};
		}
	},{
		field : "C12",
		title : "客运",
		width:"100px",
		align:"center",
		cellStyle:function(value, row, index, field){
			return {classes: 'cellHover'};
		}
	},{
		field : "C14",
		title : "清舱",
		width:"100px",
		align:"center",
		cellStyle:function(value, row, index, field){
			return {classes: 'cellHover'};
		}
	},{
		field : "C4",
		title : "装卸操作",
		width:"100px",
		align:"center",
		cellStyle:function(value, row, index, field){
			return {classes: 'cellHover'};
		}
	},{
		field : "C5",
		title : "进港行李拉运",
		width:"100px",
		align:"center",
		cellStyle:function(value, row, index, field){
			return {classes: 'cellHover'};
		}
	},{
		field : "C6",
		title : "出港行李拉运",
		width:"100px",
		align:"center",
		cellStyle:function(value, row, index, field){
			return {classes: 'cellHover'};
		}
	},{
		field : "C7",
		title : "进港货邮拉运",
		width:"100px",
		align:"center",
		cellStyle:function(value, row, index, field){
			return {classes: 'cellHover'};
		}
	},{
		field : "C8",
		title : "出港货邮拉运",
		width:"100px",
		align:"center",
		cellStyle:function(value, row, index, field){
			return {classes: 'cellHover'};
		}
	},{
		field : "C9",
		title : "污水车",
		width:"100px",
		align:"center",
		cellStyle:function(value, row, index, field){
			return {classes: 'cellHover'};
		}
	},{
		field : "C10",
		title : "清水车",
		width:"100px",
		align:"center",
		cellStyle:function(value, row, index, field){
			return {classes: 'cellHover'};
		}
	},{
		field : "C13",
		title : "推飞机",
		width:"100px",
		align:"center",
		cellStyle:function(value, row, index, field){
			return {classes: 'cellHover'};
		}
	}];
	return columns;
}
/**
 * 自动刷新获取数据
 */
function reloadGrid(){
	//刷新前滚动条
	var beforeScroll = $(".fixed-table-body").scrollTop();
	$.ajax({
		type : 'post',
		url : ctx + "/fltProgress/gridData",
		dataType:"json",
		beforeSend:function(){
		},
		error:function(){
		},
		success : function(data) {
			$("#dataGrid").bootstrapTable("load",formatData(data));
			mergeCells();
			if(beforeScroll){
				$("#dataGrid").bootstrapTable("scrollTo",beforeScroll); 
			}
			$(".fixed-table-container").height($(window).height()-50);
		}
	});
}

/**
 * 任务列表
 * @param colCode
 * @param infltid
 * @param outfltid
 */
function showDetail(colCode,infltid,outfltid){
	var win = layer.open({
		type : 2,
		closeBtn:false,
		title : false,
		area:["100%","100%"],
		content : ctx + "/fltProgress/detail?colCode="+colCode+"&infltid="+infltid+"&outfltid="+outfltid,
		btn : ["关闭"]
	})
}
/**
 * 点击航班号打开图
 * @param inFltid
 * @param outFltid
 */
function openImage(inFltid,outFltid){
	// 修改关闭按钮样式
	$('body').on('layui.layer.open', function() {
		$('.layui-layer-close').removeClass('layui-layer-close2').addClass('layui-layer-close1');
	});
	iframe = layer.open({
		type : 2,
		title : false,
		closeBtn : 1,
		content : ctx +"/fltProgress/openImage?inFltid="+inFltid+"&outFltid="+outFltid,
	});
	layer.full(iframe);// 展开弹出层直接全屏显示
}