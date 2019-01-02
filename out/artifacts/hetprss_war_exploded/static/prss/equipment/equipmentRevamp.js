	var sel= $("#deviceId").val();
	$(".deviceStatus").val(sel);
	
$(function(){
	
	$("body").css("position","relative");
	new PerfectScrollbar(document.body);
	
		var numI = 0;
		$(".typeName").change(function(){
			numI++;
			parent.typeNameI = $(".typeName").val(); 
			});
		if(numI==0){
			parent.typeNameI = $(".typeName").val();
		}
		
		//-------------------------------------
		
		var numII = 0;
		$(".deviceNo").change( function(){
			numII++;
			parent.deviceNoI = $(".deviceNo").val(); 
			});
		if(numII==0){
			parent.deviceNoI = $(".deviceNo").val(); 
		}
		
		//-------------------------------------
		
		var numIII = 0;
		$(".deviceModel").change(function(){
			numIII++;
			parent.deviceModelI = $(".deviceModel").val(); 
			
		});
		if(numIII == 0){
			parent.deviceModelI = $(".deviceModel").val();
		}
		
		//---------------------------------------------
		
		var numIV = 0;
		$(".carNumber").change(function(){
			numIV++;
			parent.carNumberI = $(".carNumber").val(); 
		});
		if(numIV == 0){
			parent.carNumberI = $(".carNumber").val();
		}
		
		//------------------------------------------
		var numXX = 0;
		$(".innerNumber").change(function(){
			numXX++;
			parent.innerNumberI = $(".innerNumber").val();
		});
		if(numXX==0){
			parent.innerNumberI = $(".innerNumber").val();
		}
		
		//-----------------------------------------
		
		var numV = 0;
		$(".deviceStatus").change(function(){
			numV++;
			parent.deviceStatusI = $(".deviceStatus").val(); 
		});
		if(numV ==0 ){
			parent.deviceStatusI = $(".deviceStatus").val();
		}
		
		//-------------------------------------------
		var numVI = 0;
		$(".seatingNum").change(function(){
			numVI++;
			parent.seatingNumI = $(".seatingNum").val(); 
		});
		if(numVI == 0){
			parent.seatingNumI = $(".seatingNum").val();
		}
		
		//-------------------------------------------
		var numVI = 0;
		$(".remark").change(function(){
			numVI++;
			parent.remarkI = $(".remark").val(); 
		});
		if(numVI == 0){
			parent.remarkI = $(".remark").val();
		}
		
	
})
	
