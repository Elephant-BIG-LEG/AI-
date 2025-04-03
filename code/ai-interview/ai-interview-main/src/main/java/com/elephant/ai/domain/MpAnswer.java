package com.elephant.ai.domain;

import lombok.*;

/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/02/14/22:55
 * @Description: 响应小程序的响应体
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MpAnswer {
    /**
     * 题目类型
     */
    private String type;
    /**
     * 题目答案
     */
    private String content;
}
