var layer,form;
var clickId = "";
var clickFeild="";
var ifModify=false;
var createDetailColumns=[];
var oldLen,len;
var table = $('#createDetailTable');
var jwLimit,jxLimit,hsLimit;//机位,机型,航空公司
var checkEditData = [];//保存修改过职责的数据

$(function() {
	layui.use('form');
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	layui.use([ "layer", "form", "element" ], function() {
		layer = layui.layer;
		form = layui.form;
		form.on('checkbox(checkBox)', function(data){
    	  checkClick(data.elem);
    	}); 
		form.on('checkbox(chooseBox)', function(data){
    	  $(data.elem).click();
    	}); 
		initTable();
	});
	
	//刷新
	$("#refreshBtn").click(function(){
		table.bootstrapTable('destroy');
		initTable();
	});
	//修改
	$("#modifyBtn").click(function(){
		doModify();
	});
	//保存
	$("#saveBtn").click(function(){
		doSave();
	});
	//查询
	$('#searchName').on('keydown',function(event){
		if(event.keyCode == 13){
			table.bootstrapTable('destroy');
			initTable();
		}
	});
	$("#searchBtn").click(function(){
		table.bootstrapTable('destroy');
		initTable();
	});
	//删除
	$("#deleteBtn").click(function(){
		deleteInfo();
	});
	//设置div高度
	$("#createDetailTableDiv").css("height",$(window).height()-40);
});

function initTable() {
	clickId = "";
	clickFeild="";
	ifModify=false;
	oldLen=null,len=null;
	createDetailColumns=[];
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/aptitude/info/getTableHeader",
		data : null,
		dataType : "json",
		success : function(result) {
			jwLimit = result.jwLimit;
			jxLimit = result.jxLimit;
			hsLimit = result.hsLimit;
			getDetailColumns(result);
		}
	});
}

function getDetailColumns(heaerPart){
	createDetailColumns = [
		{
			title : "序号",
			formatter : function(value, row, index) {
				return index + 1;
			}
		},{
			checkbox : true
		},{
			field : "ID",
			title : "ID",
			visible : false
		},
		{
			field : "GROUP_ID",
			title : "GROUP_ID",
			visible : false
		},{
			field : "LONGIN_ID",
			title : "LONGIN_ID",
			visible : false
		},
		{
			title:"班组",
			field:"GROUP_NAME",
			sortable :true
		} ,
		{
			title:"员工",
			field:"LOGIN_NAME",
			sortable :true
		} 
	];
	var colsArr = heaerPart.rstypeArr;
	for(var i in colsArr){
		var colObj = colsArr[i];
		var col = {
				title:colObj.TITLE,
				field:"FIELD_"+colObj.FIELD,
				sortable :false,
				class:"CLS_"+colObj.FIELD,
				formatter : function(value, row, index) {
					var tp="";
					if(value!=null&&value!=""){
						tp="checked";
					}
					return "<div class='layui-form-item'><div class='layui-inline'><input lay-skin='primary' lay-filter='checkBox' name='selectBox' class='selBox' id='"+value+"' "+tp+" disabled='disabled' type='checkbox' onchange='checkClick(this)'></div></div>";
				}
		};
		createDetailColumns.push($.extend({},true,col));
	}
	
	var limitStr = heaerPart.limitStr;
	if(limitStr!=null){
		var tp = limitStr.split(",");
		var limit1=false,limit2=false,limit3=false;
		if(tp[0]=="1"){
			limit1=true;
		}
		if(tp[1]=="1"){
			limit2=true;
		}
		if(tp[2]=="1"){
			limit3=true;
		}
		if(limit1){
			var col = {
				title:"机位",
				field:"JW_LIMIT",
				sortable :true,
				editable : {
					disabled : true,
					type : "select2",
					title : "机位",
					onblur : "ignore",
					source : jwLimit,
					select2 : {
						matcher : customMatcher
					}
				}
			};
			createDetailColumns.push(col);
		}
		
		if(limit2){
			var col = {
				title:"机型",
				field:"JX_LIMIT",
				sortable :true,
				editable : {
					disabled : true,
					type : "select2",
					title : "机型",
					onblur : "ignore",
					source : jxLimit,
					select2 : {
						matcher : customMatcher
					}
				}
			};
			createDetailColumns.push(col);
		}
		
		if(limit3){
			var col = {
				title:"航空公司",
				field:"HS_LIMIT",
				sortable :true,
				editable : {
					disabled : true,
					type : "select2",
					title : "航空公司",
					onblur : "ignore",
					source : hsLimit,
					select2 : {
						matcher : customMatcher
					}
				}
			};
			createDetailColumns.push(col);
		}
	}
	var col = {
		field : "operate",
		title : "操作",
		sortable : false,
		editable : false,
		formatter : function(value, row, index) {
			return "<span class='addSpan' style='display:none;cursor:pointer'><i class='fa fa-plus ' onclick='addEmptyRow(this)'></i></span>" +
					"<span class='delSpan' style='display:none;cursor:pointer'><i class='fa fa-remove ' onclick='removeRow(this)'></i></span>";
		}
	};
	createDetailColumns.push(col);
	initTableDate();
}

function initTableDate() {
	var createDetailOption = {
			url : ctx + "/aptitude/info/getGridData",
			method : 'get',
			dataType : 'json',
			striped : true, 
			uniqueId : "RM",
			toolbar:$("#tool-box"),
		    queryParams: function (params) {
		    	var param = {  
                    searchName : $("#searchName").val(),
                }; 
	            return param;     
		    },
			columns : createDetailColumns,
			onClickRow:function onClickRow(row,tr,field){
				clickId = row.ID+","+row.GROUP_ID+","+row.LONGIN_ID;
				$(".clickRow").removeClass("clickRow");
				$(tr).addClass("clickRow");
			},
			onClickCell:function onClickCell(field,value,row,td ){
				clickFeild=field;
			},
			onLoadSuccess : function(data) {
				oldLen = table.bootstrapTable("getData").length;
				len=oldLen;
				$("input[name='btSelectAll'],input[name='btSelectItem']").attr("lay-ignore","");
				form.render('checkbox');
			},
			onPostBody : function(){
				if(ifModify){
					$('.selBox').removeAttr("disabled");
					$('.editable').editable('enable');
				}
				$("input[name='btSelectAll'],input[name='btSelectItem']").attr("lay-ignore","");
				form.render('checkbox');
			}
		};
	createDetailOption.height = $(window).height() - 40;
	table.bootstrapTable(createDetailOption);
}

function mergeCells(data,fieldName,colspan,target){
    var sortMap = {};
    for(var i = 0 ; i < data.length ; i++){
        for(var prop in data[i]){
            if(prop == fieldName){
                var key = data[i][prop]
                if(sortMap.hasOwnProperty(key)){
                    sortMap[key] = sortMap[key] * 1 + 1;
                } else {
                    sortMap[key] = 1;
                }
                break;
            } 
        }
    }
    for(var prop in sortMap){
        console.log(prop,sortMap[prop])
    }
    var index = 0;
    for(var prop in sortMap){
        var count = sortMap[prop] * 1;
        $(target).bootstrapTable('mergeCells',{index:index, field:fieldName, colspan: colspan, rowspan: count});   
        index += count;
    }
}

function customMatcher(params, data) {
	if ($.trim(params.term) === '') {
		return data;
	}
	for ( var i in data) {
		var search = JSON.stringify(data[i]).toLowerCase();
		var term = params.term.toLowerCase();
		if (i != "id" && i != "disabled" && i != "selected" && i != "element") {
			if (search.indexOf(term) > -1) {
				var modifiedData = $.extend({}, data, true);
				modifiedData.text += ' (' + JSON.stringify(data[i]) + ')';
				return modifiedData;
			}
		}
	}
	return null;
}

function doModify(){
	if(!ifModify){
		$('.selBox').removeAttr("disabled");
		$('.editable').editable('toggleDisabled');
		$('.addSpan').show();
		ifModify=true;
		form.render('checkbox');
	}
}

function addEmptyRow(i) {
	var index = $(i).parents("tr").data("index");
	var uniqueid = $(i).parents("tr").data("uniqueid");
	var data = table.bootstrapTable('getRowByUniqueId', uniqueid);
	var row = {};
	for (var j = 0; j < createDetailColumns.length; j++) {
		var field = createDetailColumns[j].field;
		if(field=='undefined'){
			return;
		}
		if(field=="GROUP_ID"||field=="GROUP_NAME"||field=="LONGIN_ID"||field=="LOGIN_NAME"){
			row[field] = data[field];
		}else{
			row[field] = "";
		}
	}
	row["RM"]=len+1;
	len=len+1;
	table.bootstrapTable('insertRow', {index:index+1,row:row});
	doClickSub('add');
}

function doClickSub(flag){
	$('.selBox').removeAttr("disabled");
	$('.editable').editable('toggleDisabled');
	$('.addSpan').show();
	var addTrs = table.find("tbody>tr");
	addTrs.each(function(){
		var idstr = $(this).data("uniqueid");
		if(idstr>oldLen){
			$(this).find('.delSpan').show();
			$(this).find('.addSpan').hide();
		}
	});
	$("input[name='btSelectAll'],input[name='btSelectItem']").attr("lay-ignore","");
	form.render();
}

function removeRow(i){
	var uniqueid = $(i).parents("tr").data("uniqueid");
	table.bootstrapTable('removeByUniqueId', uniqueid);
	doClickSub();
}

function checkClick(obj){
	var uniqueid = $(obj).parents("tr").data("uniqueid");
	var data = table.bootstrapTable('getRowByUniqueId', uniqueid);
	var cls = $(obj).parents("td").attr("class");
	var cid = cls.replace("CLS_","");
	var clkField = "FIELD_"+cid;
	if($(obj).is(':checked')){
		data[clkField] = cid;
	}else{
		data[clkField] = "";
	}
	//操作的资质人员
	var workid = data["LONGIN_ID"];
	if(checkEditData.length == 0){
		checkEditData =checkEditData.concat(data);
	}else{
		for( var i = 0; i < checkEditData.length; i++ ){
			//判断是否多次的操作同一个人的资质，取最后一次的操作
			var oneData = checkEditData[i];
			if(workid == oneData["LONGIN_ID"]){
				checkEditData.splice(i,1,data);
			}else{
				checkEditData =checkEditData.concat(data);
			}
		}
	}
	
	
	
}

function doSave(){
	if(ifModify){
		var data = table.bootstrapTable("getData");
		//alert(JSON.stringify(data))
		var editDataStr = JSON.stringify(checkEditData);
		checkEditData = [];
		$.ajax({
			type : 'post',
			url : ctx + "/aptitude/info/saveInfo",
			data : {
				data : JSON.stringify(data),
				editData: editDataStr
			},
			success : function(msg) {
				if (msg == "success") {
					layer.msg('保存成功！', {
						icon : 1,
						time : 600
					});
					initTable();
					table.bootstrapTable('refresh');
				}
			}
		});
		
	}
}

function deleteInfo(){
	if(table.bootstrapTable("getSelections").length==0){
		layer.msg('请选择要删除的人员作业资质！', {
			icon : 2
		});
	}else{
		layer.confirm("是否删除选中的人员作业资质？",{offset:'100px'},function(index){
			var ids = $.map(table.bootstrapTable('getSelections'), function (row) {
				if(row.ID!=""&&row.ID!=null){
					return row.ID;
				}
            });
			deleteAtpi(ids);
			layer.close(index);
		});
	}
}

function deleteAtpi(ids){
	if(ids.length>0){
		$.ajax({
			type:'post',
			url:ctx+"/aptitude/info/deleteApti",
			async:true,
			data:{
				'ids':ids
			},
			dataType:'text',
			success:function(msg){
				if(msg=="success"){
					layer.msg('删除成功！', {
						icon : 1,
						time : 600
					});
					initTable();
					table.bootstrapTable('refresh');
				}else{
					layer.msg('删除失败！', {
						icon : 2
					});
				}
			}
		});
	}else{
		layer.msg('请选择已存在作业资质的人员！', {
			icon : 2
		});
	}
	
}