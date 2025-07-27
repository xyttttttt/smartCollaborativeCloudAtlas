-- 创建库
create database if not exists smart_picture;

-- 切换库
use smart_picture;

-- 用户表
create table if not exists user
(
    id              bigint auto_increment comment 'id' primary key,
    user_account    varchar(256)                           not null comment '账号',
    password_hash   varchar(512)                           not null comment '密码',
    user_name       varchar(256)                           null comment '用户昵称',
    avatar          varchar(1024)                          null comment '用户头像',
    invite_code     varchar(32)                            null comment '邀请码',
    invite_from     varchar(32)                            null comment '受邀请码',
    status          smallint                               null comment '状态',
    profile         varchar(512)                           null comment '用户简介',
    user_role       varchar(256) default 'user'            not null comment '用户角色：user/admin',
    edit_time       datetime     default CURRENT_TIMESTAMP not null comment '编辑时间',
    create_time     datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    last_login_time datetime     default CURRENT_TIMESTAMP not null comment '上次登录时间',
    update_time     datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete       tinyint      default 0                 not null comment '是否删除',
    version         int          default 0                 null comment '版本号',
    UNIQUE KEY uk_userAccount (user_account),
    INDEX idx_userName (user_name)
) comment '用户' collate = utf8mb4_unicode_ci;

create table if not exists user_login_record
(
    id          bigint auto_increment comment 'id' primary key,
    user_id     bigint                             not null comment '创建用户 id',
    ip          varchar(256)                       not null comment '用户登录的 ip',
    user_agent  varchar(1024)                      not null comment '用户登录的 userAgent',
    login_time  datetime default CURRENT_TIMESTAMP not null comment '登录时间',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   tinyint  default 0                 not null comment '是否删除',
    version     int      default 0                 null comment '版本号',
    INDEX idx_user (user_id)
) comment '用户登录记录表' collate = utf8mb4_unicode_ci;

create table if not exists user_invite
(
    id             bigint auto_increment comment 'id' primary key,
    user_id        bigint                             not null comment '创建用户 id',
    invite_user_id bigint                             not null comment '受邀请用户 id',
    create_time    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete      tinyint  default 0                 not null comment '是否删除',
    version        int      default 0                 null comment '版本号',
    INDEX idx_user_invite (user_id, invite_user_id)
) comment '用户邀请表' collate = utf8mb4_unicode_ci;

-- 用户表
create table if not exists user_operation_stream
(
    id           bigint auto_increment comment 'id' primary key,
    user_id      bigint                                 not null comment '创建用户 id',
    type         varchar(128) default 'user'            not null comment '用户操作类型：user/admin',
    param        varchar(512)                           not null comment '参数',
    extendInfo   varchar(256)                           null comment '扩展字段',
    operate_time datetime     default CURRENT_TIMESTAMP not null comment '操作时间时间',
    create_time  datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time  datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete    tinyint      default 0                 not null comment '是否删除',
    version      int          default 0                 null comment '版本号',
    INDEX idx_user_operate (user_id, operate_time)
) comment '用户操作流水表' collate = utf8mb4_unicode_ci;


-- 空间表
create table if not exists space
(
    id          bigint auto_increment comment 'id' primary key,
    space_name  varchar(128)                       null comment '空间名称',
    space_level int      default 0                 null comment '空间级别：0-普通版 1-专业版 2-旗舰版',
    space_type  int      default 0                 not null comment '空间类型：0-私有 1-团队',
    max_size    bigint   default 0                 null comment '空间图片的最大总大小',
    max_count   bigint   default 0                 null comment '空间图片的最大数量',
    total_size  bigint   default 0                 null comment '当前空间下图片的总大小',
    total_count bigint   default 0                 null comment '当前空间下的图片数量',
    user_id     bigint                             not null comment '创建用户 id',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    edit_time   datetime default CURRENT_TIMESTAMP not null comment '编辑时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   tinyint  default 0                 not null comment '是否删除',
    version     int      default 0                 null comment '版本号',
    -- 索引设计
    index idx_userId (user_id),        -- 提升基于用户的查询效率
    index idx_spaceName (space_name),  -- 提升基于空间名称的查询效率
    index idx_spaceLevel (space_level),-- 提升按空间级别查询的效率
    index idx_spaceType (space_type)
) comment '空间' collate = utf8mb4_unicode_ci;


-- 添加新列
create table if not exists picture
(
    id             bigint auto_increment comment 'id' primary key,
    url            varchar(512)                       not null comment '图片 url',
    name           varchar(128)                       not null comment '图片名称',
    space_id       bigint                             null comment '空间 id（为空表示公共空间）',
    introduction   varchar(512)                       null comment '简介',
    category       varchar(64)                        null comment '分类',
    tags           varchar(512)                       null comment '标签（JSON 数组）',
    pic_size       bigint                             null comment '图片体积',
    pic_width      int                                null comment '图片宽度',
    pic_height     int                                null comment '图片高度',
    pic_scale      double                             null comment '图片宽高比例',
    pic_format     varchar(32)                        null comment '图片格式',
    pic_color      varchar(16)                        null comment '图片主色调',
    review_status  INT      DEFAULT 0                 NOT NULL COMMENT '审核状态：0-待审核; 1-通过; 2-拒绝',
    review_message VARCHAR(512)                       NULL COMMENT '审核信息',
    reviewer_id    BIGINT                             NULL COMMENT '审核人 ID',
    review_time    DATETIME                           NULL COMMENT '审核时间',
    thumbnail_url  varchar(512)                       NULL COMMENT '缩略图 url',
    user_id        bigint                             not null comment '创建用户 id',
    create_time    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    edit_time      datetime default CURRENT_TIMESTAMP not null comment '编辑时间',
    update_time    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete      tinyint  default 0                 not null comment '是否删除',
    version        int      default 0                 null comment '版本号',
    INDEX idx_name (name),                 -- 提升基于图片名称的查询性能
    INDEX idx_introduction (introduction), -- 用于模糊搜索图片简介
    INDEX idx_category (category),         -- 提升基于分类的查询性能
    INDEX idx_tags (tags),                 -- 提升基于标签的查询性能
    INDEX idx_userId (user_id),            -- 提升基于用户 ID 的查询性能
    INDEX idx_reviewStatus (review_status),
    INDEX idx_spaceId (space_id)
) comment '图片' collate = utf8mb4_unicode_ci;


-- 空间成员表
create table if not exists space_user
(
    id          bigint auto_increment comment 'id' primary key,
    space_id    bigint                                 not null comment '空间 id',
    user_id     bigint                                 not null comment '用户 id',
    space_role  varchar(128) default 'viewer'          null comment '空间角色：viewer/editor/admin',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   tinyint      default 0                 not null comment '是否删除',
    version     int          default 0                 null comment '版本号',
    -- 索引设计
    UNIQUE KEY uk_spaceId_userId (space_id, user_id), -- 唯一索引，用户在一个空间中只能有一个角色
    INDEX idx_spaceId (space_id),                     -- 提升按空间查询的性能
    INDEX idx_userId (user_id)                        -- 提升按用户查询的性能
) comment '空间用户关联' collate = utf8mb4_unicode_ci;