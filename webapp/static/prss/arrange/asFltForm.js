var holdDown = 0;
var scrollbar;
$(function() {
	$(".sortable").css("position","relative");
	$(".sortable").each(function(){
		scrollbar = new PerfectScrollbar(this);
	});
	// 选择
	$("#pushright").click(function() {
		var lis = $("#allul").find("li.bechoose");
		$("#selectul").append(lis.clone(true).removeClass("bechoose"));
		lis.remove();
	});
	// 取消选择
	$("#pushleft").click(function() {
		var lis = $("#selectul").find("li.bechoose");
		$("#allul").append(lis.clone(true).removeClass("bechoose"));
		lis.remove();
	});
})

function search(e) {
	var value = e.target.value.trim();
	$(e.target).parent().parent().find(".list-group-item").each(function() {
		if ($(this).text().indexOf(value) == -1) {
			$(this).hide();
		} else {
			$(this).show();
		}
	})
	scrollbar.update();
}
function prepareMembers() {
	var selectMemberIdsp = "";
	var lis = $("#selectul").find("li");
	for (var i = 0; i < lis.length; i++) {
		var id = lis.eq(i).attr("id");
		selectMemberIdsp = selectMemberIdsp + id + ",";
	}
	selectMemberIdsp = selectMemberIdsp.substring(0, selectMemberIdsp.length - 1);
	$("#sPerson").val(selectMemberIdsp);
	parent.$("#bindFlt"+$("#day").val()).val(selectMemberIdsp);
}
function liclick(obj) {
	if (!$(obj).hasClass("bechoose")) {
		$(obj).addClass("bechoose");
	} else {
		$(obj).removeClass("bechoose");
	}
}

function lidown(obj) {
	holdDown = 1;
}
function liup(obj) {
	holdDown = 0;
}
function limove(obj) {
	if (holdDown == 1) {
		if (!$(obj).hasClass("bechoose")) {
			$(obj).addClass("bechoose");
		}
	}
}
