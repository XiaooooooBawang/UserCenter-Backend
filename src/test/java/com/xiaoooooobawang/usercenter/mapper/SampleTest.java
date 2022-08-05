package com.xiaoooooobawang.usercenter.mapper;

import com.xiaoooooobawang.usercenter.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class SampleTest {

    //    @Autowired
    //    private UserMapper userMapper;
    //      之前也见过这种，虽然idea报错但运行不报错。还有的解决办法是用@resource注解（这个没讲过，它是javaEE注解，不属于spring的注解），
    //      @resource 是先byName注入，不行再byType注入，实现@Autowired + @Qualifier的效果
    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        Assert.isTrue(5 == userList.size(), "mybatis-plus error");
        userList.forEach(System.out::println);
    }

}
