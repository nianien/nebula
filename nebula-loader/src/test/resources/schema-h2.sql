-- noinspection SqlNoDataSourceInspectionForFile
CREATE SCHEMA `rule_dict`;
SET SCHEMA `rule_dict`;
-- noinspection SqlNoDataSourceInspectionForFile
CREATE TABLE `rule_word`
(
    `id`                BIGINT(20)    NOT NULL AUTO_INCREMENT,
    `source`            INT           NOT NULL DEFAULT '-1' COMMENT '数据来源,支持按位与运算,1:汇川,2:应用商店 4:卧龙',
    `operator_id`       BIGINT        NOT NULL DEFAULT '-1' COMMENT '审核员id',
    `word`              VARCHAR(1024) NOT NULL DEFAULT '' COMMENT '关键词',
    `word_type`         INT           NOT NULL COMMENT '词表类型, 1:黑名单，2：商标词，3：竞品词，4：敏感词',
    `match_type`        INT           NOT NULL DEFAULT '-1' COMMENT '匹配规则, 0:分散,1:包含,2:精确',
    `entity_type`       INT           NOT NULL DEFAULT '-1' COMMENT '实体类型,支持按位与运算,1:创意,2:关键词',
    `exclude_words`     TEXT COMMENT '非限词,以"\n"分隔',
    `exclude_users`     TEXT COMMENT '非限用户,以","分隔',
    `exclude_customers` TEXT COMMENT '非限客户ID,以","分隔',
    `industries`        VARCHAR(1024) NOT NULL DEFAULT '0' COMMENT '生效行业ID，以","分隔',
    `grade`             tinyint(4)    NOT NULL DEFAULT 1 COMMENT '等级 1:低,2:高',
    `status`            TINYINT       NOT NULL DEFAULT '1' COMMENT '规则状态, 0:待评估, 1:生效, 2:失效',
    `remark`            VARCHAR(1024) NOT NULL DEFAULT '' COMMENT '备注',
    `risk_tag`          VARCHAR(1024) NOT NULL DEFAULT '' COMMENT '风险标签',
    `extra_data`        VARCHAR(1024) NOT NULL DEFAULT '' COMMENT '附加数据',
    `create_time`       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_time`       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
);/*规则词表*/

CREATE TABLE `similar_word`
(
    `id`            BIGINT(20)    NOT NULL AUTO_INCREMENT,
    `operator_id`   BIGINT(20)    NOT NULL DEFAULT '-1' COMMENT '审核员id',
    `source`        int(11)       NOT NULL DEFAULT 0 COMMENT '数据来源,1:卧龙,2:汇川,4:应用分发',
    `original_word` VARCHAR(1024) NOT NULL DEFAULT '' COMMENT '原体词名称',
    `similar_word`  VARCHAR(4096) NOT NULL DEFAULT '' COMMENT '变体词名称, 以"\n"分隔',
    `status`        TINYINT       NOT NULL DEFAULT '1' COMMENT '规则状态  1:生效, 2:失效',
    `remark`        VARCHAR(1024) NOT NULL DEFAULT '' COMMENT '备注',
    `extra_data`    VARCHAR(1024) NOT NULL DEFAULT '' COMMENT '附加数据',
    `create_time`   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_time`   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
);/*变体规则词表*/
