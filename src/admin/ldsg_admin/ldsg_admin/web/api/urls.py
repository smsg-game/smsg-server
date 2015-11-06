
from django.conf.urls.defaults import patterns, url

urlpatterns = patterns('web.api',
    url(r'api/get_tool_log_stat$', 'views.tool_use_info'),
    url(r'api/get_server_online$', 'views.get_server_online'),
    url(r'get_server_list$', 'views.get_server_list'),
    url(r'battle_status_report$', 'views.battle_status_report'),
)
