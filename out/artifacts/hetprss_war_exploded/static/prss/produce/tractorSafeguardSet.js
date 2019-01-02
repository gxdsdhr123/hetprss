
var form ;
$(function() {
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	});
	layui.use("form", function() {
		form = layui.form;
		form.on('select(tracotrtype)', function(data) {
			var typeId = $("#tracotrtype").val();
			getOptions(2,typeId,'','','unitmodel');
			getOptions(3,typeId,reviceModel,'','unitnumber');
			getOptions(4,typeId,reviceModel,reviceNo,'carNumber');
		});
		form.on('select(unitmodel)', function(data) {
			var typeId = $("#tracotrtype").val();
			var reviceModel = $("#unitmodel").val();
			getOptions(3,typeId,reviceModel,'','unitnumber');
			getOptions(4,typeId,reviceModel,reviceNo,'carNumber');
		});
		form.on('select(unitnumber)', function(data) {
			var typeId = $("#tracotrtype").val();
			var reviceModel = $("#unitmodel").val();
			var reviceNo = $("#unitnumber").val();
			getOptions(4,typeId,reviceModel,reviceNo,'carNumber');
		});
	})
	
	initSelect2("airplaneTypeZ","请选择可拖拽机型，支持多选");//可拖拽机型
	initSelect2("airplaneZ","请选择不可拖拽航空公司，支持多选");//可拖拽航空公司
	initSelect2("seatZ","请选择不可拖拽机位，支持多选");//可拖拽机位
	initSelect2("airplaneTypeT","请选择可推出机型，支持多选");//可推出机型
	initSelect2("airplaneT","请选择不可推出航空公司，支持多选");//可推出机型航空公司
	initSelect2("seatT","请选择不可推出机位，支持多选");//可推出机位
	//初始化默认选中值
	$('#airplaneTypeZ').val(airplaneTypeZ).trigger('change');
	$('#airplaneTypeT').val(airplaneTypeT).trigger('change');
	$('#airplaneZ').val(airplaneZ).trigger('change');
	$('#airplaneT').val(airplaneT).trigger('change');
	$('#seatZ').val(seatZ).trigger('change');
	$('#seatT').val(seatT).trigger('change');
	initS();
	
	var body = $("body");
	body.css("position", "relative");
	new PerfectScrollbar(body[0]);
	
	var layero = parent.layer.getChildFrame().context;
    var iframe = $(layero).find("iframe")[0];
    var height = iframe.clientHeight;
    $("body>div").height(height);
});

function initS(){
	layui.use("form", function() {
		form = layui.form;
		if(typeId!=null && typeId !='')
			getOptions(2,typeId,'','','unitmodel');
		if(reviceModel!=null && reviceModel !='')
			getOptions(3,typeId,reviceModel,'','unitnumber');
		if(reviceNo!=null && reviceNo !='')
			getOptions(4,typeId,reviceModel,reviceNo,'carNumber');
	});
}
/**
 * 初始化select下拉
 * @param selectId
 * @param tip
 */
function initSelect2(selectId,tip){
	$('#'+selectId).select2({  
        placeholder: tip,  
        width:"400px",
        language : "zh-CN",
		templateResult : formatRepo,
		templateSelection : formatRepoSelection,
		escapeMarkup : function(markup) {
			return markup;
		}
    }); 
}



this.checkLength = function(text,length){
    var tlen =  text.match(/[^ -~]/g)==null?text.length:text.length+text.match(/[^ -~]/g).length;
    return tlen <=length ;
  }

function doCheck(){
	if($("#tracotrtype").val()=="all" || $("#tracotrtype").val()==''){
		showWarnMsg("请选择牵引车类型！");
		return false ;
	}
	if($("#unitmodel").val()=="all" || $("#unitmodel").val()==''){
		showWarnMsg("请设备型号！");
		return false ;
	}
	if($("#unitnumber").val()=="all" || $("#unitnumber").val()==''){
		showWarnMsg("请设备编号！");
		return false ;
	}
	return true;
}
function getSubmitData() {
	var paramsJsonObj = JSON.parse($("#createForm").form2json());
	//可拖拽机型
	var airplaneTypeZ= $("#airplaneTypeZ").select2("val");
	if(airplaneTypeZ!=null)
		paramsJsonObj['airplaneTypeZ'] = airplaneTypeZ.join(",");
	//可推出机型
	var airplaneTypeT= $("#airplaneTypeT").select2("val");
	if(airplaneTypeT!=null)
		paramsJsonObj['airplaneTypeT'] = airplaneTypeT.join(",");

	//航空公司限制
	var airplaneZ= $("#airplaneZ").select2("val");
	if(airplaneZ!=null)
		paramsJsonObj['airplaneZ'] = airplaneZ.join(",");
	//航空公司限制
	var airplaneT= $("#airplaneT").select2("val");
	if(airplaneT!=null)
		paramsJsonObj['airplaneT'] = airplaneT.join(",");
	

	//机位限制
	var seatZ= $("#seatZ").select2("val");
	if(seatZ!=null)
		paramsJsonObj['seatZ'] = seatZ.join(",");
	//机位限制
	var seatT= $("#seatT").select2("val");
	if(seatT!=null)
		paramsJsonObj['seatT'] = seatT.join(",");
	
	var tracotrtype = $("#tracotrtype").val();
	if(tracotrtype==21){//有拖把牵引车
//		JWQYCbltz	抱轮拖拽
//		JWQYCblt	抱轮推
		paramsJsonObj['aptutudelT1'] = 'JWQYCygtz';
		paramsJsonObj['aptutudelT2'] = 'JWQYCygt';
	} else if(tracotrtype==22){//无杆拖把牵引车
//		JWQYCygtz	有杆拖拽
//		JWQYCygt	有杆推
		paramsJsonObj['aptutudelT1'] = 'JWQYCbltz';
		paramsJsonObj['aptutudelT2'] = 'JWQYCblt';
		
	}
	return JSON.stringify(paramsJsonObj);
}
function formatRepo(repo) {
	return repo.text;
}
// select2显示选项处理
function formatRepoSelection(repo) {
	return repo.text;
}

/**
 * 显示提示信息框
 * @param msg
 */
function showWarnMsg(msg){
	layer.msg(msg, {
		icon : 7,
		time : 1000
	});
}

function checkValue(mtext){
	 var rep=/\[\W+\]/i
	 var exp=mtext.replace(rep,10);
	 try{
		 eval(exp);
		 return true;
	 }catch(e){
		 return false;
	 }
}



//获取下拉选项
function getOptions(type,typeId,deviceModel,deviceNo,selectId) {
	var value = '';
	value = $("#" + selectId).val();
	$.getJSON(ctx + '/produce/sageguard/getOptions',{
		typeId : typeId,
		deviceModel : deviceModel,
		deviceNo : deviceNo,
		type : type
	},function(data){
		if(type==4){
			var carNumber = data.length>0?data[0].NAME:'';
			$("#carNumber").val(carNumber);
		} else {
			$("#"+selectId).children().remove();
			$("#"+selectId).append($('<option value="all">请选择</option>'));
			$.each(data,function(i,e){
				var flag='';
				if(type==2){
					flag = e.CODE==reviceModel?'selected':'';
				} else if(type==3){
					flag = e.CODE==reviceNo?'selected':''
				}
				$("#"+selectId).append($('<option value="'+e.CODE+'" '+flag+'>'+e.NAME+'</option>'))
			});
			form.render("select","filter");
		}
	})
}
