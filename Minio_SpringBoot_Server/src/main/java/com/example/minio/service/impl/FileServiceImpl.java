package com.example.minio.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.minio.config.MinioConfig;
import com.example.minio.constant.ProjectConstant;
import com.example.minio.entity.Account;
import com.example.minio.entity.Enum.DeleteStatusEnum;
import com.example.minio.entity.Enum.FileShareTypeEnum;
import com.example.minio.entity.File;
import com.example.minio.entity.vo.PageQueryForFileReq;
import com.example.minio.excption.MyException;
import com.example.minio.mapper.FileMapper;
import com.example.minio.service.AccountService;
import com.example.minio.service.FileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.minio.service.RecycleFileService;
import com.example.minio.util.JwtUtil;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import static com.example.minio.entity.Enum.FileShareTypeEnum.IS_SHARED;
import static com.example.minio.entity.Enum.FileShareTypeEnum.NONE_SHARED;

/**
 * <p>
 * 文件 服务实现类
 * </p>
 *
 * @author liteng
 * @since 2023-10-05
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;
    @Resource
    private RecycleFileService recycleFileService;

    @Resource
    private AccountService accountService;

    @Resource
    private MinioClient minioClient;

    @Resource
    private MinioConfig minioConfig;

    @Override
    public Object pageQuery(PageQueryForFileReq req) {
        boolean queryShare = req.getIsShared() != null && Objects.equals(req.getIsShared(),IS_SHARED.getValue());
        Page<File> filePage = new Page<>(req.getPageNo(),req.getPageSize());
        LambdaQueryWrapper<File> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(req.getName() != null,File::getName,req.getName())
                        .eq(req.getAccountId() != null,File::getEmpId,req.getAccountId())
                                //查询个人文件
                                .func(!queryShare,personal ->{
                                    personal.eq(File::getIsShared,NONE_SHARED.getValue())
                                            .eq(File::getParentId,req.getParentId())
                                            .eq(File::getEmpId,JwtUtil.getCurrentUser().getId());
                                })
                                //查询共享文件
                .func(queryShare, share ->{
                    share.apply("IF(mount_share_folder_id IS NOT NULL, mount_share_folder_id, " +
                            "IF(is_shared = {0}, parent_id, NULL)) = {1}", IS_SHARED.getValue(), req.getParentId());
                });


        return   baseMapper.selectPage(filePage, wrapper);
    }

    @Override
    public void createFolder(String filename, Long parentId, Long empId, Integer isShared) {
        Account account = accountService.getById(empId);
        File file = new File();
        file.setName(filename);
        file.setParentId(parentId);
        file.setEmpId(empId);
        file.setCreator(account.getNickname());
        file.setType(ProjectConstant.FOLDER);
        file.setCreateTime(new Date());
        file.setSize("-");
        file.setIsShared(isShared == null? NONE_SHARED.getValue() : isShared);
        baseMapper.insert(file);
    }

    @Override
    public void renameFile(Long id, String filename) {
        File file = new File();
        file.setId(id);
        file.setName(filename);
        baseMapper.updateById(file);
    }

    /**
     *
     * @param id 文件id
     * @param operatorId 操作人
     */
    @Override
    public void removeFileToRecycleBin(Long id, Long operatorId) {

        //校验文件是否存在
        File file = baseMapper.selectById(id);
        if (null == file){
            throw new MyException(500,"文件不存在");
        }

        //生成删除的批次
        String deleteBatchNum = IdUtil.fastUUID();

        LambdaUpdateWrapper<File> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(File::getIsDeleted, DeleteStatusEnum.DELETED.getValue())
                .set(File::getDeletedBatchNum,deleteBatchNum)
                .eq(File::getId,id);

        baseMapper.update(file,wrapper);

        // 在回收站中创建记录，operatorId不为空表示删除操作发生在共享文件，此时放入操作人的回收站中
        // 已经判断 file != null

        recycleFileService.addRecycleFile(id,operatorId != null? operatorId:file.getEmpId(), deleteBatchNum);

        //如果为文件夹，需要逻辑删除其下面的所有内容，并设置删除批次
        if (file.isFolder()){
            markDeleteAllChildByFileId(id,deleteBatchNum,file.getEmpId(),file.isShare());
        }
    }

    /**
     * 由于父文件夹被移入回收站，需要将其下所有内容也逻辑删除
     * @param fileId 父文件夹id
     * @param deleteBatchNum 删除批次号
     * @param empId 创建人
     * @param isShare 是否共享
     */

    private void markDeleteAllChildByFileId(Long fileId, String deleteBatchNum, Long empId, Boolean isShare) {
        threadPoolExecutor.submit(() ->{
            baseMapper.update(null,
                    Wrappers.<File>lambdaUpdate()
                            .set(File::getIsDeleted, DeleteStatusEnum.DELETED.getValue())
                            .set(File::getDeletedBatchNum, deleteBatchNum)
                            .in(File::getId, getAllChildFileId(fileId, empId, isShare)));
        });
    }

    @Override
    public Set<Long> getAllChildFileId(Long id, Long empId, Boolean isShare) {
        List<File> fileList;
        if (isShare) {
            // 获取所有共享文件
            LambdaQueryWrapper<File> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(File::getIsShared, FileShareTypeEnum.IS_SHARED.getValue());
            fileList = baseMapper.selectList(wrapper);
        } else {
            // 拿到创建人，查询其名下所有文件
            LambdaQueryWrapper<File> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(File::getEmpId, empId);
            fileList = baseMapper.selectList(wrapper);
        }
        // 递归构造id文件夹下的文件列表（多级）
        Set<Long> childIds = new HashSet<>();
        recursive(fileList, childIds, id, Integer.MAX_VALUE);
        return childIds;
    }

    @Override
    public void uploadFile(Long empId, Long parentId, Integer isShared, MultipartFile[] multipartFiles) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        //判断上传的文件是否为空
        if(ArrayUtil.isEmpty(multipartFiles)){
            return;
        }

        //判断bucket存储桶是否存在，不存在则创建
        boolean bucketedExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioConfig.getBucketName()).build());

        if (!bucketedExists){
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioConfig.getBucketName()).build());
        }

        Date date = new Date();

        //上传文件时，再文件名上添加时间戳，防止文件同名被覆盖
        String basePath = empId+ DateUtil.format(date,ProjectConstant.FORMAT_YEAR_MONTH_DAY_WITH_FILE_SEPARATOR) + DateUtil.beginOfDay(date).between(date,DateUnit.MS);

        List<File> fileList = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            String originalFilename = multipartFile.getOriginalFilename(); //源文件名
            String filePath = basePath + originalFilename;  //文件的存储路径
            File file = new File();
            file.setEmpId(empId);
            file.setParentId(parentId);
            file.setIsShared(isShared == null? NONE_SHARED.getValue() : isShared);
            file.setName(originalFilename);
            file.setType(FileUtil.extName(filePath));
            file.setPath(filePath);
            file.setCreator(JwtUtil.getCurrentUser().getNickname());
            file.setCreateTime(date);
            file.setSize(formatSize(multipartFile.getSize()));
            file.setContentType(multipartFile.getContentType());
            minioClient.putObject(PutObjectArgs.builder().bucket(minioConfig.getBucketName()).object(filePath)
                    .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                    .contentType(multipartFile.getContentType()).build());

            fileList.add(file);
        }

        this.saveBatch(fileList);


    }

    @Override
    public void downloadFile(Long id, HttpServletResponse response) throws UnsupportedEncodingException, ServerException, InsufficientDataException, InternalException,
            InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, XmlParserException,
            ErrorResponseException{
        File file = baseMapper.selectById(id);
        if (null == file){
            throw new MyException(500,"文件不存在或已经被删除");
        }
        // Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
        // attachment表示以附件方式下载 inline表示在线打开 "Content-Disposition: inline; filename=文件名.mp3"
        // filename表示文件的默认名称，因为网络传输只支持URL编码的相关字符，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
        // encode后replace替换"+"  解决空格问题
        // 已经判断file不为null，忽略提示
        String fileName = URLEncoder.encode(file.getName(), "UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*"); // 允许所有域名访问，或者指定允许的域名
        response.setHeader("Content-Disposition", "attachment;" + "filename=" + fileName.replace("+", "%20"));
        response.setHeader("content-type", file.getContentType());
        response.setContentType("application/octet-stream");
        try (InputStream in = minioClient.getObject(GetObjectArgs.builder().bucket(minioConfig.getBucketName()).object(file.getPath()).build());
             OutputStream out = response.getOutputStream()) {
            IoUtil.copy(in, out);
        } catch (IOException e) {
            log.error("Download file error", e);
        }
    }

    /**
     * 获取共享文件夹列表
     * @return
     */
    @Override
    public List<File> listAllShareFolder() {
        List<File> fileList = baseMapper.selectList(new LambdaQueryWrapper<File>()
                .select(File::getId, File::getName, File::getParentId)
                .eq(File::getType, ProjectConstant.FOLDER)
                .eq(File::getIsShared, IS_SHARED.getValue()));
        return fileList;
    }

    @Override
    public void shareFileTo(Long fileId, Long mountFolderId) {
        //校验文件是否存在
        File file = baseMapper.selectById(fileId);
        if (null == file){
            throw new MyException(500,"文件不存在");

        }
//        // 校验挂载的文件夹是否存在
//        File mountFolder = baseMapper.selectById(mountFolderId);
//        if (null == mountFolder){
//            throw new MyException(500,"挂载的文件夹不存在");
//        }

        file.setMountShareFolderId(mountFolderId);
        baseMapper.updateById(file);
    }


    @Override
    public File previewFile(Long fileId,HttpServletResponse response) throws UnsupportedEncodingException {
            String path1 = "https://1bd454f7.r3.cpolar.cn";
//        String path1 = " http://4u7ny6.natappfree.cc";
            File file = baseMapper.selectById(fileId);
            if (null == file){
                throw new MyException(500,"文件不存在");
            }

            String fileName = URLEncoder.encode(file.getName(), "UTF-8");
            response.setHeader("Content-Disposition", "attachment;" + "filename=" + fileName.replace("+", "%20"));
            response.setHeader("content-type", file.getContentType());
            response.setContentType("application/octet-stream");
            String type = file.getType();

            if (type.equals("doc" )|| type.equals("docx") || type.equals("xlsx")|| type.equals("pptx")|| type.equals("xls") || type.equals("ppt")){
                String path = path1 + "/" + minioConfig.getBucketName() + "/" + file.getPath();
                file.setPath(path);
            }else {
                String path = minioConfig.getEndpoint()+":"+ minioConfig.getPort() + "/" + minioConfig.getBucketName() + "/" + file.getPath();
                file.setPath(path);
            }
            return file;
    }

    @Override
    public void realDeleteByBatchNums(Collection<String> deletedBatchNums) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 获取所有需要删除的文件路径
        List<File> fileList = baseMapper.selectListByDeleteBatchNums(deletedBatchNums);
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(minioConfig.getBucketName())
                .objects(fileList.stream().filter(file -> file != null && !file.isFolder())
                        .map(file -> new DeleteObject(file.getPath())).collect(Collectors.toList()))
                .build());

        for (Result<DeleteError> result : results) {
            final DeleteError deleteError = result.get();
            log.error( "Error in deleting object " + deleteError.objectName() + "; " + deleteError.message());
        }
    }

    @Override
    public void recoveryByDeleteBatchNum(Collection<String> deletedBatchNums) {

        baseMapper.updateDeleteStatusByDeleteBatchNums(deletedBatchNums);

        baseMapper.update(null,Wrappers.<File>lambdaUpdate()
                .set(File::getIsShared,DeleteStatusEnum.NONE_DELETED.getValue())
                .set(File::getDeletedBatchNum,null)
                .in(File::getDeletedBatchNum,deletedBatchNums));
    }


    /**
     * 获取文件总数
     * @return
     */
    @Override
    public Map<String, Object> getFileTotal() {
        Map<String, Object> map = new HashMap<>();

        LambdaQueryWrapper<File> wrapperFolder = new LambdaQueryWrapper<>();
        wrapperFolder.eq(File::getType,ProjectConstant.FOLDER)
                .eq(File::getEmpId, JwtUtil.getCurrentUser().getId());

        LambdaQueryWrapper<File> wrapperPhoto = new LambdaQueryWrapper<>();
        wrapperPhoto.eq(File::getContentType,"image/jpeg")
                .eq(File::getEmpId, JwtUtil.getCurrentUser().getId());

        LambdaQueryWrapper<File> wrapperVideo = new LambdaQueryWrapper<>();
        wrapperVideo.eq(File::getType,"mp4")
                .eq(File::getEmpId, JwtUtil.getCurrentUser().getId());

        LambdaQueryWrapper<File> wrapperMp3 = new LambdaQueryWrapper<>();
        wrapperMp3.eq(File::getType,"mp3")
                .eq(File::getEmpId, JwtUtil.getCurrentUser().getId());

        LambdaQueryWrapper<File> wrapperWord = new LambdaQueryWrapper<>();
        wrapperWord.like(File::getContentType,"application/")
                .eq(File::getEmpId, JwtUtil.getCurrentUser().getId());




        map.put("folder",baseMapper.selectCount(wrapperFolder));
        map.put("photo",baseMapper.selectCount(wrapperPhoto));
        map.put("video",baseMapper.selectCount(wrapperVideo));
        map.put("mp3",baseMapper.selectCount(wrapperMp3));
        map.put("word",baseMapper.selectCount(wrapperWord));

        return map;
    }


    private String formatSize(long size) {
        if (size < 1024){
            return size + "B";
        } else if (size < 1048576) {
            return (size >> 10) + "KB";
        }else {
            return (size >> 20) + "MB";
        }
    }

    /**
     * 递归获取所有的子文件id，添加到childIds
     * @param fileList 个人文件列表
     * @param childIds 结果
     * @param parentId 指定的父文件id
     * @param recursiveCount 控制递归次数
     */
    public void recursive(List<File> fileList, Set<Long> childIds, Long parentId, int recursiveCount) {
        if (recursiveCount == 0) {
            return;
        }
        for (File file : fileList) {
            if (parentId.equals(file.getParentId())) {
                childIds.add(file.getId());
                recursive(fileList, childIds, file.getId(), recursiveCount - 1);
            }
        }
    }


}
