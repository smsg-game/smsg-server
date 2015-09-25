package com.lodogame.game.server.context;

import java.util.List;

import com.lodogame.game.server.filter.RequestFilter;

/**
 * Filter容器
 * @author CJ
 *
 */
public class FilterContext {
	private List<RequestFilter> filters;

	public List<RequestFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<RequestFilter> filters) {
		this.filters = filters;
	}
}
