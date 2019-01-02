var scrollObjs = {};
var scrollArr = [];

// 甘特图滚动
function gtScrollbar(){
	$('#SJgantt').each(function(){
		$(this).css('position' , 'relative');
		var s = new PerfectScrollbar(this);
		scrollArr.push(s);
		scrollObjs['SJgantt'] = s;
	});
}

// 表格滚动
function tableScrollbar(){
	$('table').each(function(){
		$(this).on('load-success.bs.table',function(thisObj){
			var tableBody = $(thisObj.currentTarget).parents('.fixed-table-body');
			tableBody.css('position' , 'relative');
			var s = new PerfectScrollbar(tableBody[0]);
			scrollArr.push(s);
			if(this.id)
				scrollObjs[this.id] = s;
		}).on('load-error.bs.table',function(thisObj){
			var tableBody = $(thisObj.currentTarget).parents('.fixed-table-body');
			tableBody.css('position' , 'relative');
			var s = new PerfectScrollbar(tableBody[0]);
			scrollArr.push(s);
			if(this.id)
				scrollObjs[this.id + '_error'] = s;
		}).on('editable-save.bs.table',function(e,field, row, oldValue, $el){
			try {
				// editable  select2 锁死滚动条BUG
				var select2_id = $el.next()
						.find('.select2-selection__rendered').attr('id');
				select2_id = select2_id.substr(0, select2_id.lastIndexOf('-'));
				var scrollEvent = 'scroll.select2.' + select2_id;
				var resizeEvent = 'resize.select2.' + select2_id;
				var orientationEvent = 'orientationchange.select2.'
						+ select2_id;
				$('.fixed-table-body').off(scrollEvent);
				$(window).off(
						scrollEvent + ' ' + resizeEvent + ' '
								+ orientationEvent);
			} catch (e) {
				//console.log('no select2');
			}
		});
	});
}

// 下拉框滚动
function selectScrollbar(){
	setTimeout(function(){
		$('.layui-form-select').each(function(){
			var s = new PerfectScrollbar($(this).find('.layui-anim')[0]);
			scrollArr.push(s);
			var select_input = $(this).prev();
			if(select_input.attr('id') || select_input.attr('name')){
				scrollObjs[select_input.attr('id') || select_input.attr('name')] = s;
			}
		});
		$('.select2').on('click',function(){
			$('.select2-results__options').each(function(){
				$(this).css('position' , 'relative');
				var s = new PerfectScrollbar(this);
				scrollArr.push(s);
				if(this.id)
					scrollObjs[this.id] = s;
			});
		});
	},1000);
}

//iframe滚动
var iframe_ps;
function iframeScroll(){
	// iframe增加滚动条
	//alert($(window.frames["mainFrame"].document).height());
	$("iframe").contents().find('body').each(function(){
		$(this).height(window.frames[0].document);
		$(this).css('position' , 'relative');
		if(iframe_ps){
			iframe_ps.destroy();
			iframe_ps = null;
		}
		iframe_ps = new PerfectScrollbar($(this).parent()[0]);
	});
}

//弹窗内容滚动条
function modelScrollbar(){
	setTimeout(function(){
		$('.layui-layer-content').each(function(){
			scrollArr.push(new PerfectScrollbar(this));
		});
	},400)
}


//修改滚动条
$(function(){
	// 下拉框
	selectScrollbar();
	$('body').on('layui.layer.open',function(){
		// 弹出层
		modelScrollbar();
		// 下拉框
		selectScrollbar();
		// 甘特图
		//gtScrollbar();
		// iframe滚动条
		iframeScroll();
		$('iframe').on('load',iframeScroll).on('resize',iframeScroll);
	});
	// 弹出层最大化时更新滚动条
	/*$('body').on('layui.layer.full',function(){
		$.each(scrollArr,function(i,item){
			item.update();
		})
	});*/
	// 甘特图
	//gtScrollbar();
	// 表格
	tableScrollbar();
	
	// iframe滚动条
	iframeScroll();
	$('iframe').on('load',iframeScroll).on('resize',iframeScroll);
});





/**
 * 重置框架的滚动条
 * @param options 重置后的参数
 * @param time 重置延迟时间
 */
function resetScrollBar(options , time){
	setTimeout(function(){
		if(iframe_ps){
			iframe_ps.destroy();
			iframe_ps = null;
		}
		$("iframe").contents().find('body').each(function(){
			iframe_ps = new PerfectScrollbar($(this).parent()[0],options);
		});
	},time);
}