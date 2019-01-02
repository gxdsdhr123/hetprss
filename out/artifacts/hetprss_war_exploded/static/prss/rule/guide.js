var selectVar="";
$(function(){
	var layer;
	var clickRowId = "";

	layui.use(['form','layer'],function(){
		layer = layui.layer;
	});

	initData('');
	selectVar=$("#varId").val();
	new PerfectScrollbar('#varLists');
});
function initData(val){
	$.getJSON(ctx+"/rule/taskAllotRule/getVarList", function(data) {
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
function inpuValue(flag,type){
	var val = $("input[name=input_val]").val();
	if(val == null || val == ''){
		layer.msg("取值范围不能为空",{time:1000});
		return ;
	}
	if(flag == 'string')
		val = "'" + val + "'";
	getValue(val,flag,type);
}
//设置逻辑运算符值
function setValue (obj){
	if(selectVar!=""){
		layer.msg("只能选择一个变量",{icon : 7,time:1000});
		return ;
	}
	var no= $(obj).data("id");
	var en= $(obj).data("en");
	var name = $(obj).val();
	setParams(name,no,en);
	selectVar=no;
}
//设置可编辑div值
function setParams(name,no,en){//name,no,ischg
	var div_id = no;
	var schema = $("#schema").val();
	var div_name = name;
	var html = "<div class='div_class select' data-id="+div_id+" data-en=" + en + ">["+div_name+"]</div>";
	insertNode(html);
}

//向导运算获取值方法
function getValue(val,flag,type) {
	var div_id = '';
	var html = "<span class='span_class select' data-flag="+flag+" data-type="+type+">"+val+"</span>";
	insertNode(html);
}


function insertNode(html){
	var oWin = $(html);
	var selectNode = $(".node_select");
	if(selectNode.length>0){
		selectNode.after(oWin);
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
		if(prev.length>0){
			removeSelect();
			nodeSelect(prev);
		} else {
			var next = $(obj).next();
			if(next.length>0){
				removeSelect();
				nodeSelect(next);
			}
		}
		if($(obj).data("id")){
			selectVar="";
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
			if(text != null && text != ''&&$(row).data("id")){
				ids += $(row).data("id") + ",";
			}
		})
		if(ids.length>0) ids = ids.substring(0,ids.length-1);
    }
	return ids;
}
