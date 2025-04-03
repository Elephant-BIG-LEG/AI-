package com.elephant.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.elephant.ai.domain.Category;
import com.elephant.ai.domain.CategoryItem;
import com.elephant.ai.mapper.CategoryMapper;
import com.elephant.ai.service.ICategoryService;
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
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Override
    public List<Category> getAllCategory() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getDelFlag,1);
        queryWrapper.orderByAsc(Category::getOrderNum);
        queryWrapper.select(Category::getText,Category::getSrc,Category::getEnable);
        return list(queryWrapper);
    }

}
