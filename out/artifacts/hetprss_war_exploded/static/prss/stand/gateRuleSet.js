$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	});
	$(".sortable").sortable({
		connectWith: ".sortable",
		stop: function( event, ui ) {
			setTimeout(function(){
				ui.item.removeClass("bechoose");
			},10)
		}
	});
	initFunc4();
	init();
	//登机口搜素
	$("#unfixedSearch").keyup(function(event) {
		if (event.keyCode == 13) {
			applyUnfixed();
		}
	});
	//设置滚动条--begin
	var scroll = $("#mtext");
	scroll.css("position", "relative");
	new PerfectScrollbar(scroll[0]);
	
	var body = $("#createForm");
	body.css("position", "relative");
	new PerfectScrollbar(body[0]);
	initClick();
	nodeSelectLast();
	//设置滚动条--end
});

function applyUnfixed(){
	var unfixedName = $("#unfixedSearch").val();
	$("#allUnfixedUL li").each(function(index,item){
		var code = $(item).data("code")+"";
		if(code.indexOf(unfixedName)<0){
			$(item).hide();
		} else {
			$(item).show();
		}
	});
}

//设置输入
function inpuValue(obj,flag,type){
	var val = $("input[name=input_val]").val();
	if(flag =='int' && (val == null || val == '')){
		layer.msg("取值范围不能为空",{time:1000});
		return ;
	}
	if(flag == 'string')
		val = "'" + val + "'";
	getValue(obj,val,flag,type);
}
//设置逻辑运算符值
function setValue (obj){
	var no= $(obj).data("id");
	var en= $(obj).data("en");
	var name = $(obj).val();
	var selectNode = $(".node_select");
	setParams(obj,name,no,en)
}
//设置可编辑div值
function setParams(obj,name,no,en){//name,no,ischg
	var div_id = no;
	var schema = $("#schema").val();
	var div_name = name;
	var html = "<div class='div_class select' data-id="+div_id+" data-en=" + en + ">["+div_name+"]</div>";
	insertFunc(obj,insertNode,html);
}

//向导运算获取值方法
function getValue(obj,val,flag,type) {
	var div_id = '';
	var html = "<span class='span_class select' data-flag="+flag+" data-type="+type+">"+val+"</span>";
	insertFunc(obj,insertNode,html);
}

function insertFunc(obj,func,val1){
	var selectNode = $(".node_select");
	$("#leftOrRight").hide();
	if(selectNode.length>0 && !selectNode.prev().hasClass('select')){
		$("#leftOrRight").hide();
		var left = $(obj).offset().left;
		var top = $(obj).offset().top + $(obj).height();
		$("#leftOrRight ul").css({
			'position' : "absolute",
			'left' : left,
			'top' : top
			});
		$("#leftOrRight li").removeClass("selected");
		$("#leftOrRight li").removeClass("bechoose");
		$("#leftOrRight").show();
		$("#leftOrRight li").unbind('click');
		$("#leftOrRight li").bind('click',function(){
			$(this).addClass("selected");
			func(val1);
			$("#leftOrRight").hide();
		})
	} else {
		$("#leftOrRight li").removeClass("selected");
		$("#leftOrRight li").removeClass("bechoose");
		func(val1);
	}
}

function insertNode(html){
	var oWin = $(html);
	var selectNode = $(".node_select");
	if(selectNode.length>0){
		var str = $("#leftOrRight li").attr("class");
		if(str.indexOf("selected")>-1){
			selectNode.before(oWin);
		} else {
			selectNode.after(oWin);
		}
		removeSelect();
	} else {
		var mtext = $("#mtext");
		mtext.append(oWin);
	}
	nodeSelect(oWin);
	initClick();
}
function nodeSelect(oWin){
	oWin.addClass('node_select');
}

function nodeSelectLast(){
	$('.select').last().addClass('node_select');
}
function removeSelect(){
	$(".node_select").removeClass("node_select");
}

function initClick(){
	$(".select").bind('click',function(e){
		removeSelect();
		nodeSelect($(this))
	});
}
document.oncontextmenu=function(e){ 
	var obj = e.target||window.event.srcElement;
	var className = $(obj).hasClass('select')
	if(className){
		var prev = $(obj).prev();
		if(prev.length>0 && prev.hasClass('select')){
			removeSelect();
			nodeSelect(prev);
		} else {
			var next = $(obj).next();
			if(next.length>0 && prev.hasClass('select')){
				removeSelect();
				nodeSelect(next);
			}
		}
		$(obj).remove();
		return false; 
	} 
}

function getDrlRule(){
	var drl = '';
	var rel = '-';

    var mtext = $("#mtext").text();
    if(mtext!=null && mtext != ''){
		$("#mtext").children().each(function(index,e){
			var tag_name = $(e).get(0).tagName ;
			var text = $(e).text();
			if(text != null && text != ''){
				if(tag_name == 'DIV'){
					var id = $(e).data("id");
					var en = $(e).data("en");
					if(id && en)
						drl += 'attr' + rel + id + rel + en + '|';
				} else {
					var flag = $(e).data("flag");
					var type = $(e).data("type");
					if(type == 'val'){
						if(type && flag && text)
							drl += type + rel + flag + rel + text + '|';
					} else {
						if(type && flag)
							drl += type + rel + flag + '|';
					}
				}
			}
		});
		if(drl != '')
			drl = drl.substring(0,drl.length-1);
    }
	return drl;
}

function getvarcols(){
	var ids = '';
    var mtext = $("#mtext").text();
    if(mtext!=null && mtext != ''){
		$(".div_class").each(function(index,row){
			var text = $(row).text();
			if(text != null && text != ''){
				ids += $(row).data("id") + ",";
			}
		})
		if(ids.length>0) ids = ids.substring(0,ids.length-1);
    }
	return ids;
}
function openCopy(){
	layui.use(['form','element']);
	var createIframe = parent.layer.open({
	  type: 2, 
	  title:'复制规则',
	  maxmin:false,
	  shadeClose: true,
	  area: ["650px","400px"],
	  content: ctx + "/param/common/toRuleList"
	});
}
function setRule(id){
	$.ajax({
		type : 'post',
		url : ctx + "/param/common/getRule",
		data : {
			id : id
		},
		async : false,
		dataType : 'text',
		success : function(result) {
			var mtext = $("#mtext");
			mtext.html("");
			mtext.append(result);
			initClick();
			nodeSelectLast();
		}
	});
}

function init(){
	$("li").unbind("click");
	$("li").click(function(){
		var obj = $(this);
		if(obj.hasClass("bechoose")){
			obj.removeClass("bechoose");
		}else{
			obj.addClass("bechoose");
		}
	});
}

function initFunc4(){
	$("#pushleft3").click(function(){
		var bechoose = $("#rightDiv4 .bechoose");
		bechoose.removeClass("bechoose");
		$("#leftDiv4 .list-group").append(bechoose);
	});
	$("#pushright3").click(function(){
		var bechoose = $("#leftDiv4 .bechoose");
		bechoose.removeClass("bechoose");
		$("#rightDiv4 .list-group").append(bechoose);
	});
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
	return true;
}
function getSubmitData() {
	var paramsJsonObj = JSON.parse($("#createForm").form2json());
	
	var mtext = $("#mtext").text();
	var expression = getDrlRule();
	var rule_colids = getvarcols();
	$.ajax({
		type : 'post',
		url : ctx + "/rule/taskAllotRule/createDrl",
		data : {
			expression : expression
		},
		async : false,
		dataType : 'json',
		success : function(data) {
			if(data.code == 0){
				var drl = data.result;
				paramsJsonObj['condition'] = mtext;
				paramsJsonObj['expression'] = expression;
				paramsJsonObj['colids'] = rule_colids;
				paramsJsonObj['drools'] = drl;
				//登机口
				var gateArr=[];
				$.each($("#selUnfixedUL li"),function(k,v){
					var gateStr = $(v).data("code");
					gateArr.push(gateStr.split(";")[0]);
				})
				paramsJsonObj['gateStr'] = gateArr.join(",");
			}else{
				layer.msg(data.msg,{icon: 2,time: 1000});
			}
		}
	});
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

function addParam(){
	layer.open({
		type : 2,
		title : '编辑表达式',
		content : ctx + "/stand/gate/openParam?schemaId=4",
		btn : [ "确定",  "取消" ],
		area : [ '800px', '450px' ],
		yes : function(index, layero) {
			var iframeWin = window[layero.find('iframe')[0]['name']];
			var submitData = iframeWin.getSubmitData();
			var loading = layer.load(2, {shade : [ 0.1, '#000' ]});
			$.ajax({
				type : 'post',
				url : ctx + "/stand/gate/saveParam?schemaId=4",
				data : {
					data : submitData
				},
				async : true,
				dataType : 'json',
				success : function(data) {
					layer.close(loading);
					if (data.code == 0) {
						layer.msg(data.msg, {
							icon : 1
						});
						initParam(data.paramList);
						layer.close(index);
					} else {
						layer.msg(data.msg, {
							icon : 2
						});
					}
				}
			});
		},
		btn2 : function(index, layero) {// 重置表单及筛选条件
			layer.close(index);
		}
	})
}

function initParam(list){
	$("#params").empty();
	$.each(list,function(index,item){
		$("#params").append('<input type="button" value="'+item.title +'" class="layui-btn layui-btn-small layui-btn-normal" '+
				'style="width: 120px;" data-en="'+item.title +'" data-id="'+item.id +'"onclick="setValue(this)"></input> ');
	})
}
