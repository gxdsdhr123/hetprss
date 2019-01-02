<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>班制类型</title>
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript"
	src="${ctxStatic}/prss/arrange/ambulatoryShiftsForm.js"></script>
</head>
<body>
	<form id="aftForm" action="${ctx}/arrange/ambulatoryShifts/save"
		class="layui-form">
		<input type="hidden" name="shiftsId" id="shiftsId"
			value="${AST[0].shiftsId}">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">名称：</label>
				<div class="layui-input-inline">
					 <input type="text"
						placeholder="非固定班制名称" autocomplete="off" class="layui-input"
						name="shiftsName" id="shiftsName" value="${AST[0].shiftsName}">
				</div>
			</div>
		</div>

		<table class="layui-table">
			<colgroup>
				<col width="60px">
				<col width="100px">
				<col width="250px">
				<col width="250px">
				<col>
				<col width="100px">
			</colgroup>
			<thead>
				<tr>
					<th>序号</th>
					<th>星期</th>
					<th>开始时间</th>
					<th>结束时间</th>
					<th>绑定航班</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td align="center">1</td>
					<td align="center">星期一</td>
					<td><input type="text" placeholder="格式：hhmm 例如：0800"
						autocomplete="off" class="layui-input" name="startime1"
						id="startime1" value="${AST[0].startime}"></td>
					<td><input type="text" placeholder="格式：hhmm 例如：0800"
						autocomplete="off" class="layui-input" name="endtime1"
						id="endtime1" value="${AST[0].endtime}"></td>
					<td><input type="text" placeholder="点击按钮选择航班"
						autocomplete="off" class="layui-input" name="bindFlt1"
						id="bindFlt1" value="${AST[0].bindFlt}" readonly="readonly" /></td>
					<td align="center"><button type="button" name="fltBtn1"
							class="layui-btn layui-btn-small layui-btn-primary"
							onclick="setFln(this)">选择航班</button></td>
				</tr>
				<tr>
					<td align="center">2</td>
					<td align="center">星期二</td>
					<td><input type="text" placeholder="格式：hhmm 例如：0800"
						autocomplete="off" class="layui-input" name="startime2"
						id="startime2" value="${AST[1].startime}"></td>
					<td><input type="text" placeholder="格式：hhmm 例如：0800"
						autocomplete="off" class="layui-input" name="endtime2"
						id="endtime2" value="${AST[1].endtime}"></td>
					<td><input type="text" placeholder="点击按钮选择航班"
						autocomplete="off" class="layui-input" name="bindFlt2"
						id="bindFlt2" value="${AST[1].bindFlt}" readonly="readonly" /></td>
					<td align="center"><button type="button" name="fltBtn2"
							class="layui-btn layui-btn-small layui-btn-primary"
							onclick="setFln(this)">选择航班</button></td>
				</tr>
				<tr>
					<td align="center">3</td>
					<td align="center">星期三</td>
					<td><input type="text" placeholder="格式：hhmm 例如：0800"
						autocomplete="off" class="layui-input" name="startime3"
						id="startime3" value="${AST[2].startime}"></td>
					<td><input type="text" placeholder="格式：hhmm 例如：0800"
						autocomplete="off" class="layui-input" name="endtime3"
						id="endtime3" value="${AST[2].endtime}"></td>
					<td><input type="text" placeholder="点击按钮选择航班"
						autocomplete="off" class="layui-input" name="bindFlt3"
						id="bindFlt3" value="${AST[2].bindFlt}" readonly="readonly" /></td>
					<td align="center"><button type="button" name="fltBtn3"
							class="layui-btn layui-btn-small layui-btn-primary"
							onclick="setFln(this)">选择航班</button></td>
				</tr>
				<tr>
					<td align="center">4</td>
					<td align="center">星期四</td>
					<td><input type="text" placeholder="格式：hhmm 例如：0800"
						autocomplete="off" class="layui-input" name="startime4"
						id="startime4" value="${AST[3].startime}"></td>
					<td><input type="text" placeholder="格式：hhmm 例如：0800"
						autocomplete="off" class="layui-input" name="endtime4"
						id="endtime4" value="${AST[3].endtime}"></td>
					<td><input type="text" placeholder="点击按钮选择航班"
						autocomplete="off" class="layui-input" name="bindFlt4"
						id="bindFlt4" value="${AST[3].bindFlt}" readonly="readonly" /></td>
					<td align="center"><button type="button" name="fltBtn4"
							class="layui-btn layui-btn-small layui-btn-primary"
							onclick="setFln(this)">选择航班</button></td>
				</tr>
				<tr>
					<td align="center">5</td>
					<td align="center">星期五</td>
					<td><input type="text" placeholder="格式：hhmm 例如：0800"
						autocomplete="off" class="layui-input" name="startime5"
						id="startime5" value="${AST[4].startime}"></td>
					<td><input type="text" placeholder="格式：hhmm 例如：0800"
						autocomplete="off" class="layui-input" name="endtime5"
						id="endtime5" value="${AST[4].endtime}"></td>
					<td><input type="text" placeholder="点击按钮选择航班"
						autocomplete="off" class="layui-input" name="bindFlt5"
						id="bindFlt5" value="${AST[4].bindFlt}" readonly="readonly" /></td>
					<td align="center"><button type="button" name="fltBtn5"
							class="layui-btn layui-btn-small layui-btn-primary"
							onclick="setFln(this)">选择航班</button></td>
				</tr>
				<tr>
					<td align="center">6</td>
					<td align="center">星期六</td>
					<td><input type="text" placeholder="格式：hhmm 例如：0800"
						autocomplete="off" class="layui-input" name="startime6"
						id="startime6" value="${AST[5].startime}"></td>
					<td><input type="text" placeholder="格式：hhmm 例如：0800"
						autocomplete="off" class="layui-input" name="endtime6"
						id="endtime6" value="${AST[5].endtime}"></td>
					<td><input type="text" placeholder="点击按钮选择航班"
						autocomplete="off" class="layui-input" name="bindFlt6"
						id="bindFlt6" value="${AST[5].bindFlt}" readonly="readonly" /></td>
					<td align="center"><button type="button" name="fltBtn6"
							class="layui-btn layui-btn-small layui-btn-primary"
							onclick="setFln(this)">选择航班</button></td>
				</tr>
				<tr>
					<td align="center">7</td>
					<td align="center">星期日</td>
					<td><input type="text" placeholder="格式：hhmm 例如：0800"
						autocomplete="off" class="layui-input" name="startime7"
						id="startime7" value="${AST[6].startime}"></td>
					<td><input type="text" placeholder="格式：hhmm 例如：0800"
						autocomplete="off" class="layui-input" name="endtime7"
						id="endtime7" value="${AST[6].endtime}"></td>
					<td><input type="text" placeholder="点击按钮选择航班"
						autocomplete="off" class="layui-input" name="bindFlt7"
						id="bindFlt7" value="${AST[6].bindFlt}" readonly="readonly" /></td>
					<td align="center"><button type="button" name="fltBtn7"
							class="layui-btn layui-btn-small layui-btn-primary"
							onclick="setFln(this)">选择航班</button></td>
				</tr>
			</tbody>
		</table>
	</form>
</body>
</html>