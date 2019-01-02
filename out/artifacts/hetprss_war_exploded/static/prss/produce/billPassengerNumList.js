var layer;
var clickRowId = "";
var clickRowCode = "";
var set = null;
var type;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});
$(document).ready(function() {
	initGrid();
	//新增单据
	$("#newBtn").click(function() {
		type="add";
		set = layer.open({
			type : 2,
			title : false,
			area:['1000px','80%'],
			closeBtn : false,
			content : ctx + "/produce/passengerNum/add?type="+type,
			btn : [ "保存", "取消" ],
			btn1 : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.save();
				return false;
			}
		});
//		layer.full(set);// 展开弹出层直接全屏显示
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
		set = layer.open({
			type : 2,
			title :false,
			area:['1000px','80%'],
			closeBtn : false,
			content : ctx + "/produce/passengerNum/edit?type="+type+"&id="+clickRowId,
			btn : [ "保存", "取消" ],
			btn1 : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.save();
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
		// 按钮
		}, function() {
			$.ajax({
				async : false,
				type : 'post',
				url : ctx + '/produce/passengerNum/del',
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
	
	//导出Word
	$("#wordBtn").click(function() {
		if(clickRowId==null || clickRowId==''){
			layer.msg('请选择要下载的数据', {
				icon : 2
			});
		}else{
			$("#id").val(clickRowId);
			$("#type").val("BillPassengerNumServiceTemplet");
			$("#printWord").submit();
		}
	});
	
	//导出Excel
	$("#excelBtn").click(function(){

        $("#startDate").val("");
        var index=layer.open({
            type:1
            ,title: '导出列表选项'
            ,closeBtn:false
            ,content: $("#printExcel")
            ,btn:['确定','取消']
            ,yes:function(){

                layer.close(index);
                $("#printExcel").submit();
            }
        });
    });

    //筛选
    $("#btnSubmit").click(function(){

        // initGrid();
        $('#baseTable').bootstrapTable('refresh', {
            query : {
                dateStart :encodeURIComponent($("#dateStart").val())
            }
        });
    });
});
/**
 * 初始化表格
 */
function initGrid() {
	$("#baseTable").bootstrapTable({
		striped : true,
		toolbar : "#toolbar",
		idField : "id",
		uniqueId : "rowId",
		url : ctx + "/produce/passengerNum/dataList",
		method : "get",
		pagination : false,
		showRefresh : false,
		clickToSelect : false,
		searchOnEnterKey : true,
		pagination : true,
		search : true,
		undefinedText : "",
		height : $(window).height(),
        queryParams: function (params) {
            return getParams(params);
        },
		columns : getGridColumns(),
		onClickRow:function onClickRow(row,tr,field){
			clickRowId = row.ID;
			clickRowCode = row.FLTID;
			$(".clickRow").removeClass("clickRow");
			$(tr).addClass("clickRow");
		},
        onDblClickRow : function(row,tr,field){
			//baochl_20180717 匿名账号禁止操作
			if($("#loginName").val()=="anonymous"){
				return false;
			}
			var editBtn = $("#editBtn");
			//有修改权限
			if(editBtn&&editBtn.length>0){
				$("#editBtn").trigger("click");
			}
		}
	});
	
}

//获取搜索数据
function getParams(params){
    return {
        dateStart :encodeURIComponent($("#dateStart").val())
    }
}

/**
 * 表格列与数据映射
 */
function getGridColumns() {
	var columns = [ {
		field : "rowId",
		title : "序号",
//		width : '40px',
		align : 'center',
		valign : 'middle',
		formatter : function(value, row, index) {
			return index + 1;
		}
	}, {
		field : "ID",
		title : 'ID',
		align : 'center',
		visible:false
	}, {
		field : "FLIGHT_DATE",
		title : '日期',
		align : 'center'
	}, {
		field : "FLIGHT_NUMBER",
		title : '航班号',
		align : 'center'
	}, {
		field : "DESCRIPTION_CN",
		title : '航空公司',
		align : 'center'
	}, {
		field : "ACTTYPE_CODE",
		title : '机型',
		align : 'center'
	}, {
		field : "AIRCRAFT_NUMBER",
		title : '机号',
		align : 'center'
	}, {
		field : "OPERATOR_NAME",
		title : '操作人',
		align : 'center'
	}];
	return columns;
}



function saveSuccess(){
	layer.close(set);
	$("#baseTable").bootstrapTable('refresh');
}
