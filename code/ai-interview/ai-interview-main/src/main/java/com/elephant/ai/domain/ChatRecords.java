package com.elephant.ai.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/02/24/22:20
 * @Description: 面试记录类
 */
@Setter
@Getter
public class ChatRecords {
    private InterviewRecords question;

    private InterviewRecords answer;
}
