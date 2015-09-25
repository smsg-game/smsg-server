package com.lodogame.game.server.action;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.lodogame.game.server.request.Request;
import com.lodogame.game.server.response.Response;

/**
 * RequestAction 基础类，用于实现一些基础的功能
 * @author CJ
 *
 */
public abstract class BaseRequestAction implements RequestAction {
	
	protected static final Logger LOG = Logger.getLogger(BaseRequestAction.class);
	
	protected Request request;
	protected Response response;
	
	@Override
	public void init(Request request, Response response){
		this.request = request;
		this.response = response;
		initParams();
	}
	
	protected void initParams(){
		Method[] methods = this.getClass().getMethods();
		for(int i = 0; i < methods.length; i++){
			String methodName = methods[i].getName();
			if(methodName.startsWith("set") && methodName.length() > 3){
				String fieldName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
				Object obj = request.getParameter(fieldName);
				try {
					if(obj != null){
						methods[i].invoke(this, obj);
					}
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
	}
	
}
