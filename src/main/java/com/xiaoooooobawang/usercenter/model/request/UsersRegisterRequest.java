package com.xiaoooooobawang.usercenter.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author 小霸王
 */
@Data
public class UsersRegisterRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 5627545698982081729L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String planetCode;

}
