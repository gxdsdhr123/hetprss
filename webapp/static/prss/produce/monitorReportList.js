var layer;
var clickRowId = "";
var clickRowCode = "";
var chooseTypes = [];
var set = null;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});
$(document).ready(function() {
	initGrid();
	// 查询
	$('#searchBtn').click(function(){
		refresh();
	});
	//生成报表
	$("#newBtn").click(function() {
		layer.open({
			type:1,
			title:'生成报告',
			content:$('#createView'),
			area:['600px','500px'],
			btn : [ "生成", "取消" ],
			btn1 : function(index, layero) {
				var statDay = $('#statDay').val();
				var flightNumber = $('#flightNumber').val();
				
				$.ajax({
					async : false,
					type : 'post',
					url : ctx + '/produce/monitor/getReport',
					data : {
						statDay:statDay,
						flightNumber:flightNumber
					},
					error : function() {
						layer.msg('生成失败！', {
							icon : 2
						});
					},
					success : function(dataMap) {
						if(dataMap.code == 0){
							createReport(index);
						}else{
							layer.confirm('报告已存在，请确认是否覆盖？',function(layindex){
								createReport(index);
								layer.close(layindex);
							});
						}
					}
				});
			}
		});
	});
	
	//修改报表
	$("#editBtn").click(function() {
		if (clickRowId=="") {
			layer.msg("请选择要修改的报表！", {
				icon : 7
			});
			return false;
		}
		set = layer.open({
			type : 2,
			title : "修改报表",
			area:["100%","100%"],
			content : ctx + "/produce/monitor/edit?id="+clickRowId,
			btn : [ "保存", "取消" ],
			btn1 : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.addBill();
			}
		});
	});
	
	//删除报表
	$("#delBtn").click(function() {
		if (clickRowId=="") {
			layer.msg("请选择要删除的报表！", {
				icon : 7
			});
			return false;
		}
		var confirm = layer.confirm('您确定要删除选中报表？', {
			btn : [ '确定', '取消' ]
		// 按钮
		}, function() {
			$.ajax({
				async : false,
				type : 'post',
				url : ctx + '/produce/monitor/delete',
				data : {
					'id':clickRowId
				},
				error : function() {
					layer.msg('删除失败！', {
						icon : 2
					});
				},
				success : function(dataMap) {
					if(dataMap.code == 0){
						layer.msg('删除成功！', {
							icon : 1,
							time : 600
						}, function() {
							$("#billGrid").bootstrapTable('refresh');
						});
					}else{
						layer.msg('删除失败！', {
							icon : 2
						}, function() {
							$("#billGrid").bootstrapTable('refresh');
						})
					}
				}
			});
		});
	});
	
	// 打印
	$('#printBtn').on('click',function(e){
		if (clickRowId=="") {
			layer.msg("请选择要打印的报表！", {
				icon : 7
			});
			return false;
		}
		var form = $('<form action="'+ctx+'/produce/monitor/print" method="post"></form>');
		form.append('<input type="hidden" name="id" value="'+clickRowId+'"/>')
		$('body').append(form);
		form.submit();
	});
	
	// 加载选择保障任务
	$.each(restypes,function(k,v){
		$('#chooseDept').append('<option value="'+k+'">'+k+'</option>');
	});
	loadSortable($('#chooseDept').val());
	$('#chooseDept').on('change',function(){
		var key = $(this).val();
		loadSortable(key);
	});
	
	$(".list-group").each(function(){
		$(this).css("position","relative");
		scrollArr.push(new PerfectScrollbar(this));
	});
	$("#pushleft").click(function(){
		var bechoose = $("#rightDiv .bechoose");
		bechoose.removeClass("bechoose");
		$("#leftDiv .list-group").append(bechoose);
		removeArrByValue(chooseTypes,bechoose.attr('data-code'));
	})
	$("#pushright").click(function(){
		var bechoose = $("#leftDiv .bechoose");
		bechoose.removeClass("bechoose");
		bechoose.each(function(){
			$(".choosedField ").append($(this));
			chooseTypes.push($(this).attr('data-code'));
		})
	})
	
	
});

function gridOptions(){
	return {
		striped : true,
		toolbar : "#toolbar",
		idField : "id",
		uniqueId : "rowId",
		url : ctx + "/produce/monitor/getData",
		method : "get",
		sidePagination: "server",
		showRefresh : false,
		clickToSelect : false,
		undefinedText : "",
		height : $(window).height(),
		columns : getGridColumns(),
		onClickRow:function onClickRow(row,tr,field){
			clickRowId = row.id;
//			clickRowCode = row.TYPE_CODE;
			$(".clickRow").removeClass("clickRow");
			$(tr).addClass("clickRow");
		},
		pageSize:10,
		pageList : [10, 20, 50],
		pagination : true,
		queryParams:function(param){
			return {
				offset : param.offset,
				limit : param.limit,
				searchText : $('#searchText').val(),
				startDate : $('#startDate').val(),
				endDate : $('#endDate').val()
			}
		}
		
	}
}

/**
 * 初始化表格
 */
function initGrid() {
	$("#billGrid").bootstrapTable(gridOptions());
}
/**
 * 表格列与数据映射
 * 
 * @returns {Array}
 */
function getGridColumns() {
	var columns = [ {
		field : "rowId",
		title : "序号",
		width : '40px',
		align : 'center',
		valign : 'middle',
		formatter : function(value, row, index) {
			return index + 1;
		}
	}, {
		field : "statDay",
		title : '航班日期',
		align : 'center'
	}, {
		field : "inFlightNumber",
		title : '进港航班号',
		align : 'center'
	}, {
		field : "outFlightNumber",
		title : '出港航班号',
		align : 'center'
	}, {
		field : "operatorName",
		title : '创建人',
		align : 'center'
	}, {
		field : "updateDate",
		title : '生效日期',
		align : 'center'
	}];
	return columns;
}



function refresh(){
	layer.close(set);
	$("#billGrid").bootstrapTable('destroy');
	$("#billGrid").bootstrapTable(gridOptions());
}

function loadSortable(key){
	$('.list-group.sortable').html('');
	var vlist = restypes[key];
	$.each(vlist,function(i,item){
		if(chooseTypes.indexOf(item.value) < 0){
			var li = $('<li class="list-group-item" onmousedown="liDown(this);" onmouseenter="liEnter(this);" data-code="'+item.value+'" >'+item.text+'</li>');
			$('.list-group.sortable').append(li);
		}
	});
}

function removeArrByValue(array , val){
	for(var i=0;i<array.length;i++){
		var item = array[i];
		if(item == val){
			array.splice(i, 1);
			break;
		}
	}
}

var holdDown = 0;
function liClick(obj) {
	if($(obj).hasClass("bechoose")){
		$(obj).removeClass("bechoose");
	}else{
		$(obj).addClass("bechoose");
	}
}
function liDown(obj) {
	holdDown = 1;
	if($(obj).hasClass("bechoose")){
		$(obj).removeClass("bechoose");
	}else{
		$(obj).addClass("bechoose");
	}
}
$(document).mouseup(function(){
	holdDown = 0;
});
function liEnter(obj) {
	if (holdDown == 1) {
		if(!sorting){
			if (!$(obj).hasClass("bechoose")) {
				$(obj).addClass("bechoose");
			}else{
				$(obj).removeClass("bechoose");
			}
		}
	}
}

function createReport(layerIndex){
	var loadIndex = layer.load(2);
	var jobTypes = '';
	$.each(chooseTypes,function(i,item){
		jobTypes+=(','+item);
	});
	jobTypes = jobTypes.substring(1);
	$.ajax({
		async : false,
		type : 'post',
		url : ctx + '/produce/monitor/create',
		data : {
			statDay:$('#statDay').val(),
			flightNumber:$('#flightNumber').val(),
			jobTypes:jobTypes
		},
		error : function() {
			layer.msg('生成失败！', {
				icon : 2
			});
		},
		success : function(dataMap) {
			if(dataMap.code == 0){
				layer.close(loadIndex);
				layer.close(layerIndex);
				$("#billGrid").bootstrapTable('refresh');
				set = layer.open({
					type : 2,
					title : "修改报表",
					content : ctx + "/produce/monitor/edit?id="+dataMap.data,
					btn : [ "保存", "取消" ],
					btn1 : function(index, layero) {
						var iframeWin = window[layero.find('iframe')[0]['name']];
						iframeWin.addBill();
						return false;
					}
				});
				layer.full(set);
			}else{
				layer.msg(dataMap.msg, {
					icon : 2
				});
			}
		}
	});
}
function saveSuccess(){
	layer.msg('保存成功', {
		  icon: 1,
		  time: 600 
		});
	layer.close(set);
}