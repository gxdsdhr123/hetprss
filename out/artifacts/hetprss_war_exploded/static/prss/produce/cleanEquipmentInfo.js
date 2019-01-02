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
	init(1);//工作记录
	init(2);//故障图片
}

function init(insType){
	$.ajax({
		type : 'post',
		url : ctx + "/clean/equipment/getData",
		data : {
			insType : insType,
			id : $("#id").val(),
			typeId : $("#typeId").val()
		},
		async:true,
		dataType : 'json',
		success : function(data) {
			if(insType==1){
				initData(data,'tTable');
			} else if(insType == 99){
				initUpdate(data);
			} else if(insType == 2){
				initTP(data);
			}
		},
		error : function (msg){
		}
	});
}
function initTP(data){
	var table = $("#tpTable");
	table.append($('<thead><tr><th>接车异常</th><th>收车异常</th></tr></thead>'));
	var ins1 = data[0];
	var ins2 = data[1];
	var num = 0;
	var insType = ins1!=null?ins1.INS_TYPE:'';
	var path1='',path2='';
	if(insType==2){
		path2 = ins1.FILE_PATH; 
	} else if(insType==1){
		path1 = ins1.FILE_PATH; 
		path2 = ins2==null?'':ins2.FILE_PATH; 
	}
	var arr1={},arr2={};
	if(path1 != '')
		arr1 = path1.split(",");
	if(path2 != '')
		arr2 = path2.split(",");
	var num = (arr1.length==null?0:arr1.length)>(arr2.length==null?0:arr2.length)?(arr1.length==null?0:arr1.length):(arr2.length==null?0:arr2.length);
	for(var i=0;i<num;i++){
		var html = '';
		html += '<tr>';
		html += '<td style="width: 50%;" class="l'+i+'"></td>';
		html += '<td style="width: 50%;" class="r'+i+'"></td>';
		html += '</tr>';
		table.append($(html));
	}
	for(var i=0;i<num;i++){
		var lobj = $(".l"+i);
		var robj = $(".r"+i);
		if(arr1!=null && i<arr1.length){
			lobj.append($('<img src="'+ctx+'/clean/equipment/pic?fileId='+arr1[i]+'" class="procPic" />'));
//			sleep(500);
		}
		if(arr2!=null && i<arr2.length){
			robj.append($('<img src="'+ctx+'/clean/equipment/pic?fileId='+arr2[i]+'" class="procPic" />'));
//			sleep(500);
		}
	}
}

function sleep(numberMillis) { 
	var now = new Date(); 
	var exitTime = now.getTime() + numberMillis; 
	while (true) { 
		now = new Date(); 
		if (now.getTime() > exitTime) 
			return; 
	} 
}

var data1 = ['序号','航班号','注册号','ETA','机位','收到时间','到位时间','舱门是否有划痕','开始时间','结束时间','加水量','异常情况'];
var data2 = ['序号','航班号','注册号','ETA','机位','收到时间','到位时间','舱门是否有划痕','开始时间','结束时间','异常情况'];
function initData(data,tableName){
	var table = $("#" + tableName);
	var typeId = $("#typeId").val();
	table.children().remove();
	var html = '<thead><tr><th colspan="'+(typeId==19?12:11)+'">工作记录</th></tr><tr>';
	if(typeId==19){
		for(str in data1){
			html += '<th>'+data1[str]+'</th>';
		}
	} else {
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
		tbody += '<td>' + e.ETA + '</td>';
		tbody += '<td>' + e.ACTSTAND_CODE + '</td>';
		tbody += '<td>' + e.ACT_ARRANGE_TM + '</td>';
		tbody += '<td>' + e.TIME1 + '</td>';
		tbody += '<td>' + e.huahen + '</td>';
		tbody += '<td>' + e.TIME2 + '</td>';
		tbody += '<td>' + e.TIME3 + '</td>';
		if(typeId==19){
			tbody += '<td>' + e.WATER + '</td>';
		}
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

function initUpdate(data){
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
		html +='<td>'+(ins1 ==null?'':ins1.VEHICLE_NUMBER)+'</td>';
		html +='<td><strong>车辆类型</strong></td>';
		html +='<td>'+(ins1 ==null?'':ins1.TYPENAME)+'</td>';
		html +='<td><strong>随车人员</strong></td>';
		html +='<td>'+(ins1 ==null?'':ins1.SC_PERSON)+'</td>';
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
		html +='<td><strong>车辆状态</strong></td>';
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
		html +='<td><strong>车辆状态</strong></td>';
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
