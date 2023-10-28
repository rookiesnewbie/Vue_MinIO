package com.example.minio.controller;


import com.example.minio.common.Result;
import com.example.minio.entity.File;
import com.example.minio.entity.vo.file.*;
import com.example.minio.service.FileService;
import io.minio.errors.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文件 前端控制器
 * </p>
 *
 * @author liteng
 * @since 2023-10-05
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private FileService fileService;

    /**
     * 条件分页查询
     * @param req
     * @return
     */
    @GetMapping
    public Result pageQueryForFile(@Valid PageQueryForFileReq req){
        return Result.success(fileService.pageQuery(req));
    }

    //创建文件夹
    @PostMapping("/createFolder")
    public Result createFolder(@Valid @RequestBody CreateFolderReq req){
       fileService.createFolder(req.getFilename(), req.getParentId(), req.getEmpId(), req.getIsShared());
       return Result.success().message("创建成功");
    }

    //文件夹重命名
    @PutMapping("/rename")
    public Result renameFile(@Valid @RequestBody RenameFileReq req){
        fileService.renameFile(req.getId(),req.getFilename());
        return Result.success().message("修改成功");
    }
    /**
     * 删除文件/文件夹 —— 放入回收站
     * @param id 文件id
     * @param operatorId 操作人
     * @return null
     */
    @DeleteMapping
    public Result delete(@RequestParam Long id,@Valid  @Nullable @RequestParam Long operatorId){
        fileService.removeFileToRecycleBin(id,operatorId);
        return Result.success().message("删除成功");
    }

    /**
     * 文件上传
     * @param req  请求参数
     * @param multipartFiles 上传的文件
     * @return String
     *
     */
    @PostMapping("/upload")
    public Result uploadFile(@Valid UploadFileReq req, @RequestParam MultipartFile[] multipartFiles) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        fileService.uploadFile(req.getEmpId(),req.getParentId(),req.getIsShared(),multipartFiles);
        return Result.success().message("上传成功");
    }

    /**
     * 文件下载
     * @param id  文件id
     * @param response
     */
    @GetMapping("/download/{id}")
    public void downloadFile(@PathVariable @NotNull Long id, HttpServletResponse response) throws UnsupportedEncodingException, ServerException, InsufficientDataException, InternalException,
            InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, XmlParserException,
            ErrorResponseException {
        fileService.downloadFile(id,response);
    }

    /**
     * 获取共享文件夹列表
     * @return 共享文件夹列表
     */
    @GetMapping("/listAllShareFolder")
    public Result listAllShareFolder(){
       List<File> shareFolderList = fileService.listAllShareFolder();
       return Result.success(shareFolderList);
    }

    /**
     * 个人文件共享至指定共享文件夹中
     * @param req 请求参数
     * @return null
     */
    @PostMapping("/shareFileTo")
    public Result shareFileTo(@Valid @RequestBody ShareFileToReq req){

        fileService.shareFileTo(req.getFileId(),req.getMountFolderId());

        return Result.success().message("分享成功");
    }


    /**
     * 预览文件
     * @param fileId 文件ID
     * @return
     */
    @GetMapping("/previewFile/{fileId}")
    public Result<File> previewFile(@PathVariable("fileId") @NotNull Long fileId,HttpServletResponse response) throws UnsupportedEncodingException {
        return Result.success(fileService.previewFile(fileId,response));
    }

    /**
     * 获取文件总数
     * @return
     */
    @GetMapping("/total")
    public Result getAccountTotal(){


        Map<String, Object> map = fileService.getFileTotal();

        return Result.success(map);
    }

}
