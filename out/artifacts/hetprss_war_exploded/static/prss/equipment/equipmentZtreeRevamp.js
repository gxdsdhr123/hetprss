var num =  $(".douxf").val();
$(".reskind").val(num);
$(function(){
	
	var numI = 0;
	$(".typeName").change(function(){
		numI++;
		parent.typeNameI = $(".typeName").val();
	});
	if(numI == 0){
		parent.typeNameI = $(".typeName").val();
	}
	
	
	var numII = 0;
	$(".reskind").change(function(){
		numII++;
		parent.reskindI = $(".reskind").val();
	});
	if(numII == 0){
		parent.reskindI = $(".reskind").val();
	}
});


$('#filterDouQKA .reskind').select2({  
	placeholder: "请选择",  
    width:"100%",
    language : "zh-CN",
	templateResult : formatRepo,
	templateSelection : formatRepoSelection,
	escapeMarkup : function(markup) {
		return markup;
	}
});

	
	

function formatRepo(repo) {
		return repo.text;
	}

function formatRepoSelection(repo) {
	return repo.text;
}

