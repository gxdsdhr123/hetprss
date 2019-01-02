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
	<form id="filterForm" class="form-horizontal layui-form">
		<div class="form-group">
			<label class="col-sm-1 control-label">日期</label>
			<div  class="col-sm-3">
				<input class="col-md-5 layui-input" id="dateB" name="dateB"  onclick="WdatePicker({dateFmt:'yyyyMMdd'});" class="layui-input" readonly="readonly" value="" type="text">
				<label class="col-sm-2 control-label text-center">至</label>
				<input class="col-md-5 layui-input" id="dateE" name="dateE"  onclick="WdatePicker({dateFmt:'yyyyMMdd'});" class="layui-input" readonly="readonly" value="" type="text">
			</div>
			
			<label class="col-sm-1 control-label">时间</label>
			<div class="col-sm-3">
				<input class="col-md-5 layui-input" id="beginTime" name="beginTime" 
					onkeyup="this.value=this.value.replace(/[^0-9-]+/,'');" maxlength="4" />
				<label class="col-sm-2 control-label text-center">至</label>
				<input class="col-md-5 layui-input" id="endTime" name="endTime" 
					onkeyup="this.value=this.value.replace(/[^0-9-]+/,'');" maxlength="4" />
			</div>
			<label class="col-sm-1 control-label" style="padding-left: 0px;">（四位数字）</label>
		</div>
		<div class="form-group">
			<label class="col-sm-1 control-label">航空公司</label>
			<div class="col-sm-2">
				<input id="airline" name="airline" placeholder="请选择" class="layui-input" type="text" readonly="readonly"
					onclick="openCheck('airline')">
				<input id="airlinevalue" name="airlinevalue" style="display: none;">
			</div>
			<label class="col-sm-1 control-label">机坪区域</label>
			<div class="col-sm-2">
				<select id="aprons" name="aprons" class="form-control aprons">					
					<option value='!'>不限</option>
					<option value='1'>1号机坪</option>
					<option value='2'>2号机坪</option>
					<option value='3'>备降机坪</option>
				</select>
			</div>
			<label class="col-sm-1 control-label">廊桥/远机位</label>
			<div class="col-sm-2">
				<select id="GAFlag" name="GAFlag" class="form-control GAFlag">					
					<option value='!'>请选择</option>
					<c:forEach items="${GAFlag}" var="GAFlag">
						<option value="${GAFlag.value}">${GAFlag.text}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="form-group">
		<label class="col-sm-1 control-label">起场</label>
			<div class="col-sm-2">
				<input id="departAirport" name="departAirport" placeholder="请选择" class="layui-input" type="text" readonly="readonly"
					onclick="openCheck('departAirport')">
				<input id="departAirportvalue" name="departAirportvalue" style="display: none;">
			</div>
			<label class="col-sm-1 control-label">落场</label>
			<div class="col-sm-2">
				<input id="arriveAirport" name="arriveAirport" placeholder="请选择" class="layui-input" type="text"
					readonly="readonly" onclick="openCheck('arriveAirport')">
				<input id="arriveAirportvalue" name="arriveAirportvalue" style="display: none;">
			</div>
			<label class="col-sm-1 control-label">国内/国际</label>
			<div class="col-sm-2">
				<select id="alntype" name="alntype" class="form-control alntype">
					<option value='!'>请选择</option>
					<c:forEach items="${alntype}" var="alnType">
						<option value="${alnType.value}">${alnType.text}</option>
					</c:forEach>
				</select>
			</div>
			
		</div>
		<div class="form-group">
		<label class="col-sm-1 control-label">性质</label>
			<div class="col-sm-2">
				<select id="fltPropertys" name="fltPropertys" class="form-control fltPropertys">
					<option value='!'>请选择</option>
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
				<select class="form-control iceFlt" id="iceFlt" name="iceFlt">
					<option value='!'>不限</option>
					<option value="1">上客前除冰</option>
					<option value="2">上客后除冰</option>
					<option value="3">推出后除冰</option>
				</select>
			</div>		
		</div>
		
		<!------------------------------------------ 分割线------------------------------------------------------------------------>
<!-- 		<div class="col-sm-12 col-md-12" style="border: 1px; border-style: solid; margin-bottom: 30px;"></div> -->
		<!------------------------------------------ 分割线------------------------------------------------------------------------>
		<div class="form-group">
			<label class="col-sm-1 control-label">状态</label>
			<div class="col-sm-2">
				<select id="actStatus" name="actStatus" class="form-control actStatus">
					<option value='!'>请选择</option>
					<c:forEach items="${actStatus}" var="actStatus">
						<option value="${actStatus.value}">${actStatus.text}</option>
					</c:forEach>
				</select>
			</div>
			<label class="col-sm-1 control-label">延误</label>
			<div class="col-sm-2">
				<select id="delayType" name="delayType" class="form-control delayType">					
					<option value='!'>不限</option>
					<option value='0'>保障延误</option>
					<option value='1'>放行延误</option>
					<option value='2'>出港延误</option>
				</select>
			</div>
			<label class="col-sm-1 control-label">延误原因</label>
			<div class="col-sm-2">
				<select id="delyReson" name="delyReson" class="form-control delyReson">
					<option value='!'>请选择</option>
					<c:forEach items="${delyReson}" var="delyReson">
						<option value="${delyReson.id}">${delyReson.text}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="form-group">	
		<label class="col-sm-1 control-label">机号</label>
			<div class="col-sm-2">
				<input id="aircraftNo" name="aircraftNo" placeholder="请输入" class="layui-input" type="text">
			</div>	
		<label class="col-sm-1 control-label">机型</label>
			<div class="col-sm-2">
				<input id="actType" name="actType" placeholder="请选择" class="layui-input" type="text" readonly="readonly"
					onclick="openCheck('actType')">
				<input id="actTypevalue" name="actTypevalue" style="display: none;">
			</div>
			<label class="col-sm-1 control-label">登机口</label>
			<div class="col-sm-2">
				<input id="gate" name="gate" placeholder="请选择" class="layui-input" type="text" readonly="readonly"
					onclick="openCheck('gate')">
				<input id="gatevalue" name="gatevalue" style="display: none;">
			</div>	
		</div>
	</form>
</div>