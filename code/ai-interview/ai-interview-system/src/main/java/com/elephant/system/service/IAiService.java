package com.elephant.system.service;

import com.elephant.ai.domain.MpAnswer;
import com.elephant.ai.domain.MpRequest;

import java.io.IOException;

/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/02/14/22:32
 * @Description: AI 大模型调用接口
 */
public interface IAiService {
    MpAnswer chat(MpRequest mpRequest,String openId) throws IOException;
}
