package com.xiaoooooobawang.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoooooobawang.usercenter.model.domain.Users;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 小霸王
 * @description 针对表【users(用户)】的数据库操作Service
 * @createDate 2022-08-06 13:07:41
 */
public interface UsersService extends IService<Users> {



    /**
     * 用户注册
     *
     * @param userAccount   账号名
     * @param userPassword  密码
     * @param checkPassword 二次密码
     * @return 新用户 id
     */
    Long userRegister(String userAccount, String userPassword, String checkPassword);


    /**
     * 用户登录
     *
     * @param userAccount   账号名
     * @param userPassword  密码
     * @param request
     * @return 用户脱敏信息
     */
    Users userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    Users getCleanedUser(Users originUser);
}
