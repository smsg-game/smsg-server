package com.lodogame.ldsg.bo;


/* 后台管理发送装备道具附件bean  */
public class AdminSendAttachBO{
    
	/**  附件id(装备常量id或者道具常量id) **/
	private int attachId;
	/**  附件类型(1代表道具2代表装备) **/
	private int attachType;
	/**  附件数量 **/
	private int attachNum;
	public AdminSendAttachBO(){}

	public AdminSendAttachBO(int attachId , int attachType , int attachNum){
		this.attachId=attachId;
		this.attachType=attachType;
		this.attachNum=attachNum;
	}

	public void setAttachId(int attachId) {
		this.attachId = attachId;
	}

	public int getAttachId() {
		return attachId;
	}

	public void setAttachType(int attachType) {
		this.attachType = attachType;
	}

	public int getAttachType() {
		return attachType;
	}

	public void setAttachNum(int attachNum) {
		this.attachNum = attachNum;
	}

	public int getAttachNum() {
		return attachNum;
	}

}