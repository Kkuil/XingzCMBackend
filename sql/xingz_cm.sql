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
	password VARCHAR(30) NOT NULL COMMENT "密码",
	gender TINYINT NOT NULL DEFAULT "2" COMMENT "性别（0：女 1：男 2：未知）",
	birthday DATE COMMENT "生日（1970-01-01）",
	phone VARCHAR(11) COMMENT "手机号",
	email VARCHAR(100) COMMENT "邮箱",
	tagIds VARCHAR(255) COMMENT "标签（例如：[1, 2]）",
	avatar VARCHAR(512) COMMENT "头像",
	bgCover VARCHAR(512) COMMENT "主页背景图",
	isVip ENUM("0", "1") DEFAULT "0" COMMENT "是否为VIP用户（0：非会员 1：会员）",
	isDeleted ENUM("0", "1") DEFAULT "0" COMMENT "是否逻辑删除(0：未删除 1：已删除)",
	createdTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
	modifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT "最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）" ,
	PRIMARY KEY (id)
);

-- 用户文章信息表（user_article）
CREATE TABLE IF NOT EXISTS user_article(
	id VARCHAR(36) COMMENT "用户ID",
	articleId VARCHAR(36) COMMENT "文章ID",
	type INT COMMENT "对于用户来说这篇文章的类型，例如：喜欢，收藏，自己发布",
	createdTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
	PRIMARY KEY (id, articleId, type),
	FOREIGN KEY (id) REFERENCES user(id),
	FOREIGN KEY (articleId) REFERENCES article(id)
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

-- 用户会员信息（user_vip）
CREATE TABLE IF NOT EXISTS user_vip(
	id VARCHAR(10) COMMENT "会员ID",
	userId VARCHAR(10) NOT NULL COMMENT "用户ID",
	expiredTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT "会员过期时间",
	isDeleted ENUM("0", "1") DEFAULT "0" COMMENT "是否逻辑删除(0：未删除 1：已删除)",
	createdTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间（会员开通时间）",
	modifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT "最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）" ,
	PRIMARY KEY (id),
	FOREIGN KEY (userId) REFERENCES user(id)
);

-- 用户标签信息（user_tag）
CREATE TABLE IF NOT EXISTS user_tag(
	id VARCHAR(36) COMMENT "用户ID",
	tagId INT COMMENT "标签ID",
	createdTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
	PRIMARY KEY (id, tagId),
	FOREIGN KEY (id) REFERENCES user(id),
	FOREIGN KEY (tagId) REFERENCES tag(id)
);

-- 用户背景图信息（user_bg）
CREATE TABLE IF NOT EXISTS user_bg (
	name VARCHAR(36) COMMENT "背景图名",
	url VARCHAR(512) PRIMARY KEY COMMENT "背景图url",
	isDeleted ENUM("0", "1") DEFAULT "0" COMMENT "是否逻辑删除(0：未删除 1：已删除)",
	createdTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间（会员开通时间）",
	modifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT "最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）" 
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
	gender TINYINT NOT NULL DEFAULT "2" COMMENT "性别（0：女 1：男 2：未知）",
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
	authIds VARCHAR(1024) COMMENT "权限列表",
	description VARCHAR(255) DEFAULT "" COMMENT "角色相关描述",
	isDeleted ENUM("0", "1") DEFAULT "0" COMMENT "是否逻辑删除(0：未删除 1：已删除)",
	createdTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
	modifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT "最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）" ,
	PRIMARY KEY (id)
);

-- 角色权限信息 （auth）
CREATE TABLE IF NOT EXISTS auth(
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
	id VARCHAR(10) COMMENT "部门ID",
	deptName VARCHAR(30) NOT NULL COMMENT "部门名称",
	managerId VARCHAR(36) NOT NULL COMMENT "管理员ID",
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
	userId VARCHAR(36) NOT NULL COMMENT "用户ID",
	title VARCHAR(100) NOT NULL DEFAULT "星知学习频道" COMMENT "标题",
	content TEXT NOT NULL COMMENT "内容",
	statusId INT NOT NULL DEFAULT 0 COMMENT "状态ID",
	categoryId VARCHAR(100) NOT NULL COMMENT "文章分类",
	cover VARCHAR(255) DEFAULT "https://bucket.oss.kkuil/default_cover.jpg" COMMENT "文章封面图",
	isDeleted ENUM("0", "1") DEFAULT "0" COMMENT "是否逻辑删除(0：未删除 1：已删除)",
	createdTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
	modifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT "最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）" ,
	PRIMARY KEY (id),
	FOREIGN KEY (statusId) REFERENCES article_status(id)
);

-- 文章状态信息（article_status）
CREATE TABLE IF NOT EXISTS article_status(
	id INT AUTO_INCREMENT COMMENT "文章状态ID",
	statusName VARCHAR(10) NOT NULL UNIQUE COMMENT "状态名",
	isDeleted ENUM("0", "1") DEFAULT "0" COMMENT "是否逻辑删除(0：未删除 1：已删除)",
	createdTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
	modifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT "最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）" ,
	PRIMARY KEY (id)
);

-- 文章标签信息（article_tag）
CREATE TABLE IF NOT EXISTS article_tag(
	id VARCHAR(36) COMMENT "文章ID",
	tagId INT COMMENT "标签ID",
	PRIMARY KEY (id, tagId),
	FOREIGN KEY (tagId) REFERENCES tag(id),
	FOREIGN KEY (id) REFERENCES article(id)
);

-- 文章分类信息（article_category）
CREATE TABLE IF NOT EXISTS article_category(
	id VARCHAR(36) COMMENT "文章ID",
	categoryId VARCHAR(10) COMMENT "分类ID",
	PRIMARY KEY (id, categoryId),
	FOREIGN KEY (categoryId) REFERENCES category(id),
	FOREIGN KEY (id) REFERENCES article(id)
);

-- 文章评论信息（article_comment）
CREATE TABLE IF NOT EXISTS article_comment(
  `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT "评论ID",
  `articleId` VARCHAR(36) NOT NULL COMMENT "文章ID",
  `parentId` INT UNSIGNED DEFAULT NULL COMMENT "父级评论ID",
  `userId` VARCHAR(36) NOT NULL COMMENT "用户ID",
  `content` TEXT NOT NULL COMMENT "评论内容",
  `likedCount` INT UNSIGNED DEFAULT 0 COMMENT "喜欢数",
  `dislikedCount` INT UNSIGNED DEFAULT 0 COMMENT "不喜欢数",
  `createdTime` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
  `updatedTime` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT "更新时间",
  FOREIGN KEY (`articleId`) REFERENCES `article`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`parentId`) REFERENCES `article_comment`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`userId`) REFERENCES `user`(`id`) ON DELETE CASCADE
);

-- 评论反映详情
CREATE TABLE IF NOT EXISTS comment_reaction (
  `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '唯一标识每条记录的整数',
  `commentId` INT UNSIGNED NOT NULL COMMENT '关联到评论的外键，表示该条点赞/不喜欢记录对应的评论',
  `userId` VARCHAR(36) NOT NULL COMMENT '关联到用户的外键，表示该条点赞/不喜欢记录的用户',
  `reaction` INT UNSIGNED NOT NULL COMMENT '表示用户对评论的反应类型，例如点赞或不喜欢',
  `createdTime` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间，用于记录用户点赞或表示不喜欢的时间戳',
  UNIQUE KEY `user_comment_reaction` (`userId`, `commentId`),
  FOREIGN KEY (`commentId`) REFERENCES `article_comment`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`userId`) REFERENCES `user`(`id`) ON DELETE CASCADE
);

-- 分类信息（article_category）
CREATE TABLE IF NOT EXISTS category(
	id VARCHAR(10) COMMENT "分类ID",
	categoryName TINYTEXT COMMENT "分类名",
	createdTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间（评论时间）",
	modifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT "最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）" ,
	PRIMARY KEY (id)
);

-- 标签信息
CREATE TABLE IF NOT EXISTS tag(
	id INT AUTO_INCREMENT COMMENT "标签ID",
	tagName VARCHAR(20) NOT NULL UNIQUE COMMENT "标签名",
	isDeleted ENUM("0", "1") DEFAULT "0" COMMENT "是否逻辑删除(0：未删除 1：已删除)",
	createdTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
	modifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT "最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）" ,
	PRIMARY KEY (id)
);

-- 日志信息
CREATE TABLE IF NOT EXISTS log(
	id VARCHAR(10) COMMENT "日志ID",
	path VARCHAR(50) COMMENT "请求路径",
	method VARCHAR(10) COMMENT "请求方法",
	ip VARCHAR(100) COMMENT "请求ip",
	params VARCHAR(512) COMMENT "请求参数",
	totalTime VARCHAR(10) COMMENT "请求耗时",
	isDeleted ENUM("0", "1") DEFAULT "0" COMMENT "是否逻辑删除(0：未删除 1：已删除)",
	createdTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
	modifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT "最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）" ,
	PRIMARY KEY (id)
);

-- ChatGPTModel
CREATE TABLE IF NOT EXISTS chatgpt_model (
	id VARCHAR(19) COMMENT "模型ID",
	name VARCHAR(30) COMMENT "模型名称",
	isDeleted ENUM("0", "1") DEFAULT "0" COMMENT "是否逻辑删除(0：未删除 1：已删除)",
	cover VARCHAR(255) COMMENT "模型封面图",
	createdTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间",
	modifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT "最后一次修改时间（ON UPDATE CURRENT_TIMESTAMP）" ,
	PRIMARY KEY (id)
)

SELECT a.id, COUNT(*)
FROM article AS a 
JOIN article_liked AS al ON a.id = al.id
GROUP BY a.id;

