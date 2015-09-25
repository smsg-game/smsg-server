package com.easou.sso.sdk.example;

import java.util.List;

import com.easou.sso.sdk.RegisterAPI;
import com.easou.sso.sdk.service.AuthBean;
import com.easou.sso.sdk.service.CodeConstant;
import com.easou.sso.sdk.service.EucAPIException;
import com.easou.sso.sdk.service.EucApiResult;
import com.easou.sso.sdk.service.JReason;
import com.easou.sso.sdk.service.RequestInfo;

public class RegisterExample {
	public static void main(String[] args) throws EucAPIException {
		RequestInfo info=new RequestInfo();
		info.setQn("3301");
		EucApiResult<String> result = RegisterAPI.bookingName(info);
		String username="";
		String bookNum="";
		if(CodeConstant.OK.equals(result.getResultCode())) {
			String[] results = result.getResult().split(",");
			username = results[0];
			bookNum = results[1];
			System.err.println("预约成功");
			System.out.println("bookName: " + username);
			System.out.println("bookNum:" + bookNum);
			// 检查用户名
			EucApiResult<String> checkResult = RegisterAPI.checkName(username, info);
			// 检查的resultCode应该为2001
			if(CodeConstant.OK.equals(checkResult.getResultCode())) {
				System.err.println("用户名检测成功");
			} else if(CodeConstant.BUSI_ERROR.equals(checkResult.getResultCode())) {
				System.err.println("用户名检测不成功");
				List<JReason> desc=checkResult.getDescList();
				for (JReason jReason : desc) {
					System.out.println(jReason.getC() + " " + jReason.getD());
				}
			}
		}
		// 注册一个用户
		EucApiResult<AuthBean> result1 = RegisterAPI.registByName(username, "111111", true, "", info);
		// 该注册不会成功，因为该名字已预约，而注册时没提供预约号
		if(CodeConstant.OK.equals(result1.getResultCode())) {
			System.err.println("注册成功");
			System.out.println("token: " + result1.getResult().getToken().getToken());
			System.out.println("U:" + result1.getResult().getU().getU());
		} else if(CodeConstant.BUSI_ERROR.equals(result1.getResultCode())) {
			System.err.println("注册不成功");
			List<JReason> desc=result1.getDescList();
			for (JReason jReason : desc) {
				System.out.println(jReason.getC() + " " + jReason.getD());
			}
		}
	}
}