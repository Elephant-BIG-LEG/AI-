package com.elephant.ai.service;

import com.elephant.ai.domain.Models;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Elephant
 * @since 2025-01-19
 */
public interface IModelsService extends IService<Models> {

    List<Models> getAllModels();
}
