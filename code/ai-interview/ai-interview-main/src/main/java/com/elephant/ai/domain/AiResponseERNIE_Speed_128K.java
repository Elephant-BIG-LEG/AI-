package com.elephant.ai.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/02/17/16:24
 * @Description: ERNIE-Speed-128K 大模型响应封装体
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Setter
@Getter
@ToString
public class AiResponseERNIE_Speed_128K {
    private String id;
    private String object;
    private Integer created;
    private Integer sentence_id;
    private Boolean is_end;
    private Boolean is_truncated;
    private String result;
    private Boolean need_clear_history;
    private Integer ban_round;
    private Usage usage;
}
