package com.example.minio.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.minio.entity.RecycleFile;
import com.example.minio.entity.dto.RecycleFileDTO;
import com.example.minio.entity.vo.PageParam;
import com.example.minio.excption.MyException;
import com.example.minio.mapper.FileMapper;
import com.example.minio.mapper.RecycleFileMapper;
import com.example.minio.service.FileService;
import com.example.minio.service.RecycleFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.minio.errors.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>
 * 回收站 服务实现类
 * </p>
 *
 * @author liteng
 * @since 2023-10-05
 */
@Service
public class RecycleFileServiceImpl extends ServiceImpl<RecycleFileMapper, RecycleFile> implements RecycleFileService {

    @Resource
    private FileService fileService;


    @Resource
    private FileMapper fileMapper;
    @Resource
    ThreadPoolExecutor threadPoolExecutor;

    @Override
    public void addRecycleFile(Long fileId, Long empId, String deleteBatchNum) {
        RecycleFile recycleFile = new RecycleFile();
        recycleFile.setEmpId(empId);
        recycleFile.setFileId(fileId);
        recycleFile.setDeletedBatchNum(deleteBatchNum);
        recycleFile.setDeletedTime(new Date());

        baseMapper.insert(recycleFile);
    }

    @Override
    public void deleteById(Long id) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        RecycleFile recycleFile = baseMapper.selectById(id);
        if (recycleFile == null){
            throw new MyException(500,"文件不存在");
        }

        //删除文件
        baseMapper.deleteById(id);

        //删除文件的批次号，删除服务器文件
        fileService.realDeleteByBatchNums(ListUtil.of(recycleFile.getDeletedBatchNum()));
    }

    @Override
    public void deleteAll() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        List<RecycleFile> list = this.list();
        if (CollUtil.isEmpty(list)){
            return;
        }
        Set<Long> ids = new HashSet<>();
        Set<String> deletedBatchNums = new HashSet<>();
        list.forEach(recycleFile -> {
            ids.add(recycleFile.getId());
            deletedBatchNums.add(recycleFile.getDeletedBatchNum());
        });
        //清空记录
        baseMapper.deleteBatchIds(ids);

        //删除文件
        fileService.realDeleteByBatchNums(deletedBatchNums);
    }

    @Override
    public void recoveryById(Long id) {
        RecycleFile recycleFile = baseMapper.selectById(id);
        if (recycleFile == null){
            throw new MyException(500,"文件不存在");
        }
        baseMapper.deleteById(id);

        fileService.recoveryByDeleteBatchNum(ListUtil.of(recycleFile.getDeletedBatchNum()));

    }

    private void deleteExceedRecycleTimeFile(DateTime recycleTime){
        threadPoolExecutor.submit(() ->{
            //找到过期的文件
            List<RecycleFile> recycleFiles = baseMapper.selectList(new LambdaQueryWrapper<RecycleFile>()
                    .lt(RecycleFile::getDeletedTime, recycleTime));

            if (CollUtil.isEmpty(recycleFiles)){
                return;
            }

            //获取删除批次号，逻辑删除file与recycle_file表的记录，并删除服务器上的文件
            Set<Long> deletedBatchIds = new HashSet<>();
            Set<String> deletedBatchNums = new HashSet<>();

            recycleFiles.forEach(recycleFile -> {
                deletedBatchIds.add(recycleFile.getId());
                deletedBatchNums.add(recycleFile.getDeletedBatchNum());
            });

            baseMapper.deleteBatchIds(deletedBatchIds);

            try {
                fileService.realDeleteByBatchNums(deletedBatchNums);
            } catch (Exception e) {
                log.error("清理过期文件时发生异常" + e.getMessage());
            }
        });
    }

    @Override
    public IPage<RecycleFileDTO> pageQuery(Long empId, PageParam pageParam) {
        // 七天前的这一时刻，在此此后的删除时间为有效的回收站文件
        DateTime validTime = DateUtil.offsetDay(new Date(), -7);
        IPage<RecycleFile> recycleFileDTOPage = new Page<>(pageParam.getPageNo(), pageParam.getPageSize());
        QueryWrapper<RecycleFile> wrapper = new QueryWrapper<RecycleFile>().eq("rf.emp_id", empId)
                .gt("rf.deleted_time", validTime);
        return baseMapper.pageQuery(recycleFileDTOPage,wrapper);



    }
}
