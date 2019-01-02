$(function(){
	var layer;
	var clickRowId = "";

	layui.use(['form','layer'],function(){
		layer = layui.layer;
	});

	$('#param_val').bind('keydown',function(event){
	    if(event.keyCode == "13") {
	    	var val = $("#param_val").val();
	    	debugger;
	    	initData(val);
	    }
	});
	initData('');
	new PerfectScrollbar('#varLists');
	
	//设置滚动条--begin
	var scroll = $("#mtext");
	scroll.css("position", "relative");
	new PerfectScrollbar(scroll[0]);
	//设置滚动条--end
});

function initData(val){
	$.getJSON(ctx+"/param/common/getVariableData?text="+encodeURIComponent(encodeURIComponent(val)), function(data) {
		if(data){
			$("#varLists").empty();
			var varLists = $("#varLists");
			$(data).each(function(index,row){
				var html = '<input type="button" value="'+row.name+'" style="text-align:left" data-en="'+row.en+'" data-id="'+row.no+'" onclick="setValue(this)"></input>';
				varLists.append(html);
			});
			initClick();
			nodeSelectLast();
		}
	}).success(function() {
	})
    .error(function() { 
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
	setParams(obj,name,no,en);
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
			if(next.length>0){
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