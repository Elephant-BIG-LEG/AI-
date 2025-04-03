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
@TableName("wx_login")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WxLogin implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户的唯一 ID
     */
    private String openid;

    /**
     * 会话 Key
     */
    private String sessionKey;

    /**
     * 唯一标识
     */
    private String unionid;

    private String errcode;

    private String errmsg;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
