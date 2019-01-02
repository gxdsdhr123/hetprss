<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp" %>
<link href="${ctxStatic}/prss/telegraph/css/telegraph.css" rel="stylesheet" />

<title>报文手动解析</title>
<style type="text/css">
	td{
		width: 16.6%;
	}
	.red{
		color: red;
	}
</style>
</head>

<body>
	<div class="rows">
		<div class="box-body">
			<div class="col-md-5">报文原文：</div>
			<div class="col-md-7">解析结果：</div>
		</div>
		<div class="col-md-5">
			<textarea rows="" cols="" class="layui-textarea textarea">${analysis.XMLDATA }</textarea>
		</div>
		<div class="col-md-7">
			<table id="tTable" class="layui-table tree_table">
				<tbody>
					<tr>
						<td>航班号</td>
						<td id="fligthNumber"></td>
						<td>进港计落</td>
						<td id="jl"></td>
						<td>出港计起</td>
						<td id="jq"></td>
					</tr>
					<tr>
						<td>进港预落</td>
						<td id="eta"></td>
						<td>机位</td>
						<td id="jw"></td>
						<td>机型</td>
						<td id="jx"></td>
					</tr>
					<tr>
						<td>报文ETA</td>
						<td id="bweta"></td>
						<td>报文解析机号</td>
						<td id="bwjh"></td>
						<td>报文解析机型</td>
						<td id="bwjx"></td>
					</tr>
					<tr>
						<td>进港人数</td>
						<td colspan="5" id="peg"></td>
					</tr>
					<tr>
						<td>延误信息</td>
						<td colspan="5" id="delayInfo"></td>
					</tr>
					<tr>
						<td>延误时间</td>
						<td colspan="5" id="delayTime"></td>
					</tr>
					<tr>
						<td rowspan="2">SI</td>
						<td colspan="5" id="si" rowspan="2"></td>
					</tr>
					<tr></tr>
				</tbody>
			</table>
			<div class="row text-center" id="btn-group">
				<div class="col-sm-12">
					<button type="button" class="layui-btn layui-btn-primary"
						onclick="analysis();">解析</button>
					<button type="button" onclick="reset();"
						 class="layui-btn layui-btn-primary">重置</button>
<%-- 				<c:if test="${type!=1 }"> --%>
<!-- 					<button type="button"  class="layui-btn layui-btn-primary" -->
<!-- 						onclick="back();">返回</button> -->
<%-- 				</c:if> --%>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/telegraph/autoanalysis.js"></script>
</body>
</html>