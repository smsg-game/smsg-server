#!/usr/bin/python
# -*- coding: utf-8 -*-


from django.http import HttpResponseRedirect
from django.http import HttpResponse

from ldsg_admin.config import DEBUG, SERVER_TYPE

from ldsg_admin.web.render import render_to_response

from ldsg_admin.business import user_roles, server_business, blackimei_business, package_business, \
             partner_business, admin_business, gift_code_business, payment_business
from ldsg_admin.common.decorators import login_required, privilege_required
from ldsg_admin.common import get_logging
from ldsg_admin.constants.page_constant import PageConstant
from ldsg_admin.business import userpage_business
logging = get_logging()

@login_required
def index(request):
    """后台首页"""
    return render_to_response(request, "admin/index.html", locals())

@login_required
@privilege_required(page_id=1, operator_id=4)
def server_list(request):
    page = int(request.REQUEST.get("page", 1))
    pagesize = int(request.REQUEST.get("pagesize", 20))
    server_zone = int(request.REQUEST.get("server_zone", 2))
    infos, total = server_business.get_server_list(page, pagesize, server_zone)
    return render_to_response(request, "admin/server_list.html", locals())

@login_required
@privilege_required(page_id=16, operator_id=4)
def white_ip_list(request):
    page = int(request.REQUEST.get("page", 1))
    pagesize = int(request.REQUEST.get("pagesize", 20))
    ucenter = request.REQUEST.get("ucenter", "android")
    ip = request.REQUEST.get("ip", "")
    infos, total = admin_business.get_game_web_white_ip_list(ucenter, page, pagesize, ip)
    return render_to_response(request, "admin/white_ip_list.html", locals())

@login_required
def server_partner_list(request):
    server_id = request.REQUEST.get("server_id")
    server_zone = int(request.REQUEST.get("server_zone", 0))
    if not server_zone:
        server = server_business.get_server(server_id)
        server_zone = server.server_zone
        
    infos = partner_business.get_partner_list_by_server_zone(server_zone)
    
    server_partner_list = server_business.get_server_partner_list(server_id)
    server_partner_set = set()
    for server_partner in server_partner_list:
        server_partner_set.add(server_partner.partner_id)

    for info in infos:
        if info.partner_id in server_partner_set:
            setattr(info, "checked", 1)
        else:
            setattr(info, "checked", 0)
        
    return render_to_response(request, "admin/server_partner_list.html", locals())

@login_required
def partner_list(request):
    page = int(request.REQUEST.get("page", 1))
    pagesize = int(request.REQUEST.get("pagesize", 20))
    infos, total = partner_business.get_partner_list(page, pagesize)
    return render_to_response(request, "admin/partner_list.html", locals())

@login_required
def sql_query(request):
    return render_to_response(request, "admin/sql_query.html", locals())

@login_required
@privilege_required(page_id=2, operator_id=4)
def user_list(request):
    page = int(request.REQUEST.get("page", 1))
    pagesize = int(request.REQUEST.get("pagesize", 20))
    infos, total = admin_business.get_user_list(page, pagesize)
    return render_to_response(request, "admin/user_list.html", locals())

@login_required
@privilege_required(page_id=3, operator_id=4)
def page_list(request):
    page = int(request.REQUEST.get("page", 1))
    pagesize = int(request.REQUEST.get("pagesize", 20))
    infos, total = admin_business.get_page_list(page, pagesize)
    return render_to_response(request, "admin/page_list.html", locals())

@login_required
def privilege_page(request,user_id):
    user_id = user_id
    infos = admin_business.get_page_all_list(user_id)
    return render_to_response(request, "admin/privilege.html", locals())

@login_required
def user_page_list(request):
    return render_to_response(request, "admin/user_page_list.html", locals())

@login_required
def package_list(request):
    """升级包列表"""
    ucenter = request.REQUEST.get('ucenter', 'android')
    page = int(request.REQUEST.get('page', 1))
    version = request.REQUEST.get('version', "")
    pagesize = 20
    
    is_test = 0 if user_roles.had_privilege(request, PageConstant.page_package_info_id, 11) else 1
    
    total, infos = package_business.get_package_list(ucenter, version, page, pagesize)
    return render_to_response(request, "admin/package_list.html", locals())

@login_required
@privilege_required(page_id=12, operator_id=4)
def giftcode_list(request):
    """礼包码管理"""
    category = request.REQUEST.get('category', 'ad')
    gift_bag_type = request.REQUEST.get('gift_bag_type', "")
    sub_type = request.REQUEST.get('sub_type', 0)
    gift_code = request.REQUEST.get('gift_code', "")
    page = int(request.REQUEST.get('page', 1))
    to_excel = int(request.REQUEST.get('to_excel', 0))
    total, pagesize = 0, 20
    if gift_bag_type:
        if to_excel == 1:
            pagesize = 1000000
        total, infos = gift_code_business.get_giftcode_list(category, gift_bag_type, sub_type, gift_code, page, pagesize)
        if to_excel == 1:
            datas = []
            for info in infos:
                datas.append(u"%s" % info["code"])    
            response = HttpResponse("\r\n".join(datas), mimetype='text/plain')
            response['Content-Disposition'] = 'attachment; filename=gift_code_%s_%s_%s.txt' % (gift_bag_type, sub_type, category)
            return response
    return render_to_response(request, "admin/gift_code_list.html", locals())

@login_required
def black_imei_list(request):
    """升级包列表"""
    ucenter = request.REQUEST.get('ucenter', 'android')
    page = int(request.REQUEST.get('pagesize', 1))
    imei = request.REQUEST.get('imei', "")
    
    pagesize = 20
    total, infos = blackimei_business.get_black_imei_list(ucenter, page, imei, pagesize)
    return render_to_response(request, "admin/black_imei_list.html", locals())

def login(request):
    if not request.POST:
        user = user_roles.get_userinfo(request)
        username = user and user.username or None
        data = {'username': username,
                'post_path': request.get_full_path()}
        return render_to_response(request, 'admin/login.html', data)
    
    # login post back
    username = request.POST.get('username', None)
    password = request.POST.get('password', None)

    if not username or not password:
        error_message = "用户名或者密码不能都不填写，请重新登录。";
        data = {'error_message': error_message,
                'post_path': request.get_full_path()}
        return render_to_response(request, 'admin/login.html', data) 
    
    r, session_id_or_msg = user_roles.login(username, password)
    if r == 0:
        redirect_to = request.GET.get("to", "/")
        response = HttpResponseRedirect(redirect_to)
        response.set_cookie(user_roles.SESSION_KEY, session_id_or_msg)
        return response
    else:
        data = {'username': username,
                'post_path': request.get_full_path(),
                "error_message": session_id_or_msg}
        return render_to_response(request, 'admin/login.html', data)

def logout(request):
    user_roles.logout(request)
    response = HttpResponseRedirect("/admin/login")
    response.delete_cookie("SESSION_KEY")
    return response

@login_required
def add_privilege(request):
    params = request.GET.get('params', '')
    user_id = request.GET.get('user_id','')
    if params and user_id:      
        ps = params.split("-")
        for p in ps:
            if not p:
                continue
            infos = p.split(",")
            if len(infos)==3:
                user_page_id, key, value = infos[0], infos[1], infos[2]
                userpage_business.add_userpage(user_id, key, value,user_page_id)
            else:
                key, value = infos[0], infos[1]
                userpage_business.add_userpage(user_id, key, value)
    
    infos = admin_business.get_page_all_list(user_id)
    return render_to_response(request, "admin/privilege.html", locals())
            
@login_required
@privilege_required(page_id=15, operator_id=4)
def game_web_status_list(request):
    
    infos = []
    if DEBUG:
        ucenter_list = ("android", "ios",)
    else:
        ucenter_list = ("android", "ios",)
    for ucenter in ucenter_list:
        status = admin_business.get_game_web_status(ucenter)
        infos.append({"ucenter": ucenter, "status": status})
        
    return render_to_response(request, "admin/game_web_status_list.html", locals())         

@login_required
@privilege_required(page_id=17, operator_id=4)
def system_notice_list(request):
    
    if SERVER_TYPE == 1:
        default_ucenter = "android"
    else:
        default_ucenter = "android"
    ucenter = request.REQUEST.get('ucenter', default_ucenter)
    page = int(request.REQUEST.get('page', 1))
    server_id = request.REQUEST.get('server_id', "")
    partner_id = request.REQUEST.get('partner_id', "")
    pagesize = 20
    
    total, infos = admin_business.get_notice_list(ucenter, server_id, partner_id, page, pagesize)

    return render_to_response(request, "admin/notice_list.html", locals()) 

@login_required
@privilege_required(page_id=18, operator_id=4)
def sync_system_table(request):
    return render_to_response(request, "admin/tool_table_sync.html", locals()) 
    
@login_required
@privilege_required(page_id=21, operator_id=4)
def payment_order_list(request):
    """订单列表"""
    default_ucenter = 'android'
    if SERVER_TYPE == 2:
        default_ucenter = 'android'
    ucenter = request.REQUEST.get('ucenter', default_ucenter)
    page = int(request.REQUEST.get('page', 1))
    server_id = request.REQUEST.get('server_id', "")
    name = request.REQUEST.get('name', "")
    
    pagesize = 20
    total, infos = 0, []
    
    is_test = 0 if user_roles.had_privilege(request, PageConstant.page_fill_order, 11) else 1
    
    if server_id and name:
        total, infos = payment_business.get_payment_order_list(ucenter, name, server_id, page, pagesize)
    
    return render_to_response(request, "admin/payment_order_list.html", locals())    

    
if __name__ == "__main__":
    pass