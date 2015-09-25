package com.lodogame.ldsg.bo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class DeifyBo {
	@Mapper(name = "did")
	private int deifyId;
	
	@Mapper(name = "sn")	
	private int startNum;
	
	@Mapper(name = "tp")
	private int type;
	
	@Mapper(name = "pa")
	private int physicalAttack;
	
	@Mapper(name = "pd")
	private int physicalDefense;
	
	@Mapper(name = "lf")
	private int lift;
	
	@Mapper(name = "tls")
	private List<DropToolBO> dropToolBOs = new ArrayList<DropToolBO>();
	
	@Mapper(name = "ueid")
	private String userEquipId;
	
	@Mapper(name = "ra")
	private int rate;
	
	public String getUserEquipId() {
		return userEquipId;
	}
	public void setUserEquipId(String userEquipId) {
		this.userEquipId = userEquipId;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	public int getDeifyId() {
		return deifyId;
	}
	public void setDeifyId(int deifyId) {
		this.deifyId = deifyId;
	}
	public int getStartNum() {
		return startNum;
	}
	public void setStartNum(int startNum) {
		this.startNum = startNum;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getPhysicalAttack() {
		return physicalAttack;
	}
	public void setPhysicalAttack(int physicalAttack) {
		this.physicalAttack = physicalAttack;
	}
	public int getPhysicalDefense() {
		return physicalDefense;
	}
	public void setPhysicalDefense(int physicalDefense) {
		this.physicalDefense = physicalDefense;
	}
	public int getLift() {
		return lift;
	}
	public void setLift(int lift) {
		this.lift = lift;
	}
	public List<DropToolBO> getDropToolBOs() {
		return dropToolBOs;
	}
	public void setDropToolBOs(List<DropToolBO> dropToolBOs) {
		this.dropToolBOs = dropToolBOs;
	}
	
	
}
