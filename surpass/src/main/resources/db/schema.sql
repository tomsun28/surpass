use tom;

-- ----------------------------
-- Table structure for auth_resource
-- ----------------------------
DROP TABLE IF EXISTS  auth_resource ;
CREATE TABLE  auth_resource
(
     id           bigint       not null auto_increment comment 'resource ID',
     name         varchar(50)  not null comment 'resource name',
     code         varchar(50)  not null comment 'resource code',
     uri          varchar(255) not null comment 'access URL',
     type         varchar(20)  comment 'resource type',
     parent_id    bigint       comment '父资源编码->菜单',
     method       varchar(10)  not null comment 'access method: GET POST PUT DELETE PATCH',
     status       smallint(4)  not null default 1 comment 'status   1:normal、9：filtering(Exclusion protection-all can access this api)',
     icon         varchar(50)  comment '图标',
     description  varchar(255) comment 'resource description',
     gmt_create   timestamp    default current_timestamp comment 'create time',
     gmt_update   datetime     default current_timestamp on update current_timestamp comment 'update time',
     primary key (id)
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for auth_role
-- ----------------------------
DROP TABLE IF EXISTS  auth_role ;
CREATE TABLE  auth_role
(
     id           bigint       not null auto_increment comment 'role ID',
     name         varchar(50)  not null comment 'role name',
     code         varchar(50)  not null comment 'role code',
     status       smallint(4)  not null default 1 comment 'status   1:normal、9：disable',
     description  varchar(255) comment 'role description',
     gmt_create   timestamp    default current_timestamp comment 'create time',
     gmt_update   datetime     default current_timestamp on update current_timestamp comment 'update time',
     primary key ( id )
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for auth_role_resource_bind
-- ----------------------------
DROP TABLE IF EXISTS auth_role_resource_bind;
CREATE TABLE auth_role_resource_bind
(
    id          bigint not null auto_increment comment 'ID',
    role_id     bigint not null comment 'role ID',
    resource_id bigint not null comment 'resource ID',
    gmt_create   timestamp    default current_timestamp comment 'create time',
    gmt_update   datetime     default current_timestamp on update current_timestamp comment 'update time',
    primary key (id),
    unique key unique_bind (role_id, resource_id)
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for auth_user
-- ----------------------------
DROP TABLE IF EXISTS  auth_user ;
CREATE TABLE auth_user
(
    id           bigint      not null auto_increment comment 'ID',
    username     varchar(50) not null comment 'username',
    password     varchar(50) not null comment 'password=MD5(passwd+salt)',
    salt         varchar(20) comment 'salt',
    avatar       varchar(100) comment 'avatar',
    phone        varchar(20) comment 'phone number(unique)',
    email        varchar(50) comment 'email(unique)',
    sex          tinyint(4) comment 'sex(1.man 2.woman)',
    status       tinyint(4)  not null default 1 comment 'account status(1.normal 2.locked 3.deleted 4.illegal)',
    create_where tinyint(4) comment 'create where(1.web 2.android 3.ios 4.win 5.mac 6.linux)',
    gmt_create   timestamp    default current_timestamp comment 'create time',
    gmt_update   datetime     default current_timestamp on update current_timestamp comment 'update time',
    primary key (id),
    unique (username, phone, email)
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for auth_user_role_bind
-- ----------------------------
DROP TABLE IF EXISTS  auth_user_role_bind ;
CREATE TABLE auth_user_role_bind
(
    id         bigint not null auto_increment comment 'ID',
    user_id    bigint not null comment 'user ID',
    role_id    bigint not null comment 'role ID',
    gmt_create   timestamp    default current_timestamp comment 'create time',
    gmt_update   datetime     default current_timestamp on update current_timestamp comment 'update time',
    primary key (id),
    unique key unique_bind (user_id, role_id)
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for auth_user_role_bind 用户账户操作日志
-- ----------------------------
DROP TABLE IF EXISTS  auth_account_log ;
CREATE TABLE auth_account_log
(
    id         bigint not null auto_increment comment 'ID',
    log_name   varchar(255) not null comment '日志名称(login,register,logout)',
    username   varchar(255) not null comment 'username',
    success    boolean comment '是否执行成功(0失败1成功)',
    message    varchar(255) comment '具体消息',
    remote_ip  varchar(255) comment '登录远程IP',
    gmt_create   timestamp    default current_timestamp comment 'create time',
    gmt_update   datetime     default current_timestamp on update current_timestamp comment 'update time',
    primary key (id)
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for auth_operation_log 用户API操作日志
-- ----------------------------
DROP TABLE IF EXISTS  auth_operation_log ;
CREATE TABLE auth_operation_log
(
    id         bigint not null auto_increment comment 'ID',
    log_name   varchar(255) not null comment '日志名称(login,register,logout)',
    username   varchar(255) not null comment 'username',
    api        varchar(255) comment 'API',
    method     varchar(255) comment '请求方法',
    success    boolean comment '是否执行成功(0失败1成功)',
    message    varchar(255) comment '具体消息',
    gmt_create   timestamp    default current_timestamp comment 'create time',
    gmt_update   datetime     default current_timestamp on update current_timestamp comment 'update time',
    primary key (id)
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4;
