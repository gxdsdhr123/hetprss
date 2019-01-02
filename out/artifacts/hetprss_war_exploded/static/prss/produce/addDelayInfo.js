function saveDelayInfo(index){
	var delayInfo={};	// 单据信息
	delayInfo.delayInfoId = $('#delayInfoId').val();
	delayInfo.fltid = $('#fltid').val();
	delayInfo.flightNumber = $('#flightNumber').val();
	delayInfo.flightDate = $('#flightDate').val();
	delayInfo.delayReason = $('#delayReason').val();
	delayInfo.notes=$('#notes').val();
	delayInfo.lkHotel=$('#lkHotel').val();
	delayInfo.jzHotel=$('#jzHotel').val();
	
	// 旅客当日
	var detail10 = {
			arrangeType:'1',
			dateType:'0',
			fltid:delayInfo.fltid
	};	
	$('#lk_row1 input').each(function(){
		var key = $(this).attr('name');
		var v = $(this).val();
		detail10[key] = v;
	});
	// 旅客次日
	var detail11 = {
			arrangeType:'1',
			dateType:'1',
			fltid:delayInfo.fltid
	};	
	$('#lk_row2 input').each(function(){
		var key = $(this).attr('name');
		var v = $(this).val();
		detail11[key] = v;
	});
	// 机组当日
	var detail20 = {
			arrangeType:'2',
			dateType:'0',
			fltid:delayInfo.fltid
	};	
	$('#jz_row1 input').each(function(){
		var key = $(this).attr('name');
		var v = $(this).val();
		detail20[key] = v;
	});
	// 机组次日
	var detail21 = {
			arrangeType:'2',
			dateType:'1',
			fltid:delayInfo.fltid
	};	
	$('#jz_row2 input').each(function(){
		var key = $(this).attr('name');
		var v = $(this).val();
		detail21[key] = v;
	});
	
	delayInfo.details = [detail10,detail11,detail20,detail21];
	
	$.ajax({
        type: "POST",
        url: ctx + '/bill/fwDelay/saveDelay',
        data : {
        	delayInfo : JSON.stringify(delayInfo)
        },
        dataType: "json",
        async:false,
        success: function(dataMap){
            if(dataMap.code == '0'){
            	parent.layer.msg('保存成功！',{icon:1});
            	parent.layer.close(index);
            }else{
            	parent.layer.msg(dataMap.msg,{icon:7});
            }
         },
         error : function(){
        	 parent.layer.msg('数据访问失败，请稍后重试！',{icon:7});
         }
    });
	
}

function resetDelay(){
	document.form1.reset();
}
