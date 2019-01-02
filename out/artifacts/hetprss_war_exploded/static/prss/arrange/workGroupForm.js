var holdDown = 0;
$(function() {
	
	if (lPerson) {
		lPerson = JSON.parse(lPerson);
	}
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

	// 表单提交方法被调用后触发此验证
	$('#createForm').validate({
		ignore : "",
		rules : {
			leaderId : "required",
			teamName:{//required:true,
					  maxlength:4,
					  //tnameCheck为自定义的验证方法，见本页下方
					  tnameCheck:true
					 }
		},
		errorPlacement : function(error, element) {
			error.appendTo(element.parent());
		},
		submitHandler : function(form) {
			var loading = null;
			prepareMembers();
			$("#createForm").ajaxSubmit({
				async : false,
				beforeSubmit : function() {
					loading = layer.load(2, {
						shade : [ 0.1, '#000' ]
					// 0.1透明度
					});
				},
				error : function(XmlHttpRequest, textStatus, errorThrown) {
					layer.close(loading);
					layer.msg('保存失败！', {
						icon : 2
					});
				},
				success : function(data) {
					layer.close(loading);
					if (data == "success") {
						layer.msg('保存成功！', {
							icon : 1,
							time : 600
						}, function() {
							parent.formSubmitCallback();// 成功后调用主页面，关闭弹出层并刷新表格
						});
					} else if (data == "error") {
						layer.msg('保存失败！', {
							icon : 2,
							time : 600
						});
					} else if (data == "overTime") {// 修改时间验证，若有变化说明数据已被改动，需要重新加载数据
						layer.msg('数据已失效，请重新加载数据', {
							icon : 2,
							time : 600
						}, function() {
							parent.formSubmitCallback();
						});
					}
				}
			});
			return false;
		}
	});
})

function doSubmit() {
	$('#createForm').submit();
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
jQuery.validator.addMethod("tnameCheck", function(value, element) {   
    var reg = /^[0-9a-zA-Z\u4e00-\u9fa5]*$/;
    return this.optional(element) || (reg.test($.trim(value)));
}, "只能填写字母、数字、中文");

/*
 * function searchInAll() { var keyword = $("#searchInAll").val(); var table = $("#all"); var aPerson; if (!$("#aPerson").val()) { aPerson = null; } else { aPerson = eval("(" +
 * $("#aPerson").val() + ")"); } searchPerson(keyword, table, aPerson); } function searchInSelected() { var keyword = $("#searchInSelected").val(); var table = $("#selected"); var
 * sPerson; if (!$("#aPerson").val()) { sPerson = null; } else { aPerson = eval("(" + $("#aPerson").val() + ")"); } searchPerson(keyword, table, sPerson); } // 要搜索的关键词 显示的table
 * 以及原始人员数据（json） function searchPerson(keyword, table, person) { table.find("body").html(""); // 如果为空显示所有的人员数据项 if (!keyword) { for ( var item in person) { var tr = "<tr><td>" +
 * item.name + "</td></tr>"; table.find("body").append(tr); } } else { for ( var item in person) { if (item.name.indexOf(keyword) != -1) { var tr = "<tr><td>" + item.name + "</td></tr>";
 * table.find("body").append(tr); } } } }
 */