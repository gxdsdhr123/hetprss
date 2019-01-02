$(function(){
	var planeCanvas = $(".plane")[0];
	var planeContext = planeCanvas.getContext("2d");
	planeContext.fillStyle = "#FFFFFF";
	planeContext.beginPath();
	planeContext.moveTo(9,10);
	planeContext.lineTo(27,3);
	planeContext.lineTo(28,2);
	planeContext.lineTo(27,1);
	planeContext.lineTo(24,0);
	planeContext.lineTo(22,0);
	planeContext.lineTo(21,1);
	planeContext.lineTo(15,3);
	planeContext.lineTo(8,0);
	planeContext.lineTo(6,1);
	planeContext.lineTo(10,5);
	planeContext.lineTo(6,7);
	planeContext.lineTo(4,5);
	planeContext.lineTo(2,6);
	planeContext.lineTo(5,10);
	planeContext.lineTo(8,10);
	planeContext.fill();
	$("#SJgantt").SJgantt({
		url:ctx+"/flightDynamic/ganttSingleHisData",
		queryParams:{
			inFltid:$("#inFltid").val(),
			outFltid:$("#outFltid").val()
		},
		height:$("body").height()-$("#info-box").height(),
		yData:{
			url:ctx+"/flightDynamic/ganttSingleHisYData",
			queryParams:{
				inFltid:$("#inFltid").val(),
				outFltid:$("#outFltid").val()
			}
		}
	});
});