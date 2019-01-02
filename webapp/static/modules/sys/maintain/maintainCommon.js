//新增
var editMaintainWin;
/**
 * 
 * @param tableId
 * @param opener 如果不指定由当前窗口弹出，如果为parent则由父页面打开
 */
function doAddMaintain(tableId,opener) {
	var openerWin = layer;
	if(opener=="parent"){
		openerWin = parent.layer;
	}
	editMaintainWin = openerWin.open({
		closeBtn : false,
		type : 2,
		title : "新增",
		area : [ '60%', '90%' ],
		btn : [ "重置", "保存", "取消" ],
		btn1 : function(index, layero) {
			var addForm = openerWin.getChildFrame('#addForm', index);
			addForm[0].reset();
		},
		btn2 : function(index, layero) {
			/*var addForm = layer.getChildFrame("#addForm", index);
			var iframeWin = window[layero.find('iframe')[0]['name']];
			iframeWin.doSubmit();*/
			var submitBtn = openerWin.getChildFrame("#submitBtn", index);
			submitBtn.trigger("click");
			return false;
		},
		content : ctx + '/sys/maintain/addInit?tabId=' + tableId,
		zIndex: layer.zIndex, // 重点1
		success : function(layero) {
			if(opener=="parent"){
				openerWin.setTop(layero); // 重点2
			}
		}

	});
	
	// 手工执行最大化
	//layer.full(editMaintainWin);
}
/**
 * 初始化select下拉
 * @param selectId
 * @param tip
 */
function initSelect2(selectId,tip){
	$('#'+selectId).select2({  
        placeholder: tip,  
        width:"100%",
        language : "zh-CN",
		templateResult : formatRepo,
		templateSelection : formatRepoSelection,
		escapeMarkup : function(markup) {
			return markup;
		}
    }); 
}

function formatRepo(repo) {
	return repo.text;
}
// select2显示选项处理
function formatRepoSelection(repo) {
	return repo.text;
}

/**
 * select2非空验证
 */
function vertifySelect2(){
	var res=true;
	$.each($("select[selectType='select2']"),function(k,v){
		var required=$(v).attr("lay-verify");
		if(required.indexOf("required")>=0){
			if($(v).val()==""||$(v).val()==null){
				layer.msg($(v).data("cnname")+'不能为空!', {
					icon : 2
				});
				res=false;
				return false;
			}
		}
	});
	return res;
}
