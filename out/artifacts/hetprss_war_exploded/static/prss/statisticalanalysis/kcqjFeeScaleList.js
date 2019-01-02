var layer;// 初始化layer模块
var baseTable;// 基础表格
var iframe;
var type='';
var clickRowId = "";
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})
	initSelect();

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
	//新增单据
	$("#newBtn").click(function() {
		type="add";
		iframe = layer.open({
			type : 2,
			title : false,
			area:['500px','500px'],
			closeBtn : false,
			content : ctx + "/kcqj/feeScale/add?type=add",
			btn : [ "保存", "取消" ],
			btn1 : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.save();
				return false;
			}
		});
	});
	//修改单据
	$("#editBtn").click(function() {
		type="edit";
		if (clickRowId=="") {
			layer.msg("请选择要修改的单据！", {
				icon : 7
			});
			return false;
		}
		iframe = layer.open({
			type : 2,
			title :false,
			area:['500px','500px'],
			closeBtn : false,
			content : ctx + "/kcqj/feeScale/edit?id="+clickRowId+"&type=edit",
			btn : [ "保存", "取消" ],
			btn1 : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.save();
				clickRowId='';
				return false;
			}
		})
	});
	
	//删除单据
	$("#delBtn").click(function() {
		if (clickRowId=="") {
			layer.msg("请选择要删除的单据！", {
				icon : 7
			});
			return false;
		}
		var confirm = layer.confirm('您确定要删除选中单据？', {
			btn : [ '确定', '取消' ]
		}, function() {
			$.ajax({
				async : false,
				type : 'post',
				url : ctx + '/kcqj/feeScale/del',
				data : {
					'id':clickRowId
				},
				error : function() {
					layer.msg('删除失败！', {
						icon : 2
					});
				},
				success : function(data) {
					if(data.code=="0000"){
						layer.msg(data.msg, {
							icon : 1,
							time : 600
						}, function() {
							$("#baseTable").bootstrapTable('refresh');
						});
						clickRowId='';
					}else{
						layer.msg('删除失败！', {
							icon : 2
						}, function() {
							$("#baseTable").bootstrapTable('refresh');
						})
					}
				}
			});
		});
	});
	
	jQuery.fn.bootstrapTable.columnDefaults.sortable = false;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";

	baseTable = $("#baseTable");
	var tableOptions = {
		url: ctx + "/kcqj/feeScale/dataList", 
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
			{field: "ID", align:'left',halign:'center',editable:false,visible:false},
	        {field: "NUM", title: "序号",align:'left',halign:'center',editable:false},
			{field: "ALN_3CODE", title: "航空公司",align:'left',halign:'center',editable:false,visible:false},
			{field: "AIRLINE_SHORTNAME", title: "航空公司",align:'left',halign:'center',editable:false},
			{field: "TODB_ACTYPE_CODE", title: "机型",align:'left',halign:'center',editable:false},
			{field: "HQ_CHARGES", title: "航前费用",align:'left',halign:'center',editable:false},
			{field: "HH_CHARGES", title: "航后费用",align:'left',halign:'center',editable:false},
			{field: "GZ_CHARGES", title: "过站费用",align:'left',halign:'center',editable:false}
	    ],
		onClickRow:function onClickRow(row,tr,field){
			clickRowId = row.ID;
			$(".clickRow").removeClass("clickRow");
			$(tr).addClass("clickRow");
		}
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
    	alnCode :encodeURIComponent($("#alnCode").val()),
    	actType: encodeURIComponent($("#actType").val()),
    	pageNumber: params.pageNumber,
    	pageSize: params.pageSize
    	
    }
}
function saveSuccess(){
	layer.close(iframe);
	$("#baseTable").bootstrapTable('refresh');
}
function initSelect() {
	$('#alnCode').select2({
//		placeholder : "请选择",
		width : "100%",
		language : "zh-CN",
		templateResult : formatRepo,
		templateSelection : formatRepoSelection,
		escapeMarkup : function(markup) {
			return markup;
		}
	});
	$('#actType').select2({
//		placeholder : "请选择",
		width : "100%",
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


