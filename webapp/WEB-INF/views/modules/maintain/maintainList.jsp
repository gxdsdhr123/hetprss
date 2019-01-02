<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>${tableVO.maintainName }</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp" %>
<link href="${ctxStatic}/modules/sys/maintain/css/maintain.css" type="text/css"
	rel="stylesheet">
<script type="text/javascript"
			src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/modules/sys/maintain/maintainList.js"></script>
</head>
<body>
	<div>
		<div class="box-body">
			<form:form id="searchForm" action="${ctx}/sys/maintain/" method="post" 
				class="layui-form">
				<input id="pageNum" name="pageNum" type="hidden" value="${pageNum}" />
				<input id="pages" name="pages" type="hidden" value="${pages}" />
				<input id="pageCount" name="pageCount" type="hidden" value="${pageCount}" />
				<input id="tabId" name="tabId" type="hidden" value="${tabId}" />
				<input id="totalRows" name="totalRows" type="hidden" value="${totalRows}" />
				<div class="layui-form-item">
					<c:forEach items="${condMap}" var="condList">
						<div class="layui-inline">
							<c:forEach items="${condList.value}" var="cond">
								<c:if test="${cond!=null }">
									<%--取默认值，并设置变量 --%>
									<label class="layui-form-label">${cond.condName }</label>
									<div class="layui-input-inline" <c:if test="${cond.condStyle!=null }">style="${cond.condStyle }"</c:if>>
									<c:choose>
										<%--输入框 --%>
										<c:when test="${cond.condType=='text' }">
											<input id="${cond.condCode }" name="${cond.condCode }" type="text" 	
												class="layui-input" value="${fns:getMapValue(paramMap,cond.condCode) }" /> 
										</c:when>
										<%--下拉选择框 --%>
										<c:when test="${cond.condType=='select' }">
											<select name="${cond.condCode }" id="${cond.condCode }" lay-verify="" lay-search>
											  <c:if test="${cond.dataList!=null }">
											  		<c:forEach items="${cond.dataList}" var="condData">
											  			<option 
											  				<c:if test="${fns:getMapValue(paramMap,cond.condCode)== condData.CODE}">selected</c:if> value="${condData.CODE }">${condData.NAME }</option>
											  		</c:forEach>
											  </c:if>
											</select>     
										</c:when>
										<%--日期 --%>
										<c:when test="${cond.condType=='date' }">
											<input type="text"  id="${cond.condCode }" class="layui-input" placeholder="yyyyMMdd"
												   value="${fns:getMapValue(paramMap,cond.condCode) }" name="${cond.condCode }"  
												   onFocus="WdatePicker({dateFmt:'yyyyMMdd'})" style="cursor: hand;" />
										</c:when>
										<%--月份 --%>
										<c:when test="${cond.condType=='month' }">
											<input type="text"  id="${cond.condCode }" class="layui-input" placeholder="yyyyMM"
												   value="${fns:getMapValue(paramMap,cond.condCode) }" name="${cond.condCode }"  
												   onFocus="WdatePicker({dateFmt:'yyyyMM'})" style="cursor: hand;" />
										</c:when>
										<%--年 --%>
										<c:when test="${cond.condType=='year' }">
											<input type="text"  id="${cond.condCode }" class="layui-input" placeholder="yyyy"
												   value="${fns:getMapValue(paramMap,cond.condCode) }" name="${cond.condCode }"  
												   onFocus="WdatePicker({dateFmt:'yyyy'})" style="cursor: hand;" />
										</c:when>
										<c:otherwise>
											<input id="${cond.condCode }" name="${cond.condCode }" 
												type="text" class="layui-input" value="${fns:getMapValue(paramMap,cond.condCode) }" /> 
										</c:otherwise>
									</c:choose>
									</div>
								</c:if>
							</c:forEach>
						</div>
					</c:forEach>
					<div class="layui-inline pull-right" id="toolbar">
						<c:if test="${tableVO.operateButton !=null && fn:length(tableVO.operateButton)==5}">
							<c:if test="${fn:substring(tableVO.operateButton,0,1)=='1' }">
								<button id="btnSubmit" class="layui-btn layui-btn-small layui-btn-primary" type="button" onclick="doQuery(1)">
								 <i class="fa fa-search">&nbsp;</i>查询
								</button>
							</c:if>
							<c:if test="${fn:substring(tableVO.operateButton,1,2)=='1' }">
								<button id="btnSubmit" class="layui-btn layui-btn-small layui-btn-primary" type="button" onclick="doAdd(1)">
								 <i class="fa fa-plus">&nbsp;</i>新增
								</button>
							</c:if>
							<c:if test="${fn:substring(tableVO.operateButton,2,3)=='1' }">
								<button id="btnSubmit" class="layui-btn layui-btn-small layui-btn-primary" type="button" onclick="doMultiDelete()">
								 <i class="fa fa-trash">&nbsp;</i>删除
								</button>
							</c:if>
							<c:if test="${fn:substring(tableVO.operateButton,3,4)=='1' }">
								<button id="btnSubmit" class="layui-btn layui-btn-small layui-btn-primary" type="button" onclick="">
								 <i class="fa fa-cloud-upload">&nbsp;</i>导入
								</button>
							</c:if>
							<c:if test="${fn:substring(tableVO.operateButton,4,5)=='1' }">
								<button id="btnSubmit" class="layui-btn layui-btn-small layui-btn-primary" type="button" onclick="">
								 <i class="fa fa-cloud-download">&nbsp;</i>导出
								</button>
							</c:if>
						</c:if>
					</div>
				</div>
			</form:form>
			<div id="tableDiv" style="position:relative;height:450px">
				<table id="contentTable" class="layui-table" lay-even>
					<thead>
						<tr>
							<c:if test="${fn:substring(tableVO.operateButton,2,3)=='1' }">
								<th style="text-align:center">
									<input type="checkbox" name="checkAll"  id="checkAll" title="全选" />
								</th> 
							</c:if>
							<c:forEach items="${tableVO.columnList}" var="col">
								<th class="layui-elip" <c:if test="${col.colStyle!=null }">style="${col.colStyle }"</c:if>>${col.colNameCn }</th>
							</c:forEach>
							<c:if test="${tableVO.gridButton !=null && fn:length(tableVO.gridButton)==3 && tableVO.gridButton!='000'}">
								<th class="layui-elip">操作</th>
							</c:if>
							<%-- <shiro:hasPermission name="sys:role:edit">
								<th>操作</th>
							</shiro:hasPermission> --%>
						</tr>
					</thead>
					<c:forEach items="${dataList}" var="data">
						<tr 
							<c:if test="${tableVO.gridButton !=null && fn:length(tableVO.gridButton)==3 && tableVO.gridButton!='000'}">
							ondblclick="doModify('${fns:getMapValue(fns:getDisplayValue(data[0]) ,"pkParam")}')"
							</c:if>
						>
							<c:if test="${fn:substring(tableVO.operateButton,2,3)=='1' }">
								<td style="text-align:center">
									<input type="checkbox" name="delcheckbox" value='${fns:getMapValue(fns:getDisplayValue(data[0]) ,"pkParam")}' class="delcheckbox" />
								</td> 
							</c:if>
							<c:forEach items="${data}" var="d" varStatus="status">
								<c:if test="${status.index==0 }">
									<td>${fns:getMapValue(fns:getDisplayValue(d) ,"value")}</td>
								</c:if>
								<c:if test="${status.index>0 }">
									<td nowrap>${d }</td>
								</c:if>
							</c:forEach>
							<c:if test="${tableVO.gridButton !=null && fn:length(tableVO.gridButton)==3 && tableVO.gridButton!='000'}">
								<td nowrap class="btn-cell">
									<div class="layui-btn-group" id="btn-group">
										<c:if test="${fn:substring(tableVO.operateButton,0,1)=='1' }">
											<button class="layui-btn layui-btn-small layui-btn-primary" title="查看" onclick="doModify('${fns:getMapValue(fns:getDisplayValue(data[0]) ,"pkParam")}',true)">
											    <i class="layui-icon">&#xe615;</i>
											</button>
										</c:if>
										<c:if test="${fn:substring(tableVO.operateButton,1,2)=='1' }">
											<button class="layui-btn layui-btn-small layui-btn-primary" onclick="doModify('${fns:getMapValue(fns:getDisplayValue(data[0]) ,"pkParam")}')" title="修改">
											    <i class="layui-icon">&#xe642;</i>
											</button>
										</c:if>
										<c:if test="${fn:substring(tableVO.operateButton,2,3)=='1' }">
											 <button class="layui-btn layui-btn-small layui-btn-primary" onclick="doDelete('${fns:getMapValue(fns:getDisplayValue(data[0]) ,"pkParam")}')" title="删除">
											    <i class="layui-icon">&#xe640;</i>
											 </button>
										</c:if>
									</div>
								</td>
							</c:if>
						</tr>
					</c:forEach>
				</table>
				<div id="pagination" class="pull-left"></div>
			</div>
		</div>
	</div>
</body>
</html>