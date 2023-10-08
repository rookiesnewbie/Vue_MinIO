package com.example.minio.service;

import com.example.minio.entity.File;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.minio.entity.vo.PageQueryForFileReq;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 文件 服务类
 * </p>
 *
 * @author liteng
 * @since 2023-10-05
 */
public interface FileService extends IService<File> {

    Object pageQuery(PageQueryForFileReq req);

    //创建文件夹
    void createFolder(String filename, Long parentId, Long empId, Integer isShared);

    //文件夹重命名
    void renameFile(Long id, String filename);

    /**
     * 逻辑删除文件，放入回收站
     * @param id 文件id
     * @param operatorId 操作人
     */
    void removeFileToRecycleBin(Long id, Long operatorId);

    /**
     * 获取id文件夹下的所有子结点（多级） —— 递归
     * @param id 文件id列表
     * @param empId 创建人
     * @param isShare 是否共享文件夹
     * @return 子结点id集合
     */
    Set<Long> getAllChildFileId(Long id, Long empId, Boolean isShare);

    /**
     * 上传文件
     * @param empId 用户id
     * @param parentId 父目录id
     * @param isShared 是否共享
     * @param file 上传的文件信息
     */
    void uploadFile(Long empId, Long parentId, Integer isShared, MultipartFile[] file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    /**
     *
     * @param id 文件id
     * @param response
     */
    void downloadFile(Long id, HttpServletResponse response) throws UnsupportedEncodingException, ServerException, InsufficientDataException, InternalException,
            InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, XmlParserException,
            ErrorResponseException;

    /**
     * 获取共享文件夹列表
     * @return 共享文件夹列表
     */

    List<File> listAllShareFolder();


    /**
     * 个人文件共享至指定共享文件夹中
     * @param fileId 文件id
     * @param mountFolderId 文件挂载目录id
     * @return null
     */
    void shareFileTo(Long fileId, Long mountFolderId);

    /**
     * 预览文件
     * @param fileId 文件ID
     * @return
     */
    File previewFile(Long fileId, HttpServletResponse response) throws UnsupportedEncodingException;

    //删除服务器文件
    void realDeleteByBatchNums(Collection<String> deletedBatchNums) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    /**
     * 恢复文件的逻辑删除状态为正常
     * @param deletedBatchNums 删除批次号列表
     */
    void recoveryByDeleteBatchNum(Collection<String> deletedBatchNums);

    /**
     * 获取文件总数
     * @return
     */
    Map<String, Object> getFileTotal();
}
