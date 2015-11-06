'''
Created on 2014-1-21

@author: Candon
'''

import datetime
from ldsg_admin.model import UserPage

def add_userpage(user_id, page_id, value,user_page_id=None):
    user_page = UserPage()
    if user_page_id is not None:
        user_page.user_page_id = user_page_id
        user_page.created_time = datetime.datetime.now()
        user_page.updated_time = datetime.datetime.now()
    else:
        user_page.updated_time = datetime.datetime.now()
        
    user_page.user_id = user_id
    user_page.page_id = page_id
    user_page.value = value
    user_page.persist()
    
def delete_userpage(user_page_id):
    user_page = UserPage.load(user_page_id = user_page_id)
    user_page.delete('user_page_id')