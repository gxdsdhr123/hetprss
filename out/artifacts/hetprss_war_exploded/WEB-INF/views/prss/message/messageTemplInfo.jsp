<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/prss/common/css/param.css" rel="stylesheet" />
<title>${flag=='add'?"新增":"修改" }模板</title>
</head>
<body>
<script type="text/javascript">
</script>
	<form id="createForm" class="layui-form" action="${ctx}/message/type/save">
		<input type="hidden" name="flag" id="flag" value="${flag }" /> <input
			type="hidden" name="id" id="id" value="${id}" /> <input
			type="hidden" name="mtype" id="mtype" value="${mtype }" />
			<input id="mtime" name="mtime"  type="hidden" value="${vo.mtime}">
			<input type="hidden" id="schema" name="schema" value="88"/>
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
				<label class="layui-form-label">模板关联事件</label>
				<div class="layui-input-inline">
				<input id="eventid" name="eventid" type="hidden" value="${vo.eventid}">
					<input id="eventname" name="eventname" class="layui-input" type="text" value="${vo.eventname}" ondblclick="searchEvent()">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">弹出询问框</label>
				<div class="layui-input-inline">
					<input name="ifpopup"  type="checkbox" ${vo.ifpopup==1?'checked':''}>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">事件记录</label>
				<div class="layui-input-inline">
					<input name="eventRecord"  type="checkbox" ${vo.eventRecord==1?'checked':''}>
				</div>
			</div>
		</div>
		<div class="layui-form-item  layui-form-text">
			<input type="hidden" id="old_varcols" value="${vo.varcols}"/>
			<input type="hidden" id="old_mtext" value="${vo.mtext }"/>
			<div class="layui-inline">
				<div class="layui-block" style="width: 600px;">
					<label class="layui-form-label">消息正文</label>
					<div class="layui-input-block" style="margin-left: 120px;">
						<input id="varcols" name="varcols" type="hidden"
							value="${vo.varcols}">
						<div contenteditable="true" id="mtext" class="layui-textarea"  style="overflow-wrap: break-word;" >&nbsp;${vo.mtext }</div>
					</div>
				</div>
			</div>
			<div class="layui-inline">
				<a id="varList" href="javascript:void(0)" onclick="varList()"
					class="an-h">【参数】</a>
			</div>
		</div>
		
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">右键菜单</label>
				<div class="layui-input-inline">
					<input name="ifpopm"  type="checkbox" ${vo.ifpopm==1?'checked':''}>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">是否回复</label>
				<div class="layui-input-inline" style="width: 50px;">
					<input name="ifreply" lay-filter="ifreplyFilter" 
						type="checkbox" ${vo.ifreply==1?'checked':''}>
				</div>
			</div>
			<div class="layui-inline user_defreply" ${vo.ifreply==1?'':'hidden'}>
				<label class="layui-form-label">回复内容</label>
				<div class="layui-input-inline">
					<input name="defreply" class="layui-input" type="text"
						value="${vo.defreply }" >
				</div>
			</div>
		</div>

		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">语音通话</label>
				<div class="layui-input-inline">
					<input name="ifspeak"  type="checkbox" ${vo.ifspeak==1?'checked':''}>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">语音提示</label>
				<div class="layui-input-inline" style="width: 50px;">
					<input name="ifsound" lay-filter="ifsoundFilter" 
						type="checkbox" ${vo.ifsound==1?'checked':''}>
				</div>
			</div>
			<div class="layui-inline user_ifsound" ${vo.ifsound==1?'':'hidden'}>
				<label class="layui-form-label">语音内容</label>
				<div class="layui-input-inline">
					<select name="soundtxt" id="soundtxt">
						<option value="" >请选择</option>
						<option value="起" ${ vo.soundtxt == '起'?'selected':''}>已起飞</option>
						<option value="落" ${ vo.soundtxt == '落'?'selected':''}>已落地</option>
						<option value="新" ${ vo.soundtxt == '新'?'selected':''}>您有一条新消息</option>
						<option value="位" ${ vo.soundtxt == '位'?'selected':''}>机位变更</option>
						<option value="号" ${ vo.soundtxt == '号'?'selected':''}>机号变更</option>
						<option value="降" ${ vo.soundtxt == '降'?'selected':''}>航班备降，请注意保障</option>
						<option value="返" ${ vo.soundtxt == '返'?'selected':''}>航班返航，请注意保障</option>
						<option value="取" ${ vo.soundtxt == '取'?'selected':''}>航班取消，请注意保障</option>
						<option value="保" ${ vo.soundtxt == '保'?'selected':''}>缺少保障人员，请处理</option>
					</select>
				</div>
			</div>
		</div>

		<div class="layui-form-item  layui-form-text">
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
				<label class="layui-form-label">消息类型</label>
				<div class="layui-input-inline">
					<select name="mftype" id="mftype">
						<option value="0" ${ vo.mftype == 0?'selected':''}>源指令</option>
						<option value="1" ${ vo.mftype == 1?'selected':''}>转发指令</option>
					</select>
				</div>
			</div>
		</div>
		<div class="layui-form-item  layui-form-text">
			<div class="layui-inline">
				<label class="layui-form-label">进出港范围</label>
				<div class="layui-input-inline">
					<select name="fiotype" id="fiotype">
						<option value="NO" ${vo.fiotype=='NO'?'selected':''}>无关联</option>
						<option value="A0" ${vo.fiotype=='A0'?'selected':''}>单进港</option>
						<option value="D0" ${vo.fiotype=='D0'?'selected':''}>单出港</option>
					</select>
				</div>
			</div>
			<div class="layui-inline">
				<div class="layui-form-label">关联航班</div>
				<div class="layui-input-inline ">
					<input name="ifflight" type="checkbox" 
						${vo.ifflight==1?'checked':''}>
				</div>
			</div>
			<div class="layui-inline">
				<div class="layui-form-label">默认发送</div>
				<div class="layui-input-inline ">
					<input name="senddef" type="checkbox" 
						${vo.senddef==1?'checked':''}>
				</div>
			</div>
		</div>
		
	</form>

	<ul id="myTab" class="nav nav-tabs">
		<li class="active"><a href="#home" data-toggle="tab">发送人范围</a></li>
		<li><a href="#ios" data-toggle="tab">接收人范围</a></li>
	</ul>
	<div id="myTabContent" class="tab-content">
		<div class="tab-pane fade in active" id="home">
			<button id="addPage" type="button" class="btn btn-link">增加</button>
			<button id="updatePage" type="button" class="btn btn-link">修改</button>
			<button id="delSender" type="button" class="btn btn-link">删除</button>
			<div id="baseTables">
				<table id="baseTable"></table>
			</div>
		</div>
		<div class="tab-pane fade" id="ios">
			<button id="addPageOther" type="button" class="btn btn-link">增加</button>
			<button id="updatePageOther" type="button" class="btn btn-link">修改</button>
			<button id="delSender1" type="button" class="btn btn-link">删除</button>

			<div id="baseTables1">
				<table id="baseTable1"></table>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/message/messageTemplInfo.js"></script>
	<script type="text/javascript" src="${ctxStatic}/prss/common/params.js"></script>
	<script type="text/javascript" src="${ctxStatic}/prss/message/ChkUtil.js"></script>
</body>
</html>