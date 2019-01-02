var layer;// 初始化layer模块
$(function() {
	
	layui.use([ 'layer', 'form' ], function() {
		layer = layui.layer;
	})
	$(".select2").select2({
		placeholder : "请选择",
		width : "100%",
		language : "zh-CN"
	});
	//根据航班号和航班日期填充其它表格
	$("#flightNumber").blur(function(){
		SearchFlightDetail();
	});
	$("#flightDate").blur(function(){
		SearchFlightDetail();
	});
	//填写清舱副领班等数据
	$("#TYPENAME").change(function(){
//		 $("#lingban").val($(this).val()).trigger("change")
		var fltId = $("#FLTID").val();
		$.ajax({
	        type: "POST",
	        url: PATH + '/produce/qcczAssignReport/searchTaskDetail',
	        data: {
	        	"fltId":fltId,
	        	"jobType":$(this).val()
	        },
	        dataType: "json",
	        success: function(dataList){
	        	console.info(dataList);
	            $.each(dataList,function(k,v){
	            	if(k == 'OPERATOR'){
	            		$("#lingban").val(v).trigger("change");
	            	}else{
	            		$("#"+k).val(v);
	            	}
	            })
	         }
	    });
	})
	//修改页面各种人员回显
	$("#fwt").val(eval('['+$("#selectPeople1").val()+']')).trigger("change");
	$("#dtqj").val(eval('['+$("#selectPeople2").val()+']')).trigger("change");
	$("#wsjqj").val(eval('['+$("#selectPeople3").val()+']')).trigger("change");
	$("#zjcz").val(eval('['+$("#selectPeople4").val()+']')).trigger("change");
	$("#kcqj").val(eval('['+$("#selectPeople5").val()+']')).trigger("change");
});
function SearchFlightDetail(){
	var flightNumber = $("#flightNumber").val();
	var flightDate = $("#flightDate").val();
	$.ajax({
        type: "POST",
        url: PATH + '/produce/qcczAssignReport/searchFlightDetail',
        data: {
        	"flightNumber":flightNumber,
        	"flightDate":flightDate
        },
        dataType: "json",
        success: function(dataList){
        	$("#TYPENAME").empty();
        	$("#TYPENAME").append("<option value=''></option>");
            $.each(dataList,function(k,v){
            	$.each(v,function(m,n){
	            	if(m == 'TYPENAME'){
	            		$("#TYPENAME").append("<option value="+v.JOB_TYPE+">"+n+"</option>");
	            	}else{
	            		$("#"+m).val(n);
	            	}
            	});
            })
         }
    });
}
function DoSave(){
	var tag = 0;
	var flightNumber = $("#flightNumber").val();
	var flightDate = $("#flightDate").val();
	var TYPENAME = $("#TYPENAME").val();
	var lingban = $("#lingban").val();
	
	if(flightNumber == "" || flightNumber == null){
		layer.msg('请填写航班号', {
			icon : 2
		});
		tag = 1;
	}else if(flightDate == "" || flightDate == null){
		layer.msg('请填写航班日期', {
			icon : 2
		});
		tag = 1;
	}else if(TYPENAME == "" || TYPENAME == null){
		layer.msg('请选择任务类型', {
			icon : 2
		});
		tag = 1;
	}else if(lingban == "" || lingban == null){
		layer.msg('该航班没有分配领班', {
			icon : 2
		});
		tag = 1;
	}
	if(tag == 0){
		$("#tableForm").submit();
		return tag;
	}else{
		return tag;
	}
}
