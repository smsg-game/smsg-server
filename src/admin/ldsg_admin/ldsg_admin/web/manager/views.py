#!/usr/bin/python
# -*- coding: utf-8 -*-


from datetime import datetime, timedelta

from ldsg_admin.web.render import render_to_response, redirect

from ldsg_admin.business import user_business, user_roles
from ldsg_admin.common import get_logging
from ldsg_admin.business import stat_server_business
from ldsg_admin.common.decorators import login_required, privilege_required
from ldsg_admin.business import activity_business, notice_business
from ldsg_admin.business import forces_business, mail_business

logging = get_logging()
 
@login_required
@privilege_required(page_id=26, operator_id=4)
def user_info(request):
    name = request.REQUEST.get('name', "")
    server_id = request.REQUEST.get('server_id', '')
    logging.debug(u"查询用户信息.name[%s], server_id[%s]" % (name, server_id))
    if name and server_id:    
        user_info = user_business.get_user_info(server_id, name)
    return render_to_response(request, "manager/user_info.html", locals())

@login_required
def stat_server(request):
    begindate = request.REQUEST.get('begindate', "")
    enddate = request.REQUEST.get('enddate',"")
    logging.debug(u"查询服务器信息.begindate[%s], enddate[%s]" % (begindate, enddate))
    if enddate:
        stat_server_datas_map, total, server_zone_map = stat_server_business.get_stat_server(begindate, enddate)
    else:
        stat_server_datas_map, total, server_zone_map = stat_server_business.get_stat_server(begindate)
    return render_to_response(request, "manager/server_stat.html", locals())

@login_required
def stat_server_country(request):
    begindate = request.REQUEST.get('begindate', "")
    enddate = request.REQUEST.get('enddate',"")
    logging.debug(u"查询服务器信息.begindate[%s], enddate[%s]" % (begindate, enddate))
    if enddate:
        stat_server_datas_map, total, server_zone_map = stat_server_business.get_stat_server_country(begindate, enddate)
    else:
        stat_server_datas_map, total, server_zone_map = stat_server_business.get_stat_server_country(begindate)
    return render_to_response(request, "manager/server_stat_country.html", locals())

@login_required
def currdate_register(request):
    begindate = request.REQUEST.get('begindate', "")
    server_id = request.REQUEST.get('server_id', "")
    logging.debug(u"查询注册数据信息.begindate[%s], server_id[%s]" % (begindate, server_id))
    if begindate and server_id:
        stats = user_business.get_user_register(server_id, begindate)
    return render_to_response(request, "manager/user_register.html", locals())

@login_required
def user_hero_info(request):
    name = request.REQUEST.get('name', "")
    server_id = request.REQUEST.get('server_id', 0)
    logging.debug(u"查询用户武将信息.name[%s], server_id[%s]" % (name, server_id))
    if name and server_id:
        user_hero_info = user_business.get_user_hero_info(server_id, name)
    return render_to_response(request, "manager/user_hero_info.html", locals())

@login_required
def user_package_info(request):
    name = request.REQUEST.get('name', "")
    server_id = request.REQUEST.get('server_id', 0)
    logging.debug(u"查询用户背包信息.name[%s], server_id[%s]" % (name, server_id))
    if name and server_id:
        user_package_info = user_business.get_user_package_info(server_id, name)
    return render_to_response(request, "manager/user_package_info.html", locals())

@login_required
def user_purchase_info(request):
    
    now = datetime.now()
    yesterday = now - timedelta(days=1)

    name = request.REQUEST.get('name', "")
    server_id = request.REQUEST.get('server_id', 0)
    start_date = request.REQUEST.get('start_date', yesterday.strftime("%Y-%m-%d 00:00:00"))
    end_date = request.REQUEST.get('end_date', now.strftime("%Y-%m-%d 00:00:00"))
    

    logging.debug(u"查询用户消费信息.name[%s], server_id[%s], start_date[%s], end_date[%s]" % (name, server_id, start_date, end_date))
    
    if name and server_id and start_date and end_date:
        user_purchase_info = user_business.get_user_purchase_info(server_id, name, start_date, end_date)
    return render_to_response(request, "manager/user_purchase_info.html", locals())

@login_required
@privilege_required(page_id=9, operator_id=4)
def user_mail_list(request):
    server_id = request.REQUEST.get('server_id')
    page = int(request.REQUEST.get('page', 1))
    name = request.REQUEST.get('name', "")
    source_id = request.REQUEST.get('source_id', "")
    
    pagesize = 20
    
    if name:
        total, infos = mail_business.get_user_mail_list(server_id, name, source_id, page, pagesize)
    else:
        total = 0
        
    return render_to_response(request, "manager/user_mail_list.html", locals())
   
@login_required
def stat_liucun(request):
    logging.debug(u"查询服务器留存信息")
    stats = stat_server_business.get_stat_liucun()
    return render_to_response(request, "manager/stat_server_liucun.html", locals())

@login_required
def stat_liucun_server(request):
    logging.debug(u"查询服务器留存信息")
    server_id = request.REQUEST.get('server_id', "")
    if server_id:
        stats = stat_server_business.get_stat_liucun_server(server_id)
    return render_to_response(request, "manager/stat_server_liucun_server.html", locals())

@login_required
def activity_list(request):
    logging.debug(u"查询服务器活动信息")
    server_id = request.REQUEST.get('server_id', "")
    if server_id:
        datas = activity_business.get_activity_list(server_id);
    return render_to_response(request, "manager/activity_list.html", locals())

@login_required
def forces_list(request):
    logging.debug(u"查询服务器关卡信息")
    server_id = request.REQUEST.get('server_id', "")
    if server_id:
        datas = forces_business.get_forces_list(server_id);
    return render_to_response(request, "manager/forces_list.html", locals())    
    
@login_required    
@privilege_required(page_id=4, operator_id=4)
def system_exchange_list(request):
    page = int(request.REQUEST.get('page', 1))
    show_all = int(request.REQUEST.get('all', 0))
    pagesize = int(request.REQUEST.get('pagesize', 20))
    username = None
    if show_all == 0:
        user = user_roles.get_userinfo(request)
        if user:
            username = user.username
        
    infos, total = activity_business.get_system_tool_exchange_list(page, pagesize, username=username)
    return render_to_response(request, "manager/system_exchange_list.html", locals())

@login_required    
@privilege_required(page_id=5, operator_id=4)
def system_pay_reward_list(request, reward_type=1):
    page = int(request.REQUEST.get('page', 1))
    pagesize = int(request.REQUEST.get('pagesize', 20))
    show_all = int(request.REQUEST.get('all', 0))
    name = u"单笔充值" if reward_type == 1 else u"累积充值"
    
    username = None
    if show_all == 0:
        user = user_roles.get_userinfo(request)
        if user:
            username = user.username
    
    infos, total = activity_business.get_system_pay_reward_list(page, pagesize, reward_type, username)
    return render_to_response(request, "manager/system_pay_reward_list.html", locals())

@login_required    
def system_exchange_detail_list(request):
    
    eid = int(request.REQUEST.get('eid', 0))
    
    if request.method == "POST":
        check_list = request.REQUEST.getlist("check")
        infos = []
        now = datetime.now()
        for check_index in check_list:
            rep_tool_ids = request.REQUEST.get("pre_item_val_%s" % check_index)
            gain_tool_ids = request.REQUEST.get("gain_item_val_%s" % check_index)
            rep_tool_ids_name = request.REQUEST.get("pre_item_val_name_%s" % check_index)
            gain_tool_ids_name = request.REQUEST.get("gain_item_val_name_%s" % check_index)
            time_limit = request.REQUEST.get("times_%s" % check_index)
            logging.debug("rep_tool_ids[%s], gain_tool_ids[%s], time_limit[%s]" % (rep_tool_ids, gain_tool_ids, time_limit))
            if rep_tool_ids and gain_tool_ids and time_limit:
                info = {'system_tool_exchange_id': eid, 'times': time_limit, 'pre_exchange_items': rep_tool_ids, 'post_exchange_items': gain_tool_ids, \
                          'created_time': now, 'pre_exchange_items_name': rep_tool_ids_name, 'post_exchange_items_name': gain_tool_ids_name}
                infos.append(info)
        
        #save
        activity_business.save_system_tool_exchange_detail(eid, infos)
            
        return redirect("/manager/system_exchange_detail_list?eid=%s" % eid)

    infos = activity_business.get_system_tool_exchange_detail_list(eid)
    max_index = 1
    for info in infos:
        setattr(info, "index", max_index)
        max_index += 1
    empty_infos = []
    for i in range(max_index, 21):
        empty_infos.append(i)
  
    return render_to_response(request, "manager/system_exchange_detail_list.html", locals())

@login_required    
def system_pay_reward_detail_list(request):
    
    rid = int(request.REQUEST.get('rid', 0))
    
    if request.method == "POST":
        check_list = request.REQUEST.getlist("check")
        infos = []
        now = datetime.now()
        for check_index in check_list:
            pay_money = request.REQUEST.get("pay_%s" % check_index)
            tool_ids = request.REQUEST.get("tool_ids_%s" % check_index)
            tool_ids_name = request.REQUEST.get("tool_ids_name_%s" % check_index)
            times_limit = request.REQUEST.get("times_limit_%s" % check_index)
            logging.debug("pay_money[%s], tool_ids[%s], times_limit[%s]" % (pay_money, tool_ids, times_limit))
            if pay_money and tool_ids and times_limit:
                info = {'system_pay_reward_id': rid, 'times_limit': times_limit, 'pay_money': pay_money, 'tool_ids': tool_ids, \
                          'created_time': now, 'tool_ids_name': tool_ids_name}
                infos.append(info)
        #save
        activity_business.save_system_pay_reward_detail(rid, infos)
            
        return redirect("/manager/system_pay_reward_detail_list?rid=%s" % rid)

    pay_reward_info = activity_business.get_system_pay_reward(rid)
    name = u"单笔充值" if pay_reward_info.reward_type == 1 else u"累积充值"

    infos = activity_business.get_system_pay_reward_detail_list(rid)
    max_index = 1
    for info in infos:
        setattr(info, "index", max_index)
        max_index += 1
    empty_infos = []
    amount_map = {1: 5, 2: 30, 3: 50, 4: 100, 5: 200, 6: 500, 7: 1000, 8: 2000}
    for i in range(max_index, 11):
        empty_infos.append({"index": i, "amount": amount_map.get(i, 0)})
  
    return render_to_response(request, "manager/system_pay_reward_detail_list.html", locals())

@login_required 
@privilege_required(page_id=7, operator_id=4)   
def system_exchange_sync(request):
    
    eid = int(request.REQUEST.get('eid', 0))

    infos = activity_business.get_system_tool_exchange_detail_list(eid)
    max_index = 1
    for info in infos:
        setattr(info, "index", max_index)
        max_index += 1
        
    exchange_info = activity_business.get_system_tool_exchange(eid)
  
    return render_to_response(request, "manager/system_exchange_sync.html", locals())

@login_required    
@privilege_required(page_id=6, operator_id=4)
def system_pay_reward_sync(request):
    
    rid = int(request.REQUEST.get('rid', 0))

    infos = activity_business.get_system_pay_reward_detail_list(rid)
    max_index = 1
    for info in infos:
        setattr(info, "index", max_index)
        max_index += 1
    empty_infos = []
    for i in range(max_index, 11):
        empty_infos.append(i)
        
    pay_reward_info = activity_business.get_system_pay_reward(rid)
  
    name = u"单笔充值" if pay_reward_info.reward_type == 1 else u"累积充值"
  
    return render_to_response(request, "manager/system_pay_reward_sync.html", locals())

@login_required    
@privilege_required(page_id=10, operator_id=4)
def system_mail_list(request):
    
    page = int(request.REQUEST.get("page", 1))
    pagesize = int(request.REQUEST.get("pagesize", 15))
    infos, total = mail_business.get_system_mail_list(page, pagesize)

    return render_to_response(request, "manager/system_mail_list.html", locals())

@login_required    
@privilege_required(page_id=11, operator_id=4)
def system_mail_approve_list(request):
    
    page = int(request.REQUEST.get("page", 1))
    status = int(request.REQUEST.get("status", 0))
    pagesize = int(request.REQUEST.get("pagesize", 15))
    infos, total = mail_business.get_system_mail_list(page, pagesize, status)

    return render_to_response(request, "manager/system_mail_approve_list.html", locals())

@login_required    
@privilege_required(page_id=10, operator_id=4)
def system_mail_create(request):
    system_mail_id = int(request.REQUEST.get("mid", 0))
    copy = int(request.REQUEST.get("cp", 0))
    
    title = u"创建新邮件"
    
    if system_mail_id:
        title = u"编辑邮件"
        system_mail = mail_business.get_system_mail(system_mail_id)
        
    if copy:
        title = u"创建新邮件"
        system_mail_id = 0
    
    return render_to_response(request, "manager/system_mail_create.html", locals())

@login_required    
@privilege_required(page_id=11, operator_id=4)
def system_mail_approve(request):
    system_mail_id = request.REQUEST.get("mid", 0)
    system_mail = mail_business.get_system_mail(system_mail_id)
    return render_to_response(request, "manager/system_mail_approve.html", locals())

@login_required    
@privilege_required(page_id=13, operator_id=4)
def notice_list(request):
    
    page = int(request.REQUEST.get("page", 1))
    pagesize = int(request.REQUEST.get("pagesize", 15))
    infos, total = notice_business.get_notice_list(page, pagesize)

    return render_to_response(request, "manager/notice_list.html", locals())

@login_required    
@privilege_required(page_id=19, operator_id=4)
def system_mall_discount_list(request):
    page = int(request.REQUEST.get('page', 1))
    pagesize = int(request.REQUEST.get('pagesize', 20))
    infos, total = activity_business.get_system_mall_discount_list(page, pagesize)
    return render_to_response(request, "manager/system_mall_discount_list.html", locals())

@login_required    
def system_mall_discount_detail_list(request):
    
    did = int(request.REQUEST.get('did', 0))
    
    if request.method == "POST":
        infos = []
        for i in range(1, 300):
            mall_id = request.REQUEST.get("mall_id_%s" % i)
            discount = request.REQUEST.get("discount_%s" % i, 100)
            if not discount:
                discount= 100
            discount = int(discount)
            if mall_id and discount and discount != 100:
                info = {'system_mall_discount_activity_id': did,'discount': discount, 'mall_id': mall_id}
                infos.append(info)
        #save
        activity_business.save_system_mall_discount_detail(did, infos)
            
        return redirect("/manager/system_mall_discount_detail_list?did=%s" % did)

    system_mall_discount = activity_business.get_system_mall_discount(did)
    server_id = system_mall_discount.server_ids.split(",")[0]

    detail_list = activity_business.get_system_mall_discount_detail_list(did)
    discount_map = {}
    for detail in detail_list:
        discount_map[detail.mall_id] = detail.discount
   
    mall_list = activity_business.get_system_mall_list(server_id)
    infos = []
    for mall in mall_list:
        mall_id = mall["mall_id"]
        amount = mall["amount"]
        money_type = mall["money_type"]
        discount = discount_map.get(mall_id, 100)
        discount_price =  amount * discount / 100 
        infos.append({"name": mall["name"], "mall_id": mall_id, "discount": discount, "money_type": money_type, "amount": amount, "price": amount, "discount_price": discount_price})   

    return render_to_response(request, "manager/system_mall_discount_detail_list.html", locals())

@login_required    
@privilege_required(page_id=20, operator_id=4)
def system_mall_discount_sync(request):
    
    did = int(request.REQUEST.get('did', 0))

    system_mall_discount = activity_business.get_system_mall_discount(did)
    server_id = system_mall_discount.server_ids.split(",")[0]

    detail_list = activity_business.get_system_mall_discount_detail_list(did)
    discount_map = {}
    for detail in detail_list:
        discount_map[detail.mall_id] = detail.discount
   
    mall_list = activity_business.get_system_mall_list(server_id)
    infos = []
    for mall in mall_list:
        mall_id = mall["mall_id"]
        amount = mall["amount"]
        money_type = mall["money_type"]
        discount = discount_map.get(mall_id, 100)
        if discount == 100:
            continue
        discount_price =  amount * discount / 100 
        infos.append({"name": mall["name"], "mall_id": mall_id, "discount": discount, "money_type": money_type, "amount": amount, "price": amount, "discount_price": discount_price})   

    return render_to_response(request, "manager/system_mall_discount_sync.html", locals())

