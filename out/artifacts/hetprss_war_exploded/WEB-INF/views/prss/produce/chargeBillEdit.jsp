<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>新增单据</title>
<meta name="decorator" content="default"/>
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/prss/produce/chargeBillEdit.js"></script>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
	<form:form id="billForm" action="${ctx}/produce/chargeBill/doUpdate?flag=${flag}"
		modelAttribute="vo" method="post" class="layui-form">
	<form:input type="hidden" path="fltId"></form:input>
	<form:input type="hidden" path="aircraftNumber"></form:input>
	<form:input type="hidden" path="billId"></form:input>
	<div class="layui-form-item" align="right">
		<div class="layui-inline"  style="margin-right: 13%">
		<label class="layui-form-label" style="width: 100px;">序 号：${billId}</label>
		</div>
		</div>
		<div class="layui-form-item" style="margin-left: 0px;">
			<div class="layui-inline" style="padding-left: 0px;">
				<label class="layui-form-label" style="width: 100px;">日 期：</label>
				<div class="layui-input-inline">
					 <form:input type="text"  readonly="true" onclick="WdatePicker({dateFmt:'yyyyMMdd',maxDate:'%y-%M-%d'});"
						placeholder="请选择航班日期" autocomplete="off" class="layui-input"
						path="flightDate" onClick="dateFilter()"   />
				</div>
					
				<label class="layui-form-label" style="width: 100px;">航班号：</label>
				<div class="layui-input-inline">
					 <form:input type="text"  readonly="true"
						placeholder="请输入航班号" autocomplete="off" class="layui-input"
						path="flightNumber"  contentEditable="false" />
				</div>
				<label class="layui-form-label" style="width: 100px;">进出港：</label>
				<div class="layui-input-inline" >
					<form:select path="inOutFlag">
						<form:option value="0">请选择</form:option>
						<form:option value="A">进港</form:option>
						<form:option value="D">出港</form:option>
					</form:select>
				</div>
				<label class="layui-form-label" style="width: 100px;" id ="showLab">机 位：</label>
				<div class="layui-input-inline" >
					 <form:input type="text"  readonly="true"
						placeholder="" autocomplete="off" class="layui-input"
						path="actstandCode" style="width: 186px;" ></form:input>
				</div>
			</div>	
			<div class="layui-inline" style="padding-left: 0px;"  >				
				<label class="layui-form-label" style="width: 100px;">机 型：</label>
				<div class="layui-input-inline">
					 <form:input type="text" readonly="true"
						placeholder="" autocomplete="off" class="layui-input"
						path="actTypeCode" contentEditable="false" ></form:input>
				</div>
				<label class="layui-form-label" style="width: 100px;" id="stLab">STA：</label>
				<div class="layui-input-inline">
					<form:input type="text" readonly="true"
						placeholder="" autocomplete="off" class="layui-input"
						path="sta" ></form:input>
				</div>
				<label class="layui-form-label" style="width: 100px;" id="etLab">ETA：</label>
				<div class="layui-input-inline">
					 <form:input type="text" readonly="true"
						placeholder="" autocomplete="off" class="layui-input"
						path="eta" ></form:input>
				</div>
				<label class="layui-form-label" style="width: 100px;" id="atLab">ATA：</label>
				<div class="layui-input-inline">
					 <form:input type="text" readonly="true"
						placeholder="" autocomplete="off" class="layui-input"
						path="ata"  style="width: 186px;"  ></form:input>
				</div>
			</div>	
			<div class="layui-inline" style="padding-left: 0px;"  >				
				<label class="layui-form-label" style="width: 100px;">岗位：</label>
				<div class="layui-input-inline">
					 <form:input type="text" readonly="true"
						placeholder="" autocomplete="off" class="layui-input"
						path="post"  contentEditable="false" ></form:input>
				</div>
				<label class="layui-form-label" style="width: 100px;" id="stLab">STD：</label>
				<div class="layui-input-inline">
					<form:input type="text" readonly="true"
						placeholder="" autocomplete="off" class="layui-input"
						path="std" ></form:input>
				</div>
				<label class="layui-form-label" style="width: 100px;" id="etLab">ETD：</label>
				<div class="layui-input-inline">
					 <form:input type="text" readonly="true"
						placeholder="" autocomplete="off" class="layui-input"
						path="etd" ></form:input>
				</div>
				<label class="layui-form-label" style="width: 100px;" id="atLab">ATD：</label>
				<div class="layui-input-inline">
					 <form:input type="text" readonly="true"
						placeholder="" autocomplete="off" class="layui-input"
						path="atd"  style="width: 186px;"  ></form:input>
				</div>
			</div>	
			<div id="baseTables">
						<table id="baseTable"></table>
			</div>		
		</div>
	
	<div id="detailGrid"  >
	<table  class="layui-table" style="width: 90%;">
		<tr>
		<td style="text-align: center;">项目</td>
		<td style="text-align: center;">型号及数量</td>
		<td style="text-align: center;">时间（小时）/次</td>
		<td style="text-align: center;">备注</td>
		</tr>
		<c:if test="${flag !='ZPKTC'}">
		<tr>
		<td style="text-align: center;">操作人员</td>
		<td><form:input type="text"  class="layui-input" path="czry1"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="czry2"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="czry3"></form:input></td>
		</tr>
		<tr>
		<td style="text-align: center;">传送带车</td>
		<td><form:input type="text"  class="layui-input" path="csdc1"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="csdc2"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="csdc3"></form:input></td>
		</tr>
		<tr>
		<td style="text-align: center;">装卸拖车</td>
		<td><form:input type="text"  class="layui-input" path="zxtc1"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="zxtc2"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="zxtc3"></form:input></td>
		</tr>
		<tr>
		<td style="text-align: center;">行李运输拖车</td>
		<td><form:input type="text"  class="layui-input" path="xlystc1"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="xlystc2"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="xlystc3"></form:input></td>
		</tr>
		<tr>
		<td style="text-align: center;">特种货物平台车(35t)</td>
		<td><form:input type="text"  class="layui-input" path="tzhwptc351"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="tzhwptc352"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="tzhwptc353"></form:input></td>
		</tr>
		<tr>
		<td style="text-align: center;">特种货物平台车(20t)</td>
		<td><form:input type="text"  class="layui-input" path="tzhwptc201"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="tzhwptc202"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="tzhwptc203"></form:input></td>
		</tr>
		<tr>
		<td style="text-align: center;">平台车(13.6t)</td>
		<td><form:input type="text"  class="layui-input" path="ptc1361"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="ptc1362"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="ptc1363"></form:input></td>
		</tr>
		<tr>
		<td style="text-align: center;">平台车(6.8t)</td>
		<td><form:input type="text"  class="layui-input" path="ptc681"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="ptc682"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="ptc683"></form:input></td>
		</tr>
		</c:if>
		<c:if test="${flag =='ZPKTC'}">
		<tr>
		<td style="text-align: center;">客梯车</td>
		<td><form:input type="text"  class="layui-input" path="ktc1"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="ktc2"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="ktc3"></form:input></td>
		</tr>
		</c:if>
		<c:if test="${flag !='ZPKTC'}">
		<tr>
		<td style="text-align: center;">组装集装板</td>
		<td><form:input type="text"  class="layui-input" path="zzjzb1"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="zzjzb2"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="zzjzb3"></form:input></td>
		</tr>
		<tr>
		<td style="text-align: center;">分解集装板</td>
		<td><form:input type="text"  class="layui-input" path="fjjzb1"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="fjjzb2"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="fjjzb3"></form:input></td>
		</tr>
		<tr>
		<td style="text-align: center;">组装集装箱</td>
		<td><form:input type="text"  class="layui-input" path="zzjzx1"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="zzjzx2"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="zzjzx3"></form:input></td>
		</tr>
		<tr>
		<td style="text-align: center;">分解集装箱</td>
		<td><form:input type="text"  class="layui-input" path="fjjzx1"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="fjjzx2"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="fjjzx3"></form:input></td>
		</tr>
		<tr>
		<td style="text-align: center;">重复装舱</td>
		<td><form:input type="text"  class="layui-input" path="cfzc1"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="cfzc2"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="cfzc3"></form:input></td>
		</tr>
		</c:if>
		<tr>
		<td style="text-align: center;">其它服务</td>
		<td><form:input type="text"  class="layui-input" path="qtfw1"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="qtfw2"></form:input></td>
		<td><form:input type="text"  class="layui-input" path="qtfw3"></form:input></td>
		</tr>
	
	</table>
	<div class="layui-form-item" style="margin-left: 0px;">
	<div class="layui-inline" style="padding-left: 5%;">
					<label class="layui-form-label" style="width: 100px;">填写人：</label>
				<div class="layui-input-inline">
					 <form:input type="text" readonly="true"
						placeholder="" autocomplete="off" class="layui-input"
						path="operator"  contentEditable="false" ></form:input>
				</div>	
				<label class="layui-form-label" style="width: 200px;">站坪部主管或领班:</label>
				<div class="layui-input-inline">
					 <form:input type="text"  readonly="true"
						placeholder="" autocomplete="off" class="layui-input"
						path = "scheduler"  contentEditable="false"  ></form:input>
				</div>
				<%-- <label class="layui-form-label" style="width: 100px;">岗 位：</label>
				<div class="layui-input-inline" >
					<form:input type="text" readonly="true"
						placeholder="" autocomplete="off" class="layui-input"
						path="post"  contentEditable="false" ></form:input>
				</div> --%>
			</div>
	</div>
		<div class="layui-form-item" style="margin-left: 0px;">
	<div class="layui-inline" style="padding-left: 5%;">
		<label class="layui-form-label" style="width: 150px;">航空公司代办：</label>
		<img src="${ctx}/produce/chargeBill/readExpAtta?fileId=${fileId}" hspace="2" vspace="2" border="1" align="middle" height="50" width="150"/>
<%--        <img hspace="2" vspace="2" border="1" align="middle" height="50" width="50" src="${imgSrc}"> --%>
      </div>
      </div>
	</div>	
	</form:form>
</body>
</html>