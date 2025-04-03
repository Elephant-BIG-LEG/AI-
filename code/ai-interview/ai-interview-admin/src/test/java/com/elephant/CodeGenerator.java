package com.elephant;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;


/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/01/18/22:06
 * @Description: 测试类
 */
public class CodeGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:13306/ai_interview",
                        "root", "abc123")
                .globalConfig(builder -> builder
                        .author("Elephant")
                        .outputDir("D:\\AI\\code\\ai-interview\\ai-interview-main\\src\\main\\java")
                )
                .packageConfig(builder -> builder
                        .parent("com.elephant.ai")
                        .entity("domain")
                        .mapper("mapper")
                        .service("service")
                        .serviceImpl("service.impl")
                        .xml("mapper.xml")
                )
                .strategyConfig(builder -> builder
                        .addInclude("ai_interview_banner",
                                "ai_interview_category",
                                "ai_interview_category_item",
                                "ai_interview_interview_records",
                                "ai_interview_models",
                                "wx_login")
                        .addTablePrefix("ai_interview_")
                        .entityBuilder()
                        .enableLombok()
                )
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
