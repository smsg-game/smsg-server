package com.lodogame.ldsg.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class ToolExchangeReceiveBO implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
		 * 兑换英雄列表
		 */
		@Mapper(name = "hls")
		private List<UserHeroBO> userHeroBOList = new ArrayList<UserHeroBO>();

		/**
		 * 兑换装备列表	
		 */
		@Mapper(name = "eqs")
		private List<UserEquipBO> userEquipBOList = new ArrayList<UserEquipBO>();
		
		/**
		 * 兑换道具列表
		 */
		@Mapper(name = "tls")
		private List<UserToolBO> userToolBOList = new ArrayList<UserToolBO>();
		
		/**
		 * 金币
		 */
		@Mapper(name = "gd")
		private int gold;
		
		@Mapper(name = "co")
		private int copper;
		
		/**
		 * 体力
		 */
		@Mapper(name= "pw")
		private int power;
		
		@Mapper(name = "exp")
		private int exp;
		
		@Mapper(name = "hb")
		private int heroBag;
		
		@Mapper(name = "eb")
		private int equipBag;

		public List<UserHeroBO> getUserHeroBOList() {
			return userHeroBOList;
		}

		public void setUserHeroBOList(List<UserHeroBO> userHeroBOList) {
			this.userHeroBOList = userHeroBOList;
		}

		public List<UserEquipBO> getUserEquipBOList() {
			return userEquipBOList;
		}

		public void setUserEquipBOList(List<UserEquipBO> userEquipBOList) {
			this.userEquipBOList = userEquipBOList;
		}

		public List<UserToolBO> getUserToolBOList() {
			return userToolBOList;
		}

		public void setUserToolBOList(List<UserToolBO> userToolBOList) {
			this.userToolBOList = userToolBOList;
		}

		public int getGold() {
			return gold;
		}

		public void setGold(int gold) {
			this.gold = gold;
		}

		public int getCopper() {
			return copper;
		}

		public void setCopper(int copper) {
			this.copper = copper;
		}

		public int getPower() {
			return power;
		}

		public void setPower(int power) {
			this.power = power;
		}

		public int getExp() {
			return exp;
		}

		public void setExp(int exp) {
			this.exp = exp;
		}

		public int getHeroBag() {
			return heroBag;
		}

		public void setHeroBag(int heroBag) {
			this.heroBag = heroBag;
		}

		public int getEquipBag() {
			return equipBag;
		}

		public void setEquipBag(int equipBag) {
			this.equipBag = equipBag;
		}
}
