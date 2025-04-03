package com.elephant.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.elephant.ai.domain.Models;
import com.elephant.ai.mapper.ModelsMapper;
import com.elephant.ai.service.IModelsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Elephant
 * @since 2025-01-19
 */
@Service
public class ModelsServiceImpl extends ServiceImpl<ModelsMapper, Models> implements IModelsService {

    @Override
    public List<Models> getAllModels() {
        LambdaQueryWrapper<Models> qw = new LambdaQueryWrapper<>();
        qw.eq(Models::getDelFlag,1);
        qw.select(Models::getName,Models::getCharge,Models::getMultiple);
        qw.orderByAsc(Models::getOrderNum);
        return list(qw);
    }
}
