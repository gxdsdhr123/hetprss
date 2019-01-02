var layer;
var clickRowId = "";
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});
$(function(){
	initGrid();
});
function initGrid(){
	$("#dataGrid").bootstrapTable({
		striped:true,
		toolbar : "#toolbar",
		idField : "id",
		uniqueId:"id",
		url : ctx + "/arrange/empplan/getShiftGridData",
		method : "get",
		pagination : false,
		showRefresh : false,
		queryParams: function (params) {
	    	var param = {  
	    			type : type,
	    			id1:id1,
	    			id2:id2,
	    			id3:id3
            }; 
            return param;     
	    },
		search : false,
		clickToSelect : false,
		undefinedText : "",
		columns : getGridColumns(),
		onClickRow:function onClickRow(row,tr,field){
			clickRowId = row.id;
			var id1 = "stime"+type;
			var id2 = "etime"+type;
			var id3 = "sid"+type;
			var tp = clickRowId.split(",");
			parent.document.getElementById(id1).value=tp[1];
			parent.document.getElementById(id2).value=tp[2];
			parent.document.getElementById(id3).value=tp[0];
			if(type=="1"){
				parent.document.getElementById("stime2").disabled="";
				parent.document.getElementById("etime2").disabled="";
				parent.document.getElementById("bzBtn2").disabled="";
			}
			if(type=="2"){
				parent.document.getElementById("stime3").disabled="";
				parent.document.getElementById("etime3").disabled="";
				parent.document.getElementById("bzBtn3").disabled="";
			}
			
			var index = parent.layer.getFrameIndex(window.name);
			parent.layer.close(index);
		}
	});
	
}

function getGridColumns() {
	var columns = [ {
		title : "序号",
		formatter : function(value, row, index) {
			return index + 1;
		}
	}, {
		field : "shiftsName",
		title : '班制名称',
		tabColumn:"shifts_name"
	}, {
		field : "stime",
		title : "上班时间",
		tabColumn:"stime"
	}, {
		field : "etime",
		title : '下班时间',
		tabColumn:"etime"
	}];
	return columns;
}