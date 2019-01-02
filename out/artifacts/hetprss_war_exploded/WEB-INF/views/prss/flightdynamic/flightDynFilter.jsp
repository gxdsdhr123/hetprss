<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<style>
.layui-layer-page .layui-layer-content{
	overflow:hidden !important;
}
</style>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<div id="filterDiv" style="display: none !important">
	<form id="filterForm" class="form-horizontal">
		<div class="form-group">
			<label class="col-sm-1 control-label">航空公司</label>
			<div class="col-sm-2">
				<input name="airline" placeholder="请选择" class="layui-input" type="text" readonly="readonly"
					onclick="openCheck('airline')">
				<input name="airlinevalue" style="display: none;">
			</div>
			<label class="col-sm-1 control-label">机位</label>
			<div class="col-sm-2">
				<select name="bay" id="bay" class="mutilselect2 form-control" multiple="multiple">
					<option value=''></option>
					<c:forEach items="${bay}" var="dimbay">
						<option value="${dimbay.bay_code}">${dimbay.description_cn}</option>
					</c:forEach>
				</select>
			</div>
			
			
			<label class="col-sm-1 control-label">近机位/远机位</label>
			<div class="col-sm-2">
				<select name="GAFlag" id="GAFlag" class="mutilselect2 form-control"  multiple="multiple">					
					<option value=''>请选择</option>
					<option value='2'>远机位</option>
					<option value='1'>近机位有廊桥</option>
					<option value='3'>近机位无廊桥</option>
				</select>
			</div>
			
			
		</div>
		<div class="form-group">
		<label class="col-sm-1 control-label">起场</label>
			<div class="col-sm-2">
				<input name="departAirport" placeholder="请选择" class="layui-input" type="text" readonly="readonly"
					onclick="openCheck('departAirport')">
				<input name="departAirportvalue" style="display: none;">
			</div>
			<label class="col-sm-1 control-label">落场</label>
			<div class="col-sm-2">
				<input name="arriveAirport" placeholder="请选择" class="layui-input" type="text"
					readonly="readonly" onclick="openCheck('arriveAirport')">
				<input name="arriveAirportvalue" style="display: none;">
			</div>
			<label class="col-sm-1 control-label">国内/国际</label>
			<div class="col-sm-2">
				<select name="alntype" class="mutilselect2 form-control alntype" multiple="multiple">
					
					<option value=''>请选择</option>
					<c:forEach items="${alntype}" var="alnType">
						<option value="${alnType.value}">${alnType.text}</option>
					</c:forEach>
				</select>
			</div>
			
		</div>
		<div class="form-group">
		<label class="col-sm-1 control-label">性质</label>
			<div class="col-sm-2">
				<select name="fltPropertys" class="mutilselect2 form-control fltPropertys" lay-search  multiple="multiple">
					
					<option value=''>请选择</option>
					<c:forEach items="${fltPropertys}" var="fltPropertys">
						<option value="${fltPropertys.value}">${fltPropertys.text}</option>
					</c:forEach>
				</select>
			</div>
			<label class="col-sm-1 control-label" style="padding-left: 0px;padding-right: 0px;width: 6%;margin-left: 20px;">飞机类型</label>
			<div class="col-sm-2" style="padding-top: 7px; padding-right: 0px;width: 15%;margin-left: 12px;">
				<c:forEach items="${actkinds}" var="actkind">
					<input name='actkinds' lay-skin="primary" lay-filter="actkinds" value="${actkind.value}" type="checkbox">&nbsp;${actkind.text}&nbsp;
					</c:forEach>
			</div>
					<label class="col-sm-1 control-label" style="margin-left: 20px;">除冰航班</label>
			<div class="col-sm-2" style="margin-left: 4px;">
				<select class="mutilselect2 form-control iceFlt" name="iceFlt"  multiple="multiple">
					<option value=''>不限</option>
					<c:forEach items="${iceFlags}" var="iceFlag">
					<option value="${iceFlag.value}">${iceFlag.text}</option>
					</c:forEach>
				</select>
			</div>		
			
			
		</div>

	
		
		<!------------------------------------------ 分割线------------------------------------------------------------------------>
<!-- 		<div class="col-sm-12 col-md-12" style="border: 1px; border-style: solid; margin-bottom: 30px;"></div> -->
		<!------------------------------------------ 分割线------------------------------------------------------------------------>
		<div class="form-group">
			<label class="col-sm-1 control-label">状态</label>
			<div class="col-sm-2">
				<select name="acfStatus" class="mutilselect2 form-control actStatus" multiple="multiple">
					
					<option value=''>请选择</option>
					<c:forEach items="${actStatus}" var="actStatus">
						<option value="${actStatus.value}">${actStatus.text}</option>
					</c:forEach>
				</select>
			</div>
			<label class="col-sm-1 control-label">延误类型</label>
			<div class="col-sm-2">
				<select name="delay" id="delay" class="mutilselect2 form-control actStatus"  multiple="multiple">					
					<option value=''>请选择</option>
					<c:forEach items="${delay_type}" var="delay">
						<option value="${delay.value}">${delay.text}</option>
					</c:forEach>
					
				</select>
			</div>
			<label class="col-sm-1 control-label">延误原因</label>
			<div class="col-sm-2">
				<select name="delyReson" id="delyReson" class="mutilselect2 form-control actStatus"  multiple="multiple">
					
					<option value=''>请选择</option>
					<c:forEach items="${delyReson}" var="delyReson">
						<option value="${delyReson.id}">${delyReson.text}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="form-group">	
		<label class="col-sm-1 control-label">机号</label>
			<div class="col-sm-2">
<!-- 				<input name=aircraftNo placeholder="请选择" class="layui-input" type="text" readonly="readonly" -->
<!-- 					onclick="openCheck('aircraftNo')"> -->
<!-- 				<input name="aircraftNovalue" style="display: none;"> -->
			<input name="aircraftNo" placeholder="多个机号之间请使用空格分隔" class="layui-input" type="text"  >
			</div>	
		<label class="col-sm-1 control-label">机型</label>
			<div class="col-sm-2">
				<input name="actType" placeholder="请选择" class="layui-input" type="text" readonly="readonly"
					onclick="openCheck('actType')">
				<input name="actTypevalue" style="display: none;">
			</div>
			<label class="col-sm-1 control-label">登机口</label>
			<div class="col-sm-2">
				<input name="gate" id="gate" placeholder="请选择" class="layui-input" type="text" readonly="readonly"
					onclick="openCheck('gate')">
				<input name="gatevalue" style="display: none;">
			</div>	
		</div>
		<!------------------------------------------ 分割线------------------------------------------------------------------------>
<!-- 		<div class="col-sm-12 col-md-12" style="border: 1px; border-style: solid; margin-bottom: 30px;"></div> -->
		<!------------------------------------------ 分割线------------------------------------------------------------------------>

		<div class="form-group">
		
			<label class="col-sm-1 control-label">时间</label>
			
			<div class="col-sm-2">
				<select name="timetype" id="timetype" class="form-control timetype">
					
					<option value=''>请选择</option>
					<option value="in_tab.std">进港计起</option>
					<option value="in_tab.etd">进港预起</option>
					<option value="in_tab.atd">进港实起</option>
					<option value="in_tab.sta">进港计落</option>
					<option value="in_tab.eta">进港预落</option>
					<option value="in_tab.ata">进港实落</option>
					<option value="out_tab.std">出港计起</option>
					<option value="out_tab.etd">出港预起</option>
					<option value="out_tab.atd">出港实起</option>
				</select>
			</div>
		
			<div class="col-sm-2">
				<input class="col-md-5 layui-input" id="beginTime"
					onkeyup="this.value=this.value.replace(/[^0-9-]+/,'');" maxlength="4" />
				<label class="col-sm-2 control-label text-center">至</label>
				<input class="col-md-5 layui-input" id="endTime"
					onkeyup="this.value=this.value.replace(/[^0-9-]+/,'');" maxlength="4" />
			</div>
			
			<label class="col-sm-1 control-label" style="padding-left: 0px;">（四位数字）</label>
			
			<div class="col-sm-3">
				<label class="col-sm-2 control-label text-center">前</label>
				<input class="col-md-4 layui-input" id="beforeHour"
					onkeyup="this.value=this.value.replace(/[^0-9-]+/,'');" maxlength="4" />
				<label class="col-sm-2 control-label text-center">后</label>
				<input class="col-md-4 layui-input" id="afterHour"
					onkeyup="this.value=this.value.replace(/[^0-9-]+/,'');" maxlength="4" />
			</div>
			
			
		</div>
		
		<div class="form-group">
		
			<label class="col-sm-1 control-label">始发/航后</label>
			<div class="col-sm-2">
				<select name="stayFlag" class="mutilselect2 form-control stayFlag"  multiple="multiple">
					
					<option value="">请选择</option>
					<option value="D0">始发</option>
					<option value="A0">过夜</option>
					<option value="A1D1">过站</option>
				</select>
			</div>
			<c:if test="${historyFlag!='Y'}">
				<label class="col-sm-1 control-label">日期</label>
				<div class="col-sm-2">
					<div class="layui-input-inline">
						<div class="input-group">
		                  	<input id="searchFlightDate" name="searchFlightDate"
							type="text" readonly="readonly" maxlength="20" class="layui-input"
							value="<fmt:formatDate value="${log.beginDate}" pattern="yyyy-MM-dd"/>"
							onclick="WdatePicker({dateFmt:'yyyyMMdd',isShowClear:false});" />
							<div class="input-group-addon"><i class="fa fa-calendar"></i> </div>
		                </div>
					</div>
				</div>
			</c:if>
		
			
			
		
		</div>
		<c:if test="${schemaId==11}">
		<hr>
			<div class="form-group">
				<label class="col-sm-1 control-label" >筛选项</label>
				<div class="col-sm-1">
					<input type="checkbox" id="HHXcheck" value="" > 海航系
				</div>
				<label class="col-sm-1 control-label" >航空公司</label>
				<div class="col-sm-2" >
					<input name="HHXairline" placeholder="请选择" class="layui-input ignoreClear" type="text" readonly="readonly"
					onclick="openCheck('HHXairline')" 
					<c:forEach items="${filterConfArr}" var="item" >
			          <c:if test="${item.TYPE=='HHX'}"> value="${item.AIRLINE_CODE}" </c:if>
			         </c:forEach>/>
					<input name="HHXairlinevalue" style="display: none;" class="ignoreClear"
					<c:forEach items="${filterConfArr}" var="item" >
			          <c:if test="${item.TYPE=='HHX'}"> value="${item.AIRLINE_CODE}" </c:if>
			         </c:forEach>>
				</div>
				<label class="col-sm-1 control-label">国内国际标识</label>
				<div class="col-sm-2">
					<input name="HHXalntype" placeholder="请选择" class="layui-input ignoreClear" type="text" readonly="readonly"
					onclick="openCheck('HHXalntype')"
					<c:forEach items="${filterConfArr}" var="item" >
			          <c:if test="${item.TYPE=='HHX'}"> value="${item.ATTR_TYPE_CN}" </c:if>
			         </c:forEach>>
					<input name="HHXalntypevalue" style="display: none;" class="ignoreClear"
					<c:forEach items="${filterConfArr}" var="item" >
			          <c:if test="${item.TYPE=='HHX'}"> value="${item.ATTR_TYPE}" </c:if>
			         </c:forEach>>
				</div>	
			</div>
			<div class="form-group">
				<label class="col-sm-1 control-label" >筛选项</label>
				<div class="col-sm-1">
					<input type="checkbox" id="GHXcheck" value="" > 国航系
				</div>
				<label class="col-sm-1 control-label" >航空公司</label>
				<div class="col-sm-2" >
					<input name="GHXairline" placeholder="请选择" class="layui-input ignoreClear" type="text" readonly="readonly"
					onclick="openCheck('GHXairline')"
					<c:forEach items="${filterConfArr}" var="item" >
			          <c:if test="${item.TYPE=='GHX'}"> value="${item.AIRLINE_CODE}" </c:if>
			         </c:forEach>>
					<input name="GHXairlinevalue" style="display: none;" class="ignoreClear"
					<c:forEach items="${filterConfArr}" var="item" >
			          <c:if test="${item.TYPE=='GHX'}"> value="${item.AIRLINE_CODE}" </c:if>
			         </c:forEach>>
				</div>
				<label class="col-sm-1 control-label">国内国际标识</label>
				<div class="col-sm-2">
					<input name="GHXalntype" placeholder="请选择" class="layui-input ignoreClear" type="text" readonly="readonly"
					onclick="openCheck('GHXalntype')"
					<c:forEach items="${filterConfArr}" var="item" >
			          <c:if test="${item.TYPE=='GHX'}"> value="${item.ATTR_TYPE_CN}" </c:if>
			         </c:forEach>>
					<input name="GHXalntypevalue" style="display: none;" class="ignoreClear"
					<c:forEach items="${filterConfArr}" var="item" >
			          <c:if test="${item.TYPE=='GHX'}"> value="${item.ATTR_TYPE}" </c:if>
			         </c:forEach>>
				</div>	
			</div>
			<div class="form-group">
				<label class="col-sm-1 control-label" >筛选项</label>
				<div class="col-sm-1">
					<input type="checkbox" id="GJcheck" value="" > 国际
				</div>
				<label class="col-sm-1 control-label" >航空公司</label>
				<div class="col-sm-2" >
					<input name="GJairline" placeholder="请选择" class="layui-input ignoreClear" type="text" readonly="readonly"
					onclick="openCheck('GJairline')"
					<c:forEach items="${filterConfArr}" var="item" >
			          <c:if test="${item.TYPE=='GJ'}"> value="${item.AIRLINE_CODE}" </c:if>
			         </c:forEach>>
					<input name="GJairlinevalue" style="display: none;" class="ignoreClear"
					<c:forEach items="${filterConfArr}" var="item" >
			          <c:if test="${item.TYPE=='GJ'}"> value="${item.AIRLINE_CODE}" </c:if>
			         </c:forEach>>
				</div>
				<label class="col-sm-1 control-label">国内国际标识</label>
				<div class="col-sm-2">
					<input name="GJalntype" placeholder="请选择" class="layui-input ignoreClear" type="text" readonly="readonly"
					onclick="openCheck('GJalntype')"
					<c:forEach items="${filterConfArr}" var="item" >
			          <c:if test="${item.TYPE=='GJ'}"> value="${item.ATTR_TYPE_CN}" </c:if>
			         </c:forEach>>
					<input name="GJalntypevalue" style="display: none;" class="ignoreClear"
					<c:forEach items="${filterConfArr}" var="item" >
			          <c:if test="${item.TYPE=='GJ'}"> value="${item.ATTR_TYPE}" </c:if>
			         </c:forEach>>
				</div>		
			</div>
			<div class="form-group">
				<label class="col-sm-1 control-label" >筛选项</label>
				<div class="col-sm-1">
					<input type="checkbox" id="QTcheck" value="" > 其他
				</div>
				<label class="col-sm-1 control-label" >航空公司</label>
				<div class="col-sm-2" >
					<input name="QTairline" placeholder="请选择" class="layui-input ignoreClear" type="text" readonly="readonly"
					onclick="openCheck('QTairline')" 
					<c:forEach items="${filterConfArr}" var="item" >
			          <c:if test="${item.TYPE=='QT'}"> value="${item.AIRLINE_CODE}" </c:if>
			         </c:forEach>/>
					<input name="QTairlinevalue" style="display: none;" class="ignoreClear"
					<c:forEach items="${filterConfArr}" var="item" >
			          <c:if test="${item.TYPE=='QT'}"> value="${item.AIRLINE_CODE}" </c:if>
			         </c:forEach>>
				</div>
				<label class="col-sm-1 control-label">国内国际标识</label>
				<div class="col-sm-2">
					<input name="QTalntype" placeholder="请选择" class="layui-input ignoreClear" type="text" readonly="readonly"
					onclick="openCheck('QTalntype')"
					<c:forEach items="${filterConfArr}" var="item" >
			          <c:if test="${item.TYPE=='QT'}"> value="${item.ATTR_TYPE_CN}" </c:if>
			         </c:forEach>>
					<input name="QTalntypevalue" style="display: none;" class="ignoreClear"
					<c:forEach items="${filterConfArr}" var="item" >
			          <c:if test="${item.TYPE=='QT'}"> value="${item.ATTR_TYPE}" </c:if>
			         </c:forEach>>
				</div>		
			</div>
		</c:if>
		
	</form>
</div>