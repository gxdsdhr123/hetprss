var baseTable;
var tableIndex =0;
var rowValue ;
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	});
	
//	initSelect2("acttypeCode","请选择航班号，支持多选");//航班号
//	initSelect2("alnCode","请选择注册号，支持多选");//注册号
//	//初始化默认选中值
//	$('#acttypeCode').val(defaultActtypeCode).trigger('change'); 
//	$('#alnCode').val(defaultAlnCode).trigger('change'); 
//	$('input[name=alnCode]').val(defaultAlnCode.join(","));
//	$('input[name=acttypeCode]').val(defaultActtypeCode.join(","));
	
	$('#add').click(function() {
		addRow();
	});
	
	jQuery.fn.bootstrapTable.columnDefaults.sortable = false;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	jQuery.fn.bootstrapTable.columnDefaults.editable = {toggle:'dblclick'};

	baseTable = $("#baseTable");
	var tableOptions = {
		url: ctx + "/rule/delayTime/detailList", 
	    method: "get",
	    dataType: "json",
		striped: true,
	    cache: true,
	    undefinedText:'',
	    uniqueId:'tableIndex',
	    checkboxHeader:false,
	    search:false,
	    pagination: false,
	    queryParamsType:'',
	    queryParams: function (params) {
	        return {
	        	id :$("#id").val()
	        }
	    },
	    responseHandler : function(res){
	    	tableIndex = res.length;
	    	return res;
	    },
	    onClickRow:function onClickRow(row,tr,field){
			$(".clickRow").removeClass("clickRow");
			$(tr).addClass("clickRow");
		},
	    columns: [
			{field: "tableIndex",title:"序号",sortable:false,editable:false,
				formatter:function(value,row,index){
					row.tableIndex = index;
					return index+1;
				}
			},
			{field: "ruleExtInOutPortText", title: "进出港",editable:false,align:'left',halign:'center'},
			{field: "priority", title: "优先级",editable:false,align:'left',halign:'center'},
			{field: "acttypeText", title: "机型",editable:false,align:'left',halign:'center'},
			{field: "alnText", title: "航空公司",editable:false,align:'left',halign:'center'},
			{field: "extend_condition", title: "扩展条件",editable:false,halign:'center'},
			{field: "time_condition", title: "时长等于",editable:false,halign:'center'},
			{field: "cz", title: "操作",editable:false,halign:'center',
				formatter:function(value,row,index){
					var html = '';
					html += '<button onclick="setUpdate('+row.tableIndex+')" type="button" class="btn btn-link"><i class="fa fa-edit">&nbsp;</i></button>';
					html += '<button onclick="deleteRow('+row.tableIndex+')" type="button" class="btn btn-link"><i class="fa fa-trash">&nbsp;</i></button>';
					return html;
				}}
	    ]
	};
	baseTable.bootstrapTable(tableOptions);// 加载基本表格
	
});

/**
 * 初始化select下拉
 * @param selectId
 * @param tip
 */
function initSelect2(selectId,tip){
	$('#'+selectId).select2({  
        placeholder: tip,  
        width:"400px",
        language : "zh-CN",
		templateResult : formatRepo,
		templateSelection : formatRepoSelection,
		escapeMarkup : function(markup) {
			return markup;
		}
    }); 
}

this.checkLength = function(text,length){
    var tlen =  text.match(/[^ -~]/g)==null?text.length:text.length+text.match(/[^ -~]/g).length;
    return tlen <=length ;
  }



//筛选弹出复选框
function openCheck(type) {
	layer.open({
	type : 2,
	title : '复选',
	content : ctx + "/rule/delayTime/openCheck?type=" + type + "&selectedId=" + $("input[name='" + type + "Value']").val() + "&selectedText=" + $("input[name='" + type + "']").val(),
	btn : [ "确定", "清空已选", "取消" ],
	area : [ '800px', '450px' ],
	yes : function(index, layero) {
		var iframeWin = window[layero.find('iframe')[0]['name']];
		var data = iframeWin.getChooseData();
		$("input[name='" + type + "']").val(data.chooseTitle);
		$("input[name='" + type + "Value']").val(data.chooseValue);
		layer.close(index);
	},
	btn2 : function(index, layero) {// 重置表单及筛选条件
		var iframeWin = window[layero.find('iframe')[0]['name']];
		iframeWin.clearSelect();
		return false;
	},
	btn3 : function(index) {// 关闭前重置表单及筛选条件
		layer.close(index);
	}
	})
}
function clearSelectedRow() {
	clickRowId = "";
	clickRow = null;
	$(".clickRow").removeClass("clickRow");
}

function doCheck(){
	if($("#enName").val()==""){
		showWarnMsg("请输入英文名称！");
		return false ;
	}
	if($("#cnName").val()==""){
		showWarnMsg("请输入中文时长名称！");
		return false ;
	}
	var rows = baseTable.bootstrapTable("getData");
	if(rows.length < 1){
		showWarnMsg("请编辑添加规则详情！");
		return false ;
	}
	return true;
}

function deCheckDetail(){
	if($("input[name='ruleExtInOutPort']:checked").length==0){
		showWarnMsg("请选择进出港类型！");
		return false;
	}
	var priority = $("select[name=priority]").val();
	var data = baseTable.bootstrapTable("getData");
	var flag = true;
	for(var i=0;i<data.length;i++){
		if(priority==data[i].priority ){
			if(rowValue != null ) {
				if(rowValue.tableIndex != data[i].tableIndex){
					showWarnMsg("优先级已存在，请重新选择优先级！");
					flag = false;
					return;
				}
			} else {
				showWarnMsg("优先级已存在，请重新选择优先级！");
				flag = false;
				return;
			}
		}
	}
	return flag;
}
function getSubmitData() {
	var paramsJsonObj = JSON.parse($("#createForm").form2json())
	var data = baseTable.bootstrapTable("getData");
	paramsJsonObj['tableData'] = data;
	return JSON.stringify(paramsJsonObj);
}
function formatRepo(repo) {
	return repo.text;
}
// select2显示选项处理
function formatRepoSelection(repo) {
	return repo.text;
}

/**
 * 显示提示信息框
 * @param msg
 */
function showWarnMsg(msg){
	layer.msg(msg, {
		icon : 7,
		time : 1000
	});
}

function checkValue(mtext){
	 var rep=/\[\W+\]/i
	 var exp=mtext.replace(rep,10);
	 try{
		 eval(exp);
		 return true;
	 }catch(e){
		 return false;
	 }
}

function openGuide(item){
	var ruleId = $("#"+item+"_ruleId").val();
	var condition = $("#"+item+"_condition").text();
	var rule_colids = $("#"+item+"_colids").val();
	var expression = $("#"+item+"_expression").val();
	var createIframe = parent.layer.open({
		type : 2,
		title : '条件',
		maxmin : false,
		shadeClose : false,
		area : [ "640px", "400px" ],
		content : ctx
				+ "/param/common/guide?schema=RULE&ruleId="
				+ ruleId
				+ "&colids="
				+ rule_colids
				+ "&condition="
				+ encodeURIComponent(encodeURIComponent(condition))
				+ "&expression="
				+ encodeURIComponent(encodeURIComponent(expression)),
		btn : [ "确认","复制规则" ],
		yes : function(index, layero) {
			var mtext = $(
					$(layero).find("iframe")[0].contentWindow.document.getElementById("mtext")).text();
			var expression = $(layero).find("iframe")[0].contentWindow.getDrlRule();
			var rule_colids = $(layero).find("iframe")[0].contentWindow.getvarcols();
			$("#"+item+"_condition").text(mtext);
			$("#"+item+"_colids").val(rule_colids);
			$("#"+item+"_expression").val(expression);
			if(item == 'extend'){
				$.ajax({
					type : 'post',
					url : ctx+ "/rule/delayTime/createDrl",
					data : {
						expression : expression
					},
					async : false,
					dataType : 'json',
					success : function(data) {
						if (data.code == 0) {
							var drl = data.result;
							$("#"+item+"_condition").val(mtext);
							$("#"+item+"_colids").val(rule_colids);
							$("#"+item+"_expression").val(expression);
							$("#"+item+"_drools").val(drl);
						} else {
							layer.msg(data.msg,
									{
										icon : 2,
										time : 1000
									});
						}
					}
				});
			}
			layero.find(".layui-layer-close").click();
		},
		btn2 : function(index, layero) {
			$(layero).find("iframe")[0].contentWindow.openCopy();
			return false;
		}
	});
}


/**
 * 显示公式编辑器
 */
function showGuide(item){
	var ruleId = $("#"+item+"_ruleId").val();
	var condition = $("#"+item+"_condition").text();
	var rule_colids = $("#"+item+"_colids").val();
	var expression = $("#"+item+"_expression").val();
	var createIframe = parent.layer.open({
		type : 2,
		title : '条件',
		maxmin : false,
		shadeClose : false,
		area : [ "640px", "400px" ],
		content : ctx
				+ "/rule/delayTime/guide?schema=RULE&ruleId="
				+ ruleId
				+ "&colids="
				+ rule_colids
				+ "&condition="
				+ encodeURIComponent(encodeURIComponent(condition))
				+ "&expression="
				+ encodeURIComponent(encodeURIComponent(expression)),
		btn : [ "确认","取消" ],
		yes : function(index, layero) {
			var mtext = $(
					$(layero).find("iframe")[0].contentWindow.document.getElementById("mtext")).text();
			var expression = $(layero).find("iframe")[0].contentWindow.getDrlRule();
			var rule_colids = $(layero).find("iframe")[0].contentWindow.getvarcols();
			$("#"+item+"_condition").text(mtext);
			$("#"+item+"_condition").val(mtext);
			$("#"+item+"_colids").val(rule_colids);
			$("#"+item+"_expression").val(expression);
			layero.find(".layui-layer-close").click();
		},
		btn2 : function(index, layero) {
			layero.find(".layui-layer-close").click();
		}
	});
}
function checkValue(mtext){
	var rep = /^\[{1}[\u4e00-\u9fa5A-Z_]+\]{1}([+-\s]+[0-9]+){0,1}$/ig;
	return mtext.search(rep)>-1?true:false;
}
function addRow(){
	if(deCheckDetail()){
		var paramsJsonObj = {};
		//进出港
		var ruleExtInOutPortObj=$("input[name='ruleExtInOutPort']:checked");
		var ruleExtInOutPort=[];
		var ruleExtInOutPortText=[];
		$.each(ruleExtInOutPortObj,function(k,v){
			ruleExtInOutPort[ruleExtInOutPort.length]=$(v).val();
			ruleExtInOutPortText[ruleExtInOutPortText.length]=$(v).attr("title");
		})
		paramsJsonObj['ruleExtInOutPort'] = ruleExtInOutPort.join(',');
		paramsJsonObj['ruleExtInOutPortText'] = ruleExtInOutPortText.join(',');
		//型号
		paramsJsonObj['acttypeCode'] = $("input[name=acttypeCodeValue]").val();
		paramsJsonObj['acttypeText'] = $("input[name=acttypeCode]").val();
		//航空公司
		paramsJsonObj['alnCode'] = $("input[name=alnCodeValue]").val();
		paramsJsonObj['alnText'] = $("input[name=alnCode]").val();
		//优先级
		var priority = $("select[name=priority]").val();
		paramsJsonObj['priority'] = priority;
		
		var extend_expression = $("#extend_expression").val();
		paramsJsonObj['extend_expression'] = extend_expression;
	
		var extend_colids = $("#extend_colids").val();
		paramsJsonObj['extend_colids'] = extend_colids;
	
		var extend_drools = $("#extend_drools").val();
		paramsJsonObj['extend_drools'] = extend_drools;
	
		var extend_condition = $("#extend_condition").text();
		paramsJsonObj['extend_condition'] = extend_condition;
		
		var time_expression = $("#time_expression").val();
		paramsJsonObj['time_expression'] = time_expression;
	
		var time_colids = $("#time_colids").val();
		paramsJsonObj['time_colids'] = time_colids;
	
		var time_drools = $("#time_drools").val();
		paramsJsonObj['time_drools'] = time_drools;
	
		var time_condition = $("#time_condition").text();
		paramsJsonObj['time_condition'] = time_condition;
		
		if(rowValue != null){
			paramsJsonObj['tableIndex'] = rowValue.tableIndex;
			baseTable.bootstrapTable("updateRow",{index:rowValue.tableIndex,row:paramsJsonObj});
		} else {
			paramsJsonObj['tableIndex'] = tableIndex;
			baseTable.bootstrapTable("insertRow",{index:tableIndex,row:paramsJsonObj});
			tableIndex++;
		}
		rowValue = null;
		$("#add").html("添加");
	}
}

function deleteRow(tableIndex){
	baseTable.bootstrapTable("removeByUniqueId",tableIndex);
}

function setUpdate(tableIndex){
	var row = baseTable.bootstrapTable("getRowByUniqueId",tableIndex);
	rowValue = row;
	$("#add").html("修改");
	//进出港
	var ruleExtInOutPortObj=$("input[name='ruleExtInOutPort']");
	var ruleExtInOutPort=row.ruleExtInOutPort.split(',');
	$.each(ruleExtInOutPortObj,function(k,v){
		$.each(ruleExtInOutPort,function(index,item){
			if(item==$(v).val()){
				$(v).attr("checked",'checked'); 
			}
		})
	})
	//型号
	$("input[name=acttypeCodeValue]").val(row.acttypeCode);
	$("input[name=acttypeCode]").val(row.acttypeText);
	
	//航空公司
	$("input[name=alnCodeValue]").val(row.alnCode);
	$("input[name=alnCode]").val(row.alnText);
	//优先级
	$("select[name=priority]").val(row.priority);
	
	$("#extend_expression").val(row.extend_expression);
	$("#extend_colids").val(row.extend_colids);
	$("#extend_drools").val(row.extend_drools);
	$("#extend_condition").val(row.extend_condition);
	$("#extend_condition").text(row.extend_condition);
	
	$("#time_expression").val(row.time_expression);
	$("#time_colids").val(row.time_colids);
	$("#time_drools").val(row.time_drools);
	$("#time_condition").val(row.time_condition);
	$("#time_condition").text(row.time_condition);
	layui.use("form", function() {
		var form = layui.form;
		form.render(null, 'createForm');
		form.render();
		form.render("select","filter");
		form.render("checkbox","filter")
	});
	
}