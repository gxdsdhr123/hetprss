var element;
var iframe_ps;
var validSessionInterval = null;
$(document).ready(function() {
	// 初始化菜单
	initNav();
	var defLink = getDefaultLink();
	if(defLink){
		setDefaultLink(defLink);
	} else {
		setChannelPath($('#topNav').find('dd.layui-this:first'));
	}
	layui.use([ 'element', 'layer' ], function() {
		if(!element){
			element = layui.element;
		}
		element.on('nav(menu)', function(elem) {
			if (elem && elem.parent().parent().parent().is("dd")) {
				var menuItem = elem.parent().parent().parent();
				var layui_this = menuItem.find('.active');
				$("#topNav li").removeClass("layui-this").removeClass('active');
				menuItem.parents('li').addClass("layui-this");//顶级菜单样式
				menuItem.addClass("layui-this");//二级菜单样式
				layui_this.addClass("layui-this");//当前菜单样式
				// 设置路径
				setChannelPath(menuItem);
			}
		});
		element.on('tab(mainTab)', function(data) {
			$("iframe[name=mainFrame]").removeAttr("id");
			$(".layui-tab-content .layui-show iframe[name=mainFrame]").attr("id","mainFrame");
			var elem = $("#topNav li #"+$(this).attr("lay-id"));
			if(elem && elem.parent().parent().parent().is("dd")){
				var menuItem = elem.parent().parent().parent();
				$("#topNav li").removeClass("layui-this").removeClass('active');
				$("#topNav dd").removeClass("layui-this").removeClass('active');
				menuItem.parents('li').addClass("layui-this");//顶级菜单样式
				menuItem.addClass("layui-this");//二级菜单样式
				elem.parent().find('.active');
				elem.parent().addClass("layui-this");//当前菜单样式
				// 设置路径
				setChannelPath(menuItem);
			}
		});
	});
	// 通知管理
	getNotifyNum();
	if (notifyRemindInterval && notifyRemindInterval != 0) {
		setInterval(getNotifyNum, notifyRemindInterval);
	}
	// 初始化连接
	CloudPush.initConfig(0, 0, initCP());
	// 显示当前系统时间
	setInterval(function() {
		refreshTime();
	}, 1000);
	$("#switchLogin").click(function(){
		switchLogin();
	});
	$("#logOutBtn").click(function(){
		try{
			clearTimeout(validSessionInterval);
			clearDefaultLink();
		}catch(e){
			console.log(e);
		}
	});
	// 检查当前用户是否在线
	isActive();
	setContentHeight();
	$(window).resize(function() {
		setContentHeight();
	});
	
	$.ajax({
		url : ctx + "/api/rest/schedulingAutoExport/getSheet1ExportColumn",
		dataType : "json",
		success : function(data) {
			console.log(data)
		}
	});
	//隐藏显示菜单栏
	$("#menuToggle").click(function(){
		var isHidden = $("#header").is(":hidden");
		if(isHidden){
			$("#iframeContainer").css({top:"60px"});
			$("#header").show(300,function(){
				setContentHeight();
			});
		} else {
			$("#iframeContainer").css({top:"0px"});
			$("#header").hide(300,function(){
				setContentHeight();
			});
		}
	});
});

function setContentHeight(){
	var headerHeight = 0;
	var footerHeight = $("#footer").height();
	if(!$("#header").is(":hidden")){
		headerHeight = $("#header").height();
	}
	$("#iframeContainer").height($(window).height()-headerHeight-footerHeight-1);
}

function getNotifyNum() {
	$.get(ctx + "/oa/oaNotify/self/count?updateSession=0&t="
			+ new Date().getTime(), function(data) {
		var num = parseFloat(data);
		if (num > 0) {
			$("#notifyNum span").html(num);
			$("#notifyNum span").show();
		} else {
			$("#notifyNum span").hide();
		}
	});
}
function initNav() {
	// 顶部菜单
	if (menuPosition == "T" || menuPosition == "TL") {
		var nav = $("<ul></ul>");
		nav.attr("id", "topNav");
		nav.attr("lay-filter", "menu");
		nav.addClass("layui-nav");
		nav.addClass("top-nav-container");
		var navDocument = getNavItem(nav, 1, "nav");
		$(".logo").after(navDocument);
		// 滚动条美化
		$('#topNav .layui-nav-child').each(function() {
			new PerfectScrollbar(this);
		})
	}
	// 左侧菜单
	if (menuPosition == "L" || menuPosition == "TL") {
		var parentId = 1;
		if (menuPosition == "TL") {
			parentId = $("#topNav li:first").attr("id");
			$("#topNav li:first").addClass("layui-this");
		}
		var side = $("<div></div>");
		side.addClass("layui-side");
		// side.addClass("layui-bg-black");

		var sideScroll = $("<div></div>");
		sideScroll.addClass("layui-side-scroll");
		sideScroll.attr("id", "leftSide");

		var nav = $("<ul></ul>");
		nav.addClass("layui-nav");
		nav.addClass("layui-nav-tree");

		var navDocument = getNavItem(nav, parentId, "side");
		sideScroll.append(navDocument);
		side.append(sideScroll);

		$(".layui-header").after(side);

		if (navDocument.children().length == 0) {
			$(".layui-side").hide();
			$(".layui-body").addClass("layui-body-full");
		}
	}
}

function getNavItem(nav, parentId, type) {
	var menuList = getMenuData(parentId);
	var isFirst = true;
	for (var i = 0; i < menuList.length; i++) {
		var menu = menuList[i];
		if (menu.parentId == parentId) {
			var navItem = $("<li></li>");
			navItem.attr("id", menu.id);
			navItem.addClass("layui-nav-item");
			var menuTag = $("<a></a>");
			menuTag.attr("href", "javascript:void(0)");
			var menuHref = menu.href;
			if (type == "nav" && menuPosition == "TL") {
				menuTag.bind("click", {
					parentId : menu.id,
					target : menuHref
				}, refreshSide);
			} else {
				if (menuHref && $.trim(menuHref) != "") {
					menuTag.bind("click", {
						target : menuHref,
						menuName : menu.name
					}, openPage);
				}
			}
			if (isFirst&&!getDefaultLink()) {// 页面初始化连接
				menuTag.trigger("click");
				navItem.addClass("layui-this");
			}
			if (menu.icon) {
				/*
				 * if(menu.icon.indexOf('[ICONID]')!=-1){ var iconSrc = ctx +
				 * '/sys/menu/getIcon?iconId=' + menu.icon.substring(8);
				 * menuTag.append($('<img src="'+iconSrc+'" width="15"
				 * height="15" style="margin-right:3px;"/>')); }else{
				 * menuTag.append($("<i class='fa fa-" + menu.icon + "'>&nbsp;</i>")); }
				 */
				menuTag.append($("<i class='fa fa-" + menu.icon
						+ "'>&nbsp;</i>"));
				menuTag.append($("<span>" + menu.name + "</span>"));
			} else {
				menuTag.text(menu.name);
			}
			navItem.append(menuTag);
			// 二级菜单
			if (type == "nav") {
				if (menuPosition == "T") {
					var childMenu = getNavChild(menuList, menu.id, isFirst);
					if (childMenu) {
						navItem.append(childMenu);
					}
				}
			} else {
				var childMenu = getNavChild(menuList, menu.id, isFirst);
				if (childMenu) {
					navItem.append(childMenu);
					if (isFirst&&!getDefaultLink()) {
						// 侧边导航默认展开第一个
						navItem.addClass("layui-nav-itemed");
					}
				}
			}
			nav.append(navItem);
			isFirst = false;
		}
	}
	return nav;
}

function getNavChild(menuList, parentId, isFirst) {
	var navChild = $("<dl class='layui-nav-child' ></dl>");
	for (var i = 0; i < menuList.length; i++) {
		var menu = menuList[i];
		if (menu.parentId == parentId) {
			var navItem = $("<dd></dd>");
			if (isFirst&&!getDefaultLink()) {// 默认选中第一个
				navItem.addClass("layui-this");
			}
			var menuTag = $("<a></a>");
			menuTag.attr("href", "javascript:void(0)");
			menuTag.attr("id", menu.id);
			var menuHref = menu.href;
			if (menuHref && $.trim(menuHref) != "") {
				menuTag.bind("click", {
					target : menuHref,
					menuName : menu.name
				}, openPage);
				if (isFirst&&!getDefaultLink()) {// 页面初始化连接
					menuTag.trigger("click");
				}
			}
			if (menu.icon) {
				menuTag.append($("<i class='fa fa-" + menu.icon
						+ "'>&nbsp;&nbsp;</i>"));
				menuTag.append($("<span>" + menu.name + "</span>"));
			} else {
				menuTag.text(menu.name);
			}
			navItem.append(menuTag);
			// 三级菜单
			var thirdChildMenu = getNavThirdChild(menuList, menu.id, isFirst);
			if (thirdChildMenu) {
				navItem.append(thirdChildMenu);
			}
			navChild.append(navItem);
			isFirst = false;
		}
	}
	return navChild.children("dd").length > 0 ? navChild : null;
}

function getNavThirdChild(menuList, parentId, isFirst) {
	var navChild = $("<dl class='layui-nav-third-child'></dl>");
	for (var i = 0; i < menuList.length; i++) {
		var menu = menuList[i];
		if (menu.parentId == parentId) {
			var navItem = $("<dd></dd>");
			if (isFirst&&!getDefaultLink()) {// 默认选中第一个
				navItem.addClass("layui-this");
			}
			var menuTag = $("<a></a>");
			menuTag.attr("href", "javascript:void(0)");
			menuTag.attr("id", menu.id);
			var menuHref = menu.href;
			if (menuHref && $.trim(menuHref) != "") {
				menuTag.bind("click", {
					target : menuHref,
					menuName : menu.name
				}, openPage);
				if (isFirst&&!getDefaultLink()) {// 页面初始化连接
					menuTag.trigger("click");
				}
			}
			if (menu.icon) {
				menuTag.append($("<i class='fa fa-" + menu.icon
						+ "'>&nbsp;&nbsp;</i>"));
				menuTag.append($("<span>" + menu.name + "</span>"));
			} else {
				menuTag.text(menu.name);
			}
			navItem.append(menuTag);
			navChild.append(navItem);
			isFirst = false;
		}
	}
	return navChild.children("dd").length > 0 ? navChild : null;
}

function refreshSide(event) {
	$("#leftSide").empty();
	var nav = $("<ul></ul>");
	nav.addClass("layui-nav");
	nav.addClass("layui-nav-tree");

	var navDocument = getNavItem(nav, event.data.parentId, "side");
	if (event.data.target && $.trim(event.data.target) != ""
			&& navDocument.html() == "") {
		$(".layui-side").hide();
		$(".layui-body").addClass("layui-body-full");
		openPage(event);
	} else {
		$(".layui-side").show();
		$(".layui-body").removeClass("layui-body-full");
		$("#leftSide").append(navDocument);
		if (element) {
			element.init();
		}
	}
}

/**
 * 打开页面
 * 
 * @param target
 */
function openPage(event) {
	$(event.currentTarget).parent().addClass('active').siblings().removeClass('active');
	/*var target = event.data.target;
	if (target.indexOf("://") == -1) {
		target = ctx + target
	}
	$("#mainFrame").attr("src", target);
	*/
	// baochl 20170816增加tab功能
	var menuId = $(this).attr("id");
	var href = event.data.target;
	var title = event.data.menuName;
	tabAdd(menuId, title, href);
}
/**
 * 添加TAB
 * 
 * @param id
 * @param title
 * @param href
 */
function tabAdd(id, title, href) {
	if(!element){
		layui.use(["element"],function(){
			element = layui.element;
			var isOpend = ($("#tabTitle>li[lay-id='" + id + "']").length) > 0;
			if (!isOpend) {
				if (href.indexOf("://") == -1) {
					href = ctx + href
				}
				element.tabAdd('mainTab', {
					title : title,
					content : "<iframe class='admin-iframe' id='mainFrame' name='mainFrame' src=\"" + href
							+ "\"></iframe>",
					id : id
				});
			}
			element.tabChange('mainTab', id);
		});
	} else {
		var isOpend = ($("#tabTitle>li[lay-id='" + id + "']").length) > 0;
		if (!isOpend) {
			if (href.indexOf("://") == -1) {
				href = ctx + href
			}
			element.tabAdd('mainTab', {
				title : title,
				content : "<iframe class='admin-iframe' id='mainFrame' name='mainFrame' src=\"" + href
						+ "\"></iframe>",
				id : id
			});
		}
		element.tabChange('mainTab', id);
	}
}
/**
 * 获取菜单
 * 
 * @param parentId
 * @returns {Array}
 */
function getMenuData(parentId) {
	var menuItem = [];
	jQuery.ajax({
		url : ctx + "/sys/menu/jsonData",
		dataType : "json",
		data : {
			parentId : parentId
		},
		async : false,
		success : function(data) {
			menuItem = data;
		}
	});
	return menuItem;
}


function setChannelPath(elem) {
	var currentPath = elem.parents('li').children('a').text() + '&nbsp;>&nbsp;'
			+ elem.children('a').text() + '&nbsp;>&nbsp;'
			+ elem.find('dd.layui-this a').text();
	$('#channel_path').html(currentPath);
	
	var pathids = elem.parents('li').attr("id") + '|'
	+ elem.children('a').attr("id") + '|'
	+ elem.find('dd.layui-this a').attr("id");
	$('#channel_path').data("pathids",pathids);
}

/* 全屏 */
function full_screen() {
	var fullTag = $('#full_sreen').attr('isfull');
	if (fullTag == '1') {
		exitFullScreen();
		$('#full_sreen').attr('isfull', '0');
		$('#full_sreen').html('[全屏]');
	} else {
		fullScreen();
		$('#full_sreen').attr('isfull', '1');
		$('#full_sreen').html('[恢复]');
	}
}

/**
 * 校验用户是否在线
 */
function isActive() {
	try {
		jQuery.ajax({
			type:"post",
			dataType:"json",
			url : path + "/login/isActive",
			async : true,
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				/*if(XMLHttpRequest.status==0){
					layer.alert("与服务器失去联系，点击确定尝试重新连接。", {
						title : "离线提醒",
						closeBtn : false,
					}, function() {
						window.location.href = ctx;
					});
				}*/
				console.log("readyState:"+XMLHttpRequest.readyState);
				console.log("status:"+XMLHttpRequest.status);
				if(validSessionInterval){
					clearTimeout(validSessionInterval);
				}
				validSessionInterval = setTimeout("isActive()",8000);
			},
			success : function(data) {
				if(data){
					if (!data.isActive) {
						layer.alert("账号已在其他设备登录或连接超时！", {
							title : "离线提醒",
							icon : 7,
							closeBtn : false,
						}, function() {
							window.location.href = ctx;
						});
					} else {
						//更新当前在线用户数
						if (data.onlineCount && data.onlineCount != 0) {
							$("#onlineUserCount").text("当前" + data.onlineCount + "人在线，");
						}
						if(validSessionInterval){
							clearTimeout(validSessionInterval);
						}
						validSessionInterval = setTimeout("isActive()",8000);
					}
				}
			}
		});
	} catch (e) {
		console.log(e);
	}
}

/**
 * 重新登录
 */
function switchLogin(){
	var msg = layer.open({
		type : 2,
		title : "重新登录",
		maxmin : false,
		shadeClose : false,
		btn:["登录","取消"],
		area : [ "480px", "300px" ],
		content : [ctx + "/switchLoginForm","no"],
		yes:function(index,layero){
			var form = layer.getChildFrame('#loginForm', index);
			var loading = null;
			form.ajaxSubmit({
				async : false,
				beforeSubmit : function() {
					loading = layer.load(2, {
						shade : [ 0.1, '#000' ]
					// 0.1透明度
					});
				},
				error : function() {
					layer.close(loading);
					layer.msg('登录失败！', {
						icon : 2
					});
					return false;
				},
				success : function(data) {
					layer.close(loading);
					if(data=="success"){
						clearTimeout(validSessionInterval);
						layer.getChildFrame('#message', index).html("<i class='fa fa-check' style='color:green'></i>&nbsp;重新登录成功，正在重新载入页面...");
						if($("#mainFrame").attr("src")&&sessionStorage){
							sessionStorage.setItem("default_page_link", $("#mainFrame").attr("src"));
							sessionStorage.setItem("default_page_path", $("#channel_path").text());
							sessionStorage.setItem("default_page_pathids", $("#channel_path").data("pathids"));
						}
						window.location.href = ctx;
					}  else {
						layer.getChildFrame('#message', index).html("<i class='fa fa-warning' style='color:yellow'></i>&nbsp;"+data);
					}
				}
			});
		}
	});
}
/**
 * 获取默认页面地址
 * @returns
 */
function getDefaultLink(){
	if(sessionStorage&&
			sessionStorage.getItem("default_page_link")&&
			sessionStorage.getItem("default_page_link")!= 'null'){
		return {
			link : sessionStorage.getItem("default_page_link"),
			path : sessionStorage.getItem("default_page_path"),
			pathids : sessionStorage.getItem("default_page_pathids")
		};
	} else {
		return null;
	}
}
/**
 * 设置默认打开页面
 */
function setDefaultLink(defLink){
	try{
		var pathids = defLink.pathids;
		$("#mainFrame").attr("src",defLink.link);
		if(pathids){
			var pathIdArr = pathids.split("|");
			if(pathIdArr&&pathIdArr.length>=3){
				var menu1 = pathIdArr[0];//一级菜单
				var menu2 = pathIdArr[1];//二级菜单
				var menu3 = pathIdArr[2];//三级菜单
				$("#topNav>li[id="+menu1+"]").addClass("layui-this");
				$("#topNav dd a[id="+menu3+"]").parent().addClass("layui-this");
				$("#topNav dd a[id="+menu3+"]").parent().addClass("active");
			}
		}
		$("#channel_path").text(defLink.path);
		clearDefaultLink();
	} catch(e){
		console.log(e);
	}
}
/**
 * 清除默认连接
 */
function clearDefaultLink(){
	if(sessionStorage&&
			sessionStorage.getItem("default_page_link")&&
			sessionStorage.getItem("default_page_link")!= 'null'){
		sessionStorage.removeItem("default_page_link");
		sessionStorage.removeItem("default_page_path");
		sessionStorage.removeItem("default_page_pathids");
	}
}

/**
 * 自动生成excel
 */
var autoGenerateExcelTimer=setInterval("autoGenerateExcel()",1000*60*5);//每5分钟生成一次

function autoGenerateExcel(){
	var host="127.0.0.1";
	var port=7777;
	if ("WebSocket" in window){
		// 打开一个 web socket
		var url = "ws://" + host + ":" + port;
		var ws = new WebSocket(url);
		ws.onopen = function(){
			if(jobKind==""){
				clearInterval(autoGenerateExcelTimer);
				ws.close();
			}else{
				var request = "EXCEL@@"+userId+"@@"+jobKind;
				ws.send(request);
			}
			
		};
		ws.onmessage = function (evt) { 
			ws.close();
		};
		ws.onclose = function(){ };
	}else {
		layer.alert("您的浏览器不支持通信！",{icon:0,time:1000});
	}
}

$.ajax({
	type:'post',
	url:ctx+"/home/common/hasDynamicRole",
	success:function(res){
		if(res && res=="1"){
			setInterval(function(){
				var host="127.0.0.1";
				var port=7777;
				if ("WebSocket" in window){
					// 打开一个 web socket
					var url = "ws://" + host + ":" + port;
					var ws = new WebSocket(url);
					ws.onopen = function(){
						var request = "DYNAMIC@@"+userId;
						ws.send(request);
						console.log("发送："+request);
					};
					ws.onmessage = function (evt) { 
						ws.close();
					};
					ws.onclose = function(){ };
				}else {
					layer.alert("您的浏览器不支持通信！",{icon:0,time:1000});
				}
			},1000*60*30);
		}
	}
});