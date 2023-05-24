/**
* 一、新建数据库
*/
CREATE DATABASE IF NOT EXISTS xingz_cm;

/**
* 二、使用数据库
*/
USE xingz_cm;

/**
* 三、新建表
*/

-- 用户表（user）
CREATE TABLE IF NOT EXISTS user(
	id VARCHAR(36) UNIQUE COMMENT "用户ID",
	username VARCHAR(17) UNIQUE COMMENT "用户名",
	password VARCHAR(30) NOT NULL DEFAULT "xzwz_cm_123456" COMMENT "密码",
	gender ENUM("0", "1", "2") NOT NULL DEFAULT "2" COMMENT "性别（0：女 1：男 2：未知）",
	birthday DATE COMMENT "生日（1970-01-01）",
	phone VARCHAR(11) COMMENT "手机号",
	email VARCHAR(100) COMMENT "邮箱",
	tags VARCHAR(255) COMMENT "标签（例如：['前端'，'后端']）",
	avatar VARCHAR(512) DEFAULT "https://bucket.oss.kkuil/default_avatar.jpg" COMMENT "默认头像",
	isVip ENUM("0", "1") DEFAULT "0" COMMENT "是否为VIP用户（0：非会员 1：会员）",
	isDeleted ENUM("0", "1") DEFAULT "0" COMMENT "是否逻辑删除(0：未删除 1：已删除)",
	createdTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
	modifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT "最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）" ,
	PRIMARY KEY (id)
);

-- 用户文章信息表（user_article）
CREATE TABLE IF NOT EXISTS user_article(
	id VARCHAR(36) COMMENT "用户ID",
	published JSON COMMENT "已发布文章数（例如：[1, 2, 3]记录了已发布的文章的ID）",
	liked JSON COMMENT "点赞信息（例如：[1, 2, 3]记录了点赞了的文章的ID）",
	collected JSON COMMENT "收藏信息（例如：[1, 2, 3]记录了收藏了的文章的ID）",
	isDeleted ENUM("0", "1") DEFAULT "0" COMMENT "是否逻辑删除(0：未删除 1：已删除)",
	createdTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
	modifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT "最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）" ,
	PRIMARY KEY (id),
	FOREIGN KEY (id) REFERENCES user(id)
);

-- 用户星分表（user_rank）
CREATE TABLE IF NOT EXISTS user_rank(
	id VARCHAR(36) COMMENT "用户ID",
	points INT NOT NULL DEFAULT 0 COMMENT "星分大小（每100星分为一等级）",
	isDeleted ENUM("0", "1") DEFAULT "0" COMMENT "是否逻辑删除(0：未删除 1：已删除)",
	createdTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
	modifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT "最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）" ,
	PRIMARY KEY (id),
	FOREIGN KEY (id) REFERENCES user(id)
);

-- 管理员信息表（admin）
CREATE TABLE IF NOT EXISTS admin(
	id VARCHAR(36) COMMENT "管理员账户ID（可用来登录）",
	name VARCHAR(30) NOT NULL COMMENT "管理员名称",
	password VARCHAR(100) DEFAULT "xzwz_qwe123456" NOT NULL COMMENT "管理员密码",
	roleId VARCHAR(30) NOT NULL COMMENT "管理员身份ID",
	deptid VARCHAR(36) NOT NULL COMMENT "部门ID",
	phone VARCHAR(11) NOT NULL UNIQUE COMMENT "手机号（例如：15712345674）",
	avatar VARCHAR(512) NOT NULL DEFAULT "https://bucket.oss.kkuil/default_avatar.jpg" COMMENT "默认头像",
	gender ENUM("0", "1", "2") NOT NULL DEFAULT "2" COMMENT "性别（0：女 1：男 2：未知）",
	isDeleted ENUM("0", "1") DEFAULT "0" COMMENT "是否逻辑删除(0：未删除 1：已删除)",
	createdTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
	modifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT "最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）" ,
	PRIMARY KEY (id),
	FOREIGN KEY (roleId) REFERENCES role(id),
	FOREIGN KEY (deptId) REFERENCES department(id)
);

-- 角色信息（role）
CREATE TABLE IF NOT EXISTS role(
	id VARCHAR(36) COMMENT "角色ID",
	roleName VARCHAR(30) NOT NULL UNIQUE DEFAULT "c_admin" COMMENT "角色名",
	authList VARCHAR(1024) COMMENT "权限列表",
	description VARCHAR(255) DEFAULT "" COMMENT "角色相关描述",
	isDeleted ENUM("0", "1") DEFAULT "0" COMMENT "是否逻辑删除(0：未删除 1：已删除)",
	createdTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
	modifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT "最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）" ,
	PRIMARY KEY (id)
);

-- 角色权限信息 （role_auth）
CREATE TABLE IF NOT EXISTS role_auth(
	id INT AUTO_INCREMENT COMMENT "权限ID（1000以上的整数）",
	authName VARCHAR(30) NOT NULL UNIQUE COMMENT "权限名称",
	authRoute VARCHAR(100) NOT NULL COMMENT "与权限所绑定的路由（例如：/user-manage/user-list）",
	authSideBar VARCHAR(100) NOT NULL COMMENT "与权限所绑定的侧边栏（例如：user-manage:user-list）",
	description VARCHAR(255) NOT NULL DEFAULT "" COMMENT "关于该权限的描述",
	isDeleted ENUM("0", "1") DEFAULT "0" COMMENT "是否逻辑删除(0：未删除 1：已删除)",
	createdTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
	modifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT "最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）" ,
	PRIMARY KEY (id)
);

-- 部门信息（department）
CREATE TABLE IF NOT EXISTS department(
	id VARCHAR(36) COMMENT "部门ID",
	deptName VARCHAR(30) NOT NULL COMMENT "部门名称",
	managerid VARCHAR(36) NOT NULL COMMENT "管理员ID",
	locationId INT NOT NULL COMMENT "地区ID",
	isDeleted ENUM("0", "1") DEFAULT "0" COMMENT "是否逻辑删除(0：未删除 1：已删除)",
	createdTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
	modifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT "最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）" ,
	PRIMARY KEY (id),
	FOREIGN KEY (locationId) REFERENCES location(id)
);

-- 地区信息（location）
CREATE TABLE IF NOT EXISTS location(
	id INT AUTO_INCREMENT COMMENT "地区ID",
	street VARCHAR(255) NOT NULL DEFAULT "中关村" COMMENT "街道",
	city VARCHAR(128) NOT NULL DEFAULT "北京" COMMENT "城市",
	isDeleted ENUM("0", "1") DEFAULT "0" COMMENT "是否逻辑删除(0：未删除 1：已删除)",
	createdTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
	modifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT "最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）" ,
	PRIMARY KEY (id)
);


-- 文章信息（article）
CREATE TABLE IF NOT EXISTS article(
	id VARCHAR(36) COMMENT "文章ID",
	title VARCHAR(100) NOT NULL DEFAULT "星知学习频道" COMMENT "标题",
	content TEXT NOT NULL COMMENT "内容",
	remark TINYTEXT COMMENT "评论信息（例如：'{id: '1', comment: '欢迎', 'createdTime': '1970-01-01 00:00:00', 'modifiedTime': '1970-01-01 00:00:00'}'）",
	statusId INT NOT NULL DEFAULT 0 COMMENT "状态ID",
	cover VARCHAR(255) NOT NULL DEFAULT "https://bucket.oss.kkuil/default_cover.jpg" COMMENT "文章封面图",
	liked JSON COMMENT "点赞信息（例如：[1, 2, 3]记录了点赞了该文章的星友ID）",
	collected JSON COMMENT "收藏信息（例如：[1, 2, 3]记录了收藏了该文章的星友ID）",
	shared JSON COMMENT "转发信息（例如：[1, 2, 3]记录了转发了该文章的星友ID）",
	tags JSON COMMENT "标签信息（例如：[1, 2, 3]记录了标签的ID）",
	isDeleted ENUM("0", "1") DEFAULT "0" COMMENT "是否逻辑删除(0：未删除 1：已删除)",
	createdTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
	modifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT "最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）" ,
	PRIMARY KEY (id),
	FOREIGN KEY (statusId) REFERENCES article_status(id)
);

-- 文章状态映射信息（article_status）
CREATE TABLE IF NOT EXISTS article_status(
	id INT AUTO_INCREMENT COMMENT "文章状态ID",
	statusName VARCHAR(10) NOT NULL UNIQUE COMMENT "状态名",
	isDeleted ENUM("0", "1") DEFAULT "0" COMMENT "是否逻辑删除(0：未删除 1：已删除)",
	createdTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
	modifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT "最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）" ,
	PRIMARY KEY (id)
);

-- 标签信息
CREATE TABLE IF NOT EXISTS tag(
	id INT AUTO_INCREMENT COMMENT "标签ID",
	name VARCHAR(20) NOT NULL UNIQUE COMMENT "标签名",
	isDeleted ENUM("0", "1") DEFAULT "0" COMMENT "是否逻辑删除(0：未删除 1：已删除)",
	createdTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
	modifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT "最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）" ,
	PRIMARY KEY (id)
);

/**
* 四、创建触发器
*/
-- 1. 当向user表插入数据的时候，同步对相关信息表进行插入数据（后置触发器）
CREATE TRIGGER IF NOT EXISTS insert_user_sync_reference_data
AFTER INSERT
ON user 
FOR EACH ROW
BEGIN
	INSERT INTO user_article (id) VALUES (NEW.id);
	INSERT INTO user_rank (id) VALUES (NEW.id);
END;

-- 2. 当进行user表的逻辑删除时，同步逻辑删除相关信息（后置触发器）
CREATE TRIGGER IF NOT EXISTS logic_del_user_sync_reference_data
AFTER UPDATE
ON user
FOR EACH ROW
BEGIN
	IF NEW.isDeleted != OLD.isDeleted THEN
		UPDATE user_article SET isDeleted = NEW.isDeleted WHERE id = NEW.id;
		UPDATE user_rank SET isDeleted = NEW.isDeleted WHERE id = NEW.id;
	END IF;
END;

-- 3. 当向user表插入数据没有指定username时，自动向username插入默认值（前置触发器）
CREATE TRIGGER IF NOT EXISTS specify_username_if_not_in_user
BEFORE INSERT ON user
FOR EACH ROW 
BEGIN
	IF NEW.username IS NULL OR NEW.username = "" THEN
		SET NEW.username = CONCAT("xz_user_", NEW.id);
  END IF;
END;

-- 4. 当向admin表插入数据的时候，同步对相关信息表进行插入数据（后置触发器）
CREATE TRIGGER IF NOT EXISTS insert_admin_sync_reference_data
AFTER INSERT
ON admin 
FOR EACH ROW
BEGIN
	INSERT INTO role (id) VALUES (NEW.id);
END;

-- 5. 当向user表插入数据没有指定username时，自动向username插入默认值（前置触发器）
CREATE TRIGGER IF NOT EXISTS specify_name_if_not_in_admin
BEFORE INSERT 
ON admin
FOR EACH ROW 
BEGIN
	IF NEW.name IS NULL OR NEW.name = "" THEN
		SET NEW.name = CONCAT("xz_admin_", NEW.id);
  END IF;
END;


