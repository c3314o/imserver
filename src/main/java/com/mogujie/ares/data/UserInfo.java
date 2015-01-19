package com.mogujie.ares.data;

/**
 * 用户信息表
 * @author yesong
 *
 */
	public class UserInfo {
		
	  	protected String userId; // 用户id

	    protected String uname; // 用户名

	    protected String unick; // 用户昵称

	    protected String avatar; // 用户头像

	    protected String title; // 职务

	    protected String position; // 地址，为什么不是address？

	    protected byte status; // 用户在职状态 0:正常(在职) 1:删除(离职) 可扩展

	    protected byte sex; // 性别

	    protected byte userType = 0; // 即identity，用户身份标识, 0-普通用户 1-管理员

	    private String departId; // 部门id

	    private String jobNumber; // 工号

	    private String telphone; // 电话

	    private String mail; // 邮箱

	    private long created; // 创建时间

	    private long updated; // 更新时间

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getUname() {
			return uname;
		}

		public void setUname(String uname) {
			this.uname = uname;
		}

		public String getUnick() {
			return unick;
		}

		public void setUnick(String unick) {
			this.unick = unick;
		}

		public String getAvatar() {
			return avatar;
		}

		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getPosition() {
			return position;
		}

		public void setPosition(String position) {
			this.position = position;
		}

		public byte getStatus() {
			return status;
		}

		public void setStatus(byte status) {
			this.status = status;
		}

		public byte getSex() {
			return sex;
		}

		public void setSex(byte sex) {
			this.sex = sex;
		}

		public byte getUserType() {
			return userType;
		}

		public void setUserType(byte userType) {
			this.userType = userType;
		}

		public String getDepartId() {
			return departId;
		}

		public void setDepartId(String departId) {
			this.departId = departId;
		}

		public String getJobNumber() {
			return jobNumber;
		}

		public void setJobNumber(String jobNumber) {
			this.jobNumber = jobNumber;
		}

		public String getTelphone() {
			return telphone;
		}

		public void setTelphone(String telphone) {
			this.telphone = telphone;
		}

		public String getMail() {
			return mail;
		}

		public void setMail(String mail) {
			this.mail = mail;
		}

		public long getCreated() {
			return created;
		}

		public void setCreated(long created) {
			this.created = created;
		}

		public long getUpdated() {
			return updated;
		}

		public void setUpdated(long updated) {
			this.updated = updated;
		}
	}
