<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>航班动态（列表）</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/prss/flightdynamic/css/fdbaggage.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/fdbaggage.js"></script>
<script>
var inArray=${inArray};
var outArray=${outArray};
</script>
</head>
<body>
	<div class="col-md-12" style="margin-top: 20px;">
		<div class="col-md-2"></div>
		<div class="col-md-8 dataTables_wrapper form-inline dt-bootstrap">
			<table border="1" class="table table-bordered dataTable" role="grid">
				<tr class="trTitle">
					<td rowspan="2">类型</td>
					<td colspan="3">航班号（进） 起场：</td>
					<td colspan="3">航班号（出） 落场：</td>
				</tr>
				<tr class="trTitle">
					<td>舱位</td>
					<td>集装器号</td>
					<td>重量（公斤）</td>
					<td>舱位</td>
					<td>集装器号</td>
					<td>重量（公斤）</td>
				</tr>
				<tr name="luggage">
					<td class="tdTitle">行李</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				
				<tr name="luggageSum">
					<td class="tdTitle">合计重量/数量</td>
					<td class="tdTitle" colspan="3"></td>
					<td class="tdTitle" colspan="3"></td>
				</tr>
				<tr name="goods">
					<td  class="tdTitle">货物</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				
				<tr name="goodsSum">
					<td class="tdTitle">合计重量</td>
					<td class="tdTitle" colspan="3"></td>
					<td class="tdTitle" colspan="3"></td>
				</tr>
				<tr name="email">
					<td class="tdTitle">邮件</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				
				<tr name="emailSum">
					<td class="tdTitle">合计重量</td>
					<td class="tdTitle" colspan="3"></td>
					<td class="tdTitle" colspan="3"></td>
				</tr>
				<tr class="trTitle">
					<td rowspan="2">类型</td>
					<td colspan="3">特货</td>
					<td colspan="3">特货</td>
				</tr>
				<tr name="specialCargo" class="tdTitle">
					<td>舱位</td>
					<td>集装器号</td>
					<td>重量</td>
					<td>舱位</td>
					<td>集装器号</td>
					<td>重量</td>
				</tr>
				
			</table>
		</div>
		<div class="col-md-2"></div>
	</div>

</body>
</html>