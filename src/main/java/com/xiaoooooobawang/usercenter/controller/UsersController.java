package com.xiaoooooobawang.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaoooooobawang.usercenter.model.domain.Users;
import com.xiaoooooobawang.usercenter.model.request.UsersLoginRequest;
import com.xiaoooooobawang.usercenter.model.request.UsersRegisterRequest;
import com.xiaoooooobawang.usercenter.service.UsersService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.xiaoooooobawang.usercenter.constant.UsersConstant.ADMIN_ROLE;
import static com.xiaoooooobawang.usercenter.constant.UsersConstant.USER_LOGIN_STATUS;

/**
 * 用户接口
 *
 * @author 小霸王
 */
@RestController
@RequestMapping("/user")
public class UsersController {
    @Resource
    private UsersService usersService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody UsersRegisterRequest usersRegisterRequest) {
        if (usersRegisterRequest == null) {
            return null;
        }
        String userAccount = usersRegisterRequest.getUserAccount();
        String userPassword = usersRegisterRequest.getUserPassword();
        String checkPassword = usersRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        return usersService.userRegister(userAccount, userPassword, checkPassword);
    }

    @PostMapping("/login")
    public Users userLogin(@RequestBody UsersLoginRequest usersLoginRequest, HttpServletRequest httpServletRequest) {
        if (usersLoginRequest == null) {
            return null;
        }
        String userAccount = usersLoginRequest.getUserAccount();
        String userPassword = usersLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        return usersService.userLogin(userAccount, userPassword, httpServletRequest);
    }

    @GetMapping("/search")
    public List<Users> searchUserList(String userName, HttpServletRequest request) {
        //鉴权，仅管理员可查询
        if (!isAdmin(request)) {
            return new ArrayList<>();
        }

        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userName)) {

            queryWrapper.like("user_name", userName);
        }
        List<Users> usersList = usersService.list(queryWrapper);
        return usersList.stream().map(user -> usersService.getCleanedUser(user)).collect(Collectors.toList());
    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody Long id, HttpServletRequest request) {
        //鉴权，仅管理员可删除用户
        if (!isAdmin(request)) {
            return false;
        }

        if (id <= 0) {
            return false;
        }
        return usersService.removeById(id);
    }

    /**
     * 用户鉴权
     *
     * @param request
     * @return
     */
    public boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATUS);
        Users user = (Users) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}
