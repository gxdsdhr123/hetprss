var layer;
layui.use(["layer"]);
$(function() {
	$("body").css("minHeight", "0px");
	layui.use("form");
})
var img = 0;
var vol = 0;
var vid = 0;
$(document).ready(function() {
	layui.use([ 'layer', 'form' ], function() {
		var form = layui.form;
		form.on('checkbox(img)', function(data) {
			img = data.elem.checked ? 1 : 0;
			tableOptions.queryParams.img = img;
			$("#errorTable").bootstrapTable('refresh');
			
		});
		form.on('checkbox(vol)', function(data) {
			vol = data.elem.checked ? 1 : 0;
			tableOptions.queryParams.vol = vol;
			$("#errorTable").bootstrapTable('refresh');
			
		});
		form.on('checkbox(vid)', function(data) {
			vid = data.elem.checked ? 1 : 0;
			tableOptions.queryParams.vid = vid;
			$("#errorTable").bootstrapTable('refresh');
			
		});
	});
	var tableOptions = {
			uniqueId : "rowId",
			url : ctx + "/flightDynamic/getErrorDate",
			queryParams:{
				'fltid':$("#fltid").val(),
				'hisFlag':$("#hisFlag").val(),
				'hisDate' : $("#hisDate").val(),
				'img' : img,
				'vol' : vol,
				'vid' : vid
			},
			method : "get",
			toolbar : "#toolbar",
			pagination : false,
			showRefresh : false,
			search : false,
			clickToSelect : false,
			searchOnEnterKey : true,
			undefinedText : "",
			height : $(window).height(),
			columns : getErrorGridColumns(),
			onLoadSuccess : function() {
				$("#errorTable").find("tr td:nth-child(6)").each(function() {
					$(this).attr("title", $(this).text());
				})
				$("#errorTable").find("tr td:nth-child(6)").each(function() {
					$(this).css("word-wrap", "break-word");
					$(this).css("white-space", "normal");
				})
				var tableData = $("#errorTable").bootstrapTable('getData');
				if(tableData.length > 0){
					var maxDate = new Date(0);
					for(var i = 0;i<tableData.length;i++){
						if(new Date(tableData[i]["CREATE_DATE"]) > maxDate){
							maxDate = tableData[i]["CREATE_DATE"];
						}
					}
					var user = $("#user").val();
					if(localStorage["latestErrorTimeOf"+user] && new Date(localStorage["latestErrorTimeOf"+user]) < new Date(maxDate)){
						localStorage["latestErrorTimeOf"+user] = maxDate;
					}
				}
			}
		};
	$("#errorTable").bootstrapTable(tableOptions);
	setInterval(function(){
		$("#errorTable").bootstrapTable('refresh');
	},30000)
});

/**
 * 格列与数据映射
 * 
 * @returns {Array}
 */
function getErrorGridColumns() {
	var columns = [
			{
				field : "rowId",
				title : "序号",
				width : '40px',
				align : 'center',
				formatter : function(value, row, index) {
					row["rowId"] = index;
					return index + 1;
				}
			},
			{
				field : "FLIGHT_NUMBER",
				title : "航班号",
				width : '100px',
				align : 'center'
			},
			{
				field : "atta",
				title : '图片',
				width : '50px',
				align : 'center',
				formatter : function(value, row, index) {
					var string = "";
					if (row.PIC > 0) {
						string += '<a href="javascript:void(0)" data-id="'+ row.ID + '" data-type="1" onclick="downAtta($(this))"><i class="fa fa-photo">&nbsp;<i/>查看</a>';
					}
					return string;
				}
			},
			{
				field : "atta",
				title : '语音',
				width : '50px',
				align : 'center',
				formatter : function(value, row, index) {
					var string = "";
					if (row.VOI > 0) {
						string += '<a href="javascript:void(0)" data-id="'+ row.ID + '" data-type="2" onclick="downAtta($(this))"><i class="fa fa-volume-up">&nbsp;<i/>播放</a>';
					}
					return string;
				}
			},
			{
				field : "atta",
				title : '视频',
				width : '50px',
				align : 'center',
				formatter : function(value, row, index) {
					var string = "";
					if (row.VID > 0) {
						string += '<a href="javascript:void(0)" data-id="'+ row.ID + '" data-type="3" onclick="downAtta($(this))"><i class="fa fa-video-camera">&nbsp;<i/>播放</a>';
					}
					//fa-file-audio-o
					return string;
				}
			}, {
				field : "INFO_DESC",
				title : '文字描述',
				width : '210px'
			}, {
				field : "OFFICE_ID",
				title : '报送岗位',
				width : '100px',
				align : 'center'
			}, {
				field : "USER_ID",
				title : '报送人',
				width : '100px',
				align : 'center'
			}, {
				field : "CREATE_DATE",
				title : '报送时间',
				width : '150px',
				align : 'center'
			} ];
	return columns;
}
function downAtta(btn) {
	var id = btn.data("id");
	var type = btn.data("type");
	var hisFlag = $("#hisFlag").val();
	var width = "450px";
	if(type==3){
		width = "600px";
	}
	layer.open({
		type:2,
		area: [width,'450px'],
		skin: 'layui-layer-nobg',
		shadeClose: true,
		title:false,
		content:ctx + '/flightDynamic/downAtta?id=' + id + '&type=' + type + "&hisFlag=" + hisFlag
	});
}