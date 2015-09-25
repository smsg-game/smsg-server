package com.lodogame.ldsg.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.action.BaseRequestAction;
import com.lodogame.game.server.request.Request;
import com.lodogame.game.server.response.Response;
import com.lodogame.game.utils.JedisUtils;
import com.lodogame.game.utils.RedisKey;
import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.constants.ServiceReturnCode;
import com.lodogame.ldsg.exception.ServiceException;
import com.lodogame.ldsg.render.ViewRender;

/**
 * 逻辑请求处理器基类
 * 
 * @author jacky
 * 
 */
public class LogicRequestAction extends BaseRequestAction {

	private static final Logger logger = Logger.getLogger(LogicRequestAction.class);

	@Autowired
	private ViewRender render;

	private Map<String, Object> params = new HashMap<String, Object>();

	public String getUid() {
		return (String) this.request.getParameter("uid");
	}

	public Response render() {
		params.put("uid", getUid());
		return render.render(this.response, this.request, params);
	}

	public Response render(int rc) {
		params.put("uid", getUid());
		return render.render(this.response, this.request, params, rc);
	}

	/**
	 * 设置参数
	 * 
	 * @param key
	 * @param value
	 */
	public void set(String key, Object value) {
		params.put(key, value);
	}

	public Object getParam(String key) {
		return params.get(key);
	}

	/**
	 * 获取一个字符串参数
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	protected String getString(String key, String defaultValue) {
		String value = (String) this.request.getParameter(key);
		if (StringUtils.isBlank(value)) {
			return defaultValue;
		}
		return value;
	}

	protected List<?> getList(String key) {
		try {
			return (List<?>) this.request.getParameter(key);
		} catch (Exception e) {
			return new ArrayList();
		}
	}

	/**
	 * 获取一个整形参数
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	protected int getInt(String key, int defaultValue) {
		Object value = (Object) this.request.getParameter(key);
		if (value == null) {
			return defaultValue;
		}
		return Integer.parseInt(value.toString());
	}

	/**
	 * 获取一个 long参数
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	protected long getLong(String key, long defaultValue) {
		Object value = (Object) this.request.getParameter(key);
		if (value == null) {
			return defaultValue;
		}
		return Long.parseLong(value.toString());
	}

	@Override
	public Response handle() throws IOException {
		throw new ServiceException(ServiceReturnCode.FAILD, "逻辑服方法未实现.[" + this.request.getMethodName() + "]");
	}

	@Override
	public void init(Request request, Response response) {
		super.init(request, response);

		String uid = getUid();

		String key = RedisKey.getUserCacheKey();
		String json = JedisUtils.getFieldFromObject(key, uid);

		logger.info("request_log: handlerName=" + request.getRequestHandlerName() + " methodName=" + request.getMethodName() + " userId=" + uid + " User=" + json + " param=" + Json.toJson(request.getParams()));
	}
}
