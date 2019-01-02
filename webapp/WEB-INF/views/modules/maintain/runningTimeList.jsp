<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>行驶时间</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<link href="${ctxStatic}/modules/sys/maintain/css/runningTime.css"
	type="text/css" rel="stylesheet">
<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/modules/sys/maintain/runningTime.js"></script>
</head>
<body>
	<div>
		<div class="box-body">
			<form:form id="searchForm" action="${ctx}/sys/RuningTime/list"
				method="post" class="form-horizontal">
				<div class="layui-form-item">
					<div class="layui-inline">
						<input id="pageNum" name="pageNum" type="hidden" value="${pageNum}" />
						<input id="pages" name="pages" type="hidden" value="${pages}" />
						<input id="totalRows" name="totalRows" type="hidden" value="${totalRows}" />
						<input id="pageCount" name="pageCount" type="hidden" value="${pageCount}" />
						<label class="layui-form-label">作业类型</label>
						<div class="layui-input-inline">
							<select id="jobKind" name="jobKind" class="form-control">
								<option value=''>请选择</option>
								<c:forEach items="${jobKinddata}" var="jobKinddata">
									<option value="${jobKinddata.RESTYPE}"
										<c:if test="${jobKinddata.RESTYPE==jobKind}">selected</c:if>>${jobKinddata.TYPENAME}</option>
								</c:forEach>
							</select>
						</div>
						<label class="layui-form-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;机坪</label>
						<div class="layui-input-inline">
							<select id="aprons" name="aprons" class="form-control">
								<option value=''>请选择</option>
								<c:forEach items="${apronsdata}" var="apronsdata">
									<option value="${apronsdata.code}"
										<c:if test="${apronsdata.code==aprons}">selected</c:if>>${apronsdata.code}</option>
								</c:forEach>
							</select>
						</div>
						<label class="layui-form-label">&nbsp;登机口</label>
						<div class="layui-input-inline">
							<select id="gate" name="gate" class="form-control">
								<option value=''>请选择</option>
								<c:forEach items="${gatedata}" var="gatedata">
									<option value="${gatedata.GATE_CODE}"
										<c:if test="${gatedata.GATE_CODE==gate}">selected</c:if>>${gatedata.GATE_CODE}</option>
								</c:forEach>
							</select>
						</div>
						<br></br>
						<label class="layui-form-label">起点类型</label>
						<div class="layui-input-inline">
							<select id="stype" name="stype" class="form-control">
								<option value='' <c:if test="${''==stype}">selected</c:if>>请选择</option>
								<option value="1" <c:if test="${1==stype}">selected</c:if>>登机口</option>
								<option value="2" <c:if test="${2==stype}">selected</c:if>>机坪</option>
							</select>
						</div>
						<label class="layui-form-label">终点类型</label>
						<div class="layui-input-inline">
							<select id="etype" name="etype" class="form-control">
								<option value='' <c:if test="${''==etype}">selected</c:if>>请选择</option>
								<option value="1" <c:if test="${1==etype}">selected</c:if>>登机口</option>
								<option value="2" <c:if test="${2==etype}">selected</c:if>>机坪</option>
							</select>
						</div>
					</div>
					<div class="layui-inline pull-right" id="toolbar">
						<button id="btnSubmit"
							class="layui-btn layui-btn-small layui-btn-primary" type="button"
							onclick="doSubmit()">
							<i class="fa fa-search">&nbsp;</i>查询
						</button>
						<button id="btnAdd"
							class="layui-btn layui-btn-small layui-btn-primary" type="button">
							<i class="fa fa-plus">&nbsp;</i>新增
						</button>
						<button id="btnSubmit"
							class="layui-btn layui-btn-small layui-btn-primary" type="button"
							onclick="doMultiDelete()">
							<i class="fa fa-trash">&nbsp;</i>删除
						</button>
					</div>
				</div>
			</form:form>
		<div id="tableDiv" style="position: relative; height: 450px">
			<table id="contentTable" class="layui-table" lay-even>
				<thead>
					<tr>
						<th style="text-align: center"><input type="checkbox" name="checkAll" id="checkAll" title="全选" /></th>
						<th class="layui-elip" style="display: none">id</th>
						<th class="layui-elip" style="display: none">作业代码</th>
						<th class="layui-elip">作业类型</th>
						<th class="layui-elip">起点</th>
						<th class="layui-elip" style="display: none">起点代码</th>
						<th class="layui-elip">起点类型</th>
						<th class="layui-elip">终点</th>
						<th class="layui-elip" style="display: none">终点代码</th>
						<th class="layui-elip">终点类型</th>
						<th class="layui-elip">正向时间</th>
						<th class="layui-elip">反向时间</th>
						<th class="layui-elip">创建时间</th>
						<th class="layui-elip">修改时间</th>
						<th class="layui-elip">操作人</th>
						<th class="layui-elip">操作</th>
					</tr>
				</thead>
				<c:forEach items="${runningTime}" var="runningTime">
					<tr ondblclick="doQuery(${runningTime.ID})">
						<td style="text-align: center"><input type="checkbox"
							name="delcheckbox" value="${runningTime.ID}" class="delcheckbox" />
						</td>
						<td id="${runningTime.ID}_ID" style="display: none">${runningTime.ID}</td>
						<td id="${runningTime.ID}_JOB_TYPE_CODE" style="display: none">${runningTime.JOB_TYPE_CODE}</td>
						<td id="${runningTime.ID}_TYPENAME">${runningTime.TYPENAME}</td>
						<td id="${runningTime.ID}_SPOS">${runningTime.SPOS}</td>
						<td id="${runningTime.ID}_STYPE" style="display: none">${runningTime.STYPE}</td>
						<td id="${runningTime.ID}_STYPE_NAME">${runningTime.STYPE_NAME}</td>
						<td id="${runningTime.ID}_EPOS">${runningTime.EPOS}</td>
						<td id="${runningTime.ID}_ETYPE" style="display: none">${runningTime.ETYPE}</td>
						<td id="${runningTime.ID}_ETYPE_NAME">${runningTime.ETYPE_NAME}</td>
						<td id="${runningTime.ID}_FTIME">${runningTime.FTIME}</td>
						<td id="${runningTime.ID}_RTIME">${runningTime.RTIME}</td>
						<td id="${runningTime.ID}_CREATE_TIME">${runningTime.CREATE_TIME}</td>
						<td id="${runningTime.ID}_UPDATE_TIME">${runningTime.UPDATE_TIME}</td>
						<td id="${runningTime.ID}_OPERATOR">${runningTime.OPERATOR}</td>
						<td nowrap class="btn-cell">
							<div class="layui-btn-group" id="btn-group">
								<button class="layui-btn layui-btn-small layui-btn-primary" onclick="doQuery(${runningTime.ID})" title="查看"> 
									<i class="layui-icon">&#xe615;</i>
								</button>
								<button class="layui-btn layui-btn-small layui-btn-primary" onclick="doModify(${runningTime.ID})" title="修改">
									<i class="layui-icon">&#xe642;</i>
								</button>
								<button class="layui-btn layui-btn-small layui-btn-primary" onclick="doOneDelete(${runningTime.ID})" title="删除">
									<i class="layui-icon">&#xe640;</i>
								</button>
							</div>
						</td>
					</tr>
				</c:forEach>
			</table>

			<div id="pagination" class="pull-right"></div>
			<div class="pull-left">
				每页显示行数： <select id="pageSizeSelect" onchange="doPageSizeSelect()">
					<option value="10" <c:if test="${10==pageCount}">selected</c:if>>10</option>
					<option value="20" <c:if test="${20==pageCount}">selected</c:if>>20</option>
					<option value="50" <c:if test="${50==pageCount}">selected</c:if>>50</option>
					<option value="100" <c:if test="${100==pageCount}">selected</c:if>>100</option>
				</select> <span>共${totalRows}条数据</span> <span>共${pages}页</span>
			</div>
		</div>
	</div>
</div>

<div id="editordiv" class="box-body" style="display: none">
	<div class="layui-form-item">
		<input id="spos" name="inputEditor" type="hidden" class="layui-input" />
		<input id="epos" name="inputEditor" type="hidden" class="layui-input" />
		<div class="layui-inline">
			<label class="layui-form-label">作业类型 </label> 
			<div class="layui-input-inline">
				<select id="editorjobKind" name="selectEditor" class="form-control">
					<option value=''>请选择</option>
					<c:forEach items="${jobKinddata}" var="jobKinddata">
						<option value="${jobKinddata.RESTYPE}">${jobKinddata.TYPENAME}</option>
					</c:forEach>
				</select> 
			</div>
			<br></br>
			<label class="layui-form-label">起点类型</label> 
			<div class="layui-input-inline">
				<select id="editorstype" name="selectEditor" class="form-control">
					<option value=''>请选择</option>
					<option value="1">登机口</option>
					<option value="2">机坪</option>
				</select> 
			</div>
			<label></label> 
			<div class="layui-input-inline">
				<select id="editorstypeaprons" name="selectEditor" style="display: none" class="form-control">
					<option value=''>==机坪==</option>
					<c:forEach items="${apronsdata}" var="apronsdata">
						<option value="${apronsdata.code}">${apronsdata.code}</option>
					</c:forEach>
				</select>
			</div>
			<label></label> 
			<div class="layui-input-inline"> 
				<select id="editorstypegate" name="selectEditor" style="display: none" class="form-control">
					<option value=''>==登机口==</option>
					<c:forEach items="${gatedata}" var="gatedata">
						<option value="${gatedata.GATE_CODE}">${gatedata.GATE_CODE}</option>
					</c:forEach>
				</select> 
			</div>
			<br></br>
			<label class="layui-form-label">终点类型 </label> 
			<div class="layui-input-inline">
				<select id="editoretype" name="selectEditor" class="form-control">
					<option value=''>请选择</option>
					<option value="1">登机口</option>
					<option value="2">机坪</option>
				</select>
			</div>
			<label></label> 
			<div class="layui-input-inline">
				<select id="editoretypeaprons" name="selectEditor" style="display: none" class="form-control">
					<option value=''>==机坪==</option>
					<c:forEach items="${apronsdata}" var="apronsdata">
						<option value="${apronsdata.code}">${apronsdata.code}</option>
					</c:forEach>
				</select> 
			</div>
			<label></label> 
			<div class="layui-input-inline">
				<select id="editoretypegate" name="selectEditor" style="display: none" class="form-control">
					<option value=''>==登机口==</option>
					<c:forEach items="${gatedata}" var="gatedata">
						<option value="${gatedata.GATE_CODE}">${gatedata.GATE_CODE}</option>
					</c:forEach>
				</select> 
			</div>
			<br></br>
			<label class="layui-form-label">正向时间 </label> 
			<div class="layui-input-inline">
			<input id="stime" name="inputEditor" type="text" class="layui-input" /> 
			</div>
			<br></br>
			<label class="layui-form-label">反向时间 </label> 
			<div class="layui-input-inline">
			<input id="etime" name="inputEditor" type="text" class="layui-input" /> 
			</div>
			<span name="queryShow" style="display: none"> 
			<br></br>
				<label class="layui-form-label">创建时间</label> 
				<div class="layui-input-inline">
				<input id="createTime" name="inputEditor" type="text" class="layui-input" /> 
				</div>
				<br></br>
				<label class="layui-form-label">修改时间 </label> 
				<div class="layui-input-inline">
				<input id="updateTime" name="inputEditor" type="text" class="layui-input" /> 
				</div>
				<br></br>
				<label class="layui-form-label">&nbsp;&nbsp;&nbsp;&nbsp;操作人 </label> 
				<div class="layui-input-inline">
				<input id="operator" name="inputEditor" type="text" class="layui-input" />
				</div>
			</span>
		</div>
	</div>
</div>
</body>
</html>