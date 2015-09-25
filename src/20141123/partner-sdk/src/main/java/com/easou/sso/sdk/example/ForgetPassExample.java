package com.easou.sso.sdk.example;

import java.util.List;
import java.util.Scanner;

import com.easou.sso.sdk.AuthAPI;
import com.easou.sso.sdk.service.AuthBean;
import com.easou.sso.sdk.service.CodeConstant;
import com.easou.sso.sdk.service.EucAPIException;
import com.easou.sso.sdk.service.EucApiResult;
import com.easou.sso.sdk.service.JReason;

public class ForgetPassExample {

	/**
	 * 本示例
	 * 
	 * @param args
	 * @throws EucAPIException
	 */
	public static void main(String[] args) throws EucAPIException {
		System.out.println("请输入你的手机号 :");
		Scanner in = new Scanner(System.in);
		String mobile = in.nextLine();
		EucApiResult<String> result = AuthAPI.requestResetPass(mobile, null);
		if(CodeConstant.OK.equals(result.getResultCode())) {
			// 成功发送短信
			System.out.println("请输入你收到的验证码 :");
			in = new Scanner(System.in);
			String veriCode = in.nextLine();
			System.out.println("请输入你的新密码");
			String newpwd = in.nextLine();
			in.close();
			EucApiResult<AuthBean> result1 = AuthAPI.applyResetPass(mobile, newpwd, veriCode, null);
			if(CodeConstant.OK.equals(result1.getResultCode())) {
				System.out.println("登录成功，TOKEN: " + result1.getResult().getToken());
			} else {
				List<JReason> descList=result1.getDescList();
				for (JReason jReason : descList) {
					System.out.println(jReason.getC() + " " + jReason.getD());
				}
			}
		} else {
			List<JReason> desc = result.getDescList();
			for (JReason jReason : desc) {
				System.err.println(jReason.getC() + " " + jReason.getD());
			}
		}
	}
}
