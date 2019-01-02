var layer;// 初始化layer模块
var startDate="";
var endDate="";
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})
	//查询功能
	$(".search").click(function() {
		serachDate();
	})
	//打印功能
	$(".print").click(function() {
		$("#printExcel").submit();
	})
	serachDate();
});

function serachDate(){
	if(checkSearch()){
		$.ajax({
			type: "POST",
			url: ctx + '/statisticalanalysis/fltNomalFx/dataList',
		    data:{
		    	startDate:startDate,
		    	endDate:endDate,
		    	type:$("#type").val()
		    },
//		    dataType:"json",
		     success : function(data) {
		    	 $(".dataArea").html("");
		    		 $("#planFltNum").html(data.PLAN_FLT_NUM);
			    	 $("#normalFltNum").html(data.NORMAL_FLT_NUM);
			    	 $("#unnormalFltNum").html(data.UNNORMAL_FLT_NUM0);
			    	 $("#normalFx").html(data.NORMAL_FX);
			    	 $("#unnormalFltNum1").html(data.UNNORMAL_FLT_NUM1);
			    	 $("#unnormalFltNum2").html(data.UNNORMAL_FLT_NUM2);
			    	 $("#unnormalFltNum3").html(data.UNNORMAL_FLT_NUM3);
			    	 $("#unnormalFltNum4").html(data.UNNORMAL_FLT_NUM4);
			    	 $("#unnormalFltNum5").html(data.UNNORMAL_FLT_NUM5);
			    	 $("#unnormalFltNum6").html(data.UNNORMAL_FLT_NUM6);
			    	 $("#unnormalFltNum7").html(data.UNNORMAL_FLT_NUM7);
			    	 $("#unnormalFltNum8").html(data.UNNORMAL_FLT_NUM8);
			    	 $("#unnormalFltNum9").html(data.UNNORMAL_FLT_NUM9);
			    	 $("#unnormalFltNum10").html(data.UNNORMAL_FLT_NUM10);
			    	 $("#unnormalFltNum11").html(data.UNNORMAL_FLT_NUM11);
			    	 $("#unnormalFltNum12").html(data.UNNORMAL_FLT_NUM12);
			    	 $("#yoy").html(data.YOY);
			    	 $("#mom").html(data.MOM);
		    	
		     }
		});
	}

}
function checkSearch(){
	startDate=$("#startDate").val();
	endDate=$("#endDate").val();
	//如果开始时间和结束只填写了一个
	if((startDate==""&&endDate!="")||startDate!=""&&endDate==""){
		 layer.msg("时间必须同时填写或同时为空！", {
			    icon : 7
			   });
		return false;
	}else{
		return true;
	}
}




