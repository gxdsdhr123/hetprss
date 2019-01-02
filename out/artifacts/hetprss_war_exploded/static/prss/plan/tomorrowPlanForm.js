layui.use(["form","layer"]);
var rowIndex = 0;
$(document).ready(function(){
	initGrid();
	//添加机号
	$("#aircraftBtn").click(function(){
		doAddMaintain(5,"parent");
	});
	//添加机型
	$("#actTypeBtn").click(function(){
		doAddMaintain(2,"parent");
	});
	//添加机场
	$("#airportBtn").click(function(){
		doAddMaintain(1,"parent");
	});
	//添加航空公司
	$("#alnBtn").click(function(){
		doAddMaintain(3,"parent");
	});
});

function initGrid() {
	var isNew = $("#isNew").val();
	var fltNo = $("#fltNo").val();
	var ioType = $("#ioType").val();
	var ids = $("#ids").val();
	$("#baseTable").bootstrapTable({
		url : ctx+"/tomorrow/plan/formGridData", 
		queryParams : function(params) {
			var param = {
				fltDate:$("#fltDate").val(),
				fltNo:fltNo,
				isNew:isNew,
				ioType:ioType,
				ids:ids
			};
			return param;
		},
		onLoadSuccess:function(data){
			if(isNew=="true"){
				addRow("","","");
				addRow("","","");
			}
		},
		onClickCell : function(field, value, row, $element) {
			//航班日期
			if (field == "fltDate") {
				popupDate(field, value, row, $element,'yyyyMMdd');
			}
		},
		onEditableSave : function(field, row, oldValue, $el) {
			var dataIndex = $el.parent().parent().index();
			var value = row[field];
			var tr = $el.parents("tr");
			if(field=="fltNo"){
				//航班号、航空公司
				var val = value.substring(0, 3).toUpperCase();
				updateCell(dataIndex,val,"alnCode",tr);
				updateCell(dataIndex,value.toUpperCase(),"fltNo",tr);
				//共享航班号
				$.post(ctx + "/tomorrow/plan/getShareFltNo", {
					fltNo : value
				}, function(shareFltNo) {
					updateCell(dataIndex,shareFltNo,"shareFltNo",tr);
				})
			}else if(field=="alnCode"){
				var val = value.toUpperCase() + row["fltNo"].substring(3, row["fltNo"].length);
				updateCell(dataIndex,val,"fltNo",tr);
			}else if ("aircraftNumber" == field) {
				updateCell(dataIndex,value.toUpperCase(),"aircraftNumber",tr);
				// 机号机型联动
				$.post(ctx + "/tomorrow/plan/getActType", {
					aftNum : value
				}, function(act) {
					if(act!='undefined'&&act!=null&&act!=''){
						updateCell(dataIndex,act.actype_code,"actType",tr);
						if(act.airline_code != row.alnCode){
							var actAirlineCode = '';
							if(act.airline_code!=undefined)
								actAirlineCode = act.airline_code;
							layer.msg("机号对应航空公司("+actAirlineCode+")与航班号对应航空公司不一致！",{time:3000,icon:0})
						}
					}else{
						layer.msg("没有匹配到填写机号相关信息！",{time:3000,icon:0})
					}
				})
			}else if(field=="std"){
//				updateCell(dataIndex,value,"etd",tr);
			}else if(field=="sta"){
				updateCell(dataIndex,value,"eta",tr);
			}else if(field=="departApt"||field=="arrivalApt"){
				// 根据起落场设置航班属性
				var departApt = row["departApt"];
				var arrivalApt = row["arrivalApt"];
				var departAptDOrI = aptAttrCode[departApt];
				var arrivalAptDOrI = aptAttrCode[arrivalApt];
				var attrCode = "";
				if(departAptDOrI=="I"||arrivalAptDOrI=="I"){
					attrCode = "I";
				}else{
					attrCode = "D";
				}
				updateCell(dataIndex,attrCode,"attrCode",tr);
			}else if(field=="etd"){
				//根据预起获取计算的预落
				var departApt = row["departApt"];
				var arrivalApt = row["arrivalApt"];
				var etd = row["etd"];
				var actType = row["actType"];
				var fltDate = row["fltDate"];
				
				var aptCode = "";
				var ioType = $("#ioType").val();
				if(ioType!=''&&ioType!='undefined'&&ioType!=null){
					//双击更新
					aptCode = ioType=='A'?departApt:arrivalApt;
				}else{
					//点击新增
					if(departApt==currentAirport&&arrivalApt!=currentAirport){
						aptCode = arrivalApt;
					}else if(departApt!=currentAirport&&arrivalApt==currentAirport){
						aptCode = departApt;
					}
				}
				
				if(departApt!=currentAirport&&arrivalApt!=currentAirport){
					//联程计划不计算预落
					aptCode = '';
				}
				
				if(etd!=''&&aptCode!=''&&fltDate!=''){
					$.ajax({
				         type : "post",  
				          url : ctx + "/tomorrow/plan/getEta",  
				          data : {
								actType : actType,
								aptCode : aptCode,
								fltDate : fltDate,
								etd : value
						  },  
				          async : false,  
				          success : function(eta){
				        	  if(eta!='undefined'&&eta!=null&&eta!=''){
									updateCell(dataIndex,eta,"eta",tr);
									//修正成功
									row.etaCalculateFlag = 2;
								}else{
									//修正失败
									row.etaCalculateFlag = 1;
									layer.msg("eta计算无结果！",{time:3000,icon:0})
								}  
				          }  
				     }); 
				}else{
					//不修正
					row.etaCalculateFlag = 0;
				}
			}
			if (!isNewRow(row.id)) {
				editRows[row.id] = $.extend(true,{},row);
			}
		},
		onEditableShown: function(field, row, $el, editable) {
			setTimeout(function(){
				$el.next().find(".select2-hidden-accessible").select2('open');
			},100);
        },
		onEditableHidden: function(field, row, $el, reason) {
			$el.next().find(".select2-hidden-accessible").select2('close');
			if(field == "aircraftNumber"){
				if ($el.parent().next().next() && $el.parent().next().next().is("td")) {
					var edit = $el.parent().next().next().find('.editable');
					if(edit.text() == "" || edit.text() == null){
						edit.editable('show');
					}
				}
			}else{//下一个为空的项弹出
				show($el);
				function show(ele){
					if (ele.parent().next() && ele.parent().next().is("td")) {
						var edit = ele.parent().next().find('.editable');
						if(edit.text() == "" || edit.text() == null){
							edit.editable('show');
						}else{
							show(edit);
						}
					}
				}
			}
        },
		method : "get", // 请求方式（*）
		dataType : "json",
		uniqueId:"id",
		search : false,
		pagination : false,
//		height:$("#baseTables").height(),
		height:$(window).height(),
		columns : isNew=="true"?getColumnsNew():getColumnsUpdate()
	});
}
function updateCell(dataIndex,value,field,tr){
	var dataList = $("#baseTable").bootstrapTable("getData");
	dataList[dataIndex][field] = value;
	var columns = isNew=="true"?getColumnsNew():getColumnsUpdate();
	var index = 0;
	for(var i=0;i<columns.length;i++){
		if(columns[i].field == field){
			index = i;
			break;
		}
	}
	tr.find("td").eq(index).find(".editable").editable('setValue',value,true);
}
/**
 * 表头信息
 */
function getColumnsNew() {
	var columns = [ {
		checkbox : true, // 显示一个勾选框
		align : 'center',
	}, {
		field : "order",
		title : "序号",
		width : '40px',
		align : 'center',
		formatter : function(value, row, index) {
			return index + 1;
		}
	}, {
		field : "fltNo",
		title : "航班号",
		width : "80px",
		editable : {
			type:'text',
			display : function(value){
				$(this).html(value.toUpperCase());
			},
			validate: function (value) {  
                if ($.trim(value) == '') {
                    return '航班号不能为空!';  
                }  
            }
		}
	},{
		field : "shareFltNo",
		title : "共享航班号",
		width : "80px",
		editable : {
			type:'text',
			display : function(value){
				$(this).html(value.toUpperCase());
			}
		}
	}, {
		field : "aircraftNumber",
		title : "机号",
		width : "80px",
		editable : {
			type:'text',
			display : function(value){
				$(this).html(value.toUpperCase());
			}
		}
	}, {
		field : "actType",
		title : "机型",
		width : "80px",
		editable : {
			type : "select2",
			source : actTypeSource,
			select2 : {
				placeholder : "请选择机型",
				width : "80px"
			},
			validate: function (value) {
                if ($.trim(value) == '') {
                    return '机型不能为空!';  
                }
            }
		}
	}, {
		field : "departApt",
		title : "起场",
		width : "80px",
		editable : {
			type : "select2",
			onblur : "ignore",
			source : airportCodeSource,
			select2 : {
				placeholder : "请选择起场",
				width : "80px",
				matcher: aptMatcher
			},
			validate: function (value) {
                if ($.trim(value) == '') {
                    return '起场不能为空!';  
                }  
            }
		}
	}, {
		field : "arrivalApt",
		title : "落场",
		width : "80px",
		editable : {
			type : "select2",
			onblur : "ignore",
			source : airportCodeSource,
			select2 : {
				placeholder : "请选择落场",
				width : "80px",
				matcher: aptMatcher
			},
			validate: function (value) {
                if ($.trim(value) == '') {
                    return '落场不能为空!';  
                }  
            }
		}
	}, {
		field : "std",
		title : "计起",
		width : "80px",
		editable : {
			type:'text',
			validate: function (value) {
                if ($.trim(value) == '') {
                    return '计起不能为空!';
                }
                if(!checkStaStdFormat(value)){
					return '计起时间格式不正确!格式(24小时制)：小时分钟+';
				}
            }
		}
	}, {
		field : "sta",
		title : "计落",
		width : "80px",
		editable : {
			type:'text',
			validate: function (value) {
                if ($.trim(value) == '') {
                    return '计落不能为空!';  
                }
                if(!checkStaStdFormat(value)){
					return '计落时间格式不正确!格式(24小时制)：小时分钟+';
				}
            }
		}
	}, {
		field : "etd",
		title : "预起",
		width : "80px",
		editable : {
			type:'text',
			validate: function (value) {
                if ($.trim(value) == '') {
                    return '预起不能为空!';
                }
                if(!checkStaStdFormat(value)){
					return '预起时间格式不正确!格式(24小时制)：小时分钟+';
				}
            }
		}
	}, {
		field : "eta",
		title : "预落",
		width : "80px",
		editable : {
			type:'text',
			validate: function (value) {
                if ($.trim(value) == '') {
                    return '预落不能为空!';  
                }
                if(!checkStaStdFormat(value)){
					return '预落时间格式不正确!格式(24小时制)：小时分钟+';
				}
            }
		}
	}, {
		field : "propertyCode",
		title : "性质",
		width : "80px",
		editable : {
			type : "select2",
			source : propertyCodeSource,
			select2 : {
				placeholder : "请选择性质",
				width : "80px"
			},
			validate: function (value) {
                if ($.trim(value) == '') {
                    return '性质不能为空!';  
                }
            }
		}
	}, {
		field : "attrCode",
		title : "属性",
		width : "80px",
		editable : {
			type : "select2",
			source : [{id:"I",text:"国际"},{id:"D",text:"国内"},{id:"M",text:"混合"}],
			select2 : {
				placeholder : "请选择属性",
				width : "80px"
			},
			validate: function (value) {
                if ($.trim(value) == '') {
                    return '属性不能为空!';  
                }
            }
		}
	}, {
		field : "alnCode",
		title : "航空公司",
		width : "80px",
		editable : {
			type : "select2",
			onblur : "ignore",
			source : airlinesCodeSource,
			select2 : {
				placeholder : "请选择航空公司",
				width : "80px",
				matcher: alnLineMatcher
			},
			validate: function (value) {
                if ($.trim(value) == '') {
                    return '航空公司不能为空!';  
                }
            }
		}
	},{
		field : "fltDate",
		title : "日期",
		width : "60px",
		formatter : function(value, row, index) {
			return "<a href='javascript:void(0)' id='fltDate" + index + "'>" + (value ? value : '请选择') + "</a>";
		}
	} ];
	return columns;
}

/**
 * 表头信息
 */
function getColumnsUpdate() {
	var columns = [ {
		checkbox : true, // 显示一个勾选框
		align : 'center',
	}, {
		field : "order",
		title : "序号",
		width : '40px',
		align : 'center',
		formatter : function(value, row, index) {
			return index + 1;
		}
	}, {
		field : "fltNo",
		title : "航班号",
		width : "80px",
		editable : {
			type:'text',
			display : function(value){
				$(this).html(value.toUpperCase());
			},
			validate: function (value) {
              if ($.trim(value) == '') {
                  return '航班号不能为空!';  
              }
          }
		}
	},{
		field : "shareFltNo",
		title : "共享航班号",
		width : "80px",
		editable : {
			type:'text',
			display : function(value){
				$(this).html(value.toUpperCase());
			}
		}
	}, {
		field : "aircraftNumber",
		title : "机号",
		width : "80px",
		editable : {
			type:'text',
			display : function(value){
				$(this).html(value.toUpperCase());
			}
		}
	}, {
		field : "actType",
		title : "机型",
		width : "80px",
		editable : {
			type : "select2",
			source : actTypeSource,
			select2 : {
				placeholder : "请选择机型",
				width : "80px"
			},
			validate: function (value) {
                if ($.trim(value) == '') {
                    return '机型不能为空!';  
                }
            }
		}
	}, {
		field : "departApt",
		title : "起场",
		width : "80px",
		editable : {
			type : "select2",
			onblur : "ignore",
			source : airportCodeSource,
			select2 : {
				placeholder : "请选择起场",
				width : "80px",
				matcher: aptMatcher
			},
			validate: function (value) {
                if ($.trim(value) == '') {
                    return '起场不能为空!';  
                }  
            }
		}
	}, {
		field : "arrivalApt",
		title : "落场",
		width : "80px",
		editable : {
			type : "select2",
			onblur : "ignore",
			source : airportCodeSource,
			select2 : {
				placeholder : "请选择落场",
				width : "80px",
				matcher: aptMatcher
			},
			validate: function (value) {
                if ($.trim(value) == '') {
                    return '落场不能为空!';  
                }  
            }
		}
	}, {
		field : "std",
		title : "计起",
		width : "80px",
		editable : {
			type:'text',
			validate: function (value) {
                if ($.trim(value) == '') {
                    return '计起不能为空!';
                }
                if(!checkStaStdFormat(value)){
					return '计起时间格式不正确!格式(24小时制)：小时分钟+';
				}
            }
		}
	}, {
		field : "sta",
		title : "计落",
		width : "80px",
		editable : {
			type:'text',
			validate: function (value) {
                if ($.trim(value) == '') {
                    return '计落不能为空!';  
                }
                if(!checkStaStdFormat(value)){
					return '计落时间格式不正确!格式(24小时制)：小时分钟+';
				}
            }
		}
	}, {
		field : "etd",
		title : "预起",
		width : "80px",
		editable : {
			type:'text',
			validate: function (value) {
                if ($.trim(value) == '') {
                    return '预起不能为空!';
                }
                if(!checkStaStdFormat(value)){
					return '预起时间格式不正确!格式(24小时制)：小时分钟+';
				}
            }
		}
	}, {
		field : "eta",
		title : "预落",
		width : "80px",
		editable : {
			type:'text',
			validate: function (value) {
                if ($.trim(value) == '') {
                    return '预落不能为空!';  
                }
                if(!checkStaStdFormat(value)){
					return '预落时间格式不正确!格式(24小时制)：小时分钟+';
				}
            }
		}
	}, {
		field : "propertyCode",
		title : "性质",
		width : "80px",
		editable : {
			type : "select2",
			source : propertyCodeSource,
			select2 : {
				placeholder : "请选择性质",
				width : "80px"
			},
			validate: function (value) {
                if ($.trim(value) == '') {
                    return '性质不能为空!';  
                }
            }
		}
	}, {
		field : "attrCode",
		title : "属性",
		width : "80px",
		editable : {
			type : "select2",
			source : [{id:"I",text:"国际"},{id:"D",text:"国内"},{id:"M",text:"混合"}],
			select2 : {
				placeholder : "请选择属性",
				width : "80px"
			},
			validate: function (value) {
                if ($.trim(value) == '') {
                    return '属性不能为空!';  
                }
            }
		}
	}, 
//	{
//		field : "alnCnName",
//		title : "航空公司",
//		width : "80px",
//		editable : false
//	},
	{
		field : "alnCode",
		title : "航空公司",
		width : "80px",
		editable : {
			type : "select2",
			onblur : "ignore",
			source : airlinesCodeSource,
			select2 : {
				placeholder : "请选择航空公司",
				width : "80px",
				matcher: alnLineMatcher
			},
			validate: function (value) {
                if ($.trim(value) == '') {
                    return '航空公司不能为空!';  
                }
            }
		}
	},{
		field : "fltDate",
		title : "日期",
		width : "60px",
		formatter : function(value, row, index) {
			return "<a href='javascript:void(0)' id='fltDate" + index + "'>" + (value ? value : '请选择') + "</a>";
		}
	} ];
	return columns;
}

/**
 * 新增空行
 */
function addRow(fltNo,alnCode,alnCnName) {
	$("#baseTable").bootstrapTable("append",{
		id : "new_"+(new Date().getTime()),
		order : rowIndex,
		fltNo : fltNo,
		shareFltNo : "",
		aircraftNumber : "",
		actType : "",
		departApt : "",
		arrivalApt : "",
		std : "",
		sta : "",
		etd : "",
		eta : "",
		propertyCode : "",
		attrCode : "",
		alnCode : alnCode,
		alnCnName : alnCnName,
		fltDate : getNextFormatDate()
	});
}
function removeRow(){
	var selections = $("#baseTable").bootstrapTable("getSelections");
	if(selections&&selections.length>0){
		for(var i=0;i<selections.length;i++){
			var row = selections[i];
			if(row&&row.id){
				if (!isNewRow(row.id)) {
					removeRows[row.id] = $("#baseTable").bootstrapTable("getRowByUniqueId",row.id);
				}
				$("#baseTable").bootstrapTable("removeByUniqueId",row.id);
			}
		}
	}
}

/**
 * 获取表格数据
 */
var editRows = {};//修改的行
var removeRows = {};//删除的行
function getGridData(){
	var dataList = $("#baseTable").bootstrapTable("getData");
	var isNew = $("#isNew").val();
	var ioType = $("#ioType").val();
	var columns = [];
	if(isNew=="true"){
		columns = [{field:"fltNo",title:"航班号"},{field:"actType",title:"机型"},{field:"departApt",title:"起场"},{field:"arrivalApt",title:"落场"},{field:"std",title:"计起"},{field:"sta",title:"计落"},{field:"etd",title:"预起"},{field:"eta",title:"预落"},{field:"propertyCode",title:"性质"},{field:"attrCode",title:"属性"},{field:"alnCode",title:"航空公司"},{field:"fltDate",title:"日期"}];
	}else{
		columns = [{field:"actType",title:"机型"},{field:"departApt",title:"起场"},{field:"arrivalApt",title:"落场"},{field:"std",title:"计起"},{field:"sta",title:"计落"},{field:"etd",title:"预起"},{field:"eta",title:"预落"},{field:"propertyCode",title:"性质"},{field:"attrCode",title:"属性"}];
	}
	var newRows = [];//新增的行
	for(var i=0;i<dataList.length;i++){
		var data = $.extend(true,{},dataList[i]);
		var isEmpty = true;
		for(var key in data){
			if (key != 'id' && data[key]) {
				isEmpty = false;
				break;
			}
		}
		if(!isEmpty){
			for(var j=0;j<columns.length;j++){
				var column = columns[j];
				var field = column.field;
				var title = column.title;
				if(!data[field]){
					layer.msg("第"+(i+1)+"行"+title+"不能为空！",{icon:7});
					return false;
				}
				//如果计落小于计起，或者预落小于预起，或者预起小于计起，均无法保存
				/**
				 * 三种情况下校验不通过
				 * 1 前>后 && 都没有+号
				 * 2 前>后 && 都有+号
				 * 3 前<后 && 前有+ && 后无+
				 */
				if(field=='std'){
					if(data['sta']<data['std']){
						if((data['std'].indexOf("+")!=-1&&data['sta'].indexOf("+")!=-1)||
							(data['std'].indexOf("+")==-1&&data['sta'].indexOf("+")==-1)){
							layer.msg("计落小于计起，无法保存",{icon:7});
							 return false;
						} 
					}
					if(data['sta']>data['std']&&data['std'].indexOf("+")!=-1&&data['sta'].indexOf("+")==-1){
						 layer.msg("计落小于计起，无法保存",{icon:7});
						 return false;
					}
					if(data['etd']<data['std']){
						if((data['std'].indexOf("+")!=-1&&data['etd'].indexOf("+")!=-1)||
							(data['std'].indexOf("+")==-1&&data['etd'].indexOf("+")==-1)){
							layer.msg("预起小于计起，无法保存",{icon:7});
							 return false;
						} 
					}
					if(data['etd']>data['std']&&data['std'].indexOf("+")!=-1&&data['etd'].indexOf("+")==-1){
						 layer.msg("预起小于计起，无法保存",{icon:7});
						 return false;
					}
				}
				if(field=='etd'){
					if(data['eta']<data['etd']){
						if((data['etd'].indexOf("+")!=-1&&data['eta'].indexOf("+")!=-1)||
							(data['etd'].indexOf("+")==-1&&data['eta'].indexOf("+")==-1)){
							layer.msg("预落小于预起，无法保存",{icon:7});
							 return false;
						} 
					}
					if(data['eta']>data['etd']&&data['etd'].indexOf("+")!=-1&&data['eta'].indexOf("+")==-1){
						 layer.msg("预落小于预起，无法保存",{icon:7});
						 return false;
					}
				}
				if(field=='sta'||field=='std'||field=='eta'||field=='etd'){
					var val = data[field];
					if(validFltTime(val)){
						var fltDate = data["fltDate"];
						var fltTime = formatFltTime(fltDate,val);
						data[field] = fltTime;
					} else {
						layer.msg("第"+(i+1)+"行"+title+"格式错误！",{icon:7});
						return false;
					}
				}
			}
			//新增的行
			if(data.id&&isNewRow(data.id)){
				delete data["id"];
				newRows.push(data);
			} else if(data.id&&editRows[data.id]){//修改的行
				var editedRow = editRows[data.id];
				var fltDate = editedRow.fltDate;
				editedRow.std = formatFltTime(fltDate,editedRow.std);//转换std格式
				editedRow.sta = formatFltTime(fltDate,editedRow.sta);//转换sta格式
				editedRow.etd = formatFltTime(fltDate,editedRow.etd);//转换etd格式
				editedRow.eta = formatFltTime(fltDate,editedRow.eta);//转换eta格式
			}
		}
	}
	//判断是否有修改的数据
	if(newRows.length==0&&$.isEmptyObject(editRows)&&$.isEmptyObject(removeRows)){
		layer.msg("没有需要保存的内容！",{icon:7});
		return false;
	}
	var result = {
			newRows:newRows,
			editRows:editRows,
			removeRows:removeRows,
			ioType:ioType
	}
	return result;
}
/**
 * 判断是否为新增行
 * @param id
 * @returns
 */
function isNewRow(id){
	var reg = new RegExp("^new_");
	return reg.test(id);
}
/**
 * 校验计起、计落
 * @param str
 * @returns {Boolean}
 */
function validFltTime(str){
	var reg = new RegExp("[0-9]{1,}[+]?");
	if(reg.test(str)){
		if(str.indexOf("\+")==-1&&str.length!=4){
			return false;
		} else if(str.indexOf("\+")>0&&str.length!=5){
			return false;
		} else {
			var hh = Number(str.substring(0,2));
			var mi = Number(str.substring(2,4));
			if(hh<0||hh>23){
				return false;
			}
			if(mi<0||mi>59){
				return false;
			}
		}
	} else {
		return false;
	}
	return true;
}
/**
 * 转换航班时间为 yyyy-mm-dd hh24:mi格式
 * @param fltDate
 * @param fltTime
 */
function formatFltTime(fltDate,fltTime){
	var date = new Date(fltDate.substring(0,4)+"/"+fltDate.substring(4,6)+"/"+fltDate.substring(6,8));
	if(fltTime.indexOf("\+")>0){
		date.setDate(date.getDate() + 1);
	}
	var year = date.getFullYear();
	var month = (Array(2).join(0) + (parseInt(date.getMonth()) + 1)).slice(-2);
	var day = (Array(2).join(0) + date.getDate()).slice(-2);
	fltDate = year+"-"+month+"-"+day;
	return fltDate+" "+fltTime.substring(0,2)+":"+fltTime.substring(2,4);
}

function alnLineMatcher(params, data) {
	var search = data.text+data.id+data.airline_code;
	if ($.trim(params.term) === '') {
      return data;
    }
    if (search.toUpperCase().indexOf(params.term.toUpperCase()) > -1) {
      var modifiedData = $.extend({}, data, true);
      return modifiedData;
    }
    return null;
}

function aptMatcher(params, data) {
	var search = data.text+data.id+data.icao_code;
	if ($.trim(params.term) === '') {
      return data;
    }
    if (search.toUpperCase().indexOf(params.term.toUpperCase()) > -1) {
      var modifiedData = $.extend({}, data, true);
      return modifiedData;
    }
    return null;
}
