create table users
(
    id            bigint auto_increment comment 'id'
        primary key,
    user_name     varchar(256)                       null comment '用户名',
    user_account  varchar(256)                       null comment '账号名',
    avatar_url    varchar(1024)                      null comment '头像',
    gender        tinyint                            null comment '性别',
    user_password varchar(512)                       null comment '密码',
    email         varchar(256)                       null comment '邮箱',
    user_status   int      default 0                 not null comment '状态 0正常',
    phone         varchar(128)                       null comment '电话',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null comment '更新时间',
    deleted       tinyint  default 0                 null comment '是否删除',
    user_role     int      default 0                 not null comment '用户角色，默认0-用户，1-管理员',
    planet_code   varchar(512)                       null comment '星球编号'
)
    comment '用户';
