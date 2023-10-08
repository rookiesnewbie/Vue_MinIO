package com.example.minio.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.minio.entity.RecycleFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.minio.entity.dto.RecycleFileDTO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 回收站 Mapper 接口
 * </p>
 *
 * @author liteng
 * @since 2023-10-05
 */
public interface RecycleFileMapper extends BaseMapper<RecycleFile> {
    /**
     * 分页查询
     * @param page mybatisPlus 原生分页查询，查询SQL会自动拼接分页
     * @param queryWrapper QueryWrapper条件，注意，这里需要使用 @Param("ew") 指定mybatis参数
     * @return 分页结果
     */
    IPage<RecycleFileDTO> pageQuery(IPage<RecycleFile> page, @Param("ew") QueryWrapper<RecycleFile> queryWrapper);
}
