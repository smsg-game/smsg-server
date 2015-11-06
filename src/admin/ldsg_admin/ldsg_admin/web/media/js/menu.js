$(document).ready(function() {
	
	//slides the element with class "menu_body"
	$("#menu p.menu_head").click(function() {
	     $(this).css({backgroundImage:"url(/site_media/images/down.gif)",
	    	 		  backgroundColor:"#C3D9FF",
	    	 		  fontWeight:"bold"}).next("div.menu_body").slideDown(100).siblings("div.menu_body").slideUp("slow");
         $(this).siblings('p.menu_head').css({backgroundImage:"url(/site_media/images/right.gif)",
        	 backgroundColor:"#DDDDDD", fontWeight:"normal"});
	});
	
	var current_path = window.location.pathname;
	$('div.menu_body a').each(function() {
		href = $(this).attr('href');
		if(href == current_path || current_path.indexOf(href) > -1) {
			$(this).parent().prev().click()
			$(this).css({backgroundColor:"#E0ECFF", fontWeight:"bold"});
		}
	});
	
	$("#menu_bar").click(function() {
		if($("#menu_bar").hasClass('hided')) {
			$("#menu").show();
			$("#content").css({marginLeft: $("#content").attr("old_marginLeft")});
			$(this).removeClass("hided");
		} else {
			$("#menu").hide();
			var ml = $("#content").css("marginLeft");
			$("#content").attr("old_marginLeft", ml);
			$("#content").css({marginLeft: 0});
			$(this).addClass("hided");
		}
	});
});
