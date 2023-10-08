package com.example.minio.entity.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeleteStatusEnum {

    /**
     * 未删除
     */
    NONE_DELETED(false, "未删除"),

    /**
     * 已删除
     */
    DELETED(true, "已删除")
    ;

    /**
     * 值
     */
    private final Boolean value;

    /**
     * 名称
     */
    private final String name;

}
