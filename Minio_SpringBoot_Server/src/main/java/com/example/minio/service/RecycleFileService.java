package com.example.minio.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.minio.entity.RecycleFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.minio.entity.dto.RecycleFileDTO;
import com.example.minio.entity.vo.file.PageParam;
import io.minio.errors.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 * 回收站 服务类
 * </p>
 *
 * @author liteng
 * @since 2023-10-05
 */
public interface RecycleFileService extends IService<RecycleFile> {

    void addRecycleFile(Long id, Long aLong, String deleteBatchNum);

    /**
     * 彻底删除
     *
     * @param id 回收站的记录id
     * @return
     */
    void deleteById(Long id) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    /**
     * 清空回收站
     * @return
     */
    void deleteAll() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;


    /**
     * 恢复文件
     * @param id
     * @return
     */
    void recoveryById(Long id);

    /**
     * 回收站 分页查询
     *
     * @param empId     员工id
     * @param pageParam 分页参数
     * @return 分页结果
     */
    IPage<RecycleFileDTO> pageQuery(Long empId, PageParam pageParam);
}