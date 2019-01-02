
$(function(){
	var layer;
	layui.use(['form','layer'],function(){
		layer = layui.layer;
	});
	
	layui.use("form", function() {
		var form = layui.form;
        //语音内容
		form.on('checkbox(ifsoundFilter)', function(data) {
            if(data.elem.checked==true){
            	$(".user_ifsound").removeAttr("hidden");
			}else if(data.elem.checked==false){
				$(".user_ifsound").attr("hidden","hidden");
			}    
		});
		//是否回复
		form.on('checkbox(ifreplyFilter)', function(data) {
			if(data.elem.checked==true){
				$(".user_defreply").removeAttr("hidden");
			}else if(data.elem.checked==false){
				$(".user_defreply").attr("hidden","hidden");
			}
		});
	})
	//设置滚动条--begin
	var mtext = $("#mtext");
	mtext.css("position", "relative");
	new PerfectScrollbar(mtext[0]);
	
	var createForm = $("#createForm");
	createForm.css("position", "relative");
	new PerfectScrollbar(createForm[0]);
	//设置滚动条--end
	//新增发送人范围页面
	$('#addPage').click(function() {
		         layer.open({
				  type: 2, 
				  title:'新增发送人范围',
				  maxmin:false,
				  resize : false,
				  shadeClose: true,
				  area: ["480px","410px"],
				  content: ctx + "/message/templ/senderMessage",
				  btn:["保存","重置","返回"],
				  yes:function(index, layero){
					  var mfromtype = $(layero).find("iframe")[0].contentWindow.document.getElementById('mfromtype').value;
					  var mfromtypename = $(layero).find("iframe")[0].contentWindow.document.getElementById('mfromtype').options[mfromtype==9?3:mfromtype].text;
					  var mfromer = $(layero).find("iframe")[0].contentWindow.document.getElementById("mfromer").value;
					  var mfromername = $(layero).find("iframe")[0].contentWindow.document.getElementById("mfromername").value;
					
					  var proceclsfrom = '';
					  var proceclsfromname = '';
					  var procdefparamfrom = '';
					  var ifprocfrom = $($(layero).find("iframe")[0].contentWindow.document.getElementById("ifprocfrom")).next().hasClass("layui-form-checked")== true?"1":"0";
					  var ifprocfromname = $($(layero).find("iframe")[0].contentWindow.document.getElementById("ifprocfrom")).next().hasClass("layui-form-checked")== true?"是":"否";
					  var ind = $('#baseTable').bootstrapTable("getData").length;
					  //发送人范围校验
					  if(mfromtypename==""){
						  layer.msg("发送者类型不能为空");
						  return;
					  }
					  if(mfromername==""){
						  layer.msg("发送者名称不能为空");
						  return;
					  }
					  if(ifprocfrom=="1"){
						  var returnFlag = true;
							var proceclsfroms = $($(layero).find("iframe")[0].contentWindow.document.getElementsByName("proceclsfrom"));
							$.each( proceclsfroms,function(index,item){
								if(item.value=='all'){
									layer.msg("触发处理类不能为空");
									returnFlag = false;
									return;
								}
								proceclsfrom += item.value + ",";
								proceclsfromname += $(item).find('option:selected').text() + ",";
							})
							var procdefparamfroms = $($(layero).find("iframe")[0].contentWindow.document.getElementsByName("procdefparamfrom"));
							$.each( procdefparamfroms,function(index,item){
								procdefparamfrom += item.value + ",";
							})
							if(!returnFlag)
								return returnFlag;
					  } 
					  if(proceclsfrom!='')
						  proceclsfrom = proceclsfrom.substr(0,proceclsfrom.length-1);
					  if(proceclsfromname!='')
						  proceclsfromname = proceclsfromname.substr(0,proceclsfromname.length-1);
					  if(procdefparamfrom!='')
						  procdefparamfrom = procdefparamfrom.substr(0,procdefparamfrom.length-1);
					  $('#baseTable').bootstrapTable('append',[{"ind":ind,"mfromtype": mfromtype,"mfromtypename": mfromtypename,"mfromer": mfromer,"mfromername": mfromername,"proceclsfrom": proceclsfrom,"proceclsfromname": proceclsfromname,"procdefparamfrom": procdefparamfrom,"ifprocfrom": ifprocfrom,"ifprocfromname": ifprocfromname}]);
					 layero.find(".layui-layer-close").click();
				  },
				  btn2:function(index, layero){
					  $(layero).find("iframe")[0].contentWindow.document.getElementById("createFormSender").reset();
					  					  					  
					  var ifprocfrom = $($(layero).find("iframe")[0].contentWindow.document.getElementById("ifprocfrom")).next().hasClass("layui-form-checked")== true?"1":"0";				 
					  var procdefparamfrom = $(layero).find("iframe")[0].contentWindow.document.getElementById("procdefparamfrom");
					  var proceclsfrom = $(layero).find("iframe")[0].contentWindow.document.getElementById("proceclsfrom");
						if(ifprocfrom==1){
							$(procdefparamfrom).attr("disabled",true);
							$(proceclsfrom).attr("disabled",true);
						} else {
							$(procdefparamfrom).attr("disabled",false);
							$(	proceclsfrom).attr("disabled",false);
						}

					  
					  return false;
				  }
				});
	});
	
	//修改发送人范围
	$('#updatePage').click(function() {
		if($("#baseTable").bootstrapTable("getSelections").length<1){
			layer.msg('请选择一条修改信息', {
				icon : 2
			});
			return false;
		}
		  var currentRow = $('#baseTable').find("input[type=checkbox]:checked").parents("tr").data("index");
		  var mfromtype = $('#baseTable').bootstrapTable('getSelections')[0].mfromtype;
		  var mfromtypename = $('#baseTable').bootstrapTable('getSelections')[0].mfromtypename;
		  var mfromer = $('#baseTable').bootstrapTable('getSelections')[0].mfromer;
		  var mfromername = $('#baseTable').bootstrapTable('getSelections')[0].mfromername;
		  mfromername=encodeURIComponent(encodeURIComponent(mfromername));
		  var proceclsfrom = $('#baseTable').bootstrapTable('getSelections')[0].proceclsfrom;
		 
		  var proceclsfromname = $('#baseTable').bootstrapTable('getSelections')[0].proceclsfromname;
		  proceclsfromname=encodeURIComponent(encodeURIComponent(proceclsfromname));
		  var procdefparamfrom = $('#baseTable').bootstrapTable('getSelections')[0].procdefparamfrom;
		  procdefparamfrom=encodeURIComponent(encodeURIComponent(procdefparamfrom));
		  var ifprocfrom = $('#baseTable').bootstrapTable('getSelections')[0].ifprocfrom;
		  var ifprocfromname = $('#baseTable').bootstrapTable('getSelections')[0].ifprocfromname;
		      layer.open({
			  type: 2, 
			  title:'修改发送人范围',
			  maxmin:false,
			  shadeClose: true,
			  resize : false,
			  area: ["480px","410px"],
			  content: ctx + "/message/templ/senderMessage?flag=upd&mfromtype="+mfromtype+"&mfromtypename="+mfromtypename+"&mfromer="
			  +mfromer+"&mfromername="+mfromername+"&proceclsfrom="+proceclsfrom+"&proceclsfromname="+proceclsfromname
			  +"&procdefparamfrom="+procdefparamfrom+"&ifprocfrom="+ifprocfrom+"&ifprocfromname="+ifprocfromname,
			  btn:["保存","重置","返回"],
			  yes:function(index, layero){
				  var mfromtype = $(layero).find("iframe")[0].contentWindow.document.getElementById('mfromtype').value;
				  var mfromtypename = $(layero).find("iframe")[0].contentWindow.document.getElementById('mfromtype').options[mfromtype==9?3:mfromtype].text;
				  var mfromer = $(layero).find("iframe")[0].contentWindow.document.getElementById("mfromer").value;
				  var mfromername = $(layero).find("iframe")[0].contentWindow.document.getElementById("mfromername").value;

				  var proceclsfrom = '';
				  var proceclsfromname = '';
				  var procdefparamfrom = '';
				  var ifprocfrom = $($(layero).find("iframe")[0].contentWindow.document.getElementById("ifprocfrom")).next().hasClass("layui-form-checked")== true?"1":"0";
				  var ifprocfromname = $($(layero).find("iframe")[0].contentWindow.document.getElementById("ifprocfrom")).next().hasClass("layui-form-checked")== true?"是":"否";
				//发送人范围校验
				  if(mfromtypename==""){
					  layer.msg("发送者类型不能为空");
					  return;
				  }
				  if(mfromername==""){
					  layer.msg("发送者名称不能为空");
					  return;
				  }
				  if(ifprocfrom=="1"){
					  var returnFlag = true;
						var proceclsfroms = $($(layero).find("iframe")[0].contentWindow.document.getElementsByName("proceclsfrom"));
						$.each( proceclsfroms,function(index,item){
							if(item.value=='all'){
								layer.msg("触发处理类不能为空");
								returnFlag = false;
								return;
							}
							proceclsfrom += item.value + ",";
							proceclsfromname += $(item).find('option:selected').text() + ",";
						})
						var procdefparamfroms = $($(layero).find("iframe")[0].contentWindow.document.getElementsByName("procdefparamfrom"));
						$.each( procdefparamfroms,function(index,item){
							procdefparamfrom += item.value + ",";
						})
						if(!returnFlag)
							return returnFlag;
				  } 
				  if(proceclsfrom!='')
					  proceclsfrom = proceclsfrom.substr(0,proceclsfrom.length-1);
				  if(proceclsfromname!='')
					  proceclsfromname = proceclsfromname.substr(0,proceclsfromname.length-1);
				  if(procdefparamfrom!='')
					  procdefparamfrom = procdefparamfrom.substr(0,procdefparamfrom.length-1);
				  $('#baseTable').bootstrapTable('updateRow',{index: currentRow, row:{mfromtype: mfromtype,mfromtypename: mfromtypename,mfromer: mfromer,mfromername: mfromername,proceclsfrom: proceclsfrom,proceclsfromname: proceclsfromname,procdefparamfrom: procdefparamfrom,ifprocfrom: ifprocfrom,ifprocfromname: ifprocfromname}});
				  layero.find(".layui-layer-close").click();
			  },
			  btn2:function(index, layero){
				  $(layero).find("iframe")[0].contentWindow.document.getElementById("createFormSender").reset();
				  
				  var ifprocfrom = $($(layero).find("iframe")[0].contentWindow.document.getElementById("ifprocfrom")).next().hasClass("layui-form-checked")== true?"1":"0";				 
				  var procdefparamfrom = $(layero).find("iframe")[0].contentWindow.document.getElementById("procdefparamfrom");
				  var proceclsfrom = $(layero).find("iframe")[0].contentWindow.document.getElementById("proceclsfrom");
				  console.info(ifprocfrom);
					if(ifprocfrom==1){
						$(procdefparamfrom).attr("disabled",true);
						$(proceclsfrom).attr("disabled",true);
					} else {
						$(procdefparamfrom).attr("disabled",false);
						$(	proceclsfrom).attr("disabled",false);
					}
					
				  return false;
			  }
			});
		
	});
	
	
	//新增接收人范围
	$('#addPageOther').click(function() {
		          layer.open({
				  type: 2, 
				  title:'新增接收人范围',
				  maxmin:false,
				  resize : false,
				  shadeClose: true,
				  area: ["670px","410px"],
				  content: ctx + "/message/templ/reciverMessage",
				  btn:["保存","重置","返回"],
				  yes:function(index, layero){
					  var mtotype = $(layero).find("iframe")[0].contentWindow.document.getElementById("mtotype").value;
					  if(mtotype=="8"){
						  var mtotypename="作业人"; 
					  }else{ 
						  var mtotypename = $(layero).find("iframe")[0].contentWindow.document.getElementById('mtotype').options[mtotype].text;
					}
					  var mtoer = $(layero).find("iframe")[0].contentWindow.document.getElementById("mtoer").value;
					  var mtoername = $(layero).find("iframe")[0].contentWindow.document.getElementById("mtoername").value;
					  var ifprocto = $($(layero).find("iframe")[0].contentWindow.document.getElementById("ifprocto")).next().hasClass("layui-form-checked")== true?"1":"0";
					  var ifproctoname = $($(layero).find("iframe")[0].contentWindow.document.getElementById("ifprocto")).next().hasClass("layui-form-checked")== true?"是":"否";
					  var proceclsto = $(layero).find("iframe")[0].contentWindow.document.getElementById("proceclsto").value;
					  var index=$(layero).find("iframe")[0].contentWindow.document.getElementById("proceclsto").selectedIndex 
					  var proceclstoname =$(layero).find("iframe")[0].contentWindow.document.getElementById('proceclsto').options[index].text;
					  var procdefparamto = $(layero).find("iframe")[0].contentWindow.document.getElementById("procdefparamto").value;
					  var iftrans = $($(layero).find("iframe")[0].contentWindow.document.getElementById("iftrans")).next().hasClass("layui-form-checked")== true?"1":"0";
					  var iftransname = $($(layero).find("iframe")[0].contentWindow.document.getElementById("iftrans")).next().hasClass("layui-form-checked")== true?"是":"否";
					  var ifsms = $($(layero).find("iframe")[0].contentWindow.document.getElementById("ifsms")).next().hasClass("layui-form-checked")== true?"1":"0";
					  var ifsmsname = $($(layero).find("iframe")[0].contentWindow.document.getElementById("ifsms")).next().hasClass("layui-form-checked")== true?"是":"否";
					  var transtemplid = $(layero).find("iframe")[0].contentWindow.document.getElementById("transtemplid").value;
					  var transtemplname = $(layero).find("iframe")[0].contentWindow.document.getElementById("transtemplname").value;
					  var condition = $(layero).find("iframe")[0].contentWindow.document.getElementById("condition").value;
					  var ind1 = $('#baseTable1').bootstrapTable("getData").length;

					  var colids = $(layero).find("iframe")[0].contentWindow.document.getElementById("colids").value;
					  var drlStr = $(layero).find("iframe")[0].contentWindow.document.getElementById("drlStr").value;
					  var drools = $(layero).find("iframe")[0].contentWindow.document.getElementById("drools").value;
					  
					  //接收人范围校验
					  if(mtotypename==""){
						  layer.msg("接受者类型不能为空");
						  return;
					  }
					  if(mtotype!="8"){
						  if(mtoername==""){
							  layer.msg("接收者名称不能为空");
							  return;
						  }
					  } else {
						  if(mtoername=="")
							  mtoer='';
					  }
					  if(ifprocto=="1"&&proceclsto=="all"){
						  layer.msg("处理类不能为空");
						  return;
					  }else if(ifprocto =='0') {
						  proceclsto = '';
						  proceclstoname = '';
					  }
					  if(iftrans=="1"&&transtemplname==""){
						  layer.msg("模板不能为空");
						  return;
					  }
					  $('#baseTable1').bootstrapTable('append',[{"ind1": ind1,
						  "mtotype": mtotype,
						  "mtotypename": mtotypename,
						  "mtoer": mtoer,
						  "mtoername": mtoername,
						  "ifprocto": ifprocto,
						  "ifproctoname": ifproctoname,
						  "proceclsto": proceclsto,
						  "proceclstoname": proceclstoname,
						  "procdefparamto": procdefparamto,
						  "iftrans": iftrans,
						  "iftransname": iftransname,
						  "ifsms": ifsms,
						  "ifsmsname": ifsmsname,
						  "transtemplid": transtemplid,
						  "transtemplname": transtemplname,
						  "condition": condition,
						  "colids": colids,
						  "drlStr": drlStr,
						  "drools": drools
						  }]);
					  layero.find(".layui-layer-close").click();
				  },
				  btn2:function(index, layero){
					  $(layero).find("iframe")[0].contentWindow.document.getElementById("createFormReciver").reset();

					  var old_colids = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_colids").value;
					  var old_drlStr = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_drlStr").value;
					  var old_drools = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_drools").value;
					  var old_condition = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_condition").value;
					  $(layero).find("iframe")[0].contentWindow.document.getElementById("colids").value = old_colids;
					  $(layero).find("iframe")[0].contentWindow.document.getElementById("drlStr").value = old_drlStr;
					  $(layero).find("iframe")[0].contentWindow.document.getElementById("drools").value = old_drools;
					  $($(layero).find("iframe")[0].contentWindow.document.getElementById("condition")).html(old_condition);
					  
					  var mtotype = $(layero).find("iframe")[0].contentWindow.document.getElementById("mtotype").value;					  
					  var receiverName = $(layero).find("iframe")[0].contentWindow.document.getElementById("receiverName");
					  if(mtotype==8){
						  receiverName.innerHTML='保障作业类型';
					  }else{
						  receiverName.innerHTML='接收者名称';
					  }
					  var ifprocto = $($(layero).find("iframe")[0].contentWindow.document.getElementById("ifprocto")).next().hasClass("layui-form-checked")== true?"1":"0";				 
					  var procdefparamto = $(layero).find("iframe")[0].contentWindow.document.getElementById("procdefparamto");
					  var proceclsto = $(layero).find("iframe")[0].contentWindow.document.getElementById("proceclsto");
						if(ifprocto==1){
							$(procdefparamto).attr("disabled",true);
							$(proceclsto).attr("disabled",true);
						} else {
							$(procdefparamto).attr("disabled",false);
							$(	proceclsto).attr("disabled",false);
						}
					  return false;
				  }
				});
	});
	
	var layer;
	var clickRowId = "";
	layui.use(['form','layer'],function(){
		layer = layui.layer;
	});
	
	
	//修改接收人范围
	$('#updatePageOther').click(function() {
		if($("#baseTable1").bootstrapTable("getSelections").length<1){
			layer.msg('请选择一条修改信息', {
				icon : 2
			});
			return false;
		}
		var currentRow1 = $('#baseTable1').find("input[type=checkbox]:checked").parents("tr").data("index");
		var mtotype = $('#baseTable1').bootstrapTable('getSelections')[0].mtotype;
		var mtotypename = $('#baseTable1').bootstrapTable('getSelections')[0].mtotypename;
		var mtoer = $('#baseTable1').bootstrapTable('getSelections')[0].mtoer;
		var mtoername = $('#baseTable1').bootstrapTable('getSelections')[0].mtoername;
		mtoername=encodeURIComponent(encodeURIComponent(mtoername));
		var ifprocto = $('#baseTable1').bootstrapTable('getSelections')[0].ifprocto;
		var ifproctoname = $('#baseTable1').bootstrapTable('getSelections')[0].ifproctoname;
		var proceclsto = $('#baseTable1').bootstrapTable('getSelections')[0].proceclsto;
		var proceclstoname = $('#baseTable1').bootstrapTable('getSelections')[0].proceclstoname;
		proceclstoname=encodeURIComponent(encodeURIComponent(proceclstoname));
		var procdefparamto = $('#baseTable1').bootstrapTable('getSelections')[0].procdefparamto;
		procdefparamto=encodeURIComponent(encodeURIComponent(procdefparamto));
		var iftrans = $('#baseTable1').bootstrapTable('getSelections')[0].iftrans;
		var iftransname = $('#baseTable1').bootstrapTable('getSelections')[0].iftransname;
		var ifsms = $('#baseTable1').bootstrapTable('getSelections')[0].ifsms;
		var ifsmsname = $('#baseTable1').bootstrapTable('getSelections')[0].ifsmsname;
		var transtemplid = $('#baseTable1').bootstrapTable('getSelections')[0].transtemplid;
		var transtemplname = $('#baseTable1').bootstrapTable('getSelections')[0].transtemplname;
		transtemplname=encodeURIComponent(encodeURIComponent(transtemplname));
		var condition = $('#baseTable1').bootstrapTable('getSelections')[0].condition;
		condition=encodeURIComponent(encodeURIComponent(condition));
		var colids = $('#baseTable1').bootstrapTable('getSelections')[0].colids;
		var drlStr = $('#baseTable1').bootstrapTable('getSelections')[0].drlStr;
		drlStr=encodeURIComponent(encodeURIComponent(drlStr));
		var ruleId = $('#baseTable1').bootstrapTable('getSelections')[0].ruleId;
		var drools = $('#baseTable1').bootstrapTable('getSelections')[0].drools;
		drools=encodeURIComponent(encodeURIComponent(drools));
		
		    layer.open({
			type: 2, 
			  title:"修改接收人范围",
			  maxmin:false,
			  resize : false,
			  shadeClose: true,
			  content: ctx + "/message/templ/reciverMessage?mtotype="+mtotype+"&mtotypename="+mtotypename+"&mtoer="+mtoer+"&mtoername="+
			  		mtoername+"&ifprocto="+ifprocto+"&ifproctoname="+ifproctoname+"&proceclsto="+proceclsto+"&proceclstoname="+
			  		proceclstoname+"&procdefparamto="+procdefparamto+"&iftrans="+iftrans+"&iftransname="+iftransname+"&ifsms="+
			  		ifsms+"&ifsmsname="+ifsmsname+"&transtemplid="+transtemplid+"&transtemplname="+transtemplname+"&condition="+condition+
			  		'&colids='+colids+"&drlStr="+drlStr+'&ruleId='+ruleId+'&drools='+drools,
			  btn:["保存","重置","返回"],
			  title:'修改接收人范围',
			  area: ["670px","410px"],
			  yes:function(index, layero){
				  var mtotype = $(layero).find("iframe")[0].contentWindow.document.getElementById("mtotype").value;
				 if(mtotype=="8"){
					 var mtotypename="作业人"; 
				 }else{ var mtotypename = $(layero).find("iframe")[0].contentWindow.document.getElementById('mtotype').options[mtotype].text;}
				 
				 
				  var mtoer = $(layero).find("iframe")[0].contentWindow.document.getElementById("mtoer").value;
				  var mtoername = $(layero).find("iframe")[0].contentWindow.document.getElementById("mtoername").value;
				  var ifprocto = $($(layero).find("iframe")[0].contentWindow.document.getElementById("ifprocto")).next().hasClass("layui-form-checked")== true?"1":"0";
				  var ifproctoname = $($(layero).find("iframe")[0].contentWindow.document.getElementById("ifprocto")).next().hasClass("layui-form-checked")== true?"是":"否";
				  var proceclsto = $(layero).find("iframe")[0].contentWindow.document.getElementById("proceclsto").value;
				
				  var index=$(layero).find("iframe")[0].contentWindow.document.getElementById("proceclsto").selectedIndex 
				  var proceclstoname =$(layero).find("iframe")[0].contentWindow.document.getElementById('proceclsto').options[index].text;
				  var procdefparamto = $(layero).find("iframe")[0].contentWindow.document.getElementById("procdefparamto").value;
				  var iftrans = $($(layero).find("iframe")[0].contentWindow.document.getElementById("iftrans")).next().hasClass("layui-form-checked")== true?"1":"0";
				  var iftransname = $($(layero).find("iframe")[0].contentWindow.document.getElementById("iftrans")).next().hasClass("layui-form-checked")== true?"是":"否";
				  var ifsms = $($(layero).find("iframe")[0].contentWindow.document.getElementById("ifsms")).next().hasClass("layui-form-checked")== true?"1":"0";
				  var ifsmsname = $($(layero).find("iframe")[0].contentWindow.document.getElementById("ifsms")).next().hasClass("layui-form-checked")== true?"是":"否";
				  var transtemplid = $(layero).find("iframe")[0].contentWindow.document.getElementById("transtemplid").value;
				  var transtemplname = $(layero).find("iframe")[0].contentWindow.document.getElementById("transtemplname").value;
				  var condition = $(layero).find("iframe")[0].contentWindow.document.getElementById("condition").value;

				  var colids = $(layero).find("iframe")[0].contentWindow.document.getElementById("colids").value;
				  var drlStr = $(layero).find("iframe")[0].contentWindow.document.getElementById("drlStr").value;
				  var drools = $(layero).find("iframe")[0].contentWindow.document.getElementById("drools").value;
				  //接收人范围校验
				  if(mtotypename==""){
					  layer.msg("接受者类型不能为空");
					  return;
				  }
				  if(mtotype!="8"){
					  if(mtoername==""){
						  layer.msg("接收者名称不能为空");
						  return;
					  }
				  } else {
					  if(mtoername=="")
						  mtoer='';
				  }
				  if(ifprocto=="1"&&proceclsto=="all"){
					  layer.msg("处理类不能为空");
					  return;
				  } else if(ifprocto =='0') {
					  proceclsto = '';
					  proceclstoname = '';
				  }

				  if(iftrans=="1"&&transtemplname==""){
					  layer.msg("模板不能为空");
					  return;
				  }
				   $('#baseTable1').bootstrapTable('updateRow',
						   {index: currentRow1, row:{
							   mtotype: mtotype,
							   mtotypename: mtotypename,
							   mtoer: mtoer,
							   mtoername: mtoername,
							   ifprocto: ifprocto,
							   ifproctoname: ifproctoname,
							   proceclsto: proceclsto,
							   proceclstoname: proceclstoname,
							   procdefparamto: procdefparamto,
							   iftrans: iftrans,
							   iftransname: iftransname,
							   ifsms: ifsms,
							   ifsmsname: ifsmsname,
							   transtemplid: transtemplid,
							   transtemplname: transtemplname,
							   condition: condition,
							   colids: colids,
							   drlStr: drlStr,
							   drools: drools
							   }
				   });
				  layero.find(".layui-layer-close").click();
			  },
			  btn2:function(index, layero){
				  $(layero).find("iframe")[0].contentWindow.document.getElementById("createFormReciver").reset();

				  var old_colids = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_colids").value;
				  var old_drlStr = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_drlStr").value;
				  var old_drools = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_drools").value;
				  var old_condition = $(layero).find("iframe")[0].contentWindow.document.getElementById("old_condition").value;
				  $(layero).find("iframe")[0].contentWindow.document.getElementById("colids").value = old_colids;
				  $(layero).find("iframe")[0].contentWindow.document.getElementById("drlStr").value = old_drlStr;
				  $(layero).find("iframe")[0].contentWindow.document.getElementById("drools").value = old_drools;
				  $($(layero).find("iframe")[0].contentWindow.document.getElementById("condition")).html(old_condition);
				  
				  var mtotype = $(layero).find("iframe")[0].contentWindow.document.getElementById("mtotype").value;					  
				  var receiverName = $(layero).find("iframe")[0].contentWindow.document.getElementById("receiverName");
				  if(mtotype==8){
					  receiverName.innerHTML='保障作业类型';
				  }else{
					  receiverName.innerHTML='接收者名称';
				  }
				  
				  var ifprocto = $($(layero).find("iframe")[0].contentWindow.document.getElementById("ifprocto")).next().hasClass("layui-form-checked")== true?"1":"0";				 
				  var procdefparamto = $(layero).find("iframe")[0].contentWindow.document.getElementById("procdefparamto");
				  var proceclsto = $(layero).find("iframe")[0].contentWindow.document.getElementById("proceclsto");
					if(ifprocto==1){
						$(procdefparamto).attr("disabled",true);
						$(proceclsto).attr("disabled",true);
					} else {
						$(procdefparamto).attr("disabled",false);
						$(	proceclsto).attr("disabled",false);
					}
				  
				  return false;
			  }
			});
	});
	

//发送人表格	
	jQuery.fn.bootstrapTable.columnDefaults.sortable = true;
	jQuery.fn.bootstrapTable.columnDefaults.align = "center";
	jQuery.fn.bootstrapTable.columnDefaults.editable = {toggle:'dblclick'};
	var id = $("input[name=id]").val();
	
	var tableOption = {
		url: ctx+"/message/templ/getList?id="+id, //请求后台的URL（*）
	    method: "post",					  				 //请求方式（*）
	    dataType: "json",
		striped: true,									 //是否显示行间隔色
	    pagination: false,				   				 //是否显示分页（*）
	    cache: true,
	    undefinedText:'',
//	    uniqueId:"id",
	    checkboxHeader:false,
	    toolbar:$("#tool-box"),
	    search:false,
	    queryParams:function(){
	    	var temp = {
	    			ifall:'no',
	    	    	num:'100'
	    	}
	    	return temp;
	    },
	    columns: [

			{field: "id",title:"序号",sortable:true,editable:false,
				formatter:function(value,row,index){
					return index+1;
				}
			},
			{field: "ind",visible:false},
			{field: "checkbox",checkbox:true,sortable:false,editable:false},
			{field: "mfromtype",visible:false},
			{field: "mfromer",visible:false},
			{field: "mfromtypename", title: "发送者类型",editable:false},
			{field: "mfromername", title: "发送者名称",editable:false},
			{field: "ifprocfrom", title: "是否触发处理",visible:false},
			{field: "ifprocfromname", title: "是否触发处理",editable:false},
			{field: "proceclsfrom", title: "触发处理类",visible:false},
			{field: "proceclsfromname", title: "处理类",editable:false},
			{field: "procdefparamfrom", title: "处理参数",editable:false},
			{field: "tid",visible:false}
	    ],
	    onDblClickRow:onClickRow
	};
	function onClickRow(row,tr,field){
		clickRowId = row.id;
		$(".clickRow").removeClass("clickRow");
		$(tr).addClass("clickRow");
	}
	tableOption.height = $("#baseTables").height();
	$("#baseTable").bootstrapTable(tableOption);
//接收人表格	
	var tableOptions = {
			url: ctx+"/message/templ/getList1?id="+id, //请求后台的URL（*）
		    method: "post",					  				 //请求方式（*）
		    dataType: "json",
			striped: true,									 //是否显示行间隔色
		    pagination: false,				   				 //是否显示分页（*）
		    cache: true,
		    undefinedText:'',
		    checkboxHeader:false,
		    toolbar:$("#tool-box"),
		    search:false,
		    queryParams:function(){
		    	var temp = {
		    			ifall:'no',
		    	    	num:'100'
		    	}
		    	return temp;
		    },
		    columns: [

				{field: "id",title:"序号",sortable:true,editable:false,
					formatter:function(value,row,index){
						return index+1;
					}
				},
				{field: "ind1",visible:false},
				{field: "checkbox",checkbox:true,sortable:false,editable:false},
				{field: "mtotypename",title: "接受者类型",editable:false},
				{field: "mtotype",visible:false},
				{field: "mtoername",title: "接受者名称",editable:false},
				{field: "mtoer",visible:false},
				{field: "iftransname",title: "转发模板",editable:false},
				{field: "iftrans",visible:false},
				{field: "ifproctoname",visible:true,title : '是否触发处理',editable:false},
				{field: "proceclstoname",title: "处理类",editable:false},
				{field: "proceclsto",visible:false},
				{field: "procdefparamto",title: "处理参数",editable:false},
				{field: "ifprocto",visible:false},
				{field: "ifsms",visible:false},
				{field: "ifsmsname", visible:false},
				{field: "transtemplid",visible:false},
				{field: "transtemplname",visible:false},
				{field: "condition",visible:false},
				{field: "colids",visible:false},
				{field: "drlStr",visible:false},
				{field: "text",visible:false},
				{field: "ruleId",visible:false},
				{field: "tid",visible:false}
		    ],

		};
		tableOptions.height = $("#baseTables1").height();
		$("#baseTable1").bootstrapTable(tableOptions);
		//隐藏列
		$('#baseTable1').bootstrapTable('hideColumn', '');
		
	  
	
	
	
})
var index = parent.layer.getFrameIndex(window.name);
//校验
function submitCheck() {

	var tempname = $("input[name=tempname]").val();
	var mtitle = $("input[name=mtitle]").val();
//	var mtext = $("textarea[name=mtext]").text();
	var mtext = $("#mtext").text();
	mtext = mtext == "请输入内容"?"":mtext
	var ifreply = $("input[name=ifreply]").prop("checked")== true?"1":"0";
	var defreply = $("input[name=defreply]").val();
	var ifsound = $("input[name=ifsound]").prop("checked")== true?"1":"0";
	var soundtxt = $("select[name=soundtxt]").val();
	var ifpopm = $("input[name=ifpopm]").prop("checked")== true?"1":"0";
	var ifspeak = $("input[name=ifspeak]").prop("checked")== true?"1":"0";
	var mnumb = $("select[name=mnumb]").val();
	var fiotype = $("select[name=fiotype]").val();
	var ifflight = $("input[name=ifflight]").prop("checked")== true?"1":"0";
	var mftype = $("select[name=mftype]").val();
	var eventname = $("input[name=eventname]").val();
	
	
	var fu = new ChkUtil();
	var res = '';
	res = fu.checkFormInput(tempname, true,'',50);
	if (res.length > 0) {
		var msg = '[模板名称]合法性检查错误,' + res;
		layer.msg(msg, {icon: 2,time: 1000});
		return false;
	}
	res = fu.checkFormInput(mtitle, true,'',50);
	if (res.length > 0) {
		var msg = '[消息标题]合法性检查错误,' + res;
		layer.msg(msg, {icon: 2,time: 1000});
		return false;
 	}

//	res = fu.checkFormInput(eventname, true,'',50);
//	if (res.length > 0) {
//		var msg = '[模板关联事件]合法性检查错误,' + res;
//		layer.msg(msg, {icon: 2,time: 1000});
//		return false;
//	}
	if(mftype==0){
		res = fu.checkFormInput(mtext, true,'',500);
		if (res.length > 0) {
			var msg = '[消息正文]合法性检查错误,' + res;
			layer.msg(msg, {icon: 2,time: 1000});
			return false;
	 	}
	}

	
	if (ifreply == "1") {
		if (defreply == "") {
			res = fu.checkFormInput(defreply, true, '', 50);
			if (res.length > 0) {
				var msg = '[回复内容]合法性检查错误,' + res;
				layer.msg(msg, {
					icon : 2,
					time : 1000
				});
				return false;
			}
		}
	}

	if (ifsound == "1") {
		if (soundtxt == "") {
			res = fu.checkFormInput(soundtxt, true, '', 50);
			if (res.length > 0) {
				var msg = '[语音提示内容]合法性检查错误,' + res;
				layer.msg(msg, {
					icon : 2,
					time : 1000
				});
				return false;
			}
		}
	}
	res = fu.checkFormInput(ifspeak, false,'int',2);
	if (res.length > 0) {
		var msg = '[可否语音通话]合法性检查错误,' + res;
		layer.msg(msg, {icon: 2,time: 1000});
		return false;
	}
	res = fu.checkFormInput(mftype, false,'int',2);
	if (res.length > 0) {
		var msg = '[消息源类型(原创,转发)]合法性检查错误,' + res;
		layer.msg(msg, {icon: 2,time: 1000});
		return false;
	}
	res = fu.checkFormInput(mnumb, false,'int',6);
	if (res.length > 0) {
		var msg = '[右键排序]合法性检查错误,' + res;
		layer.msg(msg, {icon: 2,time: 1000});
		return false;
	}

	var senderSize = $('#baseTable').bootstrapTable("getData").length;
	var receiverSize = $('#baseTable1').bootstrapTable("getData").length;
	
	if (senderSize == 0) {
		var msg = '请填写发送人范围信息' ;
		layer.msg(msg, {icon: 2,time: 1000});
		
		return false;
	}
	if (receiverSize == 0) {
		var msg = '请填写接收人范围信息' ;
		layer.msg(msg, {icon: 2,time: 1000});
		
		return false;
	}
	
	return true;
}
//页面保存
var save = function (){
	var id = $("input[name=id]").val();
	var flag = $("input[name=flag]").val();
	var mtype = $("input[name=mtype]").val();
	var tempname = $("input[name=tempname]").val();
	var mtitle = $("input[name=mtitle]").val();
///	var mtext = $("textarea[name=mtext]").text();
	var mtext = $("#mtext").text();
	var old = $("#mtext").html();
	old = old.replace(/\<br\>/ig, '\n');
	var oldobj = $("<div>"+old+"</div>");
	mtext = oldobj.text();
	
	var ifreply = $("input[name=ifreply]").prop("checked");
	var defreply = $("input[name=defreply]").val();
	if(ifreply==true){
		if(!defreply || !$.trim(defreply)){
			layer.msg("回复内容不能为空",{icon:7});
			return false;
		}
	}
	
	var ifsound = $("input[name=ifsound]").prop("checked");

	var soundtxt = $("select[name=soundtxt]").val();
	if(ifsound=="0"){
		var soundtxt=""
	}
	var ifpopm = $("input[name=ifpopm]").prop("checked");
	var ifspeak = $("input[name=ifspeak]").prop("checked");
	var mnumb = $("select[name=mnumb]").val();
	var fiotype = $("select[name=fiotype]").val();
	var ifflight = $("input[name=ifflight]").prop("checked");
	var mftype = $("select[name=mftype]").val();
	var eventid = $("#eventid").val();
	var ifpopup = $("input[name=ifpopup]").prop("checked");
	var eventRecord = $("input[name=eventRecord]").prop("checked");
	var senddef = $("input[name=senddef]").prop("checked");
	
//	var varcols=$("#varcols").val();
    var varcols = getvarcols();
	var mtime=$("#mtime").val();
	var eventname = $("input[name=eventname]").val();
	if(eventname == null || eventname == '')
		eventid = '';
	
	
	var check = submitCheck();
	var tabList=JSON.stringify($('#baseTable').bootstrapTable('getData'));
	var tabList1=JSON.stringify($('#baseTable1').bootstrapTable('getData'));
	if(check){
		$.ajax({
			type:'post',
			data:{
				'id': id, 
				'flag': flag,
				'tempname' : tempname
			},
			async:true,
			url:ctx+"/message/templ/filterTemplate",
			dataType:"text",
			success:function(result){
				if(result <=0){
				  	$.ajax({
						type:'post',
						data:{
							'id': id, 
							'flag': flag,
							'mtype' : mtype,
							'tempname' : tempname,
							'mtitle' : mtitle,
							'mtext' : mtext,
							'ifreply' : ifreply==true?1:0,
							'defreply' : defreply,
							'ifsound' : ifsound==true?1:0,
							'soundtxt' : soundtxt,
							'ifpopm' : ifpopm==true?1:0,
							'ifspeak' : ifspeak==true?1:0,
							'mnumb' : mnumb,
							'fiotype' : fiotype,
							'ifflight' : ifflight==true?1:0,
							'mftype' : mftype,
							'eventid' : eventid,
							'varcols' :varcols,
							'mtime' :mtime,
							'tabList' : tabList,
							'tabList1' : tabList1,
							'ifpopup' :ifpopup==true?1:0,
							'eventRecord' : eventRecord ==true?1:0,
							'senddef' :senddef==true?1:0
						},
						async:true,
						url:ctx+"/message/templ/save",
						dataType:"text",
						success:function(result){
							if(result == "操作成功"){
								parent.layer.close(index);
								parent.layer.msg(result, {icon: 1,time: 1000});
								parent.refreshTable();
							} else {
								layer.msg(result, {icon: 1,time: 1000});
							}
						},
						error:function(msg){
							var result = "操作失败";
							layer.msg(result, {icon: 1,time: 1000});
						}
					});
				} else {
					var result = "模板名称重复请重新输入";
					layer.msg(result, {icon: 1,time: 1000});
					return false;
				}
			},
			error:function(msg){
				var result = "模板名称重复请重新输入";
				layer.msg(result, {icon: 1,time: 1000});
				return false;
			}
		});
	}
}

function show(){
	var createIframe = layer.open({
		  type: 2, 
		  title:'选择参数变量',
		  maxmin:false,
		  resize : false,
		  shadeClose: true,
		  area: ["300px","400px"],
		  content: ctx + "/message/templ/param?flag=upd&id="+id
	});
}

function varList(){		
	layui.use(['form','element']);
     layer.open({
		  type: 2,  
		  title:'选择参数变量',
		  maxmin:false,
		  resize : false,
		  shadeClose: true,
		  area: ["480px","400px"],
		  content: ctx + "/message/templ/getVariable"
		});
	}
function getmtext(val){
	$("#mtext").val(val);
}

//删除发送人范围
$(function(){
$('#delSender').click(function() {
	if($("#baseTable").bootstrapTable("getSelections").length==0){
		layer.msg('请选择要删除的数据', {
			icon : 2
		});
	}else{
		layer.confirm("是否删除选中的数据？",{resize : false,offset:'100px'},function(index){
			var checkboxs = $.map($('#baseTable').bootstrapTable('getSelections'), function (row) {
                return row.checkbox;
            });
			
			$('#baseTable').bootstrapTable('remove', {
		        field: 'checkbox',
		        values: checkboxs
		    });
			layer.close(index);
		});
	}
	
	
});
});


//删除接收人范围
$(function(){
$('#delSender1').click(function() {
	if($("#baseTable1").bootstrapTable("getSelections").length==0){
		layer.msg('请选择要删除的数据', {
			icon : 2
		});
	}else{
		layer.confirm("是否删除选中的数据？",{resize : false,offset:'100px'},function(index){
			var checkboxs = $.map($('#baseTable1').bootstrapTable('getSelections'), function (row) {
                return row.checkbox;
            });
			
			$('#baseTable1').bootstrapTable('remove', {
		        field: 'checkbox',
		        values: checkboxs
		    });
			layer.close(index);
		});
	}
});
});


function removeall(){
	$('#baseTable').bootstrapTable('removeAll');
	$('#baseTable1').bootstrapTable('removeAll');
}
function resetAll(){
	$('#baseTable').bootstrapTable('refresh');
	$('#baseTable1').bootstrapTable('refresh');
}
//模板关联事件页面
function searchEvent(){	
layui.use(['form','element']);
layer.open({
		  type: 2, 
		  title:'关联事件',
		  maxmin:false,
		  resize : false,
		  shadeClose: true,
		  area: ["420px","300px"],
		  content: ctx + "/message/templ/searchEventListPage?type=MM"
		  })

	}
//关联事件信息赋值
function getEventValue(name,no){
	$('#eventname').val(name);
	$('#eventid').val(no);
}