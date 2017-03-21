$(document).ready(function() {
	$("#searchkw").focus(function() {
		if ($("#searchkw").val() == "输入关键字，搜啦一下") {
			$("#searchkw").val("");
			$("#searchkw").css("color", "#628e03");
		}
	})

	$("#searchkw").blur(function() {
		if ($("#searchkw").val() == "") {
			$("#searchkw").val("输入关键字，搜啦一下");
			$("#searchkw").css("color", "#949494");
		}
	})

	$(".search-type li").click(function() {
		$(".search-type li a").removeClass("current");
		$(this).find("a").addClass("current");
		$("#searchType").val($(this).attr("type"));
	})
	$(".flist li").hover(function() {
		$(this).addClass("overli");
	}, function() {
		$(this).removeClass("overli");
	});

	$('#tabs_gn').click(function() {
		$('#s_gn').show();
		$('#s_gw').hide();
		$('#op').val('gn');
		$('#wp').val(0);
	});

	$('#tabs_gw').click(function() {
		$('#s_gw').show();
		$('#s_gn').hide();
		$('#op').val('gw');
		$('#wp').val(16);
	});
});

function g(a) {
	return document.getElementById(a)
};
function switchTab(a) {
	var a = document.getElementById('tabs_gw');
	a.show();
};
function switchOptiontList(a, b) {
	var c = document.getElementsByName("search_op");
	for (var d = 0; d < c.length; d++) {
		if (c[d].style.display != "none") {
			c[d].style.display = "none"
		}
	}
	;
	var e = g("s_" + a);
	if (e) {
		e.style.display = 'inline-block';
		e.style.position = 'relative';
		e.style.position = 'absolute';
		if (b != "") {
			for (var f = 0; f < e.options.length; f++) {
				if (e.options[f].value == b) {
					e.options[f].selected = true;
					break
				}
			}
		}
		;
		changeWp(e.options[e.selectedIndex].value)
	}
	;
	g("op").value = b;
	g("ty").value = a
};
function changeWp(a) {
	var b = new Array();
	b["gn"] = "0";
	b["115"] = "1";
	b["rayfile"] = "2";
	b["360yun"] = "3";
	b["kuaichuan"] = "4";
	b["yimuhe"] = "5";
	b["dbank"] = "6";
	b["vdisk"] = "7";
	b["yunfile"] = "8";
	b["qiannao"] = "9";
	b["weiyun"] = "10";
	b["kuaipan"] = "11";
	b["ctdisk"] = "12";
	b["everbox"] = "13";
	b["qjwm"] = "14";
	b["baipan"] = "15";
	b["gw"] = "16";
	b["hotfile"] = "17";
	b["rapidshare"] = "18";
	b["oron"] = "19";
	b["uploaded"] = "20";
	b["easy-share"] = "21";
	b["uploading"] = "22";
	b["turbobit"] = "23";
	b["fileserve"] = "24";
	b["enterupload"] = "25";
	b["music"] = "26";
	b["video"] = "27";
	b["zhan"] = "28";
	b["docs"] = "29";
	b["xuanfeng"] = "30";
	b["wps"] = "100";
	if (b[a]) {
		g("wp").value = b[a];
		g("op").value = a
	} else {
		g("wp").value = b["gn"]
	}
};
function op_select(a) {
	changeWp(a.value)
};

var _hmt = _hmt || [];
(function() {
	var hm = document.createElement("script");
	hm.src = "//hm.baidu.com/hm.js?03c066698edc5ff57d5b861beaf89d24";
	var s = document.getElementsByTagName("script")[0];
	s.parentNode.insertBefore(hm, s);
})();
