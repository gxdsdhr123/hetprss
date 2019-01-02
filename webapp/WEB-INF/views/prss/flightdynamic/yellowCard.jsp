<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>黄卡</title>
</head>
<body>
	<form class="layui-form" action="">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">FLT.NO.</label>
				<div class="layui-input-inline">
					<label class="layui-form-label">${result.in_fltno }/${result.out_fltno }</label>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">DATE</label>
				<div class="layui-input-inline">
					<label class="layui-form-label">${result.in_flight_date }/${result.in_flight_date }</label>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">REGN</label>
				<div class="layui-input-inline">
					<label class="layui-form-label">${result.out_aircraft_number }</label>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">AIR LINE</label>
				<div class="layui-input-inline">
					<label class="layui-form-label">${result.in_route_name }${result.out_route_name }</label>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">BAY</label>
				<div class="layui-input-inline">
					<label class="layui-form-label">${result.in_actstand_code }</label>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">OSCAR</label>
				<div class="layui-input-inline">
					<label class="layui-form-label">${result.a }</label>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">ETA</label>
				<div class="layui-input-inline">
					<label class="layui-form-label">${result.in_eta }</label>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">ETD</label>
				<div class="layui-input-inline">
					<label class="layui-form-label">${result.out_etd }</label>
				</div>
			</div>
		</div>
		<table class="layui-table">
  		<tbody>
    		<tr align="center">
      			<td>STA</td>
      			<td>AA</td>
      			<td>ATA</td>
      			<td>STD</td>
      			<td>ATD</td>
      			<td>AD</td>
      		</tr>
      		<tr align="center">
      			<td>${result.in_sta }</td>
      			<td>${result.in_ata }</td>
      			<td>${result.acdm_stand_tm }</td>
      			<td>${result.out_std }</td>
      			<td>${result.a }</td>
      			<td>${result.out_atd }</td>
      		</tr>
      		<tr align="center">
      			<td>UM/YP</td>
      			<td>WCHR</td>
      			<td>VIP</td>
      			<td>OTH</td>
      			<td>LMC ON</td>
      			<td>LMC OFF</td>
      		</tr>
      		<tr align="center">
      			<td><input type="text" autocomplete="off" class="layui-input"
						name="ACT_TYPE" id="ACT_TYPE" value="${result.a }"></td>
      			<td><input type="text" autocomplete="off" class="layui-input"
						name="ACT_TYPE" id="ACT_TYPE" value="${result.a }"></td>
      			<td><input type="text" autocomplete="off" class="layui-input"
						name="ACT_TYPE" id="ACT_TYPE" value="${result.a }"></td>
      			<td><input type="text" autocomplete="off" class="layui-input"
						name="ACT_TYPE" id="ACT_TYPE" value="${result.a }"></td>
      			<td><input type="text" autocomplete="off" class="layui-input"
						name="ACT_TYPE" id="ACT_TYPE" value="${result.a }"></td>
      			<td><input type="text" autocomplete="off" class="layui-input"
						name="ACT_TYPE" id="ACT_TYPE" value="${result.a }"></td>
      		</tr>
      	</tbody>
      	</table>
      	<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">CTR CLOSED</label>
				<div class="layui-input-inline">
					<input type="text" autocomplete="off" class="layui-input"
						name="FLIGHT_NUMBER" id="FLIGHT_NUMBER" value="${result.acdm_echeckin_tm }">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">BOARD/LAST PAX</label>
				<div class="layui-input-inline">
					<input type="text" autocomplete="off" class="layui-input"
						name="ACT_TYPE" id="ACT_TYPE" value="${result.a }">
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">FINAL LOAD</label>
				<div class="layui-input-inline">
					<input type="text" autocomplete="off" class="layui-input"
						name="FLIGHT_NUMBER" id="FLIGHT_NUMBER" value="${result.ACDM_ckcl_num }">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">DISEM LOAD</label>
				<div class="layui-input-inline">
					<input type="text" autocomplete="off" class="layui-input"
						name="ACT_TYPE" id="ACT_TYPE" value="${result.a }">
				</div>
			</div>
		</div>
	</form>
</body>
</html>