var layer,form;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
	form = layui.form;
});
var workId="";
$(function(){
	initGrid();
	if(dateTp<currentDate){
		$("#addBtn").attr("disabled",true);
		$("#modifyBtn").attr("disabled",true);
		$("#deleteBtn").attr("disabled",true);
		$("#orderBtn").attr("disabled",true);
		$("#stopBtn").attr("disabled",true);
		$("#resumeBtn").attr("disabled",true);
	}
	//增加
	$("#addBtn").click(function(){
		doAddPlan("create");
	});
	//修改
	$("#modifyBtn").click(function(){
		doAddPlan("modify");
	});
	//删除
	$("#deleteBtn").click(function(){
		doDeletePlan();
	});
	//工作排序
	$("#orderBtn").click(function(){
		doOrderPlan();
	});
	//停止工作
	$("#stopBtn").click(function(){
		doStopWork();
	});
	//恢复工作
	$("#resumeBtn").click(function(){
		doResumeWork();
	});
	
});


function initGrid(){
	workId="";
	$("#dataGrid").bootstrapTable({
		striped:true,
		toolbar : "#toolbar",
		idField : "id",
		uniqueId:"id",
		url : ctx + "/arrange/empplan/getGridData",
		method : "get",
		pagination : false,
		showRefresh : false,
		search : false,
		clickToSelect : false,
		undefinedText : "",
		height : $(window).height(),
		columns : getGridColumns(),
		queryParams : function() {
			var param = {
				selDate : dateTp
			};
			return param;
		},
		onClickRow:function onClickRow(row,tr,field){
			workId = row.loginName;
		},
		onCheck:function(row,tr){
			$(".selected").removeClass("selected");
	    },
	    onUncheck:function(row){
	    	$(".selected").removeClass("selected");
	    },
		onLoadSuccess : function(data) {
			var data = $("#dataGrid").bootstrapTable("getData");
			for(var i=0;i<data.length;i++){
				var stopTime = data[i].blockupTime;
				if(stopTime!=null&&stopTime!=""){
					var id = data[i].id;
					var tr = $("tr[data-uniqueid="+id+"]");
					tr.css("background-color","#963545");
					tr.css("color","#ccc");
				}
			}
		},
		customSort:function(fieldName,sortOrder){
			this.data.sort(function(a,b){
				var order = sortOrder === 'desc' ? -1 : 1
				var val1 = a[fieldName];
				if(val1&&val1.indexOf("@")>-1){
					val1 = val1.split("@")[1];
				}
				var val2 = b[fieldName];
				if(val2&&val2.indexOf("@")>-1){
					val2 = val2.split("@")[1];
				}
	            if (val1 === undefined || val1 === null) {
	            	val1 = '';
	            }
	            if (val2 === undefined || val2 === null) {
	            	val2 = '';
	            }
	            if ($.isNumeric(val1) && $.isNumeric(val2)) {
	                // Convert numerical values form string to float.
	            	val1 = parseFloat(val1);
	            	val2 = parseFloat(val2);
	                if (val1<val2) {
	                    return order * -1;
	                }
	                return order;
	            }

	            if (val1 === val2) {
	                return 0;
	            }
	            // If value is not a string, convert to string
	            if (typeof val1 !== 'string') {
	            	val1 = val1.toString();
	            }
	            if (typeof val2 !== 'string') {
	            	val2 = val2.toString();
	            }
	            
	            if (val1.localeCompare(val2,"zh") === -1) {
	                return order * -1;
	            }
	            
	            return order;
			})
		}
	});
}

var stopArr=[];
function getGridColumns() {
	var columns = [ {
		title : "序号",
		formatter : function(value, row, index) {
			return index + 1;
		}
	}, {
		checkbox : true
	}, {
		field : "loginName",
		title : '姓名',
		tabColumn:"login_name",
		sortable : true,
		formatter : function(value, row, index) {
			var tp = value.split("@");
			return tp[1];
		}
	}, {
		field : "shiftsName",
		title : "班制",
		tabColumn:"shifts_name",
		sortable : true
	}, {
		field : "sortnum",
		title : '工作顺序',
		tabColumn:"sortnum",
		sortable : true
	}, {
		sortable : true,
		field : "busyInterval",
		title : '工作间隔(忙)',
		tabColumn:"busy_interval"
	}, {
		sortable : true,
		field : "idleInterval",
		title : '工作间隔(闲)',
		tabColumn:"idle_interval"
	}, {
		sortable : true,
		field : "label1",
		title : '工作时段一',
		tabColumn:"label1"
	}, {
		sortable : true,
		field : "label2",
		title : '工作时段二',
		tabColumn:"label2"
	}, {
		sortable : true,
		field : "label3",
		title : '工作时段三',
		tabColumn:"label3"
	}, {
		sortable : true,
		field : "blockupTime",
		title : '停止工作时段',
		tabColumn:"blockup_time"
	}, {
		field : "blockupReason",
		tabColumn:"blockup_reason",
		title : '停止工作原因'
	}];
	return columns;
}

function doAddPlan(type){
	var ts="";
	if(type=="create"){
		ts = '增加';
	}else{
		ts = '修改';
		var len = $("#dataGrid").bootstrapTable("getSelections").length;
		if(len==0){
			layer.msg('请选择要修改计划的员工！', {
				icon : 2
			});
			return false;
		}
		/*if(len>1){
			layer.msg('只能对一个员工进行修改！', {
				icon : 2
			});
			return false;
		}*/
		var id = $.map($('#dataGrid').bootstrapTable('getSelections'), function (row) {
            return row.id;
        });
	}
	var url = ctx + "/arrange/empplan/addPlanList?type="+type+"&selDate="+dateTp+"&id="+id;
	var set = layer.open({
		type : 2,
		title : ts,
		offset: '2px',
		area:["630px","510px"],
		content : url,
		btn:["保存","重置","返回"],
		yes : function(index, layero) {
			var iframeWin = window[layero.find('iframe')[0]['name']];
			var checkFlag = iframeWin.checkDate();
			if(checkFlag){
				var datas;
				if(type=="create"){
					datas = iframeWin.getDatas();
				}else{
					datas = iframeWin.getModifyDatas();
				}
				
				//先判断是否与已有数据重复
				$.ajax({
					type : 'post',
					url : ctx + "/arrange/empplan/ifHavePlanInfo?type="+type+"&id="+id,
					data : {
						data : datas
					},
					success : function(result) {
						if(result=='ok'){
							$.ajax({
								type : 'post',
								url : ctx + "/arrange/empplan/savePlanInfo?type="+type+"&id="+id,
								data : {
									data : datas
								},
								success : function(msg) {
									if (msg == "success") {
										initGrid();
										$("#dataGrid").bootstrapTable('refresh');
									}
								}
							});
							layer.close(index);
						}else{
							layer.msg(result+'已有排班！', {
								icon : 2
							});
							/*layer.msg(result+'的工作时间重叠！', {
								icon : 2
							});*/
							return false;
						}
					}
				});
			}
		},
		btn2:function(index, layero){
			layer.iframeSrc(index, url);
			return false;
		}
	});
}

function doDeletePlan(){
	if($("#dataGrid").bootstrapTable("getSelections").length==0){
		layer.msg('请选择要删除的计划！', {
			icon : 2
		});
	}else{
		layer.confirm("是否删除选中的人员排班计划？",{offset:'100px'},function(index){
			var ids = $.map($('#dataGrid').bootstrapTable('getSelections'), function (row) {
                return row.id;
            });
			deletePlan(ids);
			layer.close(index);
		});
	}
}

function deletePlan(ids){
	var loading = layer.load(2, {
		 shade: [0.1,'#000'] //0.1透明度
	});
	$.ajax({
		type:'post',
		url:ctx+"/arrange/empplan/deletePlan",
		async:true,
		data:{
			'ids':ids
		},
		dataType:'text',
		success:function(msg){
			layer.close(loading);
			if(msg=="success"){
				layer.msg('删除成功！', {
					icon : 1,
					time : 600
				});
				initGrid();
				$("#dataGrid").bootstrapTable('refresh');
			}else{
				layer.close(loading);
				layer.msg('删除失败！', {
					icon : 2
				});
			}
		}
	});
}

function doOrderPlan(){
	var url = ctx + "/arrange/empplan/showOrderPlan?selDate="+dateTp;
	var set2 = layer.open({
		type : 2,
		title : '人员排序',
		offset: '10px',
		area:["80%","90%"],
		content : url,
		btn:["保存","返回"],
		yes : function(index, layero) {
			var iframeWin = window[layero.find('iframe')[0]['name']];
			var datas = iframeWin.getOrderData();
			$.ajax({
				type : 'post',
				url : ctx + "/arrange/empplan/saveOrder",
				data : {
					data : datas
				},
				success : function(msg) {
					if (msg == "success") {
						layer.msg('保存成功！', {
							icon : 1,
							time : 600
						});
						initGrid();
						$("#dataGrid").bootstrapTable('refresh');
					}
				}
			});
			layer.close(index);
		}
	});
}

function doStopWork(){
	$("#stopStart").val("");
	$("#stopEnd").val("");
	var len = $("#dataGrid").bootstrapTable("getSelections").length;
	if(len==0){
		layer.msg('请选择要停止工作的员工！', {
			icon : 2
		});
		return false;
	}
	
	var chkFlag=true;
	var selections = $('#dataGrid').bootstrapTable('getSelections');
	$.map(selections, function (row) {
		var login = row.loginName;
		if(row.blockupTime!=null){
			var tp = login.split("@")[1];
			layer.msg(tp+'员工是停止状态,无法再次停止工作！', {
				icon : 2
			});
			chkFlag=false;
		}
	});
	
	if(chkFlag){
		var ids = $.map(selections, function (row) {
			var login = row.loginName;
	        return login;
	    });
		layer.open({
			type:1,
			title: "停用时段",
			offset: '20px',
			content:$("#planRangeDate"),
			btn:["确认","返回"],
			success:function(layero,index){
			},
			yes:function(index, layero){
				var times = "";
				var time1 = layero.find(".rangeDate").eq(0).val();
				var time2 = layero.find(".rangeDate").eq(1).val();
				if(time1==""||time2==""){
					layer.msg('请选择要停用开始时间或结束时间', {
						icon : 2
					});
					return false;
				}else{
					times = time1+":00"+","+time2+":00";
					var reason = $("select option:selected").text();
					$.ajax({
						type : 'post',
						url : ctx + "/arrange/empplan/saveStopPlan",
						data : {
							times : times,
							reason:reason,
							ids:ids
						},
						success : function(msg) {
							if (msg == "check") {
								layer.msg('此人员还有未完成的任务，请处理！', {
									icon : 2
								});
								return false;
							}else if (msg == "success") {
								layer.msg('保存成功！', {
									icon : 1,
									time : 600
								});
								initGrid();
								$("#dataGrid").bootstrapTable('refresh');
								layer.close(index);
							}
						}
					});
				}
			}
		});
	}
}

function doResumeWork(){
	var len = $("#dataGrid").bootstrapTable("getSelections").length;
	if(len==0){
		layer.msg('请选择要恢复工作的员工！', {
			icon : 2
		});
		return false;
	}
	var chkFlag=true;
	var selections = $('#dataGrid').bootstrapTable('getSelections');
	$.map(selections, function (row) {
		var login = row.loginName;
		if(row.blockupTime==null){
			var tp = login.split("@")[1];
			layer.msg(tp+'员工是工作状态,无法恢复工作！', {
				icon : 2
			});
			chkFlag=false;
		}
	});
	
	if(chkFlag){
		var ids = $.map(selections, function (row) {
			var login = row.loginName;
	        return login;
	    });
		
		layer.confirm("是否恢复已选择人员工作？",{offset:'100px'},function(index){
			$.ajax({
				type : 'post',
				url : ctx + "/arrange/empplan/resumePlan",
				data : {
					ids:ids
				},
				success : function(msg) {
					if (msg == "success") {
						layer.msg('恢复成功！', {
							icon : 1,
							time : 600
						});
						initGrid();
						$("#dataGrid").bootstrapTable('refresh');
					}
				}
			});
			layer.close(index);
		});
	}
}

