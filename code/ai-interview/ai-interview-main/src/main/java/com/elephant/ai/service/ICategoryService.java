package com.elephant.ai.service;

import com.elephant.ai.domain.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.elephant.ai.domain.CategoryItem;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Elephant
 * @since 2025-01-19
 */
public interface ICategoryService extends IService<Category> {

    /**
     * 查询所有技术类别
     *
     * @return
     */
    List<Category> getAllCategory();

}
