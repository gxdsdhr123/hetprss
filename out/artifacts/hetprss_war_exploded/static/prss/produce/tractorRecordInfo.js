var layer;// 初始化layer模块
var baseTable;// 基础表格
var clickRowId = "";// 当前选中行，以便单选后操作
var clickRow;
var filter;// 用于筛选
var iframe;
var ioFlag;
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})
	new PerfectScrollbar('#container');
	refresh();
});

function refresh() {
	init(99);//更新项目
	init(1);//推飞机工作记录
	init(2);//拖拽飞机工作记录
	init(3);//故障描述
}

function init(insType){
	$.ajax({
		type : 'post',
		url : ctx + "/produce/tractor/getData",
		data : {
			insType : insType,
			id : $("#id").val()
		},
		dataType : 'json',
		success : function(data) {
			if(insType==1){
				initData(data,1,'tTable');
			} else if(insType == 2){
				initData(data,2,'zTable');
			} else if(insType == 99){
				initUpdate(data,insType);
			} else if(insType == 3){
				initGZ(data);
			} 
		},
		error : function (msg){
		}
	});
}


function initGZ(data){
	var table = $("#gzTable>tbody");
	$.each(data,function(index,item){
		var html = '<tr>';
		html += '<td>'+(index+1)+'</td>';
		html += '<td>'+(item.INS_TYPE ==1?'接车':'收车')+'</td>';
		html += '<td>'+(item ==null?'':item.CLGZ_DESC)+'</td>';
		html += '<td>'+(item["1_FILE_PATH"]==""?"":createGZ(item["1_FILE_PATH"],1))+'</td>';
		html += '<td>'+(item["2_FILE_PATH"]==""?"":createGZ(item["2_FILE_PATH"],2))+'</td>';
		html += '<td>'+(item["3_FILE_PATH"]==""?"":createGZ(item["3_FILE_PATH"],3))+'</td>';
		html += '</tr>';
		table.append($(html));
	})
}

function createGZ(path,type){
	var html = '';
	if(type == 1){
		html += '<a href="javascript:void(0)" data-id="'+ path + '" data-type="1" onclick="show($(this))"><i class="fa fa-photo">&nbsp;<i/>查看</a>';
	} else if(type == 2){
		html += '<a href="javascript:void(0)" data-id="'+ path + '" data-type="2" onclick="show($(this))"><i class="fa fa-volume-up">&nbsp;<i/>播放</a>';
	} else if(type == 3){
		html += '<a href="javascript:void(0)" data-id="'+ path + '" data-type="3" onclick="show($(this))"><i class="fa fa-video-camera">&nbsp;<i/>播放</a>';
	}
	return html;
}

function show(btn) {
	var id = btn.data("id");
	var type = btn.data("type");
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
		content:ctx + '/produce/operator/downAtta?fileId=' + id + '&type=' + type
	});
}

var data1 = ['序号','航班号','注册号','STD','机位','收到时间','到位时间','开始作业时间','结束时间','异常情况'];
var data2 = ['序号','航班号','注册号','起始位置','拖至位置','收到时间','到位时间','开始作业时间','结束时间','异常情况'];
function initData(data,type,tableName){
	var table = $("#" + tableName);
	table.children().remove();
	var html = '<thead><tr><th colspan="11">'+(type==1?'推飞机工作记录':'拖拽飞机工作记录')+'</th></tr><tr>';
	if(type==1){
		for(str in data1){
			html += '<th>'+data1[str]+'</th>';
		}
	} else if(type==2){
		for(str in data2){
			html += '<th>'+data2[str]+'</th>';
		}
	}
	html += '</tr></thead>';
	html += '<tbody>'
	$.each(data,function(i,e){
		var tbody = '<tr>';
		tbody += '<td>' + (i+1) + '</td>';
		tbody += '<td>' + e.FLIGHT_NUMBER + '</td>';
		tbody += '<td>' + e.AIRCRAFT_NUMBER + '</td>';
		tbody += '<td>' + e.STD + '</td>';
		tbody += '<td>' + e.ACTSTAND_CODE + '</td>';
		tbody += '<td>' + e.ACT_ARRANGE_TM + '</td>';
		tbody += '<td>' + e.TIME1 + '</td>';
		tbody += '<td>' + e.TIME2 + '</td>';
		tbody += '<td>' + e.TIME3 + '</td>';
		if(e.NUM>0){
			tbody += '<td>'+e.INFO_DESC+'<button type="button" class="layui-btn layui-btn-primary"	onclick="down('+e.FLTID+','+type+');">下载</button></td>';
		} else {
			tbody += '<td>'+e.INFO_DESC+'</td>';
		}
		tbody += '</tr>';
		html += tbody;
	})
	html += '</tbody>';
	table.append($(html));
}
function down(fltid,type){
	$("input[name=fltid]").val(fltid);
	$("input[name=type]").val(type);
	$("#downForm").submit();
}

function initUpdate(data,insType){
	var table = $("#updateTable");
	var html = ''
		var ins1 = data[0];
		var ins2 = data[1];
		html +='<tr>';
		html +='<td><strong>日期</strong></td>';
		html +='<td colspan="2">'+(ins1==null?'':ins1.OPER_DATE)+'</td>';
		html +='<td><strong>驾驶员</strong></td>';
		html +='<td>'+(ins1 ==null?'':ins1.NAME)+'</td>';
		html +='<td><strong>车号</strong></td>';
		html +='<td colspan="2">'+(ins1 ==null?'':ins1.VEHICLE_NUMBER)+'</td>';
		html +='<td><strong>车辆型号</strong></td>';
		html +='<td colspan="2">'+(ins1 ==null?'':ins1.DEVICE_MODEL)+'</td>';
		html +='</tr>';
		html +='<tr>';
		html +='<td rowspan="2"><strong>接车</strong><input type="hidden" id="jc_id" value="'+(ins1 ==null?'':ins1.INSID)+'"/></td>';
		html +='<td><strong>出勤时间</strong></td>';
		html +='<td>'+(ins1 ==null?'':ins1.OPER_BOUND_DATE)+'</td>';
		html +='<td><strong>公里数</strong></td>';
		html +='<td><input type="text" id="jc_gl" class="layui-input" value="'+(ins1 ==null?'':ins1.VALUE1)+'"/></td>';
		html +='<td><strong>小时数</strong></td>';
		html +='<td><input type="text" id="jc_xs" class="layui-input" value="'+(ins1 ==null?'':ins1.VALUE2)+'"/></td>';
		html +='<td><strong>燃油</strong></td>';
		html +='<td><input type="text" id="jc_ry" class="layui-input" value="'+(ins1 ==null?'':ins1.VALUE3)+'"/></td>';
		html +='<td><strong>加油</strong></td>';
		html +='<td><input type="text" id="jc_jy" class="layui-input" value="'+(ins1 ==null?'':ins1.VALUE4)+'"/></td>';
		html +='</tr>';
		html +='<tr>';
		html +='<td><strong>异常系统记录</strong></td>';
		html +='<td colspan="9">'+(ins1 ==null?'':ins1.JCXM_NAME)+'</td>';
		html +='</tr>';
		html +='<tr>';
		html +='<td rowspan="2"><strong>收车</strong><input type="hidden" id="sc_id" value="'+(ins2 ==null?'':ins2.INSID)+'"/></td>';
		html +='<td><strong>收车时间</strong></td>';
		html +='<td>'+(ins2 ==null?'':ins2.OPER_UNBOUND_DATE)+'</td>';
		html +='<td><strong>公里数</strong></td>';
		html +='<td>'+(ins2 ==null?'':'<input type="text" id="sc_gl" class="layui-input" value="'+ins2.VALUE1+'"/>')+'</td>';
		html +='<td><strong>小时数</strong></td>';
		html +='<td>'+(ins2 ==null?'':'<input type="text" id="sc_xs" class="layui-input" value="'+ins2.VALUE2+'"/>')+'</td>';
		html +='<td><strong>燃油</strong></td>';
		html +='<td>'+(ins2 ==null?'':'<input type="text" id="sc_ry" class="layui-input" value="'+ins2.VALUE3+'"/>')+'</td>';
		html +='<td><strong>加油</strong></td>';
		html +='<td>'+(ins2 ==null?'':'<input type="text" id="sc_jy" class="layui-input" value="'+ins2.VALUE4+'"/>')+'</td>';
		html +='</tr>';
		html +='<tr>';
		html +='<td><strong>异常系统记录</strong></td>';
		html +='<td colspan="9" >'+(ins2 ==null?'':ins2.JCXM_NAME)+'</td>';
		html +='</tr>';
	html += '';
	table.append($(html));
}

function getSubmitData() {
	var paramsJsonObj = {};
	paramsJsonObj['jc_id'] = $("#jc_id").val();
	paramsJsonObj['jc_value1'] = $("#jc_gl").val();
	paramsJsonObj['jc_value2'] = $("#jc_xs").val();
	paramsJsonObj['jc_value3'] = $("#jc_ry").val();
	paramsJsonObj['jc_value4'] = $("#jc_jy").val();

	paramsJsonObj['sc_id'] = $("#sc_id").val();
	paramsJsonObj['sc_value1'] = $("#sc_gl").val();
	paramsJsonObj['sc_value2'] = $("#sc_xs").val();
	paramsJsonObj['sc_value3'] = $("#sc_ry").val();
	paramsJsonObj['sc_value4'] = $("#sc_jy").val();
	
	return JSON.stringify(paramsJsonObj);
}
