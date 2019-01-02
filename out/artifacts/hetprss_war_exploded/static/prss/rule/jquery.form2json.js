/*
 * 将form表单转换为json字符串
 * create by 许辰
 */
;
(function($) {
	"use strict";

	$.fn.form2json = function() {
		var o = {};
		var a = this.serializeArray();
		$.each(a, function() {
			if (o[this.name] !== undefined) {
				if (!o[this.name].push) {
					o[this.name] = [ o[this.name] ];
				}
				o[this.name].push(this.value || '');
			} else {
				o[this.name] = this.value || '';
			}
		});
		var r = '';
		if(JSON){ //ie8(兼容模式),ie7和ie6没有JSON对象，推荐采用JSON官方的方式，引入json.js
			r = JSON.stringify(o);
		}else{
			alert("您的浏览器不支持JSON对象，请更换高版本的浏览器！");
		}
		return r;
	};
})(jQuery);