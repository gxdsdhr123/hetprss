var layer;// 初始化layer模块
var baseTable;// 基础表格
$(function () {

    layui.use(['layer'], function () {
        layer = layui.layer;
    });

    $("#ALN_2CODE").select2({
        language: "zh-CN",
        placeholder: "请选择"
    });

    //新增功能
    $(".add").click(function () {

        var data = {
            ALN_2CODE: $('#ALN_2CODE').val(),
            CBY_FEE: $('#CBY_FEE').val(),
            FBY_FEE: $('#FBY_FEE').val()
        };

        if (!checkForm()) {

            return false;
        }

        $.ajax({
            type: 'post',
            async: false,
            url: ctx + "/jwcbjc/statistics/changeCBPrice",
            data: data,
            error: function () {
                layer.msg('保存失败！', {
                    icon: 2,
                    time: 1500
                });
            },
            success: function (data) {
                layer.msg('保存成功！', {
                    icon: 1,
                    time: 1500
                }, function () {
                    saveSuccess();
                });
            }
        });
    });

    jQuery.fn.bootstrapTable.columnDefaults.sortable = false;
    jQuery.fn.bootstrapTable.columnDefaults.align = "center";

    baseTable = $("#baseTableConfig");
    var tableOptions = {
        url: ctx + "/jwcbjc/statistics/CBPriceList",
        method: "get",
        dataType: "json",
        striped: true,
        cache: true,
        undefinedText: '',
        checkboxHeader: false,
        searchOnEnterKey: false,
        pagination: true,
        sidePagination: 'server',
        pageNumber: 1,
        pageSize: 5,
        pageList: [5, 10, 15, 20, 50, 100],
        queryParamsType: '',
        queryParams: function (params) {
            return getParams(params);
        },
        columns: [
            {field: "NUM", title: "序号", align: 'left', halign: 'center', editable: false},
            {field: "ALN_2CODE", title: "航空公司", align: 'left', halign: 'center', editable: false},
            {field: "CBY_FEE", title: "除冰液价格", align: 'left', halign: 'center', editable: false},
            {field: "FBY_FEE", title: "防冰液价格", align: 'left', halign: 'center', editable: false},
            {
                field: "OPERATE",
                title: "操作",
                align: 'left',
                halign: 'center',
                editable: false,
                formatter: function (value, row, index) {
                    var id = row["ID"];
                    return '<a onclick="delCBPrice(' + id + ')">删除</a>';
                }
            }
        ]
    };

    tableOptions.height = $("#baseTables").height();// 表格适应页面高度
    baseTable.bootstrapTable(tableOptions);// 加载基本表格
});

//校验数据
function checkForm() {

    var reg = /^(-?\d+)(\.\d+)?$$/;
    if (!reg.test($("#CBY_FEE").val())) {
        layer.alert("请输入正确的数字", {icon: 2});
        return false;
    }
    if (!reg.test($("#FBY_FEE").val())) {
        layer.alert("请输入正确的数字", {icon: 2});
        return false;
    }

    return true;
}

//删除数据
function delCBPrice(id) {

    $.ajax({
        type: 'post',
        async: false,
        url: ctx + "/jwcbjc/statistics/delCBPrice",
        data: {ID: id},
        error: function () {
            layer.msg('删除失败！', {
                icon: 2,
                time: 1500
            });
        },
        success: function (data) {
            layer.msg('删除成功！', {
                icon: 1,
                time: 1500
            }, function () {
                saveSuccess();
            });
        }
    });
}

//获取搜索数据
function getParams(params) {
    return {
        ALN_2CODE: encodeURIComponent($("#ALN_2CODE").val()),
        CBY_FEE: encodeURIComponent($("#CBY_FEE").val()),
        FBY_FEE: encodeURIComponent($("#FBY_FEE").val()),
        pageNumber: params.pageNumber,
        pageSize: params.pageSize

    }
}

function saveSuccess() {
    $("#baseTableConfig").bootstrapTable('refresh');
}





