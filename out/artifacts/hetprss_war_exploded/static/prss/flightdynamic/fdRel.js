var layer;
layui.use([ "layer", "form", "element" ], function() {
	layer = layui.layer;
});
var ioFltidIn;
var ioFltidOut;
$(document).ready(function() {
	$("#container").height($(window).height());
	new PerfectScrollbar($("#container")[0]);
	//检索
	$("#searchBtnIO").click(function() {
		var fltNo = $("#fltNoIO").val();//航班号
		var aircraftNo = $("#aircraftNoIO").val();//机号
		if($.trim(fltNo)==""&&$.trim(aircraftNo)==""){
			layer.msg("请输入航班号或机号检索！",{icon:7});
			return false;
		}
		var loading = layer.load(2, {
			shade : [ 0.1, '#000' ]
		});
		$.ajax({
			type : 'post',
			url : ctx + "/fdRel/getFltIO",
			data : {
				'fltNo':fltNo,
				'aircraftNo':aircraftNo,
				timeCond:$("#timeList",parent.document).val()
			},
			dataType : "json",
			async:false,
			error : function() {
				layer.close(loading)
				layer.msg('查询失败！', {
					icon : 2
				});
			},
			success : function(data) {
				layer.close(loading)
				if(data && data.length>0){
					//原表格数据
					var gridData = $("#fdIO").bootstrapTable("getData");
					//ioRel id 集合
					var ioIds = [];
					for(var i=0;i<gridData.length;i++){
						ioIds.push(gridData[i].IOID);
					}
					var newCount = 0;//新查询到的行
					for (var i = 0; i < data.length; i++) {
						var row = data[i];
						//将搜索列表中不存在的航班插入到列表中
						if(row.IOID&&$.inArray(row.IOID, ioIds)<0){
							$("#fdIO").bootstrapTable('append', row);
							newCount++;
						}
					}
					if(newCount==0){
						layer.msg('没有更多航班信息！', {
							icon : 7
						});
						return false;
					}
				}else{
					layer.msg('没有查询到航班信息！', {
						icon : 7
					});
				}
				
			}
		});
	});
	if(fltNum){
		$("#aircraftNoIO").val(fltNum);
	}
	//拆分配对
	$("#delRelBtn").click(function() {
		var rows = $('#fdIO').bootstrapTable('getData');
		if (!rows||rows.length==0) {
			layer.msg("请选择要拆分的航班！", {
				icon : 7
			});
			return false;
		}
		rows = $.extend(true,[],rows);
		for(var i=0;i<rows.length;i++){
			var row = rows[i];
			//将进港航班插入到进港列表
			if(row.IN_FLTID){
				$('#fdI').bootstrapTable('removeByUniqueId',row.IN_FLTID);
				$("#fdI").bootstrapTable('append',{
					FLTID:row.IN_FLTID,
					FLTNO:row.IN_FLTNO,
					DEPART_APT3CODE:row.DEPART_APT3CODE,
					AIRCRAFTNUMBER:row.AIRCRAFT_NUMBER,
					ACTTYPE_CODE:row.ACTTYPE_CODE,
					ACTSTAND_CODE:row.ACTSTAND_CODE,
					ETD:row.IN_ETD,
					ETA:row.IN_ETA,
					STATUS:row.IN_STATUS,
					ALN_3CODE:row.IN_ALN_3CODE,
					INOUTFLAG:row.IN_FLAG,
					FLTDATE:row.IN_FLTDATE
				});
			}
			//将出港航班插入到出港列表
			if(row.OUT_FLTID){
				$('#fdO').bootstrapTable('removeByUniqueId',row.OUT_FLTID);
				$("#fdO").bootstrapTable('append',{
					FLTID:row.OUT_FLTID,
					FLTNO:row.OUT_FLTNO,
					ARRIVAL_APT3CODE:row.ARRIVAL_APT3CODE,
					AIRCRAFTNUMBER:row.AIRCRAFT_NUMBER,
					ACTTYPE_CODE:row.ACTTYPE_CODE,
					ACTSTAND_CODE:row.ACTSTAND_CODE,
					GATE:row.GATE,
					ETD:row.OUT_ETD,
					ETA:row.OUT_ETA,
					STATUS:row.OUT_STATUS,
					ALN_3CODE:row.OUT_ALN_3CODE,
					INOUTFLAG:row.OUT_FLAG,
					FLTDATE:row.OUT_FLTDATE
				});
			}
			$('#fdIO').bootstrapTable('removeByUniqueId',row.IOID);
		}
	});
	//选中配对
	$('#checkRelBtn').click(function() {
		var dataI = $("#fdI").bootstrapTable("getSelections");
		var dataO = $("#fdO").bootstrapTable("getSelections");
		if (dataI.length == 0&&dataO.length == 0) {
			layer.msg('您还未选择要配对的航班！', {
				icon : 7
			});
			return false;
		} else if(dataI.length > 0 && dataO.length > 0 && dataI.length != dataO.length){
			layer.msg('请选择1条进港、1条出港航班进行配对！', {
				icon : 7
			});
			return false;
		}
		if(dataI.length==1 && dataO.length==1){//选择了一条进港一条出港时校验机号是否相同
			var inAircraftNO = dataI[0].AIRCRAFTNUMBER;//进港机号
			var outAircraftNO = dataO[0].AIRCRAFTNUMBER;//出港机号
			if(inAircraftNO!=outAircraftNO){
				layer.msg('进、出港航班机号不一致，无法配对!', {
					icon : 7
				});
				return false;
			} else {
				var inFlight = dataI[0];//进港航班
				var outFlight = dataO[0];//出港航班
				var inFltId = inFlight.FLTID;
				var outFltId = outFlight.FLTID;
				$("#fdNewIO").bootstrapTable("append",{
					IOID:inFltId+""+outFltId,
					IN_FLTID:inFltId,
					OUT_FLTID:outFltId,
					AIRCRAFT_NUMBER:inFlight.AIRCRAFTNUMBER,
					ACTTYPE_CODE:inFlight.ACTTYPE_CODE,
					ACTSTAND_CODE:inFlight.ACTSTAND_CODE,
					GATE:outFlight.GATE,
					IN_FLTNO:inFlight.FLTNO,
					DEPART_APT3CODE:inFlight.DEPART_APT3CODE,
					IN_ETD:inFlight.ETD,
					IN_ETA:inFlight.ETA,
					IN_STATUS:inFlight.STATUS,
					OUT_FLTNO:outFlight.FLTNO,
					ARRIVAL_APT3CODE:outFlight.ARRIVAL_APT3CODE,
					OUT_ETD:outFlight.ETD,
					OUT_ETA:outFlight.ETA,
					OUT_STATUS:outFlight.STATUS,
					OUT_ALN_3CODE:outFlight.ALN_3CODE,
					IN_ALN_3CODE:inFlight.ALN_3CODE,
					OUT_FLAG:outFlight.INOUTFLAG,
					IN_FLAG:inFlight.INOUTFLAG,
					IN_FLTDATA:inFlight.FLTDATE,
					OUT_FLTDATA:outFlight.FLTDATE,
				});
				$("#fdI").bootstrapTable("removeByUniqueId",inFltId);
				$("#fdO").bootstrapTable("removeByUniqueId",outFltId);
			}
		} else if(dataI.length>0){//仅选择进港列表的数据时做为单进插入
			var rows = $.extend(true,[],dataI);
			for(var i=0;i<rows.length;i++){
				var inFlight = rows[i];//进港航班
				var inFltId = inFlight.FLTID;
				$("#fdNewIO").bootstrapTable("append",{
					IOID:inFltId,
					IN_FLTID:inFltId,
					OUT_FLTID:"",
					AIRCRAFT_NUMBER:inFlight.AIRCRAFTNUMBER,
					ACTTYPE_CODE:inFlight.ACTTYPE_CODE,
					ACTSTAND_CODE:inFlight.ACTSTAND_CODE,
					GATE:"",
					IN_FLTNO:inFlight.FLTNO,
					DEPART_APT3CODE:inFlight.DEPART_APT3CODE,
					IN_ETD:inFlight.ETD,
					IN_ETA:inFlight.ETA,
					IN_STATUS:inFlight.STATUS,
					OUT_FLTNO:"",
					ARRIVAL_APT3CODE:"",
					OUT_ETD:"",
					OUT_ETA:"",
					OUT_STATUS:"",
					OUT_ALN_3CODE:"",
					IN_ALN_3CODE:inFlight.ALN_3CODE,
					OUT_FLAG:"",
					IN_FLAG:inFlight.INOUTFLAG,
					IN_FLTDATE:inFlight.FLTDATE
				});
				$("#fdI").bootstrapTable("removeByUniqueId",inFltId);
			}
		} else if(dataO.length>0){//仅选择出港时做为单出插入
			var rows = $.extend(true,[],dataO);
			for(var i=0;i<rows.length;i++){
				var outFlight = rows[i];//出港航班
				var outFltId = outFlight.FLTID;
				$("#fdNewIO").bootstrapTable("append",{
					IOID:outFltId,
					IN_FLTID:"",
					OUT_FLTID:outFltId,
					AIRCRAFT_NUMBER:outFlight.AIRCRAFTNUMBER,
					ACTTYPE_CODE:outFlight.ACTTYPE_CODE,
					ACTSTAND_CODE:outFlight.ACTSTAND_CODE,
					GATE:outFlight.GATE,
					IN_FLTNO:"",
					DEPART_APT3CODE:"",
					IN_ETD:"",
					IN_ETA:"",
					IN_STATUS:"",
					OUT_FLTNO:outFlight.FLTNO,
					ARRIVAL_APT3CODE:outFlight.ARRIVAL_APT3CODE,
					OUT_ETD:outFlight.ETD,
					OUT_ETA:outFlight.ETA,
					OUT_STATUS:outFlight.STATUS,
					OUT_ALN_3CODE:outFlight.ALN_3CODE,
					IN_ALN_3CODE:"",
					OUT_FLAG:outFlight.INOUTFLAG,
					IN_FLAG:"",
					OUT_FLTDATE:outFlight.FLTDATE
				});
				$("#fdO").bootstrapTable("removeByUniqueId",outFltId);
			}
		}
	});
	//自动配对
	$('#autoRelBtn').click(function() {
		var dataI = $("#fdI").bootstrapTable("getData");
		var dataO = $("#fdO").bootstrapTable("getData");
		if (dataI.length == 0&&dataO.length == 0) {
			layer.msg('无可配对航班', {
				icon :7
			});
			return false;
		}
		var loading = layer.load(2, {
			shade : [ 0.1, '#000' ]
		});
		var fltData = [];
		for(var i=0;i<dataI.length;i++){
			var row = dataI[i];
			if(row&&row.FLTID){
				fltData.push(row);
			}
		}
		for(var i=0;i<dataO.length;i++){
			var row = dataO[i];
			if(row&&row.FLTID){
				fltData.push(row);
			}
		}
		$.ajax({
			type : 'post',
			url : ctx + "/fdRel/autoRel",
			data : {
				fltData:JSON.stringify(fltData)
			},
			async:false,
			dataType : "json",
			error : function() {
				layer.msg('配对失败！', {
					icon : 2
				});
				layer.close(loading)
			},
			success : function(data) {
				layer.close(loading);
				if(data){
					for(var i=0;i<data.length;i++){
						var dataItem = data[i];
						var inFltId = dataItem.inFltId;//进港航班id
						var outFltId = dataItem.outFltId;//出港航班id
						//有配对
						if(inFltId&&outFltId){
							var inFlight = $("#fdI").bootstrapTable("getRowByUniqueId",inFltId);//进港航班
							var outFlight = $("#fdO").bootstrapTable("getRowByUniqueId",outFltId);//出港航班
							$("#fdNewIO").bootstrapTable("append",{
								IOID:inFltId+""+outFltId,
								IN_FLTID:inFltId,
								OUT_FLTID:outFltId,
								AIRCRAFT_NUMBER:inFlight.AIRCRAFTNUMBER,
								ACTTYPE_CODE:inFlight.ACTTYPE_CODE,
								ACTSTAND_CODE:inFlight.ACTSTAND_CODE,
								GATE:outFlight.GATE,
								IN_FLTNO:inFlight.FLTNO,
								DEPART_APT3CODE:inFlight.DEPART_APT3CODE,
								IN_ETD:inFlight.ETD,
								IN_ETA:inFlight.ETA,
								IN_STATUS:inFlight.STATUS,
								OUT_FLTNO:outFlight.FLTNO,
								ARRIVAL_APT3CODE:outFlight.ARRIVAL_APT3CODE,
								OUT_ETD:outFlight.ETD,
								OUT_ETA:outFlight.ETA,
								OUT_STATUS:outFlight.STATUS,
								OUT_ALN_3CODE:outFlight.ALN_3CODE,
								IN_ALN_3CODE:inFlight.ALN_3CODE,
								OUT_FLAG:outFlight.INOUTFLAG,
								IN_FLAG:inFlight.INOUTFLAG,
								IN_FLTDATE:inFlight.FLTDATE,
								OUT_FLTDATE:outFlight.FLTDATE
							});
							$("#fdI").bootstrapTable("removeByUniqueId",inFltId);
							$("#fdO").bootstrapTable("removeByUniqueId",outFltId);
						} else if(inFltId&&!outFltId){//单进
							var inFlight = $("#fdI").bootstrapTable("getRowByUniqueId",inFltId);//进港航班
							$("#fdNewIO").bootstrapTable("append",{
								IOID:inFltId,
								IN_FLTID:inFltId,
								OUT_FLTID:"",
								AIRCRAFT_NUMBER:inFlight.AIRCRAFTNUMBER,
								ACTTYPE_CODE:inFlight.ACTTYPE_CODE,
								ACTSTAND_CODE:inFlight.ACTSTAND_CODE,
								GATE:"",
								IN_FLTNO:inFlight.FLTNO,
								DEPART_APT3CODE:inFlight.DEPART_APT3CODE,
								IN_ETD:inFlight.ETD,
								IN_ETA:inFlight.ETA,
								IN_STATUS:inFlight.STATUS,
								OUT_FLTNO:"",
								ARRIVAL_APT3CODE:"",
								OUT_ETD:"",
								OUT_ETA:"",
								OUT_STATUS:"",
								OUT_ALN_3CODE:"",
								IN_ALN_3CODE:inFlight.ALN_3CODE,
								OUT_FLAG:"",
								IN_FLAG:inFlight.INOUTFLAG,
								IN_FLTDATE:inFlight.FLTDATE
							});
							$("#fdI").bootstrapTable("removeByUniqueId",inFltId);
						} else if(outFltId&&!inFltId){//单出
							var outFlight = $("#fdO").bootstrapTable("getRowByUniqueId",outFltId);//出港航班
							$("#fdNewIO").bootstrapTable("append",{
								IOID:outFltId,
								IN_FLTID:"",
								OUT_FLTID:outFltId,
								AIRCRAFT_NUMBER:outFlight.AIRCRAFTNUMBER,
								ACTTYPE_CODE:outFlight.ACTTYPE_CODE,
								ACTSTAND_CODE:outFlight.ACTSTAND_CODE,
								GATE:outFlight.GATE,
								IN_FLTNO:"",
								DEPART_APT3CODE:"",
								IN_ETD:"",
								IN_ETA:"",
								IN_STATUS:"",
								OUT_FLTNO:outFlight.FLTNO,
								ARRIVAL_APT3CODE:outFlight.ARRIVAL_APT3CODE,
								OUT_ETD:outFlight.ETD,
								OUT_ETA:outFlight.ETA,
								OUT_STATUS:outFlight.STATUS,
								OUT_ALN_3CODE:outFlight.ALN_3CODE,
								IN_ALN_3CODE:"",
								OUT_FLAG:outFlight.INOUTFLAG,
								IN_FLAG:"",
								OUT_FLTDATE:outFlight.FLTDATE
							});
							$("#fdO").bootstrapTable("removeByUniqueId",outFltId);
						}
					}
				}
			}
		});
	});
	//拖动配对
	$('#dragRelBtn').click(function() {
		var dataI = $("#fdI").bootstrapTable("getData");
		var dataO = $("#fdO").bootstrapTable("getData");
		if (dataI.length == 0&&dataO.length == 0) {
			layer.msg('无可配对航班', {
				icon : 7
			});
			return false;
		} else {
			var maxLength = dataI.length;
			if(dataO.length>maxLength){
				maxLength = dataO.length;
			}
			dataI = $.extend(true,[],dataI);
			dataO = $.extend(true,[],dataO);
			var errorInfo = "";
			var result = [];
			for(var i=0;i<maxLength;i++){
				var inFltId = "";
				var outFltId = "";
				var inAircraftNO = "";//进港机号
				var outAircraftNO = "";//出港机号
				var inFltNo = "";//进港航班号
				var outFltNo = "";//出港航班号
				var fltInfo = {
						IOID:"",
						IN_FLTID:"",
						OUT_FLTID:"",
						AIRCRAFT_NUMBER:"",
						ACTTYPE_CODE:"",
						ACTSTAND_CODE:"",
						GATE:"",
						IN_FLTNO:"",
						DEPART_APT3CODE:"",
						IN_ETD:"",
						IN_ETA:"",
						IN_STATUS:"",
						OUT_FLTNO:"",
						ARRIVAL_APT3CODE:"",
						OUT_ETD:"",
						OUT_ETA:"",
						OUT_STATUS:"",
						OUT_ALN_3CODE:"",
						IN_ALN_3CODE:"",
						OUT_FLAG:"",
						IN_FLAG:"",
						IN_FLTDATE:"",
						OUT_FLTDATE:""
				};
				//进港数据
				if(i<dataI.length){
					var inFlight = dataI[i];
					inFltId = inFlight.FLTID;
					inAircraftNO = inFlight.AIRCRAFTNUMBER;
					inFltNo = inFlight.FLTNO;
					fltInfo.IN_FLTID = inFltId;
					fltInfo.AIRCRAFT_NUMBER = inFlight.AIRCRAFTNUMBER;
					fltInfo.ACTTYPE_CODE = inFlight.ACTTYPE_CODE;
					fltInfo.ACTSTAND_CODE = inFlight.ACTSTAND_CODE;
					fltInfo.IN_FLTNO = inFlight.FLTNO;
					fltInfo.DEPART_APT3CODE = inFlight.DEPART_APT3CODE;
					fltInfo.IN_ETD = inFlight.ETD;
					fltInfo.IN_ETA = inFlight.ETA;
					fltInfo.IN_STATUS = inFlight.STATUS;
					fltInfo.IN_ALN_3CODE = inFlight.ALN_3CODE;
					fltInfo.IN_FLAG = inFlight.INOUTFLAG;
					fltInfo.IN_FLTDATE = inFlight.FLTDATE;
				}
				//出港数据
				if(i<dataO.length){
					var outFlight = dataO[i];
					outFltId = outFlight.FLTID;
					outAircraftNO = outFlight.AIRCRAFTNUMBER;
					outFltNo = outFlight.FLTNO;
					
					fltInfo.OUT_FLTID = outFltId;
					fltInfo.AIRCRAFT_NUMBER = outFlight.AIRCRAFTNUMBER;
					fltInfo.ACTTYPE_CODE = fltInfo.ACTTYPE_CODE?fltInfo.ACTTYPE_CODE:outFlight.ACTTYPE_CODE;
					fltInfo.ACTSTAND_CODE = fltInfo.ACTSTAND_CODE?fltInfo.ACTSTAND_CODE:outFlight.ACTSTAND_CODE;
					fltInfo.GATE = outFlight.GATE;
					fltInfo.OUT_FLTNO = outFlight.FLTNO;
					fltInfo.ARRIVAL_APT3CODE = outFlight.ARRIVAL_APT3CODE;
					fltInfo.OUT_ETD = outFlight.ETD;
					fltInfo.OUT_ETA = outFlight.ETA;
					fltInfo.OUT_STATUS = outFlight.STATUS;
					fltInfo.OUT_ALN_3CODE = outFlight.ALN_3CODE;
					fltInfo.OUT_FLAG = outFlight.INOUTFLAG;
					fltInfo.OUT_FLTDATE = outFlight.FLTDATE;
				}
				if(inFltId&&outFltId&&inAircraftNO!=outAircraftNO){
					errorInfo+="<i class='fa fa-exclamation-circle text-red'>&nbsp;</i>航班"+inFltNo+"/"+outFltNo+"机号不一致，无法配对</br>";
				} else {
					fltInfo.IOID = inFltId+""+outFltId;
					result.push($.extend(true,{},fltInfo));
				}
			}
			if(errorInfo){
				layer.alert(errorInfo);
				return false;
			} else if(result.length>0){
				for(var i=0;i<result.length;i++){
					var row = result[i];
					$("#fdNewIO").bootstrapTable("append",row);
					if(row.IN_FLTID){
						$("#fdI").bootstrapTable("removeByUniqueId",row.IN_FLTID);
					}
					if(row.OUT_FLTID){
						$("#fdO").bootstrapTable("removeByUniqueId",row.OUT_FLTID);
					}
				}
			}
		}
	});
	// 删除
	$('#delBtn').click(function(){
		var rows = $("#fdIO").bootstrapTable('getSelections')
	    if (!rows||rows.length == 0) {
	        layer.msg("请选择要删除的行!",{icon:7});
	        return false;
	    }
		for(var i=0;i<rows.length;i++){
			$('#fdIO').bootstrapTable('removeByUniqueId',rows[i].IOID);
		}
	  });
	//撤销配对
	$('#revRelBtn').click(function(){
		var rows = $("#fdNewIO").bootstrapTable('getSelections');
		if(rows.length==0){
			rows = $("#fdNewIO").bootstrapTable('getData');
		}
		if (!rows||rows.length==0) {
	        layer.msg("您未选择要撤销配对的航班！",{icon:7});
	        return false;
	    }
		rows = $.extend(true,[],rows);
		for(var i=0;i<rows.length;i++){
			var row = rows[i];
			if(row.IN_FLTID){//进港
				$("#fdI").bootstrapTable('append',{
					FLTID:row.IN_FLTID,
					FLTNO:row.IN_FLTNO,
					DEPART_APT3CODE:row.DEPART_APT3CODE,
					AIRCRAFTNUMBER:row.AIRCRAFT_NUMBER,
					ACTTYPE_CODE:row.ACTTYPE_CODE,
					ACTSTAND_CODE:row.ACTSTAND_CODE,
					ETD:row.IN_ETD,
					ETA:row.IN_ETA,
					STATUS:row.IN_STATUS,
					ALN_3CODE:row.IN_ALN_3CODE,
					INOUTFLAG:row.IN_FLAG,
					FLTDATE:row.IN_FLTDATE
				});
			}
			if(row.OUT_FLTID){//出港
				$("#fdO").bootstrapTable('append',{
					FLTID:row.OUT_FLTID,
					FLTNO:row.OUT_FLTNO,
					ARRIVAL_APT3CODE:row.ARRIVAL_APT3CODE,
					AIRCRAFTNUMBER:row.AIRCRAFT_NUMBER,
					ACTTYPE_CODE:row.ACTTYPE_CODE,
					ACTSTAND_CODE:row.ACTSTAND_CODE,
					GATE:row.GATE,
					ETD:row.OUT_ETD,
					ETA:row.OUT_ETA,
					STATUS:row.OUT_STATUS,
					ALN_3CODE:row.OUT_ALN_3CODE,
					INOUTFLAG:row.OUT_FLAG,
					FLTDATE:row.OUT_FLTDATE
				});
			}
			$('#fdNewIO').bootstrapTable('removeByUniqueId',row.IOID);
		}
	 });
	//批量修改机号
	$("#fltNosChargeBtn").click(function(){
		var inFlight = $("#fdI").bootstrapTable("getSelections");
		var outFlight = $("#fdO").bootstrapTable("getSelections");
		if(inFlight.length==0&&outFlight.length==0){
			layer.msg("请选择要修改的航班！",{icon:7});
			return false;
		}
		layer.open({
			 type: 1,
			 title:"批量机号更新",
			 btn:["保存","取消"],
			 content: $("#chgAircraftNoForm"),
			 btn1: function(index, layero){
				 var nweAircraftNo = $("#nweAircraftNo").val();
				 if(!$.trim(nweAircraftNo)){
					 layer.tips("请输入机号", $("#nweAircraftNo"),{tips:[3,"#f39c12"]});
				 } else {
					var loading = layer.load(2, {
						shade : [ 0.1, '#000' ]
					});
					var valid = [];//验证结果
					var actType = "";//修改后机号对应机型
					for(var i=0;i<inFlight.length;i++){
						var row = inFlight[i];
						$.ajax({
							type : 'POST',
							url : ctx + "/fdRel/validAircraftNo",
							async : false,
							data : {
								aftNum : nweAircraftNo,
								fltid:row.FLTID,
								ioType:"A"
							},
							dataType:"json",
							error : function() {
								layer.close(loading);
								layer.msg('机号校验请求失败，请重试！', {
									icon : 2
								});
							},
							success : function(data) {
								if(data&&data.status!=1){
									data.fltno = row.FLTNO;
									valid.push(data);
								} else if(!actType){
									actType = data.actType;
								}
							}
						});
					}
					for(var i=0;i<outFlight.length;i++){
						var row = outFlight[i];
						$.ajax({
							type : 'POST',
							url : ctx + "/fdRel/validAircraftNo",
							async : false,
							data : {
								aftNum : nweAircraftNo,
								fltid:row.FLTID,
								ioType:"D"
							},
							dataType:"json",
							error : function() {
								layer.close(loading);
								layer.msg('机号校验请求失败，请重试！', {
									icon : 2
								});
							},
							success : function(data) {
								if(data&&data.status!=1){
									data.fltno = row.FLTNO;
									valid.push(data);
								} else if(!actType){
									actType = data.actType;
								}
							}
						});
					}
					//验证失败
					if(valid.length>0){
						var errors = "";
						for(var i=0;i<valid.length;i++){
							var status = valid[i].status;
							if(status==-2){
								errors = valid[i].error;
								break;
							} else {
								errors += valid[i].error+"<br>";
							}
						}
						layer.alert(errors);
					} else {
						var rowIndex = {};
						$("#fdI tr.selected").each(function(){
							var fltid = $(this).data("uniqueid");
							var index = $(this).data("index");
							rowIndex[fltid] = index;
						});
						$("#fdO tr.selected").each(function(){
							var fltid = $(this).data("uniqueid");
							var index = $(this).data("index");
							rowIndex[fltid] = index;
						});
						for(var i=0;i<inFlight.length;i++){
							var row = inFlight[i];
							//机号
							$("#fdI").bootstrapTable("updateCell", {
								index : rowIndex[row.FLTID],
								field : "AIRCRAFTNUMBER",
								value : nweAircraftNo
							});
							//机型
							$("#fdI").bootstrapTable("updateCell", {
								index : rowIndex[row.FLTID],
								field : "ACTTYPE_CODE",
								value : actType
							});
						}
						for(var i=0;i<outFlight.length;i++){
							var row = outFlight[i];
							//机号
							$("#fdO").bootstrapTable("updateCell", {
								index : rowIndex[row.FLTID],
								field : "AIRCRAFTNUMBER",
								value : nweAircraftNo
							});
							//机型
							$("#fdO").bootstrapTable("updateCell", {
								index : rowIndex[row.FLTID],
								field : "ACTTYPE_CODE",
								value : actType
							});
						}
						layer.close(index);
					}
					layer.close(loading);
				 }
					$("#fdI tr[class='selected']").find("td[class='bs-checkbox ']").find('input').removeAttr("checked");
					$("#fdI tr[class='selected']").removeClass("selected");
					$("#fdO tr[class='selected']").find("td[class='bs-checkbox ']").find('input').removeAttr("checked");
					$("#fdO tr[class='selected']").removeClass("selected");
					
			 }
		});
	});
	initQueryGrid();
	initSplitGrid();
	initRelGrid();
});
/**
 * 搜索列表（页面最上方）
 */
function initQueryGrid(){
	var fltIds = $("#fltIds").val();
	$("#fdIO").bootstrapTable({
		url : ctx + "/fdRel/getIOSet?fltIds="+fltIds+"&aircraftNo="+(fltNum==null?"":fltNum),
		method : "get",
		toolbar : "#toolbarIO",
		uniqueId:"IOID",
		pagination : false,
		showRefresh : false,
		search : false,
		clickToSelect : false,
		searchOnEnterKey : true,
		cache:false,
		undefinedText : "",
		queryParams:function(params){
			//父页面航班范围下拉条件
			var timeCond = $("#timeList",parent.document).val();
			params.timeCond = timeCond;
			return params;
		},
		columns :[ {
			field : "checkbox",
			checkbox : true,
			sortable : false,
			editable : false
		},  {
			field : "order",
			title : "序号",
			sortable : false,
			editable : false,
			width:40,
			formatter : function(value, row, index) {
				return index + 1;
			}
		}, {
			field : "IOID",
			visible : false
		},{
			field : "IN_FLTID",
			visible : false
		},{
			field : "OUT_FLTID",
			visible : false
		},{
			field : "IN_FLTDATE",
			visible : false
		},{
			field : "OUT_FLTDATE",
			visible : false
		},{
			field : "AIRCRAFT_NUMBER",
			title : "机号",
			align : 'center'
		}, {
			field : "ACTTYPE_CODE",
			title : '机型',
			align : 'center'
		}, {
			field : "ACTSTAND_CODE",
			title : '机位',
			align : 'center'
		},{
			field : "GATE",
			title : "登机口",
			align : 'center'
		},{
			field : "IN_FLTNO",
			title : "进港航班号",
			align : 'center'
		},{
			field : "DEPART_APT3CODE",
			title : "起场",
			align : 'center',
			editable : {
				type : "select2",
				disabled:true,
				source : airports
			}
		}, {
			field : "IN_ETD",
			title : '预起',
			align : 'center',
			formatter:function(value,row,index){
				if(value&&value.length==16){
					value = toDayTime(value);
				} 
				return value;
			}
		}, {
			field : "IN_ETA",
			title : '预落',
			align : 'center',
			formatter:function(value,row,index){
				if(value&&value.length==16){
					value = toDayTime(value);
				} 
				return value;
			}
		}, {
			field : "IN_STATUS",
			title : '状态',
			align : 'center',
			editable : {
				type : "select2",
				disabled:true,
				source : acfStatus
			}
		},{
			field : "OUT_FLTNO",
			title : "出港航班号",
			align : 'center'
		}, {
			field : "ARRIVAL_APT3CODE",
			title : "落场",
			align : 'center',
			editable : {
				type : "select2",
				disabled:true,
				source : airports
			}
		} ,{
			field : "OUT_ETD",
			title : '预起',
			align : 'center',
			formatter:function(value,row,index){
				if(value&&value.length==16){
					value = toDayTime(value);
				} 
				return value;
			}
		}, {
			field : "OUT_ETA",
			title : '预落',
			align : 'center',
			formatter:function(value,row,index){
				if(value&&value.length==16){
					value = toDayTime(value);
				} 
				return value;
			}
		},{
			field : "OUT_STATUS",
			title : '状态',
			align : 'center',
			editable : {
				type : "select2",
				disabled:true,
				source : acfStatus
			}
		}]
	});
}
/**
 * 配对拆分表格（页面中间）
 */
function initSplitGrid(){
	//进港
	$("#fdI").bootstrapTable({
		data : "",
		method : "get",
		toolbar : "#toolbarI",
		pagination : false,
		showRefresh : false,
		search : false,
		clickToSelect : false,
		searchOnEnterKey : true,
		undefinedText : "",
		uniqueId:"FLTID",
		cache:false,
		singleSelect:false,
		columns : [ {
			field : "checkbox",
			checkbox : true,
			sortable : false,
			editable : false
		},  {
			field : "order",
			title : "序号",
			sortable : false,
			editable : false,
			width:40,
			formatter : function(value, row, index) {
				return index + 1;
			}
		},{
			field : "FLTID",
			visible : false
		},{
			field : "FLTDATE",
			visible : false
		},{
			field : "INOUTFLAG",
			title:"进出港标识",
			visible : false
		},{
			field : "ALN_3CODE",
			title:"航空公司三字码",
			visible : false
		}, {
			field : "FLTNO",
			title : "进港航班号",
			align : 'center'
		}, {
			field : "DEPART_APT3CODE",
			title : "起场",
			align : 'center',
			editable : {
				type : "select2",
				disabled:true,
				source : airports
			}
		}, {
			field : "AIRCRAFTNUMBER",
			title : "机号",
			align : 'center',
			width:100,
			editable : {
				type:"text"
				//mode:"poup"
			}
		}, {
			field : "ACTTYPE_CODE",
			title : '机型',
			align : 'center'
		}, {
			field : "ACTSTAND_CODE",
			title : '机位',
			align : 'center'
		}, {
			field : "ETD",
			title : '预起',
			align : 'center',
			formatter:function(value,row,index){
				if(value&&value.length==16){
					value = toDayTime(value);
				} 
				return value;
			}
		}, {
			field : "ETA",
			title : '预落',
			align : 'center',
			formatter:function(value,row,index){
				if(value&&value.length==16){
					value = toDayTime(value);
				} 
				return value;
			}
		}, {
			field : "STATUS",
			title : '状态',
			align : 'center',
			editable : {
				type : "select2",
				disabled:true,
				source : acfStatus
			}
		}],
		onEditableSave : function(field, row, oldValue, $el) {
			var rowIndx = $el.parent().parent().index();
			var value = row[field];
			//修改机号
			if("AIRCRAFTNUMBER"==field&&value!=oldValue){
				var loading = layer.load(2, {
					shade : [ 0.1, '#000' ]
				});
				$.ajax({
					type : 'POST',
					url : ctx + "/fdRel/validAircraftNo",
					async : false,
					data : {
						aftNum : value,
						fltid:row.FLTID,
						ioType:"A"
					},
					dataType:"json",
					error : function() {
						layer.close(loading);
						layer.msg('修改失败！', {
							icon : 2
						});
					},
					success : function(data) {
						layer.close(loading);
						if(data&&data.status==1){
							$("#fdI").bootstrapTable("updateCell", {
								index : rowIndx,
								field : "ACTTYPE_CODE",
								value : data.actType
							});
						} else {
							$("#fdI").bootstrapTable("updateCell", {
								index : rowIndx,
								field : "AIRCRAFTNUMBER",
								value : oldValue
							});
							layer.msg(data.error,{icon:7});
						}
					}
				});
			}
		}
	});
	//出港
	$("#fdO").bootstrapTable({
		data : "",
		method : "get",
		toolbar : "#toolbarO",
		pagination : false,
		showRefresh : false,
		search : false,
		clickToSelect : false,
		searchOnEnterKey : true,
		undefinedText : "",
		uniqueId:"FLTID",
		cache:false,
		singleSelect:false,
		columns : [ {
			field : "checkbox",
			checkbox : true,
			sortable : false,
			editable : false
		},{
			field : "order",
			title : "序号",
			sortable : false,
			editable : false,
			width:40,
			formatter : function(value, row, index) {
				return index + 1;
			}
		},{
			field : "FLTID",
			visible : false
		},{
			field : "FLTDATE",
			visible : false
		},{
			field : "INOUTFLAG",
			title:"进出港标识",
			visible : false
		},{
			field : "ALN_3CODE",
			title:"航空公司三字码",
			visible : false
		},{
			field : "FLTNO",
			title : "出港航班号",
			align : 'center'
		}, {
			field : "ARRIVAL_APT3CODE",
			title : "落场",
			align : 'center',
			editable : {
				type : "select2",
				disabled:true,
				source : airports
			}
		}, {
			field : "AIRCRAFTNUMBER",
			title : "机号",
			align : 'center',
			width:100,
			editable : {
				type:"text"
				//mode:"poup"
			}
		}, {
			field : "ACTTYPE_CODE",
			title : '机型',
			align : 'center'
		}, {
			field : "ACTSTAND_CODE",
			title : '机位',
			align : 'center'
		}, {
			field : "GATE",
			title : '登机口',
			align : 'center'
		}, {
			field : "ETD",
			title : '预起',
			align : 'center',
			formatter:function(value,row,index){
				if(value&&value.length==16){
					value = toDayTime(value);
				} 
				return value;
			}
		}, {
			field : "ETA",
			title : '预落',
			align : 'center',
			formatter:function(value,row,index){
				if(value&&value.length==16){
					value = toDayTime(value);
				} 
				return value;
			}
		},{
			field : "STATUS",
			title : '状态',
			align : 'center',
			editable : {
				type : "select2",
				disabled:true,
				source : acfStatus
			}
		}],
		onEditableSave : function(field, row, oldValue, $el) {
			var rowIndx = $el.parent().parent().index();
			var value = row[field];
			//修改机号
			if("AIRCRAFTNUMBER"==field&&value!=oldValue){
				var loading = layer.load(2, {
					shade : [ 0.1, '#000' ]
				});
				$.ajax({
					type : 'POST',
					url : ctx + "/fdRel/validAircraftNo",
					async : false,
					data : {
						aftNum : value,
						fltid:row.FLTID,
						ioType:"D"
					},
					dataType:"json",
					error : function() {
						layer.close(loading);
						layer.msg('修改失败！', {
							icon : 2
						});
					},
					success : function(data) {
						layer.close(loading);
						if(data&&data.status==1){
							$("#fdO").bootstrapTable("updateCell", {
								index : rowIndx,
								field : "ACTTYPE_CODE",
								value : data.actType
							});
						} else {
							$("#fdO").bootstrapTable("updateCell", {
								index : rowIndx,
								field : "AIRCRAFTNUMBER",
								value : oldValue
							});
							layer.msg(data.error,{icon:7});
						}
					}
				});
			}
		}
	});
}
/**
 * 配对表格（页面最下方）
 */
function initRelGrid(){
	$("#fdNewIO").bootstrapTable({
		uniqueId:"IOID",
		data : "",
		method : "get",
		toolbar : "#toolbarNewIO",
		toolbarAlign:"center",
		pagination : false,
		showRefresh : false,
		search : false,
		clickToSelect : false,
		undefinedText : "",
		cache:false,
		columns : [ {
			field : "checkbox",
			checkbox : true,
			sortable : false,
			editable : false
		},  {
			field : "order",
			title : "序号",
			sortable : false,
			editable : false,
			width:40,
			formatter : function(value, row, index) {
				return index + 1;
			}
		}, {
			field : "IOID",
			visible : false
		},{
			field : "IN_FLTID",
			visible : false
		},{
			field : "OUT_FLTID",
			visible : false
		},{
			field : "IN_FLTDATE",
			visible : false
		},{
			field : "OUT_FLTDATE",
			visible : false
		},{
			field : "AIRCRAFT_NUMBER",
			title : "机号",
			align : 'center'
		}, {
			field : "ACTTYPE_CODE",
			title : '机型',
			align : 'center'
		}, {
			field : "ACTSTAND_CODE",
			title : '机位',
			align : 'center'
		},{
			field : "GATE",
			title : "登机口",
			align : 'center',
			//width:100,
			editable : {
				type : "select2",
				disabled:true,
				//mode:"poup",
				source : gates
			}
		},{
			field : "IN_FLTNO",
			title : "进港航班号",
			align : 'center'
		},{
			field : "DEPART_APT3CODE",
			title : "起场",
			align : 'center',
			editable : {
				type : "select2",
				disabled:true,
				source : airports
			}
		}, {
			field : "IN_ETD",
			title : '预起',
			align : 'center',
			formatter:function(value,row,index){
				if(value&&value.length==16){
					value = toDayTime(value);
				} 
				return value;
			}
		}, {
			field : "IN_ETA",
			title : '预落',
			align : 'center',
			formatter:function(value,row,index){
				if(value&&value.length==16){
					value = toDayTime(value);
				} 
				return value;
			}
		}, {
			field : "IN_STATUS",
			title : '状态',
			align : 'center',
			editable : {
				type : "select2",
				disabled:true,
				source : acfStatus
			}
		},{
			field : "OUT_FLTNO",
			title : "出港航班号",
			align : 'center'
		}, {
			field : "ARRIVAL_APT3CODE",
			title : "落场",
			align : 'center',
			editable : {
				type : "select2",
				disabled:true,
				source : airports
			}
		} ,{
			field : "OUT_ETD",
			title : '预起',
			align : 'center',
			formatter:function(value,row,index){
				if(value&&value.length==16){
					value = toDayTime(value);
				} 
				return value;
			}
		}, {
			field : "OUT_ETA",
			title : '预落',
			align : 'center',
			formatter:function(value,row,index){
				if(value&&value.length==16){
					value = toDayTime(value);
				} 
				return value;
			}
		},{
			field : "OUT_STATUS",
			title : '状态',
			align : 'center',
			editable : {
				type : "select2",
				disabled:true,
				source : acfStatus
			}
		}]
	});
}

/**
 * 保存配对
 * @returns {Boolean}
 */
function saveRel(){
	var inFlight = $("#fdI").bootstrapTable("getData");
	var outFlight = $("#fdO").bootstrapTable("getData");
	if(inFlight.length>0||outFlight.length){
		layer.msg("您还有未处理的航班数据，无法保存！",{icon:7});
		return false;
	}
	var data = $("#fdNewIO").bootstrapTable("getData");
	if(data==0){
		layer.msg("没有可保存的航班信息！",{icon:7});
		return false;
	}
	layer.confirm("确定要保存调整后的航班数据？", {
		icon : 3,
		title : '提示'
	}, function(index) {
		layer.close(index);
		var loading = layer.load(2, {
			shade : [ 0.1, '#000' ]
		});
		$.ajax({
			type : 'post',
			url : ctx + "/fdRel/saveRel",
			data : {
				'data':JSON.stringify(data)
			},
			dataType : "json",
			async:false,
			error : function() {
				layer.close(loading);
				layer.msg('保存失败！', {
					icon : 2
				});
			},
			success : function(data) {
				layer.close(loading);
				if(data.status=="1"){
					if(data.msg){
						layer.msg('保存成功！'+data.msg, {
							icon : 7,
							time:1500
						},function(){
							parent.saveSuccess();
						});
					} else {
						layer.msg('保存成功！', {
							icon : 1,
							time:600
						},function(){
							parent.saveSuccess();
						});
					}
				}else{
					layer.msg('保存失败！'+data.error, {
						icon : 2
					});
					return data.status;
				}			
			}
		});
	});
}
function toDayTime(time){
	var fltDate = new Date(Date.parse(time.substring(0,10)));
	if(fltDate.getDate()!=new Date().getDate()){
		return "("+(Array(2).join(0) + (fltDate.getDate())).slice(-2)+")"+time.substring(11).replace(":","");
	} else if(fltDate.getDate()==new Date().getDate()){
		return time.substring(11).replace(":","");
	} else {
		return time;
	}
}