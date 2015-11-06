var UrlParser = function(total,size) {
	this.total = total;
	this.size = size;
	this.pageTotal = total % size > 0 ? parseInt(total / size) + 1 : parseInt(total / size);
	this.result = this.parse();
	this.baseUrl = this.genBaseUrl();
};
UrlParser.prototype.parse = function() {
	var url = document.baseURI || document.URL;
	var ret = {},
		reg = /^(\w+):\/\/([^\/:]+):?(\d*)?([^#\?]*)(\?[^#]*)?#?(\w+)?/g;
		url.replace(reg, function(all,protocol,host,port,path,params,hash){
		ret.protocol = protocol;
		ret.host = host;
		ret.port = port||80;
		ret.path = path;
		ret.query=params;
		ret.hash=hash;
     
		(function(){
			if(params===undefined)return;
			var o = {},
				seg = params.replace(/^\?/,'').split('&'),
				len = seg.length,
				i = 0,
				s;
			for (;i<len;i++){
				if (!seg[i]){ continue; }
				s = seg[i].split('=');
				o[s[0]] = s[1];
			}
			ret.params=o;
		})();
	});
	return ret;
};
UrlParser.prototype.genBaseUrl = function() {
	var obj = this.result;
	var s = obj.protocol + '://' + obj.host + ':' + obj.port + obj.path;
	var s1 = '';
	var i = 0;
	for (var key in obj.params) {
		if (key == 'page')
			continue;
		if (i++ > 0)
			s1 += '&';
		s1 += key + '=' + obj.params[key];
	}
	if (s1.length != 0)
		s1 = '?' + s1;
	s += s1;
	return s;
};
UrlParser.prototype.genUrl = function(page) {
	if (this.baseUrl.indexOf('?') == -1)
		return this.baseUrl + '?page=' + page;
	else
		return this.baseUrl + '&page=' + page;
};
UrlParser.prototype.getPrevPageUrl = function() {
	var page = this.getCurrPage();
	if (page > 1)
		page = page - 1;
	return this.genUrl(page);
};
UrlParser.prototype.getNextPageUrl = function() {
	var page = this.getCurrPage();
	if (page < this.pageTotal)
		page += 1;
	return this.genUrl(page);
};
UrlParser.prototype.getFirstPageUrl = function() {
	return this.genUrl(1);
};
UrlParser.prototype.getLastPageUrl = function() {
	return this.genUrl(this.pageTotal);
};
UrlParser.prototype.getCurrPage = function() {
	if (this.result.params&&this.result.params.page)
		return parseInt(this.result.params.page);
	else
		return 1;
};
function genPageLinks(total, size) {
	var u = new UrlParser(total, size);
	if (u.pageTotal > 1) {
		var s = '第'+u.getCurrPage() + '页|共' + u.pageTotal + '页&nbsp;';
		if(u.getCurrPage() != 1){
		    s += '<a href="'+u.getFirstPageUrl() + '">首页</a>&nbsp;<a href="'+u.getPrevPageUrl()+ '">上一页</a>&nbsp;';
		}else{
			s += '首页&nbsp;上一页&nbsp;';
		}
		if(u.getCurrPage() != u.pageTotal){
			s += '<a href="'+u.getNextPageUrl()+'">下一页</a>&nbsp;<a href="'+u.getLastPageUrl()+'">尾页</a>';
		}else{
			s += '下一页&nbsp;尾页';
		}
		
		$('#pageLink').html(s);
		$('#pageLink').parent().show();
	}
}