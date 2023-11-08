package com.example.minio.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author LiTeng
 * @Date 2023/11/8 10:34
 * Version 1.0
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountPasswordDto {
    private Long id;
    private String password;

}
