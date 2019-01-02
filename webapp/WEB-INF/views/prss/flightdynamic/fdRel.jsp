<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/jquery/plugins/jqGrid/plugins/jquery.tablednd.js"></script>
<script type="text/javascript" src="${ctxStatic}/bootstrap/plugins/bootstrap-table/extensions/reorder-rows/bootstrap-table-reorder-rows.js"></script>
<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/fdRel.js"></script>
<style>
	.btn-link{
		color:white;
	}
</style>
<title>机号拆分</title>
<script type="text/javascript">
	var airports = ${airports};
	var acfStatus = ${acfStatus};
	var gates = ${gates};
	var fltNum = "${fltNum}";
	/* var acregs = ${acregs}; */
</script>
</head>
<body>
<input type="hidden" value="${fltIds}" id="fltIds"> 
	<div class="container" style="width: 100%;position: relative;" id="container">
    	<div class="row" style="background: #021132;padding:5px;">
        	<div>
        		<div id="toolbarIO">
        			<div class="layui-inline">
    					<label class="layui-form-label">航班号:</label>
    					<div class="layui-input-inline">
      						<input type="text" id="fltNoIO" name="fltNoIO" class="layui-input">
    					</div>
  					</div>
  					  <div class="layui-inline">
    					<label class="layui-form-label">机号:</label>
    					<div class="layui-input-inline">
      						<input type="text" id="aircraftNoIO" name="aircraftNoIO" class="layui-input" >
    					</div>
  					</div>
  					<button id="searchBtnIO" type="button" class="btn btn-link">检索</button>
					<button id="delRelBtn" type="button" class="btn btn-link">拆分配对</button>
					<button id="delBtn" type="button" class="btn btn-link">删除</button>
        		</div>
        		<table id="fdIO" ></table>
        	</div>
    	</div>
    	<div class="row" style="background: #021132;padding:5px;">
        	<div class="col-xs-6 col-sm-6 col-md-6 col-lg-6 no-padding">
        		<div class="box box-solid">
					<div class="box-header with-border">
		              <h3 class="box-title">进港航班</h3>
		            </div>
		            <div class="box-body" style="min-height: 200px">
						<table id="fdI" data-use-row-attr-func="true" data-reorderable-rows="true"></table>
					</div>
				</div>
        	</div>
        	<div class="col-xs-6 col-sm-6 col-md-6 col-lg-6 no-padding" style="padding-left:3px!important;">
        		<div class="box box-solid" style="">
					<div class="box-header with-border">
		              <h3 class="box-title">出港航班</h3>
		            </div>
		            <div class="box-body" style="min-height: 200px">
						<table id="fdO" data-use-row-attr-func="true" data-reorderable-rows="true"></table>
					</div>
				</div>
        	</div>
    	</div>
		<div class="row" style="background: #021132;">	
        	<div id="toolbarNewIO" style="text-align:center;">
 				<button id="fltNosChargeBtn" type="button" class="btn btn-link">批量变更机号</button>
				<button id="autoRelBtn" type="button" class="btn btn-link" >自动配对</button>
				<button id="checkRelBtn" type="button" class="btn btn-link" >选中配对</button>
				<button id="dragRelBtn" type="button" class="btn btn-link">拖动配对</button>
				<button id="revRelBtn" type="button" class="btn btn-link">撤销配对</button>
       		</div>
       		<table id="fdNewIO"></table>
    	</div>
	</div>
	<form class="layui-form" action="" style="display:none" id="chgAircraftNoForm">
		<div class="layui-form-item">
		    <div class="layui-inline">
		      <label class="layui-form-label">变更后机号：</label>
		      <div class="layui-input-inline">
		        <input id="nweAircraftNo" class="layui-input" type="text" autocomplete="off" value="B" onkeyup="this.value = this.value.toUpperCase();">
		      </div>
		    </div>
		 </div>
	</form>
</body>
</html>