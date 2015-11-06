var GlobalFunction = (function() {
	var $ = jQuery;
	
	var _loadedFunction = [];
	
	$(document).ready(function() {
		for(var i = 0; i < _loadedFunction.length; i++) {
			var f = _loadedFunction[i];
			try {
				f();
			} catch (e) {
				e;
			}
		};
	});
	
	
	return {
		/**
		 * 设定body加载时的启动函数
		 *
		 * @param {Function} f
		 */
		main : function(f) {
			var arr = $.makeArray(arguments);
			_loadedFunction = _loadedFunction.concat(arr);
		},
		
		closure : function(f, scope, args) {
			if (f) {
				return f.apply(scope || window, args || []);
			}
		},
		
		extend : function(destination, source, pfilter, vfilter) {
			for (var property in source) {
				if (source[property] == null || source[property].dontEnum != true) {
					var _property = (pfilter ? pfilter(property, source,
							destination) : property);
					if (_property == true || typeof _property == 'string') {
						var _value = (vfilter ? vfilter(source[property], property,
								source, destination) : source[property]);
						destination[_property] = _value;
					};
				};
			};
			return destination;
		},	
		
		each : function(iterable, filter, iterator) {
			if (iterable instanceof Array && iterable.length != null) {
				var size = iterable.length;
				for (var i = 0; i < size; i++) {
					var isIterate = filter ? filter(i, iterable) : true;
					if (isIterate) {
						iterator(iterable[i], i, iterable);
					};
				};
			} else {
				var temp = {};
				this.extend(temp, iterable, filter, iterator);
				temp = null;
				delete temp;
			};
		},
		
		wordEllipsis : function(w, l) {
			if (w.length > l) {
				var w = w.substr(0, l - 3) + '...';
			};
			return w;
		},
		
		adjustIframe : function(name,iElement,fixXY) {
			var iDoc = window[name].document.documentElement;
			if (fixXY.indexOf('x') > -1) {
				iElement.width = parseInt(iDoc.scrollWidth) + '';
			};
			if (fixXY.indexOf('y') > -1) {
				iElement.height = parseInt(iDoc.scrollHeight) + '';
			};
		},
		
		checkAndRedirect : function(type, url, errmsg) {
			switch(type) {
				case 'ie':
					if (document.all) {
						window.open(url);
					} else {
						alert(errmsg);
					};
					break;
				default:
					break;
			};
		},
		
		parseQuery : function(url) {
			var q = (/^[^?]+\?(.*)$/.exec(url) || [])[1];
			var r = {};			
			if (q) {
				for (var i = 0, qs = q.split('&'); i < qs.length; i++) {
					var keyValue = /([^=]*)=(.*)/.exec(qs[i]);
					r[keyValue[1]] = keyValue[2];
				}
			};
			return r;
		}
	}
	
})();

var F = GlobalFunction;



