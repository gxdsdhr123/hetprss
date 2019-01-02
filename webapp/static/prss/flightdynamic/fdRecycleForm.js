$(function(){
	layui.use('form');
	var isCreate = $("#mainId").val()==""?true:false;
	var pageType = $("#type").val();
	jQuery.fn.bootstrapTable.columnDefaults.sortable = true;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	var wdateOpt = {};
	var fltProperties,airports,airlines;
	$.ajax({
		type:'post',
		url:ctx+"/plan/longTerm/getOptions",
		async:false,
		dataType:'json',
		success:function(data){
			fltProperties = data["fltProperties"];
			airports = data["airports"];
			airlines = data["airlines"];
		}
	});
	$("#flightDay input").focus(function(){
		WdatePicker(wdateOpt);
	});
	$("#createForm input").prop("readonly",true);
	var editable = false;
	$("#createToolBox").hide();
	if(isCreate || pageType=="edit"){
		editable = true;
		$("#createForm input").prop("readonly",false);
		$("#createToolBox").show();
	}
	var createDetailColumns = [
		           				{field: "order",title:"序号",sortable:false,editable:false,
		        					formatter:function(value,row,index){
		        						return index+1;
		        					}
		        				},
		        				{field: "id", title: "ID", visible: false},
		        				{field: "departApt", title: "起场",
		        					editable:{
		        						type:"select2",
		        						onblur:"ignore",
		        						source:airports,
		        						select2:{
		        						    matcher: customMatcher
		        						}
		        					}
		        				},
		        				{field: "arrivalApt", title: "落场",
		        					editable:{
		        						type:"select2",
		        						onblur:"ignore",
		        						source:airports,
		        						select2:{
		        						    matcher: customMatcher
		        						}
		        					}
		        				},
		        				{field: "aircraftNumber", title: "机号",editable:editable},
		        				{field: "actType", title: "机型",editable:editable},
		        				{field: "std", title: "计起",editable:editable},
		        				{field: "etd", title: "预起",editable:editable},
		        				{field: "sta", title: "计落",editable:editable},
		        				{field: "eta", title: "预落",editable:editable},
		        				{field: "flightNumber", title: "航班号",editable:editable},
		        				{field: "airline", title: "航空公司",
		        					editable:{
		        						type:"select2",
		        						onblur:"ignore",
		        						source:airlines,
		        						select2:{
		        						    matcher: customMatcher
		        						}
		        					}
		        				},
		        				{field: "remark", title: "备注",editable:editable},
		        				{field: "operate", title: "操作",sortable:false,editable:false,visible:editable,
		        					formatter:function(value,row,index){
		        						return "<i class='fa fa-remove' onclick='removeRow(this)'></i>";
		        					}}
		        		    ];
	var uniqueId = 1;
	var createDetailOption = {
			url:ctx+"/plan/recycleBin/getLongtermDetailById",
			method:'get',
			dataType:'json',
			queryParams:function(){
		    	var temp = {
		    	    	id:$("#mainId").val()
		    	}
		    	return temp;
		    },
			striped: true,									 //是否显示行间隔色
		    pagination: false,				   				 //是否显示分页（*）
		    cache: true,
		    uniqueId:"id",
		    columns: createDetailColumns,
		    responseHandler:function(res){
		    	for(var data in res){
		    		data.uniqueId = uniqueId+"";
		    		uniqueId++;
		    	}
		    	return res;
		    },
		    onLoadSuccess:function(data){
		    	if(isCreate){
		    		addEmptyRow($("#createDetailTable"),1);
		    	}
			}
		};
	function addEmptyRow(table){
		var length = table.bootstrapTable("getData").length;
		var row = {};
		if(length==0){
			for(var j=0;j<createDetailColumns.length;j++){
				row[createDetailColumns[j].field]=null;
			}
		}else{
			var lastRow = table.bootstrapTable("getData")[length-1];
			for(var j=0;j<createDetailColumns.length;j++){
				var field = createDetailColumns[j].field;
				if(field == "aircraftNumber" || field=="actType" || field=="airline"){
					row[createDetailColumns[j].field]=lastRow[field];
				}else{
					row[createDetailColumns[j].field]=null;
				}
				
			}
		}
		row["id"]=length;
		table.bootstrapTable('append',row);
	}
	$("#nextRow").click(function(){
		addEmptyRow($("#createDetailTable"),1);
	});
	$("#createDetailTable").bootstrapTable(createDetailOption);
	
	$("#valid").click(function(){
		var dataItme = $("#createDetailTable").bootstrapTable("getData");
		var result = [];
		for(var i=0;i<dataItme.length;i++){
			var planDetail = dataItme[i];
			var isEmptyRow = true;
			for(var field in planDetail){
				if(planDetail[field]){
					isEmptyRow = false;
					break;
				}
			}
			if(!isEmptyRow){
				result.push(planDetail);
			}
		}
		$("#planDetail").text(JSON.stringify(result));
	});
	
	$("#refreshDetailTable").click(function(){
		$("#createDetailTable").bootstrapTable("refresh");
	});
	
	if(isCreate || pageType=="edit"){
		$(".flight_week").on('focus',function(){
			var obj = $(this);
			var value = $(this).val();
			layer.open({
				type:1,
				title: "班期",
				offset: '100px',
				content:$("#flightWeek"),
				btn:["确定"],
				success:function(layero,index){
					layero.find(".layui-unselect").removeClass("layui-form-checked");
					var ofs = value.split('');
					for(var i=0;i<ofs.length;i++){
						if(!isNaN(ofs[i])){
							layero.find(".layui-unselect").eq(i).addClass("layui-form-checked");
						}
					}
				},
				yes:function(index, layero){
					var times = ".......";
					var disabledDays = [];
					layero.find(".layui-form-checked").each(function(){
						var index = layero.find(".layui-unselect").index($(this))+1;
						disabledDays.push(index);
						times = times.substr(0, index-1) + index + times.substring(index, times.length);
					})
					wdateOpt.disabledDays=disabledDays;
					$(".flight_week").val(times);
					layer.close(index);
				}
			});
		});
		$(".flight_date").on("focus",function(){
			var obj = $(this);
			var value = $(this).val();
			layer.open({
				type:1,
				title: "执飞时间段",
				offset: '100px',
				content:$("#flightDate"),
				btn:["确认"],
				success:function(layero,index){
					var dates = value.split("-");
					var input = layero.find(".flightDate");
					input.eq(0).val(dates[0]);
					input.eq(1).val(dates[1]);
				},
				yes:function(index, layero){
					var times = layero.find(".flightDate").eq(0).val()+"-"+layero.find(".flightDate").eq(1).val();
					$(".flight_date").val(times);
					layer.close(index);
				}
			});
		});
		$(".flight_day").on("focus",function(){
			var obj = $(this);
			var value = $(this).val();
			var count = value.split(",").length<4?4:value.split(",").length+1;
			layer.open({
				type:1,
				title: "固定执飞日期",
				offset: '100px',
				content:$("#flightDay"),
				btn:["确认","增加"],
				success:function(layero,index){
					var input = layero.find("input");
					layero.find(".layui-layer-content").css("maxHeight","125px");
					layero.find(".layui-layer-btn0").css("float","right");
					layero.find(".layui-form-label").css("width","80px");
					layero.find(".layui-layer-content").css("overflow","auto !important");
					if(value!=""){
						var dates = value.split(",");
						for(var i=0;i<dates.length;i++){
							if(i<3){
								input.eq(i).val(dates[i]);
							}else{
								var inp = $("<div class='layui-form-item' style='display:flex'>" +
										  "<label class='layui-form-label'>日期"+(i+1)+"</label>" +
										  "<div class='layui-input-inline'>" +
										    "<input placeholder='请输入' class='layui-input Wdate' type='text' value='"+dates[i]+"'>" +
										  "</div>" +
										"</div>");
								layero.find("form").append(inp);
								inp.find(input).focus(function(){
									WdatePicker(wdateOpt);
								})
							}
						}
						layero.find(".layui-form-label").css("width","80px");
					}else{
						input.val("");
					}
				},
				yes:function(index, layero){
					var times = "";
					layero.find("input").each(function(){
						if($(this).val()!=""){
							times += $(this).val()+",";
						}
					});
					times = times.substring(0,times.length-1);
					$(".flight_day").val(times);
					layero.find("input").val("");
					layero.find(".layui-form-item:gt(2)").remove();
					layer.close(index);
				},
				btn2:function(index, layero){
					var input = $("<div class='layui-form-item' style='display:flex'>" +
									  "<label class='layui-form-label'>日期"+count+"</label>" +
									  "<div class='layui-input-inline'>" +
									    "<input placeholder='请输入' class='layui-input Wdate' type='text'>" +
									  "</div>" +
									"</div>");
					input.find(".layui-form-label").css("width","80px");
					input.find("input").focus(function(){
						WdatePicker(wdateOpt);
					})
					layero.find("form").append(input);
					count++;
					return false;
				}
			});
		});
	}
	//添加机型
	$("#addType").click(function(){
		layer.open({
			type:1,
			title:"添加机型",
			area:'750px',
			content:$("#addTypeForm"),
			btn:["保存","重置","返回"],
			success:function(layero,index){
				$("#addTypeForm")[0].reset();
			},
			yes:function(index, layero){
				layer.close(index);
			},
			btn2:function(index, layero){
				$("#addTypeForm")[0].reset();
				return false;
			}
		});
	});
	//添加机号
	$("#addNum").click(function(){
		layer.open({
			type:1,
			title:"添加机号",
			area:'750px',
			content:$("#addNumForm"),
			btn:["保存","重置","返回"],
			success:function(layero,index){
				$("#addNumForm")[0].reset();
			},
			yes:function(index, layero){
				layer.close(index);
			},
			btn2:function(index, layero){
				$("#addNumForm")[0].reset();
				return false;
			}
		});
	});
	//添加机场
	$("#addAirport").click(function(){
		layer.open({
			type:1,
			title:"添加机场",
			area:'750px',
			content:$("#addAirportForm"),
			btn:["保存","重置","返回"],
			success:function(layero,index){
				$("#addAirportForm")[0].reset();
			},
			yes:function(index, layero){
				layer.close(index);
			},
			btn2:function(index, layero){
				$("#addAirportForm")[0].reset();
				return false;
			}
		});
	});
	//航空公司
	$("#addAirline").click(function(){
		layer.open({
			type:1,
			title:"航空公司",
			area:'750px',
			content:$("#addAirlineForm"),
			btn:["保存","重置","返回"],
			success:function(layero,index){
				$("#addAirlineForm")[0].reset();
			},
			yes:function(index, layero){
				layer.close(index);
			},
			btn2:function(index, layero){
				$("#addAirlineForm")[0].reset();
				return false;
			}
		});
	});
})
function removeRow(i){
	if($("#createDetailTable").bootstrapTable('getData').length>1){
		var uniqueid = $(i).parents("tr").data("uniqueid");
		$("#createDetailTable").bootstrapTable('removeByUniqueId', uniqueid);
	}
}
function customMatcher(params, data) {
	var search = data.text+data.pinyin+data.py;
	if ($.trim(params.term) === '') {
      return data;
    }
    if (search.indexOf(params.term) > -1) {
      var modifiedData = $.extend({}, data, true);
      return modifiedData;
    }
    return null;
}