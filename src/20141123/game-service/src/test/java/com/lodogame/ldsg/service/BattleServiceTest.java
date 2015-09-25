package com.lodogame.ldsg.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.lodogame.ldsg.bo.BattleBO;
import com.lodogame.ldsg.bo.BattleHeroBO;
import com.lodogame.ldsg.event.Event;
import com.lodogame.ldsg.event.EventHandle;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class BattleServiceTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private BattleService battleService;

	private void initBattleHeroBO(BattleHeroBO bo) {

		Random randon = new Random();

		bo.setLife(50000 + randon.nextInt(50000));
		bo.setPhysicalAttack(50000 + randon.nextInt(30000));
		bo.setPhysicalDefense(30000 + randon.nextInt(20000));
		bo.setPlan(11);
		//bo.setSkill1(1);
		//bo.setSkill2(2);
		bo.setHit(800 + randon.nextInt(800));
		bo.setCrit(800 + randon.nextInt(800));
		bo.setDodge(800 + randon.nextInt(800));
		bo.setParry(800 + randon.nextInt(800));
		bo.setCareer(1);
		bo.setSoliderTokenId(0);
	}

	@Test
	public void testFight() {

		BattleBO attack = new BattleBO();
		BattleBO defense = new BattleBO();

		List<BattleHeroBO> battleHeroBOList = new ArrayList<BattleHeroBO>();

		BattleHeroBO bo1 = new BattleHeroBO();
		bo1.setSystemHeroId(1);
		bo1.setName("张飞");
		bo1.setPos(1);
		this.initBattleHeroBO(bo1);

		BattleHeroBO bo2 = new BattleHeroBO();
		bo2.setSystemHeroId(2);
		bo2.setName("吕布");
		bo2.setPos(2);
		this.initBattleHeroBO(bo2);

		BattleHeroBO bo3 = new BattleHeroBO();
		bo3.setSystemHeroId(3);
		bo3.setName("赵云");
		bo3.setPos(3);
		this.initBattleHeroBO(bo3);

		BattleHeroBO bo4 = new BattleHeroBO();
		bo4.setSystemHeroId(4);
		bo4.setName("貂蝉");
		bo4.setPos(4);
		this.initBattleHeroBO(bo4);

		BattleHeroBO bo5 = new BattleHeroBO();
		bo5.setSystemHeroId(5);
		bo5.setName("张角");
		bo5.setPos(5);
		this.initBattleHeroBO(bo5);

		BattleHeroBO bo6 = new BattleHeroBO();
		bo6.setSystemHeroId(6);
		bo6.setName("袁绍");
		bo6.setPos(6);
		this.initBattleHeroBO(bo6);

		battleHeroBOList.add(bo1);
		battleHeroBOList.add(bo2);
		battleHeroBOList.add(bo3);
		battleHeroBOList.add(bo4);
		battleHeroBOList.add(bo5);
		battleHeroBOList.add(bo6);

		List<BattleHeroBO> defenseBattleHeroBOList = new ArrayList<BattleHeroBO>();

		BattleHeroBO bo7 = new BattleHeroBO();
		bo7.setSystemHeroId(7);
		bo7.setName("魏延");
		bo7.setPos(1);
		this.initBattleHeroBO(bo7);

		BattleHeroBO bo8 = new BattleHeroBO();
		bo8.setSystemHeroId(8);
		bo8.setName("马超");
		bo8.setPos(2);
		this.initBattleHeroBO(bo8);

		BattleHeroBO bo9 = new BattleHeroBO();
		bo9.setSystemHeroId(9);
		bo9.setName("孙策");
		bo9.setPos(3);
		this.initBattleHeroBO(bo9);

		BattleHeroBO bo10 = new BattleHeroBO();
		bo10.setSystemHeroId(10);
		bo10.setName("黄盖");
		bo10.setPos(4);
		this.initBattleHeroBO(bo10);

		BattleHeroBO bo11 = new BattleHeroBO();
		bo11.setSystemHeroId(11);
		bo11.setName("周泰");
		bo11.setPos(5);
		this.initBattleHeroBO(bo11);

		BattleHeroBO bo12 = new BattleHeroBO();
		bo12.setSystemHeroId(12);
		bo12.setName("甘宁");
		bo12.setPos(6);
		this.initBattleHeroBO(bo12);

		defenseBattleHeroBOList.add(bo7);
		defenseBattleHeroBOList.add(bo8);
		defenseBattleHeroBOList.add(bo9);
		defenseBattleHeroBOList.add(bo10);
		defenseBattleHeroBOList.add(bo11);
		defenseBattleHeroBOList.add(bo12);

		attack.setBattleHeroBOList(battleHeroBOList);
		defense.setBattleHeroBOList(defenseBattleHeroBOList);

		final List<Integer> l = new ArrayList<Integer>();

		int i = 1;
		while (i > 0) {

			battleService.fight(attack, defense, 1, new EventHandle() {

				public boolean handle(Event event) {
					l.add(0);
					System.out.println("战斗返回");
					System.out.println(event.getString("report"));
					return true;
				}
			});

			i--;
		}

//		while (true) {
//
//			// System.out.println(l.size());
//
//			if (l.size() >= 1000) {
//				break;
//			}
//
//			try {
//
//				Thread.sleep(1000);
//			} catch (InterruptedException ie) {
//				ie.printStackTrace();
//			}
//
//		}

	}

}
