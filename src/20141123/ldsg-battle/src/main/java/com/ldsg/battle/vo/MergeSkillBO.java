package com.ldsg.battle.vo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author jacky
 * 
 */
public class MergeSkillBO {

	private int skillId;

	private String pos;

	private Set<String> posList = new HashSet<String>();

	private List<BuffVO> buffVOList = new ArrayList<BuffVO>();

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public Set<String> getPosList() {
		return posList;
	}

	public void setPosList(Set<String> posList) {
		this.posList = posList;
	}

	public List<BuffVO> getBuffVOList() {
		return buffVOList;
	}

	public void setBuffVOList(List<BuffVO> buffVOList) {
		this.buffVOList = buffVOList;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

}
