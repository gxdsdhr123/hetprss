$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	});

	initSelect2("airline","请选择航空公司，支持多选");//航空公司
	initSelect2("airplaneType","请选择机型，支持多选");//机型
	//向导
	$('#guide').click(function() {
		var ruleId = $("#ruleId").val();
		var condition = $("#condition").text();
		var rule_colids = $("#colids").val();
		var expression = $("#expression").val();
		var createIframe = layer.open({
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
			btn : [ "确认" ],
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
				layer.closeAll('iframe');
			},
			btn2 : function(index, layero) {
				layer.getChildFrame("#createForm1", index)[0].reset();
				var refBtn = layer.getChildFrame("#refreshDetailTable",index);
				refBtn.click();
				return false;
			}
		});
	});
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
	
	paramsJsonObj['jobTypeArray']=finalDataArray;
	//$("#show_json").text(JSON.stringify(paramsJsonObj));
	alert(JSON.stringify(paramsJsonObj))
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
		showWarnMsg("请选择数量！");
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
function dealProcessTable(data,num,jobType){
	cleanCache(jobType);//清除缓存
	if(data!=null&&data.length>0){
		var jobTypeTable=$("#jobTypeTable");
		var processOptList="<select id='process"+jobType+"'>";
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
		removeRows();//删除除表头外的列
		processOptList+="</select>";
		for(var j=1;j<=num;j++){
			var tr="<tr data-jobtype="+jobType+"_"+j+">";
			tr+="<td rowspan='"+nodeName.length+"'>"+j+"</td>";//序号
			tr+="<td rowspan='"+nodeName.length+"' td-type='process'>"+processOptList+"</td>";//流程选择
			tr+="<td rowspan='"+nodeName.length+"' td-type='taskTime'><input name='' type='text'/></td>";//任务分配时间
			if(nodeName.length==0){
				tr+="<td>&nbsp;</td>";
				tr+="<td>&nbsp;</td>";
			}else{
				for(var i=0;i<nodeName.length;i++){
					tr+="<td td-type='nodeName' node-id='"+nodeId[i]+"'>"+nodeName[i]+"</td>";//节点名称
					tr+="<td td-type='nodeTime'><input name='"+nodeId[i]+"' type='text'/></td>";//标准时间
					tr+="</tr>";
					if(i!=nodeName.length-1){
						tr+="<tr>";
					}
				}
			}
			jobTypeTable.append($(tr));
		}
		//saveCache();
	}else{
		showWarnMsg("此作业类型没有配置作业流程！");
		return;
	}
}
/**
 * 删除出表头外所有行
 * @param tabObj
 */
function removeRows(){
	$("#jobTypeTable tr:not(:eq(0))").remove();
}
/**
 * 暂存信息
 */
function saveCache(type){
	var jobTypeObj={};
	var jobType="";//作业类型
	var nodeArray=[];//节点数组
	var trs=$("#jobTypeTable tr");
	if(trs){
		for(var i=1;i<trs.length;i++){
			var tr=$(trs[i]);
			if(tr.attr("data-jobtype")){
				var temp=tr.attr("data-jobtype");
				if(jobType!=temp){
					jobType=temp;
					if(!$.isEmptyObject(jobTypeObj)){
						jobTypeObj.nodeArray=nodeArray;
						finalDataArray.push(jobTypeObj);
					}
					jobTypeObj={};
					nodeArray=[];
					jobTypeObj.jobSeq=jobType.split("_")[1];
					jobTypeObj.jobType=jobType.split("_")[0];
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
							break;
						case "nodeName":
							var nodeName=$(v).text();//节点名称
							var nodeId=$(v).attr("node-id");//节点ID
							var nodeObj={};
							nodeObj.nodeName=nodeName;
							nodeObj.nodeId=nodeId;
							var nextNode=$(v).next();
							if(nextNode.attr("td-type")=="nodeTime"){
								var nodeTime=nextNode.children(0).val();
								nodeObj.nodeTime=nodeTime;//标准时间
							}
							nodeObj.nodeSeq=nodeArray.length+1;
							nodeArray.push(nodeObj);
							break;
					}
				}
			})
		}
		jobTypeObj.nodeArray=nodeArray;
		finalDataArray.push(jobTypeObj);
	}
	$("#"+type+"NumSelect").attr("disabled","disabled");
	$("#"+type+"SaveBtn").attr("disabled","disabled");
	//alert(JSON.stringify(finalDataArray));
}

function judgeIfHaveCache(jobType){
	var res=false;
	if(finalDataArray){
		var temp=finalDataArray.concat();//采用连接一个空的数组的方式复制数组
		$.each(temp,function(k,v){
			if(v.jobType==jobType){
				res=true;
			}
		})
	}
	return res;
}
/**
 * 清理缓存
 * @param jobType
 */
function cleanCache(jobType){
	var temp=[];
	if(finalDataArray){
		//var temp=finalDataArray.concat();//采用连接一个空的数组的方式复制数组
		$.each(finalDataArray,function(k,v){
			if(v.jobType!=jobType){
				temp.push(v);
			}
		})
	}
	finalDataArray=temp;
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
	
	var saveBtn=$("button[name='saveBtn']");
	$.each(saveBtn,function(k,v){
		var _v=$(v);
		if(_v.attr("id")!=(type+"SaveBtn")){
			_v.attr("disabled","disabled");
		}else{
			_v.removeAttr("disabled");
		}
	})
}
/**
 * 点击表格行事件
 * @param jobType
 * @param obj
 */
function doChangeJobType(type,obj){
	setBtnDisable(type);
	
	$(".clickTr").removeClass("clickTr");
	$(obj).parent().parent().addClass("clickTr");//增加底色
	var jobTypeTable=$("#jobTypeTable");
	removeRows();//清除列信息
	$.each(finalDataArray,function(k,v){
		if(v.jobType==type){
			var jobType=v.jobType;
			var jobSeq=v.jobSeq;
			var processOption=v.processOption;
			var nodeArray=v.nodeArray;
			var tr="<tr data-jobtype="+jobType+"_"+jobSeq+">";
			tr+="<td rowspan='"+nodeArray.length+"'>"+jobSeq+"</td>";//序号
			tr+="<td rowspan='"+nodeArray.length+"' td-type='process'>"+processOption+"</td>";//流程选择
			tr+="<td rowspan='"+nodeArray.length+"' td-type='taskTime'><input name='' type='text' value='"+v.taskTime+"'/></td>";//任务分配时间
			if(nodeArray.length==0){
				tr+="<td>&nbsp;</td>";
				tr+="<td>&nbsp;</td>";
			}else{
				for(var i=0;i<nodeArray.length;i++){
					tr+="<td td-type='nodeName' node-id='"+nodeArray[i].nodeId+"'>"+nodeArray[i].nodeName+"</td>";//节点名称
					tr+="<td td-type='nodeTime'><input name='"+nodeArray[i].nodeId+"' type='text' value='"+nodeArray[i].nodeTime+"'/></td>";//标准时间
					tr+="</tr>";
					if(i!=nodeArray.length-1){
						tr+="<tr>";
					}
				}
			}
			jobTypeTable.append($(tr));
		}
	})
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