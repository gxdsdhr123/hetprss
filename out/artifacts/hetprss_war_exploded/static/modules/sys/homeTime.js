function refreshTime() {
	var time = new Date();
	var month = time.getMonth() + 1;
	var year = time.getFullYear();
	var day = time.getDate();
	var week = time.getDay();
	var hours = time.getHours();
	var seconds = time.getSeconds();
	switch (week) {
	case 1:
		week = "星期一";
		break;
	case 2:
		week = "星期二";
		break;
	case 3:
		week = "星期三";
		break;
	case 4:
		week = "星期四";
		break;
	case 5:
		week = "星期五";
		break;
	case 6:
		week = "星期六";
		break;
	case 0:
		week = "星期日";
		break;
	default:
		week = "";
	}

	if (Number(hours) < 10) {
		hours = "0" + hours;
	}
	var min = time.getMinutes();
	if (Number(min) < 10) {
		min = "0" + min;
	}
	if (Number(seconds) < 10) {
		seconds = "0" + seconds;
	}
	$("#currentTime").html(
			year + "年" + month + "月" + day + "日&nbsp;" + week
					+ "&nbsp;<font style='font-size:16px;font-weight:bold'>"
					+ hours + ":" + min + ":" + seconds + "</font>");
}