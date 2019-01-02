layui.use([ "form" ]);
$(document).ready(function() {
	$("#aircraftNumber").blur(function() {
		validActNumber();
	});
});

function validActNumber(){
	var result = true;
	var fltid = $("#fltid").val();
	var aircraftNumber = $("#aircraftNumber").val();
	$("#aircValidFlag").empty();
	if(aircraftNumber&&$.trim(aircraftNumber)!=""){
		$.ajax({
			type : 'post',
			dataType:"html",
			url : ctx + "/flightDynamic/validActNumber",
			async:false,
			data : {
				fltid:fltid,
				actNumber:aircraftNumber
			},
			success : function(count) {
				if(count&&Number(count)>0){
					$("#aircValidFlag").html("&nbsp;<i class='fa fa-check' style='color:green;font-size:16px;'></i>");
				} else {
					result = false;
					$("#aircValidFlag").html("&nbsp;<i class='fa fa-close' style='color:red;font-size:16px;'></i>");
				}
			}
		});
	}
	return result;
}