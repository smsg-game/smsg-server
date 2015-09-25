package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class FactionBO {

	@Mapper(name = "fid")
	private int factionId;

	@Mapper(name = "fn")
	private String factionName;

	@Mapper(name = "fnt")
	private String factionNotice;
	
	//帮主名字
	@Mapper(name = "fm")
	private String factionMaster;
	
	@Mapper(name = "mn")
	private int memberNum;
	
	@Mapper(name = "ml")
	private int memberLimit;

	//帮主Id
	@Mapper(name = "fmid")
	private long factionMasterId;
	
	@Mapper(name = "fids")
	private String factionIdToStr;
	
	
	public long getFactionMasterId() {
		return factionMasterId;
	}

	public void setFactionMasterId(long factionMasterId) {
		this.factionMasterId = factionMasterId;
	}

	public String getFactionIdToStr() {
		return factionIdToStr;
	}

	public void setFactionIdToStr(String factionIdToStr) {
		this.factionIdToStr = factionIdToStr;
	}

	public int getFactionId() {
		return factionId;
	}

	public void setFactionId(int factionId) {
		this.factionId = factionId;
	}

	public String getFactionName() {
		return factionName;
	}

	public void setFactionName(String factionName) {
		this.factionName = factionName;
	}

	public String getFactionNotice() {
		return factionNotice;
	}

	public void setFactionNotice(String factionNotice) {
		this.factionNotice = factionNotice;
	}

	public String getFactionMaster() {
		return factionMaster;
	}

	public void setFactionMaster(String factionMaster) {
		this.factionMaster = factionMaster;
	}

	public int getMemberNum() {
		return memberNum;
	}

	public void setMemberNum(int memberNum) {
		this.memberNum = memberNum;
	}

	public int getMemberLimit() {
		return memberLimit;
	}

	public void setMemberLimit(int memberLimit) {
		this.memberLimit = memberLimit;
	}

}
