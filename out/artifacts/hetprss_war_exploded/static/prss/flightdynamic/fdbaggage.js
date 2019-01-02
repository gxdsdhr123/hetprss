$(function() {
	loadData("luggage", "B");
	loadData("goods", "C");
	loadData("email", "M");
	loadSpecialData();
	loadSum();
})
// 包含货物种类的tr名称，不同种类的数据
function loadData(trName, kindCode) {
	var tr = $("tr[name= '" + trName + "']");
	var inlength = 0, outlength = 0;
	var inKindArr=[], outKindArr=[];
	for (var i = 0; i < inArray.length; i++) {
		if (inArray[i].kind == kindCode) {
			inlength++;
			inKindArr.push(inArray[i]);
		}
	}
	for (var i = 0; i < outArray.length; i++) {
		if (outArray[i].kind == kindCode) {
			outlength++;
			outKindArr.push(outArray[i]);
		}
	}
	// rowspan 取最大的
	var length = inlength > outlength ? inlength : outlength;
	if (length!=0) {
		tr.find(".tdTitle").attr("rowspan", length+"");
	}

	for (var i = 0; i < length; i++) {
		var dataTr;
		if (i == 0) {
			dataTr = tr;
			dataTr.find(".tdTitle").siblings().remove();
		} else {
			dataTr = $("<tr></tr>");
		}
		var inshippingTd = inKindArr[i]&&inKindArr[i].shipping ? $("<td>" + inKindArr[i].shipping + "</td>") : $("<td></td>");
		var incontainerTd = inKindArr[i]&&inKindArr[i].container ? $("<td>" + inKindArr[i].container + "</td>") : $("<td></td>");
		var inweightTd = inKindArr[i]&&inKindArr[i].weight ? $("<td>" + inKindArr[i].weight + "</td>") : $("<td></td>");
		var outshippingTd = outKindArr[i]&&outKindArr[i].shipping ? $("<td>" + outKindArr[i].shipping + "</td>") : $("<td></td>");
		var outcontainerTd = outKindArr[i]&&outKindArr[i].container ? $("<td>" + outKindArr[i].container + "</td>") : $("<td></td>");
		var outweightTd = outKindArr[i]&&outKindArr[i].weight ? $("<td>" + outKindArr[i].weight + "</td>") : $("<td></td>");

		dataTr.append(inshippingTd);
		dataTr.append(incontainerTd);
		dataTr.append(inweightTd);
		dataTr.append(outshippingTd);
		dataTr.append(outcontainerTd);
		dataTr.append(outweightTd);
		if (i != 0) {
			$("tr[name='"+trName+"Sum']").before(dataTr);
		}
	}
}

function loadSpecialData() {
	var inSpecialArr=[], outSpecialArr=[];
	var tr = $("tr[name= 'specialCargo']");
	for (var i = 0; i < inArray.length; i++) {
		if (inArray[i].kind != 'B' && inArray[i].kind != 'C'&& inArray[i].kind != 'M') {
			inSpecialArr.push(inArray[i]);
		}
	}
	for (var j = 0; j < outArray.length; j++) {
		if (outArray[j].kind != 'B' && outArray[j].kind != 'C'&& outArray[j].kind != 'M') {
			outSpecialArr.push(outArray[j]);
		}
	}
	var outsArr = outSpecialArr;
	for (var a = 0; a < inSpecialArr.length; a++) {
		for (var b = 0; b < outSpecialArr.length; b++) {
			if (inSpecialArr[a].kind == inSpecialArr[b].kind) {
				var dataTr = $("<tr></tr>");
				var inshippingTd = inSpecialArr[a]&&inSpecialArr[a].shipping ? $("<td>" + inSpecialArr[a].shipping + "</td>") : $("<td></td>");
				var incontainerTd = inSpecialArr[a]&&inSpecialArr[a].container ? $("<td>" + inSpecialArr[a].container + "</td>") : $("<td></td>");
				var inweightTd = inSpecialArr[a]&&inSpecialArr[a].weight ? $("<td>" + inSpecialArr[a].weight + "</td>") : $("<td></td>");
				var outshippingTd = outSpecialArr[b]&&outSpecialArr[b].shipping ? $("<td>" + outSpecialArr[b].shipping + "</td>") : $("<td></td>");
				var outcontainerTd = outSpecialArr[b]&&outSpecialArr[b].container ? $("<td>" + outSpecialArr[b].container + "</td>") : $("<td></td>");
				var outweightTd = outSpecialArr[b]&&outSpecialArr[b].weight ? $("<td>" + outSpecialArr[b].weight + "</td>") : $("<td></td>");
				var kindTd=inSpecialArr[a]&&inSpecialArr[a].kind ? $("<td>" + inSpecialArr[a].kind + "</td>") : $("<td></td>");
				dataTr.append(kindTd);
				dataTr.append(inshippingTd);
				dataTr.append(incontainerTd);
				dataTr.append(inweightTd);
				dataTr.append(outshippingTd);
				dataTr.append(outcontainerTd);
				dataTr.append(outweightTd);
				$("table").append(dataTr);
				outsArr.splice(b, 1);
			}
		}
	}

	for (var c = 0; c < outsArr.length; c++) {
		var dataTr = $("<tr></tr>");
		var inshippingTd = $("<td></td>");
		var incontainerTd = $("<td></td>");
		var inweightTd = $("<td></td>");
		var outshippingTd = outsArr[c]&&outsArr[c].shipping ? $("<td>" + outsArr[c].shipping + "</td>") : $("<td></td>");
		var outcontainerTd = outsArr[c]&&outsArr[c].container ? $("<td>" + outsArr[c].container + "</td>") : $("<td></td>");
		var outweightTd = outsArr[c]&&outsArr[c].weight ? $("<td>" + outsArr[c].weight + "</td>") : $("<td></td>");
		var kindTd=outsArr[c]&&outsArr[c].kind ? $("<td>" + outsArr[c].kind + "</td>") : $("<td></td>");
		dataTr.append(kindTd);
		dataTr.append(inshippingTd);
		dataTr.append(incontainerTd);
		dataTr.append(inweightTd);
		dataTr.append(outshippingTd);
		dataTr.append(outcontainerTd);
		dataTr.append(outweightTd);
		$("table").append(dataTr);
	}
}

function loadSum(){
	var luggageSum1=0,goodsSum1=0,emailSum1=0;
	var luggageSum2=0,goodsSum2=0,emailSum2=0;
	var ltr = $("tr[name= 'luggage']");
	luggageSum1+=parseInt(ltr.find("td").eq(3).text());
	luggageSum2+=parseInt(ltr.find("td").eq(6).text());
	while (ltr.next()&&ltr.next().attr("name")!="luggageSum") {
		luggageSum1+=parseInt(ltr.next().find("td").eq(2).text());
		luggageSum2+=parseInt(ltr.next().find("td").eq(5).text());
		ltr=ltr.next();
	}
	var gtr = $("tr[name= 'goods']");
	goodsSum1+=parseInt(gtr.find("td").eq(3).text());
	goodsSum2+=parseInt(gtr.find("td").eq(6).text());
	while (gtr.next()&&gtr.next().attr("name")!="goodsSum") {
		goodsSum1+=parseInt(gtr.next().find("td").eq(2).text());
		goodsSum2+=parseInt(gtr.next().find("td").eq(5).text());
		gtr=gtr.next();
	}
	var etr = $("tr[name= 'email']");
	emailSum1+=parseInt(etr.find("td").eq(3).text());
	emailSum2+=parseInt(etr.find("td").eq(6).text());
	while (etr.next()&&etr.next().attr("name")!="emailSum") {
		emailSum1+=parseInt(etr.next().find("td").eq(2).text());
		emailSum2+=parseInt(etr.next().find("td").eq(5).text());
		etr=etr.next();
	}
	$("tr[name='luggageSum']").find("td").eq(1).text(isNaN(luggageSum1)?'0':luggageSum1);
	$("tr[name='luggageSum']").find("td").eq(2).text(isNaN(luggageSum2)?'0':luggageSum2);
	$("tr[name='goodsSum']").find("td").eq(1).text(isNaN(goodsSum1)?'0':goodsSum1);
	$("tr[name='goodsSum']").find("td").eq(2).text(isNaN(goodsSum2)?'0':goodsSum2);
	$("tr[name='emailSum']").find("td").eq(1).text(isNaN(emailSum1)?'0':emailSum1);
	$("tr[name='emailSum']").find("td").eq(2).text(isNaN(emailSum2)?'0':emailSum2);
	
}



