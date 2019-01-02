<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>资源状态</title>
</head>
<body>
	<table class="layui-table">
		<tbody>
			<tr>
				<td>机位</td>
				<td align="center">${result.in_actstand_code }</td>
				<td>航站楼</td>
				<td align="center">${result.out_terminal_code }</td>
			</tr>
			<tr>
				<td>机号</td>
				<td align="center">${result.in_aircraft_number }</td>
				<td>登机状态</td>
				<td align="center"></td>
			</tr>
			<tr>
				<td>行李转盘</td>
				<td align="center">${result.in_bag_crsl }</td>
				<td>延误原因</td>
				<td align="center">${result.out_delay_reason }</td>
<%-- 				<td align="center">${result.out_acdm_start_delay_reason }</td> --%>
			</tr>
			<tr>
				<td>行李分拣口</td>
				<td align="center">${result.out_bag_chute }</td>
				<td>值机状态</td>
				<td align="center"></td>
			</tr>
			<tr>
				<td>放行延误</td>
				<td align="center">${result.out_release_reason }</td>
<%-- 				<td align="center">${result.out_acdm_rls_delay_reason }</td> --%>
				<td>值机柜台</td>
				<td align="center">${result.out_checkin_counter }</td>
			</tr>
			<tr>
				<td>航班状态</td>
				<td align="center">${result.out_status }</td>
				<td></td>
				<td></td>
			</tr>
		</tbody>
	</table>
</body>
</html>