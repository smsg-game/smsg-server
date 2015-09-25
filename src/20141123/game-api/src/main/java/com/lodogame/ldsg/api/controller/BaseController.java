package com.lodogame.ldsg.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

public class BaseController {

	protected ModelAndView renderJson(Map<String, Object> params) {

		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.setAttributesMap(params);
		modelView.setView(view);

		return modelView;
	}
	protected ModelAndView renderJson(Object o) {

		ModelAndView modelView = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", o);
		view.setAttributesMap(map);
		modelView.setView(view);
		return modelView;
	}
	
	protected ModelAndView renderJson() {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rc", 1000);
		return renderJson(map);

	}

	protected ModelAndView renderHtml() {

		return null;
	}

}
