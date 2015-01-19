package com.mogujie.ares.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mogujie.ares.configure.BizConstants;
import com.mogujie.ares.data.User;
import com.mogujie.ares.data.UserInfo;
import com.mogujie.ares.lib.logger.Logger;
import com.mogujie.ares.lib.logger.LoggerFactory;
import com.mogujie.ares.lib.net.DataBuffer;
import com.mogujie.ares.manager.DBManager;
import com.mogujie.ares.manager.DBManager.DBPoolName;
import com.mogujie.ares.util.MoguUtil;
import com.mogujie.ares.util.UUIDGenerator;

/*
 * @Description: 用户相关的model，包括获取用户信息等
 * @author ziye
 * 
 */
public class UserModel {

    private static User serverUser = null;

    private static final Logger logger = LoggerFactory
            .getLogger(UserModel.class);
    private static UserModel instance = new UserModel();

    public static UserModel getInstance() {
        if (instance == null) {
            instance = new UserModel();
        }
        return instance;
    }

    /*
     * @Description: 获取服务用户(小T)的信息
     * 
     * @param userId
     * 
     * @return User 用户的具体信息，包括用户id，用户名，头像链接 null 参数有误或取不到用户信息时返回null
     * 
     * @throws SQLException
     */
    public User getServerUserInfo() throws SQLException {
        if (serverUser == null) {
            serverUser = getUserInfo(BizConstants.SYS_SERVER_USER_ID);
        }
        return serverUser;
    }

    /*
     * @Description: 获取单个用户的信息
     * 
     * @param userId
     * 
     * @return User 用户的具体信息，包括用户id，用户名，头像链接 null 参数有误或取不到用户信息时返回null
     * 
     * @throws SQLException
     */
    public User getUserInfo(int userId) throws SQLException {
        Map<String, User> userInfos = getUserInfo(new int[] { userId });
        return (userInfos == null) ? null : userInfos.get(userId);
    }

    /*
     * 
     * @Description: 获取一组用户的信息,蛋疼的Integer数组
     * 
     * @param userIds
     * 
     * @return
     * 
     * @throws SQLException
     */
    public Map<String, User> getUserInfo(Integer[] userIds)
            throws SQLException {
        int[] uIds = new int[userIds.length];
        for (int i = 0; i < userIds.length; i++) {
            uIds[i] = userIds[i];
        }
        return getUserInfo(uIds);
    }

    /*
     * @Description: 获取一组用户的信息
     * 
     * @param userIds
     * 
     * @return Map<String, User> 用户的具体信息Map，每个元素包括用户id，用户名，头像链接
     * 
     * @throws SQLException
     */
    public Map<String, User> getUserInfo(int[] userIds) throws SQLException {
        Map<String, User> mapUsers = new HashMap<String, User>();
        userIds = MoguUtil.distinct(userIds); // 去除重复的userId
        int countIds = userIds.length;

        DBManager dbManager = DBManager.getInstance();
        Connection conn = dbManager.getConnection(DBPoolName.macim_slave);
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            String clause = MoguUtil.getArgsHolder(countIds);
            String sql = "select u.*,i.* from IM_USER u,IM_USERINFO i where id in (" + clause
                    + ") and u.status=0";
            statement = conn.prepareStatement(sql);
            for (int i = 0; i < countIds; i++) {
                statement.setInt(i + 1, userIds[i]);
            }
            rs = statement.executeQuery();
            User user = null;
            while (rs.next()) {
                user = extractUser(rs);
                mapUsers.put(user.getId(), user);
            }
        } catch (SQLException e) {
            logger.error("get user info error with reason : " + e);
            throw e;
        } finally {
            dbManager.release(DBPoolName.macim_slave, conn, statement, rs);
        }

        return mapUsers;
    }
    
    /**
     * 获取该好友的所有联系人
     * @param userId
     * @param attachment
     * @param version
     * @return
     * @throws SQLException
     */
    public Set<User> getAllContantsByUserId(String userId,DataBuffer attachment,
			int version) throws SQLException{
    	Set<User> contantsUsers = new HashSet<User>();
    	Connection conn = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try{
    		conn = DBManager.getInstance().getConnection(DBPoolName.macim_master);
    		String sql = "select user.name,user.avater from im_userinfo user,im_relationship rsp where user.id = rsp.friend_id where rsp.user_id = ?";
    		ps = conn.prepareStatement(sql);
    		ps.setString(1, userId);
    		rs = ps.executeQuery();
    		while(rs.next()) {
    			contantsUsers.add(extractUser(rs));
    		}
    	}catch (SQLException e){
    		e.printStackTrace();
    	}
    	return contantsUsers;
    }
    
    /**
     * 添加好友
     * @param userId
     * @param fId
     * @param attachment
     * @param version
     * @return
     * @throws SQLException
     */
    public User addFriend(String userId, String fId, DataBuffer attachment,
			int version) throws SQLException { 
    	User user = null;
    	Connection conn = null;
    	PreparedStatement ps = null;
    	
    	try {
    		conn = DBManager.getInstance().getConnection(DBPoolName.macim_master);
    		String sql = "insert into im_relationship (relate_id,user_id,friend_id,status,created,updated) values (?,?,?,?,?,?)";
    		ps = conn.prepareStatement(sql);
    		ps.setString(1, UUIDGenerator.getUUID());
    		ps.setString(2, userId);
    		ps.setString(3, fId);
    		ps.setByte(4, BizConstants.ADD);
    		ps.setLong(5,System.currentTimeMillis());
    		ps.setLong(6, System.currentTimeMillis()); 
    		ps.executeUpdate();
    		
    		user = getUserInfoById(fId);
		} catch (SQLException e) {
			logger.error("add friend error with reason :" + e);
			throw e;
		} finally {
			DBManager.getInstance().release(DBPoolName.macim_master, conn, ps, null);
		}
    	return user;
    }
    
    /**
     * 删除好友
     * @param userId
     * @param fId
     * @param attachment
     * @param version
     * @return
     * @throws SQLException
     */
    public User delFriend(String userId, String fId, DataBuffer attachment,
			int version) throws SQLException { 
    	User user = null;
    	Connection conn = null;
    	PreparedStatement ps = null;
    	
    	try {
    		conn = DBManager.getInstance().getConnection(DBPoolName.macim_master);
    		String sql = "delete from im_relationship where user_id = ? and friend_id = ?";
    		ps = conn.prepareStatement(sql);
    		ps.setString(1, userId);
    		ps.setString(2, fId);
    		ps.executeUpdate();
    		
    		user = getUserInfoById(fId);
		} catch (SQLException e) {
			logger.error("add friend error with reason :" + e);
			throw e;
		} finally {
			DBManager.getInstance().release(DBPoolName.macim_master, conn, ps, null);
		}
    	return user;
    }

	/*
     * @Description: 获取一个部门的所有用户的信息
     * 
     * @param uname
     * 
     * @return Set<User> 用户的具体信息集合，每个元素包括用户id，用户名，头像链接
     * 
     * @throws SQLException
     */
    public Set<User> getUserInfoByDepartId(int departId) throws SQLException {
        Set<User> setUsers = new HashSet<User>();
        Connection conn = DBManager.getInstance().getConnection(
                DBPoolName.macim_slave);
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            String sql = "select * from IMUsers where departId = ? limit 5000"; // 最多取5000个用户
            statement = conn.prepareStatement(sql);
            statement.setInt(1, departId);
            rs = statement.executeQuery();
            User user = null;
            while (rs.next()) {
                user = extractUser(rs);
                setUsers.add(user);
            }
        } catch (SQLException e) {
            logger.error("get user info by department id error with reason : "
                    + e);
            throw e;
        } finally {
            DBManager.getInstance().release(DBPoolName.macim_slave, conn,
                    statement, rs);
        }

        return setUsers;
    }

    /*
     * @Description: 获取所有用户的信息;默认聊天用户上限5000人
     * 
     * @return Set<User> 所有用户的具体信息，每个元素包括用户id，用户名，头像链接
     * 
     * @throws SQLException
     */
    public Set<User> getAllUserInfo() throws SQLException {
        Set<User> setUsers = new HashSet<User>();
        Connection conn = DBManager.getInstance().getConnection(
                DBPoolName.macim_slave);
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            String sql = "select * from IMUsers where status = 0 limit 5000"; // 最多取5000个用户
            statement = conn.prepareStatement(sql);
            rs = statement.executeQuery();
            User user = null;
            while (rs.next()) {
                user = extractUser(rs);
                setUsers.add(user);
            }
        } catch (SQLException e) {
            logger.error("get all user info error with reason : " + e);
            throw e;
        } finally {
            DBManager.getInstance().release(DBPoolName.macim_slave, conn,
                    statement, rs);
        }

        return setUsers;
    }

    /*
     * @Description: 获取一个用户的信息
     * 
     * @param uname
     * 
     * @return User 用户的具体信息，每个元素包括用户id，用户名，头像链接
     * 
     * @throws SQLException
     */
    public User getUserInfo(String uname) throws SQLException {
        Map<String, User> usersInfos = getUserInfo(new String[] { uname });
        return (usersInfos == null) ? null : usersInfos.get(uname);
    }
    
    public User getUserInfoById(String userId) throws SQLException {
    	User user = null;
    	Connection conn = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	
    	conn = DBManager.getInstance().getConnection(DBPoolName.macim_master);
    	String sql = "select u.*,f.* from im_user u,im_userinfo f where u.id = f.user_id and u.id = ?";
    	ps = conn.prepareStatement(sql);
    	ps.setString(1, userId);
    	rs = ps.executeQuery();
    	while(rs.next()){
    		user = extractUser(rs);
    	}
    	return user;
    }

    /*
     * @Description: 获取一组用户的信息
     * 
     * @param unames
     * 
     * @return User Map 用户的具体信息Map，每个元素包括用户id，用户名，头像链接
     * 
     * @throws SQLException
     */
    public Map<String, User> getUserInfo(String[] unames) throws SQLException {
        Map<String, User> mapUsers = new HashMap<String, User>();

        Connection conn = DBManager.getInstance().getConnection(
                DBPoolName.macim_slave);
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            String clause = MoguUtil.getArgsHolder(unames.length);
            String sql = "select u.*,i.* from IM_USER u,IM_USERINFO i where login_name in (" + clause
                    + ") limit 5000"; // 最多取5000个用户
            statement = conn.prepareStatement(sql);
            for (int i = 0; i < unames.length; i++) {
                statement.setString(i + 1, unames[i]);
            }
            rs = statement.executeQuery();
            User user = null;
            while (rs.next()) {
                user = extractUser(rs);
                mapUsers.put(user.getLoginName(), user);
            }
        } catch (SQLException e) {
            logger.error("get user info error with reason : " + e);
            throw e;
        } finally {
            DBManager.getInstance().release(DBPoolName.macim_slave, conn,
                    statement, rs);
        }

        return mapUsers;
    }

    /**
     * 根据用户ID获取
     * @param userId
     * @return
     * @throws Exception
     */
    public Map<String, User> getAllUsersByUserId(String userId) throws SQLException {
    	
    	Map<String, User> userMap = new HashMap<String, User>();
    	Connection conn = DBManager.getInstance().getConnection(DBPoolName.macim_slave);
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try
    	{
    		String sql = "";
    	} catch(Exception e) {
    		e.printStackTrace();
    	} finally {
    		DBManager.getInstance().release(DBPoolName.macim_slave, conn, ps, rs);
    	}
    	
    	return null;
    }
    
     /**
     * @Description: 修改用户头像
     * @param userId
     * @param avatar
     * @return
     * @throws SQLException
     */
	public int alterAvatar(int userId, String avatar) throws SQLException {
		if (userId <= 0 || avatar == null || avatar.equals("")) {
			throw new IllegalArgumentException("argument error: userId:"
					+ userId + "avatar: " + avatar);
		}
		DBManager dbManager = DBManager.getInstance();
		Connection conn = dbManager.getConnection(DBPoolName.macim_master);
		PreparedStatement statement = null;
		ResultSet rs = null;
		int time = (int) (System.currentTimeMillis() / 1000);
		int countUpdate = 0;
		try {
			// 更新用户头像
			String sqlUpdateMemberCnt = "update IMUsers set avatar = ? , updated = ? where userId = ? limit 1";
			statement = conn.prepareStatement(sqlUpdateMemberCnt);
			int index = 1;
			statement.setString(index++, avatar);
			statement.setInt(index++, time);
			statement.setInt(index++, userId);
			countUpdate = statement.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			dbManager.release(DBPoolName.macim_master, conn, statement, rs);
		}
		if (0 == countUpdate) {
			return 1;
		}
		return 0;
	}

	/**
	 *
	 * @Description: 获得用户自定义的头像
	 * @param userIds
	 * @return
	 * @throws SQLException
	 */
	public Map<Integer, String> getUserAvatar(int[] userIds)
			throws SQLException {
		Map<Integer, String> mapUserAvatars = new HashMap<Integer, String>();
		if (userIds == null || userIds.length <= 0) {
			return mapUserAvatars;
		}

		Connection conn = DBManager.getInstance().getConnection(
				DBPoolName.macim_slave);
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			String clause = "";
			for (int i = 0; i < userIds.length; i++) {
				clause += ",?";
			}
			clause = clause.substring(1);
			String sql = "select userId, avatar from IMUsers where avatar <> '' and userId in ("
					+ clause + ")"; // 最多取5000个用户
			statement = conn.prepareStatement(sql);
			for (int i = 0; i < userIds.length; i++) {
				statement.setInt(i + 1, userIds[i]);
			}
			rs = statement.executeQuery();
			while (rs.next()) {
				int userId = rs.getInt("userId");
				String avatar = rs.getString("avatar");
				if (avatar != null && !"".equals(avatar)) {
					mapUserAvatars.put(userId, avatar);
				}
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			DBManager.getInstance().release(DBPoolName.macim_slave, conn,
					statement, rs);
		}

		return mapUserAvatars;
	}

	/**
	 *
	 * @Description: 获得用户自定义的头像
	 * @param userIds
	 * @return
	 * @throws SQLException
	 */
	public Map<Integer, String> getUserAvatar(String[] unames)
			throws SQLException {
		Map<Integer, String> mapUserAvatars = new HashMap<Integer, String>();
		if (unames == null || unames.length <= 0) {
			return mapUserAvatars;
		}

		Connection conn = DBManager.getInstance().getConnection(
				DBPoolName.macim_slave);
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			String clause = "";
			for (int i = 0; i < unames.length; i++) {
				clause += ",?";
			}
			clause = clause.substring(1);
			String sql = "select userId, avatar from IMUsers where avatar <> '' and uname in ("
					+ clause + ")"; // 最多取5000个用户
			statement = conn.prepareStatement(sql);
			for (int i = 0; i < unames.length; i++) {
				statement.setString(i + 1, unames[i]);
			}
			rs = statement.executeQuery();
			while (rs.next()) {
				int userId = rs.getInt("userId");
				String avatar = rs.getString("avatar");
				if (avatar != null && !"".equals(avatar)) {
					mapUserAvatars.put(userId, avatar);
				}
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			DBManager.getInstance().release(DBPoolName.macim_slave, conn,
					statement, rs);
		}

		return mapUserAvatars;
	}

    /*
     * 从ResultSet中提取User
     */
    private User extractUser(ResultSet rs) throws SQLException {
        User user = new User();
        UserInfo userInfo = new UserInfo();
        
        user.setId(rs.getString("id"));
        user.setLoginName(rs.getString("login_name"));
        user.setPsw(rs.getString("psw"));
        user.setStatus(rs.getByte("status"));
        user.setLoginIp(rs.getLong("login_ip"));
        user.setLoginTime(rs.getInt("login_time"));
        user.setLogoutTime(rs.getInt("logout_time"));
        
        userInfo.setUserId(rs.getString("user_id"));
        userInfo.setUname(rs.getString("uname"));
        userInfo.setUnick(rs.getString("unick"));
        userInfo.setAvatar(rs.getString("avater"));
        userInfo.setTitle(rs.getString("title"));
        userInfo.setPosition(rs.getString("position"));
        userInfo.setStatus(rs.getByte("status"));
        userInfo.setSex(rs.getByte("sex"));
        userInfo.setUserType(rs.getByte("user_type"));
        userInfo.setDepartId(rs.getString("depart_id"));
        userInfo.setJobNumber(rs.getString("job_number"));
        userInfo.setTelphone(rs.getString("telphone"));
        userInfo.setMail(rs.getString("mail"));
        user.setUserInfo(userInfo);
        logger.info("user:" + user.getId());
        return user;

    }
}
