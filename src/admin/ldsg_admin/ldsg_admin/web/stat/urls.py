#!/usr/bin/python
# -*- coding: utf-8 -*-

from django.conf.urls.defaults import patterns, url

urlpatterns = patterns('web.stat',
    url(r'^user_level_stat', 'views.user_level_stat', name='user-level-stat'),
    url(r'^user_level_rank_stat', 'views.user_level_rank_stat', name='user-level-rank-stat'),
    url(r'^user_reg_stat', 'views.user_reg_stat', name='user-reg-stat'),
    url(r'^user_payment_stat', 'views.user_payment_stat', name='user-payment-stat'),
    url(r'^user_online_stat', 'views.user_online_stat', name='user-online-stat'),
    url(r'^user_draw_time_rank', 'views.user_draw_time_rank', name='user-draw-time-rank'),
    url(r'^user-draw-power-rank', 'views.user_draw_power_rank', name='user-draw-power-rank'),
    url(r'^user-draw-pk-rank', 'views.user_draw_pk_rank', name='user-draw-pk-rank'),
    url(r'^user_use_gold_rank', 'views.user_use_gold_rank', name='user-use-gold-rank'),
    url(r'^user_payment_rank', 'views.user_payment_rank', name='user-payment-rank'),
)
