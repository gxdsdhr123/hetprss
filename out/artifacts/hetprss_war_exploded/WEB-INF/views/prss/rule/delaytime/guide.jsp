<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/prss/rule/css/guide.css" rel="stylesheet" />
<title>编辑条件</title>
</head>
<body style="min-height: 0px">
	<input type="hidden" name="flag" id="flag" value="${flag }" />
	<input type="hidden" name="id" id="id" value="${id }" />
	<table id="contentTable">
		<tbody>
			<tr>
				<td style="width: 260px;">
<!-- 					<span>变量名称</span> -->
<!-- 					<br/> -->
				<input name="param_val" id="param_val" class="layui-input" type="text" placeholder="变量名称模糊搜索">
					<div id="varLists" style="position: relative;"></div>
				</td>
				<td style="width: 69%;text-align: left;vertical-align: top;"><div>
					逻辑规则
<!-- 					<br/> -->
						<input type="button"
							class="layui-btn layui-btn-small layui-btn-normal" value="+"
							onclick="getValue(this,this.value,'add','opt')" />
						<input type="button"
							class="layui-btn layui-btn-small layui-btn-normal" value="-"
							onclick="getValue(this,this.value,'sub','opt')" />
						<input type="button"
							class="layui-btn layui-btn-small layui-btn-normal" value="*"
							onclick="getValue(this,this.value,'mul','opt')" />
						<input type="button"
							class="layui-btn layui-btn-small layui-btn-normal" value="/"
							onclick="getValue(this,this.value,'div','opt')" />
					<br/>
                    <br/>
							
<!-- 						<input type="button" -->
<!-- 							class="layui-btn layui-btn-small layui-btn-normal" value="等于" -->
<!-- 							onclick="getValue(this,'==','eq','opt')" /> -->
<!-- 						<input type="button" -->
<!-- 							class="layui-btn layui-btn-small layui-btn-normal" value="不等于" -->
<!-- 							onclick="getValue(this,'!=','uneq','opt')" /> -->
<!-- 						<input type="button" -->
<!-- 							class="layui-btn layui-btn-small layui-btn-normal" value="大于" -->
<!-- 							onclick="getValue(this,'>','gt','opt')" /> -->
<!-- 						<input type="button" -->
<!-- 							class="layui-btn layui-btn-small layui-btn-normal" value="大于等于" -->
<!-- 							onclick="getValue(this,'>=','gte','opt')" /> -->
						
<!-- 					<br/>	 -->
							
<!-- 						<input type="button" -->
<!-- 							class="layui-btn layui-btn-small layui-btn-normal" value="小于" -->
<!-- 							onclick="getValue(this,'&lt;','lt','opt')" /> -->
<!-- 						<input type="button" -->
<!-- 							class="layui-btn layui-btn-small layui-btn-normal" value="小于等于" -->
<!-- 							onclick="getValue(this,'&lt;=','lte','opt')" /> -->
<!-- 						<input type="button" -->
<!-- 							class="layui-btn layui-btn-small layui-btn-normal" value="匹配" -->
<!-- 							onclick="getValue(this,'match','match','opt')" /> -->
<!-- 						<input type="button" -->
<!-- 							class="layui-btn layui-btn-small layui-btn-normal" value="包含" -->
<!-- 							onclick="getValue(this,'contain','contain','opt')" /> -->
						
<!-- 					<br/>	 -->
							
<!-- 						<input type="button" -->
<!-- 							class="layui-btn layui-btn-small layui-btn-normal" value="前匹配" -->
<!-- 							onclick="getValue(this,'lmatch','lmatch','opt')" /> -->
<!-- 						<input type="button" -->
<!-- 							class="layui-btn layui-btn-small layui-btn-normal" value="后匹配" -->
<!-- 							onclick="getValue(this,'rmatch','rmatch','opt')" /> -->
<!-- 				</td> -->
<!-- 				<td style="width: 23%;"><div> -->
    
                            逻辑链接
<!--                    <br/>    -->
<!--                        <input type="button" -->
<!--                                class="layui-btn layui-btn-small layui-btn-normal" value="并且" -->
<!--                                onclick="getValue(this,'&&','and','join')" />  -->
<!--                        <input type="button" -->
<!--                                class="layui-btn layui-btn-small layui-btn-normal" value="或者" -->
<!--                                onclick="getValue(this,'||','or','join')" /> -->
<!--                     <br/>    -->
                        <input type="button"
                                class="layui-btn layui-btn-small layui-btn-normal" value="左括号"
                                onclick="getValue(this,'(','lb','join')" />
                        <input type="button"
                                class="layui-btn layui-btn-small layui-btn-normal" value="右括号"
                                onclick="getValue(this,')','rb','join')" />
                    <br/>   
                    <br/>
						<input name="input_val" id="input_val" class="layui-input" type="text">
<!-- 					<br/>	 -->
						<input type="button"
							class="layui-btn layui-btn-small layui-btn-normal" value="数字"
							onclick="inpuValue(this,'int','val')" /> 
						<input type="button"
							class="layui-btn layui-btn-small layui-btn-normal" value="字符"
							onclick="inpuValue(this,'string','val')" />
					<br/>
                    </div>
				</td>
			</tr>
			<tr>
				<td colspan='3'>
					<div id="mtext" class="layui-textarea"
						style="overflow-wrap: break-word;" >${condition }</div></td>
			</tr>
		</tbody>
	</table>
    <div id="leftOrRight" hidden="">
         <ul class="pop-ul">
            <li data-value="left" class="layui-nav-item">所选表达式前</li>
            <li data-value="right" class="layui-nav-item">所选表达式后</li>
         </ul>
    </div>
	<script type="text/javascript" src="${ctxStatic}/prss/rule/delaytime/guide.js"></script>

</body>
</html>