#!/usr/bin/python
# -*- coding: utf-8 -*-

from django.conf.urls.defaults import patterns, url

urlpatterns = patterns('web.system',
    url(r'^client_list/$', 'views.client_list', name='client-list'),
    url(r'^client_run_log_list/$', 'views.client_run_log_list', name='client-run-log-list'),
    url(r'^battle_client_list/$', 'views.battle_client_list', name='battle-client-list'),
)
