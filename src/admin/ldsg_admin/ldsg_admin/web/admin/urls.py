#!/usr/bin/python
# -*- coding: utf-8 -*-

from django.conf.urls.defaults import patterns, url

urlpatterns = patterns('web.admin',
    url(r'^server_list/$', 'views.server_list', name='server-list'),
    url(r'^sql-query/$', 'views.sql_query', name='sql-query'),
    url(r'^package_list/$', 'views.package_list', name='package-list'),
    url(r'^black_imei_list/$', 'views.black_imei_list', name='black-imei-list'),
    url(r'^package_list/$', 'views.package_list', name='package-list'),
    url(r'^partner_list/$', 'views.partner_list', name='partner-list'),
    url(r'^user_list/$', 'views.user_list', name='user-list'),
    url(r'^page_list/$', 'views.page_list', name='page-list'),
    url(r'^user_page_list/$', 'views.user_page_list', name='user-page-list'),
    url(r'^giftcode_list/$', 'views.giftcode_list', name='giftcode-list'),
    url(r'^server_partner_list/$', 'views.server_partner_list', name='server-partner-list'),
    url(r'^game_web_status_list/$', 'views.game_web_status_list', name='game-web-status-list'),
    url(r'^white_ip_list/$', 'views.white_ip_list', name='white-ip-list'),
    url(r'^system_notice_list', 'views.system_notice_list', name='system-notice-list'),
    url(r'^sync_system_table', 'views.sync_system_table', name='sync-system-table'),
    url(r'^payment_order_list', 'views.payment_order_list', name='payment-order-list'),
)
