$(function(){
	var numI = 0;
	$(".name").change(function(){
		numI++;
		parent.nameI = $(".name").val();
	});
	if(numI == 0){
		parent.nameI = $(".name").val();
	}
});