package net.jjjshop.front.service.user;

import net.jjjshop.common.entity.user.User;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.front.param.AppWxParam;
import net.jjjshop.front.param.user.PhoneRegisterParam;
import net.jjjshop.front.vo.user.LoginUserTokenVo;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户记录表 服务类
 * @author jjjshop
 * @since 2022-07-01
 */
public interface UserService extends BaseService<User> {

    /**
     * 手机号密码登录
     * @param mobile
     * @param password
     * @return
     */
    LoginUserTokenVo login(String mobile, String password);

    /**
     * 微信小程序登录
     * @param appWxParam
     * @return
     */
    LoginUserTokenVo loginWx(AppWxParam appWxParam);

    /**
     * 修改密码
     * @param password
     * @return
     */
    Boolean renew(String password);

    /**
     * 退出
     * @param request
     * @throws Exception
     */
    void logout(HttpServletRequest request) throws Exception;

    /**
     * 修改用户信息
     * @param userId
     * @param updateType
     * @param updateValue
     * @return
     */
    Boolean updateInfo(Integer userId, String updateType, String updateValue);

    /**
     * 绑定手机号
     * @param sessionKey
     * @param encryptedData
     * @param iv
     * @return
     */
    String bindMobileByWx(Integer userId, String sessionKey, String encryptedData, String iv);

    /**
     * 获取sessionKey
     * @param code
     * @return
     */
    String getSessionKey(String code);

    //注册
    boolean phoneRegister(PhoneRegisterParam phoneRegisterParam);
}
