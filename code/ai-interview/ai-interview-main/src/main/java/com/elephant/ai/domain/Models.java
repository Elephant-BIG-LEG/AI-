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
@TableName("ai_interview_models")
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class Models implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 模型名字
     */
    private String name;

    /**
     * 模型调用地址
     */
    private String url;

    /**
     * 提问的示词
     */
    private String questionPrompt;

    /**
     * 回答的提示词
     */
    private String answerPrompt;

    /**
     * AI 模型的身份设定
     */
    private String role;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 1 正常 0 删除
     */
    private Integer delFlag;

    /**
     * 0 免费 1 收费
     */
    private Integer charge;

    /**
     * 0 单轮对话 1 多轮对话
     */
    private Integer multiple;

    /**
     * 排序字段
     */
    private Integer orderNum;
}
