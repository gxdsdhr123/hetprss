<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%--进港航班详情 --%>
<div id="flight-in" class="container" style="display:none;padding-top:10px;">
	<div class="row">
		<div class="col-sm-6">进港航班：<span data-id="flightNumber"></span></div>
		<div class="col-sm-6">机型：<span data-id="acttypeCode"></span></div>
	</div>
	<div class="row">
		<div class="col-sm-6">机位：<span data-id="actstandCode"></span></div>
		<div class="col-sm-6">机号：<span data-id="aircraftNumber"></span></div>
	</div>
	<div class="row">
		<div class="col-sm-4">计落：<span data-id="sta"></span></div>
		<div class="col-sm-4">预落：<span data-id="eta"></span></div>
		<div class="col-sm-4">实落：<span data-id="ata"></span></div>
	</div>
	<div class="row">
		<div class="col-sm-12">作业人员：<span data-id="allOperator"></span></div>
	</div>
</div>
<%--出港航班详情 --%>
<div id="flight-out" class="container" style="display:none;padding-top:10px;">
	<div class="row">
		<div class="col-sm-6">出港航班：<span data-id="flightNumber"></span></div>
		<div class="col-sm-6">机型：<span data-id="acttypeCode"></span></div>
	</div>
	<div class="row">
		<div class="col-sm-6">机位：<span data-id="actstandCode"></span></div>
		<div class="col-sm-6">机号：<span data-id="aircraftNumber"></span></div>
	</div>
	<div class="row">
		<div class="col-sm-6">计起：<span data-id="std"></span></div>
		<div class="col-sm-6">预起：<span data-id="etd"></span></div>
	</div>
	<div class="row">
		<div class="col-sm-12">作业人员：<span data-id="allOperator"></span></div>
	</div>
</div>
<%--进出港航班详情 --%>
<div id="flight-inout" class="container" style="display:none;padding-top:10px;">
	<div class="row">
		<div class="col-sm-6">进港航班：<span data-id="inFltNum"></span></div>
		<div class="col-sm-6">机型：<span data-id="acttypeCode"></span></div>
	</div>
	<div class="row">
		<div class="col-sm-6">机位：<span data-id="actstandCode"></span></div>
		<div class="col-sm-6">机号：<span data-id="aircraftNumber"></span></div>
	</div>
	<div class="row">
		<div class="col-sm-4">计落：<span data-id="sta"></span></div>
		<div class="col-sm-4">预落：<span data-id="eta"></span></div>
		<div class="col-sm-4">实落：<span data-id="ata"></span></div>
	</div>
	<div class="row">
		<div class="col-sm-4">出港航班：<span data-id="outFltNum"></span></div>
		<div class="col-sm-4">计起：<span data-id="std"></span></div>
		<div class="col-sm-4">预起：<span data-id="etd"></span></div>
	</div>
	<div class="row">
		<div class="col-sm-12">作业人员：<span data-id="allOperator"></span></div>
	</div>
</div>
<%--任务详情 --%>
<div id="task-info" class="container" style="display:none;padding-top:10px;">
	<div class="row">
		<div class="col-sm-6"><span data-id="name"></span></div>
		<div class="col-sm-3"><span data-id="carId"></span></div>
		<div class="col-sm-3"><span data-id="status"></span></div>
	</div>
	<div class="row">&nbsp;</div>
	<div id="nodes"></div>
	<div class="row">&nbsp;</div>
</div>
 <%--人员作业组清单  --%>
 <div id="team-pop" class="container" style="display:none;padding-top:10px;">
	
</div>