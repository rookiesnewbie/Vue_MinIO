package com.example.minio.entity.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author LiTeng
 * @Date 2023/10/5 22:07
 * Version 1.0
 * @Description
 */
@Getter
@AllArgsConstructor
public enum FileShareTypeEnum {
    /**
     * 非共享文件
     */
    NONE_SHARED(0, "非共享文件"),

    /**
     * 共享文件
     */
    IS_SHARED(1, "共享文件")
    ;

    /**
     * 类型值
     */
    private final Integer value;

    /**
     * 类型名称
     */
    private final String name;
}
