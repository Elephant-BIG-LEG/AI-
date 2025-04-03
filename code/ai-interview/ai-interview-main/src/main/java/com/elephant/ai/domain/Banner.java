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
@TableName("ai_interview_banner")
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Banner implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 图片路径
     */
    private String image;

    /**
     * banner 中的标题
     */
    private String title;

    /**
     * 点击 banner 的跳转路径
     */
    private String url;

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
}
