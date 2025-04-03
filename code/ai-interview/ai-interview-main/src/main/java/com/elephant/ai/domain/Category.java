package com.elephant.ai.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>
 * 
 * </p>
 *
 * @author Elephant
 * @since 2025-01-19
 */
@Getter
@Setter
@ToString
@TableName("ai_interview_category")
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 宫格中的文本
     */
    private String text;

    /**
     * 宫格图片中的地址
     */
    private String src;

    /**
     * 宫格技术的名称
     */
    private String name;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 1 正常 0 删除
     */
    private Integer delFlag;

    /**
     * 排序字段
     */
    private Integer orderNum;

    /**
     * 是否启动
     */
    private Boolean enable;
}
