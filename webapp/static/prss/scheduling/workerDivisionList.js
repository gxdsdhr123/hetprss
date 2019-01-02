var layer,form;
var clickId = "";
var clickFeild="";
var ifModify=false;
var createDetailColumns=[];
var detailColumns=["RM","ID","GROUP_ID","LONGIN_ID","GROUP_NAME","LOGIN_NAME","PARENT_APTITUDE_ID"];
var oldLen,len;
var table = $('#createDetailTable');
var aptitable = $('#aptiduteTable');
var templateId = "all";
var group = "";
var jwLimit,jxLimit,hsLimit,aptiLimt;
$(function(){
	$("#templateConf").val("all");
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	layui.use([ "layer", "form", "element" ], function() {
		layer = layui.layer;
		form = layui.form;
	    form.on('select(templateSel)', function(data){
	        var id = data.value;
	        changeTemplate(id);
	    });
	    form.on('select(groups)', function(data){
	        var name = data.value;
	        changeGroup(name);
	    });
	    form.on('checkbox(checkBox)', function(data){
    	  checkClick(data.elem);
    	});        
	    initTable();
	});
	
	//修改
	$("#modifyBtn").click(function(){
		doModify();
	});
	//查询
	$("#searchBtn").click(function(){
		table.bootstrapTable('destroy');
		initTable();
	});
	//生效
	$("#saveBtn").click(function(){
		doSave();
	});
	
	//分工版本维护
	$("#templateBtn").click(function(){
		doTemplate();
	});
	
	var divH=$(window).height();
	$("#createDetailTableDiv").css("height",divH-40);
	new PerfectScrollbar("#createDetailTableDiv");
});

function changeTemplate(id){
	table.bootstrapTable('destroy');
	templateId = id;
	initTable();
}
function changeGroup(name){
	table.bootstrapTable('destroy');
	group = name;
	initTable();
}

function initTable(){
	clickId = "";
	clickFeild="";
	ifModify=false;
	oldLen=null,len=null;
	createDetailColumns=[];
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/division/info/getTableHeader",
		data : null,
		dataType : "json",
		success : function(result) {
			jwLimit = result.limit0;
			$.each(jwLimit,function(k,v){
				v.unshift({text:' ',value:k});
			});
			jxLimit = result.limit1;
			$.each(jxLimit,function(k,v){
				v.unshift({text:' ',value:k});
			});
			hsLimit = result.limit2;
			$.each(hsLimit,function(k,v){
				v.unshift({text:' ',value:k});
			});
			aptiLimt = result.aptiLimt;
			getDetailColumns(result);
		}
	});
}

function getDetailColumns(heaerPart){
	var createRow2 = [];
	var createRow1 = [
		{
			title : "序号",
			field : "RM",
			colspan: 1,
			rowspan: 2,
			width:30
		},{
			field : "ID",
			title : "ID",
			visible : false,
			colspan: 1,
			rowspan: 2
		},
		{
			field : "GROUP_ID",
			title : "GROUP_ID",
			visible : false,
			colspan: 1,
			rowspan: 2
		},{
			field : "LONGIN_ID",
			title : "LONGIN_ID",
			visible : false,
			colspan: 1,
			rowspan: 2
		},
		{
			field : "PARENT_APTITUDE_ID",
			title : "PARENT_APTITUDE_ID",
			visible : false,
			colspan: 1,
			rowspan: 2
		},
		{
			title:"班组",
			field:"GROUP_NAME",
			sortable :true,
			colspan: 1,
			rowspan: 2,
			width:120
		} ,
		{
			title:"员工",
			field:"LOGIN_NAME",
			sortable :true,
			colspan: 1,
			rowspan: 2,
			width:80
		}
	];
	var colsArr = heaerPart.rstypeArr;
	for(var i in colsArr){
		var colObj = colsArr[i];
		var col = {
				title:colObj.TITLE,
				sortable :false,
				colspan: 2,
				rowspan: 1,
				width:60
		};
		createRow1.push($.extend({},true,col));
		var col2 = {
				title:"主",
				field:"FIELD_"+colObj.FIELD+"_1",
				sortable :false,
				class:""+colObj.FIELD+"_1",
				formatter : function(value, row, index) {
					var tp="";
					if(value!=null&&value!=""){
						tp="checked";
					}
					return "<div class='layui-form-item'><div class='layui-inline'><input lay-skin='primary' lay-filter='checkBox' name='selectBox' class='selBox' "+tp+" disabled='disabled' type='checkbox' onchange='checkClick(this)'></div></div>";
				},
				width:30
		};
		var col3 = {
				title:"辅",
				field:"FIELD_"+colObj.FIELD+"_0",
				sortable :false, 
				class:""+colObj.FIELD+"_0",
				formatter : function(value, row, index) {
					var tp="";
					if(value!=null&&value!=""){
						tp="checked";
					}
					return "<div class='layui-form-item'><div class='layui-inline'><input lay-skin='primary' lay-filter='checkBox' name='selectBox' class='selBox' "+tp+" disabled='disabled' type='checkbox' onchange='checkClick(this)'></div></div>";
				},
				width:30
		};
		createRow2.push(col2);
		createRow2.push(col3);
		detailColumns.push("FIELD_"+colObj.FIELD+"_1");
		detailColumns.push("FIELD_"+colObj.FIELD+"_0");
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
				sortable :false,
				editable : {
					disabled : true,
					type : "select2",
					title : "机位",
					onblur : "cancel",
					source: function () {
						var result = [];
	                    var uniqueid = $(this).parents("tr").data("uniqueid");
						var data = table.bootstrapTable('getRowByUniqueId', uniqueid);
						var aptiId = data["PARENT_APTITUDE_ID"];
						var id;
						if(aptiId!=null){
							var apti = aptiLimt[aptiId];
							if(apti!=undefined){
								var apt = apti["lm0"];
								if(apt!=undefined){
									id=apt;
								}
							}
						}
						var list = jwLimit[id];
						if(list==undefined){
							result = [];
						}else{
							result = list;
						}
	                    return result;
	                },
					select2 : {
						matcher : customMatcher
					}
				},
				colspan: 1,
				rowspan: 2,
				width:150
			};
			createRow1.push(col);
			detailColumns.push("JW_LIMIT");
		}
		
		if(limit2){
			var col = {
				title:"机型",
				field:"JX_LIMIT",
				sortable :false,
				editable : {
					disabled : true,
					type : "select2",
					title : "机型",
					onblur : "cancel",
					source: function () {
						var result = [];
	                    var uniqueid = $(this).parents("tr").data("uniqueid");
						var data = table.bootstrapTable('getRowByUniqueId', uniqueid);
						var aptiId = data["PARENT_APTITUDE_ID"];
						var id;
						if(aptiId!=null){
							var apti = aptiLimt[aptiId];
							if(apti!=undefined){
								var apt = apti["lm1"];
								if(apt!=undefined){
									id=apt;
								}
							}
						}
						var list = jxLimit[id];
						if(list==undefined){
							result = [];
						}else{
							result = list;
						}
	                    return result;
	                },
					select2 : {
						matcher : customMatcher
					}
				},
				colspan: 1,
				rowspan: 2,
				width:150
			};
			createRow1.push(col);
			detailColumns.push("JX_LIMIT");
		}
		
		if(limit3){
			var col = {
					title:"航空公司",
					field:"HS_LIMIT",
					sortable :false,
					editable : {
						disabled : true,
						type : "select2",
						title : "航空公司",
						onblur : "cancel",
						source: function () {
							var result = [];
		                    var uniqueid = $(this).parents("tr").data("uniqueid");
							var data = table.bootstrapTable('getRowByUniqueId', uniqueid);
							var aptiId = data["PARENT_APTITUDE_ID"];
							var id;
							if(aptiId!=null){
								var apti = aptiLimt[aptiId];
								if(apti!=undefined){
									var apt = apti["lm2"];
									if(apt!=undefined){
										id=apt;
									}
								}
							}
							var list = hsLimit[id];
							if(list==undefined){
								result = [];
							}else{
								result = list;
							}
		                    return result;
		                },
						select2 : {
							matcher : customMatcher
						}
					},
					colspan: 1,
					rowspan: 2,
					width:150
				};
				createRow1.push(col);
				detailColumns.push("HS_LIMIT");
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
		},
		colspan: 1,
		rowspan: 2,
		width:50
	};
	createRow1.push(col);
	createDetailColumns.push(createRow1);
	createDetailColumns.push(createRow2);
	//alert(JSON.stringify(createDetailColumns))
	initTableDate();
}

function initTableDate() {
	var createDetailOption = {
			method : 'get',
			striped : true, 
			uniqueId : "RM",
		    queryParams: function (params) {
		    	var param = {  
                    searchName : $("#searchName").val(),
                    templateId:templateId,
                    group : group
                }; 
		    	if($('#operator').val()){
		    		param.operator = $('#operator').val();
		    	}
	            return param;     
		    },
		    /*search : true,
		    searchOnEnterKey : true,*/
			url : ctx + "/division/info/getGridData",
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
				form.render('checkbox');
			},
			onPostBody : function(){
				if(ifModify){
					$('.selBox').removeAttr("disabled");
					$('.editable').editable('enable');
				}
				form.render('checkbox');
			},
			onEditableHidden : function(field, row, $el, reason) {
				$el.parents("body").find(".select2-container--open").remove();
			}
		};
	createDetailOption.height = $(window).height()*0.95;
	table.bootstrapTable(createDetailOption);
}

function initCheckbox(){
	var data = table.bootstrapTable("getData");
	for(var i=0;i<data.length;i++){
		var tp = data[i].APID;
		var rm = data[i].RM;
		var tr = $("tr[data-uniqueid="+rm+"]");
		if(tp!=null){
			var apids = tp.split(",");
			for(var k=0;k<apids.length;k++){
				if(apids[k]!=null){
					tr.find("."+apids[k]+"_0 .selBox").removeAttr("disabled");
					tr.find("."+apids[k]+"_1 .selBox").removeAttr("disabled");
				}
			}
			tr.find('.editable').editable('toggleDisabled');
		}
	}
	form.render('checkbox');
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
		table.find('.addSpan').show();
		initCheckbox();
		ifModify=true;
	}
}

function addEmptyRow(i) {
	var uniqueid = $(i).parents("tr").data("uniqueid");
	var data = table.bootstrapTable('getRowByUniqueId', uniqueid);
	layer.open({
		type:1,
		title: "增加分工",
		offset: '5px',
		resize:false,
		area:["90%","90%"],
		content:$("#contentDiv"),
		btn:["保存","关闭"],
		success:function(layero,index){
			aptitable.bootstrapTable('destroy');
			initAptiduteTable(data["LONGIN_ID"]);
		},
		yes:function(index, layero){
			$("input[name='optBox']:checkbox:checked").each(function(){ 
				var uniqueid = $(this).parents("tr").data("uniqueid");
				var uniqueid2 = $(i).parents("tr").data("uniqueid");
				var aptiData = aptitable.bootstrapTable('getRowByUniqueId', uniqueid);
				var data = table.bootstrapTable('getRowByUniqueId', uniqueid2);
				var index = $(i).parents("tr").data("index");
				var row = {};
				for (var j = 0; j < detailColumns.length; j++) {
					var field = detailColumns[j];
					if(field==undefined){
						return;
					}
					if(aptiData[field]==undefined){
						if(field=="PARENT_APTITUDE_ID"){
							row[field] = aptiData["ID"];
						}else{
							row[field] = "";
						}
					}else{
						if(field=="GROUP_ID"||field=="GROUP_NAME"||field=="LONGIN_ID"||field=="LOGIN_NAME"){
							row[field] = data[field];
						}else if(field=="JW_LIMIT"||field=="JX_LIMIT"||field=="HS_LIMIT"){
							if(aptiData[field]!=null){
								row[field] = aptiData[field].split(",")[0];
							}else{
								row[field] = "";
							}
						}else if(field.indexOf("FIELD_")>-1){
							row[field] = aptiData[field]
						}else{
							row[field] = "";
						}
					}
				}
				row["RM"]=len+1;
				len=len+1;
				table.bootstrapTable('insertRow', {index:index+1,row:row});
			});
			doClickSub();
			layer.close(index);
		}
	});
}

function doClickSub(){
	table.find(":checkbox").attr("disabled","disabled");
	var addTrs = table.find("tbody>tr");
	addTrs.each(function(){
		var idstr = $(this).data("uniqueid");
		if(idstr>oldLen){
			$(this).find('.delSpan').show();
			$(this).find('.addSpan').hide();
		}else{
			$(this).find('.addSpan').show();
		}
	});
	changeBox();
}

function changeBox(){
	var objs = table.find('.delSpan');
	objs.each(function(){
		if($(this).css('display')!="none"){
			var tr = $(this).parents("tr");
			var boxs = tr.find("input:checked");
			boxs.each(function(){
				var td = $(this).parents("td");
				var t = td.attr("class").split("_")[0];
				tr.find("."+t+"_0 .selBox").removeAttr("disabled");
				tr.find("."+t+"_1 .selBox").removeAttr("disabled");
			});
			tr.find('.editable').editable('toggleDisabled');
		}
	});
	initCheckbox();
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
	var c1 = cls.split("_")[0];
	var t1 = c1+"_1",t2=c1+"_0";
	if($(obj).is(':checked')){
		data["FIELD_"+cls] = c1;
		if(cls==t1){
			$(obj).parents("tr").find("."+t2).find(".selBox").attr("checked",false);
			data["FIELD_"+t2] = "";
		}
		if(cls==t2){
			$(obj).parents("tr").find("."+t1).find(".selBox").attr("checked",false);
			data["FIELD_"+t1] = "";
		}
	}else{
		data["FIELD_"+cls] = "";
	}
	form.render('checkbox');
}
	
function doSave(){
	var data = table.bootstrapTable("getData");
	if(ifModify&&data.length>0){
		var layerIndex = layer.load(2);
		$.ajax({
			type : 'post',
			url : ctx + "/division/info/saveInfo",
			data : {
				data : JSON.stringify(data),
				templateConf: $('#templateConf').val() ,
	    		operator : $('#operator').val()
			},
			success : function(msg) {
				layer.close(layerIndex);
				if (msg == "success") {
					layer.msg('保存成功！', {
						icon : 1,
						time : 600
					});
					$('select').prop('selectedIndex', 0);
					form.render('select');
					changeTemplate("all");
					
				}
			}
		});
	}
}

var createAptiColumns=[]
function initAptiduteTable(workId){
	createAptiColumns=[];
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/division/info/getTableHeader",
		data : null,
		dataType : "json",
		success : function(result) {
			getAptiColumns(result,workId);
		}
	});
}

function getAptiColumns(heaerPart,workId){
	createAptiColumns = [
		{
			title : "序号",
			formatter : function(value, row, index) {
				return index + 1;
			}
		},
		{
			title:"班组",
			field:"GROUP_NAME",
			sortable :false
		} ,
		{
			title:"员工",
			field:"LOGIN_NAME",
			sortable :false
		} 
	];
	var colsArr = heaerPart.rstypeArr;
	for(var i in colsArr){
		var colObj = colsArr[i];
		var col = {
				title:colObj.TITLE,
				field:"FIELD_"+colObj.FIELD+"_1",
				sortable :false,
				formatter : function(value, row, index) {
					var tp="";
					if(value!=null&&value!=""){
						tp="checked";
					}
					return "<input name='aptiBox' "+tp+" disabled='disabled' type='checkbox'>";
				}
		};
		createAptiColumns.push($.extend({},true,col));
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
				sortable :false,
				formatter : function(value, row, index) {
					var tp="";
					if(value!=null&&value!=""){
						tp=value.split(",");
					}
					return tp[1];
				}
			};
			createAptiColumns.push(col);
		}
		
		if(limit2){
			var col = {
				title:"机型",
				field:"JX_LIMIT",
				sortable :false,
				formatter : function(value, row, index) {
					var tp="";
					if(value!=null&&value!=""){
						tp=value.split(",");
					}
					return tp[1];
				}
			};
			createAptiColumns.push(col);
		}
		
		if(limit3){
			var col = {
				title:"航空公司",
				field:"HS_LIMIT",
				sortable :false,
				formatter : function(value, row, index) {
					var tp="";
					if(value!=null&&value!=""){
						tp=value.split(",");
					}
					return tp[1];
				}
			};
			createAptiColumns.push(col);
		}
	}
	var col = {
		field : "ID",
		title : "操作",
		sortable : false,
		editable : false,
		formatter : function(value, row, index) {
			return "<input name='optBox' type='checkbox'>";
		}
	};
	createAptiColumns.push(col);
	initAptiTableData(workId);
}
var clickAptiFeild="";
function initAptiTableData(workId) {
	var createDetailOption = {
			method : 'get',
			striped : true, 
			uniqueId : "ID",
			height:'320',
		    queryParams: function (params) {
		    	var param = {  
		    			workId : workId
                }; 
	            return param;     
		    },
			url : ctx + "/division/info/getAptiduteGridData",
			columns : createAptiColumns,
			onClickCell:function onClickCell(field,value,row,td ){
				clickAptiFeild=field;
			}
		};
	aptitable.bootstrapTable(createDetailOption);
}

function doTemplate(){
	var tempIFrame = layer.open({
		type : 2,
		offset: '5px',
		area:["95%","95%"],
		title : '人员分工版本维护',
		content : ctx + "/division/info/showMaintainPage"
	});
}