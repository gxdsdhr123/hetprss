var layer;
var clickRowId = "";
var clickRowCode = "";
var set = null;
var type;
layui.use(["layer", "form", "element"], function () {
    layer = layui.layer;
});
$(document).ready(function () {
    initGrid();

    //查询
    $("#btnSubmit").click(function () {

        // initGrid();
        $('#baseTable').bootstrapTable('refresh', {
            query: {
                dateStart: encodeURIComponent($("#dateStart").val())
            }
        });
    });

    //查看
    $("#btnDetail").click(function () {

        if ("" != clickRowId) {

            layer.open({
                type: 2,
                title: "装卸工操作记录单",
                area: ['1000px', '80%'],
                closeBtn: true,
                content: ctx + "/produce/billOperatorZX/dataInfo?fltid=" + clickRowCode,
                btn: ["保存", "取消"],
                btn1: function (index, layero) {
                    var iframeWin = window[layero.find('iframe')[0]['name']];
                    iframeWin.save();
                    return false;
                }
            });
        } else {
            layer.msg("请点击一行！", {icon: 7, time: 800});
        }
    });
});

/**
 * 初始化表格
 */
function initGrid() {
    var tableData;
    var limit = 100;
    $("#baseTable").bootstrapTable({
        striped: true,
        toolbar: "#toolbar",
        idField: "id",
        uniqueId: "rowId",
        url: ctx + "/produce/billOperatorZX/dataList",
        method: "get",
        pagination: false,
        showRefresh: false,
        clickToSelect: false,
        searchOnEnterKey: true,
        pagination: true,
        search: false,
        undefinedText: "",
        height: $(window).height(),
        queryParamsType: '',
        queryParams: function (params) {
            return getParams(params);
        },
        columns: getGridColumns(),
        responseHandler: function (res) {
            tableData = res;
            $("#total").text(res.length);
            return res.slice(0, limit);
        },
        onClickRow: function onClickRow(row, tr, field) {
            clickRowId = row.ID;
            clickRowCode = row.FLTID;
            $(".clickRow").removeClass("clickRow");
            $(tr).addClass("clickRow");
        },
        onDblClickRow: function (row, tr, field) {
            //baochl_20180717 匿名账号禁止操作
            if ($("#loginName").val() == "anonymous") {
                return false;
            }
            $("#btnDetail").trigger("click");
        }
    });

}

//获取搜索数据
function getParams(params) {
    return {
        dateStart: encodeURIComponent($("#dateStart").val()),
        flightNumber: encodeURIComponent($("#flightNumber").val()),
        operator: encodeURIComponent($("#operator").val())
    }
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
        field: "FLIGHT_DATE",
        title: '航班日期',
        align: 'center'
    }, {
        field: "FLIGHT_NUMBER",
        title: '航班号',
        align: 'center'
    }, {
        field: "OPERATOR",
        title: '监装员',
        align: 'center'
    }, {
        field: "OPERATOR_TIME",
        title: '操作时间',
        align: 'center',
        formatter: function (value, row, index) {

            var date = new Date(value);
            var Y = date.getFullYear() + '-';
            var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
            var D = date.getDate() + ' ';
            var h = date.getHours() + ':';
            var m = date.getMinutes() + ':';
            var s = date.getSeconds();
            return Y+M+D+h+m+s;
        }
    }];
    return columns;
}

function saveSuccess() {
    layer.close(set);
    $("#baseTable").bootstrapTable('refresh');
}
