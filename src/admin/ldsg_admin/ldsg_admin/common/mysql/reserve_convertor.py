#-*- coding:utf-8 -*-
class ReserveLiteral:
    def __init__(self, text):
        self.text = text
    
    @classmethod
    def convertor(cls, d, c):
        return d.text