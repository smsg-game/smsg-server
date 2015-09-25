package com.easou.sso.sdk.example;

import com.easou.sso.sdk.UserAPI;
import com.easou.sso.sdk.service.CodeConstant;
import com.easou.sso.sdk.service.EucAPIException;
import com.easou.sso.sdk.service.EucApiResult;
import com.easou.sso.sdk.service.JUser;

public class GetUserExample {

	public static void main(String[] args) throws EucAPIException {
		EucApiResult<JUser> result = UserAPI.getUserById(22689818l, null);
		if(CodeConstant.OK.equals(result.getResultCode())) {
			JUser juser=result.getResult();
			System.out.println("登录成功: id " + juser.getId() + " 用户名 "  + juser.getName());
		}
	}
}