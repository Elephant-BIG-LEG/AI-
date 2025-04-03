package com.elephant.ai.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>
 * 
 * </p>
 *
 * @author Elephant
 * @since 2025-01-19
 */
@Getter
@Setter
@ToString
@TableName("ai_interview_category_item")
public class CategoryItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 下拉框展示的文本
     */
    private String text;

    /**
     * 下拉框具体的值
     */
    private String value;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 1 正常 0 删除
     */
    private Integer delFlag;

    /**
     * 所属的技术分类
     */
    private String category;

    /**
     * 排序字段
     */
    private Integer orderNum;
}
