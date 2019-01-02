$(function(){
	
	//查询按钮
	$("#searchBut").on('click',function(){
		var beginTime = $("#beginTime").val();
		var endTime = $("#endTime").val();
		var vsBeginTime = $("#vsBeginTime").val();
		var vsEndTime = $("#vsEndTime").val();
		if("" == beginTime || null == beginTime){
			alert("请输入开始时间！");
			return;
		}
		if("" == endTime || null == endTime){
			alert("请输入结束时间！");
			return;
		}
		if("" == vsBeginTime || null == vsBeginTime){
			alert("请输入对比开始时间！");
			return;
		}
		if("" == vsEndTime || null == vsEndTime){
			alert("请输入对比结束时间！");
			return;
		}
		location.href= PATH +"/flightCount/searchPage?endTime="+endTime+"&beginTime="+beginTime+"&vsBeginTime="+vsBeginTime+"&vsEndTime="+vsEndTime;
	});
	
	//打印Excel
	$("#printBut").on('click',function(){
		var beginTime = $("#beginTime").val();
		var endTime = $("#endTime").val();
		var vsBeginTime = $("#vsBeginTime").val();
		var vsEndTime = $("#vsEndTime").val();
		if("" == beginTime || null == beginTime){
			alert("请输入开始时间！");
			return;
		}
		if("" == endTime || null == endTime){
			alert("请输入结束时间！");
			return;
		}
		if("" == vsBeginTime || null == vsBeginTime){
			alert("请输入对比开始时间！");
			return;
		}
		if("" == vsEndTime || null == vsEndTime){
			alert("请输入对比结束时间！");
			return;
		}
		$("#beginTimeDisplay").text(beginTime);
		$("#endTimeDisplay").text(endTime);
		$("#vsBeginTimeDisplay").text(vsBeginTime);
		$("#vsEndTimeDisplay").text(vsEndTime);
		$("#printForm").submit();
	});
});