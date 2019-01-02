<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>${tableVO.maintainName }</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp" %>
<link href="${ctxStatic}/modules/sys/maintain/css/maintain.css"
	type="text/css" rel="stylesheet">
<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/modules/sys/maintain/maintainAdd.js"></script>
<script type="text/javascript"
    src="${ctxStatic}/modules/sys/maintain/maintainCommon.js"></script>
<style type="text/css">
	.layui-form-label{
		width:150px;
	}
</style>
</head>
<body>
	<div id="formDiv" style="position: relative;">
		<form:form id="addForm" action="${ctx}/sys/maintain/add"
			method="post" class="layui-form">
			<input id="tabId" name="tabId" type="hidden" value="${tabId}" />
			<c:forEach items="${tableVO.columnList }" var="column" varStatus="status">
				<c:if test="${column.ifInsert==1&& empty column.defaultValue}">
				 <c:if test="${status.index % 2 == 0 }">
					<div class="layui-form-item">
				 </c:if>
					<div class="${column.colShowType=='textarea'?'':'layui-inline'}">
						<label class="layui-form-label">
							<c:if test="${fn:indexOf(column.colDataType,'required')>=0 }"><font color="red">* </font></c:if>
							${column.colNameCn }：
						</label>
						<div class="${column.colShowType=='textarea'?'layui-input-block':'layui-input-inline'}">
							<c:choose>
								<c:when test="${column.ifPk=='1'}">
									<div class="layui-form-mid layui-word-aux">系统自动生成</div>
								</c:when>
								<%--多个输入框 --%>
								<c:when test="${column.colShowType=='multiple'}">
							 		<i class="fa fa-minus-circle" onclick="minus('${column.colNameEn}',this);">&nbsp;</i>
							 		<i class="fa fa-plus-circle" onclick="plus('${column.colNameEn}',this);">&nbsp;</i>
									<input type="text" name="${column.colNameEn }"  id="${column.colNameEn }"
										lay-verify="${column.colDataType }" placeholder="请输入" reg-validate="${column.colValueRegValidate }"
										autocomplete="off" class="layui-input" max_length="${column.colValueMaxLength }">
								</c:when>
								<%--输入框 --%>
								<c:when test="${column.colShowType=='text'}">
									<input type="text" name="${column.colNameEn }"  id="${column.colNameEn }" style="${column.colStyle}"
										lay-verify="${column.colDataType }" placeholder="请输入"  reg-validate="${column.colValueRegValidate }"
										autocomplete="off" class="layui-input" 
										max_length="${column.colValueMaxLength }" 
										alt="${column.colNameCn }" 
										value="${column.insertDefautValue}">
								</c:when>
								<%--密码 --%>
								<c:when test="${column.colShowType=='password'}">
									<input type="password" name="${column.colNameEn }"  id="${column.colNameEn }"
										lay-verify="${column.colDataType }" placeholder="请输入"
										autocomplete="off" class="layui-input" max_length="${column.colValueMaxLength }" alt="${column.colNameCn }">
								</c:when>
								<%--日期 --%>
								<c:when test="${column.colShowType=='date' }">
									<input type="text"  id="${column.colNameEn }" class="layui-input" placeholder="yyyyMMdd"
										   name="${column.colNameEn}"  
										   onFocus="WdatePicker({dateFmt:'yyyyMMdd'})" style="cursor: hand;" />
								</c:when>
								<%--月份 --%>
								<c:when test="${column.colShowType=='month' }">
									<input type="text"  id="${column.colNameEn }" class="layui-input" placeholder="yyyyMM"
										   name="${column.colNameEn }"  
										   onFocus="WdatePicker({dateFmt:'yyyyMM'})" style="cursor: hand;" />
								</c:when>
								<%--年 --%>
								<c:when test="${column.colShowType=='year' }">
									<input type="text"  id="${column.colNameEn }" class="layui-input" placeholder="yyyy"
										   name="${column.colNameEn }"  
										   onFocus="WdatePicker({dateFmt:'yyyy'})" style="cursor: hand;" />
								</c:when>
								<%--文本域 --%>
								<c:when test="${column.colShowType=='textarea'}">
									<textarea placeholder="请输入内容" name="${column.colNameEn }"  id="${column.colNameEn }"
										class="layui-textarea" max_length="${column.colValueMaxLength }" alt="${column.colNameCn }"></textarea>
								</c:when>
								<%--下拉选择框 --%>
								<c:when test="${column.colShowType=='select' }">
									<select name="${column.colNameEn }" id="${column.colNameEn }" lay-search>
									  <c:if test="${column.chooseDataList!=null }">
									  		<c:forEach items="${column.chooseDataList}" var="colData">
									  			<option value="${colData.CODE }">${colData.NAME }</option>
									  		</c:forEach>
									  </c:if>
									</select>     
								</c:when>
								<%--多选框 --%>
								<c:when test="${column.colShowType=='multi-select' }">
		                            <select name="${column.colNameEn }" id="${column.colNameEn }" class="form-control select2 ${column.colNameEn }" 
		                            data-type="${column.colNameEn }" multiple="multiple" selectType="select2" lay-verify="${column.colDataType }"
		                            data-cnname="${column.colNameCn }">
		                              <c:if test="${column.chooseDataList!=null }">
		                                    <c:forEach items="${column.chooseDataList}" var="colData">
		                                        <option value="${colData.CODE }">${colData.NAME }</option>
		                                    </c:forEach>
		                              </c:if>
		                            </select>
		                            <script type="text/javascript">
		                                initSelect2("${column.colNameEn }","请选择${column.colNameCn }，支持多选");
		                            </script>     
		                        </c:when>
							</c:choose>
						</div>
					</div>
				<c:if test="${(status.index+1) % 2 == 0 ||status.count == tableVO.columnList.size()}">
					</div>
				</c:if>
				</c:if>
			</c:forEach>
			<button class="layui-btn" lay-submit lay-filter="add" id="submitBtn" type="button" style="display: none">立即提交</button>
		</form:form>
	</div>
</body>
</html>