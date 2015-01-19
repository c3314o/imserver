package com.mogujie.ares.extend.action;

import com.alibaba.druid.util.StringUtils;
import com.mogujie.ares.configure.BizConstants;
import com.mogujie.ares.data.User;
import com.mogujie.ares.extend.BaseAction;
import com.mogujie.ares.lib.logger.Logger;
import com.mogujie.ares.lib.logger.LoggerFactory;
import com.mogujie.ares.lib.net.DataBuffer;
import com.mogujie.ares.model.LoginModel;
import com.mogujie.ares.model.UserModel;
import com.mogujie.ares.util.MoguUtil;

/*
 * @Description: 用户登陆相关的请求
 * @author ziye - ziye[at]mogujie.com
 * @date 2013-7-21 下午1:28:17
 */
public class Login extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(Login.class);

    /*
     * @Description: 用户登陆
     * 
     * @param userId 用户Id
     * 
     * @param token
     * 
     * @return
     */
    public DataBuffer login(String uname, String pwd, DataBuffer attachment,
            int version) {
    	logger.info("username:" + uname + "--password:" + pwd);
        int resultCode = 0;
        boolean isAuthed = false;
        User user = null;
        if (StringUtils.isEmpty(uname)) {
            resultCode = 1;
        }
        UserModel userModel = UserModel.getInstance();
        try {
            isAuthed = LoginModel.getInstance().auth(uname, pwd);
            logger.info("isAuthed:" + isAuthed); 
            if (isAuthed) {
                user = userModel.getUserInfo(uname);
            } else {
                logger.info("login failed with error password!");
            }
        } catch (Exception e) {
            logger.error("login failed with reason : ", e);
            e.printStackTrace();
            isAuthed = false;
        }
        isAuthed = (isAuthed && (user != null)); // 用户要存在
        resultCode = (isAuthed ? 0 : 1); // 0: 成功，1: 失败
        DataBuffer buffer = new DataBuffer();
        buffer.writeString(uname);// 用户名
        buffer.writeInt(resultCode);
        if (isAuthed) {
        	buffer = bulidBuffer(user,buffer);
            logger.info("login success: " + uname + ", " + resultCode);
        } else {
            logger.info("login error: " + uname + ", " + resultCode);
        }

        return MoguUtil.writeAttachments(buffer, attachment);
    }

    /**
     * 构建buffer
     * @param user
     * @param buffer
     * @return
     */
	private DataBuffer bulidBuffer(User user,DataBuffer buffer) {
		  buffer.writeInt(BizConstants.SUCCESS);
		  buffer.writeInt(100);
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
