package com.lodogame.ldsg.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.game.server.response.Response;
import com.lodogame.ldsg.bo.AttackRankBO;
import com.lodogame.ldsg.bo.DefenceRankBO;
import com.lodogame.ldsg.bo.ForcesRankBO;
import com.lodogame.ldsg.bo.HeroColorRankBO;
import com.lodogame.ldsg.bo.HpRankBO;
import com.lodogame.ldsg.bo.PowerRankBO;
import com.lodogame.ldsg.bo.WealthRankBO;
import com.lodogame.ldsg.service.RankService;

public class RankAction extends LogicRequestAction {
	
	@Autowired
	private RankService rankService;
	/**
	 * 获取用户争霸赛信息
	 * @return
	 */
	public Response getRanks(){
		List<PowerRankBO> powerRanks = rankService.getPowerRanks();
		List<WealthRankBO> wealthRanks = rankService.getWealthRanks();
		List<ForcesRankBO> forcesRanks = rankService.getForcesRanks();
		List<AttackRankBO> attackRanks = rankService.getAttackRanks();
		List<DefenceRankBO> defenceRanks = rankService.getDefenceRanks();
		List<HpRankBO> hpRanks = rankService.getHpRanks();
		List<HeroColorRankBO> heroColorRanks = rankService.getHeroColorRanks();
		long statTime = rankService.getRankStatTime();
		set("pr", powerRanks);
		set("wr", wealthRanks);
		set("fr", forcesRanks);
		set("ar", attackRanks);
		set("dr", defenceRanks);
		set("hr", hpRanks);
		set("hcr", heroColorRanks);
		set("st", statTime);
		return this.render();
	}
}
