<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/prss/message/css/messageTemplInfo.css"
	rel="stylesheet" />
<title>${flag=='add'?"新增":"修改" }模板</title>
</head>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">

</script>


<body>
	<form id="createForm" class="layui-form"
		action="${ctx}/message/type/save">
		<input type="hidden" name="flag" id="flag" value="${flag }" /> <input
			type="hidden" name="id" id="id" value="${id}" /> <input
			type="hidden" name="mtype" id="mtype" value="${mtype }" />
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">模板名称</label>
				<div class="layui-input-inline">
					<input name="tempname" class="layui-input" type="text"
						value="${vo.tempname}">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">消息标题</label>
				<div class="layui-input-inline">
					<input name="mtitle" class="layui-input" type="text"
						value="${vo.mtitle }">
				</div>
			</div>
		</div>
		<div class="layui-form-item  layui-form-text">
		<div class="layui-inline">
			<div class="layui-block" style="width: 600px;">
				<label class="layui-form-label">消息正文</label>
				<div class="layui-input-block">
					<textarea id="mtext" name="mtext" placeholder="请输入内容" class="layui-textarea"
						rows="2" >${vo.mtext}</textarea>
				</div>
				</div>
				
				
				
			</div>
			<div class="layui-inline">
						<a id="varList" href="javascript:void(0)" onclick="varList()" class="an-h">【参数】</a>   
				</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">是否回复</label>
				<div class="layui-input-inline" style="width: 50px;">
					<input name="ifreply" type="checkbox" ${vo.ifreply==1?'checked':''}>
				</div>
				<div class="layui-form-mid">默认内容</div>
				<div class="layui-input-inline" style="width: 75px;">
					<input name="defreply" class="layui-input" type="text"
						value="${vo.defreply }" style="width: 100px;">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">语音提示</label>
				<div class="layui-input-inline" style="width: 50px;">
					<input name="ifsound" type="checkbox" ${vo.ifsound==1?'checked':''}>
				</div>
				<div class="layui-form-mid">内容</div>
				<div class="layui-input-inline" style="width: 75px;">
					<input name="soundtxt" class="layui-input" type="text"
						value="${vo.soundtxt }" style="width: 100px;">
				</div>
			</div>
		</div>

		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">右键菜单</label>
				<div class="layui-input-inline">
					<input name="ifpopm" type="checkbox" ${vo.ifpopm==1?'checked':''}>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">语音通话</label>
				<div class="layui-input-inline">
					<input name="ifspeak" type="checkbox" ${vo.ifspeak==1?'checked':''}>
				</div>
			</div>
		</div>

		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">右键排序</label>
				<div class="layui-input-inline">
					<select name="mnumb" id="mnumb">
						<option value="1" ${ vo.mnumb == 1?'selected':''}>1</option>
						<option value="2" ${ vo.mnumb == 2?'selected':''}>2</option>
						<option value="3" ${ vo.mnumb == 3?'selected':''}>3</option>
						<option value="4" ${ vo.mnumb == 4?'selected':''}>4</option>
						<option value="5" ${ vo.mnumb == 5?'selected':''}>5</option>
						<option value="6" ${ vo.mnumb == 6?'selected':''}>6</option>
						<option value="7" ${ vo.mnumb == 7?'selected':''}>7</option>
						<option value="8" ${ vo.mnumb == 8?'selected':''}>8</option>
						<option value="9" ${ vo.mnumb == 9?'selected':''}>9</option>
					</select>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">功能链接</label>
				<div class="layui-input-inline">
					<input name="extact" class="layui-input" type="text"
						value="${vo.extact }">
				</div>
			</div>
		</div>

		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">进出港范围</label>
				<div class="layui-input-inline">
					<select name="fiotype" id="fiotype">
						<option value="NO" ${ vo.fiotype == 'NO'?'selected':''}>无关联</option>
						<option value="A0" ${ vo.fiotype == 'A0'?'selected':''}>进港</option>
						<option value="D0" ${ vo.fiotype == 'D0'?'selected':''}>出港</option>
					</select>
				</div>
				<div class="layui-form-mid">关联航班</div>
				<div class="layui-input-inline layui-input-inline-width"
					style="width: 75px;">
					<input name="ifflight" type="checkbox"
						${vo.ifflight==1?'checked':''}>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">消息类型</label>
				<div class="layui-input-inline">
					<select name="mftype" id="mftype">
						<option value="0" ${ vo.mftype == 0?'selected':''}>源指令</option>
						<option value="1" ${ vo.mftype == 1?'selected':''}>转发指令</option>
					</select>
				</div>
			</div>
		</div>
	</form>
	<ul id="myTab" class="nav nav-tabs">
	<li class="active">
	<a href="#home" data-toggle="tab">发送人范围</a>
	</li>
	<li><a href="#ios" data-toggle="tab">接收人范围</a></li>
	</ul>
	<div id="myTabContent" class="tab-content">
	<div class="tab-pane fade in active" id="home">
	<li>
	    <button id="addPage" type="button" class="btn btn-link">增加</button>
		<button id="updatePage" type="button" class="btn btn-link">修改</button>
		<button id="delSender" type="button" class="btn btn-link" onclick="delSender()">删除</button>
	</li>
		<div id="baseTables">
		<table id="baseTable"></table>
	    </div>
	</div>
	<div class="tab-pane fade" id="ios">
			<li><button id="addPageOther" type="button" class="btn btn-link">增加</button>
				<button id="updatePageOther" type="button" class="btn btn-link">修改</button>
				<button id="delSender1" type="button" class="btn btn-link">删除</button>
			</li>
	<div id="baseTables1">
		<table id="baseTable1"></table>
	</div>
	</div>
	</div>
	<script type="text/javascript"
		src="${ctxStatic}/prss/message/messageTemplInfo.js"></script>
	<script type="text/javascript"
		src="${ctxStatic}/prss/message/ChkUtil.js"></script>
</body>
</html>