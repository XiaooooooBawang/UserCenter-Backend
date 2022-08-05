package com.xiaoooooobawang.usercenter.model;

import lombok.Data;

@Data
//lombok中的注解，能自动重写getter和setter和toString方法，不需要自己写，不过idea也可以自动生成，也很方便
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}