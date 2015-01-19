package com.mogujie.ares.data;

/**
 * 用户登录信息表
 * @author yesong
 *
 */
public class User{
	
	private String id; //ID
	
	private String loginName; //登录名
	
	private String psw;//密码
	
	private byte status;// 登录状态 0:未登录,1:已登录
	
	private long loginIp;//登录IP
	
	private long loginTime;//上线时间
	
	private long logoutTime;// 下线时间

	private UserInfo userInfo; //用户信息对象

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPsw() {
		return psw;
	}

	public void setPsw(String psw) {
		this.psw = psw;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public long getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(long loginIp) {
		this.loginIp = loginIp;
	}

	public long getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}

	public long getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(long logoutTime) {
		this.logoutTime = logoutTime;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
}
