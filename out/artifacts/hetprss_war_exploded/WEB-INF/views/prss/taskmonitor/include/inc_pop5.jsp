<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<div id="flight-info" class="container" style="display:none;padding-top:10px;">
	<div class="row">
		<div class="col-sm-12">航班号：<span data-id="inFltNum"></span>/<span data-id="outFltNum"></span></div>
	</div>
	<div class="row">
		<div class="col-sm-6">进港机位：<span data-id="inActstandCode"></span></div>
		<div class="col-sm-6">出港机位：<span data-id="outActstandCode"></span></div>
	</div>
	<div class="row">
		<div class="col-sm-6">机型：<span data-id="acttypeCode"></span></div>
		<div class="col-sm-6">机号：<span data-id="aircraftNumber"></span></div>
	</div>
	<div class="row">
		<div class="col-sm-6">登机口：<span data-id="gate"></span></div>
		<div class="col-sm-6">行李转盘：<span data-id="bag"></span></div>
	</div>
	<div class="row">
		<div class="col-sm-6">计落：<span data-id="sta"></span></div>
		<div class="col-sm-6">预落：<span data-id="eta"></span></div>
	</div>
	<div class="row">
		<div class="col-sm-6">计起：<span data-id="std"></span></div>
		<div class="col-sm-6">预起：<span data-id="etd"></span></div>
	</div>
	<div class="row">
		<div class="col-sm-6">VIP标识：<span data-id="inVipFlag"></span>/<span data-id="outVipFlag"></span></div>
		<div class="col-sm-6">TSAT：<span data-id="tsat"></span></div>
	</div>
	<div class="row">
		<div class="col-sm-6">登机开始时间：<span data-id="brdBtm"></span></div>
		<div class="col-sm-6">登机结束时间：<span data-id="brdEtm"></span></div>
	</div>
	<div class="row">
		<div class="col-sm-6">进港人数：<span data-id="dPaxNum"></span></div>
		<div class="col-sm-6">出港人数：<span data-id="iPaxNum"></span></div>
	</div>
</div>
<%--任务详情 --%>
<div id="task-info" class="container" style="display:none;padding-top:10px;">
	<div class="row">
		<div class="col-sm-6">任务名称：<span data-id="name"></span></div>
		<div class="col-sm-6">节点：<span data-id="status"></span></div>
	</div>
	<div class="row">
		<div class="col-sm-6">航班号：<span data-id="flightNumber"></span></div>
		<div class="col-sm-6">机位：<span data-id="actstandCode"></span></div>
	</div>
	<div class="row">&nbsp;</div>
	<div id="nodes"></div>
	<div class="row">&nbsp;</div>
</div>