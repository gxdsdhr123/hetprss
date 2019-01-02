<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<link href="${ctxStatic}/jquery/plugins/jquery-ui/jquery-ui.min.css" rel="stylesheet" />
<link href="${ctxStatic}/prss/arrange/css/empPlanAdd.css" rel="stylesheet" />
<title>增加</title>
<script type="text/javascript">
var dateTp = "${selDate}";
var flag="${flag}";
</script>
</head>
<body style="overflow:none;">
	<form id="filterForm" class="layui-form">
		<div class="container" id="contentDiv">
			<div id="leftDiv">
				<div class="layui-form-item">
				    <label class="layui-form-label" style="width:60px;font-weight:600px;" >班组</label>
				    <div class="layui-input-inline" style="width:80px;">
				      <select name="groupInfo" id="groupInfo" lay-filter="groupSel" style="width:60px">
				      <option value="all">全部</option>
			       		<c:forEach items="${groupInfos}" var="groupInfo">
							<option value="${groupInfo.ID}">${groupInfo.NAME}</option>
						</c:forEach>
			     	  </select>
			     	</div>
			     	<div class="layui-input-inline" style="width:100px;">
			     	   <input type="text" class="form-control" tyle="width:40px" id="keyword" placeholder="请输入关键字" oninput="search(event)">
			     	</div>
				</div>
				<ul class="list-group sortable" id="empInfoUL" style="height:150px">
				</ul>
			</div>
			<div id="middleDiv">
				<button id="pushAllright" type="button" class="btn btn-default fa fa-angle-double-right"></button>
				<button id="pushright" type="button" class="btn btn-default fa fa-angle-right"></button>
				<button id="pushleft" type="button" class="btn btn-default fa fa-angle-left"></button>
			</div>
			<div id="rightDiv">
				<div id="rightTitle" >
					<label class="control-label">已选人员</label>
				</div>
				<ul class="list-group sortable choosedField" style="height:150px" id="selectEmpInfoUL">
				</ul>
			</div>
			
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label" style="width:110px;" >工作间隔(忙)</label>
			<div class="layui-input-inline" style="width:200px;">
		      <input type="text" class="form-control" id="busyInterval" 
		      	 value="${empPlanVO.busyInterval}" placeholder="只能填写数字，可填写负数">
		    </div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label" style="width:110px;" >工作间隔(闲)</label>
			<div class="layui-input-inline" style="width:200px;">
		      <input type="text" class="form-control" id="idleInterval" 
		      	 value="${empPlanVO.idleInterval}" placeholder="只能填写数字，可填写负数">
		    </div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label" style="width:110px;" >工作时段一</label>
			<div class="layui-input-inline" style="width:80px;">
				<input type="hidden" class="form-control" id="sid1"  value="${empPlanVO.shiftsId}" >
		      <input type="text" class="form-control" id="stime1"  value="${empPlanVO.stime1Label}" > 
		    </div>
		    <label class="layui-form-label" style="width:40px;">至</label>
		    <div class="layui-input-inline" style="width:80px;">
			    <input type="text" class="form-control" id="etime1"  value="${empPlanVO.etime1Label}" >
			</div>
			&nbsp;&nbsp;<button id="bzBtn1" type="button" class="btn btn-default btn2 btn-skin-blue">班制</button>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label" style="width:110px;" >工作时段二</label>
			<div class="layui-input-inline" style="width:80px;">
				<input type="hidden" class="form-control" id="sid2" value="">
		      <input type="text" class="form-control" id="stime2" disabled value="${empPlanVO.stime2Label}" >
		    </div>
		    <label class="layui-form-label" style="width:40px;">至</label>
		    <div class="layui-input-inline" style="width:80px;">
			    <input type="text" class="form-control" id="etime2" disabled value="${empPlanVO.etime2Label}" >
			</div>
			&nbsp;&nbsp;<button id="bzBtn2" type="button" disabled class="btn btn-default btn2 btn-skin-blue">班制</button>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label" style="width:110px;" >工作时段三</label>
			<div class="layui-input-inline" style="width:80px;">
				<input type="hidden" class="form-control" id="sid3" value="">
		      <input type="text" class="form-control" id="stime3" disabled value="${empPlanVO.stime3Label}">
		    </div>
		    <label class="layui-form-label" style="width:40px;">至</label>
		    <div class="layui-input-inline" style="width:80px;">
			    <input type="text" class="form-control" id="etime3" disabled value="${empPlanVO.etime3Label}" >
			</div>
			&nbsp;&nbsp;<button id="bzBtn3" type="button" disabled class="btn btn-default btn2 btn-skin-blue">班制</button>
		</div>
	</form>
	<script type="text/javascript" src="${ctxStatic}/jquery/plugins/jquery-ui/jquery-ui.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/prss/arrange/empPlanAdd.js"></script>
</body>
</html>