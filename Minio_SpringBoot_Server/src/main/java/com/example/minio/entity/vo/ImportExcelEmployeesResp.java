package com.example.minio.entity.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author LiTeng
 * @Date 2023/10/28 15:04
 * Version 1.0
 * @Description
 */
@Data
@Builder
public class ImportExcelEmployeesResp {

    /**
     * 创建成功的员工账户昵称数组
     */
    private List<String> createNicknames;

    /**
     * 更新成功的员工账户昵称数组
     */
    private List<String> updateNicknames;

    /**
     * 导入失败的员工集合：key 为员工账户昵称，value 为失败原因
     */
    private Map<String, String> failureNicknames;

}
