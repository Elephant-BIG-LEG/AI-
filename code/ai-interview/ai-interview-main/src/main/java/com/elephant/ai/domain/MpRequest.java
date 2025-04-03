package com.elephant.ai.domain;

import lombok.*;

/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/02/14/22:51
 * @Description: 小程序 AI 发送的请求内容
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MpRequest {
    /**
     * 小程序发送的主体内容
     *
     * 提问时候发送的面试内容，比如：JVM、Java 基础 ...
     */
    private String content;

    /**
     * 当前的会话 Id
     */
    private String sessionId;

    /**
     * 消息类型 q：提问 a：回答
     */
    private String type;

    /**
     * 模拟的科目
     */
    private String subject;

    /**
     * 使用大模型的名称
     */
    private String modelName;
}
