package com.xiaoooooobawang.usercenter.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author 小霸王
 */
@Data
public class UsersLoginRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -2344331642643641800L;

    private String userAccount;

    private String userPassword;

}
