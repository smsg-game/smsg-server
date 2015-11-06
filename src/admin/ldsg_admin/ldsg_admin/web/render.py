#!/usr/bin/python
# -*- coding: utf-8 -*-
"""http render shortcuts"""

from django.shortcuts import render_to_response as _render_to_response
from django.template import RequestContext
from django.http import HttpResponse, HttpResponseRedirect

from ldsg_admin.common import json


def render_to_response(request, template_name, dictionary=None, context_instance=None, mimetype=None):
    if context_instance is None:
        context_instance = RequestContext(request)
    return _render_to_response(template_name, dictionary, context_instance=context_instance, mimetype=mimetype)

def json_response(data, ensure_ascii=True):
    return HttpResponse(json.dumps(data, ensure_ascii))

def redirect(url):
    return HttpResponseRedirect(url)