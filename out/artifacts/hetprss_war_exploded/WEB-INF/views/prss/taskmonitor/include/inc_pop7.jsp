<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%--进港航班详情 --%>
<div id="flight" class="container" style="display:none;padding-top:10px;">
	<div class="row">
		<div class="col-sm-11">
			航班号：<span data-id="inFltNum"></span>
			/<span data-id="outFltNum"></span>
		</div>
	</div>
	<div class="row">
		<div class="col-sm-6">进港机位：<span data-id="inActstandCode"></span></div>
		<div class="col-sm-5">出港机位：<span data-id="outActstandCode"></span></div>
	</div>
	<div class="row">
		<div class="col-sm-6">机型：<span data-id="acttypeCode"></span></div>
		<div class="col-sm-5">机号：<span data-id="aircraftNumber"></span></div>
	</div>
	<div class="row">
		<div class="col-sm-6">计落：<span data-id="sta"></span></div>
		<div class="col-sm-5">预落：<span data-id="eta"></span></div>
	</div>
	<div class="row">
		<div class="col-sm-6">计起：<span data-id="std"></span></div>
		<div class="col-sm-5">预起：<span data-id="etd"></span></div>
	</div>
	<div class="row">
		<div class="col-sm-6">登机开始时间：<span data-id="brdBtm"></span></div>
		<div class="col-sm-5">登机结束时间：<span data-id="brdEtm"></span></div>
	</div>
	<div class="row">
		<div class="col-sm-6">进港人数：<span data-id="wu"></span></div>
		<div class="col-sm-5">出港人数：<span data-id="wu"></span></div>
	</div>
</div>
<%--任务详情 --%>
<div id="task-info" class="container" style="display:none;padding-top:10px;">
	<div class="row">
		<div class="col-sm-6"><span data-id="name"></span></div>
		<div class="col-sm-5"><span data-id="status"></span></div>
	</div>
	<div class="row">
		<div class="col-sm-6">航班号：<span data-id="flightNumber"></span></div>
		<div class="col-sm-5">车：<span data-id="carId"></span></div>
	</div>
	<div class="row">
		<div class="col-sm-6">机位：<span data-id="actstandCode"></span></div>
		<div class="col-sm-5">机型：<span data-id="acttypeCode"></span></div>
	</div>
	<div id="nodes"></div>
	<div class="row">&nbsp;</div>
</div>
