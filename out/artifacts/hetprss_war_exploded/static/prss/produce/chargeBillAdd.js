layui.use([ "layer", "form", "element" ], function() {
	var form = layui.form;
});

$(document).ready(function() {
//	$("flightDate").blur(function(){
		//alert("111");
//	}) ;
});
var tableOptions = {
		url : ctx + "/fdHistorical/getDynamic", // 请求后台的URL（*）
		method : "get", // 请求方式（*）
		dataType : "json", // 返回结果格式
		striped : true, // 是否显示行间隔色
		pagination : false, // 是否显示分页（*）
		cache : true, // 是否启用缓存
		undefinedText : '', // undefined时显示文本
		checkboxHeader : false, // 是否显示全选
		toolbar : $("#tool-box"), // 指定工具栏dom
		search : true, // 是否开启搜索功能
		columns : getBaseColumns(),
		contextMenu : '#context-menu',
		queryParams : function(params) {
			var temp = {
			schema : "1",
			suffix : "_HIS",
			hisDate : $("#hisdate").val()
			}
			return temp;
		},
		responseHandler : function(res) {
			tableData = res;
			return res.slice(0, limit);
		},
		onContextMenuItem : function(row, $el) {
			if ($el.data("item") == "edit") {
				alert("Edit: " + row.inFltno);
			} else if ($el.data("item") == "delete") {
				alert("Delete: " + row);
			} else if ($el.data("item") == "action1") {
				alert("Action1: " + row);
			} else if ($el.data("item") == "action2") {
				alert("Action2: " + row);
			}
		},
		onClickRow : function(row, tr, field) {
			if (field) {
				var th = $("#baseTable").find(".separator").eq(0).next();
				var ex = $("th[data-field=" + field + "]").offset().left;
				if (th) {
					var borderLeft = th.offset().left;
					if (ex >= borderLeft) {
						clickRowId = row.out_fltid;
					} else {
						clickRowId = row.in_fltid;
					}
				} else {
					clickRowId = row.in_fltid;
				}
				clickRow = row;
				$(".clickRow").removeClass("clickRow");
				$(tr).addClass("clickRow");
			}
		}
		};
function getBaseColumns() {
	var columns = [];
	$.ajax({
	type : 'post',
	url : ctx + "/fdHistorical/getDefaultColumns",
	async : false,
	data : {
		'schema' : "1"
	},
	dataType : 'json',
	success : function(column) {
		var order = [ {
		field : 'order',
		title : '序号',
		sortable : false,
		editable : false,
		formatter : function(value, row, index) {
			return index + 1;
		}
		}, {
		field : "checkbox",
		checkbox : true,
		sortable : false,
		editable : false
		} ];
		columns = order.concat(column);
	}
	});
	return columns;
}
//日期选择
function dateFilter() {
	WdatePicker({
	maxDate:'%y-%M-{%d-2}',
	dateFmt : 'yyyyMMdd',
	onpicking : function(dp) {
		tableOptions.queryParams = {
		schema : "1",
		hisDate : dp.cal.getNewDateStr()
		}
		$("#baseTable").bootstrapTable('refreshOptions', tableOptions);
	}
	})
}
//输入框联动



function doQuery(){
	var flightDate= $("input[name='flightDate']").val()
	var flightNumber= $("input[name='flightNumber']").val();
	var inOutFlag = $("select[name='inOutFlag']").val();
	if (flightDate!=""&&flightNumber!=""&&inOutFlag!=""&&inOutFlag!="0") {		
		$.ajax({
			type : 'post',
			async : false,
			url : ctx + "/produce/chargeBill/getChargeBillDetail",
			data : {
				'flightDate':flightDate,
				'flightNumber':flightNumber,
				'inOutFlag':inOutFlag
			},
			dataType: "json",
			success : function(result) {
				if (result&&!$.isEmptyObject(result)) {					
					$("#actstandCode").val(result.ACTSTAND_CODE);
					$("#actTypeCode").val(result.ACTTYPE_CODE);
					if (inOutFlag=="A") {
						$("#sta").val(result.STA);
						$("#eta").val(result.ETA);
						$("#ata").val(result.ATA);
						document.getElementById("stLab").innerHTML="STA";
						document.getElementById("etLab").innerHTML="ETA";
						document.getElementById("atLab").innerHTML="ATA";
					}
					if (inOutFlag=="D") {
						$("#sta").val(result.STD);
						$("#eta").val(result.ETD);
						$("#ata").val(result.ATD);
						document.getElementById("stLab").innerHTML="STD";
						document.getElementById("etLab").innerHTML="ETD";
						document.getElementById("atLab").innerHTML="ATD";
					}
					$("#fltId").val(result.FLTID);
					$("#aircraftNumber").val(result.AIRCRAFT_NUMBER);
					document.getElementById("actstandCode").style.display="";
					document.getElementById("showLab").style.display="";
					document.getElementById("detailGrid").style.display="";					
					$("#showDiv").show();
					$("#serialDiv").show();
				}
				else {
					document.getElementById("actstandCode").style.display="none";
					document.getElementById("showLab").style.display="none";
					document.getElementById("detailGrid").style.display="none";
					$("#showDiv").hide();
					$("#serialDiv").hide();
					layer.msg('无此航班信息！', {
						icon : 2,
						time : 1000
					});
				}
			}
		});						
	}	
}

function addBill(){
	$('#billForm').ajaxSubmit({
		async:false,
		success:function(data){
			if(data=="success"){
				layer.msg('保存成功！', {
					icon : 1,
					time : 1000
				});
			}else{
				layer.msg('保存失败！', {
					icon : 2,
					time : 1000
				});
			}
		}
	});
}


