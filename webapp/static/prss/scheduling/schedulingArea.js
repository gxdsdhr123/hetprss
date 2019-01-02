var clickRow;
var layer;
var iframe;
$(function(){
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
		var form = layui.form;
		/*form.on('checkbox(checkBoxes)', function(data){
			
		});  */   
	})
	var tableOptions = {
			url : ctx + "/scheduling/gantt/getResGanttArea", // 请求后台的URL（*）
			method : "get", // 请求方式（*）
			dataType : "json", // 返回结果格式
			striped : true, // 是否显示行间隔色
			pagination : false, // 是否显示分页（*）
			cache : false, // 是否启用缓存
			undefinedText : '', // undefined时显示文本
			//checkboxHeader : false, // 是否显示全选
			toolbar : $("#tool-box"), // 指定工具栏dom
			search : true,
			//searchOnEnterKey : true,
			onClickRow : function(row, tr, field) {
				clickRow = row;
				$(".clickRow").removeClass("clickRow");
				$(tr).addClass("clickRow");
			},
			columns : [
				{
					field : 'order',
					title : '序号',
					sortable : false,
					width : 44,
					formatter : function(value, row, index) {
						return index + 1;
					}
				},{
					field : 'id',
					visible : false
				},{
					field : 'name',
					title : '名称',
					sortable : true,
				},{
					field : 'attr',
					title : '属性',
					sortable : true,
				},{
					field : 'createUser',
					title : '创建人',
					sortable : true,
				},{
					field : 'createTime',
					title : '创建时间',
					sortable : true,
				}
			]
	}
	$("#areaTable").bootstrapTable(tableOptions);
	//增加
	$("#create").click(function(){
		iframe = layer.open({
			type:2,
			title:"增加",
			area:["800px","450px"],
			content:ctx+"/scheduling/gantt/listResOperationArea",
			btn:["保存","返回"],
			yes:function(index,layero){
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.save();
			}
		})
	})
	//修改
	$("#modify").click(function(){
		if(!clickRow || clickRow=="" || typeof(clickRow)=="undefined"){
			layer.msg("请选择一条记录",{icon:7});
			return false;
		}
		iframe = layer.open({
			type:2,
			title:"修改",
			area:["800px","450px"],
			content:ctx+"/scheduling/gantt/listResOperationArea?id="+clickRow.id+"&name="+clickRow.name+"&type="+(clickRow.attr=="机位"?"0":"1"),
			btn:["保存","返回"],
			yes:function(index,layero){
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.save(clickRow.id);
			}
		})
	})
	//删除
	$("#delete").click(function(){
		layer.confirm('确定要删除该保障区域吗', {icon: 3, title:'提示'}, function(index){
			var loading = layer.load(2);
			  $.ajax({
				  type:'post',
				  url : ctx+"/scheduling/gantt/deleteResArea",
				  data:{
					  id:clickRow.id
				  },
				  error:function(){
					  layer.close(loading);
					  layer.msg("请求发送失败",{icon:2,time:1000},function(){
							closeLayer();
						});
				  },
				  success:function(msg){
					  layer.close(loading);
					  if(msg == "success"){
							layer.msg("删除成功！",{icon:1,time:1000},function(){
								closeLayer();
							});
						}else{
							layer.msg("删除失败",{icon:2,time:1000});
							console.error(msg);
						}
				  }
			  });
			  layer.close(index);
			});  
	})
})
function closeLayer(){
	layer.close(iframe);
	$("#areaTable").bootstrapTable('refresh');
}