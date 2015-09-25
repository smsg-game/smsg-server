package com.easou.sso.sdk.example;

import java.util.Scanner;

import com.easou.sso.sdk.AuthAPI;
import com.easou.sso.sdk.UserAPI;
import com.easou.sso.sdk.service.AuthBean;
import com.easou.sso.sdk.service.EucAPIException;
import com.easou.sso.sdk.service.EucApiResult;
import com.easou.sso.sdk.service.EucToken;
import com.easou.sso.sdk.service.JUser;

public class BindMobileExample {

	public static void main(String[] args) throws EucAPIException {
		EucApiResult<AuthBean> result = AuthAPI.login("test", "111111", false, null);
		EucToken eucToken = result.getResult().getToken();
		String token = eucToken.getToken();
		System.out.println("请输入你要绑定的手机号(id 1000) :");
		Scanner in = new Scanner(System.in);
		String mobile = in.nextLine();
		UserAPI.requestBindMobile(token, mobile, null);		
		System.out.println("请输入你的验证码(id 1000) :");
		in=new Scanner(System.in);
		String veriCode = in.nextLine();
		in.close();
		EucApiResult<JUser> result1 = UserAPI.applyBindMobile(token, mobile, veriCode, null);
		System.out.println(result1.getResult().getMobile());
	}
}