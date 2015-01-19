package com.mogujie.ares.configure;

/**
 * 
 * @author sye
 *
 */
public class ProtocolConstant {


	/** SERVICE ID */
		
		/** 登录 SERVICE ID */
		public static final int SID_LOGIN = 1;
		/** 联系人 SERVICE ID */
		public static final int SID_BUDDY_LIST = 2; 
		/** 消息SERVICE ID */
		public static final int SID_MSG = 3; 
		
		public static final int SID_SWITCH_SERVER = 4;
		/** 群SERVICE ID */
		public static final int SID_GROUP = 5; 
		/** 文件SERVICE ID */
		public static final int SID_FILE = 6;
		public static final int SID_OTHER = 0x0007;
		public static final int SID_DEFAULT = 0x0007;
	
	/** COMMAND ID */
	// command id for group message
	public static final int CID_GROUP_LIST_REQUEST = 1; //获取所有群列表
	public static final int CID_GROUP_LIST_RESPONSE = 2;
	public static final int CID_GROUP_USER_LIST_REQUEST = 3;
	public static final int CID_GROUP_USER_LIST_RESPONSE = 4;
	public static final int CID_GROUP_UNREAD_CNT_REQUEST = 5; //获取未读消息群列表
	public static final int CID_GROUP_UNREAD_CNT_RESPONSE = 6;
	public static final int CID_GROUP_UNREAD_MSG_REQUEST = 7;
	public static final int CID_GROUP_UNREAD_MSG_RESPONSE = 8;
	public static final int CID_GROUP_HISTORY_MSG_REQUEST = 9;
	public static final int CID_GROUP_HISTORY_MSG_RESPONSE = 10;
	public static final int CID_GROUP_MSG_READ_ACK = 11;
	public static final int CID_GROUP_CREATE_TMP_GROUP_REQUEST = 12;
	public static final int CID_GROUP_CREATE_TMP_GROUP_RESPONSE = 13;
	public static final int CID_GROUP_CHANGE_MEMBER_REQUEST = 14;
	public static final int CID_GROUP_CHANGE_MEMBER_RESPONSE = 15;
	public static final int CID_GROUP_DIALOG_LIST_REQUEST = 16;
	public static final int CID_GROUP_DIALOG_LIST_RESPONSE = 17;
	public static final int CID_GROUP_CREATE_NORMAL_GROUP_NOTIFY = 18; 
	public static final int CID_GROUP_CHANGE_MEMEBER_NOTIFY = 19;
	public static final int CID_GROUP_MAX = 21;

	// command id for buddy list
	public static final int CID_BUDDY_LIST_REQUEST = 1; //获取所有最近联系人
	public static final int CID_BUDDY_LIST_FRIEND_LIST = 3; //
	public static final int CID_BUDDY_LIST_ONLINE_FRIEND_LIST = 4; //
	public static final int CID_BUDDY_LIST_STATUS_NOTIFY = 5; //
	public static final int CID_BUDDY_LIST_USER_STATUS_REQUEST = 8; //
	public static final int CID_BUDDY_LIST_USER_STATUS_RESPONSE = 9; //
	public static final int CID_BUDDY_LIST_USER_INFO_RESPONSE = 10; //
	public static final int CID_BUDDY_LIST_USER_INFO_REQUEST = 11;
	public static final int CID_BUDDY_LIST_REMOVE_SESSION_REQ = 12;
	public static final int CID_BUDDY_LIST_REMOVE_SESSION_RES = 13;
	public static final int CID_BUDDY_LIST_ALL_USER_REQUEST = 14;//获取所有联系人 请求
	public static final int CID_BUDDY_LIST_ALL_USER_RESPONSE = 15;//获取所有联系人 响应
	public static final int CID_BUDDY_LIST_USERS_STATUS_REQUEST = 16;
	public static final int CID_BUDDY_LIST_USERS_STATUS_RESPONSE = 17;
	public static final int CID_BUDDY_LIST_DEPARTMENT_REQUEST = 18;//获取所有部门
	public static final int CID_BUDDY_LIST_DEPARTMENT_RESPONSE = 19;
	public static final int CID_BUDDY_LIST_MAX = 21;

	public static final int USER_MSG_TYPE = 1;

	public static final int IM_PDU_VERSION = 3;
	
	    // COMMAND_ID FOR MSG
		public static final int CID_MSG_DATA = 1;
		public static final int CID_MSG_DATA_ACK = 2;
		public static final int CID_MSG_READ_ACK = 3;
		public static final int CID_MSG_TIME_REQUEST = 5;
		public static final int CID_MSG_TIME_RESPONSE = 6;
		public static final int CID_MSG_UNREAD_CNT_REQUEST = 7; //获取所有未读消息联系人集合
		public static final int CID_MSG_UNREAD_CNT_RESPONSE = 8;
		public static final int CID_MSG_UNREAD_MSG_REUQEST = 9;
		public static final int CID_MSG_HISTORY_MSG_REQUEST = 10;
		// public static final int CID_MSG_LIST_RESPONSE = 11;
		public static final int CID_MSG_HISTORY_SERVICE_MSG_REQUEST = 12;
		public static final int CID_MSG_HISTORY_SERVICE_MSG_RESPONSE = 13;

		public static final int CID_MSG_UNREAD_MSG_RESPONSE = 14;
		public static final int CID_MSG_HISTORY_MSG_RESPONSE = 15;
		public static final int CID_MSG_MAX = 17;

		public static final int CID_SHOP_MEMBER_RESPONSE = 2;
		public static final int CID_CONTACT_RECENT_RESPONSE = 3;
		public static final int CID_CONTACT_FRIEND_STATUS_NOTIYF = 5;
		public static final int CID_QUERY_USER_ONLINE_STATUS_REQUEST = 8;
		public static final int CID_QUERY_USER_ONLINE_STATUS_RESPONSE = 9;

	
}
