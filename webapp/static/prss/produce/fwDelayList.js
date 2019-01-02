var layer;
layui.use(["layer"], function() {
	layer = layui.layer;
});

var baseTable = $("#dataTable");

$(function(){
	_search();
})

function _search(){
	
	var tableOptions = {
			url: ctx + "/bill/fwDelay/getData", 
			method: "get",
			dataType: "json",
			striped: true,
			cache: true,
			undefinedText:'',
			checkboxHeader:true,
			pagination: true,
			sidePagination: 'server',
			pageNumber: 1,
			pageSize: 10,
			pageList: [10, 15, 20],
			queryParamsType:'',
			queryParams: function (params) {
				params.startDate = $('#startDate').val();
				params.endDate = $('#endDate').val();
				params.searchText = escape($('#searchtext').val());
				
				return params;
			},
			height : $(window).height() - 70,
			columns: [
			          {
			        	  field : "rownum",
			        	  title : "序号",
			        	  align : 'center',
			        	  formatter : function(value, row, index) {
			      			return index + 1;
		      			  }
			          },
			          {
			        	  checkbox:true
			          },
			          {
			        	  field : "flightDate",
			        	  title : "航班日期",
			        	  align : 'center'
			          },
			          {
			        	  field : "flightNumber",
			        	  title : "航班号",
			        	  align : 'center'
			          },
			          {
			        	  field : "delayReason",
			        	  title : "延误原因",
			        	  align : 'center'
			          },
			          {
			        	  field : "hotel",
			        	  title : "酒店",
			        	  align : 'center',
			        	  formatter : function(value, row, index) {
			        		  var lk = row.lkHotel?row.lkHotel:'';
			        		  var jz = row.jzHotel?row.jzHotel:'';
			        		  if(lk && jz){
			        			  return lk + '，'+jz;
			        		  }else if(lk){
			        			  return lk;
			        		  }else if(jz){
			        			  return jz;
			        		  }else{
			        			  return '';
			        		  }
		      			  }
			          },
			          {
			        	  field : "producer",
			        	  title : "制作人",
			        	  align : 'center'
			          }
		          ],
	         clickToSelect:true,
	         onDblClickRow:function(row){
	        	 viewDetail(row.fltid);
		     }     
	};
	baseTable.bootstrapTable('destroy').bootstrapTable(tableOptions);// 加载基本表格
	
}

function viewDetail(fltid){
	layer.open({
		type : 2,
		title : false,
		closeBtn : false,
		area : [ '100%', '550px' ],
		content : ctx + "/bill/fwDelay/edit?fltid=" + fltid,
		btn : [ "保存","重置","返回" ],
		yes:function(index,layero){
			$(layero).find('iframe')[0].contentWindow.saveDelayInfo(index);
			return false;
		},
		btn2:function(index,layero){
			$(layero).find('iframe')[0].contentWindow.resetDelay();
			return false;
		}
	});
}

function _print(){
	var selections = baseTable.bootstrapTable('getSelections');
	if(selections.length == 0){
		layer.alert('请选择要打印的行');
		return;
	}else{
		var ids = '';
		$.each(selections,function(i,item){
			ids += ',' + item.delayInfoId;
		});
		$('#ids').val(ids.substring(1));
		$('#printForm').submit();
	}
}