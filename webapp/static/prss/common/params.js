//设置可编辑div值
function setParams(name,no,ischg,chgval,en){//name,no,ischg
	var tc = document.getElementById("mtext");
	tc.focus(); 
	var div_id = '';
	var schema = $("#schema").val();
	if(schema == '88'){
		if (ischg == 1) {
			div_id = no + "_1_" + chgval;
		} else {
			div_id = no + "_0_0"
		}
	} else {
		div_id = no;
	}
	var div_name = "";
	if(chgval==1){
		div_name += name + "_" + "旧值";
	} else if(chgval == 2){
		div_name += name + "_" + "新值";
	} else {
		div_name += name;
	}
	var html = "<div class='div_class select' data-id="+div_id+" data-en=" + en + ">["+div_name+"]</div>";
	if(typeof document.selection != "undefined"){ 
		var range = document.selection.createRange();   
		range.pasteHTML(html);
	} else if(window.getSelection()!=undefined){
		var getSelection = window.getSelection();
		var range = getSelection.getRangeAt(0);
//		range.collapse(false);
		var node = range.createContextualFragment(html);
		var lastChild = node.lastChild;
		range.insertNode(node);
		if(lastChild){
			range.setEndAfter(lastChild);
			range.setStartAfter(lastChild);
		}
		getSelection.removeAllRanges();
		getSelection.addRange(range);
	}
}
$(function(){
//	$(document).on("keydown","#mtext",function (e) {
//	    if(e.keyCode === 8 || e.keyCode ===46){
//	        var selection = window.getSelection();
//	        var range = selection.getRangeAt(0);
//	        // 删除当前 Range 对象表示的文档区域。
//	         range.deleteContents();
//	         e.preventDefault();
//	         e.stopPropagation();
//	    }
//	});
});
function getvarcols(){
	var ids = '';
	$(".div_class").each(function(index,row){
		ids += $(row).data("id") + ",";
	})
	if(ids.length>0) ids = ids.substring(0,ids.length-1);
	return ids;
}