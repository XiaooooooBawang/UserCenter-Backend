package com.xiaoooooobawang.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaoooooobawang.usercenter.common.BaseResponse;
import com.xiaoooooobawang.usercenter.common.ErrorCode;
import com.xiaoooooobawang.usercenter.exception.BusinessException;
import com.xiaoooooobawang.usercenter.model.domain.Users;
import com.xiaoooooobawang.usercenter.model.request.UsersLoginRequest;
import com.xiaoooooobawang.usercenter.model.request.UsersRegisterRequest;
import com.xiaoooooobawang.usercenter.service.UsersService;
import com.xiaoooooobawang.usercenter.utils.ResultUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    public BaseResponse<Long> userRegister(@RequestBody UsersRegisterRequest usersRegisterRequest) {
        if (usersRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = usersRegisterRequest.getUserAccount();
        String userPassword = usersRegisterRequest.getUserPassword();
        String checkPassword = usersRegisterRequest.getCheckPassword();
        String planetCode = usersRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        Long result = usersService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<Users> userLogin(@RequestBody UsersLoginRequest usersLoginRequest, HttpServletRequest request) {
        if (usersLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        String userAccount = usersLoginRequest.getUserAccount();
        String userPassword = usersLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        Users user = usersService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        int result = usersService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<Users> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATUS);
        Users currentUser = (Users) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "用户未登录，无法获取当前用户");
        }
        long id = currentUser.getId();
        Users user = usersService.getById(id);
        Users cleanedUser = usersService.getCleanedUser(user);
        return ResultUtils.success(cleanedUser);
    }

    @GetMapping("/search")
    public BaseResponse<List<Users>> searchUserList(String userName, HttpServletRequest request) {
        //鉴权，仅管理员可查询
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无管理员权限");
        }

        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userName)) {
            queryWrapper.like("user_name", userName);
        }
        List<Users> usersList = usersService.list(queryWrapper);
        List<Users> list = usersList.stream().map(user -> usersService.getCleanedUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody Long id, HttpServletRequest request) {
        //鉴权，仅管理员可删除用户
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无管理员权限");
        }

        if (id <= 0) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "请求参数错误，该用户不存在");
        }
        boolean result = usersService.removeById(id);
        return ResultUtils.success(result);
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
