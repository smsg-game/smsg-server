#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
处理web访问异常
"""
from django.http import HttpResponseServerError, HttpResponseRedirect
from ldsg_admin.common import exception_mgr
from ldsg_admin.common import get_logging
from ldsg_admin.config import DEBUG

ERROR_500 = """<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title> Server error (500) </title>
</head>

<body>
<h1>Server Error <em>(500)</em></h1>
<p>There's been an error during visting webauth %r. It's been reported to the site administrators via e-mail and should be fixed shortly. Thanks for your patience.</p>
</body>
</html>"""

class ExceptionMiddleware(object):
    """Middleware that check user channel."""

    def process_exception(self, request, e):
        """处理异常，将异常记录到日志文件"""
        global ERROR_500
        full_path = request.get_full_path()
        s = exception_mgr.on_except('request url: %r' % full_path, 2)
        logging = get_logging()
        logging.error(s)
        if not DEBUG:
            #return HttpResponseServerError(ERROR_500 % full_path)
            return HttpResponseRedirect("/error")
        return None
    