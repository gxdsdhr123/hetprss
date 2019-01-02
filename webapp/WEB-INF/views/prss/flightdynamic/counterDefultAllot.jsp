<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>航班动态-值机柜台默认设置</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/prss/flightdynamic/css/flightDynSet.css" rel="stylesheet" />
<style type="text/css">
.list-group-item{
	-moz-user-select: -moz-none;
   	-khtml-user-select: none;
   	-webkit-user-select: none;
   	-ms-user-select: none;
   	user-select: none;
   	cursor:unset !important;
}
.ui-state-highlight { 
	height: 42px;
 	line-height: 42px; 
}
#tableDiv{
	height: calc(100% - 63px);
width: 100%;
position: absolute;
top: 63px;
}
.fixed-table-loading{
	top:0px !important;
}
.pageType2,.pageType2btn {
	display:none;
}
.select2-selection__clear {
	right:10px;
}
</style>
<script type="text/javascript">
	var counters = ${counters};
</script>
</head>
<body>
	<div class="layui-container" style="height: 100%;">
		<form class="layui-form" action="">
			  <div class="layui-inline">
			    <label class="layui-form-label">国内/国际</label>
			    <div class="layui-input-inline layui-col-md6" style="margin-top: 6px">
			    	<select id="fltAttrCode" name="fltAttrCode" lay-filter="fltAttrCode">
					  <option value="D" <c:if test='${dim eq "D"}'>selected</c:if>>国内</option>
					  <option value="I" <c:if test='${dim eq "I"}'>selected</c:if>>国际</option>
					  <option value="M" <c:if test='${dim eq "M"}'>selected</c:if>>混装</option>
					</select>
			    </div>
			  </div>
			  <div class="layui-inline">
			    <label class="layui-form-label">值机岛</label>
			    <div class="layui-input-inline layui-col-md6" style="margin-top: 6px">
			    	<select id="counterIsland" name="counterIsland" lay-filter="counterIsland">
			    		<c:forEach items="${islands}" var="item">
							<option value="${item.code}" <c:if test="${island eq item.code}">selected</c:if>>${item.name}</option>
						</c:forEach>
					</select>
			    </div>
			  </div>
			  
			  <!-- 
			  <div class="layui-inline" style="margin-left:20px">
			    <button id="ok" class="layui-btn" type="button" style="width:100px">确定</button>
			  </div> 
			  -->
			  
			  <div class="layui-inline pageType2btn" style="margin-left:20px">
			    <button id="addRow" class="layui-btn" type="button" style="width:100px">增加行</button>
			  </div>
			  <div class="layui-inline pageType2btn" style="margin-left:20px">
			    <button id="delRow" class="layui-btn" type="button" style="width:100px">删除行</button>
			  </div>
			  <div class="layui-inline pageType2btn" style="margin-left:20px">
			    <button id="reset" class="layui-btn" type="button" style="width:100px">重置</button>
			  </div>
		</form>
		<div class="container" style="height:calc(100% - 63px);position:relative">
			<div id="leftDiv" class="pageType1">
				<div id="leftTitle">
					<label for="keyword" class="col-sm-3 control-label">可选字段</label>
				    <div class="col-sm-6">
				      <input type="text" class="form-control" id="keyword" placeholder="请输入关键字" oninput="search(event)">
				    </div>
				    <button type="button" class="btn btn-default col-sm-3 chooseAll " style="margin-top:3px">全选</button>
				</div>
				<ul class="list-group sortable">
					<c:forEach items="${choose}" var="cse">
						<li class="list-group-item" onmousedown="liDown(this);" onmouseenter="liEnter(this);" data-code="${cse.code}" data-aln3code="${cse.aln3code}" data-enname="${cse.enname}" data-name="${cse.name}">${cse.code}</li>
					</c:forEach>
				</ul>
			</div>
			<div id="middleDiv" class="pageType1">
				<button id="pushright" type="button" class="btn btn-default fa fa-angle-double-right"></button>
				<button id="pushleft" type="button" class="btn btn-default fa fa-angle-double-left"></button>
			</div>
			<div id="rightDiv" class="pageType1">
				<div id="rightTitle">
					<label for="keyword" class="col-sm-3 control-label">已选字段</label>
				    <div class="col-sm-6">
				      <input type="text" class="form-control" id="choosed-keyword" placeholder="请输入关键字" oninput="search(event)">
				    </div>
				    <button type="button" class="btn btn-default col-sm-3 chooseAll" style="margin-top:3px">全选</button>
				</div>
				<div class="sortable">
					<ul class="list-group choosedField">
				  	<c:forEach items="${choosed}" var="csed">
						<li class="list-group-item" onmousedown="liDown(this);" onmouseenter="liEnter(this);" data-code="${csed.code}" data-aln3code="${csed.aln3code}" data-enname="${csed.enname}" data-name="${csed.name}">${csed.code}</li>
					</c:forEach>
				</ul>
				</div>
			</div>
		</div>
		<div id="tableDiv" class="pageType2">
			<table id="MTable"></table>
		</div>
	</div>
	
	<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/counterDefultAllot.js"></script>
</body>
</html>