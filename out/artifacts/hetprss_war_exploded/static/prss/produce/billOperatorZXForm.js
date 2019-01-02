var layer;
var form;
var type;

$(document).ready(function() {

	layui.use([ "layer", "form", "element" ], function() {
		layer = layui.layer;
		form=layui.form;

		form.on('submit(save)', function(data){

				$("#tableForm").ajaxSubmit({
					async : false,
					beforeSubmit : function() {
						loading = parent.layer.load(2, {
							shade : [ 0.3, '#000' ]// 0.1透明度
						});
					},
					error : function() {
						layer.close(loading);
						parent.layer.close(loading);
						layer.msg('保存失败！', {
							icon : 2
						});
					},
					success : function(data) {
						layer.close(loading);
						parent.layer.close(loading);
						if(data.code=="0000"){
							layer.msg(data.msg, {
								icon : 1,
								time : 1500
							}, function() {
									try{
										parent.saveSuccess();
									}catch(ex){
										
									}
							});
						}else{
							layer.msg(data.msg, {
								icon : 2
							});
						}
					}
				});
			
			return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});
	});

    initGrid();
});

function initGrid() {

	var tableData = JSON.parse($("#detail").val());
    $("#dataTable").bootstrapTable({
        striped: true,
        toolbar: false,
        idField: "id",
        uniqueId: "rowId",
        pagination: false,
        showRefresh: false,
        clickToSelect: false,
        searchOnEnterKey: false,
        search: false,
        undefinedText: "",
        columns: getGridColumns(),
        data: tableData
    });

}

/**
 * 表格列与数据映射
 */
function getGridColumns() {
    var columns = [{
        field: "rowId",
        title: "序号",
        align: 'center',
        valign: 'middle',
        formatter: function (value, row, index) {
            return index + 1;
        }
    }, {
        field: "OPERATION",
        title: '姓名',
        align: 'center'
    }, {
        field: "POS",
        title: '位置',
        align: 'center',
        formatter:function(value,row,index){//模仿这个写就行
            var s = '';

            if(value!=null && value !=""){
            	if(value==1) {
					s = '前舱内';
				} else if(value==2) {
                    s = '前舱外';
				} else if(value==3) {
                    s = '后舱内';
				} else if(value==4) {
                    s = '后舱外';
				}
            }
            return s;
        }
    }];
    return columns;
}

function save(){
	$("#submitBtn").trigger("click");
}