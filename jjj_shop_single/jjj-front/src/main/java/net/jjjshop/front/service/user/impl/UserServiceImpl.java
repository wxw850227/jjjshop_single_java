package net.jjjshop.front.service.user.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import net.jjjshop.common.entity.app.App;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.mapper.user.UserMapper;
import net.jjjshop.common.service.app.AppService;
import net.jjjshop.common.service.user.UserGradeService;
import net.jjjshop.common.util.wx.AppWxUtils;
import net.jjjshop.config.constant.CommonConstant;
import net.jjjshop.config.constant.CommonRedisKey;
import net.jjjshop.config.properties.JwtProperties;
import net.jjjshop.config.properties.SpringBootJjjProperties;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.shiro.cache.UserLoginRedisService;
import net.jjjshop.framework.shiro.jwt.JwtToken;
import net.jjjshop.framework.shiro.util.JwtTokenUtil;
import net.jjjshop.framework.shiro.util.JwtUtil;
import net.jjjshop.framework.shiro.util.SaltUtil;
import net.jjjshop.framework.shiro.vo.LoginUserVo;
import net.jjjshop.framework.util.PasswordUtil;
import net.jjjshop.front.param.AppWxParam;
import net.jjjshop.front.param.user.PhoneRegisterParam;
import net.jjjshop.front.service.user.UserService;
import net.jjjshop.front.vo.user.LoginUserTokenVo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-01
 */
@Slf4j
@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements UserService {

    @Lazy
    @Autowired
    private SpringBootJjjProperties springBootJjjProperties;
    @Lazy
    @Autowired
    private JwtProperties jwtProperties;
    @Lazy
    @Autowired
    private RedisTemplate redisTemplate;
    @Lazy
    @Autowired
    private UserLoginRedisService userLoginRedisService;
    @Lazy
    @Autowired
    private WxMaService wxMaService;
    @Lazy
    @Autowired
    private AppWxUtils appWxUtils;
    @Lazy
    @Autowired
    private UserGradeService userGradeService;
    @Autowired
    private AppService appService;

    /**
     * 手机号密码登录
     * @param mobile
     * @param password
     * @return
     */
    public LoginUserTokenVo login(String mobile, String password) {
        List<User> userList = this.list(new LambdaQueryWrapper<User>()
                .eq(User::getMobile, mobile).ne(User::getPassword, "")
                .comment(CommonConstant.NOT_WITH_App_Id));
        if (userList.size() == 0) {
            throw new AuthenticationException("用户名不存在");
        }
        User user = userList.get(0);
        String encryptPassword = PasswordUtil.encrypt(password, user.getSalt());
        if (!encryptPassword.equals(user.getPassword())) {
            throw new AuthenticationException("用户名或密码错误");
        }
        App app = appService.getOne(new LambdaQueryWrapper<App>().eq(App::getAppId,user.getAppId())
                .comment(CommonConstant.NOT_WITH_App_Id));
        if (app == null) {
            throw new BusinessException("当前应用信息不存在");
        }
        if (app.getIsDelete()) {
            throw new BusinessException("当前应用已删除");
        }
        if(!app.getIsRecycle()){
            throw new BusinessException("当前应用未启用");
        }
        if (app.getExpireTime() != null && app.getExpireTime().before(new Date())) {
            throw new BusinessException("当前应用已过期");
        }
        return this.getLoginUserTokenVo(user);
    }

    /**
     * 获取登陆用户token
     * @param user
     * @return
     */
    private LoginUserTokenVo getLoginUserTokenVo(User user) {
        // 将系统用户对象转换成登录用户对象
        LoginUserVo loginUserVo = new LoginUserVo();
        BeanUtils.copyProperties(user, loginUserVo);
        // 获取数据库中保存的盐值
        String newSalt = SaltUtil.getSalt(user.getSalt(), jwtProperties);

        // 生成token字符串并返回
        Long expireSecond = jwtProperties.getExpireSecond();
        String token = JwtUtil.generateToken("" + user.getUserId(), newSalt, Duration.ofSeconds(expireSecond));
        log.debug("token:{}", token);

        // 创建AuthenticationToken
        JwtToken jwtToken = JwtToken.build(token, "" + user.getUserId(), newSalt, expireSecond);

        boolean enableShiro = springBootJjjProperties.getShiro().isEnable();
        if (enableShiro) {
            // 从SecurityUtils里边创建一个 subject
            Subject subject = SecurityUtils.getSubject();
            // 执行认证登录
            subject.login(jwtToken);
        } else {
            log.warn("未启用Shiro");
        }

        // 缓存登录信息到Redis
        userLoginRedisService.cacheLoginInfo(jwtToken, loginUserVo);
        log.debug("登录成功,userId:{}", "" + user.getUserId());

        // 缓存登录信息到redis
        String tokenSha256 = DigestUtils.sha256Hex(token);
        redisTemplate.opsForValue().set(tokenSha256, loginUserVo, 1, TimeUnit.DAYS);

        // 返回token和登录用户信息对象
        LoginUserTokenVo loginUserTokenVo = new LoginUserTokenVo();
        loginUserTokenVo.setToken(token);
        loginUserTokenVo.setLoginUserVo(loginUserVo);
        return loginUserTokenVo;
    }

    /**
     * 微信小程序登录
     * @param appWxParam
     * @return
     */
    public LoginUserTokenVo loginWx(AppWxParam appWxParam) {
        WxMaJscode2SessionResult result;
        try {
            wxMaService.setWxMaConfig(appWxUtils.getConfig());
            result = wxMaService.getUserService().getSessionInfo(appWxParam.getCode());
        } catch (WxErrorException e) {
            log.info("微信小程序登录异常：{}", e.getMessage());
            throw new BusinessException("微信小程序登录异常：" + e.getError().getErrorMsg());
        }
        // 查找用户是否存在，不存在则先注册
        //先通过unionId查找
        User user = getUserByUnionId(result.getUnionid());
        if (user == null) {
            // 通过你openId查找
            user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getOpenId, result.getOpenid()));
        }
        if (user != null) {
            return this.getLoginUserTokenVo(user);
        } else {
            // 注册后返回
            String userInfoStr = StringEscapeUtils.unescapeHtml4(appWxParam.getUserInfo());
            JSONObject userInfo = JSONObject.parseObject(userInfoStr);
            User bean = new User();
            if (StringUtils.isNotBlank(result.getUnionid())) {
                bean.setUnionId(result.getUnionid());
            }
            bean.setOpenId(result.getOpenid());
            bean.setNickname(userInfo.getString("nickName"));
            bean.setAvatarurl(userInfo.getString("avatarUrl"));
            this.save(bean);
            return this.getLoginUserTokenVo(bean);
        }
    }

    /**
     * 通过unionid查找用户
     *
     * @param unionId
     * @return
     */
    private User getUserByUnionId(String unionId) {
        if (StringUtils.isNotBlank(unionId)) {
            return this.getOne(new LambdaQueryWrapper<User>().eq(User::getUnionId, unionId));
        }
        return null;
    }

    /**
     * 修改密码
     * @param password
     * @return
     */
    public Boolean renew(String password) {
        /*User user = this.selectOne(new LambdaQueryWrapper<User>());

        String encryptPassword = PasswordUtil.encrypt(password, adminUser.getSalt());
        User updateBean = new User();
        updateBean.setAdminUserId(adminUser.getAdminUserId());
        updateBean.setPassword(encryptPassword);
        return this.updateById(updateBean);*/
        return true;
    }

    /**
     * 退出
     * @param request
     * @throws Exception
     */
    @Override
    public void logout(HttpServletRequest request) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        //注销
        subject.logout();
        // 获取token
        String token = JwtTokenUtil.getToken(request, "");
        String username = JwtUtil.getUsername(token);
        // 删除Redis缓存信息
        deleteLoginInfo(token, username);
        log.info("登出成功,username:{},token:{}", username, token);
    }


    private void deleteLoginInfo(String token, String username) {
        if (token == null) {
            throw new IllegalArgumentException("token不能为空");
        }
        if (username == null) {
            throw new IllegalArgumentException("username不能为空");
        }
        String tokenMd5 = DigestUtils.md5Hex(token);
        // 1. delete tokenMd5
        redisTemplate.delete(String.format(CommonRedisKey.USER_LOGIN_TOKEN, tokenMd5));
        // 2. delete username
        redisTemplate.delete(String.format(CommonRedisKey.USER_LOGIN_USER, username));
        // 3. delete salt
        redisTemplate.delete(String.format(CommonRedisKey.USER_LOGIN_SALT, username));
        // 4. delete user token
        redisTemplate.delete(String.format(CommonRedisKey.USER_LOGIN_USER_TOKEN, username, tokenMd5));
    }

    /**
     * 修改用户信息
     * @param userId
     * @param updateType
     * @param updateValue
     * @return
     */
    public Boolean updateInfo(Integer userId, String updateType, String updateValue) {
        User user = new User();
        user.setUserId(userId);
        if ("nickname".equals(updateType)) {
            user.setNickname(updateValue);
        } else if ("avatarUrl".equals(updateType)) {
            user.setAvatarurl(updateValue);
        } else {
            return false;
        }
        return this.updateById(user);
    }

    /**
     * 获取sessionKey
     * @param code
     * @return
     */
    public String getSessionKey(String code) {
        try {
            wxMaService.setWxMaConfig(appWxUtils.getConfig());
            WxMaJscode2SessionResult sessionResult = wxMaService.getUserService().getSessionInfo(code);
            return sessionResult.getSessionKey();
        } catch (Exception e) {
            log.info("getSessionKey失败{}", e.getMessage());
            return "";
        }
    }

    @Override
    /**
     * 手机号注册
     */
    public boolean phoneRegister(PhoneRegisterParam phoneRegisterParam) {
        int count = this.count(new LambdaQueryWrapper<User>().eq(User::getMobile, phoneRegisterParam.getMobile()));
        if (!(count > 0)) {
            User user = new User();
            user.setMobile(phoneRegisterParam.getMobile());
            user.setRegSource(phoneRegisterParam.getRegSource() != null ? phoneRegisterParam.getRegSource() : "app");
            user.setGradeId(userGradeService.getDefaultGradeId());
            user.setPassword(DigestUtils.md5Hex(phoneRegisterParam.getPassword()));
            // 盐值
            String salt = SaltUtil.generateSalt();
            // 密码加密
            user.setSalt(salt);
            user.setPassword(PasswordUtil.encrypt(phoneRegisterParam.getPassword(), salt));
            user.setNickname("用户:"+phoneRegisterParam.getMobile());
            this.save(user);
            return true;
        } else {
            throw new BusinessException("手机号已存在");
        }
    }


    /**
     * 绑定手机号
     * @param sessionKey
     * @param encryptedData
     * @param iv
     * @return
     */
    public String bindMobileByWx(Integer userId, String sessionKey, String encryptedData, String iv) {
        try {
            wxMaService.setWxMaConfig(appWxUtils.getConfig());
            WxMaPhoneNumberInfo result = wxMaService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);
            String mobile = result.getPhoneNumber();
            User updateUser = new User();
            updateUser.setUserId(userId);
            updateUser.setMobile(mobile);
            this.updateById(updateUser);
            return mobile;
        } catch (Exception e) {
            log.info("手机号解密失败{}", e.getMessage());
            return "";
        }

    }
}
