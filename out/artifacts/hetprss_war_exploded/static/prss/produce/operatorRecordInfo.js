var layer;// 初始化layer模块
var baseTable;// 基础表格
var clickRowId = "";// 当前选中行，以便单选后操作
var clickRow;
var filter;// 用于筛选
var iframe;
var ioFlag;

var first_bdc = ['外观','灯光','转向','车门','卫生','喇叭','刹车','消防锤','仪表','灭火器'];
var second_bdc = ['轮胎','气压表','毛巾','麂皮','掸子','水桶'];
var thrid_bdc = ['公里数','加油量','小时数','燃油数','行驶公里数'];

var first_xlc = ['外观卫生','灭火器','反光镜','油箱盖','轮挡','轮胎','仪表'];
var second_xlc = ['机油','燃油','喇叭','灯光','转向','制动'];
var thrid_xlc = ['里程数','燃油数'];
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})
	new PerfectScrollbar('#container');
	refresh();
});

function query(){
	refresh();
}

function refresh(){
	var bondId = $("input[name=bondId]").val();
	var arr = bondId.split(",");
	for(var i=0;i<arr.length;i++){
		var id = arr[i];
		var first = ('JWBDC'==$("#reskind").val()?first_bdc:first_xlc);
		var second = ('JWBDC'==$("#reskind").val()?second_bdc:second_xlc);
		var thrid = ('JWBDC'==$("#reskind").val()?thrid_bdc:thrid_xlc);
		var jcTable = '';
		jcTable += '<table id="jcTable_'+id+'" class="layui-table tree_table">';
		jcTable += '<thead><tr><th colspan="'+(second.length + thrid.length)+'">创建时间：' + '<span class="jcTable_'+id+'_time"></span>'
		+ '&nbsp;&nbsp;&nbsp;车号：' + '<span class="jcTable_'+id+'_inner"></span>' + '</th></tr><tr><th colspan="11">接车项目</th></tr></thead>';
		jcTable += '<tbody>'+initCheHead(null,1,'jcTable_'+id)+'</tbody>';
		jcTable += '</table>';
		$("#baseTables").append(jcTable);
		var scTable = '';
		scTable += '<table id="scTable_'+id+'" class="layui-table tree_table">';
		scTable += '<thead><tr><th colspan="'+(second.length + thrid.length) + '">收车项目</th></tr></thead>'
		scTable += '<tbody>'+initCheHead(null,2,'scTable_'+id)+'</tbody>';
		scTable += '</table>';
		$("#baseTables").append(scTable);
		
		$("#baseTables").append('<table id="listTable_'+id+'" class="layui-table tree_table">'
				+'<thead><tr><th>序号</th><th>航班日期</th><th>航班号</th><th>注册号</th><th>出发地</th><th>目的地</th><th>操作人</th><th>车号</th><th>ETA\\STD</th>'+
				'<th>任务派发时间</th><th>任务接收时间</th><th>到位时间</th><th>上客时间</th><th>发车时间</th><th>完成时间</th></tr></thead>'
				+'<tbody></tbody></table>');
		
		var gzTable = '';
		gzTable += '<table id="gzTable_'+id+'" class="layui-table tree_table">';
		gzTable += '<thead><tr><th colspan="2">车辆故障记录</th></tr></thead>';
		gzTable += '<tbody><tr><td style="width:50%;" class="gzTable_'+id+'_left">暂无</td><td class="gzTable_'+id+'_right">暂无</td></tr></tbody>';
		gzTable += '</table>';
		$("#baseTables").append(gzTable);

		var tpTable = '';
		tpTable += '<table id="tpTable_'+id+'" class="layui-table tree_table"><tr>';
		tpTable += '<td class="tpTable_'+id+'_l1" width="16.7%"></td>';
		tpTable += '<td class="tpTable_'+id+'_l2" width="16.7%"></td>';
		tpTable += '<td class="tpTable_'+id+'_l3" width="16.7%"></td>';
		tpTable += '<td class="tpTable_'+id+'_r1" width="16.7%"></td>';
		tpTable += '<td class="tpTable_'+id+'_r2" width="16.7%"></td>';
		tpTable += '<td class="tpTable_'+id+'_r3" width="16.7%"></td>';
		tpTable += '</tr></table>';
		$("#baseTables").append(tpTable);
	}
	$.ajax({
		type : 'post',
		url : ctx + "/produce/operator/allInfo",
		data : {
			bondId : bondId,
			reskind : $("#reskind").val()
		},
		dataType : 'json',
		success : function(data) {
			$.each(data.cheArray,function(index,item){
				initChe(item,item.INS_TYPE,(item.INS_TYPE==1?'jcTable_':'scTable_') + item.BOUND_ID);//项目
			})
			$.each(data.queryDataGZ,function(index,item){
				initGZ(item,'gzTable_'+ item.ID);//故障描述
			})
			$.each(data.queryDataTP,function(index,item){
				initTP(item,'tpTable_'+ item.ID);//故障图片
			})
		},
		error : function (msg){
		}
	});
	if('JWBDC'== $("#reskind").val()){
		$.ajax({
			type : 'post',
			url : ctx + "/produce/operator/allInfoHis",
			data : {
				bondId : bondId,
				reskind : $("#reskind").val()
			},
			dataType : 'json',
			success : function(data) {
				$.each(data.list,function(index,item){
					initData(item,'listTable_'+ item.ID);//人员车辆绑定 
				})
			},
			error : function (msg){
			}
		});
	}
}

function initTP(data,tabName){
	var table = $("#"+tabName);
	var num = 0;
	var path='',type='';
	path = data.FILE_PATH; 
	type = data.FILE_TYPE +"";
	var arr1={},arr1_type={};
	
	if(path != ''){
		arr1 = path.split(",");
		arr1_type = type.split(",");
	}
	var num = arr1.length==null?0:arr1.length;
	for(var i=0;i<num;i++){
		var l_type = arr1_type[i];
		var lobj = $('.'+tabName+(data.INS_TYPE==1?'_l':'_r')+l_type);
		lobj.append($(createGZ(arr1[i],l_type)));
	}
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

function initGZ(data,tabName){
	var ins_type = data.INS_TYPE;
	var desc = data.CLGZ_DESC;
	if(desc != null && desc != ''){
		if(ins_type == "1")
			$("."+tabName+"_left").text(desc);
		else 
			$("."+tabName+"_right").text(desc);
	}
}

function initData(e,tabName){
	var table = $("#"+tabName+">tbody");
	var html = ''
	var obj = $('.'+tabName+'_data');
		var in_out = e.IN_OUT_FLAG;
		var tbody = '<tr>';
		tbody += '<td class="'+tabName+'_data">' + (obj.length + 1) + '</td>';
		tbody += '<td>' + e.FLIGHT_DATE + '</td>';
		tbody += '<td>' + e.FLIGHT_NUMBER + '</td>';
		tbody += '<td>' + e.AIRCRAFT_NUMBER + '</td>';
//		比如进港航班，出发地是机位，目的地是到达口，出港航班，出发地是登机口，目的地是机位
		tbody += '<td>' + (in_out.indexOf('D')>-1?e.GATE:e.ACTSTAND_CODE) + '</td>';
		var arrive = (in_out.indexOf('D')>-1?e.ACTSTAND_CODE:e.GATE);
		var fltAttrCode = e.FLT_ATTR_CODE;
		var processName = e.PROCESS_NAME;
		if(fltAttrCode == 'M'){
			var arr = arrive.split(",");
			if(processName =='JWBDCgjjgbdc')//国际，如果流程名称变了，会出现展示问题
				arrive = arr[0];
			else //国内
				arrive = arr.length>1?arr[1]:arr[0];
		}
		tbody += '<td>' + arrive + '</td>';
		tbody += '<td>' + e.NAME + '</td>';
		tbody += '<td>' + e.INNER_NUMBER + '</td>';
		tbody += '<td>' + (in_out.indexOf('D')>-1?e.STD:e.ETA) + '</td>';
		tbody += '<td>' + e.ACT_ARRANGE_TM + '</td>';
		tbody += '<td>' + e.RECEIVTIME + '</td>';
		tbody += '<td>' + (e.TIME1==null?'':e.TIME1) + '</td>';
		tbody += '<td>' + (e.TIME2==null?'':e.TIME2) + '</td>';
		tbody += '<td>' + (e.TIME3==null?'':e.TIME3) + '</td>';
		tbody += '<td>' + (e.TIME4==null?'':e.TIME4) + '</td>';
		tbody += '</tr>';
		html += tbody;
		html += '';
		table.append($(html));
}

function createChe(e,text,type){
	if(type ==1){
		return '<td>'+text+'</td>';
	} else {
		var value = e[text + '_INS_TYPE'];
		return '<td>' + (value==1?'√':'×') + '</td>'
	}
}

function createCheHead(tableName,text,type,insType){
	if(type ==1){
		return '<td>'+text+'</td>';
	} else {
		return '<td class="'+tableName+'_'+text+'">'+(text=='行驶公里数' && insType ==1?'':'&nbsp;')+'</td>';
	}
}

function createCheNum(e,text,type,key,insType){
	var html ='';
	if(type ==1){
		if(text == '行驶公里数'){
			if(insType==1){
				html = '<td></td>';
			} else {
				html = '<td>'+text+'</td>';
			}
		} else {
			html = '<td>'+text+'</td>';
		}
	} else {
		var value = e['VALUE' + key];
		value = value==null?'':value;
		if(text == '行驶公里数'){
			if(insType==1){
				html = '<td></td>';
			} else {
				html = '<td>'+value+'</td>';
			}
		} else {
			html = '<td>'+value+'</td>';
		}
	}
	return html;
}

function initCheHead(data,insType,tableName){
	var table = $("#" + tableName+">tbody");
	var first = ('JWBDC'==$("#reskind").val()?first_bdc:first_xlc);
	var second = ('JWBDC'==$("#reskind").val()?second_bdc:second_xlc);
	var thrid = ('JWBDC'==$("#reskind").val()?thrid_bdc:thrid_xlc);
	var html = ''
	var thead = '<tr>';
	var tbody = '<tr>';
	thead += createChe(data,'项目',1);
	tbody += createCheHead(tableName,'状态',1,insType);
	for(str in first){
		thead += createChe(data,first[str],1);
		tbody +=  createCheHead(tableName,first[str],2,insType);
	}
	thead += '</tr>';
	tbody += '</tr>';
	html += thead;
	html += tbody;
	thead = '<tr>';
	tbody = '<tr>';
	for(str in second){
		thead += createChe(data,second[str],1);
		tbody +=  createCheHead(tableName,second[str],2,insType);
	}
	for(str in thrid){
		thead += createCheNum(data,thrid[str],1,str,insType);
		tbody +=  createCheHead(tableName,thrid[str],2,insType);
	}
	
	thead += '</tr>';
	tbody += '</tr>';
	html += thead;
	html += tbody;
	return html;
}
function initChe(data,insType,tableName){
	var first = ('JWBDC'==$("#reskind").val()?first_bdc:first_xlc);
	var second = ('JWBDC'==$("#reskind").val()?second_bdc:second_xlc);
	var thrid = ('JWBDC'==$("#reskind").val()?thrid_bdc:thrid_xlc);
	if(insType == 1){
		$("." + tableName + "_time").text(data.length==0?'':data.CREATE_DATE);
		$("." + tableName + "_inner").text(data.length==0?'':data.INNER_NUMBER);
	}
	for(str in first){
		var value = data[first[str] + '_INS_TYPE'];
		value = value==1?'√':'×';
		$("."+tableName+"_" + first[str]).text(value)
	}
	for(str in second){
		var value = data[second[str] + '_INS_TYPE'];
		value = value==1?'√':'×';
		$("."+tableName+"_" + second[str]).text( value)
	}
	for(str in thrid){
		var value = data['VALUE' + str];
		value = value==null?'':value;
		if(thrid[str] == '行驶公里数'){
			if(insType==1){
				html = '<td></td>';
			} else {
				html = '<td>'+value+'</td>';
			}
		} else {
			html = '<td>'+value+'</td>';
		}
	}
}