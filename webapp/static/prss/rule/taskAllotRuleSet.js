$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	});
	
	initSelect2("airline","请选择航空公司，支持多选");//航空公司
	initSelect2("airplaneType","请选择机型，支持多选");//机型
	//初始化默认选中值
	$('#airline').val(defaultAirLine).trigger('change'); 
	$('#airplaneType').val(defaultAtcactype).trigger('change'); 
	//向导
	$('#guide').click(function() {
		var ruleId = $("#ruleId").val();
		var condition = $("#condition").text();
		var rule_colids = $("#colids").val();
		var expression = $("#expression").val();
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
				$("#condition").text(mtext);
				$("#colids").val(rule_colids);
				$("#expression").val(expression);
				$.ajax({
					type : 'post',
					url : ctx+ "/rule/taskAllotRule/createDrl",
					data : {
						expression : expression
					},
					async : false,
					dataType : 'json',
					success : function(data) {
						if (data.code == 0) {
							var drl = data.result;
							$("#condition").val(mtext);
							$("#colids").val(rule_colids);
							$("#expression").val(expression);
							$("#drools").val(drl);
						} else {
							layer.msg(data.msg,
									{
										icon : 2,
										time : 1000
									});
						}
					}
				});
				layero.find(".layui-layer-close").click();
			},
			btn2 : function(index, layero) {
				$(layero).find("iframe")[0].contentWindow.openCopy();
				return false;
			}
		});
	});
	//初始化
	initData();
});

function initData(){
	for(var i=0;i<ruleProcList.length;i++){
		var ruleProc=ruleProcList[i];
		var jobType=ruleProc.jobType;
		var procList=ruleProc.procList;
		$("#"+jobType+"NumSelect").val(procList.length);//设置数量下拉框值
		doChangeJobType(jobType,$("#"+jobType+"Href"));//创建表格
		
		//循环流程
		for(var j=0;j<procList.length;j++){
			var proc=procList[j];
			var procId=proc.procId;//流程ID
//			var sortNum=proc.sortNum;//序号
			var sortNum= j + 1;//序号
			var arrangeTm=proc.arrangeTm;//任务分配时间
			var variableId=proc.variableId;//变量ID
			var ruleProcId=proc.ruleProcId;//规则流程ID
			
			getProcessByJobTypeInit(jobType,procId,sortNum);//获取流程节点
			$("#"+jobType+"Process"+sortNum).val(procId);
			$("input[name='"+jobType+"TaskTime"+sortNum+"']").val(arrangeTm);
			$("input[name='"+jobType+"TaskTimeVar"+sortNum+"']").val(variableId);
			$("#"+jobType+sortNum+"TR").attr("proc-rule-id",ruleProcId);
			var nodeList=proc.nodeList;
			//循环节点
			for(var k=0;k<nodeList.length;k++){
				var node=nodeList[k];
				var nodeId=node.nodeId;//节点ID
				var nodeStandardTm=node.nodeStandardTm;//节点标准时间
				var nodeVariableId=node.nodeVariableId;//节点变量
				var nodeSortNum=node.nodeSortNum;//序号
				var ruleProcNodeId=node.ruleProcNodeId;//规则节点ID
				$("input[name='"+nodeId+"Time"+sortNum+"']").val(nodeStandardTm);
				$("input[name='"+nodeId+"TimeVar"+sortNum+"']").val(nodeVariableId);
				$("#"+nodeId+sortNum+"TD").attr("node-rule-id",ruleProcNodeId);
			}
			
		}
	}
}
function getProcessByJobTypeInit(jobType,procId,sortNum){
	if(jobType!=null){
		$.ajax({
			type : 'post',
			url : ctx+ "/rule/taskAllotRule/getProcessByJobType",
			data : {
				jobType : jobType
			},
			async : false,
			dataType : 'json',
			success : function(data) {
				var j=sortNum;
				var jobTypeTable=$("#"+jobType+"Table");
				var processOptList="";
				var nodeName=[];//节点名称
				var nodeId=[];//节点ID
				//循环取出工作流名称及节点名称
				$.each(data,function(k,v){
					processOptList+="<option value='"+v.id+"'>"+v.displayName+"</option>";
					//默认取出第一个流程节点
					if(v.id==procId){
						var nodeArray=v.nodeArray;
						$.each(nodeArray,function(nodeK,nodeV){
							nodeId.push(nodeV.id);
							nodeName.push(nodeV.displayName);
						});
					}
				})
				if(nodeName.length==0){
					showWarnMsg("此作业流程没有配置节点！");
					return;
				}
				var tr="<tr data-jobtype="+jobType+"_"+j+" id='"+jobType+j+"TR'>";
				tr+="<td rowspan='"+nodeName.length+"'>"+j+"</td>";//序号
				tr+="<td rowspan='"+nodeName.length+"' td-type='process'><select id='"+jobType+"Process"+j+"' onchange='getNodesByProcessId(this,\""+jobType+"\")'>"+processOptList+"</select></td>";//流程选择
				tr+="<td rowspan='"+nodeName.length+"' td-type='taskTime'><input name='"+jobType+"TaskTime"+j+"' type='text' onclick='showGuide(this)' readonly='readonly'/><input name='"+jobType+"TaskTimeVar"+j+"' type='hidden'/></td>";//任务分配时间
				if(nodeName.length==0){
					tr+="<td>&nbsp;</td>";
					tr+="<td>&nbsp;</td>";
				}else{
					for(var i=0;i<nodeName.length;i++){
						var check = "";
						if(i==0 || i==nodeName.length -1)
							check = "check";
						tr+="<td td-type='nodeName' id='"+nodeId[i]+j+"TD' node-id='"+nodeId[i]+"' >"+nodeName[i]+"</td>";//节点名称
						tr+="<td td-type='nodeTime' node-check='"+check+"'><input name='"+nodeId[i]+"Time"+j+"' type='text' onclick='showGuide(this)' readonly='readonly'/><input name='"+nodeId[i]+"TimeVar"+j+"' type='hidden'/></td>";//标准时间
						tr+="</tr>";
						if(i!=nodeName.length-1){
							tr+="<tr>";
						}
					}
				}
				jobTypeTable.append($(tr));
			}
		})
	}
}
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

function doCheck(){
	if($("#ruleName").val()==""){
		showWarnMsg("请输入规则名称！");
		return false ;
	}
	if( ! this.checkLength($("#ruleName").val(),20 ) ){
		showWarnMsg("规则名称不能超过20个字！");
		return false ;
	}
	
	if(checkNodeArray.length>0){
		showWarnMsg(checkNodeArray[0]);
		checkNodeArray=[];
		finalDataArray=[];
		return false;
	}
	
	if(finalDataArray.length==0){
		finalDataArray=[];
		showWarnMsg("请设置作业流程！");
		return false;
	}
	if($("input[name='ruleExtApronType']:checked").length==0){
		showWarnMsg("请选择远近机位！");
		return false;
	}
	if($("input[name='ruleExtInOutPort']:checked").length==0){
		showWarnMsg("请选择进出港类型！");
		return false;
	}
	return true;
}
function getSubmitData() {
	if($("#ruleName").val()==""){
		showWarnMsg("请输入规则名称！");
		return;
	}
	var paramsJsonObj = JSON.parse($("#createForm").form2json())
	//航空公司
	var airline= $("#airline").select2("val");
	paramsJsonObj['airline']=airline;
	//机型
	var airplaneType= $("#airplaneType").select2("val");
	paramsJsonObj['airplaneType'] = airplaneType;
	//机位
	var apronTypeObj=$("input[name='ruleExtApronType']:checked");
	var ruleExtApronType=[];
	$.each(apronTypeObj,function(k,v){
		ruleExtApronType[ruleExtApronType.length]=$(v).val();
	});
	paramsJsonObj['ruleExtApronType'] = ruleExtApronType;
	//进出港
	var ruleExtInOutPortObj=$("input[name='ruleExtInOutPort']:checked");
	var ruleExtInOutPort=[];
	$.each(ruleExtInOutPortObj,function(k,v){
		ruleExtInOutPort[ruleExtInOutPort.length]=$(v).val();
	})
	paramsJsonObj['ruleExtInOutPort'] = ruleExtInOutPort;
	
	var expression = $("#expression").val();
	paramsJsonObj['expression'] = expression;

	var expression = $("#expression").val();
	paramsJsonObj['expression'] = expression;

	var colids = $("#colids").val();
	paramsJsonObj['colids'] = colids;

	var drools = $("#drools").val();
	paramsJsonObj['drools'] = drools;

	var condition = $("#condition").text();
	paramsJsonObj['condition'] = condition;
	
	
	var tables=$("table[data-type='jobType']");
	if(tables!=null){
		$.each(tables,function(k,v){
			var _v=$(v);
			var tableId=_v.attr("id");
			if(tableId!="jobTypeTableTemple"){
				var jobType=tableId.replace("Table","");
				saveData(jobType);
			}
		});
	}	
	paramsJsonObj['jobTypeArray']=finalDataArray;
	paramsJsonObj['deleteRuleProc']=deleteRuleProc.join(",");
	
	
	//$("#show_json").text(JSON.stringify(paramsJsonObj));
	//alert(JSON.stringify(paramsJsonObj))
	return JSON.stringify(paramsJsonObj);
}
function formatRepo(repo) {
	return repo.text;
}
// select2显示选项处理
function formatRepoSelection(repo) {
	return repo.text;
}
//根据作业类型获取工作流
var jobTypeList;
function getProcessByJobType(jobType,obj){
	var num=$(obj).val();
	if(num==""){
		//showWarnMsg("请选择数量！");
		removeRows(jobType);
		/* 解决修改时更改作业流程无法保存问题 2018-03-16 SunJ */
		$("#"+jobType+"Table").remove();
		/* end */
		return;
	}
	if(jobType!=null){
		$.ajax({
			type : 'post',
			url : ctx+ "/rule/taskAllotRule/getProcessByJobType",
			data : {
				jobType : jobType
			},
			async : false,
			dataType : 'json',
			success : function(data) {
				dealProcessTable(data,num,jobType);
			}
		})
	}
}
var finalDataArray=[];
var checkNodeArray=[];
function dealProcessTable(data,num,jobType){
	//cleanCache(jobType);//清除缓存
	if(data!=null&&data.length>0){
		var jobTypeTable=$("#"+jobType+"Table");
		var processOptList="";
		var cnt=1;
		var nodeName=[];//节点名称
		var nodeId=[];//节点ID
		//循环取出工作流名称及节点名称
		$.each(data,function(k,v){
			processOptList+="<option value='"+v.id+"'>"+v.displayName+"</option>";
			//默认取出第一个流程节点
			if(cnt==1){
				var nodeArray=v.nodeArray;
				$.each(nodeArray,function(nodeK,nodeV){
					nodeId.push(nodeV.id);
					nodeName.push(nodeV.displayName);
				});
			}
			cnt++;
		})
		if(nodeName.length==0){
			showWarnMsg("此作业流程没有配置节点！");
			return;
		}
		removeRows(jobType);//删除除表头外的列
		//processOptList+="</select>";
		for(var j=1;j<=num;j++){
			var tr="<tr data-jobtype="+jobType+"_"+j+" id='"+jobType+j+"TR'>";
			tr+="<td rowspan='"+nodeName.length+"'>"+j+"</td>";//序号
			tr+="<td rowspan='"+nodeName.length+"' td-type='process'><select id='"+jobType+"Process"+j+"' onchange='getNodesByProcessId(this,\""+jobType+"\")'>"+processOptList+"</select></td>";//流程选择
			tr+="<td rowspan='"+nodeName.length+"' td-type='taskTime'><input name='"+jobType+"TaskTime"+j+"' type='text' onclick='showGuide(this)' readonly='readonly'/><input name='"+jobType+"TaskTimeVar"+j+"' type='hidden'/></td>";//任务分配时间
			if(nodeName.length==0){
				tr+="<td>&nbsp;</td>";
				tr+="<td>&nbsp;</td>";
			}else{
				for(var i=0;i<nodeName.length;i++){
					var check = "";
					if(i==0 || i==nodeName.length -1)
						check = "check";
					tr+="<td td-type='nodeName' id='"+nodeId[i]+j+"TD' node-id='"+nodeId[i]+"' >"+nodeName[i]+"</td>";//节点名称
					tr+="<td td-type='nodeTime' node-check='" + check + "' ><input name='"+nodeId[i]+"Time"+j+
					"' type='text' onclick='showGuide(this)' readonly='readonly'/><input name='"+nodeId[i]+"TimeVar"+j+"' type='hidden'/></td>";//标准时间
					tr+="</tr>";
					if(i!=nodeName.length-1){
						tr+="<tr>";
					}
				}
			}
			if(jobTypeTable[0]!=null){
				jobTypeTable=$("#"+jobType+"Table");
			}else{
				var jobTypeTableTemple=$("#jobTypeTableTemple");//获取模板table
				jobTypeTable=jobTypeTableTemple.clone();//克隆
				jobTypeTable.attr("id",jobType+"Table");//设置新的ID
				$("#jobTypeDiv").append(jobTypeTable);//放在div中
			}
			jobTypeTable.append($(tr))
			jobTypeTable.show();//显示
			
		}
	}else{
		showWarnMsg("此作业类型没有配置作业流程！");
		return;
	}
}
/**
 * 删除出表头外所有行
 * @param tabObj
 */
function removeRows(jobType){
	var trs=$("#"+jobType+"Table tr");
	if(trs){
		for(var i=1;i<trs.length;i++){
			var tr=$(trs[i]);
			var procRuleId=tr.attr("proc-rule-id");
			if(procRuleId){
				deleteRuleProc.push(procRuleId);
			}
		}
	}
	$("#"+jobType+"Table tr:not(:eq(0))").remove();
	
}
/**
 * 暂存信息
 */
function saveData(type){
	var jobTypeObj={};
	var nodeArray=[];//节点数组
	var trs=$("#"+type+"Table tr");
	if(trs){
		var jobKind = $("#" + type + "Href").text();
		var checkNode = true;
		for(var i=1;i<trs.length;i++){
			var tr=$(trs[i]);
			if(tr.attr("data-jobtype")){
				var temp=tr.attr("data-jobtype");
				var jobType="";//作业类型
				if(jobType!=temp && checkNode){
					jobType=temp;
					if(!$.isEmptyObject(jobTypeObj)){
						jobTypeObj.nodeArray=nodeArray;
						finalDataArray.push(jobTypeObj);
					}
					jobTypeObj={};
					nodeArray=[];
					jobTypeObj.jobSeq=jobType.split("_")[1];
					jobTypeObj.jobType=jobType.split("_")[0];
					jobTypeObj.ruleProcId=tr.attr("proc-rule-id");
				}
			}
			var tds=$(tr).children();//单元格
			$.each(tds,function(k,v){
				var tdType=$(v).attr("td-type");
				if(tdType){
					switch(tdType){
						case "process":
							var selectObj=$(v).children(0).find("option:selected");
							var processId=selectObj.val();//流程ID
							var processName=selectObj.text();//流程名称
							jobTypeObj.processOption=$(v).html();
							jobTypeObj.processId=processId;//流程ID
							jobTypeObj.processName=processName;//流程名称
							break;
						case "taskTime":
							var taskTime=$(v).children(0).val();
							jobTypeObj.taskTime=taskTime;//任务分配时间
							if(taskTime == null || taskTime ==''){
								checkNodeArray.push("请编辑【" + jobKind + "->" + jobTypeObj.processName + "->任务分配时间】");
								checkNode = false;
							}
							jobTypeObj.taskTimeVar=$(v).children(0).next().val();//变量
							break;
						case "nodeName":
							var nodeName=$(v).text();//节点名称
							var nodeId=$(v).attr("node-id");//节点ID
							var nodeObj={};
							nodeObj.nodeName=nodeName;
							nodeObj.nodeId=nodeId;
							nodeObj.nodeRuleId=$(v).attr("node-rule-id");
							var nextNode=$(v).next();
							var check = $(nextNode).attr("node-check");
							if(nextNode.attr("td-type")=="nodeTime"){
								var nodeTime=nextNode.children(0).val();
								nodeObj.nodeTime=nodeTime;//标准时间
								nodeObj.nodeTimeVar=nextNode.children(0).next().val();//变量
							}
							if('check' ==check && (nodeObj.nodeTime ==null || nodeObj.nodeTime == '')){
								checkNodeArray.push("请编辑【" + jobKind + "->" + jobTypeObj.processName + "->"+  nodeObj.nodeName + "节点】时间");
								checkNode = false;
							}
							nodeObj.nodeSeq=nodeArray.length+1;
							nodeArray.push(nodeObj);
							break;
					}
				}
			})
		}
		if(checkNode){
			jobTypeObj.nodeArray=nodeArray;
			finalDataArray.push(jobTypeObj);
		}
	}
}

function setBtnDisable(type){
	var numSelect=$("select[name='numSelect']");
	$.each(numSelect,function(k,v){
		var _v=$(v);
		if(_v.attr("id")!=(type+"NumSelect")){
			_v.attr("disabled","disabled");
		}else{
			_v.removeAttr("disabled");
		}
	});
}
/**
 * 隐藏其他table
 * @param type
 */
function hideOtherTable(type){
	var tables=$("table[data-type='jobType']");
	if(tables!=null){
		$.each(tables,function(k,v){
			var _v=$(v);
			if(_v.attr("id")!=(type+"Table")&&_v.is(":visible")){
				_v.hide();
			}
		});
	}	
}
/**
 * 点击表格行事件
 * @param jobType
 * @param obj
 */
function doChangeJobType(type,obj){
	setBtnDisable(type);//将其他按钮置灰
	hideOtherTable(type);//隐藏其他表格
	
	$(".clickTr").removeClass("clickTr");
	$(obj).parent().parent().addClass("clickTr");//增加底色
	
	var jobTypeTable;//如果对象不存在，创建表格对象
	if($("#"+type+"Table")[0]!=null){
		jobTypeTable=$("#"+type+"Table");
	}else{
		var jobTypeTableTemple=$("#jobTypeTableTemple");//获取模板table
		jobTypeTable=jobTypeTableTemple.clone();//克隆
		jobTypeTable.attr("id",type+"Table");//设置新的ID
		$("#jobTypeDiv").append(jobTypeTable);//放在div中
	}
	jobTypeTable.show();//显示
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

/**
 * 显示公式编辑器
 */
function showGuide(obj){
	var _obj=$(obj);
	var varId=_obj.next().val();
	var express=_obj.val();
	var createIframe = layer.open({
		type : 2,
		title : '条件',
		maxmin : false,
		shadeClose : false,
		area : [ "640px", "400px" ],
		content : ctx+ "/rule/taskAllotRule/guide?varId="+varId+"&express="+encodeURIComponent(express),
		btn : [ "确认" ],
		yes : function(index, layero) {
			var mtext = $(
					$(layero).find("iframe")[0].contentWindow.document.getElementById("mtext")).text();
			if(!checkValue(mtext)){
				showWarnMsg("表达式不正确！");
				return;
			}
			var rule_colids = $(layero).find("iframe")[0].contentWindow.getvarcols();
			//var rule_ens=$(layero).find("iframe")[0].contentWindow.getVarEN();
			_obj.val(mtext);
			_obj.next().val(rule_colids);
			layer.closeAll('iframe');
		}
	});
}
function checkValue(mtext){
	var rep = /^\[{1}[\u4e00-\u9fa5A-Z_]+\]{1}([+-\s]+[0-9]+){0,1}$/ig;
	return mtext.search(rep)>-1?true:false;
}
/**
 * 根据流程模板ID获取节点
 * @param processId
 */
var deleteRuleProc=[];
function getNodesByProcessId(obj,jobType){
	var jobTypeTable=$("#"+jobType+"Table");
	var _obj=$(obj)
	var processId=_obj.val();
	$.ajax({
		type : 'post',
		url : ctx+ "/rule/taskAllotRule/getNodesByProcessId",
		data : {
			processId : processId
		},
		async : false,
		dataType : 'json',
		success : function(nodeData) {
			var num=nodeData.length;
			var processOptList=_obj.html();//流程选择
			var tr=_obj.parent().parent();
			
			var trIndex=tr.index();//行号
			
			var procRuleId=tr.attr("proc-rule-id");
			var datJobtype=tr.attr("data-jobtype");
			if(procRuleId!=null){
				deleteRuleProc.push(procRuleId);
			}
			//tr.remove();
			/**删除处理  --begin*/
			var removeRows=tr.children(0).attr("rowspan");
			var removeObj=[];
			removeObj.push(tr);
			var temp=tr;
			for(var i=1;i<removeRows;i++){
				removeObj.push(temp.next());
				temp=temp.next();
			}
			for(var i=0;i<removeObj.length;i++){
				removeObj[i].remove();
			}
			/**删除处理 --end*/
			
			var j=datJobtype.replace(jobType+"_","");
			var len=nodeData.length;
			var trHtml="<tr data-jobtype="+jobType+"_"+j+" id='"+jobType+j+"TR'>";
			trHtml+="<td rowspan='"+len+"'>"+j+"</td>";//序号
			trHtml+="<td rowspan='"+len+"' td-type='process'><select id='"+jobType+"Process"+j+"' onchange='getNodesByProcessId(this,\""+jobType+"\")'>"+processOptList+"</select></td>";//流程选择
			trHtml+="<td rowspan='"+len+"' td-type='taskTime'><input name='"+jobType+"TaskTime"+j+"' type='text' onclick='showGuide(this)' readonly='readonly'/><input name='"+jobType+"TaskTimeVar"+j+"' type='hidden'/></td>";//任务分配时间
			if(len==0){
				trHtml+="<td>&nbsp;</td>";
				trHtml+="<td>&nbsp;</td>";
			}else{
				for(var i=0;i<len;i++){
					var check = "";
					if(i==0 || i==len -1)
						check = "check";
					trHtml+="<td td-type='nodeName' id='"+nodeData[i].id+j+"TD' node-id='"+nodeData[i].id+"' >"+nodeData[i].displayName+"</td>";//节点名称
					trHtml+="<td td-type='nodeTime' node-check='"+check+"'><input name='"+nodeData[i].id+"Time"+j+"' type='text' onclick='showGuide(this)' readonly='readonly'/><input name='"+nodeData[i].id+"TimeVar"+j+"' type='hidden'/></td>";//标准时间
					trHtml+="</tr>";
					if(i!=len-1){
						trHtml+="<tr>";
					}
				}
			}
			if($("#"+jobType+"Table tr:eq("+(trIndex+1)+")")[0]){
				$("#"+jobType+"Table tr:eq("+(trIndex+1)+")").before($(trHtml));
			}else{
				jobTypeTable.append($(trHtml));
			}
			$("#"+jobType+"Process"+j).val(processId);
			//dealProcessTable(data,num,jobType);
		}
	})
}