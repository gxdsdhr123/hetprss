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
	loaditem(items);
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
	var value = e.target.value.trim();
	$(e.target).parent().parent().find(".list-group-item").each(function() {
		if ($(this).text().indexOf(value) == -1) {
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


/**/
function searchWord(key) {
	if (key == "") {
		loaditem(items);
		return false;
	} else {
		$.ajax({
		type : 'post',
		url : ctx + "/rule/setUpRule/getOptions",
		data : {
		q : key,
		page : 1,
		limit : 1000,
		type : $("#type").val()
		},
		success : function(result) {
			loaditem(result.item);
		}
		});
	}
}
/*/**
 * 传入数据data jsonarray
 **/
function loaditem(data) {
	$("#allul").html(""); 
	if(data.length>0){
		for (var i = 0; i < data.length; i++) {
			var item = data[i];
			var flag= true;
			$("ul[id=selectul]>li").each(function(index,e){
				if($(e).attr("id") == item.CODE){
					flag = false;
				}
			})
			if(flag){
				var li = '<li class="list-group-item" id="'+item.CODE+'" onclick="liclick(this);" ' +
					'onmousedown="lidown(this);" onmousemove="limove(this);" onmouseup="liup(this);">'+item.DESCRIPTION+'</li>';
				$("#allul").append(li);
			}
		}
	}
}

