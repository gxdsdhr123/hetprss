$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	});
	
//	initSelect2("flightNumber","请选择航班号，支持多选");//航班号
//	initSelect2("airplaneType","请选择注册号，支持多选");//注册号
//	//初始化默认选中值
//	$('#flightNumber').val(defaultFlightNumber).trigger('change'); 
//	$('#airplaneType').val(defaultAtcactype).trigger('change'); 
	$('input[name=aircraft]').val(defaultAtcactype.join(","));
	$('input[name=flightNumber]').val(defaultFlightNumber.join(","));
	
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
	content : ctx + "/rule/setUpRule/openCheck?type=" + type + "&selectedId=" + $("input[name='" + type + "']").val() + "&selectedText=" + $("input[name='" + type + "']").val(),
	btn : [ "确定", "清空已选", "取消" ],
	area : [ '800px', '450px' ],
	yes : function(index, layero) {
		var iframeWin = window[layero.find('iframe')[0]['name']];
		var data = iframeWin.getChooseData();
		$("input[name='" + type + "']").val(data.chooseTitle);
		$("input[name='" + type + "']").val(data.chooseValue);
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
	if($("#ruleName").val()==""){
		showWarnMsg("请输入规则名称！");
		return false ;
	}
	if( ! this.checkLength($("#ruleName").val(),20 ) ){
		showWarnMsg("规则名称不能超过20个字！");
		return false ;
	}
	if($("input[name='ruleExtInOutPort']:checked").length==0){
		showWarnMsg("请选择进出港类型！");
		return false;
	}
	if($("#demand").val()==""){
		showWarnMsg("请输入补给要求！");
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
	//进出港
	var ruleExtInOutPortObj=$("input[name='ruleExtInOutPort']:checked");
	var ruleExtInOutPort=[];
	$.each(ruleExtInOutPortObj,function(k,v){
		ruleExtInOutPort[ruleExtInOutPort.length]=$(v).val();
	})
	paramsJsonObj['ruleExtInOutPort'] = ruleExtInOutPort;
	//航班号
	var flightNumber= $("input[name=flightNumber]").val();
	var flightNumbers = flightNumber.split(',');
	paramsJsonObj['flightNumber'] = flightNumbers;
	//注册号
	var airplaneType= $("input[name=aircraft]").val();
	paramsJsonObj['airplaneType'] = airplaneType.split(',');
	//子分公司
	var branch = $("select[name=branch]").val();
	paramsJsonObj['branch'] = branch;
	
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
