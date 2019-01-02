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
});

function getProcessByJobTypeInit(jobType,procId,sortNum){
	if(jobType!=null){
		$.ajax({
			type : 'post',
			url : ctx+ "/rule/waterFillRule/getProcessByJobType",
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
				tr+="<td rowspan='"+nodeName.length+"' td-type='taskTime'><input name='"+jobType+"TaskTime"+j+"' type='text' onclick='showGuide(this)'/><input name='"+jobType+"TaskTimeVar"+j+"' type='hidden'/></td>";//任务分配时间
				if(nodeName.length==0){
					tr+="<td>&nbsp;</td>";
					tr+="<td>&nbsp;</td>";
				}else{
					for(var i=0;i<nodeName.length;i++){
						tr+="<td td-type='nodeName' id='"+nodeId[i]+j+"TD' node-id='"+nodeId[i]+"' >"+nodeName[i]+"</td>";//节点名称
						tr+="<td td-type='nodeTime'><input name='"+nodeId[i]+"Time"+j+"' type='text' onclick='showGuide(this)'/><input name='"+nodeId[i]+"TimeVar"+j+"' type='hidden'/></td>";//标准时间
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
	if($("#addedWater").val()==""){
		showWarnMsg("请输入加水量！");
		return false ;
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
	return JSON.stringify(paramsJsonObj);
	
}
function formatRepo(repo) {
	return repo.text;
}
// select2显示选项处理
function formatRepoSelection(repo) {
	return repo.text;
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
		content : ctx+ "/rule/waterFillRule/guide?varId="+varId+"&express="+encodeURIComponent(express),
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
	 var rep=/\[\W+\]/i
	 var exp=mtext.replace(rep,10);
	 try{
		 eval(exp);
		 return true;
	 }catch(e){
		 return false;
	 }
}
