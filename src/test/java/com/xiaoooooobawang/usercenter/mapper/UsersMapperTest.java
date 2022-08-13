package com.xiaoooooobawang.usercenter.mapper;

import com.xiaoooooobawang.usercenter.model.domain.Users;
import com.xiaoooooobawang.usercenter.service.UsersService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class UsersMapperTest {
    @Resource
    private UsersService usersService;

    @Test
    void testAddUser() {
        Users user = new Users();
        user.setUserName("sdf");
        user.setUserAccount("123");
        user.setAvatarUrl("23423");
        user.setGender(0);
        user.setUserPassword("123");
        user.setEmail("123");
        user.setPhone("123");

        boolean res = usersService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(res);
    }


    @Test
    void testUserRegister() {
        String userAccount = "dfjs";
        String userPassword = "";   //空
        String checkPassword = "lfsdfsdl";
        String planetCode = "1";
        Long res = usersService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, res);

        userAccount = "dfjs";
        userPassword = "dfsd";  //密码小于8位
        checkPassword = "dfsd";
        res = usersService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, res);

        userAccount = "fh js";  //包含特殊字符
        userPassword = "fdsfsdfsd";
        checkPassword = "fdsfsdfsd";
        res = usersService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, res);

        userAccount = "dfjs";
        userPassword = "fdsfsdfsd";
        checkPassword = "fdsfsdfsddf";  //二次密码不相同
        res = usersService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, res);

        userAccount = "1234";  //账户名已存在
        userPassword = "fdsfsdfsd";
        checkPassword = "fdsfsdfsd";
        res = usersService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, res);

        //上面都是各种校验不通过的测试
        //下面就是正常情况的测试
        userAccount = "XiaooooooBawang";
        userPassword = "12345678";
        checkPassword = "12345678";
        res = usersService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertTrue(res > 0);
    }
}