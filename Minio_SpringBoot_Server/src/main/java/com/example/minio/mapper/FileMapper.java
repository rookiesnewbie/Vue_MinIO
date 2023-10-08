package com.example.minio.mapper;

import com.example.minio.entity.File;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 文件 Mapper 接口
 * </p>
 *
 * @author liteng
 * @since 2023-10-05
 */
public interface FileMapper extends BaseMapper<File> {

    /**
     * 根据删除批次号字段获取文件列表
     * @param deletedBatchNums 删除批次号列表
     * @return 文件列表
     */
    List<File> selectListByDeleteBatchNums(@Param("deletedBatchNums")Collection<String> deletedBatchNums);

    /**
     * 根据删除批次号恢复文件，即修改逻辑删除状态
     * @param deletedBatchNums 删除批次号列表
     */
    void updateDeleteStatusByDeleteBatchNums(@Param("deletedBatchNums") Collection<String> deletedBatchNums);


}
