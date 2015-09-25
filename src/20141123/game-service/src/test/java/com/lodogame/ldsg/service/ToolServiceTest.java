package com.lodogame.ldsg.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.lodogame.ldsg.constants.ToolType;
import com.lodogame.ldsg.constants.ToolUseType;

@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class ToolServiceTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private ToolService toolService;

	@Test
	public void testAdd() {

		Map<String, String> m = new HashMap<String, String>();
		m.put("4723596bc245495db8b632bc39240104", "tk00000002");
		m.put("92575eff1a7c4d1c8313213991749498", "tk00000003");
		m.put("8937516af8284d149dd17a4d95c1e649", "tk00000004");
		m.put("49ff94dbe9ac4b3f953a1870718cb32b", "tk00000005");
		m.put("b5d3b0daae9d43b3bce7dffe7a758233", "tk00000006");
		m.put("711615315fbd495a84bebb3f6f0d01da", "tk00000007");
		m.put("f9d72437a9d4475aa282c614a7c4644e", "tk00000008");
		m.put("559750ae595e4e27a60052ec64cdfc39", "tk00000009");

		for (Map.Entry<String, String> entry : m.entrySet()) {

			this.toolService.addTool(entry.getKey(), 1001, 11001, 1000, 20001);
			this.toolService.addTool(entry.getKey(), 1001, 11002, 1000, 20001);
			this.toolService.addTool(entry.getKey(), 1001, 11003, 1000, 20001);
			this.toolService.addTool(entry.getKey(), 1001, 11004, 1000, 20001);

		}

	}
	
	/**
	 * 向用户插入武将
	 */
	@Test
	public void testGiveTool() {
//		toolService.giveTools("711615315fbd495a84bebb3f6f0d01da", ToolType.HERO, 812, 2, ToolUseType.ADD_FORCES);
//		toolService.giveTools("711615315fbd495a84bebb3f6f0d01da", ToolType.HERO, 733, 5, ToolUseType.ADD_FORCES);
//		toolService.giveTools("711615315fbd495a84bebb3f6f0d01da", ToolType.HERO, 813, 5, ToolUseType.ADD_FORCES);
//		toolService.giveTools("711615315fbd495a84bebb3f6f0d01da", ToolType.HERO, 138, 10, ToolUseType.ADD_FORCES);
//		toolService.giveTools("711615315fbd495a84bebb3f6f0d01da", ToolType.HERO, 12, 10, ToolUseType.ADD_FORCES);
//		toolService.giveTools("711615315fbd495a84bebb3f6f0d01da", ToolType.HERO, 12, 10, ToolUseType.ADD_FORCES);
//		toolService.giveTools("711615315fbd495a84bebb3f6f0d01da", ToolType.HERO, 769, 20, ToolUseType.ADD_FORCES);
//		toolService.giveTools("711615315fbd495a84bebb3f6f0d01da", ToolType.HERO, 570, 20, ToolUseType.ADD_FORCES);
//		toolService.giveTools("711615315fbd495a84bebb3f6f0d01da", ToolType.HERO, 438, 20, ToolUseType.ADD_FORCES);
//		toolService.giveTools("711615315fbd495a84bebb3f6f0d01da", ToolType.HERO, 306, 20, ToolUseType.ADD_FORCES);
//		
//		toolService.giveTools("711615315fbd495a84bebb3f6f0d01da", ToolType.HERO, 9904, 20, ToolUseType.ADD_FORCES);
//		toolService.giveTools("711615315fbd495a84bebb3f6f0d01da", ToolType.HERO, 770, 20, ToolUseType.ADD_FORCES);
//		toolService.giveTools("711615315fbd495a84bebb3f6f0d01da", ToolType.HERO, 439, 20, ToolUseType.ADD_FORCES);
//		toolService.giveTools("711615315fbd495a84bebb3f6f0d01da", ToolType.HERO, 307, 20, ToolUseType.ADD_FORCES);
//		toolService.giveTools("711615315fbd495a84bebb3f6f0d01da", ToolType.HERO, 175, 20, ToolUseType.ADD_FORCES);
		
		toolService.giveTools("78449f8a35dc4f779dc2cd895ae158f3", ToolType.HERO, 812, 2, ToolUseType.ADD_FORCES);
		toolService.giveTools("78449f8a35dc4f779dc2cd895ae158f3", ToolType.HERO, 733, 5, ToolUseType.ADD_FORCES);
		toolService.giveTools("78449f8a35dc4f779dc2cd895ae158f3", ToolType.HERO, 813, 5, ToolUseType.ADD_FORCES);
		toolService.giveTools("78449f8a35dc4f779dc2cd895ae158f3", ToolType.HERO, 138, 10, ToolUseType.ADD_FORCES);
		toolService.giveTools("78449f8a35dc4f779dc2cd895ae158f3", ToolType.HERO, 12, 10, ToolUseType.ADD_FORCES);
		toolService.giveTools("78449f8a35dc4f779dc2cd895ae158f3", ToolType.HERO, 12, 10, ToolUseType.ADD_FORCES);
		toolService.giveTools("78449f8a35dc4f779dc2cd895ae158f3", ToolType.HERO, 769, 20, ToolUseType.ADD_FORCES);
		toolService.giveTools("78449f8a35dc4f779dc2cd895ae158f3", ToolType.HERO, 570, 20, ToolUseType.ADD_FORCES);
		toolService.giveTools("78449f8a35dc4f779dc2cd895ae158f3", ToolType.HERO, 438, 20, ToolUseType.ADD_FORCES);
		toolService.giveTools("78449f8a35dc4f779dc2cd895ae158f3", ToolType.HERO, 306, 20, ToolUseType.ADD_FORCES);
		
		toolService.giveTools("78449f8a35dc4f779dc2cd895ae158f3", ToolType.HERO, 9904, 20, ToolUseType.ADD_FORCES);
		toolService.giveTools("78449f8a35dc4f779dc2cd895ae158f3", ToolType.HERO, 770, 20, ToolUseType.ADD_FORCES);
		toolService.giveTools("78449f8a35dc4f779dc2cd895ae158f3", ToolType.HERO, 439, 20, ToolUseType.ADD_FORCES);
		toolService.giveTools("78449f8a35dc4f779dc2cd895ae158f3", ToolType.HERO, 307, 20, ToolUseType.ADD_FORCES);
		toolService.giveTools("78449f8a35dc4f779dc2cd895ae158f3", ToolType.HERO, 175, 20, ToolUseType.ADD_FORCES);
		
		
	}

}
