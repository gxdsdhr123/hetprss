<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>旅客信息</title>
<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/passengerInfo.js"></script>
</head>
<body>
	<shiro:hasPermission name="flightdynamic:passengerInfo:edit">
		<button id="editBtn" type="button" style="display: none;" class="btn btn-link">编辑</button>
	</shiro:hasPermission>
	<shiro:hasPermission name="scheduling:passengerInfo:edit">
		<button id="scheEditBtn" type="button" style="display: none;" class="btn btn-link">编辑</button>
	</shiro:hasPermission>
	<form id="passengerForm" action="" class="layui-form">
		<input type="hidden" name="inFltId" id="inFltId" value="${inFltId }">
		<input type="hidden" name="outFltId" id="outFltId" value="${outFltId }">
		<input type="hidden" name="dOri" id="dOri" value="${dOri }">
		<input type="hidden" name="attrCode" id="attrCode" value="${attrCode }">
		<table class="layui-table">
			<tbody>
				<tr align="center">
					<td>已过检人数</td>
					<td>${passenger.out_acdm_psc_num }</td>
					<td>已值机人数</td>
					<td><input type="text" style="text-align:center" autocomplete="off" class="layui-input" name="pckNum" id="pckNum" value="${passenger.out_acdm_pck_num }" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"></td>
				</tr>
				<tr align="center">
					<td>已登机人数</td>
					<td>${passenger.out_acdm_pbd_num }</td>
					<td>网上值机人数</td>
					<td>${passenger.out_acdm_pnck_num }</td>
				</tr>
				<tr align="center">
					<td>值机关闭人数</td>
					<td>${passenger.out_acdm_ckcl_num }</td>
					<td>自助值机人数</td>
					<td>${passenger.out_acdm_pack_num }</td>
				</tr>
				<tr align="center">
					<td>登机结束人数</td>
					<td>${passenger.out_acdm_bdcl_num }</td>
					<td>柜台值机人数</td>
					<td>${passenger.out_acdm_pcck_num }</td>
				</tr>
				<tr align="center">
					<td>进港国内人数</td>
					<td><input type="text" style="text-align:center" autocomplete="off" class="layui-input" <c:if test="${attrCode != 'M'}">style="display:none"</c:if> name="dPaxNumI" id="dPaxNumI" value="${nopI.D_PAX_NUM }" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"></td>
					<td>出港国内人数</td>
					<td><input type="text" style="text-align:center" autocomplete="off" class="layui-input" <c:if test="${attrCode != 'M'}">style="display:none"</c:if> name="dPaxNumO" id="dPaxNumO" value="${nopO.D_PAX_NUM }" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"></td>
				</tr>
				<tr align="center">
					<td>进港国际人数</td>
					<td><input type="text" style="text-align:center" autocomplete="off" class="layui-input" <c:if test="${attrCode != 'M'}">style="display:none"</c:if> name="iPaxNumI" id="iPaxNumI" value="${nopI.I_PAX_NUM}" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"></td>
					<td>出港国际人数</td>
					<td><input type="text" style="text-align:center" autocomplete="off" class="layui-input" <c:if test="${attrCode != 'M'}">style="display:none"</c:if> name="iPaxNumO" id="iPaxNumO" value="${nopO.I_PAX_NUM}" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"></td>
				</tr>
				<tr align="center">
					<td>进港总人数</td>
					<td><input type="text" style="text-align:center" autocomplete="off" class="layui-input" name="paxNumI" id="paxNumI" value="${nopI.PAX_NUM}" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"></td>
					<td>出港总人数</td>
					<td><input type="text" style="text-align:center" autocomplete="off" class="layui-input" name="paxNumO" id="paxNumO" value="${nopO.PAX_NUM}" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"></td>
				</tr>
			</tbody>
		</table>
	</form>
</body>
</html>