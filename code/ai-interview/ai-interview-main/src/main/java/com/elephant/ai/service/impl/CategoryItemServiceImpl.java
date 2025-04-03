package com.elephant.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.elephant.ai.domain.Category;
import com.elephant.ai.domain.CategoryItem;
import com.elephant.ai.mapper.CategoryItemMapper;
import com.elephant.ai.service.ICategoryItemService;
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
public class CategoryItemServiceImpl extends ServiceImpl<CategoryItemMapper, CategoryItem> implements ICategoryItemService {

    @Override
    public List<CategoryItem> getCategoryItems(String category) {
        LambdaQueryWrapper<CategoryItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CategoryItem::getDelFlag,1);
        queryWrapper.eq(CategoryItem::getText,category);
        queryWrapper.orderByAsc(CategoryItem::getOrderNum);
        queryWrapper.select(CategoryItem::getText,CategoryItem::getValue);
        System.out.println(queryWrapper);
        return list(queryWrapper);
    }
}
