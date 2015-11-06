#!/usr/bin/python
# -*- coding: utf-8 -*-

from django.conf.urls.defaults import patterns, url

urlpatterns = patterns('web.manager',
    url(r'^user_info', 'views.user_info', name='user-info'),
    url(r'^stat_server', 'views.stat_server', name='stat-server'),
    url(r'^country-server', 'views.stat_server_country', name='country-server'),
    url(r'^user_hero_info', 'views.user_hero_info', name='user-hero-info'),
    url(r'^user_package_info', 'views.user_package_info', name='user-package-info'),
    url(r'^user_purchase_info', 'views.user_purchase_info', name='user-purchase-info'),
    url(r'^user_mail_list', 'views.user_mail_list', name='user-mail-list'),
    url(r'^currdate_register', 'views.currdate_register', name='currdate-register'),
    url(r'^stat_liucun', 'views.stat_liucun', name='stat-liucun'),
    url(r'^server_liucun', 'views.stat_liucun_server', name='server-liucun'),
    url(r'^activity_list', 'views.activity_list', name='activity-list'),
    url(r'^system_exchange_list', 'views.system_exchange_list', name='system-exchange-list'),
    url(r'^system_exchange_detail_list', 'views.system_exchange_detail_list', name='system-exchange-detail-list'),
    url(r'^system_exchange_sync', 'views.system_exchange_sync', name='system-exchange-sync'),
    url(r'^once_pay_reward_list', 'views.system_pay_reward_list', {"reward_type": 1}, name='system-pay-reward-once-list'),
    url(r'^system_mall_discount_list', 'views.system_mall_discount_list', name='system-mall-discount-list'),
    url(r'^system_mall_discount_detail_list', 'views.system_mall_discount_detail_list', name='system-mall-discount-detail-list'),
    url(r'^system_mall_discount_sync', 'views.system_mall_discount_sync', name='system-mall-discount-sync'),
    url(r'^total_pay_reward_list', 'views.system_pay_reward_list', {"reward_type": 2}, name='system-pay-reward-total-list'),
    url(r'^system_pay_reward_detail_list', 'views.system_pay_reward_detail_list', name='system-pay-reward-detail-list'),
    url(r'^system_pay_reward_sync', 'views.system_pay_reward_sync', name='system-pay-reward-sync'),
    url(r'^system_mail_list', 'views.system_mail_list', name='system-mail-list'),
    url(r'^system_mail_approve_list', 'views.system_mail_approve_list', name='system-mail-approve-list'),
    url(r'^system_mail_create', 'views.system_mail_create', name='system-mail-create'),
    url(r'^system_mail_approve', 'views.system_mail_approve', name='system-mail-approve'),
    url(r'^notice_list', 'views.notice_list', name='notice-list'),
    
    
    
    #log
    url(r'^user_gold_use_log', 'log_views.user_gold_use_log', name='user-gold-use-log'),
    url(r'^user_tool_use_log', 'log_views.user_tool_use_log', name='user-tool-use-log'),
    url(r'^user_payment_log', 'log_views.user_payment_log', name='user-payment-log'),
    url(r'^user_hero_log', 'log_views.user_hero_log', name='user-hero-log'),
    url(r'^user_level_up_log', 'log_views.user_level_up_log', name='user-level-up-log'),
    url(r'^forces_list', 'views.forces_list', name='forces-list'),
)
