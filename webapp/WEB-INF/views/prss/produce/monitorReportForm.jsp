<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
	<meta name="decorator" content="default" />
	<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
	<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
	<title>航班监控报告</title>
	<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
	<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript"	src="${ctxStatic}/prss/produce/monitorReportForm.js"></script>
	</head>
	<body>
		<form id="bill" action="" class="form-horizontal" style="margin-top:10px;">
			<c:if test="${entity != null }">
				<input type="hidden" name="id" id="id" value="${entity.id}"/>
				<input type="hidden" name="inFltid" id="inFltid" value="${entity.inFltid}"/>
				<input type="hidden" name="outFltid" id="outFltid" value="${entity.outFltid}"/>
			</c:if>
			<!-- 单据头信息 -->
			<div class="row">
				 <div  class="col-sm-4 form-group" >
				 	<label for="statDay" class="col-sm-4 control-label">日期</label>
				    <div class="col-sm-8">
				      	<input type="text" class="form-control" name="statDay"  id="statDay"
				      	onclick="WdatePicker({dateFmt:'yyyyMMdd'});"	<c:if test="${entity != null }">value="${entity.statDay}"  </c:if>	/>
				    </div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="flightNumber" class="col-sm-4 control-label">航班号</label>
				    <div class="col-sm-3">
				      	<input type="text" class="form-control" name="inFlightNumber"  id="inFlightNumber" <c:if test="${entity != null }">value="${entity.inFlightNumber}"  </c:if> />
				    </div>
				    <label class="col-sm-1 control-label">/</label>
				    <div class="col-sm-3">
				      	<input type="text" class="form-control" name="outFlightNumber"  id="outFlightNumber" <c:if test="${entity != null }">value="${entity.outFlightNumber}"  </c:if> />
				    </div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="aln2code" class="col-sm-4 control-label">航空公司</label>
			    	<div class="col-sm-8">
		    			<input type="text" class="form-control" name="aln2code" id="aln2code" <c:if test="${entity != null }">value="${entity.aln2code}"</c:if>   />
		    		</div>
				 </div>
			</div>
			<div class="row">
				 <div  class="col-sm-4 form-group" >
				 	<label for="aircraftNumber" class="col-sm-4 control-label">机号</label>
		    		<div class="col-sm-3">
				      	<input type="text" class="form-control" name="inAircraftNumber"  id="inAircraftNumber" <c:if test="${entity != null }">value="${entity.inAircraftNumber}"  </c:if> />
				    </div>
				    <label class="col-sm-1 control-label">/</label>
				    <div class="col-sm-3">
				      	<input type="text" class="form-control" name="outAircraftNumber"  id="outAircraftNumber" <c:if test="${entity != null }">value="${entity.outAircraftNumber}"  </c:if> />
				    </div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="acttypeCode" class="col-sm-4 control-label">机型</label>
		    		<div class="col-sm-3">
				      	<input type="text" class="form-control" name="inActtypeCode"  id="inActtypeCode" <c:if test="${entity != null }">value="${entity.inActtypeCode}"  </c:if> />
				    </div>
				    <label class="col-sm-1 control-label">/</label>
				    <div class="col-sm-3">
				      	<input type="text" class="form-control" name="outActtypeCode"  id="outActtypeCode" <c:if test="${entity != null }">value="${entity.outActtypeCode}"  </c:if> />
				    </div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="actstandCode" class="col-sm-4 control-label">机位</label>
			    	<div class="col-sm-8">
		    			<input type="text" class="form-control" name="actstandCode" id="actstandCode" <c:if test="${entity != null }">value="${entity.actstandCode}"</c:if>   />
		    		</div>
				 </div>
			</div>
			<div class="row">
				 <div  class="col-sm-8 form-group" >
				 	<label for="routeName" class="col-sm-2 control-label">航线</label>
			    	<div class="col-sm-10">
		    			<input type="text" class="form-control" name="routeName" id="routeName" <c:if test="${entity != null }">value="${entity.routeName}"</c:if>   />
		    		</div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="jobLevel" class="col-sm-4 control-label">保障级别</label>
		    		<div class="col-sm-3">
				      	<input type="text" class="form-control" name="inJobLevel"  id="inJobLevel" <c:if test="${entity != null }">value="${entity.inJobLevel}"  </c:if> />
				    </div>
				    <label class="col-sm-1 control-label">/</label>
				    <div class="col-sm-3">
				      	<input type="text" class="form-control" name="outJobLevel"  id="outJobLevel" <c:if test="${entity != null }">value="${entity.outJobLevel}"  </c:if> />
				    </div>
				 </div>
			</div>
			<div class="row">
				 <div  class="col-sm-4 form-group" >
				 	<label for="sta" class="col-sm-4 control-label">计落</label>
			    	<div class="col-sm-8">
		    			<input type="text" class="form-control" name="sta" id="sta" <c:if test="${entity != null }">value="${entity.sta}"</c:if>   />
		    		</div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="beginTm" class="col-sm-4 control-label">开台时间</label>
			    	<div class="col-sm-8">
		    			<input type="text" class="form-control" name="beginTm" id="beginTm" <c:if test="${entity != null }">value="${entity.beginTm}"</c:if>   />
		    		</div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="std" class="col-sm-4 control-label">计起</label>
			    	<div class="col-sm-8">
		    			<input type="text" class="form-control" name="std" id="std" <c:if test="${entity != null }">value="${entity.std}"</c:if>   />
		    		</div>
				 </div>
			</div>
			<div class="row">
				 <div  class="col-sm-4 form-group" >
				 	<label for="eta" class="col-sm-4 control-label">预落</label>
			    	<div class="col-sm-8">
		    			<input type="text" class="form-control" name="eta" id="eta" <c:if test="${entity != null }">value="${entity.eta}"</c:if>   />
		    		</div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="echeckinTm" class="col-sm-4 control-label">结柜时间</label>
			    	<div class="col-sm-8">
		    			<input type="text" class="form-control" name="echeckinTm" id="echeckinTm" <c:if test="${entity != null }">value="${entity.echeckinTm}"</c:if>   />
		    		</div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="std" class="col-sm-4 control-label">预起</label>
			    	<div class="col-sm-8">
		    			<input type="text" class="form-control" name="etd" id="etd" <c:if test="${entity != null }">value="${entity.etd}"</c:if>   />
		    		</div>
				 </div>
			</div>
			<div class="row">
				 <div  class="col-sm-4 form-group" >
				 	<label for="ata" class="col-sm-4 control-label">实落</label>
			    	<div class="col-sm-8">
		    			<input type="text" class="form-control" name="ata" id="ata" <c:if test="${entity != null }">value="${entity.ata}"</c:if>   />
		    		</div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="brdBtm" class="col-sm-4 control-label">登机开始时间</label>
			    	<div class="col-sm-8">
		    			<input type="text" class="form-control" name="brdBtm" id="brdBtm" <c:if test="${entity != null }">value="${entity.brdBtm}"</c:if>   />
		    		</div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="pushOffTm" class="col-sm-4 control-label">推出</label>
			    	<div class="col-sm-8">
		    			<input type="text" class="form-control" name="pushOffTm" id="pushOffTm" <c:if test="${entity != null }">value="${entity.pushOffTm}"</c:if>   />
		    		</div>
				 </div>
			</div>
			<div class="row">
				 <div  class="col-sm-4 form-group" >
				 	<label for="standTm" class="col-sm-4 control-label">入位</label>
			    	<div class="col-sm-8">
		    			<input type="text" class="form-control" name="standTm" id="standTm" <c:if test="${entity != null }">value="${entity.standTm}"</c:if>   />
		    		</div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="brdEtm" class="col-sm-4 control-label">登机结束时间</label>
			    	<div class="col-sm-8">
		    			<input type="text" class="form-control" name="brdEtm" id="brdEtm" <c:if test="${entity != null }">value="${entity.brdEtm}"</c:if>   />
		    		</div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="atd" class="col-sm-4 control-label">实起</label>
			    	<div class="col-sm-8">
		    			<input type="text" class="form-control" name="atd" id="atd" <c:if test="${entity != null }">value="${entity.atd}"</c:if>   />
		    		</div>
				 </div>
			</div>
			<div class="row">
				 <div  class="col-sm-4 form-group" >
				 	<label for="inPaxNum" class="col-sm-4 control-label">进港旅客人数</label>
			    	<div class="col-sm-8">
		    			<input type="text" class="form-control" name="inPaxNum" id="inPaxNum" <c:if test="${entity != null }">value="${entity.inPaxNum}"</c:if>   />
		    		</div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="goodOpenTm" class="col-sm-4 control-label">货舱开启时间</label>
			    	<div class="col-sm-8">
		    			<input type="text" class="form-control" name="goodOpenTm" id="goodOpenTm" <c:if test="${entity != null }">value="${entity.goodOpenTm}"</c:if>   />
		    		</div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="ftsatTm" class="col-sm-4 control-label">首次TSAT</label>
			    	<div class="col-sm-8">
		    			<input type="text" class="form-control" name="ftsatTm" id="ftsatTm" <c:if test="${entity != null }">value="${entity.ftsatTm}"</c:if>   />
		    		</div>
				 </div>
			</div>
			<div class="row">
				 <div  class="col-sm-4 form-group" >
				 	<label for="outPaxNum" class="col-sm-4 control-label">出港旅客人数</label>
			    	<div class="col-sm-8">
		    			<input type="text" class="form-control" name="outPaxNum" id="outPaxNum" <c:if test="${entity != null }">value="${entity.outPaxNum}"</c:if>   />
		    		</div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="goodCloseTm" class="col-sm-4 control-label">货舱关闭时间</label>
			    	<div class="col-sm-8">
		    			<input type="text" class="form-control" name="goodCloseTm" id="goodCloseTm" <c:if test="${entity != null }">value="${entity.goodCloseTm}"</c:if>   />
		    		</div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="tsat" class="col-sm-4 control-label">TSAT</label>
			    	<div class="col-sm-8">
		    			<input type="text" class="form-control" name="tsat" id="tsat" <c:if test="${entity != null }">value="${entity.tsat}"</c:if>   />
		    		</div>
				 </div>
			</div>
			<div class="row">
				<div class="col-sm-11 panel panel-default" style="margin-left:4%">
				  <div class="panel-heading">
				    <h3 class="panel-title">流程记录</h3>
				  </div>
				  <div class="panel-body">
				    <textarea class="layui-textarea form-control" rows="" name="procRecord" id="procRecord" style="background-color:#002F63;color:#ffffff"><c:if test="${entity != null}">${entity.procRecord }</c:if></textarea>
				  </div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-11 panel panel-default" style="margin-left:4%">
				  <div class="panel-heading">
				    <h3 class="panel-title">事件记录</h3>
				  </div>
				  <div class="panel-body">
				  	<textarea class="layui-textarea form-control" rows="" name="eventRecord" id="eventRecord" style="background-color:#002F63;color:#ffffff"><c:if test="${entity != null}">${entity.eventRecord }</c:if></textarea>
				  </div>
				</div>
			</div>
		</form>
	</body>
</html>