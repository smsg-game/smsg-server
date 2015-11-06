from django.conf import settings
from django.conf.urls.defaults import patterns, url, include
from ldsg_admin.web.views import *
from ldsg_admin.web.admin.views import privilege_page, add_privilege

urlpatterns = patterns('',
    # Example:
    #url(r'^index.html?$', 'ldsg_admin.web.views.index', name='index'),
    (r'^site_media/(?P<path>.*)$', 'django.views.static.serve', {'document_root': settings.STATIC_PATH}), 
    (r'^admin/', include('web.admin.urls')),
    (r'^manager/', include('web.manager.urls')),
    (r'^stat/', include('web.stat.urls')),
    (r'^system/', include('web.system.urls')),
    (r'^web/', include('web.api.urls')),
    (r'^login/$', login),
    (r'^logout/$', logout),
    (r'^add_service/$', add_service),
    (r'^$', index),
    (r'^index/$', index),
    (r'^service/delete/(\d{1,9})/$', delete_service),
    (r'^test_connect/$', test_connect),
    (r'^service/update/(\d{1,9})/$', update_service),
    (r'^partner/$', partner),
    (r'^partner/(?P<num>\d{1,9})/$', partner),
    (r'^service/$', service),
    (r'^add_partner_service/$', add_partner_service),
    (r'^partner/delete/(\d{1,9})/$', delete_partner),
    (r'^test/$', test_privilege),
    (r'^partner/service/delete/(?P<sid>\d{1,9})/(?P<pid>\d{1,9})/$', partner_service_delete),
    (r'^partner/add/$', add_partner),
    (r'^getService/(\d{1,9})/$', getService),
    (r'^admin/privilege_page/(?P<user_id>\d+)$', privilege_page),
    (r'^admin/add_privilege$', add_privilege),
    (r'^privilege_error/$', privilege_error),
    (r'^error/$', error),
)

# json rpc settings
jsonrpc_urlpatterns = patterns('',
   (r'^services/admin/$', 'web.services.admin_service'),
   (r'^services/user/$', 'web.services.user_service'),
   (r'^services/activity/$', 'web.services.activity_service'),
   (r'^services/forces/$', 'web.services.forces_service'),
   (r'^services/once_pay/$', 'web.services.activity_service'),
   (r'^services/package/$', 'web.services.package_service'),
   (r'^services/client/$', 'web.services.client_service'),
   (r'^services/mail/$', 'web.services.mail_service'),
)
