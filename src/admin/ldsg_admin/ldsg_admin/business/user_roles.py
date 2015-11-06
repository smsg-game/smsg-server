#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
用户权限相关操作
"""

from uuid import uuid4
from ldsg_admin.common import md5mgr
from ldsg_admin.model import User, UserPage
from ldsg_admin.common import get_logging

SESSION_KEY = '_auth_user_id'
BACKEND_SESSION_KEY = '_auth_user_backend'
REDIRECT_FIELD_NAME = 'next'
SESSION_EXPIRE_TIME = 60 * 60 * 24

def login(username, password):
    """"""
    LOGIN_ERROR = "您输入的用户名或者密码不正确，请重新登录。"
    if not username or not password:
        return -1, LOGIN_ERROR
    
    #password = md5mgr.mkmd5fromstr(md5mgr.mkmd5fromstr(password))

    user = User.load(username=username)
    if not user or user.password != password:
        return -1, LOGIN_ERROR
    
    if user.status != 0:
        return -1, "您的帐号已经被停用，请联系管理员"
    
    session_id = md5mgr.mkmd5fromstr('%s%s' % (uuid4(), username))
    user.session_id = session_id
    user.persist()

    return 0, session_id
    
def logout(request):
    """用户登出，从cache中删除相关session_id"""
    session_id = request.COOKIES.get(SESSION_KEY, None)
    if session_id is not None:
        pass
    
def had_privilege(request, page_id, privilege_val):
    user = get_userinfo(request)
    user_page = UserPage.load(user_id=int(user.id), page_id=page_id)
    if not user_page:
        return False
    if user_page.value % privilege_val == 0:
        return True
    return False
    
def get_userinfo(request):
    """根据request中的session_id获取cache中的用户名. 若cache挂了，则从数据库中获取
    """
    session_id = request.COOKIES.get(SESSION_KEY, None)
    logging = get_logging("login")
    logging.info(session_id)
    if session_id is not None:
        user = User.load(session_id=session_id)
        if user:
            return user
#         if username is None and not cache.get_stats(): # 只有cache挂了才从数据库中读取，防止恶意刷后台页面
#             user = User.load(session_id=session_id)
#             if user:
#                 username = user.username
#         if username is not None:
#             return username
    return None

if __name__ == '__main__':
    pass
