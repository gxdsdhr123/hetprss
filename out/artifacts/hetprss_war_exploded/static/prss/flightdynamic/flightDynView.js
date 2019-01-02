
$(function() {
	// 表格选项默认值
	jQuery.fn.bootstrapTable.columnDefaults.sortable = true;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	$("#createDetailTable").bootstrapTable({
		method : 'get',
		url : ctx+"/flightDynamic/showDetailInfo",
		dataType : 'json',
		queryParams : function() {
			var temp = {
				infltid : $("#infltid").val(),
				outfltid : $("#outfltid").val()
			}
			return temp;
		},
		striped : true, // 是否显示行间隔色
		pagination : false, // 是否显示分页（*）
		cache : false,
		undefinedText : '',
		responseHandler:function(res){
			for(var i in res){
				if(res[i].fltid == $("#infltid").val()){
					$("#infltno").text(res[i].flightNumber);
					$("#alnFlag").text(res[i].alnFlag);
					$("#terminal").text(res[i].terminal);
					$("#inproperty").text(res[i].property);
				}
				if(res[i].fltid == $("#outfltid").val()){
					$("#outfltno").text(res[i].flightNumber);
					$("#outproperty").text(res[i].property);
				}
			}
			return res;
		},
		columns : [ {
			field : "order",
			title : "序号",
			sortable : false,
			editable : false,
			formatter : function(value, row, index) {
				return index + 1;
			}
		}, {
			field : "departApt",
			title : "起场"
		}, {
			field : "arrivalApt",
			title : "落场"
		}, {
			field : "aircraftNumber",
			title : "机号"
		}, {
			field : "actType",
			title : "机型"
		}, {
			field : "std",
			title : "计起"
		}, {
			field : "etd",
			title : "预起"
		}, {
			field : "sta",
			title : "计落"
		}, {
			field : "eta",
			title : "预落"
		}, {
			field : "flightNumber",
			title : "航班号"
		}, {
			field : "airline",
			title : "航空公司"
		}, {
			field : "remark",
			title : "备注"
		}]
	});
});