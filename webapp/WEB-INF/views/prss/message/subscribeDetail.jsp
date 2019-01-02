<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp" %>
<link href="${ctxStatic}/prss/common/css/param.css" rel="stylesheet" />
<title>${flag=='add'?"新增":"修改" }消息订阅</title>
<style type="text/css">
.layui-form-text .layui-input-inline{
	width: 155px;
}
</style>
</head>

<body>
<script type="text/javascript">

// 根据接受人类型  清空接收人名称
layui.use("form", function() {
	var form = layui.form;
	var ValNull=$("#torangenames").val();
    if (ValNull=="") {
   $("#torangenames").attr("disabled",true);   
      }else{
         $("#torangenames").attr("disabled",false);
}
	form.on('select(subFilter)', function(data) {
		$('#torangenames').val("");
		$("#torange").val("");		
		 if(data.value == "-1"){
				$("#torangenames").attr("disabled",true);
			}else{
				$("#torangenames").attr("disabled",false);
			}
		if (data.value == "9") {
			$('#torangenames').val("sys");
		}
		if(data.value=="0"){
			$(".user_ifsms").removeAttr("hidden");
			$(".texter").removeAttr("hidden");
		} else {
			$(".user_ifsms").attr("hidden","hidden");
			
			if(data.value=="1"||data.value=="2"){
				$(".texter").removeAttr("hidden");
			}else if(data.value=="8"){
				
				$(".texter").attr("hidden","hidden");
			}		
		}
	});	
})
 </script>
 <script type="text/javascript">
	layui.use("form", function() {
		var form = layui.form;
		form.on('select(hbFilter)', function(data) {
			$('#schtime').val("");
		});
	})
	function delHBevent(){
		layui.use("form", function() {
			var form = layui.form;
			$('#hbevent').val("");
			form.render('select');
			$('#colids').val("");
			$('#drlStr').val("");
			$('#drools').val("");
			$('#condition').text("");
			
		})
		
		
	}
</script>
	<form id="createForm" class="layui-form" action="${ctx}/message/subscribe/save">
	<input type="hidden" name="flagsr" id="flagsr" value="0"/>
		<input type="hidden" name="flag" id="flag" value="${flag }"/>
		<input type="hidden" name="id" id="id" value="${vo.id }"/>
		<input type="hidden" name="ruleId" id="ruleId" value="${vo.ruleid }"/>
		<input type="hidden" name="jobId" id="jobId" value="${vo.jobId }"/>
		<input type="hidden" name="mftype" id="mftype" />
		<input type="hidden" id="schema" name="schema" value="88"/>
			<div class="layui-form-item  layui-form-text">
				<div class="layui-inline">
					<label class="layui-form-label">
						<strong>触发条件设置</strong>
					</label>
				</div>
			</div>
		<div class="layui-form-item">
			<div class="layui-form-item  layui-form-text">
				<div class="layui-inline">
					<label class="layui-form-label">触发类型</label>
					<div class="layui-input-inline">
						<select name="hbevent" id="hbevent" lay-filter="hbFilter">
							<option></option>
							<c:forEach items="${listDSE}" var="listDSE" varStatus="status">
								<option value="${listDSE.code}"
									<c:if  test="${listDSE.code==vo.hbevent}"> selected="selected"</c:if>>${listDSE.name }
								</option>
							</c:forEach>
						</select>
					</div>
				</div>
			</div>

			<div class="layui-form-item  layui-form-text">
				<div class="layui-inline">
					<label class="layui-form-label">触发条件</label>
					<div class="layui-input-inline" style="width: 600px;">
						<input id="old_colids" type="hidden" value="${vo.colids}">
						<input id="old_drlStr" type="hidden" value="${fn:escapeXml(vo.drlStr) }" > 
						<input id="old_drools" type="hidden" value="${fn:escapeXml(vo.drools) }">
						<input id="old_condition" type="hidden" value="${fn:escapeXml(vo.condition) }">
					
					
						<input id="colids" name="colids" type="hidden" value="${vo.colids}">
						<input id="drlStr" name="drlStr" type="hidden" value="${fn:escapeXml(vo.drlStr) }">
						<input id="drools" name="drools" type="hidden" value="${fn:escapeXml(vo.drools) }">
						<textarea id="condition" name="condition" placeholder="请使用向导配置条件" disabled="disabled"
							class="layui-textarea" rows="2">${vo.condition}</textarea>
					</div>
					<div class="layui-inline">
						<a id="guide" href="javascript:void(0)" class="an-h">【向导】</a>
					</div>
				</div>
			</div>
			<div class="layui-form-item  layui-form-text">
				<div class="layui-inline">
					<label class="layui-form-label">
						<strong>发送条件设置</strong>
					</label>
				</div>
			</div>
			<div class="layui-form-item  layui-form-text">
					<div class="layui-inline">
						<label class="layui-form-label">航班号</label>
						<div class="layui-input-inline">
							<input id="flightnumber" name="flightnumber" class="layui-input"
								type="text" value="${vo.flightnumber }">
						</div>
					</div>

				<c:if test="${userType!='3'}">
					<div class="layui-inline">
						<label class="layui-form-label">进出港</label>
						<div class="layui-input-inline">
							<select name="hbiotype" id="hbiotype">
								<option value="all" ${vo.hbiotype=='all'?'selected':'' }>请选择</option>
								<option value="A" ${vo.hbiotype=='A'?'selected':'' }>进港</option>
								<option value="D" ${vo.hbiotype=='D'?'selected':'' }>出港</option>
							</select>
						</div>
					</div>
				</c:if>

				<div class="layui-inline">
					<label class="layui-form-label">发送时间</label>
					<div class="layui-input-inline">
						<input id="schtime" name="schtime" class="layui-input" type="text"
							value="${vo.schtime }" onkeyup="delHBevent()">
					</div>
				</div>
				<div class="layui-inline">(格式1310)</div>
			</div>
			<div class="layui-form-item  layui-form-text">
				<div class="layui-inline">
					<label class="layui-form-label">
						<strong>通用设置</strong>
					</label>
				</div>
			</div>
			<div class="layui-form-item  layui-form-text">

				<c:if test="${userType!='3'}">
					<div class="layui-inline">
						<label class="layui-form-label">模板名称</label>
						<div class="layui-input-inline">
							<input type="hidden" id="mtemplid" name="mtemplid"
								value="${vo.mtemplid}" /> <input id="mtemplname"
								placeholder="双击选择模板" name="mtemplname" class="layui-input"
								type="text" value="${vo.mtemplname }" ondblclick="mMessage()">
						</div>
					</div>
				</c:if>
				<div class="layui-inline">
					<label class="layui-form-label">启用状态</label>
					<div class="layui-input-inline">
						<select name="disable" id="disable">
							<option value="1" ${ vo.disable == 1?'selected':''}>启用</option>
							<option value="0" ${ vo.disable == 0?'selected':''}>停用</option>
						</select>
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">弹出询问框</label>
					<div class="layui-input-inline">
						<input name="ifpopup" type="checkbox"
							${vo.ifpopup==1?'checked':''}>
					</div>
				</div>
			</div>
			<div class="layui-form-item  layui-form-text">
				<div class="layui-inline">
					<label class="layui-form-label">发送日期</label>
					<div class="layui-input-inline">
						<input id="sendDate" name="sendDate" class="layui-input" type="text"
							value="${vo.sendDate }">
					</div>
				</div>
				<div class="layui-inline">(格式20180101)</div>
			</div>
			<div class="layui-form-item  layui-form-text">
		    <div class="layui-inline">
				<label class="layui-form-label">消息正文</label>
				<div class="layui-input-inline" style="width: 600px;">
					<input type="hidden" id="old_varcols" value="${vo.varcols}"/>
					<input type="hidden" id="old_mtext" value="${vo.mtext }"/>
			
                     <input id="varcols" name="varcols"  type="hidden" value="${vo.varcols}">
					<div contenteditable="true" id="mtext"  class="layui-textarea" style="overflow-wrap: break-word;" >&nbsp;${vo.mtext }</div> 
				</div>
				<div class="layui-inline">
				<a id="paramBtn" href="javascript:void(0)" onclick="paramList()" class="an-h">【参数】</a>    
				</div>
				</div>
		    </div>
		    
		    <div class="layui-form-item  layui-form-text">
			    <div class="layui-inline">
					<label class="layui-form-label">接收人类型</label>
					<div class="layui-input-inline">
						 <select name="mfromtype" id="mfromtype" lay-filter="subFilter" >
	                        <option value="-1" >请选择<option>
							<option value="0" ${ vo.mfromtype == 0?'selected':''}  >个人</option>
							<option value="1" ${ vo.mfromtype == 1?'selected':''}  >角色</option>
							<option value="2" ${ vo.mfromtype == 2?'selected':''}  >部门</option>
<%-- 							<option value="8" ${ vo.mfromtype == 8?'selected':''}  >作业人</option> --%>
						</select>
					</div>
			    </div>
			    
			    <div class="layui-inline user_ifsms" ${vo.mfromtype==0?'':'hidden'}>
					<label class="layui-form-label">短信通知</label>
					<div class="layui-input-inline">
					    <input name="ifsms" type="checkbox" ${vo.ifsms==1?'checked':''}  >
					    
<%-- 					    <input type="checkbox" name="ifsms"  value="${vo.ifsms}"   --%>
<%-- 							 title="${vo.ifsms}" /> --%>
					</div>
			    </div>
		    </div>
		    <div class="layui-form-item  layui-form-text" >
			    <div class="layui-inline texter" ${vo.mfromtype==8?'hidden':''}>
					<label class="layui-form-label">接收人</label>
					<div class="layui-input-inline">
						<input type = "hidden" id = "torange" name="torange" value="${vo.torange}"/>
						<input id="torangenames" name="torangenames" class="layui-input" type="text" contenteditable="false"
						value="${vo.torangenames }" onclick="reciverList()">
					</div>
			    </div>
		    </div>
		    <c:if test="${flag=='upd'}">
		    <div class="layui-form-item  layui-form-text">
		     <div class="layui-inline">
				<label class="layui-form-label">创建人</label>
				<div class="layui-input-inline">
				    <input type = "hidden" id = "cruserid" name="cruserid" value="${vo.cruserid}"/>
					<input id="cruseren" name="cruseren" class="layui-input" type="text" value="${vo.cruseren }" disabled="disabled">
				</div>
		    </div>
		     <div class="layui-inline">
				<label class="layui-form-label">创建时间</label>
				<div class="layui-input-inline">
					<input id="crtime" name="crtime" class="layui-input" type="text" value="${vo.crtime }"disabled="disabled">
				</div>
		    </div>
			</div>
		    </c:if>
</div>
	</form>	
	
	<script type="text/javascript" src="${ctxStatic}/prss/message/subscribeDetail.js"></script>
	<script type="text/javascript" src="${ctxStatic}/prss/common/params.js"></script>
	<script type="text/javascript" src="${ctxStatic}/prss/message/ChkUtil.js"></script>
</body>
</html>