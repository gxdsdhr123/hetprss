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
	myCanvas = $("#myCanvas")[0];  
	cxt =  myCanvas.getContext("2d"); 
	$("#myCanvas").css("top",$('.leri').offset().top);
	$("#myCanvas").css("left",$('.leri').offset().left);
	$("#myCanvas").css("width",$('.leri').outerWidth());
	$("#myCanvas").css("height",$('.leri').outerHeight()*3);
	myCanvas.width = $('.leri').outerWidth();
	myCanvas.height = $('.leri').outerHeight()*3;
//	cxt.fillStyle="#002034";
//	cxt.fillRect(0,0,$('.leri').outerWidth(),$('.leri').outerHeight()*3);
	cxt.strokeStyle = '#23527c';  
	cxt.beginPath(); 
	cxt.moveTo(0,0);  
	cxt.lineTo(myCanvas.width,myCanvas.height/3*2);
	cxt.stroke();
	cxt.save();
	cxt.beginPath(); 
	cxt.moveTo(0,0);  
	cxt.lineTo(myCanvas.width/4*3,myCanvas.height);
	cxt.stroke();
	cxt.save();
	
	serachDate();
	
});

function serachDate(){
	if(checkSearch()){
		$.ajax({
			type: "POST",
			url: ctx + '/statisticalanalysis/fltWeekly/dataList',
		    data:{
		    	startDate:startDate,
		    	endDate:endDate
		    },
		    dataType:"json",
		     success : function(data) {
		    	 $(".appendData").empty();
		    	 var sfdata=data.sfData;
		    	 var jcdata=data.jcData;
		    	 $.each(data.weeklyData, function (n, value) {
//		    		$("#weeklyDataArea").after("<tr class='appendData'><td>"+
		    		 $(".weeklyDataArea:last").after("<tr class='appendData weeklyDataArea'><td>"+
		    				value.DAY_OF_WEEK+"</td><td>"+
		    				value.FLT_TYPE0+"</td><td>"+
		    				value.FLT_TYPE1+"</td><td>"+
		    				value.FLT_TYPE2+"</td><td>"+
		    				value.FLT_TYPE3+"</td><td>"+
		    				value.FLT_TYPE4+"</td><td>"+
		    				value.FLT_TYPE5+"</td><td>"+
		    				value.FLT_TYPE6+"</td><td>"+
		    				value.FLT_TYPE7+"</td><td>"+
		    				value.FLT_TYPE8+"</td><td>"+
		    				value.FLT_TYPE9+"</td><td>"+
		    				value.FLT_TYPE10+"</td><td>"+
		    				value.FLT_TYPE11+"</td><td>"+
		    				value.FLT_TYPE12+"</td><td>"+
		    				value.FLT_TYPE13+"</td></tr>");
		    	 });
		    	 $("#sfDataArea").after("<tr class='appendData'><td>"+
		    			 "架次"+"</td><td>"+
		    			 sfdata.UNNORMAL_FLT_NUM1+"</td><td>"+
		    			 sfdata.UNNORMAL_FLT_NUM2+"</td><td>"+
		    			 sfdata.UNNORMAL_FLT_NUM3+"</td><td>"+
		    			 sfdata.UNNORMAL_FLT_NUM4+"</td><td>"+
		    			 
		    			 sfdata.UNNORMAL_FLT_NUM5+"</td><td>"+
		    			 sfdata.UNNORMAL_FLT_NUM6+"</td><td>"+
		    			 sfdata.UNNORMAL_FLT_NUM7+"</td><td>"+
		    			 sfdata.UNNORMAL_FLT_NUM8+"</td><td>"+
		    			 sfdata.UNNORMAL_FLT_NUM9+"</td><td>"+
		    			 
		    			 sfdata.UNNORMAL_FLT_NUM10+"</td><td>"+
		    			 sfdata.UNNORMAL_FLT_NUM11+"</td><td>"+
		    			 sfdata.UNNORMAL_FLT_NUM12+"</td><td>"+
		    			 sfdata.UNNORMAL_COUNT+"</td><td></td></tr>");
		    	 $("#jcDataArea").after("<tr class='appendData'><td>"+
		    			 "架次"+"</td><td>"+
		    			 jcdata.UNNORMAL_FLT_NUM1+"</td><td>"+
		    			 jcdata.UNNORMAL_FLT_NUM2+"</td><td>"+
		    			 jcdata.UNNORMAL_FLT_NUM3+"</td><td>"+
		    			 jcdata.UNNORMAL_FLT_NUM4+"</td><td>"+
		    			 
		    			 jcdata.UNNORMAL_FLT_NUM5+"</td><td>"+
		    			 jcdata.UNNORMAL_FLT_NUM6+"</td><td>"+
		    			 jcdata.UNNORMAL_FLT_NUM7+"</td><td>"+
		    			 jcdata.UNNORMAL_FLT_NUM8+"</td><td>"+
		    			 jcdata.UNNORMAL_FLT_NUM9+"</td><td>"+
		    			 
		    			 jcdata.UNNORMAL_FLT_NUM10+"</td><td>"+
		    			 jcdata.UNNORMAL_FLT_NUM11+"</td><td>"+
		    			 jcdata.UNNORMAL_FLT_NUM12+"</td><td>"+
		    			 jcdata.UNNORMAL_COUNT+"</td><td></td></tr>");
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

//canvas表格斜线  
function draw(){  
	$("#myCanvas").css("top",$('.leri').offset().top);
	$("#myCanvas").css("left",$('.leri').offset().left);
	$("#myCanvas").css("width",$('.leri').outerWidth());
	$("#myCanvas").css("height",$('.leri').outerHeight()*3);
	myCanvas.width = $('.leri').outerWidth();
	myCanvas.height = $('.leri').outerHeight()*3;
	
	cxt.beginPath(); 
	cxt.moveTo(0,0);  
	cxt.lineTo(myCanvas.width,myCanvas.height/3*2);
	cxt.strokeStyle = '#23527c';  
	cxt.stroke();
	cxt.save();
	cxt.beginPath(); 
	cxt.moveTo(0,0);  
	cxt.lineTo(myCanvas.width/4*3,myCanvas.height);
	cxt.stroke();
	cxt.save();
}  
$(window).resize(function() {//当窗口大小发生变化时，重新画线  
	cxt.clearRect(0,0,500,500);//清空画布  
    draw();  
})  




