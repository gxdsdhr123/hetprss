<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>领导排班管理</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>

<style type="text/css">
html,body{
	width:100% !important;
	height:100% !important;
	overflow:auto;
}
.layui-form select {
	display: block;
}

.select2-results__options {
	height: 105px;
}

.select2-container .select2-selection--single {
	height: 34px;
}

.select2-container--default.select2-container--disabled .select2-selection--single
	{
	background-color: #002F63;
}
.mark_c {
    position: absolute;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.01);
    z-index: 9;
}
</style>
<body>
	<div class="mark_c" style="display: block;">loading...</div>
	<input type="hidden" id="officeId" value="${officeId }" />
	<input type="hidden" id="staffId" value="${staffId }" />
	<div class="layui-form"
		style="float: left; margin-left: 10%; position: fixed;">
		<div class="layui-form-item">
			<label class="layui-form-label"><font color="red">*</font>选择人员：</label>
			<div class="layui-input-inline">
				<select name="templateConf" id="templateConf"
					class="select2 form-control departType" style="width: 100%;"
					onchange="changeSelect();"
					<c:if test="${null != staffName && '' != staffName }">disabled</c:if>>
					<option value="">请选择人员</option>
					<c:forEach items="${staffList}" var="office">
						<option value="${office.ID}">${office.NAME}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label"><font color="red">*</font>开始时间：</label>
			<div class="layui-input-inline">
			<input id="startTm" class="layui-input" name="startTm time_c" maxlength="4" 
					onfocus="WdatePicker({ dateFmt:'HHmm'})" placeholder="请输入开始时间"
					value="${startTm }" />
					
<!-- 				<input type="text" id="startTm" class="layui-input time_c" maxlength="4" onKeyPress="return changeSeqNum(event, this);" -->
<%-- 					value="${startTm }"  /> --%>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label"><font color="red">*</font>结束时间：</label>
			<div class="layui-input-inline">
				<input id="endTm" class="layui-input" name="endTm time_c" maxlength="4" 
					onfocus="WdatePicker({ dateFmt:'HHmm'})" placeholder="请输入结束时间"
					value="${endTm }" />
					
<!-- 				<input type="text" id="endTm" class="layui-input time_c" maxlength="4"  onKeyPress="return changeSeqNum(event, this);" -->
<%-- 					value="${endTm }" /> --%>
			</div>
		</div>
	</div>
	<script type="text/javascript">

		$(function() {
			$("#templateConf option[value='${selectId }']").attr("selected", true);

			$('#templateConf').select2({
				placeholder : "请选择人员",
				width : "100%",
				language : "zh-CN",
				templateResult : formatRepo,
				templateSelection : formatRepoSelection,
				escapeMarkup : function(markup) {
					return markup;
				}
			});

			function formatRepo(repo) {
				return repo.text;
			}
			// select2显示选项处理
			function formatRepoSelection(repo) {
				return repo.text;
			}


		});
		function changeSelect() {
			$('#staffName').val($('#templateConf').find("option:selected").text());
			parent.workerId = $('#templateConf').find("option:selected").val();
		}
		
		// 获取开始时间
		function getStartTm() {
			if(null == $('#startTm').val() || "" == $('#startTm').val().trim()) {
				return "";
			} else {				
				return $('#startTm').val();
			}
		}
		// 获取结束时间
		function getEndTm() {
			if(null == $('#endTm').val() || "" == $('#endTm').val().trim()) {
				return "";
			} else {				
				return $('#endTm').val();
			}
		}
		
		// 强制改变滚动条样式
		 document.getElementById('templateConf').addEventListener("click",function() {
			$('.select2-results__options').each(function(){
				$(this).css('position' , 'relative');
				var s = new PerfectScrollbar(this);
				scrollArr.push(s);
				if(this.id)
					scrollObjs[this.id] = s;
			});
		});
		window.onload = function() {
			// 取消遮罩
			setTimeout(function() {
				$('div[class=mark_c]').attr({'style': 'display: none;'});
			}, 600);
			
		};
		
		function changeSeqNum(evt, e) {
		    //火狐使用evt.which获取键盘按键值，IE使用window.event.keyCode获取键盘按键值
		    var ev = evt.which?evt.which:window.event.keyCode;
		    if((ev>=48&&ev<=57) || ev==8){
		        return true;    
		    }else{
		    	 return false;
		    }  
		};
	</script>
</body>
</html>