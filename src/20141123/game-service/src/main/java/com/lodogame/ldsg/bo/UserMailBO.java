package com.lodogame.ldsg.bo;

import java.util.Date;
import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class UserMailBO {

	@Mapper(name = "mid")
	private int userMailId;

	@Mapper(name = "tp")
	private int type;

	@Mapper(name = "st")
	private int status;

	@Mapper(name = "rst")
	private int receiveStatus;

	@Mapper(name = "content")
	private String content;

	@Mapper(name = "tt")
	private String title;

	@Mapper(name = "ct")
	private Date createdTime;

	@Mapper(name = "et")
	private Date expiredTime;

	@Mapper(name = "tls")
	private List<DropToolBO> dropToolBOList;

	public int getUserMailId() {
		return userMailId;
	}

	public void setUserMailId(int userMailId) {
		this.userMailId = userMailId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getReceiveStatus() {
		return receiveStatus;
	}

	public void setReceiveStatus(int receiveStatus) {
		this.receiveStatus = receiveStatus;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Date expiredTime) {
		this.expiredTime = expiredTime;
	}

	public List<DropToolBO> getDropToolBOList() {
		return dropToolBOList;
	}

	public void setDropToolBOList(List<DropToolBO> dropToolBOList) {
		this.dropToolBOList = dropToolBOList;
	}

}
