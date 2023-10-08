package com.example.minio.controller;


import com.example.minio.common.Result;
import com.example.minio.entity.dto.RecycleFileDTO;
import com.example.minio.entity.vo.PageParam;
import com.example.minio.service.RecycleFileService;
import io.minio.errors.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 * 回收站 前端控制器
 * </p>
 *
 * @author liteng
 * @since 2023-10-05
 */
@RestController
@RequestMapping("/recycleFile")
public class RecycleFileController {

    @Resource
    private RecycleFileService recycleFileService;

    /**
     * 回收站 分页查询
     * @param empId 员工id
     * @param pageParam 分页参数
     * @return 分页结果
     */
    @GetMapping
    public Result pageQueryForRecycleFile(@RequestParam Long empId,
                                                          @Valid PageParam pageParam){
        return Result.success(recycleFileService.pageQuery(empId, pageParam));
    }

    /**
     * 彻底删除
     * @param id  回收站的记录id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Long id) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        recycleFileService.deleteById(id);
        return Result.success().message("删除成功");
    }

    /**
     * 清空回收站
     * @return
     */
    @DeleteMapping("deleteAll")
    public Result deleteAll() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        recycleFileService.deleteAll();

        return Result.success().message("清空成功");
    }

    /**
     * 恢复文件
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public Result recoveryById(@PathVariable Long id){
        recycleFileService.recoveryById(id);
        return Result.success().message("恢复成功");
    }


}
