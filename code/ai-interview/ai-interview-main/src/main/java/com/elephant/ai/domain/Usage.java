package com.elephant.ai.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/02/17/16:28
 * @Description: AI 大模型 Tokens 的统一信息
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Usage {
    //问题tokens数
    private Integer prompt_tokens;
    //回答tokens数
    private Integer completion_tokens;
    //tokens总数
    private Integer total_tokens;
}
