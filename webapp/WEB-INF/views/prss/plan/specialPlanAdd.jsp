<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
var airportCodeSource = ${airportCodeSource};
var actTypeSource = ${actTypeSource};
var propertyCodeSource = ${propertyCodeSource};
var airlinesCodeSource = ${airlinesCodeSource};
var dataRows = '${dataRows}';
var operateType = '${operateType}';
</script>
</head>
<body>
	<div id="baseTables">
		<table id=baseTable></table>
	</div>
	<!-- 上传文件列表 -->
	<div id="upload" style="display: none">
	 	 <div class="layui-upload-list">
	    	<table class="layui-table">
		      	<thead>
		        	<tr>
		        		<th>文件名</th>
		        		<th>文件大小</th>
		        		<th>操作</th>
		      		</tr>
		      	</thead>
				<tbody>
					<tr>
						<td align="center"><span id="fileName"></span></td>
						<td align="center"><span id="fileSize"></span></td>
			      		<td align="center" style="width: 10%"><button class="layui-btn layui-btn-normal" id="chooseFile" type="button" onclick="fileInput.click()">选择文件</button></td>
		      		</tr>
		      		<tr>
						<td colspan="3" align="right">
							<button class="layui-btn" type="button" onclick="resetUpload()">重置</button>
							<button class="layui-btn" type="button" onclick="fileOnChange()">开始上传</button>
						</td>
		      		</tr>
		      	</tbody>
		    </table>
	  	</div>
	</div>
	<form id="fileList" action="${ctx}/plan/specialPlan/uploadAttachment" method="post" enctype="multipart/form-data" style="display:none;">
		<input type="text" id="oldFileId" name="oldFileId">
		<input id="fileInput" name="file" type="file" multiple="multiple" onchange="uploadOnChange(this,'fileName','fileSize')">
	</form>
	<script type="text/javascript" src="${ctxStatic}/prss/plan/specialPlanAdd.js"></script>
</body>
</html>