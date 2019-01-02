var layer;
var form;
var saved = true;
var oDim = "D";
var oIsland = "A";
var mode = "DI";
var currentRow;
var id = 1;
var delArray = [];
$(function(){
	
	layui.use(['layer','element',"form"],function(){
		layer = layui.layer;
		form = layui.form;
		//国内/国际下拉框变更事件
		form.on('select(fltAttrCode)',function (data) {
			if(!saved){
				layer.confirm("是否保存已修改内容？",{btn:["是","否","取消"],
					yes:function(){
						save(doChange);
					},
					btn2:function(){
						doChange();
					}});
			}else{
				doChange();
			}
	    });
		//值机岛下拉框变更事件
		form.on('select(counterIsland)',function (data) {
			if(!saved){
				layer.confirm("是否保存已修改内容？",{btn:["是","否","取消"],
					yes:function(){
						save(doChange);
					},
					btn2:function(){
						doChange();
					}});
			}else{
				doChange();
			}
	    });
		if($("#fltAttrCode").val() == "M"){
			doChange();
		}
	});
	jQuery.fn.bootstrapTable.columnDefaults.sortable = false;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	$(".sortable").css("position","relative");
	$(".sortable").each(function(){
		scrollArr.push(new PerfectScrollbar(this));
	});
	
	//国内/国际下拉框
	/*$("#fltAttrCode").on('change',function(){
		//doChange();
		console.log('国内/国际下拉框');
	});*/
	
	
	
	/*$("#fltAttrCode").change(function(){
		alert('tyl');
	});*/

	
	//值机岛
	/*$("#counterIsland").on('change',function(){
		console.log('值机岛');
		//doChange();
	});*/
	
	$("#pushleft").click(function(){
		var bechoose = $("#rightDiv .bechoose");
		bechoose.removeClass("bechoose");
		$("#leftDiv .list-group").prepend(bechoose);
		saved = false;
	});
	
	
	$("#pushright").click(function(){
		var bechoose = $("#leftDiv .bechoose");
		bechoose.removeClass("bechoose");
		$("#rightDiv .list-group").prepend(bechoose);
		saved = false;
	});
	$(".chooseAll").click(function(){
		if($(this).text() == "全选"){
			$(this).parent().next().find("li").addClass("bechoose");
			$(this).text("全不选");
		}else{
			$(this).parent().next().find("li").removeClass("bechoose");
			$(this).text("全选");
		}
	});
	$("#ok").click(function(){
		if(!saved){
			layer.confirm("是否保存已修改内容？",{btn:["是","否","取消"],
				yes:function(){
					save(doChange);
				},
				btn2:function(){
					doChange();
				}});
		}else{
			doChange();
		}
	});
	$("#addRow").click(function(){
		$("#MTable").bootstrapTable('prepend',{
			id:id++,
			fltNum:"",
			counter1:"",
			counter2:"",
			counter3:"",
			newRow:true
		});
		saved = false;
	});
	$("#delRow").click(function(){
		if(!currentRow){
			layer.msg("请先选择要删除的记录",{icon:7});
			return false;
		}
		$("#MTable").bootstrapTable('remove',{field:'id',values:[currentRow.id]});
		if(!currentRow.newRow){
			delArray.push(currentRow.fltNum);
		}
		saved = false;
	});
	$("#reset").click(function(){
		$("#MTable").bootstrapTable('refresh');
		saved = true;
	});
	
	
	
});
var sorting = false;
var holdDown = 0;

function liClick(obj) {
	if($(obj).hasClass("bechoose")){
		$(obj).removeClass("bechoose");
	}else{
		$(obj).addClass("bechoose");
	}
}
function liDown(obj) {
	holdDown = 1;
	if($(obj).hasClass("bechoose")){
		$(obj).removeClass("bechoose");
	}else{
		$(obj).addClass("bechoose");
	}
}
$(document).mouseup(function(){
	holdDown = 0;
});
function liEnter(obj) {
	if (holdDown == 1) {
		if(!sorting){
			if (!$(obj).hasClass("bechoose")) {
				$(obj).addClass("bechoose");
			}else{
				$(obj).removeClass("bechoose");
			}
		}
	}
}
function search(e){
	var value = e.target.value.toUpperCase().trim();
	$(e.target).parent().parent().parent().find("li").each(function(){
		if($(this).text().indexOf(value)==-1 && $(this).data("aln3code").indexOf(value)==-1 && $(this).data("enname").indexOf(value)==-1 && $(this).data("name").indexOf(value)==-1){
			$(this).hide();
		}else{
			$(this).show();
		}
	})
	for(var o in scrollArr){
		scrollArr[o].update();
	}
}
function save(func){
	var loading = layer.load(2);
	console.log("mode:"+mode+"===dim:"+oDim +"===island:"+oIsland);
	if(mode == "DI"){
		var dim = $("#fltAttrCode").val();
		var island = $("#counterIsland").val();
		/*var dim = oDim;
		var island = oIsland;*/
		var airlines = $("#rightDiv .list-group li").map(function(){
			return $(this).text();
		}).get().join(',')||"";
		var loading = layer.load(2);
		$.ajax({
			type:'post',
			url:ctx + "/flightDynamic/saveIslandAllot",
			data:{
				dim:dim,
				island:island,
				airlines:airlines
			},
			success:function(res){
				layer.close(loading);
				if("success" == res){
					saved = true;
					layer.msg("保存成功",{icon:1},function(){
						if(func){
							func();
						}
					});
				}else{
					saved = false;
					layer.msg("保存失败",{icon:2});
					console.error(res);
				}
			}
		});
	}else if(mode == "M"){
		var data = $("#MTable").bootstrapTable('getData');
		var delStr = "'"+delArray.join("','")+"'";
		$.ajax({
			type:'post',
			url:ctx + "/flightDynamic/saveMIslandAllot",
			data:{
				data:JSON.stringify(data),
				delStr:delStr
			},
			success:function(res){
				layer.close(loading);
				if("success" == res){
					saved = true;
					layer.msg("保存成功",{icon:1},function(){
						if(func){
							func();
						}
						delArray = [];
					});
				}else{
					saved = false;
					layer.msg("保存失败",{icon:2});
					console.error(res);
				}
			}
		});
	}
}
function doChange(){
	var dim = $("#fltAttrCode").val();
	var island = $("#counterIsland").val();
	var loading = layer.load(2);
	if(dim!="M"){//国内、国际
		mode = "DI";
		$(".pageType1").show();
		$(".pageType2").hide();
		$(".pageType2btn").hide();
		$.ajax({
			type:'post',
			url:ctx + "/flightDynamic/getIslandAllot",
			data:{
				dim:dim,
				island:island
			},
			dataType:'json',
			success:function(res){
				layer.close(loading);
				oDim = dim;
				oIsland = island;
				$("#leftDiv .list-group").empty();
				for(var i=0;i<res.choose.length;i++){
					var l = res.choose[i];
					var li = $("<li class='list-group-item' onmousedown='liDown(this);' onmouseenter='liEnter(this);' data-code='"+l.code+"' data-aln3code='"+l.aln3code+"' data-enname='"+l.enname+"' data-name='"+l.name+"'>"+l.code+"</li>");
					$("#leftDiv .list-group").append(li);
				}
				$("#rightDiv .list-group").empty();
				for(var j=0;j<res.choosed.length;j++){
					var l = res.choosed[j];
					var li = $("<li class='list-group-item' onmousedown='liDown(this);' onmouseenter='liEnter(this);' data-code='"+l.code+"' data-aln3code='"+l.aln3code+"' data-enname='"+l.enname+"' data-name='"+l.name+"'>"+l.code+"</li>");
					$("#rightDiv .list-group").append(li);
				}
			}
		});
	}else{//混装
		mode = "M";
		$("#MTable").bootstrapTable('destroy');
		$(".pageType1").hide();
		$(".pageType2btn").css("display","unset");
		$(".pageType2").show();
		var tableOptions = {
				url : ctx + "/flightDynamic/getMData", // 请求后台的URL（*）
				method : "get", // 请求方式（*）
				dataType : "json", // 返回结果格式
				striped : true, // 是否显示行间隔色
				pagination : false, // 是否显示分页（*）
				cache : false, // 是否启用缓存
				undefinedText : '', // undefined时显示文本
				height:$("#tableDiv").height(),
				editable:true,
				responseHandler : function(res) {
					if(!res || res==null || res.length==0){
						res = [{
								id:1,
								fltNum:"",
								counter1:"",
								counter2:"",
								counter3:""
						}];
					}
					for(var i=0;i<res.length;i++){
						res[i].id = id++;
					}
					return res;
				},
				onClickRow : function(row, tr, field) {
					currentRow = row;
					$(".clickRow").removeClass("clickRow");
					$(tr).addClass("clickRow");
				},
				onLoadSuccess:function(){
					layer.close(loading);
				},
				onEditableSave : function(field, row, oldValue, $el) {
					var value = row[field];
					var tr = $el.parents("tr");
					var rowIndex = tr.data("index");
					saved = false;
					if(field == "fltNum"){
						var dataList = $("#MTable").bootstrapTable("getData");
						for(var i=0;i<dataList.length;i++){
							if(value.toUpperCase() == dataList[i].fltNum && row.id != dataList[i].id){
								layer.msg("该航班号已存在",{icon:7});
								value = "";
								$("#MTable").bootstrapTable('updateCell',{index:rowIndex,field:field,value:value});
								break;
							}
						}
						if(""!=value){
							$("#MTable").bootstrapTable('updateCell',{index:rowIndex,field:field,value:value.toUpperCase()});
						}
					}else{
						var c1 = row.counter1;
						var c2 = row.counter2;
						var c3 = row.counter3;
						if((c1!=""&&c2!=""&&c1==c2) || (c1!=""&&c3!=""&&c1==c3) || (c2!=""&&c3!=""&&c2==c3)){
							layer.msg("两两柜台不可相同",{icon:7});
							$("#MTable").bootstrapTable('updateCell',{index:rowIndex,field:field,value:""});
						}
					}
				},
				columns : getColumns()
			}
		$("#MTable").bootstrapTable(tableOptions);
	}
}
function getColumns(){
	var columns = [{
			field : 'order',
			title : '序号',
			sortable : false,
			editable : false,
			width : 44,
			formatter : function(value, row, index) {
				return index + 1;
			}
		},{
			title:'航班号',
			field:'fltNum',
			editable:true
		},{
			title:'值机柜台1',
			field:'counter1',
			editable:{
				type:"select2",
				source:counters,
				select2:{
					allowClear:true
				}
			}
		},{
			title:'值机柜台2',
			field:'counter2',
			editable:{
				type:"select2",
				source:counters,
				select2:{
					allowClear:true
				}
			}
		},{
			title:'值机柜台3',
			field:'counter3',
			editable:{
				type:"select2",
				source:counters,
				select2:{
					allowClear:true
				}
			}
		}
	]
	return columns;
}