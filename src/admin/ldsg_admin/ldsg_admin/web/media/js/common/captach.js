//javascript document

var Captach = new function() {
    var $ = jQuery;
    DEFAULT_THEME = typeof(DEFAULT_THEME) == 'undefined'? '' : DEFAULT_THEME;
	var CAPTACH_EL = '.captchaimg';
	var CAPTACH_IMG = CONSTANTS.URI.CAPTCHA.IMG;
	
	var firstCaptach = false;
	
	F.main(function() {
		$(CAPTACH_EL).hide();
	});
	
	this.setFirstCaptach = function(value) {
		firstCaptach = value;
	};
	
	this.getCaptcha = function() {
		firstCaptach = true;
		$(CAPTACH_EL).attr("src", DEFAULT_THEME + "/images/common/loading.gif").show();
		$.get(CAPTACH_IMG, 'timestamp=' + new Date().getTime(), function(data) {
			$(CAPTACH_EL).attr("src", data.img).show();
		}, 'json');
	};
	
	this.getFirstCaptcha = function() {
		if (!firstCaptach) {
			this.getCaptcha();
		};
	};
	
	this.hideCaptcha = function() {
		$(CAPTACH_EL).hide();
	};
	
	this.showCaptcha = function() {
		$(CAPTACH_EL).show();
	};
};