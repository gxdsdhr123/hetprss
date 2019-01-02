var selectedId = '';
var selectedText = '';
var holdDown = 0;
$(function() {
	$("li").click(function() {
		$("li").each(function() {
			var obj = $(this);
			obj.removeClass("bechoose");
		});
		
		$(this).addClass("bechoose");
	});
	// 选择人员
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
	
	selectedId=$("#selectedId").val();
	selectedText=$("#selectedText").val()==""?$("#selectedText").val():$("#selectedText").val()+",";
	//loaditem(items);
	new PerfectScrollbar("#allul");
	new PerfectScrollbar("#selectul");
})
function getChooseDate() {
	var result = new Object();
	result.chooseValue = selectedId;
	result.chooseTitle = selectedText;
	return result;
}




function getChooseData() {
	var selectId = "";
	var selectText = "";
	var lis = $("#selectul").find("li");
	
	for (var i = 0; i < lis.length; i++) {
		var id = lis.eq(i).attr("id");
		var text = lis.eq(i).text();
		selectId = selectId + id + ",";
		selectText = selectText + text + ",";
	}
	selectId = selectId.substring(0, selectId.length - 1);
	selectText = selectText.substring(0, selectText.length - 1);
	var result = {};
	$("#selectedId").val(selectId);
	$("#selectedText").val(selectText);
	result.chooseValue = selectId;
	result.chooseTitle = selectText;
	return result;
}

function search(e) {
	var value = e.target.value.trim().toLocaleUpperCase();
	console.log("value"+value);
	$(e.target).parent().parent().find(".list-group-item").each(function() {
//		if ($(this).text().indexOf(value) == -1) {
		if ($(this).attr("name").toLocaleUpperCase().indexOf(value) == -1) {
			$(this).hide();
		} else {
			$(this).show();
		}
	})
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


function clearSelect() {
	var lis = $("#selectul").find("li");
	$("#allul").append(lis.clone(true).removeClass("bechoose"));
	lis.remove();
}


/*
function searchWord() {
	var key = $("input[name='keyWord']").val();
	if (key == "") {
		loaditem(items);
		return false;
	} else {
		$.ajax({
		type : 'post',
		url : ctx + "/flightDynamic/getOptions",
		data : {
		q : key,
		page : 1,
		limit : 30,
		type : $("#type").val()
		},
		success : function(result) {
			loaditem(result.item);
		}
		});
	}
}
*//**
 * 传入数据data jsonarray
 *//*
function loaditem(data) {
	$("#allItems").html("");
	var colClass = "col-md-2 col-sm-2";
	var maxLength = 0;
	// 根据数据长度 布局
	for (var i = 0; i < data.length; i++) {
		if (data[i].text.length > maxLength) {
			maxLength = data[i].text.length;
		}
	}
	if (maxLength > 7 && maxLength < 10) {
		colClass = "col-md-3 col-sm-3";
	} else if (maxLength > 9) {
		colClass = "col-md-4 col-sm-4";
	}
	for (var i = 0; i < data.length; i++) {
 		var div = $("<div class='" + colClass + "'><input title='" + data[i].text + "' value='" + data[i].id + "' type='checkbox' onclick='changeState(this)'>" + data[i].text + "</div>");
		if (selectedId.indexOf(data[i].id) > -1) {
			div.find("input").attr("checked", 'true');
			
		}else {
			div.find("input").removeAttr("checked");
		}
		$("#allItems").append(div);
	}
}

function changeState(obj) {
	var checkBox = $(obj);
	if (checkBox.is(':checked')) {// 选中
		selectedId += checkBox.val() + ",";
		selectedText+= checkBox.attr("title") + ",";
	} else {
		selectedId=selectedId.replace(checkBox.val() + ",", "");
		selectedText=selectedText.replace(checkBox.attr("title") + ",", "");
	}
}
function clearSelect() {
	 selectedId = '';
	 selectedText = '';
	 $("input").removeAttr("checked");
}*/

