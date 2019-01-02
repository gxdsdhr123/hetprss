var layer;
layui.use(["layer", "form"],function(){
	layer = layui.layer;
});
var receiverData = "";
var num = 0;
$("#flightDate").val($("#newGufl").val());
$(function(){
	initSelect();
	jQuery.fn.bootstrapTable.columnDefaults.sortable = true;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	$.ajax({
		type:"post",
		async : false,
		url:ctx+"/appear/produce/jsonData",
		success:function(result){
			receiverData = eval(result);
		}
		
	});
	
	
	
	var editable = true;
	var disabled = false;
	createDetailColumns = [
	      {field : "order",title : "序号",sortable : false,editable : false,formatter:function(value, row, index){return index + 1;}},
	      {field : "id",title : "ID",visible : false},
	      {field:"bootCode",title : "斗车<font class=required>*</font>",
	    	  editable:{title:"斗车",
	    		  		validate:function(v){
	    		  			if(v.length>30){
	    		  				return "超出了额定长度";
	    		  			}
	    		  		}
	    		  }
	      },
	      {field:"bootNumber",title : "集装器代码<font class=required>*</font>",
	    	  editable:{title:"集装器代码",
	    		  validate:function(v){
	    			  if(v.length>30){
	    				  return "超出了额定长度";
	    			  }
	    		  }
	    	  }
	      },
	      {field:"dest",title:"目的站<font class=required>*</font>",
	    	  editable:{title:"目的站",
	    		  validate:function(v){
	    			  if(v.length>30){
	    				  return "超出了额定的长度";
	    			  }
	    		  }
	    	  }
	      },
	      {field:"status",title:"装箱状态<font class=required>*</font>",
	    	  editable:{title:"装箱状态",
	    		  validate:function(v){
	    			  if(v.length>30){
	    				  return "超出了额定的长度";
	    			  }
	    		  }
	    	  }
	      
	      },
	      {field:"numPack",title:"数量<font class=required>*</font>",
	    	  editable:{title:"数量",
	    		  validate:function(v){
	    			  if(isNaN(v)){
	    				  return '数量格式必须是数字';
	    			  }
	    			  if(v.length>30){
	    				  return "超出了额定的长度";
	    			  }
	    		  }
	    	  }
	      },
	      {field:"receiver",
	    	  title:"出港行李司机<font class=required>*</font>",
	    	  'class':"select2",
	    	  editable:{
	    		  title:"出港行李司机",
	    		  type : "select2",
	    		  onblur : "ignore",
	    		  source : receiverData,//JSON对象
	    		  select2:{
	    			  width : "50%",
						language : "zh-CN"
	    		  }
	    			  
	    	  }
	      
	      },
	      {field:"operatorDate",title:"拉运开始时间<font class=required>*</font>",formatter:function(value, row, index){return "<a href='javascript:void(0)' id='receiverdate456_" + index + "'>" + (value ? value : '请选择') + "</a>";}},
	      {field:"receiveTime",title:"接收时间<font class=required>*</font>",formatter:function(value, row, index){return "<a href='javascript:void(0)' id='receiverdate123_" + index + "'>" + (value ? value : '请选择') + "</a>";}},
	      {	  field : "operate",
	    	  title : "操作",
	    	  sortable : false,
	    	  editable : false,
	    	  visible : editable,
	    	  formatter : function(value, row, index){
	    		  return "<i class='fa fa-remove' onclick='removeRow(this)'></i>";
	    		}
	      }
	];
	
	var createDetailOption = {
			method : 'get',
			dataType : 'json',
			striped : true, // 是否显示行间隔色
			pagination : false, // 是否显示分页（*）
			cache : true,
			sortable : false,
			uniqueId : "id",
			undefinedText : '', // undefined时显示文本
			onClickCell:onClickCellDou,
			columns : createDetailColumns
	};
	
	$("#createDetailTable").bootstrapTable(createDetailOption);
	function onClickCellDou(field, value, row, $element){
		if('operatorDate' == field || 'receiveTime' == field){
			popupDate(field, value, row, $element);
		}
	}
	function addEmptyRow(table, num){
		for(var i=0 ; i < num ; i++){
			var length = table.bootstrapTable("getData").length;
			var row = {};
			if(length == 0){
				for(var j = 0; j < createDetailColumns.length; j++){
					row[createDetailColumns[j].field] = null;
				}
			}else{
				var lastRow = table.bootstrapTable("getData")[length - 1];
				for(var j = 0; j < createDetailColumns.length; j++){
					var field = createDetailColumns[j].field;
					row[createDetailColumns[j].field] = null;
				}
			}
			row.id = (new Date()).valueOf();
			table.bootstrapTable('append', row);
		}
	}
	
	
	$("#nextRow").click(function(){
		addEmptyRow($("#createDetailTable"),1);
	});
	(function add(){
		addEmptyRow($("#createDetailTable"),1);
	})();
	
	$(".flightNumber").blur(function(){
		if($("#flightDate").val()=="" || $("#flightDate").val()==null){
			layer.msg("请填写日期",{icon: 0,time: 2000});
			return false;
		}
		$.ajax({
			url:ctx+"/arrival/produce/findInfo",
			type : "POST",
			data :{"flightDate": $("#flightDate").val(),"flightNumber":$(".flightNumber").val()},
			success:function(result){
				if(result!=null){
					$(".aircraftNumber").val(result.AIRCRAFT_NUMBER);
					$(".actstandCode").val(result.ACTSTAND_CODE);
				}else{
					layer.msg("请你重新操作", {icon: 2,time: 2000});

				}
			}
		});
		
		
	});
	
	$("#flightDate").blur(function(){
		if($("#flightDate").val()==null && $(".flightNumber").val()==null){
			return false;
		}
		if($("#flightDate").val()=="" && $(".flightNumber").val()==""){
			return false;
		}
		$.ajax({
			url:ctx+"/arrival/produce/findInfo",
			type : "POST",
			data :{"flightDate": $("#flightDate").val(),"flightNumber":$(".flightNumber").val()},
			success:function(result){
				if(result!=null){
					$(".aircraftNumber").val(result.AIRCRAFT_NUMBER);
					$(".actstandCode").val(result.ACTSTAND_CODE);
				}else{
					layer.msg("请你重新操作", {icon: 2,time: 2000});

				}
			}
		});
		
	});
	
	
	
	
	$("#refreshDetailTable").click(function() {
		$(".select2-selection__rendered").empty();
		$(".select2-selection__rendered").text("请选择");
		
		$("input.layui-input").val("");
		$("select").find("option[text='请选择']").attr("selected", true);
		$("#createDetailTable").bootstrapTable("removeAll");
		addEmptyRow($("#createDetailTable"),1);
	
	});
	
	
	
});

//删除一行
function removeRow(i){
	debugger
	if($("#createDetailTable").bootstrapTable('getData').length > 1){
//		var uniqueid = $(i).parents("tr").data("uniqueid");
//		$("#createDetailTable").bootstrapTable('removeByUniqueId', uniqueid);
		$(i).parents("tr").remove();
	}
}


function save(){
	debugger
	num++;
	if(num!=1){
		return false;
	}
	var index = layer.load(2);
	var dataI = $('#createDetailTable').bootstrapTable('getData');
	var data =  JSON.stringify(dataI);
	
	var flightDateI =  $("#flightDate").val();
	var flightNumberI =  $(".flightNumber").val();
	var aircraftNumberI = $(".aircraftNumber").val();
	var actstandCodeI =  $(".actstandCode").val();
	var chaliI = $(".chali").val();

	
	if(flightDateI=="" || flightNumberI=="" || chaliI==""){
		layer.msg("必须输入项不能为空",{icon: 0,time: 1000},function(){
			layer.close(index);
		});
		num = 0;
		return false;
	}
	
	if(flightDateI==null || flightNumberI==null || chaliI==null){
		layer.msg("必须输入项不能为空",{icon: 0,time: 1000},function(){
			layer.close(index);
		});
		num = 0;
		return false;
	}
	
	if(aircraftNumberI=="" && actstandCodeI==""){
		layer.msg("你输入的航班号不正确",{icon: 0,time: 1000},function(){
			$(".flightNumber").val("");
			layer.close(index);
		});
		num = 0;
		return false;
	}
	if(aircraftNumberI==null && actstandCodeI==null){
		layer.msg("你输入的航班号不正确",{icon: 0,time: 1000},function(){
			$(".flightNumber").val("");
			layer.close(index);
		});
		num = 0;
		return false;
	}
	
	$.ajax({
		url:ctx+"/appear/produce/countAppGufl",
		type : "POST",
		data:{"flightDate":flightDateI,"flightNumber":flightNumberI},
		success:function(result){
			if(result==0){
				$.ajax({
					url:ctx+"/appear/produce/save",
					type : "POST",
					data:{data:data,"flightDate":flightDateI,"flightNumber":flightNumberI,"aircraftNumber":aircraftNumberI,"actstandCode":actstandCodeI,"chali":chaliI},	
					success:function(result){
						if(result!=0){
							layer.msg("成功",{icon: 1,time: 2000},function(){
								layer.close(index);
								window.parent.location.reload();
							});
							return false;
						}else if(result==0){
							layer.msg("必填字段不能为空",{icon: 0,time: 1000},function(){
								layer.close(index);
							});
							return false;
						}
					},
					error:function(){
						return false;
					}
				
				});
			}else{
				layer.msg("今日已经有次航班了,请重新输入！",{icon: 0,time: 1000},function(){
					num = 0;
					layer.close(index);
				});
			}
		}
		
	});
}


function initSelect() {
	$('#douxuefeng').select2({
		placeholder : "请选择",
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

function formatRepoSelection(repo) {
	return repo.text;
}

/**
 * 弹出日期控件
 * @param field
 * @param value
 * @param row
 * @param td
 */
function popupDate(field, value, row, $element){
	var aElem = $element.find("a:eq(0)");
	WdatePicker({
		startDate:value,
		errDealMode:1,
		el:aElem.attr("id"),
		enableKeyboard:false,
		qsEnabled:false,
		isShowClear:false,
		isShowOthers:false,
		autoUpdateOnChanged:false,
		dateFmt:"HHmmss",
		onpicked : function(dp) {
			var newValue = dp.cal.getDateStr();
			if (newValue) {
				row[field] = newValue;
			}
		}
	});
}




