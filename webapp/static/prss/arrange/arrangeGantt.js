var layer;// 初始化layer模块
var iframe;
$(function(){
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})
	var ganttOptions = {
		url:ctx+"/arrange/gantt/ganttData",
		showEmptyRow:true,
		height:$("body").height()-$("#tool-box").height()+5,
		yData:{
			data:[{"id":"DP","name":"待排"}]
		}
	}
	$("#SJgantt").SJgantt(ganttOptions);
	//刷新
	$("#refresh").click(function() {
		$("#SJgantt").SJgantt('refresh');
	})
	// 筛选
	$("#filter").click(function() {
		var filter = {};
		layer.open({
			type:1,
			title : '筛选',
			area : ['600px','180px'],
			content : $("#filterBox"),
			btn:[ "确定", "取消" ],
			yes : function(index, layero) {
				ganttOptions.queryParams = {
						terminal:$("#terminal").val(),
						week:$("#week").val()
				}
				$("#SJgantt").SJgantt('refreshOptions',ganttOptions);
				layer.close(index);
			}
		});
	});
	$("#setGroup").click(function(){
		layer.open({
			title : '设置分组',
			content : '设置分组数<input type="number" min="1" class="layui-input layui-sm" style="width:200px;margin:0px 10px;display:inline-block"/>',
			yes : function(index, layero) {
				var groupNumber = layero.find("input").val();
				ganttOptions.yData.data = [];
				ganttOptions.yData.data.push({"id":"DP","name":"待排"});
				for(var i=1;i<=groupNumber;i++){
					var y = {"id":i+"","name":"分组"+numberConvertToUppercase(i)};
					ganttOptions.yData.data.push(y);
				}
				$("#SJgantt").SJgantt('refreshOptions',ganttOptions);
				layer.close(index);
			}
		});
	});
});
function numberConvertToUppercase(num) {
	num = Number(num);
    var upperCaseNumber = ['零', '一', '二', '三', '四', '五', '六', '七', '八', '九', '十', '百', '千', '万', '亿'];
    var length = String(num).length;
    if (length == 1) {
      return upperCaseNumber[num];
    } else if (length == 2) {
      if (num == 10) {
        return upperCaseNumber[num];
      } else if (num > 10 && num < 20) {
        return '十' + upperCaseNumber[String(num).charAt(1)];
      } else {
        return upperCaseNumber[String(num).charAt(0)] + '十' + upperCaseNumber[String(num).charAt(1)].replace('零', '');
      }
    }
  }