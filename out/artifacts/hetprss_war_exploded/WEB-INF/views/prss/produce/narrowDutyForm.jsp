<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
	<meta name="decorator" content="default" />
	<title>新增单据</title>
	<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
	<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
	<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
	<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript"	src="${ctxStatic}/prss/produce/narrowDutyForm.js"></script>
	<script type="text/javascript">
		var tmpGridData;
		<c:if test="${bill != null }">
			tmpGridData = ${bill.billZpqwbgGoodsStr};
		</c:if>
	</script>
	</head>
	<body>
		<form id="newBill" action="" class="form-horizontal">
			<c:if test="${bill != null }">
				<input type="hidden" name="id" id="id" value="${bill.id}"/>
			</c:if>
			<!-- 单据头信息 -->
			<div class="row">
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-4 control-label">日期</label>
				    <div class="col-sm-8">
				      	<input type="text" class="form-control" name="flightDate"  id="flightDate"
				      	onclick="WdatePicker({dateFmt:'yyyyMMdd'});"	<c:if test="${bill != null }">value="${bill.flightDate}"  disabled="disabled"</c:if>	/>
				    </div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-4 control-label">航班号</label>
				    <div class="col-sm-8">
				      	<input type="text" class="form-control" name="flightNumber"  id="flightNumber" <c:if test="${bill != null }">value="${bill.flightNumber}"  disabled="disabled"</c:if> />
				    </div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-4 control-label">进出港</label>
			    	<c:if test="${bill != null}">
			    		<div class="col-sm-8">
			    			<input type="text" class="form-control"  value="<c:if test="${bill.inout == 'A' }">进港</c:if><c:if test="${bill.inout == 'D' }">出港</c:if>"  disabled="disabled" />
			    		</div>
			    	</c:if>
			    	<c:if test="${bill == null}">
					    <div class="col-sm-8 form-inline">
					      	<div class="radio" style="margin-right:30px;">
							  <label>
							    <input type="radio" name="inout" id="inout1" value="A" checked>进港
							  </label>
							</div>
					      	<div class="radio">
							  <label>
							    <input type="radio" name="inout" id="inout2" value="D" >出港
							  </label>
							</div>
					    </div>
				    </c:if>
				 </div>
			</div>
			<div class="row">
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-4 control-label">查理</label>
				    <div class="col-sm-8">
				    	<c:if test="${bill != null}">
				    		<input type="text" class="form-control"  value="${bill.operatorName }"  disabled="disabled" />
				    	</c:if>
				    	<c:if test="${bill == null}">
					    	<select class="form-control select2"  name="operator" id="operator">
								<option value=""></option>
								<c:forEach items="${operatorList}" var="operator">
									<option value="${operator.id}" >${operator.name}</option>
								</c:forEach> 
					    	</select>
				    	</c:if>
				    </div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-4 control-label">组员</label>
				    <div class="col-sm-8">
				      	<input type="text" class="form-control" name="members"   <c:if test="${bill != null && bill.members != null}">value="${bill.members }"</c:if>/>
				    </div>
				 </div>
			</div>
			
			<!-- 航班信息 -->
			<div class="row">
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-4 control-label">飞机注册号</label>
				    <div class="col-sm-8">
				      	<input type="text" class="form-control" id="aircraftNumber" disabled="disabled" <c:if test="${bill != null && bill.aircraftNumber != null}">value="${bill.aircraftNumber }"</c:if> />
				    </div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-4 control-label">机型</label>
				    <div class="col-sm-8">
				      	<input type="text" class="form-control" id="acttypeCode"   disabled="disabled" <c:if test="${bill != null && bill.actType != null}">value="${bill.actType }"</c:if> />
				    </div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-4 control-label">停机位</label>
				    <div class="col-sm-8">
				      	<input type="text" class="form-control" id="actstandCode"   disabled="disabled" <c:if test="${bill != null && bill.actstandCode != null}">value="${bill.actstandCode }"</c:if> />
				    </div>
				 </div>
			</div>
			<div class="row">
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-4 control-label">预计进港时间</label>
				    <div class="col-sm-8">
				      	<input type="text" class="form-control" id="eta"   disabled="disabled" <c:if test="${bill != null && bill.eta != null}">value="${bill.eta }"</c:if> />
				    </div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-4 control-label">实际进港时间</label>
				    <div class="col-sm-8">
				      	<input type="text" class="form-control" id="ata"   disabled="disabled" <c:if test="${bill != null && bill.ata != null}">value="${bill.ata }"</c:if> />
				    </div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-4 control-label">始发地</label>
				    <div class="col-sm-8">
				      	<input type="text" class="form-control" id="departApt4code"   disabled="disabled" <c:if test="${bill != null && bill.departApt4code != null}">value="${bill.departApt4code }"</c:if> />
				    </div>
				 </div>
			</div>
			<div class="row">
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-4 control-label">预计出港时间</label>
				    <div class="col-sm-8">
				      	<input type="text" class="form-control" id="etd"  disabled="disabled" <c:if test="${bill != null && bill.etd != null}">value="${bill.etd }"</c:if> />
				    </div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-4 control-label">实际出港时间</label>
				    <div class="col-sm-8">
				      	<input type="text" class="form-control" id="atd"  disabled="disabled" <c:if test="${bill != null && bill.atd != null}">value="${bill.atd }"</c:if> />
				    </div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-4 control-label">目的地</label>
				    <div class="col-sm-8">
				      	<input type="text" class="form-control" id="arrivalApt4code"  disabled="disabled" <c:if test="${bill != null && bill.arrivalApt4code != null}">value="${bill.arrivalApt4code }"</c:if> />
				    </div>
				 </div>
			</div>
			<!-- 机务 -->
			<div class="row">
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-6 control-label">舱内壁是否符合检查标准</label>
				    <div class="col-sm-6 form-inline">
				      	<div class="radio" style="margin-right:30px;">
						  <label>
						    <input type="radio" name="cnbsf" id="cnbsf1" value="1" checked>是
						  </label>
						</div>
				      	<div class="radio">
						  <label>
						    <input type="radio" name="cnbsf" id="cnbsf2" value="2" <c:if test="${bill != null && bill.cnbsf =='2'}">checked</c:if>>否
						  </label>
						</div>
				    </div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-6 control-label">舱门是否符合检查标准</label>
				    <div class="col-sm-6 form-inline">
				      	<div class="radio" style="margin-right:30px;">
						  <label>
						    <input type="radio" name="cmsf" id="cmsf1" value="1" checked>是
						  </label>
						</div>
				      	<div class="radio">
						  <label>
						    <input type="radio" name="cmsf" id="cmsf2" value="2" <c:if test="${bill != null && bill.cmsf == '2'}">checked</c:if>>否
						  </label>
						</div>
				    </div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-6 control-label">进港装载是否与电报一致</label>
				    <div class="col-sm-6 form-inline">
				      	<div class="radio" style="margin-right:30px;">
						  <label>
						    <input type="radio" name="jgzzsf" id="jgzzsf1" value="1" checked>是
						  </label>
						</div>
				      	<div class="radio">
						  <label>
						    <input type="radio" name="jgzzsf" id="jgzzsf2" value="2" <c:if test="${bill != null && bill.jgzzsf == '2'}">checked</c:if>>否
						  </label>
						</div>
				    </div>
				 </div>
			</div>
			<div class="row">
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-4 control-label">廊桥/客梯车对靠完毕时间</label>
				    <div class="col-sm-8">
				      	<input type="text" class="form-control" id="lqdkTime"   disabled="disabled" <c:if test="${bill != null && bill.lqdkTime != null}">value="${bill.lqdkTime }"</c:if> />
				    </div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-4 control-label">货舱门开启时间</label>
				    <div class="col-sm-8">
				      	<input type="text" class="form-control" id="hcmkqTime"   disabled="disabled" <c:if test="${bill != null && bill.hcmkqTime != null}">value="${bill.hcmkqTime }"</c:if> />
				    </div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-4 control-label">装舱完成时间</label>
				    <div class="col-sm-8">
				      	<input type="text" class="form-control" id="zcwcTime"   disabled="disabled" <c:if test="${bill != null && bill.zcwcTime != null}">value="${bill.zcwcTime }"</c:if> />
				    </div>
				 </div>
			</div>
			<div class="row">
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-4 control-label">卸舱完毕时间</label>
				    <div class="col-sm-8">
				      	<input type="text" class="form-control" id="xcwcTime"   disabled="disabled" <c:if test="${bill != null && bill.xcwcTime != null}">value="${bill.xcwcTime }"</c:if> />
				    </div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-4 control-label">最后货舱门关闭时间</label>
				    <div class="col-sm-8">
				      	<input type="text" class="form-control" id="zhhcmgbTime"   disabled="disabled" <c:if test="${bill != null && bill.zhhcmgbTime != null}">value="${bill.zhhcmgbTime }"</c:if> />
				    </div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-4 control-label">装舱指示取得时间</label>
				    <div class="col-sm-8">
				      	<input type="text" class="form-control" name="zczsqdTime"  id="zczsqdTime"
				      	onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});" <c:if test="${bill != null && bill.zczsqdTime != null}">value="${bill.zczsqdTime }"</c:if>/>
				    </div>
				 </div>
			</div>
			<div class="row">
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-4 control-label">散舱门关闭时间</label>
				    <div class="col-sm-8">
				      	<input type="text" class="form-control" id="scmgbTime"   disabled="disabled" <c:if test="${bill != null && bill.scmgbTime != null}">value="${bill.scmgbTime }"</c:if> />
				    </div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-4 control-label">开始装舱时间</label>
				    <div class="col-sm-8">
				      	<input type="text" class="form-control" name="kszcTime"  id="kszcTime"
				      	onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});" <c:if test="${bill != null && bill.kszcTime != null}">value="${bill.kszcTime }"</c:if>/>
				    </div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-4 control-label">进港行李交接时间</label>
				    <div class="col-sm-8">
				      	<input type="text" class="form-control" name="jgxljjTime"  
				      	onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});" <c:if test="${bill != null && bill.jgxljjTime != null}">value="${bill.jgxljjTime }"</c:if>/>
				    </div>
				 </div>
			</div>
			<div class="row">
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-4 control-label">进港货邮交接时间</label>
				    <div class="col-sm-8">
				      	<input type="text" class="form-control" name="jghyjjTime" 
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});" <c:if test="${bill != null && bill.jghyjjTime != null}">value="${bill.jghyjjTime }"</c:if>/>
				    </div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-4 control-label">出港行李交接时间</label>
				    <div class="col-sm-8">
				      	<input type="text" class="form-control" name="cgxljjTime"  
				      	onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});" <c:if test="${bill != null && bill.cgxljjTime != null}">value="${bill.cgxljjTime }"</c:if>/>
				    </div>
				 </div>
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-4 control-label">出港货邮交接时间</label>
				    <div class="col-sm-8">
				      	<input type="text" class="form-control" name="cghyjjTime"  
				      	onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});" <c:if test="${bill != null && bill.cghyjjTime != null}">value="${bill.cghyjjTime }"</c:if>/>
				    </div>
				 </div>
			</div>
			<div class="row">
				 <div  class="col-sm-4 form-group" >
				 	<label for="username" class="col-sm-4 control-label">航材交接时间</label>
				    <div class="col-sm-8">
				      	<input type="text" class="form-control" name="hcjjTime"  
				      	onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});" <c:if test="${bill != null && bill.hcjjTime != null}">value="${bill.hcjjTime }"</c:if>/>
				    </div>
				 </div>
			</div>
			
			
			<div class="row" style="margin:10px -10px;">
				 <div  class="col-sm-12" >
				 	<div id="toolbar">
				 		<button id="addRow" type="button" class="btn btn-link">新增车辆</button>
				 	</div>
				 	<table id="createDetailTable" class="table"></table>
				 </div>
			</div>
			
			
			<!-- 机务签字及备注 -->
			<c:if test="${bill != null }">
				<div class="row">
					 <div  class="col-sm-12 form-group" >
					 	<label for="username" class="col-sm-1 control-label">机务签字</label>
					    <div class="col-sm-10">
					      	<img src="${ctx }/produce/bill/outputPicture?id=${bill.signatory }" height=45 width=80/>
					    </div>
					 </div>
				</div>
			</c:if>
			<div class="row">
				 <div  class="col-sm-12 form-group" >
				 	<label for="username" class="col-sm-1 control-label">备注</label>
				    <div class="col-sm-10">
				    	<textarea class="layui-textarea" rows="3" name="remark"><c:if test="${bill != null && bill.remark != null}">${bill.remark }</c:if></textarea>
				    </div>
				 </div>
			</div>
			
		</form>
	</body>
</html>