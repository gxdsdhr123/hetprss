var layer, form;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
	form = layui.form;
    form.on('select(templateSel)', function(data){
        var id = data.value;
//        changeTemplate(id);
    });  
});

var createDetailColumns = [];
var dateWeekStr = ["SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"];
var table = $('#createDetailTableDetail');
var officeId =  $('#officeId').val();
var clickId = "";
var time = $('#time').val();

var workerId = "";
var startTm = "";
var endTm = "";
var officeName = "";
var staffId = ""
$(function() {
	// 初始化数据
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	initTable();

	// 增加人员
	$('#addDepartmentBtn').click(function() {
		startTm = "";
		endTm = "";
		workerId = "";
		addOrUpdateStaff("增加领导计划", 'addDepartmentStaff/add?officeId='+officeId+"&pdate="+time);
	});

	// 修改人员
	$('#modifyDepartmentBtn').click(function() {
		if("" == officeId || "" == staffId) {
			layer.msg("请选择人员" , {icon : 7, time : 600});
			return;
		} else {
			addOrUpdateStaff("修改领导计划", 'addDepartmentStaff/modify?officeId='+officeId+"&staffId="+staffId+"&pdate="+time);			
		}
	});
	// 删除人员
	$('#delDepartmentBtn').click(function() {
		if("" == officeId || "" == staffId) {
			layer.msg("请选择人员" , {icon : 7, time : 600});
			return;
		} else {
			delStaff("是否删除当前人员计划？");			
		}
	});
	var divH = $(window).height();
	$("#createDetailTableDiv").css("height", divH - 40);
	new PerfectScrollbar("#createDetailTableDiv");

});
// 删除当前部门选中领导计划
function delStaff(title) {
	var index = layer.confirm(title, {
	    btn: ['确定','取消'], //按钮
	    icon : 3
	}, function(){
		
		$.ajax({
			type : 'post',
			url : ctx + "/leaderPlan/leader/delDepartment/staff",
			data : {
				officeId: officeId,
				staffId: staffId
			},
			success : function(msg) {
				if (msg == "success") {
					layer.msg('操作成功！', {
						icon : 1,
						time : 600
					});
					staffId = "";
					layer.close(index);
					changeTemplate("all");
				} else {
					layer.msg("操作失败" , {icon : 2, time : 600});
					return;
				}
			}
		});
		
	}, function(){
	    layer.close(index);
	});
}

//增加或修改领导计划
function addOrUpdateStaff(title, url) {
	var method;
	layer.open({
		id : 'addStaffIframe',
		type: 2,
		title: title,
		offset: '6%',
		resize:false,
		fix:false,
		area:["80%","88%"],
		content: [ctx + "/leaderPlan/leader/" + url, 'no'],
		btn:["保存","取消"],
		success:function(layero,index){
			var iframeId = document.getElementById('addStaffIframe').getElementsByTagName("iframe")[0].id;
//			$("#"+iframeId).contents().find("body").attr({"style": "min-height: initial;"});
			method = $('#'+iframeId)[0].contentWindow;
		},
		yes:function(index, layero){
			if(null == workerId || "" == workerId) {
				layer.msg("请选择人员" , {icon : 7, time : 600});
				return;
			}
			startTm = method.getStartTm();
			endTm = method.getEndTm();
			if("" != startTm || "" != endTm) {
				var reg = new RegExp("^[0-9]*$");
				if(!reg.test(startTm) || !reg.test(endTm)) {
					layer.msg("请输入正确时间， 如0808" , {icon : 7, time : 600});		
					return;
				}
				if(startTm.trim().length != 4 || endTm.trim().length != 4) {
					layer.msg("请输入四位时间格式， 如0808" , {icon : 7, time : 600});		
					return;
				}
			} else {
				layer.msg("请输入开始时间与结束时间！" , {icon : 7, time : 600});	
				return;
			}
			saveOrUpdateStaff(index);
		},
		btn2: function() {
//			table.bootstrapTable('refreshOptions',{pageNumber : 1});
		}
	});
}
// 保存或修改领导值班
function saveOrUpdateStaff(index) {
	var ind = layer.load(2);
	 $.ajax({
	 		type : 'post',
	 		url : ctx + "/leaderPlan/leader/addStaff/add",
	 		data : {
	 			officeId: officeId,
	 			workerId: workerId,
	 			startTm: startTm,
	 			endTm: endTm,
	 			pdate: time,
	 			id: staffId
	 		},
	 		success : function(msg) {
	 			layer.close(index);
	 			if ("success" != msg) {
	 				staffId = "";
	 				layer.close(ind);
	 				changeTemplate("all");
	 			}
	 		}
	 	});
}

function initTable() {
	getDetailColumns();
}

function getDetailColumns() {
	var createRow2 = [];
	var createRow1 = [
		{
			title : "序号",
			formatter: function (value, row, index) {  
				return index+1;  
            }  
		},{
			field : "ID",
			title : "ID",
			visible : false
		},
		{
			title : "姓名",
			field : "OFFICENAME"
		},{
			field : "STARTTM",
			title : "开始时间"
		},
		{
			field : "ENDTM",
			title : "结束时间"
		},
		{
			field: "PHONE",
			title: "手机",
			formatter: function (value, row, index) {  
				if(null != value) {
					return value;
				} else {
					return '';
				}
	        }  
		} ,
		{
			field: "MOBILE",
			title: "电话",
			formatter: function (value, row, index) {  
				if(null != value) {
					return value;
				} else {
					return '';
				}
	        }  
		},
		{
			field: "DUTY",
			title: "职务",
			formatter: function (value, row, index) {  
				if(null != value) {
					return value;
				} else {
					return '';
				}
	        }  
		}
	];

	createDetailColumns.push(createRow1);
	createDetailColumns.push(createRow2);
	initTableDate();
}

function initTableDate() {
	var createDetailOption = {
			method : 'get',
			striped : true, 
			undefinedText : '',
		    queryParams: function (params) {
		    	var param = {  
		    		officeId : officeId,
		    		pdate: time
                }; 
	            return param;     
		    },
		   /* search : true,
		    searchOnEnterKey : true,*/
			url : ctx + "/leaderPlan/leader/getGridData/leaderDetail",
			columns : createDetailColumns,
			onClickRow:function onClickRow(row,tr,field){
				staffId = row.ID;
				workerId = row.ID;
				officeName = row.OFFICENAME;
				startTm = row.STARTTM;
				endTm = row.ENDTM;
				$(".clickRow").removeClass("clickRow");
				$(tr).addClass("clickRow");
			},
			onDblClickCell: function onDblClickCell(field,value,row,td, tr) {
				officeName = row.OFFICENAME;
				orkerId = row.ID;
				addOrUpdateStaff("修改领导计划", 'addDepartmentStaff/modify?officeId='+officeId+"&staffId="+row.ID+"&pdate="+time);	
			},
			onLoadSuccess : function(data) {
			}
		};
	createDetailOption.height = $(window).height()*0.88;
	table.bootstrapTable(createDetailOption);
}

function changeTemplate(id){
//	table.bootstrapTable('destroy');
	table.bootstrapTable('refresh');
//	table.bootstrapTable('refreshOptions',{pageNumber : 1});

	initTable();
}












