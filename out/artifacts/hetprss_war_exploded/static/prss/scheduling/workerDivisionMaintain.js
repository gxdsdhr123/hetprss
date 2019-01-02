var layer,form;
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
});
var clickId = "";
var clickFeild="";
var ifNew=true;
var createDetailColumns=[];
var detailColumns=["RM","ID","GROUP_ID","LONGIN_ID","GROUP_NAME","LOGIN_NAME","PARENT_APTITUDE_ID"];
var oldLen,len;
var table = $('#createDetailTable');
var aptitable = $('#aptiduteTable');
var jwLimit,jxLimit,hsLimit,aptiLimt;//机位,机型,航空公司
var templateId = "";
var group = "";
var searchData = [];//保存查询过的数据
var searchFlag = false;//是否第二次查询
var searchConut = 0;//查询次数
$(function(){
	$("#templateConf").val("new");
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	initTable();
	//查询
	$("#searchBtn").click(function(){
		
		
		searchConut = searchConut + 1;
		
		if(searchConut > 1){
			searchFlag = true;
			var dataS = table.bootstrapTable("getData");
			if(dataS.length>0){
				searchData =searchData.concat(dataS);
			}
		}
		table.bootstrapTable('destroy');
		initTable();
	});
	//保存
	$("#saveBtn").click(function(){
		doSave(searchFlag);
		
		searchData = [];
		searchFlag = false;
		searchConut = 0;
		
	});
	
	//删除
	$("#deleteBtn").click(function(){
		doDeleteTemp();
	});
	
	//修改模板名称
	$("#nameBtn").click(function(){
		doChangeName();
	});
});

function changeTemplate(id){
	if(id=='new'){
		ifNew=true;
		id="";
	}else{
		ifNew=false;
	}
	table.bootstrapTable('destroy');
	templateId = id;
	initTable();
}
function changeGroup(name){
	table.bootstrapTable('destroy');
	group = window.encodeURI(name); 
	initTable();
}

function initTable(){
	clickId = "";
	clickFeild="";
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
			jxLimit = result.limit1;
			hsLimit = result.limit2;
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
			field : "APID",
			title : "APID",
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
					return "<input name='selectBox' class='selBox' "+tp+" disabled type='checkbox' onchange='checkClick(this)'>";
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
					return "<input name='selectBox' class='selBox' "+tp+" disabled type='checkbox' onchange='checkClick(this)'>";
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
					disabled : false,
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
					disabled : false,
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
					disabled : false,
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
	initTableDate();
}



function initTableDate() {
	var searchName = $("#searchName").val();
	var encodeSearchName = window.encodeURI(searchName);
	var createDetailOption = {
			method : 'get',
			dataType : 'json',
			striped : true, 
			uniqueId : "RM",
		    queryParams: function (params) {
		    	var param = {  
                    searchName : encodeSearchName,
                    templateId:templateId,
                    group : group
                }; 
	            return param;     
		    },
			url : ctx + "/division/info/getTempGridData",
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
				//oldLen = table.bootstrapTable("getOptions").totalRows;
				oldLen = table.bootstrapTable("getData").length;
				len=oldLen;
				table.find('.addSpan').show();
				initCheckbox();
				mergeTable("GROUP_NAME")//合并单元格
			},
			onEditableHidden : function(field, row, $el, reason) {
				$el.parents("body").find(".select2-container--open").remove();
			}
		};
	createDetailOption.height = $(window).height()*0.95;
	table.bootstrapTable(createDetailOption);
}
function mergeTable(field){
	var obj=getObjFromTable(table,field);
	 for(var item in obj){  
	    table.bootstrapTable('mergeCells',{
	        index:obj[item].index,
	        field:field,
	        colspan:1,
	        rowspan:obj[item].row
	    });
	  }
}
 
function getObjFromTable($table,field){
    var obj=[];
    var maxV=$table.find("th").length;
 
    var columnIndex=0;
    var filedVar;
    for(columnIndex=0;columnIndex<maxV;columnIndex++){
        filedVar=$table.find("th").eq(columnIndex).attr("data-field");
        if(filedVar==field) break;
 
    }
    var $trs=$table.find("tbody>tr");
    var $tr;
    var index=0;
    var content="";
    var row=1;
    for (var i = 0; i <$trs.length;i++) {   
        $tr=$trs.eq(i);
        var contentItem=$tr.find("td").eq(columnIndex).html();
        //exist
        if(contentItem&&contentItem.length>0 && content==contentItem ){
            row++;
        }else{
            //save
            if(row>1){
                obj.push({"index":index,"row":row});
            }
            index=i;
            content=contentItem;
            row=1;
        }
    }
    if(row>1)obj.push({"index":index,"row":row});
    return obj;
     
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
		}
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

function addEmptyRow(i) {
	var uniqueid = $(i).parents("tr").data("uniqueid");
	var data = table.bootstrapTable('getRowByUniqueId', uniqueid);
	layer.open({
		type:1,
		title: "增加分工",
		offset: '5px',
		area:["95%","95%"],
		resize:false,
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
							row[field] = aptiData[field];
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
				var td = $(this).parent();
				var t = td.attr("class").split("_")[0];
				tr.find("."+t+"_0 .selBox").removeAttr("disabled");
				tr.find("."+t+"_1 .selBox").removeAttr("disabled");
			});
			//tr.find('.editable').editable('toggleDisabled');
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
	var cls = $(obj).parent().attr("class");
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
}
var tempName="";
function doSave(){
	if(templateId==""||templateId=="new"){
		var objs = table.find('.delSpan:visible');
		if(ifNew&&objs.length==0){
			return false;
		}
		layer.prompt({title: '模板名称'},
			function(val, index){
			if(val==""){
				layer.msg('请输入模板名称！', {
					icon : 1,
					time : 600
				});
				return false;
			}
			tempName=val;
			addSave(searchFlag);
			layer.close(index);
		});
	}else{
		addSave(searchFlag);
	}
}

function addSave(searchFlagParam){
	var data = [];
	
	if(searchFlagParam){
		data = table.bootstrapTable("getData");
		data = data.concat(searchData)
	}else{
		data = table.bootstrapTable("getData");
	}
	
	if(data.length>0){
		var LIndex = layer.load(2);
		// 判断是否是新建模板
		var addTemp = '';
		if($('#templateConf').val() == 'new'){
			addTemp = 1;
		}
		$.ajax({
			type : 'post',
			url : ctx + "/division/info/saveInfo",
			data : {
				data : JSON.stringify(data),
				tempName:tempName,
				tempId:templateId,
				templateConf: $("#templateConf").val(),
				addTemp:addTemp
			},
			success : function(msg) {
				if(msg!="error"){
					layer.close(LIndex);
					layer.msg('保存成功！', {
						icon : 1,
						time : 600
					});
					templateId = msg;
					$.ajax({
						type : 'post',
						async : false,
						url : ctx + "/division/info/getTempList",
						data : null,
						dataType : "json",
						success : function(result) {
							var html = '<option value="new">空模板</option>';
		                    for (var i=0 in result){
		                        var d = result[i];
		                        var sel="";
		                        if(templateId==d.ID){
		                        	sel="selected";
		                        }
		                        html+= '<option value="'+d.ID+'" '+sel+'>'+d.NAME+'</option>';
		                    }
		                    $('#templateConf').html(html);
		                    form.render('select');
		                    changeTemplate(templateId);
						}
					});
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
				field:"FIELD_"+colObj.FIELD+"_0",
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
			height:'290',
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

function doDeleteTemp(){
	layer.confirm("是否删除选中的模板？",{offset:'100px'},function(index){
		deleteTemp();
		layer.close(index);
	});
}

function deleteTemp(){
	if(templateId==""){
		layer.msg('请选择要删除的模板！', {
			icon : 2
		});
		return false;
	}
	$.ajax({
		type : 'post',
		async : false,
		url : ctx + "/division/info/deleteTemp",
		data : {
			tempId:templateId
		},
		success : function(result) {
			if (result == "success") {
				layer.msg('删除成功！', {
					icon : 1
				});
				$.ajax({
					type : 'post',
					async : false,
					url : ctx + "/division/info/getTempList",
					data : null,
					dataType : "json",
					success : function(result) {
						var html = '<option value="new" selected>空模板</option>';
	                    for (var i=0 in result){
	                        var d = result[i];
	                        html+= '<option value="'+d.ID+'">'+d.NAME+'</option>';
	                    }
	                    $('#templateConf').html(html);
	                    form.render('select');
	                    changeTemplate("new");
	                    templateId="";
					}
				});
			}else{
				layer.msg('删除失败！', {
					icon : 2
				});
			}
		}
	});
}

function doChangeName(){
	if($('#templateConf').val() == 'new'){
		layer.msg('请选择已有的模板！', {
			icon : 2
		});
		return;
	}
	var LIndex = layer.load(2);
	layer.prompt({title: '模板名称',value: $('#templateConf option:selected').text()},
		function(val, index){
		if(val==""){
			layer.msg('请输入模板名称！', {
				icon : 1,
				time : 600
			});
			return false;
		}
		tempName=val;
		
		// 修改名称
		$.ajax({
			type : 'post',
			url : ctx + "/division/info/changeName",
			data : {
				tempName:tempName,
				tempId:templateId,
				templateConf: $("#templateConf").val()
			},
			success : function(msg) {
				if(msg!="error"){
					layer.close(LIndex);
					layer.msg('保存成功！', {
						icon : 1,
						time : 600
					});
					templateId = msg;
					$.ajax({
						type : 'post',
						async : false,
						url : ctx + "/division/info/getTempList",
						data : null,
						dataType : "json",
						success : function(result) {
							var html = '<option value="new">空模板</option>';
		                    for (var i=0 in result){
		                        var d = result[i];
		                        var sel="";
		                        if(templateId==d.ID){
		                        	sel="selected";
		                        }
		                        html+= '<option value="'+d.ID+'" '+sel+'>'+d.NAME+'</option>';
		                    }
		                    $('#templateConf').html(html);
		                    form.render('select');
		                    changeTemplate(templateId);
						}
					});
				}
			}
		});
		layer.close(index);
	});
}
