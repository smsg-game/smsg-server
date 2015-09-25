package com.lodogame.ldsg.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter {

	private FilterConfig filterConfig;
	private FilterChain chain;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	@Override
	public void destroy() {
		this.filterConfig = null;
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain chain) throws IOException, ServletException {
		this.chain = chain;
		this.request = (HttpServletRequest) servletRequest;
		this.response = ((HttpServletResponse) servletResponse);
		String url = request.getServletPath();
		if (url == null)
			url = "";
		HttpSession session = request.getSession();
		Boolean login = (Boolean) session.getAttribute("login");
		if (noFileUrl(url, request) || (login != null && login)) { // 不需要判断权限的请求如登录页面，则跳过
			chain.doFilter(request, response);
		} else{
			response.sendRedirect("/manage/index.do");// 返回登录页面
		}
	}

	private boolean noFileUrl(String url, HttpServletRequest request2) {
		if(url.equals("/manage/index.do") || url.equals("/manage/login.do")){
			return true;
		}
		return false;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

}
