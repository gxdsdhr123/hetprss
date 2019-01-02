var layer;
var form;
layui.use(['layer','element',"form"],function(){
	layer = layui.layer;
	form = layui.form;
	form.on('switch(switchColumn)', function(data) {
		switchColumn(data);
	});
})
$(document).ready(function(){
	$(".sortable").css("position","relative");
	$("#allColumns").css("position","relative");
	$("#allColumns").height($(window).height()-83);
	$("#selectedItem").height($(window).height()-83);
	new PerfectScrollbar($("#allColumns")[0]);
	new PerfectScrollbar($("#selectedItem")[0]);
	if($(".choosedField .hr").length == 0){
		$(".choosedField").prepend($("<li data-code=\"hr\" class=\"list-group-item hr ui-draggable ui-draggable-handle ui-sortable-handle\" style=\"position: relative; text-align:center; height: 42px;\">--------------------分隔线--------------------</li>"));
	}
	$(".choosedField").sortable({
		connectWith: ".choosedField",
		delay: 300,
		axis: "y",
		zIndex:1,
		placeholder: "ui-state-highlight",
		start: function( event, ui ) {
			ui.item.css({"background-color":"#006DC0"});
		},
		stop: function( event, ui ) {
			ui.item.css({"background-color":"#092955"});
		}
	});
	//设置列
	$("#settingBtn").click(function(){
		var lis = $("#selectedItem ul li");
		var data = [];
		for(var i=0;i<lis.length;i++){
			var li = $(lis[i]);
			if(li.data("code")=="hr"){
				if(data.length>0){
					data[data.length-1].class = "separator";
				}
				continue;
			}
			var row = {
					order:(i+1),
					id:li.attr("id"),
					cnname:li.data("cnname"),
					customCnname:li.data("custom"),
					width:li.data("width")
			}
			data.push(row);
		}
		$("#fieldTable").bootstrapTable('load',data);
		$("#controlDiv").hide();
		$("#settingDiv").show();
		$("#fieldTable").bootstrapTable('resetView');
	});
	//选择列
	$("#controlBtn").click(function(){
		$("#settingDiv").hide();
		$("#controlDiv").show();
	});
	//搜索
	$('#searchInput').bind('keypress', function(event) {
		if (event.keyCode == 13){
			var keyword = $(this).val();
			if(keyword){
				$("#allColumns input[name=column]").each(function(){
					var cnname = $(this).data("cnname");
					if(cnname&&cnname.indexOf($.trim(keyword))!=-1){
						$(this).parent().show();
					} else {
						$(this).parent().hide();
					}
				});
			} else {
				$("#allColumns input[name=column]").parent().show();
			}
		}
	});
	initGrid();
});
/**
 * 选择或取消选中列
 * @param data
 */
function switchColumn(data){
	var checked = data.elem.checked;
	var checkbox = $(data.elem);
	var colId = data.value;
	if(checked){//添加
		var text = checkbox.data("cnname");
		var li = $("<li class='list-group-item' id='"+colId+"'>"+text+"</li>");
		li.data("cnname",checkbox.data("cnname"));
		li.data("width",checkbox.data("width"));
		li.data("custom","");
		if(text.indexOf("进港")!=-1){
			$("#selectedItem .hr").before(li);
		}else{
			$(".choosedField ").append(li);
		}
	} else {//取消
		$("#selectedItem li[id="+colId+"]").remove();
	}
}
/**
 * 设置字段表格
 */
function initGrid(){
	var data = [];
	$("#selectedItem ul li").each(function(index,element){
		var curr = $(this);
		if(curr.data("code")!="hr"){
			var row = {
					order:(index+1),
					id:curr.attr("id"),
					cnname:curr.data("cnname"),
					customCnname:curr.data("custom"),
					width:curr.data("width")
			}
			data.push(row);
		}
	});
	$("#fieldTable").bootstrapTable({
		url: ctx+"/flightDynamic/getColumnsSetting",
		striped : true, // 是否显示行间隔色
		pagination : false, // 是否显示分页（*）
		checkboxHeader : false, // 是否显示全选
		height : $(window).height()-50,
		cache:false,
		queryParams:function(){							 //请求数据所带参数
	    	var temp = {
	    			schema:$("#schema").val()
	    	}
	    	return temp;
	    },
		columns : [ {
			field : 'order',
			title : '序号',
			width:40
		}, {
			field : 'id',
			visible : false
		},{
			field : 'cnname',
			width:100,
			title : '系统字段名称'
		}, {
			field : 'customCnname',
			title : '自定义名称',
			width:150,
			editable : true
		}, {
			field : 'width',
			title : '宽度',
			width:150,
			editable : true
		}],
		onEditableSave : function(field, row, oldValue, $el) {
			var li = $("#selectedItem li[id="+row.id+"]");
			if(field=="customCnname"){
				li.data("custom",row.customCnname);
			} else if(field=="width"){
				li.data("width",row.width);
			}
		}
	});
}

/**
 * 获取保存数据
 * @returns
 */
function getHeadData(){
	var lis = $("#selectedItem ul li");
	var data = [];
	for(var i=0;i<lis.length;i++){
		var li = $(lis[i]);
		if(li.data("code")=="hr"){
			if(data.length>0){
				data[data.length-1].class = "separator";
			}
			continue;
		}
		var row = {
				id:li.attr("id"),
				title:li.data("custom"),
				width:li.data("width"),
				colDesc:"",
				"class":""
		}
		data.push(row);
	}
	return JSON.stringify(data);
}