package com.lodogame.ldsg.filter;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.lodogame.game.server.constant.CodeConstants;
import com.lodogame.game.server.exception.RequestFilterException;
import com.lodogame.game.server.filter.RequestFilter;
import com.lodogame.game.server.request.Message;
import com.lodogame.game.server.request.Request;
import com.lodogame.game.server.response.Response;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.ldsg.bo.UserToken;
import com.lodogame.ldsg.manager.TokenManager;

/**
 * 登陆验证过滤器
 * 
 * @author CJ
 * 
 */
public class LoginFilter implements RequestFilter {

	// 无需过滤的action列表
	private List<String> exceptAction;

	public List<String> getExceptAction() {
		return exceptAction;
	}

	public void setExceptAction(List<String> exceptAction) {
		this.exceptAction = exceptAction;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void filter(Request request, Response response) throws RequestFilterException {
		String action = request.getMethodName();
		
		if(action.endsWith("Rq") || action.startsWith("push")){
			return;
		}
		
		if (exceptAction != null && !exceptAction.isEmpty() && exceptAction.contains(action)) {
			return;
		}
		
		UserToken userToken = (UserToken) request.getSession().getAttribute("userToken");
		if (userToken == null) {
			Message message = (Message)response.getMessage();
			message.setAct("error");
			message.setRc(CodeConstants.IS_NOT_LOGIN);
			response.setMessage(message);
			throw new RequestFilterException(request, response);
		}
	}

}
