package com.mogujie.ares.extend.action;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.mogujie.ares.data.User;
import com.mogujie.ares.extend.BaseAction;
import com.mogujie.ares.lib.logger.Logger;
import com.mogujie.ares.lib.logger.LoggerFactory;
import com.mogujie.ares.lib.net.DataBuffer;
import com.mogujie.ares.model.UserModel;
import com.mogujie.ares.util.MoguUtil;

/**
 * 
 * @Description: 用户相关的类
 * @author ziye - ziye[at]mogujie.com
 * @date 2013-8-12 下午4:55:45
 * 
 */
public class UserAction extends BaseAction {

    private static final Logger logger = LoggerFactory
            .getLogger(UserAction.class);

    /**
     * 
     * @Description: 获取用户信息
     * @param fromUserId
     * @param userIds
     * @return
     */
    public DataBuffer getUsersInfo(int fromUserId, int[] userIds,
            DataBuffer attachment, int version) {
        logger.info("get users info : " + Arrays.toString(userIds));
        DataBuffer buffer;
        if (userIds == null || userIds.length <= 0) {
            buffer = new DataBuffer();
            buffer.writeInt(fromUserId);
            buffer.writeInt(0);
            return MoguUtil.writeAttachments(buffer, attachment);
        }

        try {
            Map<String, User> users = UserModel.getInstance().getUserInfo(
                    userIds);
            buffer = new DataBuffer();
            buffer.writeInt(fromUserId); // 发请求的用户
            buffer.writeInt(users.size()); // 查询到详细信息的用户数
            User user;
            Iterator<String> it = users.keySet().iterator();
            String logText = "response userInfo: ";
            while (it.hasNext()) {
                user = users.get(it.next());
                bulidBuffer(user, buffer);
                logText += "userId=" + user.getId() + ", ";
            }
            logger.info(logText);
        } catch (SQLException e) {
            logger.error("get users info error with reason : ", e);
            buffer = new DataBuffer();
            buffer.writeInt(fromUserId);
            buffer.writeInt(0);
        }

        return MoguUtil.writeAttachments(buffer, attachment);
    }

    /**
     * 获取该用户所有联系人
     * @param userId
     * @param attachment
     * @param version
     * @return
     */
    public DataBuffer getAllContantsByUserId(String userId,DataBuffer attachment,
            int version){
    	logger.info("userId:" + userId);
    	if(StringUtils.isBlank(userId)){
    		DataBuffer buffer = new DataBuffer();
    		buffer.writeInt(0);
    		buffer.writeDataBuffer(attachment);
    		return MoguUtil.writeAttachments(buffer, attachment);
    	}
    	DataBuffer buffer = new DataBuffer();
    	try {
			Set<User> contantsUser = UserModel.getInstance().getAllContantsByUserId(userId, attachment, version);
			for (User user : contantsUser) {
				buffer.writeString(user.getId());
//				buffer.writeString(user.getUserInfo().get);
				this.bulidBuffer(user, buffer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return MoguUtil.writeAttachments(buffer, attachment);
    }
    
    /**
     * 
     * @Description: 获取所有用户信息
     * @param fromUserId
     * @return
     */
    public DataBuffer getAllUsersInfo(int fromUserId, DataBuffer attachment,
            int version) {
        logger.info("get all users info by : " + fromUserId);
        DataBuffer buffer;
        if (fromUserId <= 0) {
            buffer = new DataBuffer();
            buffer.writeInt(fromUserId);
            buffer.writeInt(0);
            buffer.writeInt(attachment.readableBytes());
            if (attachment.readableBytes() > 0) {
                buffer.writeDataBuffer(attachment);
            }
            return buffer;
        }

        try {
            Set<User> users = UserModel.getInstance().getAllUserInfo();
            buffer = new DataBuffer();
            buffer.writeInt(fromUserId); // 发请求的用户
            buffer.writeInt(users.size()); // 查询到详细信息的用户数
            User user;
            Iterator<User> it = users.iterator();
            while (it.hasNext()) {
                user = it.next();
                bulidBuffer(user, buffer);
            }
        } catch (SQLException e) {
            logger.error("get all user info error with reason", e);
            buffer = new DataBuffer();
            buffer.writeInt(fromUserId);
            buffer.writeInt(0);
        }

        return MoguUtil.writeAttachments(buffer, attachment);
    }

    /**
     * 添加/删除好友功能
     * @param userId
     * @param fId
     * @param type 
     * @param attachment
     * @param version
     * @return
     */
    public DataBuffer addAndDelFriend(String userId,String fId,byte type,DataBuffer attachment,int version) {
    	logger.info("add or del user");
    	DataBuffer buffer = null;
    	if(StringUtils.isBlank(userId) || StringUtils.isBlank(fId)){
    		buffer = new DataBuffer();
    		buffer.writeString(userId);
    		buffer.writeString(fId);
    		buffer.writeByte(type); 
    		buffer.writeInt(attachment.readableBytes());
    		if(attachment.readableBytes() > 0){
    			buffer.writeDataBuffer(attachment);
    		}
    		return buffer;
    	}
    	User user = null;
    	if(type == 0){
    		// 新增
    		user = addFriend(userId,fId,attachment,version);
    	}
    	else if(type == 1) {
    		// 删除
    		user = delFriend(userId,fId,attachment,version);
    	}
    	else{
    		throw new IllegalAccessError("添加/删除好友类型异常");
    	}
    	buffer = new DataBuffer();
    	bulidBuffer(user, buffer);
    	return MoguUtil.writeAttachments(buffer, attachment);
    } 
    
    /**
     * 删除好友
     * @param userId
     * @param fId
     * @param attachment
     * @param version
     */
     private User delFriend(String userId, String fId, DataBuffer attachment,
			int version) {
    	 try {
			return UserModel.getInstance().delFriend(userId, fId, attachment, version);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return null;
	}

     /**
      * 添加好友
      * @param userId
      * @param fId
      * @param attachment
      * @param version
      */
	private User addFriend(String userId, String fId, DataBuffer attachment,
			int version) {
		try {
			return UserModel.getInstance().addFriend(userId, fId, attachment, version);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
     * @Description: 修改用户头像
     * @param userId
     * 用户Id
     * @param avatar
     * 用户头像url
     * @return
     */
     public DataBuffer alterAvatar(int userId, String avatar,
     DataBuffer attachment, int version) {
     // logger.info("login: " + userId + ", new avatar: " + avatar);
     int resultCode = 0;
     // 数据校验扔到model层,这里不需要判断
     try {
//     resultCode = UserModel.getInstance().alterAvatar(userId, avatar);
     } catch (Exception e) {
     resultCode = 1;
     logger.error("error: update user's avatar failed! userId: "
     + userId + " avatar: " + avatar + e.toString());
     }
     DataBuffer buffer = new DataBuffer();
     buffer.writeInt(userId);
     buffer.writeInt(resultCode);
    
     return MoguUtil.writeAttachments(buffer, attachment);
     }
    /**
     * 构建buffer
     * @param user
     * @param buffer
     * @return
     */
	private DataBuffer bulidBuffer(User user,DataBuffer buffer) {
		  buffer.writeString(user.getId());
		  buffer.writeString(user.getUserInfo().getUnick());
		  buffer.writeString(user.getUserInfo().getAvatar());
		  buffer.writeString(user.getUserInfo().getTitle());
		  buffer.writeString(user.getUserInfo().getPosition());
		  buffer.writeByte(user.getUserInfo().getStatus());
		  buffer.writeByte(user.getUserInfo().getSex());
		  buffer.writeString(user.getUserInfo().getDepartId());
		  buffer.writeString(user.getUserInfo().getJobNumber());
		  buffer.writeString(user.getUserInfo().getTelphone());
		  buffer.writeString(user.getUserInfo().getMail());
		return buffer;
	}
}
