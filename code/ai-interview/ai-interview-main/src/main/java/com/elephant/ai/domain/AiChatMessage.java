package com.elephant.ai.domain;

import lombok.*;
import java.time.LocalDateTime;

/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/02/17/16:32
 * @Description: Ai 对话消息
 */
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AiChatMessage {
    private Integer id;
    private String sessionId;
    private String role;
    private String content;
    private String openId;
    private String subject;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String category;
    private Integer delFlag;

    @Override
    public String toString() {
        return "AiChatMessage{" +
                "id=" + id +
                ", sessionId='" + sessionId + '\'' +
                ", role='" + role + '\'' +
                ", content='" + content + '\'' +
                ", openId='" + openId + '\'' +
                ", subject='" + subject + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", category='" + category + '\'' +
                ", delFlag=" + delFlag +
                '}';
    }
}
