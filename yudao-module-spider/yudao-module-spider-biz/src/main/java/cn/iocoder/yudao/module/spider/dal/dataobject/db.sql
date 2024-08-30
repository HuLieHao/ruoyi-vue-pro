drop table if exists spider_games;
CREATE TABLE `spider_games` (
                                `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键编号',
                                `name` varchar(255) NOT NULL COMMENT '游戏名字',
                                `creator` varchar(64) DEFAULT '' COMMENT '创建者',
                                `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `updater` varchar(64) DEFAULT '' COMMENT '更新者',
                                `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='游戏名库';

drop table if exists spider_seller;
CREATE TABLE `spider_seller` (
                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键编号',
                                 `name` varchar(255) NOT NULL COMMENT '商家名字',
                                 `creator` varchar(64) DEFAULT '' COMMENT '创建者',
                                 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `updater` varchar(64) DEFAULT '' COMMENT '更新者',
                                 `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='爬虫商家';

drop table if exists spider_seller_games;
CREATE TABLE `spider_seller_games` (
                                       `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键编号',
                                       `games_id` bigint NOT NULL COMMENT '游戏id',
                                       `seller_id` bigint NOT NULL COMMENT '商家id',
                                       `spider_id` varchar(25) DEFAULT '' COMMENT '游戏id',
                                       `spider_keywords` varchar(512) DEFAULT '' COMMENT '爬虫关键字',

                                       `creator` varchar(64)  DEFAULT '' COMMENT '创建者',
                                       `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `updater` varchar(64)  DEFAULT '' COMMENT '更新者',
                                       `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                       `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='爬虫商家游戏';

drop table if exists spider_games_price_record;
CREATE TABLE `spider_games_price_record` (
                                             `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键编号',
                                             `games_id` bigint NOT NULL COMMENT '游戏',
                                             `games_name` varchar(255) NOT NULL COMMENT '游戏名字',
                                             `seller_id` bigint NOT NULL COMMENT '商家id',
                                             `seller_name` varchar(255) NOT NULL COMMENT '商家名字',
                                             `seller_games_id` bigint NOT NULL COMMENT '商家游戏id',
                                             `price` decimal(6, 2) NOT NULL COMMENT '二手价格',
                                             `inside_diff` int NOT NULL DEFAULT 0 COMMENT '本店回收',
                                             `outside_diff` int NOT NULL DEFAULT 0 COMMENT '其它回收',
                                             `capture_date` varchar(10) NOT NULL COMMENT '抓取日期',

                                             `creator` varchar(64)  DEFAULT '' COMMENT '创建者',
                                             `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                             `updater` varchar(64)  DEFAULT '' COMMENT '更新者',
                                             `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                             `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
                                             PRIMARY KEY (`id`) USING BTREE,
                                             UNIQUE KEY uniq_seller_games_date(`seller_games_id`, `capture_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='爬虫商家游戏记录';
