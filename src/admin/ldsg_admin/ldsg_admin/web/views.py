#-*- coding:utf-8 -*-

import MySQLdb
from ldsg_admin.web.render import render_to_response,redirect,json_response
from django.http import Http404, HttpResponse
from ldsg_admin.model import *
from ldsg_admin.common import exception_mgr
from ldsg_admin.common.json import *
from ldsg_admin.business import user_roles
from ldsg_admin.common.decorators import login_required,privilege_required


@login_required
def index(request):
    '''首页'''
    services = Service.query()
    data = {
            'post_path': request.get_full_path(),
            'services': services
            } 
    response = render_to_response(request, "index.html",data)

    return response

@login_required
def service(request):
    '''服务器列表'''
    services = Service.query()
    data = {
            'post_path': request.get_full_path(),
            'services': services
            } 
    response = render_to_response(request, "admin/index.html",data)
    return response

def login(request):
    '''登陆'''
    if request.method == 'GET':
        response = render_to_response(request, "login.html")
        return response

    elif request.method == 'POST':
        username = request.POST.get('username',None)
        password = request.POST.get('password',None)

        if not username or not password:
            error_message = "用户名或者密码不能都不填写，请重新登录。"
            data = {'error_message': error_message,
                    'post_path': request.get_full_path()}
            return render_to_response(request, 'login.html', data)
        else:
            r, session_id_or_msg = user_roles.login(username, password)
            if r == 0:
                #redirect_to = request.GET.get(REDIRECT_FIELD_NAME, '/index/')
                response = redirect('/index/')
                response.set_cookie(user_roles.SESSION_KEY, session_id_or_msg)    
                return response
            else:
                data = {'username': username,
                        'post_path': request.get_full_path(),
                        "error_message": session_id_or_msg}
                return render_to_response(request, 'login.html', data)

@login_required
def add_service(request):
    '''添加修改服务器'''
    if request.method == 'GET':
        response = render_to_response(request, "admin/add_service.html")
        return response

    elif request.method == 'POST':
        sid = request.POST.get('sid',None)
        s_num = request.POST.get('s_num',None)
        s_name = request.POST.get('s_name',None)
        db_connect = request.POST.get('db_connect',None)
        open_time = request.POST.get('open_time',None)
        isdel = request.POST.get('isdel',None)
        s_port = request.POST.get('s_port',None)
        db_connect_stat = request.POST.get('db_connect_stat',None)
        
        if not s_num or not s_name or not db_connect or not open_time or not isdel or not s_port or not db_connect_stat :
            error_message = "所有的字段都不能为空"
            data = {'error_message': error_message,
                    'post_path': request.get_full_path()}
            return render_to_response(request, 'admin/add_service.html', data)
        else:
            service = Service()
            service.s_num = s_num
            service.s_name = s_name
            service.db_connect = db_connect
            service.open_time = open_time
            service.isdel = isdel
            service.s_port = s_port
            service.db_connect_stat = db_connect_stat
            service.edit_time = datetime.datetime.now() 
            service.sid = sid
            error_message = None
            try:
                service.persist()
            except Exception, e:
                error_message = "出错了!"
                response = render_to_response(request, "admin/add_service.html",{'error_message': error_message,'service': service})
                return response
            
            services = Service.query()
            data = {
                    'post_path': request.get_full_path(),
                    'services': services
                    } 
            response = render_to_response(request, "admin/index.html",data)
            return response

@login_required
def delete_service(request,offset):
    '''删除服务器'''
    try:
        offset = int(offset)
    except ValueError:
        raise Http404()
    service = Service.load(sid=offset)
    service.isdel = 1
    service.persist()
    services = Service.query()
    data = {
            'post_path': request.get_full_path(),
            'services': services
            } 
    response = render_to_response(request, "admin/index.html",data)
    return response

@login_required
def test_connect(request):
    '''测试连接'''
    try:
        dbConnect = request.POST.get('dbConnect',None)
        db = dbConnect.split(';')
        host = db[0].split('=')[1].split(',')[0]
        dbstr = db[1].split('=')[1]
        user = db[2].split('=')[1]
        passwd = db[3].split('=')[1]
        con= MySQLdb.connect(host= host,user= user,passwd = passwd,db=dbstr)
        return HttpResponse("true")
    except:
        return HttpResponse("false") 

@login_required
def update_service(request,offset):
    try:
        offset = int(offset)
        service = Service.load(sid = offset)
        response = render_to_response(request, "admin/add_service.html",{'service': service})
        return response
    except ValueError:
        raise Http404()

@login_required
def partner(request,num='0'):
    try:
        partners = Partner.query()
        partnerServices = PartnerService.query(condition='pid='+num) 
        services = []
        filterservices = Service.query()
        for p in partnerServices:
            service = Service.load(sid=p.sid)
            services.append(service)
        if services:
            filterservices.extend(services)
            filterservices = list(set(filterservices))
        data = {
            'post_path': request.get_full_path(),
            'services': services,
            'partners':partners,
            'pid':num,
            'filterservices':filterservices
            } 
        response = render_to_response(request, "admin/partner.html",data)
        return response
    except :
        exception_mgr.on_except()
        raise Http404()

@login_required
def add_partner_service(request):
    if request.method == "POST":
        pid = request.POST.get("pid",None)
        for s in request.POST.getlist('selectedServer'):
            partnerServices = PartnerService()
            partnerServices.sid = int(s)
            partnerServices.pid = int(pid)
            partnerServices.persist()
            
        partners = Partner.query()
        partnerServices = PartnerService.query(condition='pid='+pid) 
        services = []
        filterservices = Service.query()
        for p in partnerServices:
            service = Service.load(sid=p.sid)
            services.append(service)
        if services:
            filterservices.extend(services)
            filterservices = list(set(filterservices))
        data = {
            'post_path': request.get_full_path(),
            'services': services,
            'partners':partners,
            'pid':pid,
            'filterservices':filterservices
            } 
        response = render_to_response(request, "admin/partner.html",data)
        return response

@login_required
def delete_partner(request,offset):
    try:
        offset = int(offset)
    except:
        raise Http404()
    partner = Partner.load(pid = offset)
    partner.delete('pid')
    partnerServices = PartnerService()
    partnerServices.delete_mul(pid=offset)
    return redirect('/partner/')

@login_required
def partner_service_delete(request,sid,pid):
    try:
        sid = int(sid)
        pid = int(pid)
        partnerServices = PartnerService()
        partnerServices.delete_mul(sid=sid,pid=pid)
        return redirect('/partner/'+str(pid)+'/')
    except:
        exception_mgr.on_except()
        raise Http404()

@login_required
def add_partner(request):
    if request.method == "GET":
        return render_to_response(request, 'admin/add_partner.html')
    else:
        p_num = request.POST.get("p_num",None)
        p_name = request.POST.get("p_name",None)
        if not p_name or not p_num  :
            error_message = "所有的字段都不能为空"
            data = {'error_message': error_message,
                    'post_path': request.get_full_path()}
            return render_to_response(request, 'admin/add_partner.html', data)
        else:
            partner = Partner()
            partner.p_num = p_num
            partner.p_name = p_name
            partner.create_time = datetime.datetime.now()
            
            error_message = None
            try:
                partner.persist()
            except Exception, e:
                error_message = "出错了!"
                response = render_to_response(request, "admin/add_partner.html",{'error_message': error_message})
                return response
            return redirect('/partner/')

@login_required        
def getService(request,offset):
    try:
        pid = int(offset)
    except:
        data = {'rc':3000,'ms':'pid 只能是数字'}
        return json_response(dumps(data))
    if not pid:
        data = {'rc':3001,'ms':'pid 不能为空'}
        return json_response(dumps(data))
    else:        
        partnerServices = PartnerService.query(condition='pid='+str(pid)) 
        services = []
        for p in partnerServices:
            service = Service.load(sid=p.sid)
            services.append(service)
        data = {'rc':1000,'services':[o.__dict__ for o in services]}
        return json_response(dumps(data))

def logout(request):
    user_roles.logout(request)
    response = redirect("/index/")
    response.delete_cookie("_auth_user_id")
    return response

def privilege_error(request):
    error_message = "你没有权限，请联系管理员!"
    response = render_to_response(request, "privilege_error.html",{'error_message': error_message})
    return response

def error(request):
    response = render_to_response(request, "500.html", locals())
    return response

@login_required
@privilege_required(page_id=1,operator_id=1)
def test_privilege(request):
    response = render_to_response(request, "test.html")
    return response
    