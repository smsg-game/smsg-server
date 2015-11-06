#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
合作商相关操作
"""

from ldsg_admin.model import Partner

def get_all_partner_list():
    return Partner.query(condition="is_del=0")

def get_partner_list_by_server_zone(server_zone):
    return Partner.query(condition=" server_zone = %s and is_del=0" % server_zone)

def add_partner(partner_id, name, server_zone):
    partner = Partner()
    partner.partner_id = partner_id
    partner.name = name
    partner.server_zone = server_zone
    partner.persist()
    
def get_partner_list(page=1, pagesize=20):
    return Partner.paging(page, pagesize, condition="is_del=0")

def delete_partner(partner_id):
    partner = Partner()
    partner.partner_id = partner_id
    partner.is_del = 1
    partner.persist()
    
def get_partner(partner_id):
    return Partner.load(partner_id=partner_id)