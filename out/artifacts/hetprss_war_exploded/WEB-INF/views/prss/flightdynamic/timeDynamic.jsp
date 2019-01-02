<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>时间动态</title>
</head>
<body>
	<h3 style="margin-top:6px;">航班信息时间</h3>
	<table class="layui-table">
		<tr>
			<td width=12.5%>进港计落</td>
			<td width=12.5%>${result.in_sta}</td>
			<td width=12.5%>进港预落</td>
			<td width=12.5%>${result.in_eta}</td>
			<td width=12.5%>进港实落</td>
			<td width=12.5%>${result.in_ata}</td>
			<td width=12.5%>到港</td>
			<td width=12.5%></td>
		</tr>
		<tr>
			<td>进港滑行时长</td>
			<td>${result.in_jghxsc}</td>
			<td>预计入位</td>
			<td>${result.in_acdm_est_stand_tm}</td>
			<td>入位</td>
			<td>${result.in_acdm_stand_tm}</td>
			<td>入位延误时长</td>
			<td>${result.arrival_arrival_delay_tm}</td>
<%-- 			<td>${result.in_acdm_arrival_delay_tm}</td> --%>
		</tr>
		<tr>
			<td>出港计起</td>
			<td>${result.out_std}</td>
			<td>出港预起</td>
			<td>${result.out_etd}</td>
			<td>出港实起</td>
			<td>${result.out_atd}</td>
			<td>离港</td>
			<td></td>
		</tr>
		<tr>
			<td>登机开始</td>
			<td>${result.out_brd_btm}</td>
			<td>登机结束</td>
			<td>${result.out_brd_etm}</td>
			<td>离位</td>
			<td>${result.out_rels_stand_tm}</td>
			<td>推出延误时长</td>
			<td>${result.depart_delay_tm}</td>
<%-- 			<td>${result.out_acdm_depart_delay_tm}</td> --%>
		</tr>
		<tr>
			<td>EOBT</td>
			<td>${result.out_acdm_eobt}</td>
			<td>COBT</td>
			<td>${result.out_acdm_cobt}</td>
			<td>TSAT</td>
			<td>${result.out_acdm_tsat}</td>
			<td>ASAT</td>
			<td>${result.out_acdm_asat}</td>
		</tr>
		<tr>
			<td>CTOT</td>
			<td>${result.out_acdm_ctot}</td>
			<td>T_TOBT</td>
			<td>${result.out_acdm_t_tobt}</td>
			<td>A_TOBT</td>
			<td>${result.out_acdm_a_tobt}</td>
			<td>C_TOBT</td>
			<td></td>
		</tr>
		<tr>
			<td>始发延误时长</td>
			<td>${result.depart_start_delay_tm}</td>
<%-- 			<td>${result.out_acdm_start_delay_tm}</td> --%>
			<td>进港延误时长</td>
			<td>${result.arrival_delay_tm}</td>
<%-- 			<td>${result.in_acdm_delay_tm}</td> --%>
			<td>出港延误时长</td>
			<td>${result.depart_delay_tm}</td>
<%-- 			<td>${result.out_acdm_delay_tm}</td> --%>
			<td>放行延误时长</td>
			<td>${result.depart_rls_delay_tm}</td>
<%-- 			<td>${result.out_acdm_rls_delay_tm}</td> --%>
		</tr>
		<tr>
			<td>首次TSAT较STD延误</td>
			<td>${result.out_acdm_ftsat_to_std}</td>
			<td>末次TAST较首次TAST延误</td>
			<td>${result.out_acdm_ltsat_to_ftsat}</td>
			<td>离位较末次TAST延误</td>
			<td>${result.out_acdm_rls_ltsat}</td>
			<td></td>
			<td></td>
		</tr>
	</table>
	<h3>保障环节时间</h3>
	<table class="layui-table">
<!-- 		如果rownum模8==0 说明要另起一行 -->
		<c:forEach items="${job}" var="item" varStatus="stat">
          <c:if test="${item.ROWNUM%2==1}"><tr></c:if>
          	<td  width=12.5%>${item.DISPLAY_NAME}开始</td> 
          	<td  width=12.5%>${item.START_TM}</td> 
          	<td  width=12.5%>${item.DISPLAY_NAME}结束</td> 
          	<td  width=12.5%>${item.END_TM}</td> 
          <c:if test="${item.ROWNUM%2==0}"></tr></c:if>
           <c:if test="${stat.last and item.ROWNUM%2!=0}">
           	<td  width=12.5%></td> 
          	<td  width=12.5%></td> 
          	<td  width=12.5%></td> 
          	<td  width=12.5%></td> </tr>
           </c:if>
         </c:forEach>
         
         
         
         
         
         
         
         
         
         
         
         
         
         
         
         
         
         
<!-- 		<tr> -->
<!-- 			<td width=12.5%>挡轮档时间</td> -->
<%-- 			<td width=12.5%>${bzh.in_on_chock_tm}</td> --%>
<!-- 			<td width=12.5%>廊桥靠接</td> -->
<%-- 			<td width=12.5%>${bzh.on_bridge_tm}</td> --%>
<!-- 			<td width=12.5%>配餐开始</td> -->
<!-- 			<td width=12.5%></td> -->
<!-- 			<td width=12.5%>航油加注开始</td> -->
<%-- 			<td width=12.5%>${bzh.out_acdm_bfuel_tm}</td> --%>
<!-- 		</tr> -->
<!-- 		<tr> -->
<!-- 			<td>撤轮档时间</td> -->
<%-- 			<td>${bzh.out_off_chock_tm}</td> --%>
<!-- 			<td>廊桥撤离</td> -->
<%-- 			<td>${bzh.off_bridge_tm}</td> --%>
<!-- 			<td>配餐结束</td> -->
<!-- 			<td></td> -->
<!-- 			<td>航油加注完成</td> -->
<%-- 			<td>${bzh.out_acdm_efuel_tm}</td> --%>
<!-- 		</tr> -->
<!-- 		<tr> -->
<!-- 			<td>值机开始</td> -->
<!-- 			<td></td> -->
<!-- 			<td>开客舱门</td> -->
<!-- 			<td></td> -->
<!-- 			<td>开货舱门</td> -->
<!-- 			<td></td> -->
<!-- 			<td>飞机拖拽开始</td> -->
<%-- 			<td>${bzh.jwqxctz_arrive_t}</td> --%>
<!-- 		</tr> -->
<!-- 		<tr> -->
<!-- 			<td>值机结束</td> -->
<%-- 			<td>${result.out_acdm_echeckin_tm}</td> --%>
<!-- 			<td>关客舱门</td> -->
<%-- 			<td>${result.out_cabinclose_tm}</td> --%>
<!-- 			<td>关货舱门</td> -->
<%-- 			<td>${result.out_goodclose_tm}</td> --%>
<!-- 			<td>飞机拖拽结束</td> -->
<%-- 			<td>${bzh.jwqxctz_complete_t}</td> --%>
<!-- 		</tr> -->
<!-- 		<tr> -->
<!-- 			<td>残升车靠接</td> -->
<%-- 			<td>${bzh.jwbdcgcsc_connection_t}</td> --%>
<!-- 			<td>气源车开始</td> -->
<%-- 			<td>${bzh.out_jwhxbzqyc_begin_t}</td> --%>
<!-- 			<td>空调车开始</td> -->
<%-- 			<td>${bzh.jwhxbzktc_begin_t}</td> --%>
<!-- 			<td>电源车开始</td> -->
<%-- 			<td>${bzh.jwhxbzdyc_begin_t}</td> --%>
<!-- 		</tr> -->
<!-- 		<tr> -->
<!-- 			<td>残升车撤离</td> -->
<%-- 			<td>${bzh.jwbdcgcsc_complete_t}</td> --%>
<!-- 			<td>气源车完成</td> -->
<%-- 			<td>${bzh.out_jwhxbzqyc_complete_t}</td> --%>
<!-- 			<td>空调车完成</td> -->
<%-- 			<td>${bzh.jwhxbzktc_complete_t}</td> --%>
<!-- 			<td>电源车完成</td> -->
<%-- 			<td>${bzh.jwhxbzdyc_complete_t}</td> --%>
<!-- 		</tr> -->
<!-- 		<tr> -->
<!-- 			<td>客舱清洁开始</td> -->
<%-- 			<td>${bzh.jwqccaozuo_begin_t}</td> --%>
<!-- 			<td>补配开始时间</td> -->
<%-- 			<td>${bzh.jwqccaozuobupei_begin_t}</td> --%>
<!-- 			<td>深度清舱开始</td> -->
<!-- 			<td></td> -->
<!-- 			<td>污水车开始</td> -->
<!-- 			<td></td> -->
<!-- 		</tr> -->
<!-- 		<tr> -->
<!-- 			<td>客舱清洁完成</td> -->
<%-- 			<td>${bzh.jwqccaozuo_complete_t}</td> --%>
<!-- 			<td>补配结束时间</td> -->
<%-- 			<td>${bzh.jwqccaozuobupei_complete_t}</td> --%>
<!-- 			<td>深度清舱结束</td> -->
<!-- 			<td></td> -->
<!-- 			<td>污水车完成</td> -->
<!-- 			<td></td> -->
<!-- 		</tr> -->
<!-- 		<tr> -->
<!-- 			<td>垃圾车开始</td> -->
<!-- 			<td></td> -->
<!-- 			<td>清水车开始</td> -->
<!-- 			<td></td> -->
<!-- 			<td>机务放行完成</td> -->
<%-- 			<td>${bzh.in_jwhxbzfx1_end_tm}</td> --%>
<!-- 			<td>客梯车靠接</td> -->
<%-- 			<td>${bzh.zpktckj_complete_t}</td> --%>
<!-- 		</tr> -->
<!-- 		<tr> -->
<!-- 			<td>垃圾车完成</td> -->
<!-- 			<td></td> -->
<!-- 			<td>清水车完成</td> -->
<!-- 			<td></td> -->
<!-- 			<td>货邮行卸机完成</td> -->
<%-- 			<td>${bzh.in_zphylx_complete_t}</td> --%>
<!-- 			<td>客梯车撤离</td> -->
<%-- 			<td>${bzh.zpktccl_complete_t}</td> --%>
<!-- 		</tr> -->
<!-- 		<tr> -->
<!-- 			<td>进港行李拉运开始</td> -->
<%-- 			<td>${bzh.in_zptjgxlly_start_t}</td> --%>
<!-- 			<td>进港货邮拉运开始</td> -->
<%-- 			<td>${bzh.in_hykjgly_start_t}</td> --%>
<!-- 			<td>出港行李拉运开始</td> -->
<%-- 			<td>${bzh.out_zptcgxlly_start_t}</td> --%>
<!-- 			<td>出港货邮拉运开始</td> -->
<%-- 			<td>${bzh.out_hykcgly_start_t}</td> --%>
<!-- 		</tr> -->
<!-- 		<tr> -->
<!-- 			<td>进港行李拉运结束</td> -->
<%-- 			<td>${bzh.in_zptjgxlly_complete_t}</td> --%>
<!-- 			<td>进港货邮拉运结束</td> -->
<%-- 			<td>${bzh.in_hykjgly_complete_t}</td> --%>
<!-- 			<td>出港行李拉运结束</td> -->
<%-- 			<td>${bzh.out_zptcgxlly_complete_t}</td> --%>
<!-- 			<td>出港货邮拉运结束</td> -->
<%-- 			<td>${bzh.out_hykcgly_complete_t}</td> --%>
<!-- 		</tr> -->
<!-- 		<tr> -->
<!-- 			<td>文件运输开始</td> -->
<!-- 			<td></td> -->
<!-- 			<td>文件运输结束</td> -->
<!-- 			<td></td> -->
<!-- 			<td>牵引车到位</td> -->
<!-- 			<td></td> -->
<!-- 			<td>航空器推出</td> -->
<!-- 			<td></td> -->
<!-- 		</tr> -->
<!-- 		<tr> -->
<!-- 			<td>进港摆渡车1到位</td> -->
<%-- 			<td>${bdch.in_jwbdin_arrive1_finish_time}</td> --%>
<!-- 			<td>进港摆渡车2到位</td> -->
<%-- 			<td>${bdch.in_jwbdin_arrive2_finish_time}</td> --%>
<!-- 			<td>进港摆渡车3到位</td> -->
<%-- 			<td>${bdch.in_jwbdin_arrive3_finish_time}</td> --%>
<!-- 			<td>进港摆渡车4到位</td> -->
<%-- 			<td>${bdch.in_jwbdin_arrive4_finish_time}</td> --%>
<!-- 		</tr> -->
<!-- 		<tr> -->
<!-- 			<td>进港摆渡车1完成</td> -->
<%-- 			<td>${bdch.in_jwbd_complete1_finish_time}</td> --%>
<!-- 			<td>进港摆渡车2完成</td> -->
<%-- 			<td>${bdch.in_jwbd_complete2_finish_time}</td> --%>
<!-- 			<td>进港摆渡车3完成</td> -->
<%-- 			<td>${bdch.in_jwbd_complete3_finish_time}</td> --%>
<!-- 			<td>进港摆渡车4完成</td> -->
<%-- 			<td>${bdch.in_jwbd_complete4_finish_time}</td> --%>
<!-- 		</tr> -->
<!-- 		<tr> -->
<!-- 			<td>进港摆渡车5到位</td> -->
<%-- 			<td>${bdch.in_jwbdin_arrive5_finish_time}</td> --%>
<!-- 			<td>进港摆渡车6到位</td> -->
<%-- 			<td>${bdch.in_jwbdin_arrive6_finish_time}</td> --%>
<!-- 			<td>进港摆渡车7到位</td> -->
<%-- 			<td>${bdch.in_jwbdin_arrive7_finish_time}</td> --%>
<!-- 			<td>进港摆渡车8到位</td> -->
<%-- 			<td>${bdch.in_jwbdin_arrive8_finish_time}</td> --%>
<!-- 		</tr> -->
<!-- 		<tr> -->
<!-- 			<td>进港摆渡车5完成</td> -->
<%-- 			<td>${bdch.in_jwbd_complete5_finish_time}</td> --%>
<!-- 			<td>进港摆渡车6完成</td> -->
<%-- 			<td>${bdch.in_jwbd_complete6_finish_time}</td> --%>
<!-- 			<td>进港摆渡车7完成</td> -->
<%-- 			<td>${bdch.in_jwbd_complete7_finish_time}</td> --%>
<!-- 			<td>进港摆渡车8完成</td> -->
<%-- 			<td>${bdch.in_jwbd_complete8_finish_time}</td> --%>
<!-- 		</tr> -->
<!-- 		<tr> -->
<!-- 			<td>出港摆渡车1到位</td> -->
<%-- 			<td>${bdch.out_jwbd_arrive1_finish_time}</td> --%>
<!-- 			<td>出港摆渡车2到位</td> -->
<%-- 			<td>${bdch.out_jwbd_arrive2_finish_time}</td> --%>
<!-- 			<td>出港摆渡车3到位</td> -->
<%-- 			<td>${bdch.out_jwbd_arrive3_finish_time}</td> --%>
<!-- 			<td>出港摆渡车4到位</td> -->
<%-- 			<td>${bdch.out_jwbd_arrive4_finish_time}</td> --%>
<!-- 		</tr> -->
<!-- 		<tr> -->
<!-- 			<td>出港摆渡车1完成</td> -->
<%-- 			<td>${bdch.out_jwbd_complete1_finish_tim}</td> --%>
<!-- 			<td>出港摆渡车2完成</td> -->
<%-- 			<td>${bdch.out_jwbd_complete2_finish_tim}</td> --%>
<!-- 			<td>出港摆渡车3完成</td> -->
<%-- 			<td>${bdch.out_jwbd_complete3_finish_tim}</td> --%>
<!-- 			<td>出港摆渡车4完成</td> -->
<%-- 			<td>${bdch.out_jwbd_complete4_finish_tim}</td> --%>
<!-- 		</tr> -->
<!-- 		<tr> -->
<!-- 			<td>出港摆渡车5到位</td> -->
<%-- 			<td>${bdch.out_jwbd_arrive5_finish_time}</td> --%>
<!-- 			<td>出港摆渡车6到位</td> -->
<%-- 			<td>${bdch.out_jwbd_arrive6_finish_time}</td> --%>
<!-- 			<td>出港摆渡车7到位</td> -->
<%-- 			<td>${bdch.out_jwbd_arrive7_finish_time}</td> --%>
<!-- 			<td>出港摆渡车8到位</td> -->
<%-- 			<td>${bdch.out_jwbd_arrive8_finish_time}</td> --%>
<!-- 		</tr> -->
<!-- 		<tr> -->
<!-- 			<td>出港摆渡车5完成</td> -->
<%-- 			<td>${bdch.out_jwbd_complete5_finish_tim}</td> --%>
<!-- 			<td>出港摆渡车6完成</td> -->
<%-- 			<td>${bdch.out_jwbd_complete6_finish_tim}</td> --%>
<!-- 			<td>出港摆渡车7完成</td> -->
<%-- 			<td>${bdch.out_jwbd_complete7_finish_tim}</td> --%>
<!-- 			<td>出港摆渡车8完成</td> -->
<%-- 			<td>${bdch.out_jwbd_complete8_finish_tim}</td> --%>
<!-- 		</tr> -->
	</table>
</body>
</html>