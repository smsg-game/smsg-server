package com.lodogame.ldsg.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.math.RandomUtils;

import com.lodogame.game.utils.json.Json;
import com.lodogame.ldsg.bo.UserTowerMapDataBO;
import com.lodogame.ldsg.constants.TowerObjType;
import com.lodogame.model.SystemFloorObj;
import com.lodogame.model.SystemTowerMap;

public class TowerHelper {
	public static int mapCount;
	
	/**
	 * 获得随机地图
	 */
	private static List<SystemTowerMap> maps = new ArrayList<SystemTowerMap>();
	
	/**
	 * 楼层怪物数据
	 */
	private static Map<Integer, List<SystemFloorObj>> floorForces = new HashMap<Integer, List<SystemFloorObj>>();
	
	/**
	 * 楼层宝箱数据
	 */
	private static Map<Integer, List<SystemFloorObj>> floorBoxs = new HashMap<Integer, List<SystemFloorObj>>();

	public static SystemTowerMap getRandomOneMapData(){
		int index = RandomUtils.nextInt(maps.size());
		return maps.get(index);
	}
	
	/**
	 * 随机获得部队
	 * @param floor
	 * @return
	 */
	public static List<SystemFloorObj> getRandomForces(int floor) {
		List<SystemFloorObj> ret = new ArrayList<SystemFloorObj>();
		List<SystemFloorObj> forcesList = floorForces.get(floor);
		for(SystemFloorObj sff : forcesList){
			int rand = RandomUtils.nextInt(10000);
			if(sff.getMinRand() < rand && sff.getMaxRand() > rand){
				ret.add(sff);
			}
		}
		return ret;
	}

	/**
	 * 获得开启的位置
	 * @param blockMask
	 * @param map 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Set<Integer> getEnableMasks(String blockMask, SystemTowerMap map) {
		Set<Integer> enableMaskIdx = new TreeSet<Integer>();
		List<Integer> masks = Json.toObject(blockMask, List.class);
		for(int i = 0; i < masks.size(); i++){
			int mask = masks.get(i);
			//排除入口和出口
			if(mask == 1 && mask != map.getStartPos() && mask != map.getEndPos()){
				enableMaskIdx.add(i);
			}
		}
		return enableMaskIdx;
	}

	/**
	 * 获得随机宝箱位置
	 * @param floor
	 * @return
	 */
	public static List<SystemFloorObj> getRandomBox(int floor) {
		List<SystemFloorObj> ret = new ArrayList<SystemFloorObj>();
		List<SystemFloorObj> boxList = floorBoxs.get(floor);
		for(SystemFloorObj sff : boxList){
			int rand = RandomUtils.nextInt(10000);
			if(sff.getMinRand() < rand && sff.getMaxRand() > rand){
				ret.add(sff);
			}
		}
		return ret;
	}

	/**
	 * 指定怪物和宝箱位置，生成用户地图数据
	 * @param enableMaskIdxs
	 * @param forcesList
	 * @param boxList
	 * @param map 
	 * @return
	 */
	public static List<UserTowerMapDataBO> creteUserTowerMapDataBOs(int floor, Set<Integer> enableMaskIdxs, List<SystemFloorObj> forcesList, List<SystemFloorObj> boxList, SystemTowerMap map) {
		List<UserTowerMapDataBO> bos = new ArrayList<UserTowerMapDataBO>();
		int bossIdx = map.getEndPos();
		enableMaskIdxs.remove(bossIdx);
		for(SystemFloorObj forces : forcesList){
			UserTowerMapDataBO bo = null;
			//是BOSS时，直接放到出口
			if(forces.getIsBoss() == 1){
				bo = packUserTowerMapDataBO(floor, forces.getObjId(), TowerObjType.MONSTER, bossIdx, map.getTowerMapId());
			}else{
				bo = makeObjPosData(floor, enableMaskIdxs, forces.getObjId(), TowerObjType.MONSTER, map.getTowerMapId());
			}
			bos.add(bo);
		}
		
		for(SystemFloorObj box : boxList){
			UserTowerMapDataBO bo = makeObjPosData(floor, enableMaskIdxs, box.getObjId(), TowerObjType.BOX, map.getTowerMapId());
			bos.add(bo);
		}
		
		return bos;
	}

	/**
	 * 生成指定物品位置数据
	 * @param floor
	 * @param enableMaskIdxs
	 * @param objId
	 * @param objType
	 * @return
	 */
	private static UserTowerMapDataBO makeObjPosData(int floor, Set<Integer> enableMaskIdxs, Integer objId, int objType, int mapId) {
		int idx = RandomUtils.nextInt(enableMaskIdxs.size());
		Integer[] its = new Integer[enableMaskIdxs.size()];
		enableMaskIdxs.toArray(its);
		int pos = its[idx];
		UserTowerMapDataBO bo = packUserTowerMapDataBO(floor, objId, objType, pos, mapId);
		enableMaskIdxs.remove(pos);
		return bo;
	}

	private static UserTowerMapDataBO packUserTowerMapDataBO(int floor, Integer objId, int objType, int idx, int mapId) {
		UserTowerMapDataBO bo = new UserTowerMapDataBO();
		bo.setFloor(floor);
		bo.setMapId(mapId);
		bo.setObjType(objType);
		bo.setPassed(0);
		bo.setObjId(objId);
		bo.setPosition(idx);
		return bo;
	}

	/**
	 * 设置地图数据
	 * @param towerMaps
	 */
	public static void setSystemTowerMaps(List<SystemTowerMap> towerMaps) {
		maps = towerMaps;
	}

	public static void init() {
		maps.clear();
		floorBoxs.clear();
		floorForces.clear();
	}

	/**
	 * 初始化增加部队数据
	 * @param floor
	 * @param obj
	 */
	public static void addForces(int floor, SystemFloorObj obj) {
		List<SystemFloorObj> forcesList = floorForces.get(floor);
		if(forcesList == null){
			forcesList = new ArrayList<SystemFloorObj>();
		}
		forcesList.add(obj);
		floorForces.put(floor, forcesList);
	}

	/**
	 * 初始化增加宝箱数据
	 * @param floor
	 * @param obj
	 */
	public static void addBox(int floor, SystemFloorObj obj) {
		List<SystemFloorObj> boxsList = floorBoxs.get(floor);
		if(boxsList == null){
			boxsList = new ArrayList<SystemFloorObj>();
		}
		boxsList.add(obj);
		floorBoxs.put(floor, boxsList);
	}

	public static SystemFloorObj getFloorForces(int floor, int forcesId) {
		List<SystemFloorObj> forcesList = floorForces.get(floor);
		for(SystemFloorObj forces : forcesList){
			if(forces.getObjId() == forcesId){
				return forces;
			}
		}
		return null;
	}

	public static void main(String[] args) {
		
	}
}
