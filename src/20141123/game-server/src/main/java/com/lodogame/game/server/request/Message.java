package com.lodogame.game.server.request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息对象，包含action、sessionid和参数列表
 * @author CJ
 *
 */
public class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//action name
	protected String act;
	//返回状态码
	protected int rc = 1000;
	
	protected String reqId;
	
	//请求时间
	protected long rt;
	
	
	public long getRt() {
		return rt;
	}

	public void setRt(long rt) {
		this.rt = rt;
	}

	public String getReqId() {
		return reqId;
	}

	public void setReqId(String reqId) {
		this.reqId = reqId;
	}

	protected Map<String, Object> dt;

	public String getAct() {
		return act;
	}

	public void setAct(String action) {
		this.act = action;
	}

	public void setAttribate(String name, Object value){
		if(dt == null){
		    dt = new HashMap<String, Object>();
		}
		if(value != null){
			this.dt.put(name, value);
		}
	}
	
	public Object getAttribate(String name){
		if(dt == null){
			dt = new HashMap<String, Object>();
		}
		return dt.get(name);
	}
	
	public int getRc() {
		return rc;
	}

	public void setRc(int rc) {
		this.rc = rc;
	}

	public Map<String, Object> getDt() {
		return dt;
	}

	public void setDt(Map<String, Object> dt) {
		this.dt = dt;
	}
}
