package com.elephant.ai.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.logging.log4j.message.Message;

import java.util.List;
import java.util.Map;

/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/02/17/16:14
 * @Description: ERNIE-Speed-128K 模型请求封装体
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Setter
@Getter
public class AiRequestBodyERNIE_Speed_128K {
    private List<AiChatMessage> messages;
    private Boolean stream;
    private Float temperature;
    private Float top_p;
    private Float penalty_score;
    private String system;
    private List<String> stop;
    private Integer max_output_tokens;
    private Integer frequency_penalty;
    private Float presence_penalty ;
    private String user_id;
    private Map<String,String> metadata;

    @Override
    public String toString() {
        return "AiRequestBodyERNIE_Speed_128K{" +
                "messages=" + messages +
                ", stream=" + stream +
                ", temperature=" + temperature +
                ", top_p=" + top_p +
                ", penalty_score=" + penalty_score +
                ", system='" + system + '\'' +
                ", stop=" + stop +
                ", max_output_tokens=" + max_output_tokens +
                ", frequency_penalty=" + frequency_penalty +
                ", presence_penalty=" + presence_penalty +
                ", user_id='" + user_id + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}
