package com.xiaoooooobawang.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoooooobawang.usercenter.mapper.UsersMapper;
import com.xiaoooooobawang.usercenter.model.domain.Users;
import com.xiaoooooobawang.usercenter.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xiaoooooobawang.usercenter.constant.UsersConstant.USER_LOGIN_STATUS;

/**
 * @author 小霸王
 * @description 针对表【users(用户)】的数据库操作Service实现
 * @createDate 2022-08-06 13:07:41
 */
@Service
@Slf4j
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
        implements UsersService {

    //md5加密的盐值
    private static final String SALT = "xiaoooooobawang";


    @Override
    public Long userRegister(String userAccount, String userPassword, String checkPassword) {
        /*
            1、校验
         */

        //这里用了common-lang3中的工具类，判断这三个参数是否存在空参数
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {

            return -1L;
        }

        //账户名不小于4位
        if (userAccount.length() < 4) {
            return -1L;
        }

        //密码和二次密码不小于8位
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            return -1L;
        }

        //账户名不包含特殊字符
        String validPattern = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return -1L;
        }

        //密码和二次密码要相同
        if (!userPassword.equals(checkPassword)) {
            return -1L;
        }

        //账户名不重复，这个放在最后，上面那些最基本的硬性要求有不满足就不用执行这个造成性能浪费
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        long count = this.count(queryWrapper);
        if (count > 0) {
            return -1L;
        }


        /*
            2、密码加密
         */
        String encryptedPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        /*
            3、插入数据
         */
        Users user = new Users();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptedPassword);
        this.save(user);

        return user.getId();
    }


    @Override
    public Users userLogin(String userAccount, String userPassword, HttpServletRequest request) {

        /*
            1、校验
         */

        //这里用了common-lang3中的工具类，判断这三个参数是否存在空参数
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {

            return null;
        }

        //账户名不小于4位
        if (userAccount.length() < 4) {
            return null;
        }

        //密码和二次密码不小于8位
        if (userPassword.length() < 8) {
            return null;
        }

        //账户名不包含特殊字符
        String validPattern = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }

        /*
            2、密码加密
         */
        String encryptedPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());


        /*
            3、查询用户
         */
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        queryWrapper.eq("user_password", encryptedPassword);
        Users user = this.getOne(queryWrapper);
        //用户不存咋
        if (user == null) {
            log.info("user login failed, userAccount can not match userPassword");
            return null;
        }


        /*
            4、用户脱敏
         */
        Users cleanedUser = getCleanedUser(user);

        /*
            5、记录用户登录态
         */
        request.getSession().setAttribute(USER_LOGIN_STATUS, cleanedUser);

        return cleanedUser;
    }

    /**
     * 用户脱敏
     *
     * @param originUser 原始用户数据
     * @return 脱敏后的用户数据
     */
    @Override
    public Users getCleanedUser(Users originUser) {
        Users cleanedUser = new Users();
        cleanedUser.setId(originUser.getId());
        cleanedUser.setUserName(originUser.getUserName());
        cleanedUser.setUserAccount(originUser.getUserAccount());
        cleanedUser.setAvatarUrl(originUser.getAvatarUrl());
        cleanedUser.setGender(originUser.getGender());
        cleanedUser.setEmail(originUser.getEmail());
        cleanedUser.setUserStatus(originUser.getUserStatus());
        cleanedUser.setPhone(originUser.getPhone());
        cleanedUser.setCreateTime(originUser.getCreateTime());
        cleanedUser.setUpdateTime(originUser.getCreateTime());
        cleanedUser.setUserRole(originUser.getUserRole());
        return cleanedUser;
    }

}




