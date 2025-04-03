package com.elephant.ai.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
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
@TableName("ai_interview_interview_records")
public class InterviewRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 会话 ID
     */
    private String sessionId;

    /**
     * 角色 assistant、user
     */
    private String role;

    /**
     * 消息的主体内容
     */
    private String content;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 用户的唯一 ID
     */
    private String openid;

    /**
     * 这条记录所属的技术分类细则，关键是 catrgory_item 表
     */
    private String category;

    /**
     * 面试科目，关键是 category 表
     */
    private String subject;

    /**
     * 1 正常 0 删除
     */
    private Integer delFlag;
}
