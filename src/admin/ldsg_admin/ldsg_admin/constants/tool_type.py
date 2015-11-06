#!/usr/bin/python
# -*- coding: utf-8 -*-

class ToolType:

    #金币
    TOOL_TYPE_GOLD = 1
    
    #武将
    TOOL_TYPE_HERO = 3001
    
    #银币
    TOOL_TYPE_COPPER = 2

    #休力
    TOOL_TYPE_POWER = 4

    #经验值
    TOOL_TYPE_EXP = 5

    #武将背包格子
    TOOL_TYPE_HERO_BAG = 6

    #装备背包格子
    TOOL_TYPE_EQUIP_BAG = 7

    #材料
    TOOL_TYPE_MATERIAL = 1001

    #装备
    TOOL_TYPE_EQUIP = 2001

    #宝箱
    TOOL_TYPE_GIFT_BOX = 4001

    #武将碎片
    TOOL_TYPE_HERO_SHARD = 5001

    #技能书
    TOOL_TYPE_SKILL_BOOK = 6001
    
    #定向蛋
    TOOL_TYPE_DIREC_EGG = 7001

    #VIP经验
    TOOL_TYPE_VIP_EXP = 8001
    
tool_type_map = {
    ToolType.TOOL_TYPE_COPPER: u"银币",
    ToolType.TOOL_TYPE_DIREC_EGG: u"彩蛋",
    ToolType.TOOL_TYPE_EQUIP: u"装备",
    ToolType.TOOL_TYPE_EQUIP_BAG: u"装备背包",
    ToolType.TOOL_TYPE_EXP: u"经验",
    ToolType.TOOL_TYPE_GIFT_BOX: u"宝箱",
    ToolType.TOOL_TYPE_GOLD: u"元宝",
    ToolType.TOOL_TYPE_HERO: u"武将卡牌",
    ToolType.TOOL_TYPE_HERO_BAG: u"武将背包",
    ToolType.TOOL_TYPE_HERO_SHARD: u"契约碎片",
    ToolType.TOOL_TYPE_MATERIAL: u"材料",
    ToolType.TOOL_TYPE_POWER: u"体力",  
    ToolType.TOOL_TYPE_SKILL_BOOK: u"技能书",
    ToolType.TOOL_TYPE_VIP_EXP: u"VIP卡",
}
    